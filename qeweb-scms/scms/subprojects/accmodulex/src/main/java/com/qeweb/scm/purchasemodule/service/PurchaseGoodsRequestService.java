package com.qeweb.scm.purchasemodule.service;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.modules.utils.BeanUtil;
import com.qeweb.scm.baseline.common.entity.WarnConstant;
import com.qeweb.scm.baseline.common.service.WarnMainService;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.entity.GroupConFigRelEntity;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.entity.OrganizationEntity;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.entity.UserEntity;
import com.qeweb.scm.basemodule.log.FileLogger;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.repository.MaterialDao;
import com.qeweb.scm.basemodule.repository.OrganizationDao;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.repository.UserDao;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.basemodule.service.UserConfigRelService;
import com.qeweb.scm.basemodule.service.UserServiceImpl;
import com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser;
import com.qeweb.scm.basemodule.utils.BigDecimalUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.constants.PurchaseConstans;
import com.qeweb.scm.purchasemodule.entity.DeliveryItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseGoodsRequestEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanEntity;
import com.qeweb.scm.purchasemodule.entity.PurchaseOrderItemPlanTemEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanHeadEntity;
import com.qeweb.scm.purchasemodule.entity.PurchasePlanItemEntity;
import com.qeweb.scm.purchasemodule.repository.DeliveryItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseGoodsRequestDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanDao;
import com.qeweb.scm.purchasemodule.repository.PurchaseOrderItemPlanTemDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanHeadDao;
import com.qeweb.scm.purchasemodule.repository.PurchasePlanItemDao;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.GoodsRequestReportHeadViewVo;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.GoodsRequestReportHeadVo;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.GoodsRequestReportVo;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.GoodsRequestTransfer;
import com.qeweb.scm.purchasemodule.web.goodsrequest.vo.PurchaseOrderItemPlanTransfer;
import com.qeweb.scm.purchasemodule.web.util.CommonUtil;
import com.qeweb.scm.purchasemodule.web.vo.PurchaseGoodsRequestVO;
import com.qeweb.scm.sap.service.PurchaseGoodsRequestSyncService;


