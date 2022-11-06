<template>
    <div class="app-container">
        <el-card class="main-card">
            <!-- 文章标题 -->
            <div class="article-title-container">
                <el-input v-model="article.title" size="medium" placeholder="输入文章标题" />
                <el-button type="danger" size="medium" @click="openModel" style="margin-left: 10px"> 发布文章 </el-button>
            </div>
            <!-- 文章内容 -->
            <mavon-editor ref="md" v-model="article.contentMd" @imgAdd="uploadImg"
                style="height: calc(100vh - 260px)" />
            <!-- 添加文章对话框 -->
            <el-dialog :visible.sync="addOrEdit" width="40%" top="3vh">
                <div class="dialog-title-container" slot="title">发布文章</div>
                <!-- 文章数据 -->
                <el-form label-width="80px" size="medium" :model="article">
                    <!-- 文章分类 -->
                    <el-form-item label="文章分类">
                        <el-tag type="success" v-show="article.categoryName" style="margin: 0 1rem 0 0" :closable="true"
                            @close="removeCategory">
                            {{ article.categoryName }}
                        </el-tag>
                        <!-- 分类选项 -->
                        <el-popover placement="bottom-start" width="460" trigger="click" v-if="!article.categoryName">
                            <div class="popover-title">分类</div>
                            <!-- 搜索框 -->
                            <el-autocomplete style="width: 100%" v-model="categoryName"
                                :fetch-suggestions="searchCategories" placeholder="请输入分类名搜索，enter可添加自定义分类"
                                :trigger-on-focus="false" @keyup.enter.native="saveCategory"
                                @select="handleSelectCategories">
                                <!-- cb()返回的数据 -->
                                <template slot-scope="{ item }">
                                    <div>{{ item.name }}</div>
                                </template>
                            </el-autocomplete>
                            <!-- 分类 -->
                            <div class="popover-container">
                                <div v-for="item of categorys" :key="item.id" class="category-item"
                                    @click="addCategory(item)">
                                    {{ item.name }}
                                </div>
                            </div>
                            <el-button type="success" plain slot="reference" size="small"> 添加分类 </el-button>
                        </el-popover>
                    </el-form-item>
                    <!-- 文章标签 -->
                    <el-form-item label="文章标签">
                        <el-tag v-for="(item, index) of article.tags" :key="index" style="margin: 0 1rem 0 0"
                            :closable="true" @close="removeTag(item)">
                            {{ item }}
                        </el-tag>
                        <!-- 标签选项 -->
                        <el-popover placement="bottom-start" width="460" trigger="click" v-if="article.tags.length < 3">
                            <div class="popover-title">标签</div>
                            <!-- 搜索框 -->
                            <el-autocomplete style="width: 100%" v-model="tagName" :fetch-suggestions="searchTags"
                                placeholder="请输入标签名搜索，enter可添加自定义标签" :trigger-on-focus="false"
                                @keyup.enter.native="saveTag" @select="handleSelectTag">
                                <template slot-scope="{ item }">
                                    <div>{{ item.name }}</div>
                                </template>
                            </el-autocomplete>
                            <!-- 标签 -->
                            <div class="popover-container">
                                <div style="margin-bottom: 1rem">添加标签</div>
                                <el-tag v-for="(item, index) of tagList" :key="index" :class="tagClass(item)"
                                    @click="addTag(item)">
                                    {{ item.name }}
                                </el-tag>
                            </div>
                            <el-button type="primary" plain slot="reference" size="small"> 添加标签 </el-button>
                        </el-popover>
                    </el-form-item>
                    <el-form-item label="文章类型">
                        <el-select v-model="article.isOriginal" placeholder="请选择类型">
                            <el-option v-for="item in typeList" :key="item.type" :label="item.desc"
                                :value="item.type" />
                        </el-select>
                    </el-form-item>
                    <!-- 文章类型 -->
                    <el-form-item label="原文地址" v-if="article.isOriginal != 1">
                        <el-input v-model="article.originalUrl" placeholder="请填写原文链接" />
                    </el-form-item>
                    <el-form-item label="上传封面">
                        <el-upload class="upload-cover" drag action="uploadUrl" multiple :headers="headers"
                            :before-upload="beforeUpload" :on-success="uploadCover">
                            <i class="el-icon-upload" v-if="article.avatar == ''" />
                            <div class="el-upload__text" v-if="article.avatar == ''">将文件拖到此处，或<em>点击上传</em></div>
                            <img v-else :src="article.avatar" width="360px" height="180px" />
                        </el-upload>
                    </el-form-item>
                    <el-form-item label="置顶">
                        <el-switch v-model="article.isStick" active-color="#13ce66" inactive-color="#F4F4F5"
                            :active-value="1" :inactive-value="0" />
                    </el-form-item>
                    <el-form-item label="发布">
                        <el-switch v-model="article.isPublish" active-color="#13ce66" inactive-color="#F4F4F5"
                            :active-value="1" :inactive-value="0" />
                    </el-form-item>
                    <el-form-item label="发布形式">
                        <el-radio-group v-model="article.isSecret">
                            <el-radio :label="0">公开</el-radio>
                            <el-radio :label="1">私密</el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item label="文章简介">
                        <el-input v-model="article.summary" placeholder="为你的文章写个文章简介再发布吧" />
                    </el-form-item>
                </el-form>
                <div slot="footer">
                    <el-button @click="addOrEdit = false">取 消</el-button>
                    <el-button type="danger" @click="saveOrUpdateArticle"> 发 表 </el-button>
                </div>
            </el-dialog>
        </el-card>
    </div>
