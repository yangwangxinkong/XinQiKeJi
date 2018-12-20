package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.util.ApiUtils;
import com.xss.util.LoadServicePoperties;
import com.xss.util.page.Pageable;
import org.apache.commons.collections.map.MultiValueMap;
import org.springframework.stereotype.Service;

@Service
public class ActivityService {

    private static String RESOURCE;
    private static String URL;

    public ActivityService(){
        if(RESOURCE==null){
            RESOURCE = LoadServicePoperties.props.get("resource");
        }
        if(URL==null){
            URL = LoadServicePoperties.props.get("activity_service_url");
        }
    }

    /**
     * 分页查询活动列表
     * @param pageable
     * @return
     */
    public JSONObject list(Pageable pageable, Long companyId, String[] status, String type, String cityCode){
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
        params.put("cityCode", cityCode);
        try{
            return JSONObject.parseObject(ApiUtils.getForObject(URL+"/mc/activity/query/by_type_status", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 查询 未开始、进行中的推荐活动列表
     * @param type 活动类型
     * @param count 获取数量
     * @return
     */
    public JSONArray recommend(String type, Integer count, String cityCode){
        MultiValueMap params= new MultiValueMap();

        params.put("resource", RESOURCE);
        // 活动所属企业
        params.put("company", null);
        // 活动类型（专题：advertise、促销：promotion、报名：signup），null查询所有类型
        params.put("type", type);
        params.put("cityCode", cityCode);
        params.put("count", count);
        JSONArray ja = null;
        try{
            JSONObject result = JSONObject.parseObject(ApiUtils.getForObject(URL+"/mc/activity/query/recommend", params, null));
            if (result.getInteger("errcode") == 0){
                ja = result.getJSONArray("data");
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return ja;
    }

    /**
     * 根据指定id组，查询活动列表
     * @param ids 活动id组，多个id使用英文半角逗号分隔
     * @return
     */
    public JSONObject list(String ids){
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        params.put("ids", ids);

        try{
            return JSONObject.parseObject(ApiUtils.getForObject(URL+"/mc/activity/query/by_ids", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 根据指定id组，查询活动列表
     * @param memberId 用户id
     * @param cityCode 城市编码（为null时，获取所有城市的数据）
     * @return
     */
    public JSONObject memberList(Pageable pageable, Long memberId, String cityCode){
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        params.put("memberId", memberId);
        params.put("pageNumber", pageable.getPageNumber());
        params.put("pageSize", pageable.getPageSize());
        params.put("searchProperty", pageable.getSearchProperty());
        params.put("searchValue", pageable.getSearchValue());
        params.put("orderProperty", pageable.getOrderProperty());
        params.put("orderDirection", pageable.getOrderDirection());
        params.put("cityCode", cityCode);

        try{
            return JSONObject.parseObject(ApiUtils.getForObject(URL+"/mc/activity/query/member_list", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 新增活动
     * @param activityJson
     * @return
     */
    public JSONObject save(JSONObject activityJson){
        activityJson.put("resource", RESOURCE);
        MultiValueMap params = new MultiValueMap();
        params.put("activityJson", activityJson);

        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/save", params, null));

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 修改活动
     * @param activityJson
     * @return
     */
    public JSONObject update(JSONObject activityJson){
        activityJson.put("resource", RESOURCE);
        activityJson.put("activityId", activityJson.get("id"));
        MultiValueMap params = new MultiValueMap();
        params.put("activityJson", activityJson);

        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/update", params, null));

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 报名
     * @param signUpJson
     * @return
     */
    public JSONObject signUp(JSONObject signUpJson){
        MultiValueMap params = new MultiValueMap();

        params.put("resource", RESOURCE);
        params.put("memberId", signUpJson.get("memberId"));
        params.put("activityId", signUpJson.get("activityId"));
        params.put("name", signUpJson.get("name"));
        params.put("phone", signUpJson.get("phone"));
        params.put("gender", signUpJson.get("gender"));
        params.put("position", signUpJson.get("position"));
        params.put("describe", signUpJson.get("describe"));
        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/sign_up", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 查询活动详情
     * @param activityId 活动id
     * @param companyId 所属企业id
     * @return
     */
    public JSONObject info(Long activityId, Long companyId){
        return info(activityId, companyId, false);
    }

    /**
     * 查询活动详情
     * @param activityId 活动id
     * @param companyId 所属企业id
     * @param isView 是否用户页面访问（本次请求是否增加一次该活动的访问量）
     * @return
     */
    public JSONObject info(Long activityId, Long companyId, Boolean isView){
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        params.put("activityId", activityId);
        params.put("company", companyId);
        params.put("isView", isView);
        try{
            return JSONObject.parseObject(ApiUtils.getForObject(URL+"/mc/activity/query/by_id", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 活动审核
     * @param activityId
     * @return
     */
    public JSONObject audit(Long activityId, Integer decision) {
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        params.put("activityId", activityId);
        params.put("decision", decision);
        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/audit", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 中止活动
     * @param activityId
     * @return
     */
    public JSONObject discontinue(Long activityId) {
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        params.put("activityId", activityId);
        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/discontinue", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 开始活动
     * @param activityId
     * @return
     */
    public JSONObject start(Long activityId) {
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        params.put("activityId", activityId);
        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/start", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 结束活动
     * @param activityId
     * @return
     */
    public JSONObject end(Long activityId) {
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        params.put("activityId", activityId);
        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/end", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 删除活动
     * @param ids
     * @return
     */
    public JSONObject delete(Long[] ids) {
        MultiValueMap params = new MultiValueMap();
        params.put("resource", RESOURCE);
        String idsStr = "";
        for(int i=0; i<ids.length; i++){
            idsStr+=ids[i];
            if(i<ids.length-1){
                idsStr+=",";
            }
        }
        params.put("ids", idsStr);
        try{
            return JSONObject.parseObject(ApiUtils.postForObject(URL+"/mc/activity/delete", params, null));
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }





}
