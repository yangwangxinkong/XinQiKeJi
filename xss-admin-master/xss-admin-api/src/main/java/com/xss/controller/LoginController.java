package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.annotation.Pass;
import com.xss.annotation.ValidationParam;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.Role;
import com.xss.service.AdminService;
import com.xss.util.ComUtil;
import com.xss.util.JWTUtil;
import com.xss.util.JsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Map;
import java.util.Set;

/**
 *  登录接口
 * @author zzl
 * @since 2018-05-03
 */
@RestController
@RequestMapping("/api")
@Api(description="身份认证模块")
public class LoginController {
    @Autowired
    private AdminService adminService;

    @ApiOperation(value="手机密码登录", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "requestJson", value = "{\"username\":\"admin\",\"password\":\"123123\"}"
                    , required = true, dataType = "String",paramType="body")
    })
    @PostMapping("/login")
    @Log(description="前台密码登录接口:/login")
    @Pass
    public PublicResult<Map<String, Object>> login(
            @ValidationParam("username,password")@RequestBody JSONObject requestJson) throws Exception{
        //由于 @ValidationParam注解已经验证过mobile和passWord参数，所以可以直接get使用没毛病。
        String username = requestJson.getString("username");

        String password=requestJson.getString("password");
        if(password.length()<8||password.length()>20){
            return  new PublicResult<>(PublicResultConstant.INVALID_BU_PASSWORD,null);

        }
//        if(!StringUtil.checkMobileNumber(mobile)){
//            return new PublicResult<>(PublicResultConstant.MOBILE_ERROR, null);
//        }
        Admin admin = adminService.findByUsername(username);
        //User user = userService.selectOne(new EntityWrapper<User>().where("mobile = {0} and status = 1",mobile));
//        if (ComUtil.isEmpty(admin) || !BCrypt.checkpw(requestJson.getString("password"), admin.getPassword())) {
        if (ComUtil.isEmpty(admin) || !DigestUtils.md5Hex(requestJson.getString("password")).equals(admin.getPassword())) {
            return new PublicResult<>(PublicResultConstant.INVALID_USERNAME_PASSWORD, null);
        }
        JSONObject adminJo = JsonUtil.toJSONObject(admin, new String[]{"id","username","name"});
        adminJo.put("token", JWTUtil.sign(admin.getUsername(), admin.getPassword()));
        JSONArray authoritiesJa = new JSONArray();
        Set<Role> roles = admin.getRoles();
        if (null != admin.getRoles() && !admin.getRoles().isEmpty()){
            for(Role role : admin.getRoles()){
                authoritiesJa.addAll(role.getAuthorities());
            }
        }
        adminJo.put("roles", authoritiesJa);
//        Map<String, Object> result = new HashMap<>();
//        Set<Role> roles = admin.getRoles();
//        admin.setToken(JWTUtil.sign(admin.getUsername(), admin.getPassword()));
//        result.put("admin",admin);
//        for(Role role : roles){
//            result.put(role.getName(), role.getAuthorities());
//        }

        //测试websocket用户登录给管理员发送消息的例子  前端代码参考父目录下WebSocketDemo.html
//        noticeService.insertByThemeNo("themeNo-cwr3fsxf233edasdfcf2s3","13888888888");
//        MyWebSocketService.sendMessageTo(JSONObject.toJSONString(user),"13888888888");
        return new PublicResult<>(PublicResultConstant.SUCCESS, adminJo);
    }