/**
 * 要货计划管理service
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class PurchaseGoodsRequestService {

	@Autowired
	private PurchaseGoodsRequestDao purchaseGoodsRequestDao;
	
	@Autowired
	private PurchaseOrderItemPlanDao purchaseOrderItemPlanDao;
	
	@Autowired
	private PurchaseOrderItemPlanTemDao purchaseOrderItemPlanTemDao;
	
	@Autowired
	private PurchaseOrderItemDao  purchaseOrderItemDao;
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Autowired
	private DeliveryItemDao deliveryItemDao;
	

	@Autowired
	private UserServiceImpl userServiceImpl;
	
	
	@Autowired
	private UserConfigRelService userConfigRelService;
	
	@Autowired
	private PurchasePlanItemDao purchasePlanItemDao;
	
	@Autowired
	private PurchasePlanHeadDao  purchasePlanHeadDao;
	
	@Autowired
	private GenerialDao generialDao;
	
	@Autowired
	private MaterialDao materialDao;
	
	@Autowired
	private FactoryDao factoryDao;
	
	@Autowired
	private PurchasingGroupDao groupDao;
	
	@Autowired
	private OrganizationDao vendorDao;
	
	
	@Autowired
	private UserDao userDao;

	/**
	 * 获取要货计划列表
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<PurchaseGoodsRequestEntity> getPurchaseGoodsRequest(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseGoodsRequestEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseGoodsRequestEntity.class);
		return purchaseGoodsRequestDao.findAll(spec,pagin);
	}
	
	/**
	 * 获取要货计划动态表格数据
	 * @author chao.gu
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 * @throws Exception 
	 */
	public List<PurchaseGoodsRequestVO> getRequestRowListExcel(Map<String, Object> searchParamMap,List<String> dateStrList) throws Exception{
		String commonParamSql=getCommonRequestMainFromSql(searchParamMap);
		String commonSql=getCommonAllSql(searchParamMap, commonParamSql, dateParamStr(dateStrList));
		String sql1=getBaseSql()+commonSql;
		List<?> dataList = generialDao.queryBySql_map(sql1);
		List<PurchaseGoodsRequestVO> list =dealQueryMap(dataList,dateStrList);
		return list;
	}
	
	/**
	 * 获取要货计划动态表格数据
	 * @author chao.gu
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 * @throws Exception 
	 */
	public Page<PurchaseGoodsRequestVO> getRequestRowList(int pageNumber, int pageSize, Map<String, Object> searchParamMap,List<String> dateStrList) throws Exception{
		PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, null);
		String commonParamSql=getCommonRequestMainFromSql(searchParamMap);
		String commonSql=getCommonAllSql(searchParamMap, commonParamSql, dateParamStr(dateStrList));
		String sql1=getBaseSql()+commonSql;
		List<?> dataList = generialDao.querybysql(sql1,pagin);
		String countSql="SELECT　COUNT(1) "+commonSql;
		int totalCount=generialDao.findCountBySql(countSql);
		List<PurchaseGoodsRequestVO> list =dealQueryMap(dataList,dateStrList);
		Page<PurchaseGoodsRequestVO> page = new PageImpl<PurchaseGoodsRequestVO>(list,pagin,totalCount);
		return page;
	}
	
	/**
	 * 获取动态表头
	 * @param beginRq
	 * @param endRq
	 * @return
	 * @throws ParseException
	 */
	public List<String> getDateStrList(String beginRq,String endRq) throws ParseException{
		List<String> dateStrList =new ArrayList<String>();
		//开始时间
		Timestamp beginRqTimeStamp=DateUtil.parseTimeStamp(beginRq, DateUtil.DATE_FORMAT_YYYY_MM_DD);
		//将开始时间增加至list
		dateStrList.add(beginRq);
		
		//结束时间
		Timestamp endRqTimeStamp=DateUtil.parseTimeStamp(endRq, DateUtil.DATE_FORMAT_YYYY_MM_DD);
		Date date =beginRqTimeStamp;
		while(date.before(endRqTimeStamp)){//时间未到截止时间
			date =DateUtil.addDay(date, 1);
			dateStrList.add(DateUtil.dateToString(date, DateUtil.DATE_FORMAT_YYYY_MM_DD));
		}
		return dateStrList;
	}
	
	
	/**
	 * 将日志转成传参
	 * @param dateList
	 * @return
	 */
	public String dateParamStr(List<String> dateList){
		StringBuffer str = new StringBuffer();
		for(int i=0;i<dateList.size();i++){
			str.append("TO_DATE ('"+dateList.get(i)+"', 'yyyy-mm-dd')  as col").append(i);
			if(i<dateList.size()-1){
				str.append(",");
			}
		}
		return str.toString();
	}
	
	/**
	 * 获取要货计划行转列动态表头
	 * @param beginRq
	 * @param endRq
	 * @return
	 * @throws Exception
	 */
	public PurchaseGoodsRequestVO getRequestRowHeader(String beginRq,String endRq) throws Exception{
		PurchaseGoodsRequestVO vo = new PurchaseGoodsRequestVO();
		List<String> dateStrList = getDateStrList(beginRq,endRq);
		Class clazz =vo.getClass();
		vo.setSize(dateStrList.size());
		for(int i=0;i<dateStrList.size();i++){
			String methodName="setCol"+i;
			Method method = clazz.getMethod(methodName,new Class[]{String.class});
			method.invoke(vo, dateStrList.get(i));
		}
		return vo;
	}
	
	/**
	 * 处理查询得数据
	 * @param dataList
	 * @param dataSize
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public List<PurchaseGoodsRequestVO> dealQueryMap(List<?> dataList,List<String> dateStrList) throws Exception, Exception{
		int dataSize =dateStrList.size();
		List<PurchaseGoodsRequestVO> list = new ArrayList<PurchaseGoodsRequestVO>();
		PurchaseGoodsRequestVO vo =null;
		for(Object o:dataList){
			vo = new PurchaseGoodsRequestVO();
			Class clazz =vo.getClass();
			
			Map<String,Object> m =  (Map<String, Object>) o;
			Integer vendorConfirmStatus=null==m.get("VENDOR_CONFIRM_STATUS")?0:((BigDecimal)m.get("VENDOR_CONFIRM_STATUS")).intValue();
			vo.setVendorConfirmStatus(vendorConfirmStatus);
			
			Integer publishStatus=null==m.get("PUBLISH_STATUS")?0:((BigDecimal)m.get("PUBLISH_STATUS")).intValue();
			vo.setPublishStatus(publishStatus);
			
			String flag=null==m.get("FLAG")?"":m.get("FLAG").toString();
			vo.setFlag(flag);
			
			Long factoryId=((BigDecimal)m.get("FACTORY_ID")).longValue();
			vo.setFactoryId(factoryId);
			
			String factoryCode=null==m.get("FACTORY_CODE")?"":m.get("FACTORY_CODE").toString();
			vo.setFactoryCode(factoryCode);
			
			String factoryName=null==m.get("FACTORY_NAME")?"":m.get("FACTORY_NAME").toString();
			vo.setFactoryName(factoryName);
			
			Long purchasingGroupId=((BigDecimal)m.get("PURCHASING_GROUP_ID")).longValue();
			vo.setPurchasingGroupId(purchasingGroupId);
			
			String groupName=null==m.get("GROUP_NAME")?"":m.get("GROUP_NAME").toString();
			vo.setGroupName(groupName);
			
			Long materialId=((BigDecimal)m.get("MATERIAL_ID")).longValue();
			vo.setMaterialId(materialId);
			
			String materialCode=null==m.get("MATERIAL_CODE")?"":m.get("MATERIAL_CODE").toString();
			vo.setMaterialCode(materialCode);
			
			String materialName=null==m.get("MATERIAL_NAME")?"":m.get("MATERIAL_NAME").toString();
			vo.setMaterialName(materialName);
			
			String cdqwl = null==m.get("CDQWL")?"":m.get("CDQWL").toString();
			vo.setCdqwl(cdqwl);
			
			String meins=null==m.get("MEINS")?"":m.get("MEINS").toString();
			vo.setMeins(meins);
			
			
			Long vendorId=((BigDecimal)m.get("VENDOR_ID")).longValue();
			vo.setVendorId(vendorId);
			
			String vendorCode=null==m.get("VENDOR_CODE")?"":m.get("VENDOR_CODE").toString();
			vo.setVendorCode(vendorCode);
			
			String vendorName=null==m.get("VENDOR_NAME")?"":m.get("VENDOR_NAME").toString();
			vo.setVendorName(vendorName);
			
			String drawingNumber=null==m.get("DRAWING_NUMBER")?"":m.get("DRAWING_NUMBER").toString();
			vo.setDrawingNumber(drawingNumber);
			
			Integer type=((BigDecimal)m.get("TYPE")).intValue();
			vo.setType(type);
			for(int i=0;i<dataSize;i++){
				String dhsl=null==m.get("COL"+i)?"":m.get("COL"+i).toString();
				String methodName="setCol"+i;
				Method method = clazz.getMethod(methodName,new Class[]{String.class});
				method.invoke(vo, dhsl);
			}
		    //塞开始时间和结束时间
			vo.setBeginRq(dateStrList.get(0));
			vo.setEndRq(dateStrList.get(dataSize-1));
			list.add(vo);
		}
		return list;
	}
	
	public String getBaseSql(){
		StringBuffer str = new StringBuffer();
		str.append(" SELECT");
		str.append(" MAIN_DATA.VENDOR_CONFIRM_STATUS,");
		str.append(" MAIN_DATA.PUBLISH_STATUS,");
		str.append(" MAIN_DATA.FLAG,");
		str.append(" MAIN_DATA.FACTORY_ID,");
		str.append(" MAIN_DATA.FACTORY_CODE,");
		str.append(" MAIN_DATA.FACTORY_NAME,");
		str.append(" MAIN_DATA.PURCHASING_GROUP_ID,");
		str.append(" MAIN_DATA.GROUP_NAME,");
		str.append(" MAIN_DATA.MATERIAL_ID,");
		str.append(" MAIN_DATA.MATERIAL_CODE,");
		str.append(" MAIN_DATA.MATERIAL_NAME,");
		str.append(" MAIN_DATA.MEINS,");
		str.append(" MAIN_DATA.VENDOR_ID,");
		str.append(" MAIN_DATA.VENDOR_CODE,");
		str.append(" MAIN_DATA.VENDOR_NAME,");
		str.append(" MAIN_DATA.CDQWL,");
		str.append(" DYNAMIN_DATA.*");
		return str.toString();
	}
	
	public String getCommonAllSql(Map<String,Object> searchParamMap,String commonParamSql,String dateParam){
		StringBuffer str = new StringBuffer();
		str.append(" FROM");
		str.append(" (");
		str.append(" SELECT");
		str.append(" CASE");
		str.append(" WHEN MAX (REQ.VENDOR_CONFIRM_STATUS) = 2 THEN");
		str.append(" 2");
		str.append(" WHEN MAX (REQ.VENDOR_CONFIRM_STATUS) = 1");
		str.append(" AND MIN (REQ.VENDOR_CONFIRM_STATUS) = 0 THEN");
		str.append(" 2");
		str.append(" WHEN MAX (REQ.VENDOR_CONFIRM_STATUS) = 1");
		str.append(" AND MIN (REQ.VENDOR_CONFIRM_STATUS) = 1 THEN");
		str.append(" 1");
		str.append(" ELSE");
		str.append(" 0");
		str.append(" END AS VENDOR_CONFIRM_STATUS,");
		str.append(" CASE");
		str.append(" WHEN MAX (REQ.PUBLISH_STATUS) = 2 THEN");
		str.append(" 2");
		str.append(" WHEN MAX (REQ.PUBLISH_STATUS) = 1");
		str.append(" AND MIN (REQ.PUBLISH_STATUS) = 0 THEN");
		str.append(" 2");
		str.append(" WHEN MAX (REQ.PUBLISH_STATUS) = 1");
		str.append(" AND MIN (REQ.PUBLISH_STATUS) = 1 THEN");
		str.append(" 1");
		str.append(" ELSE");
		str.append(" 0");
		str.append(" END AS PUBLISH_STATUS,");
		str.append(" FLAG,");
		str.append(" REQ.FACTORY_ID,");
		str.append(" FAC.CODE AS FACTORY_CODE,");
		str.append(" FAC. NAME AS FACTORY_NAME,");
		str.append(" REQ.PURCHASING_GROUP_ID,");
		str.append(" PURCHASE_GROUP. NAME AS GROUP_NAME,");
		str.append(" REQ.MATERIAL_ID,");
		str.append(" MAT.CODE AS MATERIAL_CODE,");
		str.append(" MAT. NAME AS MATERIAL_NAME,");
		str.append(" REQ.MEINS,");
		str.append(" REQ.VENDOR_ID,");
		str.append(" ORG.CODE AS VENDOR_CODE,");
		str.append(" REQ.DRAWING_NUMBER AS DRAWING_NUMBER,");
		str.append(" ORG. NAME AS VENDOR_NAME, ");
		str.append(" MAX(MAT. CDQWL) AS CDQWL");
		str.append(commonParamSql);
		str.append(" GROUP BY");
		str.append(" FLAG,");
		str.append(" REQ.FACTORY_ID,");
		str.append(" FAC.CODE,");
		str.append(" FAC. NAME,");
		str.append(" REQ.PURCHASING_GROUP_ID,");
		str.append(" PURCHASE_GROUP. NAME,");
		str.append(" REQ.MATERIAL_ID,");
		str.append(" MAT.CODE,");
		str.append(" MAT. NAME,");
		str.append(" REQ.MEINS,");
		str.append(" REQ.VENDOR_ID,");
		str.append(" ORG.CODE,");
		str.append(" REQ.DRAWING_NUMBER,");
		str.append(" ORG. NAME");
		str.append(" ) MAIN_DATA");
		str.append(" LEFT JOIN (");
		str.append(" SELECT");
		str.append(" *");
		str.append(" FROM");
		str.append(" (");
		str.append(" SELECT");
		str.append(" REQ.FLAG AS FLAG1,");
		str.append(" REQ.FACTORY_ID AS FACTORY_ID1,");
		str.append(" REQ.PURCHASING_GROUP_ID AS PURCHASING_GROUP_ID1,");
		str.append(" REQ.MATERIAL_ID AS MATERIAL_ID1,");
		str.append(" REQ.MEINS AS MEINS1,");
		str.append(" REQ.VENDOR_ID AS VENDOR_ID1,");
		str.append(" REQ.RQ AS RQ1,");
		str.append(" REQ.dhsl AS DHSL1,");
		str.append(" REQ.DRAWING_NUMBER AS DRAWING_NUMBER,");
		str.append(" 1 AS TYPE");
		str.append(commonParamSql);
		str.append(" ) PIVOT (");
		str.append(" SUM (DHSL1) FOR RQ1 IN (");
		str.append(dateParam);
		str.append(" )");
		str.append(" )");
		str.append(" UNION ALL");
		str.append(" SELECT");
		str.append(" *");
		str.append(" FROM");
		str.append(" (");
		str.append(" SELECT");
		str.append(" REQ.FLAG AS FLAG1,");
		str.append(" REQ.FACTORY_ID AS FACTORY_ID1,");
		str.append(" REQ.PURCHASING_GROUP_ID AS PURCHASING_GROUP_ID1,");
		str.append(" REQ.MATERIAL_ID AS MATERIAL_ID1,");
		str.append(" REQ.MEINS AS MEINS1,");
		str.append(" REQ.VENDOR_ID AS VENDOR_ID1,");
		str.append(" REQ.RQ AS RQ1,");
		str.append(" REQ.SUR_QRY AS SUR_QRY1,");
		str.append(" REQ.DRAWING_NUMBER AS DRAWING_NUMBER,");
		str.append(" 2 AS TYPE ");
		str.append(commonParamSql);
		str.append(" ) PIVOT (");
		str.append(" SUM (SUR_QRY1) FOR RQ1 IN (");
		str.append(dateParam);
		str.append(" )");
		str.append(" )");
		str.append(" ) DYNAMIN_DATA ON MAIN_DATA.FLAG = DYNAMIN_DATA.FLAG1");
		str.append(" AND MAIN_DATA.FACTORY_ID = DYNAMIN_DATA.FACTORY_ID1");
		str.append(" AND MAIN_DATA.PURCHASING_GROUP_ID = DYNAMIN_DATA.PURCHASING_GROUP_ID1");
		str.append(" AND MAIN_DATA.MATERIAL_ID = DYNAMIN_DATA.MATERIAL_ID1");
		str.append(" AND MAIN_DATA.MEINS = DYNAMIN_DATA.MEINS1");
		str.append(" AND MAIN_DATA.VENDOR_ID = DYNAMIN_DATA.VENDOR_ID1");
		str.append(" WHERE");
		str.append(" 1 = 1");
		if(searchParamMap.containsKey("EQ_publishStatus") && !StringUtils.isEmpty(searchParamMap.get("EQ_publishStatus").toString())){
			  str.append(" AND MAIN_DATA.PUBLISH_STATUS="+searchParamMap.get("EQ_publishStatus").toString()+" ");
		}
		if(searchParamMap.containsKey("EQ_vendorConfirmStatus") && !StringUtils.isEmpty(searchParamMap.get("EQ_vendorConfirmStatus").toString())){
			  str.append(" AND MAIN_DATA.VENDOR_CONFIRM_STATUS="+searchParamMap.get("EQ_vendorConfirmStatus").toString()+" ");
		}
		str.append(" ORDER BY");
		str.append(" MAIN_DATA.MATERIAL_ID || MAIN_DATA.VENDOR_ID || MAIN_DATA.FACTORY_ID || MAIN_DATA.PURCHASING_GROUP_ID || MAIN_DATA.MEINS ||  MAIN_DATA.FLAG ASC,");
		str.append(" DYNAMIN_DATA. TYPE ASC");
		return str.toString();
	}
	
	/**
	 * 动态数据公共FROM的SQL
	 * @param searchParamMap
	 * @return
	 */
	public String getCommonRequestMainFromSql(Map<String, Object> searchParamMap){
		StringBuffer str = new StringBuffer();
		str.append(" FROM");
		str.append(" PURCHASE_GOODS_REQUEST REQ");
		str.append(" LEFT JOIN QEWEB_FACTORY FAC ON REQ.FACTORY_ID = FAC. ID");
		str.append(" LEFT JOIN QEWEB_PURCHASING_GROUP PURCHASE_GROUP ON REQ.PURCHASING_GROUP_ID = PURCHASE_GROUP. ID");
		str.append(" LEFT JOIN QEWEB_MATERIAL MAT ON REQ.MATERIAL_ID = MAT. ID");
		str.append(" LEFT JOIN QEWEB_ORGANIZATION ORG ON REQ.VENDOR_ID = ORG. ID");
		str.append(" WHERE");
		str.append(" REQ.ABOLISHED = 0");
		if(searchParamMap.containsKey("GTE_rq") && !StringUtils.isEmpty(searchParamMap.get("GTE_rq").toString())){
		str.append(" AND REQ.RQ >= TO_DATE ('"+searchParamMap.get("GTE_rq").toString()+"', 'yyyy-MM-dd')");
		}
		if(searchParamMap.containsKey("LTE_rq") && !StringUtils.isEmpty(searchParamMap.get("LTE_rq").toString())){
		str.append(" AND REQ.RQ <= TO_DATE ('"+searchParamMap.get("LTE_rq").toString()+"', 'yyyy-MM-dd')");
		}
		
		if(searchParamMap.containsKey("IN_vendor.code") && !StringUtils.isEmpty(searchParamMap.get("IN_vendor.code").toString())){
			String vendorCodes=searchParamMap.get("IN_vendor.code").toString();
            if( !StringUtils.isEmpty(vendorCodes) && !vendorCodes.equals("null") ){
            	if(vendorCodes.endsWith(",")){
            		vendorCodes=vendorCodes.substring(0, vendorCodes.length()-1);
            	}
            	str.append(" AND ORG.CODE IN ('"+vendorCodes+"')");
            }
		}
		
		if(searchParamMap.containsKey("IN_material.code") && !StringUtils.isEmpty(searchParamMap.get("IN_material.code").toString())){
			String materialCodes=searchParamMap.get("IN_material.code").toString();
            if( !StringUtils.isEmpty(materialCodes) && !materialCodes.equals("null") ){
            	if(materialCodes.endsWith(",")){
            		materialCodes=materialCodes.substring(0, materialCodes.length()-1);
            	}
            	str.append(" AND MAT.CODE IN ('"+materialCodes+"')");
            }
		}
		
		if(searchParamMap.containsKey("EQ_flag") && !StringUtils.isEmpty(searchParamMap.get("EQ_flag").toString())){
		  str.append(" AND REQ.FLAG='"+searchParamMap.get("EQ_flag").toString()+"' ");
		}
		
		
		if(searchParamMap.containsKey("LIKE_group.name") && !StringUtils.isEmpty(searchParamMap.get("LIKE_group.name").toString())){
		  str.append(" AND PURCHASE_GROUP. NAME LIKE '%"+searchParamMap.get("LIKE_group.name").toString()+"%'");
		}
		
		//------供应商权限过滤
		if(searchParamMap.containsKey("EQ_vendor.id") && !StringUtils.isEmpty(searchParamMap.get("EQ_vendor.id").toString())){
			  str.append(" AND REQ.VENDOR_ID ="+searchParamMap.get("EQ_vendor.id").toString()+"");
		}
		
		if(searchParamMap.containsKey("NEQ_singlePublishStatus") && !StringUtils.isEmpty(searchParamMap.get("NEQ_singlePublishStatus").toString())){
			  str.append(" AND REQ.PUBLISH_STATUS !='"+searchParamMap.get("NEQ_singlePublishStatus").toString()+"'");
		}
		
		if(searchParamMap.containsKey("NEQ_vendorConfirmStatus") && !StringUtils.isEmpty(searchParamMap.get("NEQ_vendorConfirmStatus").toString())){
			  str.append(" AND REQ.VENDOR_CONFIRM_STATUS !='"+searchParamMap.get("NEQ_vendorConfirmStatus").toString()+"'");
		}
		
		//------供应商
		//------采购商权限过滤
		if(searchParamMap.containsKey("IN_group.id") && !StringUtils.isEmpty(searchParamMap.get("IN_group.id").toString())){
			List<Long> groupIds=(List<Long>) searchParamMap.get("IN_group.id");
			if(null!=groupIds && groupIds.size()>0){
				StringBuffer groupParam = new StringBuffer();
				for (Long id : groupIds) {
					groupParam.append(id).append(",");
				}
				if(groupParam.length()>0){
					str.append(" AND REQ.PURCHASING_GROUP_ID IN ("+groupParam.subSequence(0, groupParam.length()-1).toString()+")");
				}else{
					str.append(" AND REQ.PURCHASING_GROUP_ID IN (-1)");
				}
			}	
		}
		//------采购商
		
		return str.toString();
	}
	
	/**
	 * 获取要货计划列表的详细（供货计划）
	 * 
	 * @return
	 */
	public Page<PurchaseOrderItemPlanEntity>  getPurchaseGoodsRequestItemPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber-1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemPlanEntity.class);
		return purchaseOrderItemPlanDao.findAll(spec,pagin);
	}
	
	
	
	/**
	 * 导出要货计划列表（获取所有的）
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseGoodsRequestEntity> getPurchaseGoodsRequestList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseGoodsRequestEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseGoodsRequestEntity.class);
		return purchaseGoodsRequestDao.findAll(spec);
	}
	
	/**
	 * 导出要货计划详情列表列表（要货计划子项目）
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseOrderItemPlanEntity> getPurchaseGoodsRequestItemList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<PurchaseOrderItemPlanEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), PurchaseOrderItemPlanEntity.class);
		return purchaseOrderItemPlanDao.findAll(spec);
	}
	
	
	
	
	/**
	 * 获取单个要货计划列表
	 * 
	 * @return
	 */
	public PurchaseGoodsRequestEntity getPurchaseGoodsRequestById(Long goodsId) {
		return purchaseGoodsRequestDao.findOne(goodsId)	;
	}
	
	/**
	 * 获取供货计划
	 * 
	 * @return
	 */
	public PurchaseOrderItemPlanEntity getPurchaseOrderItemPlan(Long id) {
		return purchaseOrderItemPlanDao.findOne(id)	;
	}
	
	/**
	 * 获取供货计划临时记录
	 * 
	 * @return
	 */
	public PurchaseOrderItemPlanTemEntity getPurchaseOrderItemPlanTemp(Long id) {
		return purchaseOrderItemPlanTemDao.findOne(id)	;
	}
	
	/**
	 * 通过要货计划的id获取供货计划
	 * 
	 * @return
	 */
	public List<PurchaseOrderItemPlanEntity>  getPoItemplanListBygoodsId(Long goodsId) {
		return purchaseOrderItemPlanDao.getPoItemplanListBygoodsId(goodsId);
	}

	
	//订单单位转基本单位
	public Double orderQtyToBaseQty(String fenzi ,String fenmu ,Double qty ){
		Double ty = qty;
		if(!StringUtils.isEmpty( fenzi)  &&  ! StringUtils.isEmpty( fenmu) ){
			Double   fennzi  =  Double.valueOf(fenzi);//分子
			Double   fennmu  =  Double.valueOf(fenmu);  //分母
			ty =BigDecimalUtil.div(  BigDecimalUtil.mul(qty, fennmu, 3)  , fennzi, 3) ;
		}
		return ty;
	}
	
	//基本单位转转订单单位
	public Double baseQtyToOrderQty(String fenzi ,String fenmu ,Double qty ){
		Double ty = qty;
		if(!StringUtils.isEmpty( fenzi)  &&  ! StringUtils.isEmpty( fenmu) ){
			Double   fennzi  =  Double.valueOf(fenzi);//分子
			Double   fennmu  =  Double.valueOf(fenmu);  //分母
			ty = BigDecimalUtil.mul( qty, BigDecimalUtil.div(fennzi, fennmu, 3) ,3);
		}
		return ty;
	}
	//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	
	
	/**
	 * 要货计划直接匹配订单
	 * 
	 * @return
	 */
	public void goodsRequestMatchPo(Long goodsId) {	
		
			List<PurchaseOrderItemEntity> poItemList = new ArrayList<PurchaseOrderItemEntity>();
			PurchaseOrderItemEntity poItem=null; 
			
			List<PurchaseOrderItemPlanEntity> poItemPlanList  = new ArrayList<PurchaseOrderItemPlanEntity>();
			PurchaseOrderItemPlanEntity poItemPlan=null;
			
			List<PurchaseGoodsRequestEntity> goodsRequestList  = new ArrayList<PurchaseGoodsRequestEntity>();
			PurchaseGoodsRequestEntity  goodsRequest=null;
			
			Boolean flag = isOutData();
			
			//查询当前的要货计划
			PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findOne(goodsId);

			//通过查询已确认（不等于已发货（包括未发货和部分发货的数量））的订单的行创建时间的先后匹配（工厂，物料和供应商）
			List<PurchaseOrderItemEntity> poItemList1 = purchaseOrderItemDao.findGoodsRequestMatchPo(goods.getFactory().getId(),goods.getMaterial().getId(),goods.getVendor().getId());

			Double number =Double.valueOf(goods.getSurQry());//基本数量
			
			for(PurchaseOrderItemEntity item : poItemList1){
				poItem = new PurchaseOrderItemEntity();//订单行
				poItemPlan = new PurchaseOrderItemPlanEntity();//供货计划
				poItemPlan.setShipType(PurchaseConstans.SHIP_TYPE_NORMAL);//正常
				goodsRequest = new PurchaseGoodsRequestEntity();//要货计划
				if(item.getSurBaseQty() <= 0.01){
					continue;
				}
				
				
				if(BigDecimalUtil.sub(item.getSurBaseQty(), number)  >= 0  &&  number>0){ //订单行满足要货计划（多余和正好满足）
					//订单行项目
					poItem=item;
					
					//设置剩余基本数量
					poItem.setSurBaseQty(BigDecimalUtil.sub(poItem.getSurBaseQty(),number));
					//设置剩余订单数量
					Double  oNumber = baseQtyToOrderQty(item.getBPUMN(),item.getBPUMZ(),number);
					poItem.setSurOrderQty(BigDecimalUtil.sub(poItem.getSurOrderQty(),oNumber));
					
					
					//供货计划的添加
					poItemPlan.setOrder(poItem.getOrder());//订单（订单号和采购组）
					poItemPlan.setOrderItem(poItem);  	//订单行项目
					poItemPlan.setItemNo(poItem.getItemNo());//行号
					poItemPlan.setGoodsRequestId(goods.getId());
					poItemPlan.setPurchaseGoodsRequest(goods);//要货计划（工厂，物料，预计到货日期，物流天数，供应商，以及各种状态）
					poItemPlan.setMaterial(materialDao.findOne(goods.getMaterial().getId()));
					poItemPlan.setAbolished(0);
					
					
					poItemPlan.setOrderQty(oNumber);//要货的订单数量
					poItemPlan.setBaseQty(number);//要货的基本数量
					
					poItemPlan.setUndeliveryQty(oNumber);
					poItemPlan.setRequestTime(goods.getRq());//要求到货时间
					poItemPlan.setCurrency(poItem.getOrder().getWaers());//币种waers
					poItemPlan.setUnitName(poItem.getMeins());//订单单位
					poItemPlan.setDeliveryQty(0.00);	// 已发数量
					poItemPlan.setToDeliveryQty(0.00);// 已创建未发数量
					poItemPlan.setReceiveQty(0.00);// 实收数量
					poItemPlan.setReturnQty(0.00);// 退货数量
					poItemPlan.setDiffQty(0.00);	// 差异数量
					poItemPlan.setOnwayQty(0.00);	// 在途数量
					poItemPlan.setUndeliveryQty(0.00);// 未发数量
					poItemPlan.setConfirmStatus(0);//确认状态(初始化中4个状态都是为0)
					poItemPlan.setPublishStatus(0);//发布状态
					poItemPlan.setDeliveryStatus(0);//发货状态(初始化中4个状态都是为0)
					poItemPlan.setReceiveStatus(0);//收货状态(初始化中4个状态都是为0)
					if(flag){
						poItemPlan.setIsOutData(1);
					}
					
					
					//要货计划的修改
					goodsRequest = goods;
					goodsRequest.setIsFull(1);
					goodsRequest.setSurQry("0.000");
					poItemList.add(poItem);
					poItemPlanList.add(poItemPlan);
					goodsRequestList.add(goodsRequest);
					break;
					
				}else if(  (BigDecimalUtil.sub(item.getSurBaseQty(), number)  < 0 ) && item.getSurBaseQty() >0.01 &&  number>0){//订单行未满足要货计划（未满足）
					//订单行项目
					poItem=item;
					//要货计划的添加
					poItemPlan.setOrder(poItem.getOrder());//订单（订单号和采购组）
					poItemPlan.setOrderItem(poItem);  	//订单行项目
					poItemPlan.setItemNo(poItem.getItemNo());//行号
					poItemPlan.setGoodsRequestId(goods.getId());
					poItemPlan.setPurchaseGoodsRequest(goods);//要货计划（工厂，物料，预计到货日期，物流天数，供应商，以及各种状态）
					poItemPlan.setMaterial(materialDao.findOne(goods.getMaterial().getId()));
					poItemPlan.setAbolished(0);
					poItemPlan.setOrderQty(poItem.getSurOrderQty());//供货计划订单数量 == 订单的订单数量
					poItemPlan.setBaseQty(poItem.getSurBaseQty());//供货计划基本数量 == 订单的基本数量
					
					poItemPlan.setUndeliveryQty(poItem.getSurOrderQty());

					poItemPlan.setRequestTime(goods.getRq());//要求到货时间
					poItemPlan.setCurrency(poItem.getOrder().getWaers());//币种waers
					poItemPlan.setUnitName(poItem.getMeins());//订单单位
					poItemPlan.setDeliveryQty(0.00);	// 已发数量
					poItemPlan.setToDeliveryQty(0.00);// 已创建未发数量
					poItemPlan.setReceiveQty(0.00);// 实收数量
					poItemPlan.setReturnQty(0.00);// 退货数量
					poItemPlan.setDiffQty(0.00);	// 差异数量
					poItemPlan.setOnwayQty(0.00);	// 在途数量
					poItemPlan.setUndeliveryQty(0.00);// 未发数量
					poItemPlan.setConfirmStatus(0);//确认状态(初始化中4个状态都是为0)
					poItemPlan.setPublishStatus(0);//发布状态
					poItemPlan.setDeliveryStatus(0);//发货状态(初始化中4个状态都是为0)
					poItemPlan.setReceiveStatus(0);//收货状态(初始化中4个状态都是为0)
					if(flag){
						poItemPlan.setIsOutData(1);
					}
					//要货计划的修改
					number= BigDecimalUtil.sub(number, poItem.getSurBaseQty()); 
					
					goodsRequest = goods;
					
					if(number <= 0 ){
						goodsRequest.setIsFull(1);
						number=0.000;
					}
					
					goodsRequest.setSurQry(number+"");
					
					poItem.setSurOrderQty(0.000);
					poItem.setSurBaseQty(0.000);
					
					poItemList.add(poItem);
					poItemPlanList.add(poItemPlan);
					goodsRequestList.add(goodsRequest);
					continue;
				}
			}
			
			//保存要货计划、供货计划、订单的行项目
			purchaseOrderItemDao.save(poItemList);
			purchaseOrderItemPlanDao.save(poItemPlanList);
			purchaseGoodsRequestDao.save(goodsRequestList);
			
			//更新发布状态和确认状态

			modifyGoodsRequestPublishStatus(goods.getId());

		 
		    modifyGoodsRequestConfirmStatus(goods.getId());
			
		}
	
	/**
	 * 获取所有未发布的要货计划
	 * @author chao.gu
	 * @param vos
	 * @param str
	 * @param beginRq
	 * @param endRq
	 * @return
	 */
	public Set<PurchaseGoodsRequestEntity> findNotPublishGoodsRequest(List<PurchaseGoodsRequestVO> vos,UserEntity user){
		Set<PurchaseGoodsRequestEntity> datalist=new HashSet<PurchaseGoodsRequestEntity>();
		//获取当前时间
		Date date = new Date();
		String currDate=DateUtil.dateToString(date);
		String end7=DateUtil.dateToString(DateUtil.addDay(date, 6));//短周期 7天
		String end15=DateUtil.dateToString(DateUtil.addDay(date, 14));//长周期 15天
		String endDate;
		for (PurchaseGoodsRequestVO vo : vos) {
			    if(2==vo.getType().intValue()){
			    	continue;//过滤类型剩余匹配数量
			    }
			    
			    MaterialEntity material=materialDao.findOne(vo.getMaterialId());
			    if(StringUtils.isNotEmpty(material.getCdqwl()) && "X".equals(material.getCdqwl())){//长周期
			    	endDate=end15;
			    }else{//短周期
			    	endDate=end7;
			    }
				List<PurchaseGoodsRequestEntity> list=purchaseGoodsRequestDao.getUnpublishGoodsRequest(vo.getFlag(),vo.getFactoryId(),vo.getPurchasingGroupId(),vo.getMaterialId(),vo.getMeins(),vo.getVendorId(),DateUtil.getTimestamp(currDate),DateUtil.getTimestamp(endDate));
				if(null!=list && list.size()>0){
					for (PurchaseGoodsRequestEntity entity : list) {
						if(Double.parseDouble(entity.getDhsl())>0 && (BigDecimalUtil.sub(Double.parseDouble(entity.getDhsl()), Double.parseDouble(entity.getSurQry())) > 0 )){
							datalist.add(entity);
						}
						
						
					}
				}
		}
		
		return datalist;
	}
	
	//发布要货计划(批量)
	public Boolean batchPublishGoodsRequest(List<PurchaseGoodsRequestVO> vos, UserEntity user) {
		Boolean flag = true;
		Timestamp curr=DateUtil.getCurrentTimestamp();
		Set<PurchaseGoodsRequestEntity> set = findNotPublishGoodsRequest(vos,user);
		for(PurchaseGoodsRequestEntity goods : set ){
			List<PurchaseOrderItemPlanEntity> planList = purchaseOrderItemPlanDao.getUnPublishPoItemplanListBygoodsId(goods.getId());
			for(PurchaseOrderItemPlanEntity ItemPlan : planList){
				ItemPlan=purchaseOrderItemPlanDao.findOne(ItemPlan.getId());
				if( !StringUtils.isEmpty(ItemPlan.getOrderItem().getLoekz())  ||  !StringUtils.isEmpty(ItemPlan.getOrderItem().getLockStatus()) || !StringUtils.isEmpty(ItemPlan.getOrderItem().getZlock()) || !StringUtils.isEmpty(ItemPlan.getOrderItem().getElikz()) ){
					//对于订单的明细具有删除，冻结，锁定，交货已完成的自动过滤
					continue;
				}
				ItemPlan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
				ItemPlan.setPublishTime(curr);
				ItemPlan.setPublishUser(user);
				purchaseOrderItemPlanDao.save(ItemPlan);
			}
		}
		
		//更新主表的发布状态
		for(PurchaseGoodsRequestEntity goods : set){
			modifyGoodsRequestPublishStatus(goods.getId());
		}

	/*	//添加供应商的消息
		for(PurchaseGoodsRequestEntity goods : set){
		     List<UserEntity> users = userServiceImpl.findByCompany(goods.getVendor().getId());
		     List<Long> userIdList = new ArrayList<Long>();
		     for( UserEntity us :  users){
				 userIdList.add(us.getId());
			 }
			warnMainService.warnMessageSet(goods.getId(), WarnConstant.GOODS_UN_CONFIRM, userIdList);
		}*/
		
		
		return flag;
	}
	
	
	
	//发布要货计划(单个)
	public Boolean singlePublishGoodsRequests(List<PurchaseOrderItemPlanEntity> list, UserEntity user) {
		Boolean flag = true;
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemPlanEntity ItemPlan : list){
			ItemPlan=purchaseOrderItemPlanDao.findOne(ItemPlan.getId());
			ItemPlan.setPublishStatus(PurchaseConstans.PUBLISH_STATUS_YES);
			ItemPlan.setPublishTime(curr);
			ItemPlan.setPublishUser(user);
			purchaseOrderItemPlanDao.save(ItemPlan);
			//更新主表的发布状态
			modifyGoodsRequestPublishStatus(ItemPlan.getGoodsRequestId());
		}

	
		
		
	/*	//添加供应商的消息`
		PurchaseOrderItemPlanEntity purchaseOrderItemPlan = purchaseOrderItemPlanDao.findOne(list.get(0).getId());
	     List<UserEntity> users = userServiceImpl.findByCompany(purchaseOrderItemPlan.getPurchaseGoodsRequest().getVendor().getId());
	     List<Long> userIdList = new ArrayList<Long>();
	     for( UserEntity us :  users){
			 userIdList.add(us.getId());
		 }
		warnMainService.warnMessageSet(purchaseOrderItemPlan.getPurchaseGoodsRequest().getId(), WarnConstant.GOODS_UN_CONFIRM, userIdList);*/
		return flag;
	}
	
	
	//修改主要货计划发布状态
	public void modifyGoodsRequestPublishStatus(Long id) {
		//查询由要货计划生成的供货计划
		PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findOne(id);
		Double dhsl = Double.valueOf(goods.getDhsl());
		//到货数量大于0的要货计划可以匹配
		if(dhsl <= 0){//到货数量小于等于0 直接改为未发布
			goods.setPublishStatus(0);//未发布
			purchaseGoodsRequestDao.save(goods);
		}else{
			//通过要货计划的id获取所有的的供货计划
			List<PurchaseOrderItemPlanEntity> orderItemPlanList = purchaseOrderItemPlanDao.getPoItemplanListBygoodsId(id);
			int allNumber = 0;
			int publishCount = 0;
			
			if(orderItemPlanList != null && orderItemPlanList.size() != 0 ){
				allNumber= orderItemPlanList.size();
				for(PurchaseOrderItemPlanEntity  orderItemPlan : orderItemPlanList){
					if(orderItemPlan.getPublishStatus() == 1){
						publishCount++;
					}
					
				}
			}

			if(goods != null){
				if( allNumber >0 && publishCount >0 && allNumber  ==  publishCount){
					goods.setPublishStatus(1);//发布
				}else if(  allNumber >   publishCount  &&  0  <  publishCount){
					goods.setPublishStatus(2);//部分发布
				}else{
					goods.setPublishStatus(0);//未发布
				}
				purchaseGoodsRequestDao.save(goods);
			}
		}
	}
	
	
	/**
	 *  删除供货计划 （ 原来：批量同意供应商驳回条件【采】）
	 * @param orders
	 */
	public Boolean deleteOrderItemPlan(List<PurchaseOrderItemPlanEntity> orderItemPlans){

		//无法批量的进行
		Long goodsId =orderItemPlans.get(0).getGoodsRequestId();//对于orderItemPlans goodsId是唯一的；
		for(PurchaseOrderItemPlanEntity orderItemPlan : orderItemPlans) {
			PurchaseGoodsRequestEntity purchaseGoods =   purchaseGoodsRequestDao.findOne(orderItemPlan.getGoodsRequestId());
			PurchaseOrderItemEntity purchaseOrderItem = purchaseOrderItemDao.findOne(orderItemPlan.getOrderItem().getId());
			
			Double orderItemPlanNumber = orderItemPlan.getOrderQty();
			
			Double  bNumber = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,orderItemPlanNumber);
			//数量转换
			
			//释放订单行的资源和要货计划的资源
			if(BigDecimalUtil.sub(bNumber, Double.valueOf(purchaseGoods.getDhsl()) ) >= 0){
				
				purchaseGoods.setSurQry(bNumber.toString());
				purchaseGoods.setIsFull(0);
				
				
				//订单的数量
				if(BigDecimalUtil.sub(orderItemPlanNumber, purchaseOrderItem.getOrderQty() ) == 0){
					
					purchaseOrderItem.setSurOrderQty(purchaseOrderItem.getOrderQty());//订单数量
					//数量转换
					Double  bNumber0 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,purchaseOrderItem.getOrderQty());
					purchaseOrderItem.setSurBaseQty(bNumber0);//基本订单数量
					
					
				}else if(BigDecimalUtil.sub(orderItemPlanNumber,purchaseOrderItem.getOrderQty()  ) > 0){
					
					purchaseOrderItem.setSurOrderQty(purchaseOrderItem.getOrderQty());//订单数量
					
					//数量转换
					Double  bNumber1 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,purchaseOrderItem.getOrderQty());
					purchaseOrderItem.setSurBaseQty(bNumber1);//基本订单数量
					
				}else if(BigDecimalUtil.sub(orderItemPlanNumber,purchaseOrderItem.getOrderQty()  ) < 0){
					
					if(BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()))   == 0      ){
						
						purchaseOrderItem.setSurOrderQty(BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
					
						//数量转换
						Double  bNumber2 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
						purchaseOrderItem.setSurBaseQty(bNumber2);//基本订单数量
						
						
					}else if(BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()))   > 0     ){
						
						purchaseOrderItem.setSurOrderQty(BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
						
						//数量转换
						Double  bNumber3 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
						purchaseOrderItem.setSurBaseQty(bNumber3);//基本订单数量
						
					}else if(BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()))   < 0     ){
						
						purchaseOrderItem.setSurOrderQty(purchaseOrderItem.getOrderQty());
						
						//数量转换
						Double  bNumber4 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,purchaseOrderItem.getOrderQty());
						purchaseOrderItem.setSurBaseQty(bNumber4);//基本订单数量
						
					}
				}
			}
			

			if(BigDecimalUtil.sub(bNumber, Double.valueOf(purchaseGoods.getDhsl()) ) < 0){
				//要货计划的数量
				if(BigDecimalUtil.sub(Double.valueOf(purchaseGoods.getDhsl()) , BigDecimalUtil.add(bNumber, Double.valueOf(purchaseGoods.getSurQry())))   == 0      ){
					
					Double number = BigDecimalUtil.add(bNumber, Double.valueOf(purchaseGoods.getSurQry()));
					purchaseGoods.setSurQry(number.toString());
					purchaseGoods.setIsFull(0);
				
				}else if(BigDecimalUtil.sub(Double.valueOf(purchaseGoods.getDhsl()) , BigDecimalUtil.add(orderItemPlanNumber, Double.valueOf(purchaseGoods.getSurQry())))   > 0 ){
					
					Double number = BigDecimalUtil.add(bNumber, Double.valueOf(purchaseGoods.getSurQry()));
					purchaseGoods.setSurQry(number.toString());
					purchaseGoods.setIsFull(0);
				}else if(BigDecimalUtil.sub(Double.valueOf(purchaseGoods.getDhsl()) , BigDecimalUtil.add(orderItemPlanNumber, Double.valueOf(purchaseGoods.getSurQry())))   < 0  ){

					purchaseGoods.setSurQry(purchaseGoods.getDhsl());
					purchaseGoods.setIsFull(0);
				}
				//订单行的数量
				if(BigDecimalUtil.sub(orderItemPlanNumber, purchaseOrderItem.getOrderQty() ) == 0){
					
					purchaseOrderItem.setSurOrderQty(purchaseOrderItem.getOrderQty());
					
					//数量转换
					Double  bNumber00 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,purchaseOrderItem.getOrderQty());
					purchaseOrderItem.setSurBaseQty(bNumber00);//基本订单数量
					
				}else if(BigDecimalUtil.sub(orderItemPlanNumber,purchaseOrderItem.getOrderQty()  ) > 0){
					
					purchaseOrderItem.setSurOrderQty(purchaseOrderItem.getOrderQty());
					//数量转换
					Double  bNumber11 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,purchaseOrderItem.getOrderQty());
					purchaseOrderItem.setSurBaseQty(bNumber11);//基本订单数量
					
				}else if(BigDecimalUtil.sub(orderItemPlanNumber,purchaseOrderItem.getOrderQty()  ) < 0){
					if(BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()))   == 0      ){

					
						purchaseOrderItem.setSurOrderQty(BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
					
						//数量转换
						Double  bNumber22 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
						purchaseOrderItem.setSurBaseQty(bNumber22);//基本订单数量
					
					}else if(BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()))   > 0     ){
						
						
						purchaseOrderItem.setSurOrderQty(BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
						//数量转换
						Double  bNumber33 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()));
						purchaseOrderItem.setSurBaseQty(bNumber33);//基本订单数量
					
					}else if(BigDecimalUtil.sub(purchaseOrderItem.getOrderQty(), BigDecimalUtil.add(orderItemPlanNumber, purchaseOrderItem.getSurOrderQty()))   < 0     ){
						
						purchaseOrderItem.setSurOrderQty(purchaseOrderItem.getOrderQty());
						
						//数量转换
						Double  bNumber44 = orderQtyToBaseQty(orderItemPlan.getOrderItem().getBPUMN(),orderItemPlan.getOrderItem().getBPUMZ() ,purchaseOrderItem.getOrderQty());
						purchaseOrderItem.setSurBaseQty(bNumber44);//基本订单数量
					}
				}
			}
			//删除供货计划
			purchaseOrderItemPlanDao.abolish(orderItemPlan.getId());
			//保存要货计划
			purchaseGoodsRequestDao.save(purchaseGoods);
			//保存订单行
			purchaseOrderItemDao.save(purchaseOrderItem);
		}
		
		//重新匹配订单
		goodsRequestMatchPo(goodsId);
		//修改要货计划的确认状态
		modifyGoodsRequestConfirmStatus(goodsId);
		//修改发布的状态
		modifyGoodsRequestPublishStatus(goodsId);
		
		return true;
	}
	
	//??????????????????????????????????????????????????????????????????????????????????????????????????????????????????????
	
	
	//确认要货计划明细(供应商)
	public Boolean confirmOrderItemPlan(List<PurchaseOrderItemPlanEntity> list, UserEntity user) {
		Boolean  flag = false;
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemPlanEntity vo :  list){
			PurchaseOrderItemPlanEntity orderItemPlan = purchaseOrderItemPlanDao.findById(vo.getId());
			if(orderItemPlan != null){
				orderItemPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
				orderItemPlan.setConfirmUser(user);
				orderItemPlan.setConfirmTime(curr);
				purchaseOrderItemPlanDao.save(orderItemPlan);
				//修改要货计划的确认状态
				modifyGoodsRequestConfirmStatus(orderItemPlan.getGoodsRequestId());
			}
		}
		flag = true;
		
		return flag;
	}
	
	
	/**
	 * 获取所有未确认的要货计划【供】
	 * @author chao.gu
	 * @param vos
	 * @param str
	 * @param beginRq
	 * @param endRq
	 * @return
	 */
	public Set<PurchaseGoodsRequestEntity> findNotVendorConfirmGoodsRequest(List<PurchaseGoodsRequestVO> vos,UserEntity user){
		Set<PurchaseGoodsRequestEntity> datalist=new HashSet<PurchaseGoodsRequestEntity>();
		for (PurchaseGoodsRequestVO vo : vos) {
			  if(2==vo.getType().intValue()){
			    	continue;//过滤类型剩余匹配数量
			    }
				List<PurchaseGoodsRequestEntity> list=purchaseGoodsRequestDao.getUnVendorConfirmGoodsRequest(vo.getFlag(),vo.getFactoryId(),vo.getPurchasingGroupId(),vo.getMaterialId(),vo.getMeins(),vo.getVendorId(),DateUtil.getTimestamp(vo.getBeginRq()),DateUtil.getTimestamp(vo.getEndRq()));
				if(null!=list && list.size()>0){
					datalist.addAll(list);
				}
		}
		return datalist;
	}
	
	//确认要货计划(供应商批量)
	public Boolean batchConfirmGoodsRequest(List<PurchaseGoodsRequestVO> vos, UserEntity user) {
		Boolean  flag = false;
		Timestamp curr=DateUtil.getCurrentTimestamp();
		Set<PurchaseGoodsRequestEntity> set =findNotVendorConfirmGoodsRequest(vos, user);
		//查询要货计划的下面未确认
		for(PurchaseGoodsRequestEntity item : set){
			List<PurchaseOrderItemPlanEntity> orderItemPlanList = purchaseOrderItemPlanDao.getNotInConfirmPoItemplanListBygoodsId(item.getId());
			for(PurchaseOrderItemPlanEntity vo :  orderItemPlanList){
				PurchaseOrderItemPlanEntity orderItemPlan = null ;
				orderItemPlan = purchaseOrderItemPlanDao.findById(vo.getId());
				if(orderItemPlan != null){
					orderItemPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_YES);
					orderItemPlan.setConfirmUser(user);
					orderItemPlan.setConfirmTime(curr);
					purchaseOrderItemPlanDao.save(orderItemPlan);
				}
			}
			
			//修改要货计划的确认状态
			modifyGoodsRequestConfirmStatus(item.getId());
			
		}

		flag = true;
		
		return flag;
	}
	
	
	
	
	
	
	//驳回要货计划明细(供应商)//需要添加待办和预警的提醒
	public Boolean rejectOrderItemPlan(List<PurchaseOrderItemPlanEntity> list, UserEntity user,String reason) {
		Boolean  flag = false;
		Long goodsId = list.get(0).getPurchaseGoodsRequest().getId();
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemPlanEntity orderItemPlan :  list){
				orderItemPlan.setConfirmStatus(PurchaseConstans.CONFIRM_STATUS_NO);
				orderItemPlan.setConfirmUser(user);
				orderItemPlan.setConfirmTime(curr);
				orderItemPlan.setRejectReason(reason);
				orderItemPlan.setRejectUser(user);
				orderItemPlan.setRejectTime(curr);
				purchaseOrderItemPlanDao.save(orderItemPlan);	
		}
		flag = true;
		//修改要货计划的确认状态
		modifyGoodsRequestConfirmStatus(goodsId);
		
		return flag;
	}
	
	/**
	 * 批量拒绝供应商驳回条件【采】
	 * @param orders
	 */
	public void vetoOrderItemPlans(List<PurchaseOrderItemPlanEntity> orderItemPlans, UserEntity user,Integer confirmStatus,String reason){
		Timestamp curr=DateUtil.getCurrentTimestamp();
		for(PurchaseOrderItemPlanEntity orderItemPlan : orderItemPlans) {
			//采购商驳回供应商驳回条件
			if(PurchaseConstans.VETO_STATUS_VETO.intValue()==confirmStatus.intValue()){
				orderItemPlan.setVetoStatus(confirmStatus);
				orderItemPlan.setVetoReason(reason);
				orderItemPlan.setVetoUser(user);
				orderItemPlan.setVetoTime(curr);
				orderItemPlan.setConfirmStatus(-2);
				orderItemPlan.setConfirmTime(curr);
				orderItemPlan.setConfirmUser(user);
			}
			purchaseOrderItemPlanDao.save(orderItemPlan);
		}
	}
	
	
	//修改主要货计划确认状态的状态
	public void modifyGoodsRequestConfirmStatus(Long id) {
		//查询由要货计划生成的供货计划
		PurchaseGoodsRequestEntity goods = purchaseGoodsRequestDao.findOne(id);
		List<PurchaseOrderItemPlanEntity> orderItemPlanList = purchaseOrderItemPlanDao.getPoItemplanListBygoodsId(id);

		int allNumber = 0;
		int confirmCount = 0;
		
		if(orderItemPlanList != null && orderItemPlanList.size() != 0 ){
			allNumber = orderItemPlanList.size();
			for(PurchaseOrderItemPlanEntity  orderItemPlan : orderItemPlanList){
				if(orderItemPlan.getConfirmStatus()== 1 ){
					confirmCount++;
				}
				
			}
		}
		if(goods != null){
			if( allNumber >0 && confirmCount >0 &&  confirmCount == allNumber){
				goods.setVendorConfirmStatus(1);//已确认
			}else if(   allNumber >   confirmCount  &&  0  <  confirmCount ){
				goods.setVendorConfirmStatus(2);//部分确认
			}else{
				goods.setVendorConfirmStatus(0);//未确认
			}
			purchaseGoodsRequestDao.save(goods);
		}
	}
	
	/**
	 * 导出要货计划(通过主导出)
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<GoodsRequestTransfer> getPurchaseGoodsRequestExport(Map<String, Object> searchParamMap) {
		List<PurchaseGoodsRequestEntity> list = getPurchaseGoodsRequestList(searchParamMap);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
	   List<GoodsRequestTransfer> ret = new ArrayList<GoodsRequestTransfer>();
	   GoodsRequestTransfer trans = null;
	   
	   if(list != null && list.size() != 0){
		   for(PurchaseGoodsRequestEntity purchaseGoodsRequest : list ){
			   trans = new GoodsRequestTransfer();
			   
			   FactoryEntity fac=purchaseGoodsRequest.getFactory();
			   MaterialEntity mat =purchaseGoodsRequest.getMaterial();
			   PurchasingGroupEntity group=purchaseGoodsRequest.getGroup();
			   OrganizationEntity vendor=purchaseGoodsRequest.getVendor();
			   trans.setFactoryCode(fac.getCode());//工厂的代码
			   trans.setFactoryName(fac.getName());//工厂的名称
			   trans.setGroupCode(group.getCode());//采购组代码
			   trans.setGroupName(group.getName());//采购组名称
			   trans.setMaterialcode(mat.getCode());//物料号、
			   trans.setMaterialName(mat.getName());//物料描述
			   trans.setUnitName(purchaseGoodsRequest.getMeins());//单位
			   trans.setOrderQty(purchaseGoodsRequest.getDhsl());//到货数量
			   trans.setRq(purchaseGoodsRequest.getRq().toString());//预计到货日期
			   trans.setYsts(purchaseGoodsRequest.getYsts());//物流天数
			   trans.setVendorCode(vendor.getCode());//供应商编码
			   trans.setVendorName(vendor.getName());//供应商名称
			   trans.setPublishStatus(purchaseGoodsRequest.getPublishStatus().toString());//发布状态
			   trans.setConfirmStatus(purchaseGoodsRequest.getVendorConfirmStatus().toString());//确认状态
			   ret.add(trans);
		   }
	   }

		return ret;
	}
	
	
	
	/**
	 * 导出要货计划(通过子导出)
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public List<PurchaseOrderItemPlanTransfer> getPurchaseGoodsRequestItemExport(Map<String, Object> searchParamMap) {
		List<PurchaseOrderItemPlanEntity> list = getPurchaseGoodsRequestItemList(searchParamMap);
		if(CollectionUtils.isEmpty(list)) {
			return null;
		}
	   List<PurchaseOrderItemPlanTransfer> ret = new ArrayList<PurchaseOrderItemPlanTransfer>();
	   PurchaseOrderItemPlanTransfer trans = null;
	   
	   if(list != null && list.size() != 0){

			for(PurchaseOrderItemPlanEntity  purchaseOrderItemPlan : list){
				PurchaseGoodsRequestEntity request=purchaseOrderItemPlan.getPurchaseGoodsRequest();
				FactoryEntity fac= request.getFactory();
				MaterialEntity mat = request.getMaterial();
				PurchasingGroupEntity group=request.getGroup();
				OrganizationEntity vendor=request.getVendor();
				trans = new PurchaseOrderItemPlanTransfer();
				trans.setOrderCode(purchaseOrderItemPlan.getOrder() == null ? null : purchaseOrderItemPlan.getOrder().getOrderCode());
				trans.setGroupName(fac.getName());
				trans.setItemNo(purchaseOrderItemPlan.getItemNo() == null ? null : purchaseOrderItemPlan.getItemNo().toString());
				trans.setFactoryName(fac.getName());
				trans.setMaterialName(mat.getName());
				trans.setUnitName(mat.getUnit() );
				trans.setOrderQty(purchaseOrderItemPlan.getOrderQty().toString());
				trans.setBaseQty(purchaseOrderItemPlan.getBaseQty().toString());
				trans.setRq(purchaseOrderItemPlan.getPurchaseGoodsRequest().getRq()+"");
				trans.setYsts(purchaseOrderItemPlan.getPurchaseGoodsRequest().getYsts());
				trans.setVendorCode(vendor.getCode());
				trans.setVendorName(vendor.getName());
				trans.setPublishStatus(purchaseOrderItemPlan.getPublishStatus().toString());
				trans.setConfirmStatus(purchaseOrderItemPlan.getConfirmStatus().toString());
				trans.setDeliveryStatus(purchaseOrderItemPlan.getDeliveryStatus().toString());
				trans.setReceiveStatus(purchaseOrderItemPlan.getReceiveStatus().toString());
				ret.add(trans);
			}
		   
		   
	   }

		return ret;
	}
	
	
	
	
	
	//-------------------------------------------------- 要货计划的修改-------------------------------------------------------------------------
	
	//修改要货计划
	public Map<String,Object>  editMainGoodsRequest(List<PurchaseGoodsRequestVO> list) throws Exception {
		
		Map<String,Object> map = new HashMap<String, Object>();
		StringBuffer msg = new StringBuffer();
		int i=0;
		for (PurchaseGoodsRequestVO vo : list) {
			PurchaseGoodsRequestEntity goods  = new PurchaseGoodsRequestEntity();
			Long goodsId = vo.getId();
			if(null==vo.getDhsl()){
				continue;
			}
			Double count  =vo.getDhsl();
			
			//获取要货计划当前要货计划
		    goods = purchaseGoodsRequestDao.findById(goodsId);
			Double dhsl =Double.valueOf(goods.getDhsl()); 
			
			if(BigDecimalUtil.sub(count, dhsl)  ==  0){
				//未修改不做任何操作
				continue;
			}else if(BigDecimalUtil.sub(count, dhsl) > 0){
				//改大
				goods.setIsModify(1);
				goods.setDhsl(count.toString());
				goods.setIsFull(0);
				
				Double surQry = BigDecimalUtil.sub(count, dhsl);
				
				Double qry = BigDecimalUtil.add(surQry, Double.valueOf(goods.getSurQry()));
				goods.setSurQry(qry.toString());
				i++;
			}else if(BigDecimalUtil.sub(count, dhsl) < 0){
				//改小
	            boolean blean = manualChangeGoodsRequestSmall(goods ,count,msg);
				if(blean){
					goods.setIsModify(1);
					i++;
				}	
			}
			
			//保存当前要货计划
			purchaseGoodsRequestDao.save(goods);
			
			//自动匹配订单
			//匹配订单
			goodsRequestMatchPo(goodsId);
		}
		if(i>0){
			msg.append("修改成功！");
		}else{
			msg.append("修改失败！");
		}
		map.put("message", msg.toString());
	    map.put("success", true);
		return map;
	}
	
	//对于改小要货计划（手动）
	public boolean manualChangeGoodsRequestSmall(PurchaseGoodsRequestEntity goods,Double count,StringBuffer msg) throws Exception {
	
		boolean flag=false;
		//根据当前要货计划查询是否有以审核的asn，控制最小的修改量
		 List<DeliveryItemEntity> dItemList= deliveryItemDao.findDeliveryItemEntitysBygoosdId(goods.getId());
		 Double modifyMinCount = 0d;
		 if(dItemList != null && dItemList.size() != 0){
			 for( DeliveryItemEntity   item : dItemList){
				 //数量转换
				 Double  number = orderQtyToBaseQty(item.getOrderItem().getBPUMN(),item.getOrderItem().getBPUMZ(),item.getDeliveryQty());
				 modifyMinCount =BigDecimalUtil.add(modifyMinCount,number );  
			 }
		 }
		 //当最小值大于0，且修改值 - 最小修改值 小于0 无法修改		 
		 if(modifyMinCount > 0 &&  BigDecimalUtil.sub(count, modifyMinCount) < 0){
			 msg.append("预计到货日期为：").append(DateUtil.dateToString(goods.getRq(), DateUtil.DATE_FORMAT_YYYY_MM_DD)).append("要货计划,");
			 msg.append( "要货计划修改值"+count+",不能小于已创建ASN总数据"+modifyMinCount+"！");
		 }else{
			 //todo（对于供货计划  未审核asn做处理的方法）
			 goodsRequestSmall(goods,count );
			 flag=true;
		 }
		 
		 return flag;
	}
	
	//手动改小的逻辑
	public void goodsRequestSmall(PurchaseGoodsRequestEntity goods,Double count) throws Exception{
		 ILogger logger = new FileLogger();
		//拷贝相关类
		PurchaseGoodsRequestEntity newGoods = new PurchaseGoodsRequestEntity();
		BeanUtil.copyPropertyNotNull((PurchaseGoodsRequestEntity) goods,newGoods);
		newGoods.setDhsl(count.toString());
		
		PurchaseGoodsRequestSyncService purchaseGoodsRequestSyncService = SpringContextUtils.getBean("purchaseGoodsRequestSyncService");	
		purchaseGoodsRequestSyncService.handlePurchaseGoodsRequest(newGoods,goods, logger ,true);
		

	}
	
	//判断是否有登录信息
	public  Boolean isOutData(){
		try{
			ShiroUser user =  (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		}catch (Exception e) {
			return true;
		}
		return false;
	}
	
	//*********************************要货报表相关*******************************************//
	public GoodsRequestReportHeadViewVo goodsReportHeadGet() throws Exception{
		GoodsRequestReportHeadVo headVo =new GoodsRequestReportHeadVo(); 
		//获取本周的星期一
		Date date = new Date();
		if(!IsMonday(date)){
			date = getMonday(date); 
		}
		
		Date startTime = date;
		Date endTime = getEndTime(startTime);
		
		Class clazz = headVo.getClass();  
		Object object = clazz.newInstance();
		Field[] fields = clazz.getDeclaredFields();  
	
		for(Field field: fields){
			Calendar cd = Calendar.getInstance();  
		    cd.setTime(startTime);  
            String name = field.getName();
            String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
            Method method = clazz.getMethod(methodStr,new Class[]{field.getType()});
        	 if(field.getType().getSimpleName().equals("String")){
                 method.invoke(object, cd.get(Calendar.YEAR)+"/"+(cd.get(Calendar.MONTH) + 1)+"/"+cd.get(Calendar.DAY_OF_MONTH));
             }
        	 startTime=getNextDay(startTime);
        }
		headVo =(GoodsRequestReportHeadVo) object;
		
		
		Date startTimeTmp = date;
		GoodsRequestReportHeadViewVo viewVo = new GoodsRequestReportHeadViewVo();
		viewVo.setHeadVo(headVo);
		
		Calendar cdOne = Calendar.getInstance();  
		cdOne.setTime(startTimeTmp);  
		viewVo.setMonOne(cdOne.get(Calendar.YEAR)+"/"+(cdOne.get(Calendar.MONTH) + 1)+"/"+cdOne.get(Calendar.DAY_OF_MONTH));
		
		Calendar cdTwo = Calendar.getInstance();
		startTimeTmp = getSeventDay(startTimeTmp);
		cdTwo.setTime(startTimeTmp);  
		viewVo.setMonTwo(cdTwo.get(Calendar.YEAR)+"/"+(cdTwo.get(Calendar.MONTH) + 1)+"/"+cdTwo.get(Calendar.DAY_OF_MONTH));
		
		
		Calendar cdThree = Calendar.getInstance();  
		startTimeTmp = getSeventDay(startTimeTmp);
		cdThree.setTime(startTimeTmp);  
		viewVo.setMonThree(cdThree.get(Calendar.YEAR)+"/"+(cdThree.get(Calendar.MONTH) + 1)+"/"+cdThree.get(Calendar.DAY_OF_MONTH));
		
		Calendar cdFour = Calendar.getInstance();  
		startTimeTmp = getSeventDay(startTimeTmp);
		cdFour.setTime(startTimeTmp);  
		viewVo.setMonFour(cdFour.get(Calendar.YEAR)+"/"+(cdFour.get(Calendar.MONTH) + 1)+"/"+cdFour.get(Calendar.DAY_OF_MONTH));
		
		return viewVo;
	}
	
	//数据的重组
	public Page<GoodsRequestReportVo> getPurchaseVenodrPlans(int pageNumber, int pageSize, Map<String, Object> searchParamMap) throws Exception {		
		//searchParamMap.put("IN_headerName", getSearchParamMapHead());
		
       PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, null);
		
		StringBuffer sql=new StringBuffer();
		sql.append(" FROM");
		sql.append(" PURCHASE_GOODS_REQUEST A ");
		
		sql.append(" LEFT JOIN QEWEB_MATERIAL C ON A.MATERIAL_ID = C.ID  ");
		sql.append(" LEFT JOIN QEWEB_FACTORY D ON A.FACTORY_ID = D.ID  ");
		sql.append(" LEFT JOIN QEWEB_PURCHASING_GROUP G ON A.PURCHASING_GROUP_ID = G.ID  ");
		sql.append(" LEFT JOIN QEWEB_ORGANIZATION E ON A.VENDOR_ID = E.ID ");
		
		sql.append(" WHERE ");
		sql.append(" A.ABOLISHED = 0 ");
		//sql.append(" AND A.VENDOR_CONFIRM_STATUS = 1 ");
		
		/*if(searchParamMap.containsKey("IN_headerName")){
			if( !StringUtils.isEmpty(searchParamMap.get("IN_headerName").toString())){
				sql.append(" AND B.HEADER_NAME IN "+searchParamMap.get("IN_headerName")+"");//表头
			}
		}*/
		
		if(searchParamMap.containsKey("IN_group.id")){
			if( !StringUtils.isEmpty(searchParamMap.get("IN_group.id").toString())){
				String tmp = searchParamMap.get("IN_group.id").toString();
			    tmp = tmp.replace("[","(");
			    tmp = tmp.replace("]", ")");
				sql.append(" AND A.PURCHASING_GROUP_ID IN "+tmp+"");//表头
			}
		}
		
		if(searchParamMap.containsKey("EQ_group.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_group.code").toString())){
				sql.append(" AND G.CODE = '"+searchParamMap.get("EQ_group.code")+"'");//采购组
			}
			
		}
		

		if(searchParamMap.containsKey("EQ_factory.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("EQ_factory.code").toString())){
				sql.append(" AND D.CODE = '"+searchParamMap.get("EQ_factory.code")+"'");//工厂
			}
			
		}
		
		if(searchParamMap.containsKey("IN_material.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("IN_material.code").toString())){
				String tmp = searchParamMap.get("IN_material.code").toString();
			    tmp = tmp.replace("[","(");
			    tmp = tmp.replace("]", ")");
			    sql.append(" AND C.CODE	IN "+tmp+"");//物料
			}
			
		}
		
		
		if(searchParamMap.containsKey("IN_vendor.code")){
			if(!StringUtils.isEmpty(searchParamMap.get("IN_vendor.code").toString())){
				String tmp = searchParamMap.get("IN_vendor.code").toString();
			    tmp = tmp.replace("[","");
			    tmp = tmp.replace("]", "");
			    String [] str = tmp.split(",");
			    String s = "";
			    for(String st:str){
			    	s+="'"+st+"',";
			    }
			    sql.append(" AND E.CODE IN ("+s.substring(0, s.length()-1)+")");//供应
			}
			
		}
		
		Date date = new Date();
		if(!IsMonday(date)){
			date = getMonday(date); 
		}
		
		Date startTime = date;
		Date endTime = getEndTime(startTime);
		
		//时间的转换startTime
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String startTimeS = df.format(startTime);
		//时间的转换endTime
		String startTimeE = df.format(endTime);
		
		sql.append(" AND A.RQ >= to_date('" + startTimeS + "', 'yyyy-MM-dd HH24:mi:ss') AND A.RQ <= to_date('" + startTimeE + "', 'yyyy-MM-dd HH24:mi:ss')");
		
		String sql1="SELECT  A.ID, A.RQ, A.DHSL, A.SUR_QRY "
					+ ",C.CODE as materialCode, C.NAME as materialName, D.CODE as factoryCode, D.NAME as factoryName "
					+",G.CODE as groupCode, G.NAME as groupName, E.CODE as vendorCode, E.NAME as vendorName "+sql.toString()+" ORDER BY D.CODE,C.CODE,G.CODE,E.CODE ";
		//String sqlTotalCount="select count(A.ID ) "+sql.toString();
		List<?> list = generialDao.queryBySql_map(sql1);
		//int totalCount=generialDao.findCountBySql(sqlTotalCount);
		
		int k=0;
		String str ="";
		Map<String,Object> ma = new HashMap<String,Object>();
		for(Object Item : list){
			Map<String,Object> map =(Map<String, Object>) Item;
			String factoryCode = convertString(map.get("FACTORYCODE"));
			String materialCode = convertString(map.get("MATERIALCODE"));
			String groupCode = convertString(map.get("GROUPCODE"));
			String vendorCode = convertString(map.get("VENDORCODE"));
			str = factoryCode+materialCode+groupCode+vendorCode;
			if(!ma.containsKey(str)){
				ma.put(str, str);
				k++;
			}
		}
	
		List<GoodsRequestReportVo> dataList = formatPurchasePlanItemEntityData(list);
    	Page<GoodsRequestReportVo> page1=new PageImpl<GoodsRequestReportVo>(dataList,pagin,k);
		return page1;
	}
	
	
