<template>
  <div class="app-container">

    <div class="filter-container">
      <el-select v-model="listQuery.shippingStatusId" clearable :placeholder="$t('order.orderSelect')" class="filter-item">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
      </el-select>

      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" :placeholder="$t('orderCancelApply.sn')" v-model="listQuery.sn">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">{{$t('table.refresh')}}</el-button>
    </div>

    <el-table :data="returnApplyList" v-loading="listLoading" fit highlight-current-row style="width: 100%">

      <el-table-column min-width="100px" align="center" :label="$t('returnApply.sn')">
        <template slot-scope="scope">
          <span>{{scope.row.sn}}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="80px" align="center" :label="$t('returnApply.shippingStatus')">
        <template slot-scope="scope">
          <span>{{scope.row.shippingStatus}}</span>
        </template>
      </el-table-column>

      <el-table-column min-width="100px" align="center" :label="$t('returnApply.createDate')">
        <template slot-scope="scope">
          <span>{{scope.row.createDate}}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" :label="$t('returnApply.actions')" min-width="100px">
        <template slot-scope="scope">
          <!--<router-link :to="'view/'+scope.row.id">-->
          <router-link :to="{path: '/order/returns_apply/view', query:{sn:scope.row.orderSn, isEdit:true}}" v-if="scope.row.shippingStatusId == 4">
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('returnApply.handle')}}</el-button>
          </router-link>

          <router-link :to="{path: '/order/returns_apply/view', query:{sn:scope.row.orderSn, isEdit:false}}" v-else>
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('returnApply.view')}}</el-button>
          </router-link>
          <!--<router-link :to="'view/'+scope.row.orderSn" v-if="scope.row.shippingStatusId == 4">
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('returnApply.handle')}}</el-button>
          </router-link>

          <router-link :to="'view/'+scope.row.orderSn" v-else>
            <el-button type="primary" size="small" icon="el-icon-edit">{{$t('returnApply.view')}}</el-button>
          </router-link>-->
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNumber"
                     :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

  </div>
</template>

<script>
  import { fetchList } from '@/api/returnApply'
  import waves from "@/directive/waves"; // 水波纹指令

  export default {
    name: 'returnApplyList',
    directives: {
      waves
    },
    data() {
    return {
      options: [{
        value: '4',
        label: '申请退货'
      }, {
        value: '6',
        label: '部分退货'
      }, {
        value: '8',
        label: '退货驳回'
      }, {
        value: '7',
        label: '已退货'
      }],
      returnApplyList: null,
      total: 0,
      listLoading: true,
      listQuery: {
        pageNumber: 1,
        pageSize: 10,
        sn: undefined,
        shippingStatusId: undefined,
        searchProperty: undefined,
        searchValue: undefined,
        orderProperty: undefined,
        orderDirection: undefined
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(response => {
        this.returnApplyList = response.data.list;
        this.total = response.data.total;
        setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
      //console.log(response);
      //console.log(this.paymentList);
    }).catch((e)=>{
          console.log(e)
          this.listLoading = false
        })
  },
  handleRefresh() {
    this.getList();
  },
  handleFilter() {
    this.listQuery.pageNumber = 1
    this.getList()
  },
  handleSizeChange(val) {
    this.listQuery.pageSize = val
    this.getList()
  },
  handleCurrentChange(val) {
    this.listQuery.pageNumber = val
    this.getList()
  },
  sortChange(sort) {
    if(sort && sort.column){
      if(sort.column.order === 'descending'){
        this.listQuery.orderDirection = 'desc'
      }else if(sort.column.order === 'ascending'){
        this.listQuery.orderDirection = 'asc'
      }
      this.listQuery.orderProperty = sort.column.property
    }else {
      this.listQuery.orderProperty = undefined
      this.listQuery.orderDirection = undefined
    }
    this.getList()
  }
  }
  }
</script>

<style scoped>
  .edit-input {
    padding-right: 100px;
  }
  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 10px;
  }
</style>

<style scoped>
  .edit-input {
    padding-right: 100px;
  }
  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 10px;
  }
</style>
