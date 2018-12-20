/*
 *  
 *  
 *  
 */
package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.annotation.Pass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.*;
import com.xss.domain.enums.FeeCategory;
import com.xss.service.OrderCancelApplyService;
import com.xss.service.OrderService;
import com.xss.service.QuotationService;
import com.xss.util.BigDecimalUtils;
import com.xss.util.DateTimeUtil;
import com.xss.util.DateUtil;
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
import java.util.Date;
import java.util.List;

/**
 * Controller - 订单
 */
@RestController
@RequestMapping("/m/orderCancelApply")
@Api(description="订单模块 - 取消订单")
public class OrderCancelApplyController {

	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(OrderCancelApplyController.class);

	@Autowired
    private OrderCancelApplyService orderCancelApplyService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private QuotationService quotationService;

	/**
	 * 订单详情
	 */
	@GetMapping("/view")
	@ApiOperation(value="退款计算", notes="分页展示订单列表，支持根据名称和网址搜索",produces = "application/json")
	@Log(description="平台中心获取订单列表接口:/m/orderCancelApply/refundAmount")
	public PublicResult<JSONObject> view(String sn) {

		JSONObject orderJs = new JSONObject();

		try{

			Order order = orderService.findBySn(sn);

			if(order != null && order.getQuotation() != null) {
				//orderJs = orderService.createEntity().convertEntity(order, new String[]{"order"});
				// 申请停缴 开始日期，结束日期，月数， 预计退款金额

				Quotation quotation = order.getQuotation();

				String startDate = quotation.getStartDate();
				String endDate = quotation.getEndDate();
				Integer monthCount = quotation.getMonthCount();

				// 本月的下一个月 开始
				Date nextMonth = DateUtil.addMonths(new Date(), 1);
				String strNextMonth = DateUtil.format(nextMonth, DateTimeUtil.FMT_yyyyMM);

				// 开始日 - 下个月 的月差
				int startNowMonth = DateUtil.getIntervalMonths(DateUtil.parse(order.getQuotation().getStartDate() + "-01", DateTimeUtil.FMT_yyyyMMdd), nextMonth);

				// 开始日 < 下个月
				if(startNowMonth > 0) {
					startDate = strNextMonth;
					monthCount = monthCount - startNowMonth;
				}

				// 下个月 - 结束日 的月差
				int nowEndMonth = DateUtil.getIntervalMonths(nextMonth, DateUtil.parse(order.getQuotation().getEndDate() + "-01", DateTimeUtil.FMT_yyyyMMdd));

				// 结束日 < 下个月
				if(startNowMonth < 0) {
					return new PublicResult<JSONObject>(PublicResultConstant.PARAM_ERROR, null);
				}

				// 组装JSON
				orderJs.put("startDate", startDate);
				orderJs.put("endDate", endDate);
				orderJs.put("monthCount", monthCount);
				orderJs.put("amount", getRefundAmount(sn, monthCount));

				JSONArray ja = new JSONArray();
				JSONObject jo;
				// 月数下拉框
				for(int i = 1; i <= monthCount; i++) {
					jo = new JSONObject();
					jo.put("value", i+"个月");
					jo.put("key", i);
					ja.add(jo);
				}

				orderJs.put("monthTypes", ja);
			}
		}catch (Exception e){
			LOG.error("delete orderCancelApply error. {}", e);
			return new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
		}

		return new PublicResult(PublicResultConstant.SUCCESS, orderJs);
	}

	/**
	 * 退款计算
	 */
	@GetMapping("/refundAmount")
	@ApiOperation(value="退款计算", notes="分页展示订单列表，支持根据名称和网址搜索",produces = "application/json")
	@Log(description="平台中心获取订单列表接口:/m/orderCancelApply/refundAmount")
	public PublicResult<JSONObject> refundAmount(@RequestParam(name = "sn", required = true) String sn, String startDate, String endDate) {

		JSONObject orderJs = new JSONObject();
		int monthCount = DateUtil.getIntervalMonths(DateUtil.parse(startDate + "-01", DateTimeUtil.FMT_yyyyMMdd), DateUtil.parse(endDate + "-01", DateTimeUtil.FMT_yyyyMMdd)) + 1;
		orderJs.put("amount", getRefundAmount(sn, monthCount));

		return new PublicResult(PublicResultConstant.SUCCESS, orderJs);
	}

