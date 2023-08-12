package com.lncanswer;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement //开启事务管理
@EnableCaching //开启缓存注解
public class ReggieApplication {
     public static void main(String[] args) {
         log.info("启动程序...");
         SpringApplication.run(ReggieApplication.class,args);
    }
}
