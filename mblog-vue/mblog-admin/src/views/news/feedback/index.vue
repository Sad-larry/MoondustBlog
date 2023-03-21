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
      <el-form-item label="反馈类型">
        <el-select
          size="small"
          v-model="queryParams.type"
          filterable
          clearable
          reserve-keyword
          @change="handleQuery"
          placeholder="请选择反馈类型"
        >
          <el-option :key="1" label="需求" :value="1" />
          <el-option :key="2" label="反馈" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table
      border
      v-loading="loading"
      :data="feedBackList"
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="40" align="center" />
      <el-table-column prop="type" align="center" label="反馈类型">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.type === 1" type="success">需求</el-tag>
          <el-tag v-else type="danger">缺陷</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="email" align="center" width="160" label="联系邮箱" />
      <el-table-column prop="title" align="center" width="180" label="需求标题" />
      <el-table-column prop="content" align="center" width="180" label="详细内容" />
      <el-table-column prop="imgUrl" align="center" width="80" label="附加图片" />
      <el-table-column prop="status" align="center" width="80" label="状态">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.status === 0">待回复</el-tag>
          <el-tag v-else type="info">已回复</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="replyContent" align="center" width="180" show-overflow-tooltip label="回复内容" />
      <el-table-column prop="createTime" align="center" width="160" label="反馈时间">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" class-name="small-padding" width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="reply(scope.row)">回复</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 反馈回复对话框 -->
    <el-dialog title="反馈答复" :visible.sync="replyDialog" width="500px" append-to-body>
      <el-form ref="dataForm" :model="replyForm" :rules="rules" label-width="80px">
        <el-form-item label="反馈ID" prop="id">
          <el-input v-model="replyForm.id" :disabled="true"></el-input>
        </el-form-item>
        <el-form-item label="用户邮件" prop="email">
          <el-input v-model="replyForm.email" :disabled="true" />
        </el-form-item>
        <el-form-item label="反馈主题" prop="title">
          <el-input v-model="replyForm.title" :disabled="true" />
        </el-form-item>
        <el-form-item label="反馈类型" prop="type">
          <el-tag v-if="replyForm.type === 1" type="success">需求</el-tag>
          <el-tag v-else type="danger">缺陷</el-tag>
        </el-form-item>
        <el-form-item label="答复内容" prop="replyContent">
          <el-input
            v-model="replyForm.replyContent"
            type="textarea"
            :autosize="{ minRows: 2, maxRows: 4}"
            placeholder="请输入内容"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listBack, delBack, replyBack } from "@/api/system/feedback";

export default {
  name: "Back",
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
      // 用户反馈表格数据
      feedBackList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
      },
      replyDialog: false,
      // 回复对象表单
      replyForm: {
        id: null,
        email: null,
        title: "",
        type: null,
        replyContent: "",
      },
      // 表单校验
      rules: {
        replyContent: [
          { required: true, message: "回复内容不能为空", trigger: "blur" },
        ],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询用户反馈列表 */
    getList() {
      this.loading = true;
      listBack(this.queryParams).then((response) => {
        this.feedBackList = response.data.records;
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
      this.multiple = !selection.length;
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除用户反馈编号为"' + ids + '"的数据项？')
        .then(function () {
          return delBack(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 反馈回复 */
    reply(row) {
      this.reset();
      this.replyForm = {
        id: row.id,
        email: row.email,
        title: row.title,
        type: row.type,
      };
      this.replyDialog = true;
    },
    /** 提交回复，发送邮件 */
    submitForm() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          replyBack(this.replyForm).then((res) => {
            this.$modal.msgSuccess("已成功答复该反馈");
            this.replyDialog = false;
            this.getList();
          });
        }
      });
    },
    /** 表单重置 */
    reset() {
      this.replyForm = {
        id: null,
        email: null,
        title: "",
        type: null,
        replyContent: "",
      };
      this.resetForm("dataForm");
    },
    /** 取消 */
    cancel() {
      this.replyDialog = false;
      this.reset();
    },
  },
};
</script>
