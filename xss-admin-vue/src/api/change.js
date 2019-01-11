import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/change/list',
    method: 'get',
    params: query
  })
}

export function fetchView(id) {
  return request({
    url: '/api/change/view',
    method: 'get',
    params: { id }
  })
}
