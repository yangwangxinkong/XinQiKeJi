<template>
  <div class="createPost-container">
    <el-form ref="activityForm" id="activityForm" :model="activity" label-position="right" label-width="100px">
      <sticky :className="'sub-navbar'">
        <el-button @click="back">{{$t('promotion.back')}}</el-button>
        <el-button v-if="activity.status==='auditing'" type="success" @click="auditActivity(0)">审核通过</el-button>
        <el-button v-if="activity.status==='auditing'" type="success" @click="auditActivity(1)">审核驳回</el-button>
        <el-button v-if="activity.status==='auditThroughNotStart'" type="primary" @click="startActivity">开始</el-button>
        <el-button v-if="activity.status==='underway'" type="primary" @click="discontinueActivity">中止</el-button>
        <el-button v-if="activity.status==='underway'" type="primary" @click="endActivity">结束</el-button>
      </sticky>
      <el-tabs v-model="currentTab" style='margin:10px 30px;'>
        <!--基础信息-->
        <el-tab-pane label="基本信息" name="basic">
          <el-form-item label="活动主标题:">
            <span>{{activity.mainTitle}}</span>
          </el-form-item>
          <el-form-item label="活动副标题:">
            <span>{{activity.subTitle}}</span>
          </el-form-item>
          <el-form-item label="活动时间:">
            <span>{{activity.beginAndEndDate[0] | parseTime('{y}-{m}-{d} {h}:{i}:{s}')}} - {{activity.beginAndEndDate[1] | parseTime('{y}-{m}-{d} {h}:{i}:{s}')}}</span>
          </el-form-item>
          <el-form-item label="活动url:">
            <span>{{activity.url}}</span>
          </el-form-item>
          <el-form-item label="PC展示图:">
            <a :href="activity.bannerPc" target="_blank" style="float: left;margin-left: 12px; color: #0a76a4">查看</a>
          </el-form-item>
          <el-form-item label="微信展示图:">
            <a :href="activity.bannerWx" target="_blank" style="float: left;margin-left: 12px; color: #0a76a4">查看</a>
          </el-form-item>
          <el-form-item label="活动类型:">
            <div v-for="type in activityType" :key="type.value">
              <span v-if="type.value===activity.type">{{type.desc}}</span>
            </div>
          </el-form-item>
          <el-form-item label="活动状态:">
            <span v-if="activity.status==='auditing'">待审核</span>
            <span v-if="activity.status==='auditThroughNotStart'">未开始</span>
            <span v-if="activity.status==='auditFail'">审核失败</span>
            <span v-if="activity.status==='underway'">进行中</span>
            <span v-if="activity.status==='discontinue'">已中止</span>
            <span v-if="activity.status==='end'">已结束</span>
          </el-form-item>

          <div v-show="productsShow">
            <el-form-item label="促销商品:">
              <el-table :data="activity.promotionProducts" style="width: 60%" max-height="500">
                <el-table-column width="500" prop="fullName" label="活动商品" show-overflow-tooltip>
                </el-table-column>
                <el-table-column prop="price" label="价格">
                </el-table-column>
              </el-table>
            </el-form-item>
            <el-form-item label="原价:" prop="oldPrice">
              {{activity.oldPrice}}
            </el-form-item>
            <el-form-item label="促销价:" prop="proPrice">
              {{activity.proPrice}}
            </el-form-item>
            <el-form-item label="定金:" prop="downPayment">
              {{activity.downPayment}}
            </el-form-item>
          </div>

          <el-form-item label="报名用户:" v-show="signupUserShow">
            <el-table :data="activity.signupUsers" style="width: 80%" max-height="500" fit>
              <el-table-column prop="name" label="名称" show-overflow-tooltip>
              </el-table-column>
              <el-table-column prop="phone" label="手机号">
              </el-table-column>
              <el-table-column prop="gender" label="性别">
                <template slot-scope="scope">
                  <span v-if="scope.row.gender==='male'">男</span>
                  <span v-if="scope.row.gender==='female'">女</span>
                </template>
              </el-table-column>
              <el-table-column prop="position" label="地址" show-overflow-tooltip>
              </el-table-column>
              <el-table-column prop="describes" label="备注" show-overflow-tooltip>
              </el-table-column>
            </el-table>
          </el-form-item>
        </el-tab-pane>

        <!--介绍-->
        <el-tab-pane label="活动图文" name="introduce">
          <div class="editor-content" v-html="activity.text"></div>
        </el-tab-pane>

      </el-tabs>
    </el-form>

  </div>
