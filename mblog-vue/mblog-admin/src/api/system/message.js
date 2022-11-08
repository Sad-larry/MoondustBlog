import request from '@/utils/request'

// 查询留言板列表
export function listMessage(query) {
  return request({
    url: '/system/message/list',
    method: 'get',
    params: query
  })
}

// 审核通过留言板
export function passMessage(ids) {
  return request({
    url: '/system/message/pass/' + ids,
    method: 'get'
  })
}

// 删除留言板
export function delMessage(ids) {
  return request({
    url: '/system/message/' + ids,
    method: 'delete'
  })
}
