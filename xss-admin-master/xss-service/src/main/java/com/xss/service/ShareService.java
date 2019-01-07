package com.xss.service;

import com.xss.dao.ShareDao;
import com.xss.domain.*;
import com.xss.domain.enums.ShareCategory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;

/**
 *  Service - 分享
 * @author zzl
 * @date 2018/12/15
 */
@Service
public class ShareService extends BaseService<Share,Long> {
    @Autowired
    private MemberService memberService;
    @Autowired
    private ConfigsService configsService;
    @Autowired
    private ShareLogService shareLogService;
    @Autowired
    private OrderService orderService;
    @Resource
    private ShareDao shareDao;
    @Resource
    public void setBaseDao(ShareDao shareDao) {
        super.setBaseDao(shareDao);
    }

    public ShareDao getShareDao() {
        return shareDao;
    }

    public void setShareDao(ShareDao shareDao) {
        this.shareDao = shareDao;
    }

    /**
     * 保存用户注册分佣记录
     * @param member
     * @param shareMemberId
     * @param shareCategory
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveRegisterShare(Member member, Long shareMemberId, ShareCategory shareCategory){
        if (null != member && null != member.getId() && null != shareMemberId){
            Member shareMember = memberService.find(shareMemberId);
            if (null != shareMember) {
                //获取注册返佣金额
                BigDecimal registerMoney = BigDecimal.ZERO;
                try {
                    Configs config = configsService.getConfigsByCode(Configs.SHARE_REGISTER_MONEY);
                    registerMoney = new BigDecimal(config.getCodeValue());
                }catch (Exception e){

                }
                //分享者返佣
                shareMember.setShareBalance(shareMember.getShareBalance().add(registerMoney));
                shareMember.setHasShareOrder(false);
                memberService.update(shareMember);
                //分享者记录
                Share share = new Share();
                share.setShareMemberId(shareMember.getId());
                share.setShareMemberMobile(shareMember.getMobile());
                share.setSharedMemberId(member.getId());
                share.setSharedMemberMobile(member.getMobile());
                share.setShareCategory(shareCategory);
                share.setRegisterMoney(registerMoney);
                share.setOrderMoney(BigDecimal.ZERO);
                super.save(share);

                //分享操作记录
                ShareLog shareLog = new ShareLog();
                shareLog.setMember(shareMember);
                shareLog.setShareCategory(shareCategory);
                shareLog.setType(ShareLog.Type.register);
                shareLog.setCredit(registerMoney);
                shareLog.setDebit(BigDecimal.ZERO);
                shareLog.setBalance(shareMember.getShareBalance());
                shareLog.setOperator("system");
                shareLogService.save(shareLog);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveOrderShare(Order order){
        if (null != order && null != order.getMember()){
            Member member = order.getMember();
            //是否首单返佣配置
            Configs config = configsService.getConfigsByCode(Configs.SWITCH_FIRST_ORDER_SHARE);
            if (null == config || "1".equals(config.getCodeValue()) || !member.getHasShareOrder()){
                //订单返佣系统配置
                Configs orderDiscountConfig = configsService.getConfigsByCode(Configs.SHARE_ORDER_DISCOUNT);
                String orderDiscount = StringUtils.isBlank(orderDiscountConfig.getCodeValue()) ? "0" : orderDiscountConfig.getCodeValue();
                Configs orderMoneyConfig = configsService.getConfigsByCode(Configs.SHARE_ORDER_MONEY);
                String orderMoneyValue = StringUtils.isBlank(orderMoneyConfig.getCodeValue()) ? "0" : orderMoneyConfig.getCodeValue();
                //如果订单有服务费，则使用比例计算返佣金额；如果没有，则直接使用系统配置返佣金额
                BigDecimal shareMoney = new BigDecimal(orderMoneyValue);
                if (order.getFee().compareTo(BigDecimal.ZERO) > 0) {
                    shareMoney = order.getFee().multiply(new BigDecimal(orderDiscount));
                }
                order.setShareMoney(shareMoney);

                orderService.update(order);

                //分享者记录
                Share share = getShareDao().findBySharedMemberId(member.getId());
                share.setOrderMoney(share.getOrderMoney().add(shareMoney));
                super.update(share);

                //更新分享者分佣余额
                Member shareMember = memberService.find(share.getShareMemberId());
                shareMember.setShareBalance(shareMember.getShareBalance().add(shareMoney));
                memberService.update(shareMember);

                //分享操作记录
                ShareLog shareLog = new ShareLog();
                shareLog.setMember(shareMember);
                shareLog.setShareCategory(share.getShareCategory());
                shareLog.setType(ShareLog.Type.order);
                shareLog.setCredit(shareMoney);
                shareLog.setDebit(BigDecimal.ZERO);
                shareLog.setBalance(shareMember.getShareBalance());
                shareLog.setOperator("system");
                shareLogService.save(shareLog);

                //更新分享对象下单分享状态
                if (!member.getHasShareOrder()){
                    member.setHasShareOrder(true);
                    memberService.update(member);
                }

            }
        }
    }

}
