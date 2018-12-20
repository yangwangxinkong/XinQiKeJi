/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.*;
import com.xss.dao.ProductDao;
import com.xss.domain.*;
import com.xss.domain.Order.OrderStatus;
import com.xss.domain.Order.PaymentStatus;
import com.xss.domain.Product;
import com.xss.domain.Product.OrderType;
import com.xss.domain.enums.ProductType;
import com.xss.util.*;
import com.xss.util.page.Filter;
import com.xss.util.page.Order;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Service - 商品
 * 
 * @author zzl
 * @version 1.0
 */
@Service
public class ProductService extends BaseService<Product, Long> {

	private static final Pattern pattern = Pattern.compile("\\d*");

	@PersistenceContext
	protected EntityManager entityManager;
	@Resource
	private ProductDao productDao;
	@Resource
	private GoodsService goodsService;
	@Resource
	private SnService snService;
	@Resource
	private SpecificationDao specificationDao;
	@Resource
	private SpecificationValueDao specificationValueDao;
	@Resource
	public void setBaseDao(ProductDao productDao) {
		super.setBaseDao(productDao);
	}

	@Override
	public void remove(Product product) {
		if (product != null) {
			Goods goods = product.getGoods();
			if (goods != null && goods.getProducts() != null) {
				goods.getProducts().remove(product);
				if (goods.getProducts().isEmpty()) {
					goodsService.remove(goods);
				}
			}
		}
		super.remove(product);
	}

	public boolean snExists(String sn) {
		if (sn == null) {
			return false;
		}
		String jpql = "select count(product) from Product product where lower(product.sn) = lower(:sn)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		return count > 0;
	}

	public Product findBySn(String sn) {
		if (sn == null) {
			return null;
		}
		String jpql = "select product from Product product where lower(product.sn) = lower(:sn)";
		try {
			return entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Product findSupervisorBySn(String sn, ProductType productTypeSupervisor, ProductType productTypeDetection) {
		if (sn == null) {
			return null;
		}
		String jpql = "select product from Product product where lower(product.sn) = lower(:sn) AND ( product.productType = :productTypeSupervisor OR  product.productType = :productTypeDetection)";
		try {
			return entityManager.createQuery(jpql, Product.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).setParameter("productTypeSupervisor", productTypeSupervisor).setParameter("productTypeDetection", productTypeDetection).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public boolean snUnique(String previousSn, String currentSn) {
		if (StringUtils.equalsIgnoreCase(previousSn, currentSn)) {
			return true;
		} else {
			if (snExists(currentSn)) {
				return false;
			} else {
				return true;
			}
		}
	}

	/**
	 * 所有商品关键词分页查询
	 * @param pageable 分页信息
	 * @param keyword 关键词
	 * @param isGift 是否为赠品
	 * @return
	 */
	public Page search(Pageable pageable, String keyword, Boolean isGift, Boolean isMarketable, ProductType productType,Long[] excludeIds) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
//		if (store!=null){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("store"), store));
//		}
		if (StringUtils.isNotBlank(keyword)){
			if (pattern.matcher(keyword).matches()) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword)), criteriaBuilder.like(root.<String> get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String> get("fullName"), "%" + keyword + "%")));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String> get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String> get("fullName"), "%" + keyword + "%")));
			}
		}
		if (isGift != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
		}
		if (isMarketable !=null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (productType !=null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("productType"), productType));
		}
		if (excludeIds !=null && excludeIds.length>0) {
			CriteriaBuilder.In<Object> in=criteriaBuilder.in(root.get("id"));
			for (Long id:excludeIds ) {
				in.value(id);
			}

			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(in));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
		return super.findPage(criteriaQuery, pageable);
	}


	/**
	 * 所有商品关键词搜索
	 * @param keyword 关键词
	 * @param isGift 是否为赠品
	 * @param count 获取数量
	 * @return
	 */
	public List<Product> search(String keyword, Boolean isGift, Integer count) {
		if (StringUtils.isEmpty(keyword)) {
			return Collections.<Product> emptyList();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (pattern.matcher(keyword).matches()) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword)), criteriaBuilder.like(root.<String> get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String> get("fullName"), "%" + keyword + "%")));
		} else {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String> get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String> get("fullName"), "%" + keyword + "%")));
		}
		if (isGift != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
		return super.findList(criteriaQuery, null, count, null, null);
	}

	/**
	 * 所有商品关键词搜索
	 * @param keyword 关键词
	 * @param productCategory 商品类型
	 * @param count 获取数量
	 * @return
	 */
	public List<Product> search(String keyword, ProductCategory productCategory, String cityCode, Integer count) {
		if (StringUtils.isEmpty(keyword)) {
			return Collections.<Product> emptyList();
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (pattern.matcher(keyword).matches()) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("id"), Long.valueOf(keyword)), criteriaBuilder.like(root.<String> get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String> get("fullName"), "%" + keyword + "%")));
		} else {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.like(root.<String> get("sn"), "%" + keyword + "%"), criteriaBuilder.like(root.<String> get("fullName"), "%" + keyword + "%")));
		}
		if (productCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
		}
		if (cityCode != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<String> get("cityCode"), cityCode));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
		return super.findList(criteriaQuery, null, count, null, null);
	}

	public List<Product> findList(ProductCategory productCategory, Brand brand, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (productCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
		}
		if (brand != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
		}
//		if (promotion != null) {
//			Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
//			Root<Product> subqueryRoot1 = subquery1.from(Product.class);
//			subquery1.select(subqueryRoot1);
//			subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));
//
//			Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
//			Root<Product> subqueryRoot2 = subquery2.from(Product.class);
//			subquery2.select(subqueryRoot2);
//			subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));
//
//			Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
//			Root<Product> subqueryRoot3 = subquery3.from(Product.class);
//			subquery3.select(subqueryRoot3);
//			subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));
//
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
//		}
		if (tags != null && !tags.isEmpty()) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (attributeValue != null) {
			for (Map.Entry<Attribute, String> entry : attributeValue.entrySet()) {
				String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number> get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number> get("price"), endPrice));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isGift != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
		}
		Path<Integer> stock = root.get("stock");
		Path<Integer> allocatedStock = root.get("allocatedStock");
		if (isOutOfStock != null) {
			if (isOutOfStock) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
			}
		}
