package com.sbpj.ribbonconsumerhystrix;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.sbpj.commons.User;
import org.springframework.web.client.RestTemplate;
import rx.Observable;

import java.util.concurrent.Future;

public class MyHystrixCommand extends HystrixCommand<User> {

//    public MyHystrixCommand(HystrixCommandGroupKey groupKey) {
//        super(groupKey);
//    }
//
//    @Override
//    protected Object run() throws Exception {
//        return null;
//    }

    private RestTemplate restTemplate;
    private String pjName;

    public MyHystrixCommand(Setter setter, RestTemplate restTemplate, String pjName) {
        super(setter);
        this.restTemplate = restTemplate;
        this.pjName = pjName;
    }

    @Override
    protected User run() {
        return restTemplate.getForObject("http://TEST-SERVICE/testUser?pjName={1}", User.class, pjName);
    }

    /**
     * 重载getFallback()方法实现降级处理逻辑
     * @return
     */
    @Override
    protected User getFallback() {
        return new User();
    }

    public static void main(String[] args) {

        // 同步执行
        User u = new MyHystrixCommand(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(""))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                new RestTemplate(), "sbpj").execute();

        // 异步执行
        Future<User> futureUser = new MyHystrixCommand(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey(""))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(5000)),
                new RestTemplate(), "sbpj").queue();

        // observe()，Hot Observable
        // 命令在observe（）调用的时候立即执行，当Observable每次被订阅的时候会重放它的行为
        Observable<User> ho = new MyHystrixCommand(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")), new RestTemplate(), "sbpj").observe();

        // toObservable()，Cold Observable
        // toObservable（）执行之后，命令不会立即执行，只有当所有订阅者都订阅它之后才会执行
        Observable<User> co = new MyHystrixCommand(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("")), new RestTemplate(), "sbpj").toObservable();

    }
}
