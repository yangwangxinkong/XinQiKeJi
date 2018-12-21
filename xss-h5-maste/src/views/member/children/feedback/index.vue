<!--意见反馈  -->
<template>
  <div class="page">
    <div class="container feedback-container">
      <group :gutter="10" class=" bg_f">
        <popup-radio title="请选择反馈类型" class="f14" :options="feedbackList" v-model="currentValue"></popup-radio>
      </group>
      <group :gutter="10" class=" bg_f">
        <x-input class="f14" placeholder="请填写手机号码，方便我们与你联系" v-model="postForm.phone"></x-input>
      </group>
      <group :gutter="10" class=" bg_f">
        <x-textarea
          class="f14"
          :max="500"
          placeholder="请输入您要反馈的内容"
          v-model="postForm.content"
          :height="200"
          :rows="8"
          :cols="30"
        ></x-textarea>
      </group>
      <group :gutter="10">
        <uploader
          :max="varmax"
          :images="Images"
          :handle-click="false"
          :show-header="true"
          :readonly="false"
          :upload-url="uploadUrl"
          name="img"
          :params="params"
          size="small"
          @preview="previewMethod"
          @upload-image="addImageMethod"
          @remove-image="removeImageMethod"
        ></uploader>
      </group>
      <box gap="30px 10px">
        <x-button
          :gradients="['#C5282B', '#C5282B']"
          @click.native="saveInfo"
          style="border-radius:99px;"
        >提交</x-button>
      </box>
      <!-- footer -->
      <!-- <section class="footer-actionBar">
        <section class="footer-actionBar-container flex-ui">
          <div class="flex1 tc white" @click="saveInfo">
            <span class="f16 pl0">保存</span>
          </div>
        </section>
      </section>-->
    </div>
  </div>
</template>

<script>
import { Group, Cell, XInput, XTextarea, PopupRadio, XButton,Box } from "vux";
import Uploader from "vux-uploader";
import { validIsBlank, validIdNum } from "@/utils/validate";
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";

const defaultForm = {
  id: undefined,
  name: "",
  phone: "",
  content: ""
};
export default {
  components: { Group, Cell, XInput, XTextarea, PopupRadio, Uploader, XButton,Box },
  data() {
    return {
      postForm: Object.assign({}, defaultForm),
      currentValue: 1,
      feedbackList: [
        {
          value: "操作难用",
          key: 1
        },
        {
          value: "操作失败",
          key: 2
        },
        {
          value: "加载缓慢",
          key: 3
        },
        {
          value: "产品建议",
          key: 4
        },
        {
          value: "其他",
          key: 5
        }
      ],
      Images: [],
      varmax: 5,
      uploadUrl: process.env.BASE_UPLOAD + "/api/upload/img",
      params: null
    };
  },

  computed: {},

  mounted() {},

  created() {},
  methods: {
    //预览图片
    previewMethod() {},
    //新增图片
    addImageMethod() {},
    //移除图片
    removeImageMethod() {},
    saveInfo() {}
  }
};
</script>
<style lang='less' >
.feedback-container {
  .weui-uploader__input-box {
    border-style: dashed !important;
  }
}
</style>
