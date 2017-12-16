package com.qeweb.scm.vendorperformancemodule.service;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.BussinessRangeEntity;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialTypeEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.BussinessRangeDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialTypeDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.vendormodule.repository.VendorBaseInfoDao;
import com.qeweb.scm.vendorperformancemodule.constants.PerformanceProConstant;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforCycleEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInDetailsEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforDataInEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforFormulasEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforIndexEntity;
import com.qeweb.scm.vendorperformancemodule.entity.VendorPerforScoresEntity;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforCycleDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDataInDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforDataInDetailsDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforFormulasDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforIndexDao;
import com.qeweb.scm.vendorperformancemodule.repository.VendorPerforScoresDao;
import com.qeweb.scm.vendorperformancemodule.vo.VendorPerforDataInVo;

@Service
@Transactional
public class VendorPerforDataInService {
	
	@Autowired
	private VendorPerforDataInDao dateInDao;
	@Autowired
	private VendorBaseInfoDao baseInfoDao;
	@Autowired
	private MaterialTypeDao materialTypeDao;
	@Autowired
	private BussinessRangeDao bussinessRangeDao;
	@Autowired
	private VendorPerforIndexDao indexDao;
	@Autowired
	private VendorPerforFormulasDao formulasDao;
	@Autowired
	private VendorPerforCycleDao cycleDao;
	@Autowired
	private FactoryDao factoryDao;
	@Autowired
	private VendorPerforScoresDao vendorPerforScoresDao;
	@Autowired
	private VendorPerforDataInDetailsDao vendorPerforDataInDetailsDao;
	@Autowired
	private OrganizationDao orgDao;

