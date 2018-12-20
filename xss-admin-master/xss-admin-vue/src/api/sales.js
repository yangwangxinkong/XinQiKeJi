import request from '@/utils/request'

export function info(query) {
  return request({
    url: '/api/sales/info',
    method: 'get',
    params: query
  })
}

export function typeList() {
  return request({
    url: '/api/sales/typeList',
    method: 'get'
  })
}
