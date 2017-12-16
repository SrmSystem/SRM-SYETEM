<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>整项报价历史详情页面</title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
	<style>
     .infoT {
        border-top:1px solid #888;
        margin-top: 10px;
    }
    .infoT th{
        text-align: center; 
        height: 25px;
        font-size: 12px;
        background-color: #D0D0D0;
        border: 1px solid #888;
    }
    .infoT td{
        text-align: center; 
        height: 20px;
        font-size: 12px;
        border: 1px solid #888;
    }
</style>
</head>

<body style="margin:0;padding:0;">
<div class="easyui-panel" style="overflow: auto;width: 100%;height: 100%">
  <form id="form-epWholeQuoHis-offer">
	  <table style="width: 90%;margin: auto;margin-top: 20px">
		<tr>
		
			<td style="width: 30%">询价单号：${epWholeQuoHis.epPrice.enquirePriceCode}</td>
			<td style="width: 30%">供应商：${epWholeQuoHis.epVendor.vendorName }</td> 
			<td style="width: 30%">报价日期：${epWholeQuoHis.createTime }</td>  
		</tr>
	  </table>
	  <table class="infoT" style="width: 90%;margin: auto;margin-top: 10px;border: 1px">
        <tr >
            <th style="width: 6%;">物料编码</th>       
            <th style="width: 6%;">物料名称</th>
            <th style="width: 10%;">规格型号</th>
            <th style="width: 5%;">单位</th>
            <th style="width: 6%;">数量</th>       
            <th style="width: 7%;">运输费用</th>
            <th style="width: 10%;">无税单价</th>
            <th style="width: 10%;">含税单价</th>
           	<th style="width:10%;">协商单价</th>
            <th style="width: 13%;">产地/品牌</th>       
            <th style="width: 10%;">保修期</th>
            <th style="width: 10%;">供货周期</th>
            <th style="width: 17%;">备注</th>
        </tr>
        <tr>
            <td>${epWholeQuoHis.epMaterial.materialCode}</td>				<!-- 物料编码 -->
            <td>${epWholeQuoHis.epMaterial.materialName}</td>				<!-- 物料名称 -->	     
            <td>${epWholeQuoHis.epMaterial.materialSpec}</td>				<!-- 规格型号 -->
            <td>${epWholeQuoHis.epMaterial.materialUnit}</td>				<!-- 单位 -->
            <td>${epWholeQuoHis.epMaterial.planPurchaseQty}</td>			<!-- 数量 -->	
            <td>${epWholeQuoHis.epMaterial.freight}</td>				<!-- 运输费用 -->
            <td>${epWholeQuoHis.totalQuotePrice}</td>				<!-- 无税单价 -->
            <td>${epWholeQuoHis.quotePrice}</td>				<!-- 含税单价 -->
           	<td>${epWholeQuoHis.negotiatedPrice}</td>		<!-- 协议单价 -->
            <td>${epWholeQuoHis.brand}</td>				<!-- 产地/品牌 -->
            <td>${epWholeQuoHis.epMaterial.warranty}</td>				<!-- 保修期 -->
            <td>${epWholeQuoHis.supplyCycle}</td>				<!-- 供货周期 -->	
            <td>${epWholeQuoHis.epMaterial.remarks}</td>				<!-- 备注  -->	
         </tr>
	   </table>
	   
	   <table style="width: 90%;margin: auto;margin-top: 10px">
	   <tr>
		   <td>税率：${epWholeQuoHis.taxRate}</td>
		   <td>税种：${epWholeQuoHis.taxCategory}</td>
			<td></td>
			<td></td>
	   </tr>
	   <tr>
	   	   <td colspan="4">保质期：${epWholeQuoHis.warrantyPeriod}</td>
	   </tr>
	   <tr>
	   	   <td colspan="4">运输方式：${epWholeQuoHis.transportationMode}</td>
	   </tr>
	  <%--  <tr>
		   <td colspan="4">付款方式：${epWholeQuoHis.paymentMeans}</td>
	   </tr> --%>
	   <tr>
		   <td colspan="4">报价附件：
				<a href="javascript:;" onclick="File.download('${epWholeQuoHis.quoteTemplateUrl}','${epWholeQuoHis.quoteTemplateName}')">${epWholeQuoHis.quoteTemplateName}</td>
		   <!-- <td>图片附件：</td> -->
	   </tr>
<%-- 	   <tr>
		   <td colspan="4">技术承诺：<input class="easyui-textbox" id="technologyPromises" name="technologyPromises" value="${epWholeQuoHis.technologyPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">质量承诺：<input class="easyui-textbox" id="qualityPromises" name="qualityPromises" value="${epWholeQuoHis.qualityPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">服务承诺：<input class="easyui-textbox" id="servicePromises" name="servicePromises" value="${epWholeQuoHis.servicePromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">交期承诺：<input class="easyui-textbox" id="deliveryPromises" name="deliveryPromises" value="${epWholeQuoHis.deliveryPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr> --%>
	   <tr>
		   <td colspan="4">其他承诺：<input class="easyui-textbox" id="otherPromises" name="otherPromises" value="${epWholeQuoHis.otherPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/></td>
	   </tr>
	   </table>
  </form>
</div>
</body>
</html>
