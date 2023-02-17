import request from '@/utils/request'

// 上传图片资源
export function uploadImage(data) {
  return request({
    url: '/system/qiniu/upload/image',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 上传文章时，额外上传图片，另加key
export function uploadArticleImage(data) {
  return request({
    url: '/system/qiniu/upload/article/image',
    method: 'post',
    data: data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

// 查询所有文件列表
export function listFile(query) {
  return request({
    url: '/system/qiniu/file/list',
    method: 'get',
    params: query
  })
}
