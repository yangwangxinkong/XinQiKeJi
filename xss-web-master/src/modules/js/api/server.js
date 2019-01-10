/**
 *公共接口
 */
import url from './apiUrl'
import fetch from '@/utils/fetch'
import storage from '@/utils/common'

/**
 * 公共函数get请求
 */
export function get(path, query) {
    //   if (storage.get('cityStorage')) {
    //     query.cityCode = storage.get('cityStorage').cityCode
    //   }
    console.log(path)
    console.log(query)
    return fetch({
        method: 'get',
        url: path,
        params: query
    })
}

/**
 * 公共函数post请求
 */
export function post(path, query) {
    //   if (storage.get('cityStorage')) {
    //     query.cityCode = storage.get('cityStorage').cityCode
    //   }
    return fetch({
        method: 'post',
        url: path,
        params: query
    })
}

/**
 * 公共函数excute请求
 */
export function execute(path, data) {
    //   if (storage.get('cityStorage')) {
    //     data.cityCode = storage.get('cityStorage').cityCode
    //   }
    return fetch({
        method: 'post',
        url: path,
        data
    })
}