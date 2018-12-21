<template>
  <div class="weui-cell">
    <div class="weui-cell__bd">
      <div class="weui-uploader">
        <div class="weui-uploader__bd" :class="{small: size === 'small'}">
          <div class="pr" @click="add">
            <div class="weui-uploader__input-box">
              <input
                ref="input"
                class="weui-uploader__input"
                type="file"
                accept="image/*"
                :capture="showCapture"
                @change="change"
              >
            </div>
            <div class="weui-uploader__files pa" v-for="image in images" :key="image.url">
              <img class="image" :src="image.url" alt>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  props: {
    images: {
      type: Array,
      default: () => []
    },
    max: {
      type: Number,
      default: 1
    },
    showTip: {
      type: Boolean,
      default: true
    },
    readonly: {
      type: Boolean,
      default: false
    },
    title: {
      type: String,
      default: "图片上传"
    },
    // 是否接管+号的click事件，如果是，点击不弹出选择文件的框
    handleClick: {
      type: Boolean,
      default: false
    },
    // 选择文件后是否自动上传，如果不是，则emit change事件
    autoUpload: {
      type: Boolean,
      default: true
    },
    uploadUrl: {
      type: String,
      default: ""
    },
    size: {
      type: String,
      default: "normal"
    },
    capture: {
      type: String,
      default: ""
    },
    name: {
      type: String,
      default: "img"
    },
    params: {
      type: Object,
      default: null
    }
  },
  components: {},
  methods: {
    add() {
      if (this.handleClick) {
        this.$emit("add-image");
      }
    },
    // 适用于action的情况
    change() {
      console.log("00000000");
      //      if (this.handleClick) {
      //        return
      //      }

      let formData = new window.FormData();
      formData.append(this.name, this.$refs.input.files[0]);
      if (this.params) {
        for (let key in this.params) {
          formData.append(key, this.params[key]);
        }
      }
      console.log("111111111");
      if (this.autoUpload) {
        console.log("222222");
        if (!this.uploadUrl) {
          console.error("uploadUrl不应为空");
        }
        console.log("33333");
        if (this.$vux && this.$vux.loading) {
          this.$vux.loading.show("正在上传");
        }
        console.log("444444");

        axios.post(this.uploadUrl, formData).then(response => {
          console.log("555555");
          if (this.$vux && this.$vux.loading) {
            this.$vux.loading.hide();
          }
          this.$refs.input.value = "";
          //this.images = [];
          //          if(this.max == 1 && this.images.length == 1){
          //              this.images[0] = response.data.data;
          //          }else{
          this.images.push(response.data.data);
          //          }

          this.$emit("upload-image", "");
          console.log("6666666");
        });
      } else {
        console.log("777777");
        this.$emit("upload-image", formData);
        console.log("8888888");
      }
    }
  },
  computed: {
    showCapture() {
      return this.capture || undefined;
    }
  }
};
</script>
<style scoped lang="less">
@import "~vux/src/styles/weui/widget/weui-uploader/index.less";
.remove:before {
  width: 0;
  height: 0;
}
.weui-uploader__input-box {
  float: inherit;
  border: none;
}
.weui-uploader__bd {
  margin-bottom: 0px;
  margin-right: 0px;
}
.weui-uploader__bd.small {
  .weui-uploader__input-box {
    margin-right: 5px;
    margin-bottom: 5px;
    margin: 0 auto;
    width: 100%;
    height: 2.667rem;
  }
}
.weui-uploader__files {
  width: 4.427rem;
  top: 0;
  // padding: 2px;
  background: url(../../assets/uploader-bg.png) no-repeat;
  background-size: 100% 100%;
  height: 2.667rem;
  img[src=""],img:not([src]){
    border:0;
    opacity: 0;
  }
}
</style>
