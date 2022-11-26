<template>
    <div class="app-container">
        <el-card class="main-card">
            <!-- 相册信息 -->
            <div class="album-info">
                <el-image fit="cover" class="album-cover" :src="albumInfo.cover" />
                <div class="album-detail">
                    <div style="margin-bottom:0.6rem">
                        <span class="album-name">{{ albumInfo.name }}</span>
                        <span class="photo-count">{{ albumInfo.photoCount }}张</span>
                    </div>
                    <div>
                        <span v-if="albumInfo.info" class="album-desc">
                            {{ albumInfo.info }}
                        </span>
                        <el-button icon="el-icon-picture" type="primary" size="small" @click="handleAdd">
                            添加照片
                        </el-button>
                    </div>
                </div>
                <!-- 相册操作 -->
                <div class="operation">
                    <div class="all-check">
                        <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">
                            全选
                        </el-checkbox>
                        <div class="check-count">已选择{{ selectPhotoIdList.length }}张</div>
                    </div>
                    <el-button type="success" @click="movePhoto = true" :disabled="selectPhotoIdList.length === 0"
                        size="small" icon="el-icon-deleteItem">
                        移动到
                    </el-button>
                    <el-button type="danger" @click="deletePhoto" :disabled="selectPhotoIdList.length === 0"
                        size="small" icon="el-icon-deleteItem">
                        批量删除
                    </el-button>
                </div>
            </div>
            <!-- 照片列表 -->
            <el-row class="photo-container" :gutter="10" v-loading="loading">
                <!-- 空状态 -->
                <el-empty v-if="photoList.length === 0" description="暂无照片" />
                <el-checkbox-group v-model="selectPhotoIdList" @change="handleCheckedPhotoChange">
                    <el-col :md="4" v-for="item of photoList" :key="item.id">
                        <el-checkbox :label="item.id">
                            <div class="photo-item">
                                <!-- 照片操作 -->
                                <div class="photo-opreation">
                                    <el-dropdown @command="handleCommand">
                                        <i class="el-icon-more" style="color:#fff" />
                                        <el-dropdown-menu slot="dropdown">
                                            <el-dropdown-item :command="'update' + item.id">
                                                <i class="el-icon-edit" />编辑
                                            </el-dropdown-item>
                                            <el-dropdown-item :command="'delete' + item.id">
                                                <i class="el-icon-delete" />删除
                                            </el-dropdown-item>
                                        </el-dropdown-menu>
                                    </el-dropdown>
                                </div>
                                <el-image fit="cover" class="photo-img" :src="item.url"
                                    :preview-photoSrc-list="photoList" />
                                <div class="photo-name">{{ item.name }}</div>
                            </div>
                        </el-checkbox>
                    </el-col>
                </el-checkbox-group>
            </el-row>
            <!-- 分页 -->
            <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize" @pagination="listPhotos" />

            <!-- 新增编辑对话框 -->
            <el-dialog center :title="title" :visible.sync="open" append-to-body>
                <el-form :rules="rules" ref="dataForm" label-width="80px" size="medium" :model="photoForm">
                    <el-form-item label="照片名称" prop="name">
                        <el-input style="width:220px" v-model="photoForm.name" />
                    </el-form-item>
                    <el-form-item label="照片描述" prop="info">
                        <el-input style="width:220px" v-model="photoForm.info" />
                    </el-form-item>
                    <el-form-item label="照片" prop="url">
                        <el-upload v-loading="imgLoading" class="upload-cover" drag :show-file-list="false" multiple
                            :action="uploadPictureHost" :before-upload="beforeUpload" :http-request="uploadSectionFile">
                            <i class="el-icon-upload" v-if="photoForm.url === ''" />
                            <div class="el-upload__text" v-if="photoForm.url === ''">
                                将文件拖到此处，或<em>点击上传</em>
                            </div>
                            <img v-else :src="photoForm.url" height="180px" />
                        </el-upload>
                    </el-form-item>
                </el-form>
                <div slot="footer">
                    <el-button @click="open = false">取消</el-button>
                    <el-button type="primary" @click="submit">确定</el-button>
                </div>
            </el-dialog>

            <!-- 移动对话框 -->
            <el-dialog :visible.sync="movePhoto" width="30%">
                <div class="dialog-title-container" slot="title">
                    移动照片
                </div>
                <el-empty v-if="albumList.length < 2" description="暂无其他相册" />
                <el-form v-else label-width="80px" size="medium" :model="photoForm">
                    <el-radio-group v-model="albumId">
                        <div class="album-check-item">
                            <template v-for="item of albumList">
                                <el-radio v-if="item.id !== albumInfo.id" :key="item.id" :label="item.id"
                                    style="margin-bottom:1rem">
                                    <div class="album-check">
                                        <el-image fit="cover" class="album-check-cover" :src="item.cover" />
                                        <div style="margin-left:0.5rem">{{ item.name }}</div>
                                    </div>
                                </el-radio>
                            </template>
                        </div>
                    </el-radio-group>
                </el-form>
                <div slot="footer">
                    <el-button @click="movePhoto = false">取 消</el-button>
                    <el-button :disabled="albumId == null" type="primary" @click="updatePhotoAlbum">
                        确 定
                    </el-button>
                </div>
            </el-dialog>
        </el-card>
    </div>
