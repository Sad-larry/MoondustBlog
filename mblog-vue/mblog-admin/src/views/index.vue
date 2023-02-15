<template>
  <div class="dashboard-editor-container">

    <panel-group :chart-data="initChartData.groupCountData" @handleSetLineChartData="handleSetLineChartData" />

    <el-row style="background:#fff;padding:16px 16px 0;margin-bottom:32px;">
      <calendar-chart :chart-data="initChartData.blogContributeCount" />
    </el-row>

    <el-row :gutter="32">
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <article-top-list :chart-data="initChartData.blogReadVolume" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <pie-chart :chart-data="initChartData.blogCategory" />
        </div>
      </el-col>
      <el-col :xs="24" :sm="24" :lg="8">
        <div class="chart-wrapper">
          <span style="float: left;">文章标签词云</span>
          <tag-cloud :data="initChartData.blogTag" />
        </div>
      </el-col>
    </el-row>

  </div>
</template>

<script>
import { initChartData } from "@/api/system/home"
import PanelGroup from './dashboard/PanelGroup'
import PieChart from './dashboard/PieChart'
import BarChart from './dashboard/BarChart'
import CalendarChart from './dashboard/CalendarChart'
import ArticleTopList from './dashboard/ArticleTopList'
import TagCloud from "@/components/TagCloud/index";

export default {
  name: 'Index',
  components: {
    PanelGroup,
    PieChart,
    BarChart,
    CalendarChart,
    ArticleTopList,
    TagCloud
  },
  data() {
    return {
      initChartData: {
        groupCountData: {},
        blogContributeCount: {},
        blogReadVolume: [],
        blogCategory: [],
        blogTag: [],
        weeklyVisits: {}
      }
    }
  },
  mounted() {
    initChartData().then(res => {
      this.initChartData = res.data
    })
  },
  methods: {
    handleSetLineChartData(type) {

    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-editor-container {
  padding: 32px;
  background-color: rgb(240, 242, 245);
  position: relative;

  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 32px;
  }
}

@media (max-width:1024px) {
  .chart-wrapper {
    padding: 8px;
  }
}
</style>
