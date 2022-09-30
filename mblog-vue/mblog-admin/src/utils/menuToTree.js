import Layout from '@/layout/Index.vue'
import router from '@/router'
import store from '@/store'
import Vue from 'vue'

export function organizeMenu(menuTree) {
  if (menuTree) {
    menuTree.forEach((item) => {
      // 设置icon

      // 设置一级菜单
      if (item.component === 'Layout') {
        item.component = Layout
      }
      if (item.children && item.children.length > 0) {
        item.children.forEach((route) => {
          // 设置icon

          // 设置二级菜单路由
          route.component = loadView(route.component)
        })
      }
    })
    // 添加侧边栏菜单
    store.commit('saveUserMenus', menuTree)
    // 处理完菜单栏就添加到路由中
    menuTree.forEach((item) => {
      router.options.routes.push(item);
      router.addRoute(item)
    })
    console.log(router.options.routes)
    console.log(router)

  } else {
    Vue.prototype.$message.error(data.message)
    router.push({path: '/login'})
  }
}

export const loadView = (view) => {
  // 路由懒加载
  return (resolve) => require([`@/views${view}`], resolve)
}
