import Vue from "vue";
import Vuex from "vuex";
import getters from './getters';
import blogInfo from "./modules/blogInfo";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  getters,
  modules: {
    blogInfo
  },
  state: {
    searchFlag: false,
    loginFlag: false,
    registerFlag: false,
    forgetFlag: false,
    emailFlag: false,
    drawer: false,
    loginUrl: "",
    userId: null,
    avatar: null,
    nickname: null,
    intro: null,
    webSite: null,
    loginType: null,
    email: null,
    token: null,
    dialogFormVisible: false,
    articleLikeSet: [],
    commentLikeSet: [],
    // blogInfo: {
    //   webSite: {
    //     authorAvatar: 'https://niu.moonzs.work/2022/11/26/033b678cc9084976bb14f79dc880bde7.jpg',
    //     authorInfo: null,
    //     touristAvatar: null,
    //     loginTypeList: "",
    //     bulletin: null,
    //     aboutMe: null,
    //     logo: null,
    //     github: null,
    //     gitee: null,
    //     qqNumber: null,
    //     recordNum: null,
    //     author: null,
    //     aliPay: null,
    //     weixinPay: null,
    //     keyword: null,
    //     name: null,
    //     summary: null,
    //     webUrl: null,
    //     showList: ""
    //   },
    //   count: {
    //     articleCount: null,
    //     categoryCount: null,
    //     tagCount: null,
    //     viewsCount: null
    //   },
    //   pageList: [
    //     {
    //       pageCover: 'https://niu.moonzs.work/2022/11/26/eccf3e0f88394c30ab0ac926fc0bd2d0.jpg',
    //       pageLabel: 'home'
    //     }
    //   ]
    // }
  },
  mutations: {
    login(state, user) {
      state.userId = user.id;
      state.avatar = user.avatar;
      state.nickname = user.nickname;
      state.intro = user.intro;
      state.webSite = user.webSite;
      state.articleLikeSet = user.articleLikeSet ? user.articleLikeSet : [];
      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : [];
      state.email = user.email;
      state.token = user.token;
      state.loginType = user.loginType;
    },
    logout(state) {
      state.userId = null;
      state.avatar = null;
      state.nickname = null;
      state.intro = null;
      state.token = null;
      state.webSite = null;
      state.articleLikeSet = [];
      state.commentLikeSet = [];
      state.email = null;
      state.loginType = null;
    },
    saveLoginUrl(state, url) {
      state.loginUrl = url;
    },
    saveEmail(state, email) {
      state.email = email;
    },
    updateUserInfo(state, user) {
      state.nickname = user.nickname;
      state.intro = user.intro;
      state.webSite = user.webSite;
    },
    savePageInfo(state, pageList) {
      state.pageList = pageList;
    },
    updateAvatar(state, avatar) {
      state.avatar = avatar;
    },
    checkBlogInfo(state, blogInfo) {
      state.blogInfo = blogInfo;
    },
    closeModel(state) {
      state.registerFlag = false;
      state.loginFlag = false;
      state.searchFlag = false;
      state.emailFlag = false;
    },
    articleLike(state, articleId) {
      var articleLikeSet = state.articleLikeSet;
      if (articleLikeSet.indexOf(articleId) != -1) {
        articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1);
      } else {
        articleLikeSet.push(articleId);
      }
    },
    commentLike(state, commentId) {
      var commentLikeSet = state.commentLikeSet;
      if (commentLikeSet.indexOf(commentId) != -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1);
      } else {
        commentLikeSet.push(commentId);
      }
    },
    setDialogFormVisible(state) {
      state.dialogFormVisible = !state.dialogFormVisible;
    }
  },
  actions: {},
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
});
