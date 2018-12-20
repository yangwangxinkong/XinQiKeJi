package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.util.JsonUtil;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity - 社保公积金基数
 *
 * @author hu
 * @version 1.0
 */

@Entity
@Table(name = "xx_pay_base")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_pay_base_sequence")
public class PayBase extends BaseEntity {

    public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "socialBaseMin", "socialBaseMax", "providentBaseMin"
            , "providentBaseMax", "ssc0BaseMin", "ssc1BaseMin", "ssc2BaseMin", "ssc3BaseMin", "ssc4BaseMin", "ssc5BaseMin","ssc6BaseMin"};

    /**
     * 城市
     */
    private City city;

    /**
     * 社保缴费基数最小值
     */
    private BigDecimal socialBaseMin;

    /**
     * 社保缴费基数最大值
     */
    private BigDecimal socialBaseMax;

    /**
     * 社保缴费基数 养老 最小值
     */
    private BigDecimal ssc0BaseMin;

    /**
     * 社保缴费基数 失业 最小值
     */
    private BigDecimal ssc1BaseMin;

    /**
     * 社保缴费基数 工伤 最小值
     */
    private BigDecimal ssc2BaseMin;

    /**
         * 社保缴费基数 医疗 最小值
     */
    private BigDecimal ssc3BaseMin;

    /**
     * 社保缴费基数 生育 最小值
     */
    private BigDecimal ssc4BaseMin;

    /**
     * 社保缴费基数 残保 最小值
     */
    private BigDecimal ssc5BaseMin;

    /**
     * 社保缴费基数 大病 最小值
     */
    private BigDecimal ssc6BaseMin;

    /**
     * 公积金缴费基数最小值
     */
    private BigDecimal providentBaseMin;

    /**
     * 公积金缴费基数最大值
     */
    private BigDecimal providentBaseMax;

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
     * 获取 社保缴费基数 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSocialBaseMin() {
        return socialBaseMin;
    }

    /**
     * 设置 社保缴费基数 最小值
     * @param socialBaseMin
     */
    public void setSocialBaseMin(BigDecimal socialBaseMin) {
        this.socialBaseMin = socialBaseMin;
    }

    /**
     * 获取 社保缴费基数 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSsc0BaseMin() {
        return ssc0BaseMin;
    }

    /**
     * 设置 社保缴费基数 养老 最小值
     * @param ssc0BaseMin
     */
    public void setSsc0BaseMin(BigDecimal ssc0BaseMin) {
        this.ssc0BaseMin = ssc0BaseMin;
    }

    /**
     * 获取 社保缴费基数 养老 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSsc1BaseMin() {
        return ssc1BaseMin;
    }

    /**
     * 设置 社保缴费基数 失业 最小值
     * @param ssc1BaseMin
     */
    public void setSsc1BaseMin(BigDecimal ssc1BaseMin) {
        this.ssc1BaseMin = ssc1BaseMin;
    }

    /**
     * 获取 社保缴费基数 失业 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSsc2BaseMin() {
        return ssc2BaseMin;
    }

    /**
     * 设置 社保缴费基数 工伤 最小值
     * @param ssc2BaseMin
     */
    public void setSsc2BaseMin(BigDecimal ssc2BaseMin) {
        this.ssc2BaseMin = ssc2BaseMin;
    }

    /**
     * 获取 社保缴费基数 工伤 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSsc3BaseMin() {
        return ssc3BaseMin;
    }

    /**
     * 设置 社保缴费基数 医疗 最小值
     * @param ssc3BaseMin
     */
    public void setSsc3BaseMin(BigDecimal ssc3BaseMin) {
        this.ssc3BaseMin = ssc3BaseMin;
    }

    /**
     * 获取 社保缴费基数 医疗 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSsc4BaseMin() {
        return ssc4BaseMin;
    }

    /**
     * 设置 社保缴费基数 生育 最小值
     * @param ssc4BaseMin
     */
    public void setSsc4BaseMin(BigDecimal ssc4BaseMin) {
        this.ssc4BaseMin = ssc4BaseMin;
    }

    /**
     * 获取 社保缴费基数 生育 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSsc5BaseMin() {
        return ssc5BaseMin;
    }

    /**
     * 设置 社保缴费基数 残保 最小值
     * @param ssc5BaseMin
     */
    public void setSsc5BaseMin(BigDecimal ssc5BaseMin) {
        this.ssc5BaseMin = ssc5BaseMin;
    }

    /**
     * 获取 公积金缴费基数 最小值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getProvidentBaseMin() {
        return providentBaseMin;
    }

    /**
     * 设置 公积金缴费基数 最小值
     * @param providentBaseMin
     */
    public void setProvidentBaseMin(BigDecimal providentBaseMin) {
        this.providentBaseMin = providentBaseMin;
    }

    /**
     * 获取 社保缴费基数 最大值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getSocialBaseMax() {
        return socialBaseMax;
    }

    /**
     * 设置 社保缴费基数 最大值
     * @param socialBaseMax
     */
    public void setSocialBaseMax(BigDecimal socialBaseMax) {
        this.socialBaseMax = socialBaseMax;
    }

    /**
     * 获取 公积金缴费基数 最大值
     * @return
     */
    @Column( precision = 21, scale = 2)
    public BigDecimal getProvidentBaseMax() {
        return providentBaseMax;
    }

    /**
     * 设置 公积金缴费基数 最大值
     * @param providentBaseMax
     */
    public void setProvidentBaseMax(BigDecimal providentBaseMax) {
        this.providentBaseMax = providentBaseMax;
    }

    @Override
    public JSONArray convertList(List list, String[] params){
        JSONArray array = new JSONArray();
        if (null != list && !list.isEmpty()){
            for (Object entity : list){
                JSONObject jo = convertEntity(entity, params);
                array.add(jo);
            }
        }
        return array;
    }

    @Override
    public JSONObject convertEntity(Object entity, String[] params){
        PayBase payBase = (PayBase)entity;
        JSONObject jo = super.convertEntity(payBase, DEFAULT_JSON_PARAMS);

        if(ArrayUtils.contains(params, "city")) {
            jo.put("city", JsonUtil.toJSONObject(payBase.getCity(), new String[]{"id", "code", "name", "fullName"}));
        }

        return jo;
    }
    /*大病基数最小值*/
    @Column( precision = 21, scale = 2)
    public BigDecimal getSsc6BaseMin() {
        return ssc6BaseMin;
    }

    public void setSsc6BaseMin(BigDecimal ssc6BaseMin) {
        this.ssc6BaseMin = ssc6BaseMin;
    }
}
