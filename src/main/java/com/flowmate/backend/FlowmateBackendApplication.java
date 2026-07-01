package com.flowmate.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.flowmate.backend.config.CookieProperties;
import com.flowmate.backend.config.JwtProperties;

@SpringBootApplication
@MapperScan("com.flowmate.backend.**.mapper")
@EnableConfigurationProperties({ JwtProperties.class, CookieProperties.class })
public class FlowmateBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(FlowmateBackendApplication.class, args);
	}

}
