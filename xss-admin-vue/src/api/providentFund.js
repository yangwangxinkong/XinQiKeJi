import request from '@/utils/request'

// 所有城市
export function fetchCityList(query) {
  return request({
    url: '/api/city/list_all',
    method: 'get',
    params: query
  })
}

// 公积金比例配置列表
export function fetchList(query) {
  return request({
    url: '/api/providentFundRatioSetting/list',
    method: 'get',
    params: query
  })
}

/** 公积金比例配置 详情 */
export function fetchInfo(id) {
  return request({
    url: '/api/providentFundRatioSetting/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 公积金比例配置
export function fetchSave(data) {
  return request({
    url: 'api/providentFundRatioSetting/save',
    method: 'post',
    data
  })
}

// 删除 公积金比例配置
export function fetchRemove(query) {
  return request({
    url: '/api/providentFundRatioSetting/delete',
    method: 'post',
    params: query
  })
}

