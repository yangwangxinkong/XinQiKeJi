import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/api/tag/list',
    method: 'get',
    params:data
  })
}

export function remove(ids) {
  return request({
    url: '/api/tag/delete',
    method: 'get',
    params:ids
  })
}

export function findType() {
  return request({
    url: '/api/tag/findType',
    method: 'get'
  })
}

export function info(id) {
  return request({
    url: '/api/tag/info',
    method: 'get',
    params:id
  })
}

export function findTypeList(type) {
  return request({
    url: '/api/tag/findTypeList',
    method: 'get',
    params: {type}
  })
}

export function save(data) {
  return request({
    url: '/api/tag/save',
    method: 'post',
    data
  })
}
