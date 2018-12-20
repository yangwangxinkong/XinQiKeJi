/*
 *  
 *  
 *  
 */
package com.xss.dao;


import com.xss.domain.Admin;
import com.xss.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Dao - 管理员
 * 
 * @author DannyZou
 * @version 1.0
 */
@Component
public interface MemberDao extends JpaRepository<Member, Long> {

	/**
	 * 判断用户名是否存在
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 用户名是否存在
	 */
	//@Query("select count(e) > 0 from Admin e where e.username=?1")
	boolean existsByUsername(String username);

	/**
	 * 根据用户名查找管理员
	 * 
	 * @param username
	 *            用户名(忽略大小写)
	 * @return 管理员，若不存在则返回null
	 */
	Admin findByUsername(String username);

}