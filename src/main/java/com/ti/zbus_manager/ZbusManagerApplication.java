package com.ti.zbus_manager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@MapperScan(basePackages = "com.ti.zbus_manager.mapper")
@SpringBootApplication
@EnableTransactionManagement
public class ZbusManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ZbusManagerApplication.class, args);
    }
}
