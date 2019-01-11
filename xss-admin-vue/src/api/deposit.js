import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/deposit/list',
    method: 'get',
    params: query
  })
}
