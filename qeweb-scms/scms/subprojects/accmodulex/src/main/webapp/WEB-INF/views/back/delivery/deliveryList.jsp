<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="purchase.delivery.DeliveryManagement"/></title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/Deliverycommon.js"></script>
	 <script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
	<script type="text/javascript">
		function manageFmt(v,r,i){
			  if(r.deliveryStatus==0){//未发货可以进行编辑
				  return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="toEditDelivery('+ r.id +','+r.deliveryStatus+','+r.auditStatus+');"><spring:message code="purchase.delivery.edit"/></a>'/* 编辑 */;
			  }
		  }
		</script>
</head>
		

<body style="margin:0;padding:0;">
	<table id="datagrid-delivery-list" title='<spring:message code="purchase.delivery.InvoiceList"/>' class="easyui-datagrid" fit="true"
		data-options="url:'${ctx}/manager/order/delivery',method:'post',singleSelect:false,
		toolbar:'#deliveryListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"    
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'manage',formatter:manageFmt"></th>
		<th data-options="field:'deliveryCode',formatter:deliveryCodeFmt"><spring:message code="purchase.delivery.ASNInvoiceNo"/></th>
		<th data-options="field:'shipType',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.delivery.Normal"/>';else if(v=='-1') return '<spring:message code="purchase.delivery.Replenishment"/>';}"><spring:message code="purchase.delivery.TypeOfShipment"/></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="purchase.delivery.vendorCode"/></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}"><spring:message code="purchase.delivery.VendorName"/></th>
		<!-- <th data-options="field:'buyerCode',formatter:function(v,r,i){return r.buyer.code;}">采购组织编码</th>
		<th data-options="field:'buyerName',formatter:function(v,r,i){return r.buyer.name;}">采购组织名称</th> -->
		<th data-options="field:'purchasingGroupCode'"><spring:message code="purchase.delivery.PurchasingOrganizationCode"/></th>
		<th data-options="field:'purchasingGroupName'"><spring:message code="purchase.delivery.PurchasingOrganizationName"/></th>
		<th data-options="field:'deliveryStatus',formatter:deliveryStatusFmt"><spring:message code="purchase.delivery.ShippingState"/></th>
		<th data-options="field:'deliveyTime'"><spring:message code="purchase.delivery.DeliveryTime"/></th>
		<th data-options="field:'deliveyUserName',formatter:function(v,r,i){if(r.deliveryUser) return r.deliveryUser.name; return '';}"><spring:message code="purchase.delivery.Consignor"/></th>
		<th data-options="field:'createTime'"><spring:message code="purchase.delivery.InvoiceCreationTime"/></th>
		<th data-options="field:'receiveStatus',formatter:receiveStatusFmt"><spring:message code="purchase.delivery.ReceivingState"/></th>
		<th data-options="field:'auditStatus',formatter:auditStatusFmt"><spring:message code="purchase.delivery.AuditStatus"/></th>
		<th data-options="field:'rejectReason'"><spring:message code="purchase.delivery.ReasonsForRejection"/></th>
		</tr></thead>
	</table>
	<div id="deliveryListToolbar" style="padding:5px;">
		<!--  
		<div>
		<shiro:hasPermission name="dlv:delivery"> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="delivery()">发货</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="dlv:canceldelivery"> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="canceldelivery()">取消发货单</a>
		</shiro:hasPermission>
		</div>
		-->
		<div>
			<form id="form-delivery-search" method="post">
			<!-- ASN发货单号 --><spring:message code="purchase.delivery.ASNInvoiceNo"/>：<input type="text" name="search-LIKE_deliveryCode" class="easyui-textbox" style="width:80px;"/>
			<!-- 采购组编码 --><spring:message code="purchase.delivery.PurchasingOrganizationCode"/>：<input type="text" name="search-LIKE_purchasingGroup.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 发货状态 --><spring:message code="purchase.delivery.ShippingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_deliveryStatus"><option value="" >-<!-- 全部 --><spring:message code="purchase.delivery.Whole"/>-</option><option value="0" selected="selected"><!-- 未发货 --><spring:message code="purchase.delivery.NotShipped"/></option><option value="1"><!-- 已发货 --><spring:message code="purchase.delivery.AlreadyShipped"/></option></select>
			<!-- 收货状态 --><spring:message code="purchase.delivery.ReceivingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_receiveStatus"><option value="" >-<!-- 全部 --><spring:message code="purchase.delivery.Whole"/>-</option><option value="0"><!-- 未收货 --><spring:message code="purchase.delivery.GoodsNotReceived"/></option><option value="1"><!-- 已收货 --><spring:message code="purchase.delivery.AlreadyReceived"/></option></select>
			<!-- 审核状态 --><spring:message code="purchase.delivery.AuditStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_auditStatus"><option value="" >-<!-- 全部 --><spring:message code="purchase.delivery.Whole"/>-</option><option value="0"><!-- 未审核 --><spring:message code="purchase.delivery.NotAudited"/></option><option value="1"><!-- 审核通过 --><spring:message code="purchase.delivery.PassAudited"/></option><option value="-1"><!-- 审核驳回 --><spring:message code="purchase.delivery.AuditRejection"/></option></select>
			<!-- 创建日期 --><spring:message code="purchase.delivery.CreationDate"/>：<input class="easyui-datebox" name="search-GTE_createTime" data-options="editable:false"  value="" style="width:150px">&nbsp;&nbsp;<!-- 到 --><spring:message code="purchase.delivery.Go"/>&nbsp;&nbsp;
				  <input class="easyui-datebox" name="search-LTE_createTime" data-options="editable:false"  value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;<br>
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchDelivery()"><spring:message code="purchase.delivery.Query"/></a><!-- 查询 -->
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-delivery-search').form('reset')"><spring:message code="purchase.delivery.Reset"/></a><!-- 重置 -->
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
	
