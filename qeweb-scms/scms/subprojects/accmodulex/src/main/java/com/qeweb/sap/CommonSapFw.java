package com.qeweb.sap;

import java.io.File;
import java.io.FileWriter;
import java.util.Date;


import com.qeweb.modules.utils.PropertiesUtil;
import com.qeweb.scm.basemodule.utils.DateUtil;

public class CommonSapFw {
	/**
	    * 获取定时任务日志文件
	    * @param object
	    * @return
	    * @throws Exception
	    */
		public static FileWriter initFw(Object object) throws Exception{
			   String cName =  object.toString().replaceAll("_", "");  
		        Date d = new Date();
		        String dstr = DateUtil.dateToString(d, DateUtil.DATE_FORMAT_YYYYMMDD);
		        String fstr = DateUtil.dateToString(d, "yyyyMMdd_HH_mm_ss");
		        File fileroot = new File(PropertiesUtil.getProperty("file.log.dir", "/logs") + "/sap/task/" + cName + "/" + dstr);
				if(!fileroot.isDirectory()){
					fileroot.mkdirs();
				}
		        
		        FileWriter fw = new FileWriter(new File(PropertiesUtil.getProperty("file.log.dir", "/logs") + "/sap/task/" + cName + "/" + dstr + "/" + fstr + ".log"));
		        return fw;
		}
}
