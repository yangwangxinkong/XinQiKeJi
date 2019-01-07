package com.xss.domain;


import javax.persistence.*;

@Entity
@Table(name="xx_share_config")
@SequenceGenerator(name="sequenceGenerator", sequenceName = "xx_share_config_sequence")
public class ShareConfig extends OrderEntity{
    private static final long serialVersionUID = 2335692872946486612L;

    /*分享标题*/
    private String shareTitle;
    /*分享描述*/
    private String shareDesc;
    /*分享图标*/
    private String shareImg;
    /*分享类型*/
    private Integer shareTypes;

    /*用于测试*/
    @Override
    public String toString() {
        return "ShareConfig{" +
                "shareTitle='" + shareTitle + '\'' +
                ", shareDesc='" + shareDesc + '\'' +
                ", shareImg='" + shareImg + '\'' +
                ", shareTypes='" + shareTypes + '\'' +
                '}';
    }

    public String getShareTitle() {
        return shareTitle;
    }

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareDesc() {
        return shareDesc;
    }

    public void setShareDesc(String shareDesc) {
        this.shareDesc = shareDesc;
    }

    public String getShareImg() {
        return shareImg;
    }

    public void setShareImg(String shareImg) {
        this.shareImg = shareImg;
    }

    public Integer getShareTypes() {
        return shareTypes;
    }

    public void setShareTypes(Integer shareTypes) {
        this.shareTypes = shareTypes;
    }
}
