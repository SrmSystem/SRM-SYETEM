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
<title><spring:message code="purchase.delivery.PrintDeliveryList"/>
</title>
<style>
	.infoF {
    }
    .infoF td{
        padding-top: 10px;
    }
    .infoF th {
        padding-top: 10px;
        text-align: right;
        font-size: 12px;
    }
    .infoF td {
        border-bottom: 1px solid #888;
        height: 20px;
        font-size: 12px;
    }
    
     .infoT {
        border-top:1px solid #888;
        margin-top: 10px;
    }
    .infoT th{
        text-align: center; 
        height: 25px;
        font-size: 12px;
        background-color: #888;
    }
    .infoT td{
        text-align: left; 
        height: 20px;
        font-size: 12px;
        border-bottom: 1px solid #888;
    }
</style>
<script type="text/javascript" src="${ctx}/static/script/basedata/qrgen.js"></script>
<script type="text/javascript" src="${ctx}/static/base/jquery/jquery-1.9.1.min.js"></script>


<script type="text/javascript">
var pwin;
function myPrint(size){
	$("#printButton").hide();
    var printStr = "<html><head><meta http-equiv='Content-Type' content='text/html; charset=utf-8'>"
    +"<link rel='stylesheet' type='text/css' href='${pageContext.request.contextPath}/static/base/bootstrap-3.3.4-dist/css/bootstrap.min.css'> "
    +" <style>"
   	 +".infoF {"
   	 +"}"
   	 +" .infoF td{"
   	 +"    padding-top: 0px;"
   	 +" }"
   	 +" .infoF th {"
   	 +"   padding-top: 0px;"
   	 +"   text-align: right;"
   	 +"   font-size: 12px;"
        +"}"
        +".infoF td {"
       	 +"border-bottom: 1px solid #888;"
        +" height: 20px;"
        +"font-size: 12px;"
        +"}"
        +".infoT {"
       	  +"border-top:1px solid #888;"
         +"margin-top: 0px;"
         +"}"
         +".infoT th{"
       	 +"text-align: center; "
        +"height: 25px;"
        +"font-size: 12px;"
        +"background-color: #888;"
        +"}"
        +".infoT td{"
       	 +"text-align: left;"
        +"height: 20px;"
        +"font-size: 12px;"
        +"border-bottom: 1px solid #888;"
        +"}"
        +"form th,form td{"
        +" padding:1px;"
        +" }"
        +"</style>"
    +"</head><body>";  
   var content = "";  
   
   for(var i=1;i<=size;i++){
   	var p='page'+i;
       var str = document.getElementById(p).innerHTML;     //获取需要打印的页面元素 ，page1元素设置样式page-break-after:always，意思是从下一行开始分割。  
       content = content + str;  
   }
     
   printStr = printStr+content+"</body></html>";                                                
   pwin=window.open("Print.htm","print"); //如果是本地测试，需要先新建Print.htm，如果是在域中使用，则不需要  
   pwin.document.write(printStr);  
   pwin.document.close();                   //这句很重要，没有就无法实现    
   
   setTimeout(printTimeOut,500);
  
	} 
	
	function printTimeOut(){
		 pwin.print();  
	}
</script>
</head>
<body  width="80%">
<div style="width: 100%;height: 100%;text-align: left;padding:0px;overflow-y:auto;" align="center">
<div align="left">
    <a href="javascript:;" id="printButton" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true" onclick="myPrint(${pageSize})"><spring:message code="purchase.delivery.Print"/>
    <!-- 打印 --></a>
