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
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;
import com.qeweb.scm.qualityassurance.entity.ZeroKilometersEntity;
import com.qeweb.scm.qualityassurance.repository.PPMEntityDao;
import com.qeweb.scm.qualityassurance.repository.ZeroKilometersDao;
import com.qeweb.scm.qualityassurance.transfer.ZeroKilometersTransfer;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;

@Service
@Transactional
public class ZeroKilometersService {

	@Autowired
	private ZeroKilometersDao zeroKilometersDao;

	@Autowired
	private OrganizationDao organizationDao;

	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	
	@Autowired
	private OrganizationDao orgDao;

	@Autowired
	private PPMEntityDao pPMEntityDao;
	
	@Autowired
	private MaterialDao materialDao;

	public Page<ZeroKilometersEntity> getZeroKilometersList(int pageNumber, int pageSize,
			Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ZeroKilometersEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(),
				ZeroKilometersEntity.class);
		return zeroKilometersDao.findAll(spec, pagin);
	}

	public ZeroKilometersEntity getZeroKilometers(Long id) {
		return zeroKilometersDao.findOne(id);
	}

	public void saveAdd(ZeroKilometersEntity zkilo) {
		zeroKilometersDao.save(zkilo);
	}

	public ZeroKilometersEntity getZkListById(Long id) {
		return zeroKilometersDao.findOne(id);
	}

	public void saveMod(Long id, ZeroKilometersEntity oldzkilo) {
		zeroKilometersDao.save(oldzkilo);
	}

	/**
	 * 发布
	 * 
	 * @param informs
	 */
	public void publish(List<ZeroKilometersEntity> informs) {
		for (ZeroKilometersEntity inform : informs) {
			inform.setStatus(1);
		}
		zeroKilometersDao.save(informs);
	}

	// 导入这块
	public boolean imp(List<ZeroKilometersTransfer> list, ILogger logger) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<ZeroKilometersEntity> zkEntity = new ArrayList<ZeroKilometersEntity>();
		ZeroKilometersEntity entity = new ZeroKilometersEntity();

		for (ZeroKilometersTransfer trans : list) {
//			entity.setStartTime(Timestamp.valueOf(trans.getStartTime()));
			entity.setEndTime(Timestamp.valueOf(trans.getEndTime()));
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
			entity.setReportCode(trans.getReportCode());
			List<OrganizationEntity> organ = organizationDao.findByCode(trans.getVendorCode());
			if (organ != null) {
				if (organ.size() == 1) {
					VendorBaseInfoEntity vb = baseInfoDao.findByOrgIdAndCurrentVersion(organ.get(0).getId(),
							StatusConstant.STATUS_YES);
					if (vb != null) {
						entity.setVendorId(vb.getId());
					} else {
						logger.log("VendorBaseInfoEntity数据问题，找不到数据，请联系管理员！！！");
						return false;
					}
				} else {
					logger.log("org数据问题出现相同CODE，请联系管理员！！！");
					return false;
				}
			} else {
				logger.log("没有找此供应商编号");
				return false;
			}
			List<MaterialEntity> ms = materialDao.findByCode(trans.getFirstPictureCode());
			if (ms != null) {
				if (ms.size() == 1) {
//					entity.setMaterial(ms.get(0));
					entity.setMaterialId(ms.get(0).getId());
				} else {
					logger.log("MaterialEntity数据问题，找不到数据，请联系管理员！！！");
					return false;
				}
			} else {
				logger.log("没有找此物料图号");
				return false;
			}
			// entity.setFirstPictureCode(trans.getFirstPictureCode());
			// entity.setFirstPictureName(trans.getFirstPictureName());
			entity.setFactory(trans.getFactory());
			entity.setMotorFactory(trans.getMotorFactory());
			entity.setModels(trans.getModels());
			entity.setStandardFault(trans.getStandardFault());
			entity.setCounts(Integer.parseInt(trans.getCounts()));
			entity.setTotalCounts(Integer.parseInt(trans.getTotalCounts()));
			entity.setMileage(Integer.parseInt(trans.getMileage()));
			entity.setAgreementNo(trans.getAgreementNo());
			entity.setAssemblyModel(trans.getAssemblyModel());
			entity.setAppearanceNumber(trans.getAppearanceNumber());
			entity.setArea(trans.getArea());
			entity.setServiceStation(trans.getServiceStation());
			entity.setMaintenanceTime(Timestamp.valueOf(trans.getMaintenanceTime()));
			entity.setFaultDescription(trans.getFaultDescription());
			entity.setCauseAndAnalysis(trans.getCauseAndAnalysis());
			zkEntity.add(entity);
		}

		zeroKilometersDao.save(zkEntity);
		map.put("success", true);
		map.put("msg", "导入成功!");
		return true;

	}

	public void calculate(String time) throws Exception {
		List<ZeroKilometersEntity> list = zeroKilometersDao.findByMonth(time);
		Map<String,List<ZeroKilometersEntity>> zeroKilometersListMap = new HashMap<String,List<ZeroKilometersEntity>>();
		for (ZeroKilometersEntity z : list){
			if (zeroKilometersListMap.containsKey(z.getVendorBaseInfoEntity().getCode()+","+z.getMaterial().getCode())){
				zeroKilometersListMap.get(z.getVendorBaseInfoEntity().getCode()+","+z.getMaterial().getCode()).add(z);
			}else{
				zeroKilometersListMap.put(z.getVendorBaseInfoEntity().getCode()+","+z.getMaterial().getCode(), Lists.newArrayList(z));
			}
		}
		for(Map.Entry<String,List<ZeroKilometersEntity>> entry : zeroKilometersListMap.entrySet()) {
			List<ZeroKilometersEntity> entityList = entry.getValue();  //同一家供应商同一个月的数据
			String key = entry.getKey();
			String[] keys = key.split(",");
			String vendorCode = keys[0];
			OrganizationEntity vendor = orgDao.findByCode(vendorCode).get(0);
			MaterialEntity material = entityList.get(0).getMaterial();
			long vendorId = entityList.get(0).getVendorBaseInfoEntity().getId();
			long materialId = material.getId();
			double amount = 0; //当月零公里故障总数
			double totalAmount = 0; //月平均入库数量总和（三个月平均的，万一没数据，有几个月算几个月）
			double totalAmount0 = 0; //本月入库数量总和
			if (entityList != null && entityList.size() > 0){
				for (int i = 0; i < entityList.size(); i++) {
					amount += entityList.get(i).getCounts();
					if(entityList.get(i).getTotalCounts() != null){
						totalAmount0 +=entityList.get(i).getTotalCounts();
					}
				}
			}
			double totalAmount1 = 0; //上个月入库数量总和
			double totalAmount2 = 0; //上上个月入库数量总和
			Timestamp [] t1 = DateUtil.getBeginAndEndTimes_month(-1);
			Timestamp beginTime1 = t1[0];
			Timestamp endTime1 = t1[1];
			totalAmount1 = getMonthAmount(vendorId,beginTime1+"",endTime1+"",materialId);
			Timestamp [] t2 = DateUtil.getBeginAndEndTimes_month(-2);
			Timestamp beginTime2 = t2[0];
			Timestamp endTime2 = t2[1];
			totalAmount2 = getMonthAmount(vendorId,beginTime2+"",endTime2+"",materialId);
			double score = 0;
			if(totalAmount0 != 0){
				double count = 1;
				if(totalAmount1 != 0){
					count ++;
				}
				if(totalAmount2 != 0){
					count ++;
				}
				totalAmount = (totalAmount0 + totalAmount1 + totalAmount2)/count;
				double rate = amount/totalAmount;
				score = rate*1000000;
			}
			PPMEntity ppm = pPMEntityDao.getByMonthAndVendorAndTypeAndMaterial(time, vendor.getCode(), 3, material.getCode());
			if (ppm == null){
				ppm = new PPMEntity();
			}
			ppm.setMonth(time);
			ppm.setVendor(vendor);
			ppm.setMaterial(material);
			ppm.setPpm(score);
			ppm.setPpmType(3);				// 3为零公里PPM
			ppm.setAbolished(Constant.DELETE_FLAG);
			pPMEntityDao.save(ppm);
		}
	}

	private double getMonthAmount(Long vendorId, String beginTime, String endTime, Long materialId) {
		List<?>  list = zeroKilometersDao.getMonthAmount(vendorId, beginTime, endTime, materialId);
		double totalCount = 0;
		if(list != null && list.size()>0){
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i) != null){
					totalCount += StringUtils.convertToDouble(list.get(i).toString());
				}
			}
		}
		return totalCount;
	}
		
}