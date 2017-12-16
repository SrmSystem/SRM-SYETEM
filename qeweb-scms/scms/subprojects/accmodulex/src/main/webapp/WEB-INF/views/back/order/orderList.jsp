<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title><spring:message code="purchase.order.PurchaseOrderManagement"/></title>
	<script type="text/javascript">
	$(function() { 
		$('#datagrid-order-list').datagrid({
			rowStyler:function(index,row){
				if (row.isReject == 1 || row.isRed == 1){
					return 'background-color:red;color:black;';
				}
			}
		});
});
	
	//ASN列格式化
	function asnFmt(v,r,i){
		 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDeliveryItemByOrderItem('+ r.id +');">ASN</a>'; 
	}
	
	
		function orderCodeFmt(v,r,i){
			/* return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Order.showOrderPlanDetail('+ r.id +','+${vendor}+');">' + r.order.orderCode + '</a>'; */
				return  r.order.orderCode ;
		}
		
		function operateFmtEx (v,r,i) {
			//判断是否被锁定
			if(r.zlock != 1){
				var s='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="feedback.showFeedback(\'/manager/order/purchaseorder/feedback/'+ r.id +'\');"><spring:message code="purchase.order.Feedback"/>【'+r.feedbackCount+'】</a>';/* 反馈 */
			    return  s;
			}else{
				return "";
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
	<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/OrderRowEditor.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/Feedback.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/Order.js"></script>
    <script type="text/javascript" src="${ctx}/static/script/basedata/dialog.js"></script>
    <script type="text/javascript" src="${ctx}/static/script/contract/dialogx.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-order-list" title=<spring:message code="purchase.order.PurchaseOrderList"/> class="easyui-datagrid"  fit="true"
		data-options="url:'${ctx}/manager/order/purchaseorder/${vendor}',method:'post',singleSelect:false,   
		toolbar:'#orderListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
	 <thead data-options="frozen:true">
		<tr>
			<th data-options="field:'orderCode',formatter:orderCodeFmt"><spring:message code="purchase.order.OrderNumber"/></th>
			 <c:if test="${vendor == false}"> 
			<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><!-- 供应商编码 --><spring:message code="purchase.order.vendorCode"/></th>
		    </c:if>
		    <th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><!-- 物料号 --><spring:message code="purchase.order.MaterialNumber"/></th>
		    <th data-options="field:'materialDescride',formatter:function(v,r,i){return r.material.name;}"><!-- 物料描述 --><spring:message code="purchase.order.MaterialDescription"/></th>	
		    <th data-options="field:'col3' , formatter:getNumFmt "><spring:message code="purchase.order.NumberNotClear"/></th> <!-- 来自收货单后期加上计算 -->
		</tr>
	</thead>

	<thead>
	   <tr>
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="purchase.order.ConfirmStatus"/></th> 
	
		<th data-options="field:'itemNo'"><spring:message code="purchase.order.LineItem"/></th>
		 <c:if test="${vendor == false}"> 
		<th data-options="field:'purOrderType',formatter:function(v,r,i){return r.order.purOrderType.name;}"   ><spring:message code="purchase.order.OrderType"/></th>
		</c:if>
		
		<th data-options="field:'asn',formatter:asnFmt">ASN</th>
		<th data-options="field:'companyCode' , formatter:function(v,r,i){ if(r.order.company){ return r.order.company.code; }  else{  return '' ;}     }"    ><!-- 公司编码 --><spring:message code="purchase.order.CompanyCode"/></th>
		<th data-options="field:'companyName' ,formatter:function(v,r,i){ if(r.order.company){  return r.order.company.name; } else{  return '' ; }  }"><!-- 公司名称 --><spring:message code="purchase.order.CompanyName"/></th>

		<th data-options="field:'factoryEntity.code'"><spring:message code="purchase.order.FactoryCode"/></th>
		<th data-options="field:'factoryEntity.name'"><spring:message code="purchase.order.FactoryName"/></th>
		<th data-options="field:' pstyp'"><spring:message code="purchase.order.ItemType"/></th>
		<th data-options="field:'group.name' ,formatter:function(v,r,i){return r.order.purchasingGroup.name;} " ><spring:message code="purchase.order.PurchasingTeam"/></th>
		 <c:if test="${vendor != false}"> 
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><spring:message code="purchase.order.vendorCode"/></th>
			</c:if>
	    <th data-options="field:'vendorName',formatter:function(v,r,i){return r.order.vendor.name;} "  ><spring:message code="purchase.order.VendorName"/></th>
		<th data-options="field:' ztermms'  , formatter:function(v,r,i){return r.order.ztermms;} "   ><spring:message code="purchase.order.TermOfPayment"/></th>
	
		<th data-options="field:'orderQty' ,formatter:getNumFmt "><spring:message code="purchase.order.OrderQuantity"/></th>
		 <c:if test="${vendor == false}">       
		<th data-options="field:'zslsx' ,formatter:getNumFmt "><spring:message code="purchase.order.OrderLimitNuber"/></th> 
		</c:if>
		<th data-options="field:'deQty' , formatter:getNumFmt "><spring:message code="purchase.order.DeliveryNumber"/></th> <!-- 送货数量=在途数量+收货数量 -->
		
		<th data-options="field:'onwayQty' ,formatter:getNumFmt "><spring:message code="purchase.order.UnderWayNumber"/></th>
		<th data-options="field:'receiveQty' ,formatter:getNumFmt " ><spring:message code="purchase.order.ReceiptNumber"/></th>
		
		<th data-options="field:'col1' ,formatter:getNumFmt "><spring:message code="purchase.order.BadQuantityNub"/></th> <!-- 来自收货单后期加上计算 -->
		<th data-options="field:' col2  ' ,formatter:getNumFmt "><spring:message code="purchase.order.BadInspectionQuantity"/></th> <!-- 来自收货单后期加上计算 -->
		<th data-options="field:'col3' , formatter:getNumFmt "><spring:message code="purchase.order.NumberNotClear"/></th> <!-- 来自收货单后期加上计算 -->
		 <c:if test="${vendor == false}">      
		<th data-options="field:'zwqsl' , formatter:getNumFmt "><spring:message code="purchase.order.SAPNumberNotClear"/></th> <!-- 来自收货单后期加上计算 -->
		</c:if>
	    <th data-options="field:'undeliveryQty' , formatter:getNumFmt "><spring:message code="purchase.order.QuantityNotDelivered"/></th>
<!-- 	    <th data-options="field:'currency'">币种</th> -->
	    <th data-options="field:'unitName'"><spring:message code="purchase.order.Company"/></th>
  
  		<th data-options="field:'orderDate',formatter:function(v,r,i){return r.order.aedat;}"><spring:message code="purchase.order.OrderApprovalDate"/></th>
  		<th data-options="field:'requestTime'"><spring:message code="purchase.order.DeliveryDate"/></th>   
  		<th data-options="field:'confirmTime'"><spring:message code="purchase.order.ConfirmTime"/></th>  
  	  
  		<th data-options="field:'receiveOrg'"><spring:message code="purchase.order.ReceivingAddress"/></th>  
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="purchase.order.PublishingState"/></th> 
	 	<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="purchase.order.ShippingState"/></th>  
	    <th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="purchase.order.ReceivingState"/></th>   
	    <th data-options="field:'zlock',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.delivery.Locked"/>';else return '<spring:message code="purchase.delivery.NotLocked"/>';}"><spring:message code="purchase.delivery.LockState"/></th><!-- 锁定状态 -->
		<th data-options="field:'aedat',formatter:function(v,r,i){return r.order.aedat;}"><spring:message code="purchase.order.CreationDate"/></th>
		 <c:if test="${vendor == false}">    
		  <th data-options="field:'zsfxp',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.order.NewProduct"/>';else return '<spring:message code="purchase.order.BatchProduction"/>';} "><spring:message code="purchase.order.WhetherNewProduct"/></th>  
		  </c:if>
	     <th data-options="field:'zfree',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.order.AlreadyFree"/>';else return '<spring:message code="purchase.order.NotFree"/>';}"><spring:message code="purchase.order.FreeLogo"/></th> 
	     <th data-options="field:'retpo',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.order.ReturnedProduct"/>';else return '<spring:message code="purchase.order.NotRetrunedProduct"/>';}"><spring:message code="purchase.order.ReturnGoodsLogo"/></th> 
	     <th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.order.Deleted"/>';else return '<spring:message code="purchase.order.NotDeleted"/>';}"><spring:message code="purchase.order.DeleteLogo"/></th><!-- 删除表示  -->
	     <th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.order.NO"/>';else return '<spring:message code="purchase.order.YES"/>';}"><spring:message code="purchase.order.InwardDeliveryLogo"/></th> 
	     
	      
	       <th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.order.AlreadyFrozen"/>';else return '<spring:message code="purchase.order.NotFrozen"/>';}"><spring:message code="purchase.order.FrozenState"/></th> 
	       <th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.order.Delivered"/>';else return '<spring:message code="purchase.order.Undelivered"/>';}"><spring:message code="purchase.order.DeliveryState"/></th> 
		 <th data-options="field:'rejectReason'"><spring:message code="purchase.order.ReasonsForRejection"/></th> 
		 <th data-options="field:'bpumn'"><spring:message code="purchase.order.Molecule"/></th> 
		  <th data-options="field:'bpumz' "><spring:message code="purchase.order.Denominator"/></th> 
		  <th data-options="field:'lfimg' ,formatter:getNumFmt"><spring:message code="purchase.order.BasicOrderNumber"/></th> 
		 
		 <th data-options="field:'surBaseQty' ,formatter:getNumFmt "><spring:message code="purchase.order.BasicNumberOfOrderResidualMatching"/></th> 
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		</tr></thead>
	</table>
	<div id="orderListToolbar" style="padding:5px;">
<%-- 		<div>
			<c:if test="${vendor == false}">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()">同步订单</a>
			</c:if>  
			<c:if test="${vendor}">
			<shiro:hasPermission name="purchase:order:confirm">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="operateOrder('confirm')">确认</a>
			</shiro:hasPermission>
			<shiro:hasPermission name="purchase:order:reject">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="operateOrder('reject')">驳回</a>
			</shiro:hasPermission>
			</c:if>  
		</div> --%>
		<%-- <div>
			<form id="form-order-search" method="post">
			采购订单的类型：<input class="easyui-combobox" data-options="valueField:'value',textField:'text',url : '${ctx}/manager/basedata/dict/getDict/PURCHASE_ORDER_TYPE',editable:false" style="width:140px;" name="search-EQ_order.bsart" />
			采购订单号：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" style="width:80px;"/>
			创建日期：<input class="easyui-datebox" name="search-GTE_order.aedat" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
						    <input class="easyui-datebox" name="search-LTE_order.aedat" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;<br>
			供应商编码：<input type="text" name="search-LIKE_order.vendor.code" class="easyui-textbox" style="width:80px;"/><>
			供应商名称：<input type="text" name="search-LIKE_order.vendor.name" class="easyui-textbox" style="width:80px;"/>
			交货日期：<input class="easyui-datebox" name="search-GTE_requestTime" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
						    <input class="easyui-datebox" name="search-LTE_requestTime" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;<br>
						
			物料号：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>    
			公司代码：<input type="text" name="search-LIKE_order.company.code" class="easyui-textbox" style="width:80px;"/>
		    工厂代码：<input type="text" name="search-LIKE_factoryEntity.code" class="easyui-textbox" style="width:80px;"/>
			采购组代码：<input type="text" name="search-LIKE_order.purchasingGroup.code" class="easyui-textbox" style="width:80px;"/><br>

			发布状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-全部-</option><option value="0">待发布</option><option value="1">已发布</option><option value="2">部分发布</option></select>
			确认状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-全部-</option><option value="0">待确认</option><option value="1">已确认</option><option value="2">部分确认</option></select>
			发货状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_deliveryStatus"><option value="">-全部-</option><option value="0">待发货</option><option value="1">已发货</option><option value="2">部分发货</option></select>
			收货状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_receiveStatus"><option value="">-全部-</option><option value="0">待收货</option><option value="1">已收货</option><option value="2">部分收货</option></select> --%>
		<!-- 	关闭状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-全部-</option><option value="0">未关闭</option><option value="1">已关闭</option></select> -->
			<%-- <tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-order-search').form('reset')">重置</a> 
			</form>
		</div>
	</div> --%>
	
	<div id="orderListToolbar" style="padding:5px;">
		<div>
			<form id="form-order-search" method="post">
			<div>
				<table style="width: 100%">
					<tr>
					 <c:if test="${vendor == false}">    
						<td>
			<spring:message code="purchase.order.OrderType"/>：<input class="easyui-combobox" data-options="valueField:'value',textField:'text',url : '${ctx}/manager/basedata/dict/getDict/PURCHASE_ORDER_TYPE',editable:false" style="width:140px;" name="search-EQ_order.bsart" />
						</td>
						</c:if>
						<td>
			<spring:message code="purchase.order.PurchaseOrderNumber"/>：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" style="width:140px;"/>
						</td>
						<td colspan="2">
			<spring:message code="purchase.order.CreationDate"/>：<input class="easyui-datebox" name="search-GTE_order.aedat" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;<spring:message code="purchase.order.Go"/>&nbsp;&nbsp;
						    <input class="easyui-datebox" name="search-LTE_order.aedat" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;<br>
						</td>
					</tr>
					<tr>
						<td>
			<spring:message code="purchase.order.vendorCode"/>：<input type="text" name="search-LIKE_order.vendor.code" class="easyui-textbox" style="width:140px;"/><>
						</td>
						<td>
			<spring:message code="purchase.order.VendorName"/>：<input type="text" name="search-LIKE_order.vendor.name" class="easyui-textbox" style="width:140px;"/>
						</td>
						<td colspan="2">
			<spring:message code="purchase.order.DeliveryDate"/>：<input class="easyui-datebox" name="search-GTE_requestTime" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
						    <input class="easyui-datebox" name="search-LTE_requestTime" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;<br>
						</td>
					</tr>
					<tr>
						<td>
			<spring:message code="purchase.order.MaterialNumber"/>：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:140px;"/>    
						</td>
						<td>
			<spring:message code="purchase.order.CompanyCode"/>：<input type="text" name="search-LIKE_order.company.code" class="easyui-textbox" style="width:140px;"/>
						</td>
						<td>
		    <spring:message code="purchase.order.FactoryCode"/>：<input type="text" name="search-LIKE_factoryEntity.code" class="easyui-textbox" style="width:140px;"/>
			
					</td>
						<td>
			<spring:message code="purchase.order.PurchasingGroupCode"/>：<input type="text" name="search-LIKE_order.purchasingGroup.code" class="easyui-textbox" style="width:140px;"/><br>
						</td>
					</tr>
					<tr>
						<td>
			<spring:message code="purchase.order.PublishingState"/>：<select class="easyui-combobox" data-options="editable:false" style="width: 140px;" name="search-EQ_publishStatus"><option value="">-<!-- 全部 --><spring:message code="purchase.order.Whole"/>-</option><option value="0"><!-- 待发布 --><spring:message code="purchase.order.Waitpublisher"/></option><option value="1"><!-- 已发布 --><spring:message code="purchase.order.AlreadyPublished"/></option><option value="2"><spring:message code="purchase.orderMain.orderItemList.PartialRelease"/><!-- 部分发布 --></option></select>
						</td>
						<td>
			<spring:message code="purchase.order.ConfirmStatus"/>：<select class="easyui-combobox" data-options="editable:false" style="width: 140px;" name="search-EQ_confirmStatus"><option value="">-<!-- 全部 --><spring:message code="purchase.order.Whole"/>-</option><option value="-1"><!-- 驳回 --><spring:message code="purchase.order.Reject"/></option><option value="0"><!-- 待确认 --><spring:message code="vendor.toConfirmed"/></option><option value="1"><spring:message code="vendor.confirmed"/><!-- 已确认 --></option><option value="2"><spring:message code="vendor.partConfirmation"/><!-- 部分确认 --></option></select>
						</td>
						<td>
			<spring:message code="purchase.order.ShippingState"/>：<select class="easyui-combobox" data-options="editable:false" style="width: 140px;" name="search-EQ_deliveryStatus"><option value="">-<!-- 全部 --><spring:message code="purchase.order.Whole"/>-</option><option value="0"><!-- 待发货 --><spring:message code="purchase.orderMain.orderItemList.PendingShipment"/></option><option value="1"><!-- 已发货 --><spring:message code="purchase.delivery.AlreadyShipped"/></option><option value="2"><spring:message code="purchase.orderMain.orderList.PartialShipment"/><!-- 部分发货 --></option></select>
					</td>
						<td>
			<spring:message code="purchase.order.ReceivingState"/>：<select class="easyui-combobox" data-options="editable:false" style="width: 140px;" name="search-EQ_receiveStatus"><option value="">-<!-- 全部 --><spring:message code="purchase.order.Whole"/>-</option><option value="0"><!-- 待收货 --><spring:message code="purchase.orderMain.orderItemList.GoodToBeReceived"/></option><option value="1"><!-- 已收货 --><spring:message code="purchase.delivery.AlreadyReceived"/></option><option value="2"><spring:message code="purchase.orderMain.orderList.PartialReceipt"/><!-- 部分收货 --></option></select>
		<!-- 	关闭状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-全部-</option><option value="0">未关闭</option><option value="1">已关闭</option></select> -->
						</td>
					</tr>
					
				<tr>
						<td>
		    <spring:message code="purchase.order.LockState"/>：<select class="easyui-combobox" data-options="editable:false"  style="width: 140px;" name="search-EQ_zfree"><option value="">-<spring:message code="purchase.order.Whole"/>-</option><option value="X"><spring:message code="purchase.order.Locked"/></option><option value="!X"><spring:message code="purchase.order.NotLocked"/></option>  </select>
						</td>
						<td>
			<spring:message code="purchase.order.DeleteState"/>：<select class="easyui-combobox" data-options="editable:false"  style="width: 140px;" name="search-EQ_loekz"><option value="">-<spring:message code="purchase.order.Whole"/>-</option><option value="X"><spring:message code="purchase.order.Deleted"/></option><option value="!X"><spring:message code="purchase.order.NotDeleted"/></option>  </select>
						</td>
						<td >
			<spring:message code="purchase.order.DeliveryState"/>：<select class="easyui-combobox" data-options="editable:false"  style="width: 140px;" name="search-EQ_elikz"><option value="">-<spring:message code="purchase.order.Whole"/>-</option><option value="X"><spring:message code="purchase.order.Delivered"/></option><option value="!X"><spring:message code="purchase.order.Undelivered"/></option>  </select>
						</td>
					</tr>	

				</table>
			<tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			</div>
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder()"><!-- 查询 --><spring:message code="purchase.order.Query"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-order-search').form('reset')"><!-- 重置 --><spring:message code="purchase.order.Reset"/></a> 
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>

	<!-- 供应商驳回窗口 -->
	<div id="win-reject" class="easyui-dialog" title='<!-- 驳回 --><spring:message code="purchase.order.operation"/>' style="width:400px;height:150px"
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
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="rejectOper()"><spring:message code="button.submit"/><!-- 提交 --></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-reject').form('reset')"><spring:message code="button.reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
<script type="text/javascript">
function searchOrder(){
	debugger;
	var searchParamArray = $('#form-order-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-order-list').datagrid('load',searchParams);
}



function rejectOper(){
	var reject_type=document.getElementById("reject_type").value;
	if("orderItem"==reject_type || "orderItemView"==reject_type){
		$.messager.progress();
		$('#form-reject').form('submit',{
			ajax:true,
			iframe: true,    
			url:'${ctx}/manager/order/purchaseorder/rejectOrderItem', 
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
						if("orderItemView"==reject_type){
							//订单详情驳回后刷新dialog
						$dialog.dialog('destroy');//关闭弹出框
						   Order.showOrderPlanDetail(document.getElementById("reject_ids").value,'true');//重新打开，刷新页面
						}
						$('#datagrid-order-list').datagrid('reload'); 
					}else{
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>',obj.message,'error');
					}
				}catch (e) {
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>',e,'error'); 
				} 
			}
		});
	}else if("orderPlan"==reject_type){
		$.messager.progress();
		$('#form-reject').form('submit',{
			ajax:true,
			iframe: true,    
			url:'${ctx}/manager/order/purchaseorder/rejectItemPlan', 
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
						$('#datagrid-order-list').datagrid('reload');
						$('#datagrid-orderitemplan-list').datagrid('reload');
					}else{
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,obj.message,'error');
					}
				}catch (e) {
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
				} 
			}
		});
	}
}

