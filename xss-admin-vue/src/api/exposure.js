import request from '@/utils/request'

// 曝光 列表
export function fetchExposureList(query) {
  return request({
    url: '/api/exposure/list',
    method: 'get',
    params: query
  })
}

// 曝光 详情
export function info(id) {
  return request({
    url: '/api/exposure/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 曝光
export function save(data) {
  return request({
    url: 'api/exposure/save',
    method: 'post',
    data
  })
}

// 删除 曝光
export function remove(query) {
  return request({
    url: '/api/exposure/delete',
    method: 'get',
    params: query
  })
}

// 曝光类型列表
export function typeList() {
  return request({
    url: '/api/exposure/type_list',
    method: 'get'
  })
}

// [审核，回复]曝光
export function handle(data) {
  return request({
    url: 'api/exposure/handle',
    method: 'post',
    data
  })
}

// 企业选择列表
export function companySelList(data) {
  return request({
    url: 'api/company/company_sel_list',
    method: 'get',
    data
  })
}
