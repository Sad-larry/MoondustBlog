import request from '@/utils/request'

// 查询前端页面列表
export function listWebPage(query) {
  return request({
    url: '/system/webPage/list',
    method: 'get',
    params: query
  })
}

// 新增前端页面
export function addWebPage(data) {
  return request({
    url: '/system/webPage',
    method: 'post',
    data: data
  })
}

// 修改前端页面
export function updateWebPage(data) {
  return request({
    url: '/system/webPage',
    method: 'put',
    data: data
  })
}

// 删除前端页面
export function delWebPage(id) {
  return request({
    url: '/system/webPage/' + id,
    method: 'delete'
  })
}
