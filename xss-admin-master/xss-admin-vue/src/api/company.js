import request from '@/utils/request'

//企业列表
export function fetchList(query) {
  return request({
    url: '/api/company/list',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/company/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新商品分类
export function save(data) {
  return request({
    url: 'api/company/save',
    method: 'post',
    data
  })
}

//删除企业
export function remove(query) {
  return request({
    url: '/api/company/delete',
    method: 'get',
    params: query
  })
}

//条件查询企业列表
export function searchCompanyList(data) {
  return request({
    url: '/api/company/search',
    method: 'post',
    data
  })
}

//企业列表所有状态数据
export function fetchAllStatusInfos(query) {
  return request({
    url: '/api/company/statusInfos',
    method: 'get',
    params: query
  })
}






