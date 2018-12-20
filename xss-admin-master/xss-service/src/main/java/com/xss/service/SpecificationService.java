/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.SpecificationDao;
import com.xss.domain.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 商品规格
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class SpecificationService extends BaseService<Specification, Long>{

	@Resource
	private SpecificationDao specificationDao;

	@Resource
	public void setBaseDao(SpecificationDao specificationDao) {
		super.setBaseDao(specificationDao);
	}

	/**
	 * 获取商品规格Json数据结构（按照企业中心添加商品需要封装）
	 * @param
	 * @return
	 * @throws Exception
	 */
	public JSONObject getJsonObject(ProductCategory productCategory) throws Exception{
		JSONObject jo = new JSONObject();
		List<Specification> specifications = getSpecificationDao().findByProductCategory(productCategory);

		JSONArray specificationJa = new JSONArray();
		JSONObject SpecificationProductJo = new JSONObject();

		for (Specification specification : specifications) {
			JSONObject specificationJo = new JSONObject();
			specificationJo.put("label", specification.getName());
			specificationJo.put("value", specification.getId().toString());
			specificationJa.add(specificationJo);

			JSONObject SpecificationProductTemp = new JSONObject();
			JSONArray originListJa = new JSONArray();
			for (SpecificationValue specificationValue : specification.getSpecificationValues()) {
				JSONObject specificationValueJo = new JSONObject();
				specificationValueJo.put("label", specificationValue.getName());
				specificationValueJo.put("value", specificationValue.getId().toString());
				originListJa.add(specificationValueJo);
			}
			SpecificationProductTemp.put("valueCode", "");
			SpecificationProductTemp.put("valueText", "");
			SpecificationProductTemp.put("originList", originListJa);
			SpecificationProductJo.put(specification.getId().toString(), SpecificationProductTemp);
		}
		jo.put("specificationNames", specificationJa);  //规格复选框列别选项
		jo.put("SpecificationProduct", SpecificationProductJo);  //规格值table初始化数据接口
		return jo;
	}

	/**
	 * 商品规格回显
	 * 获取商品规格Json数据结构（按照企业中心编辑商品需要封装）
	 * @param
	 * @return  specificationIds
	 * @throws Exception
	 */
	public JSONObject getJsonObject(Product product) throws Exception{
		JSONObject jo = new JSONObject();
		ProductCategory productCategory = product.getProductCategory();
		if (productCategory.getSpecifications().isEmpty()) {
			productCategory = productCategory.getParent();
		}
		//Company company = product.getStore().getCompany();
		List<Specification> specifications = getSpecificationDao().findByProductCategory(productCategory);

		JSONArray specificationJa = new JSONArray();
		JSONArray specValueJa = new JSONArray();

		JSONArray specificationIdJa = new JSONArray();

		for (Specification specification: product.getSpecifications()) {
			specificationIdJa.add(specification.getId().toString());
		}
		JSONObject specificationProductJo = new JSONObject();
		for (Specification specification : specifications) {

			JSONObject specificationJo = new JSONObject();
			specificationJo.put("label", specification.getName());
			specificationJo.put("value", specification.getId().toString());
			specificationJa.add(specificationJo);

			JSONObject specificationProductTemp = new JSONObject();
			JSONArray originListJa = new JSONArray();
			String valueCode = "";
			for (SpecificationValue specificationValue : specification.getSpecificationValues()) {
				JSONObject specificationValueJo = new JSONObject();
				specificationValueJo.put("label", specificationValue.getName());
				specificationValueJo.put("value", specificationValue.getId().toString());
				for (SpecificationValue temp : product.getSpecificationValues()) {
					if(temp.getId().equals(specificationValue.getId())){
						valueCode = temp.getId().toString();
						break;
					}
				}
				originListJa.add(specificationValueJo);
			}

			specificationProductTemp.put("valueCode", valueCode);
			specificationProductTemp.put("valueText", "");
			specificationProductTemp.put("originList", originListJa);
			specificationProductJo.put("id", product.getId());
			specificationProductJo.put(specification.getId().toString(), specificationProductTemp);

		}
		specValueJa.add(specificationProductJo);
		for(Product siblingProduct : product.getGoods().getProducts()){
			if (!siblingProduct.equals(product)){
				JSONObject specificationProductJo1 = new JSONObject();
				for (Specification specification : specifications) {

					String valueCode = "";
					JSONArray originListJa = new JSONArray();
					JSONObject specificationProductTemp = new JSONObject();
					for (SpecificationValue specificationValue : specification.getSpecificationValues()) {
						JSONObject specificationValueJo = new JSONObject();
						specificationValueJo.put("label", specificationValue.getName());
						specificationValueJo.put("value", specificationValue.getId().toString());
						for (SpecificationValue temp : siblingProduct.getSpecificationValues()) {
							if (temp.getId().equals(specificationValue.getId())) {
								valueCode = temp.getId().toString();
								break;
							}
						}
						originListJa.add(specificationValueJo);
					}

					specificationProductTemp.put("valueCode", valueCode);
					specificationProductTemp.put("valueText", "");
					specificationProductTemp.put("originList", originListJa);
					specificationProductJo1.put("id", siblingProduct.getId());
					specificationProductJo1.put(specification.getId().toString(), specificationProductTemp);

				}
				specValueJa.add(specificationProductJo1);
			}
		}
		jo.put("specificationNames", specificationJa);  //规格复选框列别选项
		jo.put("SpecificationProduct", specValueJa);  //规格值table初始化数据接口
		jo.put("specificationIds", specificationIdJa);  //商品拥有规格id集合
		return jo;
	}

	public JSONObject convertSpecJson(JSONArray specificationJa, List<Specification> specifications, Product product){
		JSONObject specificationProductJo = new JSONObject();
		for (Specification specification : specifications) {
			JSONObject specificationJo = new JSONObject();
			specificationJo.put("label", specification.getName());
			specificationJo.put("value", specification.getId().toString());
			specificationJa.add(specificationJo);

			JSONObject specificationProductTemp = new JSONObject();
			JSONArray originListJa = new JSONArray();
			String valueCode = "";
			for (SpecificationValue specificationValue : specification.getSpecificationValues()) {
				JSONObject specificationValueJo = new JSONObject();
				specificationValueJo.put("label", specificationValue.getName());
				specificationValueJo.put("value", specificationValue.getId().toString());
				for (SpecificationValue temp : product.getSpecificationValues()) {
					if(temp.getId().equals(specificationValue.getId())){
						valueCode = temp.getId().toString();
						break;
					}
				}
				originListJa.add(specificationValueJo);
			}
			specificationProductTemp.put("valueCode", valueCode);
			specificationProductTemp.put("valueText", "");
			specificationProductTemp.put("originList", originListJa);
			specificationProductJo.put(specification.getId().toString(), specificationProductTemp);
		}
		return specificationProductJo;
	}

	public SpecificationDao getSpecificationDao() {
		return specificationDao;
	}

	public void setSpecificationDao(SpecificationDao specificationDao) {
		this.specificationDao = specificationDao;
	}
}