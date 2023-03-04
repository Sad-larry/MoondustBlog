// pages/my/index.js
const {
  $Message
} = require('../../dist/base/index');
const app = getApp();

Page({

  /**
   * 页面的初始数据
   */
  data: {
    hasLogin: false,
    noReadNewsCount: 0,
    avatar: app.globalData.defaultAvatarUrl,
    nickname: '登录/注册>',
    intro: '第一次登录则是直接注册哦~'
  },
  onLoad: function (options) {
    // 判断是否登录过
    let loginState = wx.getStorageSync('hasLogin');
    if (loginState) {
      // 如果登录过，则直接从缓存中读取数据
      let userInfo = wx.getStorageSync('userInfo')
      this.setData({
        hasLogin: true,
        avatar: userInfo.avatar,
        nickname: userInfo.nickname,
        intro: userInfo.intro
      })
    }
  },
  // 登录
  login() {
    let token = wx.getStorageSync('token');
    console.log('login-token:', token);
    // 若存在 token，可以用 token 直接获取用户
    if (!token) {
      wx.login({
        success: res => {
          if (res.code) {
            // 第一次登录
            wx.$api.wxmpLogin(res.code).then(res => {
              if (res.code == 200) {
                // 保存 token
                wx.setStorageSync('token', res.token);
                this.getUserInfo();
              }
            })
          }
        }
      })
    } else {
      this.getUserInfo()
    }
  },
  // 获取用户信息
  getUserInfo() {
    wx.$api.wxmpUserInfo().then(res => {
      console.log("res:", res);
      if (res.code == 200) {
        console.log('wxmpUserInfo', res);
        let userInfo = {
          avatar: res.data.avatar,
          nickname: res.data.nickname,
          intro: res.data.intro
        }
        this.setData(userInfo)
        wx.setStorageSync('userInfo', userInfo);
        this.setData({
          hasLogin: true
        })
        wx.setStorageSync('hasLogin', true);
      } else {
        $Message({
          content: '登录异常',
          type: 'error'
        });
        // 登录出现异常，清除token记录
        wx.removeStorageSync('token')
        wx.removeStorageSync('userInfo')
        wx.removeStorageSync('hasLogin')
      }
    })
  },
  // 注销
  logout() {
    wx.$api.wxmpLogout().then(res => {
      console.log('wxmpLogout', res);
      this.setData({
        hasLogin: false,
        avatar: app.globalData.defaultAvatarUrl,
        nickname: '登录/注册>',
        intro: '第一次登录则是直接注册哦~'
      })
      $Message({
        content: '再见咯~',
        type: 'success'
      });
      // 登录出现异常，清除token记录
      wx.removeStorageSync('token')
      wx.removeStorageSync('userInfo')
      wx.removeStorageSync('hasLogin')
    })
  },
  onShareAppMessage() {
    return {
      title: '月尘博客',
      path: 'pages/index/index',
      imageUrl: '/images/blog.png'
    }
  },
})