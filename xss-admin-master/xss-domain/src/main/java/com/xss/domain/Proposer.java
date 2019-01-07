/*
 *  
 *  
 *  
 */
package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.ShareCategory;
import com.xss.util.CurrencyMethod;
import com.xss.util.DateUtil;

import javax.persistence.Entity;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.List;

/**
 * Entity - 申请人信息
 * 
 * @author DannyZou
 * @version 1.0
 */
@Entity
@Table(name = "xx_proposer")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_proposer_sequence")
public class Proposer extends BaseEntity {

	private static final long serialVersionUID = 2673602067029665976L;

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name", "phone"};


	/**
	 * 申请人姓名
	 */
	private String name;

	/**
	 * 申请人联系电话
	 */
	private String phone;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
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
		Proposer proposer = (Proposer)entity;
		JSONObject jo = super.convertEntity(proposer, DEFAULT_JSON_PARAMS);
		jo.put("createDate", DateUtil.format(proposer.getCreateDate()));
		return jo;
	}

}