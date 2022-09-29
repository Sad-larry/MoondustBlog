import request from '@/api/request'

// 管理员登录
export function adminLogin(data) {
    return request({
        url: '/admin/login',
        method: 'POST',
        data
    })
}