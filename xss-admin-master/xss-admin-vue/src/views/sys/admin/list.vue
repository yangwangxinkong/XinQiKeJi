<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleFilter" style="width: 200px;" class="filter-item" placeholder="用户名" v-model="listQuery.searchValue">
      </el-input>
      <el-button class="filter-item" type="primary" v-waves icon="el-icon-search" @click="handleFilter">{{$t('table.search')}}</el-button>
      <!--<router-link :to="'/sys/delivery_corp/add'">-->
        <!--<el-button class="filter-item" style="margin-left: 10px;" type="primary" icon="el-icon-plus">{{$t('table.add')}}</el-button>-->
      <!--</router-link>-->

      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">{{$t('table.add')}}</el-button>

      <el-button class="filter-item" style="margin-left: 10px;" @click="handleDelte" type="danger" icon="el-icon-remove-outline">{{$t('table.delete')}}</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleRefresh" type="primary" icon="el-icon-refresh">刷新</el-button>
    </div>

    <el-table ref="multipleTable" :data="list" v-loading="listLoading" fit tooltip-effect="dark" style="width: 100%" @selection-change="handleSelectionChange" @sort-change='sortChange'>
      <el-table-column
        type="selection"
        width="55">
      </el-table-column>

      <el-table-column
        prop="username"
        label="用户名"
        width="120"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="name"
        width="120"
        label="姓名">
      </el-table-column>

      <el-table-column
        prop="department"
        label="部门"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        prop="openId"
        label="openId"
        width="250"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column
        label="状态">
        <template slot-scope="scope">
          <span v-if="!scope.row.isEnabled" style="color: red">禁用</span>
          <span v-else-if="scope.row.isLocked" style="color: red">锁定</span>
          <span v-else style="color: green">正常</span>
        </template>
      </el-table-column>

      <el-table-column
        prop="createDate"
        label="创建日期"
        width="110"
        show-overflow-tooltip>
      </el-table-column>

      <el-table-column align="center" label="操作" width="120">
        <template slot-scope="scope">
          <!--<router-link :to="'/sys/delivery_corp/edit/'+scope.row.id" @click="handleUpdate(cell.id)">-->
            <!--<el-button type="primary" size="small" icon="el-icon-edit">编辑</el-button>-->
          <!--</router-link>-->
          <el-button class="filter-item" size="mini" type="primary" icon="el-icon-edit" @click="handleUpdate(scope.row.id)">编辑</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination background @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="listQuery.pageNumber"
                     :page-sizes="[10,20,30, 50]" :page-size="listQuery.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total">
      </el-pagination>
    </div>

    <!--新增及编辑dialog--------start------->
    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form ref="temp" :rules="rules" :model="temp"  label-position="right" label-width="100px">
        <el-form-item label="用户名：" prop="username">
          <el-col :span="10">
            <el-input v-model="temp.username" v-if="dialogStatus=='create'" placeholder="用户名"></el-input>
            <span v-else>{{temp.username}}</span>
          </el-col>
        </el-form-item>

        <el-form-item label="密码：" :prop="passwordRules">
          <el-col :span="10">
          <el-input type="password" v-model.trim="temp.password" placeholder="密码" maxlength="12"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="确认密码：" :prop="password_confirm_Rules">
          <el-col :span="10">
          <el-input type="password" v-model.trim="temp.password_confirm" placeholder="确认密码" maxlength="12"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="E-mail：" prop="email">
          <el-col :span="10">
          <el-input v-model="temp.email" name="email" placeholder="如：XXXX.@163.com"></el-input>
          </el-col>
        </el-form-item>

        <el-form-item label="openId：" prop="openId">
          <el-col :span="10">
            <el-input v-model="temp.openId" name="openId"></el-input>
          </el-col>
        </el-form-item>
        <!--<el-form-item label="所属城市：" prop="cityCode">
          <el-col :span="10">
            <el-select v-model="temp.cityCode" placeholder="所属城市">
              <el-option
                v-for="item in cities"
                :key="item.code"
                :label="item.name"
                :value="item.code">
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>-->

        <el-form-item label="角色：" prop="roles">
          <el-col :span="50">
            <el-checkbox-group v-model="temp.roles">
              <el-checkbox v-for="role in roleOptions" :label="role.id" :key="role.id">{{role.name}}</el-checkbox>
            </el-checkbox-group>
          </el-col>
        </el-form-item>

        <el-form-item label="设置：">
          <el-col :span="10">
          <el-checkbox :label="temp.isEnabled" v-model="temp.isEnabled">是否启用</el-checkbox>
          <el-checkbox v-if="dialogStatus=='update'" :label="temp.isLocked"  v-model="temp.isLocked">是否锁定</el-checkbox>
          </el-col>
        </el-form-item>
        <el-form-item label="姓名：" prop="name">
          <el-col :span="10">
            <el-input v-model="temp.name" placeholder="姓名"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="部门：" prop="department">
          <el-col :span="10">
          <el-input v-model="temp.department" placeholder="部门"></el-input>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">{{$t('table.cancel')}}</el-button>
        <el-button v-if="dialogStatus=='create'" type="primary" @click="createData">{{$t('table.confirm')}}</el-button>
        <el-button v-else type="primary" @click="updateData">{{$t('table.confirm')}}</el-button>
      </div>
    </el-dialog>
    <!--新增及编辑dialog--------end------->

  </div>
