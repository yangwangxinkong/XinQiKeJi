package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.Role;
import com.xss.service.AdminService;
import com.xss.service.RoleService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 *  管理员接口
 * @author fan.hu
 * @since 2018-08-09
 */
@RestController
@RequestMapping("/api/admin")
@Api(description="管理员模块")
public class AdminController {
    private static final Log LOG = LogFactory.getLog(AdminController.class);

    @Autowired
    private AdminService adminService;
    @Autowired
    private RoleService roleService;

    /**
     * 获取当前登录用户信息
     * @param admin
     * @return
     * @throws Exception
     */
    @GetMapping("/currentUser")
    public PublicResult<Map<String, Object>> getUser(@CurrentUser Admin admin) throws Exception{
        admin = adminService.find(admin.getId());
        JSONObject adminJo = JsonUtil.toJSONObject(admin, new String[]{"id","username","name"});
        //adminJo.put("token", JWTUtil.sign(admin.getUsername(), admin.getPassword()));


        JSONArray authoritiesJa = new JSONArray();
        Set<Role> roles = admin.getRoles();
        if (null != admin.getRoles() && !admin.getRoles().isEmpty()){
            for(Role role : admin.getRoles()){
                authoritiesJa.addAll(role.getAuthorities());
            }
        }
        adminJo.put("roles", authoritiesJa);
        return new PublicResult<>(PublicResultConstant.SUCCESS, adminJo);
    }

    /**
     * 列表
     */
    @GetMapping("/list")
    public PageResult<Object> list(Pageable pageable) {
        LOG.info("get admin list param : pageable = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Admin> data = adminService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(),adminService.createEntity().convertList(data.getContent(),Admin.DEFAULT_JSON_PARAMS));
        }catch (Exception e){
            LOG.error("get admin list error." + e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        return result;
    }

    /**
     * 保存
     */
    @ApiOperation(value="保存管理员", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminJson", value = "{\"username\":\"test\",\"password\":\"111\",\"email\":\"163@.com\",\"isEnabled\":\"true\",\"roles\":[\"1\"],\"name\":\"dff\",\"department\":\"财务部\"}"
                    , required = true, dataType = "String",paramType="body")
    })
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public PublicResult<Boolean> save(@RequestBody JSONObject adminJson)throws Exception {
        LOG.info("save admin param : adminJson = " + adminJson.toString());



        try{
            //添加管理员 密码长度为8至20位
            String password=adminJson.getString("password");
            LOG.info("password.length"+password.length());
            if(password.length()<8 || password.length()>20){
                return new PublicResult<Boolean>(PublicResultConstant.INVALID_BU_PASSWORD, false);
            }

            Admin admin = new Admin();
//            List<Long> roleIds = JSONArray.toJavaObject(adminJson.getJSONArray("roles"), List.class);
            List<Long> roleIds = new ArrayList<>();
            JSONArray ja =adminJson.getJSONArray("roles");
            for (int i=0;i<ja.size();i++) {
                roleIds.add(ja.getLong(i));
            }
            admin.setRoles(new HashSet<Role>(roleService.findListByIdList(roleIds)));
            if (adminService.usernameExists(adminJson.getString("username"))) {
                return new PublicResult<Boolean>(PublicResultConstant.USERNAME_ALREADY_IN, false);
            }
            admin.setPassword(DigestUtils.md5Hex(adminJson.getString("password")));
            admin.setUsername(adminJson.getString("username"));
            admin.setEmail(adminJson.getString("email"));
            admin.setIsEnabled(adminJson.getBoolean("isEnabled"));
            admin.setName(adminJson.getString("name"));
            admin.setDepartment(adminJson.getString("department"));
            admin.setOpenId(adminJson.getString("openId"));
            admin.setIsLocked(false);
            admin.setLoginFailureCount(0);
            admin.setLockedDate(null);
            admin.setLoginDate(null);
            admin.setLoginIp(null);
            adminService.save(admin);
        }catch(Exception e){
            LOG.error("save admin error. " + e);
            return new PublicResult<Boolean>(PublicResultConstant.PARAM_ERROR, false);
        }
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 更新
     */
    @ApiOperation(value="更新管理员", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminJson", value = "{\"id\":\"1\",\"password\":\"111\",\"email\":\"163@.com\",\"isEnabled\":\"true\",\"isLocked\":\"true\",\"roles\":[\"1\"],\"name\":\"dff\",\"department\":\"财务部\"}"
                    , required = true, dataType = "String",paramType="body")
    })
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public PublicResult<Boolean> update(@RequestBody JSONObject adminJson)throws Exception {
        LOG.info("update admin param : adminJson = " + adminJson.toString());
        Admin pAdmin = adminService.find(adminJson.getLong("id"));
        if (pAdmin == null) {
            return new PublicResult<Boolean>(PublicResultConstant.INVALID_USER, false);
        }
//        List<Long> roleIds = JSONArray.toJavaObject(adminJson.getJSONArray("roles"), List.class);
        List<Long> roleIds = new ArrayList<>();
        JSONArray ja =adminJson.getJSONArray("roles");
        for (int i=0;i<ja.size();i++) {
            roleIds.add(ja.getLong(i));
        }
        pAdmin.setRoles(new HashSet<Role>(roleService.findListByIdList(roleIds)));
        if (StringUtils.isNotEmpty(adminJson.getString("password"))) {
            pAdmin.setPassword(DigestUtils.md5Hex(adminJson.getString("password")));
        }
        if (pAdmin.getIsLocked() && !adminJson.getBoolean("isLocked")) {
            pAdmin.setIsLocked(adminJson.getBoolean("isLocked"));
            pAdmin.setLoginFailureCount(0);
            pAdmin.setLockedDate(null);
        }
        pAdmin.setEmail(adminJson.getString("email"));
        pAdmin.setName(adminJson.getString("name"));
        pAdmin.setDepartment(adminJson.getString("department"));
        pAdmin.setOpenId(adminJson.getString("openId"));
        adminService.update(pAdmin);
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    public PublicResult<Boolean> delete(Long[] ids) throws Exception {
        LOG.info("delete admin param : ids = " + ids);
        if (ids.length >= adminService.count()) {
            return new PublicResult<Boolean>("不能删除所有管理员",false);
        }
        adminService.delete(ids);
        return new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
    }

    /**
     * 详情
     */
    @GetMapping("/detail")
    public PublicResult<JSONObject> info(@RequestParam Long id) {
        LOG.info("admin info param : id = " + id);
        PublicResult<JSONObject> result = null;
        try{
            Admin admin = adminService.find(id);
            if(admin == null){
                return new PublicResult<JSONObject>(PublicResultConstant.INVALID_USER,null);
            }
            JSONObject jo = admin.convertEntity(admin,new String[]{"roles"});
            result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS,jo);
        }catch (Exception e){
            LOG.error("admin info error. " + e);
            result =new PublicResult<JSONObject>(PublicResultConstant.FAILED,null);
        }
        return result;
    }

}
