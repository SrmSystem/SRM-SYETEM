<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="padding:5px;">
<div>
		<c:if test="${!vendor}">  
			<c:if test="${po.publishStatus eq 0 || po.publishStatus eq 2}">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Order.orderViewOperOrderItem(${po.id},${vendor},'publish')"><spring:message code="button.publish"/><!-- 发布 --></a>      
			</c:if>
			<c:if test="${(po.publishStatus eq 1 || po.publishStatus eq 2 ) && po.confirmStatus eq 0 }">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-2012080412301'" onclick="Order.orderViewOperOrderItem(${po.id},${vendor},'unpublish')"><spring:message code="button.cancelPublish"/><!-- 取消发布 --></a>
			</c:if>
		</c:if>
		<c:if test="${vendor}">
			<!-- 部分确认，未确认 ,驳回    +已发布未发货-->
			<c:if test="${po.publishStatus eq 1 && po.deliveryStatus eq 0}">
				<c:if test="${po.confirmStatus eq -1 || po.confirmStatus eq 0 }">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept'" onclick="Order.orderViewOperOrderItem(${po.id},${vendor},'confirm')"><spring:message code="button.confirm"/><!-- 确认 --></a>
			    </c:if>
			    <c:if test="${po.confirmStatus eq -1 || po.confirmStatus eq 0 }">
			    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="Order.displayOrderViewReject(${po.id},${vendor})"><spring:message code="purchase.order.Reject"/></a>
			    </c:if>
			</c:if>
		</c:if>
	</div>
	
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
					<td><spring:message code="purchase.order.PurchasingOrganizationCode"/>：${po.order.buyer.code }</td>
					<td><spring:message code="purchase.order.PurchasingOrganizationName"/>：${po.order.buyer.name }</td>
					<td><spring:message code="base.order.publishStatus"/><!-- 发布状态 -->：<c:if test="${po.publishStatus eq 0 }"><spring:message code="status.publish.0"/><!-- 未发布 --></c:if> <c:if test="${po.publishStatus eq 1 }"><spring:message code="status.publish.1"/><!-- 已发布 --></c:if> <c:if test="${po.publishStatus eq 2 }"><spring:message code="status.publish.2"/><!-- 部分发布 --></c:if></td>
					<td><spring:message code="purchase.order.ReleaseTime"/>：<fmt:formatDate value="${po.publishTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
				</tr>
				<tr>
				    <td><spring:message code="base.user.purchaseUser"/><!-- 采购员 -->：${po.order.purchaseUser.name }</td>
					<td><spring:message code="purchase.order.ReceivingParty"/>：${po.receiveOrg}</td>
					<td><spring:message code="purchase.order.orderQty" /><!-- 需求数量  -->：${po.orderQty }<input type="hidden" id="item_order_qty" value="${po.orderQty }"> </td>
				    <td><spring:message code="purchase.order.requestTime" /><!-- 要求到货日期  -->：<fmt:formatDate value="${po.requestTime }" pattern="yyyy-MM-dd"/> </td>
				</tr>   
				<tr> 
				    <td><spring:message code="status.delivery"/><!-- 发货状态 -->：<c:if test="${po.deliveryStatus eq 0 }"><spring:message code="status.delivery.0"/><!-- 待发货 --></c:if> <c:if test="${po.deliveryStatus eq 1 }"><spring:message code="status.delivery.1"/><!-- 已发货 --></c:if> <c:if test="${po.deliveryStatus eq 2 }"><spring:message code="status.delivery.2"/><!-- 部分发货 --></c:if></td>
				    <td><spring:message code="purchase.order.onwayQty"/><!-- 在途数量 -->：${po.onwayQty }</td>
				    <td><spring:message code="status.confirm"/><!-- 确认状态 -->：<c:if test="${po.confirmStatus eq 0 }"><spring:message code="status.confirm.0"/><!-- 待确认 --></c:if> <c:if test="${po.confirmStatus eq 1 }"><spring:message code="status.confirm.1"/><!-- 已确认 --></c:if> <c:if test="${po.confirmStatus eq 2 }"><spring:message code="status.confirm.2"/><!-- 部分已确认 --></c:if><c:if test="${po.confirmStatus eq -1 }"><spring:message code="purchase.order.Reject"/></c:if></td>
					<td><spring:message code="purchase.order.ConfirmTime"/><!-- 确认时间 -->： <fmt:formatDate value="${po.confirmTime }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td> 
				</tr>
				<tr>
				    <td><spring:message code="status.receive"/><!-- 收货状态 -->：<c:if test="${po.receiveStatus eq 0 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 1 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 2 }"><spring:message code="status.receive.2"/><!-- 部分收货 --></c:if></td>
					<td><spring:message code="purchase.order.receiveQty"/><!-- 收货数量 -->：${po.receiveQty }</td> 
				    <td><spring:message code="purchase.order.OderDate"/>： <fmt:formatDate value="${po.order.orderDate}" pattern="yyyy-MM-dd"/> </td>
				    <td><spring:message code="purchase.order.OffState"/>：<c:if test="${po.closeStatus eq 0 }"><spring:message code="purchase.order.NotClosed"/></c:if><c:if test="${po.closeStatus eq 1 }"><spring:message code="purchase.order.AlreadyClosed"/></c:if></td>
				</tr>
				<c:if test="${!vendor}">  
				<tr>
				<td width="25%"><spring:message code="purchase.order.Price"/>：${po.price}</td>
				<td></td>
				<td></td>
				<td></td>
				</tr>
				</c:if>
			</table>
		</form>
	</div>
