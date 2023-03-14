<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">归档</h1>
    </div>
    <!-- 归档列表 -->
    <v-card class="blog-container">
      <div class="count">目前共计{{ count }}篇文章，继续加油~</div>
      <ul class="timeline-wrapper">
        <li class="timeline-item" v-for="item in archiveList" :key="item.id">
          <div class="timeline-box">
            <div class="out-circle">
              <div class="in-circle"></div>
            </div>
            <div class="long-line"></div>
          </div>
          <div class="timeline-content">
            <!-- 日期 -->
            <div class="timeline-date">{{ item.createTime | date }}</div>
            <!-- 文章标题 -->
            <div class="timeline-title">
              <router-link :to="'/article/' + item.id" style="color:#666;text-decoration: none">{{ item.title }}</router-link>
            </div>
            <div class="timeline-desc">{{ item.summary }}</div>
          </div>
        </li>
      </ul>
      <!-- 分页按钮 -->
      <v-pagination color="#00C4B6" v-model="current" :length="Math.ceil(count / 10)" total-visible="7" />
    </v-card>
  </div>
</template>

<script>
import { getArchives } from "@/api";
export default {
  data() {
    return {
      count: 0,
      current: 1,
      archiveList: [],
    };
  },
  created() {
    this.listArchives();
  },
  computed: {
    cover() {
      var cover = "";
      this.$store.state.blogInfo.pageList.forEach((item) => {
        if (item.pageLabel === "archive") {
          cover = item.pageCover;
        }
      });
      return "background: url(" + cover + ") center center / cover no-repeat";
    },
  },
  watch: {
    current(value) {
      this.current = value;
      this.listArchives();
    },
  },
  methods: {
    listArchives() {
      getArchives({ pageNum: this.current, pageSize: 10 }).then((res) => {
        this.archiveList = res.records;
        this.count = res.total;
      });
    },
  },
};
</script>

<style scoped lang="scss">
.count {
  font-size: 1.25rem;
  color: #555;
  margin-bottom: 1.5rem;
}
ul.timeline-wrapper {
  list-style: none;
  margin: 0;
  padding: 0;
}

/* 时间线 */
.timeline-item {
  position: relative;

  .timeline-box {
    text-align: center;
    position: absolute;

    .out-circle {
      width: 16px;
      height: 16px;
      background: rgba(14, 116, 218, 0.1);
      box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.1);
      border-radius: 50%;
      display: flex;
      align-items: center;

      .in-circle {
        width: 8px;
        height: 8px;
        margin: 0 auto;
        background: rgba(14, 116, 218, 1);
        border-radius: 50%;
        box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.1);
      }
    }

    .long-line {
      width: 2px;
      height: 78px;
      background: rgba(14, 116, 218, 1);
      box-shadow: 0px 4px 12px 0px rgba(0, 0, 0, 0.1);
      opacity: 0.1;
      margin-left: 8px;
    }
  }

  .timeline-content {
    box-sizing: border-box;
    margin-left: 20px;
    height: 106px;
    padding: 0 0 0 20px;
    text-align: left;
    margin-bottom: 5px;

    .timeline-title {
      font-size: 14px;
      word-break: break-all;
      margin-bottom: 10px;
      color: #333;
      font-weight: 500;
    }

    .timeline-date {
      font-size: 16px;
      color: #333;
      font-weight: 500;
      margin-bottom: 8px;
    }
    .timeline-desc {
      font-size: 14px;
      color: #999999;
    }
  }
}

.timeline-item:hover {
  .out-circle {
    background: rgba(176, 201, 53, 0.1);

    .in-circle {
      background:  #ff7242;
    }
  }
}
.timeline-item:last-of-type .timeline-content {
  margin-bottom: 0;
}
</style>

