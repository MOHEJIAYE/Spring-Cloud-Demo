package com.sbpj.ribbonconsumerhystrix;

import com.netflix.hystrix.HystrixObservableCommand;
import com.sbpj.commons.User;
import org.springframework.web.client.RestTemplate;
import rx.Observable;
import rx.Subscriber;

public class MyHystrixObservableCommand extends HystrixObservableCommand<User> {

    private RestTemplate restTemplate;
    private String pjName;

    public MyHystrixObservableCommand(Setter setter, RestTemplate restTemplate, String pjName) {
        super(setter);
        this.restTemplate = restTemplate;
        this.pjName = pjName;
    }

    @Override
    protected Observable<User> construct() {
        return Observable.create(new Observable.OnSubscribe<User>() {
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
