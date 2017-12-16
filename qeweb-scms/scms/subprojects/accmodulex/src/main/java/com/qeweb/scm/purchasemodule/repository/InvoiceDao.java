package com.qeweb.scm.purchasemodule.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.qeweb.scm.basemodule.jpa.BaseRepository;
import com.qeweb.scm.purchasemodule.entity.InvoiceEntity;


public interface InvoiceDao extends BaseRepository<InvoiceEntity, Serializable>,JpaSpecificationExecutor<InvoiceEntity>{

	@Override
	public List<InvoiceEntity> findAll();
	
	@Override
	public Page<InvoiceEntity> findAll(Pageable page);

	@Query(value="select * FROM (select t.material_id as b,sum(t.req_month_qty) as rr,sum(t.stock_qty) as r from qeweb_wms_inventory t group by t.material_id) c LEFT JOIN (select t.material_id as a,sum(t.stock_qty) as rrr from qeweb_inventory t group by material_id) x ON c.b=x.a",nativeQuery=true)
	public List<Object[]> getInDeContrast();

	@Query("from InvoiceEntity a where a.billList.billCode = ?1")
	public List<InvoiceEntity> getInvoiceByBillCode(String billCode);
	
	@Query("from InvoiceEntity a where a.invoiceCode = ?1")
	public InvoiceEntity getInvoiceByCode(String invoiceCode);
}
