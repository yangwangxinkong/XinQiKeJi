package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.Log;
import com.xss.annotation.ParamXssPass;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Article;
import com.xss.domain.Tag;
import com.xss.service.ArticleCategoryService;
import com.xss.service.ArticleService;
import com.xss.service.TagService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;

/**
 *  文章接口
 * @author Huang
 * @date 2018/8/7
 */
@RestController
@RequestMapping("/api/article")
@Api(description="文章管理模块")
public class ArticleController {
    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ArticleController.class);

    @Autowired
    private ArticleService articleService;
    @Autowired
    private ArticleCategoryService articleCategoryService;
    @Autowired
    private TagService tagService;

    @ApiOperation(value="文章列表", notes="获取文章列表",produces = "application/json")
    @GetMapping("/list")
    @Log(description="平台中心获取文章列表接口:/api/article/list")
    public PageResult<Object> list(Pageable pageable)throws Exception{
        LOG.debug("get Article list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
            Page<Article> articlePage = articleService.findPage(pageable);
            result = new PageResult<Object>((int)articlePage.getTotal(),articlePage.getPageNumber(),articlePage.getPageSize(), articleService.createEntity().convertList(articlePage.getContent(),new String[]{"createDate","articleCategory"}));
        }catch (Exception e){
            LOG.error("get Article list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Article list result = " + result.toString());
        return result;
    }

    @ApiOperation(value="文章详情", notes="通过id获取文章详情",produces = "application/json")
    @GetMapping("/info")
    @Log(description="平台中心获取文章详情接口:/api/article/info")
    public PublicResult<JSONObject> getInfo(Long id)throws Exception{
        LOG.debug("id = "+id);
        try{
            Article article = articleService.find(id);
            return new PublicResult<>(PublicResultConstant.SUCCESS,articleService.createEntity().convertEntity(article,new String[]{"articleCategory","tags"}));
        }catch (Exception e){
            LOG.error("get Article info error. {}", e);
            return new PublicResult<>(PublicResultConstant.DATA_ERROR,null);
        }
    }

    @ApiOperation(value="保存文章", notes="保存文章",produces = "application/json")
    @PostMapping("/save")
    @Log(description="平台中心保存文章接口:/api/article/save")
    @ParamXssPass
    public PublicResult<Boolean> save(@RequestBody JSONObject articleObj)throws Exception{
        LOG.debug("articleObj ="+articleObj.toString());
        try{
            Article article = new Article();
            if (articleObj.getLong("id") == null){
                Long[] tagIds=new Long[articleObj.getJSONArray("tagIds").size()];
                for(int i=0;i<articleObj.getJSONArray("tagIds").size();i++){
                    tagIds[i]=articleObj.getJSONArray("tagIds").getLong(i);
                }
                article.setArticleCategory(articleCategoryService.find(Long.parseLong(articleObj.getString("articleCategory"))));
                article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
                article.setCover(articleObj.getString("cover"));
                article.setIsPublication(articleObj.getBoolean("isPublication"));
                article.setCode(articleObj.getString("code"));
                article.setAuthor(articleObj.getString("author"));
                article.setIsTop(articleObj.getBoolean("isTop"));
                article.setSeoDescription(articleObj.getString("seoDescription"));
                article.setTitle(articleObj.getString("title"));
                article.setSeoTitle(articleObj.getString("seoTitle"));
                article.setSeoKeywords(articleObj.getString("seoKeywords"));
                article.setIntroduction(articleObj.getString("introduction"));
                article.setContent(articleObj.getString("content"));
                article.setHits(0L);
                article.setPageNumber(null);
                articleService.save(article);
            }else {
                Long[] tagIds=new Long[articleObj.getJSONArray("tagIds").size()];
                for(int i=0;i<articleObj.getJSONArray("tagIds").size();i++){
                    tagIds[i]=articleObj.getJSONArray("tagIds").getLong(i);
                }
                article.setArticleCategory(articleCategoryService.find(Long.parseLong(articleObj.getString("articleCategory"))));
                article.setTags(new HashSet<Tag>(tagService.findList(tagIds)));
                article.setId(articleObj.getLong("id"));
                article.setCover(articleObj.getString("cover"));
                article.setIsPublication(articleObj.getBoolean("isPublication"));
                article.setCode(articleObj.getString("code"));
                article.setAuthor(articleObj.getString("author"));
                article.setIsTop(articleObj.getBoolean("isTop"));
                article.setSeoDescription(articleObj.getString("seoDescription"));
                article.setTitle(articleObj.getString("title"));
                article.setSeoTitle(articleObj.getString("seoTitle"));
                article.setSeoKeywords(articleObj.getString("seoKeywords"));
                article.setIntroduction(articleObj.getString("introduction"));
                article.setContent(articleObj.getString("content"));
                articleService.update(article, "hits", "pageNumber");
            }
            return new PublicResult<>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("save Article info error. {}", e);
            return new PublicResult<>(PublicResultConstant.ERROR,false);
        }
    }

    @ApiOperation(value="文章批量删除", notes="文章批量删除",produces = "application/json")
    @GetMapping("/delete")
    @Log(description="平台中心获取文章列表接口:/api/article/delete")
    public PublicResult<Boolean> delete(@RequestParam Long[] ids)throws Exception{
        articleService.delete(ids);
        return new PublicResult<>(PublicResultConstant.SUCCESS,true);
    }
}
