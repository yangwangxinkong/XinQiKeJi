package top.zwwl.management.manage.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/*
* 配置druid数据库连接池（springboot默认不支持，所以需要自己配置）
* */
@Configuration
@MapperScan(basePackages = DatasourceConfig.PACKAGE,sqlSessionFactoryRef = "sqlSessionFactory")
public class DatasourceConfig  {
    // 精确到 master 目录，以便跟其他数据源隔离
    static final String PACKAGE = "top.zhangwangweilai.springbootmaster.dao";//给mybatis配置实体类的路径
    static final String MAPPER_LOCATION = "classpath*:mapper/*.xml";//给mybatis配置映射文件的路径
    @Value("${ds1.datasource.url}")
    private String url;
    @Value("${ds1.datasource.username}")
    private String user;
    @Value("${ds1.datasource.password}")
    private String password;
    @Value("${ds1.datasource.driverClassName}")
    private String driverClass;

    @Value("${ds1.datasource.maxActive}")
    private Integer maxActive;
    @Value("${ds1.datasource.minIdle}")
    private Integer minIdle;
    @Value("${ds1.datasource.initialSize}")
    private Integer initialSize;
    @Value("${ds1.datasource.maxWait}")
    private Long maxWait;
    @Value("${ds1.datasource.timeBetweenEvictionRunsMillis}")
    private Long timeBetweenEvictionRunsMillis;
    @Value("${ds1.datasource.minEvictableIdleTimeMillis}")
    private Long minEvictableIdleTimeMillis;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testWhileIdle;
    @Value("${ds1.datasource.testWhileIdle}")
    private Boolean testOnBorrow;
    @Value("${ds1.datasource.testOnBorrow}")
    private Boolean testOnReturn;

    @Bean(name="dataSource")
    @Primary//主要的，基本的配置---@Primary表示这里定义的DataSource将覆盖其他来源的DataSource。
    public DataSource dataSource(){
        //jdbc配置
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(driverClass);
        dataSource.setUrl(url);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        //连接池配置
        dataSource.setMaxActive(maxActive);//配置最大连接数量
        dataSource.setMinIdle(minIdle);//配置最小空闲数量（闲置的）
        dataSource.setInitialSize(initialSize);//配置初始化连接数量
        dataSource.setMaxWait(maxWait);//配置最大空闲数量（等候）
        dataSource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);//配置连接回收时间毫秒
        dataSource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);//配置最小回收空闲时间毫秒
        dataSource.setTestWhileIdle(testWhileIdle);//配置测试空闲时间
        dataSource.setTestOnBorrow(testOnBorrow);
        dataSource.setTestOnReturn(testOnReturn);//配置测试是否需要返回值
        dataSource.setValidationQuery("select 'x'");//配置测试语句

        dataSource.setPoolPreparedStatements(true);//配置连接池使用带编译的PreparedStatements
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);//配置最大连接数量***？？


        try {
            dataSource.setFilters("stat");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return dataSource;
    }

    /*
    * 将DataSource数据源配置给数据源事务管理器
    * */
    @Bean(name="transactionManager")
    @Primary
    public DataSourceTransactionManager transactionManager(){
        return new DataSourceTransactionManager(dataSource());
    }

    /*
    * @Qualifier    可能用于自动装配
    * */
    @Bean(name="sqlSessionFactory")
    @Primary
    public SqlSessionFactory dslSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception{
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setTypeAliasesPackage("top.zhanwangweilai.springbootmaster.pojo");//可能配置实体类的别名方便在映射文件中使用
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(DatasourceConfig.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }

    /*
    *  配置druid监控
    * */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean();
        servletRegistrationBean.setServlet(new StatViewServlet());
        servletRegistrationBean.addUrlMappings("/druid/*");//映射的路径（访问druid监控页面的）
        Map<String, String> initParameters = new HashMap<String, String>();
        initParameters.put("loginUsername", "admin");// 用户名
        initParameters.put("loginPassword", "admin");// 密码
        initParameters.put("resetEnable", "false");// 禁用HTML页面上的“Reset All”功能
        initParameters.put("allow", ""); // IP白名单 (没有配置或者为空，则允许所有访问)
        //initParameters.put("deny", "192.168.20.38");// IP黑名单 (存在共同时，deny优先于allow)
        servletRegistrationBean.setInitParameters(initParameters);
        return servletRegistrationBean;
    }

    /*
    *  配置druid监控的路径和忽略的路径
    * */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*"); //配置监控的路径
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");//配置忽略的路径
        return filterRegistrationBean;
    }
}
