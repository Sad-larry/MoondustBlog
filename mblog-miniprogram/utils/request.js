const baseURL = 'https://refrainblog.cn/prod-api/'
// const baseURL = 'http://localhost:8080'

// 1 封装公共请求接口方法
export const request = ({
  url,
  data = {},
  method = "get",
  header = {
    'content-type': "application/json",
  }
}) => {
  // 2 请求前加载 最后在请求后隐藏
  wx.showLoading({
    title: "拼命加载中..."
  })
  // 3 Promise 
  return new Promise((resolve, reject) => {
    wx.request({
      url: baseURL + url,
      data,
      method,
      header,
      timeout: 10000,
      success: (res) => {
        // 请求成功返回的数据
        resolve(res.data)
      },
      fail: (err) => {
        // 请求失败返回的消息
        reject(err)
      },
      complete() {
        // 请求完做的事
        wx.hideLoading()
      }
    })
  })
};