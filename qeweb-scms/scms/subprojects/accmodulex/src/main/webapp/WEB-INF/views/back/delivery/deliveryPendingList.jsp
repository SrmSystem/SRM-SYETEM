<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="purchase.delivery.Deliverykanban"/></title><!-- 发货看板 -->
	<script type="text/javascript" src="${ctx}/static/script/purchase/common.js">
	function getNumFmt(v,r,i) {
		
 		if(v=="" || v==null || v == "0.000" ){
			return 0.000;
		}
		var num=v;
	    num = num.toString().replace(/\$|\,/g,'');  
	    // 获取符号(正/负数)  
	    sign = (num == (num = Math.abs(num)));  
	    num = Math.floor(num*Math.pow(10,3)+0.50000000001);  // 把指定的小数位先转换成整数.多余的小数位四舍五入  
	    cents = num%Math.pow(10,3);              // 求出小数位数值  
	    num = Math.floor(num/Math.pow(10,3)).toString();   // 求出整数位数值  
	    cents = cents.toString();               // 把小数位转换成字符串,以便求小数位长度  
	    // 补足小数位到指定的位数  
	    while(cents.length<3)  
	      cents = "0" + cents;  
	      // 对整数部分进行千分位格式化.  
	      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
	        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

	      return (((sign)?'':'-') + num + '.' + cents);   
	   
	   
	}

	
	</script>
</head>

