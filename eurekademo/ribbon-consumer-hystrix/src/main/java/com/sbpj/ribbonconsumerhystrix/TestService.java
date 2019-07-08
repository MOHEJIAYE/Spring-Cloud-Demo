package com.sbpj.ribbonconsumerhystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.ObservableExecutionMode;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;
import com.sbpj.commons.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

import java.util.concurrent.Future;

@Service
public class TestService {
    @Autowired
    RestTemplate restTemplate;

    private final Logger logger = Logger.getLogger(getClass());

    @HystrixCommand(fallbackMethod = "testFallback", commandKey = "testKey")
    public String testService() {
        long start = System.currentTimeMillis();

        // 消费服务的逻辑
        String result = restTemplate.getForEntity("http://TEST-SERVICE/testHystrix?pjName={1}", String.class,"sbpj").getBody();

        long end = System.currentTimeMillis();

        logger.info("Spend time: " + (end-start));

        return result;
    }

    public String testFallback() {
        return "error";
    }

    @HystrixCommand(fallbackMethod = "fallback1")
    public String getUserById() throws Exception {
        throw new RuntimeException("getUserById command failed");
    }

    /**
     * 在fallback实现方法的参数中增加Throwable e对象，获取触发服务降级的具体异常内容
     * @param e
     * @return
     */
    public String fallback1(Throwable e) {
        return e.getMessage();
    }

    /**
     * 使用HystrixCommand注解，同步执行
     * **********************************************************
     * 使用fallbackMethod参数来指定具体的服务降级实现方法
     * 具体的Hystrix命令与fallback实现函数定义在同一个类中
     * fallbackMethod的值必须与实现fallback方法的名字相同
     * **********************************************************
     * 设置ignoreExceptions参数，抛出异常时不会触发降级（defaultUser）
     * @param pjName
     * @return
     */
    @HystrixCommand(ignoreExceptions = {NullPointerException.class, ArithmeticException.class}, fallbackMethod = "defaultUser")
    public User getUserByPjName(String pjName) {
        return restTemplate.getForObject("http://TEST-SERVICE/testUser?pjName={1}", User.class, pjName);
    }

    @HystrixCommand(fallbackMethod = "defaultUserSec")
    public User defaultUser() {
        User user = new User();
        user.setUserName("First Fallback");
        return user;
    }

    public User defaultUserSec() {
        User user = new User();
        user.setUserName("Second Fallback");
        return user;
    }

    /**
     * 使用HystrixCommand注解，异步执行
     * @param pjName
     * @return
     */
    @HystrixCommand
    public Future<User> getUserByPjNameAsync(final String pjName) {
        return new AsyncResult<User>() {
            public User invoke() {
                return restTemplate.getForObject("http://TEST-SERVICE/testUser?pjName={1}", User.class, pjName);
            }
        };
    }

    /**
     * 使用HystrixCommand注解，construct()实现
     * observableExecutionMode控制使用observe()还是toObservable()
     * 默认EAGER：使用observe()执行方式
     * LAZY：toObservable()执行方式
     * @param pjName
     * @return
     */
    @HystrixCommand(observableExecutionMode = ObservableExecutionMode.EAGER)
    public Observable<User> getUserByPjNameEx(final String pjName) {
        return Observable.create(new Observable.OnSubscribe<User>() {
            @Override
            public void call(Subscriber<? super User> observer) {
                try {
                    if(!observer.isUnsubscribed()) {
                        User user = restTemplate.getForObject("http://TEST-SERVICE/testUser?pjName={1}", User.class, pjName);
                        observer.onNext(user);
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }
}
