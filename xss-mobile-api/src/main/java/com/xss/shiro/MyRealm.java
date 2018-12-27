package com.xss.shiro;

import com.xss.base.Constant;
import com.xss.config.SpringContextBean;
import com.xss.domain.Member;
import com.xss.exception.UnauthorizedException;
import com.xss.service.MemberService;
import com.xss.util.JWTUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;

/**
 * @author zzl
 * @since 2018-05-03
 */
public class MyRealm extends AuthorizingRealm {
    private Logger logger = LoggerFactory.getLogger(com.xss.shiro.MyRealm.class);

    @Resource
    private MemberService memberService;

    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 只有当需要检测用户权限的时候才会调用此方法，例如checkRole,checkPermission之类的
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        if (memberService == null) {
            this.memberService = SpringContextBean.getBean(MemberService.class);
        }
//        if (menuService == null) {
//            this.menuService = SpringContextBean.getBean(IMenuService.class);
//        }
//        if (roleService == null) {
//            this.roleService = SpringContextBean.getBean(IRoleService.class);
//        }

        String userNo = JWTUtil.getUserNo(principals.toString());
        Member member = memberService.findByUsername(userNo);
        //User user = userService.selectById(userNo);
        //UserToRole userToRole = userToRoleService.selectByUserNo(user.getUserNo());

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        Set<String> roleNameSet = new HashSet<>();
//        if (null != member.getCompany() && null != member.getCompany().getCompanyCategory()){
//            roleNameSet.addAll(member.getCompany().getCompanyCategory().getAuthorities());
//        }

//        Role role = roleService.selectOne(new EntityWrapper<Role>().eq("role_code", userToRole.getRoleCode()));
//        roleNameSet.add(role.getRoleName());
        //添加控制角色级别的权限
        //simpleAuthorizationInfo.addRoles(roleNameSet);
        /*
        //控制菜单级别按钮  类中用@RequiresPermissions("user:list") 对应数据库中code字段来控制controller
        ArrayList<String> pers = new ArrayList<>();
        List<Menu> menuList = menuService.findMenuByRoleCode(userToRole.getRoleCode());
        for (Menu per : menuList) {
             if (!ComUtil.isEmpty(per.getCode())) {
                  pers.add(String.valueOf(per.getCode()));
              }
        }
        Set<String> permission = new HashSet<>(pers);
        simpleAuthorizationInfo.addStringPermissions(permission);
        */

        return simpleAuthorizationInfo;
    }

    /**
     * 默认使用此方法进行用户名正确与否验证，错误抛出异常即可。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws UnauthorizedException {
        if (memberService == null) {
            this.memberService = SpringContextBean.getBean(MemberService.class);
        }
//        if (userService == null) {
//            this.userService = SpringContextBean.getBean(IUserService.class);
//        }
        String token = (String) auth.getCredentials();
        if(Constant.isPass){
            return new SimpleAuthenticationInfo(token, token, this.getName());
        }
        // 解密获得username，用于和数据库进行对比
        String userNo = JWTUtil.getUserNo(token);
        if (userNo == null) {
            throw new UnauthorizedException("token invalid");
        }
        Member member = memberService.findByUsername(userNo);
        //User userBean = userService.selectById(userNo);
        if (member == null) {
            throw new UnauthorizedException("Member didn't existed!");
        }
        if (! JWTUtil.verify(token, userNo, member.getPassword())) {
            throw new UnauthorizedException("Username or password error");
        }
        return new SimpleAuthenticationInfo(token, token, this.getName());
    }
}
