<template>
  <v-app id="app">
    <!-- 导航栏 -->
    <TopNavBar></TopNavBar>
    <!-- 侧边导航栏 -->
    <SideNavBar></SideNavBar>
    <!-- 内容 -->
    <v-main>
      <router-view :key="$route.fullPath" />
    </v-main>
    <!-- 页脚 -->
    <Footer></Footer>
    <!-- 返回顶部 -->
    <BackTop></BackTop>
    <!-- 搜索模态框 -->
    <SearchModel></SearchModel>
    <!-- 音乐播放器 -->
    <!-- <Player v-if="blogInfo.webSite.isMusicPlayer === 1 && !isMobile" /> -->
    <FeedBack />
  </v-app>
</template>

<script>
import TopNavBar from "@/components/layout/TopNavBar";
import SideNavBar from "@/components/layout/SideNavBar";
import Footer from "@/components/layout/Footer";
import BackTop from "@/components/BackTop";
import SearchModel from "@/components/model/SearchModel";
import Player from "@/components/zw-player/player.vue";
import FeedBack from "@/components/FeedBack";
import { visitTheWebsite } from "@/api";
export default {
  components: {
    TopNavBar,
    Player,
    SideNavBar,
    Footer,
    BackTop,
    SearchModel,
    FeedBack,
  },
  mounted() {
    this.getBlogInfo();
    // 上传访客信息
    visitTheWebsite();
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    },
    isMobile() {
      const flag = navigator.userAgent.match(
        /(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i
      );
      return flag;
    },
  },
  methods: {
    /** 初始化网页信息 */
    getBlogInfo() {
      this.$store.dispatch("blogInfo/initBlogInfo");
    },
  },
};
</script>
