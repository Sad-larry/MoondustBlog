import request from '@/api/request'

// 标签列表
export function listTag(params) {
    return request({
        url: '/admin/tag/list',
        method: 'GET',
        params
    })
}

export default { listTag }