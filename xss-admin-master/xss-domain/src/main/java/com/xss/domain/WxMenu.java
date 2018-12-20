package com.xss.domain;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ArrayUtils;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name = "xx_wx_menu")
//@SequenceGenerator(name = "sequenceGenerator", sequenceName = "xx_wx_menu_sequence")
public class WxMenu extends BaseEntity {

	public static final String[] DEFAULT_JSON_PARAMS = new String[]{"id", "name", "type", "category", "url", "keyName", "grade"};
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 链接类别
	 */
	public enum Category {
		notAuthorize(0,"不授权"), authorize(1,"页面授权");
		private int value;
		private String desc;

		Category(int value,String desc) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
			return desc;
		}

		public static Category findByDesc(String desc) {
			Category category = null;

			for (Category g : Category.values()) {
				if (g.getDesc().equals(desc)) {
					category = g;
					break;
				}
			}

			return category;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	public enum Type {
		click(0,"点击"), view(1,"跳转");
		private int value;
		private String desc;
		
		Type(int value,String desc) {
			this.desc = desc;
			this.value = value;
		}

		public String getDesc() {
        	return desc;
        }
		
		public static Type findByDesc(String desc) {
			Type type = null;
			
			for (Type g : Type.values()) {
				if (g.getDesc().equals(desc)) {
					type = g;
					break;
				}
			}
			
			return type;
		}

		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}
	}
	
	private String name;     
	private Type type;    
	private Category category;
	private String url;   
	private String keyName;
	private WxMenu parent;
	/** 层级 */
	private Integer grade;
	/** 下级分类 */
	private Set<WxMenu> children = new HashSet<WxMenu>();
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	
	@Transient
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	public WxMenu getParent() {
		return parent;
	}
	public void setParent(WxMenu parent) {
		this.parent = parent;
	}
	
	/**
	 * 获取层级
	 * 
	 * @return 层级
	 */
	@Column(nullable = false)
	public Integer getGrade() {
		return grade;
	}

	/**
	 * 设置层级
	 * 
	 * @param grade
	 *            层级
	 */
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
	@OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
	@OrderBy("createDate asc")
	public Set<WxMenu> getChildren() {
		return children;
	}
	public void setChildren(Set<WxMenu> children) {
		this.children = children;
	}
	
	@Transient
	public static WxMenu convertJson(JSONObject jo){
		WxMenu menu = new WxMenu();
		menu.setId(jo.getLong("id"));
		menu.setName(jo.getString("name"));
		menu.setKeyName(jo.getString("keyName"));
		menu.setUrl(jo.getString("url"));
		menu.setGrade(jo.getIntValue("grade"));
		menu.setType(Type.valueOf(jo.getString("type")));
		menu.setCategory(Category.valueOf(jo.getString("category")));
		if (null != jo.get("parent")) {
			WxMenu parent = new WxMenu();
			parent.setId(jo.getJSONObject("parent").getLong("id"));
			parent.setName(jo.getJSONObject("parent").getString("name"));
			parent.setGrade(jo.getJSONObject("parent").getIntValue("grade"));
			menu.setParent(parent);
		}
		return menu;
	}

	@Override
	public JSONObject convertEntity(Object entity, String[] params){
		WxMenu wxMenu = ((WxMenu) entity);
		JSONObject jo = super.convertEntity(entity,DEFAULT_JSON_PARAMS);
		if (ArrayUtils.contains(params, "children")){
			JSONArray childJa = new JSONArray();
			for (WxMenu child : wxMenu.getChildren()){
				JSONObject childJo = super.convertEntity(child,DEFAULT_JSON_PARAMS);
				childJa.add(childJo);
			}
			jo.put("children", childJa);
		}
		if (ArrayUtils.contains(params, "parent")){
			if (null != wxMenu.getParent()){
				JSONObject parentJo = super.convertEntity(wxMenu.getParent(),DEFAULT_JSON_PARAMS);
				jo.put("parent", parentJo);
			}

		}
		return jo;
	}
}
