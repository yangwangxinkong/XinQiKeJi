package top.zhanwangweilai.springbootmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import top.zhanwangweilai.springbootmaster.pojo.AdvertisingImg;
import top.zhanwangweilai.springbootmaster.service.AdvertisingService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class RestFullController {
    @Autowired
    private AdvertisingService advertisingService;

    /*提供所有页面的访问*/
    @RequestMapping("/page/{res}")
    public String restFull(@PathVariable String res){
        System.out.println("page执行了");
        return res;
    }
    @RequestMapping("/")
    public String index(HttpServletRequest request){
        List<AdvertisingImg> types = advertisingService.findByTypes(0);
        request.setAttribute("images",types);
        return "index";
    }
}
