package com.xss.dao;

import com.xss.domain.Navigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Dao - 导航
 * @author zzl
 * @date 2018/8/2
 */
@Component
public interface NavigationDao extends JpaRepository<Navigation,Long> {
    /**
     * 查找导航
     *
     * @param position
     *            位置
     * @return 导航
     */
    @Query("select n from Navigation n where n.position = ?1")
    List<Navigation> findList(@Param("position") Navigation.Position position);
}
