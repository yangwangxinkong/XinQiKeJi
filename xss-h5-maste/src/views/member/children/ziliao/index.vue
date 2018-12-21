<!--资料管理  -->
<template>
  <div class='page'>
      <div class="container">
        <group title="基本信息" :gutter="0" class="mb10 mt10 bg_f" >
            <cell title="头像" class="f14">
              <cropper :avator="postForm.defaultImage"  @getHeaderImage="newHeaderImage"></cropper>
            </cell>
            <x-input title="姓名" class="f14" placeholder-align="right" text-align="right" placeholder="请输入姓名" v-model="postForm.name"></x-input>
            <x-input title="身份证号"  class="f14"  placeholder-align="right" text-align="right" placeholder="请输入身份证号" v-model="postForm.identification"></x-input>
            <popup-radio title="性别" class="f14"  :options="genders" v-model="postForm.gender"></popup-radio>
            <!--<popup-radio title="行业" placeholder="请选择行业" :options="monthIndustrys" v-model="monthIndustry"></popup-radio>-->
        </group>
        <group :gutter="0" title="优惠信息"  class="f14 mb10 bg_f" >
          <cell title="是否新手" class="f14"  value="是" v-if="isNew"></cell>
          <cell title="是否新手" class="f14"  value="否" v-else></cell>
          <cell title="是否会员" class="f14"  value="是" v-if="isVip"></cell>
          <cell title="是否会员" class="f14"  value="否" v-else></cell>
          <cell title="会员有效期至" class="f14"  placeholder-align="right" text-align="right" :value="vipExpireDate" v-if="isVip"></cell>
          <cell title="优惠折扣" class="f14" placeholder-align="right" text-align="right" :value="vipDiscount" v-if="isVip"></cell>
          <!--<cell title="定点医院" value="" is-link></cell>-->
        </group>
        <group :gutter="0" title="参保资料"  class="f14">
            <cell title="参保资料" class="f14"  value="上传资料" link="/pre_insurance/index"></cell>
            <!--<cell title="定点医院" value="" is-link></cell>-->
        </group>
        <!-- footer -->
        <section class="footer-actionBar" >
          <section class="footer-actionBar-container flex-ui ">
            <div class="flex1 tc  white" @click="saveInfo"><span class="f16 pl0">保存</span></div>
          </section>
        </section>
      </div>
  </div>
</template>

<script>
import { Group,Cell,XInput,PopupRadio  } from 'vux'
import cropper from '@/components/cropper/index'
import { validIsBlank, validIdNum } from '@/utils/validate'
import storage from "@/utils/common"
import url from '@/api/apiUrl'
import {get, post, execute} from '@/api/server'

const defaultForm = {
  id:undefined,
  name:'',
  identification:'',
  gender:'male',
  headImage:undefined,
  defaultImage:require('../../../../assets/touxiang.png')
};
export default {
  components:{ Group,Cell,XInput,PopupRadio,cropper},
  data () {
    return {
      genders:[{key:'male',value:'男'},{key:'female',value:'女'}],
      postForm: Object.assign({}, defaultForm),
      isNew:true,//是否新手
      isVip:false,//是否会员
      vipExpireDate:undefined,//会员过期日期
      vipDiscount:undefined,//会员优惠折扣
    };
  },

  computed: {},

  mounted(){},

  created(){
      this.getMemberInfo();
  },
  methods: {
      getMemberInfo(){
        get(url.current, {}).then(response => {
          if(response.data.result == "00000000"){
            let memberInfo = response.data.data;
            //console.log(JSON.stringify(memberInfo))
            if(memberInfo){
              this.postForm.id = memberInfo.id;
              this.postForm.name = memberInfo.name;
              this.postForm.identification = memberInfo.identification;
              console.log("gender:" + memberInfo.gender)
              if(memberInfo.gender){
                this.postForm.gender = memberInfo.gender;
              }
//              if(memberInfo.headImage){
//                this.postForm.headImage = memberInfo.headImage;
//                this.postForm.defaultImage = memberInfo.headImage;
//              }
              if(memberInfo.isNew){
                this.isNew = memberInfo.isNew;
              }

              if(memberInfo.isVip){
                  this.isVip = memberInfo.isVip;
                  this.vipExpireDate = memberInfo.vipExpireDate;
                  this.vipDiscount = memberInfo.vipDiscount;
              }
            }
          }
        });
        let memberInfo = storage.get("member");
        if(memberInfo){
//          this.postForm.id = memberInfo.id;
//          this.postForm.name = memberInfo.name;
//          this.postForm.identification = memberInfo.identification;
//          if(memberInfo.gender){
//            this.postForm.gender = memberInfo.gender;
//          }
          if(memberInfo.headImage){
              this.postForm.headImage = memberInfo.headImage;
              this.postForm.defaultImage = memberInfo.headImage;
          }
        }
      },
      saveInfo(){
        // 参保人姓名输入项校验
        if(validIsBlank(this.postForm.name)){
          this.$vux.toast.text('请输入参保人姓名', 'middle')
          return false;
        }

        // 身份证号输入项校验
        if(!validIdNum(this.postForm.identification)){
          this.$vux.toast.text('请输入正确的身份证号', 'middle')
          return false;
        }
        execute(url.updateMember, this.postForm).then(response => {
          if(response.data.result == "00000000"){
            let memberInfo = storage.get("member");
            memberInfo.name = this.postForm.name;
            memberInfo.identification = this.postForm.identification;
            memberInfo.gender = this.postForm.gender;
            memberInfo.headImage = this.postForm.headImage;
            storage.set("member",memberInfo);
            //console.log(JSON.stringify(storage.get("member")))
            this.$vux.toast.show({
              type:'success',
              text: "保存成功",
              width:'120px',
              time:1000
            })
          }
        });
      },
      newHeaderImage(newImg){
        this.postForm.headImage = newImg;
        this.postForm.defaultImage = newImg;
        //console.log("headerImg:"+this.postForm.headImage);
      }
  }
}

