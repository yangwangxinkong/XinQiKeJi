package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.Coupon;
import com.xss.domain.CouponCode;
import com.xss.service.CityService;
import com.xss.service.CouponCodeService;
import com.xss.service.CouponService;
import com.xss.util.*;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/coupon")
public class CouponController {

    private static final Log LOG = LogFactory.getLog(CouponController.class);
    @Resource
    private CouponService couponService;
    @Resource
    private CouponCodeService couponCodeService;
    @Resource
    private CityService cityService;

    /**
     * 检查价格运算表达式是否正确
     */
    @GetMapping("/check_price_expression")
    public boolean checkPriceExpression(String priceExpression) {
        if (StringUtils.isEmpty(priceExpression)) {
            return false;
        }
        try {
            Map<String, Object> model = new HashMap<>();
            model.put("quantity", 111);
            model.put("price", new BigDecimal("9.99"));
            new BigDecimal(FreemarkerUtils.process("#{(" + priceExpression + ");M50}", model));
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("CouponController Method checkPriceExpression exception!!");
            return false;
        }
    }

    /**
     * 保存
     */
    @PostMapping("/save")
    public PublicResult<Object> save(Coupon coupon, @RequestParam("beginAndEndDate[]") Long[] beginAndEndDate, @CurrentUser Admin admin) {
        couponDate(coupon, beginAndEndDate);
        if (coupon.getBeginDate() != null && coupon.getEndDate() != null && coupon.getBeginDate().after(coupon.getEndDate())) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        if (coupon.getIsExchange() && coupon.getPoint() == null) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        try {
//            if(StringUtils.isBlank(coupon.getCityCode())) {
//                coupon.setCityCode(cityService.getCityCodeByAdmin(admin));
//                LOG.info("Method save cityCode:" + coupon.getCityCode());
//            }
            couponService.saveCoupon(coupon);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("CouponController Method save exception!! params: "
                    + "coupon:"+JsonUtil.getJsonString(coupon)
                    +",beginAndEndDate:"+beginAndEndDate);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }

    /**
     * 更新
     */
    @PostMapping("/update")
    public PublicResult<Object> update(Coupon coupon, @RequestParam("beginAndEndDate[]") Long[] beginAndEndDate, @CurrentUser Admin admin) {
        couponDate(coupon, beginAndEndDate);
        if (coupon.getBeginDate() != null && coupon.getEndDate() != null && coupon.getBeginDate().after(coupon.getEndDate())) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        if (coupon.getMinimumQuantity() != null && coupon.getMaximumQuantity() != null && coupon.getMinimumQuantity() > coupon.getMaximumQuantity()) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        if (coupon.getMinimumPrice() != null && coupon.getMaximumPrice() != null && coupon.getMinimumPrice().compareTo(coupon.getMaximumPrice()) > 0) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        if (coupon.getIsExchange() && coupon.getPoint() == null) {
            return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
        }
        try {
//            if(StringUtils.isBlank(coupon.getCityCode())) {
//                coupon.setCityCode(cityService.getCityCodeByAdmin(admin));
//                LOG.info("Method update cityCode:"+coupon.getCityCode());
//            }
            couponService.updateCoupon(coupon);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("CouponController Method update exception!! params: "
                    + "coupon:"+JsonUtil.getJsonString(coupon)
                    +",beginAndEndDate:"+beginAndEndDate);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }

        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }


    /**
     * 列表
     */
    @GetMapping("/list")
    public PageResult list(Pageable pageable,@CurrentUser Admin admin) {
        Page page = couponService.findPage(pageable);
        PageResult result = new PageResult();

        result.setTotal((int) page.getTotal());
        result.setPageSize(page.getPageSize());
        result.setPageIndex(page.getPageNumber());
        try {
            result.setList(couponService.createEntity().convertList(page.getContent(), Coupon.PROPERTIES));
        } catch (Exception e) {
            LOG.error("CouponController Method list exception!! params: "
                    + "pageable:"+JsonUtil.getJsonString(pageable));
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 查询优惠码及使用信息列表
     */
    @GetMapping("/code/list")
    public PageResult getCodeList(Pageable pageable, Long couponId) {
        PageResult<Object> result = null;
        if (null == couponId) {
            return new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        try {
            Coupon coupon = new Coupon();
            coupon.setId(couponId);
            pageable.getFilters().add(Filter.eq("coupon", coupon));
            Page<CouponCode> page = couponCodeService.findPage(pageable);
            result = new PageResult<Object>((int)page.getTotal(),page.getPageNumber(),page.getPageSize(), couponCodeService.createEntity().convertList(page.getContent(), null));
        } catch (Exception e) {
            LOG.error("CouponController Method getCodeList exception!! params: "
                    + "pageable:"+JsonUtil.getJsonString(pageable));
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 查询单个优惠券信息
     */
    @GetMapping("/info")
    public PublicResult info(Long couponId) {
        try{
            Coupon coupon = couponService.find(couponId);
            return new PublicResult<>(PublicResultConstant.SUCCESS,JsonUtil.toJSONObject(coupon, Coupon.jsonProperties));
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("CouponController Method info exception!! params: "
                    + "couponId:"+couponId);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }

    /**
     * 删除
     */
    @PostMapping("/delete")
    public PublicResult delete(@RequestParam("ids[]")Long[] ids) {
        try{
            couponService.delete(ids);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("CouponController Method delete exception!! params: "
                    + "ids:"+ids);
            return new PublicResult<>(PublicResultConstant.DATA_ERROR, null);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
    }

    /**
     * 生成优惠码
     */
    @GetMapping("/build")
    public PublicResult<JSONObject> build(Long id) {
        Coupon coupon = couponService.find(id);
        JSONObject jsonObject = null;
        try {
            jsonObject = couponService.createEntity().convertEntity(coupon, new String[]{"id","name","beginDate","endDate"});
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("CouponController Method build exception!! params: "
                    + "id:"+id);
        }
        jsonObject.put("totalCount", couponCodeService.count(coupon, null, null, null, null));
        jsonObject.put("usedCount", couponCodeService.count(coupon, null, null, null, true));
        return new PublicResult<>(PublicResultConstant.SUCCESS, jsonObject);
    }

    /**
     * 下载优惠码
     */
    @PostMapping("/download")
    public PublicResult download(Long id, Integer count, @CurrentUser Admin admin) {
        if (count == null || count <= 0) {
            count = 50;
        }
        Coupon coupon = couponService.find(id);
        List<CouponCode> data = couponCodeService.build(coupon, null, count);
        String[] contents = new String[5];
        //用作空行
        contents[0] = " ";

        contents[1] = "优惠券: " + coupon.getName();
        contents[2] = "生成数量: " + count;
        contents[3] = "操作员: " + admin.getUsername();
        contents[4] = "生成日期: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        JSONObject[] couponCodeData = new JSONObject[data==null?0:data.size() + contents.length];
        for(int i=0; i<data.size(); i++){
            couponCodeData[i] = new JSONObject();
            couponCodeData[i].put("code", data.get(i).getCode());
        }

        // 把优惠码的生成信息加在优惠码列表后面
        for (int i=0; i<contents.length; i++){
            couponCodeData[data.size()+i] = new JSONObject();
            couponCodeData[data.size()+i].put("code", contents[i]);
        }

        return new PublicResult<>(PublicResultConstant.SUCCESS, couponCodeData);
    }







    /**
     * 优惠券使用开始时间、使用结束时间处理
     * @param coupon
     * @param beginAndEndDate
     * @return
     */
    private Coupon couponDate(Coupon coupon, Long[] beginAndEndDate) {
        if(beginAndEndDate!=null){
            if(beginAndEndDate[0]!=null){
                coupon.setBeginDate(new Date(beginAndEndDate[0]));
            }
            if(beginAndEndDate[1]!=null){
                coupon.setEndDate(new Date(beginAndEndDate[1]));
            }
        }
        return coupon;
    }



}
