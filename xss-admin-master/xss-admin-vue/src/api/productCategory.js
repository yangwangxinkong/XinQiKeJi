import request from '@/utils/request'

//商品分类列表
export function fetchList(query) {
  return request({
    url: '/api/product_category/list',
    method: 'get',
    params: query
  })
}

//商品分类上级分类列表
export function fetchTree(query) {
  return request({
    url: '/api/product_category/tree',
    method: 'get',
    params: query
  })
}
//商品分类上级分类列表
export function fetchProductCategoryTree(query) {
  return request({
    url: '/api/product_category/tree',
    method: 'get',
    params: query
  })
}

//根据id查询商品分类信息
export function info(id) {
  return request({
    url: '/api/product_category/info',
    method: 'get',
    params: {id}
  })
}

//删除商品分类
export function remove(id) {
  return request({
    url: '/api/product_category/deleteById',
    method: 'get',
    params: {id}
  })
}

//保存商品分类
export function save(data) {
  return request({
    url: 'api/product_category/save',
    method: 'post',
    data
  })
}



