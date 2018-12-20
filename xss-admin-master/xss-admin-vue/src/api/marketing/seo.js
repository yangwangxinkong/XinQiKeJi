import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/seo/list',
    method: 'get',
    params: query
  })
}

export function info(id) {
  return request({
    url: '/api/seo/info',
    method: 'get',
    params: { id }
  })
}

export function updateSeo(seo) {
  return request({
    url: '/api/seo/update',
    method: 'post',
    params: seo
  })
}
