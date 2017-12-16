package com.qeweb.scm.purchasemodule.service;

import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.purchasemodule.entity.InvoiceEntity;
import com.qeweb.scm.purchasemodule.repository.InvoiceDao;


@Service
@Transactional
public class InvoiceService {

	@Autowired
	private InvoiceDao invoiceDao;
	
	
	/**
	 * 获取发票列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public  Page<InvoiceEntity> getInvoices(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InvoiceEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InvoiceEntity.class);
		return invoiceDao.findAll(spec, pagin);
	}


	public InvoiceEntity getInvoiceByCode(String invoiceCode) {
		return invoiceDao.getInvoiceByCode(invoiceCode);
	}

	public void saveErpInvoices(List<InvoiceEntity> invoiceList) {
		invoiceDao.save(invoiceList);
	}
	
	public void saveInvoice(InvoiceEntity invoice) {
		invoiceDao.save(invoice);
	}
	
	

}
