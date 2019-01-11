import request from '@/utils/request'

export function loginByUsername(username, password) {
  const data = {
    username,
    password
  }
  return request({
    url: '/api/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/login/logout',
    method: 'post'
  })
}

export function getUserInfo(token) {
  console.log(token)
  return request({
    url: '/api/admin/currentUser',
    method: 'get',
    params: { token }
  })
}

export function changePwdToDb(data) {
  console.log(data)
  return request({
    url: '/api/password',
    method: 'post',
    data
  })
}

