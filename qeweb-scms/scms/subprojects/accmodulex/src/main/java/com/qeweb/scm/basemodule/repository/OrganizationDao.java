package com.qeweb.scm.basemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.jpa.BaseRepository;

/**
 * 组织的持久化操作（JPA接口）
 * @author pjjxiajun
 * @date 2015年3月16日
 * @path com.qeweb.scm.basemodule.repository.OrgnaizationDao.java
 */
public interface OrganizationDao extends BaseRepository<OrganizationEntity, Serializable>,JpaSpecificationExecutor<OrganizationEntity>{
	
	List<OrganizationEntity> findByParentId(Long pId);

	List<OrganizationEntity> findByParentIdIsNull();

	List<OrganizationEntity> findByParentIdIsNullAndOrgType(Integer orgType);

	List<OrganizationEntity> findByParentIdAndOrgType(Long pId, Integer orgType);
	
	List<OrganizationEntity> findByIdOrRoleType(Long id, Integer roleType);

	List<OrganizationEntity> findByCode(String code);
	
	OrganizationEntity findByCodeAndAbolished(String code,Integer i);
	
	/**
	 * 获取有效的采购组织
	 * @author eleven
	 * 20170518
	 * @param abolished
	 * @return
	 */
	List<OrganizationEntity> findByAbolished(Integer i);
	
	//采购商-导入采购计划
	List<OrganizationEntity> findByCodeAndAbolishedAndRoleTypeAndEnableStatus(String code,Integer i,Integer roleType,Integer enableStatus);
	
	//供应商-导入采购计划
	@Query("select distinct org from OrganizationEntity org,BuyerVendorRelEntity rel where org.id=rel.vendorId and org.code=?1 and org.abolished=0 and rel.abolished=0 and org.roleType= ?2 and org.enableStatus=?3 and org.activeStatus=?4 and rel.buyerId=?5")
	List<OrganizationEntity> findByCodeAndAbolishedAndRoleTypeAndEnableStatusAndActiveStatusAndBuyerId(String code,Integer roleType,Integer enableStatus,Integer activeStatus,Long buyerId);
	
	OrganizationEntity findByCodeAndAbolishedAndCol10(String code,Integer i,String col10);//

	OrganizationEntity findByName(String orgName);

	OrganizationEntity findByEmail(String orgEmail);

	List<OrganizationEntity> findByParentIdAndAbolished(Long pId, Integer i);

	List<OrganizationEntity> findByParentIdIsNullAndOrgTypeAndAbolished(
			Integer orgType, Integer i);

	List<OrganizationEntity> findByParentIdAndOrgTypeAndAbolished(Long pId,
			Integer orgType, Integer i);

	List<OrganizationEntity> findByParentIdIsNullAndAbolished(Long pId, int i);

	List<OrganizationEntity> findByParentIdAndRoleTypeAndAbolished(Long pId,
			Integer roleType, int i);

	List<OrganizationEntity> findByParentIdIsNullAndRoleTypeAndAbolished(
			Integer roleType, int i);

	@Query("from OrganizationEntity where code=?1 and (abolished is null or abolished=0)")
	OrganizationEntity findValidByCode(String vendorCode);

	List<OrganizationEntity> findByRoleTypeAndAbolished(Integer orgRoleType, int abolished);

	List<OrganizationEntity> findByOrgTypeAndAbolished(Integer orgTypeDeparment, int abolished);

//	@Override
//	public List<OrganizationEntity> findAll();
//	
//	@Override
//	public Page<OrganizationEntity> findAll(Pageable page);
	
	@Query("from OrganizationEntity where upper(code)=?1")
	List<OrganizationEntity> findByUpperCode(String code);

	List<OrganizationEntity> findByRoleType(Integer roleType);
	
	@Query("from OrganizationEntity where upper(code)=?1 and abolished=?2")
	List<OrganizationEntity> findByUpperCodeAndAbolished(String code, int abolished);

	@Query(value="select * from QEWEB_ORGANIZATION t where t.code in"
			+ "( select u.login_name from qeweb_user u where u.id in "
			+ "	(select ru.user_id from qeweb_role_user ru where ru.role_id in "
			+ "		( select r.id from qeweb_role r where r.code='dsf')))",nativeQuery=true)
	List<OrganizationEntity> findDsfSenders();
	
	List<OrganizationEntity> findByOrgTypeAndRoleTypeAndAbolished(Integer orgType,int roleType ,int abolished);

	@Query(value="select * from QEWEB_ORGANIZATION t where t.org_type=0 and t.id in ( select tem.org_id from QEWEB_VENDOR_NAV_TEMPLATE tem where tem.default_flag = 1 and tem.abolished = 0)",nativeQuery=true)
	List<OrganizationEntity> getBuyerListByRoleType1();
	
	@Query("from OrganizationEntity a where a.code = ?1 and a.abolished = ?2 ")
	List<OrganizationEntity> findByCodeAndAbolisheds(String code,Integer abolished);
	
	@Query("from OrganizationEntity a where a.roleType = ?1 and a.abolished = ?2 ")
	List<OrganizationEntity> findByRoleTypeAndAbolished1(Integer roleType, int abolished);

	@Query("from OrganizationEntity a where a.code = ?1")
	OrganizationEntity getBuyerOrVendor(String buyerCode);
}
