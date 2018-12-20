package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Area;
import com.xss.service.AreaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 *  地区管理接口
 * @author fan.hu
 * @since 2018-08-07
 */
@RestController
@RequestMapping("/api/area")
@Api(description="地区管理模块")
public class AreaController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(AreaController.class);

    @Autowired
    private AreaService areaService;

    @ApiOperation(value="地区列表", notes="按层级获取地区列表",produces = "application/json")
    @GetMapping("/children")
    @Log(description="平台中心获取地区列表接口:/api/area/children")
    public PublicResult<JSONObject> list(@RequestParam(required = false) Long parentId) {
        LOG.info("get area list param : parentId = " + parentId);
        try {
            Area parent =areaService.find(parentId);
            JSONArray areaJa = new JSONArray();
            JSONObject jo = new JSONObject();
            if (parent != null) {
                jo.put("parentId",parentId);
                jo.put("parent_parentId",parent.getParent()==null?null:parent.getParent().getId());
                jo.put("parentName",parent.getName());
                List<Area> list = areaService.findChildren(parent);
                areaJa = areaService.createEntity().convertList(list,Area.DEFAULT_JSON_PARAMS);
            } else {
                List<Area> list = areaService.findRoots();
                areaJa = areaService.createEntity().convertList(list,Area.DEFAULT_JSON_PARAMS);
            }
            jo.put("areas",areaJa);
            return new PublicResult<>(PublicResultConstant.SUCCESS, jo);
        } catch (Exception e) {
            LOG.error("get area list error. " + e);
            return new PublicResult<>(PublicResultConstant.FAILED, new JSONObject());
        }
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存地区", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaJson", value = "{\"parent\":\"1\",\"name\":\"北京\",\"order\":\"1\"}"
                    , required = true, dataType = "String",paramType="body")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public PublicResult<Boolean> save(@RequestBody JSONObject areaJson) {
        LOG.info("save area param : areaJson = " + areaJson);
        try {
            Area area = new Area();
            Long parentId = areaJson.getLong("parent");
            area.setParent(areaService.find(parentId));
            area.setName(areaJson.getString("name"));
            area.setOrder(areaJson.getInteger("order"));
            area.setFullName(null);
            area.setTreePath(null);
            area.setChildren(null);
            areaService.save(area);
        } catch (Exception e) {
            LOG.error("save area error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 更新
     */
    @ApiOperation(value="更新地区", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaJson", value = "{\"id\":\"1\",\"name\":\"北京\",\"order\":\"1\"}"
                    , required = true, dataType = "String",paramType="body")
    })
    @PostMapping("/update")
    public PublicResult<Boolean> update(@RequestBody JSONObject areaJson) {
        LOG.info("update area param : areaJson = " + areaJson);
        try {
            Area area = new Area();
            area.setId(areaJson.getLong("id"));
            area.setName(areaJson.getString("name"));
            area.setOrder(areaJson.getInteger("order"));
            areaService.update(area, "fullName", "treePath", "parent", "children", "members", "receivers", "orders", "deliveryCenters");
        } catch (Exception e) {
            LOG.error("update area error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 删除
     */
    @ApiOperation(value="删除地区", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "地区ID", required = true, dataType = "Long",paramType="query")
    })
    @PostMapping("/delete")
    public PublicResult<Boolean> delete(@RequestParam Long id) {
        LOG.info("delete area param : id = " + id);
        try {
            areaService.delete(id);
        } catch (Exception e) {
            LOG.error("delete area error. " + e);
            return new PublicResult<Boolean>("该地区已被引用，暂时无法删除。", false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    @ApiOperation(value="地区下拉选项列表", notes="按层级获取地区下拉选项列表",produces = "application/json")
    @GetMapping("/areaOptions")
    @Log(description="按层级获取地区下拉选项列表:/api/area/areaOption")
    public PublicResult<JSONObject> areaOptions(@RequestParam(required = false) Long parentId) {
        LOG.info("get areaOptions param : parentId = " + parentId);
        JSONObject jo = new JSONObject();
        try {
            Area parent =areaService.find(parentId);
            JSONArray areaJa = new JSONArray();
            List<Area> list = new ArrayList<>();
            if (parent != null) {
                list = areaService.findChildren(parent);
            } else {
                list = areaService.findRoots();
            }
            for (Area area : list){
                JSONObject joo = new JSONObject();
                joo.put("value", area.getId());
                joo.put("label", area.getName());
//                joo.put("children", new JSONArray());
                if(area.getChildren()!=null && !area.getChildren().isEmpty()){
                    joo.put("children", new JSONArray());
                }
                areaJa.add(joo);
            }
            jo.put("areas",areaJa);
        } catch (Exception e) {
            LOG.error("get areaOptions error. " + e);
            return new PublicResult<>(PublicResultConstant.FAILED, jo);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS, jo);
    }

//    private JSONArray convertAreas(List<Area> areas){
//        JSONArray array = new JSONArray();
//        if (null != areas && !areas.isEmpty()){
//            for (Area area : areas){
//                JSONObject jo = convertArea(area);
//                array.add(jo);
//            }
//        }
//        return array;
//    }

//    private JSONObject convertArea(Area area){
//        JSONObject jo = JsonUtil.toJSONObject(area, new String[]{"id", "name", "order"});
//        if (null != area.getParent()){
//            jo.put("parent", area.getParent().getId());
//        }
//        return jo;
//    }
}
