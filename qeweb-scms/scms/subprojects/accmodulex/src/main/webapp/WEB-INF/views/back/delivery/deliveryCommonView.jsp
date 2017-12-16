<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<div style="padding:5px;">
<div>
   <c:if test="${vendor}"> 
	   <c:if test="${po.auditStatus eq 1 && po.deliveryStatus eq 0}">
	    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="singleDelivery(${po.id})"><spring:message code="purchase.delivery.DeliverGoods"/></a>      
	   </c:if> 
	   <c:if test="${po.receiveStatus eq 0}">
	    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="singleCancelDelivery(${po.id})"><spring:message code="purchase.delivery.CancelShipment"/></a>    
	   </c:if> 
   </c:if>
    <c:if test="${po.deliveryStatus eq 1 }">
		  <a href="javascript:;" id="printButton" class="easyui-linkbutton" data-options="iconCls:'icon-print',plain:true" onclick="gotoPrint(${po.id})"><spring:message code="purchase.delivery.PrintDeliveryList"/></a>
	</c:if>
    <c:if test="${!vendor}"> 
         <c:if test="${po.auditStatus eq 0 && po.deliveryStatus eq 0}">
	    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="doSingleAgree(${po.id})"><spring:message code="purchase.delivery.Agree"/></a>  
	    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="doSingleReject(${po.id})"><spring:message code="purchase.delivery.Reject"/></a>     
	   </c:if> 
    </c:if>
