// 引入封装的uni.request请求方法
import {
  request
} from './request.js';

/**
 * 用户登录
 */
export const wxmpLogin = (code) => {
  return request({
    url: '/web/user/mwxLogin',
    method: 'get',
    data: {
      code
    },
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