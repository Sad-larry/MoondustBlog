// pages/share/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {

  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    wx.showLoading({
      title: '海报生成中',
    })
    var that = this
    var title = options.title
    var shareCode = options.shareCode
    var listPic = options.listPic
    that.setData({
      visible: true,
      createPoster: {
        width: '600rpx',
        height: '600rpx',
        background: '#fff',
        views: [{
            type: 'image',
            url: listPic,
            css: {
              width: '600rpx',
              height: '450rpx',
            }
          },
          {
            type: 'image',
            url: shareCode,
            css: {
              width: '180rpx',
              height: '167rpx',
              mode: 'scaleToFill',
              top: '433rpx',
            }
          },
          {
            type: 'image',
            url: 'https://niu.moonzs.work/2023/03/04/47112202560e4276b0777a5d33c05515.jpg',
            css: {
              width: '420rpx',
              height: '167rpx',
              mode: 'scaleToFill',
              top: '433rpx',
              left: '180rpx'
            }
          },
          {
            type: 'text',
            text: title,
            css: {
              top: `50rpx`,
              fontSize: '45rpx',
              color: '#fff',
              fontWeight: 'bold',
              align: 'center',
              width: '600rpx',
              left: '300rpx'
            }
          },
          {
            type: 'text',
            text: "月尘博客 吴灵出品",
            css: {
              left: '300rpx',
              top: '380rpx',
              fontSize: '20rpx',
              color: '#fff',
              width: '600rpx',
              align: 'center',
            }
          }
        ]
      }
    })
  },
  onImgOK(e) {
    this.setData({
      imagePath: e.detail.path
    })
    setTimeout(function () {
      wx.hideLoading()
    }, 1000)
  },
  saveImage() {
    var that = this
    wx.getSetting({
      success(res) {
        if (!res.authSetting['scope.writePhotosAlbum']) {
          wx.authorize({
            scope: 'scope.writePhotosAlbum',
            success() {

            }
          })
          wx.openSetting({
            success(res) {
              console.log(res.authSetting)
            }
          })
        } else {
          wx.saveImageToPhotosAlbum({
            filePath: that.data.imagePath,
            success(res) {
              wx.navigateBack({
                delta: 1
              })
            }
          });
        }
      }
    })
  },
  returnArticle() {
    wx.navigateBack()
  }
})