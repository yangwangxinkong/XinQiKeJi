import request from '@/utils/request'

//设计师职级列表
export function fetchList(query) {
  return request({
    url: '/api/designer_rank/list',
    method: 'get',
    params: query
  })
}
//设计师职级列表
export function fetchAllList(query) {
  return request({
    url: '/api/designer_rank/all',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/designer_rank/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新设计师职级
export function save(data) {
  return request({
    url: 'api/designer_rank/save',
    method: 'post',
    data
  })
}

//删除设计师职级
export function remove(query) {
  return request({
    url: '/api/designer_rank/delete',
    method: 'get',
    params: query
  })
}

export function checkCode(code) {
  return request({
    url: '/api/designer_rank/check_code',
    method: 'get',
    params: {code}
  })
}




