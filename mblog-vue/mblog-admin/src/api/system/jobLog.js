import request from '@/utils/request'

// 查询定时任务调度列表
export function listJobLog(params) {
  return request({
    url: '/system/jobLog/list',
    method: 'get',
    params: params
  })
}
// 删除任务日志
export function delJobLog(jobLogIds) {
  return request({
    url: '/system/jobLog/' + jobLogIds,
    method: 'delete',
  })
}
// 清空定时任务日志
export function cleanJobLog() {
  return request({
    url: '/system/jobLog/clean',
    method: 'get',
  })
}

