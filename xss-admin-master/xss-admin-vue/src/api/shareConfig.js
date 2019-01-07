import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/api/article/list',
    method: 'get',
    params: data
  })
}
export function shareConfigList(data) {
  return request({
    url: '/api/shareConfig/list',
    method: 'get',
    params: data
  })
}

export function info(id) {
  return request({
    url: '/api/shareConfig/info',
    method: 'get',
    params: id
  })
}

export function save(data) {
  return request({
    url: '/api/shareConfig/save',
    method: 'post',
    params:data
  })
}


export function remove(ids) {
  return request({
    url: '/api/shareConfig/delete',
    method: 'get',
    params: ids
  })
}
