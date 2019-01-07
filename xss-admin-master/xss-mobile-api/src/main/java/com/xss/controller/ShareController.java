package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Configs;
import com.xss.domain.CouponCode;
import com.xss.domain.Member;
import com.xss.service.ConfigsService;
import com.xss.service.CouponCodeService;
import com.xss.service.MemberService;
import com.xss.util.*;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Set;

/**
 * 移动端用户分享接口
 * @author zzl
 * @date 2018/12/15
 */
@RestController
@RequestMapping("/m/member/share")
@Api(description="分享接口")
public class ShareController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ShareController.class);

    @Autowired
    private MemberService memberService;
    @Value("${member.qcode.path}")
    private String qrCodePath;
    @Value("${member.qcode.url}")
    private String qrCodeUrl;

    @Autowired  //自动注入系统配置service
    ConfigsService configsService;

    @ApiOperation(value="获取用户分佣余额", notes="获取用户分佣余额", produces = "application/json")
    @ApiImplicitParams({@ApiImplicitParam(name = "requestJson", value = "",required = true, dataType = "String",paramType="body")})
    @GetMapping("/info")
    @Log(description="获取用户分佣余额接口:/m/member/share/info")
    public PublicResult<JSONObject> info(@CurrentUser Member member) {
        try {
            Set<Member> sharedMembers = member.getSharedMembers();
            Integer orderNum = 0;
            Integer registerNum = sharedMembers.size();
            for (Member sharedMember : sharedMembers) {
                if (sharedMember.getHasShareOrder()) {
                    orderNum += 1;
                }
            }
            JSONObject shareJo = new JSONObject();
            shareJo.put("mid", member.getId());
            shareJo.put("shareBalance", CurrencyMethod.currency(member.getShareBalance()));
            shareJo.put("registerNum", registerNum);
            shareJo.put("orderNum", orderNum);
            String qrcodeUrl = member.getQrcodeUrl();
            if (StringUtils.isBlank(member.getQrcodeUrl())){
                String content = "http://m.xiaodoushebao.com/index.html?targetId=2&mid=" + member.getId() + "&sc=s1";
                String name = QrcodeUtil.create(qrCodePath, content);
                if (StringUtils.isNotBlank(name)) {
                    qrcodeUrl = qrCodeUrl + name;
                }
                member.setQrcodeUrl(qrcodeUrl);
                memberService.update(member);
            }
            shareJo.put("qrCodeUrl", qrcodeUrl);

            return new PublicResult<JSONObject>(PublicResultConstant.SUCCESS, shareJo);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new PublicResult<JSONObject>(PublicResultConstant.ERROR, null);
    }

    @ApiOperation("获取分享信息标题和描述")
    @GetMapping(value="/shareSet" ,produces="application/json;charset=utf-8" )//解决返回值中文乱码问题
    public PublicResult<HashMap> shareQuery(){
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            String code = Configs.SHARE_TITLE_DESC; //在后台管理中添加固定的编码，用来表示分享信息
            Configs configsByCode = configsService.getConfigsDao().findConfigsByCode(code);
            //分享标题
            result.put("headline",configsByCode.getCodeValue());
            //分享描述
            result.put("describe",configsByCode.getMemo());
            return new PublicResult<HashMap>(PublicResultConstant.SUCCESS,result);
        }catch (Exception e){
            //当没有查询到分享信息时返回默认值
            //分享标题
            result.put("headline","分享小豆社保");
            //分享描述
            result.put("describe","小豆社保欢迎您！");
            return new PublicResult<HashMap>(PublicResultConstant.ERROR,result);
        }
    }
}
