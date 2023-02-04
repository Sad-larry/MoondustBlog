import request from '@/utils/request'

// 获取redis缓存监控数据
export function cacheInfo() {
    return request({
        url: '/system/monitor/cache',
        method: 'get'
    })
}