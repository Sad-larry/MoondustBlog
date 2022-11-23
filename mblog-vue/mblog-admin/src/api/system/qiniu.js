import request from '@/utils/request'

// 上传图片资源
export function uploadImage(data) {
    return request({
        url: '/system/qiniu/upload/image',
        method: 'post',
        data: data
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
  