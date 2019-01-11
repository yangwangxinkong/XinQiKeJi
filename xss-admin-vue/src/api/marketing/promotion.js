import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/promotion/list',
    method: 'get',
    params: query
  })
}

export function info(id) {
  return request({
    url: '/api/promotion/info',
    method: 'get',
    params: {id: id}
  })
}

export function deletePromotion(ids) {
  return request({
    url: '/api/promotion/delete',
    method: 'post',
    params: {ids: ids}
  })
}

export function savePromotion(param) {
  return request({
    url: '/api/promotion/save',
    method: 'post',
    params: param
  })
}

export function updatePromotion(promotion) {
  return request({
    url: '/api/promotion/update',
    method: 'post',
    params: promotion
  })
}

export function addSelectData() {
  return request({
    url: '/api/promotion/add_select_data',
    method: 'get',
    params: {}
  })
}


