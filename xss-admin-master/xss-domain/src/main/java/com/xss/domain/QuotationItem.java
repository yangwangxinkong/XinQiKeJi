package com.xss.domain;

import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.SocialSecurityCategory;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Entity - 报价单基本表
 *
 * @author hu
 * @version 1.0
 */

@Entity
@Table(name = "xx_quotation_item")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_quotation_item_sequence")
public class QuotationItem extends BaseEntity {

    public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id"};

    /**
     * 报价单基本信息
     */
    private  Quotation quotation;

    /** 社保类别 养老 */
    private SocialSecurityCategory endowment;

    /** 养老基数 */
    private BigDecimal endowmentBase;

    /** 养老个人缴费 */
    private BigDecimal endowmentPerson;

    /** 养老公司缴费 */
    private BigDecimal endowmentCompany;

    /** 社保类别 失业 */
    private SocialSecurityCategory unemployment;

    /** 失业基数 */
    private BigDecimal unemploymentBase;

    /** 失业个人缴费 */
    private BigDecimal unemploymentPerson;

    /** 失业公司缴费 */
    private BigDecimal unemploymentCompany;

    /** 社保类别 工伤 */
    private SocialSecurityCategory injury;

    /** 工伤 基数 */
    private BigDecimal injuryBase;

    /** 工伤 个人缴费 */
    private BigDecimal injuryPerson;

    /** 工伤 公司缴费 */
    private BigDecimal injuryCompany;

    /** 社保类别 医疗 */
    private SocialSecurityCategory medicare;

    /** 医疗 基数 */
    private BigDecimal medicareBase;

    /** 医疗 个人缴费 */
    private BigDecimal medicarePerson;

    /** 医疗 公司缴费 */
    private BigDecimal medicareCompany;

    /** 社保类别 生育 */
    private SocialSecurityCategory maternity;

    /** 生育 基数 */
    private BigDecimal maternityBase;

    /** 生育 个人缴费 */
    private BigDecimal maternityPerson;

    /** 生育 公司缴费 */
    private BigDecimal maternityCompany;

    /** 社保类别 残保 */
    private SocialSecurityCategory disability;

    /** 残保 基数 */
    private BigDecimal disabilityBase;

    /** 残保 个人缴费 */
    private BigDecimal disabilityPerson;

    /** 残保 公司缴费 */
    private BigDecimal disabilityCompany;



    /*添加大病部分开始*/
    /** 社保类别 大病 */
    private SocialSecurityCategory serious;

    /** 大病 基数 */
    private BigDecimal seriousBase;

    /** 大病 个人缴费 */
    private BigDecimal seriousPerson;

    /** 大病 公司缴费 */
    private BigDecimal seriousCompany;
    /*添加大病部分结束*/






    /** 公积金 */
    private String providentFundName;

    /** 公积金 基数 */
    private BigDecimal providentFundBase;

    /** 公积金 个人缴费 */
    private BigDecimal providentFundPerson;

    /** 公积金 公司缴费 */
    private BigDecimal providentFundCompany;

    /**
     * 获取 报价单基本信息
     *
     * @return 报价单基本信息
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quotations", nullable = false, updatable = false)
    public Quotation getQuotation() {
        return quotation;
    }

    /**
     * 设置 报价单基本信息
     *
     * @param quotation
     *            报价单基本信息
     */
    public void setQuotation(Quotation quotation) {
        this.quotation = quotation;
    }

    /**
     * 获取  养老
     * @return
     */
    public SocialSecurityCategory getEndowment() {
        return endowment;
    }

    /**
     * 设置  养老
     * @param endowment
     */
    public void setEndowment(SocialSecurityCategory endowment) {
        this.endowment = endowment;
    }

    /**
     * 获取  养老基数
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getEndowmentBase() {
        return endowmentBase;
    }

    /**
     * 设置  养老基数
     * @param
     */
    public void setEndowmentBase(BigDecimal endowmentBase) {
        this.endowmentBase = endowmentBase;
    }

    /**
     * 获取  养老个人
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getEndowmentPerson() {
        return endowmentPerson;
    }

    /**
     * 设置  养老个人
     * @return
     */
    public void setEndowmentPerson(BigDecimal endowmentPerson) {
        this.endowmentPerson = endowmentPerson;
    }

