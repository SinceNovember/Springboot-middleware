package com.simple.interfaces;

import com.simple.HelloService;
import com.simple.domain.Hi;
import org.springframework.stereotype.Controller;

@Controller("helloService")
public class HelloServiceImpl implements HelloService {
    @Override
    public String hi() {
        System.out.println("hello");
        return "hi bugstack rpc";
    }

    @Override
    public String say(String str) {
        return str;
    }

    @Override
    public String sayHi(Hi hi) {
        return hi.getUserName() + " say：" + hi.getSayMsg();
    }

}
