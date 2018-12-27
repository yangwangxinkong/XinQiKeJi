package com.xss.shiro;

import com.xss.annotation.CurrentUser;
import com.xss.domain.Member;
import com.xss.exception.UnauthorizedException;
import com.xss.service.MemberService;
import com.xss.util.JWTUtil;
import com.xss.util.SpringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;

/**
 *  增加方法注入，将含有 @CurrentUser 注解的方法参数注入当前登录用户
 * @author zzl
 * @since 2018-05-03
 */
public class CurrentUserMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Resource
    private MemberService memberService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(Member.class)
                && parameter.hasParameterAnnotation(CurrentUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization");
        String userNo = JWTUtil.getUserNo(token);
        if(null == memberService){
            memberService = (MemberService) SpringUtils.getBean("memberService");
        }

        Member user = memberService.findByUsername(userNo);

        //Member user = (Member) webRequest.getAttribute("currentUser", RequestAttributes.SCOPE_REQUEST);
        if (user == null) {
            throw new UnauthorizedException("获取用户信息失败");
        }
        if (!JWTUtil.verify(token, userNo, user.getPassword())) {
            throw new UnauthorizedException("获取用户信息失败");
        }
        // 20181126 多端登录避免。token不一致
        if (!token.equals(user.getToken())) {
            throw new UnauthorizedException("用户信息已在其他地方登录");
        }
        return user;
    }
}