    /**
     * 获取  养老公司
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getEndowmentCompany() {
        return endowmentCompany;
    }

    /**
     * 设置  养老公司
     * @return
     */
    public void setEndowmentCompany(BigDecimal endowmentCompany) {
        this.endowmentCompany = endowmentCompany;
    }

    /**
     * 获取  失业
     * @return
     */
    public SocialSecurityCategory getUnemployment() {
        return unemployment;
    }

    /**
     * 设置  失业
     * @return
     */
    public void setUnemployment(SocialSecurityCategory unemployment) {
        this.unemployment = unemployment;
    }

    /**
     * 获取  失业基数
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getUnemploymentBase() {
        return unemploymentBase;
    }

    /**
     * 设置  失业基数
     * @return
     */
    public void setUnemploymentBase(BigDecimal unemploymentBase) {
        this.unemploymentBase = unemploymentBase;
    }

    /**
     * 获取  失业个人
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getUnemploymentPerson() {
        return unemploymentPerson;
    }

    /**
     * 设置  失业个人
     * @return
     */
    public void setUnemploymentPerson(BigDecimal unemploymentPerson) {
        this.unemploymentPerson = unemploymentPerson;
    }

    /**
     * 获取  失业公司
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getUnemploymentCompany() {
        return unemploymentCompany;
    }

    /**
     * 设置  失业公司
     * @return
     */
    public void setUnemploymentCompany(BigDecimal unemploymentCompany) {
        this.unemploymentCompany = unemploymentCompany;
    }

    /**
     * 获取  工伤
     * @return
     */
    public SocialSecurityCategory getInjury() {
        return injury;
    }

    /**
     * 设置  工伤
     * @return
     */
    public void setInjury(SocialSecurityCategory injury) {
        this.injury = injury;
    }

    /**
     * 获取  工伤 基数
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getInjuryBase() {
        return injuryBase;
    }

    /**
     * 设置  工伤 基数
     * @return
     */
    public void setInjuryBase(BigDecimal injuryBase) {
        this.injuryBase = injuryBase;
    }

    /**
     * 获取  工伤个人
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getInjuryPerson() {
        return injuryPerson;
    }

    /**
     * 设置  工伤个人
     * @return
     */
    public void setInjuryPerson(BigDecimal injuryPerson) {
        this.injuryPerson = injuryPerson;
    }

    /**
     * 获取  工伤公司
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getInjuryCompany() {
        return injuryCompany;
    }

    /**
     * 设置  工伤公司
     * @return
     */
    public void setInjuryCompany(BigDecimal injuryCompany) {
        this.injuryCompany = injuryCompany;
    }

    /**
     * 获取  医疗
     * @return
     */
    public SocialSecurityCategory getMedicare() {
        return medicare;
    }

    /**
     * 设置  医疗
     * @return
     */
    public void setMedicare(SocialSecurityCategory medicare) {
        this.medicare = medicare;
    }

    /**
     * 获取  医疗基数
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getMedicareBase() {
        return medicareBase;
    }

    /**
     * 设置  医疗基数
     * @return
     */
    public void setMedicareBase(BigDecimal medicareBase) {
        this.medicareBase = medicareBase;
    }

    /**
     * 获取  医疗个人
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getMedicarePerson() {
        return medicarePerson;
    }

    /**
     * 设置  医疗个人
     * @return
     */
    public void setMedicarePerson(BigDecimal medicarePerson) {
        this.medicarePerson = medicarePerson;
    }

    /**
     * 获取  医疗公司
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getMedicareCompany() {
        return medicareCompany;
    }

    /**
     * 设置  医疗公司
     * @return
     */
    public void setMedicareCompany(BigDecimal medicareCompany) {
        this.medicareCompany = medicareCompany;
    }

    /**
     * 获取  生育
     * @return
     */
    public SocialSecurityCategory getMaternity() {
        return maternity;
    }

    /**
     * 设置  生育
     * @return
     */
    public void setMaternity(SocialSecurityCategory maternity) {
        this.maternity = maternity;
    }

