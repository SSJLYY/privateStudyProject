import axios from '_l/api.request'

/**
 * 添加
 * @param leave
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const saveLeave = (leave) => {
  return axios.request({
    url: '/oa/leave/save',
    method: 'post',
    data: leave
  })
}

/**
 * 提交
 * @param leave
 * @returns {*}
 */
export const submitLeave = (leave) => {
  return axios.request({
    url: '/oa/leave/submit',
    method: 'post',
    data: leave
  })
}

/**
 * 重新提交
 * @param leave
 * @returns {*}
 */
export const reSubmitLeave = (leave) => {
  return axios.request({
    url: '/oa/leave/reSubmit',
    method: 'post',
    data: leave
  })
}

/**
 * 创建并且提交
 * @param leave
 * @returns {*}
 */
export const createAndSubmitLeave = (leave) => {
  return axios.request({
    url: '/oa/leave/createAndSubmit',
    method: 'post',
    data: leave
  })
}

/**
 * 删除
 * @param id
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteLeaveById = (id) => {
  return axios.request({
    url: '/oa/leave/delete',
    method: 'post',
    params: {
      id: id
    }
  })
}

/**
 * 获取详细
 * @param id
 * @returns {AxiosPromise}
 */
export const getLeaveById = (id) => {
  return axios.request({
    url: '/oa/leave/getLeaveById',
    params: {
      id: id
    }
  })
}
