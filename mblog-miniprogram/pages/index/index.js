Page({

  /**
   * 页面的初始数据
   */
  data: {
    current: '',
    current_scroll: '',
    category: '',
    moreData: true, //更多数据
    pageSize: 5, //数量
    pagination: 1, //页码
    articles: [],
    bottomWord: '',
    loadMore: false,
    loadMores: false
  },

  onLoad: function (options) {
    this.getArticleList(this.data.pageSize, this.data.pagination);
  },
  getArticleList(pageSize, pagination) {
    wx.$api.getArticleList(pageSize, pagination).then(res => {
      let data = [];
      res.data.records.forEach((resEach) => {
        data.push({
          'id': resEach.id,
          'title': resEach.title,
          'quantity': resEach.quantity,
          'summary': resEach.summary,
          'createTime': resEach.createTime,
          'avatar': resEach.avatar,
          'categoryName': resEach.categoryName
        })
      })
      if (data.length) {
        let articles = this.data.articles;
        let pagination = this.data.pagination;
        articles.push.apply(articles, data);
        pagination = pagination ? pagination + 1 : 1;
        this.setData({
          'articles': articles,
          'pagination': pagination,
          'bottomWord': '',
          'loadMore': false,
        })
      } else {
        this.setData({
          'moreData': false,
          'bottomWord': '加载完',
          'loadMore': false,
        })
      }
    })
  },
  onReachBottom: function () {
    if (this.data.moreData) {
      this.setData({
        'loadMore': true,
        'bottomWord': '加载中',
      })
      this.getArticleList(this.data.pageSize, this.data.pagination);
    }
  },
  onShareAppMessage() {
    return {
      title: '月尘博客',
      path: 'pages/index/index',
      imageUrl: '/images/blog.png'
    }
  }
})