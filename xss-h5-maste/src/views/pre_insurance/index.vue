<!--参保资料  -->
<template>
  <div class="page">
    <div class="container insurance-container" style="padding-bottom:0;">
      <group title="上传身份证" :gutter="0" class="mt10">
        <flexbox>
          <flexbox-item>
            <div class="flex-div">
              <uploader
                :max="max"
                :images="idFaces"
                :handle-click="true"
                :show-header="true"
                :readonly="false"
                :upload-url="uploadUrl"
                name="上传身份证"
                :params="params"
                size="small"
                @preview="previewMethod"
                @upload-image="addImageMethod"
                @remove-image="removeImageMethod"
              ></uploader>
              <div class="tc tip">
                <p class="grey_7c">身份证正面</p>
                <p class="state tc state-done f12" v-if="idFaceFlag">已上传</p>
                <p class="state tc f12" v-else>未上传</p>
              </div>
            </div>
          </flexbox-item>
          <flexbox-item>
            <div class="flex-div">
              <uploader
                :max="max"
                :images="idBackFaces"
                :handle-click="true"
                :readonly="false"
                :upload-url="uploadUrl"
                name="身份反面"
                :params="params"
                size="small"
                @upload-image="addImageMethod"
              ></uploader>
              <div class="tip tc">
                <p class="grey_7c">身份反面</p>
                <p class="state tc state-done f12" v-if="idBackFaceFlag">已上传</p>
                <p class="state tc f12" v-else>未上传</p>
              </div>
            </div>
          </flexbox-item>
        </flexbox>
      </group>
      <group title="上传户口本" :gutter="0" class="mt10">
        <flexbox>
          <flexbox-item>
            <div class="flex-div">
              <uploader
                :max="max"
                :images="hukouIndeies"
                :handle-click="true"
                :readonly="false"
                :upload-url="uploadUrl"
                name="户口本首页"
                :params="params"
                size="small"
                @upload-image="addImageMethod"
              ></uploader>
              <div class="tc tip">
                <p class="grey_7c">户口本首页</p>
                <p class="state tc state-done f12" v-if="hukouIndexFlag">已上传</p>
                <p class="state tc f12" v-else>未上传</p>
              </div>
            </div>
          </flexbox-item>
          <flexbox-item>
            <div class="flex-div">
              <uploader
                :max="max"
                :images="hukouPersons"
                :handle-click="true"
                :readonly="false"
                :upload-url="uploadUrl"
                name="户口本本人页"
                :params="params"
                size="small"
                @upload-image="addImageMethod"
              ></uploader>
              <div class="tip tc">
                <p class="grey_7c">户口本本人页</p>
                <p class="state tc state-done f12" v-if="hukouPersonFlag">已上传</p>
                <p class="state tc f12" v-else>未上传</p>
              </div>
            </div>
          </flexbox-item>
        </flexbox>
      </group>
      <group title="上传一寸照片" :gutter="0" class="mt10">
        <flexbox>
          <flexbox-item>
            <div class="flex-div" style="width: 4.427rem;margin: 0 auto;">
              <uploader
                :max="max"
                :images="onePhones"
                :handle-click="true"
                :readonly="false"
                :upload-url="uploadUrl"
                name="上传一寸照片"
                :params="params"
                size="small"
                @upload-image="addImageMethod"
              ></uploader>
              <div class="tc tip">
                <p class="grey_7c">一寸照</p>
                <p class="state tc state-done f12" v-if="onePhoneFlag">已上传</p>
                <p class="state tc f12" v-else>未上传</p>
              </div>
            </div>
          </flexbox-item>
        </flexbox>
      </group>
    </div>
  </div>
</template>

