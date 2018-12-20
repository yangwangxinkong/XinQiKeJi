<template>
  <div class='chart-container'>
    <sticky :className="'sub-navbar'">
      <el-button  @click="back" >返回</el-button>
      <el-button  style="margin-left: 10px;" type="success" @click="submitForm" >保存
      </el-button>
    </sticky>
    <el-form class="form-container" label-width="100px" :inline="true" ref="postForm" style="padding-top: 10px">
      <el-form-item labelWidth="100px" label="统计类型：" prop="type">
        <el-select class="filter-item" v-model="postForm.type" style="width:150px" @change="selectChange">
          <el-option v-for="item in  types" :key="item" :label="$t('statistics.'+item)" :value="item">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="postForm.type == 'month'" label="起始日期：" v-model="postForm.bDate" prop="bMonth" >
        <el-date-picker
          name="bMonth"
          value-format="timestamp"
          v-model="postForm.bDate"
          type="month"
          placeholder="选择月">
        </el-date-picker>
      </el-form-item>
      <el-form-item v-if="postForm.type == 'month'" label="结束日期：" v-model="postForm.eDate" prop="eMonth" >
        <el-date-picker
          name="eMonth"
          value-format="timestamp"
          v-model="postForm.eDate"
          type="month"
          placeholder="选择月">
        </el-date-picker>
      </el-form-item>
      <el-form-item v-if="postForm.type == 'year'" label="起始年份：" v-model="postForm.bDate" prop="bYear" >
        <el-date-picker
          name="bYear"
          value-format="timestamp"
          v-model="postForm.bDate"
          type="year"
          placeholder="选择年">
        </el-date-picker>
      </el-form-item>
      <el-form-item v-if="postForm.type == 'year'" label="结束年份：" v-model="postForm.eDate" prop="eYear" >
        <el-date-picker
          name="eYear"
          value-format="timestamp"
          v-model="postForm.eDate"
          type="year"
          placeholder="选择年">
        </el-date-picker>
      </el-form-item>
      <el-form-item v-if="postForm.type == 'quarter'" label="起始年份：" v-model="postForm.bDate" prop="bQuarter" >
        <el-date-picker
          style="width:150px"
          name="bQuarter"
          value-format="timestamp"
          v-model="postForm.bDate"
          type="year"
          placeholder="选择年">
        </el-date-picker>
      </el-form-item>
      <el-form-item  v-if="postForm.type == 'quarter'" label="起始季度："  prop="beginQuarter">
        <el-select class="filter-item"  v-model="postForm.beginQuarter" style="width:150px">
          <el-option v-for="item in  beginQuarter" :key="item.value" :label="item.name" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item v-if="postForm.type == 'quarter'" label="结束年份：" v-model="postForm.eDate" prop="eQuarter" >
        <el-date-picker
          style="width:150px"
          name="eQuarter"
          value-format="timestamp"
          v-model="postForm.eDate"
          type="year"
          placeholder="选择年">
        </el-date-picker>
      </el-form-item>
      <el-form-item  v-if="postForm.type == 'quarter'" labelWidth="100px" label="结束季度：" prop="endQuarter">
        <el-select class="filter-item" v-model="postForm.endQuarter" style="width:150px">
          <el-option v-for="item in  endQuarter" :key="item.value" :label="item.name" :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <!--<el-button class="filter-item" style="margin-left: 10px;float: right" @click="back" type="primary" >返回</el-button>-->
      <!--<el-button class="filter-item" style="margin-left: 10px;float: right" @click="submitForm"  type="primary" >确定</el-button>-->
    </el-form>
    <div style="width: 1000px;margin: 0 auto;height: 500px">
      <chart height='100%' width='100%'   ref="amountChartData"></chart>
    </div>
    <div style="width: 1000px;margin: 0 auto;height: 500px">
      <Chart2 height='100%' width='100%'ref="volumeChartData"></Chart2>
    </div>
  </div>
</template>

