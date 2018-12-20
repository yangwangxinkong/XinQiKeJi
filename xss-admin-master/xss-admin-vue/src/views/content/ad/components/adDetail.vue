<template>
  <div class="createPost-container">
    <el-form class="form-container" :rules="rules" labelPosition="right" label-width="150px" :model="postForm" ref="postForm">
      <sticky :className="'sub-navbar'">
         <el-button @click="closePage();">返回</el-button>
        <el-button v-loading="loadingAdd" style="margin-left: 10px;" type="success" @click="submitFormAdd">保存
        </el-button>
      </sticky>
      <div class="createPost-main-container">
        <el-form-item label="标题：" prop="title">
          <el-col :span="6">
            <el-input class="filter-item" v-model="postForm.title" name="title"> </el-input>
          </el-col>
        </el-form-item>
        <el-form-item label="类型：" prop="type">
          <el-col :span="6">
            <el-select name="type" class="filter-item" v-model="postForm.type" @change="selectChange" style="width:100%;" >
              <el-option v-for="item in  adType" :key="item.eName" :label="item.name" :value="item.eName">
                {{item.name}}
              </el-option>
            </el-select>
          </el-col>
        </el-form-item>
        <el-form-item label="广告位：" prop="adPosition">
          <el-col :span="6">
          <el-select name="adPosition" class="filter-item" v-model="postForm.adPosition" style="width:100%;">
            <el-option v-for="item in  adPositions" :key="item.id" :label="item.label" :value="item.id">
            </el-option>
          </el-select>
          </el-col>
        </el-form-item>

        <el-form-item label="路径：" prop="path" v-if="postForm.type== 'image'|| postForm.type== 'flash'" >
          <el-col :span="10">
            <el-input class="filter-item" v-model="postForm.path" name="path" style="float:left;width:230px;"> </el-input>
            <singleImage color="#1890ff" class="editor-upload-btn" :url="postForm.path" @successCBK="imageSuccessCBK"></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="预览图：" prop="videoImage" v-if="postForm.type == 'flash'">
          <el-col :span="6">
            <singleImage color="#1890ff" class="editor-upload-btn" :url="postForm.videoImage" @successCBK="imageSuccessCBKV"></singleImage>
          </el-col>
        </el-form-item>
        <el-form-item label="起始日期：" prop="beginDate" >
          <el-col :span="6">
            <el-date-picker value-format="timestamp" v-model="postForm.beginDate" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item label="结束日期：" prop="endDate" >
          <el-col :span="6">
            <el-date-picker value-format="timestamp" v-model="postForm.endDate" type="datetime" placeholder="选择日期时间">
            </el-date-picker>
          </el-col>
        </el-form-item>
        <el-form-item  label="链接地址：" prop="url">
            <el-col :span="6">
          <el-input  class="filter-item" v-model="postForm.url" >
          </el-input>
           </el-col>
        </el-form-item>
        <el-form-item  label="排序：" prop="url">
          <el-col :span="6">
          <el-input  class="filter-item" v-model="postForm.order" >
          </el-input>
           </el-col>
        </el-form-item>
        <el-form-item label="内容：" prop="content" v-if="postForm.type == 'text'">
          <div class="editor-container">
            <Tinymce :height=300 ref="editor" v-model="content" />
          </div>
        </el-form-item>
      </div>
    </el-form>
  </div>
</template>
<script>
import Tinymce from "@/components/Tinymce";
import Sticky from "@/components/Sticky";
import singleImage from "@/components/Upload/singleImage5";
import { validateURL } from "@/utils/validate";
import url from '@/api/apiUrl'
import { get,post,execute } from '@/api/server'
import { goback } from "@/utils/common";
export default {
  name: "adMethod",
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
      loadingAdd: false,
      loadingEdit: false,
      adType: [],
      adPositions: [],
      id: undefined,
      rules: {
        title: [{ required: true, message: "标题必填", trigger: "blur" }],
        type: [{ required: true, message: "类型必选", trigger: "change" }],
        adPosition: [
          { required: true, message: "广告位必选", trigger: "change" }
        ],
        path: [{ required: true, message: "路径必填", trigger: "change" }],
        videoImage: [{ required: true, message: "预览图必填", trigger: "change" }]
      },
      content:'',
      postForm: {
        id: "",
        title: "",
        type: "",
        adPosition: "",
        content: "",
        path: "",
        videoImage: "",
        product: "",
        store: "",
        article: "",
        beginDate: "",
        endDate: "",
        url: "",
        order: ""
      }
    };
  },
  created() {
    if (this.isEdit) {
      this.postForm.id = this.$route.params && this.$route.params.id;
      this.fetchData(this.postForm.id);
    }
    this.getTypeList();
    this.getAdPositionList();
  },
  methods: {
    fetchData(id) {
      get(url.adInfo, {id:id}).then(response => {
          //console.log(response.data.data)
        this.postForm.title = response.data.data.title;
        this.postForm.type = response.data.data.type;
//        if(this.postForm.type === 'product'){
//          this.postForm.product = response.data.data.product.sn;
//        }else if(this.postForm.type === 'article'){
//          this.postForm.article = response.data.data.article.id;
//        }else if(this.postForm.type === 'store'){
//          this.postForm.store = response.data.data.store.id;
//        }
        this.postForm.adPosition = response.data.data.adPosition.id;
        this.content = response.data.data.content;
        this.postForm.path = response.data.data.path;
        this.postForm.videoImage = response.data.data.videoImage;
        this.postForm.beginDate = response.data.data.beginDateTime;
        this.postForm.endDate = response.data.data.endDateTime;
        this.postForm.url = response.data.data.url;
        this.postForm.order = response.data.data.order;
      });
    },
    getTypeList() {
      get(url.adTypeList, {}).then(response => {
        this.adType = response.data.data;
      });
    },
    getAdPositionList() {
      get(url.adPositionTree, {}).then(response => {
        this.adPositions = response.data.data;
      });
    },
    submitFormAdd() {
      this.postForm.content = this.content
      this.$refs['postForm'].validate(valid => {
        if(valid){
          execute(url.adSave, this.postForm).then(response => {
            if (response.data.result === "00000000") {
              this.$notify({
                title: "成功",
                message: "操作成功",
                type: "success",
                duration: 2000
              });
              goback(this.$route.path);
            } else {
              this.$notify({
                title: "失败",
                message: "操作失败",
                type: "error",
                duration: 2000
              });
            }
          });
        }
      })
    },
    selectChange(val) {
      this.postForm.type = val;
    },
    imageSuccessCBK(path) {
      this.postForm.path = path;
    },
    imageSuccessCBKV(path) {
      this.postForm.videoImage = path;
    },
    closePage(){
       goback(this.$route.path);
    }
  }
};
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
