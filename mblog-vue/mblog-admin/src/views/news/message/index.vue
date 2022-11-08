<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px"
      @submit.native.prevent>
      <el-form-item label="用户昵称" prop="fuzzyField">
        <el-input v-model="queryParams.fuzzyField" placeholder="请输入用户昵称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="multiple" @click="handlePass">审核通过
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table border v-loading="loading" :data="messageList" style="width: 100%" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="40" align="center">
      </el-table-column>
      <el-table-column prop="avatar" align="center" width="80" label="头像">
        <template slot-scope="scope">
          <el-avatar shape="square" :size="50" :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" align="center" width="100" label="昵称" />
      <el-table-column prop="content" align="center" width="240" label="内容" />
      <el-table-column prop="ipAddress" align="center" width="100" label="ip地址" />
      <el-table-column prop="ipSource" align="center" width="120" label="ip来源" />
      <el-table-column prop="status" align="center" width="80" label="状态">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 1">正常</el-tag>
          <el-tag v-else type="info">审核中</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" width="150" align="center" label="留言时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center"  class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button v-if="!scope.row.status" size="mini" type="text"  icon="el-icon-edit" @click="handlePass(scope.row)">
            通过</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { listMessage, delMessage, passMessage } from "@/api/system/message";

export default {
  name: "Message",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 留言板表格数据
      messageList: [],
      // 弹出层标题
      title: "",
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fuzzyField: ''
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询留言板列表 */
    getList() {
      this.loading = true;
      listMessage(this.queryParams).then(response => {
        this.messageList = response.data.records;
        this.total = response.data.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.multiple = !selection.length
    },
    /** 审核通过操作 */
    handlePass(row) {
      const id = row.id || this.ids
      passMessage(id).then(res => {
        this.$modal.msgSuccess("审核通过");
        this.getList();
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除留言板编号为"' + ids + '"的数据项？').then(function () {
        return delMessage(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
  }
};
</script>
