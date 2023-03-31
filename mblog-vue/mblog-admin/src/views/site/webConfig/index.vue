<template>
  <div class="app-container">
    <el-tabs type="border-card" @tab-click="handleClick">
      <el-tab-pane>
        <span slot="label">
          <i class="el-icon-date"></i> 网站信息
        </span>
        <el-form style="margin-left: 20px;" label-position="left" :model="form" label-width="80px" ref="from">
          <el-row :gutter="12">
            <el-col :span="6">
              <el-form-item label="网站头像">
                <el-upload
                  class="avatar-uploader1"
                  action
                  ref="upload"
                  :show-file-list="false"
                  :before-upload="uploadBefore"
                  :http-request="uploadSectionFile"
                >
                  <img style="width: 100px;height: 100px" v-if="form.logo" :src="form.logo" class="avatar1" />
                  <i v-else class="el-icon-plus avatar-uploader-icon1"></i>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="6">
              <el-form-item label="游客头像" prop="touristAvatar">
                <el-upload
                  class="avatar-uploader1"
                  action
                  ref="upload"
                  :show-file-list="false"
                  :before-upload="uploadBefore"
                  :http-request="touristUpload"
                >
                  <img
                    style="width: 100px;height: 100px"
                    v-if="form.touristAvatar"
                    :src="form.touristAvatar"
                    class="avatar1"
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon1"></i>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="网站名称" prop="name">
                <el-input v-model="form.name" style="width: 300px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="网站地址" prop="webUrl">
                <el-input v-model="form.webUrl" style="width: 300px"></el-input>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="关键字" prop="newPwd2">
                <el-input v-model="form.keyword" style="width: 300px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="描述" prop="newPwd1">
                <el-input v-model="form.summary" style="width: 300px"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="公告" prop="newPwd1">
                <el-input v-model="form.bulletin" style="width: 300px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="备案号" prop="newPwd2">
                <el-input v-model="form.recordNum" style="width: 300px"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="登录方式">
                <el-checkbox-group v-model="loginTypeLists">
                  <el-checkbox
                    border
                    v-for="item in loginDictList"
                    :label="item.value"
                    :key="item.label"
                    style="margin-left: 0px"
                  >{{ item.label }}</el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-hasPermi="['system:webConfig:update']">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane>
        <span slot="label">
          <i class="el-icon-date"></i> 作者信息
        </span>

        <el-form style="margin-left: 20px;" label-position="left" :model="form" label-width="80px" ref="from">
          <el-form-item label="作者头像">
            <el-upload
              class="avatar-uploader1"
              action
              ref="upload"
              :show-file-list="false"
              :before-upload="uploadBefore"
              :http-request="authorUpload"
            >
              <img style="width: 100px;height: 100px" v-if="form.authorAvatar" :src="form.authorAvatar" class="avatar1" />
              <i v-else class="el-icon-plus avatar-uploader-icon1"></i>
            </el-upload>
          </el-form-item>

          <el-row :gutter="24">
            <el-col :span="10">
              <el-form-item label="作者" prop="newPwd2">
                <el-input v-model="form.author" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="10">
              <el-form-item label="作者简介" prop="newPwd2">
                <el-input v-model="form.authorInfo" style="width: 400px"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-form-item label="关于我">
              <mavon-editor
                placeholder="开始编辑...."
                :subfield="false"
                style="height: 300px"
                ref="md"
                v-model="form.aboutMe"
              />
            </el-form-item>
          </el-row>

          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-hasPermi="['system:webConfig:update']">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane>
        <span slot="label">
          <i class="el-icon-date"></i> 评论&打赏
        </span>

        <el-form style="margin-left: 20px;" label-position="left" :model="form" label-width="90px" ref="from">
          <el-row :gutter="24">
            <el-col :span="5">
              <el-form-item label="支付宝">
                <el-upload
                  class="avatar-uploader1"
                  action
                  ref="upload"
                  :show-file-list="false"
                  :before-upload="uploadBefore"
                  :http-request="aliPayMethod"
                >
                  <img style="width: 100px;height: 100px" v-if="form.aliPay" :src="form.aliPay" class="avatar1" alt />
                  <i v-else class="el-icon-plus avatar-uploader-icon1"></i>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="5">
              <el-form-item label="微信">
                <el-upload
                  class="avatar-uploader1"
                  action
                  ref="upload"
                  :show-file-list="false"
                  :before-upload="uploadBefore"
                  :http-request="weiXinPayMethod"
                >
                  <img
                    style="width: 100px;height: 100px"
                    v-if="form.weixinPay"
                    :src="form.weixinPay"
                    class="avatar1"
                    alt
                  />
                  <i v-else class="el-icon-plus avatar-uploader-icon1"></i>
                </el-upload>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-form-item label="网站评论">
              <el-radio
                v-for="item in openDictList"
                :key="item.id"
                v-model="form.openComment"
                :label="Number.parseInt(item.value)"
                border
                size="medium"
              >{{ item.label }}</el-radio>
            </el-form-item>
          </el-row>
          <el-row :gutter="24">
            <el-form-item label="网站打赏">
              <el-radio
                v-for="item in openDictList"
                :key="item.id"
                v-model="form.openAdmiration"
                :label="Number.parseInt(item.value)"
                border
                size="medium"
              >{{ item.label }}</el-radio>
            </el-form-item>
          </el-row>
          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-hasPermi="['system:webConfig:update']">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <el-tab-pane>
        <span slot="label">
          <i class="el-icon-date"></i> 关注我们
        </span>
        <el-form
          style="margin-left: 20px;"
          label-position="left"
          :model="form"
          label-width="80px"
          :rules="rules"
          ref="form"
        >
          <el-checkbox-group v-model="showList">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="form.email" style="width: 400px"></el-input>
              <el-checkbox label="1" style="margin-left: 10px">在首页显示</el-checkbox>
            </el-form-item>
            <el-form-item label="QQ号" prop="qqNumber">
              <el-input v-model="form.qqNumber" style="width: 400px"></el-input>
              <el-checkbox label="2" style="margin-left: 10px">在首页显示</el-checkbox>
            </el-form-item>
            <el-form-item label="github" prop="github">
              <el-input v-model="form.github" style="width: 400px"></el-input>
              <el-checkbox label="3" style="margin-left: 10px">在首页显示</el-checkbox>
            </el-form-item>
            <el-form-item label="Gitee" prop="gitee">
              <el-input v-model="form.gitee" style="width: 400px"></el-input>
              <el-checkbox label="4" style="margin-left: 10px">在首页显示</el-checkbox>
            </el-form-item>
          </el-checkbox-group>
          <el-form-item>
            <el-button type="primary" @click="submitForm()" v-hasPermi="['system:webConfig:update']">保 存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
  
