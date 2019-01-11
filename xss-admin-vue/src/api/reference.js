import request from '@/utils/request'

// 备案 列表
export function fetchReferenceList(query) {
  return request({
    url: '/api/reference/list',
    method: 'get',
    params: query
  })
}

// 备案 详情
export function info(id) {
  return request({
    url: '/api/reference/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 备案
export function save(data) {
  return request({
    url: 'api/reference/save',
    method: 'post',
    data
  })
}

// 删除 备案
export function remove(query) {
  return request({
    url: '/api/reference/delete',
    method: 'get',
    params: query
  })
}

// 备案状态列表
export function statusList() {
  return request({
    url: '/api/reference/status_list',
    method: 'get'
  })
}

// 根据备案ID获取备案处理列表
export function handleList(query) {
  return request({
    url: '/api/reference/handle_list',
    method: 'get',
    params: query
  })
}

// 处理备案
export function handle(data) {
  return request({
    url: 'api/reference/handle',
    method: 'post',
    data
  })
}

