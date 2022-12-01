import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
// 导入vuetify
import vuetify from "@/plugins/vuetify";
import animated from "animate.css";
import "./assets/css/index.css";
import "./assets/css/iconfont.css";
import "./assets/css/markdown.css";
import "./assets/css/vue-social-share/client.css";
import config from "./assets/js/config";
import Share from "vue-social-share";
import dayjs from "dayjs";
import { vueBaberrage } from "vue-baberrage";
import axios from "axios";
import VueAxios from "vue-axios";
import InfiniteLoading from "vue-infinite-loading";
import "highlight.js/styles/atom-one-dark.css";
import VueImageSwipe from "vue-image-swipe";
import "vue-image-swipe/dist/vue-image-swipe.css";
import Toast from "./components/toast/index";
import "nprogress/nprogress.css";
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import MetaInfo from 'vue-meta-info'
Vue.use(MetaInfo)
Vue.prototype.config = config;
Vue.config.productionTip = false;
Vue.use(ElementUI);
Vue.use(animated);
Vue.use(Share);
Vue.use(vueBaberrage);
Vue.use(InfiniteLoading);
Vue.use(VueAxios, axios);
Vue.use(VueImageSwipe);
Vue.use(Toast);

Vue.filter("date", function (value) {
  return dayjs(value).format("YYYY-MM-DD");
});

Vue.filter("year", function (value) {
  return dayjs(value).format("YYYY");
});

Vue.filter("hour", function (value) {
  return dayjs(value).format("HH:mm:ss");
});

Vue.filter("num", function (value) {
  if (value >= 1000) {
    return (value / 1000).toFixed(1) + "k";
  }
  return value;
});

new Vue({
  router,
  store,
  vuetify,
  render: h => h(App)
}).$mount("#app");
