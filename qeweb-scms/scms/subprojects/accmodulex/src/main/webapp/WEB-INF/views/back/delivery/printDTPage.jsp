<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="purchase.delivery.PackagingPrinting"/>
</title>
<style>

.w3cbbs { page-break-after:always;}

td.small {
    line-height: 12px;
}


</style>
<script type="text/javascript">

function goPrint() {
	document.getElementById("printButton").style.display='none';
	window.print();
	window.close();
	} 


</script>
</head>
<body style="text-align: center;padding:0px;overflow-y:auto;" >
<div align="left">
    <a id="printButton" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true" onclick="goPrint()"><spring:message code="purchase.delivery.Print"/>
   <!--  打印 --></a>
</div>
<div id="print" >
	<c:forEach var="list" items="${aaa}" varStatus="status1">
		</br>
		<c:forEach var="pc" items="${list}" varStatus="status2">
		<div >
			<table style="font-size:10px;border-collapse:collapse;margin: 0;padding: 0"   border="0" cellspacing="0" cellpadding="0">
				<tbody>
				     <tr>   
				        <td class="small"><!-- 供应商 --><spring:message code="purchase.delivery.Supplier"/>
				        :${pc.vendorCode}</td>
				     </tr>
				     <tr>   
				        <td class="small"><!-- AAC料号 --><spring:message code="purchase.delivery.Sign"/>
				        :${pc.materialCode}</td>
				     </tr>
				     <tr>   
				        <td ><img src="${ctx }/barcode?code=${pc.materialCode }&type=code128&h=4"></td>
				     </tr>
				     <tr>   
				        <td class="small"><!-- 物料描述 --><spring:message code="purchase.delivery.MaterialDescription"/>
				        :${pc.materialName}</td>
				     </tr>
				     <tr>   
				        <td class="small"><!-- 物料版本 --><spring:message code="purchase.delivery.MaterialVersion"/>
				        :${pc.materialVersion}</td>
				     </tr>
				     <tr>   
				        <td ><img src="${ctx }/barcode?code=${pc.materialVersion }&type=code128&h=4"></td>
				     </tr>
				     
				     				     <tr>   
				        <td class="small"><!-- 包装数量 --><spring:message code="purchase.delivery.PackingQuantity"/>
				        (QTY):${pc.boxNum}</td>
				     </tr>
				     <tr>   
				        <td ><img src="${ctx }/barcode?code=${pc.boxNum }&type=code128&h=4"></td>
				     </tr>
				     
				     				     <tr>   
				        <td class="small"><!-- 追溯批号 --><spring:message code="purchase.delivery.TraceabilityBatchNumber"/>
				        :${pc.vendorCharg}</td>
				     </tr>
				     <tr>   
				        <td ><img src="${ctx }/barcode?code=${pc.vendorCharg }&type=code128&h=4"></td>
				     </tr>
				     
				     				     <tr>   
				        <td class="small"><!-- 物料批号 --><spring:message code="purchase.delivery.MaterialBatchNumber"/>
				        :${pc.charg}</td>
				     </tr>
				     <tr>   
				        <td ><img src="${ctx }/barcode?code=${pc.charg }&type=code128&h=4"></td>
				     </tr>
				     
				     				     <tr>   
				        <td class="small"><!-- 生产日期 --><spring:message code="purchase.delivery.DateOfManufacture"/>
				        :${pc.manufactureDate}</td>
				     </tr>
				     <tr>   
				        <td ><img src="${ctx }/barcode?code=${pc.manufactureDate }&type=code128&h=4"></td>
				     </tr>
				     

				     
	
				</tbody>
			</table>
		</div>
		</c:forEach>
		<c:if test="${status1.count < size }">
			<div class="w3cbbs"></div>
		</c:if>
	</c:forEach>
</div>
</body>
</html>