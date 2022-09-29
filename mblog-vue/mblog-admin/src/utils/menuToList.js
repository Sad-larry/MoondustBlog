import Layout from '@/layout/index.vue'
import router from '@/router'
import store from '@/store'
import Vue from 'vue'

export function organizeMenu(menuList) {
    if (menuList) {
        menuList.forEach((item) => {
            // 设置icon

            // 设置一级菜单
            if (item.component === null) {
                item.component = Layout
            }
            if (item.subMenuList && item.subMenuList.length > 0) {
                item.subMenuList.forEach((route) => {
                    // 设置icon

                    // 设置二级菜单路由
                    route.component = loadView(route.component)
                    // router.addRoute(route)
                })
            }
        })
        // 添加侧边栏菜单
        store.commit('saveUserMenus', menuList)
        // 处理完菜单栏就添加到路由中
        menuList.forEach((item) => {
            router.addRoute(item)
        })
    } else {
        Vue.prototype.$message.error(data.message)
        router.push({ path: '/login' })
    }
}
export const loadView = (view) => {
    // 路由懒加载
    return (resolve) => require([`@/views${view}`], resolve)
}