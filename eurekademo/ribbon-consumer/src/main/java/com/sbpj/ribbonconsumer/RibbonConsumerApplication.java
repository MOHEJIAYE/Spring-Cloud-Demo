package com.sbpj.ribbonconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @EnableDiscoveryClient注解，注册为Eureka客户端应用，以获得发现服务的能力
 */
@EnableDiscoveryClient
@SpringBootApplication
public class RibbonConsumerApplication {

	/**
	 * 注入RestTemplate：向服务提供者发送rest请求,从而获取服务
	 * LoadBalanced注解：开启轮询机制，客户端负载均衡
	 * @return
	 */
	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(RibbonConsumerApplication.class, args);
	}
}
