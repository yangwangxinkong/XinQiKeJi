package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.*;
import com.xss.service.*;
import com.xss.util.WebUtils;
import com.wxap.util.Sha1Util;
import com.wxap.util.TenpayUtils;
import com.xss.util.page.Filter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.StringReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Controller - 支付
 */
@RestController
@RequestMapping("/m/payment")
@Api(description="移动端支付相关接口")
public class PaymentController {

	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(PaymentController.class);

	@Resource
	private OrderService orderService;
	@Resource
	private PaymentService paymentService;
	@Resource
	private PaymentMethodService paymentMethodService;
	@Resource
	private WeixinService weixinService;
	@Resource
	private ConfigsService configsService;

	@ApiOperation(value = "支付页面数据",notes = "订单支付页获取数据",httpMethod = "GET",produces = "application/json")
	@GetMapping("/pay_info")
	@Log(description="移动端获取支付方式列表接口:/m/payment/pay_info")
	public PublicResult payInfo(Long orderId) {
		LOG.info("Method payInfo params! orderId="+orderId);
		if(orderId==null){
			return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
		}
		try {
			JSONObject resultData = new JSONObject();
			//1. 获取订单id、编号、订单类型、应付金额
			Order order = orderService.find(orderId);
			JSONObject orderJson = new JSONObject();
			orderJson.put("id", order.getId());
			orderJson.put("sn", order.getSn());
//			orderJson.put("orderType", order.getOrderType().name());
			orderJson.put("paymethod", order.getPaymentMethod().getCode());
			BigDecimal amountPayable;
			//    判断订单类型，调用不同方法获取订单 应付金额
			amountPayable = order.getAmountPayable();
			orderJson.put("amountPayable", amountPayable);
			resultData.put("order", orderJson);

			//2. 获取所有支付方式
			List<PaymentMethod> paymentMethodList = paymentMethodService.findAll();
			JSONArray paymentMethods = paymentMethodService.createEntity().convertList(paymentMethodList, PaymentMethod.DEFAULT_JSON_PARAMS);
			resultData.put("paymentMethods", paymentMethods);

			return new PublicResult<>(PublicResultConstant.SUCCESS, resultData);
		} catch (Exception e) {
			LOG.error("Method payInfo exception! e : "+e);
			return new PublicResult<>(PublicResultConstant.ERROR, null);
		}

	}


	@ApiOperation(value = "会员充值页面数据",notes = "会员充值获取数据",httpMethod = "GET",produces = "application/json")
	@GetMapping("/recharge")
	@Log(description="移动端获取支付方式列表接口:/m/payment/recharge")
	public PublicResult recharge(@CurrentUser Member member) {

		try {
			JSONObject resultData = new JSONObject();
			Configs config = configsService.getConfigsByCode(Configs.DEFAULT_VIP_RECHARGE_FEE_CODE);
			if (null != config && StringUtils.isNotBlank(config.getCodeValue()) && new BigDecimal(config.getCodeValue()).compareTo(BigDecimal.ZERO) > 0){
				resultData.put("amount", config.getCodeValue());
				//2. 获取所有支付方式
				List<Filter> filters = new ArrayList<>();
				filters.add(Filter.eq("method", PaymentMethod.Method.online));
				List<PaymentMethod> paymentMethodList = paymentMethodService.findList(null, null, filters, null);
				JSONArray paymentMethods = paymentMethodService.createEntity().convertList(paymentMethodList, PaymentMethod.DEFAULT_JSON_PARAMS);
				resultData.put("paymentMethods", paymentMethods);

				return new PublicResult<>(PublicResultConstant.SUCCESS, resultData);
			}else{
				return new PublicResult<>(PublicResultConstant.ERROR, null);
			}
		} catch (Exception e) {
			LOG.error("Method payInfo exception! e : "+e);
			return new PublicResult<>(PublicResultConstant.ERROR, null);
		}

	}



