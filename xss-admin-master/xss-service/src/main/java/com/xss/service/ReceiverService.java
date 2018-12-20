/*
 *  
 *  
 *  
 */
package com.xss.service;

import com.xss.base.PageResult;
import com.xss.dao.ReceiverDao;
import com.xss.domain.Area;
import com.xss.domain.Member;
import com.xss.domain.Receiver;
import com.xss.util.LoadPopertiesFile;
import com.xss.util.page.Page;
import com.xss.util.page.Pageable;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Service - 收货地址
 * 
 * @author DannyZou
 * @version 1.0
 */
@Service("receiverServiceImpl")
public class ReceiverService extends BaseService<Receiver, Long>{

	@Resource
	private ReceiverDao receiverDao;
	@Resource
	private AreaService areaService;

	@Resource
	public void setBaseDao(ReceiverDao receiverDao) {
		super.setBaseDao(receiverDao);
	}

	@Transactional(readOnly = true)
	public Receiver findDefault(Member member) {
		if (member == null) {
			return null;
		}
		try {
			String jpql = "select receiver from Receiver receiver where receiver.member = :member and receiver.isDefault = true";
			return entityManager.createQuery(jpql, Receiver.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).getSingleResult();
		} catch (NoResultException e) {
			try {
				String jpql = "select receiver from Receiver receiver where receiver.member = :member order by receiver.modifyDate desc";
				return entityManager.createQuery(jpql, Receiver.class).setFlushMode(FlushModeType.COMMIT).setParameter("member", member).setMaxResults(1).getSingleResult();
			} catch (NoResultException e1) {
				return null;
			}
		}
	}

	@Transactional(readOnly = true)
	public Page<Receiver> findPage(Member member, Pageable pageable) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Receiver> criteriaQuery = criteriaBuilder.createQuery(Receiver.class);
		Root<Receiver> root = criteriaQuery.from(Receiver.class);
		criteriaQuery.select(root);
		if (member != null) {
			criteriaQuery.where(criteriaBuilder.equal(root.get("member"), member));
		}
		return super.findPage(criteriaQuery, pageable);
	}

	@Transactional(readOnly = true)
	public PageResult<Object> getAreaList(JSONArray receiverArray , PageResult<Object> jsonResult){
		List<Area> areaList = areaService.findRoots();
		if (null != areaList && areaList.size() > 0) {
			for (Area area : areaList) {
				JSONObject receiverJo = new JSONObject();
				receiverJo.accumulate("code", area.getId());
				receiverJo.accumulate("name", area.getName());
				//子节点
				JSONArray areaArray1 = new JSONArray();
				for (Area area1 : area.getChildren()) {
					JSONObject areaJo1 = new JSONObject();
					areaJo1.accumulate("code", area1.getId());
					areaJo1.accumulate("name", area1.getName());
					//子节点
					JSONArray areaArray2 = new JSONArray();
					for (Area area2 : area1.getChildren()) {
						JSONObject areaJo2 = new JSONObject();
						areaJo2.accumulate("code", area2.getId());
						areaJo2.accumulate("name", area2.getName());
						areaArray2.add(areaJo2);
					}
					areaJo1.accumulate("childs", areaArray2);
					areaArray1.add(areaJo1);
				}
				receiverJo.accumulate("childs", areaArray1);
				receiverArray.add(receiverJo);
			}
		}
		jsonResult = new PageResult<Object>(receiverArray.size(), 0, 0, receiverArray);
		return jsonResult;
	}

	/**
	 * web端三级联动区域数据
	 * @return
	 */
	@Transactional(readOnly = true)
	public JSONObject areaListWeb(){
		JSONObject resultJo = new JSONObject();
		JSONObject area1Jo = new JSONObject();
		JSONObject area2Jo = new JSONObject();
		JSONObject area3Jo = new JSONObject();
		List<Area> areas =areaService.findRoots();
		for (Area area1 :areas){
			area1Jo.put(area1.getId(),area1.getName());
			if(area1.getId() != 1L &&area1.getId() != 18L&&area1.getId() != 792L&&area1.getId() != 2240L ){
				for(Area area2 :area1.getChildren()){
					area2Jo.put(area2.getId().toString(),area2.getName());
					if(area2.getChildren().size()>0){
						for(Area area3 : area2.getChildren()){
							area3Jo.put(area3.getId(),area3.getName());
						}
						resultJo.put(area2.getId().toString(),area3Jo);
						area3Jo.clear();
					}
				}
				resultJo.put(area1.getId().toString(),area2Jo);
				area2Jo.clear();
			}else{
				area2Jo.put(area1.getId().toString()+"0000","市辖区");
				for (Area area : area1.getChildren()){
					area3Jo.put(area.getId().toString(),area.getName());
				}
				resultJo.put(area1.getId().toString(),area2Jo);
				resultJo.put(area1.getId().toString()+"0000",area3Jo);
				area2Jo.clear();
				area3Jo.clear();
			}
		}
		resultJo.put("86",area1Jo);
		return resultJo;
	}

	/**
	 * 本地生成web端区域数据文件
	 */
	@Transactional
	public void createAreaFile(){
		String areaSource = this.areaListWeb().toString();
		byte[] sourceByte = areaSource.getBytes();
		if(null != sourceByte){
			try {
				File file = new File(LoadPopertiesFile.PROP_MAP.get("areaJson.local.path"));
				if (!file.exists()) {
					File dir = new File(file.getParent());
					dir.mkdirs();
					file.createNewFile();
				}
				FileOutputStream outStream = new FileOutputStream(file);
				outStream.write(sourceByte);
				outStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}