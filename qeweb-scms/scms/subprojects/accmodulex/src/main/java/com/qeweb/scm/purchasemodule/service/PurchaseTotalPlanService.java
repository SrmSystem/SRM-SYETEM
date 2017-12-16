package com.qeweb.scm.purchasemodule.service;


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.service.SerialNumberService;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.PageUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanHeadEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanHeadEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseTotalPlanItemEntity;
import com.qeweb.scm.purchasemodule.repository.PurchaseTotalPlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseTotalPlanHeadDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseTotalPlanItemDao;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanItemHeadVO;
import com.qeweb.scm.purchasemodule.web.vo.PurchasePlanTransfer;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseTotalPlanTransfer;
import com.qeweb.scm.vendormodule.entity.VendorMaterialRelEntity;
import com.qeweb.scm.vendormodule.repository.VendorMaterialRelDao;


/**
 * 预测计划总量的service
 */
@Service
@Transactional
public class PurchaseTotalPlanService {

	@Autowired
	private PurchaseTotalPlanDao purchaseTotalPlanDao;
	
	@Autowired
	private OrganizationDao  organizationDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private PurchaseTotalPlanItemDao purchaseTotalPlanItemDao;
	
	@Autowired
	private PurchaseTotalPlanHeadDao purchaseTotalPlanHeadDao;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private SerialNumberService serialNumberService;
	
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	@Autowired
	private VendorMaterialRelDao vendorMaterialRelDao;

	public Page<PurchaseTotalPlanEntity> getPurchaseTotalPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseTotalPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseTotalPlanEntity.class);
		return purchaseTotalPlanDao.findAll(spec,pagin);
	}
	
	public Page<PurchaseTotalPlanItemEntity> getPurchasePlanItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) throws Exception {
		
		PageRequest pagin = PageUtil.buildPageRequest(pageNumber, pageSize, "auto");
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseTotalPlanItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseTotalPlanItemEntity.class);
		Page<PurchaseTotalPlanItemEntity> page = purchaseTotalPlanItemDao.findAll(spec,pagin);
		List<PurchaseTotalPlanItemEntity> purchasePlanItemList=page.getContent();
		
/*		PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, null);
		
		StringBuffer sql=new StringBuffer();
		sql.append(" FROM");
		sql.append(" QEWEB_PURCHASE_TOTAL_PLAN_ITEM A");
		sql.append(" LEFT JOIN QEWEB_USER B ON A.BUYER_ID = B. ID");
		sql.append(" LEFT JOIN QEWEB_MATERIAL C ON A.MATERIAL_ID = C. ID  ");
		sql.append(" LEFT JOIN QEWEB_FACTORY D ON A.FACTORY_ID = D. ID  ");
		sql.append(" WHERE ");
		sql.append(" A.ABOLISHED = 0");
		sql.append("AND  A.PLAN_ID = "+searchParamMap.get("EQ_plan.id")+"");
		if(searchParamMap.containsKey("LIKE_versionNumber")){
			if(!StringUtils.isEmpty(searchParamMap.get("LIKE_versionNumber").toString())){
				sql.append(" AND A.VERSION_NUMBER LIKE '%"+searchParamMap.get("LIKE_versionNumber")+"%'");//版本号  
			}
		}
		if(searchParamMap.containsKey("EQ_isNew")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_isNew").toString())){
				sql.append(" AND A.IS_NEW = '"+searchParamMap.get("EQ_isNew")+"'");//有效状态
			}
		}else{
			sql.append(" AND A.IS_NEW = 1");//有效状态
		}
		if(searchParamMap.containsKey("LIKE_factoryName")){
			if(!StringUtils.isEmpty(searchParamMap.get("LIKE_factoryName").toString())){
				sql.append(" AND D.NAME like '%"+searchParamMap.get("LIKE_factoryName")+"%'");//工厂
			}
		}
		
		if(searchParamMap.containsKey("EQ_materialCode")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_materialCode").toString())){
				sql.append(" AND C.CODE = '"+searchParamMap.get("EQ_materialCode")+"'");//物料号
			}
			
		}
		if(searchParamMap.containsKey("EQ_factoryCode")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_factoryCode").toString())){
				sql.append(" AND D.CODE = '"+searchParamMap.get("EQ_factoryCode")+"'");//工厂	
			}
			
		}
		sql.append(" ORDER BY  ");
		sql.append(" A.CREATE_TIME desc");
		String sql1="SELECT A.* "+sql.toString();
		String sqlTotalCount="select count(1) "+sql.toString();
		List<?> list = generialDao.querybysql(sql1,pagin);
		List<PurchaseTotalPlanItemEntity> purchasePlanItemList=new ArrayList<PurchaseTotalPlanItemEntity>();
		int totalCount=generialDao.findCountBySql(sqlTotalCount);
		if(null!=list && list.size()>0){
			for(Object purchaseTotalPlanItem : list){
				Map<String,Object> m =  (Map<String, Object>) purchaseTotalPlanItem;
				PurchaseTotalPlanItemEntity entity=purchaseTotalPlanItemDao.findById(((BigDecimal)m.get("ID")).longValue());
				if(entity != null){
					purchasePlanItemList.add(entity);
				}
				
			}
		}*/
		
		//获取选择大版本的第一个星期一
		PurchaseTotalPlanEntity purchaseTotalPlan = new PurchaseTotalPlanEntity();
		purchaseTotalPlan = purchaseTotalPlanDao.findOne(Long.parseLong(searchParamMap.get("EQ_plan.id").toString()));
    	String version = purchaseTotalPlan.getMonth();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	
