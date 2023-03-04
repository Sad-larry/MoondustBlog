//app.js
const utils = require('utils/util.js')
// 在main.js入口文件中引入封装的api
import * as api from 'utils/api.js'
wx.u = utils

App({
  onLaunch: function () {
    // 小程序初始化时就将所有的api函数和封装的showToast函数挂载在wx上
    wx.$api = api;
  },
  globalData: {
    // 用户无头像时的默认头像
    defaultAvatarUrl: 'https://niu.moonzs.work/2022/11/26/31d8a40e2e044bfaabdf9f9018d94e77.jpg',
    userInfo: null
  }
})