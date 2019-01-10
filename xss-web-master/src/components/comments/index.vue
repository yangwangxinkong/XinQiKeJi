<template>
 <div class="comments-article w1200">
    <div class="comments-title mb10 clearfix">
        <h5 class="f20">最新评论<span class="num">（10）</span></h5>
    </div>
    <div class="comments-container">
        <div class="comments-list mt10 mb20">
            <div class="comments-item clearfix">
                <div class="pull-left fl">
                    <img class="avatar image" src="https://avatar-static.segmentfault.com/429/243/4292433912-58b8d852dc722_big64" alt="">
               </div>
                <div class="comments-content ">
                    <div class="comment-trigger pr">
                        <strong class="comments-isauthor f14 grey-3">落花无声</strong>
                        <p class="comments-date grey-be">2018-05-06</p>
                        <span class="comment-action pa"><i class="iconfont icon-zan"></i>&nbsp;&nbsp;10</span>   
                    </div>
                    <div class="comment-txt mt10">
                       <p class="f14">这家设计公司确实挺有实力，家里就是找他们设计的非常不错！</p> 
                    </div>
                </div>
            </div>
           <div class="comments-item clearfix">
                <div class="pull-left fl">
                    <img class="avatar image" src="https://avatar-static.segmentfault.com/429/243/4292433912-58b8d852dc722_big64" alt="">
               </div>
                <div class="comments-content ">
                    <div class="comment-trigger pr">
                        <strong class="comments-isauthor f14 grey-3">落花无声</strong>
                        <p class="comments-date grey-be">2018-05-06</p>
                        <span class="comment-action pa"><i class="iconfont icon-zan"></i>&nbsp;&nbsp;10</span>   
                    </div>
                    <div class="comment-txt mt10">
                       <p class="f14">这家设计公司确实挺有实力，家里就是找他们设计的非常不错！</p> 
                    </div>
                </div>
            </div>
            <div class="comments-item clearfix">
                <div class="pull-left fl">
                    <img class="avatar image" src="https://avatar-static.segmentfault.com/429/243/4292433912-58b8d852dc722_big64" alt="">
               </div>
                <div class="comments-content ">
                    <div class="comment-trigger pr">
                        <strong class="comments-isauthor f14 grey-3">落花无声</strong>
                        <p class="comments-date grey-be">2018-05-06</p>
                        <span class="comment-action pa"><i class="iconfont icon-zan"></i>&nbsp;&nbsp;10</span>   
                    </div>
                    <div class="comment-txt mt10">
                       <p class="f14">这家设计公司确实挺有实力，家里就是找他们设计的非常不错！</p> 
                    </div>
                </div>
            </div>
        </div>
        <!-- 查看更多 -->
        <div class="look-more tc mb20">
            <a href="javascript:void(0);" class="btn btn-line btn-grey btn-radius">查看更多</a>
        </div>
        <!-- 评论的输入框 -->
        <div class="comments-box">
            <div class="comments-box-content">
             <h4 class="f16">我的评论</h4>
              <form action="."  class="" name="search" @submit.prevent="$emit('on-submit', value)">
                    <div class="form-group mb0">
                        <textarea name="text" rows="3"  
                         v-model="currentValue"  
                         class="form-control"
                         placeholder="我也说的什么"
                        @input="onComposition($event, 'input')"
                          ></textarea>
                        <div class="mt15 text-right">
                            <button class=" btn btn-green btn-radius f14" >发布评论</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
   
 </div>
</template>

<script type="text/ecmascript-6">
export default {
  components: {},
  props: {
    value: {
      type: String,
      default: ""
    }
  },
  data() {
    return {
     currentValue: "",
     onInput: false,
    };
  },
  watch: {
    value(val) {
      this.currentValue = val;
    }
  },
  computed: {},
  methods: {
    emitEvent() {
      this.$nextTick(() => {
        this.$emit("input", this.currentValue);
      });
    },
    onComposition($event, type) {
      if (type === "input") {
        if (!this.onInput) {
          this.emitEvent();
        }
      }
    }
  },
  created() {
    if (this.value) {
      this.currentValue = this.value;
    }
  },
  mounted() {}
};
</script>

<style scoped lang="less">
.comments-article {
  .comments-container {
    border: 0;
    box-shadow: none;
    border-top: 1px solid rgba(0, 0, 0, 0.09);
    margin-bottom: 25px;
    .comments-item {
      padding: 15px 0px;
      border-bottom: 1px solid rgba(0, 0, 0, 0.09);
    }
    .pull-left {
      height: 40px;
      overflow: hidden;
      .avatar {
        width: 40px;
        height: 40px;
        border-radius: 50%;
      }
    }
    .comments-content {
      padding-left: 56px;
      .comment-action {
        right: 0;
        top: 8px;
      }
    }
    .comments-box {
      background: #fafafa;
      box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.09);
      border-radius: 2px 2px 0px 0px;
      padding: 15px 20px;
      border-top: 1px solid rgba(0, 0, 0, 0.09);
      margin-top: -1px;
      textarea {
        max-height: 132px;
        min-height: 90px;
      }
    }
  }
}
</style>
