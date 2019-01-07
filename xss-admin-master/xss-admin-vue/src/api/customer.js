import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/api/article/list',
    method: 'get',
    params: data
  })
}
export function customerList(data) {
  return request({
    url: '/api/customerEvaluation/list',
    method: 'get',
    params: data
  })
}

export function info(id) {
  return request({
    url: '/api/customerEvaluation/info',
    method: 'get',
    params: id
  })
}

export function save(data) {
  return request({
    url: '/api/customerEvaluation/save',
    method: 'post',
    params:data
  })
}


export function remove(ids) {
  return request({
    url: '/api/customerEvaluation/delete',
    /*url: '/api/customerEvaluation/list',*/
    method: 'get',
    params: ids
  })
}
