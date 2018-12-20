import request from '@/utils/request'

//商品参数组列表
export function fetchList(query) {
  return request({
    url: '/api/parameter_group/list',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/parameter_group/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新商品参数组
export function save(data) {
  return request({
    url: 'api/parameter_group/save',
    method: 'post',
    data
  })
}

//删除商品参数组
export function remove(query) {
  return request({
    url: '/api/parameter_group/delete',
    method: 'get',
    params: query
  })
}




