import { getBlogInfo } from '@/api'

const defaulAuthortAvatar = require("@/assets/images/authorAvatar.jpg");
const defaulTouristAvatar = require("@/assets/images/touristAvatar.jpg");

const state = {
    // 网站配置
    webSite: {
        // logo
        logo: null,
        // 网站名称
        name: null,
        // 网站介绍
        summary: null,
        // 关键字
        keyword: null,
        // 公告
        bulletin: null,
        // 备案号
        recordNum: null,
        // 网站地址
        webUrl: null,

        // 博主
        author: null,
        // 博主头像，设置默认头像
        authorAvatar: defaulAuthortAvatar,
        // 博主介绍
        authorInfo: null,
        // 游客头像
        touristAvatar: defaulTouristAvatar,

        // 联系方式列表
        showList: '',
        // 登录方式列表
        loginTypeList: '',
        // 支付宝收款码
        aliPay: null,
        // 微信收款码
        weixinPay: null,
        // github地址
        github: null,
        // gitee地址
        gitee: null,
        // QQ号
        qqNumber: null,
        // 邮箱
        email: null,
        // 关于我
        aboutMe: null,
    },
    // 博客统计数
    count: {
        // 文章数
        articleCount: 0,
        // 分类数
        categoryCount: 0,
        // 标签数
        tagCount: 0,
        // 浏览量
        viewsCount: 0
    },
    // 每个网页标签的图片
    pageList: [
        {
            pageCover: '',
            pageLabel: ''
        }
    ]
}
const mutations = {
    SET_WEBSITE(state, webSite) {
        state.webSite = webSite
    },
    SET_COUNT(state, count) {
        state.count = count
    },
    SET_PAGELIST(state, pageList) {
        state.pageList = pageList
    }
}
const actions = {
    // 初始化博客网站配置等信息，
    initBlogInfo({ commit }) {
        return new Promise((resolve, reject) => {
            getBlogInfo().then(res => {
                commit('SET_WEBSITE', res.data.webSite)
                commit('SET_COUNT', res.data.count)
                commit('SET_PAGELIST', res.data.pageList)
                resolve()
            }).catch(error => {
                reject(error)
            })
        })
    }
}
export default {
    namespaced: true,
    state,
    mutations,
    actions
};