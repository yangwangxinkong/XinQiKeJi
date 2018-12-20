/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.dao.GoodsDao;
import com.xss.domain.Goods;
import com.xss.domain.Product;
import com.xss.domain.Sn;
import com.xss.domain.SpecificationValue;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.CompareToBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.*;


/**
 * Service - 货品
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service("goodsServiceImpl")
public class GoodsService extends BaseService<Goods, Long> {

	@Resource
	private GoodsDao goodsDao;
	@Resource
	public void setBaseDao(GoodsDao goodsDao) {
		super.setBaseDao(goodsDao);
	}
	@Resource
	private SnService snService;
	@Resource
	private ProductService productService;


	@Override
	@Transactional
	public void save(Goods goods) {
		Assert.notNull(goods);
		if (goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {
				setValue(product);
			}
		}
		super.save(goods);
	}

	@Override
	@Transactional
	public Goods update(Goods goods) {
		Assert.notNull(goods);
		if (goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {
				setValue(product);
			}
		}
//		Set<Product> excludes = new HashSet<Product>();
//		CollectionUtils.select(goods.getProducts(), new Predicate() {
//			public boolean evaluate(Object object) {
//				Product product = (Product) object;
//				return product != null && product.getId() != null;
//			}
//		}, excludes);
//		List<Product> products = productService.findList(goods, excludes);
		Goods pGoods = super.update(goods);
		goodsDao.flush();
		return pGoods;
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
			} while (productService.snExists(sn));
			product.setSn(sn);
		}
		StringBuffer fullName = new StringBuffer(product.getName());
		if (product.getSpecificationValues() != null && !product.getSpecificationValues().isEmpty()) {
			List<SpecificationValue> specificationValues = new ArrayList<SpecificationValue>(product.getSpecificationValues());
			Collections.sort(specificationValues, new Comparator<SpecificationValue>() {
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

	/**
	 * 设置值并更新
	 *
	 * @param goods
	 *            货品
	 * @return 货品
	 */
	@Override
	public Goods merge(Goods goods) {
		Assert.notNull(goods);

		if (goods.getProducts() != null) {
			for (Product product : goods.getProducts()) {
				if (product.getId() != null) {
					if (!product.getIsGift()) {
						String jpql = "delete from GiftItem giftItem where giftItem.gift = :product";
						entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
					}
					if (!product.getIsMarketable() || product.getIsGift()) {
						String jpql = "delete from CartItem cartItem where cartItem.product = :product";
						entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("product", product).executeUpdate();
					}
				}
				setValue(product);
			}
		}
		return super.merge(goods);
	}

}