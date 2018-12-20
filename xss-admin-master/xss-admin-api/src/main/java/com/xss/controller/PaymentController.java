/*
 *  
 *  
 *  
 */
package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.annotation.Pass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.Payment;
import com.xss.service.OrderService;
import com.xss.service.PaymentService;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller - 订单
 */
@RestController
@RequestMapping("/api/payment")
@Api(description="订单模块 - 收款管理")
public class PaymentController {

	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(PaymentController.class);

	@Autowired
    private PaymentService paymentService;
	@Autowired
	private OrderService orderService;

	/**
	 * 列表
	 */
	@ApiOperation(value="收款管理列表", notes="body体参数,不需要Authorization",produces = "application/json")
	@GetMapping("/list")
	@Log(description="前台收款列表接口:/list")
	@Pass
	public PageResult<Object> list(Pageable pageable, String sn, String payer, Integer typeId, @CurrentUser Admin admin) {

		LOG.debug("get Member list param = " + pageable.toString());

		PageResult<Object> result = null;

		try{

			if (StringUtils.isNotEmpty(sn)){
				pageable.getFilters().add(Filter.like("sn", "%" + sn + "%"));
			}
			if (StringUtils.isNotEmpty(payer)){
				pageable.getFilters().add(Filter.like("payer", "%" + payer + "%"));
			}
			if (typeId != null){
				pageable.getFilters().add(Filter.eq("type", Payment.Type.findByValue(typeId)));
			}

			Page<Payment> paymentPage = paymentService.findPage(pageable);

			result = new PageResult<Object>((int)paymentPage.getTotal(),paymentPage.getPageNumber(),paymentPage.getPageSize(),
					paymentService.createEntity().convertList(paymentPage.getContent(), null));

		}catch (Exception e){
			LOG.error("get Member list error. {}", e);
			result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
		}
		LOG.debug("get Member list result = " + result.toString());

		return result;
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	@ApiOperation(value="批量删除订单", notes="批量删除订单",produces = "application/json")
	@Log(description="平台中心批量删除订单接口:/api/payment/delete")
	public PublicResult<Boolean> delete(Long[] ids) {
		LOG.info("delete payment param: " + ids);
		PublicResult<Boolean> result = null;
		try{
			paymentService.delete(ids);
			result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
		}catch (Exception e){
			LOG.error("delete payment error. {}", e);
			result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
		}
		LOG.info("delete payment result = " + result.toString());
		return result;
	}

	/**
	 * 订单详情
	 */
	@ApiOperation(value="订单详情", notes="body体参数,不需要Authorization",produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "id", value = "订单ID" , dataType = "Long", paramType="query")
	})
	@GetMapping("/view")
	@Log(description="前台收款列表接口:/view")
	@Pass
	public PublicResult<JSONObject> view(
			@RequestParam(name = "id", required = true) Long id) {

			PublicResult<JSONObject> result = null;
			//订单基本信息
			JSONObject paymentJson = null;

		try{

			Payment payment = paymentService.find(id);

			//JSONObject paymentJs = new JSONObject();


			//for(payment paymentTemp : paymentPage.getContent()) {
	//		paymentJson.put("id", payment.getId());
	//		paymentJson.put("sn", payment.getSn());
	//		paymentJson.put("paymentMethod", payment.getPaymentMethod());
	//		paymentJson.put("operator", payment.getOperator());
	//		paymentJson.put("paymentStatus", payment.getStatus().getDesc());
	//		paymentJson.put("type", payment.getType().getDesc());
	//		paymentJson.put("method", payment.getMethod().getDesc());
	//		paymentJson.put("paymentDate", DateUtil.format(payment.getPaymentDate(), "yyyy-MM-dd HH:mm:ss"));
	//		paymentJson.put("createDate", DateUtil.format(payment.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
	//		paymentJson.put("bank", payment.getBank());
	//		paymentJson.put("account", payment.getAccount());
	//		paymentJson.put("payer", payment.getPayer());
	//		paymentJson.put("amount", payment.getFee().compareTo(new BigDecimal(0)) > 0 ?  payment.getAmount() + "(包含服务费:" + payment.getFee() + ")" : payment.getAmount());
	//		paymentJson.put("username", payment.getMember().getUsername());
	//		paymentJson.put("orderSn", payment.getOrder() == null ? "" : payment.getOrder().getSn());
	//		paymentJson.put("memo", payment.getMemo());

			//paymentJs.put("paymentInfo", paymentJson);

			paymentJson = paymentService.createEntity().convertEntity(payment, null);
			result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, paymentJson);
		}catch (Exception e){
			LOG.error("get Member list error. {}", e);
			result =new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
		}

		return result;
	}

}