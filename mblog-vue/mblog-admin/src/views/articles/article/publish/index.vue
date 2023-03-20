<template>
  <div class="app-container">
    <el-card class="main-card">
      <!-- 文章标题 -->
      <div class="article-title-container">
        <el-input
          v-model="article.title"
          size="medium"
          placeholder="输入文章标题"
          clearable
          @clear="clearTitle"
          ref="titleRef"
        />
        <!-- 发布文章 -->
        <el-button
          type="danger"
          size="medium"
          @click="openModel"
          style="margin-left: 10px"
          v-hasPermi="['system:category:publish']"
        >发布文章</el-button>
        <!-- 用于本地上传文章 -->
        <el-button
          type="primary"
          size="medium"
          @click="uploadArticleDialog = true"
          style="margin-left: 10px"
          v-hasPermi="['system:category:upload']"
        >上传文章</el-button>
        <!-- 上传本地文章后需要上传的图片 -->
        <!-- TODO 上传图片权限 -->
        <el-button
          v-if="imagesUploadFiles.length"
          type="warning"
          size="medium"
          @click="uploadImagesDialog = true"
          style="margin-left: 10px"
        >上传图片</el-button>
      </div>
      <!-- 文章内容 -->
      <mavon-editor ref="md" v-model="article.contentMd" @imgAdd="uploadImg" style="height: calc(100vh - 260px)" />
      <!-- 添加文章对话框 -->
      <el-dialog :visible.sync="addOrEdit" width="40%" top="3vh">
        <div class="dialog-title-container" slot="title">发布文章</div>
        <!-- 文章数据 -->
        <el-form label-width="80px" size="medium" :model="article">
          <!-- 文章分类 -->
          <el-form-item label="文章分类">
            <el-tag
              type="success"
              v-show="article.categoryName"
              style="margin: 0 1rem 0 0"
              :closable="true"
              @close="removeCategory"
            >{{ article.categoryName }}</el-tag>
            <!-- 分类选项 -->
            <el-popover placement="bottom-start" width="460" trigger="click" v-if="!article.categoryName">
              <div class="popover-title">分类</div>
              <!-- 搜索框 -->
              <el-autocomplete
                style="width: 100%"
                v-model="categoryName"
                :fetch-suggestions="searchCategories"
                placeholder="请输入分类名搜索，enter可添加自定义分类"
                :trigger-on-focus="false"
                @keyup.enter.native="saveCategory"
                @select="handleSelectCategories"
              >
                <!-- cb()返回的数据 -->
                <template slot-scope="{ item }">
                  <div>{{ item.name }}</div>
                </template>
              </el-autocomplete>
              <!-- 分类 -->
              <div class="popover-container">
                <div
                  v-for="item of categorys"
                  :key="item.id"
                  class="category-item"
                  @click="addCategory(item)"
                >{{ item.name }}</div>
              </div>
              <el-button type="success" plain slot="reference" size="small">添加分类</el-button>
            </el-popover>
          </el-form-item>
          <!-- 文章标签 -->
          <el-form-item label="文章标签">
            <el-tag
              v-for="(item, index) of article.tags"
              :key="index"
              style="margin: 0 1rem 0 0"
              :closable="true"
              @close="removeTag(item)"
            >{{ item }}</el-tag>
            <!-- 标签选项 -->
            <el-popover placement="bottom-start" width="460" trigger="click" v-if="article.tags.length < 3">
              <div class="popover-title">标签</div>
              <!-- 搜索框 -->
              <el-autocomplete
                style="width: 100%"
                v-model="tagName"
                :fetch-suggestions="searchTags"
                placeholder="请输入标签名搜索，enter可添加自定义标签"
                :trigger-on-focus="false"
                @keyup.enter.native="saveTag"
                @select="handleSelectTag"
              >
                <template slot-scope="{ item }">
                  <div>{{ item.name }}</div>
                </template>
              </el-autocomplete>
              <!-- 标签 -->
              <div class="popover-container">
                <div style="margin-bottom: 1rem">添加标签</div>
                <el-tag
                  v-for="(item, index) of tagList"
                  :key="index"
                  :class="tagClass(item)"
                  @click="addTag(item)"
                >{{ item.name }}</el-tag>
              </div>
              <el-button type="primary" plain slot="reference" size="small">添加标签</el-button>
            </el-popover>
          </el-form-item>
          <el-form-item label="文章类型">
            <el-select v-model="article.isOriginal" placeholder="请选择类型">
              <el-option v-for="item in typeList" :key="item.type" :label="item.desc" :value="item.type" />
            </el-select>
          </el-form-item>
          <el-form-item label="原文地址" v-if="article.isOriginal != 1">
            <el-input v-model="article.originalUrl" placeholder="请填写原文链接" />
          </el-form-item>
          <el-form-item label="上传封面">
            <file-upload
              :limit="uploadAvatarOption.limit"
              :file-size="uploadAvatarOption.fileSize"
              :file-type="uploadAvatarOption.fileType"
              @return-data="returnAvatarUpload"
            ></file-upload>
          </el-form-item>
          <el-form-item label="封面预览" v-if="article.avatar">
            <image-preview :src="article.avatar" :width="180" :height="180" style="margin-top: 10px;" />
          </el-form-item>
          <el-form-item label="置顶">
            <el-switch
              v-model="article.isStick"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
          <el-form-item label="发布">
            <el-switch
              v-model="article.isPublish"
              active-color="#13ce66"
              inactive-color="#F4F4F5"
              :active-value="1"
              :inactive-value="0"
            />
          </el-form-item>
          <el-form-item label="发布形式">
            <el-radio-group v-model="article.isSecret">
              <el-radio :label="0">公开</el-radio>
              <el-radio :label="1">私密</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="文章简介">
            <el-input v-model="article.summary" type="textarea" placeholder="为你的文章写个文章简介再发布吧" />
          </el-form-item>
        </el-form>
        <div slot="footer">
          <el-button @click="addOrEdit = false">取 消</el-button>
          <el-button type="danger" @click="saveOrUpdateArticle">发 表</el-button>
        </div>
      </el-dialog>

      <!-- md文件上传 -->
      <el-dialog width="400px" center :visible.sync="uploadArticleDialog" append-to-body>
        <div>
          <file-upload
            :limit="uploadArticleOption.limit"
            :file-size="uploadArticleOption.fileSize"
            :file-type="uploadArticleOption.fileType"
            @return-data="returnUploadArticle"
          />
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="uploadArticleDialog = false">取 消</el-button>
        </div>
      </el-dialog>

      <!-- 图片上传 -->
      <el-dialog center :visible.sync="uploadImagesDialog" append-to-body>
        <div>
          <file-upload
            :params="uploadImagesOption.params"
            :limit="uploadImagesOption.limit"
            :file-size="uploadImagesOption.fileSize"
            :file-type="uploadImagesOption.fileType"
            @return-data="returnImagesUpload"
          />
        </div>
        <!-- 待上传的图片名称 -->
        <div>
          <el-card>
            <el-row :gutter="20">
              <el-col>请上传以下图片（成功上传的图片为不可选中）</el-col>
            </el-row>
            <!-- 待上传的图片 -->
            <el-row :gutter="20" v-for="(item,index) in imagesUploadFiles" :key="index">
              <el-col :offset="1">待上传：{{item}}</el-col>
            </el-row>
            <div style="margin-top: 10px;">
              <!-- 已经上传成功的图片 -->
              <el-row :gutter="20" v-for="(item,index) in imagesUploadSuccess" :key="index">
                <el-col>
                  <el-checkbox :label="item" disabled checked>已完成：{{item}}</el-checkbox>
                </el-col>
              </el-row>
            </div>
          </el-card>
        </div>
        <div slot="footer" class="dialog-footer">
          <el-button @click="uploadImagesDialog = false">取 消</el-button>
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
import * as imageConversion from "image-conversion";
import { uploadImage } from "@/api/system/qiniu";

