<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="padding:5px;">

	<div class="easyui-panel" data-options="fit:true">
		<form id="form-purchaseorderitem-search">
			<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td width="25%"><spring:message code="purchase.order.orderCode"/><!--采购订单号  -->：${po.order.orderCode }</td>
					<td width="25%"><spring:message code="purchase.order.itemNo"/><!-- 行号 -->：${po.itemNo }</td>
					<td width="25%"><spring:message code="base.vendor.code"/><!-- 供应商代码 -->：${po.order.vendor.code }</td>
				    <td width="25%"><spring:message code="base.vendor.name"/><!-- 供应商名称 -->：${po.order.vendor.name }</td>
				</tr>
				<tr>
					<td><spring:message code="purchase.orderMain.orderView.CompanyCode"/>：${po.order.company.code }</td>
					<td><spring:message code="purchase.orderMain.orderView.CompanyCodeName"/>：${po.order.company.name }</td>
					<td><spring:message code="base.order.publishStatus"/><!-- 发布状态 -->：<c:if test="${po.publishStatus eq 0 }"><spring:message code="status.publish.0"/><!-- 未发布 --></c:if> <c:if test="${po.publishStatus eq 1 }"><spring:message code="status.publish.1"/><!-- 已发布 --></c:if> <c:if test="${po.publishStatus eq 2 }"><spring:message code="status.publish.2"/><!-- 部分发布 --></c:if></td>
					<td><spring:message code="purchase.orderMain.orderView.ReleaseTime"/>：<fmt:formatDate value="${po.order.publishTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				</tr>
				<tr>
				    <td><spring:message code="purchase.orderMain.orderView.PurchasingTeam"/>：${po.order.purchasingGroup.name }</td>
					<td><spring:message code="purchase.orderMain.orderView.ReceivingParty"/>：${po.receiveOrg}</td>
					<td><spring:message code="purchase.order.orderQty" /><!-- 需求数量  -->：${po.orderQty }<input type="hidden" id="item_order_qty" value="${po.orderQty }"> </td>
				    <td><spring:message code="purchase.order.requestTime" /><!-- 要求到货日期  -->：<fmt:formatDate value="${po.requestTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				</tr>   
				<tr> 
				    <td><spring:message code="status.delivery"/><!-- 发货状态 -->：<c:if test="${po.deliveryStatus eq 0 }"><spring:message code="status.delivery.0"/><!-- 待发货 --></c:if> <c:if test="${po.deliveryStatus eq 1 }"><spring:message code="status.delivery.1"/><!-- 已发货 --></c:if> <c:if test="${po.deliveryStatus eq 2 }"><spring:message code="status.delivery.2"/><!-- 部分发货 --></c:if></td>
				    <td><spring:message code="purchase.order.onwayQty"/><!-- 在途数量 -->：${po.onwayQty }</td>
				    <td><spring:message code="status.confirm"/><!-- 确认状态 -->：<c:if test="${po.confirmStatus eq 0 }"><spring:message code="status.confirm.0"/><!-- 待确认 --></c:if> <c:if test="${po.confirmStatus eq 1 }"><spring:message code="status.confirm.1"/><!-- 已确认 --></c:if> <c:if test="${po.confirmStatus eq 2 }"><spring:message code="status.confirm.2"/><!-- 部分已确认 --></c:if><c:if test="${po.confirmStatus eq -1 }"><spring:message code="purchase.orderMain.orderView.Reject"/></c:if></td>
					<td><spring:message code="purchase.orderMain.orderView.ConfirmTime"/><!-- 确认时间 -->： <fmt:formatDate value="${po.confirmTime }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td> 
				</tr>
				<tr>
				    <td><spring:message code="status.receive"/><!-- 收货状态 -->：<c:if test="${po.receiveStatus eq 0 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 1 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 2 }"><spring:message code="status.receive.2"/><!-- 部分收货 --></c:if></td>
					<td><spring:message code="purchase.order.receiveQty"/><!-- 收货数量 -->：${po.receiveQty }</td> 
				    <td><spring:message code="purchase.orderMain.orderView.OderDate"/>： <fmt:formatDate value="${po.order.aedat}" pattern="yyyy-MM-dd"/> </td>
				    <td><spring:message code="purchase.orderMain.orderView.OffState"/>：<c:if test="${po.closeStatus eq 0 }"><spring:message code="purchase.orderMain.orderView.NotClosed"/></c:if><c:if test="${po.closeStatus eq 1 }"><spring:message code="purchase.orderMain.orderView.AlreadyClosed"/></c:if></td>
				</tr>
			</table>
		</form>
	</div>
