/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.CityDao;
import com.xss.domain.Area;
import com.xss.domain.City;
import com.xss.util.page.Filter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service - 城市
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class CityService extends BaseService<City, Long> {

	@Autowired
	private CityDao cityDao;

	@Resource
	public void setBaseDao(CityDao cityDao) {
		super.setBaseDao(cityDao);
	}

	@Transactional(rollbackFor = Exception.class)
	public boolean codeExists(String code) {
		if (!StringUtils.hasText(code)) {
			return false;
		}
		String jpql = "select count(*) from City city where lower(city.code) = lower(:code)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		return count > 0;
	}

	@Transactional(readOnly = true)
	public City masterCity() {
		String jpql = "select city from City city where city.isMaster = :isMaster";
		List<City> citys = entityManager.createQuery(jpql, City.class).setFlushMode(FlushModeType.COMMIT).setParameter("isMaster", true).getResultList();
		if(citys != null && citys.size() > 0) {
			return citys.get(0);
		} else {
			return null;
		}
	}

	@Transactional(readOnly = true)
	public City getCityByCode(String code) {
		if (!StringUtils.hasText(code)) {
			return null;
		}
		String jpql = "select city from City city where lower(city.code) = lower(:code)";
		List<City> citys = entityManager.createQuery(jpql, City.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getResultList();
		if(citys != null && citys.size() > 0) {
			return citys.get(0);
		} else {
			return null;
		}
	}


	@Transactional(readOnly = true)
	public List<City> findRoots() {
		return findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<City> findRoots(Integer count) {
		String jpql = "select city from City city where city.parent is null order by city.order asc";
		TypedQuery<City> query = entityManager.createQuery(jpql, City.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public List<City> findParents(City city) {
		return findParents(city, null);
	}

	@Transactional(readOnly = true)
	public List<City> findParents(City city, Integer count) {
		if (city == null || city.getParent() == null) {
			return Collections.<City> emptyList();
		}
		String jpql = "select city from City city where city.id in (:ids) order by city.grade asc";
		TypedQuery<City> query = entityManager.createQuery(jpql, City.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", city.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public List<City> findTree() {
		return findChildren(null, null);
	}

	@Transactional(readOnly = true)
	public List<City> findChildren(City city) {
		return findChildren(city, null);
	}

	@Transactional(readOnly = true)
	public List<City> findChildren(City city, Integer count) {
		TypedQuery<City> query;
		if (city != null) {
			String jpql = "select city from City city where city.treePath like :treePath order by city.order asc";
			query = entityManager.createQuery(jpql, City.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + City.TREE_PATH_SEPARATOR + city.getId() + City.TREE_PATH_SEPARATOR + "%");
		} else {
			String jpql = "select city from City city order by city.order asc";
			query = entityManager.createQuery(jpql, City.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), city);
	}

	/**
	 * 排序文章分类
	 *
	 * @param citys
	 *            文章分类
	 * @param parent
	 *            上级文章分类
	 * @return 文章分类
	 */
	private List<City> sort(List<City> citys, City parent) {
		List<City> result = new ArrayList<City>();
		if (citys != null) {
			for (City city : citys) {
				if ((city.getParent() != null && city.getParent().equals(parent)) || (city.getParent() == null && parent == null)) {
					result.add(city);
					result.addAll(sort(citys, city));
				}
			}
		}
		return result;
	}

	@Transactional
	@Override
	public void save(City city) {
		setValue(city);
		super.persist(city);
	}

	/**
	 * 设置值
	 *
	 * @param city
	 *            文章分类
	 */
	private void setValue(City city) {
		if (city == null) {
			return;
		}
		City parent = city.getParent();
		if (parent != null) {
			city.setTreePath(parent.getTreePath() + parent.getId() + City.TREE_PATH_SEPARATOR);
		} else {
			city.setTreePath(City.TREE_PATH_SEPARATOR);
		}
		city.setGrade(city.getTreePaths().size());
	}

	/**
	 * 设置treePath、grade并更新
	 *
	 * @param city
	 *            文章分类
	 * @return 文章分类
	 */
	@Override
	public City merge(City city) {
		Assert.notNull(city);
		setValue(city);
		for (City cityTemp : findChildren(city, null)) {
			setValue(cityTemp);
		}
		return super.merge(city);
	}

	@Transactional(readOnly = true)
	public City findByAddress(String address){
		City city = null;
		if (org.apache.commons.lang.StringUtils.isBlank(address)) {
			city = masterCity();
		}else{
			List<Filter> filters = new ArrayList<Filter>();
			filters.add(Filter.like("name", splitAddress(address)));
			List<City> list = findList(null, 1, filters, null);
			if (null == list || list.isEmpty()) {
				city = masterCity();
			}else{
				city = list.get(0);
			}
		}
		return city;
	}

	private String splitAddress(String address){
		String cityName = "北京";
		if (address.contains("省") && address.contains("市")) {
			cityName = address.substring(address.indexOf("省")+1, address.indexOf("市"));
		}else if (address.contains("市")){
			cityName = address.substring(0, address.indexOf("市"));
		}
		System.out.println("cityName:" + cityName);
		return cityName;
	}

	public CityDao getCityDao() {
		return cityDao;
	}

	public void setCityDao(CityDao cityDao) {
		this.cityDao = cityDao;
	}
}