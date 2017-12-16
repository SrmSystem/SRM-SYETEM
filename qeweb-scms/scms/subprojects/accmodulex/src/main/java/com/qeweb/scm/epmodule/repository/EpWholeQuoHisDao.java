package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpWholeQuoHisEntity;

/**
 * 整项报价历史DAO
 * @author ronnie
 *
 */
public interface EpWholeQuoHisDao extends BaseRepository<EpWholeQuoHisEntity, Serializable>,JpaSpecificationExecutor<EpWholeQuoHisEntity>{

	@Query("from EpWholeQuoHisEntity a where a.wholeQuo.id =?1 and a.quoteCount =?2")
	EpWholeQuoHisEntity findByWholeQuoIdAndQuoteCount(Long wholeQuoId,Integer quoteCount);
}