</div>
			<div style="padding:5px;">
				<div>
					<form id="form-deliveryitem-search" method="post" enctype="multipart/form-data" >
					<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
						<tr>
						    <td width="25%"><spring:message code="purchase.delivery.ASNNumber"/>：${po.deliveryCode}<input id="id" type="hidden" value="${po.id}"/></td>
						    <td width="25%"><spring:message code="purchase.delivery.vendorCode"/>：${po.vendor.code}</td>
						    <td width="25%"><spring:message code="purchase.delivery.VendorName"/>：${po.vendor.name}</td>
						    <td width="25%"><spring:message code="purchase.delivery.TypeOfShipping"/>：${po.transportName}</td>
						</tr>
						<tr>
						    <td width="25%"><spring:message code="purchase.delivery.DeliveryTime"/>： <fmt:formatDate value="${po.deliveyTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
						    <td width="25%"><spring:message code="purchase.delivery.ReceivingCompany"/>：${po.companyName}</td>
						    <td width="25%"><spring:message code="purchase.delivery.ReceivingContact"/>：${po.deliveryContacter}</td>
						    <td width="25%"><spring:message code="purchase.delivery.ReceivingTelephone"/>：${po.deliveryTel}</td>   
						</tr>
						<tr>
						    <td width="25%"><spring:message code="purchase.delivery.ReceivingAddress"/>：${po.deliveryAddress}</td>
							<td width="25%"><spring:message code="purchase.delivery.LogisticsCompany"/>：${po.logisticsCompany}</td>
							<td width="25%"><spring:message code="purchase.delivery.LogisticsContact"/>：${po.logisticsContacter}</td>
							<td width="25%"><spring:message code="purchase.delivery.LogisticsTelephone"/>：${po.logisticsTel}</td>
						</tr>
						<tr>
						 	<td width="25%"><spring:message code="purchase.delivery.TheTotalNumberOfLargePackages"/>：<fmt:formatNumber value="${po.anzpk}" pattern="#,##0"/></td>
						 	<td width="25%"><spring:message code="purchase.delivery.EstimatedDeliveryTime"/>： <fmt:formatDate value="${po.planDeliveryDate}" pattern="yyyy-MM-dd"/></td>
						 	<td width="25%"><spring:message code="purchase.delivery.LogisticsDays"/>：<fmt:formatNumber value="${po.ysts}" pattern="#,##0"/></td>
						    <td width="25%"><spring:message code="purchase.delivery.EstimatedTimeOfArrival"/>： <fmt:formatDate value="${po.expectedArrivalTime }" pattern="yyyy-MM-dd"/></td>
						</tr>
						<tr>
							<td width="25%"><spring:message code="purchase.delivery.Delivery"/>：${po.shipTypeName}</td>
						    <td width="25%"><spring:message code="purchase.delivery.Enclosure"/>：<a href="javascript:;" onclick="File.download('${po.deliveryFilePath}','${po.deliveryFileName}')">${po.deliveryFileName}</a></td> 
						    <td width="25%"></td>
						    <td width="25%"></td>
						</tr>
						<tr>
						   <td colspan="4">
						   <spring:message code="purchase.delivery.Remarks"/>：<textarea readonly="readonly" style="width: 100%;height:50px">${po.remark}</textarea>
						   </td>
						</tr>
					</table>
					</form>  
				</div>
			</div>
			<c:if test="${vendor}"> 
				<c:if test="${po.deliveryStatus eq 0}">
					<div id="deliveryItemToolbar" style="padding:5px;">
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveChange()"><!-- 保存修改 --><spring:message code="purchase.delivery.SaveModification"/></a>      
					</div>
				</c:if>
			</c:if>
			<table id="datagrid-deliveryitem-list" title='<spring:message code="purchase.delivery.DetailsOfBillOfDelivery"/>' class="easyui-datagrid"
				data-options="url:'${ctx}/manager/order/delivery/deliveryitem/${po.id}',method:'post',singleSelect:false,
				  <c:if test="${vendor}"> 
					 <c:if test="${po.deliveryStatus eq 0}">
					 toolbar:'#deliveryItemToolbar',
					  onClickRow: onClickRow2,onLoadSuccess:initData,
					 </c:if>
				 </c:if>
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
					<th data-options="field:'id',hidden:true"></th>
					<th data-options="field:'manage',formatter:printFmt"><spring:message code="purchase.delivery.operation"/></th>
					<th data-options="field:'dn'">DN</th>
					 <c:if test="${vendor}"> 
					    <c:if test="${po.receiveStatus eq 0}">
					     <th data-options="field:'dnErrorMessage'"><spring:message code="purchase.delivery.DNWriteInformation"/></th>
					     </c:if>
					 </c:if>
					<th data-options="field:'orderCode'"><spring:message code="purchase.delivery.PurchaseOrderNumber"/></th>
					<th data-options="field:'itemNo'"><spring:message code="purchase.delivery.LineNumber"/></th>
				    <th data-options="field:'materialCode', formatter:function(v,r,i){return r.material.code}"><spring:message code="purchase.delivery.MaterialCoding"/></th>
					<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.delivery.MaterialName"/></th>
					<th data-options="field:'orderQty',formatter:getNumFmt"><spring:message code="purchase.delivery.QuantityOfGoodsToBePurchased"/></th>   
					<th data-options="field:'deliveryQty',editor:{type:'numberbox',options:{required:true,min:0,precision:'3'}},formatter:getNumFmt"><spring:message code="purchase.delivery.QuantityShipped"/></th>   
					<th data-options="field:'standardBoxNum',editor:{type:'numberbox',options:{editable:true,required:true,min:0,precision:'3'}},formatter:getNumFmt"><spring:message code="purchase.delivery.QuantityOfLargePackage"/></th>
					<th data-options="field:'boxNum',formatter:getNumIntegerFmt,editor:{type:'numberbox',options:{editable:false}}"><spring:message code="purchase.delivery.TheTotalNumberOfLargePackages"/></th>
					<th data-options="field:'minPackageQty',formatter:getNumFmt,editor:{type:'numberbox',options:{editable:true,required:true,min:0,precision:'3'}}"><spring:message code="purchase.delivery.QuantityOfSmallPackages"/></th>  
					<th data-options="field:'minBoxNum',formatter:getNumIntegerFmt,editor:{type:'numberbox',options:{editable:true}}"><spring:message code="purchase.delivery.TheTotalNumberOfSmallPackages"/></th>
				
					<th data-options="field:'requestTime'"><spring:message code="purchase.delivery.RequiredArrivalTime"/></th>   
					<th data-options="field:'manufactureDate'"><spring:message code="purchase.delivery.DateOfManufacture"/></th> 
					<th data-options="field:'version'"><spring:message code="purchase.delivery.Edition"/></th>   
					<th data-options="field:'charg'"><spring:message code="purchase.delivery.BatchNumber"/></th> 
					<th data-options="field:'vendorCharg'"><spring:message code="purchase.delivery.TraceabilityBatchNumber"/></th> 
					
					<th data-options="field:'meins'"><spring:message code="purchase.delivery.Company"/></th>
					<th data-options="field:'remark'"><spring:message code="purchase.delivery.Remarks"/></th>    
					<th data-options="field:'inspectionReport',width:250,formatter:inspectionReportViewFmt"><spring:message code="purchase.delivery.InspectionReport"/></th>   
					
					<th data-options="field:'zlock',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Locked"/>';else return '<spring:message code="purchase.delivery.NotLocked"/>';}"><spring:message code="purchase.delivery.LockState"/></th>
					<th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.delivery.AlreadyFrozen"/>';else return '<spring:message code="purchase.delivery.NotFrozen"/>';}"><spring:message code="purchase.delivery.FrozenState"/></th>
					<th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Deleted"/>';else return '<spring:message code="purchase.delivery.NotDeleted"/>';}"><spring:message code="purchase.delivery.DeleteMark"/></th>
					<th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Delivered"/>';else return '<spring:message code="purchase.delivery.Undelivered"/>';}"><spring:message code="purchase.delivery.DeliveryState"/></th>
					<th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.NO"/>';else return '<spring:message code="purchase.delivery.YES"/>';}"><spring:message code="purchase.delivery.InwardDeliveryLogo"/></th>
				</tr></thead>
			</table>
