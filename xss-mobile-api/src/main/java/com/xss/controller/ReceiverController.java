/*
 *  
 *  
 *  
 */
package com.xss.controller;

import com.xss.annotation.CurrentUser;
import com.xss.annotation.Log;
import com.xss.base.PageResult;
import com.xss.base.PublicResult;
import com.xss.base.PublicResultConstant;
import com.xss.domain.Area;
import com.xss.domain.Member;
import com.xss.domain.Receiver;
import com.xss.service.AreaService;
import com.xss.service.ReceiverService;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller  - 收货地址
 * 
 * @author DannyZou
 * @version 1.0
 */
@RestController
@RequestMapping("/m/receiver")
@Api(description="收货地址模块")
public class ReceiverController {

	private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(ReceiverController.class);

	@Autowired
	private AreaService areaService;
	@Autowired
	private ReceiverService receiverService;

	/**
	 * 列表
	 */
	@GetMapping("/areaList")
	@ApiOperation(value="地址列表", notes="地址列表",produces = "application/json")
	@Log(description="地址列表接口:/m/receiver/areaList")
	public PageResult<Object> areaList() {

		PageResult<Object> result = null;
		try{
			JSONObject jo = new JSONObject();

			//List<Area> areaList2 = areaService.findAll();

			List<Area> areaList = areaService.findRoots();

			JSONArray receiverArray = new JSONArray();
			if (null != areaList && areaList.size() > 0) {
				for(Area area : areaList){
					//JSONObject receiverJo = JSONUtil.toJSONObject(receiver, new String[]{"id","consignee","areaName","address"});
					JSONObject receiverJo = new JSONObject();
					receiverJo.accumulate("value",area.getId().toString());
					receiverJo.accumulate("name",area.getName());

					receiverArray.add(receiverJo);

					//子节点
					//JSONArray areaArray1 = new JSONArray();
					for(Area area1 : area.getChildren()) {
						JSONObject areaJo1 = new JSONObject();
						areaJo1.accumulate("value",area1.getId().toString());
						areaJo1.accumulate("name",area1.getName());
						areaJo1.accumulate("parent",area.getId().toString());

						receiverArray.add(areaJo1);

						//子节点
						//JSONArray areaArray2 = new JSONArray();
						for(Area area2 : area1.getChildren()) {
							JSONObject areaJo2 = new JSONObject();
							areaJo2.accumulate("value",area2.getId().toString());
							areaJo2.accumulate("name",area2.getName());
							areaJo2.accumulate("parent",area1.getId().toString());
							receiverArray.add(areaJo2);

							//子节点
							//JSONArray areaArray2 = new JSONArray();
//							for(Area area3 : area2.getChildren()) {
//								JSONObject areaJo3 = new JSONObject();
//								areaJo3.accumulate("value",area3.getId().toString());
//								areaJo3.accumulate("name",area3.getName());
//								areaJo3.accumulate("parent",area2.getId().toString());
//								receiverArray.add(areaJo3);
//							}
						}

						//areaJo1.accumulate("childs",areaArray2);
						//areaArray1.add(areaJo1);
					}

					//receiverJo.accumulate("childs",areaArray1);
					//receiverArray.add(receiverJo);
				}
				//jo.accumulate("iTotalDisplayRecords", page.getTotal());
				//jo.accumulate("iTotalRecords", page.getPageSize());
			}else{

				//jo.accumulate("iTotalDisplayRecords", 0);
				//jo.accumulate("iTotalRecords", 0);
			}

			result = new PageResult<Object>(receiverArray.size(), 0, 0, receiverArray);
		}catch (Exception e){
			LOG.error("get Product list error. {}", e);
			result = new PageResult<Object>(0, 0, 0, null);
		}
		LOG.debug("get Product list result = " + result.toString());
		return result;
	}

