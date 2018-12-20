import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/views/layout/Layout'

/** note: submenu only apppear when children.length>=1
*   detail see  https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
**/

/**
* hidden: true                   if `hidden:true` will not show in the sidebar(default is false)
* alwaysShow: true               if set true, will always show the root menu, whatever its child routes length
*                                if not set alwaysShow, only more than one route under the children
*                                it will becomes nested mode, otherwise not show the root menu
* redirect: noredirect           if `redirect:noredirect` will no redirct in the breadcrumb
* name:'router-name'             the name is used by <keep-alive> (must set!!!)
* meta : {
    roles: ['admin','editor']     will control the page roles (you can set multiple roles)
    title: 'title'               the name show in submenu and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar,
    noCache: true                if true ,the page will no be cached(default is false)
  }
**/
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/authredirect', component: () => import('@/views/login/authredirect'), hidden: true },
  { path: '/404', component: () => import('@/views/errorPage/404'), hidden: true },
  { path: '/401', component: () => import('@/views/errorPage/401'), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    children: [{
      path: 'dashboard',
      component: () => import('@/views/dashboard/index'),
      name: 'dashboard',
      meta: { title: 'dashboard', icon: 'dashboard', noCache: true }
    }]
  }

]

export default new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
})

export const asyncRouterMap = [
  /** 系统设置 start*/
  {
    path: '/sys',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'sys',
    meta: {
      title: 'sys',
      icon: 'list',
      roles: ['admin:setting', 'admin:paymentMethod', 'admin:city', 'admin:residenceType', 'admin:admin', 'admin:role', 'admin:wxMenu', 'admin:wxClickKey', 'admin:wxClickValue']
    },
    children: [
      { path: 'config/list', component: () => import('@/views/sys/configs/list'), name: 'configList', meta: { title: 'configList', noCache: true, roles: ['admin:setting'] }},
      { path: 'config/add', component: () => import('@/views/sys/configs/add'), name: 'configAdd', meta: { title: 'configAdd', roles: ['admin:setting'] }, hidden: true },
      { path: 'config/edit/:id(\\d+)', component: () => import('@/views/sys/configs/edit'), name: 'configEdit', meta: { title: 'configEdit', noCache: true, roles: ['admin:setting'] }, hidden: true },

      // { path: 'setting/info', component: () => import('@/views/sys/setting/index'), name: 'setting', meta: { title: 'setting', noCache: true, roles: ['admin:setting'] }},
      { path: 'payment_method/list', component: () => import('@/views/sys/payment-method/list'), name: 'paymentMethodList', meta: { title: 'paymentMethodList', noCache: true, roles: ['admin:paymentMethod'] }},
      { path: 'payment_method/add', component: () => import('@/views/sys/payment-method/add'), name: 'paymentMethodAdd', meta: { title: 'paymentMethodAdd', roles: ['admin:paymentMethod'] }, hidden: true },
      { path: 'payment_method/edit/:id(\\d+)', component: () => import('@/views/sys/payment-method/edit'), name: 'paymentMethodEdit', meta: { title: 'paymentMethodEdit', noCache: true, roles: ['admin:paymentMethod'] }, hidden: true },

      { path: 'city/list', component: () => import('@/views/sys/city/list'), name: 'cityList', meta: { title: 'cityList', roles: ['admin:city'] }},
      { path: 'city/add', component: () => import('@/views/sys/city/add'), name: 'cityAdd', meta: { title: 'cityAdd', roles: ['admin:city'] }, hidden: true },
      { path: 'city/edit/:id(\\d+)', component: () => import('@/views/sys/city/edit'), name: 'cityEdit', meta: { title: 'cityEdit', roles: ['admin:city'] }, hidden: true },

      { path: 'residence_type/list', component: () => import('@/views/sys/residence-type/list'), name: 'residenceTypeList', meta: { title: 'residenceTypeList', noCache: true, roles: ['admin:residenceType'] }},
      { path: 'residence_type/add', component: () => import('@/views/sys/residence-type/add'), name: 'residenceTypeAdd', meta: { title: 'residenceTypeAdd', roles: ['admin:residenceType'] }, hidden: true },
      { path: 'residence_type/edit/:id(\\d+)', component: () => import('@/views/sys/residence-type/edit'), name: 'residenceTypeEdit', meta: { title: 'residenceTypeEdit', noCache: true, roles: ['admin:residenceType'] }, hidden: true },

      // { path: 'region/list',query:{name: 'id'}, component: () => import('@/views/sys/region/list'), name: 'regionList', meta: { title: 'regionList', noCache: true, roles: ['admin:region'] }},
      { path: 'admin/list', component: () => import('@/views/sys/admin/list'), name: 'adminList', meta: { title: 'adminList', noCache: true, roles: ['admin:admin'] }},
      { path: 'role/list', component: () => import('@/views/sys/role/list'), name: 'roleList', meta: { title: 'roleList', noCache: true, roles: ['admin:role'] }},
      { path: 'role/add', component: () => import('@/views/sys/role/add'), name: 'roleAdd', meta: { title: 'roleAdd', roles: ['admin:role'] }, hidden: true },
      { path: 'role/edit/:id(\\d+)', component: () => import('@/views/sys/role/edit'), name: 'roleEdit', meta: { title: 'roleEdit', noCache: true, roles: ['admin:role'] }, hidden: true },
      // { path: 'log/list', component: () => import('@/views/sys/log/list'), name: 'logList', meta: { title: 'logList', noCache: true, roles: ['admin:log'] }},
      { path: 'wx_menu/list', component: () => import('@/views/sys/wx_menu/list'), name: 'wxMenuList', meta: { title: 'wxMenuList', noCache: true, roles: ['admin:wxMenu'] }},
      { path: 'wx_menu/add', component: () => import('@/views/sys/wx_menu/add'), name: 'wxMenuAdd', meta: { title: 'wxMenuAdd', roles: ['admin:wxMenu']}, hidden: true },
      { path: 'wx_menu/edit/:id(\\d+)', component: () => import('@/views/sys/wx_menu/edit'), name: 'wxMenuEdit', meta: { title: 'wxMenuEdit', noCache: true, roles: ['admin:wxMenu'] }, hidden: true },

      { path: 'wx_click_key/list', component: () => import('@/views/sys/wx_click_key/list'), name: 'wxClickKeyList', meta: { title: 'wxClickKeyList', noCache: true, roles: ['admin:wxClickKey'] }},
      { path: 'wx_click_key/add', component: () => import('@/views/sys/wx_click_key/add'), name: 'wxClickKeyAdd', meta: { title: 'wxClickKeyAdd', roles: ['admin:wxClickKey'] }, hidden: true },
      { path: 'wx_click_key/edit/:id(\\d+)', component: () => import('@/views/sys/wx_click_key/edit'), name: 'wxClickKeyEdit', meta: { title: 'wxClickKeyEdit', noCache: true, roles: ['admin:wxClickKey'] }, hidden: true },

      { path: 'wx_click_value/list', component: () => import('@/views/sys/wx_click_value/list'), name: 'wxClickValueList', meta: { title: 'wxClickValueList', noCache: true, roles: ['admin:wxClickValue'] }},
      { path: 'wx_click_value/add', component: () => import('@/views/sys/wx_click_value/add'), name: 'wxClickValueAdd', meta: { title: 'wxClickValueAdd', roles: ['admin:wxClickValue'] }, hidden: true },
      { path: 'wx_click_value/edit/:id(\\d+)', component: () => import('@/views/sys/wx_click_value/edit'), name: 'wxClickValueEdit', meta: { title: 'wxClickValueEdit', noCache: true, roles: ['admin:wxClickValue'] }, hidden: true }
    ]
  },

  /** 基础设置 start*/
  {
    path: '/settings',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'settings',
    meta: {
      title: 'settings',
      icon: 'list',
      roles: ['admin:providentFundSettings', 'admin:socialSecuritySettings', 'admin:serviceFeeSettings', 'admin:payBaseSettings']
    },
    children: [
      { path: 'provident_fund/list', component: () => import('@/views/settings/provident_fund/list'), name: 'providentFundList', meta: { title: 'providentFundList', roles: ['admin:providentFundSettings'] }},
      { path: 'provident_fund/add', component: () => import('@/views/settings/provident_fund/add'), name: 'providentFundAdd', meta: { title: 'providentFundAdd', roles: ['admin:providentFundSettings'] }, hidden: true },
      { path: 'provident_fund/edit/:id(\\d+)', component: () => import('@/views/settings/provident_fund/edit'), name: 'providentFundEdit', meta: { title: 'providentFundEdit', roles: ['admin:providentFundSettings'] }, hidden: true },

      { path: 'social_security/list', component: () => import('@/views/settings/social_security/list'), name: 'socialSecurityList', meta: { title: 'socialSecurityList', roles: ['admin:socialSecuritySettings'] }},
      { path: 'social_security/add', component: () => import('@/views/settings/social_security/add'), name: 'socialSecurityAdd', meta: { title: 'socialSecurityAdd', roles: ['admin:socialSecuritySettings'] }, hidden: true },
      { path: 'social_security/edit/:id(\\d+)', component: () => import('@/views/settings/social_security/edit'), name: 'socialSecurityEdit', meta: { title: 'socialSecurityEdit', roles: ['admin:socialSecuritySettings'] }, hidden: true },

      { path: 'service_fee/list', component: () => import('@/views/settings/service_fee/list'), name: 'serviceFeeList', meta: { title: 'serviceFeeList', roles: ['admin:serviceFeeSettings'] }},
      { path: 'service_fee/add', component: () => import('@/views/settings/service_fee/add'), name: 'serviceFeeAdd', meta: { title: 'serviceFeeAdd', roles: ['admin:serviceFeeSettings'] }, hidden: true },
      { path: 'service_fee/edit/:id(\\d+)', component: () => import('@/views/settings/service_fee/edit'), name: 'serviceFeeEdit', meta: { title: 'serviceFeeEdit', roles: ['admin:serviceFeeSettings'] }, hidden: true },

      { path: 'pay_base/list', component: () => import('@/views/settings/pay_base/list'), name: 'payBaseList', meta: { title: 'payBaseList', roles: ['admin:payBaseSettings'] }},
      { path: 'pay_base/add', component: () => import('@/views/settings/pay_base/add'), name: 'payBaseAdd', meta: { title: 'payBaseAdd', roles: ['admin:payBaseSettings'] }, hidden: true },
      { path: 'pay_base/edit/:id(\\d+)', component: () => import('@/views/settings/pay_base/edit'), name: 'payBaseEdit', meta: { title: 'payBaseEdit', roles: ['admin:payBaseSettings'] }, hidden: true }
    ]
  },

  {
    path: '/member',
    component: Layout,
    redirect: '/member/index',
    alwaysShow: true, // will always show the root menu
    meta: {
      title: 'member',
      icon: 'list',
      roles: ['admin:member'] // you can set roles in root nav
    },
    children: [
      { path: 'list', component: () => import('@/views/member/list'), name: 'memberList', meta: { title: 'memberList', roles: ['admin:member'] }},
      { path: 'add', component: () => import('@/views/member/add'), name: 'memberAdd', meta: { title: 'memberAdd', roles: ['admin:member'] }, hidden: true },
      { path: 'edit/:id(\\d+)', component: () => import('@/views/member/edit'), name: 'memberEdit', meta: { title: 'memberEdit', roles: ['admin:member'] }, hidden: true }
    ]
  },
  {
    path: '/product',
    component: Layout,
    redirect: '/product/index',
    alwaysShow: true, // will always show the root menu
    meta: {
      title: 'product',
      icon: 'list',
      roles: ['admin:product', 'admin:productCategory', 'admin:brand', 'admin:specification', 'admin:attribute', 'admin:parameterGroup'] // you can set roles in root nav
    },
    children: [
      { path: 'list', component: () => import('@/views/product/list'),name: 'productList',meta: {title: 'productList',roles: ['admin:product'] }},
      { path: 'add', component: () => import('@/views/product/add'),name: 'productAdd',meta: {title: 'productAdd',roles: ['admin:product'] }, hidden: true },
      { path: 'edit/:id(\\d+)', component: () => import('@/views/product/edit'),name: 'productEdit',meta: {title: 'productEdit', roles: ['admin:product'] }, hidden: true },
      { path: 'product_category/list', component: () => import('@/views/product/product_category/list'),name: 'productCategoryList', meta: { title: 'productCategoryList', roles: ['admin:productCategory'] }},
      { path: 'product_category/add', component: () => import('@/views/product/product_category/add'), name: 'productCategoryAdd', meta: { title: 'productCategoryAdd', roles: ['admin:productCategory'] }, hidden: true },
      { path: 'product_category/edit/:id(\\d+)', component: () => import('@/views/product/product_category/edit'), name: 'productCategoryEdit', meta: { title: 'productCategoryEdit', roles: ['admin:productCategory'] }, hidden: true },
      { path: 'brand/list', component: () => import('@/views/product/brand/list'),name: 'brandList',meta: {title: 'brandList', roles: ['admin:brand'] }},
      { path: 'brand/add', component: () => import('@/views/product/brand/add'), name: 'brandAdd', meta: { title: 'brandAdd', roles: ['admin:brand'] }, hidden: true },
      { path: 'brand/edit/:id(\\d+)', component: () => import('@/views/product/brand/edit'), name: 'brandEdit', meta: { title: 'brandEdit', roles: ['admin:brand'] }, hidden: true },
      { path: 'specification/list', component: () => import('@/views/product/specification/list'),name: 'specificationList',meta: {title: 'specificationList', roles: ['admin:specification'] }},
      { path: 'specification/add', component: () => import('@/views/product/specification/add'), name: 'specificationAdd', meta: { title: 'specificationAdd', roles: ['admin:specification'] }, hidden: true },
      { path: 'specification/edit/:id(\\d+)', component: () => import('@/views/product/specification/edit'), name: 'specificationEdit', meta: { title: 'specificationEdit', roles: ['admin:specification'] }, hidden: true },
      { path: 'attribute/list', component: () => import('@/views/product/attribute/list'),name: 'attributeList',meta: {title: 'attributeList', roles: ['admin:attribute'] }},
      { path: 'attribute/add', component: () => import('@/views/product/attribute/add'), name: 'attributeAdd', meta: { title: 'attributeAdd', roles: ['admin:attribute'] }, hidden: true },
      { path: 'attribute/edit/:id(\\d+)', component: () => import('@/views/product/attribute/edit'), name: 'attributeEdit', meta: { title: 'attributeEdit', roles: ['admin:attribute'] }, hidden: true },
      { path: 'parameter_group/list', component: () => import('@/views/product/parameter_group/list'),name: 'parameterGroupList',meta: {title: 'parameterGroupList', roles: ['admin:parameterGroup'] }},
      { path: 'parameter_group/add', component: () => import('@/views/product/parameter_group/add'), name: 'parameterGroupAdd', meta: { title: 'parameterGroupAdd', roles: ['admin:parameterGroup'] }, hidden: true },
      { path: 'parameter_group/edit/:id(\\d+)', component: () => import('@/views/product/parameter_group/edit'), name: 'parameterGroupEdit', meta: { title: 'parameterGroupEdit', roles: ['admin:parameterGroup'] }, hidden: true }
    ]
  },
  {
    path: '/order',
    component: Layout,
    name: 'order',
    redirect: 'noredirect',
    alwaysShow: true, // will always show the root menu
    meta: {
      title: 'order',
      icon: 'list',
      roles: ['admin:order', 'admin:payment', 'admin:orderCancelApply', 'admin:invoice'] // you can set roles in root nav
    },
    children: [
      { path: 'order/list', component: () => import('@/views/order/order/list'), name: 'orderList', meta: { title: 'orderList', roles: ['admin:order'] }},
      { path: 'order/view/:id(\\d+)', component: () => import('@/views/order/order/view'), name: 'orderView', meta: { title: 'orderView', roles: ['admin:order'] }, hidden: true },
      { path: 'excharge/list', component: () => import('@/views/order/excharge/list'), name: 'exchargeList', meta: { title: 'exchargeList', roles: ['admin:order'] }},
      { path: 'excharge/view/:id(\\d+)', component: () => import('@/views/order/excharge/view'), name: 'exchargeView', meta: { title: 'exchargeView', roles: ['admin:order'] }, hidden: true },
      // { path: 'order/edit/:id(\\d+)', component: () => import('@/views/order/order/edit'), name: 'orderEdit', meta: { title: 'orderEdit', noCache: true }, hidden: true },
      { path: 'payment/list', component: () => import('@/views/order/payment/list'), name: 'paymentList', meta: { title: 'paymentList', roles: ['admin:payment'] }},
      { path: 'payment/view/:id(\\d+)', component: () => import('@/views/order/payment/view'), name: 'paymentView', meta: { title: 'paymentView', roles: ['admin:payment'] }, hidden: true },
      { path: 'order_cancel_apply/list', component: () => import('@/views/order/order_cancel_apply/list'), name: 'orderCancelApplyList', meta: { title: 'orderCancelApplyList', roles: ['admin:orderCancelApply'] }},
      { path: 'order_cancel_apply/view', query: { name: 'sn', name: 'isEdit' }, component: () => import('@/views/order/order_cancel_apply/view'), name: 'orderCancelApplyView', meta: { title: 'orderCancelApplyView', roles: ['admin:orderCancelApply'] }, hidden: true },

      { path: 'invoice/list', component: () => import('@/views/order/invoice/list'), name: 'invoiceList', meta: { title: 'invoiceList', roles: ['admin:invoice'] }},
      { path: 'invoice/view/:id(\\d+)', component: () => import('@/views/order/invoice/view'), name: 'invoiceView', meta: { title: 'invoiceView', roles: ['admin:invoice'] }, hidden: true }
    ]
  },
  {
    path: '/marketing',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true, // will always show the root menu
    name: 'marketing',
    meta: {
      title: 'marketing',
      icon: 'list',
      roles: ['admin:coupon', 'admin:activity'] // you can set roles in root nav
    },
    children: [
      { path: 'coupon', component: () => import('@/views/marketing/coupon/coupon'),name: 'coupon',meta:{title: 'coupon',roles: ['admin', 'admin:coupon'] }},
      { path: 'coupon/add', component: () => import('@/views/marketing/coupon/add'),name: 'couponAdd', meta:{title: 'couponAdd',roles: ['admin', 'admin:coupon'] }, hidden: true },
      { path: 'coupon/edit/:id(\\d+)', component: () => import('@/views/marketing/coupon/edit'),name: 'couponEdit',meta:{title: 'couponEdit',roles: ['admin', 'admin:coupon'] }, hidden: true },
      { path: 'coupon/code_list/:id(\\d+)', component: () => import('@/views/marketing/coupon/code_list'),name: 'couponCodeList',meta:{title: 'couponCodeList',roles: ['admin', 'admin:coupon'] }, hidden: true },
      { path: 'activity', component: () => import('@/views/marketing/activity/activity'),name: 'activity',meta:{title: 'activity',roles: ['admin', 'admin:activity']}},
      { path: 'activity/add', component: () => import('@/views/marketing/activity/add'),name: 'activityAdd',hidden: true,meta:{title: 'activityAdd',roles: ['admin', 'admin:activity']}},
      { path: 'activity/edit/:id(\\d+)', component: () => import('@/views/marketing/activity/edit'),name: 'activityEdit',hidden: true,meta:{title: 'activityEdit',roles: ['admin', 'admin:activity']}},
      { path: 'activity/info/:id(\\d+)', component: () => import('@/views/marketing/activity/info'),name: 'activityInfo',hidden: true,meta:{title: 'activityInfo',roles: ['admin', 'admin:activity']}},
    ]
  },
  {
    path: '/content',
    component: Layout,
    redirect: 'noredirect',
    alwaysShow: true,
    name: 'content',
    meta: {
      title: 'content',
      icon: 'list',
      roles: ['admin:navigation', 'admin:adPosition', 'admin:ad', 'admin:articleCategory', 'admin:article', 'admin:tag']
    },
    children: [
      /*{ path: 'navigation_list', component: () => import('@/views/content/navigation/list'), name: 'navigationList', meta: { title: 'navigationList', roles: ['admin:navigation'] }},
      { path: 'navigation/add', component: () => import('@/views/content/navigation/add'), name: 'addNavigation', meta: { title: 'addNavigation', roles: ['admin:navigation'] }, hidden: true },
      { path: 'navigation/edit/:id(\\d+)', component: () => import('@/views/content/navigation/edit'), name: 'editNavigation', meta: { title: 'editNavigation', roles: ['admin:navigation'] }, hidden: true },*/
      { path: 'adPosition/list', component: () => import('@/views/content/adPosition/list'), name: 'adPositionList', meta: { title: 'adPositionList', roles: ['admin:adPosition'] }},
      { path: 'ad/list',component: () => import('@/views/content/ad/list'),name: 'adList',meta: { title: 'adList', roles: ['admin:ad'] }},
      { path: 'ad/add', component: () => import('@/views/content/ad/add'), name: 'addAd', meta: { title: 'addAd', roles: ['admin:ad'] }, hidden: true },
      { path: 'ad/edit/:id(\\d+)', component: () => import('@/views/content/ad/edit'), name: 'editAd', meta: { title: 'editAd', roles: ['admin:ad'] }, hidden: true },
      { path: 'article_category/list',component: () => import('@/views/content/article_category/list'),name: 'articleCategoryList',meta: { title: 'articleCategoryList', roles: ['admin:articleCategory'] }},
      { path: 'article_category/add', component: () => import('@/views/content/article_category/add'), name: 'articleCategoryAdd', meta: { title: 'articleCategoryAdd', roles: ['admin:articleCategory'] }, hidden: true },
      { path: 'article_category/edit/:id(\\d+)', component: () => import('@/views/content/article_category/edit'), name: 'articleCategoryEdit', meta: { title: 'articleCategoryEdit', roles: ['admin:articleCategory'] }, hidden: true },
      { path: 'article/list',component: () => import('@/views/content/article/list'),name: 'articleList',meta: { title: 'articleList', roles: ['admin:article'] }},
      { path: 'article/add', component: () => import('@/views/content/article/add'), name: 'addArticle', meta: { title: 'addArticle', roles: ['admin:article'] }, hidden: true },
      { path: 'article/edit/:id(\\d+)', component: () => import('@/views/content/article/edit'), name: 'editArticle', meta: { title: 'editArticle', roles: ['admin:article'] }, hidden: true },
      /*{ path: 'tag/list',component: () => import('@/views/content/tag/list'),name: 'tagList',meta: { title: 'tagList', roles: ['admin:tag'] }}*/
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]
