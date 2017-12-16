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

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.EightDRectification;
import com.qeweb.scm.qualityassurance.entity.OnlineInspectionEntity;
import com.qeweb.scm.qualityassurance.repository.EightDDao;
import com.qeweb.scm.qualityassurance.repository.OnlineInspectionDao;
import com.qeweb.scm.qualityassurance.transfer.OnlineInspectionTransfer;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;

@Service
@Transactional
public class OnlineInspectionService {  
	
	@Autowired
	private OnlineInspectionDao onlineInspectionDao;
	
	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	
	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private EightDDao eightDDao;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	public Page<OnlineInspectionEntity> getOnlineInspectionEntityList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<OnlineInspectionEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), OnlineInspectionEntity.class);
		return onlineInspectionDao.findAll(spec,pagin);
	}

	public Map<String, Object> addOnlineInspection(OnlineInspectionEntity entity) {
		Map<String, Object> map=new HashMap<String, Object>();
		entity.setQualityStatus(0);
		entity.setEightDStatus(0);
		if(entity.getId()==0)
		{
			entity.setCode(getSerialNumberService().geneterNextNumberByKey("OI_"));
		}
		else
		{
			OnlineInspectionEntity en=onlineInspectionDao.findOne(entity.getId());
			entity.setAbolished(en.getAbolished());
			entity.setCode(en.getCode());
			entity.setCreateTime(en.getCreateTime());
			entity.setCreateUserId(en.getCreateUserId());
			entity.setLastUpdateTime(en.getLastUpdateTime());
			entity.setUpdateUserId(en.getUpdateUserId());
			entity.setUpdateUserName(en.getUpdateUserName());
		}
		onlineInspectionDao.save(entity);
		map.put("success", true);
		map.put("msg", "添加成功！！");
		return map;
	}

	public boolean saveOnlineInspectionTransfer(List<OnlineInspectionTransfer> list, ILogger logger) {
		List<OnlineInspectionEntity> entities=new ArrayList<OnlineInspectionEntity>();
		int i=0;
		for(OnlineInspectionTransfer transfer:list)
		{
			i++;
			logger.log("-------->开始第"+i+"条数据");
			OnlineInspectionEntity entity =new OnlineInspectionEntity();
			try{
				entity.setStartTime(Timestamp.valueOf(transfer.getStartTime()));
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
				entity.setMoney(Double.parseDouble(transfer.getMoney()));
			}catch(Exception e){
				logger.log("考核金额不是数字类型");
				return false;
			}
			if(transfer.getStages().equals("产品审核"))
			{
				entity.setStages(1);
			}else if(transfer.getStages().equals("装配现场"))
			{
				entity.setStages(2);
			}else if(transfer.getStages().equals("客户反馈"))
			{
				entity.setStages(3);
			}else if(transfer.getStages().equals("咸阳工厂"))
			{
				entity.setStages(4);
			}
			else
			{
				logger.log("无效的发生地点，只能是产品审核、装配现场、客户反馈或者咸阳工厂！");
				return false;
			}
			entity.setCode(getSerialNumberService().geneterNextNumberByKey("OI_"));
			entity.setDescribe(transfer.getDescribe());
			entity.setQualityStatus(0);
			entity.setEightDStatus(0);
			entities.add(entity);
		}
		onlineInspectionDao.save(entities);
		return true;
	}

	public void publishItems(List<OnlineInspectionEntity> itemList) {
		for (OnlineInspectionEntity ifu:itemList) 
		{
			OnlineInspectionEntity iu=onlineInspectionDao.findOne(ifu.getId());
			iu.setQualityStatus(1);
			onlineInspectionDao.save(iu);
		}
	}

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	public boolean createEightD(long id) {
		OnlineInspectionEntity inspection = onlineInspectionDao.findOne(id);
		if(inspection != null){
			EightDRectification eightD = new EightDRectification();
			eightD.setVendor(inspection.getVendorBaseInfoEntity().getOrg());
			eightD.setMaterial(inspection.getMaterialEntity());
			eightD.setHappenTime(inspection.getStartTime());
			int stages = inspection.getStages();
			String happenPlace = "";
			if (stages == 1){
				happenPlace = "产品审核";
			}
			if (stages == 2){
				happenPlace = "装配现场";
			}
			if (stages == 3){
				happenPlace = "客户反馈";
			}
			eightD.setHappenPlace(happenPlace);
			eightD.setMalfunctionQty(StringUtils.convertToDouble(inspection.getTotalNumber()+""));
			eightD.setRecDescription(inspection.getDescribe());
			eightD.setStatus(0);
			eightD.setPublishStatus(0);
			eightD.setReproveStatus(0);
			eightDDao.save(eightD);
			
			inspection.setEightDStatus(1);
			onlineInspectionDao.save(inspection);
			return true;
		}else{
			return false;
		}
	}

	public List<OnlineInspectionTransfer> getOnlineTransfer(List<OnlineInspectionEntity> list) {
		List<OnlineInspectionTransfer> ret = new ArrayList<OnlineInspectionTransfer>();
		OnlineInspectionTransfer trans = null;
		for(OnlineInspectionEntity o : list) {
			trans = new OnlineInspectionTransfer();
			trans.setStartTime(o.getStartTime()+"");
			trans.setVendorCode(o.getVendorBaseInfoEntity().getCode());
			trans.setVendorName(o.getVendorBaseInfoEntity().getName());
			trans.setMaterialCode(o.getMaterialEntity().getCode());
			trans.setMaterialName(o.getMaterialEntity().getName());
			trans.setTotalNumber(o.getTotalNumber()+"");
			trans.setMoney(o.getMoney()+"");
			if(o.getStages() !=null && o.getStages() == 1){
				trans.setStages("产品审核");
			}
			if(o.getStages() !=null && o.getStages() == 2){
				trans.setStages("装配现场");
			}
			if(o.getStages() !=null && o.getStages() == 3){
				trans.setStages("客户反馈");
			}
			trans.setDescribe(o.getDescribe());
			ret.add(trans);
		}
		return ret;
	}
}