</template>
  
<script>
import { listPhoto, delPhoto, addPhoto, updatePhoto, getPhoto, movePhoto } from '@/api/system/photo'
import { listAlbum, getAlbum } from "@/api/system/album";
import { uploadImage } from "@/api/system/qiniu";

export default {
    created() {
        this.getAlbumInfo();
        this.listAlbums();
        this.listPhotos();
    },
    data() {
        return {
            loading: true,
            checkAll: false,
            isIndeterminate: false,
            imgLoading: false,
            uploadPictureHost: process.env.VUE_APP_BASE_API + "/system/qiniu/upload/image",
            img: process.env.VUE_APP_IMG_API,
            open: false,
            isEditForm: false,
            title: null,
            movePhoto: false,
            photoList: [],
            photoIdList: [],
            selectPhotoIdList: [],
            albumList: [],
            albumInfo: {
                id: null,
                name: "",
                info: "",
                cover: "",
                photoCount: null
            },
            photoForm: {
                id: null,
                name: "",
                info: ""
            },
            albumId: null,
            total: 0,
            queryParams: {
                pageNum: 1,
                pageSize: 18,
                albumId: this.$route.query.albumId,
            },
            rules: {
                'name': [{ required: true, message: '必填字段', trigger: 'blur' }],
                'info': [{ required: true, message: '必填字段', trigger: 'blur' }],
                'url': [{ required: true, message: '图片未上传或正在上传中', trigger: 'change' }],
            }
        };
    },
    methods: {
        getAlbumInfo() {
            getAlbum(this.$route.query.albumId).then(response => {
                this.albumInfo = response.data;
            });
        },
        listAlbums() {
            listAlbum({ pageNum: 1, pageSize: 100 }).then(response => {
                this.albumList = response.records;
            });
        },
        listPhotos() {
            listPhoto(this.queryParams).then(response => {
                this.photoList = response.records;
                this.total = response.total;
                this.loading = false;
            });
        },
        submit() {
            this.$refs['dataForm'].validate((valid) => {
                if (valid) {
                    if (this.isEditForm) {
                        updatePhoto(this.photoForm).then(response => {
                            this.$message.success("修改照片成功")
                            this.listPhotos();
                            this.open = false;
                        }).catch(err => {
                            console.log(err)
                        })
                    } else {
                        addPhoto(this.photoForm).then(response => {
                            this.$message.success("添加照片成功")
                            this.listPhotos()
                            this.open = false;
                        }).catch(err => {
                            console.log(err)
                        })
                    }
                } else {
                    this.$message.error("存在必填字段未填")
                }
            })
        },

        handleAdd() {
            this.photoForm = {
                id: null,
                albumId: this.$route.query.albumId,
                name: "",
                info: "",
                url: "",
            }
            this.title = "新增照片"
            this.open = true;
        },
        updatePhotoAlbum() {
            let ids = this.selectPhotoIdList.join(",");
            movePhoto({ albumId: this.albumId, ids: ids }).then(response => {
                this.$message.success("移动照片成功")
                this.getAlbumInfo();
                this.listPhotos();
                this.movePhoto = false;
            }).catch(err => {
                console.log(err)
            });
        },
        beforeUpload() {
            this.imgLoading = true
        },
        checkUploadFile(file) {
            const isJPG = file.type == 'image/png' || file.type == 'image/jpg' || file.type == 'image/jpeg' || file.type == 'image/gif';
            const isLt2M = file.size / 1024 / 1024 < 5;
            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG/JPEG/PNG/GIF 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 5MB!');
            }
            return isJPG && isLt2M;
        },
        uploadSectionFile(param) {
            let file = param.file
            if (!this.checkUploadFile(file)) {
                this.imgLoading = false
                return;
            }
            // FormData 对象
            var formData = new FormData()
            // 文件对象
            formData.append('file', file)
            uploadImage(formData).then(response => {
                this.photoForm.url = response.data
                this.imgLoading = false
            })
        },
        handleCheckAllChange(val) {
            this.selectPhotoIdList = val ? this.photoIdList : [];
            this.isIndeterminate = false;
        },
        handleCheckedPhotoChange(value) {
            let checkedCount = value.length;
            this.checkAll = checkedCount === this.photoIdList.length;
            this.isIndeterminate =
                checkedCount > 0 && checkedCount < this.photoIdList.length;
        },
        handleCommand(command) {
            const type = command.substring(0, 6);
            let id = command.substring(6);
            if (type === 'delete') {
                this.deletePhoto(id)
            } else {
                this.handleUpdate(id)
            }
        },
        handleUpdate(id) {
            getPhoto(id).then(response => {
                this.photoForm = response.data
            })
            this.title = "修改照片"
            this.isEditForm = true
            this.open = true;
        },
        deletePhoto(id) {
            if (id == null) {
                this.selectPhotoIdList.push(id)
            }
            this.$confirm('此操作将把页面删除, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                delPhoto(this.selectPhotoIdList).then(response => {
                    this.$message.success("删除照片成功")
                    this.listPhotos();
                }).catch(err => {
                    console.log(err)
                });
            }).catch(() => {
                this.$message.info("取消删除")
            })
        },
    },
    watch: {
        photoList() {
            this.photoIdList = [];
            this.photoList.forEach(item => {
                this.photoIdList.push(item.id);
            });
        }
    }
};
</script>
  
