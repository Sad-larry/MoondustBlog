import request from '@/utils/request'

// 查询用户信息列表
export function listAuth(query) {
  return request({
    url: '/system/auth/list',
    method: 'get',
    params: query
  })
}

// 查询用户信息详细
export function getAuth(id) {
  return request({
    url: '/system/auth/' + id,
    method: 'get'
  })
}

// 新增用户信息
export function addAuth(data) {
  return request({
    url: '/system/auth',
    method: 'post',
    data: data
  })
}

// 修改用户信息
export function updateAuth(data) {
  return request({
    url: '/system/auth',
    method: 'put',
    data: data
  })
}

// 删除用户信息
export function delAuth(id) {
  return request({
    url: '/system/auth/' + id,
    method: 'delete'
  })
}
