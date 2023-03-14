import Vue from "vue";
import Vuex from "vuex";
import blogInfo from "./modules/blogInfo";
import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

export default new Vuex.Store({
  modules: {
    blogInfo
  },
  state: {
    searchFlag: false,
    loginFlag: false,
    registerFlag: false,
    forgetFlag: false,
    drawer: false,
    loginUrl: "",
    userId: null,
    avatar: null,
    nickname: null,
    intro: null,
    webSite: null,
    loginType: null,
    token: null,
    dialogFormVisible: false,
    articleLikeSet: [],
    commentLikeSet: []
  },
  mutations: {
    articleLike(state, articleId) {
      var articleLikeSet = state.articleLikeSet;
      if (articleLikeSet.indexOf(articleId) != -1) {
        articleLikeSet.splice(articleLikeSet.indexOf(articleId), 1);
      } else {
        articleLikeSet.push(articleId);
      }
    },
    setDialogFormVisible(state) {
      state.dialogFormVisible = !state.dialogFormVisible;
    }
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
});
