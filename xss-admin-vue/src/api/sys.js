import request from '@/utils/request'

/**
 * 系统设置API
 * create by fan.hu
 */

/** *****************物流公司 API*********start******** */
export function getDeliveryCorpList(query) {
  return request({
    url: '/api/delivery_corp/list',
    method: 'get',
    params: query
  })
}

export function deleteDeliveryCorps(query) {
  return request({
    url: '/api/delivery_corp/delete',
    method: 'get',
    params: query
  })
}

export function createDeliveryCorp(data) {
  return request({
    url: '/api/delivery_corp/save',
    method: 'post',
    data
  })
}

export function getDeliveryCorp(id) {
  return request({
    url: '/api/delivery_corp/detail',
    method: 'get',
    params: { id }
  })
}

export function updateDeliveryCorp(data) {
  return request({
    url: '/api/delivery_corp/update',
    method: 'post',
    data
  })
}
/** *****************物流公司 API*********end******** */

/** *****************配送方式 API*********start******** */
export function getShippingMethodList(query) {
  return request({
    url: '/api/shipping_method/list',
    method: 'get',
    params: query
  })
}

export function deleteShippingMethods(query) {
  return request({
    url: '/api/shipping_method/delete',
    method: 'get',
    params: query
  })
}

export function getShippingMethod(id) {
  return request({
    url: '/api/shipping_method/detail',
    method: 'get',
    params: { id }
  })
}

export function createShippingMethod(data) {
  return request({
    url: '/api/shipping_method/save',
    method: 'post',
    data
  })
}

export function updateShippingMethod(data) {
  return request({
    url: '/api/shipping_method/update',
    method: 'post',
    data
  })
}
/** *****************配送方式 API*********end******** */

/** *****************支付方式 API*********start******** */
export function getPaymentMethodList(query) {
  return request({
    url: '/api/payment_method/list',
    method: 'get',
    params: query
  })
}

export function deletePaymentMethods(query) {
  return request({
    url: '/api/payment_method/delete',
    method: 'get',
    params: query
  })
}

export function getPaymentMethod(query) {
  return request({
    url: '/api/payment_method/method',
    method: 'get',
    params: query
  })
}

export function getPaymentMethodInfo(id) {
  return request({
    url: '/api/payment_method/detail',
    method: 'get',
    params: { id }
  })
}

export function createPaymentMethod(data) {
  return request({
    url: '/api/payment_method/save',
    method: 'post',
    data
  })
}

export function updatePaymentMethod(data) {
  return request({
    url: '/api/payment_method/update',
    method: 'post',
    data
  })
}
/** *****************支付方式 API*********end******** */

/** *****************地区管理 API*********start******** */
export function getAreaList(query) {
  console.log(query)
  return request({
    url: '/api/area/children',
    method: 'get',
    params: query
  })
}

export function createArea(data) {
  return request({
    url: '/api/area/save',
    method: 'post',
    data
  })
}

export function updateArea(data) {
  return request({
    url: '/api/area/update',
    method: 'post',
    data
  })
}

export function deleteArea(id) {
  return request({
    url: '/api/area/delete',
    method: 'post',
    params: { id }
  })
}

export function getAreaOptions(query) {
  return request({
    url: '/api/area/areaOptions',
    method: 'get',
    params: query
  })
}
/** *****************地区管理 API*********end******** */

/** *****************区域管理 API*********start******** */
export function getRegionList(query) {
  console.log(query)
  return request({
    url: '/api/region/children',
    method: 'get',
    params: query
  })
}

export function createRegion(data) {
  return request({
    url: '/api/region/save',
    method: 'post',
    data
  })
}

export function updateRegion(data) {
  return request({
    url: '/api/region/update',
    method: 'post',
    data
  })
}

export function deleteRegion(id) {
  return request({
    url: '/api/region/delete',
    method: 'post',
    params: { id }
  })
}
/** *****************区域管理 API*********end******** */

/** *****************角色管理 API*********start******** */
export function getRoleList(query) {
  return request({
    url: '/api/role/list',
    method: 'get',
    params: query
  })
}

export function deleteRoles(query) {
  return request({
    url: '/api/role/delete',
    method: 'get',
    params: query
  })
}

export function createRole(data) {
  return request({
    url: '/api/role/save',
    method: 'post',
    data
  })
}

export function getRole(id) {
  return request({
    url: '/api/role/detail',
    method: 'get',
    params: { id }
  })
}

export function updateRole(data) {
  return request({
    url: '/api/role/update',
    method: 'post',
    data
  })
}

export function getRoleAllList() {
  return request({
    url: '/api/role/list_all',
    method: 'get'
  })
}
/** *****************角色管理 API*********end******** */

/** *****************管理员 API*********start******** */
export function getAdminList(query) {
  console.log(query)
  return request({
    url: '/api/admin/list',
    method: 'get',
    params: query
  })
}

export function createAdmin(data) {
  return request({
    url: '/api/admin/save',
    method: 'post',
    data
  })
}

export function updateAdmin(data) {
  return request({
    url: '/api/admin/update',
    method: 'post',
    data
  })
}

export function deleteAdmin(query) {
  return request({
    url: '/api/admin/delete',
    method: 'get',
    params: query
  })
}

export function getAdmin(id) {
  return request({
    url: '/api/admin/detail',
    method: 'get',
    params: { id }
  })
}
/** *****************管理员 API*********end******** */

/** *****************日志管理 API*********start******** */
export function getLogList(query) {
  console.log(query)
  return request({
    url: '/api/log/list',
    method: 'get',
    params: query
  })
}

export function deleteLog(query) {
  return request({
    url: '/api/log/delete',
    method: 'get',
    params: query
  })
}

export function getLog(id) {
  return request({
    url: '/api/log/detail',
    method: 'get',
    params: { id }
  })
}

export function clearLog() {
  return request({
    url: '/api/log/clear',
    method: 'post'
  })
}
/** *****************日志管理 API*********end******** */

/** *****************系统设置 API*********end******** */
export function getSetting() {
  return request({
    url: '/api/setting/detail',
    method: 'get'
  })
}

export function updateSetting(data) {
  return request({
    url: '/api/setting/update',
    method: 'post',
    data
  })
}
/** *****************系统设置 API*********start******** */
