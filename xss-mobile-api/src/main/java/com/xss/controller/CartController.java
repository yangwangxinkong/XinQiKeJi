/*
 *  
 *  
 *  
 */
package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Cart;
import com.xss.domain.CartItem;
import com.xss.domain.Member;
import com.xss.domain.Product;
import com.xss.service.CartItemService;
import com.xss.service.CartService;
import com.xss.service.ProductService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * Controller - 购物车
 * 
 * @author DannyZou
 * @version 1.0
 */
@RestController
@RequestMapping("/m/cart")
@Api(description="购物车模块")
public class CartController {

	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(CartController.class);

	@Autowired
	private ProductService productService;
	@Autowired
	private CartService cartService;
	@Autowired
	private CartItemService cartItemService;

	/**
	 * 立即购买，添加购物车
	 */
	@PostMapping("/buy")
	@ApiOperation(value="立即购买，添加购物车", notes="立即购买，添加购物车",produces = "application/json")
	@Log(description="立即购买，添加购物车接口:/m/cart/buy")
	public PublicResult<JSONObject> buy(Long id, Integer quantity, @CurrentUser Member member) {
		LOG.info("addFavorite param: " + id);
		PublicResult<JSONObject> result;
		try {

			if (quantity == null || quantity < 1) {
				return new PublicResult<JSONObject>(PublicResultConstant.PRODUCT_QUANTITY_NULL_ERROR, null);
			}
			Product product = productService.find(id);
			if (product == null) {
				return new PublicResult<JSONObject>(PublicResultConstant.PRODUCT_NOT_EXIST_ERROR, null);
			}
			if (!product.getIsMarketable()) {
				return new PublicResult<JSONObject>(PublicResultConstant.PRODUCT_NOT_MARKETABLE_ERROR, null);
			}
			if (product.getIsGift()) {
				return new PublicResult<JSONObject>(PublicResultConstant.PRODUCT_IS_GIFT_ERROR, null);
			}

			Cart cart = cartService.getCurrent(member);

			if (cart == null) {
				cart = new Cart();
				cart.setKey(UUID.randomUUID().toString() + DigestUtils.md5Hex(RandomStringUtils.randomAlphabetic(30)));
				cart.setMember(member);
				cartService.save(cart);
			}

			if (Cart.MAX_PRODUCT_COUNT != null && cart.getCartItems().size() >= Cart.MAX_PRODUCT_COUNT) {
				return new PublicResult<JSONObject>(PublicResultConstant.MAX_PRODUCT_COUNT_ERROR, null);
			}

			if (CartItem.MAX_QUANTITY != null && quantity > CartItem.MAX_QUANTITY) {
				return new PublicResult<JSONObject>(PublicResultConstant.AVAILABLE_STOCK_ERROR, null);
			}
			if (product.getStock() != null && quantity > product.getAvailableStock()) {
				return new PublicResult<JSONObject>(PublicResultConstant.AVAILABLE_STOCK_ERROR, null);
			}
			CartItem cartItem = new CartItem();
			cartItem.setQuantity(quantity);
			cartItem.setProduct(product);
			cartItem.setCart(cart);
			cartItemService.save(cartItem);
			cart.getCartItems().add(cartItem);

			JSONObject jo = new JSONObject();
			jo.put("cartItems",cartItem.getId());

			result = new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, jo);
		}catch (Exception e){
			LOG.error("get Product list error. {}", e);
			result = new PublicResult<JSONObject>(PublicResultConstant.FAILED, null);
		}
		LOG.debug("get Product list result = " + result.toString());
		return result;
	}

}