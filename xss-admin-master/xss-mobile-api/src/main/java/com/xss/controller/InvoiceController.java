package com.xss.controller;

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
import com.xss.domain.enums.InvoiceStatus;
import com.xss.service.*;
import com.xss.util.CurrencyMethod;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 *  发票管理接口
 * @author hu
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/invoice")
@Api(description="订单管理模块")
public class InvoiceController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(InvoiceController.class);

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
    private InvoiceService invoiceService;
    @Autowired
    private OrderInvoiceService orderInvoiceService;

    /**
     * 发票索取，获取所有已支付待开发票的订单列表
     */
    @GetMapping("/orders")
    @ApiOperation(value="获取所有已支付待开发票的订单列表", notes="获取所有已支付待开发票的订单列表",produces = "application/json")
    @Log(description="获取所有已支付待开发票的订单列表接口:/m/invoice/orders")
    public PageResult<Object> getOrders(Pageable pageable, String type, @CurrentUser Member member) {
        LOG.debug("get orders list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            pageable.getFilters().add(Filter.eq("orderType", Order.OrderType.quotation));
            Page<Order> orderPage = orderService.findPageInvoice(member, InvoiceStatus.is0, Order.OrderStatus.confirmed, Order.PaymentStatus.paid, pageable);
            JSONArray orderJa = new JSONArray();
            for (Order order : orderPage.getContent()){
                JSONObject orderJo = new JSONObject();
                orderJo.put("key", order.getId());
                orderJo.put("inlineDesc", CurrencyMethod.currency(order.getAmountPaid()));

                Quotation quotation = order.getQuotation();
                String title = quotation.getFeeCategory().getDesc() + " " + quotation.getPayCategory().getDesc();
                orderJo.put("value", title);
                orderJa.add(orderJo);
            }
            result = new PageResult<Object>((int)orderPage.getTotal(), orderPage.getPageNumber(), orderPage.getPageSize(), orderJa);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("get orders list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get orders list result = " + result.toString());
        return result;
    }

    /**
     * 创建订单发票
     */
    @PostMapping("/create")
    @ApiOperation(value="创建订单发票", notes="创建订单发票",produces = "application/json")
    @Log(description="创建订单发票接口:/m/invoice/create")
    public PublicResult<Object> create(@RequestParam Long[] orderIds, @RequestParam BigDecimal amount, @CurrentUser Member member) {
        //获取订单列表
        BigDecimal orderAmount = BigDecimal.ZERO;
        try {
            for (Long id : orderIds) {
                Order order = orderService.find(id);
                if (!Order.OrderStatus.confirmed.equals(order.getOrderStatus()) && !Order.OrderStatus.completed.equals(order.getOrderStatus())
                        && !Order.PaymentStatus.paid.equals(order.getPaymentStatus()) && null != order.getInvoice()) {
                    return new PublicResult<Object>(PublicResultConstant.ERROR, "订单" + order.getSn() + "不符合开票要求");
                }
                orderAmount = orderAmount.add(order.getAmountPaid());
            }
            //判断前后端开票金额是否一致
            if (orderAmount.compareTo(amount) != 0) {
                return new PublicResult<Object>(PublicResultConstant.ERROR, "开票金额不正确");
            }

            //获取当前用户开票信息
            if (member.getInvoices().isEmpty()) {
                return new PublicResult<Object>(PublicResultConstant.ERROR, "开票信息不正确");
            }
            Invoice invoice = new ArrayList<>(member.getInvoices()).get(0);
            if (null == invoice || StringUtils.isBlank(invoice.getAddress()) || StringUtils.isBlank(invoice.getPhone())) {
                return new PublicResult<Object>(PublicResultConstant.ERROR, "开票信息不正确");
            }
            //保存订单发票信息
            OrderInvoice orderInvoice = new OrderInvoice();
            orderInvoice.setAddress(invoice.getAddress());
            orderInvoice.setConsignee(invoice.getConsignee());
            orderInvoice.setInvoiceCategory(invoice.getInvoiceCategory());
            orderInvoice.setInvoiceTitle(invoice.getInvoiceTitle());
            orderInvoice.setInvoiceNo(invoice.getInvoiceNo());
            orderInvoice.setInvoiceType(invoice.getInvoiceType());
            orderInvoice.setPhone(invoice.getPhone());
            orderInvoice.setZipCode(invoice.getZipCode());
            orderInvoice.setMember(member);
            orderInvoice.setInvoiceStatus(InvoiceStatus.is0);
            orderInvoice.setAmount(amount);
            orderInvoiceService.save(orderInvoice);
            //更新订单
            for (Long id : orderIds) {
                Order order = orderService.find(id);
                order.setInvoice(orderInvoice);
                orderService.update(order);
            }
            return new PublicResult<Object>(PublicResultConstant.SUCCESS, "订单开发票成功");
        }catch (Exception e){
            e.printStackTrace();
            return new PublicResult<Object>(PublicResultConstant.ERROR, "订单开发票失败");
        }

    }

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="发票列表", notes="分页展示发票列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取发票列表接口:/m/invoice/list")
    public PageResult<Object> list(Pageable pageable, String type, @CurrentUser Member member) {
        LOG.debug("get invoice list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            pageable.getFilters().add(Filter.eq("member", member));

            Page<OrderInvoice> data = orderInvoiceService.findPage(pageable);

            result = new PageResult<Object>((int)data.getTotalPages(), data.getPageNumber(), data.getPageSize(), orderInvoiceService.createEntity()
                    .convertList(data.getContent(), null));
        }catch (Exception e){
            LOG.error("get invoice list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get invoice list result = " + result.toString());
        return result;
    }


    /**
     * 获取用户发票信息
     */
    @GetMapping("/info")
    @ApiOperation(value="发票详情", notes="根据id获取发票详情",produces = "application/json")
    @Log(description="获取发票详情接口:/m/invoice/info")
    public PublicResult<JSONObject> info(@CurrentUser Member member) throws Exception{
        Set<Invoice> invoices = member.getInvoices();
        JSONObject result = null;
        if (!invoices.isEmpty()){
            Invoice invoice = (Invoice) invoices.toArray()[0];
            result = invoice.convertEntity(invoice, null);
        }

        LOG.debug("get quotation info result = " + (result == null ? "" : result.toString()));
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    /**
     * 保存用户发票信息
     * @param invoice
     * @return
     * @throws Exception
     */
    @PostMapping("/save")
    @ApiOperation(value="保存/更新发票", notes="保存发票",produces = "application/json")
    @Log(description="平台中心保存发票接口:/m/invoice/save")
    public PublicResult<String> save(@RequestBody Invoice invoice, @CurrentUser Member member){
        try {
            if (null == invoice.getId()) {
                invoice.setMember(member);
                invoiceService.save(invoice);
                return new PublicResult<>(PublicResultConstant.SUCCESS, "操作成功");
            }else{
                Invoice persist = invoiceService.find(invoice.getId());
                if (null != invoice.getInvoiceType()) {
                    persist.setInvoiceType(invoice.getInvoiceType());
                }
                if (null != invoice.getInvoiceCategory()){
                    persist.setInvoiceCategory(invoice.getInvoiceCategory());
                }
                if (StringUtils.isNotBlank(invoice.getInvoiceTitle())) {
                    persist.setInvoiceTitle(invoice.getInvoiceTitle());
                }
                invoiceService.update(persist);
                return new PublicResult<>(PublicResultConstant.SUCCESS, "操作成功");
            }
        }catch (Exception e){
            e.printStackTrace();;
            return new PublicResult<>(PublicResultConstant.ERROR, "操作失败");
        }

    }


//    /**
//     * 详情
//     */
//    @GetMapping("/info")
//    @ApiOperation(value="发票详情", notes="根据id获取发票详情",produces = "application/json")
//    @Log(description="平台中心获取发票详情接口:/m/invoice/info")
//    public PublicResult<JSONObject> info(Long id) throws Exception{
//        LOG.debug("get invoice info param = " + id);
//
//        Order order = orderDao.findOne(id);
//
//        // 发票信息
//        JSONObject result = order.convertEntity(order, new String[]{"status", "isExpired", "expiredTime", "amountPayable"});
//
//        // 报价单信息
//        if(order != null && order.getQuotation() != null) {
//            // 组装JSON
//            result.put("quotation", quotationService.setQuotationConfirmJson(order.getQuotation()));
//        }
//
//        LOG.debug("get quotation info result = " + (result == null ? "" : result.toString()));
//        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
//    }

    /**
     * 详情
     */
    @GetMapping("/payInfo")
    @ApiOperation(value="发票详情", notes="根据id获取发票详情",produces = "application/json")
    @Log(description="平台中心获取发票详情接口:/m/invoice/payInfo")
    public PublicResult<JSONObject> payInfo(Long id) throws Exception{
        LOG.debug("get invoice info param = " + id);

        Order order = orderDao.findOne(id);
        JSONObject result = order.convertEntity(order,  new String[]{"serviceName", "isExpired"});

        List<PaymentMethod> paymentMethodList = paymentMethodDao.findAll();
        result.put("paymentMethodList", JsonUtil.toJSONArray(paymentMethodList, new String[]{"id", "name", "content", "description", "icon", "method"}));

        LOG.debug("get invoice info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    


    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除发票", notes="批量删除发票",produces = "application/json")
    @Log(description="平台中心批量删除发票接口:/m/invoice/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete invoice param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            orderService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete invoice error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete invoice result = " + result.toString());
        return result;
    }

    /**
     * 未支付 取消
     */
    @PostMapping("/cancel")
    @ApiOperation(value="发票取消", notes="发票取消",produces = "application/json")
    @Log(description="平台中心发票取消接口:/m/invoice/cancel")
    public PublicResult<Boolean> cancel(Long id, @CurrentUser Member member) {
        LOG.info("cancel invoice param: " + id);
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
            LOG.error("cancel invoice error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("cancel invoice result = " + result.toString());
        return result;
    }

}
