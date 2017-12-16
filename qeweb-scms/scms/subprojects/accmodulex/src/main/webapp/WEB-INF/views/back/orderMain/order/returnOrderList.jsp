<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   
<c:set var="vendor" value="${vendor} "/> 
	<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/OrderRowEditor.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/Feedback.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/OrderMain.js"></script>
    <script type="text/javascript" src="${ctx}/static/script/basedata/dialog.js"></script>
<script type="text/javascript">

$(function() { 
	$('#datagrid-orderItem-list').datagrid({
		rowStyler:function(index,row){
			if (row.isReject == 1 || row.isRed == 1){
				return 'background-color:red;color:black;';
			}
		}
	});
});
function operateFmt(v,r,i){
	var isVendor = $("#isVendor").val();
      if(r.zlock == 1){
    	  return  s;
	  }else{
		  s=' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="feedback.showFeedback(\'/manager/order/purchaseorder/feedback/'+ r.id +'\');"><spring:message code="purchase.order.Feedback"/>【'+r.feedbackCount+'】</a>'/* 反馈 */;
		  //确认的订单可以编辑（供应商不可编辑）
	
/* 		  debugger;
		  if(r.confirmStatus == 1 &&  isVendor == "false"){
			  s=s+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editOrderQry('+ r.id +');">'+'编辑</a>';
		  } */
		  return  s;
	  }
}

