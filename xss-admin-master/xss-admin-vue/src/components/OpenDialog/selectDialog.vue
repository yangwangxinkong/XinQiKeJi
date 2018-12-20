<template>
  <div class="createPost-container">
    <el-button @click="dialogTableVisible=true" >选择</el-button>
    <el-dialog :title="title" :visible.sync="dialogTableVisible" width="70%">
      <div class="filter-container">
        <el-input @keyup.enter.native="handleFilter" style="width: 300px;" class="filter-item" placeholder="输入关键字搜索" v-model="listQuery.keyword">
        </el-input>
        <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      </div>
      <el-table ref="singleTable" highlight-current-row :data="gridData" @selection-change="selectionChange" :v-loading="loading" @current-change="handleCurrentChange">
        <el-table-column v-if="isSelected" type="selection" width="40"></el-table-column>
        <el-table-column v-for="item in property" :label="item.label" :key="item.propName" :property="item.propName" show-overflow-tooltip></el-table-column>
      </el-table>
      <div class="pagination-container">
        <el-pagination background @size-change="handlePageSizeChange" @current-change="handlePageCurrentChange" :current-page="listQuery.pageNumber"
                       :page-sizes="[5,10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
        </el-pagination>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogTableVisible = false">取 消</el-button>
        <el-button type="primary" @click="selectPrimary()">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 回显列表 -->
    <el-table v-if="isPreview" :data="selectedArr" style="width: 70%" max-height="500" border>
      <el-table-column v-for="item in property" :label="item.label" :key="item.propName" :property="item.propName" show-overflow-tooltip></el-table-column>
      <el-table-column fixed="right" :label="$t('table.actions')" width="90">
        <template slot-scope="scope">
          <el-button @click="deleteItem(scope.row)" type="text" size="small">{{$t('table.delete')}}</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>

</template>
<script>
  export default {
    props: {
      //弹框标题
      title:{
        type: String,
        default: '数据选择列表'
      },
      //表格显示字段数据
      property: {
        type: Array,
        default: function () {
          return []
        }
      },
      //是否多选
      isSelected:{
        type: Boolean,
        default: true
      },
      //是否回显列表
      isPreview:{
        type: Boolean,
        default: false
      },
      //数据查询特殊参数
      params:{
        type: Object,
        default: function () {
          return {}
        }
      },
      //辨别重复数据依据的字段名
      distinguishProp: {
        type: String,
        default: 'id'
      },
      //初始回显数据
      initData: {
        type: Array,
        default: function () {
          return []
        }
      },
    },
    data() {
      return {
        loading: false,
        total: 0,
        gridData: [],
        dialogTableVisible: false,
        searchUrl:'',
        formLabelWidth: '120px',
        memberText:'',
        multipleSelection:[],
        listQuery: {
          pageNumber: 1,
          pageSize: 5,
          keyword: undefined
        },
        selectedArr: [],

      };
    },
    created() {
      this.listQuery = Object.assign(this.listQuery, this.params)
      this.selectedArr = this.initData
      this.getData()
    },
    methods: {
      getData(){
        let listQuery = this.listQuery
        let selectDialog_gridData = []
        let selectDialog_total = 0
        let selectDialog_isInit = false
        this.$emit('queryData', function (queryTableData) {
          queryTableData(listQuery).then(response => {
            selectDialog_gridData = response.data.list
            selectDialog_total = response.data.total
            selectDialog_isInit = true
          })
        })
        let selectDialog_index = setInterval(() => {
          if(selectDialog_isInit){
            this.gridData = selectDialog_gridData
            this.total = selectDialog_total
            clearInterval(selectDialog_index)
          }
        }, 50)
      },
      handleFilter() {
        this.listQuery.pageNumber = 1
        this.getData()
      },
      handlePageSizeChange(val) {
        this.listQuery.pageSize = val
        this.getData()
      },
      handlePageCurrentChange(val) {
        this.listQuery.pageNumber = val
        this.getData()
      },
      handleCurrentChange(val) {
        this.currentRow = val;
      },
      selectionChange(val) {
        this.multipleSelection = val;
      },
      deleteItem(obj){
        for(let i=0; i<this.selectedArr.length; i++){
          if(this.selectedArr[i][this.distinguishProp] === obj[this.distinguishProp]){
            this.selectedArr.splice(i, 1);
          }
        }
        this.$emit('successCBK', this.selectedArr);
      },
      add(){
        let length = this.multipleSelection.length
        for(let i=0; i<length; i++){
          let isContain = false
          for(let j=0; j<this.selectedArr.length; j++){
            if(this.selectedArr[j][this.distinguishProp] === this.multipleSelection[i][this.distinguishProp]){
              isContain = true
              break
            }
          }
          if(!isContain){
            this.selectedArr.push(this.multipleSelection[i])
          }
        }
      },
      selectPrimary(){
        if(!this.isSelected){
          this.selectedArr = [this.currentRow]
        }else{
          this.add()
        }
        this.$emit('successCBK', this.selectedArr);
        this.dialogTableVisible = false
      },
    }
  };
</script>
