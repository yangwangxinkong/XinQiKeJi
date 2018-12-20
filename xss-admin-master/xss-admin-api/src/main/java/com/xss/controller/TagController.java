package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Tag;
import com.xss.service.TagService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签接口
 * @author Huang
 * @date 2018/8/8
 */
@RestController
@RequestMapping("/api/tag")
@Api(description="标签管理模块")
public class TagController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(TagController.class);

    @Autowired
    private TagService tagService;

    @ApiOperation(value = "获取所有标签列表",notes = "获取所有标签列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取标签列表接口:/api/tag/list")
    public PageResult<Object> list(Pageable pageable)throws Exception{
        LOG.debug("get Tag list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Tag> tagPage = tagService.findPage(pageable);
            result = new PageResult<Object>((int)tagPage.getTotal(),tagPage.getPageNumber(),tagPage.getPageSize(), tagService.createEntity().convertList(tagPage.getContent(),new String[]{"type","order","createDate"}));
        }catch (Exception e){
            LOG.error("get Tag list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

    @ApiOperation(value = "批量删除标签",notes = "通过id批量删除标签",httpMethod = "GET",produces = "application/json")
    @GetMapping("/delete")
    @Log(description="平台中心获取所有标签列表接口:/api/tag/delete")
    public PublicResult<Boolean> delete(Long[] ids)throws Exception{
        LOG.debug("ids[] = " + ids);
        tagService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

    @ApiOperation(value = "获取标签类型",notes = "获取标签类型",httpMethod = "GET",produces = "application/json")
    @GetMapping("/findType")
    @Log(description="平台中心获取标签类型接口:/api/tag/findType")
    public PublicResult<JSONObject>findType()throws Exception{
        JSONObject jo = new JSONObject();
        jo.put("types",Tag.Type.values());
        return new PublicResult<>(PublicResultConstant.SUCCESS,jo);
    }

    @ApiOperation(value = "获取标签详情",notes = "通过id获取标签详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取标签详情接口:/api/tag/findById")
    public PublicResult<JSONObject> info(@RequestParam Long id)throws Exception{
        LOG.debug("id = " + id);
        try{
            Tag tag = tagService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,tagService.createEntity().convertEntity(tag,new String[]{"type","order"}));
        }catch (Exception e){
            LOG.error("get info  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    @ApiOperation(value = "获取标签列表",notes = "通过类型获取标签列表 文章:article 商品:product 旅游:tourism",httpMethod = "GET",produces = "application/json")
    @GetMapping("/findTypeList")
    @Log(description="平台中心获取标签列表接口:/api/tag/findTypeList")
    public PublicResult<JSONArray>findTypeList(String type)throws Exception{
        LOG.debug("type = " + type);
        Tag.Type tagType = Tag.Type.valueOf(type);
        JSONArray  ja = new JSONArray();
        try{
            List<Tag> list = tagService.findList(tagType);
            for(Tag tag : list){
                JSONObject jo = JsonUtil.toJSONObject(tag,new String[]{"id","name"});
                ja.add(jo);
            }
        }catch (Exception e){
            LOG.error("findTypeList  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,ja);
    }

    @ApiOperation(value = "保存/更新标签",notes = "保存/更新标签",httpMethod = "POST",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心获取标签详情接口:/api/tag/save")
    public PublicResult<Boolean> save(@RequestBody Tag tag)throws Exception{
        LOG.debug("tag ="+tag.toString());
        try{
            if (null != tag.getId()){
                tagService.update(tag);
            }else {
                tagService.save(tag);
            }
            return new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("save/update  error. {}", e);
            return  new PublicResult<Boolean>(PublicResultConstant.ERROR,false);
        }
    }
}