<script>
import { getWebConfig, updateWebConfig } from "@/api/system/webConfig";
import { getDataByDictType } from "@/api/system/dictData";
import { uploadImage } from "@/api/system/qiniu";

export default {
  data() {
    return {
      loginTypeLists: [],
      showList: [],
      loading: false,
      img: process.env.VUE_APP_IMG_API,
      form: {
        name: "",
        aboutMe: "",
        webUrl: "",
        keyword: "",
        summary: "",
        author: "",
        logo: "",
        recordNum: "",
        openComment: "1",
        aliPay: "",
        weixinPay: "",
        authorAvatar: "",
        authorInfo: "",
        touristAvatar: "",
        bulletin: "",
        showList: "",
        loginTypeList: "",
      },
      systemConfig: {},
      openDictList: [], //字典
      loginDictList: [], //字典
      files: {},
      rules: {
        qqNumber: [
          { pattern: /[1-9]([0-9]{5,11})/, message: "请输入正确的QQ号码" },
        ],
        qqGroup: [{ pattern: /-?[1-9]\d*/, message: "请输入正确的QQ群" }],
        gitee: [
          {
            pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/,
            message: "请输入正确的Gitee地址",
          },
        ],
        github: [
          {
            pattern: /^((https|http|ftp|rtsp|mms)?:\/\/)[^\s]+/,
            message: "请输入正确的Github地址",
          },
        ],
        email: [
          {
            pattern: /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/,
            message: "请输入正确的邮箱",
          },
        ],
      },
    };
  },
  created() {
    // 查询字典
    this.getDictList();
    // 获取配置
    this.getWebConfigFun();
  },
  methods: {
    /**
     * 字典查询
     */
    getDictList() {
      let params = ["sys_normal_disable", "sys_login_method"];
      getDataByDictType(params).then((response) => {
        let dictMap = response.data;
        this.openDictList = dictMap.sys_normal_disable.list;
        this.loginDictList = dictMap.sys_login_method.list;
      });
    },
    handleClick(tab, event) {},
    getWebConfigFun() {
      getWebConfig().then((response) => {
        let data = response.data;
        let showList = data.showList.split(",");
        let logins = data.loginTypeList.split(",");
        this.loginTypeLists = logins;
        this.showList = showList;
        this.form = data;
      });
    },
    weiXinPayMethod(file) {
      let fromData = this.initFromData(file);
      uploadImage(fromData)
        .then((res) => {
          this.form.weixinPay = res.data;
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    aliPayMethod(file) {
      let fromData = this.initFromData(file);
      uploadImage(fromData)
        .then((res) => {
          this.form.aliPay = res.data;
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    uploadSectionFile(file) {
      let fromData = this.initFromData(file);
      uploadImage(fromData)
        .then((res) => {
          this.form.logo = res.data;
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    touristUpload(file) {
      let fromData = this.initFromData(file);
      uploadImage(fromData)
        .then((res) => {
          this.form.touristAvatar = res.data;
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    authorUpload(file) {
      let fromData = this.initFromData(file);
      uploadImage(fromData)
        .then((res) => {
          this.form.authorAvatar = res.data;
          this.loading = false;
        })
        .catch((err) => {
          this.loading = false;
        });
    },
    uploadBefore() {
      this.loading = true;
    },
    initFromData(param) {
      let file = param.file;
      if (!this.checkUploadFile(file)) {
        this.loading = false;
        return;
      }
      // FormData 对象
      var formData = new FormData();
      // 文件对象
      formData.append("file", file);
      return formData;
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
    submitForm() {
      let form = this.form;
      let logins = "";
      this.loginTypeLists.forEach((item) => {
        if (item !== "") {
          if (logins == null) {
            logins = item;
          } else {
            logins = logins + "," + item;
          }
        }
      });
      let showList = "";
      this.showList.forEach((item) => {
        if (item !== "") {
          if (showList == null) {
            showList = item;
          } else {
            showList = showList + "," + item;
          }
        }
      });
      form.loginTypeList = logins;
      form.showList = showList;
      updateWebConfig(form)
        .then((response) => {
          this.$message.success("修改网站配置成功");
          this.getWebConfigFun();
        })
        .catch((err) => {
          console.error(err);
        });
    },
  },
};
</script>
  
<style scoped>
.uploadImgBody :hover {
  border: dashed 1px #00ccff;
}

img {
  width: 100px;
  height: 100px;
}

.avatar-uploader1 .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader1 .el-upload:hover {
  border-color: #409eff;
}

.avatar-uploader-icon1 {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar1 {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
  