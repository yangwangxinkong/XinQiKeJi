<template>

    <!-- 参保信息 -->

    <div class="page">
        <div class="container bg_f">

            <group title="停缴信息"  title-color="#333"  style="font-size: 16px;" :gutter="1">
                <!-- <popup-radio title="停缴月数" class="f14" :options="monthTypes" v-model="postForm.monthCount" @on-hide="monthHideFun(postForm.monthCount)"></popup-radio> -->
                <datetime title="停缴开始时间" class="f14"
                    format='YYYY-MM'
                    v-model="postForm.startDate"
                    begin="2015-10"
                    end="2015-12"
                    @on-change="change"
                    @on-cancel="log('cancel')"
                    @on-hide="log('hide', $event)">
                </datetime>

                <!-- <datetime title="停缴结束时间" class="f14"
                    format='YYYY-MM'
                    v-model="postForm.endDate">
                </datetime> -->
                <x-input title="停缴结束时间"  class="f14" text-align="right" v-model="postForm.endDate" v-bind:readonly="true"></x-input>
            </group>

            <!-- 底部按钮 -->
            <section class="footer-bar" >
                    <flexbox>
                        <flexbox-item>
                            <p class="amount-text">预计退款：
                            <span style="font-weight:bold;">{{amount | money}}</span>
                            元
                            </p>
                        </flexbox-item>
                        <flexbox-item :span="5">
                            <div class="flex1 tc  white footer-btn" @click="nextPage"><span class="f16 pl0">提交申请</span></div>
                        </flexbox-item>
                    </flexbox>
            </section>
        </div>
    </div>
</template>
<script>
import { PopupRadio, Datetime,Cell, Group,CheckIcon,Flexbox,FlexboxItem,Flow,FlowState, FlowLine,XInput } from 'vux'
import { get, post } from '@/api/server'
import url from '@/api/apiUrl'
import storage from "@/utils/common"
import money from "@/utils/utils"
import { validIsBlank } from '@/utils/validate'

const defaultForm = {
  sn: undefined,
  //monthCount: undefined, //缴费月数
  startDate: undefined, //开始日期
  endDate: undefined, //结束日期
};

