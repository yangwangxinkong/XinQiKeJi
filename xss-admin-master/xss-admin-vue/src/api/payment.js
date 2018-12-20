import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/payment/list',
    method: 'get',
    params: query
  })
}

export function fetchView(id) {
  return request({
    url: '/api/payment/view',
    method: 'get',
    params: { id }
  })
}

//删除
export function remove(query) {
  return request({
    url: '/api/payment/delete',
    method: 'get',
    params: query
  })
}
