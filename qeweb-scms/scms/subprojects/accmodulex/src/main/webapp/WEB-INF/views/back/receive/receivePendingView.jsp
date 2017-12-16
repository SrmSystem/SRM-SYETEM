<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
	<script type="text/javascript">
	function downReportFmt(v,r,i){
		 if(null!=r.inspectionPath && null!=r.inspectionName){
				return '<a tyle="margin-right:10px" href="javascript:;" onclick="File.download(\''+r.inspectionPath+'\',\'\')">spring:message code="vendor.download"/></a>';/* 下载 */
			}
	}
	</script>	




<div style="padding: 5px" id="div1">
		<div class="easyui-panel">
			<form id="form-deliveryitem-search" method="post" enctype="multipart/form-data" action="">
				<input id="deliveryId" name="id" value="${de.id}" type="hidden"/>
				<div style="padding: 5px;">
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
						<tr>
						    <td width="25%"><spring:message code="vendor.receive.ASNno."/><!-- ASN号 -->：${de.deliveryCode}<input id="id" type="hidden" value="${de.id}"/></td>
						    <td width="25%"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：${de.vendor.code}</td>
						    <td width="25%"><spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：${de.vendor.name}</td>
						    <td width="25%"><spring:message code="vendor.receive.modeTransportation"/><!-- 运输方式 -->：${de.transportName}</td>
						</tr>
						<tr>
						    <td width="25%"><spring:message code="vendor.receive.deliveryTime"/><!-- 发货时间 -->： <fmt:formatDate value="${de.deliveyTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						    <td width="25%"><spring:message code="vendor.receive.receivingCompany"/><!-- 收货公司 -->：${de.companyName}</td>
						    <td width="25%"><spring:message code="vendor.receive.receivingContact"/><!-- 收货联系人 -->：${de.deliveryContacter}</td>
						    <td width="25%"><spring:message code="vendor.receive.pickPhoneNumber"/><!-- 收货联系电话 -->：${de.deliveryTel}</td>   
						</tr>
						<tr>
						    <td width="25%"><spring:message code="vendor.receive.receivingAddress"/><!-- 收货地址 -->：${de.deliveryAddress}</td>
							<td width="25%"><spring:message code="vendor.receive.logisticsCompany"/><!-- 物流公司 -->：${de.logisticsCompany}</td>
							<td width="25%"><spring:message code="vendor.receive.logisticsContact"/><!-- 物流联系人 -->：${de.logisticsContacter}</td>
							<td width="25%"><spring:message code="vendor.receive.logisticsContactNumber"/><!-- 物流联系电话 -->：${de.logisticsTel}</td>
						</tr>
						<tr>
						 	<td width="25%"><spring:message code="vendor.receive.bulkPack"/><!-- 大包装总数 -->：<fmt:formatNumber value="${de.anzpk}" pattern="#,##0"/></td>
						 	<td width="25%"><spring:message code="vendor.receive.estimatedDelivery"/><!-- 预计发货时间 -->： <fmt:formatDate value="${de.planDeliveryDate}" pattern="yyyy-MM-dd"/></td>
						 	<td width="25%"><spring:message code="vendor.receive.logisticsDays"/><!-- 物流天数 -->：<fmt:formatNumber value="${de.ysts}" pattern="#,##0"/></td>
						    <td width="25%"><spring:message code="vendor.receive.estimatedTimeDelivery"/><!-- 预计到货时间 -->： <fmt:formatDate value="${de.expectedArrivalTime }" pattern="yyyy-MM-dd"/></td>
						</tr>
						<tr>
						    <td width="25%"><spring:message code="vendor.attachment"/><!-- 附件 -->：<a href="javascript:;" onclick="File.download('${de.deliveryFilePath}','${de.deliveryFileName}')">${de.deliveryFileName}</a></td> 
						    <td width="25%"></td>
						    <td width="25%"></td>
						    <td width="25%"></td>
						</tr>
						<tr>
						   <td colspan="4">
						   <spring:message code="vendor.remark"/><!-- 备注 -->：<textarea readonly="readonly" style="width: 100%;height:100px">${de.remark}</textarea>
						   </td>
						</tr>
					</table>
				</div>
				<div style="padding: 5px">
			<table id="datagrid-deliveryitem-list" title="发货单详情" class="easyui-datagrid"
				data-options="url:'${ctx}/manager/order/delivery/deliveryitem/${de.id}',method:'post',singleSelect:false,
				onClickCell: onClickCell,pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
				<thead><tr>
				<th data-options="field:'id',hidden:true"></th>
				<th data-options="field:'itemNo'"><spring:message code="vendor.receive.lineNumbers"/><!-- 行号 --></th>
				<th data-options="field:'orderCode', formatter:function(v,r,i){return r.orderCode}"><spring:message code="vendor.receive.purchaseOrderno."/><!-- 采购订单号 --></th>
				<th data-options="field:'materialCode', formatter:function(v,r,i){return r.material.code}"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
				<th data-options="field:'materialName', formatter:function(v,r,i){return r.material.name}"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
				<th data-options="field:'orderQty',formatter:getNumFmt"><spring:message code="vendor.receive.orderNumber"/><!-- 订单数量 --></th>  
				<th data-options="field:'deliveryQty',formatter:getNumFmt"><spring:message code="vendor.receive.deliveryNumber"/><!-- 发货数量 --></th>
				<th data-options="field:'standardBoxNum',formatter:getNumFmt"><spring:message code="vendor.receive.bulkPackQuantity"/><!-- 大包装数量 --></th>
				<th data-options="field:'boxNum',formatter:getNumIntegerFmt"><spring:message code="vendor.receive.bulkPack"/><!-- 大包装总数 --></th>
			    <th data-options="field:'minPackageQty',formatter:getNumFmt"><spring:message code="vendor.receive.smallPackageQuantity"/><!-- 小包装数量 --></th>  
				<th data-options="field:'minBoxNum',formatter:getNumIntegerFmt"><spring:message code="vendor.receive.totalNumberSmallPackages"/><!-- 小包装总数 --></th>
           		<th data-options="field:'requestTime'"><spring:message code="vendor.receive.askDeliveryTime"/><!-- 要求到货时间 --></th>
           		<th data-options="field:'manufactureDate'"><spring:message code="vendor.receive.productionDate"/><!-- 生产日期 --></th> 
           		<th data-options="field:'version'"><spring:message code="vendor.receive.version"/><!-- 版本 --></th>   
				<th data-options="field:'charg'"><spring:message code="vendor.receive.batchNumber"/><!-- 批号 --></th> 
				<th data-options="field:'vendorCharg'"><spring:message code="vendor.receive.tracesBatchNumber"/><!-- 追溯批号 --></th> 
					
           		<th data-options="field:'unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
           		<th data-options="field:'remark'"><spring:message code="vendor.remark"/><!-- 备注 --></th>
           		<th data-options="field:'report' , formatter:downReportFmt  "><spring:message code="vendor.receive.inspectionReport"/><!-- 检验报告 --></th>
				</tr></thead>
			</table>
			</div>
		</form>
		</div>
	</div>
