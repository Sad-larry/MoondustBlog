import request from '@/utils/request'

// 查询字典数据列表
export function listDictData(query) {
  return request({
    url: '/system/dict/data/list',
    method: 'get',
    params: query
  })
}


// 新增字典数据
export function addDictData(data) {
  return request({
    url: '/system/dict/data',
    method: 'post',
    data: data
  })
}

// 修改字典数据
export function updateDictData(data) {
  return request({
    url: '/system/dict/data',
    method: 'put',
    data: data
  })
}

// 删除字典数据
export function delDictData(id) {
  return request({
    url: '/system/dict/data/' + id,
    method: 'delete'
  })
}

// 根据字典类型获取字典数据
export function getDataByDictType(type) {
  return request({
    url: '/system/dict/data/getByType',
    method: 'post',
    data: type
  })
}