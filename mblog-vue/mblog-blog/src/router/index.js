import Vue from 'vue'
import VueRouter from 'vue-router'
import NProgress from "nprogress";

Vue.use(VueRouter)

const routes = [
  {
    path: "/",
    component: resolve => require(["../views/home/Home.vue"], resolve)
  },
  {
    path: "/article/:articleId",
    component: resolve => require(["../views/article/Article.vue"], resolve)
  },
  {
    path: "/archives",
    component: resolve => require(["../views/archive/Archive.vue"], resolve),
    meta: {
      title: "归档"
    }
  },
  {
    path: "/tags",
    component: resolve => require(["../views/tag/Tag.vue"], resolve),
    meta: {
      title: "标签"
    }
  },
  {
    path: "/categories",
    component: resolve => require(["../views/category/Category.vue"], resolve),
    meta: {
      title: "分类"
    }
  },
  {
    path: "/categories/:categoryId",
    component: resolve => require(["../views/article/ArticleList.vue"], resolve)
  },
  {
    path: "/tags/:tagId",
    component: resolve => require(["../views/article/ArticleList.vue"], resolve)
  },
  {
    path: "/links",
    component: resolve => require(["../views/link/Link.vue"], resolve),
    meta: {
      title: "友链列表"
    }
  },
  {
    path: "/about",
    component: resolve => require(["../views/about/About.vue"], resolve),
    meta: {
      title: "关于我"
    }
  },
  {
    path: "/message",
    component: resolve => require(["../views/message/Message.vue"], resolve),
    meta: {
      title: "留言板"
    }
  },
  {
    path: '*',
    redirect: '/',
    hidden: true
  }
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  if (to.path) {
    if (window._hmt) {
      window._hmt.push(['_trackPageview', to.fullPath]);
    }
  }
  NProgress.start();
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  next();
});

router.afterEach(() => {
  window.scrollTo({
    top: 0,
    behavior: "instant"
  });
  NProgress.done();
});

export default router
