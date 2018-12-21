<template>
  <div class="page">
    <div class="container  pb0">
      <div class="address-edit f14">
        <group gutter="0px" class="bg_f">
          <x-input title="姓名" label-width="105" name="name" placeholder="请输入姓名" v-model="name" placeholder-align="right" text-align="right"></x-input>
          <x-input title="手机号码" label-width="105" name="mobile" placeholder="请输入手机号码" ref="refmobile" v-model="mobile" keyboard="number" is-type="china-mobile" placeholder-align="right" text-align="right"></x-input>
          <x-address @on-hide="logHide" @on-show="logShow" title="" v-model="value" :list="addressData" @on-shadow-change="onShadowChange" placeholder="请选择地址" inline-desc="请选择省份" :show.sync="showAddress"></x-address>
          <x-input title="详细地址" label-width="105" name="address_detail" v-model="addressDetail" placeholder="请输入详细地址" placeholder-align="right" text-align="right"></x-input>
          <x-input title="邮政编码" label-width="105" name="zipCode" v-model="zipCode" placeholder="请输入邮政编码" keyboard="number" placeholder-align="right" text-align="right"></x-input>
        </group>
        <group class="bg_f" >
          <x-switch title="设为默认收货地址" :value-map="['0', '1']" v-model="stringValue" @on-change="changeSwitch"></x-switch>
        </group>

        <!-- <div style="padding:20px;" v-if="id!=undefined&& id!=null">
          <x-button @click.native="deleteAddress" type="warn" style="border-radius:99px;width:200px;margin:0 auto;height:35px;">删除</x-button>
        </div> -->
        <section class="footer-actionBar">
          <section class="footer-actionBar-container flex-ui">
            <div class="flex1 tc white" @click="saveAddress()">
              <span class="f16 pl0">提交</span>
            </div>
          </section>
        </section>
      </div>

    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { XInput, XButton, Group, XAddress, XSwitch } from "vux";
import url from "@/api/apiUrl";
import { get, post } from "@/api/server";
import { validIsBlank } from "@/utils/validate";
//import address from '../../../static/json/address2.json'
//import address from '../../../static/json/address.json'
// import Address from '@/api/addressService'

// let address;
// Address.arealist().then((res)=>{
//   address = res.data.list;
// })

const defaultForm = {
  id: null,
  consignee: "", //用户名
  phone: "", //手机号
  address: "", //详细地址
  zipCode: "", //邮政编码
  isDefault: 0, //设置默认地址
  //area:{id:undefined},//["210000", "210100", "210102"]
  areaId: undefined
};

