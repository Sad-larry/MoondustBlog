<template>
  <!-- 时间线模式 -->
  <div class="image-timeline-wrapper">
    <div class="radio">
      排序：
      <el-radio-group v-model="reverse" :disabled="!imageTimelineData.length" @change="changeReverse">
        <el-radio :label="true">倒序</el-radio>
        <el-radio :label="false">正序</el-radio>
      </el-radio-group>
    </div>
    <el-timeline class="image-timeline-list" :reverse="reverse" v-if="imageTimelineData.length">
      <el-timeline-item
        class="image-timeline-item"
        v-for="(item, index) in imageTimelineData"
        :key="index"
        :timestamp="item.createTime"
        color="#409EFF"
        placement="top"
      >
        <ul class="image-list">
          <li class="image-item" v-for="(image, imageIndex) in item.imageList" :key="`${index}-${imageIndex}`">
            <img class="image" :src="image.url" :alt="item | filenameComplete" />
            <div class="image-name">{{ image | filenameComplete }}</div>
          </li>
        </ul>
      </el-timeline-item>
    </el-timeline>
    <div v-else class="noData">当前目录暂无图片~</div>
  </div>
</template>

<script>
export default {
  name: "FileTimeLine",
  props: {
    fileList: Array,
    sortReverse: Boolean,
  },
  data() {
    return {
      reverse: this.sortReverse,
    };
  },
  watch: {
    sortReverse(newValue) {
      this.reverse = newValue;
    },
  },
  computed: {
    // 因为都是同一天的文件，所以按时-分-秒分组排序
    imageTimelineData() {
      let res = [];
      // 去重，获取返回的所有日期时-分-秒
      let uploadTimeSet = new Set(
        this.fileList
          .filter((item) => !item.dir)
          .map((item) => item.createTime.split(" ")[1])
      );
      // 一定要排序不然会乱掉
      let createTime = [...uploadTimeSet].sort();
      //  分组
      createTime.forEach((element) => {
        res.push({
          createTime: element,
          imageList: this.fileList.filter(
            (item) => !item.dir && item.createTime.split(" ")[1] === element
          ), //  过滤
        });
      });
      return res;
    },
  },
  methods: {
    /** 改变排序方式 */
    changeReverse(value) {
      this.$emit("setSortReverse", value);
    },
  },
};
</script>

<style lang="scss" scoped>
.image-timeline-wrapper {
  margin-bottom: 20px;
  height: calc(100vh - 215px);
  overflow-y: auto;
  // border-top: 1px solid #dcdfe6;
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
  .radio {
    margin-top: 10px;
  }
  .image-timeline-list {
    margin-top: 20px;

    .image-timeline-item {
      margin-left: 14px;
      .image-list {
        display: flex;
        flex-wrap: wrap;
        list-style: none;

        .image-item {
          margin: 0 16px 16px 0;
          padding: 8px;
          text-align: center;
          width: 200px;

          &:hover {
            background: #f5f7fa;

            .file-name {
              font-weight: 550;
            }
          }
          .image {
            max-width: 100%;
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
  .noData {
    margin: 20px 0;
    font-size: 14px;
    color: #ccc;
    width: 100%;
    text-align: center;
  }
}
</style>
