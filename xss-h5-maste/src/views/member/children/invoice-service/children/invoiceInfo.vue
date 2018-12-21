<!--资料管理  -->
<template>
  <div class='page'>
    <div class="container" style="padding-bottom:0px;">

      <group :gutter="0" label-width="5em">
        <popup-radio title="开具类型"  class="f14" :options="invoiceTypes" v-model="postForm.invoiceType"></popup-radio>
        <popup-radio title="发票类型"  class="f14" :options="invoiceCategories" v-model="postForm.invoiceCategory"></popup-radio>
        <x-input title="发票抬头" class="f14" placeholder-align="right" text-align="right" placeholder="请输入发票抬头" v-model="postForm.invoiceTitle"></x-input>
      </group>
      <group :gutter="10" label-width="5em">
        <x-input title="收货人" class="f14" placeholder-align="right" text-align="right" placeholder="请输入收货人" v-model="postForm.consignee"></x-input>
        <x-input title="联系电话" class="f14" placeholder-align="right" text-align="right" placeholder="请输入联系电话" v-model="postForm.phone"></x-input>
        <x-input title="详细地址" class="f14" placeholder-align="right" text-align="right" placeholder="请输入详细地址" v-model="postForm.address"></x-input>
        <x-input title="邮政编码" class="f14" placeholder-align="right" text-align="right" placeholder="请输入邮政编码" v-model="postForm.zipCode"></x-input>
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
  import { validIsBlank, validMobile, validZipCode } from '@/utils/validate'
  import url from '@/api/apiUrl'
  import {get, post, execute} from '@/api/server'

  const defaultForm = {
    id:undefined,
    invoiceType: 'it1',
    invoiceCategory: 'it0',
    invoiceTitle:'',//发票抬头
    consignee: '',//收货人
    phone:'',//联系电话
    address: '',//详细地址
    zipCode:''//邮政编码
  };
  export default {
    components:{ Group,Cell,XInput,PopupRadio},
    data () {
      return {
        invoiceTypes:[{key:'it0',value:'企业'},{key:'it1',value:'个人'}],
        invoiceCategories:[{key:'it0',value:'增值税普通发票'}],
        postForm: Object.assign({}, defaultForm),
      };
    },

    computed: {},

    mounted(){},

    created(){
      this.getInvoiceInfo();
    },
    methods: {
      getInvoiceInfo(){
        get(url.invoiceInfo, {}).then(response => {
          if(response.data.result == "00000000"){
            let invoice = response.data.data;
            //console.log(JSON.stringify(invoice))
            if(invoice) {
              this.postForm.id = invoice.id;
              this.postForm.invoiceTitle = invoice.invoiceTitle;
              this.postForm.consignee = invoice.consignee;
              this.postForm.phone = invoice.phone;
              this.postForm.address = invoice.address;
              this.postForm.zipCode = invoice.zipCode;
              if (invoice.invoiceType) {
                this.postForm.invoiceType = invoice.invoiceType;
              }
              if (invoice.invoiceCategory) {
                this.postForm.invoiceCategory = invoice.invoiceCategory;
              }
            }
          }
        });
      },
      saveInfo(){
        // 发票内容
        if(validIsBlank(this.postForm.invoiceType)){
          this.$vux.toast.text('请选择开具类型', 'middle')
          return false;
        }
        if(validIsBlank(this.postForm.invoiceCategory)){
          this.$vux.toast.text('请选择发票类型', 'middle')
          return false;
        }
        if(validIsBlank(this.postForm.invoiceTitle)){
          this.$vux.toast.text('请输入发票抬头', 'middle')
          return false;
        }
        if(validIsBlank(this.postForm.consignee)){
          this.$vux.toast.text('请输入收货人', 'middle')
          return false;
        }
        if(!validMobile(this.postForm.phone)){
          this.$vux.toast.text('请输入正确的手机号码', 'middle')
          return false;
        }
        if(validIsBlank(this.postForm.address)){
          this.$vux.toast.text('请输入详细地址', 'middle')
          return false;
        }
        if(!validZipCode(this.postForm.zipCode)){
          this.$vux.toast.text('请输入正确的邮政编码', 'middle')
          return false;
        }
        execute(url.saveInvoiceInfo, this.postForm).then(response => {
          if(response.data.result == "00000000"){

            this.$vux.toast.show({
              type:'success',
              text: "保存成功",
              width:'120px',
              time:1000
            })
          }
        });
      }
    }
  }

</script>
<style lang='less'>
</style>
