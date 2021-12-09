import axios from '_l/api.request'

/**
 * 添加模型
 * @param config
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const addModel = (modelPO) => {
  return axios.request({
    url: '/flow/model/create',
    method: 'post',
    data: modelPO
  })
}

/**
 * 修改模型
 * @param modelPO
 * @returns {AxiosPromise}
 */
export const updateModel = (modelPO) => {
  return axios.request({
    url: '/flow/model/update',
    method: 'post',
    data: modelPO
  })
}

/**
 * 部署模型
 * @param config
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const deployModel = (modelId) => {
  return axios.request({
    url: '/flow/model/deploy',
    method: 'post',
    params: {
      modelId: modelId
    }
  })
}

/**
 * 删除模型
 * @param modelId
 * @returns {AxiosPromise}
 */
export const deleteModelById = (modelId) => {
  return axios.request({
    url: '/flow/model/delete',
    method: 'post',
    params: {
      modelId: modelId
    }
  })
}

/**
 * 根据modelId获取json
 * @param modelId
 * @returns {*}
 */
export const getModelJsonById = (modelId) => {
  return axios.request({
    url: '/flow/model/getModelJsonById',
    params: {
      modelId: modelId
    }
  })
}
