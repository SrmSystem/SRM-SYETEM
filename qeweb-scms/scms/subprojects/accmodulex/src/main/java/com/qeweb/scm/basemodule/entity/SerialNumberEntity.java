package com.qeweb.scm.basemodule.entity;

import java.text.ParseException;
import java.util.GregorianCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.time.DateUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.qeweb.scm.basemodule.utils.StringUtils;

/**
 * 流水号
 * @author ALEX
 *
 */
@Entity
@Table(name="qeweb_serial_number")
public class SerialNumberEntity extends BaseEntity {
	
	@JsonIgnore
	public final static String YEAR = "year";
	@JsonIgnore
	public final static String MONTH = "month";
	@JsonIgnore
	public final static String DAY = "day";
	/**
	 * 唯一标实
	 */
	private String key;
	/**
	 * 表示流水号中的前缀
	 */
	private String prefix; 
	/**
	 * 表示日期的格式如'yyyyMMdd'
	 */
	private String dataString;
	/**
	 * 开始第一个数是多少,定义长度和其实数值,如000000
	 */
	private String startNumber;
	/**
	 * 表示流水号中的日期，如20150512
	 */
	private String dateTimeString;
	/**
	 * 可以设置循环类型,如每日生成新的流水号从0开始,默认为日,循环 数值为year,month,day
	 */
	private String repeatCycle = DAY;
	/**
	 * 是否每次都从数据库验证
	 */
	private Boolean isVerify = false;
	/**
	 * 备注
	 */
	private String remark;
	
	@Column(name="serial_key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@Column(name="prefix")
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Column(name="data_string")
	public String getDataString() {
		return dataString;
	}

	public void setDataString(String dataString) {
		this.dataString = dataString;
	}

	@Column(name="start_number")
	public String getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(String startNumber) {
		this.startNumber = startNumber;
	}

	@Column(name="date_time_string")
	public String getDateTimeString() {
		return dateTimeString;
	}

	public void setDateTimeString(String dateTimeString) {
		this.dateTimeString = dateTimeString;
	}

	@Column(name="repeat_cycle")
	public String getRepeatCycle() {
		return repeatCycle;
	}

	public void setRepeatCycle(String repeatCycle) {
		this.repeatCycle = repeatCycle;
	}

	@Column(name="is_verify")
	public Boolean getIsVerify() {
		return isVerify;
	}

	public void setIsVerify(Boolean isVerify) {
		this.isVerify = isVerify;
	}
	
	@Column(name="remark") 
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 把dateTimeString转换正GregorianCalendar
	 * 
	 * @return DateTime
	 */
	@Transient
	@JsonIgnore
	public GregorianCalendar getDateTime() {
		if (StringUtils.isEmpty(dateTimeString))
			return new GregorianCalendar();
		GregorianCalendar date = new GregorianCalendar();
		try {
			date.setTime(DateUtils.parseDate(this.getDateTimeString(), new String[] { this.getDataString() }));
		} catch (ParseException e) {
			return new GregorianCalendar();
		}
		return date;
	}
}
