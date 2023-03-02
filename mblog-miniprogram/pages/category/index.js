// pages/category/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    loading: true,
    cateList: {},
    current: '',
    current_scroll: '',
    category: '',
    nodata: false, //更多数据
    articles: []
  },

  onLoad: function(options) {
    wx.showLoading({
      title:'加载中'
    })
    var that = this
    wx.$api.getCategoryList().then(res => {
      console.log("getCategoryList()", res);
      this.setData({
        cateList: res.data,
        current: res.data[0].id,
        current_scroll: res.data[0].id,
      })
      this.getArticleList(res.data[0].id);
    })

  },
  getArticleList(categoryId) {
    wx.$api.getArticleByCategory(categoryId).then(res => {
      console.log("getArticleByCategory()", res)
      let data = [];
      res.data.records.forEach((resEach) => {
        data.push({
          'objectId': resEach.id,
          'title': resEach.title,
          'read_counts': resEach.id,
          'excerpt': resEach.title,
          'createdAt': resEach.createTime,
          'category': resEach.categoryName,
          'listPic': resEach.avatar,
          'author': resEach.title
        })
      })
      if (data.length) {
        this.setData({
          'articles': data,
          'nodata':false
        })
      }else{
        this.setData({
          'articles': data,
          'nodata': true
        })
      }
      this.spinShow();
    })
  },
  handleChangeScroll({
    detail
  }) {
    console.log("detail", detail)
    this.setData({
      loading:true,
      current_scroll: detail.key
    });
    this.getArticleList(detail.key)
  },
  spinShow: function() {
    var that = this
    setTimeout(function() {
      that.setData({
        loading: false,
      });
      console.log("spinShow");
    }, 1500)
  },
  detail(e){
    var id = e.currentTarget.dataset.id
    wx.navigateTo({
      url: '/pages/detail/index?id='+id,
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