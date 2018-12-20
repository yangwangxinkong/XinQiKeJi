import request from '@/utils/request'

// 评论列表
export function commentList(query) {
  return request({
    url: '/api/comment/list',
    method: 'get',
    params: query
  })
}

// 评论详情
export function commentInfo(id) {
  return request({
    url: '/api/comment/info',
    method: 'get',
    params: { id }
  })
}

// 保存/更新评论
export function commentSave(data) {
  return request({
    url: 'api/comment/save',
    method: 'post',
    data
  })
}

// 删除评论
export function commentRemove(query) {
  return request({
    url: '/api/comment/delete',
    method: 'get',
    params: query
  })
}

// 条件查询评论列表
export function commentSearchList(data) {
  return request({
    url: '/api/comment/search',
    method: 'post',
    data
  })
}

// 评论类型
export function commentTypes(query) {
  return request({
    url: '/api/comment/commentTypes',
    method: 'get',
    params: query
  })
}

// 审核评论
export function commentReview(data) {
  return request({
    url: 'api/comment/review',
    method: 'post',
    data
  })
}
