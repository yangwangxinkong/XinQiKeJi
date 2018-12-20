<template>
  <div class="upload-container">
    <el-upload
      class="avatar-uploader"
      :action="uploadUrl"
      :show-file-list="false"
      :on-remove="handleRemove" :on-success="handleSuccess" :before-upload="beforeUpload">
      <img v-if="url" :src="url" class="avatar">
      <img v-else-if="imageUrl" :src="imageUrl" class="avatar">
      <i v-else class="el-icon-plus avatar-uploader-icon"></i>
    </el-upload>
  </div>
</template>
<script>
export default {
  name: 'editorSlideUpload',
  props: {
    url:{
      type: String,
      default: ''
    }
  },
  data() {
    return {
      uploadUrl:process.env.BASE_UPLOAD + "/api/upload/image",
      imageUrl:""
    }
  },
  methods: {

    handleSuccess(response, file) {
      this.imageUrl = response.file_name;
      this.$emit('successCBK', response.file_name);
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
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 100%;
    height: 100%;
    display: block;
  }
</style>
