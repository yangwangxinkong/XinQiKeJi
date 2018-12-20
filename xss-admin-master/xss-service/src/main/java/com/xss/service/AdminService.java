/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.AdminDao;
import com.xss.domain.Admin;
import com.xss.domain.Role;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;


/**
 * Service - 管理员
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class AdminService extends BaseService<Admin, Long>{

	@Resource
	private AdminDao adminDao;
	@PersistenceContext
	protected EntityManager entityManager;

	@Resource
	public void setBaseDao(AdminDao adminDao) {
		super.setBaseDao(adminDao);
	}
	@Transactional(readOnly = true)
	public boolean usernameExists(String username) {
		return adminDao.existsByUsername(username);
	}

	@Transactional(readOnly = true)
	public Admin findByUsername(String username) {
		return adminDao.findByUsername(username);
	}

	@Transactional(readOnly = true)
	public List<String> findAuthorities(Long id) {
		List<String> authorities = new ArrayList<String>();
		Admin admin = adminDao.findOne(id);
		if (admin != null) {
			for (Role role : admin.getRoles()) {
				authorities.addAll(role.getAuthorities());
			}
		}
		return authorities;
	}

	@Transactional
	public List<Admin> findByRoleSql(Long roleId){
		List<Admin> admins = new ArrayList<Admin>();
		String sql = "select a.* from xx_admin a, xx_admin_role ar where a.id=ar.admins_id and ar.roles_id=" + roleId;
		admins = entityManager.createNativeQuery(sql, Admin.class).getResultList();
		return admins;
	}

	@Override
	@Transactional(readOnly = true, rollbackFor = Exception.class)
	public Admin find(Long id){
		return super.find(id);
	}

}