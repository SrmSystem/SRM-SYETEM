package com.qeweb.scm.purchasemodule.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.IdEntity;

/**
 * 采购员看板
 * @author haiming.huang
 *
 */
@Entity
@Table(name = "QEWEB_BUYER_BOARD")
public class BuyerBoardEntity extends IdEntity {

	private static final long serialVersionUID = 1L;
	
	private String	factoryCode; //工厂编码
	private String	materialCode;//物料编码
	private String	materialName;//物料描述
	private String	groupCode;//采购组编码
	private String	vendorCode;//供应商
	private String	proportion;//占比
	private String	plannedTurnaroundDay;//计划周转天
	private String	qualityDay;//质量检验天数
	private String	deliveryFrequency;//送货频率
	private String	transportationDay;//运输天数
	private String	flag;//新品/量产标识（X - 新品，空白 - 量产）
	private String	dtype;//排产数量/到货数量/库存数量/发货数量
	private String	unit;//单位
	private String DAT01;
	private String QTY01;
	private String DAT02;
	private String QTY02;
	private String DAT03;
	private String QTY03;
	private String DAT04;
	private String QTY04;
	private String DAT05;
	private String QTY05;
	private String DAT06;
	private String QTY06;
	private String DAT07;
	private String QTY07;
	private String DAT08;
	private String QTY08;
	private String DAT09;
	private String QTY09;
	private String DAT10;
	private String QTY10;
	private String DAT11;
	private String QTY11;
	private String DAT12;
	private String QTY12;
	private String DAT13;
	private String QTY13;
	private String DAT14;
	private String QTY14;
	private String DAT15;
	private String QTY15;
	private String DAT16;
	private String QTY16;
	private String DAT17;
	private String QTY17;
	private String DAT18;
	private String QTY18;
	private String DAT19;
	private String QTY19;
	private String DAT20;
	private String QTY20;
	private String DAT21;
	private String QTY21;
	private String DAT22;
	private String QTY22;
	private String DAT23;
	private String QTY23;
	private String DAT24;
	private String QTY24;
	private String DAT25;
	private String QTY25;
	private String DAT26;
	private String QTY26;
	private String DAT27;
	private String QTY27;
	private String DAT28;
	private String QTY28;
	private String DAT29;
	private String QTY29;
	private String DAT30;
	private String QTY30;
	private String DAT31;
	private String QTY31;
	private String DAT32;
	private String QTY32;
	private String DAT33;
	private String QTY33;
	private String DAT34;
	private String QTY34;
	private String DAT35;
	private String QTY35;
	private String DAT36;
	private String QTY36;
	private String DAT37;
	private String QTY37;
	private String DAT38;
	private String QTY38;
	private String DAT39;
	private String QTY39;
	private String DAT40;
	private String QTY40;
	private String DAT41;
	private String QTY41;
	private String DAT42;
	private String QTY42;
	private String DAT43;
	private String QTY43;
	private String DAT44;
	private String QTY44;
	private String DAT45;
	private String QTY45;
	private String DAT46;
	private String QTY46;
	private String DAT47;
	private String QTY47;
	private String DAT48;
	private String QTY48;
	private String DAT49;
	private String QTY49;
	private String DAT50;
	private String QTY50;
	private String DAT51;
	private String QTY51;
	private String DAT52;
	private String QTY52;
	private String DAT53;
	private String QTY53;
	private String DAT54;
	private String QTY54;
	private String DAT55;
	private String QTY55;
	private String DAT56;
	private String QTY56;
	private String DAT57;
	private String QTY57;
	private String DAT58;
	private String QTY58;
	private String DAT59;
	private String QTY59;
	private String DAT60;
	private String QTY60;
	private String state;
	private Long groupId;

	
	
