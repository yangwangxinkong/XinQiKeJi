import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/api/activity/list',
    method: 'get',
    params: query
  })
}

export function info(id) {
  return request({
    url: '/api/activity/info',
    method: 'get',
    params: {activityId: id}
  })
}

export function audit(id, decision) {
  return request({
    url: '/api/activity/audit',
    method: 'post',
    params: {activityId: id, decision:decision}
  })
}

export function discontinue(id) {
  return request({
    url: '/api/activity/discontinue',
    method: 'post',
    params: {activityId: id}
  })
}

export function start(id) {
  return request({
    url: '/api/activity/start',
    method: 'post',
    params: {activityId: id}
  })
}

export function end(id) {
  return request({
    url: '/api/activity/end',
    method: 'post',
    params: {activityId: id}
  })
}

// export function deleteActivity(ids) {
//   return request({
//     url: '/api/activity/delete',
//     method: 'post',
//     params: {ids: ids}
//   })
// }

export function saveActivity(data) {
  return request({
    url: '/api/activity/save',
    method: 'post',
    data
  })
}

export function updateActivity(data) {
  return request({
    url: '/api/activity/update',
    method: 'post',
    data
  })
}

export function deleteActivity(ids) {
  return request({
    url: '/api/activity/delete',
    method: 'post',
    params: {ids: ids}
  })
}

export function queryType() {
  return request({
    url: '/api/activity/add_select_data',
    method: 'get',
    params: {}
  })
}


