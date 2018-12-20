import request from '@/utils/request'

// 评分列表
export function scoreList(query) {
  return request({
    url: '/api/score/list',
    method: 'get',
    params: query
  })
}

// 评分详情
export function scoreInfo(id) {
  return request({
    url: '/api/score/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新评分
export function scoreSave(data) {
  return request({
    url: 'api/score/save',
    method: 'post',
    data
  })
}

// 删除评分
export function scoreRemove(query) {
  return request({
    url: '/api/score/delete',
    method: 'get',
    params: query
  })
}

// 条件查询评分列表
export function scoreSearchList(data) {
  return request({
    url: '/api/score/search',
    method: 'post',
    data
  })
}