</div>	

<!-- 驳回窗口 -->
	<div id="win-reject-single" class="easyui-dialog" title='<spring:message code="purchase.delivery.Reject"/>' style="width:400px;height:150px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-single-adder-bb2'">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-reject-single" method="post" >
				<input id="single_reject_ids" name="reject_ids" type="hidden"/>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="label.reson"/><!-- 原因 --><textarea rows="4" cols="50" id="reject_reason_single" name="reject_reason"></textarea></td> 
				</tr>
				</table>
				<div id="dialog-single-adder-bb2" style="text-align: center;">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="singleRjectOper()"><spring:message code="purchase.delivery.Submit"/></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-reject-single').form('reset')"> <spring:message code="purchase.delivery.Reset"/></a>
				</div>
			</form>
		</div>
	</div>
<script>
$(function() { 
	//若已锁定|已冻结|已删除|已交付|订单数量改小则当前行高亮显示
		$('#datagrid-deliveryitem-list').datagrid({
			rowStyler:function(index,row){
				if (row.zlock == 'X' || row.lockStatus == '1' || row.loekz == 'X' || row.elikz== 'X'  || row.isQtyModify == '1' || row.bstae == 'X'){
					return 'background-color:red;color:black;';
				}
			}
		});
	
});

//打印信息
function printFmt(v,r,i){
	var str='';
 	if(r.delivery.deliveryStatus == 1){ 
		str=str+ '&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="printPC('+ r.id +');"><spring:message code="purchase.delivery.PrintingLargePackage"/></a>'/* 打印大包装 */;
		str=str+'&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="printDT('+ r.id +');"><spring:message code="purchase.delivery.PrintingSmallPackage"/></a>'/* 打印小包装 */;
 	}else {
 		var isVendor=${vendor};
 		if(isVendor){
		 	//未发货时：为已锁定|已冻结|已删除|已交付|订单数量改小则可以删除
			str=str+ '&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="deleteDlvItem('+ r.id +');"><spring:message code="vendor.deleting"/></a>'/* 删除 */;
		
			if(null!=r.dnCreateStatus && r.dnCreateStatus==-1){
 				//是供应商，并且dn创建失败，则重新调用DN
 				str=str+ '&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="singleSyncCreateDn('+ r.id +','+r.delivery.id+');"><spring:message code="purchase.delivery.OverwriteFailedData"/></a>'/* 重写失败数据 */;
 			}
 		}
 			
 	}
	return str;
}

