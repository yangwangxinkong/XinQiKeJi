package top.zhanwangweilai.springbootmaster.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RestFullController {
    @RequestMapping("/page/{res}")
    public String restFull(@PathVariable String res){
        System.out.println("============================restFull===================================");
        return res;
    }
    @RequestMapping("/index")
    public String index(){
        System.out.println("主页面访问");
        return "index";
    }
}
