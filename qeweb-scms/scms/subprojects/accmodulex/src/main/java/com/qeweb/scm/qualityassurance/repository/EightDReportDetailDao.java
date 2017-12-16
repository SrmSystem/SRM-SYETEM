package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.EightDReportDetail;


public interface EightDReportDetailDao extends BaseRepository<EightDReportDetail, Serializable>,JpaSpecificationExecutor<EightDReportDetail>{

	@Query("from EightDReportDetail a where a.rectification.id = ?1")
	List<EightDReportDetail> getDetailsByEightId(Long id);

	@Query("from EightDReportDetail a where a.rectification.id = ?1 and a.reportType = ?2")
	List<EightDReportDetail> getDetailsByEightIdAndType(Long id,int type);

}
