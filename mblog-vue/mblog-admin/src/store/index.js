//引入Vue核心库
import Vue from 'vue'
//引入Vuex
import Vuex from 'vuex'
// 高效的vuex状态数据存储，解决刷新空白
import createPersistedState from 'vuex-persistedstate'
//应用Vuex插件
Vue.use(Vuex)

//准备actions对象——响应组件中用户的动作、处理业务逻辑
const actions = {}
//准备mutations对象——修改state中的数据
const mutations = {
    // 保存菜单栏列表
    saveUserMenus(state, userMenuList) {
        state.userMenuList = userMenuList
    },

}
//准备state对象——保存具体的数据
const state = {
    collapse: false, // 是否折叠菜单
    userMenuList: [], // 菜单栏列表
    userInfo: null // 用户信息
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
