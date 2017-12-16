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
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.qualityassurance.entity.PPMEntity;
import com.qeweb.scm.qualityassurance.entity.PostSalesServiceEntity;
import com.qeweb.scm.qualityassurance.repository.PPMEntityDao;
import com.qeweb.scm.qualityassurance.repository.PostSalesServiceDao;
import com.qeweb.scm.qualityassurance.transfer.PostSalesServiceTransfer;
import com.qeweb.scm.vendormodule.entity.VendorBaseInfoEntity;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;

@Service
@Transactional
public class PostSalesServiceService {  
	
	@Autowired
	private PostSalesServiceDao postSalesServiceDao;
	
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
	
	public Page<PostSalesServiceEntity> getPostSalesServiceEntityList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PostSalesServiceEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PostSalesServiceEntity.class);
		return postSalesServiceDao.findAll(spec,pagin);
	}

	public Map<String, Object> addPostSalesService(PostSalesServiceEntity entity) {
		Map<String, Object> map=new HashMap<String, Object>();
		entity.setQualityStatus(0);
		if(entity.getId()==0)
		{
			entity.setCode(getSerialNumberService().geneterNextNumberByKey("PSS_"));
		}
		else
		{
			PostSalesServiceEntity en=postSalesServiceDao.findOne(entity.getId());
			entity.setAbolished(en.getAbolished());
			entity.setCode(en.getCode());
			entity.setCreateTime(en.getCreateTime());
			entity.setCreateUserId(en.getCreateUserId());
			entity.setLastUpdateTime(en.getLastUpdateTime());
			entity.setUpdateUserId(en.getUpdateUserId());
			entity.setUpdateUserName(en.getUpdateUserName());
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
		postSalesServiceDao.save(entity);
		map.put("success", true);
		map.put("msg", "添加成功！！");
		return map;
	}

	public boolean savePostSalesServiceTransfer(List<PostSalesServiceTransfer> list, ILogger logger) {
		List<PostSalesServiceEntity> entities=new ArrayList<PostSalesServiceEntity>();
		int i=0;
		for(PostSalesServiceTransfer transfer:list)
		{
			i++;
			logger.log("-------->开始第"+i+"条数据");
			PostSalesServiceEntity entity =new PostSalesServiceEntity();
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
				logger.log("结束时间格式不对");
				return false;
			}
			try{
				entity.setRepairTime(Timestamp.valueOf(transfer.getRepairTime()));
			}catch(Exception e){
				logger.log("维修时间格式不对");
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
				entity.setTotalCounts(Integer.parseInt(transfer.getTotalCounts()));
			}catch(Exception e){
				logger.log("数量不是数字类型");
				return false;
			}
			entity.setDescribe(transfer.getDescribe());//标准故障
			
			entity.setDriving(transfer.getDriving());//行程里数
			
			entity.setAgreement(transfer.getAgreement());//协议号
			
			entity.setTotalmodel(transfer.getTotalmodel());//总成型号
			
			entity.setOutcode(transfer.getOutcode());//出厂编号
			
			entity.setArea(transfer.getArea());//片区
			
			entity.setStation(transfer.getStation());//服务站
			
			entity.setFault(transfer.getFault());//故障简述
			
			entity.setReason(transfer.getReason());//故障原因与分析
			
			entity.setModels(transfer.getModels());
			
			entity.setGeneratings(transfer.getGeneratings());
			
			entity.setHosts(transfer.getHosts());
			
			entity.setCode(getSerialNumberService().geneterNextNumberByKey("PSS_"));
			entity.setQualityStatus(0);
			entities.add(entity);
		}
		postSalesServiceDao.save(entities);
		return true;
	}

	public void publishItems(List<PostSalesServiceEntity> itemList) {
		for (PostSalesServiceEntity ifu:itemList) 
		{
			PostSalesServiceEntity iu=postSalesServiceDao.findOne(ifu.getId());
			iu.setQualityStatus(1);
			postSalesServiceDao.save(iu);
		}
	}

	public SerialNumberService getSerialNumberService() {
		return serialNumberService;
	}

	public void setSerialNumberService(SerialNumberService serialNumberService) {
		this.serialNumberService = serialNumberService;
	}

	public void calculate(String time) throws Exception {
		List<PostSalesServiceEntity> list = postSalesServiceDao.findByMonth(time);
		Map<String,List<PostSalesServiceEntity>> postSalesServiceListMap = new HashMap<String,List<PostSalesServiceEntity>>();
		for (PostSalesServiceEntity p : list){
			if (postSalesServiceListMap.containsKey(p.getVendorBaseInfoEntity().getCode()+","+p.getMaterialEntity().getCode())){
				postSalesServiceListMap.get(p.getVendorBaseInfoEntity().getCode()+","+p.getMaterialEntity().getCode()).add(p);
			}else{
				postSalesServiceListMap.put(p.getVendorBaseInfoEntity().getCode()+","+p.getMaterialEntity().getCode(), Lists.newArrayList(p));
			}
		}
		for(Map.Entry<String,List<PostSalesServiceEntity>> entry : postSalesServiceListMap.entrySet()) {
			List<PostSalesServiceEntity> entityList = entry.getValue();  //同一家供应商同一个月的数据
			String key = entry.getKey();
			String[] keys = key.split(",");
			String vendorCode = keys[0];
			OrganizationEntity vendor = orgDao.findByCode(vendorCode).get(0);
			MaterialEntity material = entityList.get(0).getMaterialEntity();
			long vendorId = entityList.get(0).getVendorBaseInfoEntity().getId();
			long materialId = material.getId();
			double amount = 0; //当月零公里故障总数
			double totalAmount = 0; //月平均入库数量总和（十二个月平均的，万一没数据，有几个月算几个月）
			double totalAmount0 = 0; //本月入库数量总和
			if (entityList != null && entityList.size() > 0){
				for (int i = 0; i < entityList.size(); i++) {
					amount += entityList.get(i).getTotalNumber();
					if(entityList.get(i).getTotalCounts() != null){
						totalAmount0 +=entityList.get(i).getTotalCounts();
					}
				}
			}
			double totalAmount1 = 0; //上个月入库数量总和（单月）
			double totalAmount2 = 0; //上两个月入库数量总和（单月）
			double totalAmount3 = 0; //上三个月入库数量总和（单月）
			double totalAmount4 = 0; //上四个月入库数量总和（单月）
			double totalAmount5 = 0; //上五个月入库数量总和（单月）
			double totalAmount6 = 0; //上六个月入库数量总和（单月）
			double totalAmount7 = 0; //上七个月入库数量总和（单月）
			double totalAmount8 = 0; //上八个月入库数量总和（单月）
			double totalAmount9 = 0; //上九个月入库数量总和（单月）
			double totalAmount10 = 0; //上十个月入库数量总和（单月）
			double totalAmount11 = 0; //上十一个月入库数量总和（单月）
			Timestamp [] t1 = DateUtil.getBeginAndEndTimes_month(-1);
			Timestamp beginTime1 = t1[0];
			Timestamp endTime1 = t1[1];
			totalAmount1 = getMonthAmount(vendorId,beginTime1+"",endTime1+"",materialId);
			
			Timestamp [] t2 = DateUtil.getBeginAndEndTimes_month(-2);
			Timestamp beginTime2 = t2[0];
			Timestamp endTime2 = t2[1];
			totalAmount2 = getMonthAmount(vendorId,beginTime2+"",endTime2+"",materialId);
			
			Timestamp [] t3 = DateUtil.getBeginAndEndTimes_month(-3);
			Timestamp beginTime3 = t3[0];
			Timestamp endTime3 = t3[1];
			totalAmount3 = getMonthAmount(vendorId,beginTime3+"",endTime3+"",materialId);
			
			Timestamp [] t4 = DateUtil.getBeginAndEndTimes_month(-4);
			Timestamp beginTime4 = t4[0];
			Timestamp endTime4 = t4[1];
			totalAmount4 = getMonthAmount(vendorId,beginTime4+"",endTime4+"",materialId);
			
			Timestamp [] t5 = DateUtil.getBeginAndEndTimes_month(-5);
			Timestamp beginTime5 = t5[0];
			Timestamp endTime5 = t5[1];
			totalAmount5 = getMonthAmount(vendorId,beginTime5+"",endTime5+"",materialId);
			
			Timestamp [] t6 = DateUtil.getBeginAndEndTimes_month(-6);
			Timestamp beginTime6 = t6[0];
			Timestamp endTime6 = t6[1];
			totalAmount6 = getMonthAmount(vendorId,beginTime6+"",endTime6+"",materialId);
			
			Timestamp [] t7 = DateUtil.getBeginAndEndTimes_month(-7);
			Timestamp beginTime7 = t7[0];
			Timestamp endTime7 = t7[1];
			totalAmount7 = getMonthAmount(vendorId,beginTime7+"",endTime7+"",materialId);
			
			Timestamp [] t8 = DateUtil.getBeginAndEndTimes_month(-8);
			Timestamp beginTime8 = t8[0];
			Timestamp endTime8 = t8[1];
			totalAmount8 = getMonthAmount(vendorId,beginTime8+"",endTime8+"",materialId);
			
			Timestamp [] t9 = DateUtil.getBeginAndEndTimes_month(-9);
			Timestamp beginTime9 = t9[0];
			Timestamp endTime9 = t9[1];
			totalAmount9 = getMonthAmount(vendorId,beginTime9+"",endTime9+"",materialId);
			
			Timestamp [] t10 = DateUtil.getBeginAndEndTimes_month(-10);
			Timestamp beginTime10 = t10[0];
			Timestamp endTime10 = t10[1];
			totalAmount10 = getMonthAmount(vendorId,beginTime10+"",endTime10+"",materialId);
			
			Timestamp [] t11 = DateUtil.getBeginAndEndTimes_month(-11);
			Timestamp beginTime11 = t11[0];
			Timestamp endTime11 = t11[1];
			totalAmount11 = getMonthAmount(vendorId,beginTime11+"",endTime11+"",materialId);
			double score = 0;
			if(totalAmount0 != 0){
				double count = 1;
				if(totalAmount1 != 0){
					count ++;
				}
				if(totalAmount2 != 0){
					count ++;
				}
				if(totalAmount3 != 0){
					count ++;
				}
				if(totalAmount4 != 0){
					count ++;
				}
				if(totalAmount5 != 0){
					count ++;
				}
				if(totalAmount6 != 0){
					count ++;
				}
				if(totalAmount7 != 0){
					count ++;
				}
				if(totalAmount8 != 0){
					count ++;
				}
				if(totalAmount9 != 0){
					count ++;
				}
				if(totalAmount10 != 0){
					count ++;
				}
				if(totalAmount11 != 0){
					count ++;
				}
				totalAmount = (totalAmount0 + totalAmount1 + totalAmount2 + totalAmount3 + 
						totalAmount4 + totalAmount5 + totalAmount6 + totalAmount7 + 
						totalAmount8 + totalAmount9 + totalAmount10 + totalAmount11)/count;
				double rate = amount/totalAmount;
				score = rate*1000000;
			}
			PPMEntity ppm = pPMEntityDao.getByMonthAndVendorAndTypeAndMaterial(time, vendorCode, 4, material.getCode());
			if (ppm == null){
				ppm = new PPMEntity();
			}
			ppm.setMonth(time);
			ppm.setVendor(vendor);
			ppm.setMaterial(material);
			ppm.setPpm(score);
			ppm.setPpmType(4);				// 4为售后PPM
			ppm.setAbolished(Constant.DELETE_FLAG);
			pPMEntityDao.save(ppm);
		}
	}
	
	private double getMonthAmount(Long vendorId, String beginTime, String endTime, Long materialId) {
		List<?>  list = postSalesServiceDao.getMonthAmount(vendorId, beginTime, endTime, materialId);
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
