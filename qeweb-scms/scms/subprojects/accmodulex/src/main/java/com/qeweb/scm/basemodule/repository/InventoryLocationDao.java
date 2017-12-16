package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.qeweb.scm.basemodule.entity.InventoryLocationEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface InventoryLocationDao extends BaseRepository<InventoryLocationEntity, Serializable>,JpaSpecificationExecutor<InventoryLocationEntity>{

	 /**
     * 根据库位地点代码查询库位地点信息
     */
	InventoryLocationEntity findByCode(String code);
	
	@Override
	public List<InventoryLocationEntity> findAll();
	
	public List<InventoryLocationEntity> findByAbolished(Integer abolished);
}
