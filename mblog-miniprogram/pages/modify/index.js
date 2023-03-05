// pages/modify/index.js
const {
  $Message
} = require('../../dist/base/index');

Page({

  /**
   * 页面的初始数据
   */
  data: {
    avatarBase64: '',
    avatar: '',
    nickname: '',
    intro: ''
  },
  onLoad: function () {
    wx.$api.wxmpUserInfo().then(res => {
      if (res.code == 200) {
        this.setData({
          avatar: res.data.avatar,
          nickname: res.data.nickname,
          intro: res.data.intro
        })
      } else {
        wx.removeStorageSync('token')
        wx.removeStorageSync('hasLogin')
        wx.reLaunch({
          url: '/pages/my/index'
        })
      }
    })

  },
  // 保存按钮
  formSubmit(e) {
    // 表单返回的所有数据
    var formData = e.detail.value;
    // 获取上一个页面的对象
    var pages = getCurrentPages()
    var prevPage = pages[pages.length - 2]
    if (!formData.nickname) {
      $Message({
        content: '用户昵称未填写',
        type: 'warning'
      });
      return;
    }
    let data = {
      avatar: this.data.avatar,
      nickname: formData.nickname,
    }
    // 介绍不为空则也进行修改
    if (formData.intro) {
      data['intro'] = formData.intro
    }
    wx.$api.wxmpModify(data).then(res => {
      // 调用上一个页面的setData()方法，把数据存储到上一个页面中去
      prevPage.setData(data)
    })
    // 返回上一个页面
    wx.navigateBack()
  },
  // 修改头像
  onChooseAvatar(e) {
    if (e.detail.avatarUrl) {
      let avatarBase64 = 'data:image/jpeg;base64,' + wx.getFileSystemManager().readFileSync(e.detail.avatarUrl, 'base64')
      // 上传用户头像，返回头像链接
      wx.$api.wxmpAvatar(avatarBase64).then(res => {
        if (res.code == 200) {
          this.setData({
            avatarBase64: avatarBase64,
            avatar: res.data.avatar
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
  resetNickname() {
    this.setData({
      nickname: ''
    })
  },
  resetIntro() {
    this.setData({
      intro: ''
    })
  },
})