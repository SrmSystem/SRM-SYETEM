package com.qeweb.scm.check.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.check.entity.CheckInvoiceEntity;


public interface CheckInvoiceDao extends BaseRepository<CheckInvoiceEntity, Serializable>,JpaSpecificationExecutor<CheckInvoiceEntity>{

	List<CheckInvoiceEntity> findByCode(String code);

}
