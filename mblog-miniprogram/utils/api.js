// 引入封装的uni.request请求方法
import {
  request
} from './request.js';

/**
 * 用户登录
 */
export const wxmpLogin = (code) => {
  return request({
    url: '/web/user/wxmpLogin',
    method: 'get',
    data: {
      code
    },
  })
}

/**
 * 获取用户信息
 */
export const wxmpUserInfo = () => {
  return request({
    url: '/web/user/wxmpUserInfo',
    method: 'get',
    header: {
      'Authorization': 'Bearer ' + wx.getStorageSync('token'),
    }
  })
}

/**
 * 修改用户信息
 */
export const wxmpModify = (formData) => {
  return request({
    url: '/web/user/wxmpModify',
    method: 'post',
    header: {
      'Authorization': 'Bearer ' + wx.getStorageSync('token'),
    },
    data: formData
  })
}

/**
 * 用户退出登录
 */
export const wxmpLogout = () => {
  return request({
    url: '/web/user/wxmpLogout',
    method: 'get',
    header: {
      'Authorization': 'Bearer ' + wx.getStorageSync('token'),
    }
  })
}

/**
 * 获取文章列表
 * pageSize:数量
 * pagination:页码
 * categoryId:分类ID
 */
export const getArticleList = (pageSize, pageNum, categoryId) => {
  return request({
    url: '/web/article/list',
    method: 'get',
    data: {
      pageSize,
      pageNum
    }
  })
}

/**
 * 获取文章详情
 * id:文章id
 */
export const getArticleDetail = (id) => {
  return request({
    url: '/web/article/info',
    method: 'get',
    data: {
      id
    }
  })
}

getArticleDetail
/**
 * 获取分类列表
 */
export const getCategoryList = () => {
  return request({
    url: '/web/category/list',
    method: 'get'
  })
}
/**
 * 按照分类请求博文数据
 */
export const getArticleByCategory = (categoryId) => {
  return request({
    url: '/web/article/condition',
    method: 'get',
    data: {
      categoryId
    }
  })
}