function getNumFmt(v,r,i) {
	if(v=="" || v==null){
		return 0;
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


<!-- 订单的明细 -->
<div style="height: 500px">
	<table id="datagrid-orderItem-list" class="easyui-datagrid"  fit="true" title="<spring:message code="purchase.orderMain.returnOrderList.OrderDetails"/>" 
		data-options="url:'${ctx}/manager/order/returnPurchase/getReturnOrderItemList/${vendor}',method:'post',singleSelect:false,   
		toolbar:'#orderItemListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		 <thead data-options="frozen:true">
			<tr>
				<th data-options="field:'id',checkbox:true"></th>
		         <th data-options="field:'operate',formatter:operateFmt, width: 150"><spring:message code="purchase.orderMain.returnOrderList.operation"/></th>
				<th data-options="field:'orderCode' , formatter:function(v,r,i){return r.order.orderCode;} "><spring:message code="purchase.orderMain.returnOrderList.PurchaseOrderNumber"/></th>
				<c:if test="${vendor == 'false '}">  
				<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><spring:message code="purchase.orderMain.returnOrderList.vendorCode"/></th>
		        </c:if>
		     	<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.orderMain.returnOrderList.MaterialNumber"/></th>
		        <th data-options="field:'materialDescride',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.orderMain.returnOrderList.MaterialDescription"/></th>	
		        	<th data-options="field:'col3' , formatter:getNumFmt "><spring:message code="purchase.orderMain.returnOrderList.NumberNotClear"/></th> <!-- 来自收货单后期加上计算 -->
			</tr>
		</thead>
	
		<thead>
	   <tr>
	
	
		<th data-options="field:'itemNo'"><spring:message code="purchase.orderMain.returnOrderList.LineItem"/></th>
		 <c:if test="${vendor == 'false '}"> 
		<th data-options="field:'purOrderType',formatter:function(v,r,i){return r.order.purOrderType.name;}"   ><spring:message code="purchase.orderMain.returnOrderList.OrderType"/></th>
		</c:if>
		<th data-options="field:'companyCode' , formatter:function(v,r,i){ if(r.order.company){ return r.order.company.code; }  else{  return '' ;}     }"    ><spring:message code="purchase.orderMain.returnOrderList.CompanyCode"/></th>
		<th data-options="field:'companyName' ,formatter:function(v,r,i){ if(r.order.company){  return r.order.company.name; } else{  return '' ; }  }"><spring:message code="purchase.orderMain.returnOrderList.CompanyName"/></th>
		<th data-options="field:'factoryEntity.code'"><spring:message code="purchase.orderMain.returnOrderList.FactoryCode"/></th>
		<th data-options="field:'factoryEntity.name'"><spring:message code="purchase.orderMain.returnOrderList.FactoryName"/></th>
		<th data-options="field:' pstyp'"><spring:message code="purchase.orderMain.returnOrderList.ItemType"/></th>
		<c:if test="${vendor != 'false '}">  
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><spring:message code="purchase.orderMain.returnOrderList.vendorCode"/></th>
			</c:if>
	   <th data-options="field:'vendorName',formatter:function(v,r,i){return r.order.vendor.name;} "  ><spring:message code="purchase.orderMain.returnOrderList.VendorName"/></th>
		<th data-options="field:' ztermms'  , formatter:function(v,r,i){return r.order.ztermms;} "   ><spring:message code="purchase.orderMain.returnOrderList.TermOfPayment"/></th>
	
		<th data-options="field:'unitName'"><spring:message code="purchase.orderMain.returnOrderList.OrderUnit"/></th>
		<th data-options="field:'orderQty' ,formatter:getNumFmt "><spring:message code="purchase.orderMain.returnOrderList.OrderQuantity"/></th>
		 <c:if test="${vendor == 'false '}">   
		<th data-options="field:'zslsx' ,formatter:getNumFmt "><spring:message code="purchase.orderMain.returnOrderList.OrderLimitNuber"/></th> 
		</c:if>   
		<th data-options="field:'receiveQty' ,formatter:getNumFmt "><spring:message code="purchase.orderMain.returnOrderList.AACReturnQuantity"/></th><!-- 收货数量 -->
		
		<!-- <th data-options="field:'deQty' ,formatter:getNumFmt ">送货数量</th>   送货数量=在途数量+收货数量
		<th data-options="field:'onwayQty' ,formatter:getNumFmt ">在途数量</th>
		<th data-options="field:' col1  ' , formatter:getNumFmt ">来料不良数量</th>   来自收货单后期加上计算
		<th data-options="field:'  col2 ' ,formatter:getNumFmt ">送检不良数量</th>    来自收货单后期加上计算
		<th data-options="field:'undeliveryQty' , formatter:getNumFmt " >未发货数量</th>	
		 -->
	
		<c:if test="${vendor == 'false '}">     
		<th data-options="field:'zwqsl' , formatter:getNumFmt "><spring:message code="purchase.orderMain.returnOrderList.SAPNumberNotClear"/></th> <!-- 来自收货单后期加上计算 -->
		</c:if>
		
	  
<!-- 	    <th data-options="field:'currency'">币种</th> -->
	   

  		<th data-options="field:'orderDate',formatter:function(v,r,i){return r.order.aedat;}"><spring:message code="purchase.orderMain.returnOrderList.OrderApprovalDate"/></th>
  		<th data-options="field:'requestTime'"><spring:message code="purchase.orderMain.returnOrderList.DeliveryDate"/></th>   
  		<th data-options="field:'publishTime'"><spring:message code="purchase.orderMain.returnOrderList.ReleaseTime"/></th>   
  		<th data-options="field:'confirmTime'"><spring:message code="purchase.orderMain.returnOrderList.ConfirmTime"/></th>  
  		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="purchase.orderMain.returnOrderList.ConfirmStatus"/></th>   
  		<th data-options="field:'receiveOrg'"><spring:message code="purchase.orderMain.returnOrderList.ReceivingAddress"/></th>  
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="purchase.orderMain.returnOrderList.PublishingState"/></th> 
	 	<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="purchase.orderMain.returnOrderList.ShippingState"/></th>  
	    <th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="purchase.orderMain.returnOrderList.ReceivingState"/></th>   
	     <th data-options="field:'zlock',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.returnOrderList.Locked"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.NotLocked"/>';}"><spring:message code="purchase.orderMain.returnOrderList.LockState"/></th>   
	     <c:if test="${vendor == 'false '}">    
	         <th data-options="field:'zsfxp',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.returnOrderList.NewProduct"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.BatchProduction"/>';} "><spring:message code="purchase.orderMain.returnOrderList.WhetherNewProduct"/></th>
	         </c:if> 
	     <th data-options="field:'zfree',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.returnOrderList.AlreadyFree"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.NotFree"/>';} "><spring:message code="purchase.orderMain.returnOrderList.FreeLogo"/></th> 
	     <th data-options="field:'retpo',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.returnOrderList.ReturnedProduct"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.NotRetrunedProduct"/>';}"><spring:message code="purchase.orderMain.returnOrderList.ReturnGoodsLogo"/></th> 
	     <th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.returnOrderList.Deleted"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.NotDeleted"/>';}"><spring:message code="purchase.orderMain.returnOrderList.DeleteLogo"/></th> 
	      <th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.returnOrderList.NO"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.YES"/>';}"><spring:message code="purchase.orderMain.returnOrderList.InwardDeliveryLogo"/></th> 
	     
<!-- 	      <th data-options="field:'frgke',formatter:function(v,r,i){     }">审批状态</th>  -->
	      
	       <th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.orderMain.returnOrderList.AlreadyFrozen"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.NotFrozen"/>';}"><spring:message code="purchase.orderMain.returnOrderList.FrozenState"/></th> 
	       <th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.returnOrderList.Delivered"/>';else return '<spring:message code="purchase.orderMain.returnOrderList.Undelivered"/>';}"><spring:message code="purchase.orderMain.returnOrderList.DeliveryState"/></th> 
		 <th data-options="field:'rejectReason'"><spring:message code="purchase.orderMain.returnOrderList.ReasonsForRejection"/></th> 
		 <th data-options="field:'bpumn'"><spring:message code="purchase.orderMain.returnOrderList.Molecule"/></th> 
		  <th data-options="field:'bpumz' "><spring:message code="purchase.orderMain.returnOrderList.Denominator"/></th> 
		  <th data-options="field:'lfimg' ,formatter:getNumFmt"><spring:message code="purchase.orderMain.returnOrderList.BasicOrderNumber"/></th> 
		 
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		</tr></thead>
	</table>
	<div id="orderItemListToolbar" style="padding:5px;">
		<div>
			<c:if test="${vendor == 'false '}">  
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="unVetoOrderItems()"><spring:message code="purchase.orderMain.returnOrderList.Agree"/></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vetoOrderItems() " ><spring:message code="purchase.orderMain.returnOrderList.RefuseReject"/></a><!-- //采购商的驳回  -->
				<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="publishOrderItems()">发布</a> -->
			</c:if>  
			<c:if test="${vendor != 'false '}">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="orderOpt.operateOrder('datagrid-orderItem-list','confirm','orderItem')"><spring:message code="purchase.orderMain.returnOrderList.Confirm"/></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="rejectOrderItems() "><spring:message code="purchase.orderMain.returnOrderList.Reject"/></a><!-- //供应商的驳回    rejectOrderItems  -->
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="exportExcel ( ${vendor})"><spring:message code="purchase.orderMain.returnOrderList.Export"/></a>
			</c:if>  
		</div>
		<div>
			<form id="form-orderItem-search" method="post">
			<spring:message code="purchase.orderMain.returnOrderList.PurchaseOrderNumber"/>：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.returnOrderList.vendorCode"/>：<input type="text" name="search-LIKE_order.vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.returnOrderList.VendorName"/>：<input type="text" name="search-LIKE_order.vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.returnOrderList.PublishingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="purchase.orderMain.returnOrderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.returnOrderList.Unpublished"/></option><option value="1"><spring:message code="purchase.orderMain.returnOrderList.AlreadyPublished"/></option><option value="2"><spring:message code="purchase.orderMain.returnOrderList.PartialRelease"/></option></select>
			<spring:message code="purchase.orderMain.returnOrderList.ConfirmStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="purchase.orderMain.returnOrderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.returnOrderList.Unconfirmed"/></option><option value="1"><spring:message code="purchase.orderMain.returnOrderList.AlreadyConfirmed"/></option><option value="-1"><spring:message code="purchase.orderMain.returnOrderList.Reject"/></option><option value="-2"><spring:message code="purchase.orderMain.returnOrderList.RefuseReject"/></option></select><br>
			<spring:message code="purchase.orderMain.returnOrderList.ShippingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_deliveryStatus"><option value="">-<spring:message code="purchase.orderMain.returnOrderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.returnOrderList.NotShipped"/></option><option value="1"><spring:message code="purchase.orderMain.returnOrderList.AlreadyShipped"/></option><option value="2"><spring:message code="purchase.orderMain.returnOrderList.PartialShipment"/></option></select>
			<spring:message code="purchase.orderMain.returnOrderList.ReceivingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_receiveStatus"><option value="">-<spring:message code="purchase.orderMain.returnOrderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.returnOrderList.GoodsNotReceived"/></option><option value="1"><spring:message code="purchase.orderMain.returnOrderList.AlreadyReceived"/></option><option value="2"><spring:message code="purchase.orderMain.returnOrderList.PartialReceipt"/></option></select>
			<spring:message code="purchase.orderMain.returnOrderList.OffState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-<spring:message code="purchase.orderMain.returnOrderList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.returnOrderList.NotClosed"/></option><option value="1"><spring:message code="purchase.orderMain.returnOrderList.AlreadyClosed"/></option></select>
			<tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder('form-orderItem-search','datagrid-orderItem-list')"><spring:message code="purchase.orderMain.returnOrderList.Query"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-orderItem-search').form('reset')"><spring:message code="purchase.orderMain.returnOrderList.Reset"/></a> 
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
</div>

<!-- 供应商驳回窗口 -->
	<div id="win-reject" class="easyui-dialog" title='<spring:message code="purchase.orderMain.returnOrderList.Reject"/>' style="width:450px;height:200px"
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

	
	<!-- 采购商确认驳回窗口 -->
	<div id="win-veto" class="easyui-dialog" title="<spring:message code="purchase.orderMain.returnOrderList.SupplierRejectsConfirmation"/>" style="width:600px;height:350px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div id="" style="padding:5px;">
			<form id="form-veto" method="post" 	>
			  <input id="veto_type" name="veto_type" type="hidden"/>
				<input id="veto_ids" name="veto_ids" type="hidden"/>
				<table>
					<tr class="border-none">
						<td>
						<label class="common-label"><spring:message code="purchase.orderMain.returnOrderList.vendorCode"/>：</label><label  id="ve-code"></label>
						</td>
						<td>
						<label class="common-label"><spring:message code="purchase.orderMain.returnOrderList.VendorName"/>：</label><label   id="ve-name"></label>
						</td>
					</tr>

					<tr id="veto-detail" style="display: black;" class="border-none">
						<td colspan="2">
						<label class="common-label" style="float: left;"><spring:message code="purchase.orderMain.returnOrderList.DescriptionOfReasonsForRejection"/>:</label>
						<textarea id="ve-reason" name="rejectReason" class="easyui-validatebox" style="width: 400px; height: 60px;" readonly></textarea>
						</td>
					</tr> 
					
					<tr id="veto-detail1" style="display: black;" class="border-none">
						<td colspan="2">
						<label  id = "veto-label"  class="common-label" style="float: left;"></label>
						<textarea id="ve-veto" name="vetoReason" class="easyui-validatebox"    style="width: 400px; height: 60px;"   ></textarea>
						</td>
					</tr> 
				 </table>
				<div id="dialog-adder-bbb1" style="text-align: center; display : none"  >
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="unVeto()"><spring:message code="button.submit"/><!-- 提交 --></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="resetVeto()"><spring:message code="button.reset"/><!-- 重置 --></a>
				</div>
				<div id="dialog-adder-bbb2" style="text-align: center;display : none">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="veto()"><spring:message code="button.submit"/><!-- 提交 --></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="resetVeto()"><spring:message code="button.reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>

 <!--    修改订单数量 -->
 	<div id="win-qry-addoredit" class="easyui-window"
		style="width: 300px; height: 200px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-time'">
		<form id="form-qry-addoredit" method="post">
			<input id="orderId" name="id" value="0" type="hidden" /> 
			<table style="text-align: right; padding: 5px; margin: auto;" cellpadding="5">
			<tr>
				<td ><spring:message code="purchase.orderMain.returnOrderList.NumberOfOrder"/>:</td>
				<td>
				<input  id ="oldOrderQty"   name="orderQty"  type = "hidden" class="easyui-textbox" data-options="required:true" />
					<input  id ="orderQty"   name="orderQty" value="" class="easyui-textbox" data-options="required:true" />
				</td>
			</tr>
			</table>
		</form>
		<div id="dialog-adder-time">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="saveQry()"><spring:message code="purchase.orderMain.returnOrderList.Submit"/></a> <a
				href="javascript:;" class="easyui-linkbutton"
				onclick="$('#form-qry-addoredit').form('reset')"><spring:message code="purchase.orderMain.returnOrderList.Reset"/></a>
		</div>
	</div>


<script type="text/javascript">
function searchOrder(formId,tableId){
	var _formId = "#"+formId;
	var _tableId = "#"+tableId;
	var searchParamArray = $(_formId).serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$(_tableId).datagrid('load',searchParams);
}

//同步订单（单个）
function sycOneOrder(orderCodes){
	$.messager.progress();
	debugger;
	$.ajax({
		url:'${ctx}/manager/order/purchasemainorder/sycOneOrder/'+orderCodes,
		type:'POST',
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="purchase.order.news"/>'/* '消息' */,
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
//发布明细行（发布取消，）
function publishOrderItems(){
	//发布
	var selections = $("#datagrid-orderItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！*/,'info');
		return false;
	}
	var needRows = new Array();
	
	//如果有采购商同意供应商驳回的数据忽略发布等待被sap修改
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].vetoStatus == 1) {
			continue;
		}else{
			needRows.push(selections[i]);
		}
	}
	
	for(i = 0; i < needRows.length; i ++) {
		if(needRows[i].publishStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.ContainsPublishedRecordsAndCannotBeDuplicated"/>'/* 包含已发布记录无法重复发布！ */,'error');
			return false;
		} 
	}
	$.messager.progress();
	var url=ctx + '/manager/order/purchasemainorder/publishOrderItem';
	var params = $.toJSON(needRows);
	$.ajax({
		url:url,
		type:"POST",
		data:params,
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
					$('#datagrid-orderItem-list').datagrid('reload');
				}else{
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
			} 
		}
	});

}
//供应商的驳回
function rejectOrderItems(){
	//驳回
	var selections = $("#datagrid-orderItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！*/,'info');
		return false;
	}
	
    //驳回数据的ids
	var reject_ids="";
	var reject_type='orderItem';
	for(i = 0; i < selections.length; i ++) {
		reject_ids=reject_ids+selections[i].id + ",";
		if( selections[i].confirmStatus == -1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.OrdersAreAllowedToBeRejectedOnlyOnce"/>'/* '订单只允许驳回一次！' */,'error');
			return false;
		} 
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.TheSelectionDataContainsConfirmedRecordsAndCannotBeRejected"/>'/* '选择数据包含已确认记录，无法驳回！' */,'error');
			return false;
		} 
		if(selections[i].confirmStatus == -2) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.OrdersAreAllowedToBeRejectedOnlyOnce"/>'/* '订单只允许驳回一次！' */,'error');
			return false;
		} 
		if(selections[i].vetoStatus == 0) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.OrdersAreAllowedToBeRejectedOnlyOnce"/>'/* '订单只允许驳回一次！' */,'error');
			return false;
		} 
	}
	document.getElementById("reject_ids").value=reject_ids;
	document.getElementById("reject_type").value=reject_type;
	$('#win-reject').window('open');   
}

