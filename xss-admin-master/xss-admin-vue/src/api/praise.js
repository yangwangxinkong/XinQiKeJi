import request from '@/utils/request'

// 点赞列表
export function praiseList(query) {
  return request({
    url: '/api/praise/list',
    method: 'get',
    params: query
  })
}

// 点赞详情
export function praiseInfo(id) {
  return request({
    url: '/api/praise/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新点赞
export function praiseSave(data) {
  return request({
    url: 'api/praise/save',
    method: 'post',
    data
  })
}

// 删除点赞
export function praiseRemove(query) {
  return request({
    url: '/api/praise/delete',
    method: 'get',
    params: query
  })
}

// 条件查询点赞列表
export function praiseSearchList(data) {
  return request({
    url: '/api/praise/search',
    method: 'post',
    data
  })
}