	/**
	 * 列表
	 */
	@GetMapping("/list")
	@ApiOperation(value="地址列表", notes="地址列表",produces = "application/json")
	@Log(description="地址列表接口:/m/receiver/list")
	public PageResult<Object> list(@CurrentUser Member member) {
		PageResult<Object> result = null;
		try{
			JSONObject jo = new JSONObject();

			Page<Receiver> page = receiverService.findPage(member, new Pageable());
			JSONArray receiverArray = new JSONArray();
			if (null != page && null != page.getContent() && !page.getContent().isEmpty()) {

				result = new PageResult<>((int) page.getContent().size(), 1, 1,
						receiverService.createEntity().convertList(page.getContent(), new String[]{"area"}));
			}

		}catch (Exception e){
			LOG.error("get Product list error. {}", e);
			result = new PageResult<Object>(0, 0, 0, null);
		}
		LOG.debug("get Product list result = " + result.toString());
		return result;
	}

	/**
	 * 地址信息
	 */
	@GetMapping("/info")
	@ApiOperation(value="地址信息", notes="地址信息",produces = "application/json")
	@Log(description="地址信息接口:/m/receiver/info")
	public PublicResult<Object> info(Long id) {
		PublicResult<Object> result = null;
		try{
			JSONObject jo = new JSONObject();

			Receiver receiver = receiverService.find(id);

			result = new PublicResult<>(PublicResultConstant.SUCCESS, receiverService.createEntity().convertEntity(receiver, new String[]{"treePath"}));

		}catch (Exception e){
			LOG.error("get Product list error. {}", e);
			result = new PublicResult<>(PublicResultConstant.FAILED, null);
		}
		LOG.debug("get Product list result = " + result.toString());
		return result;
	}


	/**
	 * 保存
	 */
	@PostMapping("/save")
	@ApiOperation(value="地址保存", notes="地址保存",produces = "application/json")
	@Log(description="地址保存接口:/m/receiver/save")
	public PublicResult<Boolean> save(Receiver receiver, Long areaId, @CurrentUser Member member) {

		PublicResult<Boolean> result = null;
		try {

			if(areaId == null) {
				areaId = 2L;
			}
			receiver.setArea(areaService.find(areaId));

			// 更新
			if(receiver.getId() != null) {
				Receiver pReceiver = receiverService.find(receiver.getId());
				if (pReceiver == null) {
					return new PublicResult<Boolean>(PublicResultConstant.RECEIVER_NULL_ERROR, false);
				}

				if (!member.getId().equals(pReceiver.getMember().getId())) {
					return new PublicResult<Boolean>(PublicResultConstant.RECEIVER_MEMBER_ERROR, false);
				}
				receiverService.update(receiver, "member");
			} else {
				// 新增
				if (Receiver.MAX_RECEIVER_COUNT != null && member.getReceivers().size() >= Receiver.MAX_RECEIVER_COUNT) {
					//return ERROR_VIEW;
					return new PublicResult<Boolean>(PublicResultConstant.MAX_RECEIVER_COUNT_ERROR, false);
				}
				receiver.setMember(member);
				receiverService.save(receiver);
			}

			result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);

		} catch (Exception e) {
			LOG.error("add receiver error. {}", e);
			result =new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
		}
		LOG.info("add receiver result = " + result.toString());
		return result;
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@ApiOperation(value="地址删除", notes="地址删除",produces = "application/json")
	@Log(description="地址删除接口:/web/receiver/delete")
	public PublicResult<Boolean> delete(Long id, @CurrentUser Member member) {
		PublicResult<Boolean> result = null;
		try {
			List<Receiver> list = receiverService.findList(id);
			if (list.size()==0) {
				return new PublicResult<Boolean>(PublicResultConstant.RECEIVER_NULL_ERROR, false);
			}

			for(Receiver receiver : list){
				if (!member.getId().equals(receiver.getMember().getId())) {
					return new PublicResult<Boolean>(PublicResultConstant.RECEIVER_MEMBER_ERROR, false);
				}
			}
			receiverService.delete(id);
			result = new PublicResult<Boolean>(PublicResultConstant.SUCCESS, true);

		} catch (Exception e) {
			LOG.error("delete receiver error. {}", e);
			result =new PublicResult<Boolean>(PublicResultConstant.FAILED, false);
		}
		LOG.info("delete receiver result = " + result.toString());
		return result;
	}

}