import request from '@/utils/request'

// 查询异常日志列表
export function listExceptionLog(query) {
  return request({
    url: '/system/exceptionLog/list',
    method: 'get',
    params: query
  })
}

// 删除异常日志
export function delExceptionLog(ids) {
  return request({
    url: '/system/exceptionLog/' + ids,
    method: 'delete'
  })
}
