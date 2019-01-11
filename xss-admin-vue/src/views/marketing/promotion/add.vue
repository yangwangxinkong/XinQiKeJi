<template>
  <div class="createPost-container">
      <el-form :rules="addPromotionRuls" ref="promotionForm" id="promotionForm" :model="promotion" label-position="right" label-width="130px">

      <sticky :className="'sub-navbar'">
        <el-button @click="back">{{$t('promotion.back')}}</el-button>
        <el-button type="success" @click="addPromotion" :loading="addPromotionSubmitLoading">{{$t('table.confirm')}}</el-button>
      </sticky>
      <el-tabs v-model="currentTab" style='margin:10px 30px;'>
      <!--基础信息-->
        <el-tab-pane :label="$t('promotion.basic')" name="basic">
          <el-form-item :label="$t('promotion.name')+':'" prop="name">
            <el-input v-model="promotion.name" class="input"></el-input>
          </el-form-item>
          <el-form-item :label="$t('promotion.title')+':'" prop="title">
            <el-input v-model="promotion.title" class="input"></el-input>
          </el-form-item>
          <el-form-item :label="$t('promotion.useDate')+':'" prop="beginAndEndDate">
            <el-date-picker v-model="promotion.beginAndEndDate" type="datetimerange"
              align="right"
              value-format="timestamp"
              :start-placeholder="$t('promotion.beginDate')"
              :end-placeholder="$t('promotion.endDate')"
              :default-time="['00:00:00', '23:59:59']" class="input">
            </el-date-picker>
          </el-form-item>
          <el-form-item :label="$t('promotion.minimumQuantity')+':'" prop="minimumQuantity">
            <el-input-number v-model="promotion.minimumQuantity" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('promotion.maximumQuantity')+':'" prop="maximumQuantity">
            <el-input-number v-model="promotion.maximumQuantity" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('promotion.minimumPrice')+':'" prop="minimumPrice">
            <el-input-number v-model="promotion.minimumPrice" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('promotion.maximumPrice')+':'" prop="maximumPrice">
            <el-input-number v-model="promotion.maximumPrice" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('promotion.priceExpression')+':'" prop="priceExpression">
            <el-tooltip class="item" effect="dark" :content="$t('promotion.priceExpressionMsg')" placement="right">
              <el-input v-model="promotion.priceExpression" class="input" :placeholder="$t('promotion.priceExpressionMsg')"></el-input>
            </el-tooltip>
          </el-form-item>
          <el-form-item :label="$t('promotion.pointExpression')+':'" prop="pointExpression">
            <el-tooltip class="item" effect="dark" :content="$t('promotion.pointExpressionMsg')" placement="right">
              <el-input v-model="promotion.pointExpression" class="input" :placeholder="$t('promotion.pointExpressionMsg')"></el-input>
            </el-tooltip>
          </el-form-item>
          <el-form-item :label="$t('promotion.order')+':'" prop="order">
            <el-input-number v-model="promotion.order" class="input" :min="0"></el-input-number>
          </el-form-item>
          <el-form-item :label="$t('promotion.memberRanks')+':'">
            <template>
              <el-checkbox-group v-model="promotion.memberRankIds">
                <el-checkbox v-for="item in all_memberRanks" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </template>
          </el-form-item>
          <el-form-item :label="$t('promotion.productCategories')+':'">
            <template>
              <el-checkbox-group v-model="promotion.productCategoryIds">
                <el-checkbox v-for="item in all_productCategories" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </template>
          </el-form-item>
          <el-form-item :label="$t('promotion.brands')+':'">
            <template>
              <el-checkbox-group v-model="promotion.brandIds">
                <el-checkbox v-for="item in all_brands" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </template>
          </el-form-item>
          <el-form-item :label="$t('promotion.coupons')+':'">
            <template>
              <el-checkbox-group v-model="promotion.couponIds">
                <el-checkbox v-for="item in all_coupons" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </template>
          </el-form-item>
          <el-form-item :label="$t('promotion.setting')+':'">
            <el-checkbox-group v-model="promotion.isFreeShipping" prop="isFreeShipping">
              <el-checkbox :label="$t('promotion.isFreeShipping')"></el-checkbox>
            </el-checkbox-group>
            <el-checkbox-group v-model="promotion.isCouponAllowed" prop="isCouponAllowed">
              <el-checkbox :label="$t('promotion.isCouponAllowed')"></el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-row>
            <el-col>
              <el-form-item :label="$t('promotion.products')+':'" prop="products">
                <el-button @click="selectProduct()" >添加</el-button>

                <el-table :data="promotion.products" style="width: 60%" :show-header="false" max-height="500" border>
                  <el-table-column width="600" prop="fullName" label="参与促销的商品列表" show-overpflow-tooltip>
                  </el-table-column>
                  <el-table-column fixed="right" :label="$t('promotion.operation')" width="90">
                    <template slot-scope="scope">
                      <el-button @click="delProducts(scope.row)" type="text" size="small">{{$t('table.delete')}}</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
              <product-select-table @successCBK="addProducts" ref="selectProductsTableRef" title="商品列表-选择参与促销商品" :is-gift="false"></product-select-table>
            </el-col>
          </el-row>

          <el-row>
            <el-col>
              <el-form-item :label="$t('promotion.giftItems')+':'" prop="giftItems">
                <el-button @click="selectGiftItems()" >添加</el-button>

                <el-table :data="promotion.giftItems" style="width: 60%" :show-header="false" max-height="500" border>
                  <el-table-column width="480" prop="gift.fullName" label="赠品" show-overflow-tooltip>
                  </el-table-column>
                  <el-table-column width="120" prop="quantity" label="数量">
                    <template slot-scope="scope">
                      <el-input-number v-model="scope.row.quantity" class="input" style="width: 100px" size="mini" :min="1"></el-input-number>
                    </template>
                  </el-table-column>
                  <el-table-column fixed="right" :label="$t('promotion.operation')" width="90">
                    <template slot-scope="scope">
                      <el-button @click="delGiftItems(scope.row)" type="text" size="small">{{$t('table.delete')}}</el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </el-form-item>
              <product-select-table @successCBK="addGiftItems" ref="selectGiftItemsTableRef" title="商品列表-选择赠品" :is-gift="true"></product-select-table>
            </el-col>
          </el-row>
        </el-tab-pane>

        <!--介绍-->
        <el-tab-pane :label="$t('promotion.introduce')" name="introduce">
          <div class="components-container">
            <div>
              <tinymce :height="300" v-model="promotion.introduction"></tinymce>
            </div>
            <!--<div class="editor-content" v-html="promotion.introduction"></div>-->
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
  import {addSelectData,savePromotion} from '@/api/marketing/promotion'
  import Tinymce from '@/components/Tinymce'
  import {goback} from '@/utils/common'
  import Sticky from '@/components/Sticky' // 粘性header组件
  import ProductSelectTable from "@/components/OpenDialog/productSelectTable";

  export default {
    name: 'promotionAdd',
    components: {ProductSelectTable, Tinymce,Sticky },
    data() {
      return {
        currentTab: 'basic',

        promotion:{
          id: undefined,
          name: undefined,
          title: undefined,
          beginAndEndDate: undefined,
          beginDate: undefined,
          endDate: undefined,
          minimumQuantity: undefined,
          maximumQuantity: undefined,
          minimumPrice: undefined,
          maximumPrice: undefined,
          priceExpression: undefined,
          pointExpression: undefined,
          order: 0,
          isFreeShipping: false,
          isCouponAllowed: false,
          introduction: undefined,

          products: [],
          giftItems: [],
          // giftItems: [{id:0,quantity:0,gift:{id:0}}],

          memberRankIds:[],
          productCategoryIds:[],
          brandIds: [],
          couponIds: [],
        },

        all_memberRanks:[],
        all_productCategories:[],
        all_brands: [],
        all_coupons: [],

        giftItemsText: undefined,
        productsText: undefined,

        giftItemsLoading: false,
        productsLoading: false,

        addPromotionSubmitLoading: false,
        addPromotionRuls:{
          name: [{required: true, message: '必填', trigger: 'blur'}],
          title: [{required: true, message: '必填', trigger: 'blur'}],
          beginAndEndDate: [{required: true, message: '必填', trigger: 'blur'}],
          minimumQuantity: [{required: true, message: '必填', trigger: 'blur'}],
          maximumQuantity: [{required: true, message: '必填', trigger: 'blur'}],
          minimumPrice: [{required: true, message: '必填', trigger: 'blur'}],
          maximumPrice: [{required: true, message: '必填', trigger: 'blur'}],
        },
        selectedTag:{},



      }
    },

    created(){
      this.addSelectData()
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

      addSelectData(){
        addSelectData().then(response => {
          if(response.data.result === "00000000"){
            this.all_brands = response.data.data.all_brands==null?[]:response.data.data.all_brands
            this.all_coupons = response.data.data.all_coupons==null?[]:response.data.data.all_coupons
            this.all_memberRanks = response.data.data.all_memberRanks==null?[]:response.data.data.all_memberRanks
            this.all_productCategories = response.data.data.all_productCategories==null?[]:response.data.data.all_productCategories
          }

        }).catch(e => {
          console.log(e)
        })
      },

      useDateChange(){
        // this.promotion.beginDate = this.promotion.beginAndEndDate[0]
        // this.promotion.endDate = this.promotion.beginAndEndDate[1]
        console.log(JSON.stringify(this.promotion))
      },

      addPromotion(){
        this.$refs['promotionForm'].validate((valid) => {
          if (valid) {
            console.log(JSON.stringify(this.promotion))
            let productIds=[]

            if(this.promotion.products) {
              for (let i = 0; i < this.promotion.products.length; i++) {
                productIds.push(this.promotion.products[i].id)
              }
            }

            this.promotion.products = undefined

            if(productIds!==undefined && productIds.length>0){
              this.promotion.productIds = productIds
            }

            let giftItemsJson = []
            for (let i=0; i<this.promotion.giftItems.length; i++){
              let giftItemJson = {
                quantity: this.promotion.giftItems[i].quantity,
                productId: this.promotion.giftItems[i].gift.id
              }
              giftItemsJson.push(giftItemJson)
            }
            this.promotion.giftItemsJson = JSON.stringify(giftItemsJson)
            this.promotion.giftItems = undefined
            console.log(JSON.stringify(this.promotion))

            this.addPromotionSubmitLoading = true
            savePromotion(this.promotion).then(response => {
              if(response.data.result === "00000000"){
                this.$refs['promotionForm'].resetFields()
                this.promotion.introduction = undefined
                this.$message({
                  type: 'success',
                  message: '添加成功!'
                })
                this.back();
              }else{
                this.$message({
                  type: 'error',
                  message: '添加失败!'+response.data.msg
                })
              }
              this.addPromotionSubmitLoading = false
            }).catch(e => {
              console.log(e)
              this.addPromotionSubmitLoading = false
            })
          }
        })

      },

      selectProduct(){
        this.$refs.selectProductsTableRef.dialogTableVisible=true
      },
      selectGiftItems(){
        this.$refs.selectGiftItemsTableRef.dialogTableVisible=true
      },
      addGiftItems(giftItems){
        if(this.promotion.giftItems==null)
          this.promotion.giftItems = []
        if(null == giftItems) {
          giftItems = []
        }
        let length = giftItems.length
        for(let i=0; i<length; i++){
          let isContain = false
          for(let j=0; j<this.promotion.giftItems.length; j++){
            if(this.promotion.giftItems[j].id === giftItems[i].id){
              isContain = true
              break
            }
          }
          if(!isContain){
            let giftItem = {quantity:1,gift:{id:giftItems[i].id,fullName:giftItems[i].fullName}}
            this.promotion.giftItems.push(giftItem)
          }
        }

      },
      addProducts(products){
        if(this.promotion.products==null)
          this.promotion.products = []
        if(null == products) {
          products = []
        }
        let length = products.length
        for(let i=0; i<length; i++){
          let isContain = false
          for(let j=0; j<this.promotion.products.length; j++){
            if(this.promotion.products[j].id === products[i].id){
              isContain = true
              break
            }
          }
          if(!isContain){
            this.promotion.products.push(products[i])
          }
        }
      },
      delGiftItems(giftItems){
        if(this.promotion.giftItems==null)
          return
        for(let i=0; i<this.promotion.giftItems.length; i++){
          if(this.promotion.giftItems[i].id === giftItems.id){
            this.promotion.giftItems.splice(i, 1);
          }
        }
      },
      delProducts(product){
        if(this.promotion.products==null)
          return
        for(let i=0; i<this.promotion.products.length; i++){
          if(this.promotion.products[i].id === product.id){
            this.promotion.products.splice(i, 1);
          }
        }
      },


    }
  }
</script>
