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
    <!-- 反馈栏 -->
    <FeedBack />
  </v-app>
</template>

<script>
import TopNavBar from "@/components/layout/TopNavBar";
import SideNavBar from "@/components/layout/SideNavBar";
import Footer from "@/components/layout/Footer";
import BackTop from "@/components/BackTop";
import SearchModel from "@/components/model/SearchModel";
import FeedBack from "@/components/FeedBack";
import { visitTheWebsite } from "@/api";
export default {
  components: {
    TopNavBar,
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
  methods: {
    /** 初始化网页信息 */
    getBlogInfo() {
      this.$store.dispatch("blogInfo/initBlogInfo");
    },
  },
};
</script>
