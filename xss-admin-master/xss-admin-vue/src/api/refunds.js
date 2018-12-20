import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/refunds/list',
    method: 'get',
    params: query
  })
}

export function fetchView(id) {
  return request({
    url: '/api/refunds/view',
    method: 'get',
    params: { id }
  })
}

//删除
export function remove(query) {
  return request({
    url: '/api/refunds/delete',
    method: 'get',
    params: query
  })
}

