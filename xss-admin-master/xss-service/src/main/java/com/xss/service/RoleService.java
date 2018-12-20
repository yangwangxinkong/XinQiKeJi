/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.RoleDao;
import com.xss.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * Service - 角色
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class RoleService extends BaseService<Role, Long> {

	@Autowired
	private RoleDao roleDao;

	@Resource
	public void setBaseDao(RoleDao roleDao) {
		super.setBaseDao(roleDao);
	}

	public List<Role> findListByIdList(List<Long> ids){
		return roleDao.findAll(ids);
	}

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}
}