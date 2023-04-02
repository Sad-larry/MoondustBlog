import tab from './tab'
import auth from './auth'
import cache from './cache'
import modal from './modal'
import documents from './documents'

export default {
  install(Vue) {
    // 页签操作
    Vue.prototype.$tab = tab
    // 认证对象
    Vue.prototype.$auth = auth
    // 缓存对象
    Vue.prototype.$cache = cache
    // 模态框对象
    Vue.prototype.$modal = modal
    // 文件管理对象
    Vue.prototype.$documents = documents
  }
}
