package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.*;
import com.xss.domain.enums.AdPositionType;
import com.xss.service.*;
import com.xss.util.DateTimeUtil;
import com.xss.util.DateUtil;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.xss.config.ConfigInit.SYSTEM_CONFIGS;

/**
 * 移动端首页接口
 * @author zzl
 * @date 2018/8/29
 */
@RestController
@RequestMapping("/m/index")
@Api(description="首页接口")
public class IndexController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(IndexController.class);

    @Autowired
    private AdPositionService adPositionService;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private ActivityService activityService;

    /**
     * 获取首页广告位数据、最新公告、服务热线电话
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "首页数据集合接口",notes = "首页数据集合接口",httpMethod = "GET",produces = "application/json")
    @GetMapping("")
    @Log(description="首页数据集合接口接口:/m/index")
    public PublicResult<JSONObject> index()throws Exception{
        JSONObject result = new JSONObject();
        try{
            //1、获取广告位广告列表数据
            AdPosition adPosition = adPositionService.getAdPositionDao().findAdPositionByType(AdPositionType.wxIndexBanner);
            JSONArray adJo = getAdsByPosition(adPosition);
            result.put("ads", adJo);

            // 获取政策解读广告位广告列表数据
            adJo = new JSONArray();
            AdPosition adPositionConsulting = adPositionService.getAdPositionDao().findAdPositionByType(AdPositionType.wxConsultingBanner);
            adJo = getAdsByPosition(adPositionConsulting);
            result.put("consultingAds", adJo);

            //2、获取最新公告数据
            Article article = null;
            Configs config = SYSTEM_CONFIGS.get(Configs.DEFAULT_NOTICE_CODE);
            if (null != config && StringUtils.isNotBlank(config.getCode())){
                ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(config.getCode());
                if (null != articleCategory && !articleCategory.getArticles().isEmpty()){
                    result.put("notice", articleService.createEntity().convertList(new ArrayList<>(articleCategory.getArticles()), new String[]{"createDate", "articleCategory"}));

//                    for (Article a : articleCategory.getArticles()){
//                        if(null == article){
//                            article = a;
//                            break;
//                        }
//                    }
                }
            }

/*            //3、获取新闻资讯的前十条数据
            ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(config.getCode());
            if (null != articleCategory && !articleCategory.getArticles().isEmpty()){
                result.put("notice", articleService.createEntity().convertList(new ArrayList<>(articleCategory.getArticles()), new String[]{"createDate", "articleCategory"}));

//                    for (Article a : articleCategory.getArticles()){
//                        if(null == article){
//                            article = a;
//                            break;
//                        }
//                    }
            }*/

            //3、获取系统服务电话
            Configs serviceCallConfig = SYSTEM_CONFIGS.get(Configs.DEFAULT_SERVICE_CALL_CODE);
            result.put("serviceCall", null == serviceCallConfig ? "" : serviceCallConfig.getCodeValue());

            //3、代缴倒计时：XX城市代缴倒计时：XX天XX时XX分XX秒
            Configs surrenderCountDown = SYSTEM_CONFIGS.get(Configs.SURRENDER_COUNT_DOWN);
            String StrSurrenderCountDown = null == surrenderCountDown ? "" : surrenderCountDown.getCodeValue();
            if(StringUtils.isNotEmpty(StrSurrenderCountDown)) {
                Calendar now = Calendar.getInstance();
                StrSurrenderCountDown = String.valueOf(now.get(Calendar.YEAR)) + "-" + String.valueOf(now.get(Calendar.MONTH) + 1) + "-" + String.format("%02d",Integer.valueOf(StrSurrenderCountDown));
                result.put("endDate", DateUtil.parse(StrSurrenderCountDown, DateTimeUtil.FMT_yyyyMMdd).getTime());
            }
            result.put("beginDate", System.currentTimeMillis());

            // 促销活动列表
            String[] status = new String[]{"auditThroughNotStart","underway","discontinue","end"};
            //}else if("list".equals(dataType)){
            Pageable pageable = new Pageable(null, null);

            JSONObject res = activityService.list(pageable, null, status, null, null);
            if(res!=null && res.getInteger("errcode")==0){
                JSONObject data = res.getJSONObject("data");
                JSONArray activityList = data.getJSONArray("activities");
                JSONArray activityListTemp = new JSONArray();
                if(activityList.size() > 4) {
                    for (int i = 0; i < 4; i++)
                    {
                        //向jsonarray 数组添加新的元素
                        activityListTemp.add(activityList.get(i));
                    }

                } else {
                    activityListTemp = activityList;
                }
                result.put("activities", activityListTemp);
            }

            //4、获取系统配置的口号
            Configs wxSloganConfig = SYSTEM_CONFIGS.get(Configs.DEFAULT_WX_SLOGAN_CODE);
            String slogan = wxSloganConfig.getCodeValue();
            if(StringUtils.isNotBlank(slogan)){
                String[] slogans = slogan.split(",");
                result.put("slogans", slogans);
            }

            return new PublicResult<>(PublicResultConstant.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("Index list e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    private JSONArray getAdsByPosition(AdPosition adPosition){
        JSONArray ja = new JSONArray();
        if (null != adPosition && null != adPosition.getAds() && !adPosition.getAds().isEmpty()){
            for(Ad ad : adPosition.getAds()){
                if (ad.hasBegun() && !ad.hasEnded()){
                    JSONObject adJo = new JSONObject();
                    if (Ad.Type.image.equals(ad.getType())) {
                        adJo.put("url", StringUtils.isBlank(ad.getUrl()) ? "javascript:;" : ad.getUrl());
                        adJo.put("img", ad.getPath());
                    }
                    ja.add(adJo);
                }
            }
        }
        return ja;
    }

}
