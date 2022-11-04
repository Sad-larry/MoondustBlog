<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="88px"
      @submit.native.prevent>
      <!-- 分类名，标签名，发布状态等 -->
      <el-form-item label="搜索关键字" prop="fuzzyField">
        <el-input v-model="queryParams.fuzzyField" placeholder="请输入文章名称或简介" clearable @clear="resetQuery"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="single" @click="handleDelete">批量删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="文章封面地址" align="center" prop="avatar" width="120">
        <template slot-scope="scope">
          <el-image class="article-cover" :src="scope.row.avatar" />
          <i id="imgIcon" :class="scope.row.isSecret === 0 ? 'el-icon-view' : 'el-icon-lock'" />
        </template>
      </el-table-column>
      <el-table-column label="文章标题" align="center" width="120">
        <template slot-scope="scope">
          <el-link :underline="false" @click="onClick(scope.row)">{{ scope.row.title }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="类型" align="center" prop="type" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isOriginal === 0" :type="warning">{{ isOriginalList[0] }}
          </el-tag>
          <el-tag v-else-if="scope.row.isOriginal === 1">{{ isOriginalList[1] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="分类" align="center" prop="categoryName" width="120">
        <template slot-scope="scope">
          <el-tag style="margin-left: 3px" align="center" type="warning">{{ scope.row.categoryName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="标签" align="center" prop="tagNames" width="220">
        <template slot-scope="scope">
          <el-tag style="margin-left: 3px" align="center" type="primary"
            v-for="(item, index) in strSplit(scope.row.tagNames)" :key="index">
            {{ item }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="置顶" align="center" prop="isStick" width="120">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isStick" :active-value="1" :inactive-value="0" @change="handleTop(scope.row)"
            active-color="#13ce66">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="浏览量" align="center" prop="quantity" width="120" />
      <el-table-column label="添加时间" align="center" prop="createTime" sortable width="200">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300">
        <template slot-scope="scope">
          <el-button v-if="scope.row.isPublish === 1" type="text" icon="el-icon-box" size="mini"
            @click="offShelf(scope.row)">
            下架
          </el-button>
          <el-button v-if="scope.row.isPublish === 0" type="text" icon="el-icon-s-promotion" size="mini"
            @click="release(scope.row)">
            发布
          </el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleUpdate(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
  </div>
</template>

<script>
import { listArticle, delArticle, addArticle, updateArticle } from "@/api/system/article";

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
      secretList: ["公开", "私密"],
      isOriginalList: ["转载", "原创"],
      yesOrNoList: ["否", "是"],
      yesOrNoStyle: ['danger', 'success'],
      publishList: ['下架', '发布'],
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
        this.articleList = response.data.records;
        this.total = response.data.total;
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      // 跳转到发布文章页面
      this.$router.push({ path: '/publish' })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const id = row.id
      // 跳转到发布文章页面
      this.$router.push({ path: '/publish', query: { id } })
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
      this.$modal.confirm('是否确认删除博客文章编号为"' + ids + '"的数据项？').then(function () {
        return delArticle(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/article/export', {
        ...this.queryParams
      }, `article_${new Date().getTime()}.xlsx`)
    },
    /** 打开博客窗口 */
    onClick(row) {
      if (row.isPublish === 0) {
        this.$modal.msgError("文章暂未发布，无法进行浏览")
        return false;
      }
      this.$modal.msgError("博客前台未完成，无法进行浏览")
      // TODO window.open(this.BLOG_WEB_URL + "articles/" + row.id);
    },
    /** 切分字符串 */
    strSplit(item) {
      return item.split(",")
    },
  }
};
</script>
