<template>
  <!-- <div class="app-container">
    <el-row :gutter="10">
      <el-col :span="16" style="height: 240px;">
        <el-tree :data="fileList" :props="defaultProps" accordion @node-click="handleNodeClick">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span>
              <el-button v-if="node.isLeaf" type="text" size="mini" @click="deleteImage(node, data)">删除文件</el-button>
            </span>
          </span>
        </el-tree>
      </el-col>
      <el-col :span="8">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>图片预览</span>
          </div>
          <div>
            <el-skeleton style="width: 240px" animated :loading="imgLoading">
              <template slot="template">
                <el-skeleton-item variant="image" style="width: 240px; height: 240px;" />
                <div style="margin-top: 14px;">
                  <el-skeleton-item variant="div" style="width: 70%" />
                  <el-skeleton-item variant="div" style="width: 100%" />
                </div>
              </template>
              <template>
                <img :src="showItem.image" class="image multi-content" />
                <div style="padding: 14px;">
                  <span>文件名称: {{ showItem.image }}</span>
                  <div class="bottom card-header">
                    <span class="time">更新时间: {{ showItem.putTime }}</span>
                  </div>
                </div>
              </template>
            </el-skeleton>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>-->
  <div class="app-container">
    <file-list />
  </div>
</template>

<script>
import { listFile } from "@/api/system/qiniu";
import FileList from "./components/FileList";

export default {
  components: { FileList },
  data() {
    return {
      imgBaseUrl: process.env.VUE_APP_IMG_API,
      imgLoading: true,
      showItem: {
        // 卡片图片预览图
        image: null,
        name: "",
        putTime: null,
      },
      // 文件列表
      fileList: [],
      defaultProps: {
        children: "children",
        label: "fileName",
      },
      queryParams: {},
    };
  },
  created() {
    // this.getList();
  },
  methods: {
    /** 查询七牛云文件列表 */
    getList() {
      listFile(this.queryParams).then((response) => {
        this.$modal.msgSuccess("数据已更新");
        this.fileList = response.data;
      });
    },
    handleNodeClick(data) {
      if (!data.dir) {
        this.imgLoading = false;
        this.showItem.image = this.imgBaseUrl + data.key;
        this.showItem.name = data.fileName;
        this.showItem.putTime = data.putTime;
      }
    },
    deleteImage(node, data) {
      this.$modal.msgError("正在开发中");
    },
  },
};
</script>

<style scoped>
.time {
  font-size: 13px;
  color: #999;
}

.bottom {
  margin-top: 13px;
  line-height: 12px;
}

.button {
  padding: 0;
  float: right;
}

.image {
  width: 100%;
  display: block;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 14px;
  padding-right: 8px;
}
</style>