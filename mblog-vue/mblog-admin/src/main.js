import Vue from 'vue'
import App from './App'
// Element-UI组件，全部导入哈哈哈，不想分
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 路由配置
import router from './router'
// vuex配置
import store from './store'

// 阻止vue在启动时生成生产提示
Vue.config.productionTip = false

Vue.use(ElementUI);

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
