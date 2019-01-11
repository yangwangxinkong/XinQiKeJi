import request from '@/utils/request'

export function count() {
  return request({
    url: '/api/home/count',
    method: 'get'
  })
}
