package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.config.ConfigInit;
import com.xss.domain.City;
import com.xss.domain.Configs;
import com.xss.service.CityService;
import com.xss.service.ConfigsService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  系统配置接口
 * @author ct
 * @since 2018-08-14
 */
@RestController
@RequestMapping("/m/config")
@Api(description="城市管理模块")
public class ConfigController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ConfigController.class);

    @Autowired
    private ConfigsService configsService;

    /**
     * 系统配置详情
     */
    @GetMapping("/info")
    @ApiOperation(value="系统配置详情", notes="根据code获取系统配置详情",produces = "application/json")
    @Log(description="根据code获取系统配置详情接口:/m/config/info")
    public PublicResult<Object> info(@RequestParam String code) throws Exception{
        LOG.debug("get config info param = " + code);
        try {
            if (ConfigInit.SYSTEM_CONFIGS.isEmpty()) {
                List<Configs> configs = configsService.findAll();
                for (Configs config : configs) {
                    ConfigInit.SYSTEM_CONFIGS.put(config.getCode(), config);
                }
            }
            Object obj = ConfigInit.SYSTEM_CONFIGS.get(code);
            return new PublicResult<Object>(PublicResultConstant.SUCCESS, obj);
        }catch (Exception e) {
            e.printStackTrace();
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }

    /**
     * 系统配置详情列表
     */
    @GetMapping("/list")
    @ApiOperation(value="系统配置详情列表", notes="根据code获取系统配置详情列表",produces = "application/json")
    @Log(description="根据code获取系统配置详情列表接口:/m/config/list")
    public PublicResult<Object> list(@RequestParam String[] codes) throws Exception{
        LOG.debug("get config info param = " + codes.toString());
        try {
            if (ConfigInit.SYSTEM_CONFIGS.isEmpty()) {
                List<Configs> configs = configsService.findAll();
                for (Configs config : configs) {
                    ConfigInit.SYSTEM_CONFIGS.put(config.getCode(), config);
                }
            }
            JSONObject jo = new JSONObject();
            for(String code : codes){
                Object obj = ConfigInit.SYSTEM_CONFIGS.get(code);
                if (null != obj){
                    jo.put(code, obj);
                }
            }

            return new PublicResult<Object>(PublicResultConstant.SUCCESS, jo);
        }catch (Exception e) {
            e.printStackTrace();
            return new PublicResult<>(PublicResultConstant.ERROR, null);
        }
    }
}
