<template>
  <div class="page">
    <!-- 社保 -->
    <div class="container bg_f">
      <flow class="flow-style">
        <flow-state state="1" title="个人信息" is-done></flow-state>
        <flow-line></flow-line>
        <flow-state state="2" title="参保信息"></flow-state>
        <flow-line></flow-line>
        <flow-state state="3" title="确认支付"></flow-state>
      </flow>
      <group title="参保人信息" :gutter="0">
        <x-input
          title="参保人姓名"
          class="f14"
          placeholder="参保人姓名"
          text-align="right"
          v-model="postForm.username"
        ></x-input>
        <x-input
          title="身份证号"
          class="f14"
          text-align="right"
          placeholder="身份证号"
          v-model="postForm.identification"
        ></x-input>
        <x-input
          title="手机号码"
          class="f14"
          text-align="right"
          placeholder="手机号码"
          v-model="postForm.mobile"
        ></x-input>

        <!-- 弹出层 -->
        <popup-picker
          title="参保城市"
          class="f14"
          :data="cityList"
          :columns="2"
          v-model="city"
          show-name
          @on-hide="onHide"
          @on-change="onChange"
          placeholder="请选择参保城市"
        >
          <template slot="title" slot-scope="props">
            <span class="f14" @click.stop="cityTip">参保城市
              <icon type="info" class="f14"></icon>
            </span>
          </template>
        </popup-picker>
        <!-- 弹出层 -->
        <popup-radio
          title="户口性质"
          class="f14"
          :options="personalType"
          v-model="currentValue"
          @on-hide="hideFun(currentValue)"
          v-if="type === 'SB' || type === 'SG'"
        ></popup-radio>
      </group>

      <!-- 提示 -->
      <group class="bg_f" title="温馨提示" :gutter="0">
        <div class="weui-cells__tip padding-15 pb10">
          <div class="tip-info grey_be f12 mt10" v-html="shbTip">
            <!--<p>社保代理人每月通常有固定的五险一金人员增减，请在开始服务之前向代理人进行确认。</p>
          <p>如果您因为某种原因（如换工作）而需要办理减员，则请在需要减员当月的10号之前通知代理人。</p>
            <p>特别地，由于北京市公积金，每家企业每个月只能申报一次增员和减员，所以您在通过小豆社保代缴公积金时，可能会出现中断一个月的情况，请提前与服务商联系确认。</p>-->
          </div>
        </div>
      </group>

      <!-- footer -->
      <section class="footer-actionBar">
        <section class="footer-actionBar-container flex-ui">
          <div class="flex1 tc white" :disabled="btnNext" @click="!btnNext && nextPage()">
            <span class="f16 pl0">下一步</span>
          </div>
        </section>
      </section>
    </div>
  </div>
</template>

<script>
import {
  Cell,
  CellBox,
  CellFormPreview,
  Group,
  Badge,
  Icon,
  PopupRadio,
  PopupPicker,
  Flow,
  FlowState,
  FlowLine,
  XInput
} from "vux";
import { get, execute } from "@/api/server";
import url from "@/api/apiUrl";
import storage from "@/utils/common";
import { validIsBlank, validMobile, validUserName } from "@/utils/validate";

