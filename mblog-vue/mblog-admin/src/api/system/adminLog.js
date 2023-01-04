import request from '@/utils/request'

// 查询操作日志列表
export function listAdminLog(query) {
  return request({
    url: '/system/adminLog/list',
    method: 'get',
    params: query
  })
}


// 删除操作日志
export function delAdminLog(ids) {
  return request({
    url: '/system/adminLog/' + ids,
    method: 'delete'
  })
}
