import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listResource(query) {
  return request({
    url: '/system/resource/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getResource(id) {
  return request({
    url: '/system/resource/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addResource(data) {
  return request({
    url: '/system/resource',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateResource(data) {
  return request({
    url: '/system/resource',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delResource(id) {
  return request({
    url: '/system/resource/' + id,
    method: 'delete'
  })
}
