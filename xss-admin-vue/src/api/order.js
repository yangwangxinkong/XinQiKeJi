import request from '@/utils/request'

// 订单列表
export function fetchList(query) {
  return request({
    url: '/api/order/list',
    method: 'get',
    params: query
  })
}
// 订单列表
export function fetchExchargeList(query) {
  return request({
    url: '/api/order/excharge/list',
    method: 'get',
    params: query
  })
}

// 订单详情
export function fetchView(id) {
  return request({
    url: '/api/order/info',
    method: 'get',
    params: { id }
  })
}
// 订单详情
export function fetchExchargeView(id) {
  return request({
    url: '/api/order/view',
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
    url: '/api/order/confirm',
    method: 'post',
    params: query
  })
}

// 取消订单
export function fetchCancel(query) {
  return request({
    url: '/api/order/cancel',
    method: 'post',
    params: query
  })
}

// 获取方式和支付方式 下拉框列表
export function fetchMethods() {
  return request({
    url: '/api/order/methods',
    method: 'get'
  })
}

// 支付
export function fetchPay(query) {
  return request({
    url: '/api/order/payment',
    method: 'post',
    params: query
  })
}

// 获取配送方式,物流公司 下拉框列表
export function fetchShippings() {
  return request({
    url: '/api/order/shippings',
    method: 'get'
  })
}

//发货
export function fetchShipping(query) {
  return request({
    url: '/api/order/shipping',
    method: 'post',
    params: query
  })
}

//订单完成
export function fetchCompleted(query) {
  return request({
    url: '/api/order/complete',
    method: 'post',
    params: query
  })
}

//订单发货超时退款
export function fetchShippingTimeOut(query) {
  return request({
    url: '/api/order/shipping_timeout',
    method: 'post',
    params: query
  })
}

// 订单编辑
export function fetchEdit(query) {
  return request({
    url: '/api/order/edit',
    method: 'get',
    params: query
  })
}

// 订单更新
export function fetchUpdate(query) {
  return request({
    url: '/api/order/update',
    method: 'post',
    params: query
  })
}

// 添加订单商品
export function fetchGetProduct(query) {
  return request({
    url: '/api/order/order_item_add',
    method: 'get',
    params: query
  })
}
