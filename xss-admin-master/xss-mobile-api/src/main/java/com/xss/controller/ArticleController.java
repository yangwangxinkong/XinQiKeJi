package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.ArticleCategoryDao;
import com.xss.domain.*;
import com.xss.domain.enums.AdPositionType;
import com.xss.service.AdPositionService;
import com.xss.service.ArticleCategoryService;
import com.xss.service.ArticleService;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.xss.config.ConfigInit.SYSTEM_CONFIGS;

/**
 * 移动端文章分类及文章相关接口
 * @author zzl
 * @date 2018/8/29
 */
@RestController
@RequestMapping("/m/article")
@Api(description="文章接口")
public class ArticleController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ArticleController.class);

    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private ArticleService articleService;
    @Resource
    private ArticleCategoryDao articleCategoryDao;

    /**
     * 获取指定文章分类下子分类列表
     * 适用于政策解读分类列表页
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取指定文章分类下子分类列表",notes = "获取指定文章分类下子分类列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/category/children")
    @Log(description="获取指定文章分类下子分类列表接口:/m/article/category/children")
    public PublicResult<JSONArray> getCategoryChildren(@RequestParam String code )throws Exception{
        JSONArray result = new JSONArray();
        try{
            ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(code);
            if (null != articleCategory && !articleCategory.getChildren().isEmpty()){
                result = articleCategoryService.createEntity().convertList(new ArrayList<ArticleCategory>(articleCategory.getChildren()), null);
                return new PublicResult<>(PublicResultConstant.SUCCESS, result);
            }else{
                return new PublicResult<>(PublicResultConstant.PARAM_ERROR, result);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("getCategoryChildren e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    /**
     * 获取多个指定文章分类下子分类列表
     * 适用于政策解读分类列表页
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取指定文章分类下子分类列表",notes = "获取指定文章分类下子分类列表",httpMethod = "GET",produces = "application/json")
    @GetMapping("/categories/children")
    @Log(description="获取指定文章分类下子分类列表接口:/m/article/categories/children")
    public PublicResult<JSONArray> getCategoriesChildren(@RequestParam String[] codes )throws Exception{
        JSONArray result = new JSONArray();
        try{
            for (String code : codes){
                ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(code);
                if (null != articleCategory ){
                    JSONObject categoryJo = articleCategory.convertEntity(articleCategory, null);
                    if (!articleCategory.getChildren().isEmpty()) {
                        JSONArray childrenJa = articleCategoryService.createEntity().convertList(new ArrayList<ArticleCategory>(articleCategory.getChildren()), null);
                        categoryJo.put("children", childrenJa);
                        result.add(categoryJo);
                    }else if (!articleCategory.getArticles().isEmpty()){
                        categoryJo.put("articles", articleService.createEntity().convertList(new ArrayList<Article>(articleCategory.getArticles()), null));
                        result.add(categoryJo);
                    }
                }
            }
            return new PublicResult<>(PublicResultConstant.SUCCESS, result);

        }catch (Exception e){
            e.printStackTrace();
            LOG.error("getCategoryChildren e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    /**
     * 根据文章分类编码获取子分类列表和每个子分类下第一篇文章
     * 适用于政策详情页面展示
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据文章分类编码获取子分类列表和每个子分类下第一篇文章",notes = "根据文章分类编码获取子分类列表和每个子分类下第一篇文章",httpMethod = "GET",produces = "application/json")
    @GetMapping("/category/children_article")
    @Log(description="根据文章分类编码获取子分类列表和每个子分类下第一篇文章接口:/m/article/category/children_article")
    public PublicResult<JSONArray> getCategoryChildrenAndArticle(String code, Integer count )throws Exception{
        JSONArray result = new JSONArray();
        try{
            ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(code);
            if (null != articleCategory && !articleCategory.getChildren().isEmpty()){
                for (ArticleCategory child: articleCategory.getChildren()){
                    JSONObject childJo = child.convertEntity(child, null);
                    if (null != child.getArticles() && !child.getArticles().isEmpty()){
                        //Article article = new ArrayList<Article>(child.getArticles()).get(0);
                        //JSONObject articleJo = article.convertEntity(article, null);

                        List articleList = new ArrayList<>();

                        if(count != null && child.getArticles().size() > count) {
                            int i = 0;
                            for (Article article : child.getArticles()) {
                                if(i >= count) {
                                    break;
                                }
                                articleList.add(article);
                                i++;
                            }
                        } else {
                            articleList = new ArrayList<Article>(child.getArticles());
                        }

                        childJo.put("articles", articleService.createEntity().convertList(articleList, new String[]{"createDate", "articleCategory"}));
                    }
                    result.add(childJo);
                }
                return new PublicResult<>(PublicResultConstant.SUCCESS, result);
            }else{
                return new PublicResult<>(PublicResultConstant.PARAM_ERROR, result);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("getCategoryChildren e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    /**
     * 根据文章分类编码获取子分类列表和每个子分类下第一篇文章
     * 适用于政策详情页面展示
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据文章分类编码获取子分类列表和每个子分类下第一篇文章",notes = "根据文章分类编码获取子分类列表和每个子分类下第一篇文章",httpMethod = "GET",produces = "application/json")
    @GetMapping("/category/category_article")
    @Log(description="根据文章分类编码获取子分类列表和每个子分类下第一篇文章接口:/m/article/category/category_article")
    public PublicResult<JSONArray> getCategoryAndArticle(String code, Integer count )throws Exception{
        JSONArray result = new JSONArray();
        try{
            ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(code);
            if (null != articleCategory && !articleCategory.getChildren().isEmpty()){
                for (ArticleCategory child: articleCategory.getChildren()){
                    JSONObject childJo = child.convertEntity(child, null);
                    if (null != child.getArticles() && !child.getArticles().isEmpty()){
                        //Article article = new ArrayList<Article>(child.getArticles()).get(0);
                        //JSONObject articleJo = article.convertEntity(article, null);

                        List articleList = new ArrayList<>();

                        if(count != null && child.getArticles().size() > count) {
                            int i = 0;
                            for (Article article : child.getArticles()) {
                                if(i >= count) {
                                    break;
                                }
                                articleList.add(article);
                                i++;
                            }
                        } else {
                            articleList = new ArrayList<Article>(child.getArticles());
                        }

                        childJo.put("articles", articleService.createEntity().convertList(articleList, new String[]{"createDate", "articleCategory"}));
                    }
                    result.add(childJo);
                }

            }else{
                if(null != articleCategory) {
                    JSONObject childJo = articleCategory.convertEntity(articleCategory, null);
                    List articleList = new ArrayList<Article>(articleCategory.getArticles());
                    childJo.put("articles", articleService.createEntity().convertList(articleList, new String[]{"createDate", "articleCategory"}));
                    result.add(childJo);
                }

            }

            return new PublicResult<>(PublicResultConstant.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("getCategoryChildren e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    /**
     * 根据文章分类编码获取分类下指定数量文章
     * 适用于政策详情页面展示
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据文章分类编码获取分类下指定数量文章",notes = "根据文章分类编码获取分类下指定数量文章",httpMethod = "GET",produces = "application/json")
    @GetMapping("articles")
    @Log(description="根据文章分类编码获取分类下指定数量文章:/m/article/articles")
    public PublicResult<JSONArray> getArticles(String code, Integer count )throws Exception{
        JSONArray result = new JSONArray();
        try {

            ArticleCategory articleCategory = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(code);

            if (null != articleCategory && !articleCategory.getArticles().isEmpty()) {
                List articleList = new ArrayList<>();

                if(count != null && articleCategory.getArticles().size() > count) {
                    int i = 0;
                    for (Article article : articleCategory.getArticles()) {
                        if(i >= count) {
                            break;
                        }
                        articleList.add(article);
                        i++;
                    }
                } else {
                    articleList = new ArrayList<Article>(articleCategory.getArticles());
                }

                result =  articleService.createEntity().convertList(articleList, new String[]{"createDate", "articleCategory"});

            } else {
                return new PublicResult<>(PublicResultConstant.PARAM_ERROR, result);
            }
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("getCategoryChildren e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }

        return new PublicResult<>(PublicResultConstant.SUCCESS, result);
    }

    /**
     * 获取文章详情
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取文章详情",notes = "获取文章详情",httpMethod = "GET",produces = "application/json")
    @GetMapping("/info")
    @Log(description="获取文章详情接口:/m/article/info")
    public PublicResult<JSONObject> getInfo(@RequestParam Long id )throws Exception{
        JSONObject result = new JSONObject();
        try{
            Article article = articleService.find(id);
            if(article != null) {
                result = article.convertEntity(article, new String[]{"createDate"});
            }
            return new PublicResult<>(PublicResultConstant.SUCCESS, result);

        }catch (Exception e){
            e.printStackTrace();
            LOG.error("getCategoryChildren e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    /**
     * 获取用户注册协议
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取用户注册协议",notes = "获取用户注册协议",httpMethod = "GET",produces = "application/json")
    @GetMapping("/agreement")
    @Log(description="获取用户注册协议接口:/m/article/agreement")
    public PublicResult<JSONObject> agreement()throws Exception{
        JSONObject result = null;
        try{
            Article article = articleService.getArticleDao().findByCode("agreement");
            result = article.convertEntity(article, null);
            return new PublicResult<>(PublicResultConstant.SUCCESS, result);
        }catch (Exception e){
            e.printStackTrace();
            LOG.error("agreement e" + e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }


    @ApiOperation(value="文章列表", notes="获取文章列表",produces = "application/json")
    @GetMapping("/getArticleList")
    @Log(description="平台中心获取文章列表接口:/m/article/getArticleList")
    public PageResult<Object> getArticleList(Pageable pageable, String code)throws Exception{
        LOG.debug("get Article list param = " + code.toString());
        PageResult<Object> result = null;
        try{
            ArticleCategory articleCategory = null;
            if(StringUtils.isNotEmpty(code)) {
                articleCategory = articleCategoryDao.findArticleCategoryByCode(code);
            }
            pageable.getFilters().add(Filter.eq("isPublication", true));

            Page<Article> articlePage = articleService.findPage(articleCategory, pageable);
            result = new PageResult<Object>(articlePage.getTotalPages(),articlePage.getPageNumber(),articlePage.getPageSize(), articleService.createEntity().convertList(articlePage.getContent(),new String[]{"createDate","articleCategory"}));
        }catch (Exception e){
            LOG.error("get Article list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Article list result = " + result.toString());
        return result;
    }
}
