import { getWebSiteInfo } from '@/api'

const defaulAuthortAvatar = require("@/assets/images/authorAvatar.jpg");
const defaulTouristAvatar = require("@/assets/images/touristAvatar.jpg");

// 博客网站信息
const blogInfo = {
    state: {
        // 网站配置
        webSite: {
            // 博主
            author: null,
            // 博主头像，设置默认头像
            authorAvatar: defaulAuthortAvatar,
            // 博主介绍
            authorInfo: null,
            // 游客头像
            touristAvatar: defaulTouristAvatar,

            // TODO 暂时不知道干啥的
            showList: ''
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
                pageCover: 'https://niu.moonzs.work/2022/11/26/eccf3e0f88394c30ab0ac926fc0bd2d0.jpg',
                pageLabel: 'home'
            }
        ]
    },
    mutations: {
        SET_WEBSITE(state, webSite) {
            state.webSite = webSite
        },
        SET_COUNT(state, count) {
            state.count = count
        },
        SET_PAGELIST(state, pageList) {
            state.pageList = pageList
        }
    },
    actions: {
        // 初始化博客网站配置等信息，
        initBlogInfo({ commit }) {
            return new Promise((resolve, reject) => {
                getWebSiteInfo().then(res => {
                    commit('SET_WEBSITE', res.webSite)
                    commit('SET_COUNT', res.count)
                    commit('SET_PAGELIST', res.pageList)
                    resolve()
                }).catch(error => {
                    reject(error)
                })
            })
        }
    }
}
export default blogInfo;