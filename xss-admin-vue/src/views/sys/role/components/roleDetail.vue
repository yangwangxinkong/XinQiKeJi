<template>
  <div class="createPost-container">
    <el-form class="form-container" :model="postForm" :rules="rules" ref="postForm" label-position="right" label-width="100px">

      <sticky :className="'sub-navbar'">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loadingAdd" :disabled="isEdit" style="margin-left: 10px;" type="success" @click="submitFormAdd">发布
        </el-button>
        <el-button v-loading="loadingEdit" :disabled="!isEdit" style="margin-left: 10px;" type="success" @click="submitFormEdit">更新
        </el-button>
      </sticky>

      <div class="createPost-main-container">
        <el-form-item label="名称：" prop="name">
          <el-input style="width: 200px;" class="filter-item" placeholder="名称" required v-model="postForm.name" name="name">
          </el-input>
        </el-form-item>
        <el-form-item label="描述：">
          <el-input style="width: 200px;" class="filter-item" placeholder="描述" v-model="postForm.description">
          </el-input>
        </el-form-item>

        <el-form-item label="系统设置：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in systemData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="数据设置：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in settingsData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="会员管理：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in memberData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="商品管理：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in productData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="订单管理：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in orderData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <el-form-item label="营销管理：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in marketingData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        
        <el-form-item label="内容管理：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in contentData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>

        <!-- <el-form-item label="统计管理：">
          <el-checkbox-group v-model="postForm.roles" @change="changeRoleGroup">
            <el-checkbox v-for="item in statisticsData" :label="item.id" :key="item.id">{{item.name}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item> -->

        

      </div>
    </el-form>

  </div>
</template>

<script>
import Sticky from '@/components/Sticky' // 粘性header组件
import { createRole,getRole,updateRole } from '@/api/sys'
import { goback } from '@/utils/common'

const defaultForm = {
  id: undefined,
  name: undefined,
  description: undefined,
  roles: [],
}
const systemList = [
  {
    id: 'admin:setting',
    name: '系统设置',
  },
  {
    id: 'admin:paymentMethod',
    name: '支付方式',
  },
  {
    id: 'admin:city',
    name: '城市管理',
  },
  {
    id: 'admin:residenceType',
    name: '户口性质管理',
  },
  {
    id: 'admin:admin',
    name: '管理员管理',
  },
  {
    id: 'admin:role',
    name: '角色管理',
  },
  {
    id: 'admin:wxMenu',
    name: '微信菜单',
  },
  {
    id: 'admin:wxClickKey',
    name: '微信关键字',
  },
  {
    id: 'admin:wxClickValue',
    name: '微信关键字响应',
  }
]
const settingList = [
  {
    id: 'admin:providentFundSettings',
    name: '公积金比例配置',
  },
  {
    id: 'admin:socialSecuritySettings',
    name: '社保比例配置',
  },
  {
    id: 'admin:serviceFeeSettings',
    name: '服务费配置',
  },
  {
    id: 'admin:payBaseSettings',
    name: '缴费基数配置',
  }
]
const memberList = [
  {
    id: 'admin:member',
    name: '会员管理',
  }
]
const productList = [
  {
    id: 'admin:product',
    name: '商品管理',
  },
  {
    id: 'admin:productCategory',
    name: '商品分类',
  },
  {
    id: 'admin:brand',
    name: '品牌管理',
  },
  {
    id: 'admin:specification',
    name: '规格管理',
  },
  {
    id: 'admin:attribute',
    name: '属性管理',
  },
  {
    id: 'admin:parameterGroup',
    name: '参数管理',
  }
]
const orderList = [
  {
    id: 'admin:order',
    name: '订单管理',
  },
  {
    id: 'admin:payment',
    name: '收款管理',
  },
  {
    id: 'admin:orderCancelApply',
    name: '取消订单',
  },
  {
    id: 'admin:invoice',
    name: '发票管理',
  }
]
const marketingList = [
  {
    id: 'admin:coupon',
    name: '优惠券管理',
  },
  {
    id: 'admin:activity',
    name: '活动管理',
  }
]
const contentList = [
  {
    id: 'admin:navigation',
    name: '导航管理',
  },
  {
    id: 'admin:adPosition',
    name: '广告位管理',
  },
  {
    id: 'admin:ad',
    name: '广告管理',
  },
  {
    id: 'admin:articleCategory',
    name: '文章分类',
  },
  {
    id: 'admin:article',
    name: '文章管理',
  },
  {
    id: 'admin:tag',
    name: '标签管理',
  }
]

export default {
  name: 'role',
  components: {
    Sticky
  },
  props: {
    isEdit: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      postForm: Object.assign({}, defaultForm),
      loadingAdd: false,
      loadingEdit: false,
      userListOptions: [],
      rules: {
        name: [{required: true, message:'名称必填',trigger:'blur'}],
      },
      systemData: systemList,
      settingsData: settingList,
      memberData: memberList,
      productData: productList,
      orderData: orderList,
      marketingData: marketingList,
      contentData: contentList
      
    }
  },
  created() {
    if (this.isEdit) {
      const id = this.$route.params && this.$route.params.id
      this.fetchData(id)
    } else {
      this.postForm = Object.assign({}, defaultForm)
    }
  },
  methods: {
    fetchData(id) {
      getRole(id).then(response => {
        this.postForm = response.data.data
      }).catch(err => {
        console.log(err)
      })
    },
    submitFormAdd() {
      if(!this.postForm.roles.length){
        this.$message({
          message: '请至少勾选一项权限',
          type: 'error',
          duration: 2 * 1000
        })
        return
      }
      console.log(this.postForm)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loadingAdd = true
          createRole(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '新增角色成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingAdd = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/role/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '新增角色失败',
                type: 'error',
                duration: 2000
              })
              this.loadingAdd = false
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    submitFormEdit() {
      if(!this.postForm.roles.length){
        this.$message({
          message: '请至少勾选一项权限',
          type: 'error',
          duration: 2 * 1000
        })
        return
      }
      console.log(this.postForm)
      this.$refs.postForm.validate(valid => {
        if (valid) {
          this.loadingEdit = true
          updateRole(this.postForm).then(response => {
            console.log(response.data)
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '更新角色成功',
                type: 'success',
                duration: 1.5 * 1000
              })
              this.loadingEdit = false
              setTimeout(() => {
                this.$router.replace({ path: '/sys/role/list'})
              }, 1.6 * 1000)
            }
            else{
              this.$notify({
                title: '失败',
                message: '更新角色失败',
                type: 'error',
                duration: 2000
              })
              this.loadingEdit = false
            }
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    },
    changeRoleGroup(values) {
        console.log(values)
        console.log(this.postForm)
    },
    closePage() {//返回列表
      goback(this.$route.path);
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
@import "src/styles/mixin.scss";
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
</style>
