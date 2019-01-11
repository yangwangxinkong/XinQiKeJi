import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/changeApply/list',
    method: 'get',
    params: query
  })
}

export function fetchView(query) {
  return request({
    url: '/api/changeApply/view',
    method: 'get',
    params: query
  })
}

export function fetchApproval(query) {
  return request({
    url: '/api/changeApply/approve',
    method: 'post',
    params: query
  })
}

export function fetchReject(query) {
  return request({
    url: '/api/changeApply/reject',
    method: 'post',
    params: query
  })
}

export function fetchDeliveryInfo(query) {
  return request({
    url: '/api/change/delivery_query',
    method: 'get',
    params: query
  })
}
