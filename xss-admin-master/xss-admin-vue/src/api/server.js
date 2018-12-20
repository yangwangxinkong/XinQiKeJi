/**
 * Created by admin on 2018-09-03.
 */
import url from './apiUrl'
import request from '@/utils/request'

/**
 * 公共函数get请求
 */
export function get (path, query) {
   return request({
    method: 'get',
    url: path,
    params: query
  })
}

/**
 * 公共函数post请求
 */
export function post (path, query) {
   return request({
    method: 'post',
    url: path,
    params: query
  })
}

/**
 * 公共函数post请求
 */
// export function download(path, query) {
//   return request({
//     method: 'post',
//     headers: { 'Content-Type' : 'multipart/form-data' },
//     url: path,
//     params: query
//  })
// }

/**
 * 公共函数excute请求
 */
export function execute (path, data) {
   return request({
    method: 'post',
    url: path,
    data
  })
}


