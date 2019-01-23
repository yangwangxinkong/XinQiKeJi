package top.zhanwangweilai.springbootmaster.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.zhanwangweilai.springbootmaster.dao.AdvertisingImgMapper;
import top.zhanwangweilai.springbootmaster.pojo.AdvertisingImg;
import top.zhanwangweilai.springbootmaster.service.AdvertisingService;
import top.zhanwangweilai.springbootmaster.vo.PublicRelust;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/img")
public class AdvertisingController {
    @Autowired
    private AdvertisingImgMapper advertisingImgMapper;

    @Autowired
    private AdvertisingService advertisingService;

   /* @RequestMapping("/saveImg")
    public String saveImgAddress(HttpServletRequest request,String imgAddress,Integer imgType){
        try {
            //从session中获取图片的宽高
            HttpSession session = request.getSession();
            Integer width = (Integer)session.getAttribute("width");
            Integer height = (Integer)session.getAttribute("height");

            AdvertisingImg advertisingImg = new AdvertisingImg();
            advertisingImg.setWidth(width);
            advertisingImg.setHeight(height);
            advertisingImg.setImgType(imgType);
            advertisingImg.setImgAddress(imgAddress);
            int test = advertisingImgMapper.insertAdvertising(advertisingImg);

            return test+"";
        }catch (Exception e){
            e.printStackTrace();
        }
        return "失败了！";
    }*/

    /*
    * 根据图片类型获取图片
    * type：  图片类型
    * */
    @RequestMapping("/findByType")
    public PublicRelust findByType(Integer type){
        List<AdvertisingImg> types = null;
        if (type==null) return new PublicRelust(0,"图片类型不能为空");
        try {
            types = advertisingService.findByTypes(type);
            if (types == null){
                return new PublicRelust(200,"该类型图片不存在");
            }else {
                return new PublicRelust(types);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PublicRelust(201,"图片查询失败！");
    }
}
