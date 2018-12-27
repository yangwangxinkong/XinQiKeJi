package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.ArticleCategoryDao;
import com.xss.domain.Article;
import com.xss.domain.ArticleCategory;
import com.xss.domain.Coupon;
import com.xss.domain.Member;
import com.xss.service.ArticleCategoryService;
import com.xss.service.ArticleService;
import com.xss.service.CouponCodeService;
import com.xss.service.CouponService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 移动端优惠券相关接口
 * @author zzl
 * @date 2018/8/29
 */
@RestController
@RequestMapping("/m/coupon")
@Api(description="优惠券接口")
public class CouponController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(CouponController.class);

    @Autowired
    private CouponService couponService;
    @Autowired
    private CouponCodeService couponCodeService;

    @ApiOperation(value="优惠券列表", notes="获取优惠券列表",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取优惠券列表接口:/m/coupon/list")
    public PageResult<Object> list(Pageable pageable, Boolean isExchange)throws Exception{
        LOG.debug("get Coupon list param = " + isExchange);
        PageResult<Object> result = null;
        try{
            if(null == isExchange){
                isExchange = true;
            }
            Page<Coupon> page = couponService.findPage(true, isExchange, false, pageable);

            result = new PageResult<Object>(page.getTotalPages(),page.getPageNumber(),page.getPageSize(), couponService.createEntity().convertList(page.getContent(), null));
        }catch (Exception e){
            LOG.error("get Coupon list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Coupon list result = " + result.toString());
        return result;
    }

    /**
     * 查询单个优惠券信息
     */
    @GetMapping("/detail")
    public PublicResult detail(Long id) {
        try{
            Coupon coupon = couponService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS, coupon.convertEntity(coupon, null));
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("CouponController Method info exception!! params: " + "id:"+id);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }

    /**
     * 查询单个优惠券信息
     */
    @GetMapping("/exchange")
    public PublicResult exchange(Long id, @CurrentUser Member member) {
        try{
            Coupon coupon = couponService.find(id);
            if (coupon == null || !coupon.getIsEnabled() || !coupon.getIsExchange() || coupon.hasExpired()) {
                return new PublicResult<>(PublicResultConstant.ERROR, "参数错误");
            }
            if (member.getPoint() < coupon.getPoint()) {
                return new PublicResult<>(PublicResultConstant.ERROR, "用户金豆余额不足，兑换失败。");
            }
            couponCodeService.exchange(coupon, member);
            return new PublicResult<>(PublicResultConstant.SUCCESS, null);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("CouponController Method info exception!! params: " + "id:"+id);
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }
}
