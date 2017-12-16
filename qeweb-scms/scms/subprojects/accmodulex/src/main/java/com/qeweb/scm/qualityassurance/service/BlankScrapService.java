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
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.qualityassurance.entity.BlankScrapEntity;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;
import com.qeweb.scm.qualityassurance.repository.BlankScrapDao;
import com.qeweb.scm.qualityassurance.repository.PPMEntityDao;
import com.qeweb.scm.qualityassurance.transfer.BlankScrapTransfer;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;

@Service
@Transactional
public class BlankScrapService {  
	
	@Autowired
	private BlankScrapDao blankScrapDao;
	
	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	
	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private PPMEntityDao pPMEntityDao;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	public Page<BlankScrapEntity> getBlankScrapEntityList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BlankScrapEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BlankScrapEntity.class);
		return blankScrapDao.findAll(spec,pagin);
	}

	public Map<String, Object> addBlankScrap(BlankScrapEntity entity) {
		Map<String, Object> map=new HashMap<String, Object>();
		entity.setState(0);
		if(entity.getId()==0){
//			entity.setCode(getSerialNumberService().geneterNextNumberByKey("OI_"));
		}
		else{
			BlankScrapEntity bs=blankScrapDao.findOne(entity.getId());
			entity.setAbolished(bs.getAbolished());
//			entity.setCode(bs.getCode());
			
			entity.setCreateTime(bs.getCreateTime());
			entity.setCreateUserId(bs.getCreateUserId());
			entity.setLastUpdateTime(bs.getLastUpdateTime());
			entity.setUpdateUserId(bs.getUpdateUserId());
			entity.setUpdateUserName(bs.getUpdateUserName());
		}
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
		blankScrapDao.save(entity);
		map.put("success", true);
		map.put("msg", "添加成功！！");
		return map;
	}

	public boolean saveBlankScrapTransfer(List<BlankScrapTransfer> list, ILogger logger) {
		List<BlankScrapEntity> entities=new ArrayList<BlankScrapEntity>();
		int i=0;
		for(BlankScrapTransfer transfer:list){
			i++;
			logger.log("-------->开始第"+i+"条数据");
			BlankScrapEntity entity =new BlankScrapEntity();
//			try{
//				entity.setStartTime(Timestamp.valueOf(transfer.getStartTime()));
//				entity.setEndTime(Timestamp.valueOf(transfer.getEndTime()));
//			}catch(Exception e){
//				logger.log("开始结束时间格式不对");
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
			
			List<OrganizationEntity> organ= orgDao.findByCode(transfer.getManufacturerCode());
			if(organ!=null){
				if(organ.size()==1){
					VendorBaseInfoEntity vb = baseInfoDao.findByOrgIdAndCurrentVersion(organ.get(0).getId(), StatusConstant.STATUS_YES);
					if(vb!=null){
//						entity.setVendorId(vb.getId());
					}
					else{
						logger.log("VendorBaseInfoEntity数据问题，找不到数据，请联系管理员！！！");
						return false;
					}
				}
				else{
					logger.log("org数据问题出现相同CODE，请联系管理员！！！");
					return false;
				}
			}
			else{
				logger.log("没有找此供应商编号");
				return false;
			}
			List<MaterialEntity> ms=materialDao.findByCode(transfer.getDrawingNo());
			if(ms!=null){
				if(ms.size()==1){
//					entity.setMaterialId(ms.get(0).getId());
				}
				else{
					logger.log("MaterialEntity数据问题，找不到数据，请联系管理员！！！");
					return false;
				}
			}
			else{
				logger.log("没有找此物料图号");
				return false;
			}
			
			try{
				entity.setAmount(Integer.parseInt(transfer.getAmount()));
				entity.setTotalAmount(Integer.parseInt(transfer.getTotalAmount()));
			}catch(Exception e){
				logger.log("数量不是数字类型");
				return false;
			}
			entity.setManufacturerCode(transfer.getManufacturerCode());
			entity.setManufacturer(transfer.getManufacturer());
			entity.setDrawingNo(transfer.getDrawingNo());
			entity.setPartsName(transfer.getPartsName());
			entity.setUnqualifiedPhenomenon(transfer.getUnqualifiedPhenomenon());
			entity.setHandle(transfer.getHandle());
			entity.setState(0);
			entities.add(entity);
		}
		blankScrapDao.save(entities);
		return true;
	}
	
	public void publishItems(List<BlankScrapEntity> itemList) {
		for (BlankScrapEntity ifu:itemList){ 
			BlankScrapEntity bs=blankScrapDao.findOne(ifu.getId());
			bs.setState(1);
			blankScrapDao.save(bs);
		}
	}

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	public List<BlankScrapTransfer> getBlankScrapVo(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<BlankScrapEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), BlankScrapEntity.class);
		List<BlankScrapEntity> list = blankScrapDao.findAll(spec);
		List<BlankScrapTransfer> ret = new ArrayList<BlankScrapTransfer>();
		BlankScrapTransfer trans = null;
		for(BlankScrapEntity blank : list) {
			trans = new BlankScrapTransfer();
			trans.setStartTime(blank.getStartTime()+"");
			trans.setEndTime(blank.getEndTime()+"");
			trans.setManufacturerCode(blank.getManufacturerCode());
			trans.setManufacturer(blank.getManufacturer());
			trans.setDrawingNo(blank.getDrawingNo());
			trans.setPartsName(blank.getPartsName());
			trans.setAmount(blank.getAmount()+"");
			trans.setUnqualifiedPhenomenon(blank.getUnqualifiedPhenomenon());
			trans.setHandle(blank.getHandle());
			trans.setState(blank.getState()==0?"登记":"发布");
			ret.add(trans);
		}
		return ret;
	}

	public void calculate(String time) {
		List<BlankScrapEntity> list = blankScrapDao.findByMonth(time);
		Map<String,List<BlankScrapEntity>> blankScrapListMap = new HashMap<String,List<BlankScrapEntity>>();
		for (BlankScrapEntity b : list){
			if (blankScrapListMap.containsKey(b.getManufacturerCode()+","+b.getDrawingNo())){
				blankScrapListMap.get(b.getManufacturerCode()+","+b.getDrawingNo()).add(b);
			}else{
				blankScrapListMap.put(b.getManufacturerCode()+","+b.getDrawingNo(), Lists.newArrayList(b));
			}
		}
		for(Map.Entry<String,List<BlankScrapEntity>> entry : blankScrapListMap.entrySet()) {
			List<BlankScrapEntity> entityList = entry.getValue();  //同一家供应商同一个月的数据
			String key = entry.getKey();
			String[] keys = key.split(",");
			String vendorCode = keys[0];
			String materialCode = keys[1];
			OrganizationEntity vendor = orgDao.findByCode(vendorCode).get(0);
			MaterialEntity material = materialDao.findByCode(materialCode).get(0);
			double amount = 0;
			double totalAmount = 0;
			if (entityList != null && entityList.size() > 0){
				for (int i = 0; i < entityList.size(); i++) {
					amount += entityList.get(i).getAmount();
					totalAmount += entityList.get(i).getTotalAmount();
				}
			}
			double rate = 0;
			if(totalAmount != 0){
				rate = amount/totalAmount;
			}
			PPMEntity ppm = pPMEntityDao.getByMonthAndVendorAndTypeAndMaterial(time, vendorCode, 2, materialCode);
			if (ppm == null){
				ppm = new PPMEntity();
			}
			ppm.setMonth(time);
			ppm.setVendor(vendor);
			ppm.setMaterial(material);
			ppm.setRate(rate);
//			ppm.setPpm(score);
			ppm.setPpmType(2);				// 2为毛坯废品率
			ppm.setAbolished(Constant.DELETE_FLAG);
			pPMEntityDao.save(ppm);
		}
	}
	
	
}
