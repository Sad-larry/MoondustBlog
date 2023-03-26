<template>
  <div class="app-container">
    <el-card class="main-card">
      <!-- 标题 -->
      <div style="margin-bottom: 10px" class="operation-container">
        <el-input
          v-model="queryParams.name"
          prefix-icon="el-icon-search"
          size="small"
          placeholder="请输入相册名"
          style="width:200px"
          @keyup.enter.native="handleQuery"
        />
        <el-button type="primary" size="small" icon="el-icon-search" style="margin-left:1rem" @click="handleQuery">搜索</el-button>
        <el-button
          type="primary"
          size="small"
          icon="el-icon-plus"
          @click="handleAdd()"
          v-hasPermi="['system:album:add']"
        >新建相册</el-button>
      </div>
      <!-- 相册列表 -->
      <el-row class="album-container" :gutter="12" v-loading="loading">
        <!-- 空状态 -->
        <el-empty v-if="albumList == null" description="暂无相册" />
        <el-col v-for="item of albumList" :key="item.id" :md="6">
          <div class="album-item" @click="checkPhoto(item)">
            <!-- 相册操作 -->
            <div class="album-opreation">
              <el-dropdown @command="handleCommand">
                <i class="el-icon-more" style="color:#fff" />
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="'update' + item.id">
                    <i class="el-icon-edit" v-hasPermi="['system:album:update']" />编辑
                  </el-dropdown-item>
                  <el-dropdown-item :command="'delete' + item.id">
                    <i class="el-icon-delete" v-hasPermi="['system:album:delete']" />删除
                  </el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
            <div class="album-photo-count">
              <div>{{ item.photoCount }}</div>
              <i v-if="item.status === 2" class="iconfont el-icon-mymima" />
            </div>
            <el-image fit="cover" class="album-cover" :src="item.cover" />
            <div class="album-name">{{ item.name }}</div>
          </div>
        </el-col>
      </el-row>
      <!-- 分页 -->
      <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getList"
      />
      <!-- 新增模态框 -->
      <el-dialog center :title="title" :visible.sync="open" append-to-body>
        <el-form ref="dataForm" :rules="rules" label-width="80px" size="medium" :model="albumForum">
          <el-form-item label="相册名称" prop="name">
            <el-input style="width:220px" v-model="albumForum.name" />
          </el-form-item>
          <el-form-item label="相册描述" prop="info">
            <el-input style="width:220px" v-model="albumForum.info" />
          </el-form-item>
          <el-form-item label="相册封面" prop="cover">
            <el-upload
              v-loading="imgLoading"
              class="upload-cover"
              drag
              :show-file-list="false"
              multiple
              :action="uploadPictureHost"
              :before-upload="beforeUpload"
              :http-request="uploadSectionFile"
            >
              <i class="el-icon-upload" v-if="albumForum.cover === ''" />
              <div class="el-upload__text" v-if="albumForum.cover === ''">
                将文件拖到此处，或
                <em>点击上传</em>
              </div>
              <img v-else :src="albumForum.cover" height="180px" />
            </el-upload>
          </el-form-item>
          <el-form-item label="发布形式" prop="status">
            <el-radio-group v-model="albumForum.status">
              <el-radio :label="1">公开</el-radio>
              <el-radio :label="0">私密</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>
<script>
import {
  listAlbum,
  getAlbum,
  delAlbum,
  addAlbum,
  updateAlbum,
} from "@/api/system/album";
import { uploadImage } from "@/api/system/qiniu";

