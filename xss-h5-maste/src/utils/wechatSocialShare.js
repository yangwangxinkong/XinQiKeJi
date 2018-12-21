/**
 * Created by THINK on 2017/11/3.
 */
let wx = require("weixin-js-sdk");
let WeChatShare = {};
WeChatShare.install = ((Vue,options)=>{
    /**
     * 通过当前url去请求获取签名需要的信息
     * @param url:当前的url
     * @param cb:回调函数(用于通过当前url去请求获取签名需要的信息)
     * @returns {Promise}
     */
    Vue.prototype.$reqSignByUrl = function (url,cb) {
        const mUrl = encodeURIComponent(url.split("#")[0])||encodeURIComponent(window.location.href.split('#')[0]);
        return new Promise((resolve,reject)=>{
            resolve(cb(mUrl));
        })
    };
    /**
     *初始化微信分享所需的配置项
     * @param signObj:服务器端返回的签名配置项
     *          appId、timestamp、nonceStr、signature
     * @param isDebug:是否开启分享调试功能,true:开启,false:不开启
     */
    Vue.prototype.$initWXConfig = function (signObj,isDebug) {
      console.log("signObj:" + JSON.stringify(signObj));
        wx.config({
            debug: isDebug,
            appId: signObj.appId, // 和获取Ticke的必须一样------必填，公众号的唯一标识
            timestamp:signObj.timestamp.toString(), // 必填，生成签名的时间戳
            nonceStr: signObj.nonceStr, // 必填，生成签名的随机串
            signature: signObj.signature,// 必填，签名，见附录1
            //需要分享的列表项:发送给朋友，分享到朋友圈，分享到QQ，分享到QQ空间
            jsApiList: [
                'onMenuShareAppMessage','onMenuShareTimeline',
                'onMenuShareQQ','onMenuShareQZone'
            ]
        });
      wx.error(function (res) {
        //alert(res);
      });

    }
    /**
     * 分享到微信、朋友圈、QQ、微博等平台
     * @param shareParams(title、link、image、abstract)
     */
    Vue.prototype.$shareWeChat = function (shareParams) {
        wx.ready(function () {
          wx.updateAppMessageShareData({
            title: shareParams.title, // 分享标题
            desc: shareParams.abstract, // 分享描述
            link: shareParams.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: shareParams.image, // 分享图标
            success: function () {
              // 设置成功
            }
          });
          wx.updateTimelineShareData({
            title: shareParams.title, // 分享标题
            link: shareParams.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
            imgUrl: shareParams.image, // 分享图标
            success: function () {
              // 设置成功
            }
          });
            wx.onMenuShareTimeline({
                title: shareParams.title, // 分享标题
                link: shareParams.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                imgUrl: shareParams.image, // 分享图标
                success: function (res) {

                },
                cancel: function (res) {

                }
            });
            //分享给微信朋友
            wx.onMenuShareAppMessage({
                title: shareParams.title, // 分享标题
                desc: shareParams.abstract, // 分享描述
                link: shareParams.link, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
                imgUrl: shareParams.image, // 分享图标
                type: '', // 分享类型,music、video或link，不填默认为link
                dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
                success: function (res) {

                },
                cancel: function (res) {

                }
            });
            //分享到QQ
            wx.onMenuShareQQ({
                title: shareParams.title, // 分享标题
                desc: shareParams.abstract, // 分享描述
                link: shareParams.link, // 分享链接
                imgUrl: shareParams.image, // 分享图标
                success: function (res) {
                    // 用户确认分享后执行的回调函数
                    logUtil.printLog("分享到QQ好友成功返回的信息为:",res);
                },
                cancel: function (res) {
                    // 用户取消分享后执行的回调函数
                    logUtil.printLog("取消分享给QQ好友返回的信息为:",res);
                }
            });

            //分享到QQ空间
            wx.onMenuShareQZone({
                title: shareParams.title, // 分享标题
                desc: shareParams.abstract, // 分享描述
                link: shareParams.link, // 分享链接
                imgUrl: shareParams.image, // 分享图标
                success: function (res) {

                },
                cancel: function (res) {

                }
            });
        });
    }
})
export default WeChatShare;