export default {
  data(){
      return{
          monthTypes:[],
          monthType: 1,
          amount:'0',
          startDate: undefined,
          endDate: undefined,
          sn: this.$route.query && this.$route.query.sn,
          reCal: false,
          postForm: Object.assign({}, defaultForm)
      }
  },
  components: { PopupRadio, Datetime,Cell, Group,CheckIcon,Flexbox,FlexboxItem,Flow,FlowState, FlowLine,XInput },
  methods:{
      initData() {
          // 选择信息获取
        if(this.sn != null) {
            get(url.orderCancelApply, {sn: this.sn}).then(response => {
                console.log(response.data.data);
                if(response.data.result == '00000000') {

                    // 开始时间
                    this.postForm.startDate = response.data.data.startDate;
                    this.startDate = response.data.data.startDate;
                    // 结束时间
                    this.postForm.endDate = response.data.data.endDate;
                    this.endDate = response.data.data.endDate;
                    this.postForm.monthCount = response.data.data.monthCount;                   
                    this.amount = response.data.data.amount;
                    this.monthTypes = response.data.data.monthTypes;
                    this.reCal = true;
                }
            }).catch( e => {
                console.log(e);
            });
        }
      },
      hideFun(){
        // 重新计算报价金额
        this.doCalculator();
      },
      monthHideFun(intMonth){
        if(this.postForm.startDate != null && this.postForm.startDate != undefined
        && this.postForm.startDate != ""
        && intMonth != null && intMonth != undefined && intMonth != "") {
            this.postForm.endDate = this.getEndYM(this.postForm.startDate, intMonth);
        }

        // 重新计算报价金额
        this.doCalculator();
      },
      change(value){
        // if(value != null && value != undefined && value != ""
        // && this.postForm.monthCount != null && this.postForm.monthCount != undefined
        // && this.postForm.monthCount != "") {
        //     this.postForm.endDate = this.getEndYM(value, this.postForm.monthCount);
        // }

        // 重新计算报价金额
        this.doCalculator();
      },

      log(str1, str2) {
          console.log(str1, str2)
      },
      title(){
      },
      getCurrentYM() {
          var date = new Date();
          var seperator1 = "-";
          var year = date.getFullYear();
          var month = date.getMonth() + 1;
          //var strDate = date.getDate();
          if (month >= 1 && month <= 9) {
            month = "0" + month;
          }
        //   if (strDate >= 0 && strDate <= 9) {
        //     strDate = "0" + strDate;
        //   }
          var currentdate = year + seperator1 + month;
          return currentdate;
      },

      getEndYM(startYM, intMonth) {
          var date = new Date(startYM + "-01");
          date.setMonth(date.getMonth() + (intMonth - 1));

          var seperator1 = "-";
          var year = date.getFullYear();
          var month = date.getMonth() + 1;

          if (month >= 1 && month <= 9) {
            month = "0" + month;
          }
        //   if (strDate >= 0 && strDate <= 9) {
        //     strDate = "0" + strDate;
        //   }
          var currentdate = year + seperator1 + month;
          return currentdate;
      },

      nextPage(){
        this.postForm.payCategory = "pc"+this.insuredType;
        this.postForm.id = this.quotationId;

        // 开始日期输入项校验
        if(this.postForm.startDate === null || this.postForm.startDate === undefined || validIsBlank(this.postForm.startDate)){
            this.$vux.toast.text('请选择开始日期', 'middle')
            return false;
        }

        if(this.postForm.startDate > this.endDate || this.postForm.startDate < this.startDate){
            this.$vux.toast.text('停缴开始日期超出购买期间。', 'middle')
            return false;
        }

        // 缴费月数输入项校验
        // if(validIsBlank(this.postForm.monthCount)){
        //     this.$vux.toast.text('请输入代缴月数', 'middle')
        //     return false;
        // }
        let _this = this;

        this.$vux.confirm.show({
        title: "提示",
        content: "你确定要提交停缴申请",
        // onShow() {
        //   console.log("plugin show");
        // },
        // onHide() {
        //   console.log("plugin hide");
        // },
        onCancel() {
          //console.log("plugin cancel");
        },
        onConfirm() {

        post(url.refundSave, {sn: _this.sn, startDate: _this.postForm.startDate}).then(response => {
            if(response.data.result == '00000000') {
                _this.amount = response.data.data;
                //localStorage.setItem("quotationId", response.data.data);
                _this.$router.push({path:"/member/order/index"});
            }
        }).catch((e)=>{
            console.log(e)
        })
        }
      });
      },

      doCalculator(){
          if(this.reCal) {
              this.reCal = false;
              return;
          }
        this.postForm.sn = this.sn;

        // 开始日期输入项校验
        if(this.postForm.startDate === null || this.postForm.startDate === undefined || validIsBlank(this.postForm.startDate)){
            return false;
        }

        if(this.postForm.startDate > this.endDate || this.postForm.startDate < this.startDate){
            this.$vux.toast.text('停缴开始日期超出购买期间。', 'middle')
            return false;
        }

        // 缴费月数输入项校验
        // if(validIsBlank(this.postForm.monthCount)){
        //     return false;
        // }

        // let params = {
        //     sn: this.postForm.sn,
        //     payCategoryId: this.insuredType,
        //     //monthCount: this.postForm.monthCount,
        //     socialBase: this.postForm.socialBase,
        //     providentBase: this.postForm.providentBase
        // }

        get(url.refundAmount, this.postForm).then(response => {
            if(response.data.result == '00000000') {
                this.amount = response.data.data.amount;
            }
        }).catch((e)=>{
            console.log(e)
        })
      }
  },
  created() {
    this.initData();
  }
}
</script>
<style lang="less">
.hint-info .vux-label-desc{
    color:#CC3300;
}
.agree-info>span{
    color: #A9A9A9 !important;
}
.cell-text .weui-cell__ft {
    color: @amount_color;
}
.weui-icon-circle,.weui-icon-success{
    font-size: 18px !important;
}
label.weui-label {
    display: block;
    width: 160px;
    word-wrap: break-word;
    word-break: break-all;
}
</style>


