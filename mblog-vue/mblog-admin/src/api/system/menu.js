import request from '@/utils/request'

// 查询权限资源 列表
export function listMenu(query) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

// 新增权限资源 
export function addMenu(data) {
  return request({
    url: '/system/menu',
    method: 'post',
    data: data
  })
}

// 修改权限资源 
export function updateMenu(data) {
  return request({
    url: '/system/menu',
    method: 'put',
    data: data
  })
}

// 删除权限资源 
export function delMenu(id) {
  return request({
    url: '/system/menu/' + id,
    method: 'delete'
  })
}
