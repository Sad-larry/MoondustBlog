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
    drawer: false,
    avatar: null,
    nickname: null,
    dialogFormVisible: false,
    articleLikeSet: [],
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
