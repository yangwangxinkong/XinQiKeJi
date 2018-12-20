package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.QuotationItemDao;
import com.xss.domain.*;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.PayFrom;
import com.xss.domain.enums.SocialSecurityCategory;
import com.xss.util.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *  Service - 报价单明细
 * @author hu
 * @date 2018/8/29
 */
@Service
public class QuotationItemService extends BaseService<QuotationItem, Long> {
    @Resource
    private QuotationItemDao quotationItemDao;
    @Autowired
    private SocialSecurityRatioSettingService socialSecurityRatioSettingService;
    @Autowired
    private ProvidentFundRatioSettingService providentFundRatioSettingService;
    @Autowired
    private PayBaseService payBaseService;
    @Autowired
    private ConfigsService configsService;

    @Resource
    public void setBaseDao(QuotationItemDao quotationItemDao) {
        super.setBaseDao(quotationItemDao);
    }

    /**
     * 创建报价单明细
     * @param quotation
     * @return
     */
    @Transactional
    public List<QuotationItem> createQuotationItem(Quotation quotation) {

        // 基数获取（根据城市ID获取当前城市的所有基数）
        PayBase payBase = payBaseService.getPayBaseByCityId(quotation.getCity().getId());
        List<QuotationItem> quotationItemList = new ArrayList<>();
        QuotationItem quotationItem = null;

        // 社保
        if(quotation.getFeeCategory().equals(FeeCategory.fc0)) {
            quotationItem = createSocialQuotationItem(payBase, quotation);
        } else if(quotation.getFeeCategory().equals(FeeCategory.fc1)) {
            // 公积金
            quotationItem = createProvidentQuotationItem(payBase, quotation);
        } else if(quotation.getFeeCategory().equals(FeeCategory.fc2)) {
            quotationItem = createSocialQuotationItem(payBase, quotation);
            QuotationItem quotationItemTemp = createProvidentQuotationItem(payBase, quotation);

            // 合并数据
            quotationItem.setProvidentFundName(quotationItemTemp.getProvidentFundName());
            quotationItem.setProvidentFundBase(quotationItemTemp.getProvidentFundBase());
            quotationItem.setProvidentFundPerson(quotationItemTemp.getProvidentFundPerson());
            quotationItem.setProvidentFundCompany(quotationItemTemp.getProvidentFundCompany());
        }

        quotationItem.setQuotation(quotation);

        //quotationItemDao.save(quotationItem);
        quotationItemList.add(quotationItem);

        return quotationItemList;
    }