</div>

		<div style="height: 300px">
			<table id="datagrid-orderitemplan-list" title="<spring:message code="purchase.orderMain.orderView.DetailsOfSupplyPlan"/>" class="easyui-datagrid"
				data-options="url:'${ctx}/manager/order/purchasemainorder/orderitem/${po.id}?vendor=${vendor}',method:'post',singleSelect:false,onClickCell: RowEditorX.onClickCell,
				toolbar:'#purchaseorderitemToolbar',rownumbers:true,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.orderMain.orderView.MaterialCoding"/></th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.orderMain.orderView.MaterialName"/></th>
				<th data-options="field:'receiveOrg'"><spring:message code="purchase.orderMain.orderView.ReceivingParty"/></th>   
				<th data-options="field:'orderQty',width: 95"><spring:message code="purchase.orderMain.orderView.OrderQuantity"/></th>   
				<th data-options="field:'requestTime',width: 95,sortable:true "><spring:message code="purchase.orderMain.orderView.RequiredArrivalTime"/></th>   
				<!-- <th data-options="field:'currency'">币种</th>    -->
				<th data-options="field:'unitName'"><spring:message code="purchase.orderMain.orderView.Company"/></th> 
				<th data-options="field:'deliveryQty' ,formatter:function(v,r,i){if(v=='' || v == null ) return '0';else return  v;} "><spring:message code="purchase.orderMain.orderView.QuantityAlreadyIssued"/></th> 
				<th data-options="field:'toDeliveryQty',formatter:function(v,r,i){if(v=='' || v == null ) return '0';else return  v;} "><spring:message code="purchase.orderMain.orderView.TheMissingNumberHasBeenCreated"/></th> 
				<th data-options="field:'receiveQty' ,formatter:function(v,r,i){if(v=='' || v == null ) return '0';else return  v;} "><spring:message code="purchase.orderMain.orderView.AmountReceived"/></th> 
				<th data-options="field:'returnQty' ,formatter:function(v,r,i){if(v=='' || v == null ) return '0';else return  v;} "><spring:message code="purchase.orderMain.orderView.ReturnQuantity"/></th> 
				<th data-options="field:'diffQty' ,formatter:function(v,r,i){if(v=='' || v == null ) return '0';else return  v;} "><spring:message code="purchase.orderMain.orderView.DifferentialQuantity"/></th> 
				<th data-options="field:'onwayQty' ,formatter:function(v,r,i){if(v=='' || v == null ) return '0';else return  v;} "><spring:message code="purchase.orderMain.orderView.UnderWayNumber"/></th> 
				<th data-options="field:'undeliveryQty' ,formatter:function(v,r,i){if(v=='' || v == null ) return '0';else return  v;} "><spring:message code="purchase.orderMain.orderView.QuantityNotIssued"/></th> 
				<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="purchase.orderMain.orderView.PublishingState"/></th>   
				<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="purchase.orderMain.orderView.ConfirmStatus"/></th>   
				<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="purchase.orderMain.orderView.ShippingState"/></th>   
				<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="purchase.orderMain.orderView.ReceivingState"/></th>   
				<th data-options="field:'rejectReason'"><spring:message code="purchase.orderMain.orderView.ReasonsForRejection"/></th> 
				</tr></thead>
			</table>
	</div>
</div>