	@ApiOperation(value = "第三方在线支付",notes = "第三方在线支付",httpMethod = "GET",produces = "application/json")
	@GetMapping("/online")
	@Log(description="第三方在线支付接口:/m/payment/online")
	public PublicResult<Object> payOnline(Payment.Type type, String code, String sn, BigDecimal amount, @CurrentUser Member member, HttpServletRequest request) {
		PublicResult<Object> result = null;
		if (StringUtils.isBlank(code)){
			result = new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "支付方式不正确");
			return result;
		}
		System.out.println("code:" + code);
		PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodDao().findByCode(code.split("_")[0]);
		if (null == paymentMethod){
			result = new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "支付方式不正确");
			return result;
		}
		if (Payment.Type.payment.equals(type)) {
			Order order = orderService.findBySn(sn);
			if (null == order) {
				result = new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "订单号不正确");
				return result;
			}
//		if(!paymentMethod.getId().equals(order.getPaymentMethod().getId())){
			order.setPaymentMethod(paymentMethod);
			order.setPaymentMethodName(paymentMethod.getName());
			orderService.update(order);
//		}
			result = paymentService.save(member, type, order, paymentMethod, amount);
		}else{
			result = paymentService.save(member, type, null, paymentMethod, amount);
		}

		if (result.getResult().equals(PublicResultConstant.SUCCESS.getResult())){
			Payment payment = (Payment) result.getData();
			if ("weixin_h5".equals(code)){
				//微信支付-h5
				result = weixinH5Pay(member, payment, request);
			}else if ("weixin_mp".equals(code)){
				//微信支付-公众号
				result = weixinPay(member, payment, request);
			}else if ("alipay_h5".equals(code)){
				result = alipayH5Pay(member, payment, request);
			}else{
				result.setData(null);
			}
		}
		return result;
	}

	private PublicResult<Object> weixinPay(Member member, Payment payment, HttpServletRequest request){
		PublicResult<Object> result = null;
		JSONObject payInfo = new JSONObject();
		try
		{
			String wxConfig = weixinService.getConfig();

			JSONObject wxConfigJo = JSONObject.parseObject(wxConfig).getJSONObject("data");
			String openid = member.getOpenId();
			System.out.println("openid:" + openid);
			String noncestr = Sha1Util.getNonceStr();

			String totalFee = "1";
			if (Payment.Type.payment.equals(payment.getType())) {
				Order order = orderService.find(payment.getOrder().getId());
				//应付金额
				BigDecimal paymentAmount;
				//判断订单类型，调用不同方法获取订单 应付金额
				paymentAmount = order.getAmountPayable();
				totalFee = String.valueOf((paymentAmount).multiply(new BigDecimal("100"))
						.setScale(0, RoundingMode.HALF_UP));
			} else if (Payment.Type.recharge.equals(payment.getType())){
				totalFee = String.valueOf((payment.getAmount()).multiply(new BigDecimal("100"))
						.setScale(0, RoundingMode.HALF_UP));
			} else {
				result = new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "支付类型不正确");
				return result;
			}
			System.out.println("totalFee:" + totalFee);
			SortedMap<String, String> respPackage = new TreeMap<String, String>();

			respPackage.put("appid", wxConfigJo.getString("appId"));
			respPackage.put("body", "订单支付");
			respPackage.put("device_info", "WEB");
			respPackage.put("mch_id", wxConfigJo.getString("partner"));
			respPackage.put("nonce_str", noncestr);
			respPackage.put("notify_url", "http://api.xiaodoushebao.com/m/payment/notify/weixin/" + payment.getSn());
			if (StringUtils.isNotBlank(openid)) {
				respPackage.put("openid", openid);
			}
			respPackage.put("out_trade_no", payment.getSn());
			String ip = WebUtils.getIpAddr(request);
			respPackage.put("spbill_create_ip", ip);
			respPackage.put("total_fee", totalFee);
			respPackage.put("trade_type", "JSAPI");

			String sign = TenpayUtils.createMd5Sign(respPackage, wxConfigJo.getString("partnerKey"));
			System.out.println("sign : " + sign);
			respPackage.put("sign", sign);

			String xml = TenpayUtils.mapToXml(respPackage);
			System.out.println("xml--" + xml);

			String res = TenpayUtils.callForApi(TenpayUtils.unifiedorder, xml);

			SAXReader saxReader = new SAXReader();
			System.out.println("unifiedorder : " + res);
			StringReader sr = new StringReader(res);
			Document doc = saxReader.read(sr);
			Node recNode = doc.selectSingleNode("/xml/return_code");
			Node remNode = doc.selectSingleNode("/xml/return_msg");
			Node repNode = doc.selectSingleNode("/xml/prepay_id");
			Node codeNode = doc.selectSingleNode("/xml/code_url");
			if (recNode != null && remNode != null && repNode != null) {
				if ("SUCCESS".equalsIgnoreCase(recNode.getText()) && "OK".equalsIgnoreCase(remNode.getText())) {

					SortedMap<String, String> signMap = new TreeMap<String, String>();
					signMap.put("appId", wxConfigJo.getString("appId"));
					signMap.put("nonceStr", noncestr);
					signMap.put("package", "prepay_id="+repNode.getText());
					signMap.put("signType", "MD5");
					signMap.put("timeStamp", System.currentTimeMillis()/1000L+"");
					String sign1 = TenpayUtils.createMd5Sign(signMap, wxConfigJo.getString("partnerKey"));

					payInfo.put("prepayId", repNode.getText());
					payInfo.put("appId", wxConfigJo.getString("appId"));
					payInfo.put("timeStamp", System.currentTimeMillis()/1000L+"");
					payInfo.put("nonceStr", noncestr);
					payInfo.put("package", "prepay_id="+repNode.getText());
					payInfo.put("signType", "MD5");
					payInfo.put("paySign", sign1);
					result = new PublicResult<Object>(PublicResultConstant.SUCCESS, payInfo);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = new PublicResult<Object>(PublicResultConstant.ERROR, "系统错误");
		}

		return result;
	}

	/**
	 * h5非微信浏览器微信支付
	 * @param member
	 * @param payment
	 * @return
	 */
	private PublicResult<Object> weixinH5Pay(Member member, Payment payment, HttpServletRequest request){
		PublicResult<Object> result = null;
		JSONObject payInfo = new JSONObject();
		try
		{
			String wxConfig = weixinService.getConfig();

			JSONObject wxConfigJo = JSONObject.parseObject(wxConfig).getJSONObject("data");
			String openid = member.getOpenId();
			System.out.println("openid:" + openid);
			String noncestr = Sha1Util.getNonceStr();

			String totalFee = "1";
			if (Payment.Type.payment.equals(payment.getType())) {
				Order order = orderService.find(payment.getOrder().getId());
				//应付金额，//判断订单类型，调用不同方法获取订单 应付金额
				BigDecimal paymentAmount = order.getAmountPayable();
				totalFee = String.valueOf((paymentAmount).multiply(new BigDecimal("100"))
						.setScale(0, RoundingMode.HALF_UP));
			} else if (Payment.Type.recharge.equals(payment.getType())){
				totalFee = String.valueOf((payment.getAmount()).multiply(new BigDecimal("100"))
						.setScale(0, RoundingMode.HALF_UP));
			}else {
				result = new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, "支付类型不正确");
				return result;
			}

			SortedMap<String, String> respPackage = new TreeMap<String, String>();

			//微信分配的公众账号ID（企业号corpid即为此appId）
			respPackage.put("appid", wxConfigJo.getString("appId"));
			//微信支付分配的商户号
			respPackage.put("mch_id", wxConfigJo.getString("partner"));
			//终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
			respPackage.put("device_info", "WEB");
			//随机字符串，不长于32位。
			respPackage.put("nonce_str", noncestr);
			//商品简单描述
			respPackage.put("body", "小豆社保订单支付");
			//商户系统内部的订单号,32个字符内、可包含字母
			respPackage.put("out_trade_no", payment.getSn());
			//订单总金额，单位为分
			respPackage.put("total_fee", totalFee);
			//必须传正确的用户端IP
			String ip = WebUtils.getIpAddr(request);
			System.out.println("ip:" + ip);
			respPackage.put("spbill_create_ip", ip);
			//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
			respPackage.put("notify_url", "http://api.xiaodoushebao.com/m/payment/notify/weixin/" + payment.getSn());
			//H5支付的交易类型为MWEB
			respPackage.put("trade_type", "MWEB");
			//场景信息
			String screneInfo = "{\"h5_info\":{\"type\":\"Wap\",\"wap_url\":\"http://m.xiaodoushebao.com\",\"wap_name\":\"小豆社保\"}}";
			respPackage.put("scene_info", screneInfo);

			String sign = TenpayUtils.createMd5Sign(respPackage, wxConfigJo.getString("partnerKey"));
			System.out.println("sign : " + sign);
			respPackage.put("sign", sign);

			String xml = TenpayUtils.mapToXml(respPackage);
			System.out.println("xml--" + xml);

			String res = TenpayUtils.callForApi(TenpayUtils.unifiedorder, xml);

			SAXReader saxReader = new SAXReader();
			System.out.println("unifiedorder : " + res);
			StringReader sr = new StringReader(res);
			Document doc = saxReader.read(sr);
			Node recNode = doc.selectSingleNode("/xml/return_code");
			Node remNode = doc.selectSingleNode("/xml/return_msg");
			Node mwebUrl = doc.selectSingleNode("/xml/mweb_url");
			if (recNode != null && remNode != null && mwebUrl != null) {
				if ("SUCCESS".equalsIgnoreCase(recNode.getText()) && "OK".equalsIgnoreCase(remNode.getText())) {
					String mwebUrlStr = mwebUrl.getText();
//					String redirectUrl = "http://m.xiaodoushebao.com/#/member/order/orderDetail?id=" + payment.getOrder().getId();

					String redirectUrl = "http://m.xiaodoushebao.com/#/recharge/result";
					System.out.println("payment type:" + payment.getType());
					if (Payment.Type.payment.equals(payment.getType())) {
						redirectUrl = "http://m.xiaodoushebao.com/#/order/paymentResult?id=" + payment.getOrder().getId() + "&sn=" + payment.getOrder().getSn();
					}
					redirectUrl = URLEncoder.encode(redirectUrl,"UTF-8");
					System.out.println("redirectUrl:" + redirectUrl);
					mwebUrlStr += "&redirect_url=" + redirectUrl;
					payInfo.put("mwebUrl", mwebUrlStr);
					result = new PublicResult<Object>(PublicResultConstant.SUCCESS, payInfo);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			result = new PublicResult<Object>(PublicResultConstant.ERROR, "系统错误");
		}

		return result;
	}

	/**
	 * 支付宝支付-h5
	 * @param member
	 * @param payment
	 * @return
	 */
	private PublicResult<Object> alipayH5Pay(Member member, Payment payment, HttpServletRequest request){
		PublicResult<Object> result = null;
		JSONObject payInfo = weixinService.getAlipayH5Form(payment.getAmount().toString(), payment.getSn(), payment.getOrder().getId(), payment.getType(), "小豆社保订单支付");
		try
		{
			if(payInfo.containsKey("form")){
				result = new PublicResult<Object>(PublicResultConstant.SUCCESS, payInfo);
			}else{
				result = new PublicResult<Object>(PublicResultConstant.ERROR, "系统错误");
			}
		} catch (Exception ex) {
			result = new PublicResult<Object>(PublicResultConstant.ERROR, "系统错误");
		}

		return result;
	}

	/**
	 * 通知
	 */
	@ApiOperation(value = "第三方支付通知回调",notes = "第三方支付通知回调",httpMethod = "GET",produces = "application/json")
	@PostMapping("/notify/{payMethod}/{sn}")
	@Log(description="第三方支付通知回调接口:/m/payment/notify")
	public void notify(@PathVariable String payMethod, @PathVariable String sn, HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("notify sn---" + sn + "");
		LOG.info("notify sn:" + sn + ";payMethod:" + payMethod);
		try {
			Payment payment = paymentService.findBySn(sn);
			if (payment != null) {
				String tradeNo = request.getParameter("trade_no");
				LOG.info("tradeNo:" + tradeNo);

				payment.setTradeNo(tradeNo);
				PaymentMethod paymentMethod = paymentMethodService.getPaymentMethodDao().findByCode(payMethod);
				if (paymentMethod != null) {
//				if (paymentPlugin.verifyNotify(sn, notifyMethod, request)) {
					paymentService.handle(payment);
//				}
				}
				response.getWriter().write("success");
			}else{
				response.getWriter().write("fail");
			}
		}catch(Exception e){
			e.printStackTrace();
			response.getWriter().write("fail");
		}
	}

}