//采购商的驳回
function vetoOrderItems(){
	//驳回
	var selections = $("#datagrid-orderItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！*/,'info');
		return false;
	}
    //驳回数据的ids
	var veto_ids="";
	var veto_type='orderItem';
	var data="";
	for(i = 0; i < selections.length; i ++) {
		veto_ids=veto_ids+selections[i].id + ",";
		if( selections[i].vetoStatus == 0) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.IncludingRejectionOfTheRecordCannotRejectTheTejectionOfTheSupplierRepeatedly"/>'/* '包含驳回记录无法重复驳回供应商的驳回！' */,'error');
			return false;
		} 
		if(selections[i].confirmStatus != -1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.PleaseRejectTheRecord"/>'/* '请选择驳回记录！' */,'error');
			return false;
		} 
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.InclusionOfConfirmedRecordsCannotReject"/>'/* '包含已确认记录无法驳回！' */,'error');
			return false;
		} 

		data = selections[i];
	}
	//赋值
	document.getElementById("ve-code").innerHTML = data.order.vendor.code;
	document.getElementById("ve-name").innerHTML = data.order.vendor.name;
	document.getElementById("ve-reason").value  = data.rejectReason;
	
	document.getElementById("veto_ids").value=veto_ids;
	document.getElementById("veto_type").value=veto_type;

	$("#veto-label").text('<spring:message code="purchase.orderMain.orderItemList.ReasonsForRefusingToReject"/>'/* "拒绝驳回原因：" */);	
	
	document.getElementById("dialog-adder-bbb1").style.display="none";//隐藏
	document.getElementById("dialog-adder-bbb2").style.display="";//显示

	$('#win-veto').window('open');   
}

