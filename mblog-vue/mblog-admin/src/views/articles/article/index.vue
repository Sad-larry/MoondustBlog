<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户ID" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类ID" prop="categoryId">
        <el-input
          v-model="queryParams.categoryId"
          placeholder="请输入分类ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文章标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入文章标题"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文章封面地址" prop="avatar">
        <el-input
          v-model="queryParams.avatar"
          placeholder="请输入文章封面地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文章简介" prop="summary">
        <el-input
          v-model="queryParams.summary"
          placeholder="请输入文章简介"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否是私密文章(0否,1是)" prop="isSecret">
        <el-input
          v-model="queryParams.isSecret"
          placeholder="请输入是否是私密文章(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否置顶(0否,1是)" prop="isStick">
        <el-input
          v-model="queryParams.isStick"
          placeholder="请输入是否置顶(0否,1是)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否发布(0草稿,1发布)" prop="isPublish">
        <el-input
          v-model="queryParams.isPublish"
          placeholder="请输入是否发布(0草稿,1发布)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否原创(0转载,1原创)" prop="isOriginal">
        <el-input
          v-model="queryParams.isOriginal"
          placeholder="请输入是否原创(0转载,1原创)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="转载地址" prop="originalUrl">
        <el-input
          v-model="queryParams.originalUrl"
          placeholder="请输入转载地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="文章阅读量" prop="quantity">
        <el-input
          v-model="queryParams.quantity"
          placeholder="请输入文章阅读量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="SEO关键词" prop="keywords">
        <el-input
          v-model="queryParams.keywords"
          placeholder="请输入SEO关键词"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:article:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:article:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:article:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:article:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="用户ID" align="center" prop="userId" />
      <el-table-column label="分类ID" align="center" prop="categoryId" />
      <el-table-column label="文章标题" align="center" prop="title" />
      <el-table-column label="文章封面地址" align="center" prop="avatar" />
      <el-table-column label="文章简介" align="center" prop="summary" />
      <el-table-column label="文章内容" align="center" prop="content" />
      <el-table-column label="文章内容md版" align="center" prop="contentMd" />
      <el-table-column label="是否是私密文章(0否,1是)" align="center" prop="isSecret" />
      <el-table-column label="是否置顶(0否,1是)" align="center" prop="isStick" />
      <el-table-column label="是否发布(0草稿,1发布)" align="center" prop="isPublish" />
      <el-table-column label="是否原创(0转载,1原创)" align="center" prop="isOriginal" />
      <el-table-column label="转载地址" align="center" prop="originalUrl" />
      <el-table-column label="文章阅读量" align="center" prop="quantity" />
      <el-table-column label="说明" align="center" prop="remark" />
      <el-table-column label="SEO关键词" align="center" prop="keywords" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:article:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:article:remove']"
          >删除</el-button>
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

    <!-- 添加或修改博客文章对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="用户ID" prop="userId">
          <el-input v-model="form.userId" placeholder="请输入用户ID" />
        </el-form-item>
        <el-form-item label="分类ID" prop="categoryId">
          <el-input v-model="form.categoryId" placeholder="请输入分类ID" />
        </el-form-item>
        <el-form-item label="文章标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入文章标题" />
        </el-form-item>
        <el-form-item label="文章封面地址" prop="avatar">
          <el-input v-model="form.avatar" placeholder="请输入文章封面地址" />
        </el-form-item>
        <el-form-item label="文章简介" prop="summary">
          <el-input v-model="form.summary" placeholder="请输入文章简介" />
        </el-form-item>
        <el-form-item label="文章内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="文章内容md版" prop="contentMd">
          <el-input v-model="form.contentMd" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="是否是私密文章(0否,1是)" prop="isSecret">
          <el-input v-model="form.isSecret" placeholder="请输入是否是私密文章(0否,1是)" />
        </el-form-item>
        <el-form-item label="是否置顶(0否,1是)" prop="isStick">
          <el-input v-model="form.isStick" placeholder="请输入是否置顶(0否,1是)" />
        </el-form-item>
        <el-form-item label="是否发布(0草稿,1发布)" prop="isPublish">
          <el-input v-model="form.isPublish" placeholder="请输入是否发布(0草稿,1发布)" />
        </el-form-item>
        <el-form-item label="是否原创(0转载,1原创)" prop="isOriginal">
          <el-input v-model="form.isOriginal" placeholder="请输入是否原创(0转载,1原创)" />
        </el-form-item>
        <el-form-item label="转载地址" prop="originalUrl">
          <el-input v-model="form.originalUrl" placeholder="请输入转载地址" />
        </el-form-item>
        <el-form-item label="文章阅读量" prop="quantity">
          <el-input v-model="form.quantity" placeholder="请输入文章阅读量" />
        </el-form-item>
        <el-form-item label="说明" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入说明" />
        </el-form-item>
        <el-form-item label="SEO关键词" prop="keywords">
          <el-input v-model="form.keywords" placeholder="请输入SEO关键词" />
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
import { listArticle, getArticle, delArticle, addArticle, updateArticle } from "@/api/system/article";

export default {
  name: "Article",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 博客文章表格数据
      articleList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        categoryId: null,
        title: null,
        avatar: null,
        summary: null,
        content: null,
        contentMd: null,
        isSecret: null,
        isStick: null,
        isPublish: null,
        isOriginal: null,
        originalUrl: null,
        quantity: null,
        keywords: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "文章标题不能为空", trigger: "blur" }
        ],
        summary: [
          { required: true, message: "文章简介不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询博客文章列表 */
    getList() {
      this.loading = true;
      listArticle(this.queryParams).then(response => {
        this.articleList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        userId: null,
        categoryId: null,
        title: null,
        avatar: null,
        summary: null,
        content: null,
        contentMd: null,
        isSecret: null,
        isStick: null,
        isPublish: null,
        isOriginal: null,
        originalUrl: null,
        quantity: null,
        remark: null,
        keywords: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
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
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加博客文章";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getArticle(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改博客文章";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateArticle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addArticle(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除博客文章编号为"' + ids + '"的数据项？').then(function() {
        return delArticle(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/article/export', {
        ...this.queryParams
      }, `article_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
