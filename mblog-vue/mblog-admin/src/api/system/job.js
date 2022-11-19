import request from '@/utils/request'

// 查询定时任务调度列表
export function listJob(query) {
  return request({
    url: '/system/job/list',
    method: 'get',
    params: query
  })
}

// 查询定时任务调度详细
export function getJob(jobId) {
  return request({
    url: '/system/job/' + jobId,
    method: 'get'
  })
}

// 新增定时任务调度
export function addJob(data) {
  return request({
    url: '/system/job',
    method: 'post',
    data: data
  })
}

// 修改定时任务调度
export function updateJob(data) {
  return request({
    url: '/system/job',
    method: 'put',
    data: data
  })
}

// 删除定时任务调度
export function delJob(jobId) {
  return request({
    url: '/system/job/' + jobId,
    method: 'delete'
  })
}

// 立即执行一次任务
export function run(data) {
  return request({
    url: '/system/job/run',
    method: 'post',
    data: data
  })
}

// 修改定时任务状态
export function change(data) {
  return request({
    url: '/system/job/change',
    method: 'put',
    data: data
  })
}