import request from '@/utils/request'

// 订单列表
export function fetchList(query) {
  return request({
    url: '/api/order/activity_order_list',
    method: 'get',
    params: query
  })
}

// 订单详情
export function fetchView(id) {
  return request({
    url: '/api/order/activity_order_view',
    method: 'get',
    params: { id }
  })
}

// 删除订单
export function fetchRemove(query) {
  return request({
    url: '/api/order/delete',
    method: 'get',
    params: query
  })
}

// 确认订单
export function fetchConfirm(query) {
  return request({
    url: '/api/order/activity_order_confirm',
    method: 'post',
    params: query
  })
}

// 取消订单
export function fetchCancel(query) {
  return request({
    url: '/api/order/activity_order_cancel',
    method: 'post',
    params: query
  })
}

// 支付
export function fetchPay(query) {
  return request({
    url: '/api/order/activity_order_payment',
    method: 'post',
    params: query
  })
}

// 订单完成
export function fetchCompleted(query) {
  return request({
    url: '/api/order/activity_order_complete',
    method: 'post',
    params: query
  })
}

// 订单退款承认
export function fetchApproval(query) {
  return request({
    url: '/api/refundsApply/activity_order_approve',
    method: 'post',
    params: query
  })
}

// 订单退款拒绝
export function fetchReject(query) {
  return request({
    url: '/api/refundsApply/activity_order_reject',
    method: 'post',
    params: query
  })
}