    /**
     * 创建报价单明细  社保
     * @param quotation
     * @return
     */
    @Transactional
    private QuotationItem createSocialQuotationItem(PayBase payBase, Quotation quotation) {

        // 缴费比例获取（根据城市和户口性质查询出社保比例配置，能够查询出社保类型集合）
        List<SocialSecurityRatioSetting> socialSecurityRatioSettingList = socialSecurityRatioSettingService.getSocialSecurityRatioSetting(quotation.getCity(), quotation.getResidenceType());
        Configs configs = configsService.getConfigsByCode(quotation.getCity().getParent().getCode() + "_" + Configs.MEDICALCARE_CODE);

        List<QuotationItem> quotationItemList = new ArrayList<>();
        QuotationItem quotationItem = new QuotationItem();

        if(socialSecurityRatioSettingList != null && socialSecurityRatioSettingList.size() != 0) {

            BigDecimal payBaseTemp;

            for(SocialSecurityRatioSetting socialSecurityRatioSetting : socialSecurityRatioSettingList) {

                payBaseTemp = quotation.getSocialBase();

                if(payBase != null) {
                    // 养老保险
                    if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc0)) {
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数
                        if(payBaseTemp.compareTo(payBase.getSsc0BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc0BaseMin();
                        }
                        quotationItem.setEndowmentBase(payBaseTemp);//养老保险基数
                        quotationItem.setEndowment(SocialSecurityCategory.ssc0);//社保类型ID

                        // 个人 缴费
                        if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0) {
                            quotationItem.setEndowmentPerson(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        } else if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1) {
                            // 公司 缴费
                            quotationItem.setEndowmentCompany(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc1)) {
                        // 失业
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数
                        if(payBaseTemp.compareTo(payBase.getSsc1BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc1BaseMin();
                        }
                        quotationItem.setUnemploymentBase(payBaseTemp);
                        quotationItem.setUnemployment(SocialSecurityCategory.ssc1);

                        // 个人 缴费
                        if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0) {
                            quotationItem.setUnemploymentPerson(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        } else if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1) {
                            // 公司 缴费
                            quotationItem.setUnemploymentCompany(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc2)) {
                        // 工伤
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数
                        if(payBaseTemp.compareTo(payBase.getSsc2BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc2BaseMin();
                        }
                        quotationItem.setInjuryBase(payBaseTemp);
                        quotationItem.setInjury(SocialSecurityCategory.ssc2);

                        // 个人 缴费
                        if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0) {
                            quotationItem.setInjuryPerson(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        } else if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1) {
                            // 公司 缴费
                            quotationItem.setInjuryCompany(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc3)) {
                        // 医疗
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数
                        if(payBaseTemp.compareTo(payBase.getSsc3BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc3BaseMin();
                        }
                        quotationItem.setMedicareBase(payBaseTemp);
                        quotationItem.setMedicare(SocialSecurityCategory.ssc3);

                        // 个人 缴费
                        if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0) {
                            quotationItem.setMedicarePerson(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                            if(configs != null && StringUtils.hasText(configs.getCodeValue())) {
                                quotationItem.setMedicarePerson(quotationItem.getMedicarePerson().add(new BigDecimal(configs.getCodeValue())));
                            }
                        } else if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1) {
                            // 公司 缴费
                            quotationItem.setMedicareCompany(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc4)) {
                        // 生育
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数
                        if(payBaseTemp.compareTo(payBase.getSsc4BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc4BaseMin();
                        }
                        quotationItem.setMaternityBase(payBaseTemp);
                        quotationItem.setMaternity(SocialSecurityCategory.ssc4);

                        // 个人 缴费
                        if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0) {
                            quotationItem.setMaternityPerson(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        } else if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1) {
                            // 公司 缴费
                            quotationItem.setMaternityCompany(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        }
                    } else if(socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc5)) {
                        // 残保
                        // 社保缴费基数 < 社保分类明细基数 取 分类明细基数
                        if(payBase.getSsc5BaseMin().compareTo(BigDecimal.ZERO) == 0) {
                            payBaseTemp = BigDecimal.ZERO;
                        } else if(payBaseTemp.compareTo(payBase.getSsc5BaseMin()) < 0) {
                            payBaseTemp = payBase.getSsc5BaseMin();
                        }
                        quotationItem.setDisabilityBase(payBaseTemp);
                        quotationItem.setDisability(SocialSecurityCategory.ssc5);

                        // 个人 缴费
                        if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0) {
                            quotationItem.setDisabilityPerson(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        } else if(socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1) {
                            // 公司 缴费
                            quotationItem.setDisabilityCompany(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting, payBaseTemp), 2, BigDecimal.ROUND_HALF_UP));
                        }
                    } else if (socialSecurityRatioSetting.getSocialSecurityCategory().equals(SocialSecurityCategory.ssc6)) {
                        //大病
                        // 大病缴费基数 < 社保分类明细基数 取 分类明细基数
                        if (payBase.getSsc6BaseMin().compareTo(BigDecimal.ZERO) == 0) {
                            payBaseTemp = BigDecimal.ZERO;
                        } else if (payBaseTemp.compareTo(payBase.getSsc6BaseMin()) < 0){
                            payBaseTemp = payBase.getSsc6BaseMin();
                        }
                        quotationItem.setSeriousBase(payBaseTemp);//设置大病的基数
                        quotationItem.setSerious(SocialSecurityCategory.ssc6);

                        //个人 缴费
                        if (socialSecurityRatioSetting.getPayFrom() == PayFrom.pf0){
                            quotationItem.setSeriousPerson(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting,payBaseTemp),2,BigDecimal.ROUND_HALF_UP));
                        } else if (socialSecurityRatioSetting.getPayFrom() == PayFrom.pf1){
                            //公司 缴费
                            quotationItem.setSeriousCompany(BigDecimalUtils.setScale(getAmount(socialSecurityRatioSetting,payBaseTemp),2,BigDecimal.ROUND_HALF_UP));
                        }
                    }
                }
            }
        }

        return quotationItem;
    }

    private BigDecimal getAmount(SocialSecurityRatioSetting socialSecurityRatioSetting, BigDecimal payBaseTemp) {
        if(socialSecurityRatioSetting.getFixed() != null && socialSecurityRatioSetting.getFixed()) {
            return socialSecurityRatioSetting.getFixedValue();
        } else {
            return payBaseTemp.multiply(socialSecurityRatioSetting.getRatio());
        }
    }

    /**
     * 创建报价单明细  公积金
     * @param quotation
     * @return
     */
    private QuotationItem createProvidentQuotationItem(PayBase payBase, Quotation quotation) {

        // 缴费比例获取
        List<ProvidentFundRatioSetting> providentFundRatioSettingList = providentFundRatioSettingService.getProvidentFundRatioSetting(quotation.getCity());

        QuotationItem quotationItem = new QuotationItem();

        if(providentFundRatioSettingList != null && providentFundRatioSettingList.size() != 0) {

            BigDecimal payBaseTemp;

            for(ProvidentFundRatioSetting providentFundRatioSetting : providentFundRatioSettingList) {

                payBaseTemp = quotation.getProvidentBase();

                // 公积金缴费基数 < 公积金最低基数 取 公积金最低基数
                if(payBaseTemp.compareTo(payBase.getProvidentBaseMin()) < 0) {
                    payBaseTemp = payBase.getProvidentBaseMin();
                }

                quotationItem.setProvidentFundBase(payBaseTemp);
                quotationItem.setProvidentFundName(FeeCategory.fc1.getDesc());

                // 个人 缴费
                if(providentFundRatioSetting.getPayFrom() == PayFrom.pf0) {
                    quotationItem.setProvidentFundPerson(BigDecimalUtils.setScale(payBaseTemp.multiply(providentFundRatioSetting.getRatio()), 0, BigDecimal.ROUND_HALF_UP));
                } else if(providentFundRatioSetting.getPayFrom() == PayFrom.pf1) {
                    // 公司 缴费
                    quotationItem.setProvidentFundCompany(BigDecimalUtils.setScale(payBaseTemp.multiply(providentFundRatioSetting.getRatio()), 0, BigDecimal.ROUND_HALF_UP));
                }
            }
        }

        return quotationItem;
    }

    /**
     * 组合订单确认页面的JSON数据
     *
     * @param quotation
     * @param hasFee
     * @return
     */
    public JSONObject getJsonFromQuotation(Quotation quotation, boolean hasFee) {

        JSONObject joRes = quotation.convertEntity(quotation, new String[]{"feeCategory", "residenceType", "personalAmount", "companyAmount", "allAmount", "orderAmount"});

        // 缴费方式
        JSONArray ja = new JSONArray();
        JSONObject jo;

        jo = new JSONObject();
        jo.put("label", "缴费项目");
        jo.put("value", quotation.getFeeCategory().getDesc());
        ja.add(jo);

        jo = new JSONObject();
        jo.put("label", "缴费城市");
        jo.put("value", quotation.getCity().getFullName());
        ja.add(jo);

        if(quotation.getFeeCategory().equals(FeeCategory.fc0) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
            jo = new JSONObject();
            jo.put("label", "户口性质");
            jo.put("value", quotation.getResidenceType().getName());
            ja.add(jo);
        }

        joRes.put("infoList", ja);

        ja = new JSONArray();
        JSONObject joitem;

        List<QuotationItem> quotationItemList = quotation.getQuotationItems();
        int i = 0;
        for(QuotationItem quotationItem : quotationItemList) {

            if(quotation.getFeeCategory().equals(FeeCategory.fc0) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                joitem.put("project", quotationItem.getEndowment().getDesc()); //社保类型 养老
                joitem.put("cardinalNum", quotationItem.getEndowmentBase());
                joitem.put("personalNum", quotationItem.getEndowmentPerson());
                joitem.put("companyNum", quotationItem.getEndowmentCompany());
                ja.add(joitem);

                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                joitem.put("project", quotationItem.getUnemployment().getDesc());//社保类别 失业
                joitem.put("cardinalNum", quotationItem.getUnemploymentBase());
                joitem.put("personalNum", quotationItem.getUnemploymentPerson());
                joitem.put("companyNum", quotationItem.getUnemploymentCompany());
                ja.add(joitem);

                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                joitem.put("project", quotationItem.getInjury().getDesc());//社保类别 工商
                joitem.put("cardinalNum", quotationItem.getInjuryBase());
                joitem.put("personalNum", quotationItem.getInjuryPerson());
                joitem.put("companyNum", quotationItem.getInjuryCompany());
                ja.add(joitem);

                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                joitem.put("project", quotationItem.getMedicare().getDesc());
                joitem.put("cardinalNum", quotationItem.getMedicareBase());
                joitem.put("personalNum", quotationItem.getMedicarePerson());
                joitem.put("companyNum", quotationItem.getMedicareCompany());
                ja.add(joitem);

                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                joitem.put("project", quotationItem.getMaternity().getDesc());
                joitem.put("cardinalNum", quotationItem.getMaternityBase());
                joitem.put("personalNum", quotationItem.getMaternityPerson());
                joitem.put("companyNum", quotationItem.getMaternityCompany());
                ja.add(joitem);

                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                joitem.put("project", quotationItem.getDisability().getDesc());
                joitem.put("cardinalNum", quotationItem.getDisabilityBase());
                joitem.put("personalNum", quotationItem.getDisabilityPerson());
                joitem.put("companyNum", quotationItem.getDisabilityCompany());
                ja.add(joitem);

                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                SocialSecurityCategory serious = quotationItem.getSerious();
                joitem.put("project", serious.getDesc());
                joitem.put("cardinalNum", quotationItem.getSeriousBase());
                joitem.put("personalNum", quotationItem.getSeriousPerson());
                joitem.put("companyNum", quotationItem.getSeriousCompany());
                ja.add(joitem);
            }

            if(quotation.getFeeCategory().equals(FeeCategory.fc1) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
                i ++;
                joitem = new JSONObject();
                joitem.put("id", i);
                joitem.put("project", quotationItem.getProvidentFundName());
                joitem.put("cardinalNum", quotationItem.getProvidentFundBase());
                joitem.put("personalNum", quotationItem.getProvidentFundPerson());
                joitem.put("companyNum", quotationItem.getProvidentFundCompany());
                ja.add(joitem);
            }

            i ++;
            joitem = new JSONObject();
            joitem.put("id", i);
            joitem.put("project", "总计");
            joitem.put("cardinalNum", "----");
            joitem.put("personalNum", quotation.getPersonalAmount());
            joitem.put("companyNum", quotation.getCompanyAmount());
            ja.add(joitem);

        }

        joRes.put("quotationItem", ja);

        ja = new JSONArray();
        joitem = new JSONObject();
        joitem.put("label", "个人缴纳(元)");
        joitem.put("value", quotation.getPersonalAmount());
        ja.add(joitem);

        joitem = new JSONObject();
        joitem.put("label", "公司缴纳(元)");
        joitem.put("value", quotation.getCompanyAmount());
        ja.add(joitem);

        if(hasFee) {
            joitem = new JSONObject();
            joitem.put("label", "服务费用(元)");
            joitem.put("value", quotation.getFee());
            ja.add(joitem);
        }

        joRes.put("amountList", ja);

        return joRes;
    }

}
