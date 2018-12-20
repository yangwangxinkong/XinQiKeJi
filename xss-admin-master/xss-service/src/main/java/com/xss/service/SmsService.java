package com.xss.service;

import com.alibaba.fastjson.JSONObject;
import com.xss.domain.enums.SmsResource;
import com.xss.domain.enums.SmsType;
import com.xss.util.ApiUtils;
import com.xss.util.LoadServicePoperties;
import com.xss.util.page.Pageable;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private static String RESOURCE;
    private static String URL;

    public SmsService(){
        if(RESOURCE==null){
            RESOURCE = LoadServicePoperties.props.get("resource");
        }
        if(URL==null){
            URL = LoadServicePoperties.props.get("sms_service_url");
        }
    }

    /**
     * 分页查询活动列表
     * @param pageable
     * @return
     */
    public JSONObject list(Pageable pageable, Long companyId, String[] status, String type){
        MultiValueMap params= new MultiValueMap();

        params.put("resource", RESOURCE);
        params.put("company", companyId);
        String statusStr = "";
        if(status!=null){
            for(int i=0; i<status.length; i++){
                statusStr += status[i];
                if(i<status.length-1){
                    statusStr += ",";
                }
            }
        }
        params.put("status", statusStr);
        params.put("type", type);
        params.put("pageNumber", pageable.getPageNumber());
        params.put("pageSize", pageable.getPageSize());
        params.put("searchProperty", pageable.getSearchProperty());
        params.put("searchValue", pageable.getSearchValue());
        params.put("orderProperty", pageable.getOrderProperty());
        params.put("orderDirection", pageable.getOrderDirection());

        try{
            return JSONObject.parseObject(ApiUtils.getForObject(URL+"/mc/activity/query/by_type_status", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }
    /**
     * 发送短信验证码
     * @param mobile        手机号
     * @param smsResource   请求端
     *              mobile              手机
     *              web                 网站
     *              app                 app
     *              wx                  微信
     * @param smsType   发送短信验证码类型
     *              register            注册
     * 	            updatePwd           修改密码
     * 	            updateMobile        修改手机号码
     * 	            validMobile         安全验证
     * 	            login               登陆
     * 	            validMail           验证邮箱
     *          	findPwd             找回密码
     *          	hintOrder           订单提示
     *          	successOrder        订单成功
     *          	cancelOrder         订单取消
     * @return
     */
    public JSONObject sendVerificationCode(String mobile, SmsResource smsResource, SmsType smsType){

        MultiValueMap data= new MultiValueMap();
        data.put("mobile", mobile);
        data.put("resource", RESOURCE);
        data.put("smsResource", smsResource.toString());
        data.put("smsType", smsType.toString());
        System.out.println("param:" + data);
        try {
            String result = ApiUtils.postForObject(URL+"/sms/code/send", data, null);
            System.out.println("sendVerificationCode---" + result);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * 校验短信验证码
     * @param mobile        手机号
     * @param smsResource   请求端
     *              mobile              手机
     *              web                 网站
     *              app                 app
     *              wx                  微信
     * @param smsType   发送短信验证码类型
     *              register            注册
     * 	            updatePwd           修改密码
     * 	            updateMobile        修改手机号码
     * 	            validMobile         安全验证
     * 	            login               登陆
     * 	            validMail           验证邮箱
     *          	findPwd             找回密码
     *          	hintOrder           订单提示
     *          	successOrder        订单成功
     *          	cancelOrder         订单取消
     * @param verificationCode   短信验证码
     * @return
     */
    public JSONObject checkVerificationCode(String mobile, SmsResource smsResource, SmsType smsType, String verificationCode){

        MultiValueMap data= new MultiValueMap();
        data.put("mobile", mobile);             //手机号
        data.put("resource", RESOURCE);         //子系统标识
        data.put("smsResource", smsResource.toString());   //请求来源
        data.put("smsType", smsType.toString());           //短信类型
        data.put("verificationCode", verificationCode);

        System.out.println("param:" + data);
        try {
//            String result = send(URL+"/sms/code/check", data, "utf-8");
            String result = ApiUtils.postForObject(URL+"/sms/code/check", data, null);
            System.out.println("checkVerificationCode---" + result);
            return JSONObject.parseObject(result);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
