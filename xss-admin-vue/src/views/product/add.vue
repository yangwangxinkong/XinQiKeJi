<template>
  <div class="createPost-container">
    <el-form :rules="rules" ref="postForm" :model="pushData" label-position="right" label-width="150px">
      <sticky :className="'sub-navbar'" style="width: 100%">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loading" style="margin-left: 10px;" type="success" @click="submitForm">  {{$t('table.save')}}</el-button>
      </sticky>
      <div class="tab-container">
      <el-tabs>
        <el-tab-pane label="基本信息">
          <el-form-item label="商品分类：">
            <el-select class="filter-item" v-model="pushData.productCategory.id" @change="changeProductCategory">
              <el-option
                v-for="(item,index) in categoryData" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"
                :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <!--<el-form-item label="商品类型：">
            <el-radio-group v-model="pushData.productType" @change="productTypeChange">
              <el-radio v-for="item in productTypes" :label="item.name" :key="item.name">{{item.desc}}</el-radio>
            </el-radio-group>
          </el-form-item>-->
          <el-form-item label="商品名称：" prop="name">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.name" clearable></el-input>
            </el-col>
          </el-form-item>
          <!--<el-form-item label="标签：">
            <el-col :span="10">
              <el-checkbox-group v-model="pushData.tagIds" disabled>
                <el-checkbox v-for="item in tagOptions" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
              </el-checkbox-group>
            </el-col>
          </el-form-item>-->
          <el-form-item label="设置：">
            <el-col :span="15">
              <el-checkbox :label="pushData.isMarketable" v-model="pushData.isMarketable">上架</el-checkbox>
              <el-checkbox :label="pushData.isList" v-model="pushData.isList">列出</el-checkbox>
              <el-checkbox :label="pushData.isTop" v-model="pushData.isTop">置顶</el-checkbox>
              <el-checkbox :label="pushData.isGift" v-model="pushData.isGift">为赠品</el-checkbox>
              <el-checkbox :label="pushData.isLimit" v-model="pushData.isLimit">限时优惠</el-checkbox>
            </el-col>
          </el-form-item>
          <el-form-item label="促销标题：" prop="title">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.title" clearable ></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="编号：" prop="sn">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.sn" clearable placeholder="若留空则系统自动生成"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="销售价：" prop="price">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.price" clearable placeholder="销售价"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="成本价：" prop="cost">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.cost" clearable placeholder="成本价"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="市场价：" prop="marketPrice">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.marketPrice" clearable placeholder="市场价"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="单位：" prop="unit">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.unit" clearable placeholder="单位"></el-input>
            </el-col>
          </el-form-item>

          <el-form-item label="商品重量：" prop="weight">
            <el-col :span="5">
              <el-input-number v-model="pushData.weight" clearable :min="0" label="请输入商品重量" style="width: 100%"></el-input-number>
            </el-col>
            <el-col :span="1">
              &nbsp;&nbsp;KG
            </el-col>
          </el-form-item>
          <el-form-item label="库存：" prop="stock">
            <el-col :span="5">
              <el-input-number v-model="pushData.stock" clearable :min="0" label="请输入库存" style="width: 100%"></el-input-number>
            </el-col>
          </el-form-item>
          <el-form-item label="库存备注：" prop="stockMemo">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.stockMemo" clearable placeholder="库存备注"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="赠送积分：" prop="point">
            <el-col :span="5">
              <el-input-number v-model="pushData.point" clearable :min="0" style="width: 100%"></el-input-number>
            </el-col>
          </el-form-item>
          <el-form-item label="兑换积分：" prop="exchangePoint">
            <el-col :span="5">
              <el-input-number v-model="pushData.exchangePoint" clearable :min="0" style="width: 100%"></el-input-number>
            </el-col>
          </el-form-item>
          <el-form-item label="品牌：">
            <el-select class="filter-item" v-model="pushData.brand.id" placeholder="请选择品牌">
              <el-option v-for="item in  brandOptions" :key="item.id" :label="item.name" :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="发货时间：" prop="deliveryTime">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.deliveryTime" clearable placeholder="发货时间"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="发货地：" prop="deliveryPlace">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.deliveryPlace" clearable placeholder="发货地"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="备注：" prop="memo">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.memo" clearable placeholder="备注"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="搜索关键词：" prop="keyword">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.keyword" clearable placeholder="搜索关键词"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="页面标题：" prop="seoTitle">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.seoTitle" clearable placeholder="页面标题"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="页面关键词：" prop="seoKeywords">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.seoKeywords" clearable placeholder="页面关键词"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="页面描述：" prop="seoDescription">
            <el-col :span="15" class="orange">
              <el-input style="width: 200px;" v-model="pushData.seoDescription" clearable placeholder="页面描述"></el-input>
            </el-col>
          </el-form-item>
        </el-tab-pane>
        <!--<el-tab-pane label="运费策略" v-if="tabFreightShow">
          <el-form-item label="运费策略：" prop="isStoreFreight">
            <el-col :span="3" class="orange">
              <el-checkbox-group v-model="pushData.isStoreFreight" size="medium" @change="handleCheckedfreightChange">
                <el-checkbox border label="卖家承担运费" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-col>
            <el-button class="filter-item" v-if="btnFreightShow" @click="handleCreateFreight" type="success">添加运费</el-button>

            <el-table :data="FreightchildList" class="el-tb-edit" v-loading="listLoading" element-loading-text="Loading"
                      ref="FreightTable" fit highlight-current-row empty-text="暂未添加运费模板"
                      @current-change="handleCurrentChange" @selection-change="handleSelectionChange" v-if="btnFreightShow" style="width: 90%">
              <el-table-column type="selection" align="center" width="55" row-key="id"></el-table-column>
              <el-table-column prop="minWeight" label="起始件数">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.minWeight" size="mini" controls-position="right"
                                   :min="0"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column prop="maxWeight" label="结束件数">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.maxWeight" size="mini" controls-position="right"
                                   :min="0"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column prop="price" label="运费单价">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.price" size="mini" controls-position="right"
                                   :min="0"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="delDataChild(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>

          </el-form-item>
          <el-form-item label="地区选择：" prop="distributionarea">
            &lt;!&ndash;<el-cascader :options="options" v-model="areaData" @change="handleAreaData" style="width:300px"></el-cascader>&ndash;&gt;
            <el-checkbox border label="全国" checked disabled></el-checkbox>
          </el-form-item>
        </el-tab-pane>-->
        <el-tab-pane label="商品介绍">
          <el-form-item label="详细介绍：" prop="introduction">
            <Tinymce :height=500 ref="editor"  v-model="pushData.introduction" style="width: 95%"/>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="商品图片">
          <el-form-item label="商品图片：">
            <el-button class="filter-item" @click="handleCrtImgUp" type="success">添加图片</el-button>
            <el-table class="el-tb-edit" :data="pushData.productImages" ref="ImgTable"  fit highlight-current-row empty-text="点击‘添加图片’按钮" style="width: 90%">
              <el-table-column prop="title" label="标题">
                <template slot-scope="scope">
                  <el-input v-model="scope.row.title" size="mini" controls-position="right"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="图片">
                <template slot-scope="scope">
                  <singleImage color="#1890ff" class="editor-upload-btn" v-model="scope.row.source" :url="scope.row.source"  @successCBK="imageSuccessCBK" :property="scope.row"></singleImage>
                </template>
              </el-table-column>
              <el-table-column prop="order" label="排序">
                <template slot-scope="scope">
                  <el-input-number v-model="scope.row.order" size="mini" controls-position="right" :min="0"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="delImg(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="商品参数">
          <div v-for="(item,index) in parameterGroups" :key="item.id">
            <el-form-item :label="item.name+'：'"></el-form-item>
            <el-form-item :label="parameter.name+'：'" v-for="(parameter) in item.parameters" :key="parameter.id">
              <el-col :span="15" class="orange">
                <el-input style="width: 200px;" v-model="parameter.value" clearable
                          :placeholder="parameter.name"></el-input>
              </el-col>
            </el-form-item>
          </div>
        </el-tab-pane>
        <el-tab-pane label="商品属性">
          <div v-for="(attribute,index) in attributeOptions" :key="attribute.id">
            <el-form-item :label="attribute.name+'：'">
              <el-col :span="15" class="orange">
                <el-select class="filter-item" v-model="attribute.value">
                  <el-option v-for="(item,index) in attribute.options" :key="item.option" :label="item.option" :value="item.option">
                  </el-option>
                </el-select>
              </el-col>
            </el-form-item>
          </div>
        </el-tab-pane>
        <el-tab-pane label="商品规格">
          <el-form-item label="规格与尺寸：">
            <div class="filter-container">
              <el-checkbox-group v-model="specificationIds">
                <el-checkbox v-for="(specificationIds,index) in specificationNames" :label="specificationIds.value"
                             :key="specificationIds.value"
                             @change="handleCheckedspecificationNamesChange(specificationIds.label,specificationIds.value)">
                  {{specificationIds.label}}
                </el-checkbox>
              </el-checkbox-group>
              <el-button class="filter-item " @click="handleCreateSpecificationProduct" type="success">添加规格</el-button>
            </div>

            <el-table :data="tableData" :key='key' fit highlight-current-row style="width: 90%"
                      ref="SpecificationProductTable">
              <el-table-column prop="name" label="规格"></el-table-column>
              <el-table-column :key='headItem.value' v-for='headItem in formThead' :label="headItem.label">
                <template slot-scope="scope">
                  <el-select style="width: 80%" class="filter-item" v-model="scope.row[headItem.value].valueCode"
                             placeholder="请选择" @change="selectGet">
                    <el-option v-for="item in scope.row[headItem.value].originList" :key="item.value"
                               :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </template>
              </el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger"
                             @click="delSpecificationProductDataChild(scope.$index, scope.row)" v-if="SpecificationProductShow">删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>
      </div>
    </el-form>

  </div>
