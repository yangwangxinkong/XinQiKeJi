package com.xss.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.xss.annotation.CurrentUser;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Admin;
import com.xss.domain.WxMenu;
import com.xss.service.WeixinService;
import com.xss.util.page.Pageable;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * Controller - 微信管理
 * 
 * @author  DannyZou
 * @version  [1.0, 2014-12-25]
 */
@RestController
@RequestMapping("/api/wx_menu")
public class WxMenuController {

	private static final Log LOG = LogFactory.getLog(WxMenuController.class);

	@Resource
	private WeixinService weixinService;

	/**
	 * 列表
	 */
	@GetMapping("/list")
	public PublicResult<JSONArray> list(Pageable pageable) {
		LOG.info("get admin list param : pageable = " + pageable.toString());
		try{
			return new PublicResult<JSONArray>(PublicResultConstant.SUCCESS, weixinService.getMenuList());
		}catch (Exception e){
			LOG.error("get admin list error." + e);
			return new PublicResult<JSONArray>(PublicResultConstant.ERROR,null);
		}
	}

	@ApiOperation(value="微信菜单列表", notes="微信菜单列表",produces = "application/json")
	@GetMapping("/tree")
	@com.xss.annotation.Log(description="微信菜单列表接口:/api/wx_menu/tree")
	public PublicResult<JSONArray> tree() throws Exception{
		try{
			return new PublicResult<JSONArray>(PublicResultConstant.SUCCESS, weixinService.getMenuList());
		}catch (Exception e){
			LOG.error("get admin list error." + e);
			return new PublicResult<JSONArray>(PublicResultConstant.ERROR,null);
		}
	}

	@ApiOperation(value="微信菜单", notes="微信菜单",produces = "application/json")
	@GetMapping("/info")
	@com.xss.annotation.Log(description="微信菜单接口:/api/wx_menu/info")
	public PublicResult<JSONObject> info(Long id)throws Exception{
		try {
			return new PublicResult<>(PublicResultConstant.SUCCESS, weixinService.getMenuInfo(id));
		}catch (Exception e){
			e.printStackTrace();
			return new PublicResult<>(PublicResultConstant.ERROR, null);
		}
	}

	@ApiOperation(value="保存/更新微信菜单", notes="保存微信菜单",produces = "application/json")
	@PostMapping("/save")
	@com.xss.annotation.Log(description="平台中心保存微信菜单接口:/api/wx_menu/save")
	public PublicResult<Boolean> save(@RequestBody WxMenu wxMenu, @CurrentUser Admin admin)throws Exception{

		if (null == wxMenu.getId()) {
			weixinService.saveMenu(wxMenu);
		}else{
			weixinService.updateMenu(wxMenu);
		}
		return new PublicResult<>(PublicResultConstant.SUCCESS,true);
	}

	@ApiOperation(value="微信菜单删除", notes="通过id删除微信菜单",httpMethod = "GET")
	@GetMapping("/delete")
	@com.xss.annotation.Log(description="平台中心删除微信菜单列表接口:/api/wx_menu/delete")
	public PublicResult<String> delete(Long id) throws Exception{
		weixinService.delMenu(id);
		return new PublicResult<>(PublicResultConstant.SUCCESS,"操作成功");
	}

	@ApiOperation(value="微信菜单发布", notes="微信菜单发布",httpMethod = "GET")
	@GetMapping("/publish")
	@com.xss.annotation.Log(description="微信菜单发布接口:/api/wx_menu/public")
    public PublicResult<String> publishWxMenu(HttpServletRequest request, Model model, RedirectAttributes redirectAttributes) throws UnsupportedEncodingException {
    	try{
    		String result = weixinService.publishMenu();
    		System.out.println("publish wx menu:" + result);
    		if(JSONObject.parseObject(result).getInteger("errcode")==0){
        		return new PublicResult<String>(PublicResultConstant.SUCCESS, "菜单发布成功");
    		}else{
				return new PublicResult<String>(PublicResultConstant.ERROR, "菜单发布失败");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
			return new PublicResult<String>(PublicResultConstant.ERROR, "菜单发布失败");
    	}
    }

}