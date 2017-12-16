package com.qeweb.scm.basemodule.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="qeweb_questionnaire")
public class QuestionnaireEntity  extends BaseEntity{
	
	private String title;	
	private String quesHtml;	
	private Timestamp releaseTime;		
	private String releaseerUserName;
	private Timestamp endTime;	
	private Integer status;
	private Integer lookNumber;
	private Integer answerNumber;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getQuesHtml() {
		return quesHtml;
	}
	public void setQuesHtml(String quesHtml) {
		this.quesHtml = quesHtml;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(Timestamp releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getReleaseerUserName() {
		return releaseerUserName;
	}
	public void setReleaseerUserName(String releaseerUserName) {
		this.releaseerUserName = releaseerUserName;
	}
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8:00")
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getLookNumber() {
		return lookNumber;
	}
	public void setLookNumber(Integer lookNumber) {
		this.lookNumber = lookNumber;
	}
	public Integer getAnswerNumber() {
		return answerNumber;
	}
	public void setAnswerNumber(Integer answerNumber) {
		this.answerNumber = answerNumber;
	}	
}
