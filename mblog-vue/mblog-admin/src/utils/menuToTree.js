import Layout from '@/layout/Index.vue'
import router from '@/router'
import store from '@/store'
import Vue from 'vue'

export function organizeMenu(menuData) {
  let menuTree = null
  if (menuData) {
    // 将传过来的数赋值，深拷贝，如果不进行深拷贝，那么就会修改原来的数据，我被坑了很久
    // 因为我修改的component变成了函数，解析出来之后就成了undifined
    menuTree = JSON.parse(JSON.stringify(menuData))
    // 重新添加到菜单添加侧边栏菜单
    store.commit('saveUserMenus', menuData)
    // 将activeProgressEnum改变值，代表没有刷新
    store.commit('changeActiveProgressEnum')
  } else {
    // 如果没有值应该从store中获取
    let uMenuTree = store.state.userMenuTree
    if (uMenuTree && uMenuTree.length > 0) {
      // 说明userMenuTree有值，store保存了菜单数据
      menuTree = JSON.parse(JSON.stringify(uMenuTree))
    } else {
      // 即没传数据，有没有保存数据，直接返回
      Vue.prototype.$message.error("登录失效")
      router.push({ path: '/login' })
      return
    }
  }
  // 这里是动态添加路由地方
  menuTree.forEach((item) => {
    // 设置icon

    // 设置一级菜单
    if (item.component === 'Layout') {
      item.component = Layout
      // item.component = () => import('@/layout/Index.vue')
    }
    if (item.children && item.children.length > 0) {
      item.children.forEach((route) => {
        // 设置icon

        // 设置二级菜单路由
        route.component = loadView(route.component)
      })
    }
  })
  // 处理完菜单栏就添加到路由中
  menuTree.forEach((item) => {
    router.addRoute(item)
  })
  router.addRoute({
    path: '*',
    component: () => import('@/views/features/404.vue')
  })
}

export const loadView = (view) => {
  // 路由懒加载
  return (resolve) => require([`@/views${view}`], resolve)
}