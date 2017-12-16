package com.qeweb.scm.purchasemodule.web.goodsrequest.vo;

import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;

public class GoodsRequestReportVo {
	
	private OrganizationEntity vendor; //供应商
	private MaterialEntity material;		//物料	
	private FactoryEntity factory;        //工厂
	private PurchasingGroupEntity group; //采购组
	private UserEntity buyer; //采购员
	private GoodsRequestReportHeadViewVo data; //数据
	private String  meins;
	public OrganizationEntity getVendor() {
		return vendor;
	}
	public void setVendor(OrganizationEntity vendor) {
		this.vendor = vendor;
	}
	public MaterialEntity getMaterial() {
		return material;
	}
	public void setMaterial(MaterialEntity material) {
		this.material = material;
	}
	public FactoryEntity getFactory() {
		return factory;
	}
	public void setFactory(FactoryEntity factory) {
		this.factory = factory;
	}
	public PurchasingGroupEntity getGroup() {
		return group;
	}
	public void setGroup(PurchasingGroupEntity group) {
		this.group = group;
	}
	public UserEntity getBuyer() {
		return buyer;
	}
	public void setBuyer(UserEntity buyer) {
		this.buyer = buyer;
	}
	public GoodsRequestReportHeadViewVo getData() {
		return data;
	}
	public void setData(GoodsRequestReportHeadViewVo data) {
		this.data = data;
	}
	public String getMeins() {
		return meins;
	}
	public void setMeins(String meins) {
		this.meins = meins;
	}
	
	

}
