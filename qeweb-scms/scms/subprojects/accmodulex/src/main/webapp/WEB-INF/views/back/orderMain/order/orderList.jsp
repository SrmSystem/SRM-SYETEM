<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title><spring:message code="purchase.orderMain.orderList.PurchaseOrderManagement"/></title>
	<script type="text/javascript">
	
	$(function() { 
	/* 	var tmp =  ${vendor}; */
		/* if( tmp == false ){ */
			$('#datagrid-order-list').datagrid({
				rowStyler:function(index,row){
					if (row.isReject == 1 || row.isRed == 1){
						return 'background-color:red;color:black;';
					}
				}
			});
	/* 	} */
		
	});

		
	
	
		//采购主订单格式化编码
		function orderCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Order.showOrderItemList('+ r.id +','+${vendor}+');">' + r.orderCode + '</a>';
		}
		//采购明细格式化编码
		function orderCodeItemFmt(v,r,i){
			return r.order.orderCode;
		}
	</script>

	<script type="text/javascript" src="${ctx}/static/script/purchase/OrderRowEditor.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/Feedback.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/OrderMain.js"></script>
    <script type="text/javascript" src="${ctx}/static/script/basedata/dialog.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-order-list" title="<spring:message code="purchase.orderMain.orderList.PurchaseOrderList"/>" class="easyui-datagrid"  fit="true"
		data-options="method:'post',singleSelect:false,   
		toolbar:'#orderListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'orderCode',formatter:orderCodeFmt"><spring:message code="purchase.orderMain.orderList.PurchaseOrderNumber"/> </th>
		<c:if test="${vendor == false}">  
			<th data-options="field:'purOrderTypeId' ,formatter:function(v,r,i){return r.purOrderType.name;}" ><spring:message code="purchase.orderMain.orderList.OrderType"/></th>
		</c:if>
		<th data-options="field:'purchaseGroup',formatter:function(v,r,i){ if(r.purchasingGroup ){ return r.purchasingGroup.name;;} else{ return  ''; } }"><spring:message code="purchase.orderMain.orderList.PurchasingTeam"/></th>   
		<th data-options="field:'companyCode',formatter:function(v,r,i){   if(r.company ){ return r.company.code;} else{ return  ''; } }"><spring:message code="purchase.orderMain.orderList.CompanyCode"/></th>
		<th data-options="field:'companyName',formatter:function(v,r,i){  if(r.company ){ return r.company.name;} else{ return  ''; } }"><spring:message code="purchase.orderMain.orderList.CompanyName"/></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="purchase.orderMain.orderList.vendorCode"/></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}"><spring:message code="purchase.orderMain.orderList.VendorName"/></th>
	    <th data-options="field:'ztermms'"><spring:message code="purchase.orderMain.orderList.TermOfPayment"/></th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="purchase.orderMain.orderList.PublishingState"/></th> 
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="purchase.orderMain.orderList.ConfirmStatus"/></th>   
		<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="purchase.orderMain.orderList.ShippingState"/></th>   
		<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="purchase.orderMain.orderList.ReceivingState"/></th>  
	    <th data-options="field:'frgke', formatter:function(v,r,i){if(v=='R') return '<spring:message code="purchase.orderMain.orderList.Approved"/>';else return '<spring:message code="purchase.orderMain.orderList.NotApproved"/>';}   "><spring:message code="purchase.orderMain.orderList.ApprovedStatus"/></th> 
		<th data-options="field:'aedat',formatter:function(v,r,i){return r.aedat;}"><spring:message code="purchase.orderMain.orderList.CreationTime"/></th>   
   
	<!-- 	<th data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(v,'closeStatus',false);}">关闭状态</th> -->
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		</tr></thead>
	</table>
	<div id="orderListToolbar" style="padding:5px;">
		<div>
			<c:if test="${vendor == false}">  
				<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()">同步订单</a> -->
			</c:if>  
			<c:if test="${vendor}">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="orderOpt.operateOrder('datagrid-order-list','confirm','order')"><spring:message code="purchase.orderMain.orderList.Confirm"/></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="orderOpt.operateOrder('datagrid-order-list','reject','order')"><spring:message code="purchase.orderMain.orderList.Reject"/></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="exportExcel(${vendor})"><spring:message code="purchase.orderMain.orderList.Export"/></a>
			</c:if>  
		</div>
		<div>
			<form id="form-order-search" method="post">
			<c:if test="${vendor == false}"> 
			<spring:message code="purchase.orderMain.orderList.OrderType"/>：<input class="easyui-combobox" data-options="valueField:'value',textField:'text',url : '${ctx}/manager/basedata/dict/getDict/PURCHASE_ORDER_TYPE',editable:false" style="width:140px;" name="search-EQ_bsart" />
			</c:if>  
			<spring:message code="purchase.orderMain.orderList.PurchaseOrderNumber"/>：<input type="text" name="search-LIKE_orderCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.orderList.CreationDate"/>：<input class="easyui-datebox" name="search-GTE_aedat" data-options="editable:false"  value="" style="width:150px">&nbsp;&nbsp;<spring:message code="purchase.orderMain.orderList.Reach"/>&nbsp;&nbsp;
						          <input class="easyui-datebox" name="search-LTE_aedat" data-options="editable:false"  value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;<br>
			<spring:message code="purchase.orderMain.orderList.vendorCode"/>：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.orderList.VendorName"/>：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.orderList.CompanyCode"/>：<input type="text" name="search-LIKE_company.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.orderList.PurchasingGroupCode"/>：<input type="text" name="search-LIKE_purchasingGroup.code" class="easyui-textbox" style="width:80px;"/><br>
			<spring:message code="purchase.orderMain.orderList.ConfirmStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="purchase.orderMain.orderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderList.ToBeConfirmed"/></option><option value="1"><spring:message code="purchase.orderMain.orderList.AlreadyConfirmed"/></option><option value="2"><spring:message code="purchase.orderMain.orderList.PartialConfirmation"/></option></select>
			<spring:message code="purchase.orderMain.orderList.PublishingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="purchase.orderMain.orderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderList.PendingRelease"/></option><option value="1"><spring:message code="purchase.orderMain.orderList.AlreadyPublished"/></option><option value="2"><spring:message code="purchase.orderMain.orderList.PartialRelease"/></option></select>
			<spring:message code="purchase.orderMain.orderList.ShippingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_deliveryStatus"><option value="">-<spring:message code="purchase.orderMain.orderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderList.PendingShipment"/></option><option value="1"><spring:message code="purchase.orderMain.orderList.AlreadyShipped"/></option><option value="2"><spring:message code="purchase.orderMain.orderList.PartialShipment"/></option></select>
			<spring:message code="purchase.orderMain.orderList.ReceivingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_receiveStatus"><option value="">-<spring:message code="purchase.orderMain.orderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderList.GoodToBeReceived"/></option><option value="1"><spring:message code="purchase.orderMain.orderList.AlreadyReceived"/></option><option value="2"><spring:message code="purchase.orderMain.orderList.PartialReceipt"/></option></select>
		<!-- 	关闭状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-全部-</option><option value="0">未关闭</option><option value="1">已关闭</option></select> -->
			<tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			<!-- 待办 --><input type="hidden" id="backlogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 -->
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder('form-order-search','datagrid-order-list')">查询</a>
			<a href="javascript:;" class="easyui-linkbutton"  onclick="$('#form-order-search').form('reset')">重置</a> 
			
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
	
