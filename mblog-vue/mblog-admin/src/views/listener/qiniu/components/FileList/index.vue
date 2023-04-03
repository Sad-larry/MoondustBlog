<template>
  <div class="file-list-wrapper">
    <!-- 操作按钮 -->
    <el-header>
      <OperationMenu
        :operationFile="operationFile"
        :batchOperate.sync="batchOperate"
        :selectionFile.sync="selectionFile"
        :sortReverse.sync="sortReverse"
        :filePath="$documents.filePath().current()"
        @getSearchFileList="getSearchFileList"
        @getTableDataByType="getTableDataByType"
        @setMoveFileDialogData="setMoveFileDialogData"
      ></OperationMenu>
    </el-header>
    <div class="middle-wrapper">
      <!-- 面包屑导航栏 -->
      <BreadCrumb class="breadcrumb"></BreadCrumb>
    </div>
    <!-- 文件列表-表格模式 -->
    <FileTable
      :fileList="fileList"
      :loading="loading"
      v-if="$documents.fileModel().isTable()"
      @setMoveFileDialogData="setMoveFileDialogData"
      @setOperationFile="setOperationFile"
      @setSelectionFile="setSelectionFile"
      @getTableDataByType="getTableDataByType"
    ></FileTable>
    <!-- 文件列表-网格模式 -->
    <FileGrid
      :fileList="fileList"
      :loading="loading"
      v-if="$documents.fileModel().isGrid()"
      :batchOperate="batchOperate"
      @setMoveFileDialogData="setMoveFileDialogData"
      @setOperationFile="setOperationFile"
      @setSelectionFile="setSelectionFile"
      @getTableDataByType="getTableDataByType"
    ></FileGrid>
    <!-- 图片-时间线模式 -->
    <FileTimeLine
      class="image-model"
      :fileList="fileList"
      :sortReverse="sortReverse"
      v-if="$documents.fileModel().isTimeLine()"
      @setSortReverse="setSortReverse"
    ></FileTimeLine>
    <!-- 移动文件模态框 -->
    <!-- <MoveFileDialog
      :dialogMoveFile="dialogMoveFile"
      @setSelectFilePath="setSelectFilePath"
      @confirmMoveFile="confirmMoveFile"
      @setMoveFileDialogData="setMoveFileDialogData"
    ></MoveFileDialog>-->
  </div>
</template>

<script>
import OperationMenu from "./components/OperationMenu";
import BreadCrumb from "./components/BreadCrumb";
import FileTable from "./components/FileTable";
import FileGrid from "./components/FileGrid";
import FileTimeLine from "./components/FileTimeLine";
import MoveFileDialog from "./components/MoveFileDialog";

import {
  getFileList,
  //   getFileTree,
  //   moveFile,
  //   batchMoveFile,
  //   searchFile,
} from "@/api/system/qiniu.js";

