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
      default: '350px'
    },
    autoResize: {
      type: Boolean,
      default: true
    },
    chartData: {
      type: Object,
      required: true,
      default: function() {
        return {
          contributeCount: [],
          contributeDate: []
        }
      }
    }
  },
  data() {
    return {
      chart: null,
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
      // this.setOptions(this.chartData)
    },
    setOptions({ contributeCount, contributeDate } = {}) {
      this.chart.setOption({
        title: {
          top: 30,
          text: '文章贡献度',
          subtext: '一年内博客提交的数量',
          left: 'center',
          textStyle: {
            color: '#000'
          }
        },
        tooltip: {
          trigger: 'item',
          formatter: function (params) {
            return (params.data[0] + '<br>文章数：' + params.data[1])
          }
        },
        legend: {
          top: '30',
          left: '100',
          data: ['文章数', 'Top 12'],
          textStyle: {
            // 设置字体颜色
            color: '#000'
          }
        },
        calendar: [{
          top: 120,
          left: 50,
          right: 10,
          range: contributeDate,
          splitLine: {
            show: true,
            lineStyle: {
              // 设置月份分割线的颜色
              color: '#D3D3D3',
              width: 4,
              type: 'solid'
            }
          },
          yearLabel: { show: false },
          dayLabel: {
            nameMap: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"], // 设置中文显示
            textStyle: {
              // 设置周显示颜色
              color: '#000'
            }
          },
          monthLabel: {
            nameMap: 'cn', // 设置中文显示
            textStyle: {
              // 设置月显示颜色
              color: '#000'
            }
          },
          itemStyle: {
            normal: {
              // 设置背景颜色
              color: '#ffffff',
              borderWidth: 1,
              // 设置方块分割线段颜色
              borderColor: '#D3D3D3'
            }
          }
        }],
        series: [
          {
            name: '文章数',
            type: 'scatter',
            coordinateSystem: 'calendar',
            data: contributeCount,
            // 根据值设置原点大小
            symbolSize: function (val) {
              if (val[1] == 0) {
                return val[1];
              } else {
                let size = 8 + val[1] * 2;
                if (size > 18) {
                  size = 18;
                }
                return size;
              }

            },
            itemStyle: {
              normal: {
                // 设置圆点颜色
                color: '#2ec7c9'
              }
            }
          }]
      })
    }
  }
}
</script>
