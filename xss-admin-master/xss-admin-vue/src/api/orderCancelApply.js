import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/orderCancelApply/list',
    method: 'get',
    params: query
  })
}

export function fetchView(sn) {
  return request({
    url: '/api/orderCancelApply/view',
    method: 'get',
    params: { sn }
  })
}

export function fetchApproval(query) {
  return request({
    url: '/api/orderCancelApply/approve',
    method: 'post',
    params: query
  })
}

export function fetchReject(query) {
  return request({
    url: '/api/orderCancelApply/reject',
    method: 'post',
    params: query
  })
}

//获取卖家，买家物流信息
/*export function fetchDeliveryInfo(query) {
  return request({
    url: '/api/orderCancelApply/delivery_info',
    method: 'get',
    params: query
  })
}*/
