import request from '@/utils/request'

// 微信菜单列表
export function fetchList(query) {
  return request({
    url: '/api/wx_menu/list',
    method: 'get',
    params: query
  })
}

// 微信菜单上级分类列表
export function fetchTree(query) {
  return request({
    url: '/api/wx_menu/tree',
    method: 'get',
    params: query
  })
}

// 根据id查询微信菜单信息
export function info(id) {
  return request({
    url: '/api/wx_menu/info',
    method: 'get',
    params: { id }
  })
}

// 删除微信菜单
export function remove(id) {
  return request({
    url: '/api/wx_menu/delete',
    method: 'get',
    params: { id }
  })
}

// 保存微信菜单
export function save(data) {
  return request({
    url: '/api/wx_menu/save',
    method: 'post',
    data
  })
}

// 发布微信菜单
export function publish(data) {
  return request({
    url: '/api/wx_menu/publish',
    method: 'get',
    params: data
  })
}
