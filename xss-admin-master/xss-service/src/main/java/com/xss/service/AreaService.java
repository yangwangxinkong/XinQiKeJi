/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.AreaDao;
import com.xss.domain.Area;
import com.xss.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

/**
 * Service - 地区管理
 * 
 * @author fan.hu
 * @version 1.0
 */
@Service
public class AreaService extends BaseService<Area, Long>{

	@Resource
	private AreaDao areaDao;

	@Resource
	public void setBaseDao(AreaDao areaDao) {
		super.setBaseDao(areaDao);
	}

	@Transactional(readOnly = true)
	public List<Area> findRoots() {
		return findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<Area> findRoots(Integer count) {
		String jpql = "select area from Area area where area.parent is null order by area.order asc";
		TypedQuery<Area> query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public List<Area> findTree() {
		return findChildren(null, null);
	}

	@Transactional(readOnly = true)
	public List<Area> findChildren(Area area) {
		return findChildren(area, null);
	}

	@Transactional(readOnly = true)
	public List<Area> findChildren(Area area, Integer count) {
		TypedQuery<Area> query;
		if (area != null) {
//			String jpql = "select area from Area area where area.treePath like :treePath order by area.order asc";
//			query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + "," + area.getId() + "," + "%");
			String jpql = "select area from Area area where area.parent = :parent order by area.order asc";
			query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT).setParameter("parent", area);
		} else {
			String jpql = "select area from Area area order by area.order asc";
			query = entityManager.createQuery(jpql, Area.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), area);
	}

	/**
	 * 排序地区
	 *
	 * @param areas
	 *            地区
	 * @param parent
	 *            上级地区
	 * @return 地区列表
	 */
	private List<Area> sort(List<Area> areas, Area parent) {
		List<Area> result = new ArrayList<Area>();
		if (areas != null) {
			for (Area area : areas) {
				if ((area.getParent() != null && area.getParent().equals(parent)) || (area.getParent() == null && parent == null)) {
					result.add(area);
					result.addAll(sort(areas, area));
				}
			}
		}
		return result;
	}

}