package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 用户查询接口（JPA实现）
 * @author pjjxiajun
 * @date 2015年3月10日
 * @path com.qeweb.scm.basemodule.repository.com.qeweb.scm.basemodule.repository
 */
public interface UserDao extends BaseRepository<UserEntity, Serializable>,JpaSpecificationExecutor<UserEntity>{
	
	@Override
	public List<UserEntity> findAll();
	
	@Query("select u.id from UserEntity u where u.enabledStatus = 1")
	public List<Long> findEnableUserIds();
	
	@Override
	public Page<UserEntity> findAll(Pageable page);

	public UserEntity findByLoginName(String loginName);
	
	@Query("from UserEntity where company.id=?")
	public List<UserEntity> findByCompany(long id);

	@Modifying  
	@Query("update UserEntity set name=:name,mobile=:mobile,email=:email where loginName=:loginName")
    public void updateUserEntity(@Param("loginName") String loginName,@Param("name") String name,@Param("mobile") String mobile,@Param("email") String email);

	@Modifying  
	@Query("update UserEntity set password=:password,salt=:salt where loginName=:loginName")
	public void updatePassword(@Param("loginName") String loginName,@Param("password")  String password,@Param("salt")  String salt);
	
	@Query("select u.id from UserEntity u where u.loginName in (:codes)") 
	List<Long> findUserIdByCode(@Param("codes") String[] codes);  
	
	@Query("from UserEntity u where u.loginName in (:codes)") 
	List<UserEntity> findUserByCode(@Param("codes") Set<String> codes);  
	
	@Query("select u.id from UserEntity u where u.department.id in (:orgids) or u.company.id in (:orgids)") 
	List<Long> findUserIdByOrg(@Param("orgids") Long[] orgids);

	public UserEntity findByEmail(String orgEmail);
	
	@Query(value = " select * from qeweb_user where company_id in ( select id from qeweb_organization o where o.role_type =1 and o.id > 0 )",nativeQuery=true)
	public List<UserEntity> findByCompanyOrg();
	
	public List<UserEntity> findByName(String name);
}
