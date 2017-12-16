package com.qeweb.scm.basemodule.task;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;

import com.qeweb.scm.basemodule.annotation.JobBean;
import com.qeweb.scm.basemodule.constants.StatusConstant;
import com.qeweb.scm.basemodule.context.SpringContextUtils;
import com.qeweb.scm.basemodule.entity.MaterialEntity;
import com.qeweb.scm.basemodule.quartz.AbsFtpJobBean;
import com.qeweb.scm.basemodule.quartz.TaskResult;
import com.qeweb.scm.basemodule.repository.MaterialDao;

/**
 * FOTON同步物料JobBean
 * @author ALEX
 *
 */
//@JobBean
//@PersistJobDataAfterExecution
//@DisallowConcurrentExecution
public class SyncMaterialJobBean extends AbsFtpJobBean {

	private static final Logger logger = LoggerFactory.getLogger(SyncMaterialJobBean.class);
	private static final String PREFIX_EROP = "EROP";		//工程发放单
	private static final String PREFIX_UP = "UP";			//升级通知单
	private static final String PREFIX_AP = "AP";			//更改通知单
	
	/*文件类别包括wgcp，wggx，wgggcp，wgggx，wglxd*/
	private static final String FILE_TYPE_WGCP = "wgcp.txt";			//wgcp表：记录了零部件的属性特征(产品属性表)
	private static final String FILE_TYPE_WGGX = "wggx.txt";			//wggx表：记录了零部件的装配关系(产品关系表)
	private static final String FILE_TYPE_WGGGCP = "wgggcp.txt";		//wgggcp表：记录了零部件的属性更改情况(更改属性表)
	private static final String FILE_TYPE_WGGGX = "wgggx.txt";			//  wggggx表：记录了零部件的关系更改情况（更改关系表）
	private static final String FILE_TYPE_WGLXD = "wglxd.txt";			//wglxd表：记录了更改单信息(ECN属性表)
	
	@Autowired
	MaterialDao dao;

	@Override
	protected void prepare() {
		
	}

	@Override
	protected TaskResult exec() throws Exception {
		logger.info("start sync material ....");
		TaskResult result = new TaskResult(true);
		String localfilePath = "d:/qeweb";
		boolean flag = getFtpFile(null, localfilePath);
		if(!flag) {
			log("get ftp material info file is empty!");
			return result;
		}
		
		List<MaterialEntity> list = convertToObject(new File(localfilePath));
		if(CollectionUtils.isEmpty(list)) {
			log("file material info is empty!");
			return result;
		}
		getDao().save(list);
		log("end sync material ....");
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected List<MaterialEntity> convertToObject(File file) {
		if(!file.exists())
			log("file is not exists!");
		
		List<MaterialEntity> materialList = new ArrayList<MaterialEntity>();
		File[] files = file.listFiles();
		for(File f : files) {
			if(f.getName().startsWith(PREFIX_EROP) && f.getName().endsWith(FILE_TYPE_WGCP)) {
				readFile(f, materialList);
			}
		}
		return materialList;
	}

	private void readFile(File file, List<MaterialEntity> materialList) {
		InputStream fileInputStream = null;  
		BufferedReader bufferReader = null;
		String strForPlanItemLine = null;
		try {
			String[] transfer = null;
			if (!file.exists())
				return;
			
			log(file.getName() + "read material info begin.....");
			fileInputStream = new FileInputStream(file);
			bufferReader = new BufferedReader(new InputStreamReader(fileInputStream, "GBK"));
			int rowNum = 0;
			while ((strForPlanItemLine = bufferReader.readLine()) != null) {
				if (rowNum == 0) {
					rowNum++;
					continue;
				}
				transfer = strForPlanItemLine.split("\\|");
				MaterialEntity material = new MaterialEntity();
				int length = transfer.length;
				if (length >= 1) {
					material.setCode(transfer[0]);
				}
				if (length >= 4) {
					material.setName(transfer[3] == null ? transfer[0] : transfer[3]);   
				}
				rowNum++;
				material.setIsOutData(StatusConstant.STATUS_YES);		//外部数据来源标实
				materialList.add(material);
			}
			log(file.getName() + "read material info finish.....");
		} catch (Exception e) {
			e.printStackTrace();
			log(e.getMessage());
		} finally {
			try {
				if (bufferReader != null)
					bufferReader.close();
				if (fileInputStream != null)
					fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	} 
	
	public MaterialDao getDao() {
		if(dao == null)
			dao = SpringContextUtils.getBean("materialDao");  
		return dao;
	}

	@Override
	protected void destory() {
		
	}
}
