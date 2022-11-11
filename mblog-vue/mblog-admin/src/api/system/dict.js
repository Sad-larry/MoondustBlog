import request from '@/utils/request'

// 查询字典列表
export function listDict(query) {
  return request({
    url: '/system/dict/list',
    method: 'get',
    params: query
  })
}

// 新增字典
export function addDict(data) {
  return request({
    url: '/system/dict',
    method: 'post',
    data: data
  })
}

// 修改字典
export function updateDict(data) {
  return request({
    url: '/system/dict',
    method: 'put',
    data: data
  })
}

// 删除字典
export function delDict(ids) {
  return request({
    url: '/system/dict/' + ids,
    method: 'delete'
  })
}
