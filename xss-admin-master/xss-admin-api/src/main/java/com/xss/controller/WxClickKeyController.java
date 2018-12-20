package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.WxClickKey;
import com.xss.service.WeixinService;
import com.xss.util.page.Pageable;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * Controller - 微信关键字管理
 * 
 * @author  DannyZou
 * @version  [1.0, 2014-12-25]
 */
@RestController
@RequestMapping("/api/wx_click_key")
public class WxClickKeyController {

	private static final Log LOG = LogFactory.getLog(WxClickKeyController.class);

	@Resource
	private WeixinService weixinService;

	/**
	 * 列表
	 */
	@ApiOperation(value="微信关键字列表", notes="微信关键字列表",produces = "application/json")
	@GetMapping("/list")
	@com.xss.annotation.Log(description="微信关键字列表接口:/api/wx_click_key/list")
	public PageResult<Object> list(Pageable pageable) {
		LOG.info("get WxClickKey list param : pageable = " + pageable.toString());
		try{
			JSONObject result = weixinService.getClickKeyList(pageable.getPageNumber(), pageable.getPageSize());
			if (null == result){
				return new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
			}else{
				return new PageResult<Object>(result.getInteger("total"), result.getInteger("pageNumber"), result.getInteger("pageSize"), result.getJSONArray("clickKeys"));
			}
		}catch (Exception e){
			LOG.error("get WxClickKey list error." + e);
			return new PageResult<Object>(0,pageable.getPageNumber(),pageable.getPageSize(),null);
		}
	}

	@ApiOperation(value="获取所有微信关键字", notes="获取所有微信关键字",produces = "application/json")
	@GetMapping("/all")
	@com.xss.annotation.Log(description="获取所有微信关键字接口:/api/wx_click_key/all")
	public PublicResult<JSONArray> all()throws Exception{
		try {
			return new PublicResult<>(PublicResultConstant.SUCCESS, weixinService.getClickKeyAll());
		}catch (Exception e){
			e.printStackTrace();
			return new PublicResult<>(PublicResultConstant.ERROR, null);
		}
	}

	@ApiOperation(value="微信关键字", notes="微信关键字",produces = "application/json")
	@GetMapping("/info")
	@com.xss.annotation.Log(description="微信关键字接口:/api/wx_click_key/info")
	public PublicResult<JSONObject> info(Long id)throws Exception{
		try {
			return new PublicResult<>(PublicResultConstant.SUCCESS, weixinService.getClickKeyInfo(id));
		}catch (Exception e){
			e.printStackTrace();
			return new PublicResult<>(PublicResultConstant.ERROR, null);
		}
	}

	@ApiOperation(value="保存/更新微信关键字", notes="保存微信关键字",produces = "application/json")
	@PostMapping("/save")
	@com.xss.annotation.Log(description="平台中心保存微信关键字接口:/api/wx_click_key/save")
	public PublicResult<Boolean> save(@RequestBody WxClickKey wxClickKey, @CurrentUser Admin admin)throws Exception{

		if (null == wxClickKey.getId()) {
			weixinService.saveClickKey(wxClickKey.getName(), wxClickKey.getKeyString(), wxClickKey.getClickType().name(), wxClickKey.getClickNum());
		}else{
			weixinService.updateClickKey(wxClickKey.getId(), wxClickKey.getName(), wxClickKey.getKeyString(), wxClickKey.getClickType().name(), wxClickKey.getClickNum());
		}
		return new PublicResult<>(PublicResultConstant.SUCCESS,true);
	}

	@ApiOperation(value="微信关键字删除", notes="通过id删除微信关键字",httpMethod = "GET")
	@GetMapping("/delete")
	@com.xss.annotation.Log(description="平台中心删除微信关键字列表接口:/api/wx_click_key/delete")
	public PublicResult<String> delete(Long[] ids) throws Exception{
		weixinService.deleteClickKey(ids);
		return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
	}
}