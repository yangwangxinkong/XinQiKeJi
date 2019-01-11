/**
 * Created by jiachenpan on 16/11/18.
 */

export function isvalidUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
}

/* 合法uri*/
export function validateURL(textval) {
  const urlregex = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return urlregex.test(textval)
}

/* 小写字母*/
export function validateLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/* 大写字母*/
export function validateUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/* 大小写字母*/
export function validateAlphabets(str) {
  const reg = /^[A-Za-z]+$/
  return reg.test(str)
}

/**
 * validate email
 * @param email
 * @returns {boolean}
 */
export function validateEmail(email) {
  const re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
  return re.test(email)
}

/**
 * 手机号验证
 * @param mobile
 * @returns {boolean}
 */
export function validMobile(mobile) {
  const re = /^1[3|4|5|7|8][0-9]\d{8}$/
  return re.test(mobile)
}

/**
 * 电话号码验证
 * @param mobile
 * @returns {boolean}
 */
export function validPhone(phone) {
  const re = /0\d{2,3}-\d{7,8}/
  return re.test(phone)
}

/**
 * 是否未输入或只输入空格验证
 * @param str
 * @returns {boolean}
 */
export function validIsBlank(str) {
  if (str == null || str === '') {
    return true
  }
  var re = new RegExp('^[ ]+$')
  return re.test(str)
}

/**
 * 数字验证
 * @param str
 * @returns {boolean}
 */
export function validNum(str) {
  if (str == null || str === '') {
    return true
  }
  var re = new RegExp('^\d+(\.\d+)?$')
  return re.test(str)
}

/**
 * 数字验证
 * @param str
 * @returns {boolean}
 */
export function validNumber(rule, value, callback) {
  var patrn = /^\d+(\.\d+)?$/;
  if (patrn.exec(value)) {
    callback()
  } else {
    return callback(new Error(rule.message ? rule.message : '此项目格式不对'))
  }
}

/**
 * 是否未输入或只输入空格验证
 * @param str
 * @returns {boolean}
 */
export function isBlank(str) {
  if (str === null || str === '') {
    return true
  }
  var re = new RegExp('^[ ]+$')
  return re.test(str)
}

/**
 * 电话号码验证【Form验证时使用】
 */
export function validatorIsPhone(rule, value, callback) {
  // console.log(rule)
  if (isBlank(value) || validMobile(value) || validPhone(value)) {
    callback()
  } else {
    return callback(new Error(rule.message ? rule.message : '此项目格式不对'))
  }
}
