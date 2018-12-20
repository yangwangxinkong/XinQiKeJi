package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Role;
import com.xss.service.RoleService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  角色管理接口
 * @author fan.hu
 * @since 2018-08-08
 */
@RestController
@RequestMapping("/api/role")
@Api(description="角色管理模块")
public class RoleController {
    private static final Log LOG = LogFactory.getLog(RoleController.class);

    @Autowired
    private RoleService roleService;

    /**
     * 列表
     */
    @GetMapping("/list")
    public PageResult<Object> list(Pageable pageable) {
        LOG.info("get role list param : pageable = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Role> data = roleService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(),roleService.createEntity().convertList(data.getContent(),Role.DEFAULT_JSON_PARAMS));
        }catch (Exception e){
            LOG.error("get role list error. " + e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存角色", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaJson", value = "{\"name\":\"test\",\"description\":\"test\",\"roles\":[\"1\"]}"
                    , required = true, dataType = "String",paramType="body")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public PublicResult<Boolean> save(@RequestBody JSONObject roleJson)throws Exception {
        LOG.info("save role param : roleJson = " + roleJson.toString());
        Role role = new Role();
        role.setName(roleJson.getString("name"));
        role.setDescription(roleJson.getString("description"));
        JSONArray ja = roleJson.getJSONArray("roles");
        role.setAuthorities(JSONArray.toJavaObject(ja,List.class));
        role.setIsSystem(false);
        role.setAdmins(null);
        roleService.save(role);
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 更新
     */
    @ApiOperation(value="更新角色", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "areaJson", value = "{\"id\":\"1\",\"name\":\"test\",\"description\":\"test\",\"roles\":[\"1\"]}"
                    , required = true, dataType = "String",paramType="body")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public PublicResult<Boolean> update(@RequestBody JSONObject roleJson)throws Exception {
        LOG.info("update role param : roleJson = " + roleJson.toString());
        Role role = roleService.find(roleJson.getLong("id"));
        if (role == null || role.getIsSystem()) {
            return new PublicResult<Boolean>(PublicResultConstant.INVALID_ROLE, false);
        }
        role.setName(roleJson.getString("name"));
        role.setDescription(roleJson.getString("description"));
        JSONArray ja = roleJson.getJSONArray("roles");
        role.setAuthorities(JSONArray.toJavaObject(ja,List.class));
        roleService.update(role);
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public PublicResult<Boolean> delete(Long[] ids) throws Exception {
        LOG.info("delete role param : ids = " + ids);
        if (ids != null) {
            for (Long id : ids) {
                Role role = roleService.find(id);
                if (role != null && (role.getIsSystem() || (role.getAdmins() != null && !role.getAdmins().isEmpty()))) {
                    return new PublicResult<Boolean>(PublicResultConstant.ROLE_USER_USED,false);
                }
            }
            roleService.delete(ids);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    public PublicResult<JSONObject> info(@RequestParam Long id) {
        LOG.info("role info param : id = " + id);
        PublicResult<JSONObject> result = null;
        try{
            Role role = roleService.find(id);
            if(role == null){
                return new PublicResult<JSONObject>(PublicResultConstant.INVALID_ROLE,null);
            }
            JSONObject jo = role.convertEntity(role,new String[]{"roles"});
//            jo.put("roles",JSONArray.toJSON(role.getAuthorities()));
            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS,jo);
        }catch (Exception e){
            LOG.error("role info error." + e);
            result =new PublicResult<JSONObject>(PublicResultConstant.FAILED,null);
        }
        return result;
    }

    /**
     * 全部列表
     */
    @GetMapping("/list_all")
    public PublicResult<JSONArray> listAll() {
        PublicResult<JSONArray> result = null;
        try{
            List<Role> data = roleService.findAll();
            result = new PublicResult<JSONArray>(PublicResultConstant.SUCCESS,roleService.createEntity().convertList(data,Role.DEFAULT_JSON_PARAMS));
        }catch (Exception e){
            result = new PublicResult<JSONArray>(PublicResultConstant.FAILED,null);
        }
        return result;
    }

//    private JSONArray convertRoles(List<Role> roles){
//        JSONArray array = new JSONArray();
//        if (null != roles && !roles.isEmpty()){
//            for (Role role : roles){
//                JSONObject jo = convertRole(role);
//                array.add(jo);
//            }
//        }
//        return array;
//    }

//    private JSONObject convertRole(Role role){
//        JSONObject jo = JsonUtil.toJSONObject(role, new String[]{"id", "name", "isSystem", "description"});
//        jo.put("createDate", DateUtil.format(role.getCreateDate(), "yyyy-MM-dd HH:mm:ss"));
//        return jo;
//    }
}