    /**
     * 获取  生育基数
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getMaternityBase() {
        return maternityBase;
    }

    /**
     * 设置  生育基数
     * @return
     */
    public void setMaternityBase(BigDecimal maternityBase) {
        this.maternityBase = maternityBase;
    }

    /**
     * 获取  生育个人
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getMaternityPerson() {
        return maternityPerson;
    }

    /**
     * 设置  生育个人
     * @return
     */
    public void setMaternityPerson(BigDecimal maternityPerson) {
        this.maternityPerson = maternityPerson;
    }

    /**
     * 获取  生育公司
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getMaternityCompany() {
        return maternityCompany;
    }

    /**
     * 设置  生育公司
     * @return
     */
    public void setMaternityCompany(BigDecimal maternityCompany) {
        this.maternityCompany = maternityCompany;
    }

    /**
     * 获取  残保
     * @return
     */
    public SocialSecurityCategory getDisability() {
        return disability;
    }

    /**
     * 设置  残保
     * @return
     */
    public void setDisability(SocialSecurityCategory disability) {
        this.disability = disability;
    }

    /**
     * 获取  残保基数
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getDisabilityBase() {
        return disabilityBase;
    }

    /**
     * 设置  残保基数
     * @return
     */
    public void setDisabilityBase(BigDecimal disabilityBase) {
        this.disabilityBase = disabilityBase;
    }

    /**
     * 获取  残保个人
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getDisabilityPerson() {
        return disabilityPerson;
    }

    /**
     * 设置  残保个人
     * @return
     */
    public void setDisabilityPerson(BigDecimal disabilityPerson) {
        this.disabilityPerson = disabilityPerson;
    }

    /**
     * 获取  残保公司
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getDisabilityCompany() {
        return disabilityCompany;
    }

    /**
     * 设置  残保公司
     * @return
     */
    public void setDisabilityCompany(BigDecimal disabilityCompany) {
        this.disabilityCompany = disabilityCompany;
    }

    /**
     * 获取  公积金
     * @return
     */
    public String getProvidentFundName() {
        return providentFundName;
    }

    /**
     * 设置  公积金
     * @return
     */
    public void setProvidentFundName(String providentFundName) {
        this.providentFundName = providentFundName;
    }

    /**
     * 获取  公积金基数
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getProvidentFundBase() {
        return providentFundBase;
    }

    /**
     * 设置  公积金基数
     * @return
     */
    public void setProvidentFundBase(BigDecimal providentFundBase) {
        this.providentFundBase = providentFundBase;
    }

    /**
     * 获取  公积金个人
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getProvidentFundPerson() {
        return providentFundPerson;
    }

    /**
     * 设置  公积金个人
     * @return
     */
    public void setProvidentFundPerson(BigDecimal providentFundPerson) {
        this.providentFundPerson = providentFundPerson;
    }

    /**
     * 获取  公积金公司
     * @return
     */
    @Column(precision = 21, scale = 2)
    public BigDecimal getProvidentFundCompany() {
        return providentFundCompany;
    }

    /**
     * 设置  公积金公司
     * @return
     */
    public void setProvidentFundCompany(BigDecimal providentFundCompany) {
        this.providentFundCompany = providentFundCompany;
    }

    @Override
    public JSONObject convertEntity(Object entity, String[] params){
        QuotationItem industry = (QuotationItem)entity;
        JSONObject jo = super.convertEntity(industry, DEFAULT_JSON_PARAMS);

        return jo;
    }

    public SocialSecurityCategory getSerious() {
        return serious;
    }

    public void setSerious(SocialSecurityCategory serious) {
        this.serious = serious;
    }

    @Column(precision = 21, scale = 2)
    public BigDecimal getSeriousBase() {
        return seriousBase;
    }

    public void setSeriousBase(BigDecimal seriousBase) {
        this.seriousBase = seriousBase;
    }

    @Column(precision = 21, scale = 2)
    public BigDecimal getSeriousPerson() {
        return seriousPerson;
    }

    public void setSeriousPerson(BigDecimal seriousPerson) {
        this.seriousPerson = seriousPerson;
    }

    public BigDecimal getSeriousCompany() {
        return seriousCompany;
    }

    public void setSeriousCompany(BigDecimal seriousCompany) {
        this.seriousCompany = seriousCompany;
    }
}
