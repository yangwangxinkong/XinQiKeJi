package top.zwwl.management.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RestFullController {

    /*提供所有页面的访问*/
    @RequestMapping("/page/{res}")
    public String restFull(@PathVariable String res){
        System.out.println("page执行了");
        return res;
    }
    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
