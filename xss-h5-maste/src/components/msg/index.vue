<template>
  <div class="weui-msg">
    <div class="weui-msg__icon-area">
      <img class="weui-icon_msg" :class="`weui-icon-${icon || 'success'}`" :src="icon" />
    </div>
    <div class="weui-msg__text-area">
      <h2 class="weui-msg__title" v-html="title"></h2>
      <p class="weui-msg__desc">
        <slot name="description"></slot>
      </p>
      <p class="weui-msg__desc" v-if="description" v-html="description"></p>
    </div>
    <div class="weui-msg__opr-area">
      <p class="weui-btn-area">
        <slot name="buttons">
          <!-- <a v-for="button in buttons" :key="button" href="javascript:;" class="weui-btn" :class="`weui-btn_${button.type}`" @click="onClick(button.onClick, button.link)">{{$t(button.text)}}</a> -->
          <x-button  v-for="(button,index) in buttons" :key="index" :plain="button.plain" :type="button.type" :link="button.link" @click.native="onClick(button.onClick, button.link)">{{button.text}}</x-button>
        </slot>
      </p>
    </div>
  </div>
</template>

<script>
import { XButton} from "vux";
import { go } from 'vux/src/libs/router'
export default {
  name: "msg",
  props: ["icon", "title", "description", "buttons"],
  components:{XButton},
  methods: {
    onClick(handler, link) {
      typeof handler === "function" && handler();
      link && go(link, this.$router);
    }
  }
};
</script>

<style lang="less">
@import "~vux/src/styles/weui/icon/weui_icon_font.less";
@import "~vux/src/styles/weui/widget/weui_page/weui_msg.less";
@import "~vux/src/styles/weui/widget/weui-button/weui-button.less";
</style>