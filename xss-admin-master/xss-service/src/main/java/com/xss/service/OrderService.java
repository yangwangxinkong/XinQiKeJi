package com.xss.service;

import com.xss.dao.*;
import com.xss.domain.*;
import com.xss.domain.Order.OrderStatus;
import com.xss.domain.Order.PaymentStatus;
import com.xss.domain.Order.ShippingStatus;
import com.xss.domain.enums.FeeCategory;
import com.xss.domain.enums.InvoiceStatus;
import com.xss.util.DateTimeUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 *  Service - 订单
 * @author hu
 * @date 2018/8/29
 */
@Service
public class OrderService extends BaseService<Order,Long> {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderLogDao orderLogDao;
    @Resource
    private DepositDao depositDao;
    @Resource
    private MemberDao memberDao;

    @Autowired
    private QuotationService quotationService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PaymentService paymentService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private SnService snService;
    @Autowired
    private CartService cartService;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ShippingDao shippingDao;
    @Autowired
    private CouponCodeService couponCodeService;
    @Autowired
    private ShareService shareService;
    @Resource
    public void setBaseDao(OrderDao orderDao) {
        super.setBaseDao(orderDao);
    }

    @Transactional(readOnly = true)
    public Order findBySn(String sn) {
        if (sn == null) {
            return null;
        }
        String jpql = "select orders from Order orders where lower(orders.sn) = lower(:sn)";
        try {
            return entityManager.createQuery(jpql, Order.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public Long findByType(Order.OrderType type) {
        if (type == null) {
            return null;
        }

        String jpql = "select count(*) from Order orders where orders.orderType = :type";
        return entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("type", type).getSingleResult();
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Order order, Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Order.ShippingStatus shippingStatus, Boolean hasExpired, Pageable pageable) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();
        if (null != order) {
            if (null != order.getMember()) {
                if (StringUtils.isNotBlank(order.getMember().getUsername())) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("member").<String>get("username"), "%" + order.getMember().getUsername() + "%"));
                }
                if (StringUtils.isNotBlank(order.getMember().getMobile())) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("member").<String>get("mobile"), "%" + order.getMember().getMobile() + "%"));
                }
            }

            if (null != order.getQuotation()) {
                if (StringUtils.isNotBlank(order.getQuotation().getUsername())) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("quotation").<String>get("username"), "%" + order.getQuotation().getUsername() + "%"));
                }
                if (StringUtils.isNotBlank(order.getQuotation().getIdentification())) {
                    restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.get("quotation").<String>get("identification"), "%" + order.getQuotation().getIdentification() + "%"));
                }
            }
        }
        if (orderStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
        }
        if (paymentStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
        }
        if (shippingStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("shippingStatus"), shippingStatus));
        }
        if (hasExpired != null) {
            if (hasExpired) {
                restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
            }
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPage(Member member, Boolean hasExpired, Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        if (orderStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
        }
        if (paymentStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
        }

        if (hasExpired != null) {
            if (hasExpired) {
                restrictions = criteriaBuilder.and(restrictions, root.get("expire").isNotNull(), criteriaBuilder.lessThan(root.<Date>get("expire"), new Date()));
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.or(root.get("expire").isNull(), criteriaBuilder.greaterThanOrEqualTo(root.<Date>get("expire"), new Date())));
            }
        }
        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Order> findPageInvoice(Member member, InvoiceStatus invoiceStatus, Order.OrderStatus orderStatus, Order.PaymentStatus paymentStatus, Pageable pageable) {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteriaQuery = criteriaBuilder.createQuery(Order.class);
        Root<Order> root = criteriaQuery.from(Order.class);
        criteriaQuery.select(root);
        Predicate restrictions = criteriaBuilder.conjunction();

        if (member != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("member"), member));
        }
        if (orderStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("orderStatus"), orderStatus));
        }
        if (paymentStatus != null) {
            restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("paymentStatus"), paymentStatus));
        }

        // 未开票，已开票，已邮寄
        if(invoiceStatus != null) {
            if(invoiceStatus.equals(InvoiceStatus.is0)) {
                restrictions = criteriaBuilder.and(restrictions, root.get("invoice").isNull());
            } else {
                restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.get("invoice").get("invoiceStatus"), invoiceStatus));
            }
        }

        criteriaQuery.where(restrictions);
        return super.findPage(criteriaQuery, pageable);
    }

    @Transactional(readOnly = true)
    public Order build(Cart cart, Receiver receiver, PaymentMethod paymentMethod, String memo) {
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.notEmpty(cart.getCartItems());

        Order order = new Order();
        order.setShippingStatus(Order.ShippingStatus.unshipped);
        order.setFee(BigDecimal.ZERO);
        order.setPrice(BigDecimal.ZERO);
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setPoint(cart.getEffectivePoint());
        order.setMemo(memo);
        order.setMember(cart.getMember());

        if (receiver != null) {
            order.setConsignee(receiver.getConsignee());
            order.setAreaName(receiver.getAreaName());
            order.setAddress(receiver.getAddress());
            order.setZipCode(receiver.getZipCode());
            order.setPhone(receiver.getPhone());
            order.setArea(receiver.getArea());
        }

        order.setPaymentMethod(paymentMethod);

        order.setFreight(BigDecimal.ZERO);

        List<OrderItem> orderItems = order.getOrderItems();
        Map<Long, Long> tempMap = new HashMap<Long, Long>();
        for (CartItem cartItem : cart.getCartItems()) {
            if (cartItem != null && cartItem.getProduct() != null) {
                Product product = cartItem.getProduct();
                OrderItem orderItem = new OrderItem();
                orderItem.setSn(product.getSn());
                orderItem.setName(product.getName());
                orderItem.setFullName(product.getFullName());
                orderItem.setPrice(cartItem.getPrice());
                orderItem.setWeight(product.getWeight());
                orderItem.setThumbnail(product.getThumbnail());
                orderItem.setIsGift(false);
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setShippedQuantity(0);
                orderItem.setReturnQuantity(0);
                orderItem.setProduct(product);
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }
        }


        for (GiftItem giftItem : cart.getGiftItems()) {
            if (giftItem != null && giftItem.getGift() != null) {
                Product gift = giftItem.getGift();
                OrderItem orderItem = new OrderItem();
                orderItem.setSn(gift.getSn());
                orderItem.setName(gift.getName());
                orderItem.setFullName(gift.getFullName());
                orderItem.setPrice(new BigDecimal(0));
                orderItem.setWeight(gift.getWeight());
                orderItem.setThumbnail(gift.getThumbnail());
                orderItem.setIsGift(true);
                orderItem.setQuantity(giftItem.getQuantity());
                orderItem.setShippedQuantity(0);
                orderItem.setReturnQuantity(0);
                orderItem.setProduct(gift);
                orderItem.setOrder(order);
                orderItems.add(orderItem);
            }
        }

        order.setAmountPaid(new BigDecimal(0));

        if (order.getAmountPayable().compareTo(new BigDecimal(0)) == 0) {
            order.setOrderStatus(OrderStatus.confirmed);
            order.setPaymentStatus(PaymentStatus.paid);
        } else if (order.getAmountPayable().compareTo(new BigDecimal(0)) > 0 && order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
            order.setOrderStatus(OrderStatus.confirmed);
            order.setPaymentStatus(PaymentStatus.partialPayment);
        } else {
            order.setOrderStatus(OrderStatus.unconfirmed);
            order.setPaymentStatus(PaymentStatus.unpaid);
        }

        if (paymentMethod != null && paymentMethod.getTimeout() != null && order.getPaymentStatus() == PaymentStatus.unpaid) {
            order.setExpire(DateUtils.addHours(new Date(), paymentMethod.getTimeout()));
        }
        order.setOrderType(Order.OrderType.service);
        return order;
    }

    @Transactional(readOnly = true)
    public Order buildOrder(Quotation quotation, Receiver receiver, PaymentMethod paymentMethod, boolean useBalance, BigDecimal couponDiscount, Long couponCodeId, String memo, Member member) {

        Order order = new Order();
        order.setCity(quotation.getCity());
        order.setPrice(quotation.getAllAmount());
        order.setFee(quotation.getFee());

        order.setMemo(memo);
        order.setMember(member);
        if (null != paymentMethod){
            order.setPaymentMethod(paymentMethod);
            order.setPaymentMethodName(paymentMethod.getName());
        }else{
            order.setPaymentMethod(null);
            order.setPaymentMethodName(null);
        }


        if (useBalance) {
            if (member.getBalance().compareTo(order.getAmount()) >= 0) {
                order.setAmountPaid(order.getAmount());
            } else {
                order.setAmountPaid(member.getBalance());
            }
        } else {
            order.setAmountPaid(BigDecimal.ZERO);
        }

        if(null != couponDiscount && null != couponCodeId) {
            CouponCode couponCode = couponCodeService.find(couponCodeId);
            //优惠券折扣
            BigDecimal couponDiscountFee = couponDiscount.multiply(order.getFee()).setScale(2);
            order.setFee(couponDiscountFee);
//            order.setAmountPaid(order.getAmountPaid().add(couponDiscountFee));
            order.setCouponDiscount(couponDiscount);
            order.setCouponCode(couponCode);

            //更新优惠券为使用状态
            couponCode.setIsUsed(true);
            couponCode.setUsedDate(new Date());
            couponCodeService.update(couponCode);

        }

        if (order.getAmountPayable().compareTo(BigDecimal.ZERO) == 0) {
            order.setOrderStatus(Order.OrderStatus.confirmed);
            order.setPaymentStatus(Order.PaymentStatus.paid);
        } else if (order.getAmountPayable().compareTo(BigDecimal.ZERO) > 0 && order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            order.setOrderStatus(Order.OrderStatus.confirmed);
            order.setPaymentStatus(Order.PaymentStatus.partialPayment);
        } else {
            order.setOrderStatus(Order.OrderStatus.unconfirmed);
            order.setPaymentStatus(Order.PaymentStatus.unpaid);
        }

        if (paymentMethod != null && paymentMethod.getTimeout() != null && order.getPaymentStatus() == Order.PaymentStatus.unpaid) {
            order.setExpire(DateUtils.addHours(new Date(), paymentMethod.getTimeout()));
        }
        //计算订单赠送金豆
        Configs config = configsService.getConfigsByCode(Configs.DEFAULT_PRICE_TO_POINT_CODE);
        BigDecimal pointRate = BigDecimal.ZERO;
        if (null != config){
            pointRate = new BigDecimal(config.getCodeValue());
        }
        order.setPoint(order.getFee().multiply(pointRate).longValue());

        //默认值
        order.setFreight(BigDecimal.ZERO);
        order.setCouponDiscount(BigDecimal.ZERO);
        order.setOffsetAmount(BigDecimal.ZERO);
        order.setArea(null);
        order.setOrderType(Order.OrderType.quotation);
        return order;
    }

    @Transactional
    public Order create(Cart cart, Receiver receiver, PaymentMethod paymentMethod,String memo, Member member) {
        Assert.notNull(cart);
        Assert.notNull(cart.getMember());
        Assert.notEmpty(cart.getCartItems());
        Assert.notNull(receiver);
        Assert.notNull(paymentMethod);

        Order order = build(cart, receiver, paymentMethod, memo);

        order.setSn(snService.generate(Sn.Type.order));

        //order.setStore(null);
        orderDao.save(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create);
        orderLog.setOperator(member != null ? member.getUsername() : null);
        orderLog.setOrder(order);
        orderLogDao.save(orderLog);

        for (OrderItem orderItem : order.getOrderItems()) {
            if (orderItem != null) {
                Product product = orderItem.getProduct();
                entityManager.lock(product, LockModeType.PESSIMISTIC_WRITE);
                if (product != null && product.getStock() != null) {
                    product.setAllocatedStock(product.getAllocatedStock() + (orderItem.getQuantity() - orderItem.getShippedQuantity()));
                    productDao.save(product);
                    orderDao.flush();
                    //staticService.build(product);
                }
            }
        }

        cartService.remove(cart);
        //用户积分减去订单所用积分
        Long point = member.getPoint();
        if(point <= order.getPoint()){
            point = 0L;
        }else{
            point = point - order.getPoint();
        }
        member.setPoint(point);
        memberService.update(member);
        return order;
    }

    /**
     * 创建订单
     * @param quotation
     * @param receiver
     * @param paymentMethod
     * @param useBalance
     * @param memo
     * @param operator
     * @param member
     * @return
     */
    @Transactional
    public Order createOrder(Quotation quotation, Receiver receiver, PaymentMethod paymentMethod, boolean useBalance, BigDecimal couponDiscount, Long couponCodeId, String memo, Admin operator, Member member) {

        Order order = buildOrder(quotation, receiver, paymentMethod, useBalance, couponDiscount, couponCodeId, memo, member);

        // 生成订单编号
        order.setSn(createSn(quotation.getFeeCategory()));

        if (paymentMethod.getMethod() == PaymentMethod.Method.online) {
            order.setLockExpire(DateUtils.addSeconds(new Date(), 20));
            order.setOperator(operator);
        }

        order.setQuotation(quotation);

        order.setOrderCancelApplies(null);
        orderDao.save(order);

        quotation.setOrder(order);
        if(quotation.getMember() != null) {
            quotation.setMember(member);
        }
        quotationService.update(quotation);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.create);
        orderLog.setOperator(operator != null ? operator.getUsername() : null);
        orderLog.setOrder(order);
        orderLogDao.save(orderLog);

        //生成订单该用户即不是新手用户了
        member.setIsNew(false);
        if (order.getAmountPaid().compareTo(BigDecimal.ZERO) > 0) {
            entityManager.lock(member, LockModeType.PESSIMISTIC_WRITE);
            member.setBalance(member.getBalance().subtract(order.getAmountPaid()));
            memberDao.save(member);

            Deposit deposit = new Deposit();
            deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
            deposit.setCredit(BigDecimal.ZERO);
            deposit.setDebit(order.getAmountPaid());
            deposit.setBalance(member.getBalance());
            deposit.setOperator(operator != null ? operator.getUsername() : null);
            deposit.setMember(member);
            deposit.setOrder(order);
            depositDao.save(deposit);
        }
        return order;
    }

    /**
     * 生成订单编号
     * @param feeCategory
     * @return
     */
    private String createSn(FeeCategory feeCategory) {
        String strDateFormat = DateTimeUtil.formatDateTimetoString(DateTimeUtil.getSystemDate(), DateTimeUtil.FMT_yyyyMMddHHmmssS_17);

        if(feeCategory.equals(FeeCategory.fc0)) {
            strDateFormat = "SS" + strDateFormat;
        } else if(feeCategory.equals(FeeCategory.fc1)) {
            strDateFormat = "GJ" + strDateFormat;
        } else if(feeCategory.equals(FeeCategory.fc2)) {
            strDateFormat = "SG" + strDateFormat;
        }

        Random rand = new Random();
        for(int i=0; i<6; i++) {
            strDateFormat += rand.nextInt(9);
        }

        return strDateFormat;
    }

    /**
     * 生成编号
     * @return
     */
    public String createOtherSn() {
        String strDateFormat = DateTimeUtil.formatDateTimetoString(DateTimeUtil.getSystemDate(), DateTimeUtil.FMT_yyyyMMddHHmmssS_17);

        Random rand = new Random();
        for(int i=0; i<6; i++) {
            strDateFormat += rand.nextInt(9);
        }

        return strDateFormat;
    }


    /***
     * 标准订单取消
     * @param order
     * @param operator
     * @param storeOperator
     */
    @Transactional(rollbackFor = Exception.class)
    public void cancel(Order order, Admin operator, Member storeOperator) {
        Assert.notNull(order);

        order.setOrderStatus(Order.OrderStatus.cancelled);
        order.setExpire(null);
        merge(order);

        //已支付订单需要根据支付方式返回订单已付金额
        BigDecimal refund = BigDecimal.ZERO;
        Member member = order.getMember();
        if(order.getAmountPaid() != null && order.getOrderCancelApplies().size() > 0) {

            entityManager.lock(member, LockModeType.PESSIMISTIC_WRITE);

            for(OrderCancelApply orderCancelApply : order.getOrderCancelApplies()) {
                refund = refund.add(orderCancelApply.getAmount());
            }
            member.setBalance(member.getBalance().add(refund));
            memberService.merge(member);
        }

        Deposit deposit = new Deposit();
        deposit.setType(Deposit.Type.adminRefunds);
        deposit.setCredit(refund);
        deposit.setDebit(new BigDecimal(0));
        deposit.setBalance(member.getBalance());
        deposit.setOperator((operator != null && operator.getUsername() != null) ? operator.getUsername() : null == storeOperator ? "" : storeOperator.getUsername());
        deposit.setMember(member);
        deposit.setOrder(order);
        deposit.setMemo("会员取消已支付订单:" + order.getSn() + "，支付方式不是在线支付，系统将订单已付金额" + order.getAmountPaid() + "元退还到账户余额中。");
        depositDao.save(deposit);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.cancel);
        orderLog.setOperator((operator != null && operator.getUsername() != null) ? operator.getUsername() : null == storeOperator ? "" : storeOperator.getUsername());
        orderLog.setOrder(order);
        orderLogDao.save(orderLog);
    }

    /***
     * 订单确认
     * @param order
     * @param operator
     * @param storeOperator
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirm(Order order, Admin operator, Member storeOperator) {
        Assert.notNull(order);

        order.setOrderStatus(Order.OrderStatus.confirmed);
        //orderDao.merge(order);
        merge(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.confirm);
        orderLog.setOperator((operator != null && operator.getUsername() != null) ? operator.getUsername() : null == storeOperator ? "" : storeOperator.getUsername());
        orderLog.setOrder(order);
        orderLogDao.save(orderLog);
    }

    /**
     * 标准订单完成
     * @param order
     * @param operator
     * @param storeOperator
     */
    @Transactional(rollbackFor = Exception.class)
    public void complete(Order order, Admin operator, Member storeOperator) {
        Assert.notNull(order);

        Member member = order.getMember();
        entityManager.lock(member, LockModeType.PESSIMISTIC_WRITE);

        member.setAmount(member.getAmount().add(order.getAmountPaid()));

        memberService.merge(member);

        order.setOrderStatus(Order.OrderStatus.completed);
        order.setExpire(null);
        //orderDao.merge(order);
        merge(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.complete);
        orderLog.setOperator((operator != null && operator.getUsername() != null) ? operator.getUsername() : null == storeOperator ? "" : storeOperator.getUsername());
        orderLog.setOrder(order);
        orderLogDao.save(orderLog);

        //为店铺结算
        //settlementStoreByOrder(order);
    }


    /***
     * 支付
     * @param order
     * @param payment
     * @param operator
     * @param storeOperator
     */
    @Transactional(rollbackFor = Exception.class)
    public void payment(Order order, Payment payment, Admin operator, Member storeOperator) {
        System.out.println("payment order sn----" + order.getSn());
        try {
            payment.setSn(createOtherSn());
            Assert.notNull(order);
            Assert.notNull(payment);

            lock(order, LockModeType.PESSIMISTIC_WRITE);

            payment.setOrder(order);
            paymentService.merge(payment);
            //缴费订单特殊，支付成功即送积分
            Member member = order.getMember();
            Long orderPoint = order.getPoint();
            if (orderPoint != null && orderPoint != 0 && member.getPoint() + orderPoint >= 0) {
                member.setPoint(member.getPoint() + orderPoint);
            }
            if (payment.getMethod() == Payment.Method.deposit) {

                entityManager.lock(member, LockModeType.PESSIMISTIC_WRITE);
                member.setBalance(member.getBalance().subtract(payment.getAmount()));
                memberService.merge(member);

                Deposit deposit = new Deposit();
                deposit.setType(operator != null ? Deposit.Type.adminPayment : Deposit.Type.memberPayment);
                deposit.setCredit(new BigDecimal(0));
                deposit.setDebit(payment.getAmount());
                deposit.setBalance(member.getBalance());
                deposit.setOperator((operator != null && operator.getUsername() != null) ? operator.getUsername() : null == storeOperator ? "" : storeOperator.getUsername());
                deposit.setMember(member);
                deposit.setOrder(order);
                depositDao.save(deposit);
            }

            order.setAmountPaid(order.getAmountPaid().add(payment.getAmount()));
            //order.setFee(payment.getFee());
            order.setExpire(null);
            if (order.getAmountPaid().compareTo(order.getAmount()) >= 0 || order.getAmountPaid().compareTo(order.getAmountPayable()) >= 0) {
                order.setOrderStatus(Order.OrderStatus.confirmed);
                order.setPaymentStatus(Order.PaymentStatus.paid);
            } else if (order.getAmountPaid().compareTo(new BigDecimal(0)) > 0) {
                order.setOrderStatus(Order.OrderStatus.confirmed);
                order.setPaymentStatus(Order.PaymentStatus.partialPayment);
            }
            //orderDao.merge(order);
            merge(order);

            OrderLog orderLog = new OrderLog();
            orderLog.setType(OrderLog.Type.payment);
            orderLog.setOperator((operator != null && operator.getUsername() != null) ? operator.getUsername() : null == storeOperator ? "" : storeOperator.getUsername());
            orderLog.setOrder(order);
            orderLogDao.save(orderLog);

            //返佣记录
            shareService.saveOrderShare(order);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void shipping(Order order, Shipping shipping, Admin operator, Member storeOperator) {

        shipping.setSn(snService.generate(Sn.Type.shipping));

        Assert.notNull(order);
        Assert.notNull(shipping);
        Assert.notEmpty(shipping.getShippingItems());

        lock(order, LockModeType.PESSIMISTIC_WRITE);


        shipping.setOrder(order);

        for (ShippingItem shippingItem : shipping.getShippingItems()) {
            OrderItem orderItem = order.getOrderItem(shippingItem.getSn());
            if (orderItem != null) {
                Product product = orderItem.getProduct();
                entityManager.lock(product, LockModeType.PESSIMISTIC_WRITE);
                if (product != null) {
                    if (product.getStock() != null) {
                        product.setStock(product.getStock() - shippingItem.getQuantity());
                    }
                    productDao.save(product);
                    orderDao.flush();
                    //staticService.build(product);
                }
                entityManager.lock(orderItem, LockModeType.PESSIMISTIC_WRITE);
                orderItem.setShippedQuantity(orderItem.getShippedQuantity() + shippingItem.getQuantity());
            }
        }
        order.setShippingStatus(ShippingStatus.shipped);
        order.setExpire(null);
        merge(order);
        merge(order);
        shippingDao.save(shipping);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.shipping);
        orderLog.setOperator((operator != null && operator.getUsername() != null) ? operator.getUsername() : null == storeOperator ? "" : storeOperator.getUsername());
        orderLog.setOrder(order);
        orderLogDao.save(orderLog);

    }

    /*public void settlementStoreByOrder(Order order){
        if(order.getStore()!=null){
            Store store = storeDao.findOne(order.getStore().getId());
            //Store store = storeDao.findOne(order.getStore().getId());
            entityManager.lock(store, LockModeType.PESSIMISTIC_WRITE);
//	    	if(store.getStoreCategory().equals(StoreCategory.out)){
            Deposit deposit = depositService.findByTypeAndOrder(Deposit.Type.settlement, order);
            if(deposit==null){
                BigDecimal settlementScale = store.getRatio();
                BigDecimal amount = order.getAmount();
//					System.out.println("amount--" + amount + "--settlementScale--" + settlementScale);
//					System.out.println("multiply--" + amount.multiply(settlementScale));
                BigDecimal settlement = amount.multiply(settlementScale).setScale(2,RoundingMode.DOWN);
                Member m = store.getMember();
                if(settlement.compareTo(new BigDecimal(0))>0){
                    Deposit dpst = new Deposit();
                    dpst.setBalance((store.getBalance().add(settlement)).setScale(2,RoundingMode.DOWN));
                    dpst.setCredit(settlement);
                    dpst.setDebit(new BigDecimal(0));
                    dpst.setCreateDate(new Date());
                    dpst.setMemo("店铺订单完成结算。sn："+order.getSn());
                    dpst.setMember(m);
                    dpst.setOperator("系统自动");
                    dpst.setType(Deposit.Type.settlement);
                    dpst.setStatus(Deposit.Status.approved);
                    dpst.setOrder(order);
                    depositDao.save(dpst);
                    store.setBalance(dpst.getBalance());
                    storeService.merge(store);
                }
            }
//	    	}
        }
    }*/

}
