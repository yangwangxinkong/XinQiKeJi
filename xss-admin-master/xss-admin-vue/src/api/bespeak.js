import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/bespeak/list',
    method: 'get',
    params: query
  })
}

export function info(id) {
  return request({
    url: '/api/bespeak/info',
    method: 'get',
    params: {id: id}
  })
}

export function deleteBespeak(ids) {
  return request({
    url: '/api/bespeak/delete',
    method: 'post',
    params: {ids: ids}
  })
}

export function saveBespeak(bespeak) {
  return request({
    url: '/api/bespeak/save',
    method: 'post',
    params: bespeak
  })
}




