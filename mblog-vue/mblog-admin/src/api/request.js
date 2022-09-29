import axios from "axios"
// 消息提示
import { Message } from 'element-ui'

// 创建axios实例
const service = axios.create({
    baseURL: process.env.VUE_APP_BASE_API, // 请求url
    timeout: 10000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(
    config => {
        // 如果登录成功后有token，则应该携带token
        /////
        return config
    }, error => {
        // Do something with request error
        console.log(error) // for debug
        Promise.reject(error)
    })

// response拦截器
service.interceptors.response.use(
    response => {
        // 根据响应的code码判断

        // 请求成功，但是code不为200
        if (response.data.code !== 200) {
            Message.error({
                showClose: true,
                message: response.data.msg,
                center: true,
                duration: 2000
            })
            return Promise.reject('error')
        } else {
            return response
        }
    },
    error => {
        Message.error({
            showClose: true,
            message: error.message,
            center: true,
            duration: 2000
        })
        return Promise.reject(error)
    }
)

export default service