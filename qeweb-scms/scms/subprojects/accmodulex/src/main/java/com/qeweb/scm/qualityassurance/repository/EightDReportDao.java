package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.EightDReport;


public interface EightDReportDao extends BaseRepository<EightDReport, Serializable>,JpaSpecificationExecutor<EightDReport>{
	
	@Query("from EightDReport a where a.rectification.id = ?1")
	public List<EightDReport> getReportsByEightId(long id);

}
