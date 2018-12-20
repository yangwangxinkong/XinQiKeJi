import request from '@/utils/request'

// 行业标准列表
export function fetchList(query) {
  return request({
    url: '/api/standard/list',
    method: 'get',
    params: query
  })
}

// 行业标准信息
export function info(id) {
  return request({
    url: '/api/standard/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新商品分类
export function save(data) {
  return request({
    url: 'api/standard/save',
    method: 'post',
    data
  })
}

// 删除行业标准
export function remove(query) {
  return request({
    url: '/api/standard/delete',
    method: 'get',
    params: query
  })
}
