package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.wxap.util.TenpayUtils;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Member;
import com.xss.domain.Payment;
import com.xss.service.MemberService;
import com.xss.service.OrderService;
import com.xss.service.PaymentService;
import com.xss.service.WeixinService;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * 微信端接口
 * @author zzl
 * @date 2018/8/29
 */
@RestController
@RequestMapping("/weixin")
//@Api(description="微信端接口")
public class WeixinController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(WeixinController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private WeixinService weixinService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private OrderService orderService;


    //@ApiOperation(value = "微信公众号回调接口",notes = "微信公众号回调接口",httpMethod = "POST",produces = "application/json")
    @RequestMapping(value="/callback")
    //@Log(description="微信公众号回调接口:/wx/callback")
    //@ResponseBody
    public void callback(@RequestParam String signature, @RequestParam String timestamp, @RequestParam String nonce,
                                            @RequestParam(required = false) String echostr, @RequestParam(required = false) String code, HttpServletRequest request, HttpServletResponse response)throws Exception{

        System.out.println("echostr2:" + echostr);
        response.setContentType("text/html;charset=UTF-8");

        MultiValueMap map = new MultiValueMap();
        map.put("source", WeixinService.SOURCE);
        map.put("signature", signature);
        map.put("timestamp", timestamp);
        map.put("nonce", nonce);
        map.put("echostr", echostr);
        map.put("code", code);
        map.put("wxRequest", IOUtils.toString(request.getInputStream(), "utf-8"));
        map.put("response", response);
        String callback = weixinService.callback(map);//"{\"errcode\":1,\"data\":\"8682810190950554619\",\"resultCode\":\"valided\",\"errmsg\":\"验证通过\"}";//weixinService.callback(map);
        System.out.println("callback:"+callback);
        if(StringUtils.isNotBlank(callback)){
            JSONObject jsonObject = JSONObject.parseObject(callback);
            System.out.println("errcode:"+jsonObject.getInteger("errcode"));
            if(0==jsonObject.getInteger("errcode")){
                JSONObject data = jsonObject.getJSONObject("data");
                System.out.println("data:" + data.toString());
                if("subscribe".equals(data.getString("type"))){
                    memberService.mergeUserInfoByWeixin(data.getJSONObject("userInfo"), "mp", request);
//                    regist(data.getJSONObject("userInfo"), request, response);
                }else if("unsubscribe".equals(data.getString("type"))){
                    System.out.println("opendId:"+data.getString("opendId"));
                    Member member = memberService.findByOpenId(data.getString("opendId"));
                    if (null != member) {
                        member.setSubscription(Member.Subscription.unSubscribe);
                        try {
                            memberService.update(member);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                String respMessage=data.getString("respMessage");
                System.out.println("respMessage:"+data.getString("respMessage"));
                if(StringUtils.isNotBlank(respMessage)){
                    System.out.println("respMessage:"+data.getString("respMessage"));
                    //respMessage = new String(respMessage.getBytes(), "iso8859-1");
                    response.getWriter().write(respMessage);
                    return;
                }else{
                    return;
                }
            }else if (1==jsonObject.getInteger("errcode")){
                response.getWriter().write(echostr);
                return;
            }
        }
        return;
        //response.getWriter().write(echostr);
    }

    @RequestMapping("/notify")
    public void notify(HttpServletRequest request, HttpServletResponse response) throws IOException, DocumentException, ParseException {
        System.out.println("------------------------------------------notify");
        SAXReader saxReader = new SAXReader();
        String req = IOUtils.toString(request.getInputStream());
        System.out.println("req : " + req);
        StringReader sr = new StringReader(req);
        Document doc = saxReader.read(sr);
        Node outTradeNode = doc.selectSingleNode("/xml/out_trade_no");
        if (outTradeNode != null) {
            String out_trade_no = outTradeNode.getText();
            System.out.println("out_trade_no----" + outTradeNode);
            Payment payment = paymentService.findBySn(out_trade_no);

            SortedMap<String, String> respPackage = new TreeMap<String, String>();
            if (payment != null) {
                //paymentService.handle(payment);
                respPackage.put("return_code", "SUCCESS");
                respPackage.put("return_msg", "OK");
                System.out.println("即时到帐支付成功");
				/*//订单支付
				Order order = payment.getOrder();
				// 注意交易单不要重复处理
				// 注意判断返回金额
				if (PaymentStatus.unpaid.equals(order.getPaymentStatus())) {
					//order.setPaymentName("微信支付");
					//orderService.paidOrder(order.getId());
					orderService.payment(order, payment, null);
					// 给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
					respPackage.put("return_code", "SUCCESS");
					respPackage.put("return_msg", "OK");
					System.out.println("即时到帐支付成功");
				} else {
					System.out.println("order.getState() != Order.State.prePay.getValue()");
					// 给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
					respPackage.put("return_code", "SUCCESS");
					respPackage.put("return_msg", "OK");
					System.out.println("即时到帐支付成功");
				}*/
            } else {
                System.out.println("order==null");
                // 给财付通系统发送成功信息，财付通系统收到此结果后不再进行后续通知
                respPackage.put("return_code", "SUCCESS");
                respPackage.put("return_msg", "OK");
                System.out.println("即时到帐支付失败");
            }

            String res = TenpayUtils.mapToXml(respPackage);
            response.getWriter().write(res);
        }
    }
    @RequestMapping("/jsapi/ticket")
    public PublicResult<String> getJsapiTicket(HttpServletRequest request, HttpServletResponse response) {
        String jo = weixinService.getJsapiTicket();
        if (null == jo){
            return new PublicResult<String>(PublicResultConstant.ERROR, null);
        }
        return new PublicResult<String>(PublicResultConstant.SUCCESS, jo);
    }

    @RequestMapping("/jsapi/sign")
    public PublicResult<JSONObject> getJsapiSign(String url, HttpServletRequest request, HttpServletResponse response) {
        //String url = "http://m.xiaodoushebao.com/index.html#/package/index?id=1&mid=1";
        JSONObject jo = weixinService.getJsapiSign(url);
        if (null == jo){
            return new PublicResult<JSONObject>(PublicResultConstant.ERROR, null);
        }
        return new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, jo);
    }
}
