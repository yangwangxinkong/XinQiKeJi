package top.zhanwangweilai.springbootmaster.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.zhanwangweilai.springbootmaster.pojo.User;

@Mapper
@Repository
public interface UserMapper {
    User selectUserByName(String name);
}
