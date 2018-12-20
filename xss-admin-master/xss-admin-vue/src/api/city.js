import request from '@/utils/request'

// 城市列表
export function fetchList(query) {
  return request({
    url: '/api/city/list',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/city/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 城市
export function save(data) {
  return request({
    url: 'api/city/save',
    method: 'post',
    data
  })
}

// 删除 城市
export function remove(query) {
  return request({
    url: '/api/city/delete',
    method: 'get',
    params: query
  })
}

// 所有城市
export function fetchCityList(query) {
  return request({
    url: '/api/city/list_all',
    method: 'get',
    params: query
  })
}
