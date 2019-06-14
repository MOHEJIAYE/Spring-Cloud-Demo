package com.sbpj.eurekaclient;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;

import com.sbpj.commons.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @EnableDiscoveryClient注解，激活Eureka中的DiscoveryClient实现
 * （自动化配置，创建DiscoveryClient接口针对Eureka客户端的EurekaDiscoveryClient实例），
 * 才能实现服务信息的输出
 */
@RestController
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaClientApplication {

    // log4j2 org.apache.logging.log4j.Logger
    // private static Logger logger = LogManager.getLogger(EurekaClientApplication.class);

    // log4j org.apache.log4j.Logger
    private final Logger logger = Logger.getLogger(getClass());

    @Autowired
    private Registration registration;  //服务注册

    @Autowired
    private DiscoveryClient client;	//服务发现客户端

    public static void main(String[] args) {
		SpringApplication.run(EurekaClientApplication.class, args);
	}

    /**
     * 服务发现与消费
     * @return
     */
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	public String test() {
	    // 过期方法
        // ServiceInstance instance = client.getLocalServiceInstance();
        // logger.info("/test, host:" + instance.getHost() + ", service_id:" + instance.getServiceId());

        ServiceInstance instance = serviceInstance();
        String result = "/test, host:" + instance.getHost() + ", service_id:" + instance.getServiceId();
        logger.info(result);

        return "From Service-A, " + result;
	}

    /**
     * GET请求
     * @param pjName
     * @return String
     */
    @RequestMapping(value = "/testEx", method = RequestMethod.GET)
    public String testEx(@RequestParam String pjName) {
        ServiceInstance instance = serviceInstance();
        String result = "/testEx, host:" + instance.getHost() + ", service_id:" + instance.getServiceId() + ", port:" + instance.getPort();
        logger.info(result);

        return "From " + pjName + "：" + result;
    }


    /**
     * GET请求
     * @param pjName
     * @return 自定义类型User
     */
    @RequestMapping(value = "/testUser", method = RequestMethod.GET)
    public User testUser(@RequestParam String pjName) {
        User user = new User();
        ServiceInstance instance = serviceInstance();

        user.setUserName("From " + pjName);
        user.setHost(instance.getHost());
        user.setServiceId(instance.getServiceId());
        user.setPort(instance.getPort());
        return user;
    }

    @RequestMapping(value = "/testPost", method = RequestMethod.POST)
    public User testPost(@RequestBody User user, String pjName) {

        ServiceInstance instance = serviceInstance();
        user.setUserName(user.getUserName() + pjName);
        user.setHost(instance.getHost());
        user.setServiceId(instance.getServiceId());
        user.setPort(instance.getPort());

        return user;
    }

	public ServiceInstance serviceInstance() {
        List<ServiceInstance> list = client.getInstances(registration.getServiceId());
        if (null != list && list.size() > 0) {
            for (ServiceInstance itm : list) {
                if (itm.getPort() == 8080 || itm.getPort() == 8081 || itm.getPort() == 8082)
                    return itm;
            }
        }
        return null;
    }
}