</template>

<script>
  import waves from '@/directive/waves' // 水波纹指令
  import { getAdminList,deleteAdmin,getRoleAllList,createAdmin,getAdmin,updateAdmin } from '@/api/sys'
  import { fetchCityList } from '@/api/city'
  import ElCol from "element-ui/packages/col/src/col";
  import { validateEmail } from '@/utils/validate'

  export default {
    components: {ElCol},
    name: 'adminTable',
    directives: {
      waves
    },
    data() {
      const validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.temp.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      }
      const validatePassEdit = (rule, value, callback) => {
        value = this.temp.password_confirm
        if (this.temp.password === '') {
          callback();
        }
        if (value !== this.temp.password) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      }
      const validate_email = (rule, value,callback)=>{
        if(!value){
          callback(new Error('邮箱必填'))
        } else if (!validateEmail(value)){
          callback(new Error('请输入正确的邮箱'))
        }else {
          callback()
        }
      }
      const validate_minlength = (rule, value,callback)=>{
        if(!value){
          callback()
        } else if (value.length < 6){
          callback(new Error('长度不小于6'))
        }else {
          callback()
        }
      }
      const validate_minlength_edit = (rule, value,callback)=>{
        value = this.temp.password
        if(!value){
          callback()
        } else if (value.length < 6){
          callback(new Error('长度不小于6'))
        }else {
          callback()
        }
      }
      return {
        list: [],
        cities:[],
        total: 0,
        listLoading: true,
        multipleSelection: [],
        roleOptions:[],
        listQuery: {
          pageNumber: 1,
          pageSize: 10,
          name: undefined,
          searchProperty: 'username',
          searchValue: undefined,
          orderProperty: undefined,
          orderDirection: undefined
        },
        dialogFormVisible: false,
        dialogStatus:'',
        textMap: {
          update: '编辑',
          create: '新增',
          rules: {}
        },
        temp: {
          id:'',
          username:'',
          password:'',
          password_confirm:'',
          email:'',
          roles:[],
          isEnabled:true,
          isLocked:false,
          department:'',
          name:'',
          cityCode:undefined,
          openId: undefined
        },
        passwordRules:'password',
        password_confirm_Rules:'password_confirm',
        rules:{
          username: [{required: true, message:'用户名必填',trigger:'blur'}],
          email: [{required: true,trigger:'blur',validator: validate_email}],
          roles: [{required: true, message:'角色必填',trigger:'blur'}],
          password: [{required: true, message:'必填',trigger:'blur'},{validator:validate_minlength, trigger: 'blur'}],
          passwordEdit: [{validator:validate_minlength_edit, trigger: 'blur'}],
          password_confirm:[{required: true, message:'必填',trigger:'blur'},{validator: validatePass, trigger: 'blur' }],
          password_confirm_edit:[{validator: validatePassEdit, trigger: 'blur' }]
        }
      }
    },
    created() {
      this.getList()
      this.initCities()
    },
    methods: {
      initCities(){
        fetchCityList().then(response => {
          this.cities = response.data.data
          this.temp.cityCode = this.cities[0].code;
        }).catch((e)=>{
            console.log(e)
        })
      },
      resetTemp() {
        this.temp = {
          id:'',
          username:'',
          password:'',
          password_confirm:'',
          email:'',
          roles:[],
          isEnabled:true,
          isLocked:false,
          department:'',
          name:'',
          cityCode:this.temp.cityCode,
          openId: undefined
        }
      },
      getList() {
        this.listLoading = true
        getAdminList(this.listQuery).then(response => {
          this.list = response.data.list
          this.total = response.data.total
          setTimeout(() => {
            this.listLoading = false
          }, 1.5 * 1000)
        }).catch((e)=>{
          console.log(e)
          this.listLoading = false
        })
      },
      getRoleOptions() {
        getRoleAllList().then(response => {
          this.roleOptions = response.data.data
        }).catch((e)=>{
          console.log(e)
        })
      },
      handleCreate() {
        this.passwordRules= 'password'
        this.password_confirm_Rules= 'password_confirm'
        this.resetTemp()
        this.getRoleOptions()
        this.dialogStatus = 'create'
        this.dialogFormVisible = true
        this.$nextTick(() => {
          this.$refs['temp'].clearValidate()
        })
      },
      handleUpdate(id) {
        this.passwordRules= 'passwordEdit'
        this.password_confirm_Rules= 'password_confirm_edit'
        this.getRoleOptions()
        getAdmin(id).then(response => {
          if(response.data.result==='00000000'){
            this.resetTemp()
            this.temp.id=response.data.data.id
            this.temp.username=response.data.data.username
            this.temp.email=response.data.data.email
            this.temp.roles=response.data.data.roles
            this.temp.isEnabled=response.data.data.isEnabled
            this.temp.isLocked=response.data.data.isLocked
            this.temp.department=response.data.data.department
            this.temp.name=response.data.data.name
            this.temp.cityCode = response.data.data.cityCode;
            this.temp.openId = response.data.data.openId;
            this.dialogStatus = 'update'
            this.dialogFormVisible = true
            console.log(this.temp.roles)
            console.log(response.data.data.roles)
          } else {
            this.$notify({
              title: '获取管理员信息异常',
              message: response.data.msg,
              type: 'error',
              duration: 2000
            })
          }
        }).catch((e)=>{
          console.log(e)
        })
        this.$nextTick(() => {
          this.$refs['temp'].clearValidate()
        })
      },
      handleRefresh() {
          this.getList();
      },
      handleFilter() {
        this.listQuery.pageNumber = 1
        this.getList()
      },
      handleDelte() {
        if (!this.multipleSelection.length) {
          this.$message({
            message: '请先选择需要删除的数据',
            type: 'error',
            duration: 2 * 1000
          })
          return
        }
        this.$confirm('删除无法恢复, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          var ids = this.multipleSelection.map(item => item.id).join()
          deleteAdmin({ids:ids}).then(response => {
            if(response.data.result==='00000000'){
              this.$notify({
                title: '成功',
                message: '删除管理员成功',
                type: 'success',
                duration: 2000
              })
            } else {
              this.$notify({
                title: '失败',
                message: response.data.msg,
                type: 'error',
                duration: 2000
              })
            }
            this.getList()
          })
        }).catch(err => {
          console.log(err)
        })
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
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
      },
      createData() {
        this.$refs["temp"].validate(valid => {
          if (valid) {
            console.log(this.temp)
            createAdmin(this.temp).then(response => {
              console.log(response.data)
              if(response.data.result==='00000000'){
                this.$notify({
                  title: '成功',
                  message: '添加管理员成功',
                  type: 'success',
                  duration: 1.5 * 1000
                })
                setTimeout(() => {
                  this.dialogFormVisible = false
                  this.getList()
                }, 1.6 * 1000)
              }
              else{
                this.$notify({
                  title: '失败',
                  message: response.data.msg,
                  type: 'error',
                  duration: 1.5 * 1000
                })
              }
            }).catch((e)=>{
              console.log(e)
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      },
      updateData(){
        this.$refs["temp"].validate(valid => {
          if (valid) {
            updateAdmin(this.temp).then(response => {
              console.log(response.data)
              if(response.data.result==='00000000'){
                this.$notify({
                  title: '成功',
                  message: '更新管理员成功',
                  type: 'success',
                  duration: 1.5 * 1000
                })
                setTimeout(() => {
                  this.dialogFormVisible = false
                  this.getList()
                }, 1.6 * 1000)
              }
              else{
                this.$notify({
                  title: '失败',
                  message: '更新管理员失败',
                  type: 'error',
                  duration: 1.5 * 1000
                })
              }
            }).catch((e)=>{
              console.log(e)
            })
          } else {
            console.log('error submit!!')
            return false
          }
        })
      }
    }
  }
</script>
