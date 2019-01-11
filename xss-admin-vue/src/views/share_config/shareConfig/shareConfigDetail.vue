<template>
  <div class="createPost-container">
    <el-form :rules="rules" ref="dataForm" :model="temp" labelPosition="right" label-width="100px">
      <sticky :className="'sub-navbar'">
        <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loadingAdd" style="margin-left: 10px;" type="success" @click="submitFormAdd">保存
        </el-button>
      </sticky>
      <div class="createPost-main-container">
        <el-form-item label="分享标题：" prop="title">
          <el-col :span="6">
            <el-input v-model="temp.shareTitle" name="title" maxlength='30'></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="分享描述：" prop="title">
          <el-col :span="6">
            <el-input style='width: 500px;' type="textarea" :rows="10" placeholder="请输入分享信息详情！" v-model="temp.shareDesc"></el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="分享图标：" prop="title">
          <singleImage color="#1890ff" class="editor-upload-btn" :url="temp.shareImg" @successCBK="imageSuccessCBK"
                       style="width:250px"></singleImage>
        </el-form-item>
        <!--<el-form-item  label="分享类型：" prop="setting">
          <el-checkbox v-model="salesPromotion" label="促销活动" name="salesPromotion"></el-checkbox>
          <el-checkbox v-model="articleDesc" label="文章详情" name="articleDesc"></el-checkbox>
          <el-checkbox v-model="inviteFriends" label="邀请好友" name="inviteFriends"></el-checkbox>
        </el-form-item>-->
        <el-form-item label="分享类型：" prop="articleCategory" >
          <el-select class="filter-item" v-model="shareDefault" name="shareTypes" @change="selectChange">
            <el-option
              v-for="(item,index) in shareTypes" :style="'padding-left:' + ((item.grade)*20+20) + 'px'"  :key="item.value" :label="item.desc"  :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>
<script>
  import Tinymce from '@/components/Tinymce'
  import {goback} from '@/utils/common'
  import Sticky from '@/components/Sticky'
  import {info} from '@/api/shareConfig'
  import {save} from '@/api/shareConfig'
  import {findTypeList} from '@/api/tag'
  import {get, post, execute} from '@/api/server'
  import singleImage from '@/components/Upload/singleImage4'
  import {validateURL} from '@/utils/validate'

  export default {
    name: 'customerMethod',
    components: {
      Tinymce,
      Sticky,
      singleImage
    },
    props: {
      isEdit: {
        type: Boolean,
        default: false
      }
    },
    data() {
      return {
        type: "shareConfig",
        loadingAdd: false,
        loadingEdit: false,
        query: {
          id: ''
        },
        rules: {
          shareTitle: [{required: true, message: '分享标题必填', trigger: 'blur'}],
          shareDesc: [{required: true, message: '分享描述必填', trigger: 'blur'}],
          shareImg: [{required: true, message: '分享图标必选', trigger: 'change'}],
          shareTypes: [{required: true, message: '分享类型必选', trigger: 'change'}],//change改变事件
        },
        shareTypes:[{
          name: 's0',
          value: 0,
          desc: '促销活动'
        }, {
          name: 's1',
          value: 1,
          desc: '文章详情'
        },{
          name: 's2',
          value: 2,
          desc: '邀请好友'
        }],
        shareDefault: '促销活动',
        temp: {
          id: '',
          shareTitle: '',
          shareDesc: '',
          shareImg: '',
          shareTypes: '0'//默认是促销活动
        }
      }
    },
    created() {
      if (this.isEdit) {
        this.query.id = this.$route.params && this.$route.params.id
        this.fetchData()
      }
    },
    methods: {
      fetchData() {
        info(this.query).then(response => {
          this.temp.id = response.data.data.id
          this.temp.shareTitle = response.data.data.shareTitle
          this.temp.shareDesc = response.data.data.shareDesc
          this.temp.shareImg = response.data.data.shareImg
          this.temp.shareTypes = response.data.data.shareTypes
          this.shareDefault = this.shareTypes[response.data.data.shareTypes].desc
        }).catch(err => {
          console.log("获取分享信息失败");
        })
      },
      submitFormAdd() {
        this.$refs['dataForm'].validate(valid => {
          if (valid) {
            save(this.temp).then(response => {
              if (response.data.result === '00000000') {
                this.$notify({
                  title: '成功',
                  message: '操作成功',
                  type: 'success',
                  duration: 2000
                })
                //返回上一个页面
                goback(this.$route.path)
              } else {
                this.$notify({
                  title: '失败',
                  message: '保存失败:' + response.data.data,
                  type: 'error',
                  duration: 2000
                })
              }
            })
          }
        })
      },
      imageSuccessCBK(path) {
        this.temp.shareImg = path
      },
      closePage() {
        goback(this.$route.path);
      },
      selectChange(val){
        this.temp.shareTypes = val
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