<style scoped>
.album-info {
    display: flex;
    margin-top: 2.25rem;
    margin-bottom: 2rem;
}

.album-cover {
    border-radius: 4px;
    width: 5rem;
    height: 5rem;
}

.album-check-cover {
    border-radius: 4px;
    width: 4rem;
    height: 4rem;
}

.album-detail {
    padding-top: 0.4rem;
    margin-left: 0.8rem;
}

.album-desc {
    font-size: 14px;
    margin-right: 0.8rem;
}

.operation {
    padding-top: 1.5rem;
    margin-left: auto;
}

.all-check {
    display: inline-flex;
    align-items: center;
    margin-right: 1rem;
}

.check-count {
    margin-left: 1rem;
    font-size: 12px;
}

.album-name {
    font-size: 1.25rem;
}

.photo-count {
    font-size: 12px;
    margin-left: 0.5rem;
}

.photo-item {
    width: 100%;
    position: relative;
    cursor: pointer;
    margin-bottom: 1rem;
}

.photo-img {
    width: 100%;
    height: 7rem;
    border-radius: 4px;
}

.photo-name {
    font-size: 14px;
    margin-top: 0.3rem;
    text-align: center;
}

.upload-container {
    height: 400px;
}

.upload {
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
}

.upload-footer {
    display: flex;
    align-items: center;
}

.photo-opreation {
    position: absolute;
    z-index: 1000;
    top: 0.3rem;
    right: 0.5rem;
}

.album-check {
    display: flex;
    align-items: center;
}
</style>
  