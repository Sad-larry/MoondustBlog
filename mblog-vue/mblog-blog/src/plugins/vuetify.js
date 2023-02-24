import Vue from "vue";
import Vuetify from "vuetify/lib";
import '@mdi/font/css/materialdesignicons.css' 

Vue.use(Vuetify);

const opts = {
  icons: {
    // 更改你的字体库，需要在实例化过程中添加 iconfont 选项，以致能显示 radio
    iconfont: 'mdi', // 'mdi' || 'mdiSvg' || 'md' || 'fa' || 'fa4' || 'faSvg'
  },
};

export default new Vuetify(opts);
