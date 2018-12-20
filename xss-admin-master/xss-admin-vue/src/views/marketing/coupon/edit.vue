<template>
  <div class="createPost-container">
    <el-form :rules="updateCouponRuls" ref="couponForm" id="couponForm" :model="coupon" label-position="right" label-width="120px">

      <sticky :className="'sub-navbar'">
        <el-button @click="back">{{$t('coupon.back')}}</el-button>
        <el-button type="success" @click="updateCoupon" :loading="updateCouponSubmitLoading">{{$t('table.confirm')}}</el-button>
      </sticky>
      <el-tabs v-model="currentTab" style='margin:10px 30px;'>
        <!--基础信息-->
        <el-tab-pane :label="$t('coupon.basic')" name="basic">
          <el-form-item :label="$t('coupon.name')+':'" prop="name">
            <el-input v-model="coupon.name" class="input"></el-input>
          </el-form-item>
          <el-form-item :label="$t('coupon.prefix')+':'" prop="prefix">
            <el-input v-model="coupon.prefix" class="input"></el-input>
          </el-form-item>
          <el-form-item :label="$t('coupon.useDate')+':'" prop="beginAndEndDate">
            <el-date-picker v-model="coupon.beginAndEndDate" type="datetimerange"
              align="right"
              value-format="timestamp"
              :start-placeholder="$t('coupon.useStartDate')"
              :end-placeholder="$t('coupon.useEndDate')"
              class="input"
              @change="useDateChange" @blur="useDateChange">
            </el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('coupon.minimumQuantity')+':'" prop="minimumQuantity">
            <el-input-number v-model="coupon.minimumQuantity" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('coupon.maximumQuantity')+':'" prop="maximumQuantity">
            <el-input-number v-model="coupon.maximumQuantity" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('coupon.minimumPrice')+':'" prop="minimumPrice">
            <el-input-number v-model="coupon.minimumPrice" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('coupon.maximumPrice')+':'" prop="maximumPrice">
            <el-input-number v-model="coupon.maximumPrice" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('coupon.priceExpression')+':'" prop="priceExpression">
            <el-tooltip class="item" effect="dark" :content="$t('promotion.priceExpressionMsg')" placement="right">
              <el-input v-model="coupon.priceExpression" class="input" :placeholder="$t('promotion.priceExpressionMsg')"></el-input>
            </el-tooltip>
          </el-form-item>
          <el-form-item :label="$t('coupon.setting')+':'">
            <el-checkbox-group v-model="coupon.isEnabled" prop="isEnabled">
              <el-checkbox :label="$t('coupon.isEnabled')"></el-checkbox>
            </el-checkbox-group>
            <el-checkbox-group v-model="coupon.isExchange" prop="isExchange">
              <el-checkbox :label="$t('coupon.isExchange')"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item :label="$t('coupon.point')+':'" prop="point">
            <el-input-number v-model="coupon.point" class="input" :min="0"></el-input-number>
          </el-form-item>
        </el-tab-pane>

        <!--介绍-->
        <el-tab-pane :label="$t('coupon.introduce')" name="introduce">
          <div class="components-container">
            <div>
              <tinymce :height="300" v-model="coupon.introduction"></tinymce>
            </div>
            <!--<div class="editor-content" v-html="coupon.introduction"></div>-->
          </div>
        </el-tab-pane>

      </el-tabs>
    </el-form>


  </div>
</template>

<style>
  .input{
    width: 400px;
  }

</style>

<script>
  import {updateCoupon, info} from '@/api/marketing/coupon'
  import Tinymce from '@/components/Tinymce'
  import {goback} from '@/utils/common'
  import Sticky from '@/components/Sticky' // 粘性header组件

  export default {
    name: 'couponEdit',
    components: { Tinymce,Sticky },
    data() {
      return {
        currentTab: 'basic',

        coupon:{
          id: undefined,
          name: undefined,
          prefix: undefined,
          beginAndEndDate: [],
          minimumQuantity: undefined,
          maximumQuantity: undefined,
          minimumPrice: undefined,
          maximumPrice: undefined,
          priceExpression: undefined,
          isEnabled: false,
          isExchange: false,
          point: undefined,
          introduction: undefined
        },
        updateCouponSubmitLoading: false,
        updateCouponRuls:{
          name: [{required: true, message: '必填', trigger: 'blur'}],
          prefix: [{required: true, message: '必填', trigger: 'blur'}],
          beginAndEndDate: [{required: true, message: '必填', trigger: 'blur'}],
          minimumQuantity: [{required: true, message: '必填', trigger: 'blur'}],
          maximumQuantity: [{required: true, message: '必填', trigger: 'blur'}],
          minimumPrice: [{required: true, message: '必填', trigger: 'blur'}],
          maximumPrice: [{required: true, message: '必填', trigger: 'blur'}],
          point: [{required: true, message: '必填', trigger: 'blur'}]
        },


      }
    },

    created(){
      this.coupon.id = this.$route.params.id
      this.info(this.coupon.id)
    },

    methods: {
      back(){
        goback(this.$route.path)
      },

      useDateChange(val){
        console.log(val)
        console.log(JSON.stringify(this.coupon))
        // this.coupon.beginAndEndDate=
      },

      info(id){
        info(id).then(response => {
          if(response.data.result === "00000000"){
            response.data.data.beginAndEndDate=[response.data.data.beginDate,response.data.data.endDate]

            response.data.data.beginDate = undefined
            response.data.data.endDate = undefined
            this.coupon = response.data.data
            console.log("data:" + JSON.stringify(this.coupon.beginAndEndDate));
          }else {
            this.$message({
              type: 'error',
              message: '获取数据失败!'
            });
          }
        }).catch(e => {
          console.log(e)
        })
      },

      updateCoupon(){
        this.$refs['couponForm'].validate((valid) => {
          if (valid) {
            this.$confirm("确定修改该优惠券吗？", '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {
              this.updateCouponSubmitLoading = true
              updateCoupon(this.coupon).then(response => {
                if (response.data.result === "00000000") {
                  this.$refs['couponForm'].resetFields()
                  this.coupon.introduction = undefined
                  this.$message({
                    type: 'success',
                    message: '修改成功!'
                  })
                  this.back()
                }
                this.updateCouponSubmitLoading = false
              }).catch(e => {
                console.log(e)
                this.updateCouponSubmitLoading = false
              })
            })

          }

        })

      }

    }
  }
</script>
