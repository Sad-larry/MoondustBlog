<template>
  <div>
    <div class="comment-title">
      <i class="iconfont iconpinglunzu" />评论
    </div>
    <!-- 评论详情 -->
    <div v-if="count > 0 && reFresh">
      <!-- 评论数量 -->
      <div class="count">{{ count }} 条评论</div>
      <!-- 评论列表 -->
      <div style="display:flex" class="pt-5" v-for="(item, index) of commentList" :key="item.id">
        <!-- 头像 -->
        <v-avatar size="40" class="comment-avatar">
          <img :src="item.avatar" />
        </v-avatar>
        <div class="comment-meta">
          <!-- 用户名 -->
          <div class="comment-user">
            <span v-if="!item.webSite">{{ item.nickname }}</span>
            <a v-else :href="item.webSite" target="_blank">{{ item.nickname }}</a>
            <span class="blogger-tag" v-if="item.userId == 1">博主</span>
          </div>
          <!-- 信息 -->
          <div class="comment-info">
            <!-- 楼层 -->
            <span style="margin-right:10px">{{ index + 1 }}楼</span>
            <!-- 发表时间 -->
            <span style="margin-right:10px">{{ item.createTime | date }}</span>
            <!-- 喜欢数 -->
            <span v-show="item.likeCount > 0">{{ item.likeCount }}</span>
          </div>
          <!-- 评论内容 -->
          <p v-html="item.content" class="comment-content"></p>
          <!-- 回复人 -->
          <div style="display:flex" v-for="reply of item.replyVOList" :key="reply.id">
            <!-- 头像 -->
            <v-avatar size="36" class="comment-avatar">
              <img :src="reply.avatar" />
            </v-avatar>
            <div class="reply-meta">
              <!-- 用户名 -->
              <div class="comment-user">
                <span v-if="!reply.webSite">{{ reply.nickname }}</span>
                <a v-else :href="reply.webSite" target="_blank">{{ reply.nickname }}</a>
                <span class="blogger-tag" v-if="reply.userId === 1">博主</span>
              </div>
              <!-- 信息 -->
              <div class="comment-info">
                <!-- 发表时间 -->
                <span style="margin-right:10px">{{ reply.createTime | date}}</span>
                <!-- 喜欢数 -->
                <span v-show="reply.likeCount > 0">{{ reply.likeCount }}</span>
              </div>
              <!-- 回复内容 -->
              <p class="comment-content">
                <!-- 回复用户名 -->
                <template v-if="reply.replyUserId !== item.userId">
                  <span v-if="!reply.replyWebSite" class="comment-nickname ml-1">@{{ reply.replyNickname }}</span>
                  <a
                    v-else
                    :href="reply.replyWebSite"
                    target="_blank"
                    class="comment-nickname ml-1"
                  >@{{ reply.replyNickname }}</a>
                </template>
                <span v-html="reply.content" />
              </p>
            </div>
          </div>
        </div>
      </div>
      <!-- 加载按钮 -->
      <div class="load-wrapper">
        <v-btn outlined>已加载全部评论...</v-btn>
      </div>
    </div>
    <!-- 没有评论提示 -->
    <div v-else style="padding:1.25rem;text-align:center">咋还木有评论阿~</div>
  </div>
</template>

<script>
export default {
  props: {
    commentList: {
      type: Array,
    },
    count: {
      type: Number,
    },
  },
  data() {
    return {
      reFresh: true,
      content: "",
      current: 1,
    };
  },
  watch: {
    commentList() {
      this.reFresh = false;
      this.$nextTick(() => {
        this.reFresh = true;
      });
    },
  },
};
</script>

<style scoped>
p {
  margin-bottom: 1.25rem !important;
}

.blogger-tag {
  background: #ffa51e;
  font-size: 12px;
  display: inline-block;
  border-radius: 2px;
  color: #fff;
  padding: 0 5px;
  margin-left: 6px;
}

.comment-title {
  display: flex;
  align-items: center;
  font-size: 1.25rem;
  font-weight: bold;
  line-height: 40px;
  margin-bottom: 10px;
}

.comment-title i {
  font-size: 1.5rem;
  margin-right: 5px;
}

.count {
  padding: 5px;
  line-height: 1.75;
  font-size: 1.25rem;
  font-weight: bold;
}

.comment-meta {
  margin-left: 0.8rem;
  width: 100%;
  border-bottom: 1px dashed #f5f5f5;
}

.reply-meta {
  margin-left: 0.8rem;
  width: 100%;
}

.comment-user {
  font-size: 14px;
  line-height: 1.75;
}

.comment-user a {
  color: #1abc9c !important;
  font-weight: 500;
  transition: 0.3s all;
}

.comment-nickname {
  text-decoration: none;
  color: #1abc9c !important;
  font-weight: 500;
}

.comment-info {
  line-height: 1.75;
  font-size: 0.75rem;
  color: #b3b3b3;
}

.comment-content {
  font-size: 0.875rem;
  line-height: 1.75;
  padding-top: 0.625rem;
}

.comment-avatar {
  transition: all 0.5s;
}

.comment-avatar:hover {
  transform: rotate(360deg);
}

.load-wrapper {
  margin-top: 10px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.load-wrapper button {
  background-color: #49b1f5;
  color: #fff;
}
</style>
