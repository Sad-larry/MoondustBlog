<template>
  <div class="file-table-wrapper">
    <!-- 文件表格 -->
    <el-table
      class="file-table"
      ref="multipleTable"
      fit
      v-loading="loading"
      element-loading-text="文件加载中……"
      tooltip-effect="dark"
      :data="fileList"
      :default-sort="{ prop: 'dir', order: 'descending' }"
      @select-all="selectAllFileRow"
      @select="selectFileRow"
    >
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="dir" width="60" align="center">
        <template slot-scope="scope">
          <img :src="setFileImg(scope.row)" style="height: 30px" />
        </template>
      </el-table-column>
      <el-table-column prop="filename" :sort-by="['dir', 'filename']" sortable show-overflow-tooltip>
        <template slot="header">
          <span>文件名</span>
        </template>
        <template slot-scope="scope">
          <div v-if="scope.row.dir">
            <div style="cursor:pointer;" @click="clickFileName(scope.row)">{{ scope.row | filenameComplete }}</div>
          </div>
          <div v-else>
            <el-popover :ref="'popover-'+scope.row.id" placement="left-start" trigger="hover" :close-delay="0">
              <el-image :src="scope.row.url" fit="contain" style="width: 150px;" />
              <div slot="reference" style="cursor:pointer;">{{ scope.row | filenameComplete }}</div>
            </el-popover>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="路径" width="150" prop="pid">
        <template slot-scope="scope">
          <span v-text="scope.row.pid"></span>
        </template>
      </el-table-column>
      <el-table-column
        label="类型"
        width="80"
        prop="extension"
        :sort-by="['dir', 'extension']"
        sortable
        show-overflow-tooltip
        v-if="selectedColumnList.includes('extension')"
      >
        <template slot-scope="scope">
          <span v-if="scope.row.extension">{{ scope.row.extension }}</span>
          <span v-else>文件夹</span>
        </template>
      </el-table-column>
      <el-table-column
        label="大小"
        width="80"
        prop="fileSize"
        :sort-by="['dir', 'fileSize']"
        sortable
        show-overflow-tooltip
        align="right"
        v-if="selectedColumnList.includes('fileSize')"
      >
        <template slot-scope="scope">
          <div style="padding: 0 10px;">{{ calculateFileSize(scope.row.fileSize) }}</div>
        </template>
      </el-table-column>
      <el-table-column
        label="创建日期"
        prop="createTime"
        width="180"
        align="center"
        :sort-by="['dir', 'createTime']"
        show-overflow-tooltip
        sortable
        v-if="selectedColumnList.includes('createTime')"
      ></el-table-column>
      <el-table-column :width="operaColumnWidth">
        <template slot="header">
          <span>操作</span>
          <i
            class="el-icon-circle-plus"
            title="展开操作列按钮"
            @click="$store.commit('changeOperaColumnExpand', this.$documents.FOLD_TYPE.UNFOLD)"
          ></i>
          <i class="el-icon-remove" title="收起操作列按钮" @click="$store.commit('changeOperaColumnExpand', FOLD_TYPE.FOLD)"></i>
        </template>
        <template slot-scope="scope">
          <div v-if="operaColumnExpand">
            <el-button type="danger" size="mini" @click.native="deleteFileBtn(scope.row)">删除</el-button>
            <el-button type="primary" size="mini" @click.native="showMoveFileDialog(scope.row)">移动</el-button>
            <el-button type="primary" size="mini" @click.native="renameFile(scope.row)">重命名</el-button>
            <el-button type="success" size="mini" v-show="!scope.row.dir" @click.native="downloadFile(scope.row)">下载</el-button>
          </div>
          <el-dropdown trigger="click" v-else>
            <el-button size="mini">
              操作
              <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="deleteFileBtn(scope.row)">删除</el-dropdown-item>
              <el-dropdown-item @click.native="showMoveFileDialog(scope.row)">移动</el-dropdown-item>
              <el-dropdown-item @click.native="renameFile(scope.row)">重命名</el-dropdown-item>
              <el-dropdown-item v-show="!scope.row.dir" @click.native="downloadFile(scope.row)">下载</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
// import {unzipfile, deleteFile, renameFile, deleteRecoveryFile, recoverRecycleFile} from '@/request/file.js'
import { mapGetters } from "vuex";

