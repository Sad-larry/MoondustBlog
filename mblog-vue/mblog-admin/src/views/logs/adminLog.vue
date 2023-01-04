<template>
  <div class="app-container">
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">批量删除
        </el-button>
      </el-col>
      <el-col :span="1.5" :offset="20">
        <el-tooltip class="item" effect="dark" content="刷新" placement="top">
          <el-button size="mini" circle icon="el-icon-refresh" @click="refresh()" />
        </el-tooltip>
      </el-col>
    </el-row>

    <el-table border v-loading="loading" :data="adminLogList" @selection-change="handleSelectionChange"
      style="width: 100%;">
      <el-table-column type="expand">
        <template slot-scope="scope">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-row>
              <el-form-item label="请求接口">
                <span>{{ scope.row.classPath + scope.row.requestUrl }}</span>
              </el-form-item>
            </el-row>
            <el-row>
              <el-form-item label="请求参数">
                <span>{{ scope.row.paramsJson }}</span>
              </el-form-item>
            </el-row>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column align="center" type="selection" width="55" />
      <el-table-column prop="username" align="center" width="100" label="操作人" />
      <el-table-column prop="requestUrl" align="center" width="200" label="请求接口" />
      <el-table-column prop="type" align="center" label="请求方式">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 'POST'" type="success">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type === 'DELETE'" type="danger">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type === 'GET'">{{ scope.row.type }}</el-tag>
          <el-tag v-if="scope.row.type === 'PUT'" type="warning">{{ scope.row.type }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="operationName" align="center" width="130" label="接口名" />
      <el-table-column prop="ip" width="130" align="center" label="IP" />
      <el-table-column prop="source" align="center" width="200" label="IP来源" />
      <el-table-column prop="spendTime" align="center" label="请求耗时">
        <template slot-scope="scope">
          <span><el-tag type="info">{{ scope.row.spendTime }} ms</el-tag></span>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" align="center" width="200" label="创建时间">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime }}</span>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>
<script>
import { listAdminLog, delAdminLog } from "@/api/system/adminLog";

export default {
  name: "AdminLog",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 总条数
      total: 0,
      // 操作日志表格数据
      adminLogList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
    }
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询操作日志列表 */
    getList() {
      this.loading = true;
      listAdminLog(this.queryParams).then(response => {
        this.adminLogList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      }).catch(err => {
        this.loading = false;
      });
    },
    /** 刷新操作日志列表 */
    refresh() {
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除操作日志编号为"' + ids + '"的数据项？').then(function () {
        return delAdminLog(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
  }
}
</script>
  