const defaultForm = {
  id: undefined,
  feeCategory: "fc0", //缴费方式
  username: "", //用户名
  identification: "", //身份证
  mobile: "", //手机号码
  city: { id: undefined }, //城市
  residenceType: { id: undefined } //户口性质
};
export default {
  components: {
    Cell,
    CellBox,
    CellFormPreview,
    Group,
    Badge,
    Icon,
    PopupRadio,
    PopupPicker,
    Flow,
    FlowState,
    FlowLine,
    XInput
  },
  props: {},
  data() {
    return {
      type: this.$route.query && this.$route.query.type,
      btnNext: false,
      quotationId: null,
      isGetMember: false,
      currentValue: 1,
      currentValueTemp: undefined,
      personalType: [
        //   {
        //  	value:'本市城镇职工',
        //  	key:1,
        // },
        // {
        //   value:'本市农村劳动力',
        //   key:2
        // }
      ],
      city: [],
      cityList: [
        //   {
        //   name: '北京',
        //   value: '001',
        //   parent: 0
        // },{
        //   name: '河北',
        //   value: '002',
        //   parent: 0
        // }
      ],
      postForm: Object.assign({}, defaultForm),
      shbTip: undefined
    };
  },
  watch: {},
  computed: {},
  methods: {
    cityTip() {
      //console.log("1111111111")
      this.$vux.alert.show({
        title: "温馨提示",
        content:
          '<p style="font-size:14px">按照各地社保局政策，每年均会调整各地社保缴费基数。小豆社保将按照政策规定时间对基数进行调整。本次您的缴费基数核算后，缴纳的费用如有多出将退还至您的账户。补缴基数自动同步正在参保基数。如有疑问，欢迎致电小豆社保客服：</p>'
      });
    },
    initData() {
      // 获取参保提示信息
      get(url.configInfo, { code: "shbTip" }).then(response => {
        if (response.data.result == "00000000") {
          //console.log(response.data.data)
          if (
            response.data.data != undefined &&
            response.data.data.codeValue != undefined &&
            response.data.data.codeValue != null
          ) {
            this.shbTip = response.data.data.codeValue;
          }
        }
      });
      // 获取城市列表
      get(url.cityList, {})
        .then(response => {
          if (response.data.result == "00000000") {
            this.cityList = response.data.data;
            if (this.cityList != null && this.cityList.length > 0) {
              if (this.quotationId == null) {
                //let cityTemp = {id: cityList[1].value, name: this.cityList[1].fullName }
                //this.city.push(this.cityList[1].value);
                //this.city.push(this.cityList[1].parent+'');
                //this.city.push(this.cityList[1].value+'');
                this.city.push("1");
                this.city.push("2");
                if (
                  this.type != null &&
                  this.type != undefined &&
                  this.type != "GJ"
                ) {
                  //this.onChange(this.city);
                }
                // 是否登录
                let member = storage.get("member");
                if (member != null && member != undefined) {
                  this.postForm.username = member.name;
                  this.postForm.identification = member.identification;
                  this.postForm.mobile = member.mobile;
                }
              } else {
                // 没有  this.quotationId
                if (this.quotationId != null) {
                  get(url.quotationMemberInfo, { id: this.quotationId })
                    .then(response => {
                      if (response.data.result == "00000000") {
                        this.city = [];
                        this.postForm.username = response.data.data.username;
                        this.postForm.identification =
                          response.data.data.identification;
                        this.postForm.mobile = response.data.data.mobile;
                        //let cityTemp = {id: response.data.data.city.id, name: response.data.data.city.fullName }
                        this.city.push(response.data.data.city.parent + "");
                        this.city.push(response.data.data.city.id + "");
                        this.currentValueTemp =
                          response.data.data.residenceType.id;

                        if (
                          this.type != null &&
                          this.type != undefined &&
                          this.type != "GJ"
                        ) {
                          // 获取户口性质
                          get(url.residenceTypeListByCityId, {
                            id: this.city[this.city.length - 1]
                          })
                            .then(response => {
                              let list = response.data.list;
                              this.personalType = [];
                              this.currentValue = undefined;
                              for (var i in list) {
                                var temp = {};
                                temp.key = list[i].id;
                                temp.value = list[i].name;
                                this.personalType.push(temp);
                              }
                              // 下面注释不要去掉
                              console.log(this.currentValueTemp);
                              this.currentValue = this.currentValueTemp;
                            })
                            .catch(e => {
                              console.log(e);
                            });
                        }
                      }
                    })
                    .catch(e => {
                      console.log(e);
                    });
                } else {
                  // 是否登录
                  let member = storage.get("member");
                  if (member != null && member != undefined) {
                    this.postForm.username = member.name;
                    this.postForm.identification = member.identification;
                    this.postForm.mobile = member.mobile;
                  }

                  this.getCityList();
                }
              }
            }
          }
        })
        .catch(e => {
          console.log(e);
        });
    },

    getCityList() {
      get(url.cityList, {})
        .then(response => {
          if (response.data.result == "00000000") {
            this.cityList = response.data.data;
            if (this.cityList != null && this.cityList.length > 0) {
              //let cityTemp = {id: this.cityList[1].value, name: this.cityList[1].fullName }
              //this.city.push(this.cityList[1].value);
              this.city.push(this.cityList[1].parent + "");
              this.city.push(this.cityList[1].value + "");
              if (
                this.type != null &&
                this.type != undefined &&
                this.type != "GJ"
              ) {
                //this.onChange(this.city);
              }
            }
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    hideFun(val) {
      //console.log(val)
    },
    onHide() {},
    onChange(val) {
      console.log(val);
      if (val == null || val == undefined) {
        return;
      }
      get(url.residenceTypeListByCityId, { id: val[1] })
        .then(response => {
          let list = response.data.list;
          this.personalType = [];
          this.currentValue = undefined;
          for (var i in list) {
            var temp = {};
            temp.key = list[i].id;
            temp.value = list[i].name;
            this.personalType.push(temp);
          }

          if (list != undefined && list != null && list.length > 0) {
            this.currentValue = list[0].id;
          }
        })
        .catch(e => {
          console.log(e);
        });
    },
    nextPage() {
      this.postForm.city.id = this.city[this.city.length - 1];
      this.postForm.id = this.quotationId;

      // 参保人姓名输入项校验
      if (validIsBlank(this.postForm.username)) {
        this.$vux.toast.text("请输入参保人姓名", "middle");
        return false;
      } else if (!validUserName(this.postForm.username)) {
        this.$vux.toast.text("参保人姓名只限中文", "middle");
        return false;
      }

      // 身份证号输入项校验
      if (validIsBlank(this.postForm.identification)) {
        this.$vux.toast.text("请输入身份证号", "middle");
        return false;
      } else {
        //let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
        let reg = /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/;
        if (!reg.test(this.postForm.identification)) {
          this.$vux.toast.text("请输入正确的身份证号", "middle");
          return false;
        }
      }

      // 手机号码输入项校验
      if (validIsBlank(this.postForm.mobile)) {
        this.$vux.toast.text("请输入手机号", "middle");
        return false;
      } else if (!validMobile(this.postForm.mobile)) {
        this.$vux.toast.text("请输入正确的手机号", "middle");
        return false;
      }

      // 参保城市输入项校验
      if (
        this.postForm.city.id === null ||
        this.postForm.city.id === undefined ||
        validIsBlank(this.postForm.city.id)
      ) {
        this.$vux.toast.text("请选择参保城市", "middle");
        return false;
      }

      if (this.type === "GJ") {
        this.postForm.feeCategory = "fc1";
      } else if (this.type === "SG") {
        this.postForm.feeCategory = "fc2";
        this.postForm.residenceType.id = this.currentValue;
        // 户口性质输入项校验
        if (
          this.postForm.residenceType === undefined ||
          this.postForm.residenceType === null ||
          this.postForm.residenceType.id === undefined ||
          this.postForm.residenceType.id === null ||
          this.postForm.city.id === undefined ||
          validIsBlank(this.postForm.city.id)
        ) {
          this.$vux.toast.text("请选择户口性质", "middle");
          return false;
        }
      } else {
        this.postForm.feeCategory = "fc0";
        this.postForm.residenceType.id = this.currentValue;
        // 户口性质输入项校验
        if (
          this.postForm.residenceType === undefined ||
          this.postForm.residenceType === null ||
          this.postForm.residenceType.id === undefined ||
          this.postForm.residenceType.id === null ||
          this.postForm.city.id === undefined ||
          validIsBlank(this.postForm.city.id)
        ) {
          this.$vux.toast.text("请选择户口性质", "middle");
          return false;
        }
      }

      this.btnNext = true;
      //console.log(JSON.stringify(this.postForm) );
      execute(url.saveQuotationMemberInfo, this.postForm)
        .then(response => {
          if (response.data.result == "00000000") {
            localStorage.setItem("quotationId", response.data.data);
            this.$router.push({ path: "/order/orderInfo" });
          } else {
            this.$vux.toast.text(response.data.msg, "middle");
            this.btnNext = false;
          }
        })
        .catch(e => {
          console.log(e);
        });
    }
  },
  created() {
    let id = localStorage.getItem("quotationId");
    if (id != null && id != undefined && id != "") {
      this.quotationId = id;
    }

    this.initData();
  },
  mounted() {}
};
</script>

<style lang="less">
.weui-dialog__bd {
  padding: 0 15px 0.8em;
}
</style>
