import Vue from 'vue'
import Router from 'vue-router'
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

Vue.use(Router)

// import Hello from '@/components/Hello'

// webpack 按需加载（会将每个组件打包成单个js chunk）
// const Hello = r => require.ensure([], () => r(require('@/components/Hello')), 'Hello')

// vue-router 异步组件，懒加载（https://router.vuejs.org/zh-cn/advanced/lazy-loading.html）
const Hello = () => import(/* webpackChunkName: "Hello" */ '@/components/Hello')
// const Demo = () => import(/* webpackChunkName: "demo" */ '@/views/demo')
// const Child = () => import(/* webpackChunkName: "demo" */ '@/views/child')

let router = new Router({
  routes: [
    {
      path: '/',
      name: 'Hello',
      component: Hello
    },
    // {
    //   path: '/demo',
    //   name: 'Demo',
    //   component: Demo,
    //   children: [
    //     {
    //       path: '',
    //       name: 'child',
    //       component: Child
    //     }
    //   ]
    // }
  ]
})

router.beforeEach((to, from, next) => {
  NProgress.start()
  next()
})

router.afterEach(transition => {
  NProgress.done()
})

export default router
