package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.PurchasingGroupSAP;
import com.qeweb.scm.basemodule.entity.PurchasingGroupEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.PurchasingGroupDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
import com.qeweb.scm.basemodule.utils.StringUtils;
/**
 * 采购组 SAP->SRM
 * @author chao.gu
 * 20170511
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class PurchasingGroupSyncService extends BaseService{
	@Autowired
	private PurchasingGroupDao purchasingGroupDao;
	
	/**
	 * 同步sap采购组数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
	   iLogger.log("PurchasingGroupSyncService execute method execute start");
	   List<PurchasingGroupEntity> res=PurchasingGroupSAP.sync(iLogger);
	   if(res!=null){
		    List<PurchasingGroupEntity> list =new ArrayList<PurchasingGroupEntity>();
			iLogger.log("总条数:"+res.size());
			for (PurchasingGroupEntity re : res) {
				 if(!StringUtils.isEmpty(re.getCode())){
				PurchasingGroupEntity purGroup=purchasingGroupDao.findByCode(re.getCode());
			  
				if(null==purGroup){
			    	 iLogger.log("新增"+re.getCode());
			    	 purGroup = new PurchasingGroupEntity();
			    	 purGroup.setCode(re.getCode());
			    	 purGroup.setName(re.getName());
			    	 purGroup.setAbolished(BOHelper.UNABOLISHED_SINGEL);
			    	 purGroup.setCreateTime(DateUtil.getCurrentTimestamp());
			    	 purGroup.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			    	 purGroup.setIsOutData(BOHelper.OUT_DATA_YES);
			    	 list.add(purGroup);
			     }else{//add by chao.gu 20171017名称变更
			    	 if(!re.getName().equals(purGroup.getName())){
				    	 purGroup.setName(re.getName());
				    	 purGroup.setIsOutData(BOHelper.OUT_DATA_YES);
				    	 list.add(purGroup);
			    	 }
			     }
			   }
			}
			
			purchasingGroupDao.save(list);
			iLogger.log("PurchasingGroupSyncService execute method execute end");
			return true;
	   }else{
		    iLogger.log("SAP连接失败");
		    return false;
	   }
	  
   }
	
	
}
