package top.zhanwangweilai.springbootmaster.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhanwangweilai.springbootmaster.dao.UserMapper;
import top.zhanwangweilai.springbootmaster.pojo.User;

@RestController
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/user")
    public User findByName(){
        logger.info("findByName方法运行了！");
        User user = userMapper.selectUserByName("姓名");
        return user;
    }


}