</template>

<script>
  import {info, audit,discontinue,start,end} from '@/api/marketing/activity'
  import Tinymce from '@/components/Tinymce'
  import {goback} from "@/utils/common";
  import Sticky from '@/components/Sticky' // 粘性header组件

  export default {
    name: 'activityEdit',
    components: { Tinymce,Sticky },
    data() {
      return {
        currentTab: 'basic',

        activity:{
          id: undefined,
          mainTitle: undefined,
          subTitle: undefined,
          beginAndEndDate: undefined,
          beginDate: undefined,
          endDate: undefined,
          type: undefined,
          status: undefined,
          url: undefined,
          bannerPc: undefined,
          bannerWx: undefined,
          text: undefined,
          products: [],
          signupUsers: []
        },
        activityType: [
          {value:'advertise',desc:'专题活动'},
          {value:'promotion',desc:'促销活动'},
          {value:'signup',desc:'报名活动'},
        ],
        productsShow: false,
        signupUserShow: false,

        selectedTag:{}


      }
    },

    created(){
      this.info()
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

      info(){
        info(this.$route.params.id).then(response => {
          if(response.data.result==='00000000'){
            this.activity = response.data.data.activity
            this.activity.beginAndEndDate = [response.data.data.activity.startDate.time, response.data.data.activity.endDate.time]
            console.log(JSON.stringify(response.data.data.activity.promotionProducts))
            this.activity.products = response.data.data.activity.promotionProducts
            if(this.activity.type==='promotion'){
              this.productsShow = true
            }else if(this.activity.type==='signup'){
              this.signupUserShow = true
            }
          }else{
            this.$message({
              type: 'error',
              message: '数据获取失败！'
            })
          }

        }).catch(e => {
          console.log(e)
        })
      },


      auditActivity(decision){
        let decisionStr;
        if(decision===0){
          decisionStr = "通过"
        }else if(decision===1){
          decisionStr = "驳回"
        }else {
          return
        }
        this.$confirm("确定"+decisionStr+"该活动的发布申请吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          audit(this.activity.id, decision).then(response => {
            if(response.data.result==='00000000'){
              this.$message({
                type: 'success',
                message: '审核操作成功！'
              })
              this.info()
            }else {
              this.$message({
                type: 'error',
                message: '审核操作失败！'+response.data.msg
              })
            }
          }).catch(e => {
            console.log(e)
          })
        })
      },

      discontinueActivity(){
        this.$confirm("确定中止该活动吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          discontinue(this.activity.id).then(response => {
            if(response.data.result==='00000000'){
              this.$message({
                type: 'success',
                message: '活动中止成功！'
              })
              this.info()
            }else {
              this.$message({
                type: 'error',
                message: '活动中止失败！'+response.data.msg
              })
            }
          }).catch(e => {
            console.log(e)
          })
        })
      },

      startActivity(){
        this.$confirm("确定开始该活动吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          start(this.activity.id).then(response => {
            if(response.data.result==='00000000'){
              this.$message({
                type: 'success',
                message: '活动开始！'
              })
              this.info()
            }else {
              this.$message({
                type: 'error',
                message: '开始活动操作失败！'+response.data.msg
              })
            }
          }).catch(e => {
            console.log(e)
          })
        })
      },

      endActivity(){
        this.$confirm("确定结束该活动吗？", '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          end(this.activity.id).then(response => {
            if(response.data.result==='00000000'){
              this.$message({
                type: 'success',
                message: '活动结束！'
              })
              this.info()
            }else {
              this.$message({
                type: 'error',
                message: '结束活动操作失败！'+response.data.msg
              })
            }
          }).catch(e => {
            console.log(e)
          })
        })
      },

    }
  }
</script>
