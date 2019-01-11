import request from '@/utils/request'

// 微信关键字响应列表
export function fetchList(query) {
  return request({
    url: '/api/wx_click_value/list',
    method: 'get',
    params: query
  })
}

// 根据id查询微信关键字响应信息
export function info(id) {
  return request({
    url: '/api/wx_click_value/info',
    method: 'get',
    params: { id }
  })
}

// 查询所有微信关键字信息
export function selectWxClickKey(query) {
  return request({
    url: '/api/wx_click_key/all',
    method: 'get',
    params: query
  })
}

// 删除微信关键字响应
export function remove(data) {
  return request({
    url: '/api/wx_click_value/delete',
    method: 'get',
    params: data
  })
}

// 保存微信关键字响应
export function save(data) {
  return request({
    url: '/api/wx_click_value/save',
    method: 'post',
    data
  })
}

