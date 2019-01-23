package top.zwwl.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.zwwl.management.dao.AdvertisingImgMapper;
import top.zwwl.management.pojo.AdvertisingImg;

import java.util.List;

@Service
public class AdvertisingService {
    @Autowired
    private AdvertisingImgMapper advertisingImgMapper;


    /*
    * 根据图片类型获取图片
    * */
    public List<AdvertisingImg> findByTypes(Integer type){
        List<AdvertisingImg> types = null;
        try {
            types = advertisingImgMapper.findByTypes(type);
            return types;
        }catch (Exception e){
            e.printStackTrace();
        }
        return types;

    }

}
