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
	<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/Deliverycommon.js"></script>
    <script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
    <script type="text/javascript">
    function deliveryCodeFmt(v,r,i){
    	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDelivery('+ r.id +','+r.deliveryStatus+');">' + v + '</a>';
    }

    </script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-delivery-list" title='<spring:message code="purchase.delivery.InvoiceChecklist"/>'  fit="true"
		data-options="method:'post',singleSelect:false,
		toolbar:'#deliveryListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"    
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'deliveryCode',formatter:deliveryCodeFmt"><spring:message code="purchase.delivery.ASNInvoiceNo"/></th>
		<th data-options="field:'shipType',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.delivery.Normal"/>';else if(v=='-1') return '<spring:message code="purchase.delivery.Replenishment"/>';}"><spring:message code="purchase.delivery.TypeOfShipment"/></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="purchase.delivery.vendorCode"/></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}"><spring:message code="purchase.delivery.VendorName"/></th>
		<th data-options="field:'factory.code',formatter:function(v,r,i){return isNULL(isNULL(r.factory).code)}"><spring:message code="purchase.delivery.FactoryCode"/></th>
		<th data-options="field:'factory.name',formatter:function(v,r,i){return isNULL(isNULL(r.factory).name)}"><spring:message code="purchase.delivery.FactoryName"/></th>
		<!-- <th data-options="field:'buyerName',formatter:function(v,r,i){return r.buyer.name;}">采购组织名称</th> -->
		<th data-options="field:'purchasingGroupCode'"><spring:message code="purchase.delivery.PurchasingOrganizationCode"/></th>
		<th data-options="field:'purchasingGroupName'"><spring:message code="purchase.delivery.PurchasingOrganizationName"/></th>
		<th data-options="field:'deliveryStatus',formatter:deliveryStatusFmt"><spring:message code="purchase.delivery.ShippingState"/></th>
		<th data-options="field:'deliveyTime'"><spring:message code="purchase.delivery.DeliveryTime"/></th>
		<th data-options="field:'deliveyUserName',formatter:function(v,r,i){if(r.deliveryUser) return r.deliveryUser.name; return '';}"><!-- 发货人 --><spring:message code="purchase.delivery.Consignor"/></th>
		<th data-options="field:'createTime'"><spring:message code="purchase.delivery.InvoiceCreationTime"/></th>
		<th data-options="field:'receiveStatus',formatter:receiveStatusFmt"><spring:message code="purchase.delivery.ReceivingState"/></th>
		<th data-options="field:'auditStatus',formatter:auditStatusFmt"><spring:message code="purchase.delivery.AuditStatus"/></th>
		<th data-options="field:'rejectReason'"><spring:message code="purchase.delivery.ReasonsForRejection"/></th>
		</tr></thead>
	</table>
	<div id="deliveryListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="doAgree()"><!-- 同意 --><spring:message code="purchase.delivery.Agree"/></a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="doReject()"><!-- 拒绝 --><spring:message code="purchase.delivery.Refuse"/></a>
		</div>
		<div>
			<form id="form-delivery-search" method="post">
			<spring:message code="purchase.delivery.ASNInvoiceNo"/>：<input type="text" name="search-LIKE_deliveryCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.delivery.vendorCode"/>：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.delivery.AuditStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_auditStatus"><option value="" >-<spring:message code="purchase.delivery.Whole"/>-</option><option value="0" selected="selected"><spring:message code="purchase.delivery.NotAudited"/></option><option value="1"><spring:message code="purchase.delivery.PassAudited"/></option><option value="-1"><spring:message code="purchase.delivery.AuditRejection"/></option></select>
			<spring:message code="purchase.delivery.InvoiceCreationTime"/><!-- 发货单创建时间 -->:
			<input type="text" name="search-GTE_createTime" data-options="editable:false" class="easyui-datebox" >~  
			<input type="text" name="search-LTE_createTime" data-options="editable:false" class="easyui-datebox" >
			<!-- 待办 --><input type="hidden" id="backlogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 -->
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchDelivery()"><spring:message code="purchase.delivery.Query"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-delivery-search').form('reset')"><spring:message code="purchase.delivery.Reset"/></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
	<!-- 驳回窗口 -->
	<div id="win-reject" class="easyui-dialog" title='<spring:message code="purchase.delivery.Reject"/>' style="width:400px;height:150px"
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
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="rejectOper()"><spring:message code="purchase.delivery.Submit"/></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-reject').form('reset')"> <spring:message code="purchase.delivery.Reset"/></a>
				</div>
			</form>
		</div>
	</div>
	

<script type="text/javascript">
$(function() {
	var searchParamArray = $('#form-delivery-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-delivery-list').datagrid({url:'${ctx}/manager/order/deliveryAudit',queryParams:searchParams});
	
	
	
	//若已锁定|已冻结|已删除|已交付|订单数量减小则当前行高亮显示
		$('#datagrid-delivery-list').datagrid({
			rowStyler:function(index,row){
				if (row.exceptionStatus == 1){
					return 'background-color:red;color:black;';
				}
			}
		});
	
});

