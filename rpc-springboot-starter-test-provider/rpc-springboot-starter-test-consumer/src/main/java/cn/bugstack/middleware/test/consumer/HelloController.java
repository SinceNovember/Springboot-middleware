package cn.bugstack.middleware.test.consumer;

import com.simple.HelloService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/api")
public class HelloController {

    @Resource
    private HelloService helloService;

    @GetMapping(value = "hi")
    public String hi() {
        return helloService.hi();
    }

}
