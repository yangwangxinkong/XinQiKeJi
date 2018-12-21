
<!--账户信息  -->
<template>
  <div class="page">
    <div class="container invitation-container" style="padding-bottom:20px">
      <div class="invitation_content tc">
        <grid class="user_data mt10">
          <grid-item>
            <p class="f18 fw">5人</p>
            <p class="text">成功邀请(人)</p>
          </grid-item>
          <grid-item>
            <p class="f18 fw">0.00元</p>
            <p class="text">累计收益(元)</p>
          </grid-item>
        </grid>
      </div>
      <div class="head-invite tc">
        <div class="invite-title">
          <p>
            <span>简单三步 立享权益</span>
          </p>
        </div>
        <div class="invite-progress">
          <img src="../../../../assets/invite-progress.png" class="image" alt>
          <flexbox class="mt10 pb10">
            <flexbox-item class="tl">
              <p style="color: rgba(45,46,70,0.80);padding-left: 5px;">点击分享按钮</p>
            </flexbox-item>
            <flexbox-item class="tc">
              <p style="color:  rgba(45,46,70,0.80);">好友注册并下单</p>
            </flexbox-item>
            <flexbox-item class="tr">
              <p style="color: rgba(45,46,70,0.80);padding-right: 5px;">领取红包奖励</p>
            </flexbox-item>
          </flexbox>
        </div>
        <box gap="25px 10px 0" style="padding-bottom:25px;">
          <p class="pb10" style="color: #9EA0B6;">即刻分享，多邀多得，红包金额无上限</p>
          <x-button :gradients="['#F9291F ', '#FB5737']" class="btn_round">去邀请</x-button>
        </box>
      </div>
      <div class="bg_f mt10 pt10">
        <div class="invite-title">
          <p>
            <span>邀请方式</span>
          </p>
        </div>
        <grid :show-vertical-dividers="false">
          <grid-item class="f14" :label="grid.label" v-for="(grid,index) in GridList" :key="index">
            <img slot="icon" :src="grid.icon">
          </grid-item>
        </grid>
      </div>
      <div class="mt10 bg_f pt10">
        <div class="invite-title">
          <p>
            <span>面对面扫码邀请</span>
          </p>
        </div>
        <div class="qrcode-box">
          <img src="../../../../assets/login-qrcode.png" class="image" alt>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { XButton, Group, Grid, GridItem, Box, Flexbox, FlexboxItem } from "vux";
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  components: { XButton, Group, Grid, GridItem, Box, Flexbox, FlexboxItem },
  data() {
    return {
      GridList: [
        {
          id: 1,
          label: "微信",
          icon: require("../../../../assets/wx.png")
        },
        {
          id: 2,
          label: "朋友圈",
          icon: require("../../../../assets/friends.png")
        },
        {
          id: 3,
          label: "微博",
          icon: require("../../../../assets/wb.png")
        },
        {
          id: 4,
          label: "QQ",
          icon: require("../../../../assets/qq.png")
        }
      ]
    };
  },

  computed: {},

  mounted() {},

  created() {
    this.getMemberInfo();
  },

  methods: {
    getMemberInfo() {
      let member = storage.get("member");
      get(url.current, { id: member.id }).then(response => {
        if (response.data.result == "00000000") {
          this.balance = response.data.data.balance;
          this.list[0].value = this.balance;
          this.list[2].value = response.data.data.point;
          storage.set("member", response.data.data);
        }
      });
    }
  }
};
</script>
<style lang='less' >
.invitation-container {
  .weui-grids {
    &:before {
      content: none;
    }
  }
  .weui-grid__icon {
    width: 1.867rem;
    height: 1.867rem;
    & {
      + .weui-grid__label {
        color: rgba(45, 46, 70, 0.8);
      }
    }
  }
}
.invitation_content {
  color: #fff;
  position: relative;

  .user_border {
    border-radius: 20px;
    width: 120px;
    height: 34px;
    line-height: 34px;
    border: 1px solid #fff;
    margin: auto;
  }
  .user_data {
    width: 100%;
    background-color: #fff;
  }
  .fw {
    font-weight: bold;
  }
  .weui-grids {
    &:after,
    &:before {
      content: none;
    }
  }
  .weui-grid {
    &:before {
      top: 50%;
      transform: translate(-50%, -50%);
    }
    &:after {
      height: 0;
      border-bottom: 0;
    }
    p.text {
      color: #9ea0b6;
    }
  }
  .weui-grid:last-child:before {
    border-right: 0;
    width: 0;
  }
}
.invite-title {
  font-family: PingFangSC-Medium;
  font-size: 18px;
  color: #2d2e46;
  letter-spacing: 0.24px;
  text-align: center;
  padding: 10px 0;
  span {
    position: relative;
    &:after {
      position: absolute;
      content: "";
      background: url(../../../../assets/title-r.png) no-repeat;
      background-size: 100% 100%;
      width: 0.72rem;
      height: 0.373rem;
      right: -1.6rem;
      top: 50%;
      transform: translate(-50%, -50%);
    }
    &::before {
      position: absolute;
      content: "";
      background: url(../../../../assets/title-l.png) left center no-repeat;
      background-size: 100% 100%;
      width: 0.72rem;
      height: 0.373rem;
      left: -0.8rem;
      top: 50%;
      transform: translate(-50%, -50%);
    }
  }
}
.head-invite {
  background: url(../../../../assets/invite-bg.png) #fff no-repeat;
  background-size: 100% 10.133rem;
  width: 100%;
  padding: 0 0.427rem;

  .invite-title {
    padding-top: 9.067rem;
  }
  .invite-progress {
    margin: 0.667rem auto;
    img {
      width: 7.6rem;
      height: 0.693rem;
      margin: 0 auto;
    }
  }
}
.qrcode-box {
  padding: 0.267rem 0 0.533rem;
  img {
    width: 2.533rem;
    height: 2.533rem;
    margin: 0 auto;
  }
}
</style>