</div>
<c:forEach var="vo" items="${volist}" varStatus="status1">
<div id="page${status1.count}">
    <div >
		<table width="100%" style="margin-top: 1px; ">
		    <tr>
		        <td width="80%" align="center">
		            <h1 align="center"><spring:message code="purchase.delivery.Give"/> <!-- 送 -->
		           &nbsp;&nbsp;&nbsp;&nbsp;<spring:message code="purchase.delivery.Goods"/><!-- 货 -->&nbsp;&nbsp;&nbsp;&nbsp;<spring:message code="purchase.delivery.Single"/><!-- 单 -->
		            </h1>
		        </td>
		        <td width="20%" nowrap align="left" style="font: 12px tahoma,arial,helvetica,sans-serif;color:#444; vertical-align: top;">
		                 <span style="font-weight: bold;">
		                 <!-- ASN单号 --><spring:message code="purchase.delivery.ASNNumbersASN"/>：${delivery_Info.deliveryCode}</span></br>
		                <div id="qrcode${status1.count}" name="sxlcode" style="text-align: left;display:inline-block"></div>
						<textarea id="qrcode${status1.count}text" style="display: none;">${deliveryCode}</textarea>
		                <br/>
		                <span style="font-weight: bold;"><!-- 制单日期 --><spring:message code="purchase.delivery.DateOfMakingaDate"/>
		                ：</span><fmt:formatDate value="${delivery_Info.createTime}" pattern="yyyy-MM-dd" />
		                <br/>
		                <span style="font-weight: bold;"><!-- 签字 --><spring:message code="purchase.delivery.Sign"/>
		                ：</span>
		            </div>
		        </td>
		    </tr>    
		</table>
		<table class="infoF" width="100%">
			<tr>
	    		<td><span style="font-weight: bold;"><!-- 收货公司 --><spring:message code="purchase.delivery.ReceivingCompany"/>
	    		：</span>${delivery_Info.buyer.name}</td>
	    		<td><span style="font-weight: bold;"><!-- 供应商名称 --><spring:message code="purchase.delivery.VendorName"/>
	    		：</span>${delivery_Info.vendor.name}</td>
			</tr>
			<tr>
	    		<td><span style="font-weight: bold;"><!-- 收货地址 --><spring:message code="purchase.delivery.ReceivingAddress"/>
	    		：</span>${delivery_Info.deliveryAddress}</td>
	    		<td><span style="font-weight: bold;"><!-- 供应商地址 --><spring:message code="purchase.delivery.SupplierAddress"/>
	    		：</span></td>
			</tr>
			<tr>
	    		<td><span style="font-weight: bold;"><!-- 收货电话 --><spring:message code="purchase.delivery.ReceivingTelephone"/>
	    		：</span>${delivery_Info.deliveryTel}</td>
	    		<td><span style="font-weight: bold;"><!-- 联系电话 --><spring:message code="purchase.delivery.ContactNumbers"/>
	    		：</span>${delivery_Info.logisticsTel}</td>
			</tr>
			<tr>
						<td><span style="font-weight: bold;"><!-- 收货联系人 --><spring:message code="purchase.delivery.ReceivingContact"/>
						：</span>${delivery_Info.deliveryContacter}</td>
	    		<td><span style="font-weight: bold;"><!-- 交货方式 --><spring:message code="purchase.delivery.DeliveryMode"/>
	    		：</span>${delivery_Info.transportName}</td>
	    	</tr>
	    			
	    		<tr>
	    		<td><span style="font-weight: bold;"><!-- 大包装总数 --><spring:message code="purchase.delivery.TheTotalNumberOfLargePackages"/>
	    		：</span><fmt:formatNumber value="${delivery_Info.anzpk}" pattern="#,##0"/></td>
	    		<td></td>
				</tr>
			</table>
			
			<table width="100%" class="infoT" border="1px">
		        <tr >
		            <th width="5%"><!-- 序号 --><spring:message code="purchase.delivery.SerialNumber"/>
		            </th>
		            <th width="10%"><!-- DN号 --><spring:message code="purchase.delivery.DNNumbers"/>
		            </th>
		            <th width="10%"><!-- 采购订单号 --><spring:message code="purchase.delivery.PurchaseOrderNumber"/>
		            </th>
		            <th width="10%"><!-- 物料编码 --><spring:message code="purchase.delivery.MaterialCoding"/>
		            </th>
		            <th width="10%"><!-- 物料描述 --><spring:message code="purchase.delivery.MaterialDescription"/>
		            </th>
		            <c:if test="${delivery_Info.shipType==-1}"> 
		              <th width="15%"><!-- 交货单信息 --><spring:message code="purchase.delivery.DeliveryOrdersInformation"/>
		              </th>
		            </c:if> 
		            <th  width="5%"><!-- 单位 --><spring:message code="purchase.delivery.Company"/>
		            </th>
		            <th  width="10%"><!-- 发货数量 --><spring:message code="purchase.delivery.QuantityShipped"/>
		            </th>
		            <th  width="10%"><!-- 生产日期 --><spring:message code="purchase.delivery.DateOfManufacture"/>
		            </th>
		            <th  width="5%"><!-- 版本 --><spring:message code="purchase.delivery.Edition"/>
		            </th>
		            <th  width="10%"><!-- 批号 --><spring:message code="purchase.delivery.BatchNumber"/>
		            </th>
		        </tr>
		        <c:forEach var="bo" items="${vo.deliveryItem}" varStatus="status">
		            <tr>
		                <td nowrap="nowrap">${(status1.count-1)*pageCount+status.count}</td>
		                <td nowrap="nowrap">${bo.dn}</td>
		                <td nowrap="nowrap">${bo.orderItem.order.orderCode}</td>
		                <td nowrap="nowrap">${bo.material.code}</td>
		                <td nowrap="nowrap">${bo.material.name}</td>
		                <c:if test="${delivery_Info.shipType==-1}"> 
		                <td style="text-align: center; vertical-align: top;">
		                     <div id="qrcodeRepl${status1.count}_${status.count}" name="sxlcode" style="text-align: left;display:inline-block"></div>
						     <textarea id="qrcodeRepl${status1.count}_${status.count}text" style="display: none;">${bo.shipQrCode}</textarea>
		                 </td>
		                </c:if> 
		                <td nowrap="nowrap">${bo.orderItemPlan.unitName}</td>
		                <td nowrap="nowrap"><fmt:formatNumber value="${bo.deliveryQty}" pattern="#,##0.000"/></td>
		                <td nowrap="nowrap"><fmt:formatDate value="${bo.manufactureDate}" pattern="yyyy-MM-dd"/></td>
		                <td nowrap="nowrap">${bo.version}</td>
		                <td nowrap="nowrap">${bo.charg}</td>
		            </tr>
		        </c:forEach>
		    </table>
			
			 <table width="100%">
			    <tr>
			        <td >
		                <span style="font-weight: bold;"><!-- 发货人 --><spring:message code="purchase.delivery.Consignor"/>
		                ：</span>${delivery_Info.deliveryUser.name}
			        </td>
			        <td >
		                <span style="font-weight: bold;"><!-- 客户签收（盖章） --><spring:message code="purchase.delivery.Customersignstamp"/>
		                ：</span>
			        </td>
			    </tr>
			    <tr>
			        <td >
		                <span style="font-weight: bold;"><!-- 发货日期 --><spring:message code="purchase.delivery.TheDateOfIssuance"/>
		                ：</span><fmt:formatDate value="${delivery_Info.deliveyTime}" pattern="yyyy-MM-dd" />
			        </td>
			        <td >
		                <span style="font-weight: bold;"><!-- 日 --><spring:message code="purchase.delivery.Day"/>
		                &nbsp;&nbsp;&nbsp;&nbsp;<!-- 期 --><spring:message code="purchase.delivery.Stage"/>
		                ：</span>
			        </td>
			    </tr>
			</table>
			
			 <c:if test="${status1.count != pageSize }">
			<table width="95%"  style="page-break-after:always"></table>
			</c:if>
			
</div>


</div>

</c:forEach>
</div>
<script type="text/javascript" src="${ctx}/static/script/basedata/index.js"></script>
<script type="text/javascript">

//发货单的列表中的信息
function showDeliveryInfo(id){
	$('#form-deliveryitem-info').form('clear');
	$('#form-deliveryitem-info').form('load','${ctx}/manager/order/printdelivery/getDelivery/'+id);
	//详情
	$('#datagrid-deliveryiteminfo-list').datagrid({   
    	url:'${ctx}/manager/order/printdelivery/deliveryitem/' + id     
	});
	$('#datagrid-deliveryiteminfo-list').datagrid('load',{});
}

function goPrint() {
	$("#printButton").hide();
	window.print();
} 
</script>
</body>
</html>