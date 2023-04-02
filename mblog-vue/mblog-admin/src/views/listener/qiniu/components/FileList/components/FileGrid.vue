<template>
  <!-- 文件平铺 -->
  <div class="file-grid-wrapper">
    <ul
      class="file-list"
      v-loading="loading"
      element-loading-text="文件加载中……"
      @click.self="rightMenu.isShow = false"
      @scroll="rightMenu.isShow = false"
    >
      <li
        class="file-item"
        v-for="(item, index) in fileListSorted"
        :key="index"
        :title="item | filenameComplete"
        @click="clickFileName(item, index, fileListSorted)"
        @contextmenu.prevent="rightClickFile(item, index, $event)"
      >
        <img v-if="item.dir" class="file-img" :src="$documents.FILE_IMG_MAP.dir" />
        <image-preview
          v-else
          class="file-img"
          :src="item.url"
          :srcList="previewSrcList"
          :initialIndex="imageUrlIndex(item.url)"
        />
        <div class="file-name">{{ item | filenameComplete }}</div>
        <div
          class="file-checked-wrapper"
          :class="{ checked: item.checked }"
          v-show="batchOperate"
          @click.stop.self="item.checked = !item.checked"
        >
          <el-checkbox class="file-checked" v-model="item.checked" @click.stop="item.checked = !item.checked"></el-checkbox>
        </div>
      </li>
    </ul>
    <transition name="el-fade-in-linear">
      <div class="right-menu" v-show="rightMenu.isShow" :style="`top: ${rightMenu.top}px; left: ${rightMenu.left}px;`">
        <el-button type="info" size="small" plain @click.native="deleteFileBtn(selectedFile)">删除</el-button>
        <el-button type="info" size="small" plain @click.native="showMoveFileDialog(selectedFile)">移动</el-button>
        <el-button type="info" size="small" plain @click.native="renameFile(selectedFile)">重命名</el-button>
        <el-button
          type="info"
          size="small"
          plain
          v-show="!selectedFile.dir"
          @click.native="downloadFile(selectedFile)"
        >下载</el-button>
      </div>
    </transition>
  </div>
</template>

<script>
// import {
//   deleteFile,
//   renameFile,
//   deleteRecoveryFile,
//   recoverRecycleFile,
// } from "@/request/file.js";
import { mapGetters } from "vuex";

