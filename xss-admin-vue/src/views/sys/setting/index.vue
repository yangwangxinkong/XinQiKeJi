<template>
  <div class="createPost-container">
    <el-form :model="setting" label-position="right" label-width="180px" :rules="rules" ref="postForm">
    <sticky :className="'sub-navbar'" style="width: 100%">
      <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitFormEdit">更新</el-button>
    </sticky>
    <div class="tab-container">
    <el-tabs>
      <el-tab-pane label="基本设置">
          <el-form-item label="网站名称：" prop="siteName">
            <el-input style="width: 200px;" v-model="setting.siteName"></el-input>
          </el-form-item>
          <el-form-item label="网站网址：" prop="siteUrl">
            <el-input style="width: 200px;" v-model="setting.siteUrl"></el-input>
          </el-form-item>
          <el-form-item label="logo：" prop="logo">
            <el-col :span="10">
              <el-input style="width: 200px;float: left" v-model="setting.logo"></el-input>
              <singleImage @successCBK="imageSuccessCBK" :url='setting.logo'></singleImage>
            </el-col>
          </el-form-item>
          <el-form-item label="开户行：" prop="bankName">
            <el-input style="width: 200px;" v-model="setting.bankName"></el-input>
          </el-form-item>
          <el-form-item label="开户名：" prop="bankAccountName">
            <el-input style="width: 200px;" v-model="setting.bankAccountName"></el-input>
          </el-form-item>
          <el-form-item label="开户账号：" prop="bankAccount">
            <el-input style="width: 200px;" v-model="setting.bankAccount"></el-input>
          </el-form-item>
          <!-- <el-form-item label="集采业务咨询：" prop="cpContractName">
            <el-input style="width: 200px;" v-model="setting.cpContractName"></el-input>
          </el-form-item>
          <el-form-item label="集采热线：" prop="cpTelephone">
            <el-input style="width: 200px;" v-model="setting.cpTelephone"></el-input>
          </el-form-item>
          <el-form-item label="集采客服热线：" prop="cpCustomerTelephone">
            <el-input style="width: 200px;" v-model="setting.cpCustomerTelephone"></el-input>
          </el-form-item>
          <el-form-item label="商城服务热线：" prop="bsTelephone">
            <el-input style="width: 200px;" v-model="setting.bsTelephone"></el-input>
          </el-form-item>
          <el-form-item label="服务时间段：" prop="businessHours">
            <el-input style="width: 200px;" v-model="setting.businessHours"></el-input>
          </el-form-item>
          <el-form-item label="集采客服QQ：" prop="cpqq">
            <el-input style="width: 200px;" v-model="setting.cpqq"></el-input>
          </el-form-item>
          <el-form-item label="集采联系地址：" prop="cpContract">
            <el-input style="width: 200px;" v-model="setting.cpContract"></el-input>
          </el-form-item>
          <el-form-item label="集采邮编：" prop="cpZipcode">
            <el-input style="width: 200px;" v-model="setting.cpZipcode"></el-input>
          </el-form-item>
          <el-form-item label="单笔在线支付限额：" prop="onlinePayLimit">
            <el-input style="width: 200px;" v-model="setting.onlinePayLimit"></el-input>
          </el-form-item>
          <el-form-item label="保证金退还间隔天数：" prop="refundDepositInterval">
            <el-input style="width: 200px;" v-model="setting.refundDepositInterval"></el-input>
          </el-form-item>
          
          <el-form-item label="联系地址：" prop="address">
            <el-input style="width: 200px;" v-model="setting.address"></el-input>
          </el-form-item>
          <el-form-item label="包邮：" prop="freeShipping">
            <el-input style="width: 200px;" v-model="setting.freeShipping"></el-input>
          </el-form-item>
          <el-form-item label="联系电话：" prop="phone">
            <el-input style="width: 200px;" v-model="setting.phone"></el-input>
          </el-form-item>
          <el-form-item label="邮政编码：" prop="zipCode">
            <el-input style="width: 200px;" v-model="setting.zipCode"></el-input>
          </el-form-item>
          <el-form-item label="E-mail：" prop="email">
            <el-input style="width: 200px;" v-model="setting.email"></el-input>
          </el-form-item>
          
          <el-form-item label="是否网站开启：" prop="isSiteEnabled">
            <el-checkbox v-model="setting.isSiteEnabled"></el-checkbox>
          </el-form-item>
          <el-form-item label="网站关闭消息：" prop="siteCloseMessage">
            <el-input style="width: 400px;" type="textarea" :rows="7" v-model="setting.siteCloseMessage"></el-input>
          </el-form-item> -->
          <el-form-item label="热门搜索：" prop="hotSearch">
            <el-input style="width: 200px;" v-model="setting.hotSearch"></el-input>
          </el-form-item>
          <el-form-item label="备案编号：" prop="certtext">
            <el-input style="width: 200px;" v-model="setting.certtext"></el-input>
          </el-form-item>
          <el-form-item label="房型：" prop="houseType">
            <el-input style="width: 200px;" v-model="setting.houseType"></el-input>
          </el-form-item>
          <el-form-item label="房间名称：" prop="roomName">
            <el-input style="width: 200px;" v-model="setting.roomName"></el-input>
          </el-form-item>
          <el-form-item label="设计风格：" prop="designStyle">
            <el-input style="width: 200px;" v-model="setting.designStyle"></el-input>
          </el-form-item>
      </el-tab-pane>
      <el-tab-pane label="显示设置">
        <el-row>
          <el-col>
            <el-form-item label="商品图片(大)：" prop="largeProductImageWidth" style="float: left">
              宽度：<el-input style="width: 200px;" v-model="setting.largeProductImageWidth"></el-input>
            </el-form-item>
            <el-form-item  prop="largeProductImageHeight" labelWidth="0px" style="float: left;margin-left: 20px">
              高度：<el-input style="width: 200px;" v-model="setting.largeProductImageHeight"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="商品图片(中)：" prop="mediumProductImageWidth" style="float: left">
              宽度：<el-input style="width: 200px;" v-model="setting.mediumProductImageWidth"></el-input>
            </el-form-item>
            <el-form-item  prop="mediumProductImageHeight" labelWidth="0px" style="float: left;margin-left: 20px">
              高度：<el-input style="width: 200px;" v-model="setting.mediumProductImageHeight"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col>
            <el-form-item label="商品图片(缩略)：" prop="thumbnailProductImageWidth" style="float: left">
              宽度：<el-input style="width: 200px;" v-model="setting.thumbnailProductImageWidth"></el-input>
            </el-form-item>
            <el-form-item  prop="thumbnailProductImageHeight" labelWidth="0px" style="float: left;margin-left: 20px">
              高度：<el-input style="width: 200px;" v-model="setting.thumbnailProductImageHeight"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="默认商品图片(大)：" prop="defaultLargeProductImage">
          <el-col :span="10">
            <el-input style="width: 200px;float: left" v-model="setting.defaultLargeProductImage"></el-input>
            <singleImage @successCBK="imageSuccessCBK1" :url='setting.defaultLargeProductImage'></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="默认商品图片(中)：" prop="defaultMediumProductImage">
          <el-col :span="10">
            <el-input style="width: 200px;float: left" v-model="setting.defaultMediumProductImage"></el-input>
            <singleImage @successCBK="imageSuccessCBK2" :url='setting.defaultMediumProductImage'></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="默认商品图片(缩略)：" prop="defaultThumbnailProductImage">
          <el-col :span="10">
            <el-input style="width: 200px;float: left" v-model="setting.defaultThumbnailProductImage"></el-input>
            <singleImage @successCBK="imageSuccessCBK3" :url='setting.defaultThumbnailProductImage'></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="水印透明度：" prop="watermarkAlpha">
          <el-input style="width: 200px;" v-model="setting.watermarkAlpha" placeholder="取值范围:0-100,0全透明"></el-input>
        </el-form-item>
        <el-form-item label="水印图片：">
          <el-col :span="10">
            <el-input style="width: 200px;float: left" v-model="setting.watermarkImage"></el-input>
            <singleImage @successCBK="imageSuccessCBK4" :url='setting.watermarkImage'></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="水印位置:">
          <el-select class="filter-item" v-model="setting.watermarkPosition" placeholder="请选择">
            <el-option v-for="item in watermarkPositionOptions" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格精确位数:">
          <el-select class="filter-item" v-model="setting.priceScale" placeholder="请选择">
            <el-option v-for="item in priceScaleOptions" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="价格精确方式:">
          <el-select class="filter-item" v-model="setting.priceRoundType" placeholder="请选择">
            <el-option v-for="item in priceRoundTypeOptions" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否前台显示市场价：" prop="isShowMarketPrice">
          <el-checkbox v-model="setting.isShowMarketPrice"></el-checkbox>
        </el-form-item>
        <el-form-item label="默认市场价换算比例：" prop="defaultMarketPriceScale">
          <el-input style="width: 200px;" v-model="setting.defaultMarketPriceScale"></el-input>
        </el-form-item>
      </el-tab-pane>
      <el-tab-pane label="注册与安全">
        <el-form-item label="是否开放注册：" prop="isRegisterEnabled">
          <el-checkbox v-model="setting.isRegisterEnabled"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否允许E-mail重复注册：" prop="isDuplicateEmail">
          <el-checkbox v-model="setting.isDuplicateEmail"></el-checkbox>
        </el-form-item>
        <el-form-item label="禁用用户名：" prop="disabledUsername">
          <el-input style="width: 200px;" v-model="setting.disabledUsername"></el-input>
        </el-form-item>
        <el-form-item label="用户名最小长度：" prop="usernameMinLength">
          <el-input style="width: 200px;" v-model="setting.usernameMinLength"></el-input>
        </el-form-item>
        <el-form-item label="用户名最大长度：" prop="usernameMaxLength">
          <el-input style="width: 200px;" v-model="setting.usernameMaxLength"></el-input>
        </el-form-item>
        <el-form-item label="密码最小长度：" prop="passwordMinLength">
          <el-input style="width: 200px;" v-model="setting.passwordMinLength"></el-input>
        </el-form-item>
        <el-form-item label="密码最大长度：" prop="passwordMaxLength">
          <el-input style="width: 200px;" v-model="setting.passwordMaxLength"></el-input>
        </el-form-item>
        <el-form-item label="注册初始积分：" prop="registerPoint">
          <el-input style="width: 200px;" v-model="setting.registerPoint"></el-input>
        </el-form-item>
        <el-form-item label="注册协议：" prop="registerAgreement">
          <el-input style="width: 400px;" type="textarea" :rows="7" v-model="setting.registerAgreement"></el-input>
        </el-form-item>
        <el-form-item label="是否允许E-mail登录：" prop="isEmailLogin">
          <el-checkbox v-model="setting.isEmailLogin"></el-checkbox>
        </el-form-item>
        <el-form-item label="验证码类型:">
          <el-checkbox-group v-model="setting.captchaTypes">
            <el-checkbox v-for="item in captchaTypesOptions" :label="item.id"  :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="账号锁定类型:">
          <el-checkbox-group v-model="setting.accountLockTypes">
            <el-checkbox v-for="item in accountLockTypesOptions" :label="item.id"  :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="连续登录失败最大次数：" prop="accountLockCount">
          <el-input style="width: 200px;" v-model="setting.accountLockCount"></el-input>
        </el-form-item>
        <el-form-item label="自动解锁时间：" prop="accountLockTime">
          <el-input style="width: 200px;" v-model="setting.accountLockTime"></el-input>
        </el-form-item>
        <el-form-item label="安全密匙有效时间：" prop="safeKeyExpiryTime">
          <el-input style="width: 200px;" v-model="setting.safeKeyExpiryTime"></el-input>
        </el-form-item>
        <el-form-item label="上传文件最大限制：" prop="uploadMaxSize">
          <el-input style="width: 200px;" v-model="setting.uploadMaxSize"></el-input>
        </el-form-item>
        <el-form-item label="允许上传图片扩展名：" prop="uploadImageExtension">
          <el-input style="width: 200px;" v-model="setting.uploadImageExtension"></el-input>
        </el-form-item>
        <el-form-item label="允许上传Flash扩展名：" prop="uploadFlashExtension">
          <el-input style="width: 200px;" v-model="setting.uploadFlashExtension"></el-input>
        </el-form-item>
        <el-form-item label="允许上传媒体扩展名：" prop="uploadMediaExtension">
          <el-input style="width: 200px;" v-model="setting.uploadMediaExtension"></el-input>
        </el-form-item>
        <el-form-item label="允许上传文件扩展名：" prop="uploadFileExtension">
          <el-input style="width: 200px;" v-model="setting.uploadFileExtension"></el-input>
        </el-form-item>
        <el-form-item label="图片上传路径：" prop="imageUploadPath">
          <el-input style="width: 200px;" v-model="setting.imageUploadPath"></el-input>
        </el-form-item>
        <el-form-item label="Flash上传路径：" prop="flashUploadPath">
          <el-input style="width: 200px;" v-model="setting.flashUploadPath"></el-input>
        </el-form-item>
        <el-form-item label="媒体上传路径：" prop="mediaUploadPath">
          <el-input style="width: 200px;" v-model="setting.mediaUploadPath"></el-input>
        </el-form-item>
        <el-form-item label="文件上传路径：" prop="fileUploadPath">
          <el-input style="width: 200px;" v-model="setting.fileUploadPath"></el-input>
        </el-form-item>
      </el-tab-pane>
      <!--<el-tab-pane label="邮件设置">-->
        <!--<el-form-item label="发件人邮箱：" prop="smtpFromMail">-->
          <!--<el-input style="width: 200px;" v-model="setting.smtpFromMail"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="SMTP服务器地址：" prop="smtpHost">-->
          <!--<el-input style="width: 200px;" v-model="setting.smtpHost"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="SMTP服务器端口：" prop="smtpPort">-->
          <!--<el-input style="width: 200px;" v-model="setting.smtpPort"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="SMTP用户名：" prop="smtpUsername">-->
          <!--<el-input style="width: 200px;" v-model="setting.smtpUsername"></el-input>-->
        <!--</el-form-item>-->
        <!--<el-form-item label="SMTP密码：" prop="smtpPassword">-->
          <!--<el-input style="width: 200px;" v-model="setting.smtpPassword" placeholder="留空则不修改密码"></el-input>-->
        <!--</el-form-item>-->
      <!--</el-tab-pane>-->
      <el-tab-pane label="其他设置">
        <el-form-item label="货币符号：" prop="currencySign">
          <el-input style="width: 200px;" v-model="setting.currencySign"></el-input>
        </el-form-item>
        <el-form-item label="货币单位：" prop="currencyUnit">
          <el-input style="width: 200px;" v-model="setting.currencyUnit"></el-input>
        </el-form-item>
        <el-form-item label="库存警告数：" prop="stockAlertCount">
          <el-input style="width: 200px;" v-model="setting.stockAlertCount"></el-input>
        </el-form-item>
        <el-form-item label="库存分配时间点:">
          <el-select class="filter-item" v-model="setting.stockAllocationTime" placeholder="请选择">
            <el-option v-for="item in stockAllocationTimeOptions" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="默认积分换算比例：" prop="defaultPointScale">
          <el-input style="width: 200px;" v-model="setting.defaultPointScale"></el-input>
        </el-form-item>
        <el-form-item label="是否开启开发模式：" prop="isDevelopmentEnabled">
          <el-checkbox v-model="setting.isDevelopmentEnabled"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否开启评论：" prop="isReviewEnabled">
          <el-checkbox v-model="setting.isReviewEnabled"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否审核评论：" prop="isReviewCheck">
          <el-checkbox v-model="setting.isReviewCheck"></el-checkbox>
        </el-form-item>
        <el-form-item label="评论权限:">
          <el-select class="filter-item" v-model="setting.reviewAuthority" placeholder="请选择">
            <el-option v-for="item in reviewAuthorityOptions" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否开启咨询：" prop="isConsultationEnabled">
          <el-checkbox v-model="setting.isConsultationEnabled"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否审核咨询：" prop="isConsultationCheck">
          <el-checkbox v-model="setting.isConsultationCheck"></el-checkbox>
        </el-form-item>
        <el-form-item label="咨询权限:">
          <el-select class="filter-item" v-model="setting.consultationAuthority" placeholder="请选择">
            <el-option v-for="item in consultationAuthorityOptions" :key="item.value" :label="item.name" :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="是否开启发票功能：" prop="isInvoiceEnabled">
          <el-checkbox v-model="setting.isInvoiceEnabled"></el-checkbox>
        </el-form-item>
        <el-form-item label="是否开启含税价：" prop="isTaxPriceEnabled">
          <el-checkbox v-model="setting.isTaxPriceEnabled"></el-checkbox>
        </el-form-item>
        <el-form-item label="税率：" prop="taxRate">
          <el-input style="width: 200px;" v-model="setting.taxRate"></el-input>
        </el-form-item>
        <el-form-item label="Cookie路径：" prop="cookiePath">
          <el-input style="width: 200px;" v-model="setting.cookiePath"></el-input>
        </el-form-item>
        <el-form-item label="Cookie作用域：" prop="cookieDomain">
          <el-input style="width: 200px;" v-model="setting.cookieDomain"></el-input>
        </el-form-item>
        <el-form-item label="快递100授权KEY：" prop="kuaidi100Key">
          <el-input style="width: 200px;" v-model="setting.kuaidi100Key"></el-input>
        </el-form-item>
        <el-form-item label="电子合同域名：" prop="eContractUrl">
          <el-input style="width: 200px;" v-model="setting.eContractUrl"></el-input>
        </el-form-item>
        <el-form-item label="电子合同appid：" prop="eContractAppid">
          <el-input style="width: 200px;" v-model="setting.eContractAppid"></el-input>
        </el-form-item>
        <el-form-item label="电子合同appkey：" prop="eContractAppkey">
          <el-input style="width: 200px;" v-model="setting.eContractAppkey"></el-input>
        </el-form-item>
        <el-form-item label="合同签署跳转URL：" prop="eContractReturnUrl">
          <el-input style="width: 200px;" v-model="setting.eContractReturnUrl"></el-input>
        </el-form-item>
        <el-form-item label="合同卖家签署回调URL：" prop="eContractSellerNotifyUrl">
          <el-input style="width: 200px;" v-model="setting.eContractSellerNotifyUrl"></el-input>
        </el-form-item>
        <el-form-item label="合同买家签署回调URL：" prop="eContractBuyerNotifyUrl">
          <el-input style="width: 200px;" v-model="setting.eContractBuyerNotifyUrl"></el-input>
        </el-form-item>
      </el-tab-pane>
    </el-tabs>
    </div>
    </el-form>

  </div>
