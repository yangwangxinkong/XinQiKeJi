import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/sales_ranking/list',
    method: 'get',
    params: query
  })
}
