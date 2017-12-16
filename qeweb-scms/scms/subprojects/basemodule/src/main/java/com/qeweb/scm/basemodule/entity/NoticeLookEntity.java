package com.qeweb.scm.basemodule.entity;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name="qeweb_notice_look")
public class NoticeLookEntity extends BaseEntity{

	private long noticeId;

	public long getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(long noticeId) {
		this.noticeId = noticeId;
	}
	
}
