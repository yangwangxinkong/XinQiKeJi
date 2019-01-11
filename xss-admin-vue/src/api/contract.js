import request from '@/utils/request'

// 合同模板 列表
export function fetchList(query) {
  return request({
    url: '/api/contract/list',
    method: 'get',
    params: query
  })
}

// 合同模板 信息
export function info(id) {
  return request({
    url: '/api/contract/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 模板信息
export function save(data) {
  return request({
    url: 'api/contract/save',
    method: 'post',
    data
  })
}

// 删除 合同模板
export function remove(query) {
  return request({
    url: '/api/contract/delete',
    method: 'get',
    params: query
  })
}
