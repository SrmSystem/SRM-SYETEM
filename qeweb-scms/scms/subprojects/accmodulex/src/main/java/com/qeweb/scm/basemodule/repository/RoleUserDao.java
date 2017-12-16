package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.RoleUserEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;


public interface RoleUserDao extends BaseRepository<RoleUserEntity, Serializable>,JpaSpecificationExecutor<RoleUserEntity>{
    
	@Query("select a.user from RoleUserEntity a where a.role.code = ?1")
	public Page<UserEntity> findAllByPage(Pageable page,String roleCode);
	
	@Modifying
	@Query("delete from RoleUserEntity where roleId=?")
	void deleteByRoleId(Long roleId);

	@Query("select t.user from RoleUserEntity t where t.roleId=?")
	List<UserEntity> findUserByRoleId(Long roleId);
	
	@Query("select t.userId from RoleUserEntity t where t.roleId=?")
	List<Long> findUserIdByRoleId(Long roleId);
	
	List<RoleUserEntity> findByRoleIdIn(List<Long> list);
	
	List<RoleUserEntity> findByRoleId(Long roleId);
	
	@Query("select t.user from RoleUserEntity t where t.role.code = ?")
	List<UserEntity> findUserByRoleCode(String roleCode);

	/**
	 * 根据用户Id查找用户的角色
	 * @param userId 用户ID
	 * @return 角色ID集合
	 */
	@Query("select t.roleId from RoleUserEntity t where t.userId=?")
	List<Long> findRoleIdByUserId(Long userId);

	RoleUserEntity findByUserIdAndRoleId(long id, long roleId);

	@Query(value="SELECT role_Id FROM qeweb_role_user WHERE user_Id=?1",nativeQuery=true)
	List<?> findByUserIds(Long userId);

	List<RoleUserEntity> findByUserId(Long userId);

//	Page<RoleUserEntity> findAll(Specification<RoleEntity> spec,
//			PageRequest pagin);

}
