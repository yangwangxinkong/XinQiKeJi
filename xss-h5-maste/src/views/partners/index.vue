<template>
	<div class="partners">
		<!--banner图-->
		<div class="banner">
			<img src="../../assets/banner1.png" alt="百万悬赏" />
		</div>
		<div class="cent">
			<!--申请入驻按钮-->
	 	    <div class="margin_50 shenqing_btn1">
	            <img src="../../assets/btn_top1@2x.png" alt="">
	        </div>
	        <!--高佣金：条形分析图-->
	        <div class="imgbox">
	    	    <img src="../../assets/successedwords1@2x.png" alt="高佣金分析图" />
	        </div>
	        <!--常见问答-->
	        <div class="list">
		        <div class="margin_50 shenqing_btn1">
		            <img src="../../assets/btn_top1@2x.png" alt="">
		        </div>
		        <div class="imgbox">
		     	    <img src="../../assets/analy_img1@2x.png" alt="常见问答" />
		        </div>
	        </div>
	        <!--合作优势-->
	        <!--<div class="imgbox">
	        	<img src="../../assets/title_dilog@2x.png" alt="合作优势" />
	        </div>-->
	        <!--问答-->
	        <!--<div class="imgbox">
	        	<img src="../../assets/Q1@2x.png" alt=""  />
	        	<img src="../../assets/Q2@2x.png" alt=""  />
	        	<img src="../../assets/Q3@2x.png" alt=""  />
	        	<img src="../../assets/Q4@2x.png" alt=""  />
	        	<img src="../../assets/Q5@2x.png" alt=""  />
	        </div>-->
	        <!--合作优势-->
	        <div class="list_1">
	        	<div class="margin_50 shenqing_btn1">
		            <img src="../../assets/btn_top1@2x.png" alt="">
		        </div>
		        <div class="imgbox">
		        	<img src="../../assets/img_coop1@2x.png" alt="合作优势" />
		        </div>
	        </div>
	        <!--加盟要求-->
	        <div class="list_2">
		        <div class="margin_50 shenqing_btn1">
			            <img src="../../assets/btn_top1@2x.png" alt="">
			        </div>
		        <div class="imgbox">
		        	<img src="../../assets/8words1@2x.png" alt="加盟要求" />
		        </div>
		    </div>
	         <!--<div class="input_box">
             <input class="input_btn" placeholder="请输入您的城市" v-model="cityName"></input>
             <input class="input_btn" placeholder="请输入您的姓名" v-model="name"></input>
             <input class="input_btn" placeholder="请输入您的手机号" keyboard="number"  ref="refphone"  v-model="mobile" :max="11" is-type="china-mobile">
             </input>-->
	            <!--<input type="text" class="input_btn" id="city" placeholder="请输入您的城市">
	            <input type="text" class="input_btn" id="name" placeholder="请输入您的姓名">
	            <input type="text" class="input_btn" id="tel" placeholder="请输入您的手机号">-->
	        <!--</div>-->
	        <!--<div class="margin_50 shenqing_btn1">
            <span @click.native="register"  type="primary" class="color_base"><img src="../../assets/btn_top@2x.png" alt=""></span>

	        </div>-->
	    </div>
	    <div class="footer">
	    	<div class="foot">
	        	<span>年入百万不是梦，赶快加入吧！</span>
	       </div>
	       <div class="footer-bootom" style="background-color: rgba(125,148,251,0.5);">
		        <div class="footer_span">全国服务热线周一至周日9:00-21:00</div>
		        <div class="footer_span">400-9999-075</div>
		        <div class="footer_span">详情请咨询"合伙人"客服</div>
		        <div class="footer_span">微信号：leleke869</div>
		        <div style="overflow: hidden;">
		            <div class="code">
		                <img src="../../assets/login-qrcode.png" alt="">
		            </div>
		        </div>
	        </div>
	        <div class="link_a">
	            <a href="http://m.xiaodoushebao.com/">小豆社保官网</a> <span>|</span>
	            <a href="http://m.xiaodoushebao.com/#/order/orderMember?type=SB"> 缴社保</a> <span>|</span>
	            <a href="http://m.xiaodoushebao.com/#/order/orderMember?type=GJ">缴公积金</a>
	        </div>
	        <div>
	            <span>www.xiaodoushebao.com</span>
	        </div>
	    </div>
	</div>