//采购商的驳回提交
function veto(){
	
	$.messager.confirm('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.DetermineCurrentOperation"/><font style="color: #F00;font-weight: 900;"></font>'/* 确定当前操作？ */,function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="purchase.order.Prompt"/>'/* 提示 */,
				msg : '<spring:message code="purchase.order.Submission"/>...'/* '提交中' */
			});
			
			$('#form-veto').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/order/purchasemainorder/vetoOrderItem', 
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
							$('#win-veto').window('close');
							$('#datagrid-order-list').datagrid('reload');
							$('#datagrid-orderItem-list').datagrid('reload');
						}else{
							$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,obj.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
					} 
				}
			});
		}
	});
}

//采购商的同意
function unVetoOrderItems(){
	//驳回
	var selections = $("#datagrid-orderItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！*/,'info');
		return false;
	}
    //驳回数据的ids
	var veto_ids="";
	var veto_type='orderItem';
	var data="";
	for(i = 0; i < selections.length; i ++) {
		veto_ids=veto_ids+selections[i].id + ",";
		if( selections[i].vetoStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.IncludeAgreedRecordsAndCannotAgreeWithSuppliersRejection"/>'/* '包含同意记录无法重复同意供应商的驳回！' */,'error');
			return false;
		} 
		if(selections[i].confirmStatus != -1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.PleaseRejectTheRecord"/>'/* '请选择驳回记录！' */,'error');
			return false;
		} 
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.InclusionOfConfirmedRecordsCannotAgree"/>'/* '包含已确认记录无法同意！' */,'error');
			return false;
		} 

		data = selections[i];
	}
	//赋值
	document.getElementById("ve-code").innerHTML = data.order.vendor.code;
	document.getElementById("ve-name").innerHTML = data.order.vendor.name;
	document.getElementById("ve-reason").value  = data.rejectReason;
	
	document.getElementById("veto_ids").value=veto_ids;
	document.getElementById("veto_type").value=veto_type;
	
	
	$("#veto-label").text('<spring:message code="purchase.orderMain.orderItemList.ReasonsForAdjustment"/>:'/* "调整理由：" */);
	document.getElementById("dialog-adder-bbb1").style.display="";//隐藏
	document.getElementById("dialog-adder-bbb2").style.display="none";//显示
	
	$('#win-veto').window('open');   
}

