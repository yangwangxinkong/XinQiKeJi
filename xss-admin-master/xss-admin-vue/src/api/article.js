import request from '@/utils/request'

export function fetchList(data) {
  return request({
    url: '/api/article/list',
    method: 'get',
    params: data
  })
}

export function info(id) {
  return request({
    url: '/api/article/info',
    method: 'get',
    params: id
  })
}

export function save(data) {
  return request({
    url: '/api/article/save',
    method: 'post',
    data
  })
}


export function remove(ids) {
  return request({
    url: '/api/article/delete',
    method: 'get',
    params: ids
  })
}