//发布、确认、关闭
function operateOrder(st){
	var selections = $('#datagrid-order-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！ */,'info');
		return false;
	}
	var reject_ids="";
	for(i = 0; i < selections.length; i ++) {
		reject_ids=reject_ids+selections[i].id;
		if(st == "publish" && selections[i].publishStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.ContainsPublishedRecordsAndCannotBeDuplicated"/>'/* 包含已发布记录无法重复发布！ */,'error');
			return false;
		} else if(st == "confirm" && selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.ContainsConfirmedRecordAndCannotBeValidatedRepeatedly"/>'/* 包含已确认记录无法重复确认！ */,'error');
			return false;
		}else if(st == "confirm" && selections[i].deliveryStatus != 0) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.TheRecordContainingShipmentCannotBeConfirmed"/>'/* '包含已发货的记录无法整单确认！ */,'error');
			return false;
		} else if(st == "reject" && selections[i].confirmStatus != 0) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.InclusionOfConfirmedRecordsCannotBeRepeatedRejected"/>'/* 包含已确认记录无法重复驳回！ */,'error');
			return false;
		} else if(st == "confirm" && selections[i].deliveryStatus != 0) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.TheRecordsThatHaveBeenShippedCannotBeRejected"/>'/* 包含已发货的记录无法整单驳回！ */,'error');
			return false;
		}else if(st == "close" && selections[i].closeStatus == 1) {
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.ContainsClosedRecordsAndCannotBeClosedRepeatedly"/>'/* 包含已关闭记录无法重复关闭！ */,'error');
			return false;
		}
	}
	
	//1、驳回  则打开驳回窗口
	if(st == "reject"){
		document.getElementById("reject_ids").value=reject_ids;
		document.getElementById("reject_type").value='orderItem';
		$('#win-reject').window('open');   
	}else{
			//2、其他走后台方法
			$.messager.progress();
			var params = $.toJSON(selections);
			$.ajax({
				url:'${ctx}/manager/order/purchaseorder/' + st + 'OrderItem',
				type:'POST',
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
						}else{
							$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
					} 
				}
			});
	}
}