<!-- 供应商驳回窗口 -->
	<div id="win-reject" class="easyui-dialog" title='<spring:message code="purchase.orderMain.orderList.Reject"/>' style="width:450px;height:200px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb2'">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-reject" method="post" >
			    <input id="reject_type" name="reject_type" type="hidden"/>
				<input id="reject_ids" name="reject_ids" type="hidden"/>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="label.reson"/><!-- 原因 --><textarea rows="4" cols="50" id="reject_reason" name="reject_reason"></textarea></td> 
				</tr>
				</table>
				<div id="dialog-adder-bb2" style="text-align: center;">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="orderOpt.rejectOper()"><spring:message code="button.submit"/><!-- 提交 --></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-reject').form('reset')"><spring:message code="button.reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
<script type="text/javascript">
$(function() {
	var searchParamArray = $('#form-order-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-order-list').datagrid({url:'${ctx}/manager/order/purchasemainorder/${vendor}',queryParams:searchParams});
});

function searchOrder(formId,tableId){
	document.getElementById("backlogId").value="";//清除待办
	var _formId = "#"+formId;
	var _tableId = "#"+tableId;
	var searchParamArray = $(_formId).serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$(_tableId).datagrid('load',searchParams);
}

function sycOrder(){
	$.messager.progress();
	$.ajax({
		url:'${ctx}/manager/order/purchasemainorder/sycOrder',
		type:'POST',
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
						msg:  data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#datagrid-order-list').datagrid('reload'); 
				}else{
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}
function exportExcel(vendor){
	//支持选择和查询
	var selections = $("#datagrid-order-list").datagrid('getSelections');
	var ids="";
	if(selections.length > 0){
		for(i = 0; i < selections.length; i ++) {
			ids=ids+selections[i].id+",";
		}
	}else{
		ids="1";
	}
	
	$('#form-order-search').form('submit',{
		   url:'${ctx}/manager/order/purchasemainorder/exportExcelByMainIds/'+vendor+"/"+ ids,
		   success:function(data){
				$.messager.progress('close');
			}
	});
    
}



</script>
</body>
</html>