//查询
function searchDelivery(){
	document.getElementById("backlogId").value="";//清除待办
	var searchParamArray = $('#form-delivery-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-delivery-list').datagrid('load',searchParams);
}


//查看发货单详情
function showDelivery(id,deliveryStatus){
	var clientHeight = document.body.clientHeight;	
	new dialog2().showWin('<spring:message code="purchase.delivery.DetailsOfBillOfDelivery"/>'/* 发货单详情 */, 1000, clientHeight, ctx + '/manager/order/delivery/getDeliveryView/'+id);
}



//批量同意
function doAgree(){
	var selections = $('#datagrid-delivery-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！ */,'info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].auditStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.IncludeTheApprovedInvoiceAndCannotRepeatTheAgreement"/>'/* 包含已审核通过的发货单，不能重复同意 */,'error');  
			return false;
		}
		if(selections[i].auditStatus == -1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.IncludeRejectedInvoiceCannotAgree"/>'/* 包含已驳回的发货单，不能同意 */,'error');  
			return false;
		}
	}
	//审核添加锁定状态、冻结状态、删除状态、交付状态、订单减小的判断
	var temp = true;	//返回结果
	var msg = '';		//返回内容
	var datas = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/deliveryAudit/judgeAudit',
		type:'POST',
		contentType : 'application/json',
		dataType : 'json',
		data:datas ,
		async: false,
		success:function(data){
			temp = data.success;
			msg = data.message;
		}
	});
	if(temp == false){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',msg,'error');  
		return false;
	}else{
	var params = $.toJSON(selections);
	$.messager.confirm('<spring:message code="purchase.delivery.OperationHints"/>'/* 操作提示 */, '<spring:message code="purchase.delivery.ConfirmAuditApproval"/>？'/* 确定审核同意吗 */, function (data) {  
        if (data) {
        	$.messager.progress();
        	$.ajax({
        		url:'${ctx}/manager/order/deliveryAudit/doAgree',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			if(data.success){
        				$.messager.progress('close');
        				$.messager.show({
        					title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
        					msg:'<spring:message code="purchase.delivery.SuccessfulAudit"/>'/* 审核成功 */,
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-delivery-list').datagrid('reload');
        			}else{
        				$.messager.progress('close');
        				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data.message,'error');
        			}
        		}
        	});   
        }
	});
	}
}


//驳回-弹出页面
function doReject(){
	var selections = $('#datagrid-delivery-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！ */,'info');
		return false;
	}
	var reject_ids="";
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].auditStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.IncludeInvoiceIssuedByAuditAndCannotBeRejected"/>'/* 包含已审核通过的发货单，不能驳回 */,'error');  
			return false;
		}if(selections[i].auditStatus == -1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheRejectedInvoiceMustNotBeRejected"/>'/* 包含已驳回的发货单，不能驳回 */,'error');  
			return false;
		}
		reject_ids=reject_ids+selections[i].id;
	}
	
	//1、驳回  则打开驳回窗口
		document.getElementById("reject_ids").value=reject_ids;
		$('#win-reject').window('open');   
}

function rejectOper(){
		$.messager.progress();
		$('#form-reject').form('submit',{
			ajax:true,
			iframe: true,    
			url:'${ctx}/manager/order/deliveryAudit/doReject', 
			success:function(data){
				$.messager.progress('close');
				var obj = JSON.parse(data);
				try{
					if(obj.success){ 
						$.messager.show({
							title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
							msg:  obj.message, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#win-reject').window('close');
						$('#datagrid-delivery-list').datagrid('reload'); 
					}else{
						$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',obj.message,'error');
					}
				}catch (e) {
					$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',e,'error'); 
				} 
			}
		});
}




//打印信息
function gotoPrint(){
	var pid=aid;
	window.open('${ctx}/manager/order/printdelivery/pending/'+pid);
}

//打印信息
function printFmt(v,r,i){
	if(r.material.col3 == "X"){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="printPC('+ r.id +');"><spring:message code="purchase.delivery.PrintBatch"/></a>'/* 打印批次 */+
		'&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="printDT('+ r.id +');"><spring:message code="purchase.delivery.PrintingMonomer"/></a>'/* 打印单体 */;
	}else{
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="printPC('+ r.id +');"><spring:message code="purchase.delivery.PrintBatch"/></a>'/* 打印批次 */;
	}
}

function printPC(id){
	window.open('${ctx}/manager/order/printdelivery/pici/'+id);
}

function printDT(id){
	window.open('${ctx}/manager/order/printdelivery/danti/'+id);
}

function downFile(){
	var fileName = $("#deliveryFileName").val(); //附件名称
	var fileUrl = $("#deliveryFilePath").val(); //附件地址
	if(fileName==''||fileUrl==''){
		return ;
	}
	var url = ctx+'/manager/order/delivery/downloadFile';
	var inputs = '<input type="hidden" name="fileUrl" value="'+fileUrl+'">'+'<input type="hidden" name="fileName" value="'+fileName+'">';
	
	jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
    .appendTo('body').submit().remove();
}

</script>
</body>
</html>

