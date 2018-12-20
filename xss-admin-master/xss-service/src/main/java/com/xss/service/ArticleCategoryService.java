/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.dao.ArticleCategoryDao;
import com.xss.domain.ArticleCategory;
import com.xss.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service - 文章分类
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class ArticleCategoryService extends BaseService<ArticleCategory, Long>{

	@Resource
	private ArticleCategoryDao articleCategoryDao;

	@Resource
	public void setBaseDao(ArticleCategoryDao articleCategoryDao) {
		super.setBaseDao(articleCategoryDao);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findRoots() {
		return findRoots(null);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findRoots(Integer count) {
		String jpql = "select articleCategory from ArticleCategory articleCategory where articleCategory.parent is null order by articleCategory.order asc";
		TypedQuery<ArticleCategory> query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findParents(ArticleCategory articleCategory) {
		return findParents(articleCategory, null);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findParents(ArticleCategory articleCategory, Integer count) {
		if (articleCategory == null || articleCategory.getParent() == null) {
			return Collections.<ArticleCategory> emptyList();
		}
		String jpql = "select articleCategory from ArticleCategory articleCategory where articleCategory.id in (:ids) order by articleCategory.grade asc";
		TypedQuery<ArticleCategory> query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("ids", articleCategory.getTreePaths());
		if (count != null) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findTree() {
		return findChildren(null, null);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findChildren(ArticleCategory articleCategory) {
		return findChildren(articleCategory, null);
	}

	@Transactional(readOnly = true)
	public List<ArticleCategory> findChildren(ArticleCategory articleCategory, Integer count) {
		TypedQuery<ArticleCategory> query;
		if (articleCategory != null) {
			String jpql = "select articleCategory from ArticleCategory articleCategory where articleCategory.treePath like :treePath order by articleCategory.order asc";
			query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT).setParameter("treePath", "%" + ArticleCategory.TREE_PATH_SEPARATOR + articleCategory.getId() + ArticleCategory.TREE_PATH_SEPARATOR + "%");
		} else {
			String jpql = "select articleCategory from ArticleCategory articleCategory order by articleCategory.order asc";
			query = entityManager.createQuery(jpql, ArticleCategory.class).setFlushMode(FlushModeType.COMMIT);
		}
		if (count != null) {
			query.setMaxResults(count);
		}
		return sort(query.getResultList(), articleCategory);
	}

	@Transactional
	public void save(ArticleCategory articleCategory) {
		setValue(articleCategory);
		super.persist(articleCategory);
	}

	/**
	 * 排序文章分类
	 *
	 * @param articleCategories
	 *            文章分类
	 * @param parent
	 *            上级文章分类
	 * @return 文章分类
	 */
	private List<ArticleCategory> sort(List<ArticleCategory> articleCategories, ArticleCategory parent) {
		List<ArticleCategory> result = new ArrayList<ArticleCategory>();
		if (articleCategories != null) {
			for (ArticleCategory articleCategory : articleCategories) {
				if ((articleCategory.getParent() != null && articleCategory.getParent().equals(parent)) || (articleCategory.getParent() == null && parent == null)) {
					result.add(articleCategory);
					result.addAll(sort(articleCategories, articleCategory));
				}
			}
		}
		return result;
	}

	/**
	 * 设置值
	 *
	 * @param articleCategory
	 *            文章分类
	 */
	private void setValue(ArticleCategory articleCategory) {
		if (articleCategory == null) {
			return;
		}
		ArticleCategory parent = articleCategory.getParent();
		if (parent != null) {
			articleCategory.setTreePath(parent.getTreePath() + parent.getId() + ArticleCategory.TREE_PATH_SEPARATOR);
		} else {
			articleCategory.setTreePath(ArticleCategory.TREE_PATH_SEPARATOR);
		}
		articleCategory.setGrade(articleCategory.getTreePaths().size());
	}

	/**
	 * 设置treePath、grade并更新
	 *
	 * @param articleCategory
	 *            文章分类
	 * @return 文章分类
	 */
	@Override
	public ArticleCategory merge(ArticleCategory articleCategory) {
		Assert.notNull(articleCategory);
		setValue(articleCategory);
		for (ArticleCategory category : findChildren(articleCategory, null)) {
			setValue(category);
		}
		return super.merge(articleCategory);
	}

	public ArticleCategoryDao getArticleCategoryDao() {
		return articleCategoryDao;
	}

	public void setArticleCategoryDao(ArticleCategoryDao articleCategoryDao) {
		this.articleCategoryDao = articleCategoryDao;
	}

	public boolean codeExists(String code) {
		if (code == null) {
			return false;
		}
		String jpql = "select count(*) from ArticleCategory articleCategory where lower(articleCategory.code) = lower(:code)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("code", code).getSingleResult();
		return count > 0;
	}

	public JSONArray convertArticleCategories(List<ArticleCategory> articleCategories){
		JSONArray array = new JSONArray();
		if (null != articleCategories && !articleCategories.isEmpty()){
			for (ArticleCategory articleCategory : articleCategories){
				JSONObject jo = convertArticleCategory(articleCategory);
				jo.put("children", convertArticleCategories(new ArrayList<ArticleCategory>(articleCategory.getChildren())));
				array.add(jo);
			}
		}
		return array;
	}

	public JSONObject convertArticleCategory(ArticleCategory articleCategory){
		JSONObject jo = JsonUtil.toJSONObject(articleCategory, new String[]{"id", "name", "code","grade", "order"});
		jo.put("path",articleCategory.getPath());
		if (null != articleCategory.getParent()){
			jo.put("parent", articleCategory.getParent().getId());
		}
		return jo;
	}
}