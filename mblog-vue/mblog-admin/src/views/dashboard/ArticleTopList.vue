<template>
  <el-table :data="chartData" style="width: 100%; height: 300px;" max-height="300">
    <el-table-column prop="title" label="文章标题" width="180">
      <template slot-scope="scope">
        <el-link :underline="false" @click="onClick(scope.row)">{{ scope.row.title }}</el-link>
      </template>
    </el-table-column>
    <el-table-column prop="quantity" label="浏览量">
    </el-table-column>
  </el-table>
</template>

<script>

export default {
  props: {
    chartData: {
      type: Array,
      required: true,
      default: []
    }
  },
  methods: {
    onClick(row) {
      window.open("http://refrainblog.cn/article/" + row.id);
    }
  }
}
</script>
