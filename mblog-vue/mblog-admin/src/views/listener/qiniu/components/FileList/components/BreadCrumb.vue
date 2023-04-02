<template>
  <div class="breadcrumb-wrapper">
    <div class="title">当前位置：</div>
    <div class="breadcrumb-box">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item
          v-for="(item, index) in breadCrumbList"
          :key="index"
          :to="{ query: { pid: item.id} }"
        >{{item.filename}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
  </div>
</template>

<script>
export default {
  name: "BreadCrumb",
  computed: {
    //  面包屑导航栏数组
    breadCrumbList() {
      return this.$store.getters.getFilePathList;
    },
  },
};
</script>

<style lang="scss" scoped>
.breadcrumb-wrapper {
  padding: 0px;
  height: 30px;
  line-height: 30px;
  display: flex;
  .title,
  ::v-deep .el-breadcrumb {
    height: 30px;
    line-height: 30px;
  }

  .breadcrumb-box {
    padding: 0 8px;
    flex: 1;
    display: flex;
  }
}
</style>