//采购商的同意提交
function unVeto(){
	$.messager.confirm('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.DetermineCurrentOperation"/><font style="color: #F00;font-weight: 900;"></font>'/* 确定当前操作？ */,function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="purchase.order.Prompt"/>'/* 提示 */,
				msg : '<spring:message code="purchase.order.Submission"/>...'/* '提交中' */
			});
			
			$('#form-veto').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/order/purchasemainorder/unVetoOrderItem', 
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
							$('#win-veto').window('close');
							$('#datagrid-order-list').datagrid('reload');
							$('#datagrid-orderItem-list').datagrid('reload');
						}else{
							$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,obj.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
					} 
				}
			});
			
		}
	});
	

}

function exportExcel(vendor){
     $('#form-orderItem-search').form('submit',{
 	   url:'${ctx}/manager/order/returnPurchase/exportExcel/'+ vendor,
 	   success:function(data){
 			$.messager.progress('close');
 		}
 	}); 
 }
 
 
function editOrderQry(id){
	$('#win-qry-addoredit').dialog({
		iconCls:'icon-edit',
		title:'<spring:message code="purchase.orderMain.orderItemList.Editing"/>'/* '编辑' */
	});
	$('#form-qry-addoredit').form('clear');
	$('#win-qry-addoredit').dialog('open');
	$('#form-qry-addoredit').form('load',ctx+'/manager/order/purchasemainorder/getOrderItem/'+id);
}
function saveQry(){
	var url = ctx+'/manager/order/purchasemainorder/editOrderItem';
	var sucMeg = '<spring:message code="purchase.orderMain.orderItemList.SuccessfulOperation"/>'/* '操作成功！' */;
	var  id  =  document.getElementById("orderId").value;
	var oldNumber = document.getElementById("oldOrderQty").value;
    var number =document.getElementById("orderQty").value;
    if(parseInt(number) > parseInt(oldNumber) ){
    	$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.TheNumberOfModificationsMustBeLessThanTheVurrentQuantity"/>'/* "修改数量必须小于等于当前数量！" */,'info');
    	 return false;
    }
    
	if( isNaN( number ) ){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.orderMain.orderItemList.PleaseEnterANumber"/>'/* "请输入数字！" */,'info');
	     return false;
	}	
	
	$.messager.progress({
		title:'<spring:message code="purchase.order.Prompt"/>'/* 提示 */,
		msg : '<spring:message code="purchase.order.Submission"/>...'/* '提交中' */
	});
	
	var data = {
			id : id,
			orderQty:number
	}
	
  	$.ajax({
  		url:url,
        type: 'post',
        data: data, 
        dataType:"json",
        success: function (data) {   
			$.messager.progress('close');
			debugger;
			var obj = data;
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
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,sucMeg,'info');
					$('#win-qry-addoredit').dialog('close');
					$('#datagrid-orderItem-list').datagrid('reload');
				}else{
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,obj.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
			} 
        }
      });
}
function resetVeto(id){
    $("#ve-veto").val("");
}

</script>

