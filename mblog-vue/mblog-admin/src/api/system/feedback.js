import request from '@/utils/request'

// 查询用户反馈列表
export function listBack(query) {
  return request({
    url: '/system/feedback/list',
    method: 'get',
    params: query
  })
}

// 删除用户反馈
export function delBack(ids) {
  return request({
    url: '/system/feedback/' + ids,
    method: 'delete'
  })
}

// 回复用户反馈
export function replyBack(data) {
  return request({
    url: '/system/feedback/reply',
    method: 'post',
    data: data
  })
}