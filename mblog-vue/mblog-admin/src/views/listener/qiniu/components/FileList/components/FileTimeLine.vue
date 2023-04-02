<template>
  <!-- 时间线模式 -->
  <div class="image-timeline-wrapper">
    <div class="radio">
      排序：
      <el-radio-group v-model="reverse">
        <el-radio :label="true">倒序</el-radio>
        <el-radio :label="false">正序</el-radio>
      </el-radio-group>
    </div>
    <el-timeline class="image-timeline-list" :reverse="reverse" v-if="imageTimelineData.length">
      <el-timeline-item
        class="image-timeline-item"
        v-for="(item, index) in imageTimelineData"
        :key="index"
        :timestamp="item.uploadDate"
        color="#409EFF"
        placement="top"
      >
        <ul class="image-list">
          <li class="image-item" v-for="(image, imageIndex) in item.imageList" :key="`${index}-${imageIndex}`">
            <img
              class="image"
              :src="image.url"
              :alt="item | filenameComplete"
            />
            <div class="image-name">{{ image | filenameComplete }}</div>
          </li>
        </ul>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>

<script>
export default {
  name: "FileTimeLine",
  props: {
    fileList: Array,
  },
  data() {
    return {
      reverse: true,
    };
  },
  computed: {
    //  按年-月-日分组排序
    imageTimelineData() {
      let res = [];
      //  去重，获取返回的所有日期年-月-日
      let uploadTimeSet = new Set(
        this.fileList
          .filter((item) => !item.dir)
          .map((item) => item.createTime.split(" ")[0])
      );
      let uploadDate = [...uploadTimeSet];
      //  分组
      uploadDate.forEach((element) => {
        res.push({
          uploadDate: element,
          imageList: this.fileList.filter(
            (item) => !item.dir && item.createTime.split(" ")[0] === element
          ), //  过滤
        });
      });
      return res;
    },
  },
};
</script>

<style lang="scss" scoped>
.image-timeline-wrapper {
  margin-top: 20px;
  height: calc(100vh - 215px);
  overflow-y: auto;
  &::-webkit-scrollbar {
    width: 6px;
  }
  //  修改滚动条的下面的样式
  &::-webkit-scrollbar-track {
    background-color: transparent;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
  }
  //  修改滑块
  &::-webkit-scrollbar-thumb {
    background-color: #c0c4cc;
    -webkit-border-radius: 2em;
    -moz-border-radius: 2em;
    border-radius: 2em;
  }

  .image-timeline-list {
    margin-top: 10px;

    .image-timeline-item {
      .image-list {
        display: flex;
        flex-wrap: wrap;
        list-style: none;

        .image-item {
          margin: 0 16px 16px 0;
          padding: 8px;
          text-align: center;

          &:hover {
            background: #f5f7fa;

            .file-name {
              font-weight: 550;
            }
          }
          .image {
            height: 80px;
          }

          .image-name {
            margin-top: 8px;
            height: 44px;
            line-height: 22px;
            font-size: 12px;
            word-break: break-all;
            &::-webkit-scrollbar {
              width: 2px;
            }
            &::-webkit-scrollbar-track {
              background-color: #ebeef5;
              -webkit-border-radius: 2em;
              -moz-border-radius: 2em;
              border-radius: 2em;
            }
            &::-webkit-scrollbar-thumb {
              background-color: #909399;
              -webkit-border-radius: 2em;
              -moz-border-radius: 2em;
              border-radius: 2em;
            }

            ::v-deep .keyword {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }
}
</style>
