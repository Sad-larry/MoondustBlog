<template>
    <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="作者" prop="userId">
                <el-input v-model="queryParams.userId" placeholder="请输入作者" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="文章标题" prop="title">
                <el-input v-model="queryParams.title" placeholder="请输入文章标题" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="所属分类" prop="categoryId">
                <el-input v-model="queryParams.categoryId" placeholder="请输入所属分类" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="缩略图" prop="thumbnail">
                <el-input v-model="queryParams.thumbnail" placeholder="请输入缩略图" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="是否置顶(1是,0否)" prop="isTop">
                <el-input v-model="queryParams.isTop" placeholder="请输入是否置顶(1是,0否)" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="浏览量" prop="viewCount">
                <el-input v-model="queryParams.viewCount" placeholder="请输入浏览量" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="是否允许评论(1是,0否)" prop="isComment">
                <el-input v-model="queryParams.isComment" placeholder="请输入是否允许评论(1是,0否)" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="评论数" prop="commentCount">
                <el-input v-model="queryParams.commentCount" placeholder="请输入评论数" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item label="点赞数" prop="starCount">
                <el-input v-model="queryParams.starCount" placeholder="请输入点赞数" clearable
                    @keyup.enter.native="handleQuery" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
            </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
            <el-col :span="1.5">
                <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
                    v-hasPermi="['system:article:add']">新增</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
                    v-hasPermi="['system:article:edit']">修改</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete" v-hasPermi="['system:article:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['system:article:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="articleList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center" />
            <el-table-column label="主键" align="center" prop="id" />
            <el-table-column label="作者" align="center" prop="userId" />
            <el-table-column label="文章标题" align="center" prop="title" />
            <el-table-column label="文章内容" align="center" prop="content" />
            <el-table-column label="文章摘要" align="center" prop="summary" />
            <el-table-column label="所属分类" align="center" prop="categoryId" />
            <el-table-column label="缩略图" align="center" prop="thumbnail" />
            <el-table-column label="是否置顶(1是,0否)" align="center" prop="isTop" />
            <el-table-column label="文章状态(1发布,0草稿,2待删除)" align="center" prop="status" />
            <el-table-column label="浏览量" align="center" prop="viewCount" />
            <el-table-column label="是否允许评论(1是,0否)" align="center" prop="isComment" />
            <el-table-column label="评论数" align="center" prop="commentCount" />
            <el-table-column label="点赞数" align="center" prop="starCount" />
            <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                        v-hasPermi="['system:article:edit']">修改</el-button>
                    <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                        v-hasPermi="['system:article:remove']">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
            @pagination="getList" />

        <!-- 添加或修改【请填写功能名称】对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="作者" prop="userId">
                    <el-input v-model="form.userId" placeholder="请输入作者" />
                </el-form-item>
                <el-form-item label="文章标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入文章标题" />
                </el-form-item>
                <el-form-item label="文章内容">
                    <editor v-model="form.content" :min-height="192" />
                </el-form-item>
                <el-form-item label="文章摘要" prop="summary">
                    <el-input v-model="form.summary" type="textarea" placeholder="请输入内容" />
                </el-form-item>
                <el-form-item label="所属分类" prop="categoryId">
                    <el-input v-model="form.categoryId" placeholder="请输入所属分类" />
                </el-form-item>
                <el-form-item label="缩略图" prop="thumbnail">
                    <el-input v-model="form.thumbnail" placeholder="请输入缩略图" />
                </el-form-item>
                <el-form-item label="是否置顶(1是,0否)" prop="isTop">
                    <el-input v-model="form.isTop" placeholder="请输入是否置顶(1是,0否)" />
                </el-form-item>
                <el-form-item label="浏览量" prop="viewCount">
                    <el-input v-model="form.viewCount" placeholder="请输入浏览量" />
                </el-form-item>
                <el-form-item label="是否允许评论(1是,0否)" prop="isComment">
                    <el-input v-model="form.isComment" placeholder="请输入是否允许评论(1是,0否)" />
                </el-form-item>
                <el-form-item label="评论数" prop="commentCount">
                    <el-input v-model="form.commentCount" placeholder="请输入评论数" />
                </el-form-item>
                <el-form-item label="点赞数" prop="starCount">
                    <el-input v-model="form.starCount" placeholder="请输入点赞数" />
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
            // 【请填写功能名称】表格数据
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
                title: null,
                content: null,
                summary: null,
                categoryId: null,
                thumbnail: null,
                isTop: null,
                status: null,
                viewCount: null,
                isComment: null,
                commentCount: null,
                starCount: null,
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
                userId: [
                    { required: true, message: "作者不能为空", trigger: "blur" }
                ],
                categoryId: [
                    { required: true, message: "所属分类不能为空", trigger: "blur" }
                ],
                createTime: [
                    { required: true, message: "创建时间不能为空", trigger: "blur" }
                ],
            }
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询【请填写功能名称】列表 */
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
                title: null,
                content: null,
                summary: null,
                categoryId: null,
                thumbnail: null,
                isTop: null,
                status: "0",
                viewCount: null,
                isComment: null,
                commentCount: null,
                starCount: null,
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
            this.reset();
            this.open = true;
            this.title = "添加【请填写功能名称】";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const id = row.id || this.ids
            getArticle(id).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改【请填写功能名称】";
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
            this.$modal.confirm('是否确认删除【请填写功能名称】编号为"' + ids + '"的数据项？').then(function () {
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
        }
    }
};
</script>
  