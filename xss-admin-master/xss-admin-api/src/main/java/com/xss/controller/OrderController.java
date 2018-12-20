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
import com.xss.service.AreaService;
import com.xss.service.OrderService;
import com.xss.service.PaymentMethodService;
import com.xss.util.DateUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 *  订单管理接口
 * @author hu
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/api/order")
@Api(description="订单管理模块")
public class OrderController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(OrderController.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private AreaService areaService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="订单列表", notes="分页展示订单列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取订单列表接口:/api/city/list")
    public PageResult<Object> list(Integer orderStatusId, Integer paymentStatusId, Boolean hasExpired,
                                   String memberName, String memberIdNo, Pageable pageable, Order.OrderType orderType, @CurrentUser Admin admin) {
        LOG.debug("get order list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            List<Filter> filters = new ArrayList<Filter>();
            if (null != orderType){
                filters.add(Filter.eq("orderType", orderType));

            }else{
                filters.add(Filter.eq("orderType", "quotation"));
            }
            pageable.setFilters(filters);
            Order order = new Order();
            Quotation quotation = new Quotation();
//            if (StringUtils.isNotBlank(memberName)) {
//                memberName = Base64.encodeBase64String(memberName.getBytes("utf-8"));
//            }
            quotation.setUsername(memberName);
            quotation.setIdentification(memberIdNo);
            order.setQuotation(quotation);

            Order.OrderStatus orderStatus = null;
            if(orderStatusId != null) {
                orderStatus = Order.OrderStatus.findByValue(orderStatusId);
            }

            Order.PaymentStatus paymentStatus = null;
            if(paymentStatusId != null) {
                paymentStatus = Order.PaymentStatus.findByValue(paymentStatusId);
            }

            Page<Order> data = orderService.findPage(order, orderStatus, paymentStatus, null, hasExpired, pageable);

            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(),
                    orderService.createEntity().convertList(data.getContent(), new String[]{"userName", "status", "typeName", "feeCategory", "payCategory", "startEndDate", "isExpired", "city"}));
        }catch (Exception e){
            LOG.error("get order list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get order list result = " + result.toString());
        return result;
    }

    /**
     * 列表
     */
    @ApiOperation(value="订单列表", notes="body体参数,不需要Authorization",produces = "application/json")
    @GetMapping("/excharge/list")
    @Log(description="前台订单列表接口:/list")
    @Pass
    public PublicResult<JSONObject> getExchargeList(Integer orderStatusId, Integer paymentStatusId, Integer shippingStatusId,
                                         Boolean hasExpired, String memberName, String memberMobile,
                                         Pageable pageable, @CurrentUser Admin admin) {

        List<Filter> filters = new ArrayList<Filter>();

        filters.add(Filter.eq("orderType", Order.OrderType.service));

        Order order = new Order();
        Member member = new Member();
        member.setUsername(memberName);
        member.setMobile(memberMobile);
        order.setMember(member);

        Order.OrderStatus orderStatus = null;
        if(orderStatusId != null) {
            orderStatus = Order.OrderStatus.findByValue(orderStatusId);
        }

        Order.PaymentStatus paymentStatus = null;
        if(paymentStatusId != null) {
            paymentStatus = Order.PaymentStatus.findByValue(paymentStatusId);
        }

        Order.ShippingStatus shippingStatus = null;
        if(shippingStatusId != null) {
            shippingStatus = Order.ShippingStatus.findByValue(shippingStatusId);
        }

        pageable.setFilters(filters);

        Page<Order> orderPage = orderService.findPage(order, orderStatus, paymentStatus, shippingStatus, hasExpired, pageable);

        JSONArray orderJa = new JSONArray();
        JSONObject orderJson;
        for(Order orderTemp : orderPage.getContent()) {
            orderJson = new JSONObject();
            orderJson.put("id", orderTemp.getId());
            orderJson.put("sn", orderTemp.getSn());
            orderJson.put("amount", orderTemp.getAmount());
            orderJson.put("point", orderTemp.getPoint());
            orderJson.put("consignee", orderTemp.getConsignee());
            orderJson.put("phone", orderTemp.getPhone());
            orderJson.put("address", orderTemp.getAddress());
            orderJson.put("paymentMethodName", orderTemp.getPaymentMethodName());
            String strStatus = orderTemp.getOrderStatus().getDesc();
            if(orderTemp.isExpired()) {
                strStatus = strStatus + "(" + "已过期" + ")";
            }
            orderJson.put("orderStatus", strStatus);
            orderJson.put("isExpired", orderTemp.isExpired());
            orderJson.put("orderStatusId", orderTemp.getOrderStatus().getValue());
            orderJson.put("paymentStatus", orderTemp.getPaymentStatus().getDesc());
            orderJson.put("shippingStatus", orderTemp.getShippingStatus().getDesc());
            orderJson.put("createDate", DateUtil.format(orderTemp.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
            orderJa.add(orderJson);
        }

        JSONObject jsonRes = new JSONObject();
        jsonRes.put("order", orderJa);
        jsonRes.put("orderStatus", orderStatus);
        jsonRes.put("paymentStatus", paymentStatus);
        jsonRes.put("shippingStatus", shippingStatus);
        jsonRes.put("hasExpired", hasExpired);
        jsonRes.put("total", orderPage.getTotal());
        jsonRes.put("totalPage", orderPage.getTotalPages());

        return new PublicResult(PublicResultConstant.SUCCESS, jsonRes);
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="订单详情", notes="根据id获取订单详情",produces = "application/json")
    @Log(description="平台中心获取订单详情接口:/api/city/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get order info param = " + id);

        Order order = orderService.find(id);
        JSONObject result = order.convertEntity(order, new String[]{"userName", "status", "typeName", "feeCategory", "payCategory", "startEndDate", "isExpired", "identification", "mobile", "invoice", "city"});

        LOG.debug("get order info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }

    /**
     * 订单详情
     */
    @ApiOperation(value="订单详情", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "订单ID" , dataType = "Long", paramType="query")
    })
    @GetMapping("/view")
    @Log(description="前台订单列表接口:/view")
    @Pass
    public PublicResult<JSONObject> view(@RequestParam(name = "id", required = true) Long id) {

        JSONObject orderJs = new JSONObject();

        try {

            Order order = orderService.find(id);
            boolean shippingTimeout = false;


            //订单基本信息
            JSONObject orderJson = new JSONObject();
            //for(Order orderTemp : orderPage.getContent()) {
            orderJson.put("id", order.getId());
            orderJson.put("sn", order.getSn());
            orderJson.put("amount", order.getAmount());
            orderJson.put("consignee", order.getConsignee());
            orderJson.put("paymentMethodName", order.getPaymentMethodName());
            String strStatus = order.getOrderStatus().getDesc();
            if(order.isExpired()) {
                strStatus = strStatus + "(" + "已过期" + ")";
            }
            orderJson.put("orderStatus", strStatus);
            orderJson.put("paymentStatus", order.getPaymentStatus().getDesc());
            orderJson.put("shippingStatus", order.getShippingStatus().getDesc());
            orderJson.put("createDate", DateUtil.format(order.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
            orderJson.put("expiredDate", order.getExpire() == null ? "" : DateUtil.format(order.getExpire(), "yyyy-MM-dd"));
            orderJson.put("userName", order.getMember().getUsername());
            orderJson.put("amountPaid", order.getAmountPaid());
            orderJson.put("amountPayable", order.getAmountPayable());
            orderJson.put("coupon", order.getCouponCode() == null ? "" : order.getCouponCode().getCoupon().getName());
            orderJson.put("couponDiscount", order.getCouponDiscount());
            orderJson.put("offsetAmount", order.getOffsetAmount());
            orderJson.put("point", order.getPoint());
            orderJson.put("freight", order.getFreight());
            orderJson.put("fee", order.getFee());

            orderJson.put("paymentMethodName", order.getPaymentMethodName());
            orderJson.put("areaName", order.getAreaName());
            //orderJson.put("area", areaService.createEntity().convertEntity(order.getArea(), Area.DEFAULT_JSON_PARAMS));
            orderJson.put("address", order.getAddress());
            orderJson.put("zipCode", order.getZipCode());
            orderJson.put("phone", order.getPhone());
            orderJson.put("memo", order.getMemo());

            orderJson.put("shippingTimeout", shippingTimeout);
            orderJson.put("expired", order.isExpired());
            orderJson.put("orderStatusId", order.getOrderStatus().getValue());
            orderJson.put("paymentStatusId", order.getPaymentStatus().getValue());
            orderJson.put("shippingStatusId", order.getShippingStatus().getValue());

            orderJs.put("orderInfo", orderJson);

            //订单商品信息
            JSONArray orderItemJa = new JSONArray();
            JSONObject orderItemJo;
            Integer quantity = 0;
            for(OrderItem orderItem : order.getOrderItems()) {
                orderItemJo = new JSONObject();
                orderItemJo.put("sn", orderItem.getSn());
                orderItemJo.put("fullName", orderItem.getFullName());
                orderItemJo.put("price", orderItem.getIsGift() ? "-" : orderItem.getPrice());
                orderItemJo.put("quantity", orderItem.getQuantity());
                quantity += orderItem.getQuantity();
                orderItemJo.put("shippedQuantity", orderItem.getShippedQuantity());

                Integer shippingQuantity = 0;
                if(orderItem.getProduct() != null && orderItem.getProduct() != null) {
                    if(orderItem.getProduct().getStock() == null) {
                        shippingQuantity = 0;
                    } else if(orderItem.getProduct().getStock() <= 0 || orderItem.getQuantity() - orderItem.getShippedQuantity() <= 0) {
                        shippingQuantity = 0;
                    } else if(orderItem.getProduct().getStock() < orderItem.getQuantity() - orderItem.getShippedQuantity()) {
                        shippingQuantity = orderItem.getProduct().getStock();
                    } else {
                        shippingQuantity = orderItem.getQuantity() - orderItem.getShippedQuantity();
                    }
                } else {
                    shippingQuantity = orderItem.getQuantity() - orderItem.getShippedQuantity();
                }

                orderItemJo.put("shippingQuantity", shippingQuantity);
                orderItemJo.put("returnQuantity", orderItem.getReturnQuantity());
                orderItemJo.put("stock", orderItem.getProduct() == null ? "" : orderItem.getProduct().getStock());

                orderItemJo.put("subtotal", orderItem.getIsGift() ? "-" : orderItem.getSubtotal());

                orderItemJa.add(orderItemJo);
            }
            orderJs.put("orderItemInfo", orderItemJa);
            orderJson.put("quantity", quantity);

            //收款信息
            JSONArray paymentJa = new JSONArray();
            JSONObject paymentJo;
            for(Payment payment : order.getPayments()) {
                paymentJo = new JSONObject();
                paymentJo.put("sn", payment.getSn());
                paymentJo.put("method", payment.getMethod());
                paymentJo.put("methodDesc", payment.getMethod().getDesc());
                paymentJo.put("paymentMethod", payment.getPaymentMethod());
                paymentJo.put("paymentAmount", payment.getAmount());
                paymentJo.put("paymentStatus", payment.getStatus().getDesc());
                paymentJo.put("paymentDate", DateUtil.format(payment.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));

                paymentJa.add(paymentJo);
            }
            orderJs.put("paymentInfo", paymentJa);

            //发货信息
            JSONArray shippingJa = new JSONArray();
            JSONObject shippingJo;
            for(Shipping shipping : order.getShippings()) {
                shippingJo = new JSONObject();
                shippingJo.put("sn", shipping.getSn());
                shippingJo.put("shippingMethod", shipping.getShippingMethod());
                shippingJo.put("deliveryCorp", shipping.getDeliveryCorp());
                shippingJo.put("trackingNo", shipping.getTrackingNo());
                shippingJo.put("consignee", shipping.getConsignee());
                shippingJo.put("createDate", DateUtil.format(shipping.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));

                shippingJa.add(shippingJo);
            }
            orderJs.put("shippingInfo", shippingJa);

            //订单日志
            JSONArray orderLogJa = new JSONArray();
            JSONObject orderLogJo;
            for(OrderLog orderLog : order.getOrderLogs()) {
                orderLogJo = new JSONObject();
                orderLogJo.put("type", orderLog.getType());
                orderLogJo.put("operator", orderLog.getOperator());
                orderLogJo.put("content", orderLog.getContent());
                orderLogJo.put("createDate", DateUtil.format(orderLog.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));

                orderLogJa.add(orderLogJo);
            }

            orderJs.put("orderLogInfo", orderLogJa);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new PublicResult(PublicResultConstant.SUCCESS, orderJs);
    }

    /**
     * 发货
     */
    @PostMapping("/shipping")
    @ApiOperation(value="订单发货", notes="订单发货",produces = "application/json")
    @Log(description="平台中心订单发货接口:/api/order/shipping")
    public  PublicResult<Boolean> shipping(Long orderId, String shippingMethodName, String deliveryCorp, Long areaId,
                                           String orderItems, Shipping shipping, @CurrentUser Admin admin) {

        LOG.info("cancel order param: " + orderId);
        PublicResult<Boolean> result = null;

        try {
            Order order = orderService.find(orderId);
            if (order == null) {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_NULL_ERROR, false);
            }

            //解析json字符串 到 List<ReturnsItem>
            orderItems = orderItems.replaceAll("&quot;", "\"");
            JSONArray ja = JSONArray.parseArray(orderItems);

            List<ShippingItem> shippingItems = new ArrayList<ShippingItem>();

            for(Object ob : ja) {
                JSONObject jo_temp=JSONObject.parseObject(ob.toString());

                ShippingItem shippingItem = new ShippingItem();

                shippingItem.setSn(jo_temp.getString("sn"));
                shippingItem.setName(jo_temp.getString("fullName"));
                shippingItem.setQuantity(jo_temp.getInteger("shippingQuantity"));

                shippingItems.add(shippingItem);
            }

            shipping.setShippingItems(shippingItems);

            for (Iterator<ShippingItem> iterator = shipping.getShippingItems().iterator(); iterator.hasNext();) {
                ShippingItem shippingItem = iterator.next();
                if (shippingItem == null || org.apache.commons.lang.StringUtils.isEmpty(shippingItem.getSn()) || shippingItem.getQuantity() == null || shippingItem.getQuantity() <= 0) {
                    iterator.remove();
                    continue;
                }
                OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
                if (orderItem == null || shippingItem.getQuantity() > orderItem.getQuantity() - orderItem.getShippedQuantity()) {
                    return new PublicResult<Boolean>(PublicResultConstant.PARAM_ERROR, false);
                }
                if (orderItem.getProduct() != null && orderItem.getProduct().getStock() != null && shippingItem.getQuantity() > orderItem.getProduct().getStock()) {
                    return new PublicResult<Boolean>(PublicResultConstant.PARAM_ERROR, false);
                }
                shippingItem.setName(orderItem.getFullName());
                shippingItem.setShipping(shipping);
            }

            shipping.setOrder(order);
            shipping.setShippingMethod(shippingMethodName);
            shipping.setDeliveryCorp(deliveryCorp);
            shipping.setDeliveryCorpUrl(null);
            shipping.setDeliveryCorpCode(null);
            Area area = areaService.find(areaId);
            shipping.setArea(area != null ? area.getFullName() : null);
			/*if (!isValid(shipping)) {
				return new PublicResult<Boolean>(PublicResultConstant.PARAM_ERROR, false);
			}*/
            if (order.isExpired() || (order.getOrderStatus() != Order.OrderStatus.confirmed&&order.getOrderStatus() != Order.OrderStatus.cancel_reject)) {
                return new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            if (order.getShippingStatus() != Order.ShippingStatus.unshipped && order.getShippingStatus() != Order.ShippingStatus.partialShipment) {
                return new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }

            if (order.isLocked(admin)) {
                return new PublicResult<Boolean>(PublicResultConstant.USER_NO_AUTHORITY, false);
            }
            //shipping.setSn(snService.generate(Sn.Type.shipping));
            shipping.setOperator(admin.getUsername());

            orderService.shipping(order, shipping, admin, null);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 确认
     */
    @PostMapping("/confirm")
    @ApiOperation(value="订单确认", notes="订单确认",produces = "application/json")
    @Log(description="平台中心订单确认接口:/api/order/confirm")
    public PublicResult<Boolean> confirm(Long id, @CurrentUser Admin admin) {
        LOG.info("confirm order param: " + id);
        PublicResult<Boolean> result = null;
        try{
            Order order = orderService.find(id);
            if (order != null && !order.isExpired() && order.getOrderStatus() == Order.OrderStatus.unconfirmed && !order.isLocked(admin)) {
                orderService.confirm(order, admin, null);
                result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
            } else {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
        }catch (Exception e){
            LOG.error("confirm order error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("confirm order result = " + result.toString());
        return result;
    }

    /**
     * 取消
     */
    @PostMapping("/cancel")
    @ApiOperation(value="订单取消", notes="订单取消",produces = "application/json")
    @Log(description="平台中心订单取消接口:/api/order/cancel")
    public PublicResult<Boolean> cancel(Long id, @CurrentUser Admin admin) {
        LOG.info("cancel order param: " + id);
        PublicResult<Boolean> result = null;
        try{
            Order order = orderService.find(id);
            if (order != null && !order.isExpired() && order.getOrderStatus() == Order.OrderStatus.unconfirmed && !order.isLocked(admin)) {
                orderService.cancel(order, admin, null);
                result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
            } else {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
        }catch (Exception e){
            LOG.error("cancel order error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("cancel order result = " + result.toString());
        return result;
    }

    /**
     * 支付
     */
    @PostMapping("/payment")
    @ApiOperation(value="订单支付", notes="订单支付",produces = "application/json")
    @Log(description="平台中心订单支付接口:/api/order/payment")
    public  PublicResult<Boolean> payment(Long orderId, Long paymentMethodId, Integer methodId, Payment payment, @CurrentUser Admin admin) {

        LOG.info("cancel order param: " + orderId);
        PublicResult<Boolean> result = null;
        try{
            Order order = orderService.find(orderId);
            payment.setOrder(order);
            payment.setMethod(Payment.Method.findByValue(methodId));
            PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
            payment.setPaymentMethod(paymentMethod != null ? paymentMethod.getName() : null);
			/*if (!isValid(payment)) {
				result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
			}*/
            if (order.isExpired() || order.getOrderStatus() != Order.OrderStatus.confirmed) {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            if (order.getPaymentStatus() != Order.PaymentStatus.unpaid && order.getPaymentStatus() != Order.PaymentStatus.partialPayment) {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            if (payment.getAmount().compareTo(new BigDecimal(0)) <= 0 || payment.getAmount().compareTo(order.getAmountPayable()) > 0) {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            if (order.isLocked(admin)) {
                result = new PublicResult<Boolean>(PublicResultConstant.USER_NO_AUTHORITY, false);
            }

            //payment.setSn(snService.generate(Sn.Type.payment));
            payment.setType(Payment.Type.payment);
            payment.setStatus(Payment.Status.success);
            payment.setFee(order.getFee());
            payment.setOperator(admin.getUsername());
            payment.setPaymentDate(new Date());
            payment.setPaymentPluginId(null);
            payment.setExpire(null);
            //payment.setDeposit(null);
            payment.setMember(order.getMember());
            //payment.setCityCode(admin.getCityCode());

            //order.setCityCode(cityService.getCityCodeByAdmin(admin));
            //LOG.info("Method payment cityCode:"+order.getCityCode());
            orderService.payment(order, payment, admin, null);

            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);

        }catch (Exception e){
            LOG.error("cancel order error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("cancel order result = " + result.toString());
        return result;
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新订单", notes="保存订单",produces = "application/json")
    @Log(description="平台中心保存订单接口:/api/city/save")
    public PublicResult<String> save(@RequestBody Order order)throws Exception{
        if (null != order && null != order.getId()) {
            orderService.update(order);
        }else{
            orderService.save(order);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除订单", notes="批量删除订单",produces = "application/json")
    @Log(description="平台中心批量删除订单接口:/api/city/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete order param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            orderService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete order error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete order result = " + result.toString());
        return result;
    }

    /**
     * 完成
     */
    @PostMapping("/complete")
    @ApiOperation(value="订单完成", notes="订单完成",produces = "application/json")
    @Log(description="平台中心订单完成接口:/api/order/complete")
    public PublicResult<Boolean> complete(Long id, @CurrentUser Admin admin) {

        LOG.info("cancel order param: " + id);
        PublicResult<Boolean> result = null;

        try {
            Order order = orderService.find(id);

            if (order != null && !order.isExpired()
                    && (order.getOrderStatus() == Order.OrderStatus.confirmed || order.getOrderStatus() == Order.OrderStatus.cancel_reject)
                    && (order.getPaymentStatus() == Order.PaymentStatus.paid ||order.getPaymentStatus() == Order.PaymentStatus.refundReject)
                    && !order.isLocked(admin)) {
                orderService.complete(order, admin, null);
            } else {
                new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 列表
     */
    @ApiOperation(value="订单列表", notes="body体参数,不需要Authorization",produces = "application/json")
    @GetMapping("/methods")
    @Log(description="前台订单列表接口:/methods")
    @Pass
    public PublicResult<JSONObject> methods() {

        JSONObject jsonRes = new JSONObject();

        try {
            //方式
            JSONArray methodJa = new JSONArray();
            JSONObject methodJo;
            for(Payment.Method method : Payment.Method.values()) {
                methodJo = new JSONObject();
                methodJo.put("id", method.getValue());
                methodJo.put("name", method.getDesc());

                methodJa.add(methodJo);
            }
            jsonRes.put("methods", methodJa);

            //支付方式
            JSONArray paymentMethodJa = new JSONArray();
            JSONObject paymentMethodJo;
            for(PaymentMethod paymentMethod : paymentMethodService.findAll()) {
                paymentMethodJo = new JSONObject();
                paymentMethodJo.put("id", paymentMethod.getId());
                paymentMethodJo.put("name", paymentMethod.getName());

                paymentMethodJa.add(paymentMethodJo);
            }
            jsonRes.put("paymentMethods", paymentMethodJa);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PublicResult(PublicResultConstant.SUCCESS, jsonRes);
    }
}
