import Cookies from 'js-cookie'

const TokenKey = 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyTm8iOiJhZG1pbiIsImV4cCI6MTUzMzg2NzYwNH0.UKwZEQt8Jue6a5jR84XD8z4Ih1n3QKbtsHT-Wz0uhfI'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}