	public Page<VendorPerforDataInEntity> getVendorPerforDateInList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<VendorPerforDataInEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), VendorPerforDataInEntity.class);
		Page<VendorPerforDataInEntity>  page= dateInDao.findAll(spec,pagin);
		return page;
	}
	public VendorPerforCycleEntity getVendorPerforCycleEntity(Long cycleId)
	{
		return cycleDao.findOne(cycleId);
	}
	public boolean combine(List<VendorPerforDataInVo> list, ILogger logger,VendorPerforCycleEntity vendorCycleEntity) throws RuntimeException, Exception {
		List<VendorPerforDataInEntity> vendorPerforDateInEntities=new ArrayList<VendorPerforDataInEntity>();
		String s=(String) juectDataInDate(logger, vendorCycleEntity, DateUtil.getCurrentTimestamp(), 0);
		VendorPerforScoresEntity  scoresEntity=vendorPerforScoresDao.findByCycleIdAndAssessEndDateIsNull(vendorCycleEntity.getId());
		if(scoresEntity==null)
		{
			logger.log("------>找不到所对应的评估列表");
			return false;
		}
		int index=1;
		OrganizationEntity org = null;
		BussinessRangeEntity br=null;
		FactoryEntity fr=null;
		int level = Integer.parseInt(PropertiesUtil.getProperty(PerformanceProConstant.PERFORMANCE_LEVEL, "0"));
		for(VendorPerforDataInVo vo:list)
		{
			
			VendorPerforDataInEntity vdi=new VendorPerforDataInEntity();
			vdi.setCycleId(vendorCycleEntity.getId());
			index++;
			//指标名称
			VendorPerforIndexEntity indexEntity=indexDao.findByIndexName(vo.getIndexName());
			if(indexEntity==null)
			{
				logger.log("->[FAILED]行索引[" + index + "],指标名称[" + vo.getIndexName() + "]未在系统中维护");
				return false;
			}
			vdi.setIndexName(indexEntity.getIndexName());
			//验证供应商
			org = orgDao.findValidByCode(vo.getVendorCode());
			if(null==org)
			{
				logger.log("->[FAILED]行索引[" + index + "],供应商代码[" + vo.getVendorCode() + "]未在系统中维护");
				return false;
			}
			vdi.setVendorCode(vo.getVendorCode());
			vdi.setVendorName(org.getName());
			if(level>0){
			List<MaterialTypeEntity> ms=materialTypeDao.findByCode(vo.getMaterialCode());
			//验证物料
			if(null==ms||ms.size()!=1)
			{
				logger.log("->[FAILED]行索引[" + index + "],物料类别代码[" + vo.getMaterialCode() + "]未在系统中维护");
				return false;
			}
			vdi.setMaterialCode(vo.getMaterialCode());
			vdi.setMaterialName(ms.get(0).getName());
			if(level>1){
				//验证工厂
				fr=factoryDao.findByNameAndAbolished(vo.getFactoryName(),StatusConstant.STATUS_NO);
				if(fr==null)
				{
					logger.log("->[FAILED]行索引[" + index + "],工厂名称[" + vo.getFactoryName() + "]未在系统中维护");
					return false;
				}
				vdi.setFactoryName(vo.getFactoryName());
				
				//验证品牌
				br=bussinessRangeDao.findByNameAndBussinessTypeAndAbolished(vo.getCol1(), 2,StatusConstant.STATUS_YES);
				if(br==null)
				{
					logger.log("->[FAILED]行索引[" + index + "],品牌名称[" + vo.getCol1() + "]未在系统中维护");
					return false;
				}
				vdi.setBrandName(vo.getCol1());
			}
			}
			int colIndex=1;
			colIndex++;
			//要素
			VendorPerforFormulasEntity formulasEntity=formulasDao.findByIndexIdAndCycleId(indexEntity.getId(),vendorCycleEntity.getId());
			String formulas=formulasEntity.getContent();
	        String []sa=formulas.split("\\[");
	        int ikdr=0;
	        for(int i=0;i<sa.length-1;i++)
	        {
	        	String elements=getStringMethod(vo,colIndex);
	        	if(elements!=null)
	        	{
	        		if(formulas.indexOf("["+elements+"]")!=-1)
	        		{
	        			VendorPerforDataInEntity vdii=new VendorPerforDataInEntity();
	        			PropertyUtils.copyProperties(vdii, vdi);
	        			vdii.setElement(elements);
						colIndex++;
						vdii.setElementValue(getStringMethod(vo,colIndex));
						colIndex++;
						vdii.setBelongDate(DateUtil.stringToTimestamp(s));
						vdii.setAssessDate(scoresEntity.getAssessStartDate());
						VendorPerforDataInEntity vdiii=dateInDao.findByCycleIdAndIndexNameAndVendorCodeAndMaterialCodeAndBrandNameAndFactoryNameAndBelongDateAndElementAndAssessDate(vdii.getCycleId(),vdii.getIndexName(),vdii.getVendorCode(),vdii.getMaterialCode(),vdii.getBrandName(),vdii.getFactoryName(),vdii.getBelongDate(),vdii.getElement(),vdii.getAssessDate());
						if(vdiii!=null)
						{
							dateInDao.delete(vdiii.getId());
						}
						
						vendorPerforDateInEntities.add(vdii);
						ikdr++;
	        		}
	        		else
	        		{
	        			logger.log("->[FAILED]行索引[" + index + "],要素[" + elements + "]未在系统中维护");
	    				return false;
	        		}
	        	}
	        	else
	        	{
	        		break;
	        	}
	        }
	        if(ikdr==0)
	        {
	        	logger.log("->[FAILED]行索引[" + index + "],要素未填写");
				return false;
	        }
	        
		}
		
		dateInDao.save(vendorPerforDateInEntities);
		List<Object[]>  dataInEntities=dateInDao.getVendorPerforDataInEntity(scoresEntity.getAssessStartDate());
		for(Object[] object:dataInEntities)
		{
			VendorPerforDataInEntity vendorPerforDataInEntity=new VendorPerforDataInEntity();
			vendorPerforDataInEntity.setVendorCode(""+object[0]);
			vendorPerforDataInEntity.setCycleId(Long.parseLong(""+object[1]));
			vendorPerforDataInEntity.setMaterialCode(""+object[2]);
			vendorPerforDataInEntity.setIndexName(""+object[3]);
			vendorPerforDataInEntity.setFactoryName(""+object[4]);
			vendorPerforDataInEntity.setBelongDate((Timestamp) object[5]);
			vendorPerforDataInEntity.setBrandName(""+ object[6]);
			vendorPerforDataInEntity.setAssessDate((Timestamp) object[7]);
			vendorPerforDataInEntity.setVendorName(""+ object[8]);
			vendorPerforDataInEntity.setMaterialName(""+ object[9]);
			VendorPerforDataInDetailsEntity vendorPerforDataInDetailsEntity=vendorPerforDataInDetailsDao.findByCycleIdAndIndexNameAndVendorCodeAndMaterialCodeAndBrandNameAndFactoryNameAndBelongDateAndAssessDate(vendorPerforDataInEntity.getCycleId(), vendorPerforDataInEntity.getIndexName(), vendorPerforDataInEntity.getVendorCode(), vendorPerforDataInEntity.getMaterialCode(), vendorPerforDataInEntity.getBrandName(), vendorPerforDataInEntity.getFactoryName(), vendorPerforDataInEntity.getBelongDate(),vendorPerforDataInEntity.getAssessDate());
			
			VendorPerforIndexEntity indexEntity=indexDao.findByIndexName(vendorPerforDataInEntity.getIndexName());
			VendorPerforFormulasEntity formulasEntity=formulasDao.findByIndexIdAndCycleId(indexEntity.getId(),vendorCycleEntity.getId());
			String formulas=formulasEntity.getContent();
	        String []sa=formulas.split("\\[");
			Integer mustImportElements=sa.length-1;
			List<VendorPerforDataInEntity> li=dateInDao.findByCycleIdAndIndexNameAndVendorCodeAndMaterialCodeAndBrandNameAndFactoryNameAndBelongDateAndAssessDate(vendorPerforDataInEntity.getCycleId(),vendorPerforDataInEntity.getIndexName(),vendorPerforDataInEntity.getVendorCode(),vendorPerforDataInEntity.getMaterialCode(),vendorPerforDataInEntity.getBrandName(),vendorPerforDataInEntity.getFactoryName(),vendorPerforDataInEntity.getBelongDate(),vendorPerforDataInEntity.getAssessDate());
			Integer noImportElements=mustImportElements-li.size();
			for(VendorPerforDataInEntity vendorPerforDataInEntity2:li)
			{
				String elements=vendorPerforDataInEntity2.getElement();
				formulas=formulas.replace("["+elements+"]", "");
			}
			char[] formulasChar=formulas.toCharArray();
			int j=0;
			String formulasString="";
			for(int i=0;i<formulasChar.length;i++)
			{
				if(formulasChar[i]=='[')
				{
					j=1;
					i++;
				}
				if(formulasChar[i]==']')
				{
					j=0;
					formulasString=formulasString+",";
				}
				if(j==1) {
					formulasString=formulasString+formulasChar[i];
				}
			}
			if(vendorPerforDataInDetailsEntity==null)
			{
				
				vendorPerforDataInDetailsEntity=new VendorPerforDataInDetailsEntity();
				vendorPerforDataInDetailsEntity.setCycleId(vendorPerforDataInEntity.getCycleId());
				vendorPerforDataInDetailsEntity.setIndexName(vendorPerforDataInEntity.getIndexName());
				vendorPerforDataInDetailsEntity.setVendorCode(vendorPerforDataInEntity.getVendorCode());
				vendorPerforDataInDetailsEntity.setVendorName(vendorPerforDataInEntity.getVendorName());
				vendorPerforDataInDetailsEntity.setBrandName(vendorPerforDataInEntity.getBrandName());
				vendorPerforDataInDetailsEntity.setMaterialCode(vendorPerforDataInEntity.getMaterialCode());
				vendorPerforDataInDetailsEntity.setMaterialName(vendorPerforDataInEntity.getMaterialName());
				vendorPerforDataInDetailsEntity.setFactoryName(vendorPerforDataInEntity.getFactoryName());
				vendorPerforDataInDetailsEntity.setBelongDate(vendorPerforDataInEntity.getBelongDate());
				vendorPerforDataInDetailsEntity.setAssessDate(vendorPerforDataInEntity.getAssessDate());
				vendorPerforDataInDetailsEntity.setDimensionsName(indexEntity.getDimensionsEntity().getDimName());
				vendorPerforDataInDetailsEntity.setMustImportElements(mustImportElements);
				vendorPerforDataInDetailsEntity.setNoImportElements(noImportElements);
				vendorPerforDataInDetailsEntity.setCycleName(formulasEntity.getCycleEntity().getCycleName());
				vendorPerforDataInDetailsEntity.setNoImportElementsName(formulasString);
			}
			else
			{
				vendorPerforDataInDetailsEntity.setMustImportElements(mustImportElements);
				vendorPerforDataInDetailsEntity.setNoImportElements(noImportElements);
			}
			vendorPerforDataInDetailsDao.save(vendorPerforDataInDetailsEntity);
		}
		return true;
	}

	public Object juectDataInDate(ILogger logger, VendorPerforCycleEntity vendorCycleEntity,Timestamp timestamp,int i) throws Exception {
		String cycString=vendorCycleEntity.getCycleName();
		String dats=new String();
		if("month".equals(cycString))
		{
			Timestamp[]  old=DateUtil.getBeginAndEndTimes_month(-1,null);//上个月
			Date newDate= DateUtil.addDay(old[1], vendorCycleEntity.getInitDates());
			dats=DateUtil.dateToString(old[0]);
			if(i==1)
			{
				if(newDate.getTime()<timestamp.getTime())
				{
					logger.log("------------------->周期：月,不在导入时间内<---------------------");
					return false;
				}
			}
			if(i==2)
			{
				if(newDate.getTime()<timestamp.getTime())
				{
					return false;
				}
			}
		}
		else if("quarter".equals(cycString))
		{
			Timestamp[]  old=DateUtil.getBeginAndEndTimes_quarter(-1,null);//上个季度
			Date newDate= DateUtil.addDay(old[1], vendorCycleEntity.getInitDates());
			dats=DateUtil.dateToString(old[0]);
			if(i==1)
			{
				if(newDate.getTime()<timestamp.getTime())
				{
					
					logger.log("------------------->周期：季度,不在导入时间内<---------------------");
					return false;
				}
			}
			if(i==2)
			{
				if(newDate.getTime()<timestamp.getTime())
				{
					return false;
				}
			}
		}
		else if("halfyear".equals(cycString))
		{
			Timestamp[]  old=DateUtil.getBeginAndEndTimes_halfYear(-1,null);//上个半年
			Date newDate= DateUtil.addDay(old[1], vendorCycleEntity.getInitDates());
			dats=DateUtil.dateToString(old[0]);
			if(i==1)
			{
				if(newDate.getTime()<timestamp.getTime())
				{
					logger.log("------------------->周期：半年,不在导入时间内<---------------------");
					return false;
				}
			}
			if(i==2)
			{
				if(newDate.getTime()<timestamp.getTime())
				{
					return false;
				}
			}
		}
		else if("year".equals(cycString))
		{
			Timestamp[]  old=DateUtil.getBeginAndEndTimes_year(-1,null);//上个年
			Date newDate= DateUtil.addDay(old[1], vendorCycleEntity.getInitDates());
			dats=DateUtil.dateToString(old[0]);
			if(i==1)
			{
				if(newDate.getTime()<new Date().getTime())
				{
					logger.log("------------------->周期：年,不在导入时间内<---------------------");
					return false;
				}
			}
			if(i==2)
			{
				if(newDate.getTime()<timestamp.getTime())
				{
					return false;
				}
			}
		}
		if(i==1||i==2)
		{
			return true;
		}
		else
		{
			return dats;
		}
	}

	private String getStringMethod(VendorPerforDataInVo vo, int colIndex)
	{
		Method mk;
		try {
			mk = vo.getClass().getMethod("getCol"+colIndex);
			return (String)mk.invoke(vo);
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, Object> update(String useid, String elementValue) {
		Map<String, Object> map =new HashMap<String, Object>();
		VendorPerforDataInEntity vendorPerforDateInEntitie=dateInDao.findOne(Long.parseLong(useid));
		vendorPerforDateInEntitie.setElementValue(elementValue);
		dateInDao.save(vendorPerforDateInEntitie);
		map.put("success", true);
		map.put("msg", "更新成功");
		return map;
	}

	public String getVendorPerforCycle() {
		List<VendorPerforCycleEntity> iterable=(List<VendorPerforCycleEntity>) cycleDao.findAll();
		String data="[";
		int i=0;
		for(VendorPerforCycleEntity vendorCycleEntity:iterable){
			if(i>0)
			{
				data=data+",";
			}
			data=data+"{\"id\":\""+vendorCycleEntity.getId()+"\",\"text\":\""+vendorCycleEntity.getCycleName()+"\"}";
					i++;
		}
		data=data+"]";
		return data; 
	}
}
