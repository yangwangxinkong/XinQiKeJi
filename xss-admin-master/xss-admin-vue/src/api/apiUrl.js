/**
 * Created by admin on 2018-09-03.
 */
let url = {
  //系统配置
  configList: '/api/config/list',
  configInfo: '/api/config/info',
  configSave: '/api/config/save',
  configDelete: '/api/config/delete',

  //内容版块
  adPositionTypes: '/api/adPosition/getTypes',//初始化广告位类型接口
  adPositionList: '/api/adPosition/list',//广告位列表接口
  adPositionInfo: '/api/adPosition/info',//广告位详情接口
  adPositionSave: '/api/adPosition/save',//广告位保存或更新接口
  adPositionDelete: '/api/adPosition/delete',//广告位批量删除接口
  adPositionTree: '/api/ad/adPositionList',//广告位层级列表接口
  adTypeList: '/api/ad/typeList', //广告类型接口
  adList: '/api/ad/list',//广告列表接口
  adInfo: '/api/ad/info',//广告详情接口
  adSave: '/api/ad/save',//广告保存或更新接口
  adDelete: '/api/ad/delete',//广告批量删除接口
  articleCategoryTree: '/api/article_category/tree',//文章分类层级接口
  articleCategoryList: '/api/article_category/list',//文章分类列表接口
  articleCategoryCheckCode: '/api/article_category/check_code',//文章分类检查编码是否存在接口
  articleCategoryInfo: '/api/article_category/info',//文章分类详情接口
  articleCategorySave: '/api/article_category/save',//文章分类保存或更新接口
  articleCategoryDelete: '/api/article_category/delete',//文章分类批量删除接口

  // 会员版块
  memberList: '/api/member/list',//会员列表接口
  memberSearch: '/api/member/search',//会员条件搜索接口
  memberInfo: '/api/member/info',//会员详细信息接口
  memberSave: 'api/member/save',//会员保存或更新接口
  memberDelete: '/api/member/delete',//会员批量删除接口
  exportMember:'/api/member/export', //导出用户
  // imgDownload: '/api/member/download',//会员图片下载

  // 1城市管理
  cityCategoryTree: '/api/city/tree',//城市分类层级接口
  cityInfo: '/api/city/info',//城市信息接口
  citySave: 'api/city/save',//城市信息保存接口
  cityCheckCode: '/api/city/check_code',//城市检查编码是否存在接口

  // 城市管理 《-地区管理
  cityChildren: '/api/city/children',
  cityUpdate: '/api/city/update',
  cityDelete: '/api/city/delete',

  // 户口性质管理
  residenceTypeList: '/api/residenceType/list',
  residenceTypeInfo: '/api/residenceType/info',
  residenceTypeSave: '/api/residenceType/save',
  residenceTypeDelete: '/api/residenceType/delete',

  // 缴费基数管理
  payBaseList: '/api/payBase/list',
  payBaseInfo: '/api/payBase/info',
  payBaseSave: '/api/payBase/save',
  payBaseDelete: '/api/payBase/delete',

  // 优惠券
  couponCodeList: '/api/coupon/code/list',
  // 分享管理
  shareList: '/api/share/list',
  // 意见反馈管理
  feedbackList: '/api/feedback/list',
  // 意见反馈信息
  feedbackInfo: '/api/feedback/info',
  // 申请人管理
  proposerList: '/api/proposer/list',
}

for (var key in url) {
  if (url.hasOwnProperty(key)) {
    url[key] = url[key]
  }
}

export default url
