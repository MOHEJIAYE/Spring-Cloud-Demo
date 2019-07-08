package com.sbpj.ribbonconsumerhystrix;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {
    @Autowired
    RestTemplate restTemplate;

    @Autowired
    TestService testService;

    /**
     * 断路器Hytrix
     * @returns
     */
    @RequestMapping(value = "ribbon-consumer-hystrix", method = RequestMethod.GET)
    public String testConsumerHystrix() {
        return testService.testService();
    }
}