export default {
  created() {
    this.getList();
  },
  data() {
    return {
      loading: true,
      imgLoading: false,
      img: process.env.VUE_APP_IMG_API,
      uploadPictureHost:
        process.env.VUE_APP_BASE_API + "/system/qiniu/upload/image",
      isEditForm: false,
      open: false,
      albumForum: {
        id: null,
        name: "",
        info: "",
        cover: "",
        status: 1,
      },
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
      },
      albumList: [],
      total: 0,
      title: null,
      rules: {
        name: [{ required: true, message: "必填字段", trigger: "blur" }],
        cover: [
          {
            required: true,
            message: "图片未上传或正在上传中",
            trigger: "change",
          },
        ],
        info: [{ required: true, message: "必填字段", trigger: "blur" }],
        status: [{ required: true, message: "必填字段", trigger: "change" }],
      },
    };
  },
  methods: {
    getList() {
      this.loading = true;
      listAlbum(this.queryParams).then((response) => {
        this.albumList = response.records;
        this.total = response.total;
        this.loading = false;
      });
    },
    submitForm() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          if (this.isEditForm) {
            updateAlbum(this.albumForum)
              .then((res) => {
                this.$message.success("修改相册成功");
                this.getList();
                this.open = false;
              })
              .catch((err) => {
                console.log(err);
              });
          } else {
            addAlbum(this.albumForum)
              .then((res) => {
                this.$message.success("添加相册成功");
                this.getList();
                this.open = false;
              })
              .catch((err) => {
                console.log(err);
              });
          }
          this.open = false;
        } else {
          this.$message.error("存在必填字段未填");
        }
      });
    },
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.albumForum = {
        id: null,
        name: "",
        info: "",
        cover: "",
        status: 1,
      };
      this.resetForm("albumForum");
    },
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新建相册";
    },
    handleEdit() {
      getAlbum(this.albumForum.id).then((res) => {
        this.albumForum = res.data;
      });
      this.title = "修改相册";
      this.isEditForm = true;
      this.open = true;
    },
    checkPhoto(item) {
      this.$router.push({ path: "/photos", query: { albumId: item.id } });
    },
    beforeUpload() {
      this.imgLoading = true;
    },
    checkUploadFile(file) {
      const isJPG =
        file.type == "image/png" ||
        file.type == "image/jpg" ||
        file.type == "image/jpeg" ||
        file.type == "image/gif";
      const isLt2M = file.size / 1024 / 1024 < 5;
      if (!isJPG) {
        this.$message.error("上传头像图片只能是 JPG/JPEG/PNG/GIF 格式!");
      }
      if (!isLt2M) {
        this.$message.error("上传头像图片大小不能超过 5MB!");
      }
      return isJPG && isLt2M;
    },
    uploadSectionFile(param) {
      let file = param.file;
      if (!this.checkUploadFile(file)) {
        this.imgLoading = false;
        return;
      }
      // FormData 对象
      var formData = new FormData();
      // 文件对象
      formData.append("file", file);

      uploadImage(formData)
        .then((res) => {
          this.albumForum.cover = res.data;
          this.imgLoading = false;
        })
        .catch((error) => {
          this.imgLoading = false;
        });
    },
    handleCommand(command) {
      const type = command.substring(0, 6);
      this.albumForum.id = command.substring(6);
      if (type === "delete") {
        this.handleDelete();
      } else {
        this.handleEdit();
      }
    },
    handleDelete() {
      this.$confirm("此操作将把页面删除, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          delAlbum(this.albumForum.id)
            .then((res) => {
              this.$message.success("删除相册成功");
              this.getList();
            })
            .catch((err) => {
              console.log(err);
            });
        })
        .catch(() => {
          this.$message.info("取消删除");
        });
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
  },
};
</script>
  
<style scoped>
.album-cover {
  position: relative;
  border-radius: 4px;
  width: 100%;
  height: 170px;
}

.album-cover::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
}

.album-photo-count {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 1.5rem;
  z-index: 1000;
  position: absolute;
  left: 0;
  right: 0;
  padding: 0 0.5rem;
  bottom: 2.6rem;
  color: #fff;
}

.album-name {
  text-align: center;
  margin-top: 0.5rem;
}

.album-item {
  position: relative;
  cursor: pointer;
  margin-bottom: 1rem;
}

.album-opreation {
  position: absolute;
  z-index: 1000;
  top: 0.5rem;
  right: 0.8rem;
}
</style>