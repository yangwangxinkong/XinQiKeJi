<!--设置  -->
<template>
  <div class='page'>
      <div class="container bg_f">
           <group :gutter="0">
                <cell title="头像">
                    <cropper :headerImage="memberInfo.src"  @getHeaderImage="newHeaderImage"></cropper>
                </cell>
                <cell title="昵称" :value="memberInfo.name" ></cell>
                <cell title="绑定手机" :value="memberInfo.mobile" is-link></cell>
                <cell title="退出" link="/login/index"></cell>
            </group>
      </div>
  </div>
</template>

<script>
import { Panel, Cell,Group } from 'vux'
import cropper from '@/components/cropper/index'
export default {
  data () {
    return {
      memberInfo: {
          src: 'http://placeholder.qiniudn.com/60x60/3cc51f/ffffff',
          fallbackSrc: 'http://placeholder.qiniudn.com/60x60/3cc51f/ffffff',
          name: '刘德华',
          username: '18201077251',
          mobile:'18201077251'
      },
    };
  },

  components: {Panel, Cell,Group,cropper},

  computed: {},

  mounted(){},

  methods: {
     onImgError (item, $event) {
            this.$emit('on-img-error', JSON.parse(JSON.stringify(item)), $event)
            if (item.fallbackSrc) {
                $event.target.src = item.fallbackSrc
            }
        },
        newHeaderImage(newImg){
            this.memberInfo.src = newImg;
        }
  }
}

</script>
<style lang='less' scoped>
</style>
