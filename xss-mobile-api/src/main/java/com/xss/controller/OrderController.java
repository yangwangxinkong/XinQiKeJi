package com.xss.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.OrderDao;
import com.xss.dao.PaymentMethodDao;
import com.xss.dao.QuotationDao;
import com.xss.domain.*;
import com.xss.domain.Order.OrderStatus;
import com.xss.domain.Order.PaymentStatus;
import com.xss.domain.Order.ShippingStatus;
import com.xss.service.*;
import com.xss.util.DateUtil;
import com.xss.util.FreemarkerUtils;
import com.xss.util.JsonUtil;
import com.xss.util.KdniaoTrackQueryAPI;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 *  订单管理接口
 * @author hu
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/order")
@Api(description="订单管理模块")
public class OrderController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(OrderController.class);

    @Resource
    private OrderDao orderDao;
    @Resource
    private QuotationDao quotationDao;
    @Resource
    private PaymentMethodDao paymentMethodDao;
    @Autowired
    private CityService cityService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private OrderCancelApplyService orderCancelApplyService;
    @Autowired
    private PaymentMethodService paymentMethodService;
    @Autowired
    private CartService cartService;
    @Autowired
    private CartItemService cartItemService;
    @Autowired
    private OrderItemService orderItemService;
    @Autowired
    private ReceiverService receiverService;
    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Resource
    private AdminService adminService;
    @Resource
    private WeixinService weixinService;
    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="订单列表", notes="分页展示订单列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取订单列表接口:/m/order/list")
    public PageResult<Object> list(Pageable pageable, String type, Order.OrderType orderType, @CurrentUser Member member) {
        LOG.debug("get order list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{

            if(StringUtils.isEmpty(type)) {
                type = "all";
            }

            List<Order.OrderStatus> statusList = new ArrayList<Order.OrderStatus>();

            List<Filter> filters = new ArrayList<Filter>();

            filters.add(Filter.eq("member", member));

            if (type.equals("topay")) {
                filters.add(Filter.eq("orderStatus", OrderStatus.unconfirmed));
                filters.add(Filter.eq("paymentStatus", PaymentStatus.unpaid));
            } else if (type.equals("paid")) {
                statusList.add(OrderStatus.confirmed);
                statusList.add(OrderStatus.completed);

                filters.add(Filter.in("orderStatus", statusList));

                filters.add(Filter.eq("paymentStatus", PaymentStatus.paid));
            } else if (type.equals("cancelled")) {
                statusList.add(OrderStatus.canceling);
                statusList.add(OrderStatus.cancel_reject);
                statusList.add(OrderStatus.cancelled);

                filters.add(Filter.in("orderStatus", statusList));
            }
            if (null != orderType){
                filters.add(Filter.eq("orderType", orderType));
            }
            pageable.setFilters(filters);

            Page<Order> data = orderService.findPage(pageable);

            result = new PageResult<Object>((int)data.getTotal(), data.getPageNumber(), data.getPageSize(), orderService.createEntity()
                    .convertList(data.getContent(), new String[]{"status", "userName", "expiredTime", "serviceName", "typeName", "feeCategory", "isExpired", "cancelPaid"}));
        }catch (Exception e){
            LOG.error("get order list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get order list result = " + result.toString());
        return result;
    }

    /**
     * 订单详情查看
     */
    @ApiOperation(value="订单确认信息", notes="body体参数,不需要Authorization",produces = "application/json")
    @GetMapping("/exchargeInfo")
    @Log(description="前台订单确认信息接口:/exchargeInfo")
    public PublicResult<JSONObject> exchargeInfo(Long id, @CurrentUser Member member) {

        PublicResult<JSONObject> result = null;

        try {

            Order order = orderService.find(id);
            if (order == null) {
                return new PublicResult<>(PublicResultConstant.ORDER_NULL_ERROR, null);
            }

            //买家判断
            if (!order.getMember().equals(member)) {
                return new PublicResult<>(PublicResultConstant.ORDER_MEMBER_MATCH_ERROR, null);
            }

            //DecimalFormat decimalFormat = new DecimalFormat("#.00");

            JSONObject orderJo = orderService.createEntity().convertEntity(order, new String[]{"userName"});

            // 订单状态判定
            orderJo.put("stepList", setStepJson(order));

            for (Shipping shipping : order.getShippings()) {
                // 物流公司 订单号
                orderJo.put("deliveryCorp", shipping.getDeliveryCorp());
                orderJo.put("trackingNo", shipping.getTrackingNo());
                break;
            }

            //最后一条物流信息
            JSONArray ja;
            JSONArray shippingArray = new JSONArray();
            JSONObject shippingJson;
            String trackingNo = "";
            String strStatu = "";
            for (Shipping shipping : order.getShippings()) {
                KdniaoTrackQueryAPI api = new KdniaoTrackQueryAPI();
                try {
                    trackingNo = shipping.getTrackingNo();
                    String strRes = api.getOrderTracesByJson(shipping.getDeliveryCorpCode(), trackingNo);
                    JSONObject json = JSON.parseObject(strRes);
                    strStatu = json.getString("State");
                    ja = json.getJSONArray("Traces");

                    String acceptStation = null;
                    String acceptTime = null;

                    //循环Traces物流记录
                    for(Object ob2 : ja) {
                        JSONObject jo_temp2 = JSON.parseObject(ob2.toString());
                        acceptStation = jo_temp2.getString("AcceptStation");
                        acceptTime = jo_temp2.getString("AcceptTime");
                    }

                    shippingJson = new JSONObject();
                    if(strStatu.equals("2")) {
                        strStatu = "在途中";
                    } else if(strStatu.equals("3")) {
                        strStatu = "已签收";
                    } else if(strStatu.equals("4")) {
                        strStatu = "问题件";
                    } else {
                        strStatu = "无信息";
                    }

                    shippingJson.put("shippingStatu", strStatu);
                    shippingJson.put("shippingTrackingNo", trackingNo);
                    shippingJson.put("shippingLastInfo", acceptStation);
                    shippingJson.put("shippingLastTime", acceptTime);

                    shippingArray.add(shippingJson);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            if(shippingArray.size() == 0) {
                JSONObject orderjo_temp = new JSONObject();

                orderjo_temp.put("shippingStatu", "无信息");
                orderjo_temp.put("shippingTrackingNo", "");
                orderjo_temp.put("shippingLastInfo", "暂无轨迹信息");
                orderjo_temp.put("shippingLastTime", "");

                shippingArray.add(orderjo_temp);
            }
            orderJo.put("shippingList", shippingArray);

            //订单操作按钮控制开关,true为开，false为关
            //物流开关，如果订单物流配送表有记录就显示，没有则不显示
            Boolean deliverySwitch = !(order.getShippings().isEmpty());
            orderJo.put("deliverySwitch", deliverySwitch);

            //取消订单开关，订单未超时&&订单未确认&&订单未支付
            Boolean cancelSwitch = (!order.isExpired())
                    && (order.getOrderStatus().equals(OrderStatus.unconfirmed) && order.getPaymentStatus().equals(PaymentStatus.unpaid));
            orderJo.put("cancelSwitch", cancelSwitch);

            //已支付订单取消开关，订单未超时且订单已确认且订单已支付且订单未发货
            Boolean cancelPaidSwitch = (!order.isExpired())
                    && order.getOrderStatus().equals(OrderStatus.confirmed)
                    && order.getPaymentStatus().equals(PaymentStatus.paid)
                    && order.getShippingStatus().equals(ShippingStatus.unshipped);
            orderJo.put("cancelPaidSwitch", cancelPaidSwitch);

            //待支付开关，订单未超时且(订单状态为未确认或已确认)且(支付状态为未支付或部分支付)
            Boolean paySwitch = (!order.isExpired())
                    && (order.getOrderStatus().equals(OrderStatus.unconfirmed) || order.getOrderStatus().equals(OrderStatus.confirmed))
                    && (order.getPaymentStatus().equals(PaymentStatus.unpaid) || order.getPaymentStatus().equals(PaymentStatus.partialPayment));
            orderJo.put("paySwitch", paySwitch);

            //待收货开关，订单未超时且配送状态为已发货
            Boolean receiveSwitch = (!order.isExpired())
                    && order.getShippingStatus().equals(ShippingStatus.shipped);
            orderJo.put("receiveSwitch", receiveSwitch);

            //申请退款开关，订单未超时且(订单状态为已确认或取消被驳回)且(订单已收货)且(订单支付状态为已支付或部分支付)
            Boolean refundSwitch = (!order.isExpired())
                    && (order.getOrderStatus().equals(OrderStatus.confirmed) || order.getOrderStatus().equals(OrderStatus.cancel_reject))
                    && (order.getPaymentStatus().equals(PaymentStatus.paid) || order.getPaymentStatus().equals(PaymentStatus.partialPayment))
                    && order.getShippingStatus().equals(ShippingStatus.received);
            orderJo.put("refundSwitch", refundSwitch);

            //申请退货开关，订单未超时且(订单状态为已确认或取消被驳回)且订单已支付且订单已确认收货
            Boolean returnSwitch = (!order.isExpired())
                    && (order.getOrderStatus().equals(OrderStatus.confirmed) || order.getOrderStatus().equals(OrderStatus.cancel_reject))
                    && order.getPaymentStatus().equals(PaymentStatus.paid)
                    && order.getShippingStatus().equals(ShippingStatus.received);
            orderJo.put("returnSwitch", returnSwitch);

            //完成订单开关，订单未超时且(订单状态为已确认或取消被驳回)且(订单支付状态为已支付或申请退款被驳回)且(订单配送状态为确认收货或申请退货被驳回)
            Boolean finishSwitch = (!order.isExpired())
                    && (order.getOrderStatus().equals(OrderStatus.confirmed) || order.getOrderStatus().equals(OrderStatus.cancel_reject))
                    && (order.getPaymentStatus().equals(PaymentStatus.paid ) || order.getPaymentStatus().equals(PaymentStatus.refundReject))
                    && (order.getShippingStatus().equals(ShippingStatus.received) || order.getShippingStatus().equals(ShippingStatus.returnReject));
            orderJo.put("finishSwitch", finishSwitch);

            //评价开关，订单未超时且订单已完成且未评价过
            Boolean reviewSwtich = (!order.isExpired())
                    && order.getOrderStatus().equals(OrderStatus.completed);
            orderJo.put("reviewSwtich", reviewSwtich);

            String strStatus = "";
            if (order.isExpired()) {
                strStatus = "已过期";
            } else if (order.getOrderStatus() != OrderStatus.confirmed && order.getOrderStatus() != OrderStatus.cancel_reject) {
                strStatus = order.getOrderStatus().getDesc();
            } else if (order.getPaymentStatus() == PaymentStatus.unpaid || order.getPaymentStatus() == PaymentStatus.partialPayment) {
                strStatus = "等待付款";
                if (order.getShippingStatus() != ShippingStatus.unshipped) {
                    strStatus = strStatus + "  " + order.getShippingStatus().getDesc();
                }
            } else {
                strStatus = order.getPaymentStatus().getDesc();
                if (order.getPaymentStatus() == PaymentStatus.paid && order.getShippingStatus() == ShippingStatus.unshipped) {
                    strStatus = strStatus + "  " + "等待发货";
                } else {
                    strStatus = strStatus + "  " + order.getShippingStatus().getDesc();
                }
            }

            orderJo.put("allStatus", strStatus);
            orderJo.put("createDate", DateUtil.format(order.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
            if(order.isExpired()){
                orderJo.put("expired", "1");
            }else{
                orderJo.put("expired", "0");
            }

            orderJo.put("orderItems", orderItemService.createEntity().convertList(order.getOrderItems(), new String[]{"product", "subtotal"}));

            result = new PublicResult<>(PublicResultConstant.SUCCESS, orderJo);
        }catch (Exception e){
            LOG.error("delete order error. {}", e);
            return new PublicResult<>(PublicResultConstant.FAILED, null);
        }

        return result;
    }
    /**
     * 组装订单状态Step
     * @param order
     * @return
     */
    private JSONArray setStepJson(Order order) {
        JSONArray stepJa = new JSONArray();
        JSONObject stepJo = new JSONObject();

        stepJo.put("id", 1);
        stepJo.put("title", "提交订单");
        stepJo.put("current", order.getOrderStatus() == OrderStatus.unconfirmed);
        stepJo.put("done", order.getOrderStatus() == OrderStatus.confirmed);
        stepJa.add(stepJo);

        stepJo = new JSONObject();
        stepJo.put("id", 2);
        stepJo.put("title", "配送中");
        stepJo.put("current", order.getShippingStatus() == ShippingStatus.shipped);
        stepJo.put("done", order.getShippingStatus() == ShippingStatus.received);
        stepJa.add(stepJo);

        stepJo = new JSONObject();
        stepJo.put("id", 3);
        stepJo.put("title", "交易完成");
        stepJo.put("current", order.getOrderStatus() == OrderStatus.confirmed && order.getShippingStatus() == ShippingStatus.received);
        stepJo.put("done", order.getOrderStatus() == OrderStatus.completed);
        stepJa.add(stepJo);

        return stepJa;
    }
    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="订单详情", notes="根据id获取订单详情",produces = "application/json")
    @Log(description="平台中心获取订单详情接口:/m/order/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get order info param = " + id);

        Order order = orderDao.findOne(id);

        // 订单信息
        JSONObject result = order.convertEntity(order, new String[]{"status", "isExpired", "expiredTime", "amountPayable"});

        // 报价单信息
        if(order != null && order.getQuotation() != null) {
            // 组装JSON
            result.put("quotation", quotationService.setQuotationConfirmJson(order.getQuotation()));
        }

        LOG.debug("get quotation info result = " + (result == null ? "" : result.toString()));
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    /**
     * 前端订单确认信息
     */
    @ApiOperation(value="订单确认信息", notes="body体参数,不需要Authorization",produces = "application/json")
    @GetMapping("/serviceInfo")
    @Log(description="前台订单确认信息接口:/serviceInfo")
    public PublicResult<JSONObject> serviceInfo(String cartItemIds, Long paymentMethodId, Long shippingMethodId, @CurrentUser Member member) {

        PublicResult<JSONObject> result = null;

        try {
            JSONObject joo = new JSONObject();
            JSONArray ja = new JSONArray();

            Cart cart = cartService.getCurrent(member);
            if (cart == null || cart.isEmpty()) {
                return new PublicResult(PublicResultConstant.CART_ITEM_NOT_EXSIT_ERROR, null);
            }

            List<CartItem> cartItems = new ArrayList<CartItem>();
            //String ids = "";
            String[] strCarItemIds = cartItemIds.split(",");
            for (String id : strCarItemIds) {
                if (null != id) {
                    CartItem cartItem = cartItemService.find(Long.parseLong(id));
                    if (null == cartItem || null == cartItem.getId()) {
                        return new PublicResult(PublicResultConstant.CART_ITEM_NOT_EXSIT_ERROR, null);
                    }
                    cartItems.add(cartItem);
                }
            }
            cart.setCartItems(new HashSet<CartItem>());
            cart.getCartItems().addAll(cartItems);

            Set<Receiver> receives = member.getReceivers();
            Receiver receiver = null;
            Receiver receiverTemp0 = null;
            int i = 0;
            for (Receiver receiverTemp : receives) {
                if (i == 0) {
                    receiverTemp0 = receiverTemp;
                }

                if (receiverTemp.getIsDefault()) {
                    receiver = receiverTemp;
                    break;
                }
                i++;
            }

            if (receiver == null && receives.size() > 0) {
                receiver = receiverTemp0;
            }

            if (paymentMethodId == null) {
                paymentMethodId = 1L;
            }
            PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
            if (shippingMethodId == null) {
                shippingMethodId = 1L;
            }

            Order order = orderService.build(cart, receiver, paymentMethod, null);
            joo = orderService.createEntity().convertEntity(order, null);
            List<OrderItem> orderItems = order.getOrderItems();
            if (orderItems.size() > 0) {
                joo.put("orderItems", orderItemService.createEntity().convertList(orderItems, new String[]{"product", "subtotal"}));
            }

            if (receiver != null) {
                JSONObject receiverJo = JsonUtil.toJSONObject(receiver, new String[]{"id", "consignee", "areaName", "address", "zipCode", "phone", "isDefault"});
                joo.put("receiver", receiverJo);
            } else {
                JSONObject receiverJo = new JSONObject();
                receiverJo.put("id", "");
                receiverJo.put("consignee", "");
                receiverJo.put("areaName", "");
                receiverJo.put("address", "");
                receiverJo.put("zipCode", "");
                receiverJo.put("phone", "");
                receiverJo.put("isDefault", "");

                joo.put("receiver", receiverJo);
            }

            joo.put("cartToken", cart.getToken());
            joo.put("memberPoint", member.getPoint());

            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, joo);
        }catch (Exception e){
            LOG.error("delete order error. {}", e);
            return new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
        }

        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/payInfo")
    @ApiOperation(value="订单详情", notes="根据id获取订单详情",produces = "application/json")
    @Log(description="平台中心获取订单详情接口:/m/order/payInfo")
    public PublicResult<JSONObject> payInfo(Long id) throws Exception{
        LOG.debug("get order info param = " + id);

        Order order = orderDao.findOne(id);
        JSONObject result = order.convertEntity(order,  new String[]{"serviceName", "isExpired", "amountPayable"});

        List<PaymentMethod> paymentMethodList = paymentMethodDao.findAll();
        result.put("paymentMethodList", JsonUtil.toJSONArray(paymentMethodList, new String[]{"id", "name", "content", "description", "icon", "method", "code"}));

        LOG.debug("get order info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新订单", notes="保存订单",produces = "application/json")
    @Log(description="平台中心保存订单接口:/m/order/save")
    public PublicResult<String> save(@RequestBody Order order)throws Exception{
        if (null != order && null != order.getId()) {
            orderService.update(order);
        }else{
            orderService.save(order);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 创建订单
     */
    @ApiOperation(value="创建订单", notes="body体参数,不需要Authorization",produces = "application/json")
    @PostMapping("/create")
    @Log(description="前台创建订单接口:/create")
    public PublicResult<JSONObject> create(String cartToken, Long receiverId, Long paymentMethodId, String cartItemIds, String memo, @CurrentUser Member member) {

        JSONObject jsonRes = new JSONObject();

        try {
            Cart cart = cartService.getCurrent(member);
            if (cart == null || cart.isEmpty()) {
                return new PublicResult(PublicResultConstant.CART_ITEM_NOT_EXSIT_ERROR, null);
            }

            List<CartItem> cartItems = new ArrayList<CartItem>();
            String[] strCarItemIds = cartItemIds.split(",");
            for (String id : strCarItemIds) {
                if (null != id) {
                    CartItem cartItem = cartItemService.find(Long.parseLong(id));
                    if (null == cartItem || null == cartItem.getId()) {
                    }
                    cartItems.add(cartItem);
                }
            }

            Cart cart1 = new Cart();
            BeanUtils.copyProperties(cart, cart1, new String[]{"cartItems"});
            cart1.getCartItems().clear();
            cart1.getCartItems().addAll(cartItems);
            if (!org.apache.commons.lang.StringUtils.equals(cart1.getToken(), cartToken)) {
                return new PublicResult(PublicResultConstant.CART_HAS_CHANGED_ERROR, null);
            }
            if (cart1.getIsLowStock()) {
                return new PublicResult(PublicResultConstant.CART_LOW_STOCK_ERROR, null);
            }
            Receiver receiver = receiverService.find(receiverId);
            if (receiver == null) {
                return new PublicResult(PublicResultConstant.RECEIVER_NOT_EXSIT_ERROR, null);
            }
            //判断购物车中的商品配送地址和收货地址是否冲突
			/*Long serceiverAreaId = receiver.getArea().getId();
			if(!canArrive(serceiverAreaId,cartItems)){
				return new PublicResult(PublicResultConstant.PARTISIAL_RECEIVER_NOT_EXSIT_ERROR, null);
			}*/
            PaymentMethod paymentMethod = paymentMethodService.find(paymentMethodId);
            if (paymentMethod == null) {
                paymentMethod = paymentMethodService.findAll().get(0);
                //return new PublicResult(PublicResultConstant.PAYMENT_METHOD_NOT_EXSIT_ERROR, null);
            }
            Order order = orderService.create(cart1, receiver, paymentMethod, memo, member);
            jsonRes.put("orderSn", order.getSn());
            jsonRes.put("orderId", order.getId());
            /**
             * 1.用户提交订单成功
             * 2.获取有openid的管理员列表，
             * 3.发送微信通知
             */
            List<Filter> filters = new ArrayList<>();
            filters.add(Filter.isNotNull("openId"));
            List<Admin> admins = adminService.findList(null, null, filters, null);
            if (null != admins && !admins.isEmpty()){
                for (Admin admin:admins) {
                    try {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("openid", admin.getOpenId());
                        map.put("type", order.getOrderType().getDesc());
                        map.put("sn", order.getSn());
                        map.put("phone", StringUtils.isEmpty(order.getPhone()) ? "-" : order.getPhone());
                        map.put("address", StringUtils.isEmpty(order.getAddress()) ? "-" : order.getAddress());
                        map.put("price", order.getAmount().setScale(2));
                        Template t = FreemarkerUtils.getTemplate(freeMarkerConfigurer, "newOrder");
                        String json = FreemarkerUtils.processTemplateIntoString(t, map);
                        System.out.println("json-------------"+json);
                        weixinService.sendTemplateMessage(json);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new PublicResult(PublicResultConstant.SUCCESS, jsonRes);
    }


    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除订单", notes="批量删除订单",produces = "application/json")
    @Log(description="平台中心批量删除订单接口:/m/order/delete")
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
     * 未支付 取消
     */
    @PostMapping("/cancel")
    @ApiOperation(value="订单取消", notes="订单取消",produces = "application/json")
    @Log(description="平台中心订单取消接口:/m/order/cancel")
    public PublicResult<Boolean> cancel(Long id, @CurrentUser Member member) {
        LOG.info("cancel order param: " + id);
        PublicResult<Boolean> result = null;
        try{
            Order order = orderService.find(id);
            if (order != null && !order.isExpired() && order.getOrderStatus() == Order.OrderStatus.unconfirmed ) {
                orderService.cancel(order, null, member);
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
     * 已支付订单取消申请
     */
    @PostMapping("/cancelPaid")
    @ApiOperation(value="订单取消", notes="订单取消",produces = "application/json")
    @Log(description="平台中心订单取消接口:/m/order/cancelPaid")
    public PublicResult<Boolean> cancelPaid(Long id, @CurrentUser Member member) {
        LOG.info("cancel order param: " + id);
        PublicResult<Boolean> result = null;
        try{
            Order order = orderService.find(id);
            if (order != null && member.equals(order.getMember()) && !order.isExpired()
                    && order.getOrderStatus() == OrderStatus.confirmed
                    && order.getPaymentStatus() == PaymentStatus.paid) {
                if (order.isLocked(null)) {
                    return  new PublicResult<Boolean>(PublicResultConstant.ORDER_LOCKED_ERROR, false);
                }

                OrderCancelApply orderCancelApply = new OrderCancelApply();
                orderCancelApply.setOrder(order);
                orderCancelApply.setAmount(order.getAmountPaid());
                orderCancelApply.setSn(order.getSn());
                orderCancelApply.setMember(member);
                orderCancelApplyService.cancelApply(orderCancelApply);

                result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
            } else {
                result = new PublicResult<Boolean>(PublicResultConstant.DATA_ERROR, false);
            }
        }catch (Exception e){
            LOG.error("cancel order error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        LOG.info("cancel order result = " + result.toString());
        return result;
    }

    /**
     * 支付
     */
    @PostMapping("/payment")
    @ApiOperation(value="订单支付", notes="订单支付",produces = "application/json")
    @Log(description="平台中心订单支付接口:/m/order/payment")
    public  PublicResult<Boolean> payment(Long orderId, Long paymentMethodId, Integer methodId, Payment payment, @CurrentUser Member member) {

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
            if (order.isExpired() || order.getOrderStatus() != OrderStatus.confirmed) {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            if (order.getPaymentStatus() != PaymentStatus.unpaid && order.getPaymentStatus() != PaymentStatus.partialPayment) {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            if (payment.getAmount().compareTo(new BigDecimal(0)) <= 0 || payment.getAmount().compareTo(order.getAmountPayable()) > 0) {
                result = new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
			/*if (order.isLocked(admin)) {
				result = new PublicResult<Boolean>(PublicResultConstant.USER_NO_AUTHORITY, false);
			}*/

            //payment.setSn(snService.generate(Sn.Type.payment));
            payment.setType(Payment.Type.payment);
            payment.setStatus(Payment.Status.success);
            payment.setFee(order.getFee());
            payment.setOperator(member.getUsername());
            payment.setPaymentDate(new Date());
            payment.setPaymentPluginId(null);
            payment.setExpire(null);
            payment.setMember(order.getMember());

            orderService.payment(order, payment, null, member);

            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);

        }catch (Exception e){
            LOG.error("cancel order error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("cancel order result = " + result.toString());
        return result;
    }

    /**
     * 完成
     */
    @PostMapping("/complete")
    @ApiOperation(value="订单完成", notes="订单完成",produces = "application/json")
    @Log(description="平台中心订单完成接口:/m/order/complete")
    public PublicResult<Boolean> complete(Long id, @CurrentUser Member member) {

        LOG.info("cancel order param: " + id);
        PublicResult<Boolean> result = null;

        try {
            Order order = orderService.find(id);

            if (order != null && !order.isExpired()
                    && (order.getOrderStatus() == OrderStatus.confirmed || order.getOrderStatus() == OrderStatus.cancel_reject)
                    && (order.getPaymentStatus() == PaymentStatus.paid ||order.getPaymentStatus() == PaymentStatus.refundReject)
                    ) {
                orderService.complete(order, null, member);
            } else {
                new PublicResult<Boolean>(PublicResultConstant.ORDER_STATUS_ERROR, false);
            }
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
