<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
      @submit.native.prevent
    >
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
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          @click="handlePass"
          v-hasPermi="['system:message:pass']"
        >审核通过</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['system:message:delete']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="messageList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40" align="center"></el-table-column>
      <el-table-column prop="avatar" align="center" width="80" label="头像">
        <template slot-scope="scope">
          <el-avatar shape="square" :size="50" :src="scope.row.avatar"></el-avatar>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" align="center" width="100" label="昵称" />
      <el-table-column prop="content" align="center" width="240" label="内容" />
      <el-table-column prop="ipAddress" align="center" width="150" label="ip地址" />
      <el-table-column prop="ipSource" align="center" width="180" label="ip来源" />
      <el-table-column prop="status" align="center" width="80" label="状态">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === 1">正常</el-tag>
          <el-tag v-else type="info">审核中</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" width="160" align="center" label="留言时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="!scope.row.status"
            size="mini"
            type="success"
            icon="el-icon-edit"
            @click="handlePass(scope.row)"
            v-hasPermi="['system:message:pass']"
          >通过</el-button>
          <el-button
            size="mini"
            type="danger"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:message:delete']"
          >删除</el-button>
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
        fuzzyField: "",
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
      listMessage(this.queryParams).then((response) => {
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
      this.ids = selection.map((item) => item.id);
    },
    /** 审核通过操作 */
    handlePass(row) {
      if (!row.id && this.ids != null) {
        // 全部审核通过时，只提交被选取并且是待审核的数据
        this.ids = this.messageList
          .filter((x) => x.status === 0 && this.ids.includes(x.id))
          .map((x) => x.id);
        if (this.ids.length === 0) {
          this.$modal.msgWarning("审核已通过，无需再审核");
          return;
        }
      }
      const id = row.id || this.ids;
      passMessage(id).then((res) => {
        this.$modal.msgSuccess("审核通过");
        this.getList();
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除留言板编号为"' + ids + '"的数据项？')
        .then(function () {
          return delMessage(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
  },
};
</script>
