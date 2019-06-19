package com.sbpj.ribbonconsumer;


import com.sbpj.commons.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ConsumerController {

    @Autowired
    RestTemplate restTemplate;

    /**
     * 通过RestTemplate来实现TEST-SERVICE服务提供的/testEx接口进行调用
     * 访问的地址是服务名TEST-SERVICE，而不是一个具体的地址
     * @return
     */
    @RequestMapping(value = "/ribbon-consumer-str", method = RequestMethod.GET)
    public String testConsumerStr() {
//        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://TEST-SERVICE/test", String.class).getBody();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://TEST-SERVICE/testEx?pjName={1}", String.class, "sbpj");

        String body = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();
        int statusCodeValue = responseEntity.getStatusCodeValue();
        HttpHeaders headers = responseEntity.getHeaders();

        StringBuffer result = new StringBuffer();
        result.append("Body: ").append(body).append("<hr>")
                .append("StatusCode: ").append(statusCode).append("<hr>")
                .append("StatusCodeValue: ").append(statusCodeValue).append("<hr>")
                .append("Headers: ").append(headers).append("<hr>");

        return result.toString();
    }

    @RequestMapping(value = "ribbon-consumer-user", method = RequestMethod.GET)
    public String testConsumerUser() {
        ResponseEntity<User> responseEntity = restTemplate.getForEntity("http://TEST-SERVICE/testUser?pjName={1}", User.class, "sbpj");

        User body = responseEntity.getBody();
        HttpStatus statusCode = responseEntity.getStatusCode();
        int statusCodeValue = responseEntity.getStatusCodeValue();
        HttpHeaders headers = responseEntity.getHeaders();

        String res = body.getUserName() + ", host: " + body.getHost() + ", service_id: " + body.getServiceId() + ", port: " + body.getPort();

        StringBuffer result = new StringBuffer();
        result.append("Body: ").append(res).append("<hr>")
                .append("StatusCode: ").append(statusCode).append("<hr>")
                .append("StatusCodeValue: ").append(statusCodeValue).append("<hr>")
                .append("Headers: ").append(headers).append("<hr>");
        return result.toString();
    }

    @RequestMapping(value = "ribbon-consumer-map", method = RequestMethod.GET)
    public String testConsumerMap() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "sbpj");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity("http://TEST-SERVICE/testEx?pjName={name}", String.class, params);
        String body = responseEntity.getBody();
        return "map -> " + body;
    }

    @RequestMapping(value = "ribbon-consumer-url", method = RequestMethod.GET)
    public String testConsumerUrl() {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://TEST-SERVICE/testEx?pjName={name}")
                .build()
                .expand("sbpj")
                .encode();
        URI uri = uriComponents.toUri();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uri, String.class);
        String body = responseEntity.getBody();
        return "url -> " + body;
    }

    @RequestMapping(value = "/ribbon-consumer-strobj", method = RequestMethod.GET)
    public String testConsumerStrObj() {
        String result = restTemplate.getForObject("http://TEST-SERVICE/testEx?pjName={1}", String.class, "sbpj");
        return "strobj -> " + result;
    }

    @RequestMapping(value = "ribbon-consumer-userobj", method = RequestMethod.GET)
    public String testConsumerUserObj() {
        User body = restTemplate.getForObject("http://TEST-SERVICE/testUser?pjName={1}", User.class, "sbpj");

        String result = body.getUserName() + ", host: " + body.getHost() + ", service_id: " + body.getServiceId() + ", port: " + body.getPort();
        return result;
    }

    @RequestMapping(value = "ribbon-consumer-mapobj", method = RequestMethod.GET)
    public String testConsumerMapObj() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "sbpj");
        String result = restTemplate.getForObject("http://TEST-SERVICE/testEx?pjName={name}", String.class, params);
        return "mapobj -> " + result;
    }

    @RequestMapping(value = "ribbon-consumer-urlobj", method = RequestMethod.GET)
    public String testConsumerUrlObj() {
        UriComponents uriComponents = UriComponentsBuilder.fromUriString(
                "http://TEST-SERVICE/testEx?pjName={name}")
                .build()
                .expand("sbpj")
                .encode();
        URI uri = uriComponents.toUri();
        String result = restTemplate.getForObject(uri, String.class);
        return "urlobj -> " + result;
    }

    @RequestMapping(value = "/ribbon-consumer-post", method = RequestMethod.GET)
    public String testConsumerPost() {
        //request：普通对象
//        User user = new User();
//        user.setUserName("From");
//        ResponseEntity<User> responseEntity = restTemplate.postForEntity("http://TEST-SERVICE/testPost?pjName={1}", user, User.class, "sbpj");
//        User body = responseEntity.getBody();
//
//        String result = body.getUserName() + ", host: " + body.getHost() + ", service_id: " + body.getServiceId() + ", port: " + body.getPort();

        //request:HttpEntity对象
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, Object> parammap = new LinkedMultiValueMap<>();
        parammap.add("userName", "From");
        HttpEntity<Map> entity = new HttpEntity<>(parammap, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity("http://TEST-SERVICE/testPostEx?pjName={1}", entity, String.class, "sbpj");
        String result = responseEntity.getBody();

        return "post -> " + result;
    }

    @RequestMapping(value = "/ribbon-consumer-put", method = RequestMethod.GET)
    public String testConsumerPut() {
        User user = new User();
        user.setUserName("From");
        restTemplate.put("http://TEST-SERVICE/testPut?pjName={1}", user, "sbpj");

        return "put -> void";
    }

    @RequestMapping(value = "/ribbon-consumer-delete", method = RequestMethod.GET)
    public String testConsumerDelete() {
        restTemplate.delete("http://TEST-SERVICE/testDelete?pjName={1}", "sbpj");

        return "delete -> void";
    }
}
