import Vue from 'vue'
import App from './App'
// Element-UI组件，全部导入哈哈哈，不想分
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
// 路由配置
import router from './router'
// vuex配置
import store from './store'
// vcharts
import VCharts from 'v-charts'
// 富文本编辑器
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'



// 阻止vue在启动时生成生产提示
Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(VCharts)
Vue.use(mavonEditor)

new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
