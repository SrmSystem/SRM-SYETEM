package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.qeweb.scm.basemodule.entity.FactoryInventoryRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface FactoryInventoryRelDao extends BaseRepository<FactoryInventoryRelEntity, Serializable>,JpaSpecificationExecutor<FactoryInventoryRelEntity>{

	@Override
	public List<FactoryInventoryRelEntity> findAll();
	
	FactoryInventoryRelEntity findByFactoryIdAndInventoryIdAndAbolished(Long  factoryId , Long inventoryId,Integer abolished);
	
	
	@Modifying  
	@Query("update FactoryInventoryRelEntity set abolished=1 where id=?1")
	public void abolishFactoryInventory(long id);
	
	//通过工厂的id获取工厂和库存地点关系
	List<FactoryInventoryRelEntity> findByFactoryIdAndAbolished(Long  factoryId ,Integer abolished);
	
	//通过库存地点获取工厂和库存地点关系
	List<FactoryInventoryRelEntity> findByInventoryIdAndAbolished(Long  inventoryId ,Integer abolished);
	
    
}
