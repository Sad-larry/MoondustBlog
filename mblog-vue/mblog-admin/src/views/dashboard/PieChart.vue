<template>
  <div :class="className" :style="{ height: height, width: width }" />
</template>

<script>
import * as echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '300px'
    },
    chartData: {
      type: Array,
      required: true,
      default: []
    }
  },
  data() {
    return {
      chart: null
    }
  },
  watch: {
    chartData: {
      deep: true,
      handler(val) {
        this.setOptions(val)
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$el, 'macarons')
      this.setOptions(this.chartData)
    },
    setOptions(baseData = []) {
      const optionData = this.generateDate(baseData)
      this.chart.setOption({
        title: {
          text: '文章分类统计',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b} : {c} 篇 ({d}%)'
        },
        legend: {
          type: 'scroll',
          left: 'center',
          bottom: '20',
          data: optionData.legendData,
          selected: optionData.selectedLegent
        },
        series: [
          {
            name: '文章数',
            type: 'pie',
            roseType: 'radius',
            radius: [15, 65],
            center: ['50%', '45%'],
            data: optionData.seriesData,
            animationEasing: 'cubicInOut',
            animationDuration: 2600
          }
        ]
      })
    },
    generateDate(data) {
      const legendData = [];
      const selectedLegent = {};
      const seriesData = [];
      data.forEach(item => {
        legendData.push(item.name);
        if (item.articleNum === 0) {
          // 若文章数为0，则默认置灰
          selectedLegent[item.name] = false
        }
        seriesData.push({
          name: item.name,
          value: item.articleNum
        });
      })
      return {
        legendData: legendData,
        selectedLegent: selectedLegent,
        seriesData: seriesData
      };
    }
  }
}
</script>