//    @ApiOperation(value="短信验证码登录", notes="body体参数,不需要Authorization",produces = "application/json")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "requestJson", value = "{\"mobile\":\"17765071662\",\"captcha\":\"5780\"}"
//                    , required = true, dataType = "String",paramType="body")
//    })
//    @PostMapping("/login/captcha")
//    @Log(description="前台短信验证码登录接口:/login/captcha")
//    @Pass
//    public PublicResult<Map<String, Object>> loginBycaptcha(
//            @ValidationParam("mobile,captcha")@RequestBody JSONObject requestJson) throws Exception{
//        String mobile = requestJson.getString("mobile");
//        if(!StringUtil.checkMobileNumber(mobile)){
//            return new PublicResult<>(PublicResultConstant.MOBILE_ERROR, null);
//        }
//        User user = userService.getUserByMobile(mobile);
//        //如果不是启用的状态
//        if(!ComUtil.isEmpty(user) && user.getStatus() != Constant.ENABLE){
//            return new PublicResult<>("该用户状态不是启用的!", null);
//        }
//        List<SmsVerify> smsVerifies = smsVerifyService.getByMobileAndCaptchaAndType(mobile,
//                requestJson.getString("captcha"), SmsSendUtil.SMSType.getType(SmsSendUtil.SMSType.AUTH.name()));
//        if(ComUtil.isEmpty(smsVerifies)){
//            return new PublicResult<>(PublicResultConstant.VERIFY_PARAM_ERROR, null);
//        }
//        if(SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())){
//            return new PublicResult<>(PublicResultConstant.VERIFY_PARAM_PASS, null);
//        }
//        if (ComUtil.isEmpty(user)) {
//            User userRegister = new User();
//            //设置默认密码
//            userRegister.setPassWord(BCrypt.hashpw("123456", BCrypt.gensalt()));
//            userRegister.setMobile(mobile);
//            userRegister.setUserName(mobile);
//            user =userService.register(userRegister, Constant.RoleType.USER);
//        }
//        Map<String, Object> result = userService.getLoginUserAndMenuInfo(user);
//        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
//    }
//
//
//
//    @ApiOperation(value="手机验证码注册", notes="body体参数,不需要Authorization",produces = "application/json")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "requestJson", value = "{\"userName\":\"zzl\",\"mobile\":\"17765071662\",</br>" +
//                    "\"captcha\":\"5780\",\"passWord\":\"123456\",</br>\"rePassWord\":\"123456\",\"job\":\"java开发\"," +
//                    "</br>\"unit(可不传)\":\"xxx公司\"}"
//                    , required = true, dataType = "String",paramType="body")
//    })
//    @PostMapping("/register")
//    @Log(description="注册接口:/register")
//    @Pass
//    public PublicResult<User> register(@ValidationParam("userName,passWord,rePassWord,mobile,captcha,job")
//                                       @RequestBody JSONObject requestJson) {
//        //可直接转为java对象,简化操作,不用再set一个个属性
//        User userRegister = requestJson.toJavaObject(User.class);
//        if(!StringUtil.checkMobileNumber(userRegister.getMobile())){
//            return new PublicResult<>(PublicResultConstant.MOBILE_ERROR, null);
//        }
//        if (!userRegister.getPassWord().equals(requestJson.getString("rePassWord"))) {
//            return new PublicResult<>(PublicResultConstant.INVALID_RE_PASSWORD, null);
//        }
//        List<SmsVerify> smsVerifies = smsVerifyService.getByMobileAndCaptchaAndType(userRegister.getMobile(),
//                requestJson.getString("captcha"), SmsSendUtil.SMSType.getType(SmsSendUtil.SMSType.REG.name()));
//        if(ComUtil.isEmpty(smsVerifies)){
//            return new PublicResult<>(PublicResultConstant.VERIFY_PARAM_ERROR, null);
//        }
//        //验证码是否过期
//        if(SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())){
//            return new PublicResult<>(PublicResultConstant.VERIFY_PARAM_PASS, null);
//        }
//        userRegister.setPassWord(BCrypt.hashpw(requestJson.getString("passWord"), BCrypt.gensalt()));
//        //默认注册普通用户
//        userService.register(userRegister, Constant.RoleType.USER);
//        return new PublicResult<>(PublicResultConstant.SUCCESS, null);
//    }
//
//
//    @ApiOperation(value="忘记密码", notes="body体参数,不需要Authorization",produces = "application/json")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "requestJson", value = "{\"mobile\":\"17765071662\",\"captcha\":\"5780\",</br>" +
//                    "\"passWord\":\"123456\",\"rePassWord\":\"123456\"}"
//                    , required = true, dataType = "String",paramType="body")
//    })
//    @PostMapping("/forget/password")
//    @Pass
//    public PublicResult<String> resetPassWord (@ValidationParam("mobile,captcha,passWord,rePassWord")
//                                               @RequestBody JSONObject requestJson ) throws Exception{
//        String mobile = requestJson.getString("mobile");
//        if(!StringUtil.checkMobileNumber(mobile)){
//            return new PublicResult<>(PublicResultConstant.MOBILE_ERROR, null);
//        }
//        if (!requestJson.getString("passWord").equals(requestJson.getString("rePassWord"))) {
//            return new PublicResult<>(PublicResultConstant.INVALID_RE_PASSWORD, null);
//        }
//        User user = userService.getUserByMobile(mobile);
//        roleService.getRoleIsAdminByUserNo(user.getUserNo());
//        if(ComUtil.isEmpty(user)){
//            return new PublicResult<>(PublicResultConstant.INVALID_USER, null);
//        }
//        List<SmsVerify> smsVerifies = smsVerifyService.getByMobileAndCaptchaAndType(mobile,
//                requestJson.getString("captcha"), SmsSendUtil.SMSType.getType(SmsSendUtil.SMSType.FINDPASSWORD.name()));
//        if(ComUtil.isEmpty(smsVerifies)){
//            return new PublicResult<>(PublicResultConstant.VERIFY_PARAM_ERROR, null);
//        }
//        if(SmsSendUtil.isCaptchaPassTime(smsVerifies.get(0).getCreateTime())){
//            return new PublicResult<>(PublicResultConstant.VERIFY_PARAM_PASS, null);
//        }
//        user.setPassWord(BCrypt.hashpw(requestJson.getString("passWord"),BCrypt.gensalt()));
//        userService.updateById(user);
//        return  new PublicResult<String>(PublicResultConstant.SUCCESS, null);
//    }