export default {
  components: { XInput, XButton, Group, XAddress, XSwitch },
  props: {},
  data() {
    return {
      id: this.$route.query && this.$route.query.id,
      cartItemIds: this.$route.query && this.$route.query.cartItemIds,
      name: "", //用户名
      mobile: "", //手机号
      addressDetail: "", //详细地址
      zipCode: "", //邮政编码
      stringValue: 0, //设置默认地址
      value: [], //["210000", "210100", "210102"]
      addressData: [],
      showAddress: false,
      postForm: Object.assign({}, defaultForm)
    };
  },
  watch: {},
  computed: {},
  methods: {
    doShowAddress() {
      this.showAddress = true;
      setTimeout(() => {
        this.showAddress = false;
      }, 2000);
    },
    //地址改变事件
    onShadowChange(ids, names) {
      console.log(ids, names);
    },
    logHide(str) {
      console.log("on-hide", str);
    },
    logShow(str) {
      console.log("on-show");
    },
    // 获取area列表
    getAreaList() {
      get(url.areaList, {}).then(response => {
        this.addressData = response.data.list;
        // console.log("this.navList=" + response.data.data)
      });
    },
    // 获取地址信息
    getAddressInfo() {
      get(url.addressInfo, { id: this.id }).then(response => {
        let addressInfo = response.data.data;
        this.name = addressInfo.consignee;
        this.mobile = addressInfo.phone;
        this.addressDetail = addressInfo.address;
        this.zipCode = addressInfo.zipCode;
        this.stringValue = addressInfo.isDefault ? "1" : "0";

        //treePath
        let treePath = addressInfo.treePath;
        if (treePath != null && treePath != undefined && treePath != "") {
          this.value = (treePath + addressInfo.areaId).split(",");
        }

        // console.log("this.navList=" + response.data.data)
      });
    },

    //新增地址
    saveAddress() {
      // 输入项校验
      if (validIsBlank(this.name)) {
        this.$vux.toast.text("请输入姓名", "middle");
        return false;
      }
      if (validIsBlank(this.mobile)) {
        this.$vux.toast.text("请输入手机号码", "middle");
        return false;
      } else if (!this.$refs.refmobile.valid) {
        this.$vux.toast.text("请输入手机号码", "middle");
        return false;
        // var re= /^((0\d{2,3}-\d{7,8})|(1[3,5,7,8][0-9]\d{8}))$/;
        // if(!re.test(this.mobile)) {
        //   this.$vux.toast.text('手机号码格式不对', 'bottom')
        //   return false;
        // }
      }
      if (this.value == null || this.value.length == 0) {
        this.$vux.toast.text("请选择省市区", "bottom");
        return false;
      }
      if (validIsBlank(this.zipCode)) {
        this.$vux.toast.text("请输入邮政编码", "bottom");
        return false;
      } else {
        var re = /^[0-9][0-9]{5}$/;
        if (!re.test(this.zipCode)) {
          this.$vux.toast.text("邮政编码格式不对", "bottom");
          return false;
        }
      }
      if (validIsBlank(this.addressDetail)) {
        this.$vux.toast.text("请输入详细地址", "bottom");
        return false;
      }

      this.postForm.id = this.id;
      this.postForm.consignee = this.name;
      this.postForm.phone = this.mobile;
      this.postForm.address = this.addressDetail;
      this.postForm.zipCode = this.zipCode;
      this.postForm.isDefault = this.stringValue;
      //console.log(this.value[this.value.length - 1]);
      //this.postForm.area.id = parseInt(this.value[this.value.length - 1]);
      this.postForm.areaId = parseInt(this.value[this.value.length - 1]);

      // 提交信息
      post(url.addressSave, this.postForm)
        .then(res => {
          if (res.data.result === "00000000") {
            this.$vux.toast.show({
              type: "success",
              text: "提交成功!",
              width: "120px",
              time: 1000
            });
            this.$router.push({
              path: "/address/list",
              query: { cartItemIds: this.cartItemIds }
            });
          } else if (res.data.result == "10000007") {
            this.$router.push({
              path: "/login",
              query: { redirect: this.$route.fullPath }
            });
          } else {
            this.$vux.toast.show({
              type: "warn",
              text: res.data.msg,
              width: "120px",
              time: 1000
            });
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    //删除方法
    deleteAddress() {},
    //switch开关
    changeSwitch(value) {
      this.stringValue = value;
      console.log(this.stringValue);
      // if(this.stringValue==true){

      // }
    }
  },
  created() {
    //addressData = address;
    this.getAreaList();
    if (this.id != null) {
      this.getAddressInfo();
    }
  },
  mounted() {}
};
</script>
<style  lang="less">
@import "../../assets/styles/less/public.less";
.address-edit {
  .vux-popup-picker-placeholder {
    color: #81839f;
    font-size: 15px;
  }
  .vux-label-desc {
    .gray_base;
  }
  .weui-cell__ft {
    &::after {
      margin-top: -2px !important;
    }
  }
  .weui-label,
  .weui-input {
    .f14;
    .gray_base;
    &::-webkit-input-placeholder {
      color: #81839f;
    }
    &::-moz-placeholder {
      /* Mozilla Firefox 19+ */
      color: #81839f;
    }
    &:-moz-placeholder {
      /* Mozilla Firefox 4 to 18 */
      color: #81839f;
    }
    &:-ms-input-placeholder {
      /* Internet Explorer 10-11 */
      color: #81839f;
    }
  }
  .weui-switch,
  .weui-switch-cp__box {
    height: 30px;
  }
  .weui-switch:before,
  .weui-switch-cp__box:before {
    height: 28px;
  }
  .weui-switch:after,
  .weui-switch-cp__box:after {
    width: 28px;
    height: 28px;
  }
}
</style>

<style scoped lang="less">
@import "../../assets/styles/less/public.less";
.weui-btn_primary {
  background-color: @color_green;
}
</style>
