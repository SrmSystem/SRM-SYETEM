package com.qeweb.scm.baseline.common.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.qeweb.scm.basemodule.entity.BaseEntity;

/**
 * 节假日
 *
 */
@Entity
@Table(name = "QEWEB_HOLIDAYS")
public class HolidaysEntity extends BaseEntity {
	private String name;//名称
	
	private Integer year;
	
	private Integer month;
	
	private Integer day;
	
	Timestamp dayTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	@JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8:00")
	public Timestamp getDayTime() {
		return dayTime;
	}

	public void setDayTime(Timestamp dayTime) {
		this.dayTime = dayTime;
	}
	
	
}
