package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.DictItemEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

public interface DictItemDao extends BaseRepository<DictItemEntity, Serializable>,JpaSpecificationExecutor<DictItemEntity>{

	List<DictItemEntity> findByDictType(String type);
	
	
	@Query("select a from DictItemEntity a where a.dict.dictCode=?1 and a.code=?2 and a.abolished=0 and a.dict.abolished=0")
	DictItemEntity findByDictCodeAndCode(String dictCode,String code);
	
	@Query("select a from DictItemEntity a where a.dict.dictCode=?1  and a.abolished=0 and a.dict.abolished=0 order by id")
	List<DictItemEntity> findByDictCode(String dictCode);
	
	@Query("select a from DictItemEntity a where a.dict.id=?1  and a.abolished=0 and a.dict.abolished=0 order by id")
	List<DictItemEntity> findDictItemListByDictId(Long dictId);
	
    //通过子表的code获取对象
	@Query("select a from DictItemEntity a where a.code=?1  and a.abolished=0")
	DictItemEntity findDictItemByCode(String code);
	
	DictItemEntity findById(Long id);
	
	
	

	@Query("select a from DictItemEntity a where a.dict.id=?1 and a.code=?2  and a.abolished=?3 and a.id not in(?4)")
	DictItemEntity getDictItemByCodeAndAbolishedAndNotId(Long dictId,
			String code, Integer abolished, long id);

	@Query("select a from DictItemEntity a where a.dict.id=?1 and a.code=?2  and a.abolished=?3")
	DictItemEntity getDictItemByCodeAndAbolished(Long dictId, String code,
			Integer abolished);
	
	@Modifying  
	@Query("update DictItemEntity  a  set  a.abolished = 0  where a.id=?1")
	void effect(Long id);
	
	
	@Query("select a from DictItemEntity a where a.dict.dictCode=?1 and a.code=?2")
	DictItemEntity findByDictItemDictCodeAndCode(String dictCode,String code);
	
	@Query("select a.code from DictItemEntity a where a.dict.dictCode=?1 and a.abolished=0 and a.dict.abolished=0")
	List<String> findListByDictCode(String dictCode);
	
	
}
