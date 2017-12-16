package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.DictEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface DictDao extends BaseRepository<DictEntity, Serializable>,JpaSpecificationExecutor<DictEntity>{
	
	 @Query("from DictEntity where dictCode=?1 and abolished=?2 order by id")
	DictEntity findByDictCodeAndAbolished(String dictCode,Integer abolished);
    
    DictEntity findById(Long id);
    
    @Query("from DictEntity where dictCode=?1 and abolished=?2 and id not in(?3)")
    DictEntity getDictByCodeAndAbolishedAndNotId(String dictCode,Integer abolished,Long id);
}
