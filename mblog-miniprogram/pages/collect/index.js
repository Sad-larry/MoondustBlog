// pages/collect/index.js
const defaultAvatarUrl = 'https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0'
Page({

  /**
   * 页面的初始数据
   */
  data: {
    avatarUrl: defaultAvatarUrl,
    nickname: '',
    intro: ''
  },
  // 保存按钮
  formSubmit(e) {
    //表单返回的所有数据
    var formData = e.detail.value;
    //获取上一个页面的对象
    var pages = getCurrentPages()
    var prevPage = pages[pages.length - 2]
    //调用上一个页面的setData()方法，把数据存储到上一个页面中去
    prevPage.setData({
      username: formData.username,
      gender: formData.gender
    })
    //返回上一个页面
    wx.navigateBack()
  },
  // 修改昵称
  bindNicknameInput(e) {
    this.setData({
      nickname: e.detail.value
    })
  },
  // 修改个人介绍
  bindIntroInput(e) {
    this.setData({
      intro: e.detail.value
    })
  },
})