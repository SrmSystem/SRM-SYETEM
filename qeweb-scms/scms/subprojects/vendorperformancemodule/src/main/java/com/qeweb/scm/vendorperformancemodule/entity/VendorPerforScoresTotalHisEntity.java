package com.qeweb.scm.vendorperformancemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 *	评估结果调分历史
 */
@Entity
@Table(name = "qeweb_assess_scores_total_his")
public class VendorPerforScoresTotalHisEntity extends BaseEntity {

	private Long scoresId; 	// 评估列表ID
	private Long orgId;		// 供应商ID
	private Long brandId;	// 品牌

	/** 被维度得分映射 */
	private Double score1;
	private Double score2;
	private Double score3;
	private Double score4;
	private Double score5;
	private Double score6;
	private Double score7;
	private Double score8;
	private Double score9;
	private Double score10;
	private Double score11;
	private Double score12;
	private Double score13;
	private Double score14;
	private Double score15;
	private Double score16;
	private Double score17;
	private Double score18;
	private Double score19;
	private Double score20;
	// 映射子绩效模版
	private Double totals1;
	private Double totals2;
	private Double totals3;
	private Double totals4;
	private Double totals5;
	
	private String adjustReason;		//调分原因

	public Long getScoresId() {
		return scoresId;
	}

	public void setScoresId(Long scoresId) {
		this.scoresId = scoresId;
	}

	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getBrandId() {
		return brandId;
	}

	public void setBrandId(Long brandId) {
		this.brandId = brandId;
	}

	public Double getScore1() {
		return score1;
	}

	public void setScore1(Double score1) {
		this.score1 = score1;
	}

	public Double getScore2() {
		return score2;
	}

	public void setScore2(Double score2) {
		this.score2 = score2;
	}

	public Double getScore3() {
		return score3;
	}

	public void setScore3(Double score3) {
		this.score3 = score3;
	}

	public Double getScore4() {
		return score4;
	}

	public void setScore4(Double score4) {
		this.score4 = score4;
	}

	public Double getScore5() {
		return score5;
	}

	public void setScore5(Double score5) {
		this.score5 = score5;
	}

	public Double getScore6() {
		return score6;
	}

	public void setScore6(Double score6) {
		this.score6 = score6;
	}

	public Double getScore7() {
		return score7;
	}

	public void setScore7(Double score7) {
		this.score7 = score7;
	}

	public Double getScore8() {
		return score8;
	}

	public void setScore8(Double score8) {
		this.score8 = score8;
	}

	public Double getScore9() {
		return score9;
	}

	public void setScore9(Double score9) {
		this.score9 = score9;
	}

	public Double getScore10() {
		return score10;
	}

	public void setScore10(Double score10) {
		this.score10 = score10;
	}

	public Double getScore11() {
		return score11;
	}

	public void setScore11(Double score11) {
		this.score11 = score11;
	}

	public Double getScore12() {
		return score12;
	}

	public void setScore12(Double score12) {
		this.score12 = score12;
	}

	public Double getScore13() {
		return score13;
	}

	public void setScore13(Double score13) {
		this.score13 = score13;
	}

	public Double getScore14() {
		return score14;
	}

	public void setScore14(Double score14) {
		this.score14 = score14;
	}

	public Double getScore15() {
		return score15;
	}

	public void setScore15(Double score15) {
		this.score15 = score15;
	}

	public Double getScore16() {
		return score16;
	}

	public void setScore16(Double score16) {
		this.score16 = score16;
	}

	public Double getScore17() {
		return score17;
	}

	public void setScore17(Double score17) {
		this.score17 = score17;
	}

	public Double getScore18() {
		return score18;
	}

	public void setScore18(Double score18) {
		this.score18 = score18;
	}

	public Double getScore19() {
		return score19;
	}

	public void setScore19(Double score19) {
		this.score19 = score19;
	}

	public Double getScore20() {
		return score20;
	}

	public void setScore20(Double score20) {
		this.score20 = score20;
	}

	public Double getTotals1() {
		return totals1;
	}

	public void setTotals1(Double totals1) {
		this.totals1 = totals1;
	}

	public Double getTotals2() {
		return totals2;
	}

	public void setTotals2(Double totals2) {
		this.totals2 = totals2;
	}

	public Double getTotals3() {
		return totals3;
	}

	public void setTotals3(Double totals3) {
		this.totals3 = totals3;
	}

	public Double getTotals4() {
		return totals4;
	}

	public void setTotals4(Double totals4) {
		this.totals4 = totals4;
	}

	public Double getTotals5() {
		return totals5;
	}

	public void setTotals5(Double totals5) {
		this.totals5 = totals5;
	}

	public String getAdjustReason() {
		return adjustReason;
	}

	public void setAdjustReason(String adjustReason) {
		this.adjustReason = adjustReason;
	}

}
