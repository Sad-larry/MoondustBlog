import request from '@/utils/request'

// 查询博客文章列表
export function listArticle(query) {
  return request({
    url: '/system/article/list',
    method: 'get',
    params: query
  })
}

// 查询博客文章详细
export function getArticle(id) {
  return request({
    url: '/system/article/' + id,
    method: 'get'
  })
}

// 新增博客文章
export function addArticle(data) {
  return request({
    url: '/system/article',
    method: 'post',
    data: data
  })
}

// 修改博客文章
export function updateArticle(data) {
  return request({
    url: '/system/article',
    method: 'put',
    data: data
  })
}

// 删除博客文章
export function delArticle(ids) {
  return request({
    url: '/system/article/' + ids,
    method: 'delete'
  })
}

// 更新文章分类
export function updateArticleCategory(params) {
  return request({
    url: '/system/article/updateCategory',
    method: 'post',
    data: params
  })
}

// 更新标签列表
export function updateArticleTags(params) {
  return request({
    url: '/system/article/updateTags',
    method: 'post',
    data: params
  })
}

// 置顶文章
export function topArticle(data) {
  return request({
    url: '/system/article/top',
    method: 'post',
    data: data
  })
}