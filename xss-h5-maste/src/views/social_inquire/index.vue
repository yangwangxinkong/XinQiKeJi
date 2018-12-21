<!--  社保查询-->
<template>
  <div class="page">
    <div class="container">
      <div class="model-inquire-wrap">
        <group :gutter="10" v-for="(item,index) in inquireList" :key="index">
          <cell class="value_color f16 fw" :title="item.title"></cell>
          <cell-box>
            <div style="width:100%" class="flex-ui align-items-center">
              <span class="f14 flex1 gray_base ellipsis">{{item.url}}</span>
              <div class="fr">
                <template>
                  <x-button
                    class="mt0 mr0 ml5 base_btn_border"
                    mini
                    plain
                    v-clipboard:copy="item.url"
                    v-clipboard:success="onCopy"
                    v-clipboard:error="onError"
                  >复制</x-button>
                  <x-button class="mt0 mr0 ml5 btn_round" mini type="warn" :link="item.url">访问</x-button>
                </template>
              </div>
            </div>
          </cell-box>
        </group>
        <!-- 提示 -->
        <group class="bg_f" title="温馨提示" :gutter="0">
          <div class="weui-cells__tip padding-15 pb10">
            <div class="tip-info gray_base f12 mt10" style="color: #81839F;">
              <p>1、点击访问可在手机端直接打开当前网址查询；</p>
              <p class="mt10">2、点击复制可复制网址信息，方便您分享或在其他浏览器打开。</p>
            </div>
          </div>
        </group>
      </div>
    </div>
  </div>
</template>

<script>
import { Group, Cell, CellBox, Flexbox, FlexboxItem, XButton } from "vux";
export default {
  data() {
    return {
      inquireList: [
        {
          title: "小豆社保官网",
          url: "http://www.xiaodoushebao.com"
        },
        {
          title: "全国社保官网",
          url: "http://www.mohrss.gov.cn"
        },
        {
          title: "北京社保官网",
          url: "http://www.bjrbj.gov.cn/csibiz/indinfo/login.jsp"
        }
      ]
    };
  },

  components: { Group, Cell, CellBox, Flexbox, FlexboxItem, XButton },

  computed: {},

  mounted() {},

  methods: {
    handleCopy(item, index) {},
    onCopy: function(e) {
      this.$vux.toast.text("复制成功！", "bottom");
    },
    onError: function(e) {
      this.$vux.toast.text("复制失败！", "bottom");
    }
  }
};
</script>
<style lang='less' >
.model-inquire-wrap {
  .fw {
    font-weight: 550;
  }
  .vux-label {
    font-weight: 550;
    padding-left: 10px;
    position: relative;
    &::before {
      content: "";
      position: absolute;
      width: .267rem;
      height: .72rem;
      background-color: @color_base;
      border-radius: 2px;
      left: 0;
      top: 50%;
      transform: translate(-50%, -50%);
    }
  }
}
</style>
