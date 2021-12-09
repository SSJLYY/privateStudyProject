import axios from '_l/api.request'

/**
 * 添加部门表
 * @param dept
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addDept = (dept) => {
  return axios.request({
    url: '/system/dept/save',
    method: 'post',
    data: dept
  })
}

/**
 * 修改部门表
 * @param dept
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const updateDept = (dept) => {
  return axios.request({
    url: '/system/dept/update',
    method: 'post',
    data: dept
  })
}

/**
 * 删除部门表
 * @param deptId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deleteDeptById = (deptId) => {
  return axios.request({
    url: '/system/dept/delete',
    method: 'post',
    params: {
      id: deptId
    }
  })
}

/**
 * 获取tree数据
 * @param deptId
 * @returns {AxiosPromise}
 */
export const getDeptTreeList = () => {
  return axios.request({
    url: '/system/dept/getDeptTreeList',
    method: 'get'
  })
}