</template>
<script>
  import { XInput, XButton,CheckIcon } from "vux";
  import { getToken, setToken, removeToken } from '@/utils/auth'
  import storage from "@/utils/common";
  import url from "@/api/apiUrl";
  import { get, post, execute } from "@/api/server";

  export default {
    data() {
      return {
        mobile: "",//手机号码
        cityName:'',//城市名称
        name:'',//姓名
        redirectUrl:'/'
      };
    },
    components: { XInput, XInput,XButton,CheckIcon },

    computed: {},

    mounted() {},
    created() {
      this.redirectUrl = this.$route.query.redirect;
    },
    methods: {
      register(){
        if(this.cityName == ''){
          this.$vux.toast.text('请输入城市名称', 'middle')
        }else if(this.name == ''){
          this.$vux.toast.text('请输入姓名', 'middle')
        }else if(this.mobile==""){
          this.$vux.toast.text('请输入手机号', 'middle')
        }else if(!this.$refs.refphone.valid){
          this.$vux.toast.text('请输入正确手机号', 'middle')
        }else{
          const params = {
            mobile:this.mobile,
            cityName:this.cityName,
            name:this.name
          }
          // 调用接口
          post(url.partnerRegister, params).then(response => {
            console.log("register response:" + JSON.stringify(response));
            if(response.data.result=="00000000"){
              const data = response.data.data;
              setToken(data.token)
              storage.set("member", data);
              //console.log("redirectUrl:" + this.redirectUrl);
              if(this.redirectUrl != undefined && this.redirectUrl != ""){
                this.$router.push({ path: this.redirectUrl })
              }else{
                this.$router.push({ path: '/' })
              }
            }else{
              this.$vux.toast.text(response.data.msg);
            }
          });
        }
      }
    }
  };
</script>

 <style>
				body{ border: none; max-width: 100%; width: 100%; height: 100%; -ms-interpolation-mode: bicubic; font-size: 1.2rem; }
				/*ul, li { list-style: none; }*/
				/*body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,code,form,fieldset,legend,input,button,textarea,p,blockquote,th,td { margin:0; padding:0; }*/
				h1, h2, h3, h4, h5, h6 { font-weight:normal;  }
				/*em,i{font-style: normal;}*/
				a,input{
				    text-decoration: none;
				    color:black;
				    -webkit-tap-highlight-color:transparent;
				}
				a,input{-webkit-tap-highlight-color:rgba(255,0,0,0);}
				img {
					 border:none;
					 max-width: 100%;
					 }
			/*	ol,ul,li { list-style:none; }*/
				input, textarea, select, button { font:13px Verdana,Helvetica,Arial,sans-serif; border: 1px solid #999; }
				table { border-collapse:collapse; }
				.clearFix:after {
				    content: "";
				    display: block;
				    clear:both;
				}
				.partners{
				    height: 100%;
				    -webkit-text-size-adjust: 100%;
				    -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
				    background: #6d94ff;
				}

				.banner{
				    position: relative;
				}
				.banner img{
				    width: 100%;
				    height: 15.333333333333333rem;
				    position: relative;
				}
				.cent{
				    text-align: center;
				}
				.margin_50{
				    margin: 0.6666666666666666rem;
				}
				.margin_50 img{
				    position: relative;
                    bottom: 36px;
                    padding: 0px;
                    border-radius: 0.5rem;
				}
				.imgbox{
				    background: #6d94ff;
				    padding: 2rem 1.44444rem;
				    z-index: 99999;
				}
				.imgbox img{
				    margin-top: 0.5066666666666667rem;
			        border-radius: 0.2rem;
				    position: relative;
				    bottom: 95px;
				}
				.list{
					position: relative;
                    bottom: 80px;
				}
				.list_1{
					position: relative;
                    bottom: 160px;
				}
				.list_2{
					position: relative;
                    bottom: 240px;
				}
				/*.margin_30{
				    margin:0 0.4rem;
				}
				.imgbox_1{
				    margin: 0.5333333333333333rem;
				}
				
				.input_box{
				    text-align: center;
				    margin-top: 15px;
				}
				.input_btn{
				    line-height: 2.2rem;
				    display: block;
				    margin: 15px 35px;
				    height: 2.2rem;
				    border-radius: 0.9rem;
				    background: white;
				    border: none;
				    font-size: 1.1rem;
				    padding: 0px 60px 0px 15px;
				}*/
				.footer{
				    text-align: center;
				    padding: 0.4rem 0.5333333333333333rem;
				    font-size: 0.26666666666666666rem;
				    color: #F1F1F1;
				    position: relative;
    				bottom: 365px;
    				background: url(../../assets/qqq_03.png) no-repeat;
    				background-size:100% 100%;
				}
				.foot{
					padding:0px;
				}
				.foot span{
					font-size: 19px;
					font-family: 微软雅黑;
					color: #fff36b;
					font-weight: bold;
				}
				.footer-bootom{
					width: 330px;
				    height: 210px;
				    margin-left: 13px;
				    padding-top: 5px;
				    display: inline-block;
				    
				}
				.footer_span{
					font-size: 0.8888888rem;
				    font-weight: 300;
				}
				/*.footer_logo{
				    color: #666666;
				    font-size: 0.6rem;
				    font-weight: bold;
				}
				.tel{
				    font-size: 0.4266666666666667rem;
				    color: #5a87f2;
				    font-weight: bold;
				}*/
				.code{
				    width: 7.5rem;
				    display: inline-block;
				    margin-top: 0.1rem;
				    text-align: center;
				}
				.code img{
				    width: 200px;
				    height: 110px;
				}
				.code span{
					text-align: center;
				}
				span{
					color: #3938b3;
				}
				.link_a a{
				    color: #3938b3;
				    font-size: 0.25333333333333335rem;
				}
				/*.banner1_btn1{
				    width: 2.2rem;
				    position: absolute;
				    bottom: 1.3rem;
				    left: 0.3rem;
				    background: none;
				    border: 1px solid white;
				}

				.banner1_btn2{
				    left: 3rem;
				}*/


</style>
