<template>
  <div class="rater">
    <input v-model="currentValue" style="display:none">
    <a class="rater-box" v-for="i in max" :key="i" 
     :style="{color: colors && colors[i-1] ? colors[i-1] : '#ccc',marginRight:margin+'px',fontSize: fontSize + 'px', width: fontSize + 'px', height: fontSize + 'px', lineHeight: fontSize + 'px'}">
      <span class="rater-inner">
        
          <i class='iconfont  f20' :class="[ cutIndex >= i? 'icon-collected-full star-active' : cutPercent!==0 && cutIndex >= i-1?'icon-star-half star-active':'icon-collected-full']" ></i>
      </span>
    </a>
  </div>
</template>

<script>
export default {
  name: "rater",
  created() {
    this.currentValue =this.value;
  },
  mounted() {},
  props: {
    min: {
      type: Number,
      default: 0
    },
    max: {
      type: Number,
      default: 5
    },
    value: {
      type: [Number, String],
      default: 0
    },
    disabled: Boolean,
    activeColor: {
      type: String,
      default: "#fc6"
    },
    margin: {
      type: Number,
      default: 2
    },
    fontSize: {
      type: Number,
      default: 25
    }
  },
  computed: {
    sliceValue() {
      const _val = this.currentValue.toFixed(1).split(".");
      //console.log(this.currentValue)
      return _val.length === 1 ? [_val[0], 0] : _val;
    },
    //得到第一数
    cutIndex() {
     // console.log(this.sliceValue[1] * 1)
      return this.sliceValue[0] * 1;

    },
    cutPercent() {
      return this.sliceValue[1] * 1;
    }
  },
  methods: {
    
  },
  data() {
    return {
      colors: [],
      currentValue: 0
    };
  },
  watch: {
    value(val) {
      console.log(val)
      this.currentValue = val;
    }
  }
};
</script>

<style>
.rater {
  text-align: left;
  display: inline-block;
  line-height: normal;
}
.rater a {
  display: inline-block;
  text-align: center;
  cursor: pointer;
  color: #ccc;
}

.rater a:last-child {
  padding-right: 2px !important;
  margin-right: 0px !important;
}
.rater a:hover {
  color: #ffdd99;
}
.rater a.is-disabled {
  color: #ccc !important;
  cursor: not-allowed;
}

.rater-box {
  position: relative;
}
.rater-box i.star-active{
  color:#ffab00;
}
.rater-inner {
  position: relative;
  display: inline-block;
}
.rater-outer {
  position: absolute;
  left: 0;
  top: 0;
  display: inline-block;
  overflow: hidden;
}
</style>