</template>

<script>
import { getSetting,updateSetting } from '@/api/sys'
import singleImage from '@/components/Upload/singleImage5'
import Sticky from '@/components/Sticky' // 粘性header组件

export default {
  name: 'setting',
  components: { singleImage,Sticky },
  data() {
    const validateRequired = (rule, value,callback)=>{
      if(!value){
        callback(new Error('必填'))
      } else {
        callback()
      }
    }
    return {
      loading: false,
      setting: {},
      watermarkPositionOptions: [{name: '无',value: 'no'},{name: '左上',value: 'topLeft'},{name: '右上',value: 'topRight'},{name: '居中',value: 'center'},{name: '左下',value: 'bottomLeft'},{name: '右下',value: 'bottomRight'}],
      priceScaleOptions: [{name: '无小数位',value: 0},{name: '1位小数',value: 1},{name: '2位小数',value: 2},{name: '3位小数',value: 3}],
      priceRoundTypeOptions: [{name: '四舍五入',value: 'roundHalfUp'},{name: '向上取整',value: 'roundUp'},{name: '向下取整',value: 'roundDown'}],
      captchaTypesOptions: [{name: '会员登陆',id: 'memberLogin'},{name: '会员注册',id: 'memberRegister'},{name: '后台登陆',id: 'adminLogin'},{name: '商品评论',id: 'review'},
          {name: '退款申请',id: 'refundApply'},{name: '退货申请',id: 'returnApply'},{name: '商品咨询',id: 'consultation'},{name: '找回密码',id: 'findPassword'},
          {name: '重置密码',id: 'resetPassword'},{name: '其它',id: 'other'}],
      accountLockTypesOptions: [{name: '会员',id: 'member'},{name: '管理员',id: 'admin'}],
      stockAllocationTimeOptions: [{name: '下订单',value: 'order'},{name: '订单支付',value: 'payment'},{name: '订单发货',value: 'ship'}],
      reviewAuthorityOptions: [{name: '任何访问者',value: 'anyone'},{name: '注册会员',value: 'member'},{name: '已购买会员',value: 'purchased'}],
      consultationAuthorityOptions: [{name: '任何访问者',value: 'anyone'},{name: '注册会员',value: 'member'}],
      rules: {
          siteName: [{required: true,validator: validateRequired, trigger: 'blur'}],
          siteUrl: [{required: true,validator: validateRequired, trigger: 'blur'}],
          logo: [{required: true,validator: validateRequired, trigger: 'blur'}],
          bankName: [{required: true,validator: validateRequired, trigger: 'blur'}],
          bankAccountName: [{required: true,validator: validateRequired, trigger: 'blur'}],
          bankAccount: [{required: true,validator: validateRequired, trigger: 'blur'}],
          hotSearch: [{required: true,validator: validateRequired, trigger: 'blur'}],
          houseType: [{required: true,validator: validateRequired, trigger: 'blur'}],
          roomName: [{required: true,validator: validateRequired, trigger: 'blur'}],
          designStyle: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // cpContractName: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // cpTelephone: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // cpCustomerTelephone: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // bsTelephone: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // businessHours: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // cpqq: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // cpContract: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // cpZipcode: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // onlinePayLimit: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // refundDepositInterval: [{required: true,validator: validateRequired, trigger: 'blur'}],
          // siteCloseMessage: [{required: true,validator: validateRequired, trigger: 'blur'}],
          largeProductImageWidth: [{required: true,validator: validateRequired, trigger: 'blur'}],
          largeProductImageHeight: [{required: true,validator: validateRequired, trigger: 'blur'}],
          mediumProductImageWidth: [{required: true,validator: validateRequired, trigger: 'blur'}],
          mediumProductImageHeight: [{required: true,validator: validateRequired, trigger: 'blur'}],
          thumbnailProductImageWidth: [{required: true,validator: validateRequired, trigger: 'blur'}],
          thumbnailProductImageHeight: [{required: true,validator: validateRequired, trigger: 'blur'}],
          defaultLargeProductImage: [{required: true,validator: validateRequired, trigger: 'blur'}],
          defaultMediumProductImage: [{required: true,validator: validateRequired, trigger: 'blur'}],
          defaultThumbnailProductImage: [{required: true,validator: validateRequired, trigger: 'blur'}],
          watermarkAlpha: [{required: true,validator: validateRequired, trigger: 'blur'}],
          defaultMarketPriceScale: [{required: true,validator: validateRequired, trigger: 'blur'}],
          usernameMinLength: [{required: true,validator: validateRequired, trigger: 'blur'}],
          usernameMaxLength: [{required: true,validator: validateRequired, trigger: 'blur'}],
          passwordMinLength: [{required: true,validator: validateRequired, trigger: 'blur'}],
          passwordMaxLength: [{required: true,validator: validateRequired, trigger: 'blur'}],
          registerPoint: [{required: true,validator: validateRequired, trigger: 'blur'}],
          registerAgreement: [{required: true,validator: validateRequired, trigger: 'blur'}],
          accountLockCount: [{required: true,validator: validateRequired, trigger: 'blur'}],
          accountLockTime: [{required: true,validator: validateRequired, trigger: 'blur'}],
          safeKeyExpiryTime: [{required: true,validator: validateRequired, trigger: 'blur'}],
          uploadMaxSize: [{required: true,validator: validateRequired, trigger: 'blur'}],
          imageUploadPath: [{required: true,validator: validateRequired, trigger: 'blur'}],
          flashUploadPath: [{required: true,validator: validateRequired, trigger: 'blur'}],
          mediaUploadPath: [{required: true,validator: validateRequired, trigger: 'blur'}],
          fileUploadPath: [{required: true,validator: validateRequired, trigger: 'blur'}],
//          smtpFromMail: [{required: true,validator: validateRequired, trigger: 'blur'}],
//          smtpHost: [{required: true,validator: validateRequired, trigger: 'blur'}],
//          smtpPort: [{required: true,validator: validateRequired, trigger: 'blur'}],
//          smtpUsername: [{required: true,validator: validateRequired, trigger: 'blur'}],
          currencySign: [{required: true,validator: validateRequired, trigger: 'blur'}],
          currencyUnit: [{required: true,validator: validateRequired, trigger: 'blur'}],
          stockAlertCount: [{required: true,validator: validateRequired, trigger: 'blur'}],
          defaultPointScale: [{required: true,validator: validateRequired, trigger: 'blur'}],
          taxRate: [{required: true,validator: validateRequired, trigger: 'blur'}],
          cookiePath: [{required: true,validator: validateRequired, trigger: 'blur'}],
          eContractUrl: [{required: true,validator: validateRequired, trigger: 'blur'}],
          eContractAppid: [{required: true,validator: validateRequired, trigger: 'blur'}],
          eContractAppkey: [{required: true,validator: validateRequired, trigger: 'blur'}],
          eContractReturnUrl: [{required: true,validator: validateRequired, trigger: 'blur'}],
          eContractSellerNotifyUrl: [{required: true,validator: validateRequired, trigger: 'blur'}],
          eContractBuyerNotifyUrl: [{required: true,validator: validateRequired, trigger: 'blur'}]
      }
    }
  },
  created() {
    this.getSetting()
  },
  methods: {
    getSetting() {
      getSetting().then(response => {
        this.setting = response.data.data
      }).catch((e)=>{
        console.log(e)
      })
    },
    submitFormEdit() {
      console.log("edit param = " + this.setting)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loading = true
          updateSetting(this.setting).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '修改系统设置成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              setTimeout(() => {
                this.loading = false
                this.$router.replace({ path: '/dashboard'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '修改系统设置失败',
                type: 'error',
                duration: 2000
              })
              this.loading = false
            }
          })
        } else {
          this.$message({
            message: '请输入必填项',
            type: 'error',
            duration: 2 * 1000
          })
          return false
        }
      })
    },
    imageSuccessCBK(path) {
      this.setting.logo = path
    },
    imageSuccessCBK1(path) {
      this.setting.defaultLargeProductImage = path
    },
    imageSuccessCBK2(path) {
      this.setting.defaultMediumProductImage = path
    },
    imageSuccessCBK3(path) {
      this.setting.defaultThumbnailProductImage = path
    },
    imageSuccessCBK4(path) {
      this.setting.watermarkImage = path
    }
  }
}
</script>

<style scoped>
  .tab-container{
    margin: 30px;
  }
</style>
