<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">友情链接</h1>
    </div>
    <!-- 链接列表 -->
    <v-card class="blog-container">
      <div class="link-title mb-1">
        <v-icon color="blue">mdi-link-variant</v-icon>大佬链接
      </div>
      <v-row class="link-container">
        <v-col class="link-wrapper" md="4" cols="12" v-for="item of friendLinkList" :key="item.url">
          <a :href="item.url" target="_blank">
            <v-avatar size="65" class="link-avatar">
              <img :src="item.avatar" />
            </v-avatar>
            <div style="width:100%;z-index:10;">
              <div class="link-name">{{ item.name }}</div>
              <div class="link-intro">{{ item.info }}</div>
            </div>
          </a>
        </v-col>
      </v-row>
      <!-- 说明 -->
      <div class="link-title mt-4 mb-4">
        <v-icon color="blue">mdi-dots-horizontal-circle</v-icon>添加友链
      </div>
      <blockquote>
        <div>网站名称：{{ blogInfo.webSite.name }}</div>
        <div>网站简介：{{ blogInfo.webSite.summary }}</div>
        <div>网站地址：{{ blogInfo.webSite.webUrl }}</div>
        <div>网站头像：{{ blogInfo.webSite.logo }}</div>
      </blockquote>
      <div class="mt-5 mb-5">
        需要交换友链的可点击
        <a style="color: #409eff" @click="onclick">此处</a>💖
        <br />
        <font style="color: red">注:如果已经申请过友链 再次接入则会进行下架处理 需重新审核</font>
      </div>

      <div>
        <v-dialog title="申请友链" width="50%" transition="dialog-top-transition" :value.sync="dialogFormVisible">
          <v-card>
            <v-card-title>
              <span class="text-h5">申请友链</span>
            </v-card-title>
            <v-card-text>
              <v-container>
                <v-form ref="dataForm" lazy-validation>
                  <v-text-field v-model="link.name" :rules="rules.name" label="网站名称" required clearable></v-text-field>
                  <v-text-field v-model="link.avatar" :rules="rules.avatar" label="网站头像" required clearable></v-text-field>
                  <v-text-field v-model="link.info" :rules="rules.info" label="网站简介" required clearable></v-text-field>
                  <v-text-field v-model="link.url" :rules="rules.url" label="网站地址" required clearable></v-text-field>
                  <v-text-field v-model="link.email" :rules="rules.email" label="联系邮箱" required clearable></v-text-field>
                </v-form>
              </v-container>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="warning" @click="dialogFormVisible = false">取 消</v-btn>
              <v-btn color="success" @click="submit">确 定</v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
      <!--      <blockquote class="mb-10">
              友链信息展示需要，你的信息格式要包含：名称、介绍、链接、头像
      </blockquote>-->
    </v-card>
  </div>
</template>

<script>
import { fetchFriend, addLink } from "@/api";

export default {
  created() {
    this.listFriendLink();
  },
  data() {
    return {
      friendLinkList: [],
      formLabelWidth: "120px",
      img: process.env.VUE_APP_IMG_API,
      commentList: [],
      dialogFormVisible: false,
      count: 0,
      link: {
        url: null,
        name: null,
        avatar: null,
        info: null,
        email: null,
      },
      rules: {
        name: [(v) => !!v || "必填字段"],
        email: [
          (v) => !!v || "必填字段",
          (v) =>
            /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(
              v
            ) || "填写正确的邮箱",
        ],
        info: [(v) => !!v || "必填字段"],
        url: [
          (v) => !!v || "必填字段",
          (v) =>
            /^((https|http|ftp|rtsp|mms){0,1}(:\/\/){0,1})(([A-Za-z0-9-~]+)\.)+([A-Za-z0-9-~\/])+$/.test(
              v
            ) || "填写正确的网址",
        ],
        avatar: [(v) => !!v || "必填字段"],
      },
    };
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    cover() {
      var cover = "";
      this.$store.state.blogInfo.pageList.forEach((item) => {
        if (item.pageLabel === "link") {
          cover = item.pageCover;
        }
      });
      return "background: url(" + cover + ") center center / cover no-repeat";
    },
  },
  methods: {
    onclick() {
      this.link = {};
      this.dialogFormVisible = true;
    },
    listFriendLink() {
      fetchFriend().then((res) => {
        this.friendLinkList = res.records;
      });
    },
    submit() {
      if (this.$refs["dataForm"].validate()) {
        addLink(this.link)
            .then((res) => {
              this.$toast({
                type: "success",
                message: "申请成功,通过申请将邮件告知!!",
              });
              this.dialogFormVisible = false;
            })
            .catch((err) => {
              this.$toast({ type: "error", message: err.message });
            });
      } else {
        this.$toast({ type: "error", message: "存在必填字段未填" });
      }
    },
  },
};
</script>

<style scoped>
blockquote {
  line-height: 2;
  margin: 0;
  font-size: 15px;
  border-left: 0.2rem solid #49b1f5;
  padding: 10px 1rem !important;
  background-color: #ecf7fe;
  border-radius: 4px;
}

.link-title {
  color: #344c67;
  font-size: 21px;
  font-weight: bold;
  line-height: 2;
}

.link-container {
  margin: 10px 10px 0;
}

.link-wrapper {
  position: relative;
  transition: all 0.3s;
  border-radius: 8px;
}

.link-avatar {
  margin-top: 5px;
  margin-left: 10px;
  transition: all 0.5s;
}

@media (max-width: 759px) {
  .link-avatar {
    margin-left: 30px;
  }
}

.link-name {
  text-align: center;
  font-size: 1.25rem;
  font-weight: bold;
  z-index: 1000;
}

.link-intro {
  text-align: center;
  padding: 16px 10px;
  height: 50px;
  font-size: 13px;
  color: #1f2d3d;
  width: 100%;
}

.link-wrapper:hover a {
  color: #fff;
}

.link-wrapper:hover .link-intro {
  color: #fff;
}

.link-wrapper:hover .link-avatar {
  transform: rotate(360deg);
}

.link-wrapper a {
  color: #333;
  text-decoration: none;
  display: flex;
  height: 100%;
  width: 100%;
}

.link-wrapper:hover {
  box-shadow: 0 2px 20px #49b1f5;
}

.link-wrapper:hover:before {
  transform: scale(1);
}

.link-wrapper:before {
  position: absolute;
  border-radius: 8px;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background: #49b1f5 !important;
  content: "";
  transition-timing-function: ease-out;
  transition-duration: 0.3s;
  transition-property: transform;
  transform: scale(0);
}
</style>
