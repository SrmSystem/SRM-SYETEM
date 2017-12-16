package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qeweb.scm.basemodule.entity.RoleViewEntity;
import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface RoleViewDao extends BaseRepository<RoleViewEntity, Serializable>,JpaSpecificationExecutor<RoleViewEntity>{

	@Modifying
	@Query("delete from RoleViewEntity where roleId=?")
	public void deleteByRoleId(Long roleId);

	@Query("select viewId from RoleViewEntity where roleId=? and viewType=?")
	public List<Long> findRoleMenuLIdList(Long roleId,Integer viewType);

	public List<RoleViewEntity> findByViewTypeAndRoleIdIn(int viewMenu, List<Long> roleIdList);

	@Query("select distinct t.view from RoleViewEntity t where t.viewType=:viewType and t.roleId in:roleIdList order by t.view.menuSn asc")
	public List<ViewEntity> findViewByViewTypeAndRoleIdIn(@Param("viewType") int viewType,@Param("roleIdList") List<Long> roleIdList);

	@Query("select viewId from RoleViewEntity where roleId in (?1)")
	public List<Long> findRoleMenuIds(List<Long> roleIdList);

}
