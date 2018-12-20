import request from '@/utils/request'

//设计师分类列表 全部
export function fetchDesignerCategoryAllList(query) {
  return request({
    url: '/api/designer_category/all',
    method: 'get',
    params: query
  })
}





