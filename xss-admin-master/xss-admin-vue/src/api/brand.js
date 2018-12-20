import request from '@/utils/request'

//商品品牌列表
export function fetchList(query) {
  return request({
    url: '/api/brand/list',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/brand/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新商品分类
export function save(data) {
  return request({
    url: 'api/brand/save',
    method: 'post',
    data
  })
}

//删除商品品牌
export function remove(query) {
  return request({
    url: '/api/brand/delete',
    method: 'get',
    params: query
  })
}


//商品品牌列表all
export function fetchBrandAllList(query) {
  return request({
    url: '/api/brand/all',
    method: 'get',
    params: query
  })
}




