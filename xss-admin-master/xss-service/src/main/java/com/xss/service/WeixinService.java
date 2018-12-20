package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.domain.Payment;
import com.xss.domain.WxClickValue;
import com.xss.domain.WxMenu;
import com.xss.util.ApiUtils;
import com.xss.util.JsonUtil;
import com.xss.util.LoadServicePoperties;
import org.apache.commons.collections.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeixinService {

    public static String SOURCE;
    private static String URL;

    public WeixinService(){
        if(SOURCE==null){
            SOURCE = LoadServicePoperties.props.get("resource");
        }
        if(URL==null){
            URL = LoadServicePoperties.props.get("wx_service_url");
        }
    }

    public String getConfig(){
        try {
            String result = ApiUtils.getForObject(URL+"/config/detail/"+SOURCE, null, null);
            System.out.println("getConfig---" + result);
            return result;
        } catch (IOException e) {
            return "";
        }
    }

    public String getJsapiTicket(){
        try {
            String result = ApiUtils.getForObject(URL+"/config/jsapi/ticket/"+SOURCE, null, null);
            System.out.println("getJsapiTicket---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")){
                return jo.getString("data");
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public JSONObject getJsapiSign(String url){
        try {
            MultiValueMap map= new MultiValueMap();
            map.put("url", url);
            String result = ApiUtils.getForObject(URL+"/config/jsapi/sign/"+SOURCE, map, null);
            System.out.println("getJsapiSign---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")){
                return jo.getJSONObject("data");
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public static String updateConfig(String config){
        MultiValueMap map= new MultiValueMap();
        map.put("config", config);
        try {
            String result = ApiUtils.getForObject(URL+"/config/save", map, null);
            System.out.println("updateConfig---" + result);
            return result;
        } catch (IOException e) {
            return "";
        }
    }

    public JSONObject getClickKeyList(int pageNumber,int pageSize){
        MultiValueMap map= new MultiValueMap();
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        map.put("source", SOURCE);

        try {
            String result = ApiUtils.getForObject(URL+"/click-key/list", map, null);
            System.out.println("clickKeys---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")){
                return jo.getJSONObject("data");
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
    public JSONArray getClickKeyAll(){
        MultiValueMap map= new MultiValueMap();
        map.put("source", SOURCE);
        try {
            String result = ApiUtils.getForObject(URL+"/click-key/all", map, null);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")){
                return jo.getJSONArray("data");
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
    public String saveClickKey(String name,String keyString,String clickType,Integer clickNum){
        MultiValueMap map= new MultiValueMap();
        map.put("name", name);
        map.put("keyString", keyString);
        map.put("clickType", clickType);
        map.put("clickNum", clickNum);
        map.put("source", SOURCE);

        try {
            String result = ApiUtils.postForObject(URL+"/click-key/save", map, null);
            System.out.println("clickKeyAdd---" + result);
            return result;
        } catch (IOException e) {
            return "";
        }
    }

    public JSONObject getClickKeyInfo(Long id){
        try {
            String info = ApiUtils.getForObject(URL+"/click-key/detail/"+SOURCE+"/"+id, null, null);
            JSONObject jo = JSONObject.parseObject(info);
            if (0 == jo.getInteger("errcode")){
                return jo.getJSONObject("data");
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public String updateClickKey(Long id,String name,String keyString,String clickType,Integer clickNum){
        MultiValueMap map= new MultiValueMap();
        map.put("id", id);
        map.put("name", name);
        map.put("keyString", keyString);
        map.put("clickType", clickType);
        map.put("clickNum", clickNum);
        map.put("source", SOURCE);

        try {
            return ApiUtils.postForObject(URL+"/click-key/update", map, null);
        } catch (IOException e) {
            return "";
        }
    }

    public String deleteClickKey(Long[] ids){
        MultiValueMap map= new MultiValueMap();
        if(null!=ids&&ids.length>0){
            for(int i=0;i<ids.length;i++){
                map.put("ids", ids[i]);
            }
        }

        try {
            return ApiUtils.getForObject(URL+"/click-key/delete/"+SOURCE, map, null);
        } catch (IOException e) {
            return "";
        }
    }
    public JSONObject getClickValueList(int pageNumber,int pageSize){
        MultiValueMap map= new MultiValueMap();
        map.put("pageNumber", pageNumber);
        map.put("pageSize", pageSize);
        map.put("source", SOURCE);

        try {
            String result = ApiUtils.getForObject(URL+"/click-value/list", map, null);
            System.out.println("click value:" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")){
                return jo.getJSONObject("data");
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }
    public String saveClickValue(WxClickValue wxClickValue){
        MultiValueMap map= new MultiValueMap();
        map.put("title", wxClickValue.getTitle());
        map.put("content", wxClickValue.getContent());
        map.put("imagePath", wxClickValue.getImagePath());
        map.put("url", wxClickValue.getUrl());
        map.put("order", wxClickValue.getOrder());
        map.put("category", wxClickValue.getCategory().name());
        try {
            return ApiUtils.postForObject(URL+"/click-value/save/"+SOURCE+"/"+wxClickValue.getWxClickKey().getId(), map, null);
        } catch (IOException e) {
            return "";
        }
    }

    public JSONObject getClickValueInfo(Long id){
        try {
            String info = ApiUtils.getForObject(URL+"/click-value/detail/"+SOURCE+"/"+id, null, null);
            JSONObject jo = JSONObject.parseObject(info);
            if (0 == jo.getInteger("errcode")){
                return jo.getJSONObject("data");
            }else{
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public String updateClickValue(WxClickValue wxClickValue){
        MultiValueMap map= new MultiValueMap();
        map.put("id", wxClickValue.getId());
        map.put("title", wxClickValue.getTitle());
        map.put("content", wxClickValue.getContent());
        map.put("imagePath", wxClickValue.getImagePath());
        map.put("url", wxClickValue.getUrl());
        map.put("order", wxClickValue.getOrder());
        map.put("category", wxClickValue.getCategory().name());

        try {
            return ApiUtils.postForObject(URL+"/click-value/update/"+SOURCE+"/"+wxClickValue.getWxClickKey().getId(), map, null);
        } catch (IOException e) {
            return "";
        }
    }

    public String deleteClickValue(Long[] ids){
        MultiValueMap map= new MultiValueMap();
        if(null!=ids&&ids.length>0){
            for(int i=0;i<ids.length;i++){
                map.put("ids", ids[i]);
            }
        }
        try {
            return ApiUtils.getForObject(URL+"/click-value/delete/"+SOURCE, map, null);
        } catch (IOException e) {
            return "";
        }
    }
    /**
     * 获取该公众号所有自定义菜单
     * @return
     */
    public JSONArray getMenuList(){
        List<WxMenu> list = new ArrayList<WxMenu>();
        MultiValueMap map= new MultiValueMap();
        map.put("source", SOURCE);

        try {
            String result = ApiUtils.getForObject(URL+"/menu/list", map, null);
            System.out.println("getMenuList---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            JSONArray ja = jo.getJSONArray("data");
//            if (null != ja && ja.size() > 0) {
//                for (int i = 0; i < ja.size(); i++) {
//                    JSONObject mJo = ja.getJSONObject(i);
//                    WxMenu menu = WxMenu.convertJson(mJo);
//                    list.add(menu);
//                    JSONArray mJa = mJo.getJSONArray("children");
//                    for (int j = 0; j < mJa.size(); j++) {
//                        list.add(WxMenu.convertJson(mJa.getJSONObject(j)));
//                    }
//                }
//            }
//            return list;
            return ja;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取指定自定义菜单信息
     * @return
     */
    public JSONObject getMenuInfo(Long id){
        WxMenu menu = new WxMenu();
        MultiValueMap map= new MultiValueMap();
        String url = URL + String.format("/menu/detail/%s/%s", SOURCE, id);

        try {
            String result = ApiUtils.getForObject(url, map, null);
            System.out.println("getMenuInfo---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            JSONObject mJo = jo.getJSONObject("data");
            
           return mJo;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 保存该公众号自定义菜单
     * @return
     */
    public String saveMenu(WxMenu wxMenu){
        MultiValueMap map= new MultiValueMap();
        map.put("source", SOURCE);
        map.put("name", wxMenu.getName());
        map.put("type", wxMenu.getType().name());
        map.put("category", wxMenu.getCategory().name());
        map.put("keyName", wxMenu.getKeyName());
        map.put("url", wxMenu.getUrl());
        map.put("parent.id", wxMenu.getParent().getId());

        try {
            String result = ApiUtils.postForObject(URL+"/menu/save", map, null);
            System.out.println("saveMenu---" + result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 更新该公众号自定义菜单
     * @return
     */
    public String updateMenu(WxMenu wxMenu){
        MultiValueMap map= new MultiValueMap();
        map.put("source", SOURCE);
        map.put("id", wxMenu.getId());
        map.put("name", wxMenu.getName());
        map.put("type", wxMenu.getType().name());
        map.put("category", wxMenu.getCategory().name());
        map.put("keyName", wxMenu.getKeyName());
        map.put("url", wxMenu.getUrl());
        map.put("parent.id", wxMenu.getParent().getId());

        try {
            String result = ApiUtils.postForObject(URL+"/menu/update", map, null);
            System.out.println("updateMenu---" + result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 删除该公众号自定义菜单
     * @return
     */
    public String delMenu(Long id){
        MultiValueMap map= new MultiValueMap();
        map.put("source", SOURCE);
        map.put("ids", id);

        try {
            String url = URL + String.format("/menu/delete/%s", SOURCE);
            String result = ApiUtils.getForObject(url, map, null);
            System.out.println("delMenu---" + result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 发布该公众号自定义菜单
     * @return
     */
    public String publishMenu(){
        MultiValueMap map= new MultiValueMap();

        try {
            String url = URL + String.format("/menu/publish/%s", SOURCE);
            String result = ApiUtils.getForObject(url, map, null);
            System.out.println("publishMenu---" + result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }
    /**
     * 公众号回调接口
     * @return
     */
    public String callback(MultiValueMap map){

        try {
            String url = URL + "/callback";
            String result = ApiUtils.postForObject(url, map, null);
            System.out.println("callback---" + result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }
    /**
     * 发送模板消息
     * @return
     */
    public String sendTemplateMessage(String json){
        MultiValueMap map=new MultiValueMap();
        map.put("json", json);
        try {
            String url = URL + "/message/template/send/"+SOURCE;
            String result = ApiUtils.getForObject(url, map, null);
            System.out.println("sendTemplateMessage---" + result);
            return result;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取公众号关注人数接口
     * @return
     */
    public static int getUserCount(){

        MultiValueMap map = new MultiValueMap();
        try {
            String url = URL + "/user/count/" + SOURCE;
            String result = ApiUtils.getForObject(url, map, null);
            System.out.println("getUserCount---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")) {
                return jo.getInteger("data");
            } else {
                return 0;
            }
        } catch (IOException e) {
            return 0;
        }
    }
    /**
     * 获取微信用户基本信息接口
     * @return
     */
    public JSONObject getUserInfo(String redirectType, String code){

        MultiValueMap map = new MultiValueMap();
        map.put("redirectType", redirectType);
        map.put("code", code);
        try {
            String url = URL + "/user/info/" + SOURCE;
            String result = ApiUtils.getForObject(url, map, null);
            System.out.println("getUserCount---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")) {
                return jo.getJSONObject("data");
            } else {
                return JSONObject.parseObject("{\"subscribe\":1,\"openid\":\"\"}");
            }
        } catch (Exception e) {
            return JSONObject.parseObject("{\"subscribe\":1,\"openid\":\"\"}");
        }
    }

    /**
     * 获取第三方登录授权地址
     * @return
     */
    public static String authorizePc(String response_type,String state,String scope){
        MultiValueMap map = new MultiValueMap();
        map.put("response_type", response_type);
        map.put("state", state);
        map.put("scope", scope);
        try {
            String url = URL + "/authorize/" + SOURCE;
            String result = ApiUtils.getForObject(url, map, null);
            System.out.println("authorize---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")) {
                return jo.getString("data");
            }
            return "";
        } catch (IOException e) {
            return "";
        }
    }

    /**
     * 为url加上微信授权链接前缀
     * 如果失败，返回原链接
     * @param url 链接
     * @return
     */
    public static String concatAuthorizeUrl(String url){
        try {
            MultiValueMap map = new MultiValueMap();
            map.put("url", url);
            String apiUrl = URL + "/common/url/authorize/" + SOURCE;
            String result = ApiUtils.getForObject(apiUrl, map, null);
            System.out.println("concatAuthorizeUrl result---" + result);
            JSONObject jo = JSONObject.parseObject(result);
            if (0 == jo.getInteger("errcode")) {
                return jo.getString("data");
            }else{
                return url;
            }
        } catch (IOException e) {
            return url;
        }
    }
    /**
     * 获取支付宝支付提交信息链接及提交信息
     * @param map
     * 		需求信息
     * @return
     * 		失败返回null
     */
    public static Map<String,Object> getAliPayMap(MultiValueMap map){
        try {
            String url = URL + String.format("/pay/alipay/getPayMap/%s", SOURCE);
            String result = ApiUtils.postForObject(url,map,null);
            JSONObject jo = JSONObject.parseObject(result);
            JSONObject  jasonObject = JSONObject.parseObject(jo.getString("data"));
            Map resultMap = (Map)jasonObject;
            return resultMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取支付宝h5支付form
     * @param amount
     * @param sn
     * @param subject
     * @return
     */
    public JSONObject getAlipayH5Form(String amount, String sn, Long orderId, Payment.Type type, String subject){
        JSONObject resultJo = new JSONObject();
        MultiValueMap map= new MultiValueMap();
        map.put("amount", amount);
        map.put("sn", sn);
        map.put("description", subject);
        if (Payment.Type.recharge.equals(type)) {
            map.put("returnUrl", "http://m.xiaodoushebao.com/#/recharge/result");
        }else{
            map.put("returnUrl", "http://m.xiaodoushebao.com/#/order/paymentResult?id=" + orderId + "&sn=" + sn);
        }
        //map.put("returnUrl", "http://m.xiaodoushebao.com/#/member/order/orderDetail?id=" + orderId);

        map.put("notifyUrl", "http://api.xiaodoushebao.com/m/payment/notify/alipay/" + sn);

        try {
            String url = URL + String.format("/pay/alipay/h5/%s", SOURCE);
            System.out.println("getAlipayH5Form url---" + url);
            String result = ApiUtils.postForObject(url, map, null);
            System.out.println("getAlipayH5Form result---" + result);
            if(StringUtils.isNotEmpty(result)){
                JSONObject jo = JSONObject.parseObject(result);
                if(jo.getInteger("errcode")==0){
                    resultJo = jo.getJSONObject("data");
                }else{
                    System.out.println("getAlipayH5Form error---" + jo.getString("errmsg"));
                }
            }
        } catch (IOException e) {
            System.out.println("getAlipayH5Form error. e = " + e);
        }
        System.out.println("getWeixinH5PayMap map = " + resultJo.toString());
        return resultJo;
    }

    /**
     * 支付宝退款
     * @param map
     * 			需求信息
     * @return
     * 			失败返回null
     */
    public static JSONObject aliRefund(MultiValueMap map){
        try {
            String url = URL + String.format("/pay/aliPay/aliRefund/%s", SOURCE);
            String result = ApiUtils.postForObject(url,map,null);
            JSONObject jo = JSONObject.parseObject(result);
            return jo;
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 获取微信pc支付二维码url
     * @param total_fee
     * @param payment_sn
     * @param notify_url
     * @param create_ip
     * @return
     */
    public static String getWeixinPcPayQrcode(String total_fee,String payment_sn,String notify_url,String create_ip){
        String code_url = "";
        MultiValueMap map= new MultiValueMap();
        map.put("total_fee", total_fee);
        map.put("payment_sn", payment_sn);
        map.put("notify_url", notify_url);
        map.put("create_ip", create_ip);
        try {
            String url = URL + String.format("/pay/weixin/pc/%s", SOURCE);
            System.out.println("getWeixinPcPayQrcode url---" + url);
            String result = ApiUtils.postForObject(url, map, null);
            System.out.println("getWeixinPcPayQrcode result---" + result);
            if(StringUtils.isNotEmpty(result)){
                JSONObject jo = JSONObject.parseObject(result);
                if(jo.getInteger("errcode")==0){
                    JSONObject joo = jo.getJSONObject("data");
                    code_url = joo.getString("code_url");
                }else{
                    System.out.println("getWeixinPcPayQrcode error---" + jo.getString("errmsg"));
                }
            }
        } catch (IOException e) {
            System.out.println("getWeixinPcPayQrcode error. e = " + e);
        }
        System.out.println("getWeixinPcPayQrcode code_url = " + code_url);
        return code_url;
    }

    /**
     * 获取微信h5支付map
     * @param total_fee
     * @param payment_sn
     * @param notify_url
     * @param create_ip
     * @param openid
     * @return
     */
    public static Map<String,Object> getWeixinH5PayMap(String total_fee, String payment_sn, String notify_url, String create_ip, String openid){
        Map<String,Object> resultMap = new HashMap<>();
        MultiValueMap map= new MultiValueMap();
        map.put("total_fee", total_fee);
        map.put("payment_sn", payment_sn);
        map.put("notify_url", notify_url);
        map.put("create_ip", create_ip);
        map.put("openid", openid);
        try {
            String url = URL + String.format("/pay/weixin/h5/%s", SOURCE);
            System.out.println("getWeixinH5PayMap url---" + url);
            String result = ApiUtils.postForObject(url, map, null);
            System.out.println("getWeixinH5PayMap result---" + result);
            if(StringUtils.isNotEmpty(result)){
                JSONObject jo = JSONObject.parseObject(result);
                if(jo.getInteger("errcode")==0){
                    resultMap = JsonUtil.json2Map(jo.getString("data"));
                }else{
                    System.out.println("getWeixinH5PayMap error---" + jo.getString("errmsg"));
                }
            }
        } catch (IOException e) {
            System.out.println("getWeixinH5PayMap error. e = " + e);
        }
        System.out.println("getWeixinH5PayMap map = " + resultMap.toString());
        return resultMap;
    }

    /**
     * 微信支付退款
     * @param total_fee
     * @param refund_fee
     * @param out_trade_no
     * @param out_refund_no
     * @return
     */
    public static String weixinRefund(BigDecimal total_fee, BigDecimal refund_fee, String out_trade_no, String out_refund_no) {
        String code = "";
        Map<String, Object> resultMap = new HashMap<>();
        MultiValueMap map = new MultiValueMap();
        map.put("total_fee", total_fee);
        map.put("refund_fee", refund_fee);
        map.put("out_trade_no", out_trade_no);
        map.put("out_refund_no", out_refund_no);
        try {
            String url = URL + String.format("/pay/weixin/refund/%s", SOURCE);
            System.out.println("weixinRefund url---" + url);
            String result = ApiUtils.postForObject(url, map, null);
            System.out.println("weixinRefund result---" + result);
            if(StringUtils.isNotEmpty(result)){
                JSONObject jo = JSONObject.parseObject(result);
                if(jo.getInteger("errcode")==0){
                    JSONObject joo = jo.getJSONObject("data");
                    code = joo.getString("result_code");
                }else{
                    System.out.println("weixinRefund error---" + jo.getString("errmsg"));
                    code = jo.getString("errmsg");
                }
            }
        } catch (IOException e) {
            System.out.println("weixinRefund error. e = " + e);
        }
        System.out.println("weixinRefund code = " + code);
        return code;
    }

}
