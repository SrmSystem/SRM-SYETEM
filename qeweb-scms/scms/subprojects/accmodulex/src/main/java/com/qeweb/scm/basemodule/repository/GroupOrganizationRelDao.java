package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.GroupOrganizationRelEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface GroupOrganizationRelDao extends BaseRepository<GroupOrganizationRelEntity, Serializable>,JpaSpecificationExecutor<GroupOrganizationRelEntity>{

	@Override
	public List<GroupOrganizationRelEntity> findAll();
	
	/**
     * 通过采购组和采购组织的id获取数据
     */
	GroupOrganizationRelEntity getGroupOrganizationRelByGroupIdAndOrgIdAndAbolished(Long groupId,Long orgId,Integer abolished);
	
	@Modifying  
	@Query("update GroupOrganizationRelEntity set abolished=1 where id=?1")
	public void abolishGroupOrg(long id);
	
	/**
     * 通过采购组的id获取采购组额采购组织的关系
     */
	List<GroupOrganizationRelEntity> findByGroupIdAndAbolished(Long groupId,Integer abolished);
	
	/**
     * 通过采购组织id获取采购组额采购组织的关系
     */
	List<GroupOrganizationRelEntity> findByOrgIdAndAbolished(Long orgId,Integer abolished);
	
	/**
     * 获取采购组
     */
	List<GroupOrganizationRelEntity> findByAbolished(Integer abolished);
	
	
}
