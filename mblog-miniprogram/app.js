//app.js
const utils = require('utils/util.js')
// 在main.js入口文件中引入封装的api
import * as api from 'utils/api.js'
wx.u = utils
App({
  onLaunch: function () {
    // 小程序初始化时就将所有的api函数和封装的showToast函数挂载在wx上
    wx.$api = api;
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        console.log("login()", res);
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
        if (res.code) { //当有临时登录凭证code码时，我们请求登录接口
          //请求登录接口
          wx.$api.wxmpLogin(res.code).then(res=>{
            console.log("login:", res);
          })
        }
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo

              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        } else {
          // 没有授权，需要登录
        }
      }
    })
  },
  globalData: {
    userInfo: null
  }
})