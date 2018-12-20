package com.xss.service;

import com.xss.dao.CartDao;
import com.xss.domain.Cart;
import com.xss.domain.CartItem;
import com.xss.domain.Member;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import java.util.Date;

@Service
public class CartService extends BaseService<Cart, Long> {

    @Resource
    private CartDao cartDao;
    @Resource
    public void setBaseDao(CartDao cartDao) {
        super.setBaseDao(cartDao);
    }

    @Transactional
    public Cart getCurrent(Member member) {

        if (member != null) {
            Cart cart = member.getCart();
            if (cart != null) {
                if (!cart.hasExpired()) {
                    if (!DateUtils.isSameDay(cart.getModifyDate(), new Date())) {
                        cart.setModifyDate(new Date());
                        merge(cart);
                    }
                    return cart;
                } else {
                    remove(cart);
                }
            }
        }

        return null;
    }

    @Override
    public void remove(Cart entity) {
        Cart current = cartDao.findOne(entity.getId());
        if (current.getCartItems().size() != entity.getCartItems().size()) {
            //只删除指定的购物项
            for (CartItem cartItem : entity.getCartItems()) {
                String jpql = "delete from CartItem cartItem where cartItem.id = :cartItemId";
                entityManager.createQuery(jpql).setFlushMode(FlushModeType.COMMIT).setParameter("cartItemId", cartItem.getId()).executeUpdate();
            }
        }else{
            super.remove(current);
        }
    }

}
