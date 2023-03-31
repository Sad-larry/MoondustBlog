import axios from 'axios'
import { Notification } from 'element-ui'

axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// 创建axios实例
const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: process.env.VUE_APP_BASE_API,
    // 超时
    timeout: 10000
})

// request拦截器
service.interceptors.request.use(config => {
    return config
}, error => {
    console.log("request error : ", error)
    Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
    console.log("response:", res);
    // 未设置状态码则默认成功状态
    const code = res.data.code || 200;
    const msg = res.data.msg || 'Error'
    if (code !== 200) {
        Notification.error({
            title: code,
            message: msg
        })
        return Promise.reject(new Error(msg))
    } else {
        return res.data
    }
},
    error => {
        console.log('response error : ' + error)
        return Promise.reject(error)
    }
)

export default service