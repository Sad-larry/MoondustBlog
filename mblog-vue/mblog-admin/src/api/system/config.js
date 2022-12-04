import request from '@/utils/request'

// 查询系统配置列表
export function listConfig(query) {
  return request({
    url: '/system/config/list',
    method: 'get',
    params: query
  })
}

// 查询系统配置详细
export function getConfig(id) {
  return request({
    url: '/system/config/' + id,
    method: 'get'
  })
}

// 查询指定配置的配置值
export function getConfigKey(configKey) {
  return request({
    url: '/system/config/configKey/' + configKey,
    method: 'get'
  })
}

// 新增系统配置
export function addConfig(data) {
  return request({
    url: '/system/config',
    method: 'post',
    data: data
  })
}

// 修改系统配置
export function updateConfig(data) {
  return request({
    url: '/system/config',
    method: 'put',
    data: data
  })
}

// 删除系统配置
export function delConfig(ids) {
  return request({
    url: '/system/config/' + ids,
    method: 'delete'
  })
}

// 刷新配置缓存
export function refreshCache() {
  return request({
    url: '/system/config/refreshCache',
    method: 'get'
  })
}