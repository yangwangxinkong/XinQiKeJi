import request from '@/utils/request'

//商品品牌列表
export function fetchList(query) {
  return request({
    url: '/api/specification/list',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/specification/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新商品规格
export function save(data) {
  return request({
    url: 'api/specification/save',
    method: 'post',
    data
  })
}

//删除商品品牌
export function remove(query) {
  return request({
    url: '/api/specification/delete',
    method: 'get',
    params: query
  })
}




