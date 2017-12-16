<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>
<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
<script type="text/javascript">
function getNumOrderQtyFmt(v,r,i) {
	if(r.orderItem.orderQty=="" || r.orderItem.orderQty==null){
		return 0;
	}
	var num=r.orderItem.orderQty;
    return getNumFmt(num,r,i);  
}

function getNumCol3Fmt(v,r,i) {
	/* if(r.orderItem.col3=="" || r.orderItem.col3==null){
		return 0;
	} 
	var num=r.orderItem.col3;
	*/
	//订单未清数量 = 订单数量-订单收货数量
	var orderQty = r.orderItem.orderQty;
	var receiveQty = r.orderItem.receiveQty;
	var num=orderQty-receiveQty;
    return getNumFmt(num,r,i);  
}

function manageFmt(v,r,i){
	  if(r.shipType==-1){//有补货
		  return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showReplDetail('+ r.deliveryItemId +');"><spring:message code="vendor.receive.replenishmentDetails"/></a>';/* 补货详情 */
	  }
}

//查看补货单详情
function showReplDetail(id){
	var clientHeight = document.body.clientHeight;	
	new dialog().showWin('<spring:message code="vendor.receive.detailsOrder"/>', 1000, clientHeight, '${ctx}/manager/order/delivery/toReplDlvItem/'+id);/* 补货单详情 */
}
</script>
<div style="padding: 5px">
	<div class="easyui-panel">
		<form id="form-receiveitem-search" method="post" enctype="multipart/form-data">
		 <input id="receiveId" name="id" value="${re.id}" type="hidden"/>
		 <div style="padding: 5px">
		 <table style="text-align:left;padding:5px;margin:auto;" cellpadding="5">
			<tr>
				<td width="30%"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：${re.vendor.code}</td>
				<td width="30%"><spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：${re.vendor.name}</td>
			    <td width="25%"><spring:message code="vendor.receive.modeTransportation"/><!-- 运输方式 -->：${de.transportName}</td>
			</tr>
			<tr>
				<td><spring:message code="vendor.receive.ASNinvoiceNo"/><!-- ASN发货单号 -->：${re.delivery.deliveryCode}</td>
				<td><spring:message code="vendor.receive.deliveryTime"/><!-- 发货时间 -->：${re.delivery.deliveyTime}</td>
				<td></td>
			</tr>
		</table>
		</div>
		<div style="padding: 5px;">
		<table id="datagrid-receiveitem-list" title="收货单详情" class="easyui-datagrid" 
			data-options="url:'${ctx}/manager/order/receive/receiveitem/${re.id}',method:'post',singleSelect:false,
			pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
			>
			<thead><tr>
			<th data-options="field:'manage',formatter:manageFmt"></th>
			<th data-options="field:'orderCode', formatter:function(v,r,i){return isNULL(isNULL(isNULL(r.orderItem).order).orderCode)}"><spring:message code="vendor.receive.purchaseOrderno."/><!-- 采购订单号 --></th>
			<th data-options="field:'itemNo', formatter:function(v,r,i){return isNULL(isNULL(r.orderItem).itemNo)}"><spring:message code="vendor.receive.lineNumbers"/><!-- 行号 --></th>
			<th data-options="field:'materialCode'"><spring:message code="vendor.receive.materialNo."/><!-- 物料号 --></th>
			<th data-options="field:'materialName'"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
			
			<th data-options="field:'orderItem.orderQty',formatter:getNumOrderQtyFmt"><spring:message code="vendor.receive.cargoQuantity"/><!-- 要货数量 --></th>
			<th data-options="field:'unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
			
			<th data-options="field:'deliveryCode', formatter:function(v,r,i){return r.deliveryCode}"><spring:message code="vendor.receive.ASNNumber"/><!-- ASN单号 --></th>
			<th data-options="field:'dn', formatter:function(v,r,i){return r.dn}">DN</th>
			<th data-options="field:'orderItem.col3',formatter:getNumCol3Fmt"><spring:message code="vendor.receive.quantityOrdernotClear"/><!-- 订单未清数量 --></th>
			<th data-options="field:'deliveryQty',formatter:getNumFmt"><spring:message code="vendor.receive.supplierDeliveryQuantity"/><!-- 供应商发货数量 --></th>
			<th data-options="field:'receiveQty',formatter:getNumFmt"><spring:message code="vendor.receive.AACactualReceiptQuantity"/><!-- AAC实际收货数量 --></th>
			<th data-options="field:'diffQty',formatter:getNumFmt"><spring:message code="vendor.receive.quantityVariance"/><!-- 差异数量 --></th>
			<th data-options="field:'zdjsl',formatter:getNumFmt"><spring:message code="vendor.receive.numberAACInspection"/><!-- AAC待检数量 --></th>
			<th data-options="field:'zzjbl',formatter:getNumFmt"><spring:message code="vendor.receive.quantityDefective"/><!-- 质量不良数量 --></th>
			<th data-options="field:'zsjhg',formatter:getNumFmt"><spring:message code="vendor.receive.quantityQualifiedInspection"/><!-- 送检合格数量 -->	</th>
			
			<th data-options="field:'zllbl',formatter:getNumFmt"><spring:message code="vendor.receive.badQuantityIncoming"/><!-- 来料不良数量 --></th>
			<th data-options="field:'badRate'"><spring:message code="vendor.receive.defectiveRate"/><!-- 不良率 --></th>
			</tr></thead>
		</table>
		</div>
	</form>
	</div>
 </div>