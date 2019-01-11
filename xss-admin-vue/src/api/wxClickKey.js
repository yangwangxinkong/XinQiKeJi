import request from '@/utils/request'

// 微信关键字列表
export function fetchList(query) {
  return request({
    url: '/api/wx_click_key/list',
    method: 'get',
    params: query
  })
}

// 根据id查询微信关键字信息
export function info(id) {
  return request({
    url: '/api/wx_click_key/info',
    method: 'get',
    params: { id }
  })
}

// 删除微信关键字
export function remove(data) {
  return request({
    url: '/api/wx_click_key/delete',
    method: 'get',
    params: data
  })
}

// 保存微信关键字
export function save(data) {
  return request({
    url: '/api/wx_click_key/save',
    method: 'post',
    data
  })
}

