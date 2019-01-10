/**
 * Created by admin on 2018-09-03.
 */
let url = {
  //短信验证码接口
  sendSmsCode: '/m/sms/sendCode',
  //用户注册接口
  register: '/m/register',
  //用户注册协议接口
  agreement: '/m/article/agreement',
  //用户密码登录
  login: '/m/login/pwd',
  //根据code获取系统配置接口
  configInfo: '/m/config/info',

  // 首页获取服务电话
  serviceCall: '/m/web/serviceCall',
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
  // 订单详情
  orderDetail: '/m/order/info',

  // PC 首页接口
  indexData: '/m/web/index',
  // PC 缴社保页面接口
  getShbInfo: '/m/web/shbInfo',
  // 社保 公积金 文章
  getArticles: '/m/article/articles',
  getCategoryArticleList: '/m/article/category/category_article',
  // 新闻资讯 文章列表
  getArticleList: '/m/article/getArticleList',
  // 活动详情
  getActivityInfo: '/m/activity/info'

}

for (var key in url) {
    if (url.hasOwnProperty(key)) {
        url[key] = url[key]
    }
}

export default url
