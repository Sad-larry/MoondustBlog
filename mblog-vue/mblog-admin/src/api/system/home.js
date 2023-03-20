import request from '@/utils/request'

// 初始化图表数据
export function initChartData() {
  return request({
    url: '/system/home/init',
    method: 'get'
  })
}