export default {
  name: "FileList",
  components: {
    OperationMenu,
    BreadCrumb,
    FileTable,
    FileGrid,
    FileTimeLine,
    MoveFileDialog,
  },
  data() {
    return {
      fileNameSearch: "",
      // 表格数据-loading
      loading: true,
      // 表格数据-文件列表
      fileList: [],
      // 移动文件模态框数据
      dialogMoveFile: {
        isBatchMove: false,
        visible: false,
        // 目录树
        fileTree: [],
      },
      // 移动文件路径
      selectFilePath: Number,
      // 当前操作行
      operationFile: {},
      // 勾选的文件
      selectionFile: [],
      // 批量操作模式
      batchOperate: false,
      // 时间线模式反向排序
      sortReverse: true,
    };
  },
  computed: {
    // 当前查看的文件路径
    currentPath() {
      return this.$route.query.pid;
    },
    // 文件查看模式 0列表模式 1网格模式 2 时间线模式
    currentFileModel() {
      return this.$store.getters.currentFileModel;
    },
  },
  watch: {
    currentPath() {
      this.getTableDataByType();
    },
    // 监听文件查看模式
    currentFileModel() {
      this.getTableDataByType();
    },
  },
  created() {
    // 设置根目录
    this.$store.dispatch("initFileList");
    this.getTableDataByType();
  },
  methods: {
    /** 表格数据获取相关事件 */
    reflushFileList(res) {
      this.loading = false;
      this.fileList = res.data.records;
      // 将已浏览目录添加至map集合
      let dirs = res.data.records.filter((item) => item.dir);
      this.$store.dispatch("fetchPathTreeMap", dirs);
    },
    // 获取文件列表数据
    getTableDataByType() {
      this.batchOperate = false;
      this.loading = true;
      // 当前目录下全部文件
      this.showFileList();
    },
    /** 获取当前路径下的文件列表 */
    showFileList() {
      let queryParams = {
        limit: 100,
        marker: "",
      };
      // 查询当前目录
      if (this.currentPath !== undefined) {
        this.$store.dispatch("setFilePath", this.currentPath);
        // 空串是根目录
        queryParams.pid = this.currentPath;
      }
      getFileList(queryParams)
        .then((res) => {
          this.reflushFileList(res);
        })
        .catch((err) => {
          console.log(err);
          this.$message.error("服务器正忙");
        });
    },
    /** 表格勾选的行 */
    setSelectionFile(selection) {
      this.selectionFile = selection;
    },
    /** 当前操作行 */
    setOperationFile(operationFile) {
      this.operationFile = operationFile;
    },
    /** 改变时间线展示排序 */
    setSortReverse(sortReverse) {
      this.sortReverse = sortReverse;
    },
    /** 设置移动文件模态框相关数据，isBatchMove为null时是确认移动，值由之前的值而定 */
    setMoveFileDialogData(isBatchMove, visible) {
      this.initFileTree();
      this.dialogMoveFile.isBatchMove = isBatchMove
        ? isBatchMove
        : this.dialogMoveFile.isBatchMove;
      this.dialogMoveFile.visible = visible;
    },
    /** 移动文件模态框：初始化文件目录树 */
    initFileTree() {
      getFileTree()
        .then((res) => {
          this.dialogMoveFile.fileTree = [res];
        })
        .catch((err) => {
          console.log(err);
          this.$message.error("服务器正忙");
        });
    },
    /** 设置移动后的文件路径 */
    setSelectFilePath(selectFilePath) {
      this.selectFilePath = selectFilePath;
    },
    /** 移动文件模态框-确定按钮事件 */
    confirmMoveFile() {
      if (this.dialogMoveFile.isBatchMove) {
        //  批量移动
        let data = {
          from: this.selectionFile.flatMap((row) => row.id),
          to: this.selectFilePath,
        };
        if (!data.to) {
          delete data.to;
        }
        batchMoveFile(data)
          .then(() => {
            this.$message.success("文件移动成功");
            this.getTableDataByType();
            this.dialogMoveFile.visible = false;
            this.selectionFile = [];
            this.$store.dispatch("fetchPathTreeMap");
          })
          .catch((err) => {
            console.error(err);
            this.$message.error("系统忙，移动失败");
          });
      } else {
        //  单文件移动
        let data = {
          from: this.operationFile.id,
          to: this.selectFilePath,
        };
        if (!data.to) {
          delete data.to;
        }
        moveFile(data)
          .then(() => {
            this.$message.success("移动文件成功");
            this.getTableDataByType();
            this.dialogMoveFile.visible = false;
            this.$store.dispatch("fetchPathTreeMap");
          })
          .catch((err) => {
            console.error(err);
            this.$message.error("系统忙，移动失败");
          });
      }
    },
    /** 获取搜索文件结果列表 */
    getSearchFileList(filename) {
      if (1 == 1) {
        this.$modal.msg("搜索文件待开发..");
        return;
      }
      this.loading = true;
      searchFile({
        filename: filename,
      })
        .then((res) => {
          this.reflushFileList(res);
        })
        .catch((err) => {
          console.error(err);
          this.$message.error("服务器正忙");
        });
    },
  },
};
</script>
<style lang="scss" scoped>
.file-list-wrapper {
  ::v-deep .el-header {
    padding: 0;
  }

  .middle-wrapper {
    margin-bottom: 8px;
  }
}
</style>
