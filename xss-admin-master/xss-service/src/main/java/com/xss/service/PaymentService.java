package com.xss.service;

import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.dao.PaymentDao;
import com.xss.domain.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;


/**
 * Created by huperson on 2018/08/08.
 */
@Service
public class PaymentService extends BaseService<Payment, Long> {

    @Resource
    private PaymentDao paymentDao;
    @Autowired
    private OrderService orderService;
    @Resource
    private MemberService memberService;
    @Resource
    private ConfigsService configsService;

    @Resource
    public void setBaseDao(PaymentDao paymentDao) {
        super.setBaseDao(paymentDao);
    }

    @Transactional(readOnly = true)
    public Payment findBySn(String sn) {
        if (sn == null) {
            return null;
        }
        String jpql = "select payment from Payment payment where lower(payment.sn) = lower(:sn)";
        try {
            return entityManager.createQuery(jpql, Payment.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional
    public PublicResult<Object> save(Member member, Payment.Type type, Order order, PaymentMethod paymentMethod, BigDecimal amount) {
        PublicResult<Object> result = null;
        try {
            if (null == member) {
                result = new PublicResult<Object>(PublicResultConstant.ERROR, "未登录或登录超时");
                return result;
            }
            if (type.equals(Payment.Type.payment)) {
                //订单支付
                if (order == null || !member.equals(order.getMember()) || order.isExpired() || order.isLocked(null)) {
                    result = new PublicResult<Object>(PublicResultConstant.ERROR, "订单信息不正确");
                    return result;
                }
                if (order.getPaymentMethod() == null || order.getPaymentMethod().getMethod() != PaymentMethod.Method.online) {
                    result = new PublicResult<Object>(PublicResultConstant.ERROR, "订单支付方式不正确");
                    return result;
                }
                if (order.getPaymentStatus() != Order.PaymentStatus.unpaid && order.getPaymentStatus() != Order.PaymentStatus.partialPayment) {
                    result = new PublicResult<Object>(PublicResultConstant.ERROR, "订单已支付");
                    return result;
                }
                if (order.getAmountPayable().compareTo(new BigDecimal(0)) <= 0) {
                    result = new PublicResult<Object>(PublicResultConstant.ERROR, "订单支付金额不正确");
                    return result;
                }
                Payment payment = new Payment();

                payment.setSn(orderService.createOtherSn());
                payment.setType(Payment.Type.payment);
                payment.setMethod(Payment.Method.online);
                payment.setStatus(Payment.Status.wait);
                payment.setPaymentMethod(Payment.Method.online.getDesc() + Payment.PAYMENT_METHOD_SEPARATOR + paymentMethod.getName());
                payment.setFee(new BigDecimal(0));
                payment.setAmount(order.getAmountPayable().setScale(2, RoundingMode.UP));
                //payment.setPaymentPluginId(paymentMethod.getCode());
                payment.setExpire(paymentMethod.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()) : null);
                payment.setOrder(order);
                payment.setMember(order.getMember());
                save(payment);
                result = new PublicResult<Object>(PublicResultConstant.SUCCESS, payment);
            }else if (type.equals(Payment.Type.recharge)){
                Configs config = configsService.getConfigsByCode(Configs.DEFAULT_VIP_RECHARGE_FEE_CODE);
                if (null == config || null == amount || StringUtils.isBlank(config.getCodeValue()) || amount.compareTo(new BigDecimal(config.getCodeValue())) != 0){
                    //如果会员充值金额与系统配置会员充值金额不相等，则不能支付。
                    result = new PublicResult<Object>(PublicResultConstant.ERROR, "充值金额不一致");
                }else{
                    Payment payment = new Payment();

                    payment.setSn(orderService.createOtherSn());
                    payment.setType(Payment.Type.recharge);
                    payment.setMethod(Payment.Method.online);
                    payment.setStatus(Payment.Status.wait);
                    payment.setPaymentMethod(Payment.Method.online.getDesc() + Payment.PAYMENT_METHOD_SEPARATOR + paymentMethod.getName());
                    payment.setFee(new BigDecimal(0));
                    payment.setAmount(amount.setScale(2, RoundingMode.UP));
                    payment.setExpire(paymentMethod.getTimeout() != null ? DateUtils.addMinutes(new Date(), paymentMethod.getTimeout()) : null);
                    payment.setMember(member);
                    save(payment);
                    result = new PublicResult<Object>(PublicResultConstant.SUCCESS, payment);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            result = new PublicResult<Object>(PublicResultConstant.ERROR, "系统错误");
        }
        return result;
    }
    @Transactional
    public void handle(Payment payment) {
        System.out.println("notify handle---");
        String tradeNo = payment.getTradeNo();
        entityManager.refresh(payment, LockModeType.PESSIMISTIC_WRITE);
        if (payment != null && payment.getStatus() == Payment.Status.wait) {
            if (payment.getType() == Payment.Type.payment) {
                Order order = payment.getOrder();
                if (order != null) {
                    orderService.payment(order, payment, null, null);
                }
            } else if (payment.getType() == Payment.Type.recharge) {
                Member member = payment.getMember();
                if (member != null) {
                    memberService.recharge(member, null, null, null);
                }
            }
            payment.setStatus(Payment.Status.success);
            payment.setPaymentDate(new Date());
            payment.setTradeNo(tradeNo);
            merge(payment);
        }
    }
}
