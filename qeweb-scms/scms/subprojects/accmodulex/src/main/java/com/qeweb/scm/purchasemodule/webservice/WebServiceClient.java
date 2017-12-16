package com.qeweb.scm.purchasemodule.webservice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;


public class WebServiceClient{
	
	public static void main(String[] args)  throws Exception{
		  	String xml=readFileByLines("F:\\1111.xml");
		   JaxWsProxyFactoryBean svr = new JaxWsProxyFactoryBean();
           svr.setServiceClass(OrderUpdateWebService.class);
           svr.setAddress("http://localhost:8080/acc-scms/webservice/orderWriteinfo");
           OrderUpdateWebService hw = (OrderUpdateWebService) svr.create();
           hw.writeinfo(xml);
	}
	
	
	/**
	 * 读取文件
	 * @param fileName
	 * @return
	 */
    public static String readFileByLines(String fileName) {
    	StringBuilder sb=new StringBuilder();
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
//            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
//                System.out.println("line " + line + ": " + tempString);
                sb.append(tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
		return sb.toString();
    }

}
