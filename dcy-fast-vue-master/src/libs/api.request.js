import HttpRequest from '@/libs/axios'
// 多环境
const baseUrl = process.env.VUE_APP_BASE_API;

const axios = new HttpRequest(baseUrl)
export default axios
