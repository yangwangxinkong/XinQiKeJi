/**
 * Created by admin on 2018-09-03.
 */
let url = {
  //获取微信jsapi ticket
  jsapiTicket: '/weixin/jsapi/ticket',
  //获取微信jsapi sign
  jsapiSign: '/weixin/jsapi/sign',
  //短信验证码接口
  sendSmsCode: '/m/sms/sendCode',
  //用户注册接口
  register: '/m/register',
  //短信推广注册接口
  partnerRegister: '/m/register/partner',
  // 用户找回密码短信验证码接口
  sendSmsCodeByFindPassword: '/m/sms/sendSmsCodeByFindPassword',
  // 用户找回密码接口
  findPassword: '/m/register/findPassword',
  //用户注册协议接口
  agreement: '/m/article/agreement',
  //用户密码登录
  login: '/m/login/pwd',
  //根据code获取系统配置接口
  configInfo: '/m/config/info',
  //首页接口
  indexData: '/m/index',
  indexArticlesData: '/m/article/articles',
  //微信用户自动登录
  loginWxCode: '/m/login/wx',
  //获取登录用户接口
  current: 'm/member/current',
  //修改用户基本信息接口
  updateMember: '/m/member/update',
  //政策解读分类列表接口
  articleCategoryChildren: '/m/article/category/children',
  //政策解读详情列表数据接口
  articleCategoryChildrenArticles: '/m/article/category/children_article',
  //新手帮助列表数据接口
  articleCategoriesChildren: '/m/article/categories/children',
  //文章详情接口
  articleInfo: '/m/article/info',
  cityList: '/m/city/wxListAll',
  residenceTypeListByCityId: '/m/residenceType/listByCityId',
  quotationMemberInfo: '/m/quotation/quotationMemberInfo',
  quotationPayInfo: '/m/quotation/quotationPayInfo',
  quotationConfirmInfo: '/m/quotation/quotationConfirmInfo',
  quotationConfirmInfoTest: '/m/quotation/quotationConfirmInfoTest',
  quotationItemList: '/m/quotationItem/list',
  calculateQuotationBaseInfo: '/m/quotation/calculateQuotationBaseInfo',
  saveQuotationMemberInfo: '/m/quotation/saveQuotationMemberInfo',
  saveQuotationPayInfo: '/m/quotation/saveQuotationPayInfo',
  createOrder: '/m/quotation/createOrder',
  calculateQuotation: '/m/quotation/calculateQuotation',
  calculatePayInfo: '/m/quotation/calculatePayInfo',

  invoiceList: '/m/invoice/list',
  //发票索取订单列表
  invoiceOrderList: '/m/invoice/orders',
  //创建订单发票
  createInvoice: '/m/invoice/create',
  // 获取发票信息接口
  invoiceInfo: '/m/invoice/info',
  // 保存发票信息接口
  saveInvoiceInfo: '/m/invoice/save',

  // 订单列表
  orderList: '/m/order/list',
  // 未支付 取消订单
  orderCancel: '/m/order/cancel',
  orderCancelApply: '/m/orderCancelApply/view',
  refundAmount: '/m/orderCancelApply/refundAmount',
  refundSave: '/m/orderCancelApply/save',
  // 支付页面
  payInfo: '/m/order/payInfo',
  payOnline: '/m/payment/online',
  //会员充值
  recharge: '/m/payment/recharge',
  // 订单详情
  orderDetail: '/m/order/info',
  // 套餐详情
  activityDetail: '/m/activity/info',
  // 商品列表
  productList: '/m/product/list',
  //商品详情
  productDetail: '/m/product/info',
  //商品购买接口
  productBuyNow: '/m/cart/buy',
  //订单信息
  orderInfo: '/m/order/serviceInfo',
  //创建订单
  orderCreate: '/m/order/create',
  //用户地址相关接口
  areaList: '/m/receiver/areaList',
  addressList: '/m/receiver/list',
  addressInfo: '/m/receiver/info',
  addressSave: '/m/receiver/save',
  //兑换商品详情
  exchargeInfo: '/m/order/exchargeInfo',
  //推荐活动
  recommondActivities: '/m/activity/recommend',
  //优惠券列表
  couponList: '/m/coupon/list',
  //优惠券详情
  couponDetail: '/m/coupon/detail',
  //优惠券兑换
  couponExchange: '/m/coupon/exchange',
  //我的优惠券
  memberCoupons: '/m/member/coupon/list'
}

for (var key in url) {
  if (url.hasOwnProperty(key)) {
    url[key] = url[key]
  }
}

export default url
