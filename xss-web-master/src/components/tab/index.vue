<template>
   <div class="tabs w1200">
        <div class="tabs-bar clearfix">
            <div :class="['fl',{'current':currentValue==index}]" v-for="(item,index) in navList" :key="index"  @click="handleChange(index)">
                {{item.label}}
            </div>
        </div>
    </div>
</template>

<script type="text/ecmascript-6">
export default {
  components: {},
  props: {
    //接收父组件的navValue
    navValue: {
      type: Number,
      default:0
    },
    navList:{
        type:Array,
        default:[]
    }
  },
  data() {
    return {
      currentValue: this.navValue
    };
  },
  watch: {
   
  },
  computed: {},
  methods: {
    handleChange(index) {
      //改变当前选中的tab，触发watch
      this.currentValue = index;
      //实现子组件与父组件通信
      this.$emit("input", index);
    }
  },
  created() {},
  mounted() {}
};
</script>

<style scoped lang="less">
.tabs-bar {
    >div{
        padding: 10px 10px;
        &.current{
            background: #2ba35c;
            color: #ffffff;
        }
    }
}

</style>
