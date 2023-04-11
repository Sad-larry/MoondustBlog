// pages/category/index.js
Page({

  /**
   * 页面的初始数据
   */
  data: {
    cateList: {},
    current: '',
    current_scroll: '',
    category: '',
    nodata: false, //更多数据
    articles: []
  },

  onLoad: function(options) {
    wx.$api.getCategoryList().then(res => {
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
      let data = [];
      res.data.records.forEach((resEach) => {
        data.push({
          'id': resEach.id,
          'title': resEach.title,
          'quantity': resEach.quantity,
          'summary': resEach.summary,
          'createTime': resEach.createTime,
          'categoryName': resEach.categoryName,
          'avatar': resEach.avatar,
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
    })
  },
  handleChangeScroll({
    detail
  }) {
    this.setData({
      loading:true,
      current_scroll: detail.key
    });
    this.getArticleList(detail.key)
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