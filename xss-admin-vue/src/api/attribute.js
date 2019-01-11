import request from '@/utils/request'

//商品属性列表
export function fetchList(query) {
  return request({
    url: '/api/attribute/list',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/attribute/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新商品属性
export function save(data) {
  return request({
    url: 'api/attribute/save',
    method: 'post',
    data
  })
}

//删除商品属性
export function remove(query) {
  return request({
    url: '/api/attribute/delete',
    method: 'get',
    params: query
  })
}




