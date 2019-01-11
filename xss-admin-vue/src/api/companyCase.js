import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/companyCase/list',
    method: 'get',
    params: query
  })
}

export function save(data) {
  return request({
    url: '/api/companyCase/save',
    method: 'post',
    data
  })
}

export function remove(ids) {
  return request({
    url: '/api/companyCase/delete',
    method: 'get',
    params: ids
  })
}

export function info(id) {
  return request({
    url: '/api/companyCase/info',
    method: 'get',
    params: id
  })
}

export function getCityCode(id) {
  return request({
    url: '/api/companyCase/cityCode',
    method: 'get',
    params: id
  })
}

export function getCompanyList() {
  return request({
    url: '/api/companyCase/companyList',
    method: 'get'
  })
}

export function getDesignerList(id) {
  return request({
    url: '/api/companyCase/designerList',
    method: 'get',
    params: id
  })
}

export function getHouseTypeList() {
  return request({
    url: '/api/companyCase/houseTypeList',
    method: 'get'
  })
}

export function getDesignStyleList() {
  return request({
    url: '/api/companyCase/designStyleList',
    method: 'get'
  })
}

export function getRoomNameList() {
  return request({
    url: '/api/companyCase/roomNameList',
    method: 'get'
  })
}
