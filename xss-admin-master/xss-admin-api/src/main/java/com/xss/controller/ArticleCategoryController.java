package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.ArticleCategory;
import com.xss.service.ArticleCategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *  文章分类接口
 * @author zzl
 * @since 2018-08-02
 */
@RestController
@RequestMapping("/api/article_category")
@Api(description="文章分类管理模块")
public class ArticleCategoryController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(NavigationController.class);
    @Autowired
    private ArticleCategoryService articleCategoryService;

    @ApiOperation(value="文章分类列表", notes="按层级获取所有文章分类，不需要传参",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取文章分类列表接口:/api/article_category/list")
    public PublicResult<JSONArray> list() throws Exception{
        JSONArray articleCategoryJa = new JSONArray();
        try{
            List<ArticleCategory> list = articleCategoryService.findRoots();
            articleCategoryJa = articleCategoryService.convertArticleCategories(list);
            return new PublicResult<>(PublicResultConstant.SUCCESS, articleCategoryJa);
        }catch (Exception e){
            LOG.error("get ArticleCategory list error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,null);
        }
    }

    @ApiOperation(value="文章分类详情", notes="通过id获取文章分类详情",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取文章分类接口:/api/article_category/info")
    public PublicResult<JSONObject> getInfo(Long id)throws Exception{
        try{
            ArticleCategory articleCategory = articleCategoryService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,articleCategoryService.createEntity().convertEntity(articleCategory,new String[]{"order","parent"}));
        }catch (Exception e){
            LOG.error("get ArticleCategory info error. {}", e);
            return new PublicResult<>(PublicResultConstant.DATA_ERROR,null);
        }
    }

    @ApiOperation(value="保存文章分类", notes="保存文章分类",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存文章分类接口:/api/article_category/save")
    public PublicResult<String> save(@RequestBody JSONObject articleCategoryObj)throws Exception{
        LOG.debug("articleCategoryObj = " + articleCategoryObj.toString());
        try{
            ArticleCategory articleCategory = new ArticleCategory();
            if (StringUtils.isEmpty(articleCategoryObj.getString("parent"))){
                articleCategory.setParent(null);
            }else{
                articleCategory.setParent(articleCategoryService.find(Long.parseLong(articleCategoryObj.getString("parent"))));
            }
            articleCategory.setName(articleCategoryObj.getString("name"));
            articleCategory.setSeoTitle(articleCategoryObj.getString("seoTitle"));
            articleCategory.setCode(articleCategoryObj.getString("code"));
            articleCategory.setSeoKeywords(articleCategoryObj.getString("seoKeywords"));
            articleCategory.setSeoDescription(articleCategoryObj.getString("seoDescription"));
            articleCategory.setImage(articleCategoryObj.getString("image"));
            articleCategory.setOrder(articleCategoryObj.getInteger("order"));
            if (StringUtils.isBlank(articleCategoryObj.getString("id"))){
//                ArticleCategory exist = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(articleCategory.getCode());
//                if (null != exist && null != exist.getId()){
//                    return new PublicResult<>(PublicResultConstant.ERROR,"文章分类编码已经存在");
//                }
                articleCategory.setTreePath(null);
                articleCategory.setGrade(null);
                articleCategory.setChildren(null);
                articleCategory.setArticles(null);
                articleCategoryService.save(articleCategory);
            }else {
                articleCategory.setId(articleCategoryObj.getLong("id"));
                if (articleCategory.getParent() != null) {
                    ArticleCategory parent = articleCategory.getParent();
                    if (parent.equals(articleCategory)) {
                        return new PublicResult<>(PublicResultConstant.ERROR,"文章分类父分类不能是自己");
                    }
                    List<ArticleCategory> children = articleCategoryService.findChildren(parent);
                    if (children != null && children.contains(parent)) {
                        return new PublicResult<>(PublicResultConstant.ERROR,"文章分类父分类不能是自己的子类");
                    }
                }
                articleCategoryService.update(articleCategory,"treePath", "grade", "children", "articles");
            }
        }catch (Exception e){
            LOG.error("save/update ArticleCategory  error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,"操作失败");
        }

        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    @ApiOperation(value="文章分类批量删除", notes="通过id批量删除文章分类",httpMethod = "GET")
    @GetMapping("/delete")
    @Log(description="平台中心删除文章分类列表接口:/api/article_category/delete")
    public PublicResult<Boolean> deleteById(@RequestParam Long[] ids) throws Exception{
        LOG.debug("ids ="+ids);
       articleCategoryService.delete(ids);
       return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }

    @ApiOperation(value="上级分类列表", notes="上级分类列表，不需要传参",produces = "application/json")
    @GetMapping("/tree")
    @Log(description="平台中心获取文章分类上级分类列表接口:/api/product_category/tree")
    public PublicResult<JSONArray> tree() throws Exception{
        JSONArray articleCategoryJa = new JSONArray();
        List<ArticleCategory> list = articleCategoryService.findTree();
        articleCategoryJa = articleCategoryService.createEntity().convertList(list, null);
        return new PublicResult<>(PublicResultConstant.SUCCESS, articleCategoryJa);
    }

    /**
     * 检查编号是否唯一
     */
    @GetMapping("/check_code")
    public PublicResult<Boolean> checkCode(Long id, @RequestParam String code) throws Exception{
        PublicResult<Boolean> result = null;
        if (StringUtils.isEmpty(code)) {
            result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS,false);
        }
        ArticleCategory exist = articleCategoryService.getArticleCategoryDao().findArticleCategoryByCode(code);
        if (null == exist){
            result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS,false);
        }else{
            if (null == id || !id .equals(exist.getId())){
                result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
            }else{
                result =  new PublicResult<Boolean>(PublicResultConstant.SUCCESS,false);
            }
        }
        return result;
    }
}
