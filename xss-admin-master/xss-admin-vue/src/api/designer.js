import request from '@/utils/request'

//设计师列表
export function fetchList(query) {
  return request({
    url: '/api/designer/list',
    method: 'get',
    params: query
  })
}
//设计师列表
export function fetchAllList(query) {
  return request({
    url: '/api/designer/all',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/designer/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新设计师
export function save(data) {
  return request({
    url: 'api/designer/save',
    method: 'post',
    data
  })
}

//删除设计师
export function remove(query) {
  return request({
    url: '/api/designer/delete',
    method: 'get',
    params: query
  })
}