//		if (isStockAlert != null) {
//			Setting setting = SettingUtils.get();
//			if (isStockAlert) {
//				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
//			} else {
//				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
//			}
//		}
		criteriaQuery.where(restrictions);
		if (orderType == OrderType.priceAsc) {
			orders.add(Order.asc("price"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.priceDesc) {
			orders.add(Order.desc("price"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.salesDesc) {
			orders.add(Order.desc("sales"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.scoreDesc) {
			orders.add(Order.desc("score"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.dateDesc) {
			orders.add(Order.desc("createDate"));
		} else {
			orders.add(Order.desc("isTop"));
			orders.add(Order.desc("modifyDate"));
		}
		return super.findList(criteriaQuery, null, count, filters, orders);
	}

	public List<Product> findList(ProductCategory productCategory, Date beginDate, Date endDate, Integer first, Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), true));
		if (productCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
		}
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("createDate"), endDate));
		}
		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
		return super.findList(criteriaQuery, first, count, null, null);
	}

	public List<Product> findList(Goods goods, Set<Product> excludes) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (goods != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("goods"), goods));
		}
		if (excludes != null && !excludes.isEmpty()) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.not(root.in(excludes)));
		}
		criteriaQuery.where(restrictions);
		return entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT).getResultList();
	}

	/**
	 * 社区拼团商品列表
	 * @param nowTime
	 * @param first
	 * @param count
	 * @param filters
	 * @param orders
	 * @return
	 */
	public List<Product> findList(Date nowTime , Integer first, Integer count, List<Filter> filters, List<Order> orders) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), true));
		//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isRecommend"), true));
		//restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("productType"), ProductType.group));

        if(nowTime != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(root.<Date> get("beginTime"), nowTime));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(root.<Date> get("endTime"), nowTime));
		}

		criteriaQuery.where(restrictions);
		criteriaQuery.orderBy(criteriaBuilder.desc(root.get("isTop")));
		return super.findList(criteriaQuery, first, count, filters, orders);
	}

	public List<Object[]> findSalesList(Date beginDate, Date endDate, Integer count) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
		Root<Product> product = criteriaQuery.from(Product.class);
		Join<Product, OrderItem> orderItems = product.join("orderItems");
		Join<Product, com.xss.domain.Order> order = orderItems.join("order");
		criteriaQuery.multiselect(product.get("id"), product.get("sn"), product.get("name"), product.get("fullName"), product.get("price"), criteriaBuilder.sum(orderItems.<Integer> get("quantity")), criteriaBuilder.sum(criteriaBuilder.prod(orderItems.<Integer> get("quantity"), orderItems.<BigDecimal> get("price"))));
		Predicate restrictions = criteriaBuilder.conjunction();
		if (beginDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.greaterThanOrEqualTo(order.<Date> get("createDate"), beginDate));
		}
		if (endDate != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.lessThanOrEqualTo(order.<Date> get("createDate"), endDate));
		}
