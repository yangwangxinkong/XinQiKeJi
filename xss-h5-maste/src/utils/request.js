require('es6-promise').polyfill();
import axios from 'axios'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.BASE_API, // api的base_url
  timeout: 50000 // request timeout
})
console.log(process.env.BASE_API)
// request interceptor
service.interceptors.request.use(config => {
  // Do something before request is sent
  const token = localStorage.getItem("cToken")
 if (token) {
    //让每个请求携带token-- ['X-Token']为自定义key 请根据实际情况自行修改
    config.headers['ctoken'] = token
 }
 config.headers['Authorization'] = getToken()

  return config
}, error => {
  // Do something with request error
  console.log(error) // for debug
  Promise.reject(error)
})

// respone interceptor
service.interceptors.response.use(
  response => response,
  error => {
    console.log('err' + error) // for debug

    return Promise.reject(error)
  })

export default service
