package top.zwwl.management.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import top.zwwl.management.pojo.AdvertisingImg;

import java.util.List;

@Mapper
@Repository
public interface AdvertisingImgMapper {

    int insertAdvertising(AdvertisingImg advertisingImg);

    /*根据图片的类型获取当前类型的所有图片*/
    List<AdvertisingImg> findByTypes(int imgType);
}
