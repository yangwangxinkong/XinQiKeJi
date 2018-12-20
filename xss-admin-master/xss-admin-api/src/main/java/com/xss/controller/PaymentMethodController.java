package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.ParamXssPass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.PaymentMethod;
import com.xss.service.PaymentMethodService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 *  支付方式接口
 * @author fan.hu
 * @since 2018-08-06
 */

@RestController
@RequestMapping("/api/payment_method")
@Api(description="支付方式")
public class PaymentMethodController {

    private static final Log LOG = LogFactory.getLog(PaymentMethodController.class);

    @Autowired
    PaymentMethodService paymentMethodService;
//    @Autowired
//    ShippingMethodService shippingMethodService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public PageResult<Object> list(Pageable pageable) {
        LOG.info("get PaymentMethod list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<PaymentMethod> data = paymentMethodService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(),paymentMethodService.createEntity().convertList(data.getContent(),PaymentMethod.DEFAULT_JSON_PARAMS));
        }catch (Exception e){
            LOG.error("get PaymentMethod list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.info("get PaymentMethod list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    public PublicResult<JSONObject> info(@RequestParam Long id) {
        LOG.info("get PaymentMethod info param: id = " + id);
        PublicResult<JSONObject> result = null;
        try{
            PaymentMethod temp = paymentMethodService.findById(id);
            JSONObject jo = temp.convertEntity(temp,new String[]{"shippingMethods"});
//            JSONArray ja = new JSONArray();
//            if(temp.getShippingMethods()!=null && !temp.getShippingMethods().isEmpty()){
//                for (ShippingMethod shippingMethod:temp.getShippingMethods()) {
//                    ja.add(shippingMethod.getId());
//                }
//            }
//            jo.put("shippingMethods",ja);
            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, jo);
        }catch (Exception e){
            LOG.error("get PaymentMethod info error. {}", e);
            result =new PublicResult<JSONObject>(PublicResultConstant.FAILED,null);
        }
        LOG.info("get PaymentMethod info result = " + result.toString());
        return result;
    }

    /**
     * 新增
     */
    @PostMapping("/save")
    @ParamXssPass
    public PublicResult<Boolean> save(@RequestBody JSONObject paymentMethodJO) {
        LOG.info("save PaymentMethod param: " + paymentMethodJO.toString());
        PublicResult<Boolean> result = null;
        try{
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setName(paymentMethodJO.getString("name"));
            paymentMethod.setTimeout(paymentMethodJO.getInteger("timeout"));
            paymentMethod.setDescription(paymentMethodJO.getString("description"));
            paymentMethod.setOrder(paymentMethodJO.getInteger("order"));
            paymentMethod.setContent(paymentMethodJO.getString("content"));
            paymentMethod.setIcon(paymentMethodJO.getString("icon"));
            paymentMethod.setCode(paymentMethodJO.getString("code"));

            //配送方式
            List<Long> shippingMethodIds = new ArrayList<>();
            JSONArray ja =paymentMethodJO.getJSONArray("shippingMethods");
            for (int i=0;i<ja.size();i++) {
                shippingMethodIds.add(ja.getLong(i));
            }
//            paymentMethod.setShippingMethods(new HashSet<ShippingMethod>(shippingMethodService.findListByIDList(shippingMethodIds)));

            //方式
            String me = paymentMethodJO.getString("method");
            PaymentMethod.Method method = null;
            for (PaymentMethod.Method method1 : PaymentMethod.Method.values()){
                if (method1.name().equals(me)) {
                    method = method1;
                    break;
                }
            }
            paymentMethod.setMethod(method);

            paymentMethodService.save(paymentMethod);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("save PaymentMethod error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("save PaymentMethod result = " + result.toString());
        return result;
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete PaymentMethod param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            paymentMethodService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete PaymentMethod error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete PaymentMethod result = " + result.toString());
        return result;
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    @ParamXssPass
    public PublicResult<Boolean> update(@RequestBody JSONObject paymentMethodJO) {
        LOG.info("update PaymentMethod param: " + paymentMethodJO.toString());
        PublicResult<Boolean> result = null;
        try{
            if(paymentMethodJO.getLong("id") == null){
                return new PublicResult<Boolean>(PublicResultConstant.PARAM_ERROR,false);
            }
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setId(paymentMethodJO.getLong("id"));
            paymentMethod.setName(paymentMethodJO.getString("name"));
            paymentMethod.setTimeout(paymentMethodJO.getInteger("timeout"));
            paymentMethod.setDescription(paymentMethodJO.getString("description"));
            paymentMethod.setOrder(paymentMethodJO.getInteger("order"));
            paymentMethod.setContent(paymentMethodJO.getString("content"));
            paymentMethod.setIcon(paymentMethodJO.getString("icon"));
            paymentMethod.setCode(paymentMethodJO.getString("code"));

            //配送方式
            List<Long> shippingMethodIds = new ArrayList<>();
            JSONArray ja =paymentMethodJO.getJSONArray("shippingMethods");
            for (int i=0;i<ja.size();i++) {
                shippingMethodIds.add(ja.getLong(i));
            }
//            paymentMethod.setShippingMethods(new HashSet<ShippingMethod>(shippingMethodService.findListByIDList(shippingMethodIds)));

            //方式
            String me = paymentMethodJO.getString("method");
            PaymentMethod.Method method = null;
            for (PaymentMethod.Method method1 : PaymentMethod.Method.values()){
                if (method1.name().equals(me)) {
                    method = method1;
                    break;
                }
            }
            paymentMethod.setMethod(method);

            paymentMethodService.update(paymentMethod,"orders");
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("update PaymentMethod error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("update PaymentMethod result = " + result.toString());
        return result;
    }

    /**
     * 支付方式
     */
    @GetMapping("/method")
    public PublicResult<JSONObject> getMethod() {
        PublicResult<JSONObject> result = null;
        try{
//            JSONObject jo = paymentMethodService.getMethod();
            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS,paymentMethodService.getMethod());
        }catch (Exception e){
            LOG.error("delete PaymentMethod error. {}", e);
            result =new PublicResult<JSONObject>(PublicResultConstant.FAILED,null);
        }
        LOG.info("delete PaymentMethod result = " + result.toString());
        return result;
    }

//    private JSONArray convertPaymentMethods(List<PaymentMethod> paymentMethods){
//        JSONArray array = new JSONArray();
//        if (null != paymentMethods && !paymentMethods.isEmpty()){
//            for (PaymentMethod paymentMethod : paymentMethods){
//                JSONObject jo = convertPaymentMethod(paymentMethod);
//                array.add(jo);
//            }
//        }
//        return array;
//    }

//    private JSONObject convertPaymentMethod(PaymentMethod paymentMethod){
//        JSONObject jo = JsonUtil.toJSONObject(paymentMethod, new String[]{"id", "name", "content", "description", "icon", "method", "timeout"});
//        jo.put("order",paymentMethod.getOrder() != null ? paymentMethod.getOrder() : null);
//        return jo;
//    }

}