//		if (null != store) {
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(product.get("store"), store));
//		}
		restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(order.get("orderStatus"), OrderStatus.completed), criteriaBuilder.equal(order.get("paymentStatus"), PaymentStatus.paid));
		criteriaQuery.where(restrictions);
		criteriaQuery.groupBy(product.get("id"), product.get("sn"), product.get("name"), product.get("fullName"), product.get("price"));
		criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.sum(criteriaBuilder.prod(orderItems.<Integer> get("quantity"), orderItems.<BigDecimal> get("price")))));
		TypedQuery<Object[]> query = entityManager.createQuery(criteriaQuery).setFlushMode(FlushModeType.COMMIT);
		if (count != null && count >= 0) {
			query.setMaxResults(count);
		}
		return query.getResultList();
	}

	public Page<Product> findPage(ProductCategory productCategory, Brand brand, List<Tag> tags, Map<Attribute, String> attributeValue, BigDecimal startPrice, BigDecimal endPrice, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert, OrderType orderType, ProductType productType, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
//		if (storeCategory != null) {
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("store").get("storeCategory"), storeCategory));
//		}
		if (productCategory != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.equal(root.get("productCategory"), productCategory), criteriaBuilder.like(root.get("productCategory").<String> get("treePath"), "%" + ProductCategory.TREE_PATH_SEPARATOR + productCategory.getId() + ProductCategory.TREE_PATH_SEPARATOR + "%")));
		}
		if (brand != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("brand"), brand));
		}
//		if (promotion != null) {
//			Subquery<Product> subquery1 = criteriaQuery.subquery(Product.class);
//			Root<Product> subqueryRoot1 = subquery1.from(Product.class);
//			subquery1.select(subqueryRoot1);
//			subquery1.where(criteriaBuilder.equal(subqueryRoot1, root), criteriaBuilder.equal(subqueryRoot1.join("promotions"), promotion));
//
//			Subquery<Product> subquery2 = criteriaQuery.subquery(Product.class);
//			Root<Product> subqueryRoot2 = subquery2.from(Product.class);
//			subquery2.select(subqueryRoot2);
//			subquery2.where(criteriaBuilder.equal(subqueryRoot2, root), criteriaBuilder.equal(subqueryRoot2.join("productCategory").join("promotions"), promotion));
//
//			Subquery<Product> subquery3 = criteriaQuery.subquery(Product.class);
//			Root<Product> subqueryRoot3 = subquery3.from(Product.class);
//			subquery3.select(subqueryRoot3);
//			subquery3.where(criteriaBuilder.equal(subqueryRoot3, root), criteriaBuilder.equal(subqueryRoot3.join("brand").join("promotions"), promotion));
//
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.exists(subquery1), criteriaBuilder.exists(subquery2), criteriaBuilder.exists(subquery3)));
//		}
		if (tags != null && !tags.isEmpty()) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (attributeValue != null) {
			for (Map.Entry<Attribute, String> entry : attributeValue.entrySet()) {
				String propertyName = Product.ATTRIBUTE_VALUE_PROPERTY_NAME_PREFIX + entry.getKey().getPropertyIndex();
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get(propertyName), entry.getValue()));
			}
		}
		if (startPrice != null && endPrice != null && startPrice.compareTo(endPrice) > 0) {
			BigDecimal temp = startPrice;
			startPrice = endPrice;
			endPrice = temp;
		}
		if (startPrice != null && startPrice.compareTo(new BigDecimal(0)) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.ge(root.<Number> get("price"), startPrice));
		}
		if (endPrice != null && endPrice.compareTo(new BigDecimal(0)) >= 0) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.le(root.<Number> get("price"), endPrice));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isGift != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
		}
		if (productType != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("productType"), productType));
		}
		Path<Integer> stock = root.get("stock");
		Path<Integer> allocatedStock = root.get("allocatedStock");
		if (isOutOfStock != null) {
			if (isOutOfStock) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
			}
		}
