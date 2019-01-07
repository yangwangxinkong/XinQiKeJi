package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.Member;
import com.xss.service.MemberService;
import com.xss.util.JsonUtil;
import com.xss.util.page.Filter;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

/**
 *  会员管理接口
 * @author zzl
 * @since 2018-08-13
 */
@RestController
@RequestMapping("/api/member")
@Api(description="会员管理模块")
public class MemberController {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(MemberController.class);

    @Autowired
    private MemberService memberService;

    /**
     * 列表
     */
    @GetMapping("/list")
    @ApiOperation(value="会员列表", notes="分页展示会员列表，支持根据名称搜索",produces = "application/json")
    @Log(description="平台中心获取会员列表接口:/api/member/list")
    public PageResult<Object> list(Pageable pageable, Member member) {
        LOG.debug("get Member list param = " + pageable.toString());
        PageResult<Object> result = null;
        try{
//            if (StringUtils.isNotEmpty(username)){
//                pageable.getFilters().add(Filter.like("username", "%" + username + "%"));
//            }
            if (StringUtils.isNotEmpty(member.getMobile())){
                pageable.getFilters().add(Filter.like("mobile", "%" + member.getMobile() + "%"));
            }
            if(null != member.getNature()){
                pageable.getFilters().add(Filter.eq("nature", member.getNature()));
            }
            if(null != member.getIsVip()){
                pageable.getFilters().add(Filter.eq("isVip", member.getIsVip()));
            }
            if(null != member.getIsNew()){
                pageable.getFilters().add(Filter.eq("isNew", member.getIsNew()));
            }
            Page<Member> data = memberService.findPage(pageable);
            result = new PageResult<Object>((int)data.getTotal(),data.getPageNumber(),data.getPageSize(), memberService.createEntity().convertList(data.getContent(), new String[]{"memberRank","memberStatus","amount"}));
        }catch (Exception e){
            LOG.error("get Member list error. {}", e);
            result = new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
        }
        LOG.debug("get Member list result = " + result.toString());
        return result;
    }

    /**
     * 条件查询会员列表
     */
    @PostMapping("/search")
    @ApiOperation(value="条件查询会员列表", notes="条件查询会员列表",produces = "application/json")
    @Log(description="平台中心条件查询会员列表:/api/member/search")
    public PublicResult<Object> search(@RequestBody(required = false) Member member, String mobile) {
        if (null != member){
            LOG.debug("get Member search param = " + member.toString());
        }

        PublicResult<Object> result = null;
        try{
            List<Member> members = memberService.search(member);
            result = new PublicResult<>(PublicResultConstant.SUCCESS, memberService.createEntity().convertList(members, null));
        }catch (Exception e){
            LOG.error("get Member search error. {}", e);
            result = new PublicResult<>(PublicResultConstant.ERROR,null);
        }
        LOG.debug("get Member search result = " + result.toString());
        return result;
    }

    /**
     * 详情
     */
    @GetMapping("/info")
    @ApiOperation(value="会员详情", notes="根据id获取会员详情",produces = "application/json")
    @Log(description="平台中心获取会员详情接口:/api/member/info")
    public PublicResult<JSONObject> info(Long id) throws Exception{
        LOG.debug("get member info param = " + id);

        Member member = memberService.find(id);
        JSONObject result = member.convertEntity(member, new String[]{"memberRank","memberStatus","amount"});

        LOG.debug("get member info result = " + result.toString());
        return new PublicResult<>(PublicResultConstant.SUCCESS,result);
    }
    