</template>
  
<script>
import { parseTime } from "@/utils/ruoyi";
import { getArticle, addArticle, updateArticle } from "@/api/system/article";
import { listCategory } from "@/api/system/category";
import { listTag } from "@/api/system/tag";
import * as imageConversion from 'image-conversion'
export default {
    name: "PublishArticle",
    data() {
        return {
            uploadUrl: process.env.VUE_APP_BASE_API + "/system/uploadImg",
            addOrEdit: false,
            autoSave: true,
            categoryName: '',
            tagName: '',
            categorys: [],
            tagList: [],
            typeList: [
                { type: 1, desc: '原创' },
                { type: 0, desc: '转载' }
            ],
            article: {
                id: null,
                title: parseTime(new Date()),
                avatar: '',
                summary: '',
                contentMd: '',
                isSecret: 0,
                isStick: 0,
                isPublish: 1,
                isOriginal: 1,
                originalUrl: null,
                remark: null,
                keywords: null,
                categoryName: null,
                tags: []
            },
            headers: { token: sessionStorage.getItem('token') }
        }
    },
    created() {
        // 若是从文章列表新增，则应该将文章id传递过来
        const id = this.$route.query.id;
        if (id) {
            getArticle(id).then(response => {
                this.article = response.data
                this.article.tags = this.article.tagNames.split(",")
            })
        } else {
            const article = sessionStorage.getItem('article')
            if (article) {
                this.article = JSON.parse(article)
            }
        }
        this.$modal.msg('已开启自动保存文章草稿')
    },
    destroyed() {
        //文章自动保存功能
        this.autoSaveArticle()
    },
    methods: {
        listCategories() {
            listCategory().then(response => {
                this.categorys = response.data.records
            })
        },
        listTags() {
            listTag().then(response => {
                this.tagList = response.data.records
            })
        },
        openModel() {
            if (this.article.title.trim() == '') {
                this.$modal.msgError('文章标题不能为空')
                return false
            }
            if (this.article.contentMd.trim() == '') {
                this.$modal.msgError('文章内容不能为空')
                return false
            }
            this.listCategories()
            this.listTags()
            this.addOrEdit = true
        },
        uploadCover(response) {
            this.article.avatar = response.data
        },
        beforeUpload(file) {
            return new Promise((resolve) => {
                if (file.size / 1024 < process.env.UPLOAD_SIZE) {
                    resolve(file)
                }
                // 压缩到200KB,这里的200就是要压缩的大小,可自定义
                imageConversion.compressAccurately(file, process.env.UPLOAD_SIZE).then((res) => {
                    resolve(res)
                })
            })
        },
        uploadImg(pos, file) {
            var formdata = new FormData()
            if (file.size / 1024 < process.env.UPLOAD_SIZE) {
                formdata.append('file', file)
                // this.axios.post('/api/admin/articles/images', formdata).then(({ data }) => {
                //     this.$refs.md.$img2Url(pos, data.data)
                // })
            } else {
                // 压缩到200KB,这里的200就是要压缩的大小,可自定义
                imageConversion.compressAccurately(file, process.env.UPLOAD_SIZE).then((res) => {
                    formdata.append('file', new window.File([res], file.name, { type: file.type }))
                    // this.axios.post('/api/admin/articles/images', formdata).then(({ data }) => {
                    //     this.$refs.md.$img2Url(pos, data.data)
                    // })
                })
            }
        },
        saveOrUpdateArticle() {
            if (this.article.title.trim() == '') {
                this.$modal.msgError('文章标题不能为空')
                return false
            }
            if (this.article.contentMd.trim() == '') {
                this.$modal.msgError('文章内容不能为空')
                return false
            }
            if (this.article.categoryName == null) {
                this.$modal.msgError('文章分类不能为空')
                return false
            }
            if (this.article.tags.length == 0) {
                this.$modal.msgError('文章标签不能为空')
                return false
            }
            if (this.article.summary.trim() == '') {
                this.$modal.msgError('文章封面不能为空')
                return false
            }
            if (this.article.id === null) {
                addArticle(this.article).then(response => {
                    sessionStorage.removeItem('article')
                    this.$router.push({ path: '/article' })
                    this.$notify.success({
                        title: '成功',
                        message: data.message
                    })
                    this.addOrEdit = false
                })
            } else {
                updateArticle(this.article).then(response => {
                    sessionStorage.removeItem('article')
                    this.$router.push({ path: '/article' })
                    this.$notify.success({
                        title: '成功',
                        message: data.message
                    })
                    this.addOrEdit = false
                })
            }
            //关闭自动保存功能
            this.autoSave = false
        },
        autoSaveArticle() {
            // 自动上传文章
            if (
                this.autoSave &&
                this.article.title.trim() != '' &&
                this.article.contentMd.trim() != '' &&
                this.article.id != null
            ) {
                // this.axios.post('/api/admin/articles', this.article).then(({ data }) => {
                //     if (data.flag) {
                //         this.$notify.success({
                //             title: '成功',
                //             message: '自动保存成功'
                //         })
                //     } else {
                //         this.$notify.error({
                //             title: '失败',
                //             message: '自动保存失败'
                //         })
                //     }
                // })
            }
            // 保存本地文章记录
            if (this.autoSave && this.article.id == null) {
                sessionStorage.setItem('article', JSON.stringify(this.article))
            }
        },
        searchCategories(keywords, cb) {
            listCategory({ fuzzyField: keywords }).then(response => {
                cb(response.data.records)
            })
        },
        handleSelectCategories(item) {
            this.addCategory({
                categoryName: item.name
            })
        },
        saveCategory() {
            if (this.categoryName.trim() != '') {
                this.addCategory({
                    categoryName: this.categoryName
                })
                this.categoryName = ''
            }
        },
        addCategory(item) {
            this.article.categoryName = item.categoryName
        },
        removeCategory() {
            this.article.categoryName = null
        },
        searchTags(keywords, cb) {
            listTag({ fuzzyField: keywords }).then(response => {
                cb(response.data.records)
            })
        },
        handleSelectTag(item) {
            this.addTag({
                tagName: item.name
            })
        },
        saveTag() {
            if (this.tagName.trim() != '') {
                this.addTag({
                    tagName: this.tagName
                })
                this.tagName = ''
            }
        },
        addTag(item) {
            if (this.article.tags.indexOf(item.name) == -1) {
                this.article.tags.push(item.name)
            }
        },
        removeTag(item) {
            const index = this.article.tags.indexOf(item)
            this.article.tags.splice(index, 1)
        }
    },
    computed: {
        tagClass() {
            return function (item) {
                const index = this.article.tags.indexOf(item.name)
                return index != -1 ? 'tag-item-select' : 'tag-item'
            }
        }
    }
}
</script>
  
<style scoped>
.article-title-container {
    display: flex;
    align-items: center;
    margin-bottom: 1.25rem;
    margin-top: 2.25rem;
}

.save-btn {
    margin-left: 0.75rem;
    background: #fff;
    color: #f56c6c;
}

.tag-item {
    margin-right: 1rem;
    margin-bottom: 1rem;
    cursor: pointer;
}

.tag-item-select {
    margin-right: 1rem;
    margin-bottom: 1rem;
    cursor: not-allowed;
    color: #ccccd8 !important;
}

.category-item {
    cursor: pointer;
    padding: 0.6rem 0.5rem;
}

.category-item:hover {
    background-color: #f0f9eb;
    color: #67c23a;
}

.popover-title {
    margin-bottom: 1rem;
    text-align: center;
}

.popover-container {
    margin-top: 1rem;
    height: 260px;
    overflow-y: auto;
}
</style>
  