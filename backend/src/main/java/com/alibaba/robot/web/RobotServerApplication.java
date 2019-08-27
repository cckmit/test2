package com.alibaba.robot.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import com.alibaba.robot.mqtt.SpringUtil;

@SpringBootApplication
@Import(value={SpringUtil.class})
@MapperScan("com.alibaba.robot.web.manage.mapper")
public class RobotServerApplication {

	public static void main(String[] args) {
		SpringApplication springApplication = new SpringApplication(RobotServerApplication.class);
		springApplication.setBannerMode(Mode.OFF);
		springApplication.run(args);
	}
}
