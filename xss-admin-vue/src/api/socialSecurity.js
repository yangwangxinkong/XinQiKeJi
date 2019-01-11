import request from '@/utils/request'

// 所有城市
export function fetchCityList(query) {
  return request({
    url: '/api/city/list_all',
    method: 'get',
    params: query
  })
}

// 根据城市id取户口性质
export function fetchResidenceTypeList(query) {
  return request({
    url: '/api/residenceType/listByCityId',
    method: 'get',
    params: query
  })
}

// 社保比例配置列表
export function fetchList(query) {
  return request({
    url: '/api/socialSecurityRatioSetting/list',
    method: 'get',
    params: query
  })
}

/** 社保比例配置 详情 */
export function fetchInfo(id) {
  return request({
    url: '/api/socialSecurityRatioSetting/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 社保比例配置
export function fetchSave(data) {
  return request({
    url: 'api/socialSecurityRatioSetting/save',
    method: 'post',
    data
  })
}

// 删除 社保比例配置
export function fetchRemove(query) {
  return request({
    url: '/api/socialSecurityRatioSetting/delete',
    method: 'post',
    params: query
  })
}

