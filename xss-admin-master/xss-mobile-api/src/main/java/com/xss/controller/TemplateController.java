package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.City;
import com.xss.service.CityService;
import com.xss.service.WeixinService;
import com.xss.util.FreemarkerUtils;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import freemarker.template.Template;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  城市管理接口
 * @author ct
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/template")
@Api(description="城市管理模块")
public class TemplateController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(TemplateController.class);

    @Resource(name = "freeMarkerConfigurer")
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Resource
    private WeixinService weixinService;

    /**
     * 列表
     */
    @GetMapping("/test")
    @ApiOperation(value="城市列表", notes="分页展示城市列表，支持根据名称和网址搜索",produces = "application/json")
    @Log(description="平台中心获取城市列表接口:/api/city/list")
    public PageResult<Object> test(Pageable pageable) {
        PageResult<Object> result = null;
        try{
            System.out.println("111111111111111111111");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("openid", "orMfR0kYji6AtDIKJq-ywU9piLCA");
            map.put("type", "报价订单");
            map.put("phone", "18201077251");
            map.put("address", "成都市天府五街");
            map.put("price", "1674.89");
            System.out.println("222222222222222222");
            Template t = FreemarkerUtils.getTemplate(freeMarkerConfigurer, "newOrder");
            System.out.println("33333333333333333333333");
            String json = FreemarkerUtils.processTemplateIntoString(t, map);
            System.out.println("json-------------"+json);
            weixinService.sendTemplateMessage(json);
            System.out.println("55555555555555555");
        }catch (Exception e){
            LOG.error("get city list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }
}