//		if (isStockAlert != null) {
//			Setting setting = SettingUtils.get();
//			if (isStockAlert) {
//				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
//			} else {
//				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
//			}
//		}
		criteriaQuery.where(restrictions);
		List<Order> orders = pageable.getOrders();
		if (orderType == OrderType.priceAsc) {
			orders.add(Order.asc("price"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.priceDesc) {
			orders.add(Order.desc("price"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.salesDesc) {
			orders.add(Order.desc("sales"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.scoreDesc) {
			orders.add(Order.desc("score"));
			orders.add(Order.desc("createDate"));
		} else if (orderType == OrderType.dateDesc) {
			orders.add(Order.desc("createDate"));
		} else {
			orders.add(Order.desc("isTop"));
			orders.add(Order.desc("modifyDate"));
		}
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Product> findPage(Member member, Pageable pageable) {
		if (member == null) {
			return new Page<Product>(Collections.<Product> emptyList(), 0, pageable);
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		criteriaQuery.where(criteriaBuilder.equal(root.join("favoriteMembers"), member));
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Product> findPage(Pageable pageable) {
//		if (store == null) {
//			return new Page<Product>(Collections.<Product> emptyList(), 0, pageable);
//		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
//		if (null != store){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("store"), store));
//		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Page<Product> findPage(Pageable pageable, List<Tag> tags, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift) {
		/*if (store == null) {
			return new Page<Product>(Collections.<Product> emptyList(), 0, pageable);
		}*/
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		//criteriaQuery.where(criteriaBuilder.equal(root.join("store"), store));

		Predicate restrictions = criteriaBuilder.conjunction();
//		if (null != store){
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("store"), store));
//		}
		if (tags != null && !tags.isEmpty()) {
			Subquery<Product> subquery = criteriaQuery.subquery(Product.class);
			Root<Product> subqueryRoot = subquery.from(Product.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root), subqueryRoot.join("tags").in(tags));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isGift != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
		}
		criteriaQuery.where(restrictions);
		return super.findPage(criteriaQuery, pageable);
	}

	public Long count(Member favoriteMember, Boolean isMarketable, Boolean isList, Boolean isTop, Boolean isGift, Boolean isOutOfStock, Boolean isStockAlert) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
		Root<Product> root = criteriaQuery.from(Product.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
//		if (store != null) {
//			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("store"), store));
//		}
		if (favoriteMember != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.join("favoriteMembers"), favoriteMember));
		}
		if (isMarketable != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isMarketable"), isMarketable));
		}
		if (isList != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isList"), isList));
		}
		if (isTop != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isTop"), isTop));
		}
		if (isGift != null) {
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("isGift"), isGift));
		}
		Path<Integer> stock = root.get("stock");
		Path<Integer> allocatedStock = root.get("allocatedStock");
		if (isOutOfStock != null) {
			if (isOutOfStock) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, allocatedStock));
			} else {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, allocatedStock)));
			}
		}