<!-- 待发货看板 -->
<body style="margin:0;padding:0;">
	<table id="datagrid-pending-list" title='<spring:message code="purchase.delivery.DeliverykanbanList"/>' class="easyui-datagrid" fit="true"
		data-options="method:'post',singleSelect:false,
		toolbar:'#pendingListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead data-options="frozen:true">
			<tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'orderCode',formatter:function(v,r,i){return r.order.orderCode;} "  ><spring:message code="purchase.delivery.PurchaseOrderNumber"/><!-- 采购订单号 --></th>
				<th data-options="field:'shipType',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.delivery.Normal"/>';else if(v=='-1') return '<spring:message code="purchase.delivery.Replenishment"/>';}"><spring:message code="purchase.delivery.TypeOfShipment"/><!-- 发货类型 --></th>
				<th data-options="field:'orderItem.order.purchasingGroup.code'"><spring:message code="purchase.delivery.PurchasingOrganizationCode"/><!-- 采购组编码 --></th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.delivery.MaterialCoding"/><!-- 物料编码 --></th>
		        <th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.delivery.MaterialName"/><!-- 物料名称 --></th>
				<th data-options="field:'orderQty' ,formatter:getNumFmt "><spring:message code="purchase.delivery.QuantityOfGoodsToBePurchased"/><!-- 要货数量 --></th>
		       <th data-options="field:'shouldQty' ,formatter:getNumFmt "><spring:message code="purchase.delivery.PayableQuantity"/><!-- 应发数量 --></th>  
		       	<th data-options="field:'requestTime'"><spring:message code="purchase.delivery.RequiredArrivalTime"/><!-- 要求到货时间 --></th>
			</tr>
		</thead>
	
	<thead>
	   <tr>

		<th data-options="field:'itemNO',formatter:function(v,r,i){return r.orderItem.itemNo;}"><spring:message code="purchase.delivery.LineNumber"/><!-- 行号 --></th>
		<th data-options="field:'vendorCode'"><spring:message code="purchase.delivery.vendorCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'vendorName'"><spring:message code="purchase.delivery.VendorName"/><!-- 供应商名称 --></th>
	
		<th data-options="field:'orderItem.order.purchasingGroup.name'"><spring:message code="purchase.delivery.PurchasingOrganizationName"/><!-- 采购组名称 --></th>
		<th data-options="field:'orderItem.order.company.code',formatter:function(v,r,i){return isNULL(isNULL(isNULL(isNULL(r.orderItem).order).company).code)}"><spring:message code="purchase.delivery.CompanyCode"/><!-- 公司代码 --></th>
		<th data-options="field:'orderItem.order.company.name',formatter:function(v,r,i){return isNULL(isNULL(isNULL(isNULL(r.orderItem).order).company).name)}"><spring:message code="purchase.delivery.CompanyName"/><!-- 公司名称 --></th>
		<th data-options="field:'orderItem.order.factoryEntity.code',formatter:function(v,r,i){return isNULL(isNULL(isNULL(r.orderItem).factoryEntity).code)}"><spring:message code="purchase.delivery.FactoryCode"/><!-- 工厂代码 --></th>
		<th data-options="field:'orderItem.order.factoryEntity.name',formatter:function(v,r,i){return isNULL(isNULL(isNULL(r.orderItem).factoryEntity).name)}"><spring:message code="purchase.delivery.FactoryName"/><!-- 工厂名称 --></th>

		<th data-options="field:'unitName'"><spring:message code="purchase.delivery.OrderUnit"/><!-- 订单单位 --></th>
		<th data-options="field:'receiveOrg' , formatter:function(v,r,i){return r.orderItem.receiveOrg;}  "><spring:message code="purchase.delivery.ReceivingAddress"/><!-- 收货地址 --></th>

		<th data-options="field:'deliveryQty' ,formatter:getNumFmt "><spring:message code="purchase.delivery.QuantityAlreadyIssued"/><!-- 已发数量 --></th>
		<th data-options="field:'receiveQty' ,formatter:getNumFmt "><spring:message code="purchase.delivery.AmountReceived"/><!-- 实收数量 --></th>
		<th data-options="field:'returnQty' ,formatter:getNumFmt "><spring:message code="purchase.delivery.CheckOutQuantity"/><!-- 验退数量 --></th> 
	
		<th data-options="field:'ysts',formatter:function(v,r,i){return r.purchaseGoodsRequest.ysts;}"><spring:message code="purchase.delivery.LogisticsDays"/><!-- 物流天数 --></th>
		<th data-options="field:'zlock',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Locked"/>';else return '<spring:message code="purchase.delivery.NotLocked"/>';}"><spring:message code="purchase.delivery.LockState"/><!-- 锁定状态 --></th>
		<th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.delivery.AlreadyFrozen"/>';else return '<spring:message code="purchase.delivery.NotFrozen"/>';}"><spring:message code="purchase.delivery.FrozenState"/><!-- 冻结状态 --></th>
		<th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Deleted"/>';else return '<spring:message code="purchase.delivery.NotDeleted"/>';}"><spring:message code="purchase.delivery.DeleteMark"/><!-- 删除标记 --></th>
		<th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Delivered"/>';else return '<spring:message code="purchase.delivery.Undelivered"/>';}"><spring:message code="purchase.delivery.DeliveryState"/><!-- 交付状态 --></th>
		<th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.NO"/>';else return '<spring:message code="purchase.delivery.YES"/>';}"><spring:message code="purchase.delivery.InwardDeliveryLogo"/><!-- 内向交货单标识 --></th>
		<th data-options="field:'dn'">DN</th>
		</tr></thead>
	</table>
	<div id="pendingListToolbar" style="padding:5px;">
		<div>
	<%-- 	<shiro:hasPermission name="dlv:pending:add">  --%>
	<c:if test="${vendor}">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="toAddDelivery()"><spring:message code="purchase.delivery.CreateInvoice"/><!-- 创建发货单 --></a>
	</c:if>
	<%-- 	</shiro:hasPermission> --%>
		</div>
		<div>
			<form id="form-pending-search" method="post">
			   <table>
					<tr>
					   <td>
					  		 <!-- 采购订单号 --><spring:message code="purchase.delivery.PurchaseOrderNumber"/>：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" />
					   </td>
					    <td colspan="2">
					  		  <!-- 要求到货时间 --><spring:message code="purchase.delivery.RequiredArrivalTime"/>：<input class="easyui-datebox"  name="search-GTE_requestTime"  style="width: 150px"/>
						              <input class="easyui-datebox"  name="search-LTE_requestTime"  style="width: 150px"/>
					   </td>
					   <td>
					   		<!-- 供应商编码 --><spring:message code="purchase.delivery.vendorCode"/>：<input type="text" name="search-LIKE_order.vendor.code" class="easyui-textbox" />
					   </td>
					</tr>
					<tr>
					  
					    <td>
					 		  <!-- 物料编码 --><spring:message code="purchase.delivery.MaterialCoding"/>：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" />
					    </td>
					    <td>
					    	  <!-- 物料名称 --><spring:message code="purchase.delivery.MaterialName"/>：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" />
					    </td>
					    <td><!-- 待办 --><input type="hidden" id="backlogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 --></td>
					    <td></td>
					</tr>
				</table>	
			  
			
				<div style="text-align:right;padding-right: 50px;padding-bottom:10px">
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPending()"><spring:message code="purchase.delivery.Query"/><!-- 查询 --></a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-pending-search').form('reset')"><spring:message code="purchase.delivery.Reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
	
	

