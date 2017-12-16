<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="purchase.delivery.InvoiceChecklist"/></title>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-request-list" title='<!-- 要货单列表 --><spring:message code="purchase.delivery.ListOfRequiredInvoices"/>' class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/delivery/getDlvItemsByOderItemId/${orderItemId}',method:'post',singleSelect:false,
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>  
		<thead><tr>
					<th data-options="field:'delivery.deliveryCode', formatter:function(v,r,i){return r.delivery.deliveryCode}"><spring:message code="purchase.delivery.ASNNumber"/><!-- ASN号 --></th>
					<th data-options="field:'delivery.deliveyTime', formatter:function(v,r,i){return r.delivery.deliveyTime}"><spring:message code="purchase.delivery.DeliveryTime"/><!-- 发货时间 --></th>
					<th data-options="field:'dn'">DN</th>
				    <th data-options="field:'materialCode', formatter:function(v,r,i){return r.material.code}"><spring:message code="purchase.delivery.MaterialCoding"/><!-- 物料编码 --></th>
					<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name}"><spring:message code="purchase.delivery.MaterialName"/><!-- 物料名称 --></th>
					
					<th data-options="field:'orderQty',formatter:getNumFmt"><spring:message code="purchase.delivery.QuantityOfGoodsToBePurchased"/><!-- 要货数量 --></th>   
					<th data-options="field:'deliveryQty',formatter:getNumFmt"><spring:message code="purchase.delivery.QuantityShipped"/><!-- 发货数量 --></th>   
					<th data-options="field:'standardBoxNum',formatter:getNumFmt"><spring:message code="purchase.delivery.QuantityOfLargePackage"/><!-- 大包装数量 --></th>
					<th data-options="field:'boxNum',formatter:getNumIntegerFmt"><spring:message code="purchase.delivery.TheTotalNumberOfLargePackages"/><!-- 大包装总数 --></th>
					<th data-options="field:'minPackageQty',formatter:getNumFmt"><spring:message code="purchase.delivery.QuantityOfSmallPackages"/><!-- 小包装数量 --></th>  
					<th data-options="field:'minBoxNum',formatter:getNumIntegerFmt"><spring:message code="purchase.delivery.TheTotalNumberOfSmallPackages"/><!-- 小包装总数 --></th>
					
					<th data-options="field:'requestTime'"><spring:message code="purchase.delivery.RequiredArrivalTime"/><!-- 要求到货时间 --></th>   
					<th data-options="field:'manufactureDate'"><spring:message code="purchase.delivery.DateOfManufacture"/><!-- 生产日期 --></th> 
					<th data-options="field:'version'"><spring:message code="purchase.delivery.Edition"/><!-- 版本 --></th>   
					<th data-options="field:'charg'"><spring:message code="purchase.delivery.BatchNumber"/><!-- 批号 --></th> 
					<th data-options="field:'vendorCharg'"><spring:message code="purchase.delivery.TraceabilityBatchNumber"/><!-- 追溯批号 --></th> 
					
					<th data-options="field:'meins'"><spring:message code="purchase.delivery.Company"/><!-- 单位 --></th>
					<th data-options="field:'remark'"><spring:message code="purchase.delivery.Remarks"/><!-- 备注 --></th>    
					
					<th data-options="field:'zlock',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Locked"/><!-- 已锁定 -->';else return '<spring:message code="purchase.delivery.NotLocked"/><!-- 未锁定 -->';}"><spring:message code="purchase.delivery.LockState"/><!-- 锁定状态 --></th>
					<th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.delivery.AlreadyFrozen"/><!-- 已冻结 -->';else return '<spring:message code="purchase.delivery.NotFrozen"/><!-- 未冻结 -->';}"><spring:message code="purchase.delivery.FrozenState"/><!-- 冻结状态 --></th>
					<th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Deleted"/><!-- 已删除 -->';else return '<spring:message code="purchase.delivery.NotDeleted"/><!-- 未删除 -->';}"><spring:message code="purchase.delivery.DeleteMark"/><!-- 删除标记 --></th>
					<th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Delivered"/><!-- 已交付 -->';else return '<spring:message code="purchase.delivery.Undelivered"/><!-- 未交付 -->';}"><spring:message code="purchase.delivery.DeliveryState"/><!-- 交付状态 --></th>
					<th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.NO"/><!-- 否 -->';else return '<!-- 是 --><spring:message code="purchase.delivery.YES"/>';}"><spring:message code="purchase.delivery.InwardDeliveryLogo"/><!-- 内向交货单标识 --></th>
					<th data-options="field:'delivery.auditStatus',formatter:function(v,r,i){if(r.delivery.auditStatus==1) return '<spring:message code="purchase.delivery.PassAudited"/><!-- 审核通过 -->';else if(r.delivery.auditStatus==-1) return '<spring:message code="purchase.delivery.AuditRejection"/><!-- 审核驳回 -->'; return '<spring:message code="purchase.delivery.NotAudited"/><!-- 未审核 -->';}"><spring:message code="purchase.delivery.AuditStatus"/><!-- 审核状态 --></th>
				    <th data-options="field:'delivery.deliveryStatus',formatter:function(v,r,i){if(r.delivery.deliveryStatus==1) return '<spring:message code="purchase.delivery.AlreadyShipped"/><!-- 已发货 -->';else return '<spring:message code="purchase.delivery.NotShipped"/><!-- 未发货 -->';}"><spring:message code="purchase.delivery.ShippingState"/><!-- 发货状态 --></th>
				    <th data-options="field:'delivery.receiveStatus',formatter:function(v,r,i){if(r.delivery.receiveStatus==1) return '<spring:message code="purchase.delivery.AlreadyReceived"/><!-- 已收货 -->';else return '<spring:message code="purchase.delivery.GoodsNotReceived"/><!-- 未收货 -->';}"><spring:message code="purchase.delivery.ReceivingState"/><!-- 收货状态 --></th>
				</tr>
		</thead>
	</table>
</body>
</html>

