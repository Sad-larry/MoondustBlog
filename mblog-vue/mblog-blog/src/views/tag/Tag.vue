<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">标签</h1>
    </div>
    <!-- 标签列表 -->
    <v-card class="blog-container">
      <div class="tag-cloud-title">标签 - {{ tagList.length }}</div>
      <div class="tag-cloud">
        <router-link :style="{ 'font-size': Math.floor(Math.random() * 10) + 18 + 'px' }" v-for="item of tagList"
          :key="item.id" :to="'/tags/' + item.id">
          {{ item.name }}
        </router-link>
      </div>
    </v-card>
  </div>
</template>

<script>
import { getTags } from '@/api'
export default {
  data() {
    return {
      tagList: [],
      count: 0
    };
  },
  created() {
    this.listTags();
  },
  computed: {
    cover() {
      var cover = "";
      this.$store.state.blogInfo.pageList.forEach(item => {
        if (item.pageLabel === "tag") {
          cover = item.pageCover;
        }
      });
      return "background: url(" + cover + ") center center / cover no-repeat";
    }
  },
  methods: {
    listTags() {
      getTags().then(res => {
        this.tagList = res.data;
      });
    }
  },
};
</script>

<style scoped>
.tag-cloud-title {
  line-height: 2;
  font-size: 36px;
  text-align: center;
}

@media (max-width: 759px) {
  .tag-cloud-title {
    font-size: 25px;
  }
}

.tag-cloud {
  text-align: center;
}

.tag-cloud a {
  color: #616161;
  display: inline-block;
  text-decoration: none;
  padding: 0 8px;
  line-height: 2;
  transition: all 0.3s;
}

.tag-cloud a:hover {
  color: #03a9f4 !important;
  transform: scale(1.1);
}
</style>
