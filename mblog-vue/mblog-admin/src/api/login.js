import request from '@/utils/request'

// 登录方法
export function login(username, password, code, uuid) {
  const data = {
    username,
    password,
    code,
    uuid
  }
  return request({
    url: '/system/login',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 注册方法
export function register(data) {
  return request({
    url: '/system/register',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/user/getInfo',
    method: 'get'
  })
}

// 退出方法
export function logout() {
  return request({
    url: '/system/logout',
    method: 'post'
  })
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: '/system/captchaImage',
    headers: {
      isToken: false
    },
    method: 'get',
    timeout: 20000
  })
}

// 发送邮箱验证码
export function sendMailCode(username) {
  return request({
    url: '/system/mailCode',
    headers: {
      isToken: false
    },
    method: 'get',
    params: { username: username }
  })
}

// 用户密码重置
export function resetPwdBySendEmail(email) {
  return request({
    url: '/system/password/email',
    headers: {
      isToken: false
    },
    method: 'get',
    params: {
      email
    }
  })
}

// 用户密码重置
export function resetUserPwd(data) {
  return request({
    url: '/system/password/update',
    headers: {
      isToken: false
    },
    method: 'post',
    data: data
  })
}