<%@include file="deliveryCommonEdit.jsp" %>		
<script type="text/javascript">
$(function() { 
	//若已锁定|已冻结|已删除|已交付|订单数量减小则当前行高亮显示
		$('#datagrid-delivery-list').datagrid({
			rowStyler:function(index,row){
				if (row.exceptionStatus == 1){
					return 'background-color:red;color:black;';
				}
			}
		});
	
});


function searchDelivery(){
	var searchParamArray = $('#form-delivery-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-delivery-list').datagrid('load',searchParams);
}


//查看发货单详情
function showDelivery(id,deliveryStatus){
	var clientHeight = document.body.clientHeight;	
	new dialog2().showWin('<spring:message code="purchase.delivery.DetailsOfBillOfDelivery"/>'/* 发货单详情 */, 1000, clientHeight, ctx + '/manager/order/delivery/getDeliveryView/'+id);
}

//弹出编辑页面
function toEditDelivery(id,deliveryStatus,auditStatus){
	$('#form-deliveryitem-search').form('clear');
	//获取运输方式
	  $('#transport_type').combobox({ 
		  url:ctx+'/manager/basedata/dict/getDictItemSelect/TRANSPORT_TYPE',
	      editable:false,
	      cache: true,
	      valueField:'code',   
	      textField:'name'
   }); 
	
	$('#win-delivery-addoredit').window('open');
	$('#form-delivery-addoredit').form('load','${ctx}/manager/order/delivery/getDelivery/'+id+"?Time=new Date()&status=1");
	//详情
	$('#datagrid-delivery-item-addoredit-list').datagrid({   
    	url:'${ctx}/manager/order/delivery/getItemListByDlvId/' + id     
	});
	$('#datagrid-delivery-item-addoredit-list').datagrid('load',{});
	
	var expectedArrivalTime=document.getElementById("expectedArrivalTime").value;
	$('#expectedArrivalTimeId').datebox('setValue',expectedArrivalTime);
}

//检验报告
function inspectionReportFmt(v,r,i){
	 var fileNameRow=document.getElementById("fileName_"+r.id);
	 var filePathRow=document.getElementById("filePath_"+r.id);
	if(null==filePathRow||null==filePathRow.value){
		if(null!=r.inspectionPath && null!=r.inspectionName){
			return '<span id="'+r.id+'"><a tyle="margin-right:10px" href="javascript:;" onclick="File.download(\''+r.inspectionPath+'\',\'\')">'+r.inspectionName+'</a></span><a id="btn_imp" href="#" class="easyui-linkbutton"  onclick="toUploadInspectionReport(\''+r.id+'\')"><spring:message code="purchase.order.Upload"/></a><input type="hidden"  id="fileName_'+r.id+'"  name="fileName_'+r.id+'" value="'+r.inspectionName+'"/><input type="hidden"  id="filePath_'+r.id+'"  name="filePath_'+r.id+'" value="'+r.inspectionPath+'"/>';
		}
		return '<span id="'+r.id+'"></span><input type="hidden"  id="fileName_'+r.id+'"  name="fileName_'+r.id+'"/><input type="hidden"  id="filePath_'+r.id+'"  name="filePath_'+r.id+'"/><a id="btn_imp" href="#" class="easyui-linkbutton"  onclick="toUploadInspectionReport(\''+r.id+'\')"><spring:message code="purchase.order.Upload"/></a>';
	}else{
		return '<span id="'+r.id+'"><a tyle="margin-right:10px" href="javascript:;" onclick="File.download(\''+filePathRow.value+'\',\'\')">'+fileNameRow.value+'</a></span><a id="btn_imp" href="#" class="easyui-linkbutton"  onclick="toUploadInspectionReport(\''+r.id+'\')"><spring:message code="purchase.order.Upload"/></a><input type="hidden"  id="fileName_'+r.id+'"  name="fileName_'+r.id+'" value="'+fileNameRow.value+'"/><input type="hidden"  id="filePath_'+r.id+'"  name="filePath_'+r.id+'" value="'+filePathRow.value+'"/>';
	}
}


