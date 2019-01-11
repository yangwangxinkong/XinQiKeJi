<template>
  <div class="upload-container">
    <el-upload
      class="avatar-uploader"
      :action="uploadUrl"
      :show-file-list="false"
      :on-remove="handleRemove" :on-success="handleSuccess" :before-upload="beforeUpload" style="float: left;margin-left: 12px;">
      <el-button size="small" type="primary" >点击上传</el-button>
    </el-upload>
    <a v-if="url" target="_blank" :href="url" style="float: left;margin-left: 12px; color: #0a76a4">查看</a>
    <a v-else-if="imageUrl" target="_blank" :href="imageUrl" style="float: left;margin-left: 12px; color: #0a76a4">查看</a>
  </div>
</template>
<script>
export default {
  name: 'editorSlideUpload',
  props: {
      url:{
        type: String,
        default: ''
      },
    property:{
          type:Object,
          default:undefined
    }
  },
  data() {
    return {
//      uploadUrl:process.env.BASE_API + "/api/upload/image",
      uploadUrl:process.env.BASE_UPLOAD + "/api/upload/image",
      imageUrl:""
    }
  },
  methods: {

    handleSuccess(response, file) {
      this.imageUrl = response.file_name;
      this.$emit('successCBK', response.file_name, this.property);
    },
    handleRemove(file) {

    },
    beforeUpload(file) {
      const isJPG = (file.type === 'image/jpg' || file.type === 'image/jpeg' || file.type === 'image/bmp' || file.type === 'image/gif' || file.type === 'image/png');
      const isLt2M = file.size / 1024 / 1024 < 2;

      if (!isJPG) {
        this.$message.error('上传头像图片只能是 JPG 格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>
<style>

.avatar-uploader .el-upload {
    cursor: pointer;
    position: relative;
    overflow: hidden;

  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }

</style>
