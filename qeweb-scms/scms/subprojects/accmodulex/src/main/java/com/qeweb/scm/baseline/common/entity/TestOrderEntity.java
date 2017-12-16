package com.qeweb.scm.baseline.common.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
@Entity
@Table(name = "QEWEB_TEST_ORDER")
@Component
@Scope(value="prototype")
public class TestOrderEntity extends BaseAuditEntity{

	private String code;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
