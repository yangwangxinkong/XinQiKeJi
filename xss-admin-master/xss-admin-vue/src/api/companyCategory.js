import request from '@/utils/request'

//企业分类列表
export function fetchList(query) {
  return request({
    url: '/api/company_category/list',
    method: 'get',
    params: query
  })
}
//企业分类列表
export function fetchCategoryList(query) {
  return request({
    url: '/api/company_category/all',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/company_category/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新商品分类
export function save(data) {
  return request({
    url: 'api/company_category/save',
    method: 'post',
    data
  })
}

//删除企业分类
export function remove(query) {
  return request({
    url: '/api/company_category/delete',
    method: 'get',
    params: query
  })
}




