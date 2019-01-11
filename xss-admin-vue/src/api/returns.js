import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/returns/list',
    method: 'get',
    params: query
  })
}

export function fetchView(id) {
  return request({
    url: '/api/returns/view',
    method: 'get',
    params: { id }
  })
}
