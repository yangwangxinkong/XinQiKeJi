package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.PayBaseDao;
import com.xss.domain.City;
import com.xss.domain.ResidenceType;
import com.xss.service.CityService;
import com.xss.service.PayBaseService;
import com.xss.service.ResidenceTypeService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 *  社保公积金基数管理接口
 * @author ct
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/payBase")
@Api(description="社保公积金基数管理")
public class PayBaseController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(PayBaseController.class);

    @Resource
    private PayBaseDao payBaseDao;
    @Autowired
    private PayBaseService payBaseService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="社保公积金基数管理列表", notes="分页展示社保公积金基数管理列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取社保公积金基数管理列表接口:/m/payBase/list")
    public PageResult<Object> list(Pageable pageable) {
        LOG.debug("get city list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            //Page<City> data = cityService.findPage(pageable);
            //result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), cityService.createEntity().convertList(data.getContent(), new String[]{"area"}));
        }catch (Exception e){
            LOG.error("get city list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get city list result = " + result.toString());
        return result;
    }

}
