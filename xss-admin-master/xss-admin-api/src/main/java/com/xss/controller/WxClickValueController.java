package com.xss.controller;

import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.WxClickValue;
import com.xss.service.WeixinService;
import com.xss.util.page.Pageable;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Controller - 微信关键字响应响应管理
 * 
 * @author  DannyZou
 * @version  2018-05-03
 */
@RestController
@RequestMapping("/api/wx_click_value")
public class WxClickValueController {

	private static final Log LOG = LogFactory.getLog(WxClickValueController.class);

	@Resource
	private WeixinService weixinService;

	/**
	 * 列表
	 */
	@ApiOperation(value="微信关键字响应列表", notes="微信关键字响应列表",produces = "application/json")
	@GetMapping("/list")
	@com.xss.annotation.Log(description="微信关键字响应列表接口:/api/wx_click_value/list")
	public PageResult<Object> list(Pageable pageable) {
		LOG.info("get WxClickValue list param : pageable = " + pageable.toString());
		try{
			JSONObject result = weixinService.getClickValueList(pageable.getPageNumber(), pageable.getPageSize());
			if (null == result){
				return new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
			}else{
				return new PageResult<Object>(result.getInteger("total"), result.getInteger("pageNumber"), result.getInteger("pageSize"), result.getJSONArray("clickValues"));
			}
		}catch (Exception e){
			LOG.error("get WxClickValue list error." + e);
			return new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
		}
	}

	@ApiOperation(value="微信关键字响应", notes="微信关键字响应",produces = "application/json")
	@GetMapping("/info")
	@com.xss.annotation.Log(description="微信关键字响应接口:/api/wx_click_value/info")
	public PublicResult<JSONObject> info(Long id)throws Exception{
		try {
			return new PublicResult<>(PublicResultConstant.SUCCESS, weixinService.getClickValueInfo(id));
		}catch (Exception e){
			e.printStackTrace();
			return new PublicResult<>(PublicResultConstant.ERROR, null);
		}
	}

	@ApiOperation(value="保存/更新微信关键字响应", notes="保存微信关键字响应",produces = "application/json")
	@PostMapping("/save")
	@com.xss.annotation.Log(description="平台中心保存微信关键字响应接口:/api/wx_click_value/save")
	public PublicResult<Boolean> save(@RequestBody WxClickValue wxClickValue, @CurrentUser Admin admin)throws Exception{

		if (null == wxClickValue.getId()) {
			weixinService.saveClickValue(wxClickValue);
		}else{
			weixinService.updateClickValue(wxClickValue);
		}
		return new PublicResult<>(PublicResultConstant.SUCCESS,true);
	}

	@ApiOperation(value="微信关键字响应删除", notes="通过id删除微信关键字响应",httpMethod = "GET")
	@GetMapping("/delete")
	@com.xss.annotation.Log(description="平台中心删除微信关键字响应列表接口:/api/wx_click_value/delete")
	public PublicResult<String> delete(Long[] ids) throws Exception{
		weixinService.deleteClickValue(ids);
		return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
	}
}