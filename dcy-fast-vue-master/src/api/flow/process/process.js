import axios from '_l/api.request'

/**
 * 根据流程定义id 操作挂起激活 true 挂起， false 未挂起
 * @param procDefId
 * @returns {ClientHttp2Stream | * | AxiosPromise<any> | ClientRequest | void}
 */
export const hangChange = (procDefId) => {
  return axios.request({
    url: '/flow/process/hangChange',
    method: 'post',
    params: {
      processDefinitionId: procDefId
    }
  })
}

/**
 * 删除流程定义
 * @param deploymentId
 * @returns {AxiosPromise}
 */
export const deleteProcessByDeploymentId = (deploymentId) => {
  return axios.request({
    url: '/flow/process/delete',
    method: 'post',
    params: {
      deploymentId: deploymentId
    }
  })
}

export const flowNodeSave = (flowNodeUpdateInputDTOs) => {
  return axios.request({
    url: '/flow/flow-node/save',
    method: 'post',
    data: flowNodeUpdateInputDTOs
  })
}


export const getActivityListByProDefId = (processDefinitionId) => {
  return axios.request({
    url: '/flow/flow-node/getActivityListByProDefId',
    params: {
      processDefinitionId:processDefinitionId
    }
  })
}
