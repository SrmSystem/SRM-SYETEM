package com.qeweb.scm.baseline.common.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.qeweb.scm.baseline.common.entity.ClassSystemEntity;
import com.qeweb.scm.baseline.common.entity.HolidaysEntity;
/**
 * 
 * 工作日历service接口
 *
 */
public interface IClassSystemService {
	
	/**
	 * 获取班制
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<ClassSystemEntity> getClassSystemList(int pageNumber, int pageSize, Map<String, Object> searchParamMap);
	
	
	/**
	 * 获取节假日
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<HolidaysEntity> getHolidaysList(int pageNumber, int pageSize, Map<String, Object> searchParamMap);
	
	
	/**
	 * 根据需要预警的用户,预警时长，班制与节假日计算出需要预警的时间
	 * @param userId
	 * @return
	 */
	public Timestamp getWarnTimeByUserId(Long userId,Double warnTime);
	
	public Page<HolidaysEntity> getHolidays(int pageNumber,int pageSize,Map<String,Object>searchParamMap,String sort,String order);
	
	public void saveHolidays(HolidaysEntity holidays);
	
	public void deleteHolidays(Long id);
	
	public Page<ClassSystemEntity> getClassSystemEntity(int pageNumber,int pageSize,Map<String,Object>searchParamMap);
	
	public ClassSystemEntity findClassSystem(Long id);
	
	public void deleteClassSystem(Long id);
	
	public void saveClassSystem(ClassSystemEntity classSystem);
	
	public ClassSystemEntity getClassSystemByUserId(Long userId);
	
	public double getWorkTimeByClassSystem(ClassSystemEntity classSystem,int week);
	
	public Timestamp countTime(Integer year,Integer month,Integer day,Double nowTime,ClassSystemEntity classSystem,Double warnTime);
	
	
	public Boolean isHoliday(Integer year,Integer month,Integer day);
	
	public List<ClassSystemEntity> findAllClassSystem( );
	
	public List<ClassSystemEntity> findEffective( );
}
