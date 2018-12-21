<template>
  <div class='page'>
    <div class="container">
      <!--header  -->
      <!--<x-header :left-options="{backText: ''}">发票索取</x-header>-->
      <div v-if="inlineDescList && inlineDescList.length > 0">
        <checklist title="订单列表" :options="inlineDescList" v-model="inlineDescListValue" @on-change="change"></checklist>
        <!-- 底部按钮 -->
        <section class="footer-bar">
          <flexbox>
            <flexbox-item >
              <checklist :options="selectAllList" v-model="selectAllValue" @on-change="checkAll" style="font-size:1px;"></checklist>
            </flexbox-item>
            <flexbox-item :span="4">
              <div class="flex1 tc  white footer-btn" >
                <span class="f16 pl0" @click="createInvoice">我开发票</span>
              </div>
            </flexbox-item>
          </flexbox>
        </section>
      </div>
      <div v-else>
        <img class="no_data" src="~@/assets/no_data.png" alt="">
        <p class="f18 tc">您还没有相关订单</p>
        <p class="f12 tc">去下一单试试吧~</p>
        <div class="mt30 tc">
          <x-button class="fp_btn f16 mb10 base_btn_border" plain mini type="primary" @click.native="nextPageSocial">去缴社保</x-button>
          <x-button class="fp_btn f16 mb10 base_btn_border" plain mini type="primary" @click.native="nextPageProvident">去缴公积金</x-button>
        </div>
      </div>

    </div>
  </div>


</template>
<script>
import {XHeader,Checklist,Flexbox,FlexboxItem, XButton, Toast, Confirm} from "vux";
import { get, post } from '@/api/server'
import url from '@/api/apiUrl'
import money from "@/utils/utils"
export default {
  name: "selectOrder",
  components: {XHeader,Checklist,Flexbox,FlexboxItem, XButton, Toast, Confirm},
  data() {
    return {
      inlineDescList: [

      ],
      inlineDescListValue: [],
      selectAllList: [
        {key: '1', value: ''}
      ],
      selectAllValue: [],
      amount:0
    };
  },
  mounted() {
//    this.checkboxData.forEach((item, index) => {
//      this.selectedData.push(item.id);
//      this.$set(this.checkBox.items, index, !this.checkBox.checked);
//    });
  },
  computed: {

  },
  created(){
      this.getOrders();
  },
  methods: {
    getOrders(){
      let params = {
          pageNumber:1,
          pageSize:100
      }
      get(url.invoiceOrderList,params).then(response => {
          console.log(JSON.stringify(response))
//        if(response.data.result == "00000000"){
          this.inlineDescList = response.data.list
//        }
      })
    },
    change (val, label) {
      console.log('change1', val[0])
      console.log('change2', label)
      this.calculateAmount();
    },
    checkAll (val, label) {

      if(val[0]){
        this.inlineDescListValue = [];
        this.inlineDescList.forEach((item, index) => {
          this.inlineDescListValue.push(item.key);
        })
      }else{
        this.inlineDescListValue = [];
      }
      this.calculateAmount();
      console.log(JSON.stringify(this.inlineDescListValue));
    },
    calculateAmount(){
        console.log("222");
      this.amount = 0;
      this.inlineDescList.map((item, index) => {
        this.inlineDescListValue.map((key, index) => {
          if(item.key == key){
              this.amount += parseFloat(item.inlineDesc);
          }
        })
      })
      this.selectAllList[0].value= "（金额：" + this.amount + "元）";

    },
    nextPageSocial() {
      this.$router.push({path:"/order/orderMember?type=SB"});
    },
    nextPageProvident() {
      this.$router.push({path:"/order/orderMember?type=GJ"});
    },
    createInvoice(){
      console.log("ids:" + JSON.stringify(this.inlineDescListValue));
      if(this.amount && this.amount > 0){
        const _this = this // 需要注意 onCancel 和 onConfirm 的 this 指向
        this.$vux.confirm.show({
          title: '提醒',
          content: '确定开发票？',
          onShow () {
            //console.log('plugin show')
          },
          onHide () {
            //console.log('plugin hide')
          },
          onCancel () {
            //console.log('plugin cancel')
          },
          onConfirm () {
            //console.log('plugin confirm')
            let ids = _this.inlineDescListValue.join()
            post(url.createInvoice, {orderIds:ids,amount:_this.amount}).then(response => {
                if(response.data.result == "00000000"){
                  _this.$vux.toast.show({
                    type:'success',
                    text: '提交成功!',
                    width:'120px',
                    time:1000
                  })
                  this.$router.push({path:"/member/invoiceservice/index"});
                }else{
                  _this.$vux.toast.show({
                    type:'warn',
                    text: response.data.data,
                    width:'120px',
                    time:1000
                  })
                }
            })
          }
        })
      }else{
        this.$vux.toast.text('必须选择开票订单且金额大于0', 'middle')
      }
    }
  }
};
</script>
<style scoped>
  .no_data{
    margin: 60px auto 30px;
  }
  .fp_btn{
    display: block;
    padding: 2px 0;
    width: 180px;
  }
  .base_btn_border{
    border-color: @color_base;
    color: @color_base;
  }
  .btn_round{
    border-radius: 20px;
  }
  .error {
    padding-left: 15px;
    line-height: 28px;
    color: #888;
    font-size: 12px;
  }
</style>
