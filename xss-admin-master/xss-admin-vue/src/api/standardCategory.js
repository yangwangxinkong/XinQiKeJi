import request from '@/utils/request'

// 行业标准分类列表
export function fetchList(query) {
  return request({
    url: '/api/standard_category/list',
    method: 'get',
    params: query
  })
}

// 行业标准分类信息
export function info(id) {
  return request({
    url: '/api/standard_category/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 行业标准分类
export function save(data) {
  return request({
    url: 'api/standard_category/save',
    method: 'post',
    data
  })
}

// 删除 行业标准分类
export function remove(query) {
  return request({
    url: '/api/standard_category/delete',
    method: 'get',
    params: query
  })
}

// 行业标准分类 上级分类列表
export function fetchTree(query) {
  return request({
    url: '/api/standard_category/tree',
    method: 'get',
    params: query
  })
}

