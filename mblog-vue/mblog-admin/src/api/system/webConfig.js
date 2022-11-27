import request from '@/utils/request'

// 查询网站配置
export function getWebConfig() {
    return request({
        url: '/system/webConfig',
        method: 'get'
    })
}


// 修改网站配置
export function updateWebConfig(data) {
    return request({
        url: '/system/webConfig',
        method: 'put',
        data: data
    })
}

