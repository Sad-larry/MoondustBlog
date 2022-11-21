import request from '@/utils/request'

// 查询友情链接列表
export function listFriendlink(query) {
  return request({
    url: '/system/friendlink/list',
    method: 'get',
    params: query
  })
}

// 新增友情链接
export function addFriendlink(data) {
  return request({
    url: '/system/friendlink',
    method: 'post',
    data: data
  })
}

// 修改友情链接
export function updateFriendlink(data) {
  return request({
    url: '/system/friendlink',
    method: 'put',
    data: data
  })
}

// 删除友情链接
export function delFriendlink(ids) {
  return request({
    url: '/system/friendlink/' + ids,
    method: 'delete'
  })
}

// 置顶友情链接
export function topFriendlink(data) {
  return request({
    url: '/system/friendlink/top',
    method: 'post',
    data: data
  })
}
// 删除友情链接
export function passFriendlink(ids) {
  return request({
    url: '/system/friendlink/pass/' + ids,
    method: 'get'
  })
}
