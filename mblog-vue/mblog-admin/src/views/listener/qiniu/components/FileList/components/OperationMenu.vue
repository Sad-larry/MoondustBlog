<template>
  <div class="operation-menu-wrapper">
    <el-button-group class="operate-group">
      <el-button size="mini" type="primary" icon="el-icon-upload2" id="uploadFileId" @click="upload()">上传</el-button>
      <el-button size="mini" type="primary" icon="el-icon-plus" @click="addFolder()">新建文件夹</el-button>
      <el-button
        size="mini"
        type="primary"
        :disabled="!selectionFile.length"
        icon="el-icon-delete"
        @click="deleteSelectedFile()"
      >批量删除</el-button>
      <el-button
        size="mini"
        type="primary"
        :disabled="!selectionFile.length"
        icon="el-icon-rank"
        @click="moveSelectedFile()"
      >批量移动</el-button>
      <el-button
        size="mini"
        type="primary"
        :disabled="!selectionFile.length"
        icon="el-icon-download"
        @click="downloadSelectedFile()"
      >批量下载</el-button>
    </el-button-group>

    <!-- 全局搜素文件 -->
    <el-input
      class="select-file-input"
      v-model="searchFile.filename"
      placeholder="搜索您的文件"
      size="mini"
      maxlength="255"
      :clearable="true"
      @change="handleSearchInputChange"
      @clear="$emit('getTableDataByType')"
      @keyup.enter.native="handleSearchInputChange(searchFile.filename)"
    >
      <i slot="prefix" class="el-input__icon el-icon-search" title="点击搜索" @click="handleSearchClick"></i>
    </el-input>

    <!-- 文件展示模式 -->
    <div class="change-file-model">
      <el-radio-group v-model="fileGroupLable" size="mini" @change="changeFileDisplayModel">
        <el-radio-button :label="0">
          <i class="el-icon-tickets"></i> 列表
        </el-radio-button>
        <el-radio-button :label="1">
          <i class="el-icon-s-grid"></i> 网格
        </el-radio-button>
        <el-radio-button :label="2">
          <i class="el-icon-date"></i> 时间线
        </el-radio-button>
      </el-radio-group>
    </div>
    <!-- 批量操作 -->
    <el-button
      class="opera-btn"
      :type="batchOperate ? 'primary' : ''"
      icon="el-icon-finished"
      size="mini"
      v-if="$documents.fileModel().isGrid()"
      @click="changeBatchOperate()"
    >{{ batchOperate ? '取消批量操作' : '批量操作' }}</el-button>

    <!-- 选择表格列 -->
    <SelectColumn class="select-column" v-if="$documents.fileModel().isTable()"></SelectColumn>

    <!-- 时间线模式占位 -->
    <el-button
      class="opera-btn"
      plain
      :type="sortReverse ? 'primary' : 'info'"
      :icon="sortReverse?'el-icon-sort-down':'el-icon-sort-up'"
      size="mini"
      v-if="$documents.fileModel().isTimeLine()"
      @click="changeReverse($event)"
    >{{ sortReverse ? '倒序排序' : '正序排序' }}</el-button>
  </div>
</template>

<script>
// import {
//   batchDeleteFile
// } from '@/request/file.js'
import SelectColumn from "./SelectColumn";

