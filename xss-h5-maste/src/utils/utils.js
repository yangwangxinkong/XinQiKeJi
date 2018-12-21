import Vue from 'vue'

// 取两位小数
Vue.filter('money', function (val) {
  if (val === null || val === undefined) {
    return '';
  }

  val = val.toString().replace(/\$|\,/g,'');
  if (val === '----') {
    return val;
  }
  if (isNaN(val)) {
    val = "0";  
  } 
  let sign = (val == (val = Math.abs(val)));
  val = Math.floor(val * 100 + 0.50000000001);
  let cents = val % 100;
  val = Math.floor(val / 100).toString();
  if (cents < 10) {
    cents = "0" + cents
  }
  for (var i = 0; i < Math.floor((val.length - (1 + i)) / 3); i++) {
    val = val.substring(0, val.length - (4 * i + 3)) + ',' + val.substring(val.length - (4 * i + 3));
  }

  return (((sign) ? '' : '-') + val + '.' + cents);
})

// 取整
Vue.filter('moneyRound', function (val) {
  val = val.toString().replace(/\$|\,/g,'');
  if (val === '----') {
    return val;
  }
  if (isNaN(val)) {
    val = "0";  
  } 
  let sign = (val == (val = Math.abs(val)));
  val = Math.round(val);

  for (var i = 0; i < Math.round((val.length - (1 + i)) / 3); i++) {
    val = val.substring(0, val.length - (4 * i + 3)) + ',' + val.substring(val.length - (4 * i + 3));
  }

  return (((sign) ? '' : '-') + val);
})


export function parseTime(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}-{m}-{d} {h}:{i}:{s}'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (('' + time).length === 10) time = parseInt(time) * 1000
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1,
    d: date.getDate(),
    h: date.getHours(),
    i: date.getMinutes(),
    s: date.getSeconds(),
    a: date.getDay()
  }
  const time_str = format.replace(/{(y|m|d|h|i|s|a)+}/g, (result, key) => {
    let value = formatObj[key]
    if (key === 'a') return ['一', '二', '三', '四', '五', '六', '日'][value - 1]
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}
export function parseTime1(time, cFormat) {
  if (arguments.length === 0) {
    return null
  }
  const format = cFormat || '{y}月{m}日'
  let date
  if (typeof time === 'object') {
    date = time
  } else {
    if (('' + time).length === 10) time = parseInt(time) * 1000
    date = new Date(time)
  }
  const formatObj = {
    y: date.getFullYear(),
    m: date.getMonth() + 1
  }
  const time_str = format.replace(/{(y|m)+}/g, (result, key) => {
    let value = formatObj[key]
    if (result.length > 0 && value < 10) {
      value = '0' + value
    }
    return value || 0
  })
  return time_str
}
export function formatTime(time, option) {
  time = +time * 1000
  const d = new Date(time)
  const now = Date.now()

  const diff = (now - d) / 1000

  if (diff < 30) {
    return '刚刚'
  } else if (diff < 3600) { // less 1 hour
    return Math.ceil(diff / 60) + '分钟前'
  } else if (diff < 3600 * 24) {
    return Math.ceil(diff / 3600) + '小时前'
  } else if (diff < 3600 * 24 * 2) {
    return '1天前'
  }
  if (option) {
    return parseTime(time, option)
  } else {
    return d.getMonth() + 1 + '月' + d.getDate() + '日' + d.getHours() + '时' + d.getMinutes() + '分'
  }
}

export function numFilter (num) {
  // 截取当前数据到小数点后两位
  let realVal = Number(num).toFixed(2)
  // num.toFixed(2)获取的是字符串
  return Number(realVal)
}