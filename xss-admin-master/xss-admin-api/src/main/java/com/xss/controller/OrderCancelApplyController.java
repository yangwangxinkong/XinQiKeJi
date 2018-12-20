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
import com.xss.domain.Member;
import com.xss.domain.Order;
import com.xss.domain.OrderCancelApply;
import com.xss.service.OrderCancelApplyService;
import com.xss.service.OrderService;
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
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * Controller - 订单
 */
@RestController
@RequestMapping("/api/orderCancelApply")
@Api(description="订单模块 - 取消订单")
public class OrderCancelApplyController {

	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(OrderCancelApplyController.class);

	@Autowired
    private OrderCancelApplyService orderCancelApplyService;
	@Autowired
	private OrderService orderService;

	/**
	 * 列表
	 */
	@ApiOperation(value="取消订单列表", notes="body体参数,不需要Authorization",produces = "application/json")
	@GetMapping("/list")
	@Log(description="前台取消订单列表接口:/list")
	@Pass
	public PageResult<Object> list(Pageable pageable, String sn, String orderStatusId, @CurrentUser Admin admin) {

		LOG.debug("get orderCancelApply list param = " + pageable.toString());

		PageResult<Object> result = null;

		try{

			if (StringUtils.isNotEmpty(sn)){
				pageable.getFilters().add(Filter.like("sn", "%" + sn + "%"));
			}
			if (StringUtils.isNotEmpty(orderStatusId)){
				pageable.getFilters().add(Filter.eq("orderStatus", Order.OrderStatus.findByValue(Integer.parseInt(orderStatusId))));
			}

			Page<OrderCancelApply> orderCancelApplyPage = orderCancelApplyService.findPage(pageable);

			result = new PageResult<Object>((int)orderCancelApplyPage.getTotal(),orderCancelApplyPage.getPageNumber(),orderCancelApplyPage.getPageSize(),
					orderCancelApplyService.createEntity().convertList(orderCancelApplyPage.getContent(), new String[]{"order"}));

		}catch (Exception e){
			LOG.error("get orderCancelApply list error. {}", e);
			result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
		}
		LOG.debug("get orderCancelApply list result = " + result.toString());

		return result;
	}

	/**
	 * 删除
	 */
	@GetMapping("/delete")
	@ApiOperation(value="批量删除取消订单", notes="批量删除取消订单",produces = "application/json")
	@Log(description="平台中心批量删除取消订单接口:/api/shipping/delete")
	public PublicResult<Boolean> delete(Long[] ids) {
		LOG.info("delete orderCancelApply param: " + ids);
		PublicResult<Boolean> result = null;
		try{
			orderCancelApplyService.delete(ids);
			result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
		}catch (Exception e){
			LOG.error("delete orderCancelApply error. {}", e);
			result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
		}
		LOG.info("delete orderCancelApply result = " + result.toString());
		return result;
	}

	/**
	 * 订单详情
	 */
	@ApiOperation(value="订单取消详情", notes="body体参数,不需要Authorization",produces = "application/json")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "sn", value = "订单编号" , dataType = "String", paramType="query")
	})
	@GetMapping("/view")
	@Log(description="前台订单取消接口:/view")
	@Pass
	public PublicResult<JSONObject> view(@RequestParam(name = "sn", required = true) String sn) {

		JSONObject orderJs = new JSONObject();

		try{

			OrderCancelApply orderCancelApply = orderCancelApplyService.findByOrderSn(sn);

			if(orderCancelApply != null) {
				orderJs = orderCancelApplyService.createEntity().convertEntity(orderCancelApply, new String[]{"order"});
			}
		}catch (Exception e){
			LOG.error("delete orderCancelApply error. {}", e);
			return new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
		}

		return new PublicResult(PublicResultConstant.SUCCESS, orderJs);
	}

	/**
	 * 取消订单申请通过操作
	 * 1.将取消申请状态更新为已取消
	 * 2.将订单状态更新为已取消
	 * 3.如果是在线支付调用第三方支付退款接口原路退款
	 * 4.如果是其它支付方式则将订单金额退回用户余额中
	 * 5.记录订单日志和退款日志
	 * @param sn
	 * @return
	 */
	@PostMapping("/approve")
	@ApiOperation(value="取消订单申请通过", notes="取消订单申请通过",produces = "application/json")
	@Log(description="平台中心取消订单申请通过接口:/api/orderCancelApply/approve")
	public PublicResult<Boolean> approve(@RequestParam(name = "sn", required = true) String sn, String amount, @CurrentUser Admin admin) {

		PublicResult<Boolean> result = null;

		try{
			OrderCancelApply orderCancelApply = orderCancelApplyService.findByOrderSn(sn);

			if(null==admin){
				return new PublicResult<Boolean>(PublicResultConstant.UNAUTHORIZED, false);
			}
			if (orderCancelApply != null  && orderCancelApply.getOrderStatus() == Order.OrderStatus.canceling) {
				orderCancelApply.setAmount(new BigDecimal(amount));
				orderCancelApplyService.approve(orderCancelApply,admin, null);
			}
			result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
		}
		catch (Exception e){
			LOG.error("get refundsApplyPage list error. {}", e);
			return new PublicResult<Boolean>(PublicResultConstant.ERROR, false);
		}
		return result;
	}

	/**
	 * 取消订单申请驳回操作
	 * 1.将取消申请状态更新为已驳回，并保存驳回原因
	 * 2.将订单状态更新为已驳回
	 * 3.记录订单日志和退款日志
	 * @param sn
	 * @return
	 */
	@PostMapping("/reject")
	@ApiOperation(value="取消订单申请驳回", notes="取消订单申请驳回",produces = "application/json")
	@Log(description="平台中心取消订单申请驳回接口:/api/orderCancelApply/reject")
	public PublicResult<Boolean> reject(String sn, String strRejectContent, @CurrentUser Admin admin) {

		PublicResult<Boolean> result = null;
		try{
			OrderCancelApply orderCancelApply = orderCancelApplyService.findByOrderSn(sn);
			if(null==admin){
				return new PublicResult<Boolean>(PublicResultConstant.UNAUTHORIZED, false);
			}
			orderCancelApply.setRejectContent(strRejectContent);
			if (orderCancelApply != null  && orderCancelApply.getOrderStatus() == Order.OrderStatus.canceling) {

				orderCancelApplyService.reject(orderCancelApply,admin, null);
			}
			result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
		}
		catch (Exception e){
			LOG.error("get refundsApplyPage list error. {}", e);
			return new PublicResult<Boolean>(PublicResultConstant.ERROR, false);
		}

		return result;
	}

}