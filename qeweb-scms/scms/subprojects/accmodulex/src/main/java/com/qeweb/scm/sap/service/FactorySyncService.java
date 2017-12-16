package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.FactorySAP;
import com.qeweb.scm.basemodule.entity.FactoryEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.FactoryDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
/**
 * 工厂 SAP->SRM
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class FactorySyncService extends BaseService{
	@Autowired
	private FactoryDao factoryDao;
	
	/**
	 * 同步sap工厂数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
	   iLogger.log("FactorySyncService execute method execute start");
	   List<FactoryEntity> res=FactorySAP.sync(iLogger);
	   if(res!=null){
		    List<FactoryEntity> list =new ArrayList<FactoryEntity>();
			iLogger.log("总条数:"+res.size());
			for (FactoryEntity re : res) {
				FactoryEntity factory=factoryDao.findByCode(re.getCode());
			     if(null==factory){
			    	 iLogger.log("新增"+re.getCode());
			    	 factory = new FactoryEntity();
			    	 factory.setCode(re.getCode());
			    	 factory.setName(re.getName());
			    	 factory.setAbolished(BOHelper.UNABOLISHED_SINGEL);
			    	 factory.setCreateTime(DateUtil.getCurrentTimestamp());
			    	 factory.setLastUpdateTime(DateUtil.getCurrentTimestamp());
			    	 factory.setIsOutData(BOHelper.OUT_DATA_YES);
			    	 list.add(factory);
			     }else{//工厂名称变更
			    	 if(!re.getName().equals(factory.getName())){
				    	 factory.setName(re.getName());
				    	 factory.setIsOutData(BOHelper.OUT_DATA_YES);
				    	 list.add(factory);
			    	 }
			     }
			}
			
			factoryDao.save(list);
			iLogger.log("FactorySyncService execute method execute end");
			return true;
	   }else{
		    iLogger.log("SAP连接失败");
			return false;
	   }
	 
   }
	
	
}