export default {
  name: "OperationMenu",
  props: {
    selectionFile: Array,
    operationFile: Object,
    batchOperate: Boolean, //  批量操作模式
    sortReverse: Boolean,
  },
  components: {
    SelectColumn,
  },
  data() {
    return {
      // 文件搜索数据
      searchFile: {
        filename: "",
      },
      fileTree: [],
      batchDeleteFileDialog: false,
      // 文件展示模式
      fileGroupLable: this.$documents.FILE_MODEL.TABLE,
    };
  },
  computed: {
    //  上传文件组件参数
    uploadFileData: {
      get() {
        let data = {
          folderId: this.$documents.filePath().current(),
          isDir: false,
        };
        if (!data.folderId) {
          delete data.folderId;
        }
        return data;
      },
      set() {
        return {
          folderId: this.ROOT_PATH,
          isDir: false,
        };
      },
    },
  },
  watch: {},
  mounted() {
    this.fileGroupLable = this.$documents.fileModel().current();
  },
  methods: {
    upload() {
      // 打开文件选择框
      this.$modal.msg("上传文件待开发..");
    },
    /**
     * 新建文件夹
     * 在七牛云中，没有创建文件夹这个概念，七牛存储模式是key-value模式，意思就是
     * 所有文件的文件名都是以key值存储，而value就是文件存储内容，所以可以通过域名+key访问文件
     * 如果要新建文件夹的话，文件夹也相当于文件，可以试试创建空内容的文件，文件名结尾要有 /
     * 七牛云创建目录要求：不支持仅由英文句号 . 组成的名称； 不支持以 / 开头，不能包含连续的 /。
     * */
    addFolder() {
      this.$prompt("请输入文件夹名称", "创建文件夹", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      })
        .then(({ value }) => {
          this.createFolder(value);
        })
        .catch((err) => {
          this.$message({
            type: "info",
            message: err.message || "取消输入",
          });
        });
    },
    //  新建文件夹模态框-确定按钮
    createFolder(filename) {
      if (1 == 1) {
        throw new Error("新建文件夹待开发..");
      }
      if (filename === null || filename === undefined || filename === "") {
        throw new Error("文件名不能为空");
      }
      let data = {
        name: filename,
      };
      if (this.filePath().current()) {
        data.pid = this.filePath().current();
      }
      makeDir(data)
        .then(() => {
          this.$message.success("添加成功");
          this.$emit("getTableDataByType");
        })
        .catch((err) => this.$message.warning(err.msg));
    },

    //  批量操作-删除按钮
    deleteSelectedFile() {
      if (1 == 1) {
        this.$modal.msg("批量操作待开发..");
        return;
      }
      let data = {
        userFileIds: this.selectionFile.flatMap((row) => row.id),
      };
      //  批量删除接口
      batchDeleteFile(data)
        .then(() => {
          this.$message({
            message: "文件删除成功",
            type: "success",
          });
          this.$emit("getTableDataByType");
        })
        .catch((err) => {
          console.error(err);
          this.$message.error("系统忙，删除失败");
        });
    },
    //  批量操作-移动按钮
    moveSelectedFile() {
      this.$emit("setMoveFileDialogData", true, true);
      if (1 == 1) {
        this.$modal.msg("移动操作待开发..");
        return;
      }
    },
    //  批量操作：下载按钮
    downloadSelectedFile() {
      this.$modal
        .confirm("是否下载所选中文件")
        .then(() => {
          // 暂时只能下载图片，文件夹等以后下压缩的时候下载
          this.selectionFile.forEach((item) => {
            if (!item.dir) {
              this.download(item.url, {}, item.id + "." + item.extension);
            }
          });
        })
        .catch((err) => {
          this.$message({
            type: "info",
            message: "取消下载",
          });
        });
    },
    handleSearchInputChange(value) {
      if (value === "") {
        this.$emit("getTableDataByType");
      } else {
        this.$emit("getSearchFileList", value);
      }
    },
    // 图片网格模式下 - 批量操作切换
    changeBatchOperate() {
      this.$emit("update:batchOperate", !this.batchOperate);
      this.$emit("update:selectionFile", []);
    },
    // 切换排序方式
    changeReverse(e) {
      // 按钮恢复样式，让他处于失焦状态就可
      e.target.blur();
      if (e.target.nodeName == "SPAN") {
        e.target.parentNode.blur();
      }
      this.$emit("update:sortReverse", !this.sortReverse);
    },
    // 切换文件查看模式
    changeFileDisplayModel(label) {
      this.$store.commit("CHANGE_FILE_MODEL", label);
    },
    handleSearchClick() {
      this.$emit("getSearchFileList", this.searchFile.filename);
    },
  },
};
</script>

<style lang="scss" scoped>
.operation-menu-wrapper {
  padding: 16px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .operate-group {
    flex: 1;
  }

  .select-file-input {
    margin-right: 8px;
    width: 180px;

    .el-icon-search {
      cursor: pointer;
      font-size: 16px;

      &:hover {
        color: #409eff;
      }
    }
  }

  .opera-btn {
    margin-right: 8px;
  }

  .change-image-model,
  .change-file-model {
    margin-right: 8px;
  }
}
</style>
