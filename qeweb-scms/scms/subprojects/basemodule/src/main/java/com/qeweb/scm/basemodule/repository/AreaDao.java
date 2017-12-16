package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.qeweb.scm.basemodule.entity.AreaEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 区域Dao
 * @author lw
 * 创建时间：2015年6月2日14:17:05
 * 最后更新时间：2015年6月2日14:22:54
 * 最后更新人：lw
 */
public interface AreaDao extends BaseRepository<AreaEntity, Serializable>,JpaSpecificationExecutor<AreaEntity>{

	@Override
	public Page<AreaEntity> findAll(Pageable page);
	
	public List<AreaEntity> findByLevel(Integer level);
	
	public List<AreaEntity> findByParentId(Long parentId);
	
	@Query("from AreaEntity where parentId=?1 order by id asc")
	public List<AreaEntity> getAreaEntity(Long parentId);

}
