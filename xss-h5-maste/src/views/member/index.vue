<!--  用户中心-->
<template>
  <div class="page">
    <div class="container">
      <!-- 用户信息 -->
      <div class="model-user-wrap" :style="{backgroundImage:'url(' +memberBg + ')'}">
        <div class="weui-media-box weui-media-box_text">
          <div class="weui-media-box_appmsg">
            <div class="weui-media-box__hd"><img :src="memberInfo.headImage" alt="" class="weui-media-box__thumb"></div>
            <div class="weui-media-box__bd">
              <h4 class="weui-media-box__title" style="color:#ffffff;">{{memberInfo.nickName}}</h4>
              <p class="weui-media-box__desc" style="color:#ffffff;">{{memberInfo.mobile}}</p>
            </div>
          </div>
        </div>
      </div>
      <!-- center-con -->
      <div class="center-con">
        <group :gutter="10">
          <cell title="资料管理" link="/member/ziliao/index">
            <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_information@2x.png">
          </cell>
        </group>
        <group :gutter="10">
          <cell title="账户余额" link="/member/zhanghu/index">
            <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_account@2x.png">
          </cell>
          <cell title="我的优惠券" link="/member/coupon/list">
            <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_account@2x.png">
          </cell>
          <cell title="我的订单" link="/member/order/index">
            <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_afterS@2x.png">
          </cell>
          <cell title="我的兑换" link="/member/order/excharge">
            <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_afterS@2x.png">
          </cell>
          <cell title="发票服务" link="/member/invoiceservice/index">
            <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_invoice@2x.png">
          </cell>
          <!--  -->
          <!-- <cell title="售后服务"  link="/member/after-sales-service/index">
                    <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_afterS@2x.png">
                </cell>-->
          <cell title="邀请好友" link="/member/invitation/index">
            <img
              slot="icon"
              width="20"
              style="display:block;margin-right:5px;"
              src="../../assets/PersonalC_icon_invoice@2x.png"
            >
          </cell>
          <cell title="意见反馈" link="/member/feedback/index">
            <img
              slot="icon"
              width="20"
              style="display:block;margin-right:5px;"
              src="../../assets/PersonalC_icon_feedback@2x.png"
            >
          </cell>
          <!--<cell title="设置"  link="/member/setting/index">
                    <img slot="icon" width="20" style="display:block;margin-right:5px;" src="../../assets/PersonalC_icon_afterS@2x.png">
               </cell>-->
        </group>
         <group :gutter="10">
          <cell title="关于我们" link="/member/about/index">
            <img
              slot="icon"
              width="20"
              style="display:block;margin-right:5px;"
              src="../../assets/PersonalC_icon_aboutus@2x.png"
            >
          </cell>
        </group>
        <div class="padding-15 mt20">
          <x-button @click.native="logout" type="primary" >退出/切换账户</x-button>
        </div>

      </div>
    </div>
  </div>
</template>

<script>
import { Panel, Cell, Group,XButton } from "vux";
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
import { removeToken } from '@/utils/auth'
export default {
  data() {
    return {
       memberBg: require("../../assets/member-bg.png"),
      memberInfo: {
        id: undefined,
        headImage: require("../../assets/touxiang.png"),
        fallbackSrc: require("../../assets/touxiang.png"),
        name: "小豆社保",
        nickName: '小豆社保',
        username: "",
        mobile: undefined
      }
    };
  },

  components: {
    Panel,
    Cell,
    Group,
    XButton
  },

  computed: {},

  mounted() {},

  created() {
    let member = storage.get("member");
    this.memberInfo.id = member.id;
    if (member.headImage) {
      this.memberInfo.headImage = member.headImage;
    }
    this.memberInfo.name = member.name;
    this.memberInfo.nickName = member.nickName;
    this.memberInfo.username = member.username;
    this.memberInfo.mobile = member.mobile;
  },

  methods: {
    logout(){
      let that=this;
      this.$vux.confirm.show({
        content:'您确定退出登录吗？',
        onConfirm () {
          removeToken();
          that.$router.push({path:"/login/index"});
        }
      });
    }
  }
};
</script>
<style lang='less' scoped>
@import "~vux/src/styles/weui/widget/weui_media_box/weui_media_box";
.model-user-wrap {
   height: 4.613rem;
  width: 100%;
  background-size: contain;
  background-position: center;
  .weui-media-box {
    top: 1.8rem;
  }
  .weui-media-box_appmsg .weui-media-box__thumb {
    border-radius: 50%;
  }
  .weui-media-box__bd {
    position: relative;
    /*&:after {*/
    /*content: " ";*/
    /*display: inline-block;*/
    /*height: 6px;*/
    /*width: 6px;*/
    /*border-width: 2px 2px 0 0;*/
    /*border-color: #c8c8cd;*/
    /*border-style: solid;*/
    /*-webkit-transform: matrix(0.71, 0.71, -0.71, 0.71, 0, 0);*/
    /*transform: matrix(0.71, 0.71, -0.71, 0.71, 0, 0);*/
    /*position: relative;*/
    /*top: -2px;*/
    /*position: absolute;*/
    /*top: 50%;*/
    /*margin-top: -4px;*/
    /*right: 2px;*/
    /*}*/
  }
}
.weui-btn_primary{
  background-color: @color_base;
}
</style>
