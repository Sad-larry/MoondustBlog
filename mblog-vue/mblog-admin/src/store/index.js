//引入Vue核心库
import Vue from 'vue'
//引入Vuex
import Vuex from 'vuex'
// 高效的vuex状态数据存储，解决刷新空白
import createPersistedState from 'vuex-persistedstate'

//应用Vuex插件
Vue.use(Vuex)

//准备state对象——保存具体的数据
const state = {
    activeProgressEnum: 0, // 用于判断用户是否刷新了，如果刷新了则vuex会回归初始化
    collapse: false, // 是否折叠菜单
    userMenuTree: [], // 菜单栏列表
    userInfo: null, // 用户信息
    tabList: [{ name: '首页', path: '/' }], // 导航栏
}
//准备actions对象——响应组件中用户的动作、处理业务逻辑
const actions = {}
//准备mutations对象——修改state中的数据
const mutations = {
    // 保存菜单栏列表
    saveUserMenus(state, userMenuTree) {
        state.userMenuTree = userMenuTree
    },
    // 标记是否已经登录
    changeActiveProgressEnum(state) {
        state.activeProgressEnum = 1
    },
    // 是否水平折叠收起菜单
    trigger(state) {
        state.collapse = !state.collapse
    },
    // 退出登录
    logout(state) {
        state.activeProgressEnum = 0
        state.userMenuTree = []
    },
    //////////////////
    saveTab(state, tab) {
        if (state.tabList.findIndex((item) => item.path === tab.path) == -1) {
            state.tabList.push({ name: tab.name, path: tab.path })
        }
    },
    removeTab(state, tab) {
        var index = state.tabList.findIndex((item) => item.name === tab.name)
        state.tabList.splice(index, 1)
    },
    resetTab(state) {
        state.tabList = [{ name: '首页', path: '/' }]
    },
    updateAvatar(state, avatar) {
        state.userInfo.avatar = avatar
      },
      updateUserInfo(state, user) {
        state.userInfo.nickname = user.nickname
        state.userInfo.intro = user.intro
        state.userInfo.webSite = user.webSite
      }
    /////////////////
}

//vuex的插件 
const plugins = [
    createPersistedState({
        storage: window.sessionStorage
    })
]

//创建并暴露store
export default new Vuex.Store({
    actions,
    mutations,
    state,
    plugins
})