//    /**
//     * 检查用户是否注册过
//     * @param mobile
//     * @return
//     * @throws Exception
//     */
//    @GetMapping("/check/mobile")
//    @Pass
//    @ApiIgnore
//    public PublicResult loginBycaptcha(@RequestParam("mobile") String mobile) throws Exception{
//        User user = userService.getUserByMobile(mobile);
//        return new PublicResult<>(PublicResultConstant.SUCCESS, !ComUtil.isEmpty(user));
//    }

    @ApiIgnore
    @RequestMapping(path = "/401",produces = "application/json;charset=utf-8")
    public PublicResult<String> unauthorized() {
        return new PublicResult<>(PublicResultConstant.UNAUTHORIZED, null);
    }

    @PostMapping("/password")
    public PublicResult<String> resetPassWord (@CurrentUser Admin currentUser,@ValidationParam("oldPassword,newPassword,rePassword")
    @RequestBody JSONObject requestJson ) throws Exception{
        //两次输入的密码 是否一致
        if (!requestJson.getString("newPassword").equals(requestJson.getString("rePassword"))) {
            return new PublicResult<>(PublicResultConstant.INVALID_RE_PASSWORD, null);
        }

        //密码长度不能小于8或大于20
        if(requestJson.getString("newPassword").length()<8||requestJson.getString("newPassword").length()>20){
            return  new PublicResult<>(PublicResultConstant.INVALID_BU_PASSWORD, null);
        }
        //用户名 密码 错误
        if (!DigestUtils.md5Hex(requestJson.getString("oldPassword")).equals(currentUser.getPassword())){
            return new PublicResult<>(PublicResultConstant.INVALID_USERNAME_PASSWORD, null);
        }
        Admin memberDB=adminService.find(currentUser.getId());
        memberDB.setPassword(DigestUtils.md5Hex(requestJson.getString("newPassword")));
        adminService.update(memberDB);
        return  new PublicResult<String>(PublicResultConstant.SUCCESS, null);
    }
}
