import request from '@/utils/request'

// 发票列表
export function fetchList(query) {
  return request({
    url: '/api/invoice/list',
    method: 'get',
    params: query
  })
}

// 发票详情
export function fetchView(id) {
  return request({
    url: '/api/invoice/info',
    method: 'get',
    params: { id }
  })
}

// 更新发票状态
export function fetchUpdate(query) {
  return request({
    url: '/api/invoice/update',
    method: 'post',
    params: query
  })
}
