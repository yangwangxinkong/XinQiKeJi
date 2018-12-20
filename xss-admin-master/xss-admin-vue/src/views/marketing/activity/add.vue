<template>
  <div class="createPost-container">
    <el-form :rules="addActivityRuls" ref="activityForm" id="activityForm" :model="activity" label-position="right" label-width="100px">
      <sticky :className="'sub-navbar'">
        <el-button @click="back">{{$t('promotion.back')}}</el-button>
        <el-button type="success" @click="addActivity" :loading="addActivitySubmitLoading">{{$t('table.confirm')}}</el-button>
      </sticky>
      <el-tabs v-model="currentTab" style='margin:10px 30px;'>
      <!--基础信息-->
        <el-tab-pane label="基本信息" name="basic">
          <el-form-item label="活动主标题:" prop="mainTitle">
            <el-input v-model="activity.mainTitle" class="input"></el-input>
          </el-form-item>
          <el-form-item label="活动副标题:" prop="subTitle">
            <el-input v-model="activity.subTitle" class="input"></el-input>
          </el-form-item>
          <el-form-item label="活动时间:" prop="beginAndEndDate">
            <el-date-picker v-model="activity.beginAndEndDate" type="datetimerange"
              align="right"
              value-format="timestamp"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              :default-time="['00:00:00', '23:59:59']" class="input">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="活动url:" prop="url">
            <el-input v-model="activity.url" class="input"></el-input>
          </el-form-item>
          <el-form-item label="PC展示图:" prop="bannerPc">
            <singleImage color="#1890ff" :url = activity.bannerPc  class="editor-upload-btn" @successCBK="pcBannerSuccessCBK"></singleImage>
          </el-form-item>
          <el-form-item label="微信展示图:" prop="bannerWx">
            <singleImage color="#1890ff" :url = activity.bannerWx  class="editor-upload-btn" @successCBK="wxBannerSuccessCBK"></singleImage>
          </el-form-item>
          <el-form-item label="是否推荐：">
            <el-checkbox-group v-model="activity.isRecommend" prop="isEnabled">
              <el-checkbox label="推荐"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="活动类型:">
            <template>
              <el-select v-model="activity.type" placeholder="请选择">
                <el-option
                  v-for="type in activityType"
                  :key="type.value"
                  :label="type.desc"
                  :value="type.value">
                </el-option>
              </el-select>
            </template>
          </el-form-item>

          <div v-if="activity.type==='promotion'">
            <el-row>
              <el-col>
                <el-form-item label="促销商品:" prop="promotionProducts">
                  <el-button @click="selectProducts()" >添加</el-button>
                  <el-table :data="activity.promotionProducts" style="width: 60%" max-height="500" border>
                    <el-table-column width="500" prop="fullName" label="活动商品" show-overflow-tooltip>
                    </el-table-column>
                    <el-table-column prop="price" label="价格">
                    </el-table-column>
                    <el-table-column fixed="right" :label="$t('table.actions')" width="90">
                      <template slot-scope="scope">
                        <el-button @click="delProducts(scope.row)" type="text" size="small">{{$t('table.delete')}}</el-button>
                      </template>
                    </el-table-column>
                  </el-table>
                </el-form-item>
                <product-select-table @successCBK="addProducts" ref="selectProductsTableRef" title="商品列表-选择促销商品" :is-gift="false"></product-select-table>
              </el-col>
            </el-row>
            <el-form-item label="原价:" prop="oldPrice">
              {{activity.oldPrice}}
            </el-form-item>
            <el-form-item label="促销价:" prop="proPrice">
              <el-input-number v-model="activity.proPrice" class="input"></el-input-number>
            </el-form-item>
            <el-form-item label="定金:" prop="downPayment">
              <el-input-number v-model="activity.downPayment" class="input"></el-input-number>
            </el-form-item>
          </div>
          <el-form-item  label="文字简介:" prop="memo">
            <el-input  style='width: 500px;' type="textarea" :rows="10" placeholder="请输入文字简介"  v-model="activity.memo" ></el-input>
          </el-form-item>
          <el-form-item label="排序:" prop="order">
            <el-input-number v-model="activity.order" class="input" :min="0"></el-input-number>
          </el-form-item>
        </el-tab-pane>

        <!--介绍-->
        <el-tab-pane label="活动图文" name="introduce">
          <div class="components-container">
            <div>
              <tinymce :height="300" v-model="activity.text"></tinymce>
            </div>
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
  import {saveActivity,getAllProducts} from '@/api/marketing/activity'
  import Tinymce from '@/components/Tinymce'
  import {goback} from "@/utils/common";
  import Sticky from '@/components/Sticky' // 粘性header组件
  import ProductSelectTable from "@/components/OpenDialog/productSelectTable";
  import {selectProducts} from '@/api/product'
  import singleImage from '@/components/Upload/singleImage5'

  export default {
    name: 'activityAdd',
    components: { Tinymce,Sticky,ProductSelectTable,singleImage },
    data() {
      return {
        currentTab: 'basic',

        activity:{
          activityId: undefined,
          mainTitle: undefined,
          subTitle: undefined,
          beginAndEndDate: undefined,
          beginDate: undefined,
          endDate: undefined,
          isRecommend: false,
          type: 'advertise',
          url: undefined,
          bannerPc: undefined,
          bannerWx: undefined,
          text: undefined,
          promotionProducts: [],
          oldPrice: 0,
          proPrice: 0,
          downPayment: 0,
          memo:undefined,
          order:0
        },
        activityType: [
          {value:'advertise',desc:'专题活动'},
//          {value:'promotion',desc:'促销活动'},
//          {value:'signup',desc:'报名活动'},
        ],
        addActivitySubmitLoading: false,
        addActivityRuls:{
          mainTitle: [{required: true, message: '必填', trigger: 'blur'}],
          subTitle: [{required: true, message: '必填', trigger: 'blur'}],
          type: [{required: true, message: '必填', trigger: 'change'}],
          beginAndEndDate: [{required: true, message: '必填', trigger: 'blur'}],
          proPrice: [{required: true, message: '必填', trigger: 'blur'}],
          downPayment: [{required: true, message: '必填', trigger: 'blur'}],
          bannerPc: [{required: true, message: '必填', trigger: 'blur'}],
          bannerWx: [{required: true, message: '必填', trigger: 'blur'}],
        },
        property: [
          {propName:'sn', label:'编号'},
          {propName:'fullName', label:'名称'},
          {propName:'price', label:'价格'},
        ],


        productsText: undefined,
        productsLoading: false,

        selectedTag:{}


      }
    },

    computed: {
      visitedViews() {
        return this.$store.state.tagsView.visitedViews
      }
    },
    methods: {
      back() {
        goback(this.$route.path)
      },

      addActivity(){
        this.$refs['activityForm'].validate((valid) => {
          if (valid) {
            this.$confirm("确定添加活动吗？", '提示', {
              confirmButtonText: '确定',
              cancelButtonText: '取消',
              type: 'warning'
            }).then(() => {

              if (this.activity.type === 'promotion'
                && (this.activity.promotionProducts === undefined || this.activity.promotionProducts.length < 1)) {
                this.$message({
                  type: 'error',
                  message: '促销活动至少要有一个促销商品!'
                })
                return
              }

              this.addActivitySubmitLoading = true
              // let activityJson = JSON.stringify(this.activity)
              console.log(this.activity)
              saveActivity(this.activity).then(response => {
                if (response.data.result === "00000000") {
                  this.$refs['activityForm'].resetFields()
                  this.activity.text = undefined
                  this.$message({
                    type: 'success',
                    message: '添加成功!'
                  })
                  this.back();
                }
                this.addActivitySubmitLoading = false
              }).catch(e => {
                console.log(e)
                this.addActivitySubmitLoading = false
              })
            })
          }
        })

      },

      selectProducts(){
        this.$refs.selectProductsTableRef.dialogTableVisible=true
      },
      addProducts(products){
        if(this.activity.promotionProducts==null)
          this.activity.promotionProducts = []
        if(null == products) {
          products = []
        }
        let length = products.length
        for(let i=0; i<length; i++){
          let isContain = false
          for(let j=0; j<this.activity.promotionProducts.length; j++){
            if(this.activity.promotionProducts[j].productSn === products[i].sn){
              isContain = true
              break
            }
          }
          if(!isContain){
            let product = {
              productSn: products[i].sn,
              storeId: products[i].store===undefined? undefined: products[i].store.id,
              fullName: products[i].fullName,
              price: products[i].price
            }
            this.activity.promotionProducts.push(product)
            this.activity.oldPrice = this.activity.oldPrice===undefined? product.price: this.activity.oldPrice+product.price
          }
        }
      },
      delProducts(product){
        if(this.activity.promotionProducts==null)
          return
        for(let i=0; i<this.activity.promotionProducts.length; i++){
          if(this.activity.promotionProducts[i].productSn === product.productSn){
            this.activity.oldPrice -= product.price
            this.activity.promotionProducts.splice(i, 1);
          }
        }
      },
      pcBannerSuccessCBK(path) {
        this.activity.bannerPc = path;
      },
      wxBannerSuccessCBK(path) {
        this.activity.bannerWx = path;
      },

    }
  }
</script>


<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/mixin.scss";
</style>
