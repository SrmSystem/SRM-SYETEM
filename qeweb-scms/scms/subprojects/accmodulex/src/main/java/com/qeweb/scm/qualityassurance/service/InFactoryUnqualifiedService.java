package com.qeweb.scm.qualityassurance.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;
import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.Constant;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.qualityassurance.entity.InFactoryUnqualifiedEntity;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;
import com.qeweb.scm.qualityassurance.repository.InFactoryUnqualifiedDao;
import com.qeweb.scm.qualityassurance.repository.PPMEntityDao;
import com.qeweb.scm.qualityassurance.transfer.InFactoryUnqualifiedTransfer;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;

@Service
@Transactional
public class InFactoryUnqualifiedService {  
	
	@Autowired
	private InFactoryUnqualifiedDao unqualifiedDao;
	
	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	
	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private PPMEntityDao pPMEntityDao;
	
	public Page<InFactoryUnqualifiedEntity> getInFactoryUnqualifiedEntityList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InFactoryUnqualifiedEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InFactoryUnqualifiedEntity.class);
		return unqualifiedDao.findAll(spec,pagin);
	}

	public Map<String, Object> addInFactoryUnqualified(InFactoryUnqualifiedEntity entity) {
		Map<String, Object> map=new HashMap<String, Object>();
		if(entity.getSamplings()==0)
		{
			entity.setPpm(entity.getTotalNumber());
		}
		else
		{
			double j=(double)entity.getUnqualified()/(double)entity.getSamplings();
			int k=(int) (j*entity.getTotalNumber());
			entity.setPpm(k);
		}
		if(entity.getId()!=0)
		{
			InFactoryUnqualifiedEntity en=unqualifiedDao.findOne(entity.getId());
			entity.setAbolished(en.getAbolished());
			entity.setCreateTime(en.getCreateTime());
			entity.setCreateUserId(en.getCreateUserId());
			entity.setLastUpdateTime(en.getLastUpdateTime());
			entity.setUpdateUserId(en.getUpdateUserId());
			entity.setUpdateUserName(en.getUpdateUserName());
		}
		entity.setQualityStatus(0);
		String year = entity.getEndTime().getYear()+1900+"";
		int month = entity.getEndTime().getMonth() +1;
		String mon = "";
		if(month <10){
			mon = "0"+month;
		}else{
			mon = month +"";
		}
		String time = year+mon;
		entity.setMonth(time);
		unqualifiedDao.save(entity);
		map.put("success", true);
		map.put("msg", "操作成功！！");
		return map;
	}

	public boolean saveInFactoryUnqualifiedTransfer(List<InFactoryUnqualifiedTransfer> list, ILogger logger) {
		List<InFactoryUnqualifiedEntity> entities=new ArrayList<InFactoryUnqualifiedEntity>();
		int i=0;
		for(InFactoryUnqualifiedTransfer transfer:list)
		{
			i++;
			logger.log("-------->开始第"+i+"条数据");
			InFactoryUnqualifiedEntity entity =new InFactoryUnqualifiedEntity();
//			try{
//				entity.setStartTime(Timestamp.valueOf(transfer.getStartTime()));
//			}catch(Exception e){
//				logger.log("开始时间格式不对");
//				return false;
//			}
			try{
				entity.setEndTime(Timestamp.valueOf(transfer.getEndTime()));
				String year = entity.getEndTime().getYear()+1900+"";
				int month = entity.getEndTime().getMonth() +1;
				String mon = "";
				if(month <10){
					mon = "0"+month;
				}else{
					mon = month +"";
				}
				String time = year+mon;
				entity.setMonth(time);
			}catch(Exception e){
				logger.log("时间格式不对");
				return false;
			}
			List<OrganizationEntity> organ= orgDao.findByCode(transfer.getVendorCode());
			if(organ!=null)
			{
				if(organ.size()==1)
				{
					VendorBaseInfoEntity vb = baseInfoDao.findByOrgIdAndCurrentVersion(organ.get(0).getId(), StatusConstant.STATUS_YES);
					if(vb!=null)
					{
						entity.setVendorId(vb.getId());
					}
					else
					{
						logger.log("VendorBaseInfoEntity数据问题，找不到数据，请联系管理员！！！");
						return false;
					}
				}
				else
				{
					logger.log("org数据问题出现相同CODE，请联系管理员！！！");
					return false;
				}
			}
			else
			{
				logger.log("没有找此供应商编号");
				return false;
			}
			List<MaterialEntity> ms=materialDao.findByCode(transfer.getMaterialCode());
			if(ms!=null)
			{
				if(ms.size()==1)
				{
					entity.setMaterialId(ms.get(0).getId());
				}
				else
				{
					logger.log("MaterialEntity数据问题，找不到数据，请联系管理员！！！");
					return false;
				}
			}
			else
			{
				logger.log("没有找此物料图号");
				return false;
			}
			try{
				entity.setTotalNumber(Integer.parseInt(transfer.getTotalNumber()));
			}catch(Exception e){
				logger.log("数量不是数字类型");
				return false;
			}
			try{
				entity.setUnqualified(Integer.parseInt(transfer.getUnqualified()));
			}catch(Exception e){
				logger.log("不合格数量不是数字类型");
				return false;
			}
			try{
				entity.setSamplings(Integer.parseInt(transfer.getSampling()));
			}catch(Exception e){
				logger.log("抽检数量不是数字类型");
				return false;
			}
			if(transfer.getDispose().equals("让步接收"))
			{
				entity.setDispose(1);
			}else if(transfer.getDispose().equals("返修"))
			{
				entity.setDispose(2);
			}else if(transfer.getDispose().equals("报废"))
			{
				entity.setDispose(3);
			}
			else
			{
				logger.log("无效的处置类型标识");
				return false;
			}
			if(entity.getSamplings()==0)
			{
				entity.setPpm(entity.getTotalNumber());
			}
			else
			{
				double j=(double)entity.getUnqualified()/(double)entity.getSamplings();
				int k=(int) (j*entity.getTotalNumber());
				entity.setPpm(k);
			}
			entity.setDescribe(transfer.getDescribe());
			entity.setQualityStatus(0);
			entities.add(entity);
		}
		unqualifiedDao.save(entities);
		return true;
	}

	public void publishItems(List<InFactoryUnqualifiedEntity> itemList) {
		for (InFactoryUnqualifiedEntity ifu:itemList) 
		{
			InFactoryUnqualifiedEntity iu=unqualifiedDao.findOne(ifu.getId());
			iu.setQualityStatus(1);
			unqualifiedDao.save(iu);
		}
	}

	public void calculate(String time) {
		List<InFactoryUnqualifiedEntity> list = unqualifiedDao.findByMonth(time);
		Map<String,List<InFactoryUnqualifiedEntity>> inFactoryListMap = new HashMap<String,List<InFactoryUnqualifiedEntity>>();
		for (InFactoryUnqualifiedEntity i : list){
			if (inFactoryListMap.containsKey(i.getVendorBaseInfoEntity().getCode())){
				inFactoryListMap.get(i.getVendorBaseInfoEntity().getCode()).add(i);
			}else{
				inFactoryListMap.put(i.getVendorBaseInfoEntity().getCode(), Lists.newArrayList(i));
			}
		}
		for(Map.Entry<String,List<InFactoryUnqualifiedEntity>> entry : inFactoryListMap.entrySet()) {
			List<InFactoryUnqualifiedEntity> entityList = entry.getValue();  //同一家供应商同一个月的数据
			OrganizationEntity vendor = orgDao.findByCode(entry.getKey()).get(0);
			double amount = 0;
			double totalAmount = 0;
			if (entityList != null && entityList.size() > 0){
				for (int i = 0; i < entityList.size(); i++) {
					double unqualifide = entityList.get(i).getUnqualified();
					double samplings = entityList.get(i).getSamplings();
					amount += (unqualifide/samplings)*entityList.get(i).getTotalNumber();
					totalAmount += entityList.get(i).getTotalNumber();
				}
			}
			double score = 0;
			if(totalAmount != 0){
				double rate = amount/totalAmount;
				score = rate*1000000;
			}
			PPMEntity ppm = pPMEntityDao.getByMonthAndVendorAndType(time, entry.getKey(), 1);
			if (ppm == null){
				ppm = new PPMEntity();
			}
			ppm.setMonth(time);
			ppm.setVendor(vendor);
			ppm.setPpm(score);
			ppm.setPpmType(1);				// 1为交付PPM
			ppm.setAbolished(Constant.DELETE_FLAG);
			pPMEntityDao.save(ppm);
		}
	}
}
