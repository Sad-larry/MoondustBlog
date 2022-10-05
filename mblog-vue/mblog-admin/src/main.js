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
// 配置文件
import config from './utils/config'
// 时间处理文件
import dayjs from 'dayjs'



// 阻止vue在启动时生成生产提示
Vue.config.productionTip = false
Vue.prototype.config = config

Vue.use(ElementUI)
Vue.use(VCharts)
Vue.use(mavonEditor)

Vue.filter('date', function (value, formatStr = 'YYYY-MM-DD') {
  return dayjs(value).format(formatStr)
})

Vue.filter('dateTime', function (value, formatStr = 'YYYY-MM-DD HH:mm:ss') {
  return dayjs(value).format(formatStr)
})


new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
