package com.qeweb.scm.baseline.common.service;

import java.sql.Timestamp;
import java.util.Calendar;
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
import com.qeweb.scm.baseline.common.entity.ClassSystemEntity;
import com.qeweb.scm.baseline.common.entity.HolidaysEntity;
import com.qeweb.scm.baseline.common.repository.ClassSystemDao;
import com.qeweb.scm.baseline.common.repository.HolidaysDao;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;

/**
 * 
 * 工作日历service
 *
 */
@Service
@Transactional
public class ClassSystemService  implements IClassSystemService{
	
	@Autowired
	private ClassSystemDao classSystemDao;
	
	@Autowired
	private HolidaysDao holidaysDao;
	
	@Autowired
	private UserDao userDao;


	@Override
	public List<ClassSystemEntity> getClassSystemList(int pageNumber,
			int pageSize, Map<String, Object> searchParamMap) {
		return null;
	}

	@Override
	public List<HolidaysEntity> getHolidaysList(int pageNumber, int pageSize,
			Map<String, Object> searchParamMap) {
		return null;
	}
	
	public Page<HolidaysEntity> getHolidays(int pageNumber, int pageSize,Map<String, Object> searchParamMap,String sort,String order){
//		PageRequest pagin = new PageRequest(pageNumber-1, pageSize);
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, sort,order);//增加排序功能
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<HolidaysEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), HolidaysEntity.class);
		return holidaysDao.findAll(spec,pagin);
	}
	public Page<ClassSystemEntity> getClassSystemEntity(int pageNumber, int pageSize,
			Map<String, Object> searchParamMap){
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ClassSystemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ClassSystemEntity.class);
		return classSystemDao.findAll(spec,pagin);
	}
	
	
	
	public List<ClassSystemEntity> findAllClassSystem( ){
		return classSystemDao.findAll();
	}
	
	public List<ClassSystemEntity> findEffective( ){
		return classSystemDao.findEffective();
	}
	
	public void saveHolidays(HolidaysEntity holidays){
		holidaysDao.save(holidays);
	}
	
	public HolidaysEntity findHolidays(Long id){
		return holidaysDao.findOne(id);
	}
	
	public ClassSystemEntity findClassSystem(Long id){
		return classSystemDao.findOne(id);
	}
	public void deleteHolidays(Long id){
		//holidaysDao.delete(id);
		holidaysDao.abolish(id);
	}
	public void deleteClassSystem(Long id){
		//classSystemDao.delete(id);
		classSystemDao.abolish(id);
	}
	
	public void saveClassSystem(ClassSystemEntity classSystem){
		classSystemDao.save(classSystem);
	}
	
	/**
	 * 是否节假日
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public Boolean isHoliday(Integer year,Integer month,Integer day){
		HolidaysEntity po=holidaysDao.findByYearAndMonthAndDay(year, month, day);
		return po!=null?true:false;
	}
	
	/**
	 * 获取用户的班制
	 * @param userId
	 * @return
	 */
	public ClassSystemEntity getClassSystemByUserId(Long userId){
		UserEntity user= userDao.findOne(userId);
		ClassSystemEntity classSystem = new ClassSystemEntity();
		if(user.getClassSystem() == null){
			//设置默认班别
			classSystem.setMorningStart(8.30d);
			classSystem.setMorningEnd(12.00d);
			classSystem.setAfterStart(1.30d);
			classSystem.setAfterEnd(6.00d);
			classSystem.setBillType(1);
			classSystem.setName("系统班别");
			classSystem.setRemarks("系统班别");	
		}else{
			classSystem = user.getClassSystem();
		}
		return classSystem;
	}
	
	/**
	 * 周末的算法，需要工作多少时间
	 * @param week 星期表示1-7，是从星期日开始，   
	 * @param classSystem
	 * @return
	 */
	public double getWorkTimeByClassSystem(ClassSystemEntity classSystem,int week){
		if(week==1){
			return 0;
		}else if(week==7){
			Integer billType=classSystem.getBillType();//类型  5天1   5天半2   6天3
			if(billType==1){
				return 0;
			}else if(billType==3){
				return 1;
			}else{
				return 0.5;
			}
		}else{
			return 1;
		}
	}
	
	/**
	 * 计算最终预警日期
	 * @param nowTime 当前业务操作时间
	 * @param warnTime  预警时长
	 * @return 最终预警日期
	 */
	public Timestamp countTime(Integer year,Integer month,Integer day,Double nowTime,ClassSystemEntity classSystem,Double warnTime){
		 Calendar calendar = Calendar.getInstance();//获得一个日历
		 calendar.set(year, month-1, day);//设置当前时间,月份是从0月开始计算
		 int week=calendar.get(Calendar.DAY_OF_WEEK);
		 calendar.getTimeInMillis();
		 
		Boolean isHoliday=isHoliday(year, month, day);
		if(isHoliday){
			 calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
			 year=calendar.get(Calendar.YEAR);
			 month=calendar.get(Calendar.MONTH);
			 day=calendar.get(Calendar.DATE);
			 nowTime=0d;
			 return countTime(year, month, day, nowTime, classSystem, warnTime);
		}else{
			 double workTime=getWorkTimeByClassSystem(classSystem, week);
			 if(workTime==1){
				 Double morningStart =classSystem.getMorningStart();
				 Double morningEnd =classSystem.getMorningEnd();
				 Double afterStart = classSystem.getAfterStart();
				 Double afterEnd =classSystem.getAfterEnd();
				 
				 //上午
				 if(nowTime<morningStart){
					 double t= morningEnd-morningStart;//可用时间
					 warnTime=warnTime-t;
					 if(warnTime<=0){
						 //时间够，直接返回截止时间
						return getTimestamp(year, month, day, morningStart+warnTime);
					 }
					 nowTime=afterStart;
				 }else if(nowTime<morningEnd){
					 double t=morningEnd-nowTime;
					 warnTime=warnTime-t;
					 if(warnTime<=0){
						 //时间够，直接返回截止时间
						 return getTimestamp(year, month, day, morningStart+warnTime);
					 }
					 nowTime=afterStart;
				 }
				
				
				 //下午
				 if(nowTime<afterStart){
					 double t= afterEnd-afterStart;//可用时间
					 warnTime=warnTime-t;
					 if(warnTime<=0){
						 //时间够，直接返回截止时间
						return getTimestamp(year, month, day, afterStart+warnTime);
					 }
				 }else if(nowTime<afterEnd){
					 double t=afterEnd-nowTime;
					 warnTime=warnTime-t;
					 if(warnTime<=0){
						 //时间够，直接返回截止时间
						 return getTimestamp(year, month, day, afterStart+warnTime);
					 }
				 }
				 if(warnTime>0){
					 calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
					 year=calendar.get(Calendar.YEAR);
					 month=calendar.get(Calendar.MONTH);
					 day=calendar.get(Calendar.DATE);
					 nowTime=0d;
					 return  countTime(year, month, day, nowTime, classSystem, warnTime);
				 }
	 
			 }else if(workTime==0.5){
				 Double morningStart =classSystem.getMorningStart();
				 Double morningEnd =classSystem.getMorningEnd();
				 
				 //上午
				 if(nowTime<morningStart){
					 double t= morningEnd-morningStart;//可用时间
					 warnTime=warnTime-t;
					 if(warnTime<=0){
						 //时间够，直接返回截止时间
						return getTimestamp(year, month, day, morningStart+warnTime);
					 }
				 }else if(nowTime<morningEnd){
					 double t=morningEnd-nowTime;
					 warnTime=warnTime-t;
					 if(warnTime<=0){
						 //时间够，直接返回截止时间
						 return getTimestamp(year, month, day, morningStart+warnTime);
					 }
				 }
				 if(warnTime>0){
					 calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
					 year=calendar.get(Calendar.YEAR);
					 month=calendar.get(Calendar.MONTH);
					 day=calendar.get(Calendar.DATE);
					 nowTime=0d;
					 return countTime(year, month, day, nowTime, classSystem, warnTime);
				 }
			 }else{
				 calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动 
				 year=calendar.get(Calendar.YEAR);
				 month=calendar.get(Calendar.MONTH);
				 day=calendar.get(Calendar.DATE);
				 nowTime=0d;
				 return  countTime(year, month, day, nowTime, classSystem, warnTime);
			 }
		}
		return null;
	}
	
	/**
	 * 组装字符串生成日期
	 * @param year
	 * @param month
	 * @param day
	 * @param time
	 * @return
	 */
	public Timestamp getTimestamp(Integer year,Integer month,Integer day,Double time){
		String monthStr=String.valueOf(month).length()==1?"0"+month:String.valueOf(month);
		String dayStr=String.valueOf(day).length()==1?"0"+day:String.valueOf(day);
		String[] str=String.valueOf(time).split("\\.");
		String hour=str[0].length()==1?"0"+str[0]:str[0];
		String min=str[1].equals("0")?"00":"30";
		return DateUtil.stringToTimestamp(year+"-"+monthStr+"-"+dayStr+" "+hour+":"+min+":"+"00", "yyyy-MM-dd HH:mm:ss");
	}

	@Override
	public Timestamp getWarnTimeByUserId(Long userId, Double warnTime) {
		 Calendar calendar = Calendar.getInstance();//获得一个日历
		 calendar.setTime(DateUtil.getCurrentTimestamp());
		Integer year=calendar.get(Calendar.YEAR);
		Integer month=calendar.get(Calendar.MONTH)+1;
		Integer day=calendar.get(Calendar.DATE);
		Integer hour=calendar.get(Calendar.HOUR_OF_DAY);
		Integer min=calendar.get(Calendar.MINUTE);
		Double time=Double.valueOf(hour+"");
		//当前时间精确到半小时，15-44分算半小时，小于15算0，超过45进1
		if(min<15){
		}else if(min<45){
			time=time+0.5;
		}else{
			time++;
		}
		return countTime(year, month, day, time, getClassSystemByUserId(userId), warnTime);
	}
	

	
	
	
}
