package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.PayCategory;
import com.xss.util.CurrencyMethod;
import com.xss.util.JsonUtil;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import springfox.documentation.spring.web.json.Json;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Entity - 报价单基本表
 *
 * @author hu
 * @version 1.0
 */

@Entity
@Table(name = "xx_quotation")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_quotation_sequence")
public class Quotation extends BaseEntity {

    public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "identification", "mobile", "socialBase",
            "providentBase", "startDate", "endDate", "amount", "monthFee", "fee", "personSocialAmount", "companySocialAmount",
            "personProvidentAmount", "companyProvidentAmount", "monthCount"};

    /**
     * 会员
     */
    private  Member member;

    /** 用户名 */
    private String username;

    /**
     * 身份证
     */
    private String identification;

    /**
     * 手机号码
     */
    private String mobile;

    /**
     * 城市
     */
    private City city;

    /** 户口性质 */
    private ResidenceType residenceType;

    /** 缴费类别 */
    private FeeCategory feeCategory;

    /**
     * 缴费方式
     */
    private PayCategory payCategory;

    /** 缴费月数 */
    private Integer monthCount;

    /**
     * 社保缴费基数
     */
    private BigDecimal socialBase;

    /**
     * 公积金缴费基数
     */
    private BigDecimal providentBase;

    /** 月服务费 */
    private BigDecimal monthFee;

    /** 服务费 */
    private BigDecimal fee;

    /** 会员套餐优惠金额 */
    private BigDecimal vipDiscountFee;

    /** 新手套餐优惠金额 */
    private BigDecimal firstOrderDiscountFee;

    /** 社保 个人缴费金额  */
    private BigDecimal personSocialAmount;

    /** 社保 公司缴费金额  */
    private BigDecimal companySocialAmount;

    /** 公积金 个人缴费金额  */
    private BigDecimal personProvidentAmount;

    /** 公积金 公司缴费金额 */
    private BigDecimal companyProvidentAmount;

    /** 缴费开始年月 */
    private String startDate;

    /** 缴费结束年月 */
    private String endDate;

    /**
     * 订单
     */
    private Order order;

    /**
     * 报价单明细
     */
    private List<QuotationItem> quotationItems;

    /**
     * 报价单总金额
     */
    private BigDecimal amount;

    /**
     * 获取
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public Member getMember() {
        return member;
    }

    /**
     * 设置 会员
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;
    }

    /**
     * 获取用户名
     *
     * @return 用户名
     */
    @NotEmpty(groups = Save.class)
    @Column(nullable = false, length = 100)
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户名
     *
     * @param username
     *            用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取身份证
     *
     * @return 身份证
     */
    @NotNull
    @Column(nullable = false, length = 18)
    public String getIdentification() {
        return identification;
    }

    /**
     * 设置身份证
     * @param identification
     */
    public void setIdentification(String identification) {
        this.identification = identification;
    }

    /**
     * 获取手机号码
     *
     * @return 手机号码
     */
    @NotNull
    @Column(nullable = false, length = 20)
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置手机号码
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取 城市
     * @return
     */
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    public City getCity() {
        return city;
    }

    /**
     * 设置 城市
     * @param city
     */
    public void setCity(City city) {
        this.city = city;
    }

    /**
     * 获取 户口性质
     * @return
     */
    @ManyToOne(fetch = FetchType.LAZY)
    public ResidenceType getResidenceType() {
        return residenceType;
    }

    /**
     * 设置 户口性质
     * @param residenceType
     */
    public void setResidenceType(ResidenceType residenceType) {
        this.residenceType = residenceType;
    }

    /**
     * 获取 缴费类别
     * @return
     */
    @NotNull
    public FeeCategory getFeeCategory() {
        return feeCategory;
    }

    /**
     * 设置 缴费类别
     * @param feeCategory
     */
    public void setFeeCategory(FeeCategory feeCategory) {
        this.feeCategory = feeCategory;
    }

    /**
     * 获取 缴费方式
     * @return
     */
    @NotNull
    public PayCategory getPayCategory() {
        return payCategory;
    }

    /**
     * 设置 缴费方式
     * @param payCategory
     */
    public void setPayCategory(PayCategory payCategory) {
        this.payCategory = payCategory;
    }

    /**
     * 获取 缴费月数
     * @return
     */
    @NotNull
    @Min(1)
    @Column(nullable = false)
    public Integer getMonthCount() {
        return monthCount;
    }

    /**
     * 设置 缴费月数
     * @param monthCount
     */
    public void setMonthCount(Integer monthCount) {
        this.monthCount = monthCount;
    }

    /**
     * 获取 社保缴费基数
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSocialBase() {
        return socialBase;
    }

    /**
     * 设置 社保缴费基数
     * @param socialBase
     */
    public void setSocialBase(BigDecimal socialBase) {
        this.socialBase = socialBase;
    }

    /**
     * 获取 公积金缴费基数
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getProvidentBase() {
        return providentBase;
    }

    /**
     * 设置 公积金缴费基数
     * @param providentBase
     */
    public void setProvidentBase(BigDecimal providentBase) {
        this.providentBase = providentBase;
    }

    /**
     * 获取 月服务费
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getMonthFee() {
        return monthFee;
    }

    /**
     * 设置 服务费
     * @param monthFee
     */
    public void setMonthFee(BigDecimal monthFee) {
        this.monthFee = monthFee;
    }

    /**
     * 获取 服务费
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * 设置 服务费
     * @param fee
     */
    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Column( precision = 21, scale = 2)
    public BigDecimal getVipDiscountFee() {
        return vipDiscountFee;
    }

    public void setVipDiscountFee(BigDecimal vipDiscountFee) {
        this.vipDiscountFee = vipDiscountFee;
    }

    @Column( precision = 21, scale = 2)
    public BigDecimal getFirstOrderDiscountFee() {
        return firstOrderDiscountFee;
    }

    public void setFirstOrderDiscountFee(BigDecimal firstOrderDiscountFee) {
        this.firstOrderDiscountFee = firstOrderDiscountFee;
    }

    /**
     * 获取 社保 个人缴费金额
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getPersonSocialAmount() {
        return personSocialAmount;
    }

    /**
     * 设置 社保 个人缴费金额
     * @param personSocialAmount
     */
    public void setPersonSocialAmount(BigDecimal personSocialAmount) {
        this.personSocialAmount = personSocialAmount;
    }

    /**
     * 获取 社保 公司缴费金额
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getCompanySocialAmount() {
        return companySocialAmount;
    }

    /**
     * 设置 社保 公司缴费金额
     * @param companySocialAmount
     */
    public void setCompanySocialAmount(BigDecimal companySocialAmount) {
        this.companySocialAmount = companySocialAmount;
    }

    /**
     * 获取 公积金 个人缴费金额
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getPersonProvidentAmount() {
        return personProvidentAmount;
    }

    /**
     * 设置 公积金 个人缴费金额
     * @param personProvidentAmount
     */
    public void setPersonProvidentAmount(BigDecimal personProvidentAmount) {
        this.personProvidentAmount = personProvidentAmount;
    }

    /**
     * 获取 公积金 公司缴费金额
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getCompanyProvidentAmount() {
        return companyProvidentAmount;
    }

    /**
     * 设置 公积金 公司缴费金额
     * @param companyProvidentAmount
     */
    public void setCompanyProvidentAmount(BigDecimal companyProvidentAmount) {
        this.companyProvidentAmount = companyProvidentAmount;
    }

    /**
     * 获取 缴费开始年月
     * @return
     */
    @NotBlank
    @Column(nullable = false, length = 8)
    public String getStartDate() {
        return startDate;
    }

    /**
     * 设置 缴费开始年月
     * @param startDate
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取 缴费结束年月
     * @return
     */
    @NotBlank
    @Column(nullable = false, length = 8)
    public String getEndDate() {
        return endDate;
    }

    /**
     * 设置 缴费结束年月
     * @param endDate
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取 缴费订单
     * @return
     */
    @OneToOne(mappedBy = "quotation", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    public Order getOrder() {
        return order;
    }

    /**
     * 设置 缴费订单
     * @param order
     */
    public void setOrder(Order order) {
        this.order = order;
    }

    /**
     * 获取 报价单明细
     * @return
     */
    @OneToMany(mappedBy = "quotation", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<QuotationItem> getQuotationItems() {
        return quotationItems;
    }

    /**
     * 设置 报价单明细
     * @param quotationItems
     */
    public void setQuotationItems(List<QuotationItem> quotationItems) {
        this.quotationItems = quotationItems;
    }

    /**
     * 获取 报价单总金额
     * @return
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * 设置 报价单总金额
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * 获取个人缴费
     *
     * @return 个人缴费
     */
    @Transient
    public BigDecimal getPersonalAmount() {
        BigDecimal amountTemp = BigDecimal.ZERO;
        // 社保
        if(getFeeCategory().equals(FeeCategory.fc0) || getFeeCategory().equals(FeeCategory.fc2)) {
            if (getPersonSocialAmount() != null) {
                amountTemp = amountTemp.add(getPersonSocialAmount());
            }
        }

        if(getFeeCategory().equals(FeeCategory.fc1) || getFeeCategory().equals(FeeCategory.fc2)) {
            if (getPersonProvidentAmount() != null) {
                amountTemp = amountTemp.add(getPersonProvidentAmount());
            }
        }

        return amountTemp;
    }

    /**
     * 获取公司缴费
     *
     * @return 公司缴费
     */
    @Transient
    public BigDecimal getCompanyAmount() {

        BigDecimal amountTemp = BigDecimal.ZERO;

        // 社保
        if(getFeeCategory().equals(FeeCategory.fc0) || getFeeCategory().equals(FeeCategory.fc2)) {
            if (getCompanySocialAmount() != null) {
                amountTemp = amountTemp.add(getCompanySocialAmount());
            }
        }

        if(getFeeCategory().equals(FeeCategory.fc1) || getFeeCategory().equals(FeeCategory.fc2)) {
            if (getCompanyProvidentAmount() != null) {
                amountTemp = amountTemp.add(getCompanyProvidentAmount());
            }
        }

        return amountTemp;
    }

    /**
     * 获取公司+个人缴费
     *
     * @return 公司+个人缴费
     */
    @Transient
    public BigDecimal getAllAmount() {
        BigDecimal amountTemp = BigDecimal.ZERO;
        if (getPersonalAmount() != null) {
            amountTemp = amountTemp.add(getPersonalAmount());
        }
        if (getCompanyAmount() != null) {
            amountTemp = amountTemp.add(getCompanyAmount());
        }
        return amountTemp;
    }

    /**
     * 获取公司+个人缴费+服务费
     *
     * @return 公司+个人缴费+服务费
     */
    @Transient
    public BigDecimal getOrderAmount() {
        BigDecimal amountTemp = BigDecimal.ZERO;
        if (getAllAmount() != null) {
            amountTemp = amountTemp.add(getAllAmount());
        }
        if (getFee() != null) {
            amountTemp = amountTemp.add(getFee());
        }
        return amountTemp;
    }


    @Override
    public JSONObject convertEntity(Object entity, String[] params){
        Quotation quotation = (Quotation)entity;
        JSONObject jo = super.convertEntity(quotation, DEFAULT_JSON_PARAMS);
        if(StringUtils.isNotBlank(quotation.getUsername())){
            try {
//                String nickName = new String(Base64.decodeBase64(quotation.getUsername()), "utf-8");
                jo.put("username", quotation.getUsername());
            }catch (Exception e){
                jo.put("username", "");
            }

        }
        jo.put("vipDiscountFee", CurrencyMethod.currency(quotation.getVipDiscountFee()));
        jo.put("firstOrderDiscountFee", CurrencyMethod.currency(quotation.getFirstOrderDiscountFee()));
        //用户
        if(ArrayUtils.contains(params, "city")){
            JSONObject jocity = JsonUtil.toJSONObject(quotation.getCity(), new String[]{"id", "code", "name", "fullName"});
            if(quotation.getCity().getParent() != null) {
                jocity.put("parent", quotation.getCity().getParent().getId());
            }
            jo.put("city", jocity);
        }
        if(ArrayUtils.contains(params, "cityid")){
            jo.put("cityid", quotation.getCity().getId().toString());
        }
        if(ArrayUtils.contains(params, "residenceType")){
            if(quotation.getResidenceType() != null) {
                jo.put("residenceType", JsonUtil.toJSONObject(quotation.getResidenceType(), new String[]{"id", "name"}));
            }
        }
        if(ArrayUtils.contains(params, "feeCategory")){
            if(quotation.getFeeCategory() != null) {
                jo.put("feeCategoryId", quotation.getFeeCategory().getValue());
                jo.put("feeCategoryDesc", quotation.getFeeCategory().getDesc());
            }
        }
        if(ArrayUtils.contains(params, "payCategory")){
            jo.put("payCategoryId", quotation.getPayCategory().getValue());
            jo.put("payCategoryDesc", quotation.getPayCategory().getDesc());
        }
        if(ArrayUtils.contains(params, "personalAmount")){
            jo.put("personalAmount", quotation.getPersonalAmount());
        }
        if(ArrayUtils.contains(params, "companyAmount")){
            jo.put("companyAmount", quotation.getCompanyAmount());
        }
        if(ArrayUtils.contains(params, "allAmount")){
            jo.put("allAmount", quotation.getAllAmount());
        }
        if(ArrayUtils.contains(params, "orderAmount")){
            jo.put("orderAmount", quotation.getOrderAmount());
        }

        if(ArrayUtils.contains(params, "quotationItem")){
            List <QuotationItem> quotationItemList = quotation.getQuotationItems();
            JSONArray ja = new JSONArray();
            JSONObject joitem;
            int i = 0;
            for(QuotationItem quotationItem : quotationItemList) {

                if(quotation.getFeeCategory().equals(FeeCategory.fc0) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
                    i++;
                    joitem = new JSONObject();
                    joitem.put("id", i);
                    joitem.put("project", quotationItem.getEndowment().getDesc());
                    joitem.put("cardinalNum", quotationItem.getEndowmentBase());
                    joitem.put("personalNum", quotationItem.getEndowmentPerson());
                    joitem.put("companyNum", quotationItem.getEndowmentCompany());
                    ja.add(joitem);

                    i ++;
                    joitem = new JSONObject();
                    joitem.put("id", i);
                    joitem.put("project", quotationItem.getUnemployment().getDesc());
                    joitem.put("cardinalNum", quotationItem.getUnemploymentBase());
                    joitem.put("personalNum", quotationItem.getUnemploymentPerson());
                    joitem.put("companyNum", quotationItem.getUnemploymentCompany());
                    ja.add(joitem);

                    i ++;
                    joitem = new JSONObject();
                    joitem.put("id", i);
                    joitem.put("project", quotationItem.getInjury().getDesc());
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
                } else if(quotation.getFeeCategory().equals(FeeCategory.fc1)) {
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

            jo.put("quotationItem", quotation.getAllAmount());
        }

        return jo;
    }
}
