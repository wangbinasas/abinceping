package com.abin.app.micro_app.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.LocalDateTimeTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * 数据库配置
 */
@Slf4j
@Configuration
@MapperScan(basePackages = {
        "com.abin.app.micro_app.proxy.impl.db.mapper"
}, sqlSessionFactoryRef = "sqlSessionFactory")
public class MybatisConfig {

    public static final String MAPPER_PATH = "mapper/com/abin/mapper/*Mapper.xml";

    @Primary
    @Bean(initMethod = "init")
    public DruidDataSource dataSource() {
        DruidDataSource dataSource = DruidDataSourceBuilder.create().build();
        dataSource.setUrl("jdbc:mysql://39.102.214.160:3306/ai_img");
        dataSource.setUsername("wangbinasas");
        dataSource.setPassword("love0615oO!");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager polarTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        // 注册LocalDateTime转换typeHandler
        sessionFactoryBean.setTypeHandlers(new LocalDateTimeTypeHandler());
        sessionFactoryBean.setPlugins(mybatisPlusInterceptor());
        Resource[] resources = null;
        try {
            resources = new PathMatchingResourcePatternResolver().getResources(MAPPER_PATH);
            sessionFactoryBean.setMapperLocations(resources);
        } catch (Exception ex) {
            log.warn("{} 下无Mapper.xml文件", MAPPER_PATH);
        }

        //打印sql语句
        MybatisConfiguration configuration = new MybatisConfiguration();
        configuration.setLogImpl(StdOutImpl.class);
//        sessionFactoryBean.setConfiguration(configuration);

        return sessionFactoryBean.getObject();
    }

    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor plugin = new MybatisPlusInterceptor();
        plugin.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return plugin;
    }
}
