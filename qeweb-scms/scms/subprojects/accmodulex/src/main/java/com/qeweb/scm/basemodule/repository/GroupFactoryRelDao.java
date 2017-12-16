package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.GroupFactoryRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface GroupFactoryRelDao extends BaseRepository<GroupFactoryRelEntity, Serializable>,JpaSpecificationExecutor<GroupFactoryRelEntity>{

	@Override
	public List<GroupFactoryRelEntity> findAll();
	
	//暂时留着后期删掉
	@Query("select a from GroupFactoryRelEntity a where a.group.code=?1 and a.factory.code=?2")
	public GroupFactoryRelEntity findByGroupCodeFactoryCode(String groupCode,String factoryCode);
	
	/**
     * 通过采购组和工厂的id获取数据
     */
	 GroupFactoryRelEntity getGroupFactoryRelByGroupIdAndFactoryIdAndAbolished(Long groupId,Long factoryId,Integer abolished);
	 
	@Modifying  
	@Query("update GroupFactoryRelEntity set abolished=1 where id=?1")
	public void abolishGroupFactory(long id);
	
	
	/**
     * 通过工厂的id获取采购组和工厂的关系
     */
	List<GroupFactoryRelEntity> findByFactoryIdAndAbolished(Long factoryId,Integer abolished);
	 
	 
		/**
	     * 通过采购组id 获取采购组和工厂的关系
	     */
	List<GroupFactoryRelEntity> findByGroupIdAndAbolished(Long groupId,Integer abolished);
		 
	
}