    @PostMapping("/save")
    @ApiOperation(value="保存/更新会员", notes="保存会员",produces = "application/json")
    @Log(description="平台中心保存会员接口:/api/member/save")
    public PublicResult<String> save(@RequestBody JSONObject memberJo, HttpServletRequest request, @CurrentUser Admin current)throws Exception{
        Member member = JsonUtil.toBean(memberJo.toJSONString(), Member.class);
        if (null != member && null != member.getId()) {
            Integer modifyPoint = memberJo.getInteger("modifyPoint");
            BigDecimal modifyBalance = memberJo.getBigDecimal("modifyBalance");
            String depositMemo = memberJo.getString("depositMemo");
            Member pMember = memberService.find(member.getId());
            if (null == pMember){
                return new PublicResult<>(PublicResultConstant.ERROR,"会员不存在");
            }
            if (!(member.getUsername().equals(pMember.getUsername())) && memberService.usernameExists(member.getUsername())){
                return new PublicResult<>(PublicResultConstant.ERROR,"用户账号已存在");
            }
            if (!(member.getMobile().equals(pMember.getMobile())) && memberService.mobileExists(member.getMobile())){
                return new PublicResult<>(PublicResultConstant.ERROR,"手机号码已存在");
            }
            if (StringUtils.isNotEmpty(member.getPassword())) {
                pMember.setPassword(DigestUtils.md5Hex(member.getPassword()));
            }
            if (pMember.getIsLocked() && !member.getIsLocked()) {
                pMember.setLoginFailureCount(0);
                pMember.setLockedDate(null);
            }
            pMember.setUsername(member.getUsername());
            System.out.println("memberName:" + member.getName());
            System.out.println("name is not null:" + StringUtils.isNotBlank(member.getName()));
//            String nickName = StringUtils.isNotBlank(member.getName()) ? Base64.encodeBase64String(member.getName().getBytes("utf-8")) : member.getName();
            pMember.setName(member.getName());
            pMember.setMobile(member.getMobile());
            pMember.setIsLocked(member.getIsLocked());
            if (member.getIsLocked()){
                pMember.setLockedDate(new Date());
            }
            memberService.update(pMember, modifyPoint, modifyBalance, depositMemo, current);
        }else{
            member.setUsername(member.getUsername().toLowerCase());
            if (memberService.usernameExists(member.getUsername())){
                return new PublicResult<>(PublicResultConstant.ERROR,"用户账号已存在");
            }
            if (memberService.mobileExists(member.getMobile())){
                return new PublicResult<>(PublicResultConstant.ERROR,"手机号码已存在");
            }
            member.setPassword(DigestUtils.md5Hex(member.getPassword()));
            member.setAmount(new BigDecimal(0));
//            String nickName = Base64.encodeBase64String(member.getName().getBytes("utf-8"));
//            member.setName(nickName);
            member.setIsLocked(false);
            member.setLoginFailureCount(0);
            member.setLockedDate(null);
            member.setRegisterIp(request.getRemoteAddr());
            member.setLoginIp(null);
            member.setLoginDate(null);
            member.setOrders(null);
            member.setReceivers(null);
            member.setIsVip(false);
            member.setIsNew(true);
            if (null == member.getNature()) {
                member.setNature(Member.Nature.buyer);
            }
            member.setType(Member.Type.def);
            member.setCityName(null);
            member.setShareBalance(BigDecimal.ZERO);
            member.setHasShareOrder(false);
            memberService.save(member, current);
        }
        return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
    }

    /**
     * 删除
     */
    @GetMapping("/delete")
    @ApiOperation(value="批量删除会员", notes="批量删除会员",produces = "application/json")
    @Log(description="平台中心批量删除会员接口:/api/member/delete")
    public PublicResult<Boolean> delete(Long[] ids) {
        LOG.info("delete member param: " + ids);
        PublicResult<Boolean> result = null;
        try{
            if (ids != null) {
                for (Long id : ids) {
                    Member member = memberService.find(id);
                    if((member.getQuotations() != null && !member.getQuotations().isEmpty())
                            || (member.getOrders() != null && !member.getOrders().isEmpty())) {
                        return new PublicResult<Boolean>(PublicResultConstant.MEMBER_QUOTATION_IS_NOT_NULL_ERROR,null);
                    } else {
                        memberService.delete(id);
                    }
                }
            }
            //memberService.delete(ids);
            result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS,true);
        }catch (Exception e){
            LOG.error("delete member error. {}", e);
            result =new PublicResult<Boolean>(PublicResultConstant.FAILED,false);
        }
        LOG.info("delete member result = " + result.toString());
        return result;
    }

    /**
     * 下载优惠码
     */
    @PostMapping("/export")
    public PublicResult export(Integer count, @CurrentUser Admin admin) {
        if (count == null || count <= 0) {
            count = 100;
        }
       List<Member> members = memberService.findAll();

        JSONObject[] memberData = new JSONObject[members==null?0:members.size()];

        for(int i=0; i<members.size(); i++){
            memberData[i] = new JSONObject();
            memberData[i].put("userName", members.get(i).getUsername());
            String name = null;
//            try {
//                name = new String(Base64.decodeBase64(members.get(i).getName()), "utf-8");
//            }catch (Exception e){
//                //e.printStackTrace();
//            }
            memberData[i].put("name", members.get(i).getName());
            memberData[i].put("mobile", members.get(i).getMobile());
            memberData[i].put("type", members.get(i).getType().getDesc());
        }

        return new PublicResult<>(PublicResultConstant.SUCCESS, memberData);
    }

    /**
     * 下载会员图片
     */
 /*   @PostMapping("/download")
    @ApiOperation(value="下载会员图片", notes="下载会员图片",produces = "application/json")
    @Log(description="平台中心下载会员图片接口:/api/member/download")
    public void download(String filePath, HttpServletResponse response) {
        try {
            URL url = new URL(filePath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();

            //得到输入流
            InputStream inputStream = conn.getInputStream();
            String fileName = "aaa.jpg";
            fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename="+fileName);
            String len = String.valueOf(inputStream.available()/1000+"k");
            response.setHeader("Content-Length", len);
            OutputStream out = response.getOutputStream();
            byte[] getData = readInputStream(inputStream);
            out.write(getData);
            inputStream.close();
            out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/

    /**
     * 从输入流中获取字节数组
     * @param inputStream
     * @return
     * @throws IOException
     */
/*    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
        }
*/
    }
