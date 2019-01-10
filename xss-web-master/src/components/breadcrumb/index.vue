<template>
    <div class="crumbs-nav-main clearfix">
        <div class="warp_main">
            <div class="breadcrumbs w1200 pr">
                <a href="/">首页</a>
                <span class="breadcrumb-item" v-for="(item,index) in routerList" :key="index">
                    <span class="sep">&gt;</span>
                    <a :href="item.linkUrl" :class="{'on':index===routerList.length-1}">{{item.value}}</a>
                </span>
                <div v-if="slotRight" class="breadcrumb-action pa">
                    <a href="javascript:void(0)" @click="btnClick" class="btn btn-orange ">{{title}}</a>
                </div> 
            </div>
        </div>
    </div>
    
</template>

<script type="text/ecmascript-6">
import eventVue from '../../modules/js/eventBus.js'
export default {
  components:{},
  props:{
    value:{
        type:String,
        default:''
    },
    linkUrl:{
        type:String,
        default:''
    },
    routerList:{
        type:Array,
        default:[]
    },
    slotRight:{
        type:Boolean,
        default:false
    },
    title:{
        type:String,
        default:'我要交社保'
    },
   
  },
  data(){
    return {
    }
  },
  watch:{
    
  },
  computed:{},
  methods:{
      btnClick(){
        eventVue.$emit('PopShowTap',true)
      }
  },
  created(){
    // console.log(this.routerList)
  },
  mounted(){}
}
</script>

<style  lang="less">
/*公用面包屑的样式*/
.warp_main{
	background: transparent;
}
.warp_main .breadcrumbs{
	width: 1200px;
	margin: 0 auto;
}
 .breadcrumbs {
	height: 40px;
	font-size: 12px;
	line-height: 40px;
	color: #616161;
}
.breadcrumbs a {
	color: #757575;
}
.breadcrumbs  .sep {
	margin: 0 0.5em;
	color: #b0b0b0;
	font-family: simsun;
    font-style: normal;
}
.breadcrumbs  a.on{
       color: #333;
}
.breadcrumb-action{
    top:0;
    right: 0;
    a.btn{
    color: #ffffff
    }
}
</style>
