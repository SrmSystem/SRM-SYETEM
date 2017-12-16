package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.StatusDictEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 状态字典Dao
 * @author lw
 * 创建时间：2015年6月8日15:48:56
 * 最后更新时间：2015年6月24日14:12:54
 * 最后更新人：lw
 */
public interface StatusDictDao extends BaseRepository<StatusDictEntity, Serializable>,JpaSpecificationExecutor<StatusDictEntity>{

	@Override
	public Page<StatusDictEntity> findAll(Pageable page);
	
	@Override
	public List<StatusDictEntity> findAll();

	public List<StatusDictEntity> findByStatusType(String statusType);

}
