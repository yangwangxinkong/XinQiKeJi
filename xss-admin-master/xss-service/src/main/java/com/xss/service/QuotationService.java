package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.CityDao;
import com.xss.dao.QuotationDao;
import com.xss.domain.*;
import com.xss.domain.enums.CalculatorCategory;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.PayFrom;
import com.xss.domain.enums.SocialSecurityCategory;
import com.xss.util.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  Service - 报价单
 * @author hu
 * @date 2018/8/29
 */
@Service
public class QuotationService extends BaseService<Quotation,Long> {

    @Resource
    private CityDao cityDao;
    @Resource
    private QuotationDao quotationDao;
    @Autowired
    private SocialSecurityRatioSettingService socialSecurityRatioSettingService;
    @Autowired
    private ServiceFeeSettingService serviceFeeSettingService;
    @Autowired
    private ProvidentFundRatioSettingService providentFundRatioSettingService;
    @Autowired
    private PayBaseService payBaseService;
    @Autowired
    private QuotationItemService quotationItemService;
    @Autowired
    private ConfigsService configsService;
    @Resource
    public void setBaseDao(QuotationDao quotationDao) {
        super.setBaseDao(quotationDao);
    }

    /**
     * 社保 获取所需缴纳金额 = 个人缴纳 + 公司缴纳
     * @param quotation
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void setSocialPayAmount (Quotation quotation, PayBase payBase) {

        //BigDecimal res = new BigDecimal(0);
        //根据城市信息和户口性质（类型），查询出该城市内该户口下所有的社保类型ID
        List<SocialSecurityRatioSetting> socialSecurityRatioSettingList = socialSecurityRatioSettingService.getSocialSecurityRatioSetting(quotation.getCity(), quotation.getResidenceType());

        Configs configs = configsService.getConfigsByCode(quotation.getCity().getParent().getCode() + "_" + Configs.MEDICALCARE_CODE);

        if(socialSecurityRatioSettingList != null && socialSecurityRatioSettingList.size() != 0) {
            //个人缴费比例
            BigDecimal personal = BigDecimal.ZERO;
            //公司缴费比例
            BigDecimal company = BigDecimal.ZERO;
            //社保缴费基数
            BigDecimal payBaseTemp;
            //遍历获得的社保类型ID
            for(SocialSecurityRatioSetting socialSecurityRatioSetting : socialSecurityRatioSettingList) {

                payBaseTemp = quotation.getSocialBase();

                if(payBase != null) {
                    if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc0)) {
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数 >> 养老
                        if(payBaseTemp.compareTo(payBase.getSsc0BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc0BaseMin();
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc1)) {
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数 >> 失业
                        if(payBaseTemp.compareTo(payBase.getSsc1BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc1BaseMin();
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc2)) {
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数 >> 工伤
                        if(payBaseTemp.compareTo(payBase.getSsc2BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc2BaseMin();
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc3)) {
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数 >> 医疗
                        if(payBaseTemp.compareTo(payBase.getSsc3BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc3BaseMin();
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc4)) {
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数 >> 生育
                        if(payBaseTemp.compareTo(payBase.getSsc4BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc4BaseMin();
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc5)) {
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数 >> 残保
                        if(payBase.getSsc5BaseMin().compareTo(BigDecimal.ZERO) == 0) {
                            payBaseTemp = BigDecimal.ZERO;
                        } else if(payBaseTemp.compareTo(payBase.getSsc5BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc5BaseMin();
                        }
                    }else if (socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc6)){
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数 >> 大病
                        if(payBaseTemp.compareTo(payBase.getSsc6BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc6BaseMin();
                        }
                    }
                }

                // 个人 缴费（缴费对象）
                if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0) {

                    // 20181129 固定值是否，分开计算（残保金是否固定值，将用户输入的值保留两位小数）
                    if(socialSecurityRatioSetting.getFixed() != null && socialSecurityRatioSetting.getFixed()) {
                        personal = personal.add(BigDecimalUtils.setScale(socialSecurityRatioSetting.getFixedValue(), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        //getRatio()获取缴费比例，之后进行计算
                        personal = personal.add(BigDecimalUtils.setScale(payBaseTemp.multiply(socialSecurityRatioSetting.getRatio()), 2, BigDecimal.ROUND_HALF_UP));
                    }

                    //如果社保类型是医疗，就使用指定编码值
                    if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc3)) {
                        if(configs != null && StringUtils.hasText(configs.getCodeValue())) {
                            personal = personal.add(new BigDecimal(configs.getCodeValue()));
                        }
                    }
                } else if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1) {
                    // 公司 缴费
                    // 20181129 固定值是否，分开计算
                    if(socialSecurityRatioSetting.getFixed() != null && socialSecurityRatioSetting.getFixed()) {
                        company = company.add(BigDecimalUtils.setScale(socialSecurityRatioSetting.getFixedValue(), 2, BigDecimal.ROUND_HALF_UP));
                    } else {
                        company = company.add(BigDecimalUtils.setScale(payBaseTemp.multiply(socialSecurityRatioSetting.getRatio()), 2, BigDecimal.ROUND_HALF_UP));
                    }
                }
            }
            //获取缴费月数并和个人缴费比例进行计算，最后获得总共需要缴纳的费用---------个人
            personal = personal.multiply(BigDecimal.valueOf(quotation.getMonthCount()));
            //获取缴费月数并和公司缴费比例进行计算，最后获得总共需要缴纳的费用---------公司
            company = company.multiply(BigDecimal.valueOf(quotation.getMonthCount()));

            //设置个人缴费金额（所有社保类别的总额）
            quotation.setPersonSocialAmount(personal);
            //设置公司缴费金额（所有社保类别的总额）
            quotation.setCompanySocialAmount(company);
        }
    }

    /**
     * 公积金 获取所需缴纳金额 = 个人缴纳 + 公司缴纳
     * @param quotation
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void setProvidentPayAmount (Quotation quotation, PayBase payBase) {

        //BigDecimal res = new BigDecimal(0);
        List<ProvidentFundRatioSetting> providentFundRatioSettingList = providentFundRatioSettingService.getProvidentFundRatioSetting(quotation.getCity());

        if(providentFundRatioSettingList != null && providentFundRatioSettingList.size() != 0) {

            BigDecimal personal = BigDecimal.ZERO;
            BigDecimal company = BigDecimal.ZERO;
            BigDecimal payBaseTemp = quotation.getProvidentBase();

            for(ProvidentFundRatioSetting providentFundRatioSetting : providentFundRatioSettingList) {

                // 公积金缴费基数 < 公积金最低基数 取 公积金最低基数
                if(payBaseTemp.compareTo(payBase.getProvidentBaseMin()) < 0) {
                    payBaseTemp = payBase.getProvidentBaseMin();
                }

                // 个人 缴费
                if(providentFundRatioSetting.getPayFrom() == PayFrom.pf0) {
                    personal = personal.add(BigDecimalUtils.setScale(payBaseTemp.multiply(providentFundRatioSetting.getRatio()), 0, BigDecimal.ROUND_HALF_UP));
                } else if(providentFundRatioSetting.getPayFrom() == PayFrom.pf1) {
                    // 公司 缴费
                    company = company.add(BigDecimalUtils.setScale(payBaseTemp.multiply(providentFundRatioSetting.getRatio()), 0, BigDecimal.ROUND_HALF_UP));
                }
            }

            personal = personal.multiply(BigDecimal.valueOf(quotation.getMonthCount()));
            company = company.multiply(BigDecimal.valueOf(quotation.getMonthCount()));

            quotation.setPersonProvidentAmount(BigDecimalUtils.setScale(personal, 0, BigDecimal.ROUND_HALF_UP));
            quotation.setCompanyProvidentAmount(BigDecimalUtils.setScale(company, 0, BigDecimal.ROUND_HALF_UP));
        }
    }

    /**
     * 计算 服务费用
     * @param quotation
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void setFeeAmount (Quotation quotation) {

        ServiceFeeSetting serviceFeeSetting = serviceFeeSettingService.getServiceFeeSettings(quotation);

        if(serviceFeeSetting != null) {
            quotation.setMonthFee(serviceFeeSetting.getMonthFee());
            quotation.setFee(serviceFeeSetting.getFee());
            //计算服务费优惠折扣
            setDiscount(quotation);
        } else {
            quotation.setMonthFee(BigDecimal.ZERO);
            quotation.setFee(BigDecimal.ZERO);
            quotation.setVipDiscountFee(BigDecimal.ZERO);
            quotation.setFirstOrderDiscountFee(BigDecimal.ZERO);
        }
    }

    /**
     * 判断并设置服务费优惠
     * 1.如果用户购买了会员套餐，则获取系统配置项中会员服务费折扣优惠，且不再享受其他折扣优惠。
     * 2.如果未购买会员套餐，则判断用户是否为首次下单，是则享受新手套餐服务费折扣优惠。
     * @param quotation
     */
    @Transactional(rollbackFor = Exception.class)
    public void setDiscount (Quotation quotation) {
        Member member = quotation.getMember();
        if (null != member){
            //1.获取会员套餐折扣
            if (null != member.getIsVip() && member.getIsVip()){
                Configs config = configsService.getConfigsByCode(Configs.DEFAULT_VIP_DISCOUNT_CODE);
                if (null != config){
//                    BigDecimal discount = BigDecimal.ONE;
//                    try {
//                        discount = new BigDecimal(config.getCodeValue()).setScale(2);
//                    }catch (Exception e){
//
//                    }
//                    BigDecimal discountFee = quotation.getFee().multiply(discount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    BigDecimal discountFee = quotation.getFee().multiply(member.getVipDiscount()).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    quotation.setVipDiscountFee(quotation.getFee().subtract(discountFee));
                    quotation.setFirstOrderDiscountFee(BigDecimal.ZERO);
                    quotation.setFee(discountFee);
                }
            }else if(null != member.getIsNew() && member.getIsNew()){
                //2.获取新手套餐折扣
                Configs config = configsService.getConfigsByCode(Configs.DEFAULT_FIRST_ORDER_DISCOUNT_CODE);
                if (null != config){
                    BigDecimal discount = BigDecimal.ONE;
                    try {
                        discount = new BigDecimal(config.getCodeValue()).setScale(2);
                    }catch (Exception e){

                    }
                    BigDecimal discountFee = quotation.getFee().multiply(discount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                    quotation.setFirstOrderDiscountFee(quotation.getFee().subtract(discountFee));
                    quotation.setVipDiscountFee(BigDecimal.ZERO);
                    quotation.setFee(discountFee);
                }
            }else{
                quotation.setVipDiscountFee(BigDecimal.ZERO);
                quotation.setFirstOrderDiscountFee(BigDecimal.ZERO);
            }
        }else{
            quotation.setVipDiscountFee(BigDecimal.ZERO);
            quotation.setFirstOrderDiscountFee(BigDecimal.ZERO);
        }

    }

    /**
     * 计算报价单
     * @param quotation  新的报价单信息（要保存的报价单）
     * @param calculatorCategory 是计算方式
     */
    @Transactional(rollbackFor = Exception.class)
    public Quotation setCalculateQuotation (Quotation quotation, CalculatorCategory calculatorCategory) {

        /*根据城市ID查询出城市信息其中包括（
            社保比例配置集合，公积金比例配置集合，服务费集合，
            报价单集合，订单集合，户口类型集合，缴费基数集合
         */
        City city = cityDao.findOne(quotation.getCity().getId());
        quotation.setCity(city);
        System.out.println("city:" + city.getFullName());  //获取城市的全称
        //根据城市ID查询社保公积金基数（包括：社保缴费基数 大病 最小值等）
        PayBase payBase = payBaseService.getPayBaseByCityId(quotation.getCity().getId());

        // 缴费类别 fc0(0, "社保"),fc1(1, "公积金"),fc2(2, "社保+公积金");
        // 20181127 城市重新选择后，社保，公积金基数会发生变化
        //if(quotation.getSocialBase() == null) {
            //社保缴费基数
            quotation.setSocialBase(payBase.getSocialBaseMin());
        //}
        // 20181127 城市重新选择后，社保，公积金基数会发生变化
        //if(quotation.getProvidentBase() == null) {
            //公积金缴费基数
            quotation.setProvidentBase(payBase.getProvidentBaseMin());
        //}

        if(quotation.getFeeCategory() == FeeCategory.fc0) {
            // 计算社保金额
            setSocialPayAmount(quotation, payBase);

            // 公积金金额设为0
            quotation.setPersonProvidentAmount(BigDecimal.ZERO);
            quotation.setCompanyProvidentAmount(BigDecimal.ZERO);

        } else if(quotation.getFeeCategory() == FeeCategory.fc1) {
            // 计算公积金金额
            setProvidentPayAmount(quotation, payBase);

            // 社保金额金额设为0
            quotation.setPersonSocialAmount(BigDecimal.ZERO);
            quotation.setCompanySocialAmount(BigDecimal.ZERO);
        } else {
            // 计算社保金额
            setSocialPayAmount(quotation, payBase);
            // 计算公积金金额
            setProvidentPayAmount(quotation, payBase);
        }

        // 计算器的话，不需要计算服务费
        if(calculatorCategory.equals(CalculatorCategory.cc0)) {
            // 设置服务费 只是计算的话，不需要计算服务费
            quotation.setMonthFee(BigDecimal.ZERO); //月服务费
            quotation.setFee(BigDecimal.ZERO);      //服务费
        } else {
            // 设置服务费
            setFeeAmount(quotation);
        }

        // 设置总费用（公司+个人+服务费）
        quotation.setAmount(quotation.getAllAmount().add(quotation.getFee()));

        // 社保信息输入更新页面  或者 计算社保页面  要计算明细cc0(0, "计算器"),cc1(1, "计算报价单"),cc2(2, "计算报价单+明细")
        if(calculatorCategory.equals(CalculatorCategory.cc2) || calculatorCategory.equals(CalculatorCategory.cc0)) {

            List<QuotationItem> quotationItemList = quotation.getQuotationItems();
            if(quotationItemList == null) {
                quotationItemList = new ArrayList<>();
            } else {
                quotationItemList.clear();
            }

            // 制造报价明细
            for(QuotationItem quotationItem : quotationItemService.createQuotationItem(quotation)) {
                quotationItemList.add(quotationItem);
            }

            // 参保信息保存页面，保存报价单详情信息
            quotation.setQuotationItems(quotationItemList);
        }

        return quotation;
    }

    /**
     * 保存报价单
     * @param quotation 新的报价单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveQuotaion (Quotation quotation) {
        System.out.println("userName3:" + quotation.getUsername());
        // 正常的计算 保存
        setCalculateQuotation(quotation, CalculatorCategory.cc2);
        System.out.println("userName4:" + quotation.getUsername());
        // 更新报价单数据库
        if(quotation.getId() != null) {
            update(quotation);
        } else {
            save(quotation);
        }
    }

    @Transactional(readOnly = true)
    public List<Quotation> getExistQuotation(Quotation quotation) {

            String jpql = "select quotation from Quotation quotation where quotation.identification = :identification" +
                    " and (quotation.feeCategory = :feeCategory or quotation.feeCategory = :feeCategoryfc2) ";
                    //" and (quotation.startDate <= :endDate or quotation.endDate >= :startDate) ";

        TypedQuery<Quotation> query = entityManager.createQuery(jpql, Quotation.class).setFlushMode(FlushModeType.COMMIT)
                .setParameter("identification", quotation.getIdentification())
                .setParameter("feeCategory", quotation.getFeeCategory()).setParameter("feeCategoryfc2", FeeCategory.fc2);
                //.setParameter("endDate", quotation.getEndDate()).setParameter("startDate", quotation.getStartDate());

        return query.getResultList();
    }

    /**
     * 组合报价单确认页面的JSON数据
     *
     * @param quotation
     * @return
     */
    public JSONObject setQuotationConfirmJson(Quotation quotation) {

        JSONObject result = quotation.convertEntity(quotation, new String[]{"city", "cityid", "residenceType", "allAmount", "orderAmount"});

        // 缴费方式
        JSONArray ja = new JSONArray();

        JSONObject jo = new JSONObject();
        jo.put("label", "参保人姓名");
        try {
//            String nickName = new String(org.apache.commons.codec.binary.Base64.decodeBase64(quotation.getUsername()), "utf-8");
            jo.put("value", quotation.getUsername());
        }catch (Exception e){
            jo.put("value", "");
        }

        ja.add(jo);

        jo = new JSONObject();
        jo.put("label", "身份证号");
        jo.put("value", quotation.getIdentification());
        ja.add(jo);

        jo = new JSONObject();
        jo.put("label", "参保城市");
        jo.put("value", quotation.getCity().getFullName());
        ja.add(jo);

        jo = new JSONObject();
        jo.put("label", "参保项目");
        jo.put("value", quotation.getFeeCategory().getDesc() + " " + quotation.getPayCategory().getDesc());
        ja.add(jo);

        if(quotation.getFeeCategory().equals(FeeCategory.fc0) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
            jo = new JSONObject();
            jo.put("label", "社保参保基数");
            jo.put("value", quotation.getSocialBase());
            ja.add(jo);
        }

        if(quotation.getFeeCategory().equals(FeeCategory.fc1) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
            jo = new JSONObject();
            jo.put("label", "公积金参保基数");
            jo.put("value", quotation.getProvidentBase());
            ja.add(jo);
        }

        jo = new JSONObject();
        jo.put("label", "缴纳月份");
        jo.put("value", quotation.getStartDate() + "至" + quotation.getEndDate());
        ja.add(jo);

        result.put("infoList", ja);

        ja = new JSONArray();
        jo = new JSONObject();
        jo.put("label", "缴纳费用");
        jo.put("value", quotation.getAllAmount() + "元");
        ja.add(jo);

        jo = new JSONObject();
        jo.put("label", "服务费");
        jo.put("value", quotation.getMonthFee() + "元*" + quotation.getMonthCount() + "月");
        ja.add(jo);

        if (null != quotation.getVipDiscountFee() && quotation.getVipDiscountFee().compareTo(BigDecimal.ZERO) > 0) {
            jo = new JSONObject();
            jo.put("label", "会员优惠");
            jo.put("value", quotation.getVipDiscountFee() + "元");
            ja.add(jo);
        }

        if (null != quotation.getFirstOrderDiscountFee() && quotation.getFirstOrderDiscountFee().compareTo(BigDecimal.ZERO) > 0) {
            jo = new JSONObject();
            jo.put("label", "首单优惠");
            jo.put("value", quotation.getFirstOrderDiscountFee() + "元");
            ja.add(jo);
        }

        result.put("payList", ja);

        return result;
    }
}
