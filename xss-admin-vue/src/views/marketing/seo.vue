<template>
  <div class="app-container">

    <div class="filter-container">
      <el-button type="primary" icon="el-icon-refresh" @click="refresh">{{$t('refresh')}}</el-button>
    </div>

    <el-table :data="tableData" ref="seoTable" v-loading="listLoading" @sort-change="sort" style="width: 100%" fit highlight-current-row>
      <el-table-column prop="typeDesc" :label="$t('seo.type')" sortable show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="title" :label="$t('seo.title')" sortable show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="keywords" :label="$t('seo.keywords')" sortable show-overflow-tooltip>
      </el-table-column>
      <el-table-column prop="description" :label="$t('seo.describe')" sortable show-overflow-tooltip>
      </el-table-column>
      <el-table-column fixed="right" :label="$t('seo.operation')">
        <template slot-scope="scope">
          <el-button @click="editSeo(scope.row.id)" type="primary" size="mini">{{$t('table.edit')}}</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="pageSizeChange" @current-change="pageCurrentChange" :current-page="listQuery.pageNumber" :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <el-dialog :title="$t('seo.editTitle')" :visible.sync="seoEditFormVisible">
      <el-form :rules="rules" ref="seoEditForm" :model="seo" label-position="right" label-width="120px">
        <el-form-item :label="$t('seo.type')+':'" prop="typeDesc">
          <span>{{seo.typeDesc}}</span>
        </el-form-item>

        <el-form-item :label="$t('seo.title')+':'" prop="title">
          <el-input v-model="seo.title" style="width: 350px;" @focus="seoLabButtonsShow(0)" :title="$t('seo.keywordsTitle')" :placeholder="$t('required')+','+$t('seo.keywordsTitle')"></el-input>
        </el-form-item>

        <!-- 循环当前SEO的标签按钮 -->
        <el-form-item prop="title" style="margin-top: -20px; width: 500px" v-show="seoTagButtonsIsShow[0]">
          <el-button v-for="item in currentSeoTags" size="mini" :key="item.type" @click="addTagContent(item,'title')" class="seoLabButton">{{$t(item.name)}}</el-button>
        </el-form-item>


        <el-form-item :label="$t('seo.keywords')+':'" prop="keywords">
          <el-input v-model="seo.keywords" style="width: 350px;" @focus="seoLabButtonsShow(1)" :title="$t('seo.keywordsTitle')" :placeholder="$t('required')+','+$t('seo.keywordsTitle')"></el-input>
        </el-form-item>

        <!-- 循环当前SEO的标签按钮 -->
        <el-form-item prop="title" style="margin-top: -20px; width: 500px" v-show="seoTagButtonsIsShow[1]">
          <el-button v-for="item in currentSeoTags" size="mini" :key="item.type" @click="addTagContent(item,'keywords')" class="seoLabButton">{{$t(item.name)}}</el-button>
        </el-form-item>


        <el-form-item :label="$t('seo.describe')+':'" prop="description">
          <el-input v-model="seo.description" style="width: 350px;" @focus="seoLabButtonsShow(2)" :title="$t('seo.keywordsTitle')" :placeholder="$t('required')+','+$t('seo.keywordsTitle')"></el-input>
        </el-form-item>

        <!-- 循环当前SEO的标签按钮 -->
        <el-form-item prop="title" style="margin-top: -20px; width: 500px" v-show="seoTagButtonsIsShow[2]">
          <el-button v-for="item in currentSeoTags" size="mini" :key="item.type" @click="addTagContent(item,'description')" class="seoLabButton">{{$t(item.name)}}</el-button>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="seoTagButtonsIsShow = [false,false,false]; seoEditFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button type="primary" @click="updateSeo()" :loading="updateSeoConfirmLoding">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<style>
  .seoLabButton{
    margin-left: 3px;
    margin-right: -5px;
    padding: 5px 7px;
  }
</style>