public List<GoodsRequestReportVo> formatPurchasePlanItemEntityData(List<?> list ) throws Exception{
		
		List<GoodsRequestReportVo> dataList = new  ArrayList<GoodsRequestReportVo>();

		//获取本周的星期一
		Date date = new Date();
		if(!IsMonday(date)){
			date = getMonday(date); 
		}
		
		Date startTime = date;
		//Date endTime = getEndTime(startTime);
		
		/*//时间的转换startTime
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTimeS = df.format(startTime);
		Timestamp startTimeT = Timestamp.valueOf(startTimeS);
		//时间的转换endTime
		String startTimeE = df.format(endTime);
		Timestamp endTimeT = Timestamp.valueOf(startTimeE);
		*/
		
		//获取表头的数据
		GoodsRequestReportHeadViewVo  headView = goodsReportHeadGet();
		GoodsRequestReportHeadVo head = headView.getHeadVo();
		GoodsRequestReportVo vo  = null;
		GoodsRequestReportHeadVo dataHead = null;
		GoodsRequestReportHeadViewVo viewVo = null;
		String str = "";
		int k = 0;
		Map<String,Object> ma = new HashMap<String,Object>();
		
		Class clazz = head.getClass();  
		Object object =  null;
		Field[] fields = null;
		
		for(Object Item : list){
			k++;
			Map<String,Object> map =(Map<String, Object>) Item;
			
			
			String factoryCode = convertString(map.get("FACTORYCODE"));
			String materialCode = convertString(map.get("MATERIALCODE"));
			String groupCode = convertString(map.get("GROUPCODE"));
			String vendorCode = convertString(map.get("VENDORCODE"));
			
			str = factoryCode+materialCode+groupCode+vendorCode;
			
			if(!ma.containsKey(str)){
				ma.put(str, str);
				if(k>1){
					dataList.add(vo);
					vo.setData(viewVo);
				}
				object= clazz.newInstance();
				fields = clazz.getDeclaredFields();  
				vo =new  GoodsRequestReportVo();
				
				dataHead = new GoodsRequestReportHeadVo();//获取表头的数据
				viewVo =new GoodsRequestReportHeadViewVo();//日期项目
				
				vo.setFactory(factoryDao.findByCode(factoryCode));
				vo.setMaterial(materialDao.getMaterials(materialCode));
				vo.setGroup(groupDao.findByCode(groupCode));
				vo.setVendor(vendorDao.getBuyerOrVendor(vendorCode));
				
				List<PurchasePlanItemEntity> purchasePlanItemList = purchasePlanItemDao.findPurchasePlanItemList(factoryCode, materialCode, groupCode, vendorCode);
				
				//时间设置计划的表头
				Date startTimeTmp =startTime;
				Calendar cdOne = Calendar.getInstance();
				cdOne.setTime(startTimeTmp);  
				String oneTime = cdOne.get(Calendar.YEAR)+"/"+(cdOne.get(Calendar.MONTH) + 1)+"/"+cdOne.get(Calendar.DAY_OF_MONTH);
						
				Calendar cdTwo = Calendar.getInstance();
				startTimeTmp = getSeventDay(startTimeTmp);
				cdTwo.setTime(startTimeTmp);  
				String twoTime = cdTwo.get(Calendar.YEAR)+"/"+(cdTwo.get(Calendar.MONTH) + 1)+"/"+cdTwo.get(Calendar.DAY_OF_MONTH);
						
				Calendar cdThree = Calendar.getInstance();		
				startTimeTmp = getSeventDay(startTimeTmp);
				cdThree.setTime(startTimeTmp);  
			    String threeTime = cdThree.get(Calendar.YEAR)+"/"+(cdThree.get(Calendar.MONTH) + 1)+"/"+cdThree.get(Calendar.DAY_OF_MONTH);
			    		
			    Calendar cdFour = Calendar.getInstance();
			    startTimeTmp = getSeventDay(startTimeTmp);
			    cdFour.setTime(startTimeTmp);  
			    String fourTime = cdFour.get(Calendar.YEAR)+"/"+(cdFour.get(Calendar.MONTH) + 1)+"/"+cdFour.get(Calendar.DAY_OF_MONTH);
			    
			    if(purchasePlanItemList.size()>0){
			    	
			    	for(PurchasePlanItemEntity purchasePlanItem :purchasePlanItemList){
			    		vo.setBuyer( userDao.findById( purchasePlanItem.getCreateUserId()));
						Set<PurchasePlanHeadEntity> purchasePlanHeadEntitySet = purchasePlanItem.getPurchasePlanHeadEntity();
						if(CommonUtil.isNotNullCollection(purchasePlanHeadEntitySet)){
							for (PurchasePlanHeadEntity purchasePlanHeadEntity : purchasePlanHeadEntitySet) {
								if(purchasePlanHeadEntity.getHeaderName().equals(oneTime)){
									viewVo.setMonOne(purchasePlanHeadEntity.getHeaderValues());
								    continue;
								}
								if(purchasePlanHeadEntity.getHeaderName().equals(twoTime)){
									viewVo.setMonTwo(purchasePlanHeadEntity.getHeaderValues());
									continue;
								}
								if(purchasePlanHeadEntity.getHeaderName().equals(threeTime)){
									viewVo.setMonThree(purchasePlanHeadEntity.getHeaderValues());
									continue;
								}
								if(purchasePlanHeadEntity.getHeaderName().equals(fourTime)){
									viewVo.setMonFour(purchasePlanHeadEntity.getHeaderValues());
									continue;
								}
							}
						}
			    	}
			    }
				
			}
		    
			Timestamp rq = (Timestamp) map.get("RQ");
			int year = rq.getYear()+1900;
			int month = rq.getMonth() + 1;
			int day = rq.getDate();				
			String  rqS = year+"/"+month+"/"+day;
			
			
		
			 for(int i = 0 ; i < fields.length; i++){ 
				 
			    	Field f1 = fields[i];
			    	f1.setAccessible(true); //设置些属性是可以访问的  
			        Object val1 = f1.get(head);//得到此属性的值  
			        if(rqS.equals(val1)){
		            	 String name = f1.getName();
				         String methodStr = "set"+name.toUpperCase().substring(0, 1)+name.substring(1);
				         Method method = clazz.getMethod(methodStr,new Class[]{f1.getType()});
				         String dhsl = convertString(map.get("DHSL"));
				         String surqry = "";
				         if(map.get("SUR_QRY")!=null){
				        	 surqry = convertString(map.get("SUR_QRY"));
				         }else{
				        	 surqry="0.000";
				         }
				         
				         Double viewNum = BigDecimalUtil.sub(Double.parseDouble(dhsl), Double.parseDouble(surqry));
				         String number = "";
				         if(viewNum  != Double.parseDouble(dhsl)){
				        	 number =dhsl+"(-"+surqry+")red";
				         }else{
				        	 number =  viewNum+"";
				         }
		            	 method.invoke(object,number);
		            	 break;
					}
			       
			        
			    }
			 
			 	dataHead =(GoodsRequestReportHeadVo) object;
			 	viewVo.setHeadVo(dataHead);
				
				
				if(list.size()==1 || list.size()==k){
					vo.setData(viewVo);
				}
			    
				if(list.size()==k){
					dataList.add(vo);
				}
		}
		
		return dataList;
	}
	
	
	public String  convertString(Object obj){
		String str = "";
		if(obj!=null){
			str = obj.toString();
		}
		return str;
	}
	
	
	  // 获得本周星期一的日期  
    public  Date getMonday(Date gmtCreate) {  
        int mondayPlus = getMondayPlus(gmtCreate);  
        GregorianCalendar currentDate = new GregorianCalendar();  
        currentDate.add(GregorianCalendar.DATE, mondayPlus);  
        Date monday = currentDate.getTime();  
        return monday;  
    }  
	
 // 获得当前日期与本周日相差的天数  
    private int getMondayPlus(Date gmtCreate) {  
        Calendar cd = Calendar.getInstance();    
        cd.setTime(gmtCreate);  
        // 获得今天是一周的第几天，星期日是第一天，星期二是第二天......    
        int dayOfWeek = cd.get(Calendar.DAY_OF_WEEK)-1;         //因为按中国礼拜一作为第一天所以这里减1    
        if (dayOfWeek == 1) {    
            return 0;    
        } else if(dayOfWeek == 0){    
        	return -6;
        }  else{
        	return 1 - dayOfWeek;
        }
    } 
    
    
  //获取当前日期是否为周一
    public Boolean  IsMonday(Date dt) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w == 1){
        	return true;
        }
        return false;
    } 

