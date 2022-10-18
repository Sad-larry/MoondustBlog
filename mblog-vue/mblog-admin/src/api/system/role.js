import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listRole(query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getRole(id) {
  return request({
    url: '/system/role/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addRole(data) {
  return request({
    url: '/system/role',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateRole(data) {
  return request({
    url: '/system/role',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delRole(id) {
  return request({
    url: '/system/role/' + id,
    method: 'delete'
  })
}
