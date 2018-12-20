package com.xss.service;

import com.xss.domain.Sn;
import com.xss.domain.Sn.Type;
import com.xss.util.FreemarkerUtils;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.io.IOException;

/**
 * Created by huperson on 2018/08/03.
 */
@Service
public class SnService implements InitializingBean {
    private HiloOptimizer productHiloOptimizer;
    private HiloOptimizer orderHiloOptimizer;
    private HiloOptimizer paymentHiloOptimizer;
    private HiloOptimizer refundsHiloOptimizer;
    private HiloOptimizer shippingHiloOptimizer;
    private HiloOptimizer returnsHiloOptimizer;
    private HiloOptimizer storeHiloOptimizer;
    private HiloOptimizer tourismHiloOptimizer;
    private HiloOptimizer centralPurchaseProductHiloOptimizer;

    @PersistenceContext
    private EntityManager entityManager;
    @Value("${sn.product.prefix}")
    private String productPrefix;
    @Value("${sn.product.maxLo}")
    private int productMaxLo;
    @Value("${sn.order.prefix}")
    private String orderPrefix;
    @Value("${sn.order.maxLo}")
    private int orderMaxLo;
    @Value("${sn.payment.prefix}")
    private String paymentPrefix;
    @Value("${sn.payment.maxLo}")
    private int paymentMaxLo;
    @Value("${sn.refunds.prefix}")
    private String refundsPrefix;
    @Value("${sn.refunds.maxLo}")
    private int refundsMaxLo;
    @Value("${sn.shipping.prefix}")
    private String shippingPrefix;
    @Value("${sn.shipping.maxLo}")
    private int shippingMaxLo;
    @Value("${sn.returns.prefix}")
    private String returnsPrefix;
    @Value("${sn.returns.maxLo}")
    private int returnsMaxLo;
    @Value("${sn.store.prefix}")
    private String storePrefix;
    @Value("${sn.store.maxLo}")
    private int storeMaxLo;
    @Value("${sn.tourism.prefix}")
    private String tourismPrefix;
    @Value("${sn.tourism.maxLo}")
    private int tourismMaxLo;
    @Value("${sn.centralPurchaseProduct.prefix}")
    private String centralPurchaseProductPrefix;
    @Value("${sn.centralPurchaseProduct.maxLo}")
    private int centralPurchaseProductMaxLo;

    @Override
    public void afterPropertiesSet() throws Exception {
        productHiloOptimizer = new HiloOptimizer(Type.product, productPrefix, productMaxLo);
        orderHiloOptimizer = new HiloOptimizer(Type.order, orderPrefix, orderMaxLo);
        paymentHiloOptimizer = new HiloOptimizer(Type.payment, paymentPrefix, paymentMaxLo);
        refundsHiloOptimizer = new HiloOptimizer(Type.refunds, refundsPrefix, refundsMaxLo);
        shippingHiloOptimizer = new HiloOptimizer(Type.shipping, shippingPrefix, shippingMaxLo);
        returnsHiloOptimizer = new HiloOptimizer(Type.returns, returnsPrefix, returnsMaxLo);
        storeHiloOptimizer = new HiloOptimizer(Type.store, storePrefix, storeMaxLo);
        tourismHiloOptimizer = new HiloOptimizer(Type.tourism, tourismPrefix, tourismMaxLo);
        centralPurchaseProductHiloOptimizer = new HiloOptimizer(Type.centralPurchaseProduct, centralPurchaseProductPrefix, centralPurchaseProductMaxLo);
    }

    public String generate(Type type) {
        Assert.notNull(type);
        if (type == Type.product) {
            return productHiloOptimizer.generate();
        } else if (type == Type.order) {
            return orderHiloOptimizer.generate();
        } else if (type == Type.payment) {
            return paymentHiloOptimizer.generate();
        } else if (type == Type.refunds) {
            return refundsHiloOptimizer.generate();
        } else if (type == Type.shipping) {
            return shippingHiloOptimizer.generate();
        } else if (type == Type.returns) {
            return returnsHiloOptimizer.generate();
        } else if (type == Type.store) {
            return storeHiloOptimizer.generate();
        } else if (type == Type.tourism) {
            return tourismHiloOptimizer.generate();
        } else if (type == Type.centralPurchaseProduct) {
            return centralPurchaseProductHiloOptimizer.generate();
        }
        return null;
    }

    private long getLastValue(Type type) {
        String jpql = "select sn from Sn sn where sn.type = :type";
        Sn sn = null;
        try{
            sn = entityManager.createQuery(jpql, Sn.class).setFlushMode(FlushModeType.COMMIT).setLockMode(LockModeType.PESSIMISTIC_WRITE).setParameter("type", type).getSingleResult();
        }catch (NoResultException e){
        }
        if(sn==null){
            sn = new Sn();
            sn.setType(type);
            sn.setLastValue(0L);
        }
        long lastValue = sn.getLastValue();
        sn.setLastValue(lastValue + 1);
        entityManager.merge(sn);
        return lastValue;
    }

    /**
     * 高低位算法
     */
    private class HiloOptimizer {

        private Type type;
        private String prefix;
        private int maxLo;
        private int lo;
        private long hi;
        private long lastValue;

        public HiloOptimizer(Type type, String prefix, int maxLo) {
            this.type = type;
            this.prefix = prefix != null ? prefix.replace("{", "${") : "";
            this.maxLo = maxLo;
            this.lo = maxLo + 1;
        }

        public synchronized String generate() {
            if (lo > maxLo) {
                lastValue = getLastValue(type);
                lo = lastValue == 0 ? 1 : 0;
                hi = lastValue * (maxLo + 1);
            }
            try {
                return FreemarkerUtils.process(prefix, null) + (hi + lo++);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            return String.valueOf(hi + lo++);
        }
    }
}
