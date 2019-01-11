import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/returnApply/list',
    method: 'get',
    params: query
  })
}

export function fetchView(sn) {
  return request({
    url: '/api/returnApply/view',
    method: 'get',
    params: { sn }
  })
}

export function fetchApproval(query) {
  return request({
    url: '/api/returnApply/approve',
    method: 'post',
    params: query
  })
}

export function fetchReject(query) {
  return request({
    url: '/api/returnApply/reject',
    method: 'post',
    params: query
  })
}