<script>
  import { fetchList, info, updateSeo } from '@/api/marketing/seo'
  import waves from '@/directive/waves' // 水波纹指令
  import { parseTime } from '@/utils'

  export default {
    name: 'seoTable',
    directives: {
      waves
    },
    data() {
      return {
        listLoading: false,
        total: 1,
        tableData: null,
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          searchProperty: undefined,
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },

        seoEditFormVisible: false,
        seo: {
          id: undefined,
          type: '',
          typeDesc: '',
          title: '',
          keywords: '',
          description: ''
        },
        rules: {
          title: [{required: true, message: 'title is required', trigger: 'blur'}],
          keywords: [{required: true, message: 'keywords is required', trigger: 'blur'}],
          description: [{required: true, message: 'description is required', trigger: 'blur'}]
        },
        updateSeoConfirmLoding: false,

        // 对应三个输入框下面的三个tag按钮区域
        seoTagButtonsIsShow: [false,false,false],

        // 当前seo拥有的tag （当前seo类型特有的tag + 公共的tag，编辑页加载时给该变量赋值）
        currentSeoTags: [],

        // 各seo类型特有的tag （其中的name是用来获取国际化内容的）
        seoTypeTags: [
          {
            type: 'productList',      //商品列表
            tags: [
              {name: 'seo.productCategory.name', value: '${productCategory.name}'}
            ]
          },
          {
            type: 'productSearch',    //商品搜索
            tags: [
              {name: 'seo.productKeyword', value: '${productKeyword}'}
            ]
          },
          {
            type: 'productContent',   //商品页
            tags: [
              {name: 'seo.product.name', value: '${product.name}'},
              {name: 'seo.product.fullName', value: '${product.fullName}'},
              {name: 'seo.product.sn', value: '${product.sn}'},
              {name: 'seo.product.productCategory.name', value: '${product.productCategory.name}'}
            ]
          },
          {
            type: 'articleList',      //文章列表
            tags: [
              {name: 'seo.articleCategory.name', value: '${articleCategory.name}'}
            ]
          },
          {
            type: 'articleSearch',    //文章搜索
            tags: [
              {name: 'seo.articleKeyword', value: '${articleKeyword}'}
            ]
          },
          {
            type: 'articleContent',   //文章页
            tags: [
              {name: 'seo.article.title', value: '${article.title}'},
              {name: 'seo.article.author', value: '${article.author}'},
              {name: 'seo.article.pageNumber', value: '${article.pageNumber}'},
              {name: 'seo.article.articleCategory.name', value: '${article.articleCategory.name}'}
            ]
          },
          {
            type: 'brandContent',     //品牌页
            tags: [
              {name: 'seo.brand.name', value: '${brand.name}'}
            ]
          }
        ],
        // 公共的tag （其中的name是用来获取国际化内容的）
        publicTags: [
          {name: 'seo.setting.siteName', value: '${setting.siteName}'},
          {name: 'seo.setting.siteUrl', value: '${setting.siteUrl}'},
          {name: 'seo.setting.address', value: '${setting.address}'},
          {name: 'seo.setting.phone', value: '${setting.phone}'},
          {name: 'seo.setting.zipCode', value: '${setting.zipCode}'},
          {name: 'seo.setting.email', value: '${setting.email}'}
        ]


      }
    },


    created(){
      this.getList()
    },


    methods: {

      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          this.tableData = response.data.list
          this.total = response.data.total

          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        }).catch(e => {
          console.log(e)
          this.listLoading = false
        })
      },


      editSeo(seoId){
        info(seoId).then(response => {
          this.seo = response.data.data.seo

          // 先把公用tag赋值给currentSeoTags
          this.currentSeoTags = this.publicTags;

          // 获取到当前seo对应类型的tags，并加到currentSeoTags数组中
          for(var i=0; i<this.seoTypeTags.length; i++){
            if(this.seoTypeTags[i].type === this.seo.type){
              this.currentSeoTags = this.currentSeoTags.concat(this.seoTypeTags[i].tags)
            }
          }
          console.log(this.currentSeoTags)
        })
        this.seoEditFormVisible = true
        this.$nextTick(() => {
          this.$refs['seoEditForm'].clearValidate()
        })
      },


      updateSeo(){
        this.$refs['seoEditForm'].validate((valid) => {
          if (valid) {
            this.updateSeoConfirmLoding = true
            updateSeo(this.seo).then(() => {
              for (const v of this.tableData) {
                if (v.id === this.seo.id) {
                  const index = this.tableData.indexOf(v)
                  this.tableData.splice(index, 1, this.seo)
                  break
                }
              }
              this.updateSeoConfirmLoding = false
              this.seoTagButtonsIsShow = [false, false, false]
              this.seoEditFormVisible = false
              this.$notify({
                title: '成功',
                message: '更新成功',
                type: 'success',
                duration: 2000
              })
            })
          }
        })
      },


      pageSizeChange(val) {
        this.listQuery.pageSize = val
        this.getList()
      },
      pageCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getList()
      },

      // 排序
      sort(sort){
        if(sort && sort.column){
          if(sort.column.order === 'descending'){
            this.listQuery.orderDirection = 'desc'
          }else if(sort.column.order === 'ascending'){
            this.listQuery.orderDirection = 'asc'
          }

          if(sort.column.property === 'typeDesc'){
            this.listQuery.orderProperty = 'type'
          }else {
            this.listQuery.orderProperty = sort.column.property
          }
        }else {
          this.listQuery.orderProperty = undefined
          this.listQuery.orderDirection = undefined
        }

        this.getList()
      },

      refresh(){
        this.listQuery.orderProperty = undefined
        this.listQuery.orderDirection = undefined
        this.listQuery.pageNumber = 1
        this.listQuery.pageSize = 10
        this.$refs['seoTable'].clearSort()
        this.getList()
      },

      seoLabButtonsShow(index){
        if(typeof index == 'number' && !this.seoTagButtonsIsShow[index]){
          this.seoTagButtonsIsShow = [false,false,false]
          this.seoTagButtonsIsShow[index] = true
        }
        console.log(index)
      },

      addTagContent(tag, text){
        if(text === 'title'){
          this.seo.title = this.seo.title + tag.value
        }else if(text === 'keywords'){
          this.seo.keywords = this.seo.keywords + tag.value
        }else if(text === 'description'){
          this.seo.description = this.seo.description + tag.value
        }

      }


    }


  }

</script>
