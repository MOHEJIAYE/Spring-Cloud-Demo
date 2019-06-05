package com.sbpj.ribbonconsumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 通过RestTemplate来实现TEST-SERVICE服务提供的/test接口进行调用
     * 访问的地址是服务名TEST-SERVICE，而不是一个具体的地址
     * @return
     */
    @RequestMapping(value = "/ribbon-consumer", method = RequestMethod.GET)
    public String testConsumer() {
        return restTemplate.getForEntity("http://TEST-SERVICE/test", String.class).getBody();
    }
}
