import request from '@/utils/request'

// 查询表盘数据
export function lineCount() {
  return request({
    url: '/system/home/lineCount',
    method: 'get'
  })
}

// 初始化图表数据
export function initChartData() {
  return request({
    url: '/system/home/init',
    method: 'get'
  })
}