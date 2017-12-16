package com.qeweb.scm.vendorperformancemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforPurchasedatainEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforPurchasedatainDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFormulasDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforIndexDao;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforPurchasedatainVo;

@Service
@Transactional
public class VendorPerforPurchasedatainService {
	
	@Autowired
	private VendorPerforPurchasedatainDao purchasedatainDao;
	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	@Autowired
	private OrganizationDao organizationDao;
	@Autowired
	private MaterialDao materialDao;
	@Autowired
	private BussinessRangeDao bussinessRangeDao;
	@Autowired
	private VendorPerforIndexDao indexDao;
	@Autowired
	private VendorPerforFormulasDao formulasDao;

	public Page<VendorPerforPurchasedatainEntity> getVendorPerforPurchasedatainList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforPurchasedatainEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforPurchasedatainEntity.class);
		Page<VendorPerforPurchasedatainEntity>  page= purchasedatainDao.findAll(spec,pagin);
		return page;
	}

	public boolean combine(List<VendorPerforPurchasedatainVo> list, ILogger logger, Long cycleId) throws RuntimeException, Exception {
		List<VendorPerforPurchasedatainEntity> vendorPerforDateInEntities=new ArrayList<VendorPerforPurchasedatainEntity>();
		int index=1;
		BussinessRangeEntity br=null;
		Map<String,Object> maps=new HashMap<String, Object>();
		for(VendorPerforPurchasedatainVo vo:list)
		{
			VendorPerforPurchasedatainEntity vdi = new VendorPerforPurchasedatainEntity();

			//验证供应商
			List<OrganizationEntity> orgs=organizationDao.findByCode(vo.getVendorCode());
			if(null==orgs||orgs.size()==0)
			{
				logger.log("->[FAILED]行索引[" + (index) + "],供应商代码[" + vo.getVendorCode() + "]未在系统中维护");
				return false;
			}
			else
			{
				vdi.setVendorCode(vo.getVendorCode());
				vdi.setVendorName(orgs.get(0).getName());
			}
			//验证日期
			String eL = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
			Pattern p = Pattern.compile(eL);
			String sr=vo.getVendorDate()+"-28";
			Matcher m = p.matcher(sr);
			boolean dateFlag = m.matches();
			if (!dateFlag) {
				logger.log("->[FAILED]行索引[" + (index) + "],评估年月[" + vo.getVendorDate() + "]错误格式");
				return false;
			}
			vdi.setVendorDate(vo.getVendorDate());
			//验证品牌
			br=bussinessRangeDao.findByCodeAndBussinessTypeAndAbolished(vo.getBrandCode(), 2, Constant.UNDELETE_FLAG);
			if(br==null)  
			{
				logger.log("->[FAILED]行索引[" + (index) + "],品牌名称[" + vo.getBrandName() + "]未在系统中维护");
				return false;
			}
			vdi.setBrandName(br.getName());
			vdi.setBrandId(br.getId());
			//采购额
			vdi.setDefaultPurchase(Integer.parseInt(vo.getDefaultPurchase()));
			index++;
			
			int ik=maps.size();
			String string=vdi.getVendorCode()+"-"+vdi.getBrandId()+"-"+vdi.getVendorDate();
			maps.put(string,vdi);
			if(ik==maps.size())
			{
				logger.log("-->数据忽略------..."); 
			}
			else
			{
				List<VendorPerforPurchasedatainEntity> vppcd=purchasedatainDao.findByVendorCodeAndBrandIdAndVendorDate(vdi.getVendorCode(),vdi.getBrandId(),vdi.getVendorDate());
				if(vppcd.size()==0)
				{
					vendorPerforDateInEntities.add(vdi);
				}
				else
				{
					logger.log("->[FAILED]行索引[" + (index) + "],忽略[" + vo.getBrandName() + "]，已经存在");
				}
			}
		}
		
		purchasedatainDao.save(vendorPerforDateInEntities);
		return true;
	}
}