<script>
  import Chart from './components/amountChart'
  import Chart2 from './components/volumeChart'
  import { info , typeList } from '@/api/sales'
  import {goback} from '@/utils/common'
  import Sticky from '@/components/Sticky'

  export default {
    name: 'salesChart',
    components: { Chart,Chart2,Sticky },
    data() {
      return{
        types:[],
        beginQuarter:[{name:'第一季度',value:'1'},{name:'第二季度',value:'2'},{name:'第三季度',value:'3'},{name:'第四季度',value:'4'}],
        endQuarter:[{name:'第一季度',value:'1'},{name:'第二季度',value:'2'},{name:'第三季度',value:'3'},{name:'第四季度',value:'4'}],
        postForm: {
          type:'',
          bDate: "",
          eDate: "",
          beginQuarter:'1',
          endQuarter:'1'
        },
      }
    },
    created(){
      this.getTypeList()
      this.getList()

    },
    methods:{
      getTypeList(){
        typeList().then(response => {
          this.types = response.data.data.types
        })
      },
      getList(){
        info(this.postForm).then(response => {
          if (response.data.result==='00000000') {
            this.postForm.type = response.data.data.type
            this.postForm.bDate = response.data.data.beginDate
            this.postForm.eDate = response.data.data.endDate
            this.$refs.amountChartData.xData=response.data.data.amountX
            this.$refs.amountChartData.vData= response.data.data.amountV
            this.$refs.volumeChartData.xData=response.data.data.volumeX
            this.$refs.volumeChartData.vData= response.data.data.volumeV
            this.$refs.amountChartData.title = this.formatDateMonth(this.postForm.bDate) + '~'+this.formatDateMonth(this.postForm.eDate)+'销售额统计'
            this.$refs.volumeChartData.title = this.formatDateMonth(this.postForm.bDate) + '~'+this.formatDateMonth(this.postForm.eDate)+'销售量统计'
            this.$refs.amountChartData.initChart()
            this.$refs.volumeChartData.initChart()
          }
        })
      },
      submitForm(){
          if(this.postForm.eDate=== ''){
            this.$notify({
              title: '警告',
              message: '结束日期必选',
              type: 'error',
              duration: 2000
            })
          }else if(this.postForm.eDate < this.postForm.bDate){
            this.$notify({
              title: '警告',
              message: '结束日期不可小于起始日期',
              type: 'error',
              duration: 2000
            })
          }else{
            info(this.postForm).then(response => {
              if (response.data.result==='00000000') {
                this.$refs.amountChartData.xData=response.data.data.amountX
                this.$refs.amountChartData.vData= response.data.data.amountV
                this.$refs.volumeChartData.xData=response.data.data.volumeX
                this.$refs.volumeChartData.vData= response.data.data.volumeV
                if(this.postForm.type === 'month'){
                  this.$refs.amountChartData.title = this.formatDateMonth(this.postForm.bDate) + '~'+this.formatDateMonth(this.postForm.eDate)+'销售额统计'
                  this.$refs.volumeChartData.title = this.formatDateMonth(this.postForm.bDate) + '~'+this.formatDateMonth(this.postForm.eDate)+'销售量统计'
                }else{
                  this.$refs.amountChartData.title = this.formatDateYear(this.postForm.bDate) + '~'+this.formatDateYear(this.postForm.eDate)+'销售额统计'
                  this.$refs.volumeChartData.title = this.formatDateYear(this.postForm.bDate) + '~'+this.formatDateYear(this.postForm.eDate)+'销售量统计'
                }
                this.$refs.amountChartData.initChart()
                this.$refs.volumeChartData.initChart()
              }
            })
          }
      },
      back(){
        goback(this.$route.path)
      },
      selectChange(){
        this.postForm.bDate =''
        this.postForm.eDate =''
      },
      formatDateMonth(inputTime) {
        var date = new Date(inputTime);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        return y + '-' + m;
      },
      formatDateYear(inputTime) {
        var date = new Date(inputTime);
        var y = date.getFullYear();
        return y ;
      }
    }
  }
</script>

<style scoped>
  .chart-container{
    position: relative;
    width: 100%;
    height:auto;
  }
</style>

