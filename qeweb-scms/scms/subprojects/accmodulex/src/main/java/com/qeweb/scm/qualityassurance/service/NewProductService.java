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
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.purchasemodule.constans.PurchaseConstans;
import com.qeweb.scm.qualityassurance.entity.NewProductEntity;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;
import com.qeweb.scm.qualityassurance.repository.NewProductDao;
import com.qeweb.scm.qualityassurance.repository.PPMEntityDao;
import com.qeweb.scm.qualityassurance.transfer.NewProductTransfer;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;

@Service
@Transactional
public class NewProductService {

	@Autowired
	private NewProductDao newProductDao;

	@Autowired
	private OrganizationDao orgDao;
	
	@Autowired
	private VendorBaseInfoDao baseInfoDao;

	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private PPMEntityDao pPMEntityDao;

	public NewProductEntity getNewproductEntity(String vendorCode, String matName) {
		return newProductDao.findNewproductEntityByCode(vendorCode, matName);
	}

	public int getPpap(int deliverTimes, int qualified, long vendorId) {
		return newProductDao.findPpap(deliverTimes, qualified, vendorId);
	}

	public int getPpap(int deliverTimes, long vendorId) {
		return newProductDao.findPpap(deliverTimes, vendorId);
	}

	/**
	 * 获取新产品开发质量列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<NewProductEntity> getNewProductEntityList(int pageNumber, int pageSize,
			Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<NewProductEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(),
				NewProductEntity.class);
		return newProductDao.findAll(spec, pagin);
	}

	/**
	 * 批量发布
	 * 
	 * @param itemList
	 * @param userEntity
	 */
	public void publishItems(List<NewProductEntity> itemList, UserEntity userEntity) {
		for (NewProductEntity entity : itemList) {
			entity.setDataStatus(PurchaseConstans.PUBLISH_STATUS_YES);
		}
		newProductDao.save(itemList);
	}

	public NewProductEntity getNewProductEntity(Long id) {
		return newProductDao.findOne(id);
	}

	/**
	 * 批量导入字段配置
	 * 
	 * @param list
	 * @param logger
	 * @return
	 */
	public boolean saveNewProductTransfer(List<NewProductTransfer> list, ILogger logger) {
		logger.log("\n->正在准备合并主数据与明细数据\n");
//		Map<String, NewProductEntity> tmpMap = new HashMap<String, NewProductEntity>();
		List<NewProductEntity> newproList = new ArrayList<NewProductEntity>();
		String key = null;
		String key2 = null;
		NewProductEntity entity = null;
		for (NewProductTransfer trans : list) {
			key = trans.getVendorCode();
			key2 = trans.getMatName();
//			if (!tmpMap.containsKey(key)) {
//				entity = getNewproductEntity(key, key2);
//				if (entity == null)
					entity = new NewProductEntity();
				entity.setDataStatus(PurchaseConstans.PUBLISH_STATUS_NO);
				entity.setDeliverTimes(Integer.valueOf(trans.getDeliverTimes()));
				// entity.setMatName(trans.getMatName());
				try{
					entity.setQmTime(Timestamp.valueOf(trans.getQmTime()));
					String year = entity.getQmTime().getYear()+1900+"";
					int month = entity.getQmTime().getMonth() +1;
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
				
				entity.setQualified(Integer.valueOf(trans.getQualified()));
				entity.setSampleSize(Integer.valueOf(trans.getSampleSize()));
				entity.setSeq(trans.getSeq());
				// entity.setVendorCode(trans.getVendorCode());
				// entity.setVendorName(trans.getVendorName());

				List<OrganizationEntity> organ = orgDao.findByCode(trans.getVendorCode());
				if (organ != null && organ.size() > 0) {
					entity.setVendor(organ.get(0));
				} else {
					logger.log("没有找此供应商编号");
					return false;
				}
				entity.setMatName(trans.getMatName());
				newproList.add(entity);
//				OrganizationEntity orgname = orgDao.findByName(trans.getVendorName());
//				entity.setVendor(orgname);
				// MaterialEntity
				// material=materialDao.findByName(trans.getMatName());
				// entity.setMaterial(material);
//				tmpMap.put(key, entity);
//			} else {
//				entity = tmpMap.get(key);
//			}
		}
		String logMsg = "合并主数据与明细数据结束,共有[" + newproList.size() + "]条有效的数据";
		logger.log(logMsg);
		logger.log("-->开始对合并数据进行保存操作....");
		newProductDao.save(newproList);
		logger.log("-->保存完成");
		return true;
	}

	public void calculate(String time) {
		List<NewProductEntity> list = newProductDao.findByMonth(time);
		Map<String,List<NewProductEntity>> newProductListMap = new HashMap<String,List<NewProductEntity>>();
		for (NewProductEntity n : list){
			if (newProductListMap.containsKey(n.getVendor().getCode())){
				newProductListMap.get(n.getVendor().getCode()).add(n);
			}else{
				newProductListMap.put(n.getVendor().getCode(), Lists.newArrayList(n));
			}
		}
		for(Map.Entry<String,List<NewProductEntity>> entry : newProductListMap.entrySet()) {
			List<NewProductEntity> entityList = entry.getValue();  //同一家供应商同一个月的数据
			OrganizationEntity vendor = orgDao.findByCode(entry.getKey()).get(0);
			double amount = 0; //本月第一次交付就合格的总数量
			double totalAmount = 0; //本月所有交付的总数量
			if (entityList != null && entityList.size() > 0){
				for (int i = 0; i < entityList.size(); i++) {
					if (entityList.get(i).getDeliverTimes() == 1 && entityList.get(i).getQualified() == 1){
						amount += entityList.get(i).getSampleSize();
					}
					totalAmount += entityList.get(i).getSampleSize();
				}
			}
			double rate = 0;
			if(totalAmount != 0){
				rate = amount/totalAmount;
			}
			PPMEntity ppm = pPMEntityDao.getByMonthAndVendorAndType(time, entry.getKey(),5);
			if (ppm == null){
				ppm = new PPMEntity();
			}
			ppm.setMonth(time);
			ppm.setVendor(vendor);
			ppm.setRate(rate);
//			ppm.setPpm(score);
			ppm.setPpmType(5);				// 5为毛坯废品率
			ppm.setAbolished(Constant.DELETE_FLAG);
			pPMEntityDao.save(ppm);
		}
	}
}
