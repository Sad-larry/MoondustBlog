const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

/**
 * 生成文章对应小程序二维码
 * id:文章id
 */
const getShareCode = (id) => {
  return new Promise((resolve, reject) => {
    let path = 'pages/detail/index?id=' + id
    let qrData = {
      path: path,
      width: 430,
      interface: 'a',
      type: 1
    };

    wx.Bmob.generateCode(qrData).then(res => {
      const query = wx.Bmob.Query('articles')
      query.get(id).then(res1 => {
        res1.set('shareCode', res.url)
        res1.save()
        resolve({
          'shareCode': res.url
        })
      }).catch(err => {
        console.log(err);
      })
    })
  })
}

/**
 * 查询是否点赞文章
 * id:文章id
 */
const getIsLiked = (id) => {
  return new Promise((resolve, reject) => {
    let current = wx.Bmob.User.current();
    let uid = current.objectId
    const query = wx.Bmob.Query('like')
    query.equalTo('article', '==', id)
    query.equalTo('user', '==', uid)
    return query.count().then(res => {
      resolve({
        'result': res
      })
    })
  })
}

/**
 * 取消/点赞文章
 * id:文章id
 * action：取消或者点赞
 */
const likeAction = (id, action) => {
  return new Promise((resolve, reject) => {
    let current = wx.Bmob.User.current();
    let uid = current.objectId
    const query = wx.Bmob.Query('like');
    if (action == 'noLike') {
      query.equalTo('article', '==', id);
      query.equalTo('user', '==', uid);
      query.find().then(todos => {
        todos.destroyAll().then(res => {
          resolve({
            'result': true
          })
        }).catch(err => {
          resolve({
            'result': false
          })
        })
      })
    } else {
      query.set("user", uid);
      query.set("article", id);
      query.save().then(res => {
        resolve({
          'result': true
        })
      }).catch(err => {
        resolve({
          'result': false
        })
      })
    }
  })
}

/**
 * 获取文章评论
 * id：文章id
 */
const getComment = (id) => {
  return new Promise((resolve, reject) => {
    const query = wx.Bmob.Query('comments')
    query.include('replyer,user', '_User')
    query.equalTo('article', '==', id)
    query.find().then(res => {
      resolve({
        'result': res
      })
    })
  })
}

/**
 * 保存评论
 * id:文章id
 * userId:被评论者id
 * content:评论内容
 * form_Id:表单id
 */
const saveComment = (id, userId, content, form_Id) => {
  return new Promise((resolve, reject) => {
    let current = wx.Bmob.User.current();
    let replyerId = current.objectId
    const query = wx.Bmob.Query('comments')
    const pointer1 = wx.Bmob.Pointer("_User");
    const poiID1 = pointer1.set(replyerId);
    const pointer2 = wx.Bmob.Pointer("articles");
    const poiID2 = pointer2.set(id);
    if (userId) {
      var pointer3 = wx.Bmob.Pointer("_User");
      var poiID3 = pointer3.set(userId);
      query.set("user", poiID3)
    }

    query.set("content", content);
    query.set("replyer", poiID1);
    query.set("article", poiID2);
    query.set("formID", form_Id);
    query.save().then(res => {
      resolve({
        'result': true
      })
    }).catch(err => {
      resolve({
        'result': false
      })
    })
  })
}

/**
 * 修改信息的阅读状态
 */
const changeStatus = (id) => {
  const query = wx.Bmob.Query('news');
  query.get(id).then(res => {
    res.set('status', 'true');
    res.save();
  })
}

module.exports = {
  formatTime: formatTime
}