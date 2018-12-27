package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xpath.internal.operations.Quo;
import com.xss.annotation.Log;
import com.xss.annotation.Pass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.*;
import com.xss.domain.enums.*;
import com.xss.service.*;
import com.xss.util.DateTimeUtil;
import com.xss.util.DateUtil;
import com.xss.util.JWTUtil;
import com.xss.util.JsonUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;
import java.util.Base64;

import static com.xss.config.ConfigInit.SYSTEM_CONFIGS;

/**
 *  PC端管理接口
 * @author hu
 * @since 2018-10-14
 */
@RestController
@RequestMapping("/m/web")
@Api(description="PC端管理模块")
public class WebController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(WebController.class);

    @Autowired
    private AdPositionService adPositionService;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CityService cityService;
    @Autowired
    private QuotationService quotationService;
    @Autowired
    private ResidenceTypeService residenceTypeService;
    @Autowired
    private ServiceFeeSettingService serviceFeeSettingService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ProposerService proposerService;

    /**
     * 获取首页广告位数据、最新公告、服务热线电话
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "首页数据集合接口",notes = "首页数据集合接口",httpMethod = "GET",produces = "application/json")
    @GetMapping("serviceCall")
    @Log(description="首页数据集合接口接口:/m/web/serviceCall")
    public PublicResult<JSONObject> serviceCall()throws Exception{
        JSONObject result = new JSONObject();
        try{
              //3、获取系统服务电话
            Configs serviceCallConfig = SYSTEM_CONFIGS.get(Configs.DEFAULT_SERVICE_CALL_CODE);
            result.put("serviceCall", null == serviceCallConfig ? "" : serviceCallConfig.getCodeValue());

            return new PublicResult<>(PublicResultConstant.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("Index list e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    /**
     * 获取PC首页显示数据
     * 获取首页广告位数据、文章，最新公告、社保公积金套餐，服务热线电话
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "首页数据集合接口",notes = "首页数据集合接口",httpMethod = "GET",produces = "application/json")
    @GetMapping("index")
    @Log(description="首页数据集合接口接口:/m/web/index")
    public PublicResult<JSONObject> index(String articleCategoryCode1, String articleCategoryCode2,
                                          String articleCategoryCode3, String articleCategoryCode4, String cityCode)throws Exception{
        JSONObject result = new JSONObject();
        try{
            //1、获取广告位广告列表数据
            AdPosition adPosition = adPositionService.getAdPositionDao().findAdPositionByType(AdPositionType.webIndexBanner);
            JSONArray adJo = getAdsByPosition(adPosition);
            result.put("ads", adJo);

            //2、获取最新公告数据
            Article article = null;
            Configs config = SYSTEM_CONFIGS.get(Configs.DEFAULT_NOTICE_CODE);
            if (null != config && StringUtils.isNotBlank(config.getCode())){
                ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(config.getCode());
                if (null != articleCategory && !articleCategory.getArticles().isEmpty()){
                    result.put("notice", articleService.createEntity().convertList(new ArrayList<>(articleCategory.getArticles()), new String[]{"createDate"}));
                }
            }

            //3、获取文章分类的前三条数据
            int count = 3;

            JSONArray articleLists = new JSONArray();
            articleLists.add(getArticles(articleCategoryCode1, count));
            articleLists.add(getArticles(articleCategoryCode2, count));
            articleLists.add(getArticles(articleCategoryCode3, count));

            result.put("articles", articleLists);

            //3、代缴倒计时：XX城市代缴倒计时：XX天XX时XX分XX秒
            Configs surrenderCountDown = SYSTEM_CONFIGS.get(Configs.SURRENDER_COUNT_DOWN);
            String StrSurrenderCountDown = null == surrenderCountDown ? "" : surrenderCountDown.getCodeValue();
            if(StringUtils.isNotEmpty(StrSurrenderCountDown)) {
                Calendar now = Calendar.getInstance();
                StrSurrenderCountDown = String.valueOf(now.get(Calendar.YEAR)) + "-" + String.valueOf(now.get(Calendar.MONTH) + 1) + "-" + String.format("%02d",Integer.valueOf(StrSurrenderCountDown));
                result.put("endDate", DateUtil.parse(StrSurrenderCountDown, DateTimeUtil.FMT_yyyyMMdd).getTime());
            }
            result.put("beginDate", System.currentTimeMillis());

            // 社保套餐
            JSONArray ssLists = new JSONArray();
            // 年套餐，半年套餐，季套餐，
            Quotation quotation = getQuotation(cityCode, FeeCategory.fc0);
            if(quotation != null) {
                // 一年套餐
                JSONObject js = new JSONObject();
                js = quotation.convertEntity(getFee(quotation, 12), new String[]{"personalAmount", "companyAmount", "allAmount", "orderAmount"});
                js.put("name", "社保年度套餐");
                ssLists.add(js);

                // 半年套餐
                js = quotation.convertEntity(getFee(quotation, 6), new String[]{"personalAmount", "companyAmount", "allAmount", "orderAmount"});
                js.put("name", "社保半年套餐");
                ssLists.add(js);

                // 季度套餐
                js = quotation.convertEntity(getFee(quotation, 3), new String[]{"personalAmount", "companyAmount", "allAmount", "orderAmount"});
                js.put("name", "社保季度套餐");
                ssLists.add(js);
            }
            result.put("sssets", ssLists);


            // 公积金套餐
            // 年套餐，半年套餐，季套餐，
            JSONArray gjLists = new JSONArray();
            // 年套餐，半年套餐，季套餐，
            Quotation quotationGJ = getQuotation(cityCode, FeeCategory.fc1);
            if(quotationGJ != null) {
                // 一年套餐
                JSONObject js = new JSONObject();
                js = quotationGJ.convertEntity(getFee(quotationGJ, 12), new String[]{"personalAmount", "companyAmount", "allAmount", "orderAmount"});
                js.put("name", "公积金年度套餐");
                gjLists.add(js);

                // 半年套餐
                js = quotationGJ.convertEntity(getFee(quotationGJ, 6), new String[]{"personalAmount", "companyAmount", "allAmount", "orderAmount"});
                js.put("name", "公积金半年套餐");
                gjLists.add(js);

                // 季度套餐
                js = quotationGJ.convertEntity(getFee(quotationGJ, 3), new String[]{"personalAmount", "companyAmount", "allAmount", "orderAmount"});
                js.put("name", "公积金季度套餐");
                gjLists.add(js);
            }
            result.put("gjsets", gjLists);

            //获取新闻资讯的前二十条数据
            count = 20;

            result.put("xwzxArticles", getArticles(articleCategoryCode4, count));

            //3、获取系统服务电话
            //Configs serviceCallConfig = SYSTEM_CONFIGS.get(Configs.DEFAULT_SERVICE_CALL_CODE);
            //result.put("serviceCall", null == serviceCallConfig ? "" : serviceCallConfig.getCodeValue());

            //4、获取系统配置的口号
            Configs webSloganConfig = SYSTEM_CONFIGS.get(Configs.DEFAULT_WEB_SLOGAN_CODE);
            String slogan = webSloganConfig.getCodeValue();
            if(StringUtils.isNotBlank(slogan)){
                String[] slogans = slogan.split(",");
                result.put("slogans", slogans);
            }

            //4、获取系统配置的口号
            Configs webVideoUrlConfig = SYSTEM_CONFIGS.get(Configs.WEB_VIDEO_URL);
            String webVideoUrl = webVideoUrlConfig.getCodeValue();
            if(StringUtils.isNotBlank(webVideoUrl)){
                result.put("webVideoUrl", webVideoUrl);
            }

            // 促销活动列表
            String[] status = new String[]{"auditThroughNotStart","underway","discontinue","end"};
            //}else if("list".equals(dataType)){
            Pageable pageable = new Pageable(null, null);

            JSONObject res = activityService.list(pageable, null, status, null, null);
            if(res!=null && res.getInteger("errcode")==0){
                JSONObject data = res.getJSONObject("data");
                JSONArray activityList = data.getJSONArray("activities");
                JSONArray activityListTemp = new JSONArray();
                if(activityList.size() > 2) {
                    for (int i = 0; i < 2; i++)
                    {
                        //向jsonarray 数组添加新的元素
                        activityListTemp.add(activityList.get(i));
                    }

                } else {
                    activityListTemp = activityList;
                }
                result.put("activities", activityListTemp);
            }

            return new PublicResult<>(PublicResultConstant.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("Index list e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    /**
     * 获取PC缴社保显示数据
     * 获取首页广告位数据、文章，最新公告、社保公积金套餐
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "首页数据集合接口",notes = "首页数据集合接口",httpMethod = "GET",produces = "application/json")
    @GetMapping("shbInfo")
    @Log(description="首页数据集合接口接口:/m/web/shbInfo")
    public PublicResult<JSONObject> shbInfo(String articleCategoryCode1, String articleCategoryCode2,
                                            String articleCategoryCode3, Integer count,
                                            String cityCode, String feeCategory)throws Exception{
        JSONObject result = new JSONObject();
        try{
            //1、获取广告位广告列表数据
            AdPosition adPosition;
            if(FeeCategory.fc0.equals(FeeCategory.valueOf(feeCategory))) {
                adPosition = adPositionService.getAdPositionDao().findAdPositionByType(AdPositionType.webSocialBanner);
            } else {
                adPosition = adPositionService.getAdPositionDao().findAdPositionByType(AdPositionType.wxProvidentBanner);
            }

            JSONArray adJo = getAdsByPosition(adPosition);
            result.put("ads", adJo);

            //3、获取文章分类的前三条数据
            if(count == null) {
                count = 5;
            }

            JSONArray articleLists = new JSONArray();
            articleLists.add(getArticles(articleCategoryCode1, count));
            articleLists.add(getArticles(articleCategoryCode2, count));
            articleLists.add(getArticles(articleCategoryCode3, count));

            result.put("articles", articleLists);

            // 社保套餐
            Configs shbSetSubTitleConfig;
            String StrShbSetSubTitle;
            String[] arrShbSetSubTitle;

            JSONArray ssLists = new JSONArray();
            // 年套餐，半年套餐，季套餐，
            Quotation quotation;
            String setName = "";
            if(FeeCategory.fc0.equals(FeeCategory.valueOf(feeCategory))) {
                quotation = getQuotation(cityCode, FeeCategory.fc0);
                setName = "社保";

                shbSetSubTitleConfig = SYSTEM_CONFIGS.get(Configs.SHB_SET_SUB_TITLE);
                StrShbSetSubTitle = null == shbSetSubTitleConfig ? "" : shbSetSubTitleConfig.getCodeValue();
                arrShbSetSubTitle = StrShbSetSubTitle.split(";");

            } else {
                quotation = getQuotation(cityCode, FeeCategory.fc1);
                setName = "公积金";

                shbSetSubTitleConfig = SYSTEM_CONFIGS.get(Configs.GJJ_SET_SUB_TITLE);
                StrShbSetSubTitle = null == shbSetSubTitleConfig ? "" : shbSetSubTitleConfig.getCodeValue();
                arrShbSetSubTitle = StrShbSetSubTitle.split(";");

            }
            if(quotation != null) {
                // 一年套餐
                JSONObject js = new JSONObject();
                js = quotation.convertEntity(getFee(quotation, 12), new String[]{"monthFee"});
                js.put("name", setName + "年度套餐");
                if(arrShbSetSubTitle.length > 0) {
                    js.put("subTitle", arrShbSetSubTitle[0]);
                }
                ssLists.add(js);

                // 半年套餐
                js = quotation.convertEntity(getFee(quotation, 6), new String[]{"monthFee"});
                js.put("name", setName + "半年套餐");
                if(arrShbSetSubTitle.length > 1) {
                    js.put("subTitle", arrShbSetSubTitle[1]);
                }
                ssLists.add(js);

                // 季度套餐
                js = quotation.convertEntity(getFee(quotation, 3), new String[]{"monthFee"});
                js.put("name", setName + "季度套餐");
                if(arrShbSetSubTitle.length > 2) {
                    js.put("subTitle", arrShbSetSubTitle[2]);
                }
                ssLists.add(js);

                // 月套餐
                js = quotation.convertEntity(getFee(quotation, 1), new String[]{"monthFee"});
                js.put("name", setName + "月度套餐");
                if(arrShbSetSubTitle.length > 3) {
                    js.put("subTitle", arrShbSetSubTitle[3]);
                }
                ssLists.add(js);
            }
            result.put("sssets", ssLists);

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

    /**
     * 3、获取文章分类下的前三条文章
     * @param articleCategoryCode
     * @param count
     * @return
     */
    private JSONObject getArticles(String articleCategoryCode, int count) {
        try{
            JSONObject article = new JSONObject();
            List articleList = new ArrayList<>();
            ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(articleCategoryCode);

            if(null != articleCategory) {
                article = articleCategory.convertEntity(articleCategory, null);
            }

            if(null != articleCategory && !articleCategory.getArticles().isEmpty()){
                if(articleCategory.getArticles().size() > count) {
                    int i = 0;
                    for (Article articleTemp : articleCategory.getArticles()) {
                        if(i >= count) {
                            break;
                        }
                        articleList.add(articleTemp);
                        i++;
                    }
                } else {
                    articleList = new ArrayList<Article>(articleCategory.getArticles());
                }
            }

            if (null != articleCategory && !articleCategory.getChildren().isEmpty() && articleCategory.getArticles().size() < count) {
                for (ArticleCategory child : articleCategory.getChildren()) {
                    if (null != child.getArticles() && !child.getArticles().isEmpty()) {
                        if (child.getArticles().size() > count - articleCategory.getArticles().size()) {
                            int i = 0;
                            for (Article articleTemp : child.getArticles()) {
                                if (i >= count - articleCategory.getArticles().size()) {
                                    break;
                                }
                                articleList.add(articleTemp);
                                i++;
                            }
                        } else {
                            List<Article> lists = new ArrayList<Article>(child.getArticles());
                            for(Article articleTemp : lists) {
                                articleList.add(articleTemp);
                            }
                        }
                    }
                }
            }

            article.put("articles", articleService.createEntity().convertList(articleList, new String[]{"createDate", "articleCategory"}));

            return article;
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取一个月的社保计算金额
     *
     * @param cityCode
     * @return
     */
    private Quotation getQuotation(String cityCode, FeeCategory feeCategory) {
        Quotation quotation = new Quotation();
        City city = cityService.getCityByCode(cityCode);
        quotation.setCity(city);
        // 缴费月数
        quotation.setMonthCount(1);
        // 缴费方式 pc0(0, "新参"),pc1(1, "续缴"),pc2(2, "补缴");
        quotation.setPayCategory(PayCategory.pc0);
        // 社保
        quotation.setFeeCategory(feeCategory);
        // 户口性质
        List<ResidenceType> residenceTypeList = residenceTypeService.getResidenceTypeByCityId(city.getId());
        if(residenceTypeList != null && residenceTypeList.size() > 0) {
            quotation.setResidenceType(residenceTypeList.get(0));
        }
        quotationService.setCalculateQuotation(quotation, CalculatorCategory.cc1);

        return quotation;
    }

    @ApiOperation(value="保存申请人信息", notes="body体参数,不需要Authorization",produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @PostMapping("/proposer/save")
    @Log(description="PC保存申请人信息接口:/m/web/proposer/save")
    @Pass
    public PublicResult<Map<String, Object>> saveProposer(@RequestParam String phone, @RequestParam String name, HttpServletRequest request, HttpServletResponse response)throws Exception {

        try {
            if (StringUtils.isEmpty(phone) || StringUtils.isEmpty(name)) {
                return new PublicResult<>(PublicResultConstant.PARAM_ERROR, null);
            }
            Proposer proposer = new Proposer();
            proposer.setName(name);
            proposer.setPhone(phone);
            proposerService.save(proposer);

            return new PublicResult<>(PublicResultConstant.SUCCESS, null);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PublicResult<>(PublicResultConstant.ERROR, null);
    }
    /**
     * 一个月的费用计算 套餐价格
     * @param quotationTemp
     * @param monthCount
     * @return
     */
    private Quotation getFee(Quotation quotationTemp, int monthCount) {

        Quotation quotation = new Quotation();

        quotation.setCity(quotationTemp.getCity());
        // 缴费月数
        quotation.setMonthCount(monthCount);
        // 缴费方式 pc0(0, "新参"),pc1(1, "续缴"),pc2(2, "补缴");
        quotation.setPayCategory(quotationTemp.getPayCategory());
        // 社保
        quotation.setFeeCategory(quotationTemp.getFeeCategory());
        // 户口性质
        quotation.setResidenceType(quotationTemp.getResidenceType());

        quotation.setPersonSocialAmount(quotationTemp.getPersonSocialAmount());
        quotation.setCompanySocialAmount(quotationTemp.getCompanySocialAmount());
        quotation.setPersonProvidentAmount(quotationTemp.getPersonProvidentAmount());
        quotation.setCompanyProvidentAmount(quotationTemp.getCompanyProvidentAmount());

        // 社保
        if(quotation.getFeeCategory().equals(FeeCategory.fc0) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
            quotation.setPersonSocialAmount(quotationTemp.getPersonSocialAmount().multiply(BigDecimal.valueOf(monthCount)));
            quotation.setCompanySocialAmount(quotationTemp.getCompanySocialAmount().multiply(BigDecimal.valueOf(monthCount)));

            // 公积金
        } else if(quotation.getFeeCategory().equals(FeeCategory.fc1) || quotation.getFeeCategory().equals(FeeCategory.fc2)) {
            quotation.setPersonProvidentAmount(quotationTemp.getPersonProvidentAmount().multiply(BigDecimal.valueOf(monthCount)));
            quotation.setCompanyProvidentAmount(quotationTemp.getCompanyProvidentAmount().multiply(BigDecimal.valueOf(monthCount)));
        }

        ServiceFeeSetting serviceFeeSetting = serviceFeeSettingService.getServiceFeeSettings(quotation);
        if(serviceFeeSetting != null) {
            quotation.setMonthFee(serviceFeeSetting.getMonthFee());
            quotation.setFee(serviceFeeSetting.getFee());
        } else {
            quotation.setMonthFee(BigDecimal.ZERO);
            quotation.setFee(BigDecimal.ZERO);
        }
        // 设置总费用
        quotation.setAmount(quotationTemp.getAllAmount().add(quotation.getFee()));

        return quotation;
    }
}
