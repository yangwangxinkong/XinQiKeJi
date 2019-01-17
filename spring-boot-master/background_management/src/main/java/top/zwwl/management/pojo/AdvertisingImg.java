package com.sample;


public class AdvertisingImg {

  private long id;
  private java.sql.Timestamp createdTime;
  private java.sql.Timestamp modifyTime;
  private long width;
  private long heigth;
  private long imgType;
  private String imgAdress;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public java.sql.Timestamp getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(java.sql.Timestamp createdTime) {
    this.createdTime = createdTime;
  }


  public java.sql.Timestamp getModifyTime() {
    return modifyTime;
  }

  public void setModifyTime(java.sql.Timestamp modifyTime) {
    this.modifyTime = modifyTime;
  }


  public long getWidth() {
    return width;
  }

  public void setWidth(long width) {
    this.width = width;
  }


  public long getHeigth() {
    return heigth;
  }

  public void setHeigth(long heigth) {
    this.heigth = heigth;
  }


  public long getImgType() {
    return imgType;
  }

  public void setImgType(long imgType) {
    this.imgType = imgType;
  }


  public String getImgAdress() {
    return imgAdress;
  }

  public void setImgAdress(String imgAdress) {
    this.imgAdress = imgAdress;
  }

}
