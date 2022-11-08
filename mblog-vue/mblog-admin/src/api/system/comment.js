import request from '@/utils/request'

// 查询评论列表
export function listComment(query) {
  return request({
    url: '/system/comment/list',
    method: 'get',
    params: query
  })
}

// 删除评论
export function delComment(ids) {
  return request({
    url: '/system/comment/' + ids,
    method: 'delete'
  })
}
