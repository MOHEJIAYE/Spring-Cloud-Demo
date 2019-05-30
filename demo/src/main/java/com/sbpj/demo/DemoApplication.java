package com.sbpj.demo;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class DemoApplication {

//	@Autowired
//	private CounterService counterService;
	/**
	 * 主函数，启动类
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/demo1")
	public String demo1()
	{
		return "Hello battcn";
	}

//	@Bean
//    MeterRegistryCustomizer<MeterRegistry> configurer(
//            @Value("${spring.application.name}") String applicationName) {
//	    return (registry) -> registry.config().commonTags("application", applicationName);
//    }

//	@GetMapping("/count")
//	public String count() {
//		countService.increment("sbpj.demo.count");
//		return "";
//	}
//	@Bean
//	public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
//		// 目的是
//		return args -> {
//			System.out.println("来看看 SpringBoot 默认为我们提供的 Bean：");
//			String[] beanNames = ctx.getBeanDefinitionNames();
//			Arrays.sort(beanNames);
//			Arrays.stream(beanNames).forEach(System.out::println);
//		};
//	}
}
