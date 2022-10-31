import request from '@/utils/request'

// 查询用户反馈列表
export function listBack(query) {
  return request({
    url: '/system/back/list',
    method: 'get',
    params: query
  })
}

// 查询用户反馈详细
export function getBack(id) {
  return request({
    url: '/system/back/' + id,
    method: 'get'
  })
}

// 新增用户反馈
export function addBack(data) {
  return request({
    url: '/system/back',
    method: 'post',
    data: data
  })
}

// 修改用户反馈
export function updateBack(data) {
  return request({
    url: '/system/back',
    method: 'put',
    data: data
  })
}

// 删除用户反馈
export function delBack(id) {
  return request({
    url: '/system/back/' + id,
    method: 'delete'
  })
}
