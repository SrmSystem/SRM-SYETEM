package com.qeweb.scm.vendormodule.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.qeweb.scm.vendormodule.entity.VendorMaterialSupplyRelEntity;

/**
 * 供货关系Dao
 * @author lw
 * 创建时间：2015年6月15日09:27:35
 * 最后更新时间：2015年6月15日09:27:40
 * 最后更新人：lw
 */
public interface VendorMaterialSupplyRelDao extends PagingAndSortingRepository<VendorMaterialSupplyRelEntity, Serializable>,JpaSpecificationExecutor<VendorMaterialSupplyRelEntity>{

	public List<VendorMaterialSupplyRelEntity> findByMaterialRelId(Long materialRelId);
	
	public List<VendorMaterialSupplyRelEntity> findByMaterialRelIdAndVendorId(Long materialRelId,Long vendorId);
	
	@Override
	public List<VendorMaterialSupplyRelEntity> findAll(Specification<VendorMaterialSupplyRelEntity> spec);
	
	@Query(value="SELECT COUNT(DISTINCT BRAND_NAME) FROM QEWEB_VENDOR_MAT_SUPPLY_REL WHERE VENDOR_ID =?1",nativeQuery=true)
	public int getBrandCountByVendor(Long vendorId);
	
	@Override
	public List<VendorMaterialSupplyRelEntity> findAll(Specification<VendorMaterialSupplyRelEntity> spec, Sort sort);
	
	@Query(value="SELECT DISTINCT BRAND_NAME,VENDOR_NAME,VENDOR_ID FROM QEWEB_VENDOR_MAT_SUPPLY_REL WHERE VENDOR_NAME LIKE ?1 AND BRAND_NAME LIKE ?2 ",nativeQuery=true)
	public List findlist(String vendor,String material);
	
	@Query(value="SELECT DISTINCT s.BRAND_NAME,s.VENDOR_NAME,s.VENDOR_ID,x.SO_ID,x.CODE FROM QEWEB_VENDOR_MAT_SUPPLY_REL s LEFT JOIN QEWEB_VENDOR_BASE_INFO x  ON  s.VENDOR_ID=x.ID WHERE s.VENDOR_NAME LIKE ?1 AND s.BRAND_NAME LIKE ?2 AND x.SO_ID in (?3)",nativeQuery=true)
	public List findlist2(String vendor,String material,List<Long> sid);
	
	@Query(value="SELECT DISTINCT s.BRAND_NAME,s.VENDOR_NAME,s.VENDOR_ID,x.CODE FROM QEWEB_VENDOR_MAT_SUPPLY_REL s JOIN QEWEB_VENDOR_BASE_INFO x  ON  s.VENDOR_ID=x.ID WHERE s.VENDOR_NAME LIKE ?1 AND s.BRAND_NAME LIKE ?2 ",nativeQuery=true)
	public List findlist3(String vendor,String material);


	@Query(value="SELECT SUM(SUPPLY_COEFFICIENT) FROM QEWEB_VENDOR_MAT_SUPPLY_REL WHERE MATERIAL_REL_ID = ?1",nativeQuery=true)
	public Double getTotalSupplyCoeFFicientByRelId(Long id);

	public List<VendorMaterialSupplyRelEntity> findByOrgId(Long orgId);

	public List<VendorMaterialSupplyRelEntity> findByVendorIdAndBrandId(Long id, long id2);

	public List<VendorMaterialSupplyRelEntity> findByVendorIdAndBrandIdAndFactoryId(long id, Long bid, long id2);

	@Query(value="SELECT distinct BRAND_ID from  QEWEB_VENDOR_MAT_SUPPLY_REL where ORG_ID=?1",nativeQuery=true)
	public List getOrgIdAndBrandId(Long id);

	public List<VendorMaterialSupplyRelEntity> findByOrgIdAndBrandId(Long vid,
			Long bid, Long fid);

	@Query(value="SELECT distinct FACTORY_ID from  QEWEB_VENDOR_MAT_SUPPLY_REL where ORG_ID=?1 and BRAND_ID=?2",nativeQuery=true)
	public List getOrgIdAndBrandIdAndFactoryId(Long vid, Long bid);
	
	@Query(value="SELECT t.id from (SELECT * from QEWEB_MATERIAL WHERE id IN(SELECT distinct MATERIAL_ID  from  QEWEB_VENDOR_MAT_SUPPLY_REL where ORG_ID=?1 and BRAND_ID=?2 and FACTORY_ID=?3) and ABOLISHED=0)m LEFT JOIN (SELECT * from QEWEB_MATERIAL_TYPE where name not in(?4))t on m.MATERIAL_TYPE_ID=t.ID",nativeQuery=true)
	public List getOrgIdAndBrandIdAndFactoryId(Long vid, Long bid,Long fid, String property);
	
	@Query(value="SELECT t.id from (SELECT * from QEWEB_MATERIAL WHERE id IN(SELECT distinct MATERIAL_ID  from  QEWEB_VENDOR_MAT_SUPPLY_REL where ORG_ID=?1 and BRAND_ID=?2 and FACTORY_ID=?3) and ABOLISHED=0)m LEFT JOIN QEWEB_MATERIAL_TYPE t on m.MATERIAL_TYPE_ID=t.ID",nativeQuery=true)
	public List getOrgIdAndBrandIdAndFactoryId(Long vid, Long bid,Long fid);
	
	@Query(value="select distinct ORG_ID from QEWEB_VENDOR_MAT_SUPPLY_REL where BRAND_ID in (?1) and FACTORY_ID in(?2)",nativeQuery=true)
	public List<?> findByBrandIdAndFactoryId(Set<Long> brandId, Set<Long> factoryId);
}
