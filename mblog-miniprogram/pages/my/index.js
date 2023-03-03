// pages/my/index.js
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'

Page({

  /**
   * 页面的初始数据
   */
  data: {
    hasLogin: false,
    userData: {},
    signNum: 0,
    sign: false,
    signTime: '',
    loading: true,
    noReadNewsCount: 0,

    avatarUrl: defaultAvatarUrl,
    nickname: '登录/注册>',
    intro: '第一次登录则是直接注册哦~'
  },

  onLoad: function (options) {
    console.log("hasLogin", getApp().globalData.hasLogin);
    wx.showLoading({
      title: '加载中'
    })
    var that = this
    wx.getUserInfo({
      success: (result) => {
        console.log("onLoad-getUserInfo()", result);
        var nickName = result.userInfo.nickName
        var userPic = result.userInfo.avatarUrl
        var userData = {
          'nickName': nickName,
          'userPic': userPic
        }
        wx.setStorageSync('userInfo', userData)
        // that.setData({
        //   userData: userData
        // })
        that.spinShow()
      },
      complete: (result) => {
        wx.hideLoading()
      }
    })

    // wx.u.getSignNum().then(res=>{
    //   if (res.result[0]!="" && res.result[0]!=undefined){
    //     var day = new Date(res.result[0].createdAt).toDateString();

    //     if (day == new Date().toDateString()){
    //       that.setData({
    //         sign:true,
    //         signTime:res.result[0]
    //       })
    //     }
    //   }
    //   that.setData({
    //     signNum:res.signNum
    //   })
    //   that.spinShow()
    // })
    // wx.u.getNewsCount().then(res=>{
    //   this.setData({
    //     noReadNewsCount:res.result
    //   })
    // })
  },
  onShareAppMessage() {
    return {
      title: '月尘博客',
      path: 'pages/index/index',
      imageUrl: '/images/blog.png'
    }
  },
  //授权获取用户数据
  bindGetUserInfo() {

  },
  onChooseAvatar(e) {
    let avatarUrl = 'data:image/jpeg;base64,' + wx.getFileSystemManager().readFileSync(e.detail.avatarUrl, 'base64')
    console.log(avatarUrl)
    this.setData({
      avatarUrl: e.detail.avatarUrl
    })
  },
  bindNicknameInput(e) {
    this.setData({
      nickname: e.detail.value
    })
    console.log(this.data.nickname);
  },
  // 登录
  login() {
    console.log("dataHasLogin:", this.data.hasLogin);
    if (this.data.hasLogin) {
      return;
    }
    console.log('tap-login');
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
        this.setData({
          hasLogin: true,
          avatarUrl: res.data.avatar,
          nickname: res.data.nickname,
          intro: res.data.intro
        })
      } else {
        this.setData({
          hasLogin: false,
          nickname: '出现异常，请重新登录>'
        })
        // 登录出现异常，清除token记录
        wx.removeStorageSync('token')
      }
    })
  },
  logout() {
    wx.$api.wxmpLogout().then(res => {
      console.log('wxmpLogout', res);
      this.setData({
        hasLogin: false,
        avatarUrl: defaultAvatarUrl,
        nickname: '登录/注册>',
        intro: '第一次登录则是直接注册哦~'
      })
      // 登录出现异常，清除token记录
      wx.removeStorageSync('token')
    })
  },
  //签到
  sign() {
    var that = this
    wx.showLoading({
      title: '签到中',
    })
    wx.u.saveSign().then(res => {
      if (res.result) {
        setTimeout(function () {
          wx.hideLoading()
          that.setData({
            sign: true,
            signShow: true,
            signTime: res.data,
            signNum: that.data.signNum + 1
          })
        }, 1500)
      }
    })
  },
  //分享
  share() {

  },
  spinShow: function () {
    var that = this
    // setTimeout(function () {
    that.setData({
      loading: !that.data.loading,
    });
    console.log("spinShow");
    // }, 1500)
  }
})