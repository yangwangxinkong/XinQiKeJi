import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/shipping/list',
    method: 'get',
    params: query
  })
}

export function fetchView(id) {
  return request({
    url: '/api/shipping/view',
    method: 'get',
    params: { id }
  })
}

//删除
export function remove(query) {
  return request({
    url: '/api/shipping/delete',
    method: 'get',
    params: query
  })
}
