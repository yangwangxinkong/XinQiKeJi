package com.xss.service;

import com.xss.dao.OrderCancelApplyDao;
import com.xss.dao.OrderLogDao;
import com.xss.domain.*;
import com.xss.domain.Order;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.Collections;
import java.util.List;

/**
 * Created by huperson on 2018/08/08.
 */
@Service
public class OrderCancelApplyService extends BaseService<OrderCancelApply, Long> {

    @Resource
    private OrderCancelApplyDao orderCancelApplyDao;
    @Resource
    private OrderLogDao orderLogDao;
    @Autowired
    private OrderService orderService;
    @Autowired
    private OrderCancelApplyService orderCancelApplyService;

    @Resource
    public void setBaseDao(OrderCancelApplyDao orderCancelApplyDao) {
        super.setBaseDao(orderCancelApplyDao);
    }

    @Transactional
    public OrderCancelApply findByOrderSn(String sn) {

        if (sn == null) {
            return null;
        }
        String jpql = "select orderCancelApply from OrderCancelApply orderCancelApply where orderCancelApply.order.sn = :sn";
        try {
            return entityManager.createQuery(jpql, OrderCancelApply.class).setFlushMode(FlushModeType.COMMIT).setParameter("sn", sn).getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancelApply(OrderCancelApply orderCancelApply) {
        orderCancelApply.setOrderStatus(Order.OrderStatus.canceling);
        super.save(orderCancelApply);
        Order order = orderService.findBySn(orderCancelApply.getOrder().getSn());
        entityManager.lock(order, LockModeType.PESSIMISTIC_WRITE);
        order.setOrderStatus(Order.OrderStatus.canceling);
        orderService.update(order);

        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.canceling);
        orderLog.setOperator("会员");
        orderLog.setContent("您的取消申请已提交");
        orderLog.setOrder(order);
        orderLogDao.save(orderLog);

        /*Notice notice=new Notice();
        notice.setoSn(order.getSn());
        notice.setStatus(com.xss.entity.Notice.Status.unReade);
        notice.setType(com.xss.entity.Notice.Type.orderCancelApply);
        notice.setRemind(com.xss.entity.Notice.Remind.unRemind);
        notice.setStore(order.getStore());
        noticeDao.persist(notice);*/
    }

     /**
     * 取消订单申请通过操作
     * 1.将取消申请状态更新为已取消
     * 2.将订单状态更新为已取消
     * 3.如果是在线支付调用第三方支付退款接口原路退款
     * 4.如果是其它支付方式则将订单金额退回用户余额中
     * 5.记录订单日志和退款日志
     * @param orderCancelApply
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public void approve(OrderCancelApply orderCancelApply, Admin operator, Member member){
        //orderCancelApply = orderCancelApplyDao.findOne(orderCancelApply.getId());
        Order order = orderCancelApply.getOrder();
        entityManager.lock(order, LockModeType.PESSIMISTIC_WRITE);
        /*//已取消*/
        orderCancelApply.setOrderStatus(Order.OrderStatus.cancelled);
        orderCancelApplyService.merge(orderCancelApply);
        //订单取消通过日志
        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.cancel_agree);
        orderLog.setOrder(orderCancelApply.getOrder());
        orderLog.setOperator(operator != null ? operator.getUsername() : member.getUsername());
        orderLog.setContent("平台管理员:" + operator != null ? operator.getUsername() : member.getUsername() + "同意取消订单:" + order.getSn());
        orderLogDao.save(orderLog);

        ///*Notice notice=new Notice();
        //notice.setMember(order.getMember());
       // notice.setoSn(order.getSn());
        //notice.setType(Notice.Type.cancelled);
        //notice.setStatus(Notice.Status.unReade);
        //notice.setRemind(Notice.Remind.unRemind);
        //noticeDao.persist(notice);*/

        orderService.cancel(order, operator, member);
    }

    @Transactional(rollbackFor = Exception.class)
    public void reject(OrderCancelApply orderCancelApply, Admin operator, Member member) {
        //驳回
        orderCancelApply.setOrderStatus(Order.OrderStatus.cancel_reject);
        Order order = orderCancelApply.getOrder();
        entityManager.lock(order, LockModeType.PESSIMISTIC_WRITE);
        merge(orderCancelApply);
        //驳回
        order.setOrderStatus(Order.OrderStatus.cancel_reject);
        orderService.merge(order);
        //订单取消驳回日志
        OrderLog orderLog = new OrderLog();
        orderLog.setType(OrderLog.Type.cancel_reject);
        orderLog.setOrder(orderCancelApply.getOrder());
        orderLog.setOperator(operator != null ? operator.getUsername() : member.getUsername());
        orderLog.setContent("平台管理员:" + operator != null ? operator.getUsername() : member.getUsername() + "驳回取消订单:" + order.getSn());
        orderLogDao.save(orderLog);

        /*Notice notice=new Notice();
        notice.setMember(order.getMember());
        notice.setoSn(order.getSn());
        notice.setType(Notice.Type.cancelReject);
        notice.setStatus(Notice.Status.unReade);
        notice.setRemind(Notice.Remind.unRemind);
        noticeDao.persist(notice);*/
    }
}
