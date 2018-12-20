package com.xss.controller;

import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Order;
import com.xss.service.*;
import io.swagger.annotations.Api;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *  后台首页接口
 * @author fan.hu
 * @since 2018-08-10
 */
@RestController
@RequestMapping("/api/home")
@Api(description="后台首页接口")
public class HomeController {

    @Autowired
    private MemberService memberService;
//    @Autowired
//    private MessageService messageService;
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
//    @Autowired
//    private StoreService storeService;

    /**
     * 统计
     */
    @GetMapping("/count")
    public PublicResult<JSONObject> count() {
        PublicResult<JSONObject> result = null;
        try{
            JSONObject jo = new JSONObject();
            //会员数
            Long memberNum = memberService.count();
            jo.accumulate("memberNum", memberNum);
            //未读消息数
//            Long messageNum = messageService.count(null, false);
//            jo.accumulate("messageNum",0);
            //上架商品数
            Long marketableProductNum = productService.count(null, true, null, null, false, null, null);
            jo.accumulate("marketableProductNum", marketableProductNum);
            //兑换订单数
            Long orderServiceNum = orderService.findByType(Order.OrderType.service);
            jo.accumulate("orderServiceNum", orderServiceNum);

            //报价订单数
            Long orderQuotationNum = orderService.findByType(Order.OrderType.quotation);
            jo.accumulate("orderQuotationNum", orderQuotationNum);
            //企业数
//            Long storeNum = storeService.count();
            //jo.accumulate("storeNum",0);
            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, jo);
        }catch (Exception e){
            result = new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
        }
        return result;
    }

}