	@Column(name="FACTORY_CODE")
	public String getFactoryCode() {
		return factoryCode;
	}
	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}
	
	@Column(name="MATERIAL_CODE")
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	
	@Column(name="MATERIAL_NAME")
	public String getMaterialName() {
		return materialName;
	}
	public void setMaterialName(String materialName) {
		this.materialName = materialName;
	}
	
	@Column(name="GROUP_CODE")
	public String getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(String groupCode) {
		this.groupCode = groupCode;
	}
	
	@Column(name="VENDOR_CODE")
	public String getVendorCode() {
		return vendorCode;
	}
	public void setVendorCode(String vendorCode) {
		this.vendorCode = vendorCode;
	}
	
	public String getProportion() {
		return proportion;
	}
	public void setProportion(String proportion) {
		this.proportion = proportion;
	}
	
	@Column(name="PLANNED_TURNAROUND_DAY")
	public String getPlannedTurnaroundDay() {
		return plannedTurnaroundDay;
	}
	public void setPlannedTurnaroundDay(String plannedTurnaroundDay) {
		this.plannedTurnaroundDay = plannedTurnaroundDay;
	}
	
	@Column(name="QUALITY_DAY")
	public String getQualityDay() {
		return qualityDay;
	}
	public void setQualityDay(String qualityDay) {
		this.qualityDay = qualityDay;
	}
	
	@Column(name="DELIVERY_FREQUENCY")
	public String getDeliveryFrequency() {
		return deliveryFrequency;
	}
	public void setDeliveryFrequency(String deliveryFrequency) {
		this.deliveryFrequency = deliveryFrequency;
	}
	
	@Column(name="TRANSPORTATION_DAY")
	public String getTransportationDay() {
		return transportationDay;
	}
	public void setTransportationDay(String transportationDay) {
		this.transportationDay = transportationDay;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getDtype() {
		return dtype;
	}
	public void setDtype(String dtype) {
		this.dtype = dtype;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDAT01() {
		return DAT01;
	}
	public void setDAT01(String dAT01) {
		DAT01 = dAT01;
	}
	public String getQTY01() {
		return QTY01;
	}
	public void setQTY01(String qTY01) {
		QTY01 = qTY01;
	}
	public String getDAT02() {
		return DAT02;
	}
	public void setDAT02(String dAT02) {
		DAT02 = dAT02;
	}
	public String getQTY02() {
		return QTY02;
	}
	public void setQTY02(String qTY02) {
		QTY02 = qTY02;
	}
	public String getDAT03() {
		return DAT03;
	}
	public void setDAT03(String dAT03) {
		DAT03 = dAT03;
	}
	public String getQTY03() {
		return QTY03;
	}
	public void setQTY03(String qTY03) {
		QTY03 = qTY03;
	}
	public String getDAT04() {
		return DAT04;
	}
	public void setDAT04(String dAT04) {
		DAT04 = dAT04;
	}
	public String getQTY04() {
		return QTY04;
	}
	public void setQTY04(String qTY04) {
		QTY04 = qTY04;
	}
	public String getDAT05() {
		return DAT05;
	}
	public void setDAT05(String dAT05) {
		DAT05 = dAT05;
	}
	public String getQTY05() {
		return QTY05;
	}
	public void setQTY05(String qTY05) {
		QTY05 = qTY05;
	}
	public String getDAT06() {
		return DAT06;
	}
	public void setDAT06(String dAT06) {
		DAT06 = dAT06;
	}
	public String getQTY06() {
		return QTY06;
	}
	public void setQTY06(String qTY06) {
		QTY06 = qTY06;
	}
	public String getDAT07() {
		return DAT07;
	}
	public void setDAT07(String dAT07) {
		DAT07 = dAT07;
	}
	public String getQTY07() {
		return QTY07;
	}
	public void setQTY07(String qTY07) {
		QTY07 = qTY07;
	}
	public String getDAT08() {
		return DAT08;
	}
	public void setDAT08(String dAT08) {
		DAT08 = dAT08;
	}
	public String getQTY08() {
		return QTY08;
	}
	public void setQTY08(String qTY08) {
		QTY08 = qTY08;
	}
	public String getDAT09() {
		return DAT09;
	}
	public void setDAT09(String dAT09) {
		DAT09 = dAT09;
	}
	public String getQTY09() {
		return QTY09;
	}
	public void setQTY09(String qTY09) {
		QTY09 = qTY09;
	}
	public String getDAT10() {
		return DAT10;
	}
	public void setDAT10(String dAT10) {
		DAT10 = dAT10;
	}
	public String getQTY10() {
		return QTY10;
	}
	public void setQTY10(String qTY10) {
		QTY10 = qTY10;
	}
	public String getDAT11() {
		return DAT11;
	}
	public void setDAT11(String dAT11) {
		DAT11 = dAT11;
	}
	public String getQTY11() {
		return QTY11;
	}
	public void setQTY11(String qTY11) {
		QTY11 = qTY11;
	}
	public String getDAT12() {
		return DAT12;
	}
	public void setDAT12(String dAT12) {
		DAT12 = dAT12;
	}
	public String getQTY12() {
		return QTY12;
	}
	public void setQTY12(String qTY12) {
		QTY12 = qTY12;
	}
	public String getDAT13() {
		return DAT13;
	}
	public void setDAT13(String dAT13) {
		DAT13 = dAT13;
	}
	public String getQTY13() {
		return QTY13;
	}
	public void setQTY13(String qTY13) {
		QTY13 = qTY13;
	}
	public String getDAT14() {
		return DAT14;
	}
	public void setDAT14(String dAT14) {
		DAT14 = dAT14;
	}
	public String getQTY14() {
		return QTY14;
	}
	public void setQTY14(String qTY14) {
		QTY14 = qTY14;
	}
	public String getDAT15() {
		return DAT15;
	}
	public void setDAT15(String dAT15) {
		DAT15 = dAT15;
	}
	public String getQTY15() {
		return QTY15;
	}
	public void setQTY15(String qTY15) {
		QTY15 = qTY15;
	}
	public String getDAT16() {
		return DAT16;
	}
	public void setDAT16(String dAT16) {
		DAT16 = dAT16;
	}
	public String getQTY16() {
		return QTY16;
	}
	public void setQTY16(String qTY16) {
		QTY16 = qTY16;
	}
	public String getDAT17() {
		return DAT17;
	}
	public void setDAT17(String dAT17) {
		DAT17 = dAT17;
	}
	public String getQTY17() {
		return QTY17;
	}
	public void setQTY17(String qTY17) {
		QTY17 = qTY17;
	}
	public String getDAT18() {
		return DAT18;
	}
	public void setDAT18(String dAT18) {
		DAT18 = dAT18;
	}
	public String getQTY18() {
		return QTY18;
	}
	public void setQTY18(String qTY18) {
		QTY18 = qTY18;
	}
	public String getDAT19() {
		return DAT19;
	}
	public void setDAT19(String dAT19) {
		DAT19 = dAT19;
	}
	public String getQTY19() {
		return QTY19;
	}
	public void setQTY19(String qTY19) {
		QTY19 = qTY19;
	}
	public String getDAT20() {
		return DAT20;
	}
	public void setDAT20(String dAT20) {
		DAT20 = dAT20;
	}
	public String getQTY20() {
		return QTY20;
	}
	public void setQTY20(String qTY20) {
		QTY20 = qTY20;
	}
	public String getDAT21() {
		return DAT21;
	}
	public void setDAT21(String dAT21) {
		DAT21 = dAT21;
	}
	public String getQTY21() {
		return QTY21;
	}
	public void setQTY21(String qTY21) {
		QTY21 = qTY21;
	}
	public String getDAT22() {
		return DAT22;
	}
	public void setDAT22(String dAT22) {
		DAT22 = dAT22;
	}
	public String getQTY22() {
		return QTY22;
	}
	public void setQTY22(String qTY22) {
		QTY22 = qTY22;
	}
	public String getDAT23() {
		return DAT23;
	}
	public void setDAT23(String dAT23) {
		DAT23 = dAT23;
	}
	public String getQTY23() {
		return QTY23;
	}
	public void setQTY23(String qTY23) {
		QTY23 = qTY23;
	}
	public String getDAT24() {
		return DAT24;
	}
	public void setDAT24(String dAT24) {
		DAT24 = dAT24;
	}
	public String getQTY24() {
		return QTY24;
	}
	public void setQTY24(String qTY24) {
		QTY24 = qTY24;
	}
	public String getDAT25() {
		return DAT25;
	}
	public void setDAT25(String dAT25) {
		DAT25 = dAT25;
	}
	public String getQTY25() {
		return QTY25;
	}
	public void setQTY25(String qTY25) {
		QTY25 = qTY25;
	}
	public String getDAT26() {
		return DAT26;
	}
	public void setDAT26(String dAT26) {
		DAT26 = dAT26;
	}
	public String getQTY26() {
		return QTY26;
	}
	public void setQTY26(String qTY26) {
		QTY26 = qTY26;
	}
	public String getDAT27() {
		return DAT27;
	}
	public void setDAT27(String dAT27) {
		DAT27 = dAT27;
	}
	public String getQTY27() {
		return QTY27;
	}
	public void setQTY27(String qTY27) {
		QTY27 = qTY27;
	}
	public String getDAT28() {
		return DAT28;
	}
	public void setDAT28(String dAT28) {
		DAT28 = dAT28;
	}
	public String getQTY28() {
		return QTY28;
	}
	public void setQTY28(String qTY28) {
		QTY28 = qTY28;
	}
	public String getDAT29() {
		return DAT29;
	}
	public void setDAT29(String dAT29) {
		DAT29 = dAT29;
	}
	public String getQTY29() {
		return QTY29;
	}
	public void setQTY29(String qTY29) {
		QTY29 = qTY29;
	}
	public String getDAT30() {
		return DAT30;
	}
	public void setDAT30(String dAT30) {
		DAT30 = dAT30;
	}
	public String getQTY30() {
		return QTY30;
	}
	public void setQTY30(String qTY30) {
		QTY30 = qTY30;
	}
	public String getDAT31() {
		return DAT31;
	}
	public void setDAT31(String dAT31) {
		DAT31 = dAT31;
	}
	public String getQTY31() {
		return QTY31;
	}
	public void setQTY31(String qTY31) {
		QTY31 = qTY31;
	}
	public String getDAT32() {
		return DAT32;
	}
	public void setDAT32(String dAT32) {
		DAT32 = dAT32;
	}
	public String getQTY32() {
		return QTY32;
	}
	public void setQTY32(String qTY32) {
		QTY32 = qTY32;
	}
	public String getDAT33() {
		return DAT33;
	}
	public void setDAT33(String dAT33) {
		DAT33 = dAT33;
	}
	public String getQTY33() {
		return QTY33;
	}
	public void setQTY33(String qTY33) {
		QTY33 = qTY33;
	}
	public String getDAT34() {
		return DAT34;
	}
	public void setDAT34(String dAT34) {
		DAT34 = dAT34;
	}
	public String getQTY34() {
		return QTY34;
	}
	public void setQTY34(String qTY34) {
		QTY34 = qTY34;
	}
	public String getDAT35() {
		return DAT35;
	}
	public void setDAT35(String dAT35) {
		DAT35 = dAT35;
	}
	public String getQTY35() {
		return QTY35;
	}
	public void setQTY35(String qTY35) {
		QTY35 = qTY35;
	}
	public String getDAT36() {
		return DAT36;
	}
	public void setDAT36(String dAT36) {
		DAT36 = dAT36;
	}
	public String getQTY36() {
		return QTY36;
	}
	public void setQTY36(String qTY36) {
		QTY36 = qTY36;
	}
	public String getDAT37() {
		return DAT37;
	}
	public void setDAT37(String dAT37) {
		DAT37 = dAT37;
	}
	public String getQTY37() {
		return QTY37;
	}
	public void setQTY37(String qTY37) {
		QTY37 = qTY37;
	}
	public String getDAT38() {
		return DAT38;
	}
	public void setDAT38(String dAT38) {
		DAT38 = dAT38;
	}
	public String getQTY38() {
		return QTY38;
	}
	public void setQTY38(String qTY38) {
		QTY38 = qTY38;
	}
	public String getDAT39() {
		return DAT39;
	}
	public void setDAT39(String dAT39) {
		DAT39 = dAT39;
	}
	public String getQTY39() {
		return QTY39;
	}
	public void setQTY39(String qTY39) {
		QTY39 = qTY39;
	}
	public String getDAT40() {
		return DAT40;
	}
	public void setDAT40(String dAT40) {
		DAT40 = dAT40;
	}
	public String getQTY40() {
		return QTY40;
	}
	public void setQTY40(String qTY40) {
		QTY40 = qTY40;
	}
	public String getDAT41() {
		return DAT41;
	}
	public void setDAT41(String dAT41) {
		DAT41 = dAT41;
	}
	public String getQTY41() {
		return QTY41;
	}
	public void setQTY41(String qTY41) {
		QTY41 = qTY41;
	}
	public String getDAT42() {
		return DAT42;
	}
	public void setDAT42(String dAT42) {
		DAT42 = dAT42;
	}
	public String getQTY42() {
		return QTY42;
	}
	public void setQTY42(String qTY42) {
		QTY42 = qTY42;
	}
	public String getDAT43() {
		return DAT43;
	}
	public void setDAT43(String dAT43) {
		DAT43 = dAT43;
	}
	public String getQTY43() {
		return QTY43;
	}
	public void setQTY43(String qTY43) {
		QTY43 = qTY43;
	}
	public String getDAT44() {
		return DAT44;
	}
	public void setDAT44(String dAT44) {
		DAT44 = dAT44;
	}
	public String getQTY44() {
		return QTY44;
	}
	public void setQTY44(String qTY44) {
		QTY44 = qTY44;
	}
	public String getDAT45() {
		return DAT45;
	}
	public void setDAT45(String dAT45) {
		DAT45 = dAT45;
	}
	public String getQTY45() {
		return QTY45;
	}
	public void setQTY45(String qTY45) {
		QTY45 = qTY45;
	}
	public String getDAT46() {
		return DAT46;
	}
	public void setDAT46(String dAT46) {
		DAT46 = dAT46;
	}
	public String getQTY46() {
		return QTY46;
	}
	public void setQTY46(String qTY46) {
		QTY46 = qTY46;
	}
	public String getDAT47() {
		return DAT47;
	}
	public void setDAT47(String dAT47) {
		DAT47 = dAT47;
	}
	public String getQTY47() {
		return QTY47;
	}
	public void setQTY47(String qTY47) {
		QTY47 = qTY47;
	}
	public String getDAT48() {
		return DAT48;
	}
	public void setDAT48(String dAT48) {
		DAT48 = dAT48;
	}
	public String getQTY48() {
		return QTY48;
	}
	public void setQTY48(String qTY48) {
		QTY48 = qTY48;
	}
	public String getDAT49() {
		return DAT49;
	}
	public void setDAT49(String dAT49) {
		DAT49 = dAT49;
	}
	public String getQTY49() {
		return QTY49;
	}
	public void setQTY49(String qTY49) {
		QTY49 = qTY49;
	}
	public String getDAT50() {
		return DAT50;
	}
	public void setDAT50(String dAT50) {
		DAT50 = dAT50;
	}
	public String getQTY50() {
		return QTY50;
	}
	public void setQTY50(String qTY50) {
		QTY50 = qTY50;
	}
	public String getDAT51() {
		return DAT51;
	}
	public void setDAT51(String dAT51) {
		DAT51 = dAT51;
	}
	public String getQTY51() {
		return QTY51;
	}
	public void setQTY51(String qTY51) {
		QTY51 = qTY51;
	}
	public String getDAT52() {
		return DAT52;
	}
	public void setDAT52(String dAT52) {
		DAT52 = dAT52;
	}
	public String getQTY52() {
		return QTY52;
	}
	public void setQTY52(String qTY52) {
		QTY52 = qTY52;
	}
	public String getDAT53() {
		return DAT53;
	}
	public void setDAT53(String dAT53) {
		DAT53 = dAT53;
	}
	public String getQTY53() {
		return QTY53;
	}
	public void setQTY53(String qTY53) {
		QTY53 = qTY53;
	}
	public String getDAT54() {
		return DAT54;
	}
	public void setDAT54(String dAT54) {
		DAT54 = dAT54;
	}
	public String getQTY54() {
		return QTY54;
	}
	public void setQTY54(String qTY54) {
		QTY54 = qTY54;
	}
	public String getDAT55() {
		return DAT55;
	}
	public void setDAT55(String dAT55) {
		DAT55 = dAT55;
	}
	public String getQTY55() {
		return QTY55;
	}
	public void setQTY55(String qTY55) {
		QTY55 = qTY55;
	}
	public String getDAT56() {
		return DAT56;
	}
	public void setDAT56(String dAT56) {
		DAT56 = dAT56;
	}
	public String getQTY56() {
		return QTY56;
	}
	public void setQTY56(String qTY56) {
		QTY56 = qTY56;
	}
	public String getDAT57() {
		return DAT57;
	}
	public void setDAT57(String dAT57) {
		DAT57 = dAT57;
	}
	public String getQTY57() {
		return QTY57;
	}
	public void setQTY57(String qTY57) {
		QTY57 = qTY57;
	}
	public String getDAT58() {
		return DAT58;
	}
	public void setDAT58(String dAT58) {
		DAT58 = dAT58;
	}
	public String getQTY58() {
		return QTY58;
	}
	public void setQTY58(String qTY58) {
		QTY58 = qTY58;
	}
	public String getDAT59() {
		return DAT59;
	}
	public void setDAT59(String dAT59) {
		DAT59 = dAT59;
	}
	public String getQTY59() {
		return QTY59;
	}
	public void setQTY59(String qTY59) {
		QTY59 = qTY59;
	}
	public String getDAT60() {
		return DAT60;
	}
	public void setDAT60(String dAT60) {
		DAT60 = dAT60;
	}
	public String getQTY60() {
		return QTY60;
	}
	public void setQTY60(String qTY60) {
		QTY60 = qTY60;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	@Column(name="GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
}
