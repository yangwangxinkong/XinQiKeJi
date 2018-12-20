import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/api/navigation/list',
    method: 'get',
    params: data
  })
}

export function positionList() {
  return request({
    url: '/api/navigation/positionList',
    method: 'get'
  })
}

export function remove(query) {
  return request({
    url: '/api/navigation/delete',
    method: 'get',
    params: query
  })
}

export function save(data) {
  return request({
    url: '/api/navigation/save',
    method: 'post',
    data
  })
}

export function info(id) {
  return request({
    url: '/api/navigation/info',
    method: 'get',
    params:id
  })
}

