package com.xss.domain;

import javax.persistence.*;

@Entity
@Table(name="xx_customer_evaluation")
@SequenceGenerator(name="sequenceGenerator", sequenceName = "xx_customer_evaluation_sequence")
public class CustomerEvaluation extends OrderEntity{
    private static final long serialVersionUID = -2447689027580348610L;

    //public static final String DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
    //public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id"};


    @Override
    public String toString() {
        return "CustomerEvaluation{" +
                "customerName='" + customerName + '\'' +
                ", companyName='" + companyName + '\'' +
                ", evaluation='" + evaluation + '\'' +
                ", customerImg='" + customerImg + '\'' +
                '}';
    }

    /*客户名称*/
    private String customerName;
    /*所属公司名称*/
    private String companyName;
    /*评价内容*/
    private String evaluation;
    /*客户图片*/
    private String customerImg;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getCustomerImg() {
        return customerImg;
    }

    public void setCustomerImg(String customerImg) {
        this.customerImg = customerImg;
    }
}
