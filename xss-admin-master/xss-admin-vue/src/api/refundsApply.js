import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/refundsApply/list',
    method: 'get',
    params: query
  })
}

export function fetchView(sn) {
  return request({
    url: '/api/refundsApply/view',
    method: 'get',
    params: { sn }
  })
}

export function fetchApproval(sn) {
  return request({
    url: '/api/refundsApply/approve',
    method: 'post',
    params: { sn }
  })
}

export function fetchReject(query) {
  return request({
    url: '/api/refundsApply/reject',
    method: 'post',
    params: query
  })
}
