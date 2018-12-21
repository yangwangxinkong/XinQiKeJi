<!--  工具-->
<template>
  <div class="page">
    <div class="container">
      <div class="model-tool-wrap pt10">
        <ul class="div-ul padding-15 clearfix">
          <li>
            <router-link to="/calculator/calculatorInfo?type=SB">
              <div class="tool-box">
                <p class="title ">社保计算器</p>
                <div class="pic">
                  <img class="image" src="../../assets/toolpage_icon_sb@2x.png" alt>
                </div>
              </div>
            </router-link>
          </li>
           <li>
            <router-link to="/calculator/calculatorInfo?type=SB">
              <div class="tool-box">
                <p class="title ">公积金计算器</p>
                <div class="pic">
                  <img class="image" src="../../assets/toolpage_icon_gjj@2x.png" alt>
                </div>
              </div>
            </router-link>
          </li>
           <li>
            <router-link to="/social/inquire">
              <div class="tool-box">
                <p class="title ">社保查询</p>
                <div class="pic">
                  <img class="image" src="../../assets/toolpage_icon_sb_search@2x.png" alt>
                </div>
              </div>
            </router-link>
          </li>
            <li>
            <router-link to="/calculator/calculatorInfo?type=SB">
              <div class="tool-box">
                <p class="title ">常见问题</p>
                <div class="pic">
                  <img class="image" src="../../assets/toolpage_icon_qa@2x.png" alt>
                </div>
              </div>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },

  components: {},

  computed: {},

  mounted() {},

  methods: {}
};
</script>
<style lang='less' scoped>
.model-tool-wrap {
  .div-ul {
    li {
      width: 50%;
      float: left;
      margin-bottom: 10px;
      .tool-box {
        background-color: #fff;
        padding: 15px 10px;
        position: relative;
        box-sizing: border-box;
        height: 2.32rem;
        text-align: left;
        
        border: 1px solid rgba(45,46,70,0.10);
         border-radius: 4px;
      }
      a {
        display: block;
        width: 100%;
        height: 100%;
      }
      &:nth-child(odd) {
        padding-right: 5px;
      }
      &:nth-child(2n) {
        padding-left: 5px;
      }
      p{
          font-size: .427rem;
          color: @font-color;
      }
      .pic {
        width:1.067rem;
        height:1.067rem;
        margin: 0 auto;
        position: absolute;
        bottom: 10px;
        right: 10px;
      }
    }
  }
}
</style>
