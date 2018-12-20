<template>
  <el-cascader
    :options="options"
    v-model="selectOptions"
    @change="handleAreaChange"
    @active-item-change="addSelect"
    :change-on-select="true">
  </el-cascader>
</template>
<script>
import { getAreaOptions } from '@/api/sys'
import $ from 'jquery';
import { getToken } from '@/utils/auth'
export default {
  name: 'areaCascader',
  props: {

  },
  data() {
    return {
      options:[],
      selectOptions:[],
      pIndexArr:[]
    }
  },
  created() {
    this.initProvince() //省份
  },
  methods: {
    //获取省份的方法
    initProvince() {
      getAreaOptions({}).then(response => {
        this.options=response.data.data.areas
        this.addSelect()
      })
    },
    addSelect(){
      this.pIndexArr = []
      let val = this.selectOptions;
      for(let i=0;i<val.length;i++){
        var childrenData = this.getChildren(val[i]);
        this.addChildren(val[i],childrenData);
      }
    },
    addChildren(parentId, children){
      if(children.length > 0){
        if(this.pIndexArr.length == 0){
          this.options.forEach((item,index)=>{
            if(item.value==parentId){
              this.pIndexArr[0] = index;
              item.children=children
            }
          })
        }else{
          var areaObj = this.options;
          if(this.pIndexArr.length > 0) {
            for(var j in this.pIndexArr){
              var idx = this.pIndexArr[j];
              areaObj = areaObj[idx].children
            }
            if(areaObj){
              areaObj.forEach((item, index) => {
                if (item.value == parentId) {
                  this.pIndexArr[this.pIndexArr.length] = index;
                  item.children = children
                }
              })
            }
          }
        }
      }
    },
    getChildren(parentId){
      var dd = [];
      let param={
        parentId:parentId
      }
      $.ajax({
        url: process.env.BASE_API + '/api/area/areaOptions',
        type:'GET', //GET
        async:false, //或false,是否异步
        data:param,
        timeout:5000, //超时时间
        dataType:'json', //返回的数据格式：
        beforeSend:function(xhr){
          headers:["Authorization", getToken()]
        },
        success:function(data,textStatus,jqXHR){
          dd = data.data.areas;
        },
        error:function(xhr,textStatus){
          console.log("err:" + textStatus)
        },
        complete:function(){
        }
      })
      return dd
    },
    //点击上一级地区获取下一级地区列表
    handleAreaChange(val){
      this.addSelect(val)
      this.selectOptions=val
      this.$emit('successCBK', val);
    }
  }
}
</script>
