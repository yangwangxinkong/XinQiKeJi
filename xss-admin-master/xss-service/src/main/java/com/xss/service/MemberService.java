/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.alibaba.fastjson.JSONObject;
import com.xss.dao.DepositDao;
import com.xss.dao.MemberDao;
import com.xss.domain.*;
import com.xss.domain.enums.Gender;
import com.xss.domain.enums.ShareCategory;
import com.xss.util.DateUtil;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import com.xss.util.wxap.EmojiFilter;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service - 角色
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service
public class MemberService extends BaseService<Member, Long> {

	@Autowired
	private MemberDao memberDao;
	@Autowired
	private DepositDao depositDao;
	@Autowired
	private ConfigsService configsService;
	@Autowired
	private ShareService shareService;
	@Autowired
	private ShareLogService shareLogService;

	@Resource
	public void setBaseDao(MemberDao memberDao) {
		super.setBaseDao(memberDao);
	}
//	@Transactional
//	public void save(Member member) {
//		memberDao.save(member);
//	}

	public void save(Member member, Admin operator) {
		Assert.notNull(member);
		memberDao.save(member);
		if (member.getBalance().compareTo(BigDecimal.ZERO) > 0) {
			Deposit deposit = new Deposit();
			deposit.setType(operator != null ? Deposit.Type.adminRecharge : Deposit.Type.memberRecharge);
			//deposit.setCityCode(operator.getCityCode());
			deposit.setCredit(member.getBalance());
			deposit.setDebit(BigDecimal.ZERO);
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMember(member);
			depositDao.save(deposit);
		}
	}

	@Transactional
	public Member update(Member member) {
		return memberDao.save(member);
	}

	@Transactional(rollbackFor = Exception.class)
	public void update(Member member, Integer modifyPoint, BigDecimal modifyBalance, String depositMemo, Admin operator) {
		Assert.notNull(member);

		lock(member, LockModeType.PESSIMISTIC_WRITE);

		if (modifyPoint != null && modifyPoint != 0 && member.getPoint() + modifyPoint >= 0) {
			member.setPoint(member.getPoint() + modifyPoint);
		}

		if (modifyBalance != null && modifyBalance.compareTo(BigDecimal.ZERO) != 0 && member.getBalance().add(modifyBalance).compareTo(BigDecimal.ZERO) >= 0) {
			member.setBalance(member.getBalance().add(modifyBalance));
			Deposit deposit = new Deposit();
			if (modifyBalance.compareTo(BigDecimal.ZERO) > 0) {
				deposit.setType(operator != null ? Deposit.Type.adminRecharge : Deposit.Type.memberRecharge);
				deposit.setCredit(modifyBalance);
				deposit.setDebit(BigDecimal.ZERO);
			} else {
				deposit.setType(operator != null ? Deposit.Type.adminChargeback : Deposit.Type.memberPayment);
				deposit.setCredit(BigDecimal.ZERO);
				deposit.setDebit(modifyBalance);
			}
			deposit.setBalance(member.getBalance());
			deposit.setOperator(operator != null ? operator.getUsername() : null);
			deposit.setMemo(depositMemo);
			deposit.setMember(member);
			//deposit.setCityCode(operator.getCityCode());
			depositDao.save(deposit);
		}
		//super.merge(member);
		super.update(member, "idFaces", "idBackFaces", "hukouIndeies", "hukouPersons", "onePhones");
	}

	@Transactional(rollbackFor = Exception.class)
	public void recharge(Member member, BigDecimal modifyBalance, String depositMemo, Admin operator) {
		Assert.notNull(member);

		lock(member, LockModeType.PESSIMISTIC_WRITE);


		//记录失效日期
		Configs config = configsService.getConfigsByCode(Configs.DEFAULT_VIP_VALID_MONTH_CODE);
		if (null != config && StringUtils.isNotEmpty(config.getCodeValue())){
			Integer monthNum = Integer.valueOf(config.getCodeValue());
			Date vipExpireDate = DateUtil.addMonths(new Date(), monthNum);
			member.setVipExpireDate(vipExpireDate);
			Configs discountConfig = configsService.getConfigsByCode(Configs.DEFAULT_VIP_DISCOUNT_CODE);
			if (null != discountConfig){
				BigDecimal vipDiscount = new BigDecimal(discountConfig.getCodeValue());
				member.setVipDiscount(vipDiscount);
			}
			depositMemo = "会员充值成功，有效期至" + DateUtil.format(vipExpireDate) + ",每单服务费：" + discountConfig.getCodeValue();
		}
		member.setIsVip(true);//成为vip会员

		//记录充值记录
		Deposit deposit = new Deposit();
		deposit.setType(operator != null ? Deposit.Type.adminRecharge : Deposit.Type.memberRecharge);
		deposit.setCredit(BigDecimal.ZERO);
		deposit.setDebit(BigDecimal.ZERO);
		deposit.setBalance(member.getBalance());
		deposit.setOperator(operator != null ? operator.getUsername() : null);
		deposit.setMemo(depositMemo);
		deposit.setMember(member);
		depositDao.save(deposit);


		super.merge(member);
	}

	@Transactional
	public boolean usernameExists(String username) {
		if (username == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.username) = lower(:username)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
		return count > 0;
	}

	@Transactional
	public boolean emailExists(String email) {
		if (email == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.email) = lower(:email)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getSingleResult();
		return count > 0;
	}

	@Transactional
	public boolean mobileExists(String mobile) {
		if (mobile == null) {
			return false;
		}
		String jpql = "select count(*) from Member members where lower(members.mobile) = lower(:mobile)";
		Long count = entityManager.createQuery(jpql, Long.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).getSingleResult();
		return count > 0;
	}

