package com.qeweb.scm.check.service;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.qeweb.modules.persistence.DynamicSpecificationsEx;
import com.qeweb.modules.persistence.SearchFilterEx;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.repository.general.GenerialDao;
import com.qeweb.scm.check.entity.NoCheckItemsEntity;
import com.qeweb.scm.check.repository.NoCheckItemDao;
import com.qeweb.scm.check.repository.NoCheckItemsDao;
import com.qeweb.scm.check.vo.NoCheckedItemsVo;
import com.qeweb.scm.purchasemodule.entity.InStorageItemEntity;
import com.qeweb.scm.purchasemodule.entity.ReceiveItemEntity;
import com.qeweb.scm.purchasemodule.repository.InStorageItemDao;
import com.qeweb.scm.purchasemodule.repository.ReceiveItemDao;
@Service
@Transactional(rollbackOn=Exception.class)
public class NoCheckedItemsService{
	
	@Autowired
	private ReceiveItemDao recItemDao;
	
	@Autowired
	private InStorageItemDao inStorageItemDao;
	
	@Autowired
	private NoCheckItemDao noCheckItemDao;
	
	@Autowired
	private NoCheckItemsDao noCheckItemsDao;
	
	public ReceiveItemEntity getRecItemEntity(Long id) {
		return recItemDao.findOne(id);
	}
	
	/**
	 * 获取所有未对账的入库单
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @return
	 */
	public Page<NoCheckedItemsVo> getRecItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap) {
		try {
			String condition = "";
			if(searchParamMap!=null&&!searchParamMap.isEmpty()){
				String orderCode = (String) searchParamMap.get("orderCode");
				String recCode = (String) searchParamMap.get("recCode");
				String vCode = (String) searchParamMap.get("vCode");
				String vName = (String) searchParamMap.get("vName");
				condition += "where 1=1 ";
				if(orderCode!=null){
					condition += " and po.order_code like '%"+orderCode+"%'";
				}
				if(recCode!=null){
					condition += " and REC_ITEM.RECEIVE_CODE like '%"+recCode+"%'";
				}
				if(vCode!=null){
					condition += " and PO.vcode like '%"+vCode+"%'";
				}
				if(vName!=null){
					condition += " and PO.vname like '%"+vName+"%'";
				}
			}
			Sort sort = new Sort(Direction.DESC, "REC_ITEM.ID");
			PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, sort);
			GenerialDao gd = SpringContextUtils.getBean("generialDao");
			String sql = "FROM ( SELECT REC.RECEIVE_CODE, REC.CREATE_TIME, REC_ITEM.RECEIVE_QTY,"+
					" REC_ITEM.RETURN_QTY,rec_item.id, REC_ITEM.IN_STORE_QTY, REC_ITEM.ORDER_ITEM_ID FROM QEWEB_RECEIVE rec, QEWEB_RECEIVE_ITEM rec_item WHERE REC_ITEM.RECEIVE_ID = REC. ID ) rec_item"+
					" LEFT JOIN ( SELECT o. ID, o.order_id, o.ITEM_NO, M.code as mcode, M.NAME as mname, M.UNIT as munit FROM QEWEB_PURCHASE_ORDER_ITEM o INNER JOIN QEWEB_MATERIAL M ON o.MATERIAL_ID = M . ID"+
					") po_item ON po_item. ID = ORDER_ITEM_ID"+
					" INNER JOIN ( SELECT o. ID, o.order_code, b.code AS bcode, b. NAME AS bname, v.code AS vcode, v. NAME AS vname FROM QEWEB_PURCHASE_ORDER o INNER JOIN QEWEB_ORGANIZATION b ON o.buyer_id = b. ID"+
					" INNER JOIN QEWEB_ORGANIZATION v ON o.vendor_id = v. ID ) po ON po. ID = po_item.order_id "+condition;
			String sql2 = "select count(1) "+sql;
			sql = "SELECT REC_ITEM.*, po.order_code, po_item.ITEM_NO, PO.vcode, PO.vname, PO_ITEM.mUNIT,po_item.mcode,po_item.mname " +sql;
			List<?> list = gd.querybysql(sql,pagin);
			List<NoCheckedItemsVo> items = new ArrayList<NoCheckedItemsVo>();
			int totalCount = gd.findCountBySql(sql2);
			for(Object o:list){
				Map<String,Object> m =  (Map<String, Object>) o;
				NoCheckedItemsVo item = new NoCheckedItemsVo();
				item.setRecQty(((BigDecimal)m.get("RECEIVE_QTY")).doubleValue());
				item.setRecCode((String)m.get("RECEIVE_CODE"));
				item.setvCode((String)m.get("VCODE"));
				item.setvName((String)m.get("VNAME"));
				item.setId(((BigDecimal)m.get("ID")).longValue());
				item.setmCode((String)m.get("MCODE"));
				item.setmName((String)m.get("MNAME"));
				item.setmUnit((String)m.get("MUNIT"));
				item.setOrderCode((String)m.get("ORDER_CODE"));
				item.setItemNo(((BigDecimal)m.get("ITEM_NO")).intValue());
				item.setCreateTime((Timestamp)m.get("CREATE_TIME"));
				items.add(item);
			}
			Page<NoCheckedItemsVo> page =new PageImpl<NoCheckedItemsVo>(items, pagin, totalCount);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Page<ReceiveItemEntity> getRecItems2(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<ReceiveItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), ReceiveItemEntity.class);
		return recItemDao.findAll(spec,pagin);
	}
	
	/**
	 * 从入库单获取未对账明细
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @param sort
	 * @return
	 */
	public Page<InStorageItemEntity> getInStorageItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<InStorageItemEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), InStorageItemEntity.class);
		return inStorageItemDao.findAll(spec,pagin);
	}
	
	/**
	 * 从未对账明细获取数据
	 * @param pageNumber
	 * @param pageSize
	 * @param searchParamMap
	 * @param sort
	 * @return
	 */
	public Page<NoCheckItemsEntity> getNoCheckItems(int pageNumber, int pageSize, Map<String, Object> searchParamMap,Sort sort) {
		PageRequest pagin = new PageRequest(pageNumber - 1, pageSize, sort);
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<NoCheckItemsEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), NoCheckItemsEntity.class);
		return noCheckItemsDao.findAll(spec,pagin);
	}
	
	/**
	 * 根据页面的查询条件获得list数据
	 * @param searchParamMap
	 * @return
	 */
	public List<NoCheckItemsEntity> getNoCheckItemList(Map<String, Object> searchParamMap) {
		Map<String, SearchFilterEx> filters = SearchFilterEx.parse(searchParamMap);
		Specification<NoCheckItemsEntity> spec = DynamicSpecificationsEx.bySearchFilterEx(filters.values(), NoCheckItemsEntity.class);
		return noCheckItemsDao.findAll(spec);
	}

	
	public  Map<String,Object> deleteCheckOpt(Map<String ,Object> param) {
		
		Map<String,Object> map = new HashMap<String,Object>();
		try{
			if(param.size()>0){
				String[] id = param.get("ids").toString().split(",");
				for(String i:id){
					noCheckItemsDao.updateCheckOpt(Long.valueOf(i));
				}
				map.put("msg", "去除对账成功");
				map.put("code", true);
			}else{
				map.put("msg", "请选择未对账信息");
				map.put("code", false);
			}
		}catch(Exception e){
			map.put("msg", "系统异常请联系程序员");
			map.put("code", false);
			e.printStackTrace();
		}
		
		return map;
		
	}

	public NoCheckItemsEntity getByItemId(long id) {
		return noCheckItemsDao.getReceiveByItemId(id);
	}

}
