package com.qeweb.sap;  
  
import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;


import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.ext.DestinationDataProvider;
  
/** 
 * 与SAP连接配置 
 * @author jay 
 */  
public class SAPConn {  
    private static final String ABAP_AS_POOLED = "ABAP_AS_WITH_POOL";  
    static{  
        Properties connectProperties = new Properties();  
       
        connectProperties.setProperty(DestinationDataProvider.JCO_SYSNR,  SapConfigUtil.getInstance().getJcoSysnr());        //系统编号  
        
        connectProperties.setProperty(DestinationDataProvider.JCO_LANG,   SapConfigUtil.getInstance().getJcoLang());        //登录语言  
        connectProperties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY, SapConfigUtil.getInstance().getJcoPoolCapacity());  //最大连接数    
        connectProperties.setProperty(DestinationDataProvider.JCO_PEAK_LIMIT,SapConfigUtil.getInstance().getJcoPeakLimit());     //最大连接线程  
        
        connectProperties.setProperty(DestinationDataProvider.JCO_ASHOST, SapConfigUtil.getInstance().getJcoAshost());//服务器  
        connectProperties.setProperty(DestinationDataProvider.JCO_CLIENT, SapConfigUtil.getInstance().getJcoClinet());       //SAP集团  
        connectProperties.setProperty(DestinationDataProvider.JCO_USER,   SapConfigUtil.getInstance().getJcoUser());  //SAP用户名  
        connectProperties.setProperty(DestinationDataProvider.JCO_PASSWD, SapConfigUtil.getInstance().getJcoPasswd());     //密码  

        createDataFile(ABAP_AS_POOLED, "jcoDestination", connectProperties);  
    }  
      
    /** 
     * 创建SAP接口属性文件。 
     * @param name  ABAP管道名称 
     * @param suffix    属性文件后缀 
     * @param properties    属性文件内容 
     */  
    private static void createDataFile(String name, String suffix, Properties properties){  
        File cfg = new File(name+"."+suffix);  
        if(cfg.exists()){  
            cfg.deleteOnExit();  
        }  
        try{  
            FileOutputStream fos = new FileOutputStream(cfg, false);  
            properties.store(fos, "for tests only !");  
            fos.close(); 
          
        }catch (Exception e){  
         
            throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);  
        }  
    }  
      
    /** 
     * 获取SAP连接 
     * @return  SAP连接对象 
     */  
    public static JCoDestination connect(){  
        JCoDestination destination =null;  
        try {  
            destination = JCoDestinationManager.getDestination(ABAP_AS_POOLED);  
         
        } catch (JCoException e) {  
          
        }  
        return destination;  
    }  

}  