import request from '@/utils/request'

//店铺等级列表
export function fetchList(query) {
  return request({
    url: '/api/store_rank/list',
    method: 'get',
    params: query
  })
}
export function info(id) {
  return request({
    url: '/api/store_rank/info',
    method: 'get',
    params: {id}
  })
}

//保存/更新商品分类
export function save(data) {
  return request({
    url: 'api/store_rank/save',
    method: 'post',
    data
  })
}

//删除店铺等级
export function remove(query) {
  return request({
    url: '/api/store_rank/delete',
    method: 'get',
    params: query
  })
}




