package top.zwwl.management.manage.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.zwwl.management.manage.pojo.User;


@Mapper
@Repository
public interface UserMapper {
    User selectUserByName(String name);
}