export default {
  name: "PublishArticle",
  data() {
    return {
      addOrEdit: false,
      autoSave: true,
      // 搜索框中的搜索的分类以及标签名称
      categoryName: "",
      tagName: "",
      // 分类以及标签列表
      categorys: [],
      tagList: [],
      typeList: [
        { type: 1, desc: "原创" },
        { type: 0, desc: "转载" },
      ],
      article: {
        id: null,
        title: parseTime(new Date()),
        avatar: "",
        summary: "",
        contentMd: "",
        isSecret: 0,
        isStick: 0,
        isPublish: 1,
        isOriginal: 1,
        originalUrl: null,
        remark: null,
        keywords: null,
        categoryName: null,
        tags: [],
      },
      // 负责文章封面上传
      uploadAvatarOption: {
        limit: 1,
        fileSize: 5,
        fileType: ["jpg", "png", "gif", "jpeg"],
      },
      // 负责 md 本地文件上传
      uploadArticleDialog: false,
      uploadArticleOption: {
        limit: 1,
        fileSize: 10,
        fileType: ["md"],
      },
      // 负责上传本地文章后的图片上传
      uploadImagesDialog: false,
      uploadImagesOption: {
        params: { key: "" },
        limit: 100,
        fileSize: 5,
        fileType: ["jpg", "png", "gif", "jpeg"],
      },
      // 待上传的图片名称
      imagesUploadFiles: [],
      // 已经上传成功的图片名称
      imagesUploadSuccess: [],
    };
  },
  created() {
    // 若是从文章列表新增，则应该将文章id传递过来
    const id = this.$route.query.id;
    if (id) {
      getArticle(id).then((response) => {
        this.article = response.data;
        this.article.tags = this.article.tagNames.split(",");
      });
    } else {
      const article = sessionStorage.getItem("article");
      if (article) {
        this.article = JSON.parse(article);
      }
    }
    this.$modal.msg("已开启自动保存文章草稿");
  },
  destroyed() {
    //文章自动保存功能
    this.autoSaveArticle();
  },
  methods: {
    /** 清除默认生成的文章标题时触发 */
    clearTitle(e) {
      this.$nextTick(() => {
        this.$refs.titleRef.focus();
      });
    },
    listCategories() {
      listCategory().then((response) => {
        this.categorys = response.data.records;
      });
    },
    listTags() {
      listTag().then((response) => {
        this.tagList = response.data.records;
      });
    },
    openModel() {
      if (this.article.title.trim() == "") {
        this.$modal.msgError("文章标题不能为空");
        return false;
      }
      if (this.article.contentMd.trim() == "") {
        this.$modal.msgError("文章内容不能为空");
        return false;
      }
      this.listCategories();
      this.listTags();
      this.addOrEdit = true;
    },
    uploadImg(pos, file) {
      var formdata = new FormData();
      if (file.size / 1024 < process.env.UPLOAD_SIZE) {
        formdata.append("file", file);
        uploadImage(formdata).then((res) => {
          this.$refs.md.$img2Url(pos, res.data);
        });
      } else {
        // 压缩到200KB,这里的200就是要压缩的大小,可自定义
        imageConversion
          .compressAccurately(file, process.env.UPLOAD_SIZE)
          .then((res) => {
            formdata.append(
              "file",
              new window.File([res], file.name, { type: file.type })
            );
            uploadImage(formdata).then((res) => {
              this.$refs.md.$img2Url(pos, res.data);
            });
          });
      }
    },
    saveOrUpdateArticle() {
      if (this.article.title.trim() == "") {
        this.$modal.msgError("文章标题不能为空");
        return false;
      }
      if (this.article.contentMd.trim() == "") {
        this.$modal.msgError("文章内容不能为空");
        return false;
      }
      if (this.article.categoryName == null) {
        this.$modal.msgError("文章分类不能为空");
        return false;
      }
      if (this.article.tags.length == 0) {
        this.$modal.msgError("文章标签不能为空");
        return false;
      }
      if (this.article.summary.trim() == "") {
        this.$modal.msgError("文章封面不能为空");
        return false;
      }
      if (this.article.id === null) {
        addArticle(this.article).then((res) => {
          sessionStorage.removeItem("article");
          this.$router.push({ path: "/article" });
          this.$notify.success({
            title: "成功",
            message: data.message,
          });
          this.addOrEdit = false;
        });
      } else {
        updateArticle(this.article).then((res) => {
          sessionStorage.removeItem("article");
          this.$router.push({ path: "/article" });
          this.$notify.success({
            title: "成功",
            message: data.message,
          });
          this.addOrEdit = false;
        });
      }
      //关闭自动保存功能
      this.autoSave = false;
    },
    autoSaveArticle() {
      // 自动保存本地文章记录
      if (this.autoSave && this.article.id == null) {
        sessionStorage.setItem("article", JSON.stringify(this.article));
      }
    },
    searchCategories(keywords, cb) {
      listCategory({ fuzzyField: keywords }).then((response) => {
        cb(response.data.records);
      });
    },
    handleSelectCategories(item) {
      this.addCategory({
        name: item.name,
      });
    },
    saveCategory() {
      if (this.categoryName.trim() != "") {
        this.addCategory({
          name: this.categoryName,
        });
        this.categoryName = "";
      }
    },
    addCategory(item) {
      this.article.categoryName = item.name;
    },
    removeCategory() {
      this.article.categoryName = null;
    },
    searchTags(keywords, cb) {
      listTag({ fuzzyField: keywords }).then((response) => {
        cb(response.data.records);
      });
    },
    handleSelectTag(item) {
      this.addTag({
        name: item.name,
      });
    },
    saveTag() {
      if (this.tagName.trim() != "") {
        this.addTag({
          name: this.tagName,
        });
        this.tagName = "";
      }
    },
    addTag(item) {
      if (this.article.tags.indexOf(item.name) == -1) {
        this.article.tags.push(item.name);
      }
    },
    removeTag(item) {
      const index = this.article.tags.indexOf(item);
      this.article.tags.splice(index, 1);
    },
    /** 上传文章封面返回的响应数据 */
    returnAvatarUpload(res) {
      this.article.avatar = res.data;
    },
    /** 上传md文件时返回的响应数据 */
    returnUploadArticle(res) {
      this.article.contentMd = res.data.contentMd;
      this.article.title = res.data.title;
      this.uploadImagesOption.params.key = res.data.imageCacheKey;
      this.imagesUploadFiles = res.data.imageUrl;
      this.$message.warning("可在10分钟内上传图片，以至能正常显示图片");
      // 关闭上传文章对话框
      this.uploadArticleDialog = false;
    },
    /** 上传本地图片时返回的响应数据 */
    returnImagesUpload(res) {
      // 删除已上传的图片名称
      let new_set = new Set(this.imagesUploadFiles);
      new_set.delete(res.data.filename);
      this.imagesUploadFiles = [...new_set];
      // 添加成功上传的图片
      this.imagesUploadSuccess.push(res.data.filename);
      // 文章中替换掉图片链接
      this.article.contentMd = this.article.contentMd.replace(
        new RegExp(res.data.filename, "g"),
        res.data.fileLink
      );
      // 若图片全部上传成功，则自动退出对话框
      if (!this.imagesUploadFiles.length) {
        this.uploadImagesDialog = false;
      }
    },
  },
  computed: {
    tagClass() {
      return function (item) {
        const index = this.article.tags.indexOf(item.name);
        return index != -1 ? "tag-item-select" : "tag-item";
      };
    },
  },
};
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