	private BigDecimal getRefundAmount(String sn, Integer monthCount) {

		BigDecimal amount = BigDecimal.ZERO;

		try{

			Order order = orderService.findBySn(sn);

			if(order != null && order.getQuotation() != null) {
				// 申请停缴 开始日期，结束日期，月数， 预计退款金额

				Quotation quotation = order.getQuotation();

				List<QuotationItem> quotationItems = quotation.getQuotationItems();
				for (QuotationItem quotationItem : quotationItems) {
					if (quotation.getFeeCategory().equals(FeeCategory.fc0) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
						// 养老
						amount = amount.add(quotationItem.getEndowmentPerson()).add(quotationItem.getEndowmentCompany());
						// 失业
						amount = amount.add(quotationItem.getUnemploymentPerson()).add(quotationItem.getUnemploymentCompany());
						// 工伤
						amount = amount.add(quotationItem.getInjuryPerson()).add(quotationItem.getInjuryCompany());
						// 医疗
						amount = amount.add(quotationItem.getMedicarePerson()).add(quotationItem.getMedicareCompany());
						// 生育
						amount = amount.add(quotationItem.getMaternityPerson()).add(quotationItem.getMaternityCompany());
						// 残保
						amount = amount.add(quotationItem.getDisabilityPerson()).add(quotationItem.getDisabilityCompany());
					}

					if (quotation.getFeeCategory().equals(FeeCategory.fc1) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
						amount = amount.add(quotationItem.getProvidentFundPerson()).add(quotationItem.getProvidentFundCompany());
					}

					amount = BigDecimalUtils.setScale(amount.multiply(BigDecimal.valueOf(monthCount)), 2, BigDecimal.ROUND_HALF_UP);
				}
			}

		}catch (Exception e){
			LOG.error("delete orderCancelApply error. {}", e);
		}

		return amount;
	}

	/**
	 * 取消订单申请操作
	 * 1.将取消申请状态更新为已取消
	 * 2.将订单状态更新为已取消
	 * 3.如果是在线支付调用第三方支付退款接口原路退款
	 * 4.如果是其它支付方式则将订单金额退回用户余额中
	 * 5.记录订单日志和退款日志
	 * @param sn
	 * @return
	 */
	@PostMapping("/save")
	@ApiOperation(value="取消订单申请", notes="取消订单申请",produces = "application/json")
	@Log(description="平台中心取消订单申请接口:/api/orderCancelApply/approve")
	public PublicResult<Boolean> save(String sn, String startDate, @CurrentUser Member member) {

		PublicResult<Boolean> result = null;

		try{
			Order order = orderService.findBySn(sn);
			if (order != null && member.equals(order.getMember()) && !order.isExpired()
					&& (order.getOrderStatus() == Order.OrderStatus.confirmed || order.getOrderStatus() == Order.OrderStatus.completed)
					&& order.getPaymentStatus() == Order.PaymentStatus.paid) {
				if (order.isLocked(null)) {
					return  new PublicResult<Boolean>(PublicResultConstant.ORDER_LOCKED_ERROR, false);
				}

				int monthCount = DateUtil.getIntervalMonths(DateUtil.parse(startDate + "-01", DateTimeUtil.FMT_yyyyMMdd), DateUtil.parse(order.getQuotation().getEndDate() + "-01", DateTimeUtil.FMT_yyyyMMdd)) + 1;


				OrderCancelApply orderCancelApply = new OrderCancelApply();
				orderCancelApply.setStartDate(startDate);
				orderCancelApply.setEndDate(order.getQuotation().getEndDate());
				orderCancelApply.setOrder(order);
				orderCancelApply.setMonthCount(monthCount);
				orderCancelApply.setAmount(getRefundAmount(sn, monthCount));
				orderCancelApply.setSn(order.getSn());
				orderCancelApply.setMember(member);
				orderCancelApplyService.cancelApply(orderCancelApply);

				result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
			} else {
				result = new PublicResult<Boolean>(PublicResultConstant.DATA_ERROR, false);
			}
		}
		catch (Exception e){
			LOG.error("get refundsApplyPage list error. {}", e);
			return new PublicResult<Boolean>(PublicResultConstant.ERROR, false);
		}
		return result;
	}

}