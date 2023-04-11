// pages/my/login/index.js
const {
  $Message
} = require('../../../dist/base/index');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    userInfo: {
      code: '',
      avatar: '/images/avatar.png',
      nickname: '',
      intro: ''
    }
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad(options) {

  },
  onChooseAvatar(e) {
    if (e.detail.avatarUrl) {
      let avatarBase64 = 'data:image/jpeg;base64,' + wx.getFileSystemManager().readFileSync(e.detail.avatarUrl, 'base64')
      // 上传用户头像，返回头像链接
      wx.$api.wxmpAvatar(avatarBase64).then(res => {
        if (res.code == 200) {
          this.setData({
            ['userInfo.avatar']: res.data,
          })
        } else {
          $Message({
            content: '用户头像上传失败',
            type: 'error'
          });
        }
      })
    }
  },
  inputNickname(e) {
    this.setData({
      ['userInfo.nickname']: e.detail.value,
    })
  },
  inputIntro(e) {
    this.setData({
      ['userInfo.intro']: e.detail.value,
    })
  },
  handleRegister() {
    wx.login({
      success: (res) => {
        if (res.code) {
          this.setData({
            ['userInfo.code']: res.code
          })
          wx.$api.wxmpRegister(this.data.userInfo).then(res => {
            if (res.code === 200) {
              // 注册成功
              wx.showModal({
                title: '提示',
                content: '注册成功，请返回页面登录',
                showCancel: false,
                success (res) {
                  if (res.confirm) {
                    wx.navigateBack()
                  }
                }
              })
            } else {
              $Message({
                content: '注册失败',
                type: 'error'
              });
            }
          })
        }
      },
    })
  },
  goBack() {
    wx.navigateBack()
  }
})