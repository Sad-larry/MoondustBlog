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
export function searchArticle(keywords) {
    return request({
        url: '/web/article/searchArticle',
        method: 'get',
        params: {
            keywords:keywords
        }
    })
}

export function like(id) {
    return request({
        url: '/web/article/articleLike',
        method: 'get',
        params: {articleId:id}
    })
}

// 归档接口
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

//友链页面请求接口
export function addLink(data) {
    return request({
        url: '/web/friend/add',
        method: 'POST',
        data
    })
}
export function fetchFriend() {
    return request({
        url: '/web/friend/list',
        method: 'post',
        params: {}
    })
}

//留言接口
export function addMessage(data) {
    return request({
        url: '/web/message/add',
        method: 'post',
        data
    })
}

export function listMessage() {
    return request({
        url: '/web/message/webMessage',
        method: 'get',
        params: {}
    })
}
//添加评论
export function addComment(data) {
    return request({
        url: '/web/comment/addComment',
        method: 'post',
        data
    })
}
//评论列表
export function fetchComments(params) {
    return request({
          url: '/web/comment/comments',
          method: 'get',
          params: params
      })
}
//查询回复评论
export function repliesByComId(params) {
    return request({
          url: '/web/comment/repliesByComId',
          method: 'get',
          params: params
      })
}

//qq登录
export function qqLogin(data) {
    return request({
        url: '/user/login',
        method: 'post',
        data
    })
}
//qq退出
export function logout() {
    return request({
        url: '/logout',
        method: 'get',
    })
}
//gitee登录
export function gitEELogin(code) {
    return request({
        url: '/user/gitEELogin',
        method: 'get',
        params: {
            code:code
        }
    })
}
//微博登录
export function weiboLogin(code) {
    return request({
        url: '/user/weiboLogin',
        method: 'get',
        params: {
            code:code
        }
    })
}
//添加访客信息
export function report() {
    return request({
        url: '/web/home/report',
        method: 'get',
        params: {}
    })
}
//相册接口
export function getAlbum() {
    return request({
        url: '/web/album/list',
        method: 'get',
        params: {}
    })
}
export function getPhotos(params) {
    return request({
        url: '/web/album/listPhotos',
        method: 'get',
        params: params
    })
}
export function addFeedback(data) {
    return request({
        url: '/web/feedback/add',
        method: 'post',
        data
    })
}
export function sendEmailCode(email) {
    return request({
        url: '/user/sendEmailCode',
        method: 'get',
        params:{
            email:email
        }
    })
}
export function bindEmail(data) {
    return request({
        url: '/user/bindEmail',
        method: 'post',
        data
    })
}
export function emailRegister(data) {
    return request({
        url: '/user/emailRegister',
        method: 'post',
        data
    })
}
export function emailLogin(data) {
    return request({
        url: '/user/emailLogin',
        method: 'post',
        data
    })
}
export function updatePassword(data) {
    return request({
        url: '/user/updatePassword',
        method: 'post',
        data
    })
}
export function upload(data) {
    return request({
        url: '/file/upload',
        method: 'POST',
        headers:{'Content-Type': 'multipart/articles-data'},
        data
    })
}
export function updateUser(data) {
    return request({
        url: '/user/updateUser',
        method: 'post',
        data
    })
}

