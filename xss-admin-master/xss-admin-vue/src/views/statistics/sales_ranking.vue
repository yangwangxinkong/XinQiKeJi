<template>
  <div class="app-container">
    <div class="filter-container">
      <el-button class="filter-item" style="margin: 0 0 3px 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
      <el-select v-model="listQuery.count" placeholder="请选择" style="width: 100px;margin-left: 5px">
        <el-option
          v-for="item in countArr"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>
      <span class="demonstration" style="margin-left: 20px; font-size: small">开始日期:</span>
      <el-date-picker
        v-model="listQuery.beginDate"
        value-format="timestamp"
        type="date"
        placeholder="选择开始日期">
      </el-date-picker>
      <span class="demonstration" style="margin-left: 5px; font-size: small">结束日期:</span>
      <el-date-picker
        v-model="listQuery.endDate"
        value-format="timestamp"
        type="date"
        placeholder="选择结束日期"
        @change="endDateChange">
      </el-date-picker>
      <el-button class="filter-item" style="margin: 1px 0 3px 3px;" @click="handleRefresh" type="primary">确定</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%">
        <el-table-column
          label="排名"
          width="120"
          show-overflow-tooltip>
          <template slot-scope="scope">
            <span>{{scope.$index+1}}</span>
          </template>

        </el-table-column>

      <el-table-column
        label="商品编号"
        width="200"
        prop="1"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        label="商品名称"
        width="500"
        prop="3"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        label="销售量"
        prop="5"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        label="销售额"
        prop="6"
        show-overflow-tooltip>
      </el-table-column>


    </el-table>

  </div>
</template>

<script>
  import waves from '@/directive/waves' // 水波纹指令
  import { fetchList } from '@/api/sales_ranking'

  export default {
    name: 'multipleTable',
    directives: {
      waves
    },
    data() {
      return {
        list: [],
        total: 0,
        listLoading: true,
        listQuery: {
          count: 10,
          beginDate: undefined,
          endDate: undefined,
          searchProperty: undefined,
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },
        countArr:[
          {value:10, label:'10条'},
          {value:20, label:'20条'},
          {value:50, label:'50条'},
          {value:100, label:'100条'},
        ],

      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          this.list = response.data.data;
          console.log(this.list)
          this.total = response.data.total

          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        }).catch((e)=>{
          console.log(e)
          this.listLoading = false
        })
      },
      handleRefresh() {
        this.getList();
      },
      endDateChange(){
        this.listQuery.endDate += 1000*60*60*24-1;
        console.log(this.listQuery.endDate)
      },


    }
  }
</script>
