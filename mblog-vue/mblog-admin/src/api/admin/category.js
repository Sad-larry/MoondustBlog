import request from '@/api/request'

// 分类列表
export function listCategory(params) {
    return request({
        url: '/admin/category/list',
        method: 'GET',
        params
    })
}

export default { listCategory }