export default {
  name: "FileGrid",
  props: {
    fileList: Array, //  文件列表
    loading: Boolean,
    batchOperate: Boolean,
  },
  data() {
    return {
      fileListSorted: this.fileList,
      previewSrcList: [],
      //  移动文件模态框数据
      dialogMoveFile: {
        isBatchMove: false,
        visible: false, //  是否可见
        defaultProps: {
          children: "children",
          label: "label",
        },
      },
      downloadFilePath: "",
      viewFilePath: "",
      // 右键菜单
      rightMenu: {
        isShow: false,
        top: 0,
        left: 0,
      },
      selectedFile: {},
    };
  },
  computed: {
    /**
     * selectedColumnList: 列显隐
     * operaColumnExpand: 判断当前用户设置的操作列是否展开 0不展开 1展开
     * fileModel: 文件查看模式 0列表模式 1网格模式
     *  */
    ...mapGetters([
      "selectedColumnList",
      "operaColumnExpand",
      "currentFileModel",
    ]),
    //  判断当前路径下是否有普通文件
    isIncludeNormalFile() {
      return this.fileList.map((data) => data.dir).includes(0);
    },
    //  判断当前路径下是否有压缩文件
    isIncludeZipRarFile() {
      return (
        this.fileList.map((data) => data.extension).includes("zip") ||
        this.fileList.map((data) => data.extension).includes("rar")
      );
    },
    // 批量操作模式 - 被选中的文件
    selectedFileList() {
      let res = this.fileListSorted.filter((item) => item.checked);
      return res;
    },
  },
  watch: {
    // 文件平铺模式 排序-文件夹在前
    fileList(newValue) {
      this.fileListSorted = [...newValue]
        .sort((pre, next) => {
          return next.dir - pre.dir;
        })
        .map((item) => {
          return {
            ...item,
            checked: false,
          };
        });
      // 图片则加入预览图片集合
      this.previewSrcList = this.fileListSorted
        .filter((item) => !item.dir)
        .map((item) => item.url);
    },
    // 批量操作模式 - 被选中的文件
    selectedFileList(newValue) {
      if (newValue.length) {
        this.$emit("setSelectionFile", newValue);
      }
    },
  },
  methods: {
    /** 文件右键事件 */
    rightClickFile(item, index, mouseEvent) {
      if (!this.batchOperate) {
        this.rightMenu.isShow = false;
        setTimeout(() => {
          this.selectedFile = item;
          this.rightMenu.top = mouseEvent.clientY;
          this.rightMenu.left = mouseEvent.clientX + 8;
          this.rightMenu.isShow = true;
        }, 50);
      }
    },
    /** 点击文件名*/
    clickFileName(row) {
      //  若是目录则进入目录
      if (row.dir) {
        this.$store.dispatch("setFilePath", row.id);
        this.$router.push({
          query: {
            pid: row.id,
          },
        });
      }
    },
    /** 操作列-移动按钮：打开移动文件模态框 */
    showMoveFileDialog(file) {
      this.rightMenu.isShow = false;
      //  第一个参数: 是否批量移动；第二个参数：打开/关闭移动文件模态框
      this.$emit("setOperationFile", file);
      this.$emit("setMoveFileDialogData", false, true);
    },
    /** 操作列-删除按钮 */
    deleteFileBtn(fileInfo) {
      this.rightMenu.isShow = false;
      this.$confirm(`是否确认删除 ${fileInfo.filename} ?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.confirmDeleteFile(fileInfo, false);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    /** 删除文件模态框-确定按钮 */
    confirmDeleteFile(fileInfo, type) {
      let data = { userFileId: fileInfo.id };
      if (type) {
        //  回收站中删除
        deleteRecoveryFile(data)
          .then(() => {
            this.$emit("getTableDataByType");
            this.$store.dispatch("showStorage");
            this.$message.success("删除成功");
            this.$store.dispatch("fetchPathTreeMap");
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("系统忙，删除失败");
          });
      } else {
        //  非回收站删除
        deleteFile(data)
          .then(() => {
            this.$emit("getTableDataByType");
            this.$store.dispatch("showStorage");
            this.$message.success("删除成功");
          })
          .catch((err) => {
            console.log(err);
            this.$message.error("系统忙，删除失败");
          });
      }
    },
    /** 文件重命名 */
    renameFile(fileInfo) {
      this.rightMenu.isShow = false;
      let filename = filenameComplete(fileInfo);
      this.$prompt("请输入文件名", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        inputValue: filename,
      })
        .then(({ value }) => {
          let name = filenameSplit(value);
          if (!name.filename) {
            throw new Error("错误文件名");
          }
          if (
            fileInfo.filename === name.filename &&
            fileInfo.extension === name.extension
          ) {
            throw new Error("文件名未发生改变");
          }
          fileInfo.oldFilename = fileInfo.filename;
          fileInfo.oldExtension = fileInfo.extension;
          fileInfo.filename = name.filename;
          fileInfo.extension = name.extension;
          this.confirmRenameFile(fileInfo);
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消输入",
          });
        });
    },
    /** 下载图片 */
    downloadFile(fileInfo) {
      this.rightMenu.isShow = false;
      this.download(fileInfo.url, {}, fileInfo.id + "." + fileInfo.extension);
    },
    /** 文件重命名模态框 确定按钮 */
    confirmRenameFile(fileInfo) {
      console.log(fileInfo);
      let data = {
        userFileId: fileInfo.id,
        filename: fileInfo.filename,
      };
      if (!fileInfo.dir) {
        data.extension = fileInfo.extension;
      }
      renameFile(data)
        .then(() => {
          this.$emit("getTableDataByType");
          this.$store.dispatch("showStorage");
          this.$message.success("重命名成功");
        })
        .catch((err) => {
          fileInfo.filename = fileInfo.oldFilename;
          fileInfo.extension = fileInfo.oldExtension;
          console.error(err);
          this.$message.error("系统忙，重命名失败");
        });
    },
    /** 图片预览的下标 */
    imageUrlIndex(url) {
      return this.previewSrcList.indexOf(url);
    },
  },
};
</script>
<style lang="scss" scoped>
.file-grid-wrapper {
  border-top: 1px solid #dcdfe6;

  .file-list {
    height: calc(100vh - 206px);
    overflow-y: auto;
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    align-content: flex-start;
    list-style: none;

    .file-item {
      margin: 0 16px 16px 0;
      position: relative;
      padding: 8px;
      width: 96px;
      height: 130px;
      text-align: center;
      cursor: pointer;

      &:hover {
        background: #f5f7fa;

        .file-name {
          font-weight: 550;
        }
      }

      .file-img {
        width: 80px;
        height: 80px;
      }

      .file-name {
        margin-top: 8px;
        height: 22px;
        line-height: 22px;
        font-size: 12px;
        word-break: break-all;
        text-overflow: ellipsis;
        overflow: hidden;
        -webkit-line-clamp: 1;
        display: -webkit-box;
        -webkit-box-orient: vertical;
      }

      .file-checked-wrapper {
        position: absolute;
        top: 0;
        left: 0;
        z-index: 2;
        background: rgba(245, 247, 250, 0.5);
        width: 100%;
        height: 100%;

        .file-checked {
          position: absolute;
          top: 16px;
          left: 24px;
        }
      }

      .file-checked-wrapper.checked {
        background: rgba(245, 247, 250, 0);
      }
    }
    .file-item.active {
      background: #e5f3ff;
    }
  }

  .right-menu {
    position: fixed;
    display: flex;
    flex-direction: column;
    z-index: 999;
    ::v-deep .el-button {
      margin: 0;
      border-radius: 0;
    }
  }
}
</style>