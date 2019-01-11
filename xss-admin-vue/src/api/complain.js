import request from '@/utils/request'

// 维权 列表
export function fetchComplainList(query) {
  return request({
    url: '/api/complain/list',
    method: 'get',
    params: query
  })
}

// 维权 详情
export function info(id) {
  return request({
    url: '/api/complain/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新 维权
export function save(data) {
  return request({
    url: 'api/complain/save',
    method: 'post',
    data
  })
}

// 删除 维权
export function remove(query) {
  return request({
    url: '/api/complain/delete',
    method: 'get',
    params: query
  })
}

// 维权状态列表
export function statusList() {
  return request({
    url: '/api/complain/status_list',
    method: 'get'
  })
}

// 根据维权ID获取维权处理列表
export function handleList(query) {
  return request({
    url: '/api/complain/handle_list',
    method: 'get',
    params: query
  })
}

// 处理维权
export function handle(data) {
  return request({
    url: 'api/complain/handle',
    method: 'post',
    data
  })
}

