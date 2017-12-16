package com.qeweb.scm.basemodule.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="qeweb_answer")
public class AnswerEntity extends IdEntity {

	private String title;
	
	private Integer type;
	
	private Long subjectId;

	private Integer choiceNumber;
	
	private List<UserAnswerEntity> answerEntitys;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public Integer getChoiceNumber() {
		return choiceNumber;
	}

	public void setChoiceNumber(Integer choiceNumber) {
		this.choiceNumber = choiceNumber;
	}
	
	@Transient
	public List<UserAnswerEntity> getAnswerEntitys() {
		return answerEntitys;
	}

	public void setAnswerEntitys(List<UserAnswerEntity> answerEntitys) {
		this.answerEntitys = answerEntitys;
	}
}