export default {
  name: "FileTable",
  props: {
    fileList: Array, //  文件列表
    loading: Boolean,
  },
  data() {
    return {
      //  移动文件模态框数据
      dialogMoveFile: {
        isBatchMove: false,
        visible: false, //  是否可见
        fileTree: [], //  目录树
        defaultProps: {
          children: "children",
          label: "label",
        },
      },
      viewFilePath: "",
    };
  },
  computed: {
    //  selectedColumnList:列显隐, operaColumnExpand:判断当前用户设置的操作列是否展开
    ...mapGetters(["selectedColumnList", "operaColumnExpand"]),
    //  判断当前路径下是否有普通文件
    isIncludeNormalFile() {
      return this.fileList.map((data) => data.dir).includes(false);
    },
    //  判断当前路径下是否有压缩文件
    isIncludeZipRarFile() {
      return (
        this.fileList.map((data) => data.extension).includes("zip") ||
        this.fileList.map((data) => data.extension).includes("rar")
      );
    },
    operaColumnWidth() {
      return this.operaColumnExpand
        ? this.isIncludeNormalFile
          ? 290
          : 220
        : 100;
    },
  },
  methods: {
    /**
     * 表格数据获取相关事件
     */
    //  根据文件扩展名设置文件图片
    setFileImg(row) {
      if (!row.extension) {
        //  文件夹
        return this.$documents.FILE_IMG_MAP.dir;
      } else if (["jpg", "png", "jpeg", "gif"].includes(row.extension)) {
        // 图片类型
        return this.$documents.FILE_IMG_MAP[row.extension];
      } else {
        //  无法识别文件类型的文件
        return this.$documents.FILE_IMG_MAP.unknown;
      }
    },
    //  计算文件大小
    calculateFileSize(size) {
      const B = 1024;
      const KB = Math.pow(1024, 2);
      const MB = Math.pow(1024, 3);
      const GB = Math.pow(1024, 4);
      if (!size) {
        return "_";
      } else if (size < KB) {
        return (size / B).toFixed(0) + "KB";
      } else if (size < MB) {
        return (size / KB).toFixed(1) + "MB";
      } else if (size < GB) {
        return (size / MB).toFixed(2) + "GB";
      } else {
        return (size / GB).toFixed(3) + "TB";
      }
    },

    //  点击文件名
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
    /**
     * 表格勾选框事件
     */
    //  表格-全选事件, selectoin 勾选的行数据
    selectAllFileRow(selection) {
      this.$emit("setSelectionFile", selection);
    },
    //  表格-选中一行事件, selectoin 勾选的行数据
    selectFileRow(selection) {
      this.$emit("setSelectionFile", selection);
    },

    /**
     * 移动按钮相关事件
     */
    //  操作列-移动按钮：打开移动文件模态框
    showMoveFileDialog(file) {
      //  第一个参数: 是否批量移动；第二个参数：打开/关闭移动文件模态框
      this.$emit("setOperationFile", file);
      this.$emit("setMoveFileDialogData", false, true);
    },

    //  操作列-解压缩按钮
    unzipFile(fileInfo) {
      const loading = this.$loading({
        lock: true,
        text: "正在解压缩，请稍等片刻...",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      unzipfile(fileInfo)
        .then(() => {
          this.$emit("getTableDataByType");
          this.$store.dispatch("showStorage");
          this.$message.success("解压成功");
          loading.close();
        })
        .catch((err) => {
          console.log(err);
          this.$message.error("系统忙，解压失败");
        });
    },

    /**
     * 恢复按钮事件
     */
    recoverFileBtn(fileInfo) {
      let data = {
        userFileId: fileInfo.id,
      };
      if (this.$documents.fileType().isRecycle()) {
        //  回收站里 - 彻底删除
        this.$confirm("是否恢复该文件?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "success",
        })
          .then(() => {
            recoverRecycleFile(data).then(() => {
              this.$message({
                type: "success",
                message: filenameComplete(fileInfo) + "已恢复",
              });
              this.$emit("getTableDataByType");
              this.$store.dispatch("showStorage");
              this.$store.dispatch("fetchPathTreeMap");
            });
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消恢复",
            });
          });
      }
    },

    //  操作列-删除按钮
    deleteFileBtn(fileInfo) {
      if (this.$documents.fileType().isRecycle()) {
        //  回收站里 - 彻底删除
        this.$confirm("此操作将永久删除该文件, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        })
          .then(() => {
            this.confirmDeleteFile(fileInfo, true);
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消删除",
            });
          });
      } else {
        //  非回收站
        this.$confirm("删除后可在回收站查看, 是否继续删除?", "提示", {
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
      }
    },
    //  删除文件模态框-确定按钮
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
            console.error(err);
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
            console.error(err);
            this.$message.error("系统忙，删除失败");
          });
      }
    },
    /** 下载图片 */
    downloadFile(fileInfo) {
      this.download(fileInfo.url, {}, fileInfo.id + "." + fileInfo.extension);
    },
    // 文件重命名
    renameFile(fileInfo) {
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
    // 文件重命名模态框 确定按钮
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
  },
};
</script>

<style lang="scss" scoped>
.file-table-wrapper {
  margin-top: 2px;

  .file-table.share {
    height: calc(100vh - 109px) !important;

    ::v-deep .el-table__body-wrapper {
      height: calc(100vh - 161px) !important;
    }
  }

  .file-table {
    width: 100% !important;
    height: calc(100vh - 203px);

    ::v-deep .el-table__header-wrapper {
      th {
        // background: $tabBackColor;
        padding: 4px 0;
        color: #606266;
      }

      .el-icon-circle-plus,
      .el-icon-remove {
        margin-left: 6px;
        cursor: pointer;
        font-size: 16px;

        &:hover {
          color: #409eff;
        }
      }
    }

    ::v-deep .el-table__body-wrapper {
      height: calc(100vh - 255px);
      overflow-y: auto;
      &::-webkit-scrollbar {
        width: 6px;
      }
      //  修改滚动条的下面的样式
      &::-webkit-scrollbar-track {
        background-color: transparent;
        -webkit-border-radius: 2em;
        -moz-border-radius: 2em;
        border-radius: 2em;
      }
      //  修改滑块
      &::-webkit-scrollbar-thumb {
        background-color: #c0c4cc;
        -webkit-border-radius: 2em;
        -moz-border-radius: 2em;
        border-radius: 2em;
      }

      td {
        padding: 8px 0;
        .file-name {
          .keyword {
            color: #f56c6c;
          }
        }
      }

      .el-icon-warning {
        font-size: 16px;
        color: #e6a23c;
      }

      .el-icon-time {
        font-size: 16px;
        color: #67c23a;
      }
    }
  }
}
</style>
