import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/api/dimension/list',
    method: 'get',
    params: data
  })
}

export function save(data) {
  return request({
    url: '/api/dimension/save',
    method: 'post',
    data
  })
}

export function info(id) {
  return request({
    url: '/api/dimension/info',
    method: 'get',
    params:id
  })
}

export function remove(ids) {
  return request({
    url: '/api/dimension/delete',
    method: 'get',
    params: ids
  })
}

export function companyCategory() {
  return request({
    url: '/api/dimension/companyCategory',
    method: 'get'
  })
}

export function checkCode(code) {
  return request({
    url: '/api/dimension/check_code',
    method: 'get',
    params : {code}
  })
}

export function checkSize(id) {
  return request({
    url: '/api/dimension/check_size',
    method: 'get',
    params : {id}
  })
}