/*	 public static Date getSundayOfThisWeek() {
		  Calendar c = Calendar.getInstance();
		  int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		  if (day_of_week == 0)
		  day_of_week = 7;
		  c.add(Calendar.DATE, -day_of_week + 7);
		  return c.getTime();
	 }
	 */
	 public static Date getEndTime(Date date) {  
	        Calendar cal = Calendar.getInstance();  
	        cal.setTime(date);  
	        cal.add(Calendar.DATE, +27);  
	        return cal.getTime();  
	    }  
	
    public  Date getNextDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +1);//+1今天的时间加一天
        date = calendar.getTime();
        return date;
    }
	
    //获取七天后的日期
    public  Date getSeventDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, +7);//+7今天的时间加一天
        date = calendar.getTime();
        return date;
    }
    
    
    
    //获取表头的数据
    public  String getSearchParamMapHead() {
    	
		//获取本周的星期一
		Date date = new Date();
		if(!IsMonday(date)){
			date = getMonday(date); 
		}
		Date startTimeTmp = date;
		
		Calendar cdOne = Calendar.getInstance();
		cdOne.setTime(startTimeTmp);  
		String oneTime = cdOne.get(Calendar.YEAR)+"/"+(cdOne.get(Calendar.MONTH) + 1)+"/"+cdOne.get(Calendar.DAY_OF_MONTH);
		oneTime="'"+oneTime+"'";

		Calendar cdTwo = Calendar.getInstance();
		startTimeTmp = getSeventDay(startTimeTmp);
		cdTwo.setTime(startTimeTmp);  
		String twoTime = cdTwo.get(Calendar.YEAR)+"/"+(cdTwo.get(Calendar.MONTH) + 1)+"/"+cdTwo.get(Calendar.DAY_OF_MONTH);
		twoTime="'"+twoTime+"'";
		
		Calendar cdThree = Calendar.getInstance();		
		startTimeTmp = getSeventDay(startTimeTmp);
		cdThree.setTime(startTimeTmp);  
	    String threeTime = cdThree.get(Calendar.YEAR)+"/"+(cdThree.get(Calendar.MONTH) + 1)+"/"+cdThree.get(Calendar.DAY_OF_MONTH);
	    threeTime="'"+threeTime+"'";
	    
	    Calendar cdFour = Calendar.getInstance();
	    startTimeTmp = getSeventDay(startTimeTmp);
	    cdFour.setTime(startTimeTmp);  
	    String fourTime = cdFour.get(Calendar.YEAR)+"/"+(cdFour.get(Calendar.MONTH) + 1)+"/"+cdFour.get(Calendar.DAY_OF_MONTH);
	    fourTime="'"+fourTime+"'";
	    
	    String search = "(   "+ oneTime+","+twoTime+"," +threeTime+"," +fourTime+")";
	   
        return search;
    }

}