</script>
<style lang='less'>
</style>
<style>
  * {
    margin: 0;

    padding: 0;
  }

  #demo #button {
    position: absolute;

    right: 10px;

    top: 10px;

    width: 80px;

    height: 40px;

    border: none;

    border-radius: 5px;

    background: white;
  }

  #demo .show {
    width: 60px;

    height: 60px;

    overflow: hidden;

    position: relative;

    border-radius: 50%;

    border: 1px solid #d5d5d5;
  }

  #demo .picture {
    width: 100%;

    height: 100%;

    overflow: hidden;

    background-position: center center;

    background-repeat: no-repeat;

    background-size: cover;
  }

  .file-box {
    width: 60px;
    height: 60px;
    position: absolute;
    top:0;
    right:0;
    left: 0;
    bottom:0;
    opacity: 0;
  }
  #demo .container {
    z-index: 99;

    position: fixed;

    padding-top: 60px;

    left: 0;

    top: 0;

    right: 0;

    bottom: 0;

    background: rgba(0, 0, 0, 1);
  }

  #demo #image {
    max-width: 100%;
  }

  .cropper-view-box,
  .cropper-face {
    border-radius: 50%;
  }

  .cropper-container {
    font-size: 0;

    line-height: 0;

    position: relative;

    -webkit-user-select: none;

    -moz-user-select: none;

    -ms-user-select: none;

    user-select: none;

    direction: ltr;

    -ms-touch-action: none;

    touch-action: none;
  }

  .cropper-container img {
    /* Avoid margin top issue (Occur only when margin-top <= -height) */

    display: block;

    min-width: 0 !important;

    max-width: none !important;

    min-height: 0 !important;

    max-height: none !important;

    width: 100%;

    height: 100%;

    image-orientation: 0deg;
  }

  .cropper-wrap-box,
  .cropper-canvas,
  .cropper-drag-box,
  .cropper-crop-box,
  .cropper-modal {
    position: absolute;

    top: 0;

    right: 0;

    bottom: 0;

    left: 0;
  }

  .cropper-wrap-box {
    overflow: hidden;
  }

  .cropper-drag-box {
    opacity: 0;

    background-color: #fff;
  }

  .cropper-modal {
    opacity: 0.5;

    background-color: #000;
  }

  .cropper-view-box {
    display: block;

    overflow: hidden;

    width: 100%;

    height: 100%;

    outline: 1px solid #39f;

    outline-color: rgba(51, 153, 255, 0.75);
  }

  .cropper-dashed {
    position: absolute;

    display: block;

    opacity: 0.5;

    border: 0 dashed #eee;
  }

  .cropper-dashed.dashed-h {
    top: 33.33333%;

    left: 0;

    width: 100%;

    height: 33.33333%;

    border-top-width: 1px;

    border-bottom-width: 1px;
  }

  .cropper-dashed.dashed-v {
    top: 0;

    left: 33.33333%;

    width: 33.33333%;

    height: 100%;

    border-right-width: 1px;

    border-left-width: 1px;
  }

  .cropper-center {
    position: absolute;

    top: 50%;

    left: 50%;

    display: block;

    width: 0;

    height: 0;

    opacity: 0.75;
  }

  .cropper-center:before,
  .cropper-center:after {
    position: absolute;

    display: block;

    content: " ";

    background-color: #eee;
  }

  .cropper-center:before {
    top: 0;

    left: -3px;

    width: 7px;

    height: 1px;
  }

  .cropper-center:after {
    top: -3px;

    left: 0;

    width: 1px;

    height: 7px;
  }

  .cropper-face,
  .cropper-line,
  .cropper-point {
    position: absolute;

    display: block;

    width: 100%;

    height: 100%;

    opacity: 0.1;
  }

  .cropper-face {
    top: 0;

    left: 0;

    background-color: #fff;
  }

  .cropper-line {
    background-color: #39f;
  }

  .cropper-line.line-e {
    top: 0;

    right: -3px;

    width: 5px;

    cursor: e-resize;
  }

  .cropper-line.line-n {
    top: -3px;

    left: 0;

    height: 5px;

    cursor: n-resize;
  }

  .cropper-line.line-w {
    top: 0;

    left: -3px;

    width: 5px;

    cursor: w-resize;
  }

  .cropper-line.line-s {
    bottom: -3px;

    left: 0;

    height: 5px;

    cursor: s-resize;
  }

  .cropper-point {
    width: 5px;

    height: 5px;

    opacity: 0.75;

    background-color: #39f;
  }

  .cropper-point.point-e {
    top: 50%;

    right: -3px;

    margin-top: -3px;

    cursor: e-resize;
  }

  .cropper-point.point-n {
    top: -3px;

    left: 50%;

    margin-left: -3px;

    cursor: n-resize;
  }

  .cropper-point.point-w {
    top: 50%;

    left: -3px;

    margin-top: -3px;

    cursor: w-resize;
  }

  .cropper-point.point-s {
    bottom: -3px;

    left: 50%;

    margin-left: -3px;

    cursor: s-resize;
  }

  .cropper-point.point-ne {
    top: -3px;

    right: -3px;

    cursor: ne-resize;
  }

  .cropper-point.point-nw {
    top: -3px;

    left: -3px;

    cursor: nw-resize;
  }

  .cropper-point.point-sw {
    bottom: -3px;

    left: -3px;

    cursor: sw-resize;
  }

  .cropper-point.point-se {
    right: -3px;

    bottom: -3px;

    width: 20px;

    height: 20px;

    cursor: se-resize;

    opacity: 1;
  }

  @media (min-width: 768px) {
    .cropper-point.point-se {
      width: 15px;

      height: 15px;
    }
  }

  @media (min-width: 992px) {
    .cropper-point.point-se {
      width: 10px;

      height: 10px;
    }
  }

  @media (min-width: 1200px) {
    .cropper-point.point-se {
      width: 5px;

      height: 5px;

      opacity: 0.75;
    }
  }

  .cropper-point.point-se:before {
    position: absolute;

    right: -50%;

    bottom: -50%;

    display: block;

    width: 200%;

    height: 200%;

    content: " ";

    opacity: 0;

    background-color: #39f;
  }

  .cropper-invisible {
    opacity: 0;
  }

  .cropper-bg {
    background-image: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAAA3NCSVQICAjb4U/gAAAABlBMVEXMzMz////TjRV2AAAACXBIWXMAAArrAAAK6wGCiw1aAAAAHHRFWHRTb2Z0d2FyZQBBZG9iZSBGaXJld29ya3MgQ1M26LyyjAAAABFJREFUCJlj+M/AgBVhF/0PAH6/D/HkDxOGAAAAAElFTkSuQmCC");
  }

  .cropper-hide {
    position: absolute;

    display: block;

    width: 0;

    height: 0;
  }

  .cropper-hidden {
    display: none !important;
  }

  .cropper-move {
    cursor: move;
  }

  .cropper-crop {
    cursor: crosshair;
  }

  .cropper-disabled .cropper-drag-box,
  .cropper-disabled .cropper-face,
  .cropper-disabled .cropper-line,
  .cropper-disabled .cropper-point {
    cursor: not-allowed;
  }
</style>
