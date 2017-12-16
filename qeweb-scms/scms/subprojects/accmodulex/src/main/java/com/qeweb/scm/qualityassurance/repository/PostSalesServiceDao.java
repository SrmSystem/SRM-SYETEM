package com.qeweb.scm.qualityassurance.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.qualityassurance.entity.PostSalesServiceEntity;

public interface PostSalesServiceDao extends BaseRepository<PostSalesServiceEntity, Serializable>,JpaSpecificationExecutor<PostSalesServiceEntity> {

	@Query("from PostSalesServiceEntity a where a.month = ?1")
	List<PostSalesServiceEntity> findByMonth(String time);

	@Query(value="select t.total_counts from qeweb_qm_postsalesservice t where t.vendor_id=?1 and "
			+ "t.END_TIME >= TO_TIMESTAMP(?2,'YYYY-MM-DD HH24:MI:SS.FF') and "
			+ "t.END_TIME < TO_TIMESTAMP(?3,'YYYY-MM-DD HH24:MI:SS.FF') and t.material_id=?4",nativeQuery=true)
	List<?> getMonthAmount(Long vendorId, String beginTime, String endTime, Long materialId);

}
