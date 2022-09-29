// 1.导入Vue和VueRouter，并调用 Vue.use(VueRouter)
import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 2.定义路由
const routes = [
    {
        path: '/login',
        name: '登录', // 命名路由标识
        hidden: true, // 当hidden为true时则不会显示在sidebar，默认为fasle
        component: () => import('../views/Login.vue') // 当前路由映射一个Login组件
    }
]

// 3.创建 router 实例，然后传 `routes` 配置
const createRouter = () =>
    new VueRouter({
        mode: 'history',
        routes: routes
    })

// 4.暴露出去并在Vue示例中挂载路由
const router = createRouter()

// 替换老routers，这里相当于重新回到登录界面
export function resetRouter() {
    const newRouter = createRouter()
    router.matcher = newRouter.matcher
}

import store from '@/store'
// 配置路由守卫
router.beforeEach((to, from, next) => {
    // 按道理应该是利用Token判断的，这里我暂时用菜单栏列表判断
    // console.log(store.state.userMenuList)
    // if (store.state.userMenuList) {
    //     if (to.path === '/login') {
    //         next({path: '/'})
    //     } else {
    //         next()
    //     }
    // } else {
    //     next({path: '/login'})
    // }
    next()
})

export default router