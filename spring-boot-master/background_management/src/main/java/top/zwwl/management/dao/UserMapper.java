package top.zwwl.management.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.zwwl.management.pojo.User;

@Mapper
@Repository
public interface UserMapper {
    User selectUserByName(String name);
}
