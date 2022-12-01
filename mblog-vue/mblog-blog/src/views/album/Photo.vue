<template>
  <div>
    <!-- banner -->
    <div class="banner" :style="cover">
      <h1 class="banner-title">{{ photoAlbumName }}</h1>
    </div>
    <!-- 相册列表 -->
    <v-card class="blog-container">
      <div class="photo-wrap">
        <img v-for="(item, index) of photoList" class="photo" :key="index" :src="item" @click="preview(index)" />
      </div>
      <!-- 无限加载 -->
      <infinite-loading @infinite="infiniteHandler">
        <div slot="no-more" />
        <div slot="no-results" />
      </infinite-loading>
    </v-card>
  </div>
</template>

<script>
import { getPhotos } from "@/api";

export default {
  data() {
    return {
      photoAlbumName: "",
      photoAlbumCover: "",
      photoList: [],
      current: 1,
      size: 10
    };
  },
  computed: {
    cover() {
      return (
        "background: url(" +
        this.photoAlbumCover +
        ") center center / cover no-repeat"
      );
    }
  },
  methods: {
    preview(index) {
      this.$imagePreview({
        images: this.photoList,
        index: index
      });
    },
    infiniteHandler($state) {
      getPhotos(
        {
          albumId: this.$route.params.albumId,
          pageNo: this.current,
          pageSize: this.size
        }).then(res => {
          this.photoAlbumCover = res.data.photoAlbumCover;
          this.photoAlbumName = res.data.photoAlbumName;
          if (res.data.photoList.length) {
            this.current++;
            this.photoList.push(...res.data.photoList);
            $state.loaded();
          } else {
            $state.complete();
          }
        });
    }
  },
};
</script>

<style scoped>
.photo-wrap {
  display: flex;
  flex-wrap: wrap;
}

.photo {
  margin: 3px;
  cursor: pointer;
  flex-grow: 1;
  object-fit: cover;
  height: 200px;
}

.photo-wrap::after {
  content: "";
  display: block;
  flex-grow: 9999;
}

@media (max-width: 759px) {
  .photo {
    width: 100%;
  }
}
</style>
