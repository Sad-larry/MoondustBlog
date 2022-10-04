import request from '@/api/request'

// 发布文章
export function publishArticle(data) {
    return request({
        url: '/admin/article',
        method: 'POST',
        data
    })
}
// 查询文章列表
export function listArticles(params) {
    return request({
        url: '/admin/article/list',
        method: 'GET',
        params
    })
}
// 更新文章
export function updateArticle(data) {
    return request({
        url: '/admin/article',
        method: 'PUT',
        data
    })
}
// 更新文章分类
export function updateArticleCategory(data) {
    return request({
        url: '/admin/article/updateCategory',
        method: 'PUT',
        data
    })
}
// 更新文章标签
export function updateArticleTags(data) {
    return request({
        url: '/admin/article/updateTags',
        method: 'PUT',
        data
    })
}
// 删除文章
export function deleteArticle(id) {
    return request({
        url: '/admin/article/' + id,
        method: 'DELETE'
    })
}
export default { publishArticle, listArticles, updateArticle, updateArticleCategory, updateArticleTags, deleteArticle }