/*    	//过滤查询条件（时间）
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		if(searchParamMap.containsKey("EQ_startDate") && searchParamMap.get("EQ_startDate") != null && searchParamMap.get("EQ_startDate") != ""){
    		start = sdf.parse(searchParamMap.get("EQ_startDate").toString());  
    	}else{
    		start= sdf.parse("0000-00-00"); 
    	}
        if(searchParamMap.containsKey("EQ_endDate") && searchParamMap.get("EQ_endDate") != null && searchParamMap.get("EQ_endDate") != "" ){
        	end   = sdf.parse(searchParamMap.get("EQ_endDate").toString());  
    	}else{
    		end   = sdf.parse("9999-99-99"); 
    	}*/
    	
        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO purchasePlanItemHeadVO = new PurchasePlanItemHeadVO();    	
		Class clazz = purchasePlanItemHeadVO.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
       /*     if(start.getTime() <=  date.getTime()  &&  date.getTime() <=  end.getTime() ){*/
            	 if(field.getType().getSimpleName().equals("String")){
                     method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
                 }
       /*     }*/
            date=getSeventDay(date);
        }
		


		//重新加载list
		List<PurchaseTotalPlanItemEntity> purchaseTotalPlanItemList=new ArrayList<PurchaseTotalPlanItemEntity>();
		for(PurchaseTotalPlanItemEntity purchaseTotalPlanItem : purchasePlanItemList){
			
			purchasePlanItemHeadVO =(PurchasePlanItemHeadVO) object;
			
			
			PurchasePlanItemHeadVO tmpPurchasePlanItemHeadVO =purchasePlanItemHeadVO ;
			Class clazz1 = tmpPurchasePlanItemHeadVO.getClass();  
			Object object1 = clazz1.newInstance();
			Field[] fields1 = clazz1.getDeclaredFields();  
			
			PurchaseTotalPlanItemEntity item = purchaseTotalPlanItem;
			List<PurchaseTotalPlanHeadEntity>   headList =    purchaseTotalPlanHeadDao.findPurchasePlanHeadByplanItemId(purchaseTotalPlanItem.getId());
			tmpPurchasePlanItemHeadVO = purchasePlanItemHeadVO;
			for(PurchaseTotalPlanHeadEntity purchaseTotalPlanHead : headList ){	
			    for(int i = 0 ; i < fields1.length; i++){ 
			    	Field f1 = fields1[i];
			    	f1.setAccessible(true); //设置些属性是可以访问的  
			        Object val1 = f1.get(tmpPurchasePlanItemHeadVO);//得到此属性的值  
			        if(purchaseTotalPlanHead.getHeaderName().equals(val1)){
		            	 String name = f1.getName();
				         String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
				         Method method = clazz1.getMethod(methodStr,new Class[]{f1.getType()});
		            	 method.invoke(object1,purchaseTotalPlanHead.getHeaderValues());
		            	 break;
					}
			    }	
			}
			PurchasePlanItemHeadVO tempVo = new PurchasePlanItemHeadVO();
		
			BeanUtil.copyPropertyNotNull((PurchasePlanItemHeadVO) object1,tempVo);

			item.setPurchasePlanItemHeadVO(tempVo);
			item.setHeadVO(purchasePlanItemHeadVO);
			purchaseTotalPlanItemList.add(item);
		}
 		return page;	
	}
	
	//表头数据
	public PurchasePlanItemHeadVO getItemHead(Long id,String startDate,String endDate) throws Exception {
		//获取选择大版本的第一个星期一
    	PurchaseTotalPlanEntity purchaseTotalPlan = new PurchaseTotalPlanEntity();
    	purchaseTotalPlan = purchaseTotalPlanDao.findOne(id);
    	String version = purchaseTotalPlan.getMonth();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	
/*    	//过滤查询条件（时间）
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		if(startDate != null && startDate!="" ){
    		start = sdf.parse(startDate);  
    	}else{
    		start= sdf.parse("0000-00-00"); 
    	}
        if(endDate != null && endDate !="" ){
        	end   = sdf.parse(endDate);  
    	}else{
    		end   = sdf.parse("9999-99-99"); 
    	}*/
    	
        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO purchasePlanItemHeadVO = new PurchasePlanItemHeadVO();    	
		Class clazz = purchasePlanItemHeadVO.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
        /*    if(start.getTime() <=  date.getTime()  &&  date.getTime() <=  end.getTime() ){*/
            	 if(field.getType().getSimpleName().equals("String")){
                     method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
                 }
        /*    }*/
            date=getSeventDay(date);
        }
		
		purchasePlanItemHeadVO =(PurchasePlanItemHeadVO) object;
		
		return purchasePlanItemHeadVO;
	}
	
	
	/**
	 * 通过版本号和计划员的id获取预计总量的头
	 * @param month
	 * @param buyerCode
	 * @return
	 */
	protected PurchaseTotalPlanEntity getPurchaseTotalPlanEntity(String month, Long createId) {  
		PurchaseTotalPlanEntity planTotal = purchaseTotalPlanDao.findPurchasePlanEntityByMonthAndCreateUserId(month, createId);
		if(planTotal == null) {
			return new PurchaseTotalPlanEntity();
		}
		return planTotal;
	}
	
   protected PurchaseTotalPlanItemEntity getPurchaseTotalPlanItemEntity(PurchaseTotalPlanEntity purchaseTotalPlan, PurchaseTotalPlanTransfer trans) {
		if(purchaseTotalPlan.getId() == 0l) {
			return new PurchaseTotalPlanItemEntity();
		}
		PurchaseTotalPlanItemEntity planItem = purchaseTotalPlanItemDao.findPurchaseTotalPlanItemEntityByMainId(purchaseTotalPlan.getId(),trans.getFactoryCode(),trans.getMaterialCode(),trans.getGroupCode());
		return planItem == null ? new PurchaseTotalPlanItemEntity() : planItem;
   }
	

	/**
	 * 转换并保存
	 * @param planTotalTranList
	 * @param logger
	 * @throws Exception
	 */
	public boolean combinePurchasePlan(List<PurchaseTotalPlanTransfer> planTranList, ILogger logger) throws Exception {	
		
		//工厂
		Map<String, FactoryEntity> factoryMap = new HashMap<String, FactoryEntity>();
		
		//采购组
		Map<String, PurchasingGroupEntity> groupMap = new HashMap<String, PurchasingGroupEntity>();

		//物料
		Map<String, MaterialEntity> materialMap = new HashMap<String, MaterialEntity>();
		
		
		//验证当前数据
		Integer[] counts = {0,0};	//总记录数， 验证通过数
		List<PurchaseTotalPlanTransfer> list = validateTransfers(planTranList,factoryMap,groupMap,materialMap ,counts, logger);
		if(list.size() != planTranList.size()) {
			int listCount = list.size() - 1;
			int planCount = planTranList.size() - 1;
			logger.log("-> <b><font color='red'> 导入的预测计划总量失败！有效数据条数["+listCount+"]与导入数据条数["+planCount+"]不匹配！请修改预测计划总量！</font></b>");
			return false;
		}
		
		//验证当前数据是否重复
		boolean flag =  checkDataSource(list,logger);
		if(!flag){
			return false;
		}
		//查询是否存在大版本
		Date date = new Date();
		Calendar cd = Calendar.getInstance();  
	    cd.setTime(date);
	    String month = cd.get(Calendar.YEAR)+""+ String.format("%02d", (cd.get(Calendar.MONTH) + 1));
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
	    String version ="";
	    PurchaseTotalPlanEntity plan = null;
		plan = purchaseTotalPlanDao.findPurchasePlanEntityByMonthAndCreateUserId(month,user.id);
		if(plan == null){
			//添加新的版本
			return addPurchaseTotalPlan(planTranList,month, version, factoryMap ,groupMap,materialMap,logger);
		}else{
			//修改大版本信息
			return updatePurchaseTotalPlanItemHead(planTranList,month,version,factoryMap,groupMap,materialMap,plan,logger);
		}
	}
	
	/**
	 * 新增预测计划总量数据
	 * @param plans
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Boolean addPurchaseTotalPlan(List<PurchaseTotalPlanTransfer> planTranList,String month,String version,Map<String, FactoryEntity> factoryMap ,Map<String, PurchasingGroupEntity> groupMap,	Map<String, MaterialEntity> materialMap  ,ILogger logger)  throws Exception {
 
		
		List<PurchaseTotalPlanTransfer> headList = new ArrayList<PurchaseTotalPlanTransfer>();//表头数据
		headList.add(planTranList.get(0));
		planTranList.removeAll(headList);//除去表头的数据项
		
		Boolean flag = true;

		
		for(PurchaseTotalPlanTransfer tr : planTranList){
			//判断添加的类容是否存在(版本   采购组  工厂  物料)
			PurchaseTotalPlanItemEntity item = purchaseTotalPlanItemDao.findPurchaseTotalPlanItemEntityByMonthAndFaAndMaAnGr(month,tr.getFactoryCode(),tr.getMaterialCode());
			if(item != null){
				flag =false;
				logger.log("-><b><font color='red'> [FAILED] 工厂编码[" + tr.getFactoryCode() + "],物料编码[" + tr.getMaterialCode() + "],已分配采购组[" + item.getPurchasingGroup().getCode()+ "]！请修改预测计划！<b><font color='red'>");
			}
		}

		if(!flag){
			return flag;
		}else{
			
			/*logger.log("\n->数据校验成功！正在准备新增主数据与明细数据\n");*/
			
			PurchaseTotalPlanEntity purchaseTotalPlan = new PurchaseTotalPlanEntity();//预测总量的表头
			PurchaseTotalPlanItemEntity purchaseTotalPlanItem = new PurchaseTotalPlanItemEntity();//预测总量的行项目

			List<PurchaseTotalPlanEntity> purchaseTotalPlanList = new ArrayList<PurchaseTotalPlanEntity>();
			List<PurchaseTotalPlanItemEntity> purchaseTotalPlanItemList = new ArrayList<PurchaseTotalPlanItemEntity>();
			List<PurchaseTotalPlanHeadEntity> purchaseTotalPlanHeadList = new ArrayList<PurchaseTotalPlanHeadEntity>();
			Map<String, PurchaseTotalPlanEntity> tmpMap = new HashMap<String, PurchaseTotalPlanEntity>();
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();

			String key = null;
		
			for(PurchaseTotalPlanTransfer trans : planTranList) {

				key = month + ";" +user.id ;
				if(!tmpMap.containsKey(key)) {
					purchaseTotalPlan = getPurchaseTotalPlanEntity(month,user.id);   //获取预计总量头
					purchaseTotalPlan.setMonth(month);
					tmpMap.put(key, purchaseTotalPlan);
				} else {
					purchaseTotalPlan = tmpMap.get(key);
				}
				
				//操作人+物料+工厂+采购组+key
				String versionKey = user.id+trans.getMaterialCode()+trans.getFactoryCode()+trans.getGroupCode()+"poTotalPlan"+",";
			    String  allVersion =  serialNumberService.geneterPHNextNumberByKey(versionKey);
			    String [] strs = allVersion.split(",");
				version =strs[1];
						
				//设置明细单表信息
				purchaseTotalPlanItem = getPurchaseTotalPlanItemEntity(purchaseTotalPlan, trans);  //获取预测计划总量的子项目
				purchaseTotalPlanItem.setPlan(purchaseTotalPlan);
				purchaseTotalPlanItem.setPurchasingGroup(groupMap.get(trans.getGroupCode()));//采购组
				purchaseTotalPlanItem.setFactory(factoryMap.get(trans.getFactoryCode()));//工厂
				purchaseTotalPlanItem.setMaterial(materialMap.get(trans.getMaterialCode()));//物料
				purchaseTotalPlanItem.setVersionNumber(version);
				purchaseTotalPlanItem.setIsNew(1);
				purchaseTotalPlanItem.setDayStock(trans.getInventory());
				purchaseTotalPlanItem.setUnpaidQty(trans.getDeliveredCount());
				purchaseTotalPlanItemList.add(purchaseTotalPlanItem);
				
				//设置表头信息
				PurchaseTotalPlanTransfer headerTransfer = headList.get(0);
			
				
				Class clazz1 = trans.getClass();  
				Class clazz2 = headerTransfer.getClass();
	          
				Field[] fs1 = clazz1.getDeclaredFields();  
				Field[] fs2 = clazz2.getDeclaredFields();  
		        for(int i = 5 ; i < 29; i++){ 
		        	Field f1 = fs1[i];
		        	f1.setAccessible(true); //设置些属性是可以访问的  
		            Object val1 = f1.get(trans);//得到此属性的值  
		        	for(int j = 0; j < fs2.length;j++){
		        		Field f2 = fs1[j];
		        		f2.setAccessible(true); //设置些属性是可以访问的  
			            Object val2 = f2.get(headerTransfer);//得到此属性的值  
			            if(f1.getName().equals(f2.getName())){
			            	PurchaseTotalPlanHeadEntity purchasePlanHead=new PurchaseTotalPlanHeadEntity() ;
			            	purchasePlanHead.setPlanItem(purchaseTotalPlanItem);
							purchasePlanHead.setHeaderName(val2 == null || val2 == "" ? "0" : val2.toString());
							purchasePlanHead.setHeaderValues( val1 == null || val1 == "" ? "0" : val1.toString());
							purchasePlanHead.setVersionNumber(month+"01");
							purchasePlanHead.setIsNew(1);
							purchaseTotalPlanHeadList.add(purchasePlanHead);
							break;
			            }
		        	}
		        }   	
			}
			String logMsg = "添加成功[" + tmpMap.size() + "]条有效的数据";
			logger.log(logMsg);
			//保存
			purchaseTotalPlanDao.save(tmpMap.values());
			purchaseTotalPlanItemDao.save(purchaseTotalPlanItemList);   
			purchaseTotalPlanHeadDao.save(purchaseTotalPlanHeadList);
			
			return flag;
		}

	}
	
	/**
	 * 修改预测计划总量表头表
	 * @param plans
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public Boolean updatePurchaseTotalPlanItemHead(List<PurchaseTotalPlanTransfer> planTranList,String month,String version,Map<String, FactoryEntity> factoryMap ,Map<String, PurchasingGroupEntity> groupMap,	Map<String, MaterialEntity> materialMap  ,PurchaseTotalPlanEntity  plan ,ILogger logger)  throws Exception {
		
		List<PurchaseTotalPlanTransfer> headList = new ArrayList<PurchaseTotalPlanTransfer>();//表头
		headList.add(planTranList.get(0));
		planTranList.removeAll(headList);//数据项
	
		
		List<PurchaseTotalPlanItemEntity> purchaseTotalPlanItemList = new ArrayList<PurchaseTotalPlanItemEntity>();//重新插入的
		List<PurchaseTotalPlanItemEntity> upPurchaseTotalPlanItemList = new ArrayList<PurchaseTotalPlanItemEntity>();//升级的
		//失效的list集合
		List<Long> invalidPlanItemIds = new ArrayList<Long>();
		
		//重新填入的数据项
		List<PurchaseTotalPlanTransfer> newUpdateList = new ArrayList<PurchaseTotalPlanTransfer>();//更新表头的
		List<PurchaseTotalPlanTransfer> newInsertList = new ArrayList<PurchaseTotalPlanTransfer>();//重新插入的
		List<PurchaseTotalPlanTransfer> newUpgradeList = new ArrayList<PurchaseTotalPlanTransfer>();//升级的
		
		//导入的中有上次未导入的数据（新添入的需要加入头部）
		newInsertList.addAll(headList);

		//组合新的集合插入
		Map<String, PurchaseTotalPlanHeadEntity> oldHeadMap = new HashMap<String, PurchaseTotalPlanHeadEntity>();
    	Map<String, PurchaseTotalPlanHeadEntity> newHeadMap = new HashMap<String, PurchaseTotalPlanHeadEntity>();
    	
    	ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		//插入新的list和作废老版本
		for(PurchaseTotalPlanTransfer trans : planTranList) {
			
			//根据当前数据确定唯一预测计划总量计划的行
			PurchaseTotalPlanItemEntity purchaseTotalPlanItem = getPurchaseTotalPlanItemEntity(plan, trans);  //获取预测计划总量的子项目
			if(purchaseTotalPlanItem == null || purchaseTotalPlanItem.getId() == 0 ){
				newInsertList.add(trans);
				continue;
			}
	
			//设置表头信息
			PurchaseTotalPlanTransfer headerTransfer = headList.get(0);
			
			//获取盖子项目的表头
			List<PurchaseTotalPlanHeadEntity> oldPurchasePlanHeadList =new  ArrayList<PurchaseTotalPlanHeadEntity>();
			List<PurchaseTotalPlanHeadEntity> newPurchasePlanHeadList = new ArrayList<PurchaseTotalPlanHeadEntity>();
			
			//获取老的数据项的数据
			oldPurchasePlanHeadList = purchaseTotalPlanHeadDao.findNewPurchasePlanHeadByplanItemId(purchaseTotalPlanItem.getId());			
          


			Class clazz1 = trans.getClass();  
			Class clazz2 = headerTransfer.getClass();
          
			Field[] fs1 = clazz1.getDeclaredFields();  
			Field[] fs2 = clazz2.getDeclaredFields();  
	        for(int i = 5 ; i < 29; i++){ 
	        	Field f1 = fs1[i];
	        	f1.setAccessible(true); //设置些属性是可以访问的  
	            Object val1 = f1.get(trans);//得到此属性的值  
	        	for(int j = 0; j < fs2.length;j++){
	        		Field f2 = fs1[j];
	        		f2.setAccessible(true); //设置些属性是可以访问的  
		            Object val2 = f2.get(headerTransfer);//得到此属性的值  
		            if(f1.getName().equals(f2.getName())){
		            	PurchaseTotalPlanHeadEntity purchasePlanHead=new PurchaseTotalPlanHeadEntity();
						purchasePlanHead.setHeaderName( val2 == null ?  ""   : val2.toString());
						purchasePlanHead.setHeaderValues( val1 == null ? "" :  val1.toString());
						purchasePlanHead.setVersionNumber(version);
						purchasePlanHead.setIsNew(1);
						newPurchasePlanHeadList.add(purchasePlanHead);
						break;
		            }
	        	}
	        }
	        
	        
	        //整理数据结构进行对比
	        for(PurchaseTotalPlanHeadEntity item1 :    oldPurchasePlanHeadList){
	        	if(item1.getHeaderValues() == null || item1.getHeaderValues() == ""){
	        		item1.setHeaderValues("0");
	        	}
	        }
	        
	        for(PurchaseTotalPlanHeadEntity item2 :    newPurchasePlanHeadList){
	        	if(item2.getHeaderValues() == null || item2.getHeaderValues() == ""){
	        		item2.setHeaderValues("0");
	        	}
	        }
	        
			
	    	if(isActualVersion(oldPurchasePlanHeadList,newPurchasePlanHeadList)){
				//操作人+物料 +工厂+采购组+key
				String versionKey = user.id+trans.getMaterialCode()+trans.getFactoryCode()+trans.getGroupCode()+"poTotalPlan"+",";
			    String  allVersion =  serialNumberService.geneterPHNextNumberByKey(versionKey);
			    String [] strs = allVersion.split(",");
				version =strs[1];
				//修改预测总量的子项目的类容(添加)
				
				PurchaseTotalPlanItemEntity tem = new PurchaseTotalPlanItemEntity();//设置明细单表信息
				tem.setPlan(plan);
				tem.setPurchasingGroup(groupMap.get(trans.getGroupCode()));//采购组
				tem.setFactory(factoryMap.get(trans.getFactoryCode()));//工厂
				tem.setMaterial(materialMap.get(trans.getMaterialCode()));//物料
				tem.setVersionNumber(version);
				tem.setIsNew(1);
				tem.setDayStock(trans.getInventory());
				tem.setUnpaidQty(trans.getDeliveredCount());
				upPurchaseTotalPlanItemList.add(tem);
	    		newUpgradeList.add(trans);
	    		//将原来的版本失效ids集合(表头表)
	    		if(oldPurchasePlanHeadList != null && oldPurchasePlanHeadList.size() >0){
	    			invalidPlanItemIds.add(oldPurchasePlanHeadList.get(0).getPlanItem().getId());
	    		}
	    		
	    		//获取要重新添加的数据项
		    	for (PurchaseTotalPlanHeadEntity oldPurchasePlanHead : oldPurchasePlanHeadList) {  
		    		oldHeadMap.put(oldPurchasePlanHead.getHeaderName()+trans.getMaterialCode()+trans.getFactoryCode()+trans.getGroupCode(), oldPurchasePlanHead);  
		        }
		    	for (PurchaseTotalPlanHeadEntity newPurchasePlanHead : newPurchasePlanHeadList) {  
		    		newPurchasePlanHead.setPlanItem(tem);
		    		newHeadMap.put(newPurchasePlanHead.getHeaderName()+trans.getMaterialCode()+trans.getFactoryCode()+trans.getGroupCode(), newPurchasePlanHead);  
		        }
		    	//去除老版本的中的新版本的覆盖的元素
		    	List<String> keys =new  ArrayList<String>();
		    	 for (Map.Entry<String, PurchaseTotalPlanHeadEntity> oldHeadMaprntry : oldHeadMap.entrySet()) {
		    		 for (Map.Entry<String, PurchaseTotalPlanHeadEntity> newHeadMapEntry : newHeadMap.entrySet()) {
			    		 if(oldHeadMaprntry.getKey().equals(newHeadMapEntry.getKey())){
			    			 keys.add(oldHeadMaprntry.getKey());
			    		 }
			    	}
		    	}
		    	 for(String s : keys){
		    		 oldHeadMap.remove(s);
		    	 }
		    	 
		    	//合并剩下的老版本的额数据
		    	if(oldHeadMap.size() != 0){
		    		 for (Map.Entry<String, PurchaseTotalPlanHeadEntity> oldHeadMaprntry : oldHeadMap.entrySet()) { 
		    			 oldHeadMaprntry.getValue().setVersionNumber(version);
			    	}
		    		newHeadMap.putAll(oldHeadMap);
		    	}	
		    	
	    	}else{
				//修改采购计划的子项目的内容（只是修改数据表头不变）

				//设置明细单表信息
	    		PurchaseTotalPlanItemEntity _purchaseTotalPlanItem = new PurchaseTotalPlanItemEntity();
				_purchaseTotalPlanItem = getPurchaseTotalPlanItemEntity(plan, trans);  //获取预测计划总量的子项目
				_purchaseTotalPlanItem.setPlan(plan);
				_purchaseTotalPlanItem.setPurchasingGroup(groupMap.get(trans.getGroupCode()));//采购组
				_purchaseTotalPlanItem.setFactory(factoryMap.get(trans.getFactoryCode()));//工厂
				_purchaseTotalPlanItem.setMaterial(materialMap.get(trans.getMaterialCode()));//物料
				_purchaseTotalPlanItem.setDayStock(trans.getInventory());
				_purchaseTotalPlanItem.setUnpaidQty(trans.getDeliveredCount());
				purchaseTotalPlanItemList.add(_purchaseTotalPlanItem);
	    		newUpdateList.add(trans);
	    	}	
		}
		
		String logMsg = ""; 
		
		//添加新的数据
		if(newInsertList.size() > 1){
			Boolean flag = addPurchaseTotalPlan(newInsertList,month,version, factoryMap ,groupMap,materialMap,logger);
			if(!flag){
				return flag;
			}
		}
		//更新现有的数据
		if(newUpdateList.size() > 0 && newUpdateList != null){
			//只是单纯的更新行数据不做升级的操作
			purchaseTotalPlanItemDao.save(purchaseTotalPlanItemList);
			logMsg = "更新未升级[" + newUpdateList.size() + "]条有效的数据";
		}
		//升级当前的版本（失效老版本并添加新的数据）
        if(newUpgradeList.size() > 0 && newUpgradeList != null){
        	//失效表头-预测计划总量字表
    		if(invalidPlanItemIds.size() > 0 && invalidPlanItemIds != null){
    			for(Long planItemId : invalidPlanItemIds){//失效表头表
    			      purchaseTotalPlanHeadDao.invalidPurchaseTotalPlanHead(planItemId);
    			}
    			for(Long planItemId : invalidPlanItemIds){//失效字表
    				purchaseTotalPlanItemDao.invalidPurchaseTotalPlanItem(planItemId);
    			}
    		}
    		purchaseTotalPlanItemDao.save(upPurchaseTotalPlanItemList);   
    		purchaseTotalPlanHeadDao.save(newHeadMap.values());
    		
    		logMsg = "更新并升级[" + newUpgradeList.size() + "]条有效的数据";
		}
		logger.log(logMsg);
		return true;
	}
	
	
	
	/**
	 * 判断是否需要升级版本
	 * @param plans
	 */
	public Boolean isActualVersion(List<PurchaseTotalPlanHeadEntity> oldHeadList ,List<PurchaseTotalPlanHeadEntity> newHeadList) {
		 //整合list为map
    	Map<String, PurchaseTotalPlanHeadEntity> oldHeadMap = new HashMap<String, PurchaseTotalPlanHeadEntity>();
    	Map<String, PurchaseTotalPlanHeadEntity> newHeadMap = new HashMap<String, PurchaseTotalPlanHeadEntity>();
    	Map<String, PurchaseTotalPlanHeadEntity> invariableHeadMap = new HashMap<String, PurchaseTotalPlanHeadEntity>();
		
    	for (PurchaseTotalPlanHeadEntity oldPurchasePlanHead : oldHeadList) {  
    		oldHeadMap.put(oldPurchasePlanHead.getHeaderName()+"-"+oldPurchasePlanHead.getHeaderValues(), oldPurchasePlanHead);  
        }
        
    	for (PurchaseTotalPlanHeadEntity newPurchasePlanHead : newHeadList) {  
    		newHeadMap.put(newPurchasePlanHead.getHeaderName()+"-"+newPurchasePlanHead.getHeaderValues(), newPurchasePlanHead);  
        }
        
    	//判断是否需要升级版本
    	 for (Map.Entry<String, PurchaseTotalPlanHeadEntity> newHeadMapEntry : newHeadMap.entrySet()) {
    		 for (Map.Entry<String, PurchaseTotalPlanHeadEntity> oldHeadMaprntry : oldHeadMap.entrySet()) {
	    		 if(newHeadMapEntry.getKey().equals(oldHeadMaprntry.getKey())){
	    			 invariableHeadMap.put(oldHeadMaprntry.getKey(), oldHeadMaprntry.getValue());
	    		 }  
	    	}
    	}
        
    	if(invariableHeadMap.size() != 24){
    		return true;
    	}
		return false;
	}
	
	
	
	
	/**
	 * 预处理预测需求总量数据
	 * @param plans
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private List<PurchaseTotalPlanTransfer> validateTransfers(List<PurchaseTotalPlanTransfer> planTranList,Map<String, FactoryEntity> factoryMap,Map<String, PurchasingGroupEntity> groupMap, Map<String, MaterialEntity> materialMap, Integer[] counts, ILogger logger) {
			String logMsg = "->现在对导入的预测计划总量信息进行预处理,共有[" + (planTranList == null ? 0 : planTranList.size() - 1) + "]条数据";
			logger.log(logMsg);
			counts[0] = planTranList.size();
			List<PurchaseTotalPlanTransfer> ret = new ArrayList<PurchaseTotalPlanTransfer>();
			
			List<PurchasingGroupEntity> groupList=null; //采购组
			List<MaterialEntity> materialList = null; //物料
			List<FactoryEntity> factoryList = null ; //工厂
			boolean lineValidat = true;
			int index  = 2; 
			int size= 1;
			
			Date date = new Date();
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);
		    String month = cd.get(Calendar.YEAR)+""+ String.format("%02d", (cd.get(Calendar.MONTH) + 1));
			
			
			for(PurchaseTotalPlanTransfer trans : planTranList) {
				////每次循环之后重置start
				groupList=null;
				materialList = null;
				factoryList = null;
				////重置end
				
				//自动过滤添加表头的数据
				if(size ==1){
					ret.add(trans);
					size++;
					continue;
				}
				
				if(StringUtils.isEmpty(trans.getFactoryCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'> [FAILED]行索引[" + index + "],工厂编码不能为空,忽略此数据</font></b>");
				}
				
				
				if(StringUtils.isEmpty(trans.getGroupCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'> [FAILED]行索引[" + index + "],采购组编码不能为空,忽略此数据</font></b>");
				}
				
				if(StringUtils.isEmpty(trans.getMaterialCode())){
					lineValidat = false;
					logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],物料编码不能为空,忽略此数据 </font></b>");
				}
				
				
				
				
				
				//查询工厂
				if(!factoryMap.containsKey(trans.getFactoryCode())) {
					factoryList = factoryDao.findFactoryByCodeAndAbolished(trans.getFactoryCode(),0);
					if(CollectionUtils.isEmpty(factoryList)) {
						lineValidat = false;
						logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],工厂编码[" + trans.getFactoryCode() + "]未在系统中维护,或被禁用,忽略此预测计划 </font></b>");
					} else {
						factoryMap.put(trans.getFactoryCode(), factoryList.get(0));
					}
				}
				
				
				//查询采购组
				if(!groupMap.containsKey(trans.getGroupCode())) {
					groupList = purchasingGroupDao.findByCodeAndAbolisheds(trans.getGroupCode(),0);
					if(CollectionUtils.isEmpty(groupList)) {
						lineValidat = false;
						logger.log("-><b><font color='red'>[FAILED]行索引[" + index + "],采购组编码[" + trans.getGroupCode() + "]未在系统中维护,或被禁用,忽略此预测计划 </font></b>");
					} else {
						groupMap.put(trans.getGroupCode(), groupList.get(0));
					}
				}
				
				//查询物料
				if(!materialMap.containsKey(trans.getMaterialCode())) {
					materialList = materialDao.findByCodeAndAbolished(trans.getMaterialCode(),0);
					if(CollectionUtils.isEmpty(materialList)) {
						lineValidat = false;
						logger.log(logMsg = "-> <b><font color='red'>[FAILED]行索引[" + index + "],物料[" + trans.getMaterialCode() + "]未在系统中维护,或被禁用,忽略此预测计划 </font></b>");
					} else {
						materialMap.put(trans.getMaterialCode(), materialList.get(0));
					}
				}

				//添加查询此工厂下面是否有这个物料
				List<VendorMaterialRelEntity>  vmRel =  vendorMaterialRelDao.findByMaterialCodeAndFactoryCodeAndAbolished(trans.getMaterialCode(),trans.getFactoryCode(),0);
				if(vmRel == null || vmRel.size() == 0){
					lineValidat = false;
					logger.log(logMsg = "-><b><font color='red'>[FAILED]行索引[" + index + "],工厂[" + trans.getFactoryCode() + "],物料[" + trans.getMaterialCode() + "] 未维护此关系,忽略此预测计划 </font></b> ");
				}
					
		
				if(lineValidat) {
				/*	logMsg = "[SUCCESS]行索引[" + index + "],预处理[ "+trans.getFactoryCode() +" | " + trans.getGroupCode() + "|" + trans.getMaterialCode() + "|]成功.";*/
				/*logger.log(logMsg);*/
					ret.add(trans);
				}
				index ++;
				
				lineValidat = true;
			}
			int count= ret.size()-1;
			counts[1] = count;
			logMsg = "<-导入的预测计划总量主信息预处理完毕,共有[" + count+ "]条有效数据";
			logger.log(logMsg);
			return ret;   
   }
	
	
	/**
	 *导出
	 * @param plans
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(Map<String, Object> searchParamMap,Long planid,HttpServletResponse response) throws Exception {
		//获取表头的数据
		PurchasePlanItemHeadVO head = getItemHead(planid,null,null);
		//获取内容数据
		List<PurchaseTotalPlanItemEntity>  contentList= exportExcelList(searchParamMap);
		
        // 第一步，创建一个webbook，对应一个Excel文件  
        HSSFWorkbook wb = new HSSFWorkbook();  
        // 第二步，在webbook中添加一个sheet,对应Excel文件中的sheet  
        
        HSSFSheet sheet = wb.createSheet("预测计划总量"); 
		for(int i = 0 ; i < 37 ; i++ ){
			sheet.setColumnWidth(i, 120*35);
		}
		
        // 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short  
        HSSFRow row = sheet.createRow((int) 0);  
        // 第四步，创建单元格，并设置值表头 设置表头居中  
        HSSFCellStyle style = wb.createCellStyle();  
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 创建一个居中格式  
        style.setWrapText(true);  
  
        HSSFCell cell = row.createCell((short) 0);  
        cell.setCellValue("工厂编码");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 1);  
        cell.setCellValue("工厂名称");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 2);  
        cell.setCellValue("采购组编码"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 3);  
        cell.setCellValue("采购组名称");  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 4);  
        cell.setCellValue("物料号");  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 5);  
        cell.setCellValue("物料描述"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 6);  
        cell.setCellValue("单位"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 7);  
        cell.setCellValue("当日库存"); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 8);  
        cell.setCellValue("当日未交PO数");  
        cell.setCellStyle(style);
        
        

        //动态表头加载项
        
        cell = row.createCell((short) 9);  
        cell.setCellValue(head.getCol1());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 10);  
        cell.setCellValue(head.getCol2()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 11);  
        cell.setCellValue(head.getCol3());  
        cell.setCellStyle(style);
        
        
        cell = row.createCell((short) 12);  
        cell.setCellValue(head.getCol4());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 13);  
        cell.setCellValue(head.getCol5()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 14);  
        cell.setCellValue(head.getCol6());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 15);  
        cell.setCellValue(head.getCol7());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 16);  
        cell.setCellValue(head.getCol8()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 17);  
        cell.setCellValue(head.getCol9());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 18);  
        cell.setCellValue(head.getCol10());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 19);  
        cell.setCellValue(head.getCol11()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 20);  
        cell.setCellValue(head.getCol12());  
        cell.setCellStyle(style);
        
        cell = row.createCell((short) 21);  
        cell.setCellValue(head.getCol13());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 22);  
        cell.setCellValue(head.getCol4()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 23);  
        cell.setCellValue(head.getCol15());  
        cell.setCellStyle(style);
          
        
        cell = row.createCell((short) 24);  
        cell.setCellValue(head.getCol6());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 25);  
        cell.setCellValue(head.getCol17()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 26);  
        cell.setCellValue(head.getCol18());  
        cell.setCellStyle(style);
        
        
        
        
        
        cell = row.createCell((short) 27);  
        cell.setCellValue(head.getCol19());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 28);  
        cell.setCellValue(head.getCol20()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 29);  
        cell.setCellValue(head.getCol21());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 30);  
        cell.setCellValue(head.getCol22());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 31);  
        cell.setCellValue(head.getCol23()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 32);  
        cell.setCellValue(head.getCol24());  
        cell.setCellStyle(style);
        
        
        
        
        cell = row.createCell((short) 33);  
        cell.setCellValue(head.getCol25());  
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 34);  
        cell.setCellValue(head.getCol26()); 
        cell.setCellStyle(style);  
        
        cell = row.createCell((short) 35);  
        cell.setCellValue(head.getCol27());  
        cell.setCellStyle(style);
        
        
        
        cell = row.createCell((short) 36);  
        cell.setCellValue(head.getCol28());  
        cell.setCellStyle(style);  
        

        
        
        
        
  
        // 第五步，写入实体数据 实际应用中这些数据从数据库得到，  
  
        for (int i = 0; i < contentList.size(); i++){  
            row = sheet.createRow((int) i + 1);  
            PurchaseTotalPlanItemEntity data =contentList.get(i);  
            // 第四步，创建单元格，并设置值  
            row.createCell((short) 0).setCellValue(data.getFactory().getCode());  
            row.createCell((short) 1).setCellValue(data.getFactory().getName());  
            
            row.createCell((short) 2).setCellValue(data.getPurchasingGroup()  == null ? "" :    data.getPurchasingGroup().getCode());  
            row.createCell((short) 3).setCellValue(data.getPurchasingGroup()  == null ? "" :    data.getPurchasingGroup().getName());  

            row.createCell((short) 4).setCellValue(data.getMaterial().getCode()); 
            row.createCell((short) 5).setCellValue(data.getMaterial().getName()); 
            row.createCell((short) 6).setCellValue(data.getMaterial().getUnit()); 
            
            row.createCell((short) 7).setCellValue(data.getDayStock()); 
            row.createCell((short) 8).setCellValue(data.getUnpaidQty());  
            
            row.createCell((short) 9).setCellValue(data.getPurchasePlanItemHeadVO().getCol1()); 
            row.createCell((short) 10).setCellValue(data.getPurchasePlanItemHeadVO().getCol2());  
            row.createCell((short) 11).setCellValue(data.getPurchasePlanItemHeadVO().getCol3());  
            row.createCell((short) 12).setCellValue(data.getPurchasePlanItemHeadVO().getCol4());  
            row.createCell((short) 13).setCellValue(data.getPurchasePlanItemHeadVO().getCol5()); 
            row.createCell((short) 14).setCellValue(data.getPurchasePlanItemHeadVO().getCol6());  
            row.createCell((short) 15).setCellValue(data.getPurchasePlanItemHeadVO().getCol7());  
            row.createCell((short) 16).setCellValue(data.getPurchasePlanItemHeadVO().getCol8());  
            row.createCell((short) 17).setCellValue(data.getPurchasePlanItemHeadVO().getCol9()); 
            row.createCell((short) 18).setCellValue(data.getPurchasePlanItemHeadVO().getCol10());  
            row.createCell((short) 19).setCellValue(data.getPurchasePlanItemHeadVO().getCol11());  
            row.createCell((short) 20).setCellValue(data.getPurchasePlanItemHeadVO().getCol12());  
            row.createCell((short) 21).setCellValue(data.getPurchasePlanItemHeadVO().getCol13()); 
            row.createCell((short) 22).setCellValue(data.getPurchasePlanItemHeadVO().getCol14());  
            row.createCell((short) 23).setCellValue(data.getPurchasePlanItemHeadVO().getCol15());  
            row.createCell((short) 24).setCellValue(data.getPurchasePlanItemHeadVO().getCol16());  
            row.createCell((short) 25).setCellValue(data.getPurchasePlanItemHeadVO().getCol17()); 
            row.createCell((short) 26).setCellValue(data.getPurchasePlanItemHeadVO().getCol18());  
            row.createCell((short) 27).setCellValue(data.getPurchasePlanItemHeadVO().getCol19());  
            row.createCell((short) 28).setCellValue(data.getPurchasePlanItemHeadVO().getCol20());  
            row.createCell((short) 29).setCellValue(data.getPurchasePlanItemHeadVO().getCol21()); 
            row.createCell((short) 30).setCellValue(data.getPurchasePlanItemHeadVO().getCol22());  
            row.createCell((short) 31).setCellValue(data.getPurchasePlanItemHeadVO().getCol23());  
            row.createCell((short) 32).setCellValue(data.getPurchasePlanItemHeadVO().getCol24());  
            row.createCell((short) 33).setCellValue(data.getPurchasePlanItemHeadVO().getCol25()); 
            row.createCell((short) 34).setCellValue(data.getPurchasePlanItemHeadVO().getCol26());  
            row.createCell((short) 35).setCellValue(data.getPurchasePlanItemHeadVO().getCol27());  
            row.createCell((short) 36).setCellValue(data.getPurchasePlanItemHeadVO().getCol28());  

        }
		wb.write(response.getOutputStream());

	}
	
	
	public List<PurchaseTotalPlanItemEntity> exportExcelList(Map<String, Object> searchParamMap) throws Exception{
		ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		
		StringBuffer sql=new StringBuffer();
		sql.append(" FROM");
		sql.append(" QEWEB_PURCHASE_TOTAL_PLAN_ITEM A");
		sql.append(" LEFT JOIN QEWEB_USER B ON A.BUYER_ID = B. ID");
		sql.append(" LEFT JOIN QEWEB_MATERIAL C ON A.MATERIAL_ID = C. ID  ");
		sql.append(" LEFT JOIN QEWEB_FACTORY D ON A.FACTORY_ID = D. ID  ");
		sql.append(" WHERE ");
		sql.append(" A.ABOLISHED = 0");
		sql.append(" AND A.CREATE_USER_ID = "+user.id+" ");
		sql.append("AND  A.PLAN_ID = "+searchParamMap.get("EQ_plan.id")+"");
		if(searchParamMap.containsKey("EQ_versionNumber")){
			sql.append(" AND A.VERSION_NUMBER like '%"+searchParamMap.get("EQ_versionNumber")+"%'");//版本号                            
		}
		if(searchParamMap.containsKey("EQ_isNew")){
			
			if(searchParamMap.get("EQ_isNew") != null){
				sql.append(" AND A.IS_NEW like '%"+searchParamMap.get("EQ_isNew")+"%'");//有效状态
			}else{
				sql.append(" AND A.IS_NEW like 1");//有效状态
			}
			
		
		}else{
			sql.append(" AND A.IS_NEW like 1");//有效状态
		}
		if(searchParamMap.containsKey("EQ_factory.name")){
			sql.append(" AND D.NAME like '%"+searchParamMap.get("EQ_factory.name")+"%'");//工厂
		}
		if(searchParamMap.containsKey("EQ_factory.code")){
			sql.append(" AND D.CODE like '%"+searchParamMap.get("EQ_factory.code")+"%'");//工厂
		}
		sql.append(" ORDER BY  ");
		sql.append(" A.CREATE_TIME desc");
		String sql1="SELECT A.* "+sql.toString();
		String sqlTotalCount="select count(1) "+sql.toString();
		List<?> list = generialDao.queryBySql_map(sql1);
		List<PurchaseTotalPlanItemEntity> purchasePlanItemList=new ArrayList<PurchaseTotalPlanItemEntity>();
		int totalCount=generialDao.findCountBySql(sqlTotalCount);
		if(null!=list && list.size()>0){
			for(Object purchaseTotalPlanItem : list){
				Map<String,Object> m =  (Map<String, Object>) purchaseTotalPlanItem;
				PurchaseTotalPlanItemEntity entity=purchaseTotalPlanItemDao.findOne(((BigDecimal)m.get("ID")).longValue());
				purchasePlanItemList.add(entity);
			}
		}
		
		//获取选择大版本的第一个星期一
		PurchaseTotalPlanEntity purchaseTotalPlan = new PurchaseTotalPlanEntity();
		purchaseTotalPlan = purchaseTotalPlanDao.findOne(Long.parseLong(searchParamMap.get("EQ_plan.id").toString()));
    	String version = purchaseTotalPlan.getMonth();
    	String timeString = version.substring(0, 4)+"-"+ version.substring(4, 6)+"-01";
    	Date date = getVersionMonday(timeString);
    	
    	//过滤查询条件（时间）
/*    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date start = null;
		Date end = null;
		if(searchParamMap.containsKey("EQ_startDate") && searchParamMap.get("EQ_startDate") != null && searchParamMap.get("EQ_startDate") != ""){
    		start = sdf.parse(searchParamMap.get("EQ_startDate").toString());  
    	}else{
    		start= sdf.parse("0000-00-00"); 
    	}
        if(searchParamMap.containsKey("EQ_endDate") && searchParamMap.get("EQ_endDate") != null && searchParamMap.get("EQ_endDate") != "" ){
        	end   = sdf.parse(searchParamMap.get("EQ_endDate").toString());  
    	}else{
    		end   = sdf.parse("9999-99-99"); 
    	}
    	*/
        
    	//加载 PurchasePlanItemHeadVO（表头）
    	PurchasePlanItemHeadVO purchasePlanItemHeadVO = new PurchasePlanItemHeadVO();    	
		Class clazz = purchasePlanItemHeadVO.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(date);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
          /*  if(start.getTime() <=  date.getTime()  &&  date.getTime() <=  end.getTime() ){*/
            	 if(field.getType().getSimpleName().equals("String")){
                     method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
                 }
         /*   }*/
            date=getSeventDay(date);
        }
		

		//重新加载list
		List<PurchaseTotalPlanItemEntity> purchaseTotalPlanItemList=new ArrayList<PurchaseTotalPlanItemEntity>();
		for(PurchaseTotalPlanItemEntity purchaseTotalPlanItem : purchasePlanItemList){
			purchasePlanItemHeadVO =(PurchasePlanItemHeadVO) object;
			
			
			PurchasePlanItemHeadVO tmpPurchasePlanItemHeadVO =purchasePlanItemHeadVO ;
			Class clazz1 = tmpPurchasePlanItemHeadVO.getClass();  
			Object object1 = clazz1.newInstance();
			Field[] fields1 = clazz1.getDeclaredFields();  

			
			PurchaseTotalPlanItemEntity item = purchaseTotalPlanItem;
			List<PurchaseTotalPlanHeadEntity>   headList =    purchaseTotalPlanHeadDao.findPurchasePlanHeadByplanItemId(purchaseTotalPlanItem.getId());
			tmpPurchasePlanItemHeadVO = purchasePlanItemHeadVO;
			for(PurchaseTotalPlanHeadEntity purchaseTotalPlanHead : headList ){	
			    for(int i = 0 ; i < fields1.length; i++){ 
			    	Field f1 = fields1[i];
			    	f1.setAccessible(true); //设置些属性是可以访问的  
			        Object val1 = f1.get(tmpPurchasePlanItemHeadVO);//得到此属性的值  
			        if(purchaseTotalPlanHead.getHeaderName().equals(val1)){
		            	 String name = f1.getName();
				         String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
				         Method method = clazz1.getMethod(methodStr,new Class[]{f1.getType()});
		            	 method.invoke(object1,purchaseTotalPlanHead.getHeaderValues());
		            	 break;
					}
			    }	
			}
			PurchasePlanItemHeadVO tempVo = new PurchasePlanItemHeadVO();
			BeanUtil.copyPropertyNotNull((PurchasePlanItemHeadVO) object1,tempVo);
			item.setPurchasePlanItemHeadVO(tempVo);
			item.setHeadVO(purchasePlanItemHeadVO);
			purchaseTotalPlanItemList.add(item);
		}		
		return purchaseTotalPlanItemList;
	}
	
	
	public   Boolean checkDataSource(List<PurchaseTotalPlanTransfer> list,ILogger logger) throws IllegalArgumentException, IllegalAccessException{
		String logMsg = "->现在对导入的预测信息进行有效数据检验,共有[" + (list == null ? 0 : list.size() - 1) + "]条数据";
		
		//表头处理
		List<PurchaseTotalPlanTransfer> headList = new ArrayList<PurchaseTotalPlanTransfer>();//表头
		headList.add(list.get(0));
		list.removeAll(headList);//数据项
		
		//数据处理
		boolean flag = true;
		
		
		
		//将导入数据物料相同的物料进行组合
		Map<String,PurchaseTotalPlanTransfer> planMap = new HashMap<String,PurchaseTotalPlanTransfer>();//导入物料的集合map
		
		String key = null;
		for(PurchaseTotalPlanTransfer totalPlan  : list){
			key = totalPlan.getFactoryCode()+"," + totalPlan.getMaterialCode();
			if(!planMap.containsKey(key)){
				planMap.put(key, totalPlan);
			}else{
				flag = false;
				logger.log(logMsg = "-><b><font color='red'>[FAILED] 数据存在，工厂[" + totalPlan.getFactoryCode() + "]，物料[" + totalPlan.getMaterialCode() + "] ,采购组["+totalPlan.getGroupCode()+"]有重复的数据，请修改预测计划！</font></b>");
			    break;
			}
		}
		return flag;
	}
	
	
	
	/**
	 * 删除计划(作废当前数据)
	 * @param plans
	 */
	public void deletePlanItem(List<PurchaseTotalPlanItemEntity> totalPlanItemList) {
		for(PurchaseTotalPlanItemEntity item : totalPlanItemList){
			purchaseTotalPlanItemDao.invalidPurchaseTotalPlanItem(item.getId()); 
			purchaseTotalPlanHeadDao.invalidPurchaseTotalPlanHead(item.getId());
		}
	}
    
	
	

	
	
	/**
	 * 获取当前大版本的第一个星期一
	 * @param plans
	 */
	protected Date getVersionMonday(String dateString) {  
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
    		date = sdf.parse(dateString);  
    	} catch (Exception   e) {
    	    e.printStackTrace();
    	}
		while(true){
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			int week = c.get(Calendar.DAY_OF_WEEK);		
			if(week == 2){
				return date;
			}
			Date d = new Date();
			d.setTime(date.getTime() + 24*60*60*1000);
			date = d;
		}
	}
	
    //获取七天后的日期
    public  Date getSeventDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +7);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }
	
}