<script>
import { Group, Cell, Flexbox, FlexboxItem } from "vux";
import Uploader from "@/components/uploader/uploader.vue";
import storage from "@/utils/common";
import url from "@/api/apiUrl";
import { get, post, execute } from "@/api/server";
export default {
  components: { Group, Cell, Flexbox, FlexboxItem, Uploader },
  data() {
    return {
      max: 1,
      idFaces: [{ url: undefined }],
      idBackFaces: [{ url: undefined }],
      hukouIndeies: [{ url: undefined }],
      hukouPersons: [{ url: undefined }],
      onePhones: [{ url: undefined }],
      idFaceFlag: false,
      idBackFaceFlag: false,
      hukouIndexFlag: false,
      hukouPersonFlag: false,
      onePhoneFlag: false,
      uploadUrl: "http://test.028bmz.com/upload-web/api/upload/img", //接收上传图片的url
      params: {}, //上传时携带的参数,
      memberInfo: []
    };
  },
  computed: {},

  mounted() {},
  created() {
    this.memberInfo = storage.get("member");
    console.log(JSON.stringify(this.memberInfo));
    if (this.memberInfo.idFace) {
      this.idFaces[0].url = this.memberInfo.idFace;
      this.idFaceFlag = true;
    }
    if (this.memberInfo.idBackFace) {
      this.idBackFaces[0].url = this.memberInfo.idBackFace;
      this.idBackFaceFlag = true;
    }
    if (this.memberInfo.hukouIndex) {
      this.hukouIndeies[0].url = this.memberInfo.hukouIndex;
      this.hukouIndexFlag = true;
    }
    if (this.memberInfo.hukouPerson) {
      this.hukouPersons[0].url = this.memberInfo.hukouPerson;
      this.hukouPersonFlag = true;
    }
    if (this.memberInfo.onePhone) {
      this.onePhones[0].url = this.memberInfo.onePhone;
      this.onePhoneFlag = true;
    }
  },
  methods: {
    //预览图片
    previewMethod() {},
    //新增图片
    addImageMethod() {
      if (this.idFaces.length > 0) {
        console.log(JSON.stringify(this.idFaces));
        this.memberInfo.idFace = this.idFaces[this.idFaces.length - 1].url;
        storage.set("member", this.memberInfo);
        this.idFaceFlag = true;
        execute(url.updateMember, { idFace: this.memberInfo.idFace }).then(
          response => {}
        );
      }
      if (this.idBackFaces.length > 0) {
        this.memberInfo.idBackFace = this.idBackFaces[
          this.idBackFaces.length - 1
        ].url;
        storage.set("member", this.memberInfo);
        this.idBackFaceFlag = true;
        execute(url.updateMember, {
          idBackFace: this.memberInfo.idBackFace
        }).then(response => {});
      }
      if (this.hukouIndeies.length > 0) {
        this.memberInfo.hukouIndex = this.hukouIndeies[
          this.hukouIndeies.length - 1
        ].url;
        storage.set("member", this.memberInfo);
        this.hukouIndexFlag = true;
        execute(url.updateMember, {
          hukouIndex: this.memberInfo.hukouIndex
        }).then(response => {});
      }
      if (this.hukouPersons.length > 0) {
        this.memberInfo.hukouPerson = this.hukouPersons[
          this.hukouPersons.length - 1
        ].url;
        storage.set("member", this.memberInfo);
        this.hukouPersonFlag = true;
        execute(url.updateMember, {
          hukouPerson: this.memberInfo.hukouPerson
        }).then(response => {});
      }
      if (this.onePhones.length > 0) {
        this.memberInfo.onePhone = this.onePhones[
          this.onePhones.length - 1
        ].url;
        storage.set("member", this.memberInfo);
        this.onePhoneFlag = true;
        execute(url.updateMember, { onePhone: this.memberInfo.onePhone }).then(
          response => {}
        );
      }
    },
    //移除图片
    removeImageMethod() {}
  }
};
</script>
<style>
.from-group .vux-label {
  font-weight: bold;
}
</style>

<style lang='less' scoped>
.fw {
  font-weight: bold;
}
.insurance-container {
  .vux-flexbox-item {
    &:first-child {
      .weui-cell {
        padding-right: 0px;
      }
    }
    &:last-child {
      .weui-cell {
        padding-left: 0px;
      }
    }
  }
}
.flex-div {
  .state {
    color: @color_base;
  }
  .state-done {
    color: @color_bule;
  }
  div.tip {
    padding-bottom: 10px;
  }
}
</style>
