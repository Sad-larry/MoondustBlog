import request from '@/utils/request'

// 查询角色列表
export function listRole(query) {
  return request({
    url: '/system/role/list',
    method: 'get',
    params: query
  })
}

// 查询角色详细
export function getRole(id) {
  return request({
    url: '/system/role/' + id,
    method: 'get'
  })
}

// 新增角色
export function addRole(data) {
  return request({
    url: '/system/role',
    method: 'post',
    data: data
  })
}

// 修改角色
export function updateRole(data) {
  return request({
    url: '/system/role',
    method: 'put',
    data: data
  })
}

// 删除角色
export function delRole(ids) {
  return request({
    url: '/system/role/' + ids,
    method: 'delete'
  })
}

// 获取角色权限列表
export function getRolePerms(id) {
  return request({
    url: '/system/role/rolePerms',
    method: 'get',
    params: { roleId: id }
  })
}

// 获取所有权限列表
export function getAllPerms() {
  return request({
    url: '/system/role/allPerms',
    method: 'get',
  })
}

// 更新角色权限列表
export function updateRolePerms(id, perms) {
  return request({
    url: '/system/role/updatePerms',
    method: 'post',
    data: {
      id: id,
      perms: perms
    },
  })
}