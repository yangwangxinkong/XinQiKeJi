import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/coupon/list',
    method: 'get',
    params: query
  })
}

export function fetchCodeList(query) {
  return request({
    url: '/api/coupon/code/list',
    method: 'get',
    params: query
  })
}

export function info(id) {
  return request({
    url: '/api/coupon/info',
    method: 'get',
    params: {couponId: id}
  })
}

export function deleteCoupon(ids) {
  return request({
    url: '/api/coupon/delete',
    method: 'post',
    params: {ids: ids}
  })
}

export function buildCoupon(couponId) {
  return request({
    url: '/api/coupon/build',
    method: 'get',
    params: {id: couponId}
  })
}

export function downloadCoupon(couponId, count) {
  return request({
    url: '/api/coupon/download',
    method: 'post',
    params: {id: couponId, count: count}
  })
}

export function saveCoupon(coupon) {
  return request({
    url: '/api/coupon/save',
    method: 'post',
    params: coupon
  })
}

export function updateCoupon(coupon) {
  return request({
    url: '/api/coupon/update',
    method: 'post',
    params: coupon
  })
}