//		if (isStockAlert != null) {
//			Setting setting = SettingUtils.get();
//			if (isStockAlert) {
//				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.isNotNull(stock), criteriaBuilder.lessThanOrEqualTo(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount())));
//			} else {
//				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(criteriaBuilder.isNull(stock), criteriaBuilder.greaterThan(stock, criteriaBuilder.sum(allocatedStock, setting.getStockAlertCount()))));
//			}
//		}
		criteriaQuery.where(restrictions);
		return super.count(criteriaQuery, null);
	}

	public boolean isPurchased(Member member, Product product) {
		if (member == null || product == null) {
			return false;
		}
		String jqpl = "select count(*) from OrderItem orderItem where orderItem.product = :product and orderItem.order.member = :member and orderItem.order.orderStatus = :orderStatus";
		Long count = entityManager.createQuery(jqpl, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).setParameter("member", member).setParameter("orderStatus", OrderStatus.completed).getSingleResult();
		return count > 0;
	}

	@Override
	@Transactional
	public Product update(Product product) {
		Assert.notNull(product);
		Product pProduct = super.update(product);
		productDao.flush();
		return pProduct;
	}

	/**
	 * 设置值并更新
	 *
	 * @param product
	 *            商品
	 * @return 商品
	 */
	@Override
	public Product merge(Product product) {
		Assert.notNull(product);

		if (!product.getIsGift()) {
			String jpql = "delete from GiftItem giftItem where giftItem.gift = :product";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
		}
		if (!product.getIsMarketable() || product.getIsGift()) {
			String jpql = "delete from CartItem cartItem where cartItem.product = :product";
			entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
		}
		setValue(product);
		return super.merge(product);
	}

	/**
	 * 设置值
	 *
	 * @param product
	 *            商品
	 */
	private void setValue(Product product) {
		if (product == null) {
			return;
		}
		if (StringUtils.isEmpty(product.getSn())) {
			String sn;
			do {
				sn = snService.generate(Sn.Type.product);
			} while (snExists(sn));
			product.setSn(sn);
		}
		StringBuffer fullName = new StringBuffer(product.getName());
		if (product.getSpecificationValues() != null && !product.getSpecificationValues().isEmpty()) {
			List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>(product.getSpecificationValues());
			Collections.sort(specificationValues, new Comparator<SpecificationValue>() {
				@Override
				public int compare(SpecificationValue a1, SpecificationValue a2) {
					return new CompareToBuilder().append(a1.getSpecification(), a2.getSpecification()).toComparison();
				}
			});
			fullName.append(Product.FULL_NAME_SPECIFICATION_PREFIX);
			int i = 0;
			for (Iterator<SpecificationValue> iterator = specificationValues.iterator(); iterator.hasNext(); i++) {
				if (i != 0) {
					fullName.append(Product.FULL_NAME_SPECIFICATION_SEPARATOR);
				}
				fullName.append(iterator.next().getName());
			}
			fullName.append(Product.FULL_NAME_SPECIFICATION_SUFFIX);
		}
		product.setFullName(fullName.toString());
	}

	@Transactional
	public void addCommentNum(Long id) {
		Product product=super.find(id);
		if (product.getCommentNum() == null) {
			product.setCommentNum(0L);
		}
		product.setCommentNum(product.getCommentNum()+1);
		super.update(product);
	}

	@Transactional
	public void addPraiseNum(Long id) {
		Product product=super.find(id);
		if (product.getPraiseNum() == null) {
			product.setPraiseNum(0L);
		}
		product.setPraiseNum(product.getPraiseNum()+1);
		super.update(product);
	}

	@Transactional
	public void subPraiseNum(Long id) {
		Product product=super.find(id);
		if (product.getPraiseNum() == null) {
			product.setPraiseNum(0L);
		}
		product.setPraiseNum(product.getPraiseNum()-1);
		super.update(product);
	}

	/**
	 * 同货号商品规格值转换成商品列表
	 * @param products
	 * @param product
	 * @param persistenceProduct
	 * @param productJson
	 * @param cityCode
	 */
	public void convertSpecProducts(List<Product> products, Product product, Product persistenceProduct, com.alibaba.fastjson.JSONObject productJson, String cityCode){
		com.alibaba.fastjson.JSONArray specifications = productJson.getJSONArray("specificationList");
		//货号其它商品列表
		com.alibaba.fastjson.JSONArray slibingProducts = productJson.getJSONArray("specificationProducts");
		if (specifications != null && !specifications.isEmpty()) {
			/**
			 * 该货品有i个对应的商品
			 * i=0，对应的是本次更新的商品
			 * i>0，包含原有其它规格对应商品及新增或修改规格商品
			 */
			for (int i = 0; i < specifications.size(); i++) {
				if(i == 0){
					BeanUtils.copyProperties(product, persistenceProduct, new String[]{"id", "createDate", "modifyDate",
							"fullName", "allocatedStock", "score", "totalScore", "scoreCount", "hits",
							"weekHits", "monthHits", "sales", "weekSales", "monthSales", "weekHitsDate",
							"monthHitsDate", "weekSalesDate", "monthSalesDate", "goods", "reviews",
							"consultations", "favoriteMembers", "specifications", "specificationValues",
							"promotions", "cartItems", "orderItems", "giftItems", "productNotifies",
							"relationProducts", "supervisorOrderItems", "bespeaks"});
//					persistenceProduct.setCityCode(cityCode);
					com.alibaba.fastjson.JSONArray specificationTemps = specifications.getJSONArray(i);
					persistenceProduct.getSpecifications().clear();
					persistenceProduct.getSpecificationValues().clear();
					for (int j = 0; j < specificationTemps.size(); j++) {
						com.alibaba.fastjson.JSONObject specificationTemp = specificationTemps.getJSONObject(j);
						persistenceProduct.getSpecifications().add(specificationDao.findOne(specificationTemp.getLong("specificationId")));
						persistenceProduct.getSpecificationValues().add(specificationValueDao.findOne(specificationTemp.getLong("specificationValueId")));
					}
					products.add(persistenceProduct);
				}else{

					if(null != slibingProducts && !slibingProducts.isEmpty() && i<slibingProducts.size()){
						if(i<slibingProducts.size()){
							Long id = slibingProducts.getLong(i);
							Product slibingProduct = find(id);
							System.out.println("i:"+i + ";pid:" + id);
							System.out.println("goods id:" + slibingProduct.getGoods().getId() + ";gid:" + persistenceProduct.getGoods().getId());
							if (null == slibingProduct || (slibingProduct.getGoods() != null && !slibingProduct.getGoods().equals(persistenceProduct.getGoods()))){
								System.out.println("goods not equal goods");
								products = null;
								return ;
							}
							com.alibaba.fastjson.JSONArray specificationTemps = specifications.getJSONArray(i);
							for (int j = 0; j < specificationTemps.size(); j++) {
								com.alibaba.fastjson.JSONObject specificationTemp = specificationTemps.getJSONObject(j);
								slibingProduct.getSpecifications().add(specificationDao.findOne(specificationTemp.getLong("specificationId")));
								slibingProduct.getSpecificationValues().add(specificationValueDao.findOne(specificationTemp.getLong("specificationValueId")));
							}
							products.add(slibingProduct);
						}
					}else{
						//新增规格商品
						Product specificationProduct = new Product();
						BeanUtils.copyProperties(product, specificationProduct);
						specificationProduct.setId(null);
						specificationProduct.setCreateDate(null);
						specificationProduct.setModifyDate(null);
						specificationProduct.setSn(null);
						specificationProduct.setFullName(null);
						specificationProduct.setAllocatedStock(0);
						specificationProduct.setIsList(false);
						specificationProduct.setScore(0F);
						specificationProduct.setTotalScore(0L);
						specificationProduct.setScoreCount(0L);
						specificationProduct.setHits(0L);
						specificationProduct.setWeekHits(0L);
						specificationProduct.setMonthHits(0L);
						specificationProduct.setSales(0L);
						specificationProduct.setWeekSales(0L);
						specificationProduct.setMonthSales(0L);
						specificationProduct.setWeekHitsDate(new Date());
						specificationProduct.setMonthHitsDate(new Date());
						specificationProduct.setWeekSalesDate(new Date());
						specificationProduct.setMonthSalesDate(new Date());
						specificationProduct.setGoods(persistenceProduct.getGoods());
						specificationProduct.setReviews(null);
//						specificationProduct.setConsultations(null);
						specificationProduct.setFavoriteMembers(null);
						specificationProduct.setSpecifications(new HashSet<Specification>());
						specificationProduct.setSpecificationValues(new HashSet<SpecificationValue>());
//						specificationProduct.setPromotions(null);
						specificationProduct.setCartItems(null);
						specificationProduct.setOrderItems(null);
						specificationProduct.setGiftItems(null);
						specificationProduct.setProductNotifies(null);

//						Set<RelationProduct> rPs = new HashSet<>();
//						for (RelationProduct relationProduct : product.getRelationProducts()) {
//							RelationProduct rp = new RelationProduct();
//							rp.setProductId(relationProduct.getProductId());
//							rp.setProductName(relationProduct.getProductName());
//							rp.setPrice(relationProduct.getPrice());
//							rp.setOrder(relationProduct.getOrder());
//							rp.setRelation(specificationProduct);
//							rPs.add(rp);
//						}
//						specificationProduct.setRelationProducts(rPs);

//						specificationProduct.setCityCode(cityCode);
						com.alibaba.fastjson.JSONArray specificationTemps = specifications.getJSONArray(i);
						for (int j = 0; j < specificationTemps.size(); j++) {
							com.alibaba.fastjson.JSONObject specificationTemp = specificationTemps.getJSONObject(j);
							specificationProduct.getSpecifications().add(specificationDao.findOne(specificationTemp.getLong("specificationId")));
							specificationProduct.getSpecificationValues().add(specificationValueDao.findOne(specificationTemp.getLong("specificationValueId")));
						}
						products.add(specificationProduct);
					}

				}
			}
		}else{
			product.setSpecifications(null);
			product.setSpecificationValues(null);
			BeanUtils.copyProperties(product, persistenceProduct, new String[]{"id", "createDate", "modifyDate", "fullName",
					"allocatedStock", "score", "totalScore", "scoreCount", "hits", "weekHits", "monthHits", "sales",
					"weekSales", "monthSales", "weekHitsDate", "monthHitsDate", "weekSalesDate", "monthSalesDate",
					"goods", "reviews", "consultations", "favoriteMembers", "promotions", "cartItems",
					"orderItems", "giftItems", "productNotifies","relationProducts", "supervisorOrderItems", "bespeaks"});
//			persistenceProduct.setCityCode(cityCode);
			products.add(persistenceProduct);
		}
	}

}