//删除行项目
function deleteDlvItem(id){
	//判断当前只有一个明细时，建议用户取消发货
	var rows = $('#datagrid-deliveryitem-list').datagrid('getRows');
	if(rows.length<=1){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.ThereIsOnlyOneDetailOfTheCurrentInvoicePleaseClickCancelShipment"/>'/* 当前发货单只有一条明细，请点击【取消发货】！ */,'info');
		return false;
	}
	
	$.messager.confirm('<spring:message code="purchase.delivery.OperationHints"/><!-- 操作提示 -->', '<spring:message code="purchase.delivery.DoYouWantToDeleteTheOperation"/>'/* 确定删除操作吗？ */, function (data) {  
        if (data) {
      	$.messager.progress();
        	$('#form-deliveryitem-search').form('submit',{
        		ajax:true,
        		type:'POST',
        		iframe: true,    
        		url:'${ctx}/manager/order/delivery/singleDeleteDelivery/'+id, 
        		success:function(data){
        			$.messager.progress('close');
        			try{
        			var result = eval('('+data+')');
        			if(result.success){
        				$.messager.show({
        					title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
        					msg:'<spring:message code="purchase.order.DeleteSuccessfully"/>'/* 删除成功 */,
        					timeout:2000,
        					showType:'show',
        					style:{
        						right:'',
        						top:document.body.scrollTop+document.documentElement.scrollTop,
        						bottom:''
        					}
        				});
        				$('#datagrid-deliveryitem-list').datagrid('reload'); 
        			}else{
        				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.message,'error');
        			}
        			}catch (e) {  
        				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data,'error');
        			}
        		}
        	});
        	
        }
	});
}

//写入：行项目重新调用创建DN
function singleSyncCreateDn(id,dlvId){
	$.messager.progress();
	$('#form-deliveryitem-search').form('submit',{
		ajax:true,
		type:'POST',
		iframe: true,    
		url:'${ctx}/manager/order/delivery/singleSyncCreateDn/'+id, 
		success:function(data){
			$.messager.progress('close');
			try{
			var result = eval('('+data+')');
			if(result.success){
				if("singleSyncCreateDn" ==result.method){
					$.messager.show({
						title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
						msg:'<spring:message code="purchase.delivery.SuccessfulOperation"/>'/* 操作成功 */,
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#datagrid-deliveryitem-list').datagrid('reload'); 
				}else if("doSingledelivery"==result.method){
					$.messager.show({
						title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
						msg:'<spring:message code="purchase.delivery.SuccessfulDelivery"/>'/* 发货成功 */,
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$dialog.dialog('close');
					$('#datagrid-delivery-list').datagrid('reload');
      				var clientHeight = document.body.clientHeight;	
      				new dialog2().showWin('<spring:message code="purchase.delivery.DetailsOfBillOfDelivery"/>'/* 发货单详情 */, 1000, clientHeight, ctx + '/manager/order/delivery/getDeliveryView/'+dlvId);
				}
			}else{
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.message,'error');
			}
			}catch (e) {  
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data,'error');
			}
		}
	});
}

function printPC(id){
	window.open('${ctx}/manager/order/printdelivery/maxPackage/'+id);
}

function printDT(id){
	window.open('${ctx}/manager/order/printdelivery/minPackage/'+id);
}

  function inspectionReportViewFmt(v,r,i){
	  if(null!=r.inspectionPath && null!=r.inspectionName){
			return '<a tyle="margin-right:10px" href="javascript:;" onclick="File.download(\''+r.inspectionPath+'\',\'\')">'+r.inspectionName+'</a>';
		}
  }
  
//单个发货
  function singleDelivery(id){
	var rows = $('#datagrid-deliveryitem-list').datagrid('getRows');
	for(var i = 0; i < rows.length; i ++) {
		if(null!=rows[i].zlock && rows[i].zlock=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.DataCannotBeShippedDueToLockedData"/>'/* 存在已锁定的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=rows[i].lockStatus && rows[i].lockStatus=='1'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.ThereIsFrozenDataThatCannotbeShipped"/>'/* 存在已冻结的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=rows[i].loekz && rows[i].loekz=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.ExistingDeletedDataCannotBeShipped"/>'/* 存在已删除的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=rows[i].elikz && rows[i].elikz=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.DeliveryDataCannotBeShipped"/>'/* 存在已交付的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=rows[i].bstae && rows[i].bstae=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheOrderIsNotInwardDeliveryAndNotAllowedToDeliver"/>'/* 该订单不是内向交货,不允许发货！ */,'info');
			return false;
		}
	}
  	$.messager.confirm('<spring:message code="purchase.delivery.OperationHints"/><!-- 操作提示 -->', '<spring:message code="purchase.delivery.DetermineTheDeliveryOperation"/>'/* 确定发货操作吗？ */, function (data) {  
          if (data) {
        	$.messager.progress();  
          	$('#form-deliveryitem-search').form('submit',{
          		ajax:true,
          		type:'POST',
          		iframe: true,    
          		url:'${ctx}/manager/order/delivery/doSingledelivery/'+id, 
          		success:function(data){
          			$.messager.progress('close');
          			try{
          			var result =eval('('+data+')');
          			if(result.success){
          				$.messager.show({
          					title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
          					msg:'<spring:message code="purchase.delivery.SuccessfulDelivery"/>'/* 发货成功 */,
          					timeout:2000,
          					showType:'show',
          					style:{
          						right:'',
          						top:document.body.scrollTop+document.documentElement.scrollTop,
          						bottom:''
          					}
          				});
          				
          				$dialog.dialog('close');
          				$('#datagrid-delivery-list').datagrid('reload');
          				var clientHeight = document.body.clientHeight;	
          				new dialog2().showWin('<spring:message code="purchase.delivery.DetailsOfBillOfDelivery"/>'/* 发货单详情 */, 1000, clientHeight, ctx + '/manager/order/delivery/getDeliveryView/'+id);
          			}else{
          				$('#datagrid-deliveryitem-list').datagrid('reload'); 
          				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.message,'error');
          			}
          			}catch (e) {  
          				$('#datagrid-deliveryitem-list').datagrid('reload'); 
          				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data,'error');
          			}
          		}
          	});
          	
          }
  	});
  }
  
  
