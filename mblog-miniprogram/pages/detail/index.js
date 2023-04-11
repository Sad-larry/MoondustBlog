// pages/detail/index.js
const {
  $Message
} = require('../../dist/base/index');

Page({

  data: {
    hasLogin: false,
    detail: {},
    userInfo: {},
    isShow: !1,
    menuBackgroup: !1,
    isLiked: false,
    comments: [],
    comment_count: 0,
    userId: '',
    parentId: 0,
    replyUserId: 0,
    replyNickname: '',
    content: '',
    commentContent: '说点什么吧...',
    flyLike: {}
  },

  onLoad: function (options) {
    let hasLogin = wx.getStorageSync('hasLogin')
    let userInfo = wx.getStorageSync('userInfo')
    let id = options.id
    // 获取文章细节
    wx.$api.getArticleDetail(id).then(res => {
      this.setData({
        detail: res.data,
        hasLogin: hasLogin,
        userInfo: userInfo
      })
      // 获取文章评论
      wx.$api.getArticleComment(id).then(res => {
        if (res.total) {
          this.setData({
            comment_count: res.total,
            comments: res.records
          })
        }
      })
    })
  },
  showHideMenu: function () {
    this.setData({
      isShow: !this.data.isShow,
      isLoad: !1,
      menuBackgroup: !this.data.false
    });
  },
  //打开赞赏
  reward() {
    $Message({
      content: '谢谢大哥支持，功能正在开发中...',
      type: 'default',
      duration: 3
    });
  },
  //生成海报
  createPic() {
    var id = this.data.detail.id
    var title = this.data.detail.title
    var shareCode = this.data.categoryId
    var listPic = this.data.detail.avatar
    wx.navigateTo({
      url: '/pages/share/index?id=' + id + '&title=' + title + '&shareCode=' + shareCode + '&listPic=' + listPic,
    })
  },
  //取消和点赞文章
  like(e) {
    this.setData({
      isLiked: true
    })
    var animation = wx.createAnimation({
      duration: 1000,
      timingFunction: 'ease-out',
    })
    this.animation = animation;
    this.animation.rotateY(180).step();
    this.setData({
      flyLike: this.animation.export()
    });
    setTimeout(function () {
      this.animation.rotateY(360).step();
      this.setData({
        flyLike: this.animation.export()
      })
    }.bind(this), 1000)
    setTimeout(function () {
      this.setData({
        isLiked: false
      })
    }.bind(this), 2500)
  },
  formSubmit(e) {
    let articleId = this.data.detail.id;
    let userId = this.data.userInfo.userId;
    let replyUserId = this.data.replyUserId;
    let parentId = this.data.parentId;
    let content = e.detail.value.inputComment || e.detail.value;
    if (!content) {
      $Message({
        content: '请输入评论内容',
        type: 'warning'
      });
      return false;
    }
    let comment = {
      articleId,
      userId,
      content,
    }
    if (parentId) {
      comment['parentId'] = parentId;
    }
    if (replyUserId) {
      comment['replyUserId'] = replyUserId;
    }
    wx.$api.sendArticleComment(comment).then(res => {
      if (res.code == 200) {
        comment['id'] = res.data;
        comment['avatar'] = this.data.userInfo.avatar;
        comment['nickname'] = this.data.userInfo.nickname;
        comment['createTime'] = '刚刚';
        var comments = this.data.comments
        // 将页面上的数据和最新获取到的数据合并
        // 如果是回复评论，则添加到回复评论中
        if (parentId) {
          for (let i = 0; i < comments.length; i++) {
            if (comments[i].id == parentId) {
              comment['replyNickname'] = this.data.replyNickname;
              comments[i].replyCount = comments[i].replyCount + 1;
              comments[i].replyVOList.unshift(comment);
              break;
            }
          }
        } else {
          // 加到第一个评论上
          comments.unshift(comment);
        }
        this.setData({
          comments: comments,
          comment_count: parseInt(this.data.comment_count) + 1
        })
        $Message({
          content: '评论成功',
          type: 'success'
        });
      } else {
        $Message({
          content: '评论失败',
          type: 'warning'
        });
      }
      // 清除变量数据
      this.setData({
        parentId: 0,
        replyUserId: 0,
        replyNickname: '',
        content: '',
        commentContent: '说点什么吧...'
      })
    });
  },
  replyComment(e) {
    this.setData({
      replyNickname: e.currentTarget.dataset.nickname,
      commentContent: '回复给@' + e.currentTarget.dataset.nickname + " ",
      parentId: e.currentTarget.dataset.parentid,
      replyUserId: e.currentTarget.dataset.replyuserid
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
  wxmlTagATap(e) {
    console.log("外部链接暂不能访问");
  }
})