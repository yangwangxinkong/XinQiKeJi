import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
const Home = resolve => require(['@/views/home/index'], resolve)

const Index = resolve => require(['@/views/index/index'], resolve)

const ArticleDetail = resolve => require(['@/views/article/detail'], resolve)
const Consulting = resolve => require(['@/views/consulting/list'], resolve)
const Instrument = resolve => require(['@/views/instrument/index'], resolve)
const Member = resolve => require(['@/views/member/index'], resolve)
const PreInsurance = resolve => require(['@/views/pre_insurance/index'], resolve)
const PolicyList = resolve => require(['@/views/policy/index'], resolve)
const Policyinfo = resolve => require(['@/views/policy/detail'], resolve)
const Help = resolve => require(['@/views/help/index'], resolve)
const invoiceService = resolve => require(['@/views/member/children/invoice-service/index'], resolve)
const SelectOrder = resolve => require(['@/views/member/children/invoice-service/children/selectOrderList'], resolve)
const InvoiceInfo = resolve => require(['@/views/member/children/invoice-service/children/invoiceInfo'], resolve)
const InvoiceList = resolve => require(['@/views/member/children/invoice-service/children/invoiceList'], resolve)
const Order = resolve => require(['@/views/member/children/order/index'], resolve)
const CancelOrder = resolve => require(['@/views/member/children/order/cancelOrder'], resolve)
const OrderDetail = resolve => require(['@/views/member/children/order/orderDetail'], resolve)
const Ziliao = resolve => require(['@/views/member/children/ziliao/index'], resolve)
const Zhanghu = resolve => require(['@/views/member/children/zhanghu/index'], resolve)
const CouponList = resolve => require(['@/views/member/children/coupon/list'], resolve)
const CouponSelect = resolve => require(['@/views/member/children/coupon/select'], resolve)
const Setting = resolve => require(['@/views/member/children/setting/index'], resolve)
const Login = resolve => require(['@/views/login/index'], resolve)
const Register = resolve => require(['@/views/register/index'], resolve)
const FindPassword = resolve => require(['@/views/password/index'], resolve)
const userAgreement = resolve => require(['@/views/register/children/useragreement'], resolve)
    //
const orderMember = resolve => require(['@/views/order/orderMember'], resolve)
const orderInfo = resolve => require(['@/views/order/orderInfo'], resolve)
const orderConfirm = resolve => require(['@/views/order/orderConfirm'], resolve)
const orderPay = resolve => require(['@/views/order/orderPay'], resolve)
const orderItemInfo = resolve => require(['@/views/order/orderItemInfo'], resolve)

const calculatorInfo = resolve => require(['@/views/calculator/calculatorInfo'], resolve)
const calculatorDetail = resolve => require(['@/views/calculator/calculatorDetail'], resolve)

const AfterSalesService = resolve => require(['@/views/member/children/after-sales-service/index'], resolve)
const About = resolve => require(['@/views/member/children/about/index'], resolve)
const Package = resolve => require(['@/views/package/index'], resolve)
const Partners = resolve => require(['@/views/partners/index'], resolve)
const RechargeInfo = resolve => require(['@/views/recharge/info'], resolve)
const RechargeResult = resolve => require(['@/views/recharge/result'], resolve)
const PaymentResult = resolve => require(['@/views/order/paymentResult'], resolve)
const ProductList = resolve => require(['@/views/product/list'], resolve)
const ProductDetail = resolve => require(['@/views/product/detail'], resolve)
const ExchangeList = resolve => require(['@/views/exchange/list'], resolve)
const ExchangeDetail = resolve => require(['@/views/exchange/couponDetail'], resolve)
const OrderInfo = resolve => require(['@/views/order/info'], resolve)
const OrderResult = resolve => require(['@/views/order/orderResult'], resolve)
const AddressList = resolve => require(['@/views/address/list'], resolve)
const AddressAdd = resolve => require(['@/views/address/add'], resolve)
const OrderExchargeList = resolve => require(['@/views/member/children/order/excharges'], resolve)
const OrderExchargeDetail = resolve => require(['@/views/member/children/order/exchargeDetail'], resolve)


