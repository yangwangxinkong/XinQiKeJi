<template>
  <div class="createPost-container">
      <el-form ref="promotionForm" id="promotionForm" :model="promotion" label-position="right" label-width="130px">

          <el-row>
            <el-col>
              <el-form-item :label="$t('promotion.products')+':'" prop="products">
                <select-dialog @successCBK="addProducts" @queryData="selectProducts"
                               :params="params"
                               :property="property"
                               :init-data="initProducts"
                               distinguishProp="id"
                               :is-preview="true"
                               :is-selected="false"
                               ref="selectProductsTableRef"
                               title="商品列表-选择参与促销商品"></select-dialog>
              </el-form-item>
            </el-col>
          </el-row>

    </el-form>

  </div>
</template>

<script>
  import SelectDialog from "@/components/OpenDialog/selectDialog";
  import {selectProducts} from '@/api/product'

  export default {
    name: 'testAdd',
    components: {SelectDialog},
    data() {
      return {
        currentTab: 'basic',
        params: {
          isGift: false,
        },
        //要显示的列表字段，propName为字段名，label为列标题
        property: [
          {propName:"sn",label:"编号"},
          {propName:"fullName",label:"名称"},
          {propName:"price",label:"价格"},
        ],
        promotion:{
          products: [],
        },
        //初始回显数据
        initProducts: [{
          "image":"",
          "marketPrice":93.60,
          "fullName":"大红袍花椒[四川茂县 特级 鲜红有光泽 浓郁醇厚无刺激 大而均匀]",
          "weight":0,
          "title":"",
          "point":78,
          "productCategory":{"name":"四川花椒","id":12},
          "unit":"",
          "price":78.00,
          "name":"大红袍花椒",
          "id":1,
          "sn":"201803276161",
          "stock":0,
          "productType":{"name":"standard","desc":"标准商品"},
          "createDate":"2018-03-27 18:22:46"
        }],
      }
    },

    methods: {
      //将接口函数传入组件
      selectProducts(callback){
        return callback(selectProducts)
      },

      //选择回调，弹框点确认 或删除，都会返回选择的所有选项
      addProducts(products){
        console.log(JSON.stringify(products))
        if(this.promotion.products==null)
          this.promotion.products = []
        if(null == products) {
          products = []
        }
        this.promotion.products = products
      },


    }
  }
</script>
