package com.qeweb.scm.basemodule.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="qeweb_subject")
public class SubjectEntity extends IdEntity{
	
	private String title;	

	private long quesId;
	
	private List<AnswerEntity> answers=new ArrayList<AnswerEntity>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getQuesId() {
		return quesId;
	}

	public void setQuesId(long quesId) {
		this.quesId = quesId;
	}

	@Transient
	public List<AnswerEntity> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerEntity> answers) {
		this.answers = answers;
	}
}
