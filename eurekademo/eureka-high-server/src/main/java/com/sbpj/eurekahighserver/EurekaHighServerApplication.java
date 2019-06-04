package com.sbpj.eurekahighserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @EnableEurekaServer注解，启动一个服务注册中心提供给其他应用进行对话
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaHighServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(EurekaHighServerApplication.class, args);
	}

}