function sycOrder(){
	$.messager.progress();
	$.ajax({
		url:'${ctx}/manager/order/purchaseorder/sycOrder',
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

var clientWidth = document.body.clientWidth;
var clientHeight = document.body.clientHeight;
function materialProcess(id,isVendor){
	var title='<spring:message code="purchase.order.WorkingProcedure"/>'/* '工序' */;
	new dialogx().showWin(title, clientWidth, clientHeight, ctx
			+ '/manager/order/process/displayProcess/'+id+'/'+isVendor,
			'dialog-material-process');
}
function saveOrderProcess(){
	$.messager.progress();
	$('#datagrid-orderProcess-list').datagrid('acceptChanges'); 
		  
	var rows = $('#datagrid-orderProcess-list').datagrid('getRows');
	  
	if(rows == null || rows.length == 0) {
				$.messager.progress('close');
				return false;
	} 
	
	for(var i=0;i<rows.length;i++){
		if(Number(rows[i].orderNum)>Number(rows[i].orderCount)){
			$.messager.progress('close');
			$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.TheQuantityShouldNotExceedTheOrderQuantity"/>'/* '数量不能大于订单数量' */,'error');
			$('#datagrid-orderProcess-list').datagrid('beginEdit', 0);
			return false;
		}
	}


	
	var o =$('#datagrid-orderProcess-list').datagrid('getData'); 
	var datas = JSON.stringify(o);   

	$("#tableDatas").val(datas);
	

	$('#form-orderProcess').form('submit',{
		ajax:true,
	
		url:ctx+'/manager/order/process/saveOrderProcess',  
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			data = $.evalJSON(data);
			if(data.success){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data.msg,'info');
			}else{
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data.msg,'error');
			}
			$.messager.progress('close');
			
			$('#dialog-material-process').dialog('destroy');
			$('#datagrid-orderProcess-list').datagrid('reload');
			
		}
	});
}


//根据订单明细查发货明细
function showDeliveryItemByOrderItem(orderItemId){
	var clientHeight = document.body.clientHeight;	
	new dialog().showWin('<spring:message code="purchase.order.DetailsOfBillOfDelivery"/>'/* "发货单详情" */, 1000, clientHeight, ctx + '/manager/order/delivery/displayDeliveryItemsByOrderItemIdView/'+orderItemId);
}

</script>
</body>
</html>
