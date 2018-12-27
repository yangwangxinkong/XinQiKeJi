package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.CityDao;
import com.xss.dao.PaymentMethodDao;
import com.xss.dao.QuotationDao;
import com.xss.dao.ResidenceTypeDao;
import com.xss.domain.*;
import com.xss.domain.enums.*;
import com.xss.service.*;
import com.xss.util.BigDecimalUtils;
import com.xss.util.DateUtil;
import com.xss.util.FreemarkerUtils;
import com.xss.util.JWTUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 *  报价单管理接口
 * @author hu
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/quotation")
@Api(description="报价单管理模块")
public class QuotationController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(QuotationController.class);

    @Resource
    private QuotationDao quotationDao;
    @Resource
    private CityDao cityDao;
    @Resource
    private ResidenceTypeDao residenceTypeDao;
    @Resource
    private PaymentMethodDao paymentMethodDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private PayBaseService payBaseService;
    @Autowired
    private QuotationItemService quotationItemService;
    @Autowired
    private MemberService memberService;
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
    @ApiOperation(value="报价单列表", notes="分页展示报价单列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取报价单列表接口:/m/quotation/list")
    public PageResult<Object> list(Pageable pageable) {
        LOG.debug("get quotation list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Quotation> data = quotationService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), quotationService.createEntity().convertList(data.getContent(), new String[]{""}));
        }catch (Exception e){
            LOG.error("get quotation list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get quotation list result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="报价单详情", notes="根据id获取报价单详情",produces = "application/json")
    @Log(description="平台中心获取报价单详情接口:/m/quotation/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get quotation info param = " + id);

        Quotation quotation = quotationDao.findOne(id);
        JSONObject result = quotation.convertEntity(quotation, new String[]{"city", "residenceType"});

        LOG.debug("get quotation info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }

    /**
     * 详情 报价个人信息
     */
    @GetMapping("/quotationMemberInfo")
    @ApiOperation(value="报价单详情", notes="根据id获取报价单详情",produces = "application/json")
    @Log(description="平台中心获取报价单详情接口:/m/quotation/quotationMemberInfo")
    public PublicResult<JSONObject> quotationMemberInfo(Long id) throws Exception{
        LOG.debug("get quotation info param = " + id);

        Quotation quotation;
        JSONObject result = null;

        if(id != null) {
            quotation = quotationDao.findOne(id);
            if (null != quotation) {
                result = quotation.convertEntity(quotation, new String[]{"city", "cityid", "residenceType", "allAmount", "orderAmount"});
            }else{
                return new PublicResult<>(PublicResultConstant.FAILED, result);
            }
        }

        LOG.debug("get quotation info result = " + (result == null ? "" : result.toString()));
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    /**
     * 详情 报价信息
     */
    @GetMapping("/quotationPayInfo")
    @ApiOperation(value="报价单详情", notes="根据id获取报价单详情",produces = "application/json")
    @Log(description="平台中心获取报价单详情接口:/m/quotation/quotationPayInfo")
    public PublicResult<JSONObject> quotationPayInfo(Long id) throws Exception{
        LOG.debug("get quotation info param = " + id);

        Quotation quotation;
        JSONObject result = null;

        if(id != null) {
            quotation = quotationDao.findOne(id);

            result = quotation.convertEntity(quotation, new String[]{"city", "cityid", "residenceType", "feeCategory", "allAmount", "orderAmount"});

            // 参保信息页面
            PayBase payBase = payBaseService.getPayBaseByCityId(quotation.getCity().getId());

            // 缴费基数
            result.put("payBase", payBaseService.createEntity().convertEntity(payBase, new String[]{""}));

            // 缴费方式
            JSONArray ja = new JSONArray();
            for(PayCategory payCategory : PayCategory.values()) {
                JSONObject jo = new JSONObject();
                jo.put("key", payCategory.getValue());
                jo.put("value", payCategory.getDesc());
                ja.add(jo);
            }
            result.put("payCategorys", ja);
        }

        LOG.debug("get quotation info result = " + (result == null ? "" : result.toString()));
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    /**
     * 详情 报价确认信息
     */
    @GetMapping("/quotationConfirmInfo")
    @ApiOperation(value="报价单详情", notes="根据id获取报价单详情",produces = "application/json")
    @Log(description="平台中心获取报价单详情接口:/m/quotation/quotationConfirmInfo")
    public PublicResult<JSONObject> quotationConfirmInfo(Long id, @CurrentUser Member member) throws Exception{
        LOG.debug("get quotation info param = " + id);

        Quotation quotation;
        JSONObject result = null;

        if(id != null) {
            quotation = quotationDao.findOne(id);

            // 组装JSON
            result = quotationService.setQuotationConfirmJson(quotation);

            result.put("balance", member.getBalance());
            result.put("useBalance", member.getBalance().compareTo(quotation.getAmount()) > 0 ? quotation.getAmount() : member.getBalance());
            result.put("usedBalanceAmount", member.getBalance().compareTo(quotation.getAmount()) > 0 ? BigDecimal.ZERO : quotation.getAmount().subtract(member.getBalance()));
        }

        LOG.debug("get quotation info result = " + (result == null ? "" : result.toString()));
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    @PostMapping("/saveQuotationMemberInfo")
    @ApiOperation(value="保存/更新报价单", notes="保存报价单",produces = "application/json")
    @Log(description="平台中心保存报价单接口:/m/quotation/saveQuotationMemberInfo")
    public PublicResult<String> saveQuotationMemberInfo(@RequestBody Quotation quotation, HttpServletRequest request)throws Exception{
        if (null != quotation && null != quotation.getId()) {

            Quotation quotationTemp = quotationDao.findOne(quotation.getId());
            // 如果更新的数据为空 或者 次报价单已经关联有订单时，重新生成报价单  加大容错
            if(quotationTemp == null || quotationTemp.getOrder() != null) {
                quotationTemp = new Quotation();
                if(StringUtils.isEmpty(quotation.getStartDate())) {
                    quotationTemp.setStartDate(DateUtil.format(new Date(), "yyyy-MM"));
                }
                if (quotation.getMonthCount() == null) {
                    quotationTemp.setMonthCount(1);
                }
                if(StringUtils.isEmpty(quotation.getEndDate())) {
                    quotationTemp.setEndDate(quotationTemp.getStartDate());
                }
                if(quotation.getPayCategory() == null) {
                    quotationTemp.setPayCategory(PayCategory.pc0);
                }
            }

            // 用户名
            if(StringUtils.hasText(quotation.getUsername())) {
//                quotationTemp.setUsername(Base64.encodeBase64String(quotation.getUsername().getBytes("utf-8")));
                quotationTemp.setUsername(quotation.getUsername());
            }
            System.out.println("userName1:" + quotationTemp.getUsername());
            // 身份证
            if(StringUtils.hasText(quotation.getIdentification())) {
                quotationTemp.setIdentification(quotation.getIdentification());
            }

            // 手机号
            if(StringUtils.hasText(quotation.getMobile())) {
                quotationTemp.setMobile(quotation.getMobile());
            }

            // 缴费方式
            if(quotation.getFeeCategory() != null) {
                FeeCategory feeCategoryTemp = quotationTemp.getFeeCategory();
                quotationTemp.setFeeCategory(quotation.getFeeCategory());
                //
//                if(!quotationTemp.getFeeCategory().equals(feeCategoryTemp)) {
//                    // 正常的计算 保存
//                    quotationService.setCalculateQuotation(quotationTemp, CalculatorCategory.cc2);
//                }
            }
            // 户口性质
            if(quotation.getResidenceType() != null && quotation.getResidenceType().getId() != null) {
                quotationTemp.setResidenceType(quotation.getResidenceType());
            }
            // 城市
            if(quotation.getCity() != null && quotation.getCity().getId() != null) {
                quotationTemp.setCity(cityDao.findOne(quotation.getCity().getId()));
            }
            String token = request.getHeader("Authorization");
            if (StringUtils.hasText(token) && null == quotationTemp.getMember()){
                String userNo = JWTUtil.getUserNo(token);
                Member user = memberService.findByUsername(userNo);
                if (null != user){
                    quotationTemp.setMember(user);
                }
            }
            quotationService.saveQuotaion(quotationTemp);

//            // 如果更新的数据为空  容错率更新
//            if(quotationTemp.getId() == null) {
//                quotationService.save(quotationTemp);
//            } else {
//                quotationService.update(quotationTemp);
//            }
            return new PublicResult<>(PublicResultConstant.SUCCESS, quotationTemp.getId().toString());
        }else{
            // 初次保存默认值
            if(StringUtils.isEmpty(quotation.getStartDate())) {
                quotation.setStartDate(DateUtil.format(new Date(), "yyyy-MM"));
            }
            if (quotation.getMonthCount() == null) {
                quotation.setMonthCount(1);
            }
            if(StringUtils.isEmpty(quotation.getEndDate())) {
                quotation.setEndDate(quotation.getStartDate());
            }
            if(quotation.getPayCategory() == null) {
                quotation.setPayCategory(PayCategory.pc0);
            }
            // 城市
            if(quotation.getCity() != null && quotation.getCity().getId() != null) {
                quotation.setCity(cityDao.findOne(quotation.getCity().getId()));
            }
            // 户口性质
            if(quotation.getFeeCategory().equals(FeeCategory.fc1)) {
                if(quotation.getResidenceType() == null || quotation.getResidenceType().getId() == null) {
                    quotation.setResidenceType(null);
                }
            }

            String token = request.getHeader("Authorization");
            if (StringUtils.hasText(token)){
                String userNo = JWTUtil.getUserNo(token);
                Member user = memberService.findByUsername(userNo);
                if (null != user){
                    quotation.setMember(user);
                }
            }
//            quotation.setUsername(Base64.encodeBase64String(quotation.getUsername().getBytes("utf-8")));
            System.out.println("userName2:" + quotation.getUsername());
            quotationService.saveQuotaion(quotation);
            return new PublicResult<>(PublicResultConstant.SUCCESS, quotation.getId().toString());
        }
    }

    @PostMapping("/saveQuotationPayInfo")
    @ApiOperation(value="保存/更新报价单", notes="保存报价单",produces = "application/json")
    @Log(description="平台中心保存报价单接口:/m/quotation/saveQuotationPayInfo")
    public PublicResult<String> saveQuotationPayInfo(@RequestBody Quotation quotation)throws Exception{

        if (null != quotation && null != quotation.getId()) {

            Quotation quotationTemp = quotationDao.findOne(quotation.getId());

            // 开始时间
            if(StringUtils.hasText(quotation.getStartDate())) {
                quotationTemp.setStartDate(quotation.getStartDate());
            }

            // 缴费月数
            if (quotation.getMonthCount() != null) {
                quotationTemp.setMonthCount(quotation.getMonthCount());
            }

            // 结束时间
            if(StringUtils.hasText(quotation.getEndDate())) {
                quotationTemp.setEndDate(quotation.getEndDate());
            }

            // 缴费方式 pc0(0, "新参"),pc1(1, "续缴"),pc2(2, "补缴");
            if(quotation.getPayCategory() != null) {
                quotationTemp.setPayCategory(quotation.getPayCategory());
            }

            // 缴费基数设置
            if(quotationTemp.getFeeCategory().equals(FeeCategory.fc0)) {
                quotationTemp.setSocialBase(quotation.getSocialBase());
                //quotation.setProvidentBase(BigDecimal.ZERO);
            } else if(quotationTemp.getFeeCategory().equals(FeeCategory.fc1)) {
                quotationTemp.setProvidentBase(quotation.getProvidentBase());
                //quotation.setSocialBase(BigDecimal.ZERO);
            } else {
                quotationTemp.setSocialBase(quotation.getSocialBase());
                quotationTemp.setProvidentBase(quotation.getProvidentBase());
            }

            // 3同一用户重复下单校验（身份证号，缴费类型，缴费月份)
            List<Quotation> quotationList = quotationService.getExistQuotation(quotationTemp);
            for(Quotation quotationExist : quotationList) {
                // 状态判断 取消 或者 过期
                // 日期存在冲突 开始日期 <= .结束日期 && 结束日期 >= .开始日期
                if(quotationExist.getOrder() != null && !quotationExist.getOrder().isExpired()) {
                    if(!quotationExist.getOrder().getOrderStatus().equals(Order.OrderStatus.cancelled)
                            && (Integer.parseInt(quotationTemp.getStartDate().replace("-", "")) <= Integer.parseInt(quotationExist.getEndDate().replace("-", "")))
                            && (Integer.parseInt(quotationTemp.getEndDate().replace("-", "")) >= Integer.parseInt(quotationExist.getStartDate().replace("-", "")))) {
                        return new PublicResult<>(PublicResultConstant.QUOTATION_IS_EXSIT_ERROR, null);
                    }
                }
            }

            quotationService.saveQuotaion(quotationTemp);
        } else {
            new PublicResult<>(PublicResultConstant.FAILED, null);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, quotation.getId().toString());
    }

    @PostMapping("/createOrder")
    @ApiOperation(value="创建订单", notes="创建订单",produces = "application/json")
    @Log(description="创建订单:/m/quotation/createOrder")
    public PublicResult<Object> createOrder(Long id, boolean useBalance, BigDecimal couponDiscount, Long couponCodeId, @CurrentUser Member member)throws Exception{

        Order order = null;
        try{
            if(member == null || member.getId() == null) {
                return new PublicResult(PublicResultConstant.USER_NO_PERMITION, null);
            }

            if(id == null) {
                return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
            }

            // 默认取第一条付款方式
            PaymentMethod paymentMethod = paymentMethodDao.findAll().get(0);

            Quotation quotationTemp = quotationDao.findOne(id);

            if(quotationTemp.getOrder() != null) {
                return new PublicResult<>(PublicResultConstant.ORDER_IS_EXSIT_ERROR, null);
            }

            order = orderService.createOrder(quotationTemp, null, paymentMethod, useBalance, couponDiscount, couponCodeId, null, null, member);

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

        }catch (Exception e){
            LOG.error("delete quotation error. {}", e);
            return new PublicResult<>(PublicResultConstant.ORDER_IS_EXSIT_ERROR, null);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, order.getId());
    }

    @PostMapping("/update")
    @ApiOperation(value="更新报价单", notes="保存报价单",produces = "application/json")
    @Log(description="平台中心保存报价单接口:/m/quotation/update")
    public PublicResult<String> updatePayInfo(@RequestBody Quotation quotation)throws Exception{
        if (null != quotation && null != quotation.getId()) {

            Quotation quotationTemp = quotationDao.findOne(quotation.getId());

            quotationTemp.setPayCategory(PayCategory.findByValue(quotation.getPayCategory().getValue()));
            quotationTemp.setStartDate(quotation.getStartDate());
            quotationTemp.setMonthCount(quotation.getMonthCount());

            if(quotationTemp.getFeeCategory() == FeeCategory.fc0) {
                quotationTemp.setSocialBase(quotation.getSocialBase());
            } else if(quotationTemp.getFeeCategory() == FeeCategory.fc1) {
                quotationTemp.setProvidentBase(quotation.getProvidentBase());
            } else {
                quotationTemp.setSocialBase(quotation.getSocialBase());
                quotationTemp.setProvidentBase(quotation.getProvidentBase());
            }

            PayBase payBase = payBaseService.getPayBaseByCityId(quotation.getCity().getId());
            quotationService.setSocialPayAmount(quotationTemp, payBase);
            quotationService.setProvidentPayAmount(quotationTemp, payBase);

            quotationService.update(quotationTemp);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, quotation.getId().toString());
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除报价单", notes="批量删除报价单",produces = "application/json")
    @Log(description="平台中心批量删除报价单接口:/m/quotation/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete quotation param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            quotationService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete quotation error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete quotation result = " + result.toString());
        return result;
    }

    /**
     * 详情 报价信息
     */
    @GetMapping("/calculateQuotationBaseInfo")
    @ApiOperation(value="计算器基数获取", notes="计算器基数获取",produces = "application/json")
    @Log(description="计算器基数获取接口:/m/quotation/calculateQuotationBaseInfo")
    public PublicResult<JSONObject> calculateQuotationBaseInfo(Long cityId) throws Exception{
        LOG.debug("get quotation info param = " + cityId);

        JSONObject result = new JSONObject();

        if(cityId != null) {

            // 参保信息页面
            PayBase payBase = payBaseService.getPayBaseByCityId(cityId);

            // 缴费基数
            result.put("payBase", payBaseService.createEntity().convertEntity(payBase, new String[]{""}));
        }

        LOG.debug("get quotation info result = " + (result == null ? "" : result.toString()));
        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    @GetMapping("/calculateQuotation")
    @ApiOperation(value="计算报价单", notes="保存报价单",produces = "application/json")
    @Log(description="计算报价单:/m/quotation/calculateQuotation")
    public PublicResult<Object> calculateQuotation(Integer feeCategoryId, Long cityId, Long residenceTypeId, String socialBase, String providentBase)throws Exception{
        PublicResult<Object> result = null;
        JSONObject jo = new JSONObject();
        try{
            Quotation quotation = new Quotation();

            if(feeCategoryId == null || cityId == null || (feeCategoryId == 0 && residenceTypeId == null)) {
                return new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, null);
            }

            // 缴费月数
            quotation.setMonthCount(1);
            // 缴费类别
            FeeCategory feeCategory = FeeCategory.findByValue(feeCategoryId);
            quotation.setFeeCategory(feeCategory);
            if(feeCategory.equals(FeeCategory.fc0)) {
                quotation.setSocialBase(new BigDecimal(socialBase));
            } else if(feeCategory.equals(FeeCategory.fc1)) {
                quotation.setProvidentBase(new BigDecimal(providentBase));
            }

            // 缴费城市
            quotation.setCity(cityDao.findOne(cityId));
            // 户口类型
            if(residenceTypeId != null) {
                quotation.setResidenceType(residenceTypeDao.findOne(residenceTypeId));
            }

            quotationService.setCalculateQuotation(quotation, CalculatorCategory.cc0);
            if(quotation != null) {
                jo = quotationItemService.getJsonFromQuotation(quotation, false);
            }

            result = new PublicResult<>(PublicResultConstant.SUCCESS, jo);
        }catch (Exception e){
            LOG.error("get quotation list error. {}", e);
            result = new PublicResult<Object>(PublicResultConstant.FAILED, null);
        }
        LOG.debug("get quotation list result = " + result.toString());
        return result;

    }

    @GetMapping("/calculatePayInfo")
    @ApiOperation(value="计算报价单", notes="保存报价单",produces = "application/json")
    @Log(description="计算报价单:/m/quotation/calculatePayInfo")
    public PublicResult<Object> calculatePayInfo(Long id, Integer payCategoryId, Integer monthCount, String socialBase, String providentBase)throws Exception{
        PublicResult<Object> result = null;
        JSONObject jo = new JSONObject();
        try{
            if(id == null || payCategoryId == null || monthCount == null || (StringUtils.isEmpty(socialBase)  && StringUtils.isEmpty(providentBase))) {
                return new PublicResult<Object>(PublicResultConstant.PARAM_ERROR, null);
            }

            Quotation quotation = quotationDao.getOne(id);

            // 缴费月数
            quotation.setMonthCount(monthCount);
            // 缴费方式 pc0(0, "新参"),pc1(1, "续缴"),pc2(2, "补缴");
            quotation.setPayCategory(PayCategory.findByValue(payCategoryId));
            // 缴费基数
            if(quotation.getFeeCategory().equals(FeeCategory.fc0)) {
                quotation.setSocialBase(new BigDecimal(socialBase));
                //quotation.setProvidentBase(BigDecimal.ZERO);
            } else if(quotation.getFeeCategory().equals(FeeCategory.fc1)) {
                quotation.setProvidentBase(new BigDecimal(providentBase));
                //quotation.setSocialBase(BigDecimal.ZERO);
            } else {
                quotation.setSocialBase(new BigDecimal(socialBase));
                quotation.setProvidentBase(new BigDecimal(providentBase));
            }

            quotationService.setCalculateQuotation(quotation, CalculatorCategory.cc1);
            if(quotation != null) {
                jo = quotation.convertEntity(quotation, new String[]{"personalAmount", "companyAmount", "allAmount", "orderAmount"});
            }

            result = new PublicResult<>(PublicResultConstant.SUCCESS, jo);
        }catch (Exception e){
            LOG.error("get quotation list error. {}", e);
            result = new PublicResult<Object>(PublicResultConstant.FAILED, null);
        }
        LOG.debug("get quotation list result = " + result.toString());
        return result;

    }

}
