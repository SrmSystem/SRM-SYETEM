	package com.qeweb.scm.vendorperformancemodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springside.modules.persistence.DynamicSpecifications;
import org.springside.modules.persistence.SearchFilter;

import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforPurchaseEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCycleDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforPurchaseDao;

@Service
@Transactional
public class VendorPerforCycleService {
	
	@Autowired
	private VendorPerforCycleDao cycleDao;
	
	@Autowired
	private MaterialTypeDao materialTypeDao;
	
	@Autowired
	private VendorPerforPurchaseDao purchaseDao;

	public Page<VendorPerforCycleEntity> getCycleList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilter> filters = SearchFilter.parse(searchParamMap);
		Specification<VendorPerforCycleEntity> spec = DynamicSpecifications.bySearchFilter(filters.values(), VendorPerforCycleEntity.class);
		Page<VendorPerforCycleEntity> cycleEntityPage = cycleDao.findAll(spec,pagin);
		return cycleEntityPage;
	}

	public Page<MaterialTypeEntity> getMaterialtype(int pageNumber, int pageSize, Map<String, Object> searchParamMap,String ids) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		ArrayList<Long> newArrayList=new ArrayList<Long>();
		if(ids!=null&&!ids.equals(""))
		{
			ids=ids.substring(1);
			for(String id:ids.split(","))
			{
				newArrayList.add(Long.parseLong(id));
			}
		}
		else
		{
			newArrayList.add(Long.parseLong("0"));
		}
		List<MaterialTypeEntity> materialTypeEntity = new ArrayList<MaterialTypeEntity>();
		
		if((searchParamMap.get("LIKE_code")!=null&&!(searchParamMap.get("LIKE_code").equals("")))&&(searchParamMap.get("LIKE_name")==null||searchParamMap.get("LIKE_name").equals("")))
			materialTypeEntity = materialTypeDao.findByLevelLayerAndAbolishedAndIdNotInAndCodeLike(1,0,newArrayList,"%"+searchParamMap.get("LIKE_code")+"%");
		else if((searchParamMap.get("LIKE_name")!=null&&!(searchParamMap.get("LIKE_name").equals("")))&&(searchParamMap.get("LIKE_code")==null||searchParamMap.get("LIKE_code").equals("")))
			materialTypeEntity = materialTypeDao.findByLevelLayerAndAbolishedAndIdNotInAndNameLike(1,0,newArrayList,"%"+searchParamMap.get("LIKE_name")+"%");
		else if((searchParamMap.get("LIKE_name")!=null&&!(searchParamMap.get("LIKE_name").equals("")))&&(searchParamMap.get("LIKE_code")!=null&&!(searchParamMap.get("LIKE_code").equals(""))))
			materialTypeEntity = materialTypeDao.findByLevelLayerAndAbolishedAndIdNotInAndCodeLikeAndNameLike(1,0,newArrayList,"%"+(String)(searchParamMap.get("LIKE_code"))+"%","%"+searchParamMap.get("LIKE_name")+"%");
		else
			materialTypeEntity = materialTypeDao.findByLevelLayerAndAbolishedAndIdNotIn(1,0,newArrayList);
		Integer i=(pageNumber-1)*pageSize;
		Integer p=0;
		if((materialTypeEntity.size()-i)<=pageSize)
			p=materialTypeEntity.size()-i;
		else
			p=pageSize;
		Page<MaterialTypeEntity> page =new PageImpl<MaterialTypeEntity>(materialTypeEntity.subList(i, i+p), pagin, materialTypeEntity.size());
		List<MaterialTypeEntity> list=page.getContent();
		for(MaterialTypeEntity m:list)
		{
			MaterialTypeEntity mm=materialTypeDao.findOne(m.getParentId());
			if(mm!=null)
			{
				m.setFaname(mm.getName());
			}
		}
		return page;
	}
	public Map<String,Object> addCycle(VendorPerforCycleEntity cycleEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforCycleEntity vc=cycleDao.findByCode(cycleEntity.getCode());
		if(vc==null)
		{
			cycleDao.save(cycleEntity);
			map.put("success", true);
			map.put("msg", "添加成功");
		}
		else	
		{
			map.put("success", false);
			map.put("msg", "添加失败,编号已经存在！");
		}
		return map;
	}

	public String look(Long id) {
		List<VendorPerforPurchaseEntity> lists=purchaseDao.findByCycleId(id);
		if(lists!=null&&lists.size()>0)
		{
			return ""+lists.get(0).getPurchaseNumber();
		}
		return "";
		
	}

	public String updateCycleStart(Long id) {
		// TODO Auto-generated method stub
		VendorPerforCycleEntity cycleEntity=cycleDao.findOne(id);
		String data="";
		data=cycleEntity.getId()+",";
		data=data+cycleEntity.getCode()+",";
		if(null!=cycleEntity.getInitDates())
		{
			data=data+cycleEntity.getCycleName()+",";
		}
		else
		{
			data=data+""+",";
		}
		if(null!=cycleEntity.getInitDates())
		{
			data=data+cycleEntity.getInitDates()+",";
		}
		else
		{
			data=data+""+",";
		}
		data=data+cycleEntity.getFixDates()+",";
		if(null!=cycleEntity.getDefaultPurchase())
		{
			data=data+cycleEntity.getDefaultPurchase()+",";
		}
		else
		{
			data=data+""+",";
		}
		if(null!=cycleEntity.getRemarks())
		{
			data=data+cycleEntity.getRemarks();
		}
		else
		{
			data=data+"";
		}
		return data;
	}

	public Map<String,Object> updateCycle(VendorPerforCycleEntity cycleEntity) {
		Map<String,Object> map=new HashMap<String, Object>();
		VendorPerforCycleEntity cycleEntity1=cycleDao.findOne(cycleEntity.getId());
		cycleEntity1.setCode(cycleEntity.getCode());
		cycleEntity1.setDefaultPurchase(cycleEntity.getDefaultPurchase());
		cycleEntity1.setCycleName(cycleEntity.getCycleName());
		cycleEntity1.setInitDates(cycleEntity.getInitDates());
		cycleEntity1.setFixDates(cycleEntity.getFixDates());
		cycleEntity1.setRemarks(cycleEntity.getRemarks());
		cycleDao.save(cycleEntity1);
		map.put("success", true);
		map.put("msg", "修改成功");
		return map;
	}

	public String releaseCycle(List<VendorPerforCycleEntity> cycleEntitys) {
		for(VendorPerforCycleEntity cycleEntity:cycleEntitys)
		{
			VendorPerforCycleEntity c=cycleDao.findOne(cycleEntity.getId());
			c.setAbolished(0);
			cycleDao.save(c);
		}
		return "1";
	}

	public String delsCycle(List<VendorPerforCycleEntity> cycleEntitys) {
		for(VendorPerforCycleEntity cycleEntity:cycleEntitys)
		{
			cycleDao.abolish(cycleEntity.getId());
		}
		return "1";
	}

	public List<VendorPerforPurchaseEntity> getPurchase(Long cid) {
		List<VendorPerforPurchaseEntity> list=purchaseDao.findByCycleId(cid);
		if(list==null||list.size()==0)
		{
			return new ArrayList<VendorPerforPurchaseEntity>();
		}
		return list;
	}

	public String saveUpdatePCycle(VendorPerforPurchaseEntity purchaseEntity) {
		VendorPerforPurchaseEntity  p=purchaseDao.findByMaterialtypeIdAndCycleId(purchaseEntity.getMaterialtypeId(), purchaseEntity.getCycleId());
		if(p!=null)
		{
			p.setPurchaseNumber(purchaseEntity.getPurchaseNumber());
			purchaseDao.save(p);
		} else {
			purchaseDao.save(purchaseEntity);
		}
		return "1";
	}

	public String deletePurchase(Long mid, Long cid) {
		VendorPerforPurchaseEntity  p=purchaseDao.findByMaterialtypeIdAndCycleId(mid,cid);
		if(p!=null)
		{
			purchaseDao.delete(p);
		}
		return "1";
	}
	
	public void saveUpdatePCycle(Long cid, String[] materialtypeIds,
			String[] materialtypeName, String[] materialtypeNamef,
			String[] purchaseNumber) {
		for(int i=0;i<materialtypeIds.length;i++)
		{
			VendorPerforPurchaseEntity purchaseEntity=new VendorPerforPurchaseEntity();
			purchaseEntity.setMaterialtypeId(Long.parseLong(materialtypeIds[i]));
			purchaseEntity.setMaterialtypeName(materialtypeName[i]);
			purchaseEntity.setMaterialtypeNamef(materialtypeNamef[i]);
			purchaseEntity.setPurchaseNumber(Integer.parseInt(purchaseNumber[i]));
			purchaseEntity.setCycleId(cid);
			VendorPerforPurchaseEntity  p=purchaseDao.findByMaterialtypeIdAndCycleId(purchaseEntity.getMaterialtypeId(), purchaseEntity.getCycleId());
			if(p!=null)
			{
				p.setPurchaseNumber(purchaseEntity.getPurchaseNumber());
				purchaseDao.save(p);
			}
			else
				purchaseDao.save(purchaseEntity);
		}
	}

	/**
	 * 获得有效的周期
	 * @return 有效周期
	 */
	public List<VendorPerforCycleEntity> getList() {
		return cycleDao.findByAbolished(StatusConstant.STATUS_NO);
	}
}
