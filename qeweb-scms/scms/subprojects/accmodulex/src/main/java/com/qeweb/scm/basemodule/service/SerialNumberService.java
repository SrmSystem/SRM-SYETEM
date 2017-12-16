package com.qeweb.scm.basemodule.service;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.entity.SerialNumberEntity;
import com.qeweb.scm.basemodule.repository.SerialNumberDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;

@Component
@Transactional
public class SerialNumberService {
	private static Logger logger = LoggerFactory.getLogger(SerialNumberService.class);
	
	@Autowired
	private SerialNumberDao serialNumberDao;

	private static Map<String, SerialNumberEntity> SerialNumberBeans = new HashMap<String, SerialNumberEntity>(); 
	
	public Page<SerialNumberEntity> getSerialNumberList(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, null);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<SerialNumberEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), SerialNumberEntity.class);
		return serialNumberDao.findAll(spec,pagin);
	}
	
	public SerialNumberEntity getSerialNumberById(Long id) {
		return serialNumberDao.findOne(id);
	}
	
	/**
	 * 添加新流水规则
	 * @param serialNumber
	 */
	public void addNewSerial(SerialNumberEntity serialNumber) {
		serialNumberDao.save(serialNumber);
		SerialNumberBeans.put(serialNumber.getKey(), serialNumber); 
	} 
	
	/**
	 * 删除
	 * @param list
	 */
	public void deleteSerials(List<SerialNumberEntity> list) {
		if(CollectionUtils.isEmpty(list))
			return;
		
		for(SerialNumberEntity entity : list) {
			SerialNumberBeans.remove(entity.getKey()); 
		}
		serialNumberDao.delete(list);      
	}

	/**
	 * 修改
	 * @param serialNumber
	 */
	public void updateSerialNumber(SerialNumberEntity serialNumber) {
		SerialNumberEntity entity = serialNumberDao.findOne(serialNumber.getId());
		entity.setPrefix(serialNumber.getPrefix());
		entity.setDataString(serialNumber.getDataString());
		entity.setStartNumber(serialNumber.getStartNumber());
		entity.setDateTimeString(serialNumber.getDateTimeString());
		entity.setRepeatCycle(serialNumber.getRepeatCycle());
		entity.setIsVerify(serialNumber.getIsVerify());
		entity.setRemark(serialNumber.getRemark());
		serialNumberDao.save(entity);
		SerialNumberBeans.put(entity.getKey(), entity); 
	}
	
	/**
	 * 根据Key获取
	 * @param key
	 * @return
	 */
	@SuppressWarnings("all")  
	public SerialNumberEntity getSerialNumberEntityByKey(String key) {
		SerialNumberEntity serialNumberEntity = this.SerialNumberBeans.get(key);
		if (serialNumberEntity != null && !serialNumberEntity.getIsVerify())
			return serialNumberEntity;
		
		serialNumberEntity = serialNumberDao.getSerialNumberEntityByKey(key); 
		if(serialNumberEntity != null)
			SerialNumberBeans.put(serialNumberEntity.getKey(), serialNumberEntity);
		
		return serialNumberEntity; 
	}
	
	/**
	 * 通过数据库查询键位生成流水号
	 * 
	 * @param key
	 * @return
	 */
	@SuppressWarnings("all") 
	public synchronized String geneterNextNumberByKey(String key) {   
		logger.info("geneter nextnumber key ->" + key); 
		SerialNumberEntity serialNumber = getSerialNumberEntityByKey(key);
		if(serialNumber == null) {
			serialNumber = new SerialNumberEntity();
			serialNumber.setKey(key);
			serialNumber.setPrefix(key);
			serialNumber.setDataString(DateUtil.DATE_FORMAT_YYYYMMDD);
			//modify by chao.gu 20171017 瑞声要求序列号从1开始
			//serialNumber.setStartNumber("0000");
			serialNumber.setStartNumber("0001");
			//modify end
			serialNumber.setRepeatCycle(serialNumber.DAY);
			serialNumber.setIsVerify(true);
			serialNumber.setIsOutData(StatusConstant.STATUS_YES);  
			serialNumberDao.save(serialNumber);   
			SerialNumberBeans.put(serialNumber.getKey(), serialNumber);
		}
		return this.geneterNextNumber(serialNumber);
	} 
	
	public synchronized String geneterPHNextNumberByKey(String key) {   
		logger.info("geneter nextnumber key ->" + key); 
		SerialNumberEntity serialNumber = getSerialNumberEntityByKey(key);
		if(serialNumber == null) {
			serialNumber = new SerialNumberEntity();
			serialNumber.setKey(key);
			serialNumber.setPrefix(key);
			serialNumber.setDataString(DateUtil.DATE_FORMAT_YYYYMMDD);
			serialNumber.setStartNumber("00");
			serialNumber.setRepeatCycle(serialNumber.DAY);
			serialNumber.setIsVerify(true);
			serialNumber.setIsOutData(StatusConstant.STATUS_YES);  
			serialNumberDao.save(serialNumber);   
			SerialNumberBeans.put(serialNumber.getKey(), serialNumber);
		}
		return this.geneterNextNumber(serialNumber);
	} 
	
	/**
	 * 通过数据库查询键位生成流水号
	 * 应对自动任务中添加
	 * @param key
	 * @return
	 */
	@SuppressWarnings("all") 
	public synchronized String geneterNextNumberByKeyIsOutData(String key) {   
		logger.info("geneter nextnumber key ->" + key); 
		SerialNumberEntity serialNumber = getSerialNumberEntityByKey(key);
		if(serialNumber == null) {
			serialNumber = new SerialNumberEntity();
			serialNumber.setKey(key);
			serialNumber.setPrefix(key);
			serialNumber.setDataString(DateUtil.DATE_FORMAT_YYYYMMDD);
			serialNumber.setStartNumber("0000");
			serialNumber.setRepeatCycle(serialNumber.DAY);
			serialNumber.setIsVerify(false);
			serialNumber.setIsOutData(StatusConstant.STATUS_YES);
			serialNumber.setAbolished(0);
			serialNumberDao.save(serialNumber);   
			SerialNumberBeans.put(serialNumber.getKey(), serialNumber);
		}
		return this.geneterNextNumber(serialNumber);
	} 

	/**
	 * 通过封装类生成流水号
	 * 
	 * @param serialNumber
	 * @return
	 */
	public synchronized String geneterNextNumber(SerialNumberEntity serialNumber) {
		String nextNumber = this.geneterNextNumber(isRestart(serialNumber), serialNumber.getStartNumber());
		serialNumber.setStartNumber(nextNumber);
		String dataString = this.geneterDataString(serialNumber.getDataString());
		serialNumber.setDateTimeString(dataString);
		serialNumberDao.save(serialNumber);   
		return serialNumber.getPrefix() + dataString + nextNumber; 
	}

	private synchronized Boolean isRestart(SerialNumberEntity serialNumber) {
		GregorianCalendar gcNow = new GregorianCalendar();
		GregorianCalendar date = serialNumber.getDateTime();
		if (StringUtils.equalsIgnoreCase(SerialNumberEntity.YEAR, serialNumber.getRepeatCycle())) {
			if (gcNow.get(GregorianCalendar.YEAR) == date.get(GregorianCalendar.YEAR))
				return false;
		}
		if (StringUtils.equalsIgnoreCase(SerialNumberEntity.MONTH, serialNumber.getRepeatCycle())) {
			if (gcNow.get(GregorianCalendar.YEAR) == date.get(GregorianCalendar.YEAR)
					&& gcNow.get(GregorianCalendar.MONTH) == date.get(GregorianCalendar.MONTH))
				return false;
		}
		if (StringUtils.equalsIgnoreCase(SerialNumberEntity.DAY, serialNumber.getRepeatCycle())) {
			if (DateUtils.isSameDay(gcNow, date))
				return false;
		}
		return true; 
	}

	/**
	 * 通过开始数字字符串生成下一个流水号
	 * @param startNumber
	 * @return
	 */
	public synchronized String geneterNextNumber(Boolean isRestart, String startNumber) {
		Long temp = Long.valueOf(startNumber) + 1;
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < startNumber.length(); i++){
			sb.append("0");
		}
		DecimalFormat df = new DecimalFormat(sb.toString());
		//modify by chao.gu 20171017 流水号从1开始
		if(isRestart){
			return  df.format(1);
		}else{
			return  df.format(temp);
		}
		//return isRestart ? sb.toString() : df.format(temp);
		//modify end
	}

	/**
	 * 通过 格式生成日期格式
	 * @param dataformat
	 * @return
	 */
	private synchronized String geneterDataString(String dataformat) {
		return DateUtil.dateToString(new Date(), dataformat == null ? DateUtil.DATE_FORMAT_YYYYMMDD : dataformat);
	}

	@SuppressWarnings("all")
	public String geneterNextPCNumberByKey(String key) {
		logger.info("geneter nextnumber key ->" + key); 
		SerialNumberEntity serialNumber = getSerialNumberEntityByKey(key);
		if(serialNumber == null) {
			serialNumber = new SerialNumberEntity();
			serialNumber.setKey(key);
			serialNumber.setPrefix(key);
			serialNumber.setDataString(DateUtil.DATE_FORMAT_YYYYMMDD);
			serialNumber.setStartNumber("000000");
			serialNumber.setRepeatCycle(serialNumber.DAY);
			serialNumber.setIsVerify(true);
			serialNumber.setIsOutData(StatusConstant.STATUS_YES);  
			serialNumberDao.save(serialNumber);   
			SerialNumberBeans.put(serialNumber.getKey(), serialNumber);
		}
		return this.geneterNextNumber(serialNumber);
	}

}
