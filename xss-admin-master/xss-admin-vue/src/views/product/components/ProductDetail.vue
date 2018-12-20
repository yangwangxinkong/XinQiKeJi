<template>
  <div class="app-container">
    <el-form :rules="rules" ref="addEditPopForm" :model="pushData" :label-position="labelPosition" label-width="100px">
      <el-tabs type="border-card">
        <el-tab-pane>
          <span slot="label"><i class="el-icon-date"></i> 基本信息</span>
          <el-form-item label="所属店铺" prop="shopId">
            <el-col :span="5" class="orange">
              <el-select style="width: 100%" class="filter-item" v-model="pushData.shopId" placeholder="不选择店铺">
                <el-option v-for="item in shopData" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-col>
          </el-form-item>
          <el-form-item label="商品分类" prop="categoryId">
            <el-col :span="5" class="orange">
              <el-select style="width: 100%" class="filter-item" v-model="pushData.categoryId" placeholder="商品分类">
                <el-option-group
                  v-for="group in categoryData"
                  :key="group.label"
                  :label="group.label">
                  <el-option
                    v-for="item in group.options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                  </el-option>
                </el-option-group>
              </el-select>
            </el-col>
          </el-form-item>
          <el-form-item label="商品名称" prop="name">
            <el-col :span="15" class="orange">
              <el-input v-model="pushData.name" clearable @keyup.enter.native="handleCreateSave"></el-input>
            </el-col>
          </el-form-item>
          <el-form-item label="商品状态" prop="status">
            <el-col :span="5">
              <el-select style="width: 100%" class="filter-item" v-model="pushData.status" placeholder="商品状态">
                <el-option label="上架" value="0"></el-option>
                <el-option label="下架" value="1"></el-option>
                <el-option label="置顶" value="2"></el-option>
                <el-option label="为赠品" value="3"></el-option>

              </el-select>
            </el-col>
          </el-form-item>
          <el-form-item label="原价" prop="originalPrice">
            <el-col :span="5">
              <el-input-number v-model="pushData.originalPrice" clearable @keyup.enter.native="handleCreateSave" :min="0" label="请输入原价" style="width: 100%"></el-input-number>
            </el-col>
          </el-form-item>
          <el-form-item label="现价" prop="minPrice">
            <el-col :span="5">
              <el-input-number v-model="pushData.minPrice" clearable @keyup.enter.native="handleCreateSave" :min="0" label="请输入现价" style="width: 100%"></el-input-number>
            </el-col>
          </el-form-item>

          <el-form-item label="库存数" prop="stores">
            <el-col :span="5">
              <el-input-number v-model="pushData.stores" clearable @keyup.enter.native="handleCreateSave" :min="0" label="请输入库存数" style="width: 100%"></el-input-number>
            </el-col>
          </el-form-item>
          <el-form-item label="商品重量" prop="weight">
            <el-col :span="5">
              <el-input-number v-model="pushData.weight" clearable @keyup.enter.native="handleCreateSave" :min="0" label="请输入商品重量" style="width: 100%"></el-input-number>
            </el-col>
            <el-col :span="1">
              &nbsp;&nbsp;KG
            </el-col>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="运费策略">
          <el-form-item label="运费策略" prop="freight">
            <el-col :span="3" class="orange">
              <el-checkbox-group v-model="pushData.freight" size="medium" @change="handleCheckedfreightChange">
                <el-checkbox border label="卖家承担运费" name="type"></el-checkbox>
              </el-checkbox-group>
            </el-col>
            <el-button class="filter-item" v-if="btnFreightShow" @click="handleCreateFreight" type="success">添加运费</el-button>
          </el-form-item>
          <el-form-item label="地区选择" prop="distributionarea">
            <el-cascader :options="options" v-model="areaData" @change="handleAreaData" style="width:300px">
            </el-cascader>
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="商品介绍">
          <el-form-item label="详细介绍" prop="content">
            <Tinymce :height=500 ref="editor" v-model="pushData.content" />
          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="商品图片">
          <el-form-item label="商品图片" prop="photos">
            <el-col :span="3" class="orange">
              <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" list-type="picture" :data="upLoadData" :headers="fileHeaders" :on-success="uploadSuccess" :file-list="fileList" accept="image/jpeg,.jpg,image/gif,.gif,image/png,.png,image/bmp,.bmp,.jpeg,.JPG,.JPEG,.PBG,.GIF,.BMP,.JPEG" :before-upload="beforeImgUpload">
                <el-button size="small" type="primary">[选择本地文件]</el-button>
              </el-upload>
            </el-col>
            <ul class="el-upload-list el-upload-list--picture-card">
              <draggable :list="pushData.photos">
                <li v-for="(item, index) in pushData.photos" :key="index" tabindex="0" class="el-upload-list__item is-success">
                  <img :src="item.pic" alt="" class="el-upload-list__item-thumbnail" draggable="false">
                  <span class="el-upload-list__item-actions">
                  <span class="el-upload-list__item-delete">
                      <i @click="deletePhotos(item,index)" class="el-icon-delete"></i>
                  </span>
							</span>
                </li>
              </draggable>
            </ul>

          </el-form-item>
        </el-tab-pane>
        <el-tab-pane label="商品参数">商品参数</el-tab-pane>
        <el-tab-pane label="商品属性">商品属性</el-tab-pane>
        <el-tab-pane label="商品规格">
          <el-form-item label="规格与尺寸">
            <div class="filter-container">
              <el-checkbox-group v-model="checkboxVal">
                <el-checkbox v-for="(specificationIds,index) in specificationNames" :label="specificationIds.label" :key="specificationIds.value" @change="handleCheckedspecificationNamesChange(specificationIds.label,specificationIds.value)">{{specificationIds.label}}</el-checkbox>
              </el-checkbox-group>
              <el-button class="filter-item " @click="handleCreateSpecificationProduct" type="success">添加规格</el-button>
            </div>

            <el-table :data="tableData" :key='key' border fit highlight-current-row style="width: 100%" ref="SpecificationProductTable" >
              <el-table-column prop="name" label="规格" width="180"></el-table-column>
              <el-table-column :key='headItem.value' v-for='headItem in formThead' :label="headItem.label">
                <template slot-scope="scope">
                  <el-select style="width: 100%" class="filter-item" v-model="scope.row[headItem.value].valueCode" placeholder="请选择" @change="selectGet">
                    <el-option v-for="item in scope.row[headItem.value].originList" :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </template>

              </el-table-column>
              <el-table-column label="操作" v-if="SpecificationProductShow">
                <template slot-scope="scope">
                  <el-button size="mini" type="danger" @click="delSpecificationProductDataChild(scope.$index, scope.row)">删除</el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-tab-pane>
      </el-tabs>


    </el-form>
    <div slot="footer" class="dialog-footer" style="margin-top:30px">
      <el-button type="primary" @click="handleCreateSave">确定</el-button>
      <el-button @click="goBack();">返回</el-button>
    </div>
    <!-- 新增运费模板列表弹窗 -->
    <el-dialog title="新增运费" :visible.sync="FreightdialogListVisible" :close-on-click-modal="false" :close-on-press-escape="false">
      <div class="filter-container">
        <el-button class="filter-item" @click="handleCreateChild" type="success" icon="el-icon-edit">添加</el-button>
        <el-button class="filter-item" type="primary" icon="el-icon-delete" @click="delDataMore">删除</el-button>
      </div>
      <el-table :data="FreightchildList" class="el-tb-edit" v-loading="listLoading" element-loading-text="Loading" ref="FreightTable" border fit highlight-current-row empty-text="暂未添加运费模板" @current-change="handleCurrentChange" @selection-change="handleSelectionChange">
        <el-table-column type="selection" align="center" width="55" row-key="id"></el-table-column>
        <el-table-column prop="FreightstratNum" label="起始件数">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.FreightstratNum" size="mini" controls-position="right" :min="0"></el-input-number>
            <span>{{scope.row.FreightstratNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="FreightendNum" label="结束件数">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.FreightendNum" size="mini" controls-position="right" :min="0"></el-input-number>
            <span>{{scope.row.FreightendNum}}</span>
          </template>
        </el-table-column>
        <el-table-column prop="FreightPrice" label="运费单价">
          <template slot-scope="scope">
            <el-input-number v-model="scope.row.FreightPrice" size="mini" controls-position="right" :min="0"></el-input-number>
            <span>{{scope.row.FreightPrice}}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="danger" @click="delDataChild(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

  </div>
</template>

<script>
  const defaultFormThead = [
    {
      label: "操作",
      value: "caozuo"
    }
  ];
  import { Message } from "element-ui";
  import { getToken } from "@/utils/auth";
  import draggable from "vuedraggable";
  import Tinymce from "@/components/Tinymce";

  export default {
    computed: {
    },
    data() {
      return {
        btnFreightShow: false,
        FreightdialogListVisible: false,
        FreightchildList: [],
        currentRow: null,
        listLoading: false,
        categoryData: [
          {
            label: '花椒产地',
            options: [{
              value: 'sichuanhuajiao',
              label: '四川花椒'
            }, {
              value: 'chongqinhuajiao',
              label: '重庆花椒'
            }]
          },
          {
            label: '花椒产品',
            options: [{
              value: 'sichuanhuajiao',
              label: '花椒粉'
            }, {
              value: 'chongqinhuajiao',
              label: '盐椒'
            }]
          }
        ],
        shopData: [
          {
            label: "不选择店铺",
            value: 0
          }
        ],
        labelPosition: "right",
        specificationNames: [
          {
            label: "产地",
            value: "diqu"
          },
          {
            label: "等级",
            value: "dengji"
          },
          {
            label: "颜色",
            value: "yanse"
          }
        ],
        SpecificationProductShow: false,
        //表单验证
        rules: {
          categoryId: [
            {
              required: true,
              message: "请选择商品分类",
              trigger: "change"
            }
          ],
          name: [
            {
              required: true,
              message: "请输入店铺名称",
              trigger: "blur"
            }
          ],
          freight: [
            {
              required: true,
              message: "请选择运费策略",
              trigger: "change"
            }
          ],
          distributionarea: [
            {
              message: "请选择所在地",
              trigger: "blur"
            },
            {
              validator: this.handleArea,
              trigger: "change"
            }
          ],
          photos: [
            {
              required: true,
              message: "请上传商品图片",
              trigger: "blur"
            }
          ],
          content: [
            {
              required: true,
              message: "请输入详细介绍",
              trigger: "blur"
            }
          ],
          status: [
            {
              required: true,
              message: "请选择商品状态",
              trigger: "blur"
            }
          ],
          originalPrice: [
            {
              required: true,
              message: "请输入商品原价",
              trigger: "blur"
            }
          ],
          minPrice: [
            {
              required: true,
              message: "请输入商品现价",
              trigger: "blur"
            }
          ],
          stores: [
            {
              required: true,
              message: "请输入总库存数",
              trigger: "blur"
            }
          ]
        },

        pushData: {
          id: undefined,
          shopId: 0,
          categoryId: undefined,
          freight: undefined,
          provinceId: undefined,
          cityId: undefined,
          districtId: undefined,
          name: undefined,
          status: "0",
          originalPrice: 0,
          minPrice: 0,
          stores: 0,
          weight: 0,
          commission: 0,
          pics: [],
          photos: [],
          content: undefined,
          dateAddStr: undefined,
          originid: undefined,
          tableData: []
        },

        //   listLoading: true,
        //所在省份参数
        areaStatus: false,
        options: [],
        props: {
          value: "id",
          label: "name",
          children: "cities"
        },
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
        checkboxVal: defaultFormThead, // checkboxVal
        formThead: defaultFormThead, // 默认表头 Default header
        selectedLable: "",
        selectedValue: ""
      };
    },
    components: {
      draggable,
      Tinymce
    },
    created() {
      this.getShopData();
      this.getGoodsCategoryData();
      this.getFreightTemplate();

      //获取基本数据
      this.fetchData();

      this.handleCreate();
    },
    mounted() {},
    methods: {
      selectGet(vId) {
        console.log(vId);

      },
      //添加规格
      handleCreateSpecificationProduct() {
        var SpecificationProduct = {
          diqu: {
            valueText: "四川茂县4",
            valueCode:'001',
            originList: [
              {
                label: "四川茂县4",
                value: "001"
              },
              {
                label: "四川汉源5",
                value: "002"
              }
            ]
          },
          dengji: {
            valueText: "等级一",
            valueCode:'003',
            originList: [
              {
                label: "等级一",
                value: "003"
              },
              {
                label: "等级二",
                value: "004"
              }
            ]
          },
          yanse: {
            valueText: "绿色",
            valueCode:'006',
            originList: [
              {
                label: "红色",
                value: "005"
              },
              {
                label: "绿色",
                value: "006"
              }
            ]
          }
        };
        if (this.checkboxVal[0].label == "操作" && this.checkboxVal.length == 1) {
          Message({
            message: "请先选择需要规格尺寸",
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
          this.btnFreightShow = true;
        } else {
          this.btnFreightShow = false;
        }
      },
      handleCreateFreight() {
        this.FreightdialogListVisible = true;
      },
      handleCreateChild() {
        var objFreight = {
          FreightstratNum: 0,
          FreightendNum: 0,
          FreightPrice: 0
        };
        this.FreightchildList.push(objFreight);
        setTimeout(() => {
          this.$refs.FreightTable.setCurrentRow(objFreight);
        }, 10); //<==用于延时渲染后选中这行
      },

      //批量删除运费模板
      delDataMore() {
        if (!this.multipleSelection.length) {
          Message({
            message: "请先选择需要删除的数据",
            type: "error",
            duration: 1 * 1000
          });
          return;
        }
        this.$confirm("删除无法恢复, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.multipleSelection.forEach((obj, index) => {
              this.FreightchildList.splice(obj[index], 1);
            });
          })
          .catch(() => {});
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
      //上传图片成功
      uploadSuccess(res) {
        // if (res.code !== 0) {
        //     console.log("--")
        //     Message({
        //         message: res.msg,
        //         type: 'error',
        //         duration: 3 * 1000
        //     })
        // } else {
        //     this.pushData.photos.push({
        //         id: res.data.name,
        //         name: `店铺图片${res.data.type}`,
        //         pic: res.data.url
        //     });
        //     this.fileList = [];
        //     this.handleCreate();
        // }
      },
      //表单验证
      handleCreate() {
        this.$nextTick(() => {
          this.$refs["addEditPopForm"].clearValidate();
        });
      },
      //保存
      handleCreateSave() {
        this.$refs["addEditPopForm"].validate(valid => {
          if (valid) {
            this.pushData.detailsJsonStr = JSON.stringify(this.detailsJsonStr);
            let photos = [];
            this.pushData.photos.forEach(a => {
              photos.push(a.pic);
            });
            this.pushData.pic = photos[0];
            this.pushData.pics = photos.toString();

          } else {
            Message({
              message: "操作失败,请填写正确信息",
              type: "error",
              duration: 3 * 1000
            });
          }
        });
      },
      //返回商品列表
      goBack() {
        this.$router.push({
          path: "/user/apiExtShopGoods/list"
        });
      }
    },
    watch: {
      checkboxVal(valArr) {
        this.formThead = this.specificationNames;
        this.formThead = this.formThead.filter((aa, index) => {
          //					console.log(valArr.indexOf(aa.label))
          return valArr.indexOf(aa.label) >= 0;
        });

        if (this.formThead.length > 0) {
          this.SpecificationProductShow = true;
          this.checkboxVal.forEach(item => {
            if (item == "操作") {
              this.checkboxVal.splice(item, 1);
            }
          });
        } else {
          this.tableData = [];
          this.checkboxVal = defaultFormThead;
        }
        this.key = this.key + 1; // 为了保证table 每次都会重渲
      }
    }
  };
</script>

<style rel="stylesheet/scss" lang="scss">
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
  /* 可编辑表格 */

  .el-tb-edit .el-input,
  .el-tb-edit .el-input-number,
  .el-tb-edit .el-select {
    display: none;
    width: 100%;
  }

  .el-tb-edit .current-row .el-input,
  .el-tb-edit .current-row .el-input-number,
  .el-tb-edit .current-row .el-select {
    display: inherit;
  }

  .el-tb-edit .current-row .el-input + span,
  .el-tb-edit .current-row .el-input-number + span,
  .el-tb-edit .current-row .el-select + span {
    display: none;
  }
</style>
