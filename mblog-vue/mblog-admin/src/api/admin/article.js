import request from '@/api/request'

// 查询【请填写功能名称】列表
export function listArticle(query) {
    return request({
        url: '/system/article/list',
        method: 'get',
        params: query
    })
}

// 查询【请填写功能名称】详细
export function getArticle(id) {
    return request({
        url: '/system/article/' + id,
        method: 'get'
    })
}

// 新增【请填写功能名称】
export function addArticle(data) {
    return request({
        url: '/system/article',
        method: 'post',
        data: data
    })
}


// 修改【请填写功能名称】
export function updateArticle(data) {
    return request({
        url: '/system/article',
        method: 'put',
        data: data
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

// 删除【请填写功能名称】
export function delArticle(id) {
    return request({
        url: '/system/article/' + id,
        method: 'delete'
    })
}

