package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.annotation.ParamXssPass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Ad;
import com.xss.domain.AdPosition;
import com.xss.service.*;
import com.xss.util.JsonUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 *  广告管理接口
 * @author Administrator
 * @date 2018/8/9
 */
@RestController
@RequestMapping("/api/ad")
@Api(description="广告管理模块")
public class AdController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(AdController.class);

    @Autowired
    AdService adService;
    @Autowired
    AdPositionService adPositionService;
    @Autowired
    ArticleService articleService;

    @ApiOperation(value = "获取广告列表",notes = "获取广告列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取广告列表接口:/api/ad/list")
    public PageResult<Object> list(Pageable pageable)throws Exception{
        LOG.debug("get Ad list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Ad> adPage = adService.findPage(pageable);
            result = new PageResult<Object>((int)adPage.getTotal(),adPage.getPageNumber(),adPage.getPageSize(), adService.createEntity().convertList(adPage.getContent(),new String[]{"adPosition","type","beginDate","endDate","order"}));
        }catch (Exception e){
            LOG.error("get Ad list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

    @GetMapping("/info")
    @ApiOperation(value="获取广告详情", notes="根据id获取获取广告详情",produces = "application/json")
    @Log(description="平台中心获取广告详情接口:/api/ad/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("id = " + id);
        try{
            Ad ad = adService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,adService.createEntity().convertEntity(ad,new String[]{"intType","adPosition","product","store","order","article","beginDate","endDate"}));
        }catch (Exception e){
            LOG.error("get info  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    @ApiOperation(value = "保存/更新广告",notes = "保存/更新广告",httpMethod = "POST",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存/更新广告接口:/api/ad/save")
    @ParamXssPass
    public PublicResult<Boolean> save(@RequestBody JSONObject adObj){
        LOG.debug("ad = " + adObj.toString());
        Ad ad = new Ad();
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            ad.setTitle(adObj.getString("title"));
            ad.setAdPosition(adPositionService.find(Long.parseLong(adObj.getString("adPosition"))));
            ad.setOrder(adObj.getInteger("order"));
            ad.setType(Ad.Type.valueOf(adObj.getString("type")));
            ad.setContent(adObj.getString("content"));
            ad.setPath(adObj.getString("path"));
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(adObj.getString("beginDate"))){
                ad.setBeginDate(format.parse(format.format(Long.parseLong(adObj.getString("beginDate")))));
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(adObj.getString("beginDate")) && org.apache.commons.lang3.StringUtils.isNotEmpty(adObj.getString("endDate"))){
                ad.setEndDate(format.parse(format.format(Long.parseLong(adObj.getString("endDate")))));
            }
            ad.setUrl(adObj.getString("url"));
            ad.setVideoImage(adObj.getString("videoImage"));
            if (org.apache.commons.lang.StringUtils.isNotEmpty(adObj.getString("id"))){
                ad.setId(Long.parseLong(adObj.getString("id")));
                adService.update(ad);
            }else {
                adService.save(ad);
            }
            return new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("save ad  error. {}", e);
            return new PublicResult<Boolean>(PublicResultConstant.ERROR,false);
        }
    }


    @ApiOperation(value = "批量删除广告",notes = "批量删除广告",httpMethod = "GET",produces = "application/json")
    @GetMapping("/delete")
    @Log(description="平台中心批量删除广告接口:/api/ad/delete")
    public PublicResult<Boolean> delete(@RequestParam Long[] ids)throws Exception{
        LOG.debug("ids[] = " + ids);
        adService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }


    @ApiOperation(value = "获取广告类型列表",notes = "获取广告类型列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/typeList")
    @Log(description="平台中心获取广告类型列表接口:/api/ad/typeList")
    public PublicResult<JSONArray> typeList()throws Exception{
        JSONArray ja = new JSONArray();
        Ad.Type[] types = Ad.Type.values();
        for (Ad.Type type : types){
            JSONObject jo = new JSONObject();
            jo.put("name",type.getDesc());
            jo.put("eName",type.toString());
            jo.put("value",type.getValue());
            ja.add(jo);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,ja);
    }

    @ApiOperation(value = "获取广告位下拉列表",notes = "获取广告位下拉列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/adPositionList")
    @Log(description="平台中心获取广告位下拉列表接口:/api/ad/adpositionList")
    public PublicResult<JSONArray> adPositionList()throws Exception{
        JSONArray ja = new JSONArray();
        try{
            List<AdPosition> adPositions = adPositionService.findAll();
            for (AdPosition adPosition : adPositions){
                JSONObject jo = JsonUtil.toJSONObject(adPosition,new String[]{"id"});
                jo.put("label",adPosition.getName()+"["+adPosition.getWidth()+"×"+adPosition.getHeight()+"]");
                ja.add(jo);
            }
            return new PublicResult<>(PublicResultConstant.SUCCESS,ja);
        }catch (Exception e){
            LOG.error("get adPositionList list error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }
}