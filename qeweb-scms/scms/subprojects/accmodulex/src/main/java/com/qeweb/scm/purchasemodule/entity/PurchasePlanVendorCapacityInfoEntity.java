package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

@Entity
@Table(name = "QEWEB_VENDOR_CAPACITY_INFO")
public class PurchasePlanVendorCapacityInfoEntity extends BaseEntity {
	
	private PurchasePlanItemEntity planItem;
	private String capa1;
	private String capa2;
	private String capa3;
	private String capa4;
	private String capa5;
	private String capa6;
	private String capa7;
	private String capa8;
	private String capa9;
	private String capa10;
	private String capa11;
	private String capa12;
	private String capa13;
	private String capa14;
	private String capa15;
	private String capa16;
	private String capa17;
	private String capa18;
	private String capa19;
	private String capa20;
	
	
	
	@ManyToOne
	@JoinColumn(name="plan_item_id")
	public PurchasePlanItemEntity getPlanItem() {
		return planItem;
	}


	public void setPlanItem(PurchasePlanItemEntity planItem) {
		this.planItem = planItem;
	}


	public String getCapa1() {
		return capa1;
	}



	public void setCapa1(String capa1) {
		this.capa1 = capa1;
	}



	public String getCapa2() {
		return capa2;
	}



	public void setCapa2(String capa2) {
		this.capa2 = capa2;
	}



	public String getCapa3() {
		return capa3;
	}



	public void setCapa3(String capa3) {
		this.capa3 = capa3;
	}



	public String getCapa4() {
		return capa4;
	}



	public void setCapa4(String capa4) {
		this.capa4 = capa4;
	}



	public String getCapa5() {
		return capa5;
	}



	public void setCapa5(String capa5) {
		this.capa5 = capa5;
	}



	public String getCapa6() {
		return capa6;
	}



	public void setCapa6(String capa6) {
		this.capa6 = capa6;
	}



	public String getCapa7() {
		return capa7;
	}



	public void setCapa7(String capa7) {
		this.capa7 = capa7;
	}



	public String getCapa8() {
		return capa8;
	}



	public void setCapa8(String capa8) {
		this.capa8 = capa8;
	}



	public String getCapa9() {
		return capa9;
	}



	public void setCapa9(String capa9) {
		this.capa9 = capa9;
	}



	public String getCapa10() {
		return capa10;
	}



	public void setCapa10(String capa10) {
		this.capa10 = capa10;
	}



	public String getCapa11() {
		return capa11;
	}



	public void setCapa11(String capa11) {
		this.capa11 = capa11;
	}



	public String getCapa12() {
		return capa12;
	}



	public void setCapa12(String capa12) {
		this.capa12 = capa12;
	}



	public String getCapa13() {
		return capa13;
	}



	public void setCapa13(String capa13) {
		this.capa13 = capa13;
	}



	public String getCapa14() {
		return capa14;
	}



	public void setCapa14(String capa14) {
		this.capa14 = capa14;
	}



	public String getCapa15() {
		return capa15;
	}



	public void setCapa15(String capa15) {
		this.capa15 = capa15;
	}



	public String getCapa16() {
		return capa16;
	}



	public void setCapa16(String capa16) {
		this.capa16 = capa16;
	}



	public String getCapa17() {
		return capa17;
	}



	public void setCapa17(String capa17) {
		this.capa17 = capa17;
	}



	public String getCapa18() {
		return capa18;
	}



	public void setCapa18(String capa18) {
		this.capa18 = capa18;
	}



	public String getCapa19() {
		return capa19;
	}



	public void setCapa19(String capa19) {
		this.capa19 = capa19;
	}



	public String getCapa20() {
		return capa20;
	}



	public void setCapa20(String capa20) {
		this.capa20 = capa20;
	}



    
}
