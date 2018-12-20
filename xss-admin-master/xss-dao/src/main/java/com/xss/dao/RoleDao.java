/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 角色
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface RoleDao extends JpaRepository<Role, Long> {

}