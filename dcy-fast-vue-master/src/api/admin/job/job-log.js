import axios from '_l/api.request'

/**
 * 删除定时任务调度日志表
 * @param id
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteJobLogById = (id) => {
  return axios.request({
    url: '/monitor/jobLog/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

/**
 * 批量删除定时任务调度日志表
 * @param ids
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteBatchJobLogById = (ids) => {
  return axios.request({
    url: '/monitor/jobLog/deleteBatch',
    method: 'post',
    data: ids
  })
}

/**
 * 清空日志
 * @returns {AxiosPromise}
 */
export const cleanJobLog = () => {
  return axios.request({
    url: '/monitor/jobLog/clean',
    method: 'post'
  })
}
