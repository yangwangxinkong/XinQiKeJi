package top.zwwl.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class EchoController {
    @RequestMapping("/index3")
    public String test(HttpServletRequest request){
        //逻辑处理
        request.setAttribute("key", "springboot 启用thymeleaf成功测试");
        return "index3";
    }
}
