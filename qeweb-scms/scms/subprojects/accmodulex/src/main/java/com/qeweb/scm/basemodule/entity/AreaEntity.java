package com.qeweb.scm.basemodule.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 地理区域管理
 * 国家、省、市、区镇……
 * @author lw
 * 创建时间：2015年6月2日14:16:51
 * 最后更新时间：2015年6月2日14:16:57
 * 最后更新人：lw
 */

@Entity
@Table(name="qeweb_area")
public class AreaEntity extends IdEntity {
	
	private String code;//编码
	private String name;//名称
	private Integer level; //级别
	private Long parentId;//上级Id
	private String upName;//上级名称
	
	@Transient
	public String getUpName() {
		return upName;
	}
	public void setUpName(String upName) {
		this.upName = upName;
	}
	@Column(name="area_level")
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	@Column(name="parent_id")
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
}
