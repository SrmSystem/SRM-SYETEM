package com.qeweb.scm.vendorperformancemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresTotalHisEntity;

/**
 * 调分历史
 * @author ALEX
 *
 */
public interface VendorPerforScoresTotalHisDao extends BaseRepository<VendorPerforScoresTotalHisEntity, Serializable>,JpaSpecificationExecutor<VendorPerforScoresTotalHisEntity>{

	@Modifying
	@Query("delete VendorPerforScoresTotalHisEntity where scoresId=?1")
	public void deleteByOrgx(Long scoresId);

}
