package com.hwua.config;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class C3P0Config {
    @Bean
    @ConfigurationProperties(prefix = "jdbc.datasource")
    /*获取一个数据源*/
    public DataSource dataSource(){
        return new ComboPooledDataSource();
    }
}
