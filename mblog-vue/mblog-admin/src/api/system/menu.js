import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listMenu(query) {
  return request({
    url: '/system/menu/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getMenu(id) {
  return request({
    url: '/system/menu/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addMenu(data) {
  return request({
    url: '/system/menu',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateMenu(data) {
  return request({
    url: '/system/menu',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delMenu(id) {
  return request({
    url: '/system/menu/' + id,
    method: 'delete'
  })
}
