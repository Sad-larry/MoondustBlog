import request from '@/utils/request'

// 查询用户日志列表
export function listUserLog(query) {
  return request({
    url: '/system/userLog/list',
    method: 'get',
    params: query
  })
}

// 删除用户日志
export function delUserLog(ids) {
  return request({
    url: '/system/userLog/' + ids,
    method: 'delete'
  })
}
