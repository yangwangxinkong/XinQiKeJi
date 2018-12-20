package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Navigation;
import com.xss.service.NavigationService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Order;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  Controller - 导航
 * @author Huang
 * @date 2018/8/2
 */
@RestController
@Api(description = "导航管理模块")
@RequestMapping("/api/navigation")
public class NavigationController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(NavigationController.class);

    @Autowired
    NavigationService navigationService;

    @ApiOperation(value = "获取导航列表",notes = "获取导航列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取导航列表接口:/api/navigation/list")
    public PageResult<Object> getList(Pageable pageable)throws Exception{
        LOG.debug("get navigation list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            List<Order> orders=new ArrayList<Order>();
            orders.add(Order.asc("order"));
            pageable.setOrders(orders);
            Page<Navigation> navigationPage = navigationService.findPage(pageable);
            result = new PageResult<Object>((int)navigationPage.getTotal(),navigationPage.getPageNumber(),navigationPage.getPageSize(), navigationService.createEntity().convertList(navigationPage.getContent(), new String[]{"position","isBlankTarget","order"}));
        }catch (Exception e){
            LOG.error("get navigation list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get navigation list result = " + result.toString());
        return result;
    }

    @ApiOperation(value = "获取导航详情",notes = "获取导航详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取获取导航详情接口:/api/navigation/info")
    public PublicResult<JSONObject> getInfo(@RequestParam Long id){
        LOG.debug("id = "+id);
        try{
            Navigation navigation = navigationService.find(id);
            JSONObject jo = JsonUtil.toJSONObject(navigation,new String[]{"id","name","url","isBlankTarget",});
            jo.put("position",navigation.getPosition().getValue());
            jo.put("order",navigation.getOrder());
            return new PublicResult<>(PublicResultConstant.SUCCESS,jo);
        }catch (Exception e){
           return new PublicResult<>(PublicResultConstant.DATA_ERROR,null);
        }
    }

    @ApiOperation(value = "获取位置列表",notes = "获取位置列表，不需要传参",httpMethod = "GET",produces = "application/json")
    @GetMapping("/positionList")
    @Log(description="平台中心获取位置列表接口:/api/navigation/positionList")
    public PublicResult<JSONArray> positionList()throws Exception{
        net.sf.json.JSONObject jo = new net.sf.json.JSONObject();
        JSONArray ja = new JSONArray();
        for (Navigation.Position position :  Navigation.Position.values()){
            jo.accumulate("name",position.getDesc());
            jo.accumulate("value",position.getValue());
            ja.add(jo);
            jo.clear();
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,ja);
    }

    @ApiOperation(value = "批量删除导航",notes = "通过id批量删除导航",httpMethod = "GET")
    @GetMapping("/delete")
    @Log(description="平台中心删除导航接口:/api/navigation/deleteById")
    public PublicResult<Boolean> deleteById(@RequestParam Long[] ids)throws Exception{
        LOG.info("delete navigation param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            navigationService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete navigation error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete navigation result = " + result.toString());
        return result;
    }

    @ApiOperation(value = "保存/更新导航",notes = "保存/更新导航",httpMethod = "POST")
    @PostMapping("/save")
    @Log(description="平台中心保存/更新导航接口:/api/navigation/save")
    public PublicResult<Boolean> save(@RequestBody Navigation navigation)throws Exception{
        LOG.debug("navigation = " + navigation.toString());
        if (null != navigation && null != navigation.getId()){
            navigationService.update(navigation,null);
        }else {
            navigationService.save(navigation);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

}