//单个取消发货
  function singleCancelDelivery(id){
  	$.messager.confirm('<spring:message code="purchase.delivery.OperationHints"/><!-- 操作提示 -->', '<spring:message code="purchase.delivery.DetermineTheCancellationOfTheShipmentOperation"/>'/* 确定取消发货操作吗？ */, function (data) {  
          if (data) {
        	$.messager.progress();
          	$('#form-deliveryitem-search').form('submit',{
          		ajax:true,
          		type:'POST',
          		iframe: true,    
          		url:'${ctx}/manager/order/delivery/singleCancelDelivery/'+id, 
          		success:function(data){
          			$.messager.progress('close');
          			try{
          			var result = eval('('+data+')');
          			if(result.success){
          				$.messager.show({
          					title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
          					msg:'<spring:message code="purchase.delivery.CancelShipmentSuccessfully"/>'/* 取消发货成功 */,
          					timeout:2000,
          					showType:'show',
          					style:{
          						right:'',
          						top:document.body.scrollTop+document.documentElement.scrollTop,
          						bottom:''
          					}
          				});
          				$dialog.dialog('close');
          				$('#datagrid-delivery-list').datagrid('reload'); 
          			}else{
          				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.message,'error');
          			}
          			}catch (e) {  
          				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data,'error');
          			}
          		}
          	});
          	
          }
  	});
  }
  
  

	var editIndex = undefined;
	function initData(){
		endEditing();
	}
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		if ($('#datagrid-deliveryitem-list').datagrid('validateRow', editIndex)) {
			$('#datagrid-deliveryitem-list').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow2(index, data) {
			if (editIndex != index) {
				if (endEditing()) {
					$('#datagrid-deliveryitem-list').datagrid('beginEdit', index);
					editIndex = index;
				} else {
					$('#datagrid-deliveryitem-list').datagrid('selectRow', editIndex);
				}
			}
			
			var sendQty = $('#datagrid-deliveryitem-list').datagrid('getEditor', {
				index : index,
				field : 'deliveryQty'
			});
			var standardBoxNum = $('#datagrid-deliveryitem-list').datagrid('getEditor', {
				index : index,
				field : 'standardBoxNum'
			});
			var boxNum = $('#datagrid-deliveryitem-list').datagrid('getEditor', {
				index : index,
				field : 'boxNum'
			});
		
			
		
			
			var minPackageQty = $('#datagrid-deliveryitem-list').datagrid('getEditor', {
				index : index,
				field : 'minPackageQty'
			});
			var minBoxNum = $('#datagrid-deliveryitem-list').datagrid('getEditor', {
				index : index,
				field : 'minBoxNum'
			});
			
			$(sendQty.target).numberbox({
				onChange : function() {
					caculate();
				}
			});
			$(standardBoxNum.target).numberbox({
				onChange : function() {
					caculate();
				}
			});
			$(minPackageQty.target).numberbox({
				onChange : function() {
					caculate();
				}
			});
			
			function caculate(){
				$(boxNum.target).numberbox('setValue', Math.ceil(sendQty.target.val()/standardBoxNum.target.val()));
				$(minBoxNum.target).numberbox('setValue', Math.ceil(sendQty.target.val()/minPackageQty.target.val()));
			}
	}
	
	function boxNumFmt(v,r,i){
		if(null!=r.standardBoxNum && null!=r.deliveryQty){
			return Math.ceil(r.deliveryQty/r.standardBoxNum);//向上取整，有小数则加1
		}
		return "";
	}

	function minBoxNumFmt(v,r,i){
		if(null!=r.minPackageQty && null!=r.deliveryQty){
			return Math.ceil(r.deliveryQty/r.minPackageQty);//向上取整，有小数则加1
		}
		return "";
	}
	
	function saveChange(){
		$.messager.progress();
		$('#datagrid-deliveryitem-list').datagrid('acceptChanges');    
	 	var rows = $('#datagrid-deliveryitem-list').datagrid('getRows');
		for(i = 0;i < rows.length;i++) {
			 if(rows[i].deliveryQty == null || rows[i].deliveryQty == '') {
				$.messager.progress('close');
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheQuantityOfShipmentCannotBeEmpty"/>'/* 发货数量不能为空 */,'error');
				return false;
			} 
			if(rows[i].minPackageQty == null || rows[i].minPackageQty == '') {
				$.messager.progress('close');
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheNumberOfSmallPackagesCannotBeEmpty"/>'/* 小包装数量不能为空 */,'error');
				return false;
			}
			if(rows[i].minBoxNum == null || rows[i].minBoxNum == '') {
				$.messager.progress('close');
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheTotalOfSmallPackagesCannotBeEmpty"/>'/* 小包装总数不能为空 */,'error');
				return false;
			}
			if(rows[i].standardBoxNum == null || rows[i].standardBoxNum == '') {
				$.messager.progress('close');
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheNumberOfLargePackagesCannotBeEmpty"/>'/* 大包装数量不能为空 */,'error');
				return false;
			}
			if(rows[i].boxNum == null || rows[i].boxNum == '') {
				$.messager.progress('close');
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheTotalOfLargesPackagesCannotBeEmpty"/>'/* 大包装总数不能为空 */,'error');
				return false;
			}
	    }

		var length=rows.length;
		var str="{total:"+length+",rows:"+$.toJSON(rows)+"}";
		var datas=str;

		$('#form-deliveryitem-search').form('submit',{
			ajax:true,
			iframe: true,    
			url: '${ctx}/manager/order/delivery/saveChange',
			onSubmit:function(param){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				
				param.datas = datas; 			//发货明细数据
				return isValid;
			},
			 success: function (data) {
				    editIndex = undefined;
					$.messager.progress('close');
					try{
						var result = eval('('+data+')');
						if(result.success){ 
							$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->', result.message ,'info');
						}else{
							$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',e,'error'); 
					} 
		       	}
		 }); 
	}
	
	
	//单个同意-deliveryCommonView
	function doSingleAgree(id){
		//审核添加锁定状态、冻结状态、删除状态、交付状态、订单减小的判断
		var temp = true;	//返回结果
		var msg = '';		//返回内容
		$.ajax({
			url:'${ctx}/manager/order/deliveryAudit/singleJudgeAudit?id='+id,
			type:'POST',
			contentType : 'application/json',
			dataType : 'json',
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
		$.messager.confirm('<spring:message code="purchase.delivery.OperationHints"/><!-- 操作提示 -->', '<spring:message code="purchase.delivery.ConfirmAuditApproval"/>'/* 确定审核同意吗？ */, function (data) {  
	        if (data) {
	        	$.messager.progress();
	        	$.ajax({
	        		url:'${ctx}/manager/order/deliveryAudit/doSingleAgree?id='+id,
	        		type:'POST',
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
	        				$dialog.dialog('close');
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


	//单个驳回-deliveryCommonView
	function doSingleReject(id){
		//1、驳回  则打开驳回窗口
		document.getElementById("single_reject_ids").value=id;
		$('#win-reject-single').window('open');   
	}

	function singleRjectOper(){
		$.messager.progress();
		$('#form-reject-single').form('submit',{
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
						$('#win-reject-single').window('close');
						$dialog.dialog('close');
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

</script>