</template>

<script>
  const defaultFormThead = [
    {
      label: "操作",
      value: "caozuo"
    }
  ];
  import {Message} from "element-ui";
  import {getToken} from "@/utils/auth";
  import draggable from "vuedraggable";
  import Tinymce from "@/components/Tinymce";
  import {fetchProductCategoryTree} from "@/api/productCategory";
  import {fetchBrandAllList} from "@/api/brand";
  import {createProduct, loadParameters, loadAttributes, loadSpecification, getAddProductInitInfo} from "@/api/product";
  import Sticky from '@/components/Sticky' // 粘性header组件
  import { goback } from '@/utils/common'
  import singleImage from '@/components/Upload/singleImage5' // 图片上传
  import $ from 'jquery';
  import ProductSelectTable from "@/components/OpenDialog/productSelectTable";

  export default {
    name: 'add',
    components: { Tinymce,Sticky,draggable,singleImage,ProductSelectTable },
    computed: {},
    data() {
      return {
        tagOptions: [],
        productTypes: [],
        storeName: '',
        btnFreightShow: false,
        tabFreightShow: true,
        groupShow: false,
        FreightdialogListVisible: false,
        FreightchildList: [],
        currentRow: null,
        listLoading: false,
        loading: false,
        categoryData: [],
        communityList: [],
        specificationNames: [

        ],
//        SpecificationProduct: {},
        SpecificationProductShow: false,
        //表单验证
        rules: {
          name: [{required: true, message: "请输入商品名称", trigger: "blur"}],
          price: [{required: true, message: "请输入销售价", trigger: "blur"}],
        },
        pushData: {
          id: undefined,
          tagIds: [],
          isStoreFreight: true, //是否卖家承担运费
          provinceId: undefined,
          cityId: undefined,
          districtId: undefined,
          name: undefined,
          status: "0",
          originalPrice: 0,
          point: 0,
          exchangePoint: 0,
          minPrice: 0,
          groupPrice: 0,
          groupQuantity: 0,
          beginTime: undefined,
          endTime: undefined,
          groupStatus: undefined,
          community:{id:undefined},// 社区
          stores: 0,
          weight: 0,
          commission: 0,
          pics: [],
          photos: [],
          introduction: undefined,
          dateAddStr: undefined,
          originid: undefined,
          tableData: [],
          brand:{id:undefined},
          productCategory:{id:undefined},
          productType:'service',
          isMarketable:false,
          isTop:false,
          isGift:false,
          isList:false,
          isLimit:false,
          isFullService:false,    //监理商品全程管家式商品标识
          //isRecommend:false,
          deliveryTime: undefined,
          deliveryPlace: undefined,
          parameterGroupList:[],
          attributeList:[],
          productImages:[],
          specificationList:[],
          productFreights:[],
          relationProducts:[], //关联商品
          productDelivery: 'all', //配送区域，all为全国，area指定地区,station指定站点。 这里固定为全国,

        },
        //所在省份参数
        areaStatus: false,
        options: [],
        //状态参数
        areaData: [],
        propertyIds: undefined,
        //图片参数
        fileList: [],
        upLoadData: {
          upfile: null
        },
        fileHeaders: {
          "X-Token": getToken()
        },
        curPropertyNumber: 0,
        propertyPrice: [],
        subPriceArrayStack: [],

        priceExts: [],
        multipleSelection: [],
        tableData: [],
        key: 1, // table key
        specificationIds: [], // specificationIds
//        formThead: defaultFormThead, // 默认表头 Default header
        formThead: [], // 默认表头 Default header
        selectedLable: "",
        selectedValue: "",
        brandOptions: [],
        parameterGroups:[],
        attributeOptions: [],
        supervisorProduct: false,
        attributeOptions: [],
        groupStatusList: [{
          name:"unstart",
          value: 0,
          desc: '未开始'
        }, {
          name:"going",
          value: 1,
          desc: '进行中'
        }, {
          name:"end",
          value: 2,
          desc: '已结束'
        }]
      };
    },
    created() {
      this.getShopData();
      this.getGoodsCategoryData();
      this.getFreightTemplate();
      this.getAddProductInitInfo() //获取店铺名和tags
      //获取基本数据
//    this.fetchData();
      this.handleCreate();
      this.fetchProductCategoryTree() //获取商品分类
      this.fetchBrandAllList() //获取品牌下拉框


    },
    mounted() {
    },
    methods: {
      changeProductCategory(id){
        //获取商品分类下的商品参数
        this.loadParameters(id);
        //获取商品分类下的商品属性
        this.loadAttributes(id)
        //获取商品规格初始化数据
        this.loadSpecification(id)
      },
      loadParameters(productCategoryId){
          this.parameterGroups = [];
          loadParameters({id:productCategoryId}).then((response)=>{
          if(response.data.result == "00000000"){
              this.parameterGroups = response.data.data;
          }
        }).catch((e)=>{
          console.log(e)
        })
      },
      loadAttributes(productCategoryId){
        this.attributeOptions = [];
        loadAttributes({id:productCategoryId}).then((response)=>{
          if(response.data.result == "00000000"){
            this.attributeOptions = response.data.data;
          }
        }).catch((e)=>{
          console.log(e)
        })
      },
      loadSpecification(productCategoryId){
        this.specificationNames = []
//        this.SpecificationProduct = {}
        loadSpecification({id:productCategoryId}).then((response)=>{
          if(response.data.result == "00000000"){
            this.specificationNames = response.data.data.specificationNames
//            this.SpecificationProduct = response.data.data.SpecificationProduct
          }
        }).catch((e)=>{
          console.log(e)
        })
      },
      handleCrtImgUp() {
        var imgObj = {
          title: null,
          source: null,
          order:0
        };
        this.pushData.productImages.push(imgObj);
        setTimeout(() => {
          this.$refs.ImgTable.setCurrentRow(imgObj);
        }, 10); //<==用于延时渲染后选中这行
      },
      imageSuccessCBK(path,row) {
        console.log(row)
        console.log("path:" + path);
        row.source= path;
        console.log(this.pushData.productImages)
      },
      delImg(index, r) {
        console.log(index, r);
        this.pushData.productImages.splice(index, 1);
      },
      selectGet(vId) {
        console.log(vId);

      },
      //添加规格
      handleCreateSpecificationProduct() {
        const cid = this.pushData.productCategory.id;
        var SpecificationProduct = {}
        var token = getToken();
        console.log("token:" +token);
        $.ajax({
          url: process.env.BASE_API + '/api/product/specification',
          type:'GET', //GET
          async:false, //或false,是否异步
          data:{id:cid},
          timeout:5000, //超时时间
          dataType:'json', //返回的数据格式：
          beforeSend: function(request) {
            request.setRequestHeader("Authorization", token);
          },
//          beforeSend:function(xhr){
//            headers:["Authorization", token]
//          },
          success:function(data,textStatus,jqXHR){
            SpecificationProduct = data.data.SpecificationProduct;
          },
          error:function(xhr,textStatus){
            console.log("err:" + textStatus)
          },
          complete:function(){
          }
        })
        if ( this.specificationIds.length == 0) {
          Message({
            message: "请先选择需要规格",
            type: "error",
            duration: 1 * 1000
          });
          return;
        }
        this.tableData.push(SpecificationProduct);
        setTimeout(() => {
          this.$refs.SpecificationProductTable.setCurrentRow(
            SpecificationProduct
          );
        }, 10); //<==用于延时渲染后选中这行
      },

      delSpecificationProductDataChild(index, r) {
        this.tableData.splice(index, 1);
      },
      handleCheckedspecificationNamesChange(label, value) {
        console.log(label, value);
        this.selectedLable = label;
        this.selectedValue = value;
      },
      handleCheckedfreightChange(value) {
        if (value) {
          console.log("我被选中了");
          this.btnFreightShow = false;
        } else {
          this.btnFreightShow = true;
        }
      },
      handleCreateFreight() {
//        this.FreightdialogListVisible = true;
        var objFreight = {
          minWeight: 0,
          maxWeight: 0,
          price: 0
        };
        this.FreightchildList.push(objFreight);
        setTimeout(() => {
          this.$refs.FreightTable.setCurrentRow(objFreight);
        }, 10); //<==用于延时渲染后选中这行
      },
      //选中行
      handleCurrentChange(val) {
        this.currentRow = val;
      },
      //选中checkbox
      handleSelectionChange(val) {
        this.multipleSelection = val;
      },

      delDataChild(index, r) {
        console.log(index, r);
        this.FreightchildList.splice(index, 1);
      },

      handleArea(rule, value, callback) {
        if (!this.areaStatus) {
          callback(new Error("请选择所在地"));
        }
        callback();
      },
      //绑定所在地区
      handleAreaData(val) {
        console.log(val);
        this.pushData.provinceId = val[0];
        this.pushData.cityId = val.length >= 2 ? val[1] : "";
        this.pushData.districtId = val.length === 3 ? val[2] : "";
        this.areaStatus = true;
      },

      //获取省份
      getProvinceData() {
        let data = [];
        this.$http.get("static/data/address.json").then(
          response => {
            console.log("数据加载成功");
            let addressData = response.body.list;
            addressData.forEach((a, i) => {
              data.push(a);
            });
            this.options = data;
          },
          response => {
            console.log("数据加载失败");
          }
        );
      },
      //删除图片
      deletePhotos(item, index) {
        this.pushData.photos.splice(index, 1);
      },
      //获取店铺数据
      getShopData() {

      },
      //获取商品分类列表
      getGoodsCategoryData() {

      },
      //运费模板列表
      getFreightTemplate() {

      },

      //获取基本数据
      fetchData() {
        this.getProvinceData();

      },

      //赋值图片参数
      beforeImgUpload(file) {
        this.upLoadData.upfile = file;
      },
      //表单验证
      handleCreate() {
        this.$nextTick(() => {
          this.$refs["postForm"].clearValidate();
        });
      },
      //保存
      submitForm() {
        this.loading = true
        if(!this.pushData.productCategory.id){
          this.showMsg('提交失败','商品分类必选！','error')
          return false
        }

        this.$refs["postForm"].validate(valid => {
          if (valid) {
            if(!this.pushData.servicePrice){
              this.pushData.servicePrice = 0;
            }
            this.pushData.parameterGroupList = this.parameterGroups;
            this.pushData.attributeList = this.attributeOptions;
            this.pushData.productFreights = this.FreightchildList
            this.pushData.specificationList = this.getSpecificationList()

            createProduct(this.pushData).then((response)=>{
              if(response.data.result == "00000000"){
                this.showMsg('成功','操作成功','success')
                this.$router.push({path:"/product/list"})
              }else{
                this.showMsg('失败',response.data.msg,'error');
              }
            }).catch((e)=>{
              this.loading = false
              console.log(e)
            })
          } else {
            this.showMsg('提交失败','请填写正确信息！','error')
          }
        });
      },
      getSpecificationList() {
        var result = []
        for(var i =0 ; i < this.tableData.length; i++){
          var result1 = []
          for(var j =0 ; j < this.specificationIds.length; j++){
            var temp = {}
            var q = this.specificationIds[j]
            temp.specificationId=this.specificationIds[j]
            temp.specificationValueId=this.tableData[i][q].valueCode
            result1.push(temp)
          }
          result.push(result1)
        }
        return result
        console.log(result)
      },
      //返回商品列表
      closePage() {//返回
        goback(this.$route.path);
      },
      fetchProductCategoryTree() { //获取商品分类选项
        fetchProductCategoryTree().then(response => {
          this.categoryData = response.data.data
        }).catch((e) => {
          console.log(e)
        })
      },
      fetchBrandAllList (){ //获取品牌选项
        fetchBrandAllList().then(response => {
          this.brandOptions = response.data.data
        }).catch((e) => {
          console.log(e)
        })
      },
      showMsg(title,msg,type) {//显示消息 返回商品列表
        this.$notify({title: title, message: msg,type: type, duration: 2000 });
        this.loading = false;
      },
      productTypeChange(val) {
        if(val == 'standard'){
          this.tabFreightShow = true
          this.FreightchildList = []
          this.supervisorProduct = false;
          this.groupShow = false
          this.pushData.groupPrice = 0
          this.pushData.groupQuantity = 0
          this.pushData.beginTime = undefined
          this.pushData.endTime = undefined
          //this.pushData.isRecommend = false
          this.pushData.groupStatus = undefined
          this.pushData.community.id = undefined
        } else {
          this.tabFreightShow = false
          this.FreightchildList = []
          this.supervisorProduct = false;
          this.groupShow = false
          this.pushData.groupPrice = 0
          this.pushData.groupQuantity = 0
          this.pushData.beginTime = undefined
          this.pushData.endTime = undefined
          //this.pushData.isRecommend = false
          this.pushData.groupStatus = undefined
          this.pushData.community.id = undefined
        }
        this.$refs.selectProductsTableRef.productType=val
        this.$refs.selectProductsTableRef.getList()
        this.pushData.relationProducts = []
      },
      selectProducts(){
        this.$refs.selectProductsTableRef.dialogTableVisible=true
      },
      addProducts(products){
        if(this.pushData.relationProducts==null){
          this.pushData.relationProducts = []
        }
        if(null == products) {
          products = []
        }
        let length = products.length
        for(let i=0; i<length; i++){
          let isContain = false
          for(let j=0; j<this.pushData.relationProducts.length; j++){
            if(this.pushData.relationProducts[j].productId === products[i].id){
              isContain = true
              break
            }
          }
          if(!isContain){
            let product = {
//              relation: {id: products[i].id},
              productId: products[i].id,
              productName: products[i].fullName,
              price: products[i].price,
              order: 0
            }
            this.pushData.relationProducts.push(product)
          }
        }
      },
      delProducts(product){
        if(this.pushData.relationProducts==null){
          return
        }
        for(let i=0; i<this.pushData.relationProducts.length; i++){
          if(this.pushData.relationProducts[i].productSn === product.productSn){
            this.pushData.relationProducts.splice(i, 1)
            return
          }
        }
      },
      getAddProductInitInfo() {
        getAddProductInitInfo().then((response)=>{
          if(response.data.result == "00000000"){
            this.storeName = response.data.data.store
            this.tagOptions = response.data.data.tags
            this.productTypes = response.data.data.productTypes
            this.communityList = response.data.data.communityList;
          }
        }).catch((e)=>{
          console.log(e)
        })
      },
      getBeginTime(val) {
        let endTime = this.pushData.endTime;

        if(val === undefined || val === null || val === ""
        || endTime === undefined || endTime === null || endTime === "") {
          this.pushData.groupStatus = undefined;
          return;
        }

        if(endTime < val) {
          this.showMsg('失败','开始日期必须在结束日期之前！','error')
          this.pushData.groupStatus = undefined;
          return;
        }

        let nowDate = new Date().getTime();
        if(val > nowDate) {
          this.pushData.groupStatus = "unstart"
        } else {
          if(endTime >= nowDate) {
            this.pushData.groupStatus = "going"
          } else {
            this.pushData.groupStatus = "end"
          }
        }
      },
      getEndTime(val) {
        let beginTime = this.pushData.beginTime;

        if(val === undefined || val === null || val === ""
        || beginTime === undefined || beginTime === null || beginTime === "") {
          this.pushData.groupStatus = undefined;
          return;
        }

        if(beginTime > val) {
          this.showMsg('失败','结束日期必须在开始日期之后！','error')
          this.pushData.groupStatus = undefined;
          return;
        }

        let nowDate = new Date().getTime();
        if(val < nowDate) {
          this.pushData.groupStatus = "end"
        } else {
          if(beginTime <= nowDate) {
            this.pushData.groupStatus = "going"
          } else {
            this.pushData.groupStatus = "unstart"
          }
        }
      }
    },
    watch: {
      specificationIds(valArr) {
        this.formThead = this.specificationNames;
        this.formThead = this.formThead.filter((aa, index) => {
          //					console.log(valArr.indexOf(aa.label))
          return valArr.indexOf(aa.value) >= 0;
        });
        if (this.formThead.length > 0) {
          this.SpecificationProductShow = true;
        } else {
          this.tableData = [];
        }
        this.key = this.key + 1; // 为了保证table 每次都会重渲
      }
    }
  };
</script>

<style rel="stylesheet/scss" lang="scss">
  @import "src/styles/mixin.scss";
  .filter-container {
    padding-bottom: 10px;

  .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
  }

  }

  #container {
    min-width: 600px;
    min-height: 400px;
  }

  .orange {
    color: #ff892a !important;
  }

  .red {
    color: #dd5a43 !important;
  }

  .createPost-container {
    position: relative;
  .createPost-main-container {
    padding: 40px 45px 20px 50px;
  .postInfo-container {
    position: relative;
  @include clearfix;
    margin-bottom: 10px;
  .postInfo-container-item {
    float: left;
  }
  }
  .editor-container {
    min-height: 500px;
    margin: 0 0 30px;
  .editor-upload-btn-container {
    text-align: right;
    margin-right: 10px;
  .editor-upload-btn {
    display: inline-block;
  }
  }
  }
  }
  .word-counter {
    width: 40px;
    position: absolute;
    right: -10px;
    top: 0px;
  }
  }
  .tab-container{
    margin:30px;
  }
</style>
