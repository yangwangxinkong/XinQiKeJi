package com.xss.dao;

import com.xss.domain.Proposer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 *  Dao - 申请人
 * @author zzl
 * @date 2018/12/15
 */
@Component
public interface ProposerDao extends JpaRepository<Proposer,Long> {

}
