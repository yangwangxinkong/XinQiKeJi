/**
 * 
 */

import axios from 'axios'

const service = axios.create({
    baseURL: process.env.BASE_API, // api的base_url
    timeout: 5000 // 请求超时时间
})

// request拦截器
service.interceptors.request.use(config => {

    return config
}, error => {
    console.log(error) // for debug
    Promise.reject(error)
})

// respone拦截器
service.interceptors.response.use(config => {
    return config;

}, function(error) {
    return Promise.reject(error);
})

export default service