package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.ViewEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface ViewDao extends BaseRepository<ViewEntity, Serializable>,JpaSpecificationExecutor<ViewEntity>{

	public List<ViewEntity> findByViewType(int viewMenu);

	public List<ViewEntity> findByParentIdAndViewType(Long parentId, int viewType);
	
	public List<ViewEntity> findByParentIdAndViewTypeOrderByMenuSnAsc(Long parentId, int viewType);

	public ViewEntity findByParentIdAndViewTypeAndMenuSn(Long parentId, int viewMenu, int menuSn);

	public List<ViewEntity> findByParentId(Long id);

	public List<ViewEntity> findByViewTypeOrderByMenuSnAsc(int viewMenu);

	@Query("from ViewEntity where id in (?1) and viewType = ?2 ORDER BY menuSn asc")
	public List<ViewEntity> findByIdsAndType(List<Long> ids , int viewType); 
}