//编辑后保存发货单
function saveDelivery(type){
	$.messager.progress();
	$('#datagrid-delivery-item-addoredit-list').datagrid('acceptChanges');    
 	var rows = $('#datagrid-delivery-item-addoredit-list').datagrid('getRows');
	for(i = 0;i < rows.length;i++) {
		var manuDateVal = '';	//生产日期
		var chargVal ='';	    //批号
		var vendorChargVal =''; //追溯批号
		var standardBoxNumVal = '';	//大包装数量
		var minPackageQtyVal = '';	//小包装数量
		
		 if(rows[i].sendQty == null || rows[i].sendQty == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheQuantityOfShipmentCannotBeEmpty"/>'/* 发货数量不能为空 */,'error');
			return false;
		} 
		 if(rows[i].sendQty > rows[i].shouldQty){
			 $.messager.progress('close');
			 $.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheActualQuantityShippedIsGreaterThanTheAmountToBeDelivered"/>'/* 实际发货数量大于应发数量 */,'error');
			 return false;
		 }
		 
		 var standardBoxNum=$('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {index:i,field:"standardBoxNum"});
		 if(standardBoxNum!=null){
			standardBoxNumVal =$(standardBoxNum.target).numberbox('getValue');
		}else{
			standardBoxNumVal =rows[i].standardBoxNum ;
		}
		if(standardBoxNumVal == null || standardBoxNumVal == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheNumberOfLargePackagesCannotBeEmpty"/>'/* 大包装数量不能为空 */,'error');
			return false;
		}
			
		var minPackageQty=$('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {index:i,field:"minPackageQty"});
		if(minPackageQty!=null){
			minPackageQtyVal =$(minPackageQty.target).numberbox('getValue');
		}else{
			minPackageQtyVal = rows[i].minPackageQty ;
		}
		
		if(minPackageQtyVal == null || minPackageQtyVal == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheNumberOfSmallPackagesCannotBeEmpty"/>'/* 小包装数量不能为空 */,'error');
			return false;
		}
		rows[i].boxNum = Math.ceil(rows[i].sendQty/rows[i].standardBoxNum);
		rows[i].minBoxNum= Math.ceil(rows[i].sendQty/rows[i].minPackageQty);
		if(rows[i].minBoxNum == null || rows[i].minBoxNum == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheTotalOfSmallPackagesCannotBeEmpty"/>'/* 小包装总数不能为空 */,'error');
			return false;
		}

		if(rows[i].boxNum == null || rows[i].boxNum == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheTotalOfLargesPackagesCannotBeEmpty"/>'/* 大包装总数不能为空 */,'error');
			return false;
		}
		
		
		var manufactureDate=$('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {index:i,field:"manufactureDate"});
		if(manufactureDate!=null){
			manuDateVal =$(manufactureDate.target).datebox('getValue');
        }else{
        	manuDateVal = rows[i].manufactureDate;
        }
		if(manuDateVal == null ||manuDateVal == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheProductionDateCannotBeEmpty"/>'/* 生产日期不能为空 */,'error');
			return false;
		}else{
			var planDeliveryDateVal = $('#planDeliveryDate').datebox('getValue');
        	var date=(new Date).format('yyyy-MM-dd');
        	if(planDeliveryDateVal !=null && planDeliveryDateVal !=''){
        		if((manuDateVal <= planDeliveryDateVal) && (manuDateVal <= date)){

        		}else{
        			$.messager.progress('close');
    				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheProductionDateShallNotExceedTheCurrentTimeAndShallNotExceedTheExpectedDeliveryDate"/>'/* 生产日期不能超过当前时间，并且不能超过预计发货日期！ */,'error');
    				return false;
        		}
        	}else{
        		if(manuDateVal <= date){

        		}else{
        			$.messager.progress('close');
    				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheProductionDateShallNotExceedTheCurrentTimeAndShallNotExceedTheExpectedDeliveryDate"/>'/* 生产日期不能超过当前时间，并且不能超过预计发货日期！ */,'error');
    				return false;
        		}
        	}
		}
		var charg=$('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {index:i,field:"charg"});
		if(charg!=null){
			chargVal =$(charg.target).textbox('getValue');
        }else{
        	chargVal = rows[i].charg;
        }
		if(chargVal == null ||chargVal == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.BatchNumberCannotBeEmpty"/>'/* 批号不能为空 */,'error');
			return false;
		}
		
		var vendorCharg=$('#datagrid-delivery-item-addoredit-list').datagrid('getEditor', {index:i,field:"vendorCharg"});
		if(vendorCharg!=null){
			vendorChargVal =$(vendorCharg.target).textbox('getValue');
        }else{
        	vendorChargVal = rows[i].vendorCharg;
        }
		if(vendorChargVal == null ||vendorChargVal == '') {
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TraceabilityBatchNumberCannotBeEmpty"/>'/* 追溯批号不能为空 */,'error');
			return false;
		}
    } 

	var length=rows.length;
	var str="{total:"+length+",rows:"+$.toJSON(rows)+"}";
	var datas=str;
	$('#form-delivery-addoredit').form('submit',{
		ajax:true,
		iframe: true,    
		url: '${ctx}/manager/order/delivery/editDelivery', 
		onSubmit:function(param){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			
			param.datas = datas; 			//发货明细数据
			param.type = type;	//发货状态
			return isValid;
		},
		 success: function (data) {   
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){ 
						$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->', result.message ,'info');
						$('#win-delivery-addoredit').window('close');
						$('#datagrid-delivery-list').datagrid('reload'); 
					}else{
						editIndex = undefined;
						$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.message,'error');
					}
				}catch (e) {
					editIndex = undefined;
					$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',e,'error'); 
				} 
	       	}
	 }); 
	
}

//批量发货
function delivery(){
	var selections = $('#datagrid-delivery-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！ */,'info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].deliveryStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.ContainsShippingRecordsAndCannotRepeatShipment"/>'/* 包含已发货记录无法重复发货 */,'error');  
			return false;
		}else if(selections[i].auditStatus!=1){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.IncludeInvoiceIssuedWithoutReview"/>'/* 包含未审核通过的发货单 */,'error');  
			return false;
		}
	}
	
	$.messager.progress();
	var params = $.toJSON(selections);
	$.messager.confirm('<spring:message code="purchase.delivery.OperationHints"/>'/* 操作提示 */, '<spring:message code="purchase.delivery.DetermineTheDeliveryOperation"/>'/* 确定发货操作吗？ */, function (data) {  
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/order/delivery/dodelivery',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			$.messager.progress('close');
        			if(data.success){
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
        				$('#datagrid-delivery-list').datagrid('reload');
        			}
        		}
        	});   
        }
	});
}


//取消发货单
function canceldelivery() {
	var selections = $('#datagrid-delivery-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！ */,'info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].receiveStatus > 0) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheInvoiceCannotBeCancelledIfTeReceivedRecordIsIncluded"/>'/* 包含已收货记录无法取消发货单！ */,'error');
			return false;
		}
	}
	
	$.messager.progress();
	var params = $.toJSON(selections);
	$.messager.confirm('<spring:message code="purchase.delivery.OperationHints"/>'/* 操作提示 */, '<spring:message code="purchase.delivery.WillThisOperationDeleteTheShippingListAndDetermineWhetherTheOperationIsToBePerformed"/>'/* 执行此操作将删除发货单,确定要执行操作吗？ */, function (data) {
        if (data) {
        	$.ajax({
        		url:'${ctx}/manager/order/delivery/canceldeliverys',
        		type:'POST',
        		data:params,
        		dataType:"json",
        		contentType : 'application/json',
        		success:function(data){
        			$.messager.progress('close');
        			if(data.success){
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
        				$('#datagrid-delivery-list').datagrid('reload');
        			}
        		}
        	});
        }
    });
}

//打印信息
function gotoPrint(pid){
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

</script>
</body>
</html>

