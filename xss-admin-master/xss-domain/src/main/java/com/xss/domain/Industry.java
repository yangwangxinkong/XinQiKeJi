package com.xss.domain;

import com.alibaba.fastjson.JSONObject;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity - 行业
 *
 * @author hu
 * @version 1.0
 */

@Entity
@Table(name = "xx_industry")
@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_industry_sequence")
public class Industry extends OrderEntity {

    public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name"};

    /** 行业名称 */
    private String name;

    /**
     * 用户
     */
    private Set<Member> members = new HashSet<Member>();

    @NotNull
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取用户
     * @return
     */
    @OneToMany(mappedBy = "industry", fetch = FetchType.LAZY)
    public Set<Member> getMembers() {
        return members;
    }

    /**
     * 设置用户
     * @param members
     */
    public void setMembers(Set<Member> members) {
        this.members = members;
    }


    @Override
    public JSONObject convertEntity(Object entity, String[] params){
        Industry industry = (Industry)entity;
        JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);

        return jo;
    }
}
