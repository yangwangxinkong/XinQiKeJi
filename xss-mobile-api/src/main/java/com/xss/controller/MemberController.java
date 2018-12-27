package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.CouponCode;
import com.xss.domain.Member;
import com.xss.service.CouponCodeService;
import com.xss.service.MemberService;
import com.xss.util.CommonUtil;
import com.xss.util.DateUtil;
import com.xss.util.JsonUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 移动端用户接口
 * @author zzl
 * @date 2018/8/31
 */
@RestController
@RequestMapping("/m/member")
@Api(description="用户接口")
public class MemberController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(MemberController.class);

    @Autowired
    private MemberService memberService;
    @Autowired
    private CouponCodeService couponCodeService;

    @ApiOperation(value="获取当前用户", notes="获取当前用户", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @GetMapping("/current")
    @Log(description="获取当前用户接口:/m/member/current")
    public PublicResult<JSONObject> current(@CurrentUser Member member) {
        JSONObject memberJo = member.convertEntity(member, null);
        return new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, memberJo);
    }

    @ApiOperation(value="更新用户信息", notes="更新用户信息", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "", required = true, dataType = "String",paramType="body")})
    @PostMapping("/update")
    @Log(description="更新用户信息接口:/m/member/update")
    public PublicResult<Boolean> update(@RequestBody Member member, @CurrentUser Member current) {
        try {
            if (StringUtils.isNotBlank(member.getNickName())){
                //昵称
                current.setName(member.getNickName());
            }
            if (StringUtils.isNotBlank(member.getName())){
                //姓名
                //String nickName = Base64.encodeBase64String(member.getName().getBytes("utf-8"));
                current.setName(member.getName());
            }
            if (null != member.getGender()){
                //性别
                current.setGender(member.getGender());
            }
            if (StringUtils.isNotBlank(member.getIdentification())){
                //身份证号
                current.setIdentification(member.getIdentification());
            }
            if (StringUtils.isNotBlank(member.getHeadImage())){
                //个人头像
                current.setHeadImage(member.getHeadImage());
            }
            if (StringUtils.isNotBlank(member.getMobile())){
                //手机号码
                current.setMobile(member.getMobile());
            }
            if (StringUtils.isNotBlank(member.getIdFace())) {
                //身份证正面照
                current.setIdFace(member.getIdFace());
            }
            if (StringUtils.isNotBlank(member.getIdBackFace())) {
                //身份证反面照
                current.setIdBackFace(member.getIdBackFace());
            }
            if (StringUtils.isNotBlank(member.getHukouIndex())) {
                //户口本首页附件
                current.setHukouIndex(member.getHukouIndex());
            }
            if (StringUtils.isNotBlank(member.getHukouPerson())) {
                //户口本本人页附件
                current.setHukouPerson(member.getHukouPerson());
            }
            if (StringUtils.isNotBlank(member.getOnePhone())) {
                //本人一寸照片
                current.setOnePhone(member.getOnePhone());
            }

            memberService.update(current);
            return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, null);
        }catch (Exception e){
            e.printStackTrace();
            return new PublicResult<Boolean>(PublicResultConstant.ERROR, null);
        }
    }

    @ApiOperation(value="用户优惠券列表", notes="获取用户优惠券列表",produces = "application/json")
    @GetMapping("/coupon/list")
    @Log(description="平台中心获取用户优惠券列表接口:/m/member/coupon/list")
    public PageResult<Object> list(Pageable pageable,Boolean isUsed,Boolean hasBegun, Boolean hasExpired,@CurrentUser Member member)throws Exception{
        LOG.debug("get Member coupon list param = " + isUsed);
        PageResult<Object> result = null;
        try{
            JSONObject jo = new JSONObject();
            Page<CouponCode> page = couponCodeService.findPage(member, isUsed,hasBegun,hasExpired, pageable);
            JSONArray couponCodeArray = new JSONArray();
            if (null != page && null != page.getContent() && !page.getContent().isEmpty()) {
                for(CouponCode couponCode:page.getContent()){
                    JSONObject couponCodeJo = JsonUtil.toJSONObject(couponCode, new String[]{"id","code"});
                    couponCodeJo.put("name",couponCode.getCoupon().getName());
                    if(couponCode.getIsUsed()){
                        couponCodeJo.put("isUsed","已使用");
                    }else{
                        couponCodeJo.put("isUsed","未使用");
                    }
                    if(couponCode.getUsedDate()!=null){
                        couponCodeJo.put("usedDate", DateUtil.format(couponCode.getUsedDate(), "yyyy-MM-dd HH:mm:ss"));
                    }else{
                        couponCodeJo.put("usedDate","-");
                    }
                    if(couponCode.getCoupon().getBeginDate()!=null){
                        couponCodeJo.put("beginDate", DateUtil.format(couponCode.getCoupon().getBeginDate(), "yyyy.MM.dd"));
                    }else{
                        couponCodeJo.put("beginDate","-");
                    }
                    if(couponCode.getCoupon().getEndDate()!=null){
                        couponCodeJo.put("endDate", DateUtil.format(couponCode.getCoupon().getEndDate(), "yyyy.MM.dd"));
                    }else{
                        couponCodeJo.put("endDate","-");
                    }

                    String discount="";
                    if(couponCode.getCoupon().getPriceExpression()!=null){
                        String priceExpression=couponCode.getCoupon().getPriceExpression();
                        if(priceExpression.indexOf("*")>-1){
                            String disStr=priceExpression.substring(priceExpression.indexOf("*")+1);
                            if(CommonUtil.isDouble(disStr)){
                                double disDb=Double.parseDouble(disStr);
                                discount=String.format("%.1f", (disDb*10));
                            }
                        }
                    }
                    couponCodeJo.put("discount", discount);

                    couponCodeArray.add(couponCodeJo);
                }
            }

            result = new PageResult<Object>(page.getTotalPages(),page.getPageNumber(),page.getPageSize(), couponCodeArray);
        }catch (Exception e){
            LOG.error("get Member coupon list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Member coupon list result = " + result.toString());
        return result;
    }
}
