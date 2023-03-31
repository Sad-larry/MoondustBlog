import request from '@/utils/request'

// 获取网站所有信息
export function getBlogInfo() {
    return request({
        url: '/web/home/blogInfo',
        method: 'get'
    })
}

// 文章列表接口
export function fetchList(params) {
    return request({
        url: '/web/article/list',
        method: 'get',
        params: params
    })
}

// 通过分类或标签id获取文章列表
export function getArticlesByQuaryId(params) {
    return request({
        url: '/web/article/condition',
        method: 'get',
        params: params
    })
}

// 获取文章详细信息
export function getArticleInfo(params) {
    return request({
        url: '/web/article/info',
        method: 'get',
        params: params
    })
}

// 搜索文章
export function searchArticle(keywords) {
    return request({
        url: '/web/article/search',
        method: 'get',
        params: {
            keywords: keywords
        }
    })
}

// 查询归档接口
export function getArchives(params) {
    return request({
        url: '/web/article/archive',
        method: 'get',
        params: params
    })
}

// 分类接口
export function getCategory() {
    return request({
        url: '/web/category/list',
        method: 'get',
    })
}

// 标签接口
export function getTags() {
    return request({
        url: '/web/tag/list',
        method: 'get',
    })
}

// 留言接口
export function addMessage(data) {
    return request({
        url: '/web/message/add',
        method: 'post',
        data
    })
}

// 留言弹幕列表
export function listMessage() {
    return request({
        url: '/web/message/list',
        method: 'get',
    })
}

// 友链页面请求接口
export function addLink(data) {
    return request({
        url: '/web/friendlink/add',
        method: 'post',
        data
    })
}

// 友链列表
export function fetchFriend() {
    return request({
        url: '/web/friendlink/list',
        method: 'get',
        params: {}
    })
}

//评论列表
export function listArticleComment(params) {
    return request({
        url: '/web/comment/list',
        method: 'get',
        params: params
    })
}

// 添加访客信息
export function visitTheWebsite() {
    return request({
        url: '/web/home/welcome',
        method: 'get',
    })
}

// 添加反馈
export function addFeedback(data) {
    return request({
        url: '/web/feedback/add',
        method: 'post',
        data
    })
}