<%@include file="deliveryCommonEdit.jsp" %>		
<script type="text/javascript">
$(function() { 
	var searchParamArray = $('#form-pending-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-pending-list').datagrid({url:'${ctx}/manager/order/delivery/pending',queryParams:searchParams});
	
	//若已锁定|已冻结|已删除|已交付|订单数量改小则当前行高亮显示
	$('#datagrid-pending-list').datagrid({
		rowStyler:function(index,row){
			if (row.zlock == 'X' || row.lockStatus == '1' || row.loekz == 'X' || row.elikz== 'X'  || row.isRed == '1' || row.bstae == 'X'){
				return 'background-color:red;color:black;';
			}
		}
	});
	
});

function searchPending(){
	document.getElementById("backlogId").value="";//清除待办
	var searchParamArray = $('#form-pending-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-pending-list').datagrid('load',searchParams);
}

//创建发货单
function toAddDelivery(){
	var selections = $('#datagrid-pending-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！ */,'info');
		return false;
	}
	var select = new Array();
	var receiveorg = "";
	var factoryCode="";
	var buyerCode="";
	var buyerGroupCode = "";
	var shipType="";
	var companyCode="";
	var companyName="";
	for(var i = 0; i < selections.length; i ++) {
		if(null!=selections[i].zlock && selections[i].zlock=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.DataCannotBeShippedDueToLockedData"/>'/* 存在已锁定的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=selections[i].lockStatus && selections[i].lockStatus=='1'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.ThereIsFrozenDataThatCannotbeShipped"/>'/* 存在已冻结的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=selections[i].loekz && selections[i].loekz=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.ExistingDeletedDataCannotBeShipped"/>'/* 存在已删除的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=selections[i].elikz && selections[i].elikz=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.DeliveryDataCannotBeShipped"/>'/* 存在已交付的数据无法进行发货！ */,'info');
			return false;
		}
		
		if(null!=selections[i].bstae && selections[i].bstae=='X'){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.TheOrderIsNotInwardDeliveryAndNotAllowedToDeliver"/>'/* 该订单不是内向交货,无法发货！ */,'info');
			return false;
		}
		
		if(receiveorg == "")
			receiveorg = selections[i].orderItem.receiveOrg;
		else if(receiveorg != selections[i].orderItem.receiveOrg) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.PleaseSelectTheSameReceivingPartyToSendTheData"/>'/* 请选择同一收货方的数据进行发货！！ */,'info');
			return false;
		}
		
		if(factoryCode== ""){
			factoryCode=selections[i].orderItem.order.company.code;
			companyCode = selections[i].orderItem.order.company.code;
			companyName = selections[i].orderItem.order.company.name;
		}else if(factoryCode != selections[i].orderItem.order.company.code) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.PleaseSelectTheSameFactoryForShipment"/>'/* 请选择同一工厂进行发货！！ */,'info');
			return false;
		}
		
		//判断是否是同一采购组
		if(buyerGroupCode==""){
			buyerGroupCode = selections[i].orderItem.order.purchasingGroup.code;
		}else if(buyerGroupCode != selections[i].orderItem.order.purchasingGroup.code){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.PleaseSelectTheSamePurchasingGroupForShipment"/>'/* 请选择同一采购组进行发货！！ */,'info');
			return false;
		}
		
		//判断是否是同一种发货类型
		if(shipType==""){
			shipType = selections[i].shipType;
		}else if(shipType != selections[i].shipType){
			$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.delivery.PleaseSelectTheSameTypeOfDeliveryForShipment"/>'/* 请选择同一发货类型进行发货！ */,'info');
			return false;
		}
		
		select.push(selections[i].id + ",");
		
		
	}
	
	//获取运输方式
	  $('#transport_type').combobox({ 
		  url:ctx+'/manager/basedata/dict/getDictItemSelect/TRANSPORT_TYPE',
      editable:false,
      cache: true,
      valueField:'code',   
      textField:'name',
     }); 
	
	//收货地址
	/* $('#deliveryAddress').textbox('setValue',receiveorg); */
	$('#win-delivery-addoredit').window('open');  
	var id = selections[0].id;
	var col = id+','+companyCode+","+companyName;
	$('#form-delivery-addoredit').form('load','${ctx}/manager/order/delivery/createDelivery?col='+encodeURI(col));  
	
	//明细
	$('#datagrid-delivery-item-addoredit-list').datagrid({url:'${ctx}/manager/order/delivery/createDeliveryItem?search-IN_id='+select.join('')});
} 

