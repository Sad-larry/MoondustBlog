//引入Vue核心库
import Vue from 'vue'
//引入Vuex
import Vuex from 'vuex'
//应用Vuex插件
Vue.use(Vuex)
   
//准备actions对象——响应组件中用户的动作、处理业务逻辑
const actions = {}
//准备mutations对象——修改state中的数据
const mutations = {}
//准备state对象——保存具体的数据
const state = {
    collapse: false, // 是否折叠菜单
    userMenuList: [], // 菜单栏列表
    userInfo: null
}
   
//创建并暴露store
export default new Vuex.Store({
   	actions,
   	mutations,
   	state
})
