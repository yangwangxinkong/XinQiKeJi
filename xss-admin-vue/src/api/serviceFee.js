import request from '@/utils/request'

// 所有城市
export function fetchCityList(query) {
  return request({
    url: '/api/city/list_all',
    method: 'get',
    params: query
  })
}

// 服务费配置列表
export function fetchList(query) {
  return request({
    url: '/api/serviceFeeSetting/list',
    method: 'get',
    params: query
  })
}

/** 服务费配置 详情 */
export function fetchInfo(id) {
  return request({
    url: '/api/serviceFeeSetting/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 服务费配置
export function fetchSave(data) {
  return request({
    url: 'api/serviceFeeSetting/save',
    method: 'post',
    data
  })
}

// 删除 服务费配置
export function fetchRemove(query) {
  return request({
    url: '/api/serviceFeeSetting/delete',
    method: 'post',
    params: query
  })
}