//检验报告
function inspectionReportFmt(v,r,i){
	 var fileNameRow=document.getElementById("fileName_"+r.id);
	 var filePathRow=document.getElementById("filePath_"+r.id);
	if(null==filePathRow||null==filePathRow.value){
		return '<span id="'+r.id+'"></span><input type="hidden"  id="fileName_'+r.id+'"  name="fileName_'+r.id+'"/><input type="hidden"  id="filePath_'+r.id+'"  name="filePath_'+r.id+'"/><a id="btn_imp" href="#" class="easyui-linkbutton"  onclick="toUploadInspectionReport(\''+r.id+'\')"><spring:message code="purchase.order.Upload"/></a>'/* 上传 */;
	}else{
		return '<span id="'+r.id+'"><a tyle="margin-right:10px" href="javascript:;" onclick="File.download(\''+filePathRow.value+'\',\'\')">'+fileNameRow.value+'</a></span><a id="btn_imp" href="#" class="easyui-linkbutton"  onclick="toUploadInspectionReport(\''+r.id+'\')"><spring:message code="purchase.order.Upload"/></a><input type="hidden"  id="fileName_'+r.id+'"  name="fileName_'+r.id+'" value="'+fileNameRow.value+'"/><input type="hidden"  id="filePath_'+r.id+'"  name="filePath_'+r.id+'" value="'+filePathRow.value+'"/>';
	}
}


//提交审核
function saveDelivery(type) {  
	
	
	var deliveryContacter = $("#deliveryContacter").val();
	var deliveryAddress = $("#deliveryAddress").val();
	var logisticsCompany = $("#logisticsCompany").val();
	var logisticsContacter = $("#logisticsContacter").val();
	var deliveryTel = $("#deliveryTel").val();
	var logisticsTel = $("#logisticsTel").val();
	var stri = "deliveryContacter="+deliveryContacter+":deliveryAddress="+deliveryAddress+":logisticsCompany="+logisticsCompany+":logisticsContacter="+logisticsContacter+":deliveryTel="+deliveryTel+":logisticsTel="+logisticsTel;
	document.cookie = stri
						
	$.messager.progress();
	
	$('#datagrid-delivery-item-addoredit-list').datagrid('acceptChanges');    
 	var rows = $('#datagrid-delivery-item-addoredit-list').datagrid('getRows');
 	if(rows.length==0){
		$.messager.progress('close');
		$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！ */,'error');
		return false;
	}
 	
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
		url: '${ctx}/manager/order/delivery/saveDelivery', 
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
						$('#datagrid-pending-list').datagrid('reload'); 
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
	
</script>
</body>
</html>
