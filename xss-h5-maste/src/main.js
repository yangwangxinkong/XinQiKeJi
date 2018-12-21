// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import VueRouter from 'vue-router'
import App from './App'
import Vuex from 'vuex'
import { sync } from 'vuex-router-sync'
import store from './vuex/store'
import router from './router'
Vue.use(VueRouter)
Vue.use(Vuex)
FastClick.attach(document.body)
Vue.config.productionTip = false
import infiniteScroll from 'vue-infinite-scroll'
Vue.use(infiniteScroll)
import { ToastPlugin,ConfirmPlugin,AlertPlugin} from 'vux'
Vue.use(ToastPlugin)
Vue.use(ConfirmPlugin)
Vue.use(AlertPlugin)
import VueWeChatShare from 'vue-wechat-share'
Vue.use(VueWeChatShare)
import storage from "@/utils/common"
import url from '@/api/apiUrl'
import {get, post, execute, getUrlStr, loginByCode} from '@/api/server'
import VueClipboards from 'vue-clipboard2'
Vue.use(VueClipboards);
// global filters
import * as filters from './filters'
// register global utility filters.
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})
let code = getUrlStr("code");
// alert(code)

loginByCode(code);
import md5 from 'js-md5';
Vue.prototype.$md5 = md5;
/* eslint-disable no-new */
sync(store, router)

router.afterEach((to, from, next) => {
  window.scroll(0,0)
  })
router.beforeEach((to, from, next) => {
  /* 路由发生变化修改页面title */
  if (to.meta.title) {
    document.title = to.meta.title;
  }
  if (to.meta.login) {
    get(url.current, {}).then(response => {
      //console.log("member1 response:" + JSON.stringify(response))
      if (response.data.result == "00000000") {
        let member = response.data.data;
        //console.log("member:" + JSON.stringify(member));
        if(member.mobile && member.mobile != ""){
          storage.set("member", member);
          next();
        }else{
          return next(
            {path: '/login/index',query:{redirect:to.fullPath}}
          );
        }
      } else if (response.data.result == "10000007") {
        //console.log("1111")
        return next(
          {path: '/login/index', query: {redirect: to.fullPath}}
        );
      } else {
        next();
      }
    })
  }else{
    next();
  }
})
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app-box')
