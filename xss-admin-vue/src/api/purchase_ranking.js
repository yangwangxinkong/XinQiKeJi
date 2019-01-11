import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/purchase_ranking/list',
    method: 'get',
    params: query
  })
}
