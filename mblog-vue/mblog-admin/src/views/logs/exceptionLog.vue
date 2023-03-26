<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['system:exceptionLog:delete']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5" :offset="21">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
        </el-tooltip>
      </el-col>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="exceptionLogList"
      @selection-change="handleSelectionChange"
      style="width: 100%;"
    >
      <el-table-column align="center" type="selection" width="55"></el-table-column>
      <el-table-column prop="exceptionMessage" :show-overflow-tooltip="true" align="center" width="270" label="异常内容" />
      <el-table-column prop="operation" align="center" width="200" label="接口名" />
      <el-table-column prop="ip" align="center" width="130" label="IP" />
      <el-table-column prop="ipSource" align="center" width="200" label="IP来源" />
      <el-table-column prop="username" align="center" width="130" label="操作人" />
      <el-table-column prop="createTime" align="center" width="200" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="80">
        <template slot-scope="scope">
          <el-button size="mini" type="success" icon="el-icon-more" @click="logDetail(scope.row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog title="异常详情" :visible.sync="dialogVisible" :fullscreen="true">
      <el-descriptions title="异常详情" column="1" border>
        <el-descriptions-item label="请求参数">{{ exceptionLog.params }}</el-descriptions-item>
        <el-descriptions-item label="异常详情">{{ exceptionLog.exceptionJson }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>
<script>
import { listExceptionLog, delExceptionLog } from "@/api/system/exceptionLog";

export default {
  name: "ExceptionLog",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 操作日志表格数据
      exceptionLogList: [],
      // 操作日志
      exceptionLog: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 异常信息详情框
      dialogVisible: false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询操作日志列表 */
    getList() {
      this.loading = true;
      listExceptionLog(this.queryParams)
        .then((response) => {
          this.exceptionLogList = response.data.records;
          this.total = response.data.total;
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    /** 刷新操作日志列表 */
    refresh() {
      this.getList();
    },
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除操作日志编号为"' + ids + '"的数据项？')
        .then(function () {
          return delExceptionLog(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 异常信息详细 */
    logDetail(row) {
      this.dialogVisible = true;
      this.exceptionLog = row;
    },
  },
};
</script>
  