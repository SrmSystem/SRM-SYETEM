package com.qeweb.scm.epmodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.epmodule.entity.EpSubQuoEntity;

/**
 * 分项报价实体类DAO
 * @author ronnie
 *
 */
public interface EpSubQuoDao extends BaseRepository<EpSubQuoEntity, Serializable>,JpaSpecificationExecutor<EpSubQuoEntity>{

/*	@Query("from EpSubQuoEntity a where a.wholeQuo.id = ?1 And a.moduleItemId =?2")
	EpSubQuoEntity findByWholeQuoAndModuleItem(Long wholeQuoId,Long moduleItemId);*/
	
	@Query("from EpSubQuoEntity a where a.wholeQuo.id = ?1 ")
	List<EpSubQuoEntity> findByWholeQuoId(Long wholeQuoId);
}
