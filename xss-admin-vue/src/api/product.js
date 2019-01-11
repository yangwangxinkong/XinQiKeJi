import request from '@/utils/request'

//商品列表
export function fetchList(query) {
  return request({
    url: '/api/product/list',
    method: 'get',
    params: query
  })
}
export function findById(id) {
  return request({
    url: '/api/product/info',
    method: 'get',
    params: {id}
  })
}

//保存商品
export function createProduct(data) {
  return request({
    url: '/api/product/save',
    method: 'post',
    data
  })
}

//更新商品
export function updateProduct(data) {
  return request({
    url: '/api/product/update',
    method: 'post',
    data
  })
}

//删除商品
export function deleteProducts(query) {
  return request({
    url: '/api/product/delete',
    method: 'get',
    params: query
  })
}

//根据商品分类获取商品参数
export function loadParameters(query) {
  return request({
    url: '/api/product/parameter_groups',
    method: 'get',
    params: query
  })
}

//根据商品分类获取商品属性
export function loadAttributes(query) {
  return request({
    url: '/api/product/attributes',
    method: 'get',
    params: query
  })
}

//获取商品规格初始数据
export function loadSpecification(query) {
  return request({
    url: '/api/product/specification',
    method: 'get',
    params: query
  })
}


//弹框选择商品查询
export function selectProducts(query) {
  return request({
    url: '/api/product/select_products',
    method: 'get',
    params: query
  })
}

//复制商品
export function copy(id) {
  return request({
    url: '/api/product/copy',
    method: 'get',
    params: {id}
  })
}

//异步更新上下架
export function updateMarketable(sn) {
  return request({
    url: '/api/product/updateMarketable',
    method: 'get',
    params: {sn}
  })
}

//异步获取发布商品相关初始化信息
export function getAddProductInitInfo(query) {
  return request({
    url: '/api/product/getAddProductInitInfo',
    method: 'get',
    params: query
  })
}





