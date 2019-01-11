import request from '@/utils/request'

//查询列表
export function commonSearch(url,query) {
  return request({
    url: url,
    method: 'get',
    params: query
  })
}





