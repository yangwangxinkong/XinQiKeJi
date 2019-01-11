package top.zhanwangweilai.springbootmaster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement // 开启事务管理
@MapperScan(value ="top.zhanwangweilai.springbootmaster.dao")
public class SpringBootMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMasterApplication.class, args);
    }

}