</div>
			<!-- 外协 -->
		<c:if test="${po.order.orderType==3}">  
		<div style="height: 250px">
			<table id="datagrid-orderitemprocess-list" title='<spring:message code="purchase.order.ComponentBOM"/>' class="easyui-datagrid"
				data-options="url:'${ctx}/manager/order/process/getPurchaseOrderItemMatList/${po.id}',method:'post',singleSelect:false,
				toolbar:'#purchaseorderprocessToolbar',rownumbers:true,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
					<th data-options="field:'operate',formatter:operateViewFmtEx, width: 80"><spring:message code="purchase.order.operation"/></th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.order.MaterialCoding"/></th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.order.MaterialName"/></th>
				<th data-options="field:'itemNo'"><spring:message code="purchase.order.LineNumber"/></th>   
				<th data-options="field:'orderNum'"><spring:message code="purchase.order.Number"/></th>  
				<th data-options="field:'unit'"><spring:message code="purchase.order.Company"/></th>  
				<th data-options="field:'orderTime'"><spring:message code="purchase.order.DateOfArrival"/></th>  

				</tr></thead>
			</table>
			<div id="purchaseorderprocessToolbar" style="padding:5px;">

			</div>
	</div>
	</c:if>
	

		<div style="height: 220px">
			<table id="datagrid-orderitemplan-list" title='<spring:message code="purchase.order.DetailsOfSupplyPlan"/>' class="easyui-datagrid" fit="true"
				data-options="url:'${ctx}/manager/order/purchaseorder/orderitem/${po.id}?vendor=${vendor}',method:'post',singleSelect:false,onClickCell: RowEditorX.onClickCell,
				toolbar:'#purchaseorderitemToolbar',height:40,rownumbers:true,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.order.MaterialCoding"/></th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.order.MaterialName"/></th>
				<th data-options="field:'receiveOrg'"><spring:message code="purchase.order.ReceivingParty"/></th>   
				<th data-options="field:'orderQty',editor:{type:'numberbox',options:{required:true}}, width: 95"><spring:message code="purchase.order.OrderQuantity"/></th>   
				<th data-options="field:'requestTime',editor:{type:'datebox',options:{required:true,editable:false}}, width: 95"><spring:message code="purchase.order.RequiredArrivalTime"/></th>   
				<th data-options="field:'currency'"><spring:message code="purchase.order.currency"/></th>   
				<th data-options="field:'unitName'"><spring:message code="purchase.order.Company"/></th> 
				<th data-options="field:'deliveryQty'"><spring:message code="purchase.order.QuantityAlreadyIssued"/></th> 
				<th data-options="field:'toDeliveryQty'"><spring:message code="purchase.order.TheMissingNumberHasBeenCreated"/></th> 
				<th data-options="field:'receiveQty'"><spring:message code="purchase.order.AmountReceived"/></th> 
				<th data-options="field:'returnQty'"><spring:message code="purchase.order.ReturnQuantity"/></th> 
				<th data-options="field:'diffQty'"><spring:message code="purchase.order.DifferentialQuantity"/></th> 
				<th data-options="field:'onwayQty'"><spring:message code="purchase.order.UnderWayNumber"/></th> 
				<th data-options="field:'undeliveryQty'"><spring:message code="purchase.order.QuantityNotIssued"/></th> 
				<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="purchase.order.PublishingState"/></th>   
				<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="purchase.order.ConfirmStatus"/></th>   
				<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="purchase.order.ShippingState"/></th>   
				<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="purchase.order.ReceivingState"/></th>   
				<th data-options="field:'rejectReason'"><spring:message code="purchase.order.ReasonsForRejection"/></th> 
				</tr></thead>
			</table>
			<div id="purchaseorderitemToolbar" style="padding:5px;">
			<c:if test="${vendor == false}">  
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditorX.append('datagrid-orderitemplan-list')"><spring:message code="purchase.order.NewlyAdded"/></a>
			   	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="RowEditorX.removeit('datagrid-orderitemplan-list')"><spring:message code="purchase.order.Delete"/></a>
			   	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="Order.saveSplitItemPlan()"><spring:message code="purchase.order.SaveResolution"/></a>
			    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Order.itemPlanOper('publish')"><spring:message code="purchase.order.Release"/></a>
			    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Order.itemPlanOper('unpublish')"><spring:message code="purchase.order.CancelPublication"/></a>
			 </c:if>
			  <c:if test="${vendor}">  
			    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Order.itemPlanOper('confirm')"><spring:message code="purchase.order.Confirm"/></a>
			    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="Order.itemPlanOper('reject')"><spring:message code="purchase.order.Reject"/></a>
			  </c:if> 
			    <input type="hidden" id="item_order_qty" value="${po.orderQty}"/>
			    <input type="hidden" id="item_id" value="${po.id }"/>
			</div>
	</div>
			<!-- 发货单 -->
	<div  style="height: 200px">
			<table id="datagrid-purchasedelivery-list" title='<spring:message code="purchase.order.DetailsOfBillOfDelivery"/>' class="easyui-datagrid"
				data-options="url:'${ctx}/manager/order/delivery/byorderdeliveryitem/${po.id}',method:'post',singleSelect:false,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'deliveryCode',formatter:function(v,r,i){return r.delivery.deliveryCode;}"><spring:message code="purchase.order.ASNInvoiceNo"/></th>
				<th data-options="field:'itemNo'"><spring:message code="purchase.order.LineNumber"/></th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.order.MaterialCoding"/></th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.order.MaterialName"/></th>
				<th data-options="field:'receiveOrg',formatter:function(v,r,i){return r.delivery.receiveOrg;}"><spring:message code="purchase.order.ReceivingParty"/></th>   
				<th data-options="field:'deliveryQty'"><spring:message code="purchase.order.QuantityShipped"/></th>   
				<!-- <th data-options="field:'receiveQty'">收货数量</th>   --> 
				<th data-options="field:'unitName',formatter:function(v,r,i){return r.unitName;}"><spring:message code="purchase.order.Company"/></th>     
				<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(r.delivery.deliveryStatus,'deliveryStatus',false);}"><spring:message code="purchase.order.ShippingState"/></th>   
				<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="purchase.order.ReceivingState"/></th>   
				</tr></thead>
			</table>  
	</div>
			<!-- 收货单 --> 
	<div  style="height: 200px"> 
			<table id="datagrid-purchasereceive-list" title='<spring:message code="purchase.order.ReceiptDetails"/>' class="easyui-datagrid"
				data-options="url:'${ctx}/manager/order/receive/byorderreceiveitem/${po.id}' ,method:'post',singleSelect:false,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'receiveCode',formatter:function(v,r,i){return r.receive.receiveCode;}"><spring:message code="purchase.order.ReceiptNo"/></th>
				<th data-options="field:'deliveryCode',formatter:function(v,r,i){return r.deliveryCode;}"><spring:message code="purchase.order.ASNInvoiceNo"/></th>
				<th data-options="field:'itemNo'"><spring:message code="purchase.order.LineNumber"/></th>
				<th data-options="field:'materialCode'"><spring:message code="purchase.order.MaterialCoding"/></th>
				<th data-options="field:'materialName'"><spring:message code="purchase.order.MaterialName"/></th>
				<th data-options="field:'receiveOrg',formatter:function(v,r,i){return r.receive.receiveOrg;}">收货方<spring:message code="purchase.order.operation"/></th>   
				<th data-options="field:'deliveryQty'"><spring:message code="purchase.order.QuantityShipped"/></th>   
				<th data-options="field:'receiveQty'"><spring:message code="purchase.order.ReceiptNumber"/></th>   
				<th data-options="field:'inStoreQty'">入<spring:message code="purchase.order.QuantityOfWarehousing"/></th>   
				<th data-options="field:'returnQty'"><spring:message code="purchase.order.CheckOutQuantity"/></th>     
				</tr></thead>
			</table>  
	</div>
</div>
