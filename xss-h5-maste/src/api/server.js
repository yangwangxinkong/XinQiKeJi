/**
 * Created by admin on 2018-09-03.
 */
import url from './apiUrl'
import request from '@/utils/request'
import storage from '@/utils/common'
import { getToken, setToken, removeToken } from '@/utils/auth'

/**
 * 公共函数get请求
 */
export function get (path, query) {
  return request({
    method: 'get',
    url: path,
    params: query
  })
}

/**
 * 公共函数post请求
 */
export function post (path, query) {
  return request({
    method: 'post',
    url: path,
    params: query
  })
}

/**
 * 公共函数excute请求
 */
export function execute (path, data) {
  return request({
    method: 'post',
    url: path,
    data
  })
}

export function isWeiXin(){
  var ua = window.navigator.userAgent.toLowerCase();
  if(ua.match(/MicroMessenger/i) == 'micromessenger'){
    return true;
  }else{
    return false;
  }
}


/**
 * 公众号访问时，根据公众号code获取用户信息
 */
export async function loginByCode(code){

  if(isWeiXin()){
    let params = {
      code: code
    }
    await post(url.loginWxCode, params).then(response => {
      console.log(JSON.stringify(response));
      if(response.data.result == "00000000"){
        const data = response.data.data;
        setToken(data.token)
        storage.set("member", data);
        location.replace("http://m.xiaodoushebao.com/index.html");
      }
    })
  }
}
export function  getUrlStr(name){
  /**
   * 获取地址栏参数
   */
  let reg = new RegExp("(^|\\?|&)" + name + "=([^&]*)(\\s|&|$)","i");
  console.log("url:" + location.search.substr(1))
  if(reg.test(location.search.substr(1))){
    return unescape(RegExp.$2.replace(/\+/g," "))
  }
  return undefined
}

