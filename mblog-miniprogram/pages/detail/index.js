// pages/detail/index.js
const {
  $Message
} = require('../../dist/base/index');

Page({

  data: {
    hasLogin: false,
    detail: {},
    spinShows: '',
    isShow: !1,
    menuBackgroup: !1,
    isCollect: false,
    isLiked: false,
    comments: {},
    comment_count: 0,
    userId: '',
    commentContent: ''
  },

  onLoad: function (options) {
    // 加载时判断是否登录
    this.setData({
      hasLogin: wx.getStorageSync('hasLogin')
    })
    var id = options.id
    // var id = 1
    // 获取文章细节
    wx.$api.getArticleDetail(id).then(res => {
      console.log("getArticleDetail():", res)
      this.setData({
        detail: res.data
      })
    })
  },
  showHideMenu: function () {
    console.log('show')
    this.setData({
      isShow: !this.data.isShow,
      isLoad: !1,
      menuBackgroup: !this.data.false
    });
  },
  //打开赞赏
  reward() {
    $Message({
      content: '开发中...',
      type: 'default'
    });
  },
  //生成海报
  createPic() {
    var id = this.data.detail.objectId
    var title = this.data.detail.title
    var shareCode = this.data.shareCode
    var listPic = this.data.detail.listPic
    wx.navigateTo({
      url: '/pages/share/index?id=' + id + '&title=' + title + '&shareCode=' + shareCode + '&listPic=' + listPic,
    })
  },
  //取消和点赞文章
  like(e) {
    $Message({
      content: '开发中...',
      type: 'default'
    });
    /*
    var id = this.data.detail.objectId
    var action = e.currentTarget.dataset.action
    if (action == 'noLike') {
      wx.u.likeAction(id, 'noLike').then(res => {
        if (res.result) {
          this.setData({
            isLiked: false
          })
          $Message({
            content: '取消成功',
            type: 'success'
          });
        }
      })
    } else {
      wx.u.likeAction(id, 'like').then(res => {
        if (res.result) {
          this.setData({
            isLiked: true
          })
          $Message({
            content: '点赞成功',
            type: 'success'
          });
        }
      })
    }
    */
  },
  formSubmit(e) {
    $Message({
      content: '开发中...',
      type: 'default'
    });
    /*
    var userId = this.data.userId;
    var content = e.detail.value.inputComment;
    var form_Id = e.detail.formId
    var id = this.data.detail.objectId
    var user = null;
    if (!content) {
      $Message({
        content: '请输入评论内容',
        type: 'warning'
      });
      return false;
    }
    if (userId != '') {
      content = content.replace('@' + this.data.userName + " ", "");
      user = {
        nickName: this.data.userName
      }
    }
    wx.u.saveComment(id, userId, content, form_Id).then(res => {

      if (res.result) {
        var openId = wx.getStorageSync('openid')
        var userData = wx.getStorageSync('userInfo');
        var objectId = wx.Bmob.User.current().objectId
        var replyer = {
          objectId: objectId,
          userPic: userData.userPic,
          nickName: userData.nickName,
          authData: {
            weapp: {
              openid: openId
            }
          }
        };

        var data = [];
        data.push({
          replyer: replyer,
          createdAt: "刚刚",
          content: content,
          user: user,
          formID: form_Id
        });
        var comments = this.data.comments

        comments.push.apply(comments, data); //将页面上的数据和最新获取到的数据合并

        this.setData({
          comments: comments,
          commentContent: '',
          userId: '',
          comment_count: parseInt(this.data.comment_count) + 1
        })
        console.log(this.data.comments)
        $Message({
          content: '评论成功',
          type: 'success'
        });
        wx.u.sendNew('own', userData.nickName + "在《" + this.data.detail.title + "》中评论，请查看！", "", this.data.detail.objectId)
        if (userId != '') {
          wx.u.sendNew('user', userData.nickName + "在《" + this.data.detail.title + "》中评论，请查看！", userId, this.data.detail.objectId)
          //发送模板消息通知
          let modelData = {
            "touser": this.data.openid,
            "template_id": "WXM3zHZQX6X6IMqgKux5S6_Z8R3wWCPrQ_oSSH3zBSg", //模板id
            "page": "pages/detail/index?id=" + this.data.detail.objectId,
            "form_id": this.data.formID,
            "data": {
              "keyword1": {
                "value": content
              },
              "keyword2": {
                "value": this.data.detail.title
              },
              "keyword3": {
                "value": new Date().toLocaleString()
              }
            },
            //关键字
            "emphasis_keyword": "keyword2.DATA"
          }
          //使用Bmob-SDK发送模板消息
          wx.Bmob.sendWeAppMessage(modelData).then(function (response) {
            console.log(response);
          }).catch(function (error) {
            console.log(error);
          });
        }
      } else {
        $Message({
          content: '评论失败',
          type: 'warning'
        });
      }
    })*/
  },
  replyComment(e) {
    this.setData({
      userId: e.currentTarget.dataset.id,
      userName: e.currentTarget.dataset.nickname,
      commentContent: '@' + e.currentTarget.dataset.nickname + " ",
      formID: e.currentTarget.dataset.formid,
      openid: e.currentTarget.dataset.openid
    })
  },
  goLogin() {
    wx.switchTab({
      url: '/pages/my/index',
    })
  },
  onShareAppMessage() {
    return {
      title: this.data.detail.title,
      path: 'pages/detail/index?id=' + this.data.detail.objectId,
      imageUrl: '/images/blog.png'
    }
  },
})