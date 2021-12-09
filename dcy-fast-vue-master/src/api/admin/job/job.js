import axios from '_l/api.request'

/**
 * 添加定时任务调度表
 * @param job
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addJob = (job) => {
  return axios.request({
    url: '/monitor/job/save',
    method: 'post',
    data: job
  })
}

/**
 * 运行一次
 * @param jobId
 * @returns {AxiosPromise}
 */
export const runJob = (jobId) => {
  return axios.request({
    url: '/monitor/job/run',
    method: 'post',
    params: {
      jobId: jobId
    }
  })
}

/**
 * 修改定时任务调度表
 * @param job
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateJob = (job) => {
  return axios.request({
    url: '/monitor/job/update',
    method: 'post',
    data: job
  })
}

/**
 * 切换状态
 * @param job
 * @returns {*}
 */
export const changeStatusJob = (job) => {
  return axios.request({
    url: '/monitor/job/changeStatus',
    method: 'post',
    data: job
  })
}

/**
 * 删除定时任务调度表
 * @param id
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteJobById = (id) => {
  return axios.request({
    url: '/monitor/job/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

/**
 * 批量删除定时任务调度表
 * @param ids
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchJobById = (ids) => {
  return axios.request({
    url: '/monitor/job/deleteBatch',
    method: 'post',
    data: ids
  })
}

/**
 * 校验cron表达式是否有效
 * @param cronExpression
 * @returns {AxiosPromise}
 */
export const checkCronExpressionIsValid = (cronExpression) => {
  return axios.request({
    url: '/monitor/job/checkCronExpressionIsValid',
    method: 'post',
    params: {
      cronExpression: cronExpression
    }
  })
}
