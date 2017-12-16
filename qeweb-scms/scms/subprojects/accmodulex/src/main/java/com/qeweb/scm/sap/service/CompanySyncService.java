package com.qeweb.scm.sap.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qeweb.sap.CompanySAP;
import com.qeweb.scm.basemodule.entity.CompanyEntity;
import com.qeweb.scm.basemodule.log.ILogger;
import com.qeweb.scm.basemodule.repository.CompanyDao;
import com.qeweb.scm.basemodule.service.base.BaseService;
import com.qeweb.scm.basemodule.utils.DateUtil;
/**
 * 公司 SAP->SRM
 * @author chao.gu
 *
 */
@Service
@Transactional(rollbackOn=Exception.class)
public class CompanySyncService extends BaseService{
	@Autowired
	private CompanyDao companyDao;
	
	/**
	 * 同步sap公司数据
	 * @param iLogger
	 * @return
	 * @throws Exception
	 */
	public boolean execute(ILogger iLogger)throws Exception {
	   iLogger.log("CompanySyncService execute method execute start");
	   List<CompanyEntity> res=CompanySAP.sync(iLogger);
	   if(res!=null){
		    List<CompanyEntity> list =new ArrayList<CompanyEntity>();
			iLogger.log("总条数:"+res.size());
			for (CompanyEntity re : res) {
				 CompanyEntity company=companyDao.findByCode(re.getCode());
			     if(null==company){
			    	 iLogger.log("新增"+re.getCode());
			    	 company = new CompanyEntity();
			    	 company.setCode(re.getCode());
			    	 company.setName(re.getName());
			    	 company.setAbolished(BOHelper.UNABOLISHED_SINGEL);
			    	 company.setCreateTime(DateUtil.getCurrentTimestamp());
			    	 company.setIsOutData(BOHelper.OUT_DATA_YES);
			    	 list.add(company);
			     }else{
			    	 if(!re.getName().equals(company.getName())){
			    		 iLogger.log("修改"+re.getCode());
			    		 company.setName(re.getName());
				    	 company.setIsOutData(BOHelper.OUT_DATA_YES);
				    	 list.add(company);
			    	 }
			     }
			}
			  companyDao.save(list);
			  iLogger.log("CompanySyncService execute method execute end");
				return true;
	   }else{
		   iLogger.log("SAP连接失败");
		   return false;
		   
	   }
	 
   }
	
	
}