	@Transactional
	public Member findByUsername(String username) {
		if (username == null) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.username) = lower(:username)";
			return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public Member findByMobile(String mobile) {
		if (mobile == null) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.mobile) = lower(:mobile)";
			return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("mobile", mobile).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public Member findByOpenId(String openId) {
		if (openId == null) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.openId) = lower(:openId)";
			return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("openId", openId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public Member findByUniondId(String unionId) {
		if (unionId == null) {
			return null;
		}
		try {
			String jpql = "select members from Member members where lower(members.unionId) = lower(:unionId)";
			return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("unionId", unionId).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Transactional
	public List<Member> findListByEmail(String email) {
		if (email == null) {
			return Collections.<Member> emptyList();
		}
		String jpql = "select members from Member members where lower(members.email) = lower(:email)";
		return entityManager.createQuery(jpql, Member.class).setFlushMode(FlushModeType.COMMIT).setParameter("email", email).getResultList();
	}

	@Transactional
	public Page<Member> findPage(Member member, City city, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
		addRestrictions(criteriaQuery, member, city);
		return super.findPage(criteriaQuery, pageable);
	}

	private void addRestrictions(CriteriaQuery<Member> criteriaQuery, Member member, City city) {
		if (criteriaQuery == null) {
			return;
		}
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		Root<Member> root = criteriaQuery.from(Member.class);
		criteriaQuery.select(root);
		Predicate restrictions = criteriaBuilder.conjunction();
		if (null != city) {
			Subquery<Member> subquery = criteriaQuery.subquery(Member.class);
			Root<Member> subqueryRoot = subquery.from(Member.class);
			subquery.select(subqueryRoot);
			subquery.where(criteriaBuilder.equal(subqueryRoot, root), criteriaBuilder.equal(subqueryRoot.join("memberCitys").join("city"), city));
			restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.exists(subquery));
		}

		if (null != member) {
			if (StringUtils.isNotBlank(member.getName())) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("name"), "%" +member.getName() + "%"));
			}

			if (null!=member.getGender()) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<Gender>get("gender"), member.getGender()));
			}

			if (null!=member.getMobile()) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("mobile"),"%" +member.getMobile() + "%"));
			}

			if (null!=member.getUsername()) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.like(root.<String>get("username"),"%" +member.getUsername() + "%"));
			}

			if (null!=member.getOpenId()) {
				restrictions = criteriaBuilder.and(restrictions, criteriaBuilder.equal(root.<String>get("openId"), member.getOpenId()));
			}
		}
		criteriaQuery.where(restrictions);
	}


	@Transactional
	public List<Member> search(Member member) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Member> criteriaQuery = criteriaBuilder.createQuery(Member.class);
		addRestrictions(criteriaQuery, member, null);
		return super.findList(criteriaQuery, null, null, null, null);
	}
	/**
	 * 保存或更新微信用户信息到数据库
	 * @param userInfo 微信用户基本信息
	 * @param type mp：微信公众号；open：微信开放平台
	 * @return
	 */
	@Transactional
	public Member mergeUserInfoByWeixin(JSONObject userInfo, String type, HttpServletRequest request){
		Member member = null;
		try {
			if (null == userInfo || userInfo.isEmpty() || StringUtils.isBlank(type) || !("mp".equals(type) || "open".equals(type))) {
				return member;
			}
			//Object unionId = userInfo.get("unionid");
			String openId = userInfo.getString("openid");
			String nickName = userInfo.getString("nickname");
			nickName = Base64.encodeBase64String(nickName.getBytes("utf-8"));
			//nickName = EmojiFilter.filterEmoji(nickName);
			int sex = userInfo.getInteger("sex");
			String avatar = userInfo.getString("headimgurl");

			member = findByOpenId(openId);
			if (null == member) {
				member = new Member();
				member.setUsername("XSS" + (System.currentTimeMillis()/1000));
				member.setNickName(nickName);
				//member.setName(nickName);
				member.setGender(sex == 1 ? Gender.male : Gender.female);
				//member.setUnionId(unionId.toString());
				member.setOpenId(openId);
				member.setPoint(0L);
				member.setAmount(new BigDecimal(0));
				member.setBalance(new BigDecimal(0));
				member.setIsEnabled(true);
				member.setIsLocked(false);
				member.setLoginFailureCount(0);
				member.setLockedDate(null);
				member.setRegisterIp(request.getRemoteAddr());
				member.setLoginIp(request.getRemoteAddr());
				member.setLoginDate(new Date());
				member.setNature(Member.Nature.buyer);//注册用户默认为买家
				member.setShareBalance(BigDecimal.ZERO);
				member.setHasShareOrder(false);


				member.setPassword(DigestUtils.md5Hex(DigestUtils.md5Hex(member.getMemberDefaultPassward())));
				member.setHeadImage(avatar);

				if ("mp".equals(type)) {
					//微信公众号
					member.setSubscribeDate(new Date());
					member.setSubscription(Member.Subscription.subscribe);
				}else if ("open".equals(type)) {
					//微信开放平台
					//member.setOpenIdPc(openId);
					member.setSubscription(Member.Subscription.unSubscribe);
				}
				save(member);
			} else {
//				if(!usernameExists(nickName.toLowerCase())){
//					member.setUsername(nickName.toLowerCase());
//				}
				member.setNickName(nickName);
//				member.setName(nickName);
				member.setGender(Gender.findByValue(sex));
				member.setHeadImage(avatar);
				if ("mp".equals(type)) {
					//微信公众号
					member.setOpenId(openId);
					member.setSubscribeDate(new Date());
					member.setSubscription(Member.Subscription.subscribe);
				}else if ("open".equals(type)) {
					//微信开放平台
					//member.setOpenIdPc(openId);
				}
				update(member);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return member;
	}


}