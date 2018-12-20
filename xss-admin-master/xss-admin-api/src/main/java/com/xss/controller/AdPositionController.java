package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.AdPosition;
import com.xss.domain.enums.AdPositionType;
import com.xss.service.AdPositionService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 *  广告位管理接口
 * @author Huang
 * @date 2018/8/9
 */
@RestController
@RequestMapping("/api/adPosition")
@Api(description="广告位管理模块")
public class AdPositionController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(AdPositionController.class);

    @Autowired
    AdPositionService adPositionService;

    @ApiOperation(value = "获取广告位列表",notes = "获取广告位列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取广告位列表接口:/api/adPosition/list")
    public PageResult<Object> list(Pageable pageable)throws Exception{
        LOG.debug("get Adposition list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<AdPosition> adpositionPage = adPositionService.findPage(pageable);
            result = new PageResult<Object>((int)adpositionPage.getTotal(),adpositionPage.getPageNumber(),adpositionPage.getPageSize(), adPositionService.createEntity().convertList(adpositionPage.getContent(),null));
        }catch (Exception e){
            LOG.error("get Article list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

    @ApiOperation(value = "获取广告位详情",notes = "获取广告位详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取广告位详情接口:/api/adPosition/findById")
    public PublicResult<JSONObject>findById(@RequestParam Long id)throws Exception{
        LOG.debug("id = " + id);
        try{
            AdPosition adPosition = adPositionService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,adPositionService.createEntity().convertEntity(adPosition,new String[]{"value"}));
        }catch (Exception e){
            LOG.error("get info  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }

    }

    @ApiOperation(value = "保存/更新广告位",notes = "保存/更新广告位",httpMethod = "POST",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存/更新广告位接口:/api/adPosition/save")
    public PublicResult<String> save(@RequestBody AdPosition adPosition)throws Exception{
        LOG.debug("articleObj ="+adPosition.toString());
        try{
            if (null == adPosition.getId()){
                AdPosition exist = adPositionService.getAdPositionDao().findAdPositionByType(adPosition.getType());
                if (null != exist && null != exist.getId()){
                    return  new PublicResult<String>(PublicResultConstant.ERROR,"该类型已发布广告位");
                }
                adPositionService.save(adPosition);
            }else {
                AdPosition exist = adPositionService.getAdPositionDao().findAdPositionByType(adPosition.getType());
                if (null != exist && null != exist.getId() && !exist.getId().equals(adPosition.getId())){
                    return  new PublicResult<String>(PublicResultConstant.ERROR,"该类型已发布广告位");
                }
                adPositionService.update(adPosition);
            }
            return new PublicResult<String>(PublicResultConstant.SUCCESS,"操作成功");
        }catch (Exception e){
            LOG.error("save/update  error. {}", e);
            return  new PublicResult<String>(PublicResultConstant.ERROR,"操作失败");
        }
    }

    @ApiOperation(value = "批量删除广告位",notes = "批量删除广告位",httpMethod = "GET",produces = "application/json")
    @GetMapping("/delete")
    @Log(description="平台中心批量删除广告位接口:/api/adPosition/delete")
    public PublicResult<Boolean> delete(@RequestParam Long[] ids)throws Exception{
        adPositionService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

    @ApiOperation(value = "获取广告位类型枚举初始化数据",notes = "获取广告位类型枚举初始化数据",httpMethod = "GET",produces = "application/json")
    @GetMapping("/getTypes")
    @Log(description="获取广告位类型枚举初始化数据接口:/api/adPosition/getTypes")
    public PublicResult<JSONArray> getTypes()throws Exception{
        JSONArray ja = new JSONArray();
        for(AdPositionType adpositionType : AdPositionType.values()){
            JSONObject jo = new JSONObject();
            jo.put("name", adpositionType.name());
            jo.put("value", adpositionType.getValue());
            jo.put("desc", adpositionType.getDesc());
            ja.add(jo);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, ja);
    }

}
