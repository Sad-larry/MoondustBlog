import request from '@/api/request'

// 上传文件
export function uploadImage(data) {
    return request({
        url: '/admin/upload/image',
        method: 'POST',
        data
    })
}