//
const SocialInquire = resolve => require(['@/views/social_inquire/index'], resolve)
const FeedBack = resolve => require(['@/views/member/children/feedback/index'], resolve)
const Invitation = resolve => require(['@/views/member/children/invitation/index'], resolve)


export const constantRouterMap = [
    // {
    //     path: '/',
    //     component: Home,
    //     meta: { title: '小豆社保', isShowNav: false }
    // },
    //新首页
    {
        path: '/',
        component: Index,
        meta: { title: '小豆社保', isShowNav: false, isShowBar: true }
    },
    {
        path: '/consulting/list',
        component: Consulting,
        meta: { title: '资讯', isShowNav: true, isShowBar: true }

    },
    {
        path: '/article/detail',
        component: ArticleDetail,
        meta: { title: '文章详情', isShowNav: true }
    },
    {
        path: '/instrument/index',
        component: Instrument,
        meta: { title: '自助工具', isShowNav: true, isShowBar: true }
    },
    {
        path: '/member/index',
        component: Member,
        meta: { title: '用户中心', login: true, isShowNav: true, isShowBar: true }
    },
    // 社保
    {
        path: '/order/orderMember',
        component: orderMember,
        meta: { title: '填写个人资料', login: true, isShowNav: true }
    },
    {
        path: '/order/orderInfo',
        component: orderInfo,
        meta: { title: '填写参保资料', login: true, isShowNav: true }
    },
    {
        path: '/order/orderItemInfo',
        component: orderItemInfo,
        meta: { title: '订单明细', login: true, isShowNav: true }
    },
    {
        path: '/order/orderConfirm',
        component: orderConfirm,
        meta: { title: '订单确认', login: true, isShowNav: true }
    },
    {
        path: '/order/orderPay',
        component: orderPay,
        meta: { title: '支付订单', login: true, isShowNav: true }
    },
    // 社保计算器
    {
        path: '/calculator/calculatorInfo',
        component: calculatorInfo,
        meta: { title: '计算器', isShowNav: true }
    },
    {
        path: '/calculator/calculatorDetail',
        component: calculatorDetail,
        meta: { title: '计算结果', isShowNav: true }
    },
    {
        path: '/pre_insurance/index',
        component: PreInsurance,
        meta: { title: '参保资料', login: true, isShowNav: true }
    },
    {
        path: '/policy/index',
        component: PolicyList,
        meta: { title: '政策解读', isShowNav: true }
    },
    {
        path: '/policy/detail',
        component: Policyinfo,
        meta: { title: '政策详情', isShowNav: true }
    },
    {
        path: '/help/index',
        component: Help,
        meta: { title: '新手帮助', isShowNav: true }
    },
    {
        path: '/member/order/index',
        component: Order,
        meta: { title: '我的订单', login: true, isShowNav: true }
    },
    {
        path: '/member/order/orderDetail',
        component: OrderDetail,
        meta: { title: '订单详情', login: true, isShowNav: true }
    },
    {
        path: '/member/order/cancelOrder',
        component: CancelOrder,
        meta: { title: '取消订单', login: true, isShowNav: true }
    },
    {
        path: '/member/invoiceservice/index',
        component: invoiceService,
        meta: { title: '发票服务', login: true, isShowNav: true }
    },
    {
        path: '/member/invoice/info',
        component: InvoiceInfo,
        meta: { title: '发票信息管理', login: true, isShowNav: true }
    },
    {
        path: '/member/invoice/list',
        component: InvoiceList,
        meta: { title: '已开发票', login: true, isShowNav: true }
    },
    {
        path: '/member/invoice/selectOrder',
        component: SelectOrder,
        meta: { title: '发票索取' }
    },

    {
        path: '/member/ziliao/index',
        component: Ziliao,
        meta: { title: '资料', login: true, isShowNav: true }
    },
    {
        path: '/member/zhanghu/index',
        component: Zhanghu,
        meta: { title: '账户余额', login: true, isShowNav: true }
    },
    {
        path: '/member/coupon/list',
        component: CouponList,
        meta: { title: '我的优惠券', login: true, isShowNav: true }
    },
    {
        path: '/member/coupon/select',
        component: CouponSelect,
        meta: { title: '选择优惠券', login: true, isShowNav: true }
    },
    {
        path: '/member/about/index',
        component: About,
        meta: { title: '关于我们', isShowNav: true }
    },
    {
        path: '/member/setting/index',
        component: Setting,
        meta: { title: '设置', login: true, isShowNav: true }
    },
    {
        path: '/login/index',
        component: Login,
        meta: { title: '登录', isShowNav: true }
    },
    {
        path: '/register/index',
        component: Register,
        meta: { title: '注册', isShowNav: true }
    },
    {
        path: '/password/index',
        component: FindPassword,
        meta: { title: '找回密码', isShowNav: true }
    },
    {
        path: '/register/useragreement/index',
        component: userAgreement,
        meta: { title: '用户协议', isShowNav: true }
    },
    //
    {
        path: '/member/after-sales-service/index',
        component: AfterSalesService,
        meta: { title: '售后服务', isShowNav: true }
    },
    {
        path: '/package/index',
        component: Package,
        meta: { title: '套餐介绍', login: true, isShowNav: true }
    },
    {
        path: '/partners/index',
        component: Partners,
        meta: { title: '注册', isShowNav: true }
    },
    {
        path: '/order/paymentResult',
        component: PaymentResult,
        meta: { title: '支付结果', login: true, isShowNav: true }
    },
    {
        path: '/recharge/info',
        component: RechargeInfo,
        meta: { title: '会员充值', login: true, isShowNav: true }
    },
    {
        path: '/recharge/result',
        component: RechargeResult,
        meta: { title: '会员充值结果', isShowNav: true }
    },
    {
        path: '/product/list',
        component: ProductList,
        meta: { title: '小豆福利', isShowNav: true }
    },
    {
        path: '/exchange/list',
        component: ExchangeList,
        meta: { title: '兑换商品列表', login: true, isShowNav: true }
    },
    {
        path: '/coupon/detail',
        component: ExchangeDetail,
        meta: { title: '代金券详情', login: true, isShowNav: true }
    },
    {
        path: '/product/detail',
        component: ProductDetail,
        meta: { title: '商品详情', login: true, isShowNav: true }
    },
    {
        path: '/order/info',
        component: OrderInfo,
        meta: { title: '订单确认', login: true, isShowNav: true }
    },
    {
        path: '/order/result',
        component: OrderResult,
        meta: { title: '订单结果', login: true, isShowNav: true }
    },
    {
        path: '/address/list',
        component: AddressList,
        meta: { title: '地址列表', login: true, isShowNav: true }
    },
    {
        path: '/address/add',
        component: AddressAdd,
        meta: { title: '地址新增', login: true, isShowNav: true }
    },
    {
        path: '/member/order/excharge',
        component: OrderExchargeList,
        meta: { title: '我的兑换', login: true, isShowNav: true }
    },
    {
        path: '/member/order/exchargeDetail',
        component: OrderExchargeDetail,
        meta: { title: '兑换详情', login: true, isShowNav: true }
    },

    {
        path: '/social/inquire',
        component: SocialInquire,
        meta: { title: '社保查询', login: true, isShowNav: true }
    },
    {
        path: '/member/feedback/index',
        component: FeedBack,
        meta: { title: '意见反馈', login: true, isShowNav: true }
    },
    {
        path: '/member/invitation/index',
        component: Invitation,
        meta: { title: '邀请奖励', login: true, isShowNav: true }
    }
]
export default new Router({
    scrollBehavior: () => ({ y: 0 }),
    routes: constantRouterMap
})