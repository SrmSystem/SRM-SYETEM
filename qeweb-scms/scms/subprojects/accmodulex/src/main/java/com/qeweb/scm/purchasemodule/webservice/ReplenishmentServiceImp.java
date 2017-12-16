package com.qeweb.scm.purchasemodule.webservice;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.jws.WebService;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.qeweb.sap.CommonSapFw;
import com.qeweb.scm.basemodule.utils.StringUtils;
import com.qeweb.scm.purchasemodule.service.DeliveryService;
import com.qeweb.scm.purchasemodule.web.vo.ReplishmentVO;

@WebService(endpointInterface = "com.qeweb.scm.purchasemodule.webservice.ReplenishmentWebService", serviceName = "readinfo")
public class ReplenishmentServiceImp  implements ReplenishmentWebService{
	private static final Logger logger = LoggerFactory.getLogger(ReplenishmentWebService.class);
	
	@Autowired
	private DeliveryService deliveryService;
	
	@Override
	public String readinfo(String asnCode) {
		 String flag="Y";
		 String MESS="";
		 FileWriter fw=null;
		 StringBuilder log =new StringBuilder();
		 List<ReplishmentVO> vos = null;
		 String resultXml = null;
		 try {
			 fw = CommonSapFw.initFw(ReplenishmentServiceImp.class);
			 resultXml = replishToSapXml(asnCode,flag,MESS);
		} catch (Exception e) {
			e.printStackTrace();
			resultXml=replishToSapXml(asnCode,"N","异常："+e.toString());
		}finally{
			try {
				log.append("SRM返回XML为：").append(resultXml);
				fw.write(log.toString());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return resultXml;
	}
	
	/**
	 * 将数据转成系xml
	 * @param asnCode
	 * @return
	 */
	public String replishToSapXml(String asnCode,String flag,String MESS){
		List<ReplishmentVO>  vos=null;
		if(StringUtils.isEmpty(asnCode)){
			flag="N";
			MESS="SAP传来的ASN补货单号为空";
		}else{
			vos =  deliveryService.getReplishmentVOs(asnCode);
			if(null==vos || vos.size()==0){
				flag="N";
				MESS="SAP传来的ASN补货单号为："+asnCode+",没有查到明细数据";
			}
		}
		
		
		//拼接xml
		StringBuffer str =new StringBuffer();
		str.append("<ROOT>").append("<FLAG>").append(flag).append("</FLAG>")//<!--表头信息-->
		.append("<MESS>").append(MESS).append("</MESS>");
		str.append("<LIST>");//<!--明细信息-->
		if(null!=vos && vos.size()>0){
			for (ReplishmentVO vo : vos) {
				str.append("<ITEM>");
				str.append("<BOLNR>").append(vo.getBOLNR()).append("</BOLNR>");//<!--ASN单号-->
				str.append("<EBELP>").append(vo.getEBELP()).append("</EBELP>");//<!--采购订单号行项目-->
				str.append("<SOURCE_BOLNR>").append(vo.getSOURCE_BOLNR()).append("</SOURCE_BOLNR>");//<!--原ASN单号-->
				str.append("<VBELN>").append(vo.getVBELN()).append("</VBELN>");//<!--原交货单号（DN）-->
				str.append("<MATNR>").append(vo.getMATNR()).append("</MATNR>");//<!--物料号-->
				str.append("<MAKTX>").append(vo.getMAKTX()).append("</MAKTX>");//<!--物料描述-->
				str.append("<LIFNR>").append(vo.getLIFNR()).append("</LIFNR>");//<!--供应商账号-->
				str.append("<EBELN>").append(vo.getEBELN()).append("</EBELN>");//<!--采购订单号-->
				str.append("<LFIMG>").append(vo.getLFIMG()).append("</LFIMG>");//<!-- 补货数量-->
				str.append("<MEINS>").append(vo.getMEINS()).append("</MEINS>");//<!- 采购订单单位-->
				str.append("<CHARG>").append(vo.getCHARG()).append("</CHARG>");//<!--批号-->
				str.append("<HSDAT>").append(vo.getHSDAT()).append("</HSDAT>");//<!--生产日期-->
				str.append("<VERSION>").append(vo.getVERSION()).append("</VERSION>");//<!--版本-->
				str.append("</ITEM>");
			}
		}
		str.append("</LIST>");
		str.append("</ROOT>");
		return str.toString();
	}
	
}
