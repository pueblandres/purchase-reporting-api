package com.example.purchasereportingapi.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.purchasereportingapi.mapper")
public class MyBatisConfig {
}
