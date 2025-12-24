package com.football;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 足球联赛数据管理系统启动类
 */
@SpringBootApplication
@MapperScan("com.football.mapper")
public class FootballLeagueApplication {

    public static void main(String[] args) {
        SpringApplication.run(FootballLeagueApplication.class, args);
        System.out.println("=========================================");
        System.out.println("足球联赛数据管理系统启动成功！");
        System.out.println("后端访问地址: http://localhost:8080");
        System.out.println("前端访问地址: http://localhost:3000");
        System.out.println("=========================================");
    }
}

