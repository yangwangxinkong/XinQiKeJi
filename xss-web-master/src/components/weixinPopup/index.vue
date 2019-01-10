<template>
    <div class="popUpblack" v-show="popUpshow">
        <div class="popUp" style="margin-top: -221px;">
            <div class="t f14">官方微信公众号<span class="close" @click="closeClick"><i class="icon-close">X</i></span></div>
            <div class="weixin-content">请关注官网微信公众号“小豆社保”，在微信公众号完成下单流程！</div>
            <div class="img">
                <img :src="qrcode">
            </div>
            <div class="weixin-info">打开手机微信扫描完成关注</div>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">
import eventVue from '../../modules/js/eventBus.js'
export default {
  components: {},
  props: {
  },
  data() {
    return {
      popUpshow:false,
      qrcode:require('../../assets/images/qrcode.jpg')
    };
  },
  watch: {
  },
  computed: {},
  methods: {
      closeClick(){
        this.popUpshow=false
      }
  },
  created() {
  },
  mounted() {
    eventVue.$on("PopShowTap",(message)=>{
    console.log("message："+message)
        this.popUpshow = message
    })
  }
};
</script>

<style  lang="less">
/* winxin */
.popUpblack {
	background: url(../../assets/images/back-bg-opacity.png) repeat;
	width: 100%;
	height: 100%;
	position: fixed;
	left: 0px;
	top: 0px;
	z-index: 1010;
}

.popUp {
	width: 520px;
	height: 442px;
	border: 5px solid #f1f1f1;
	background: #fff;
	position: fixed;
	left: 50%;
	top: 50%;
	margin: -200px 0 0 -255px;
	z-index: 1004;
}

.popUp .t {
	padding: 0 0 0 10px;
	height: 36px;
	line-height: 36px;
	color: #666;
	font-weight: bold;
	border-bottom: 1px solid #e8e8e8;
	background: #f2f2f2;
}
.popUp .weixin-content{
        padding: 20px 20px;
    border-bottom: 1px solid #f5f5f5;
    text-indent: 20px;
    max-height: 100px;
    overflow: hidden;
}
.popUp .t .close {
	width: 30px;
	height: 30px;
	border-radius: 50%;
	background:#8CC4A5;
	float: right;
	cursor: pointer;
	color: #666;
	font-weight: normal;
}

.popUp .t .close .icon-close{
	display: block;
    color: #fff;
    width: 13px;
    height: 13px;
    vertical-align: middle;
    transition: 300ms;
    -webkit-transition: 300ms;
    transition: 400ms;
    -webkit-transition: 400ms;
    text-align: center;
    margin: 8px auto;
    background: url(../../assets/images/close_ico2.png)no-repeat;
    text-indent: -999999px;
    background-size: cover;
    overflow:hidden;
}
.popUp .t .close:hover .icon-close{
	transform: rotate(180deg);
    -webkit-transform: rotate(180deg);
}
.popUp .img {
	    padding: 20px 150px;
    text-align: center;
    vertical-align: middle;
}

.popUp .img img {
	width: 100%;
	height: 100%;
	display: block;
	margin: 0 auto;
}
.popUp .weixin-info{
	text-align: center;
	color: #0f87dd;
}
/* winxin end */

</style>


