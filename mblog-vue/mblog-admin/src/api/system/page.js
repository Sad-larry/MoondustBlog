import request from '@/utils/request'

// 查询前端页面列表
export function listPage(query) {
  return request({
    url: '/system/page/list',
    method: 'get',
    params: query
  })
}

// 查询前端页面详细
export function getPage(id) {
  return request({
    url: '/system/page/' + id,
    method: 'get'
  })
}

// 新增前端页面
export function addPage(data) {
  return request({
    url: '/system/page',
    method: 'post',
    data: data
  })
}

// 修改前端页面
export function updatePage(data) {
  return request({
    url: '/system/page',
    method: 'put',
    data: data
  })
}

// 删除前端页面
export function delPage(id) {
  return request({
    url: '/system/page/' + id,
    method: 'delete'
  })
}
