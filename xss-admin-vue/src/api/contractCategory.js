import request from '@/utils/request'

// 合同分类 列表
export function fetchList(query) {
  return request({
    url: '/api/contract_category/list',
    method: 'get',
    params: query
  })
}

// 合同分类 信息
export function info(id) {
  return request({
    url: '/api/contract_category/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 合同分类
export function save(data) {
  return request({
    url: 'api/contract_category/save',
    method: 'post',
    data
  })
}

// 删除 合同分类
export function remove(query) {
  return request({
    url: '/api/contract_category/deleteById',
    method: 'get',
    params: query
  })
}

// 合同分类 上级分类列表
export function fetchTree(query) {
  return request({
    url: '/api/contract_category/tree',
    method: 'get',
    params: query
  })
}
