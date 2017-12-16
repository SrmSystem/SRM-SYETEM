<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%-- <%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %> --%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   
<c:set var="vendor" value="${vendor} "/>   
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

//ASN列格式化
function asnFmt(v,r,i){
	 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showDeliveryItemByOrderItem('+ r.id +');">ASN</a>'; 
}

//千分位，整数
function getNumIntegerFmt(v,r,i) {
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
   
      // 对整数部分进行千分位格式化.  
      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

      return (((sign)?'':'-') + num);  
   
}




//根据订单明细查发货明细
function showDeliveryItemByOrderItem(orderItemId){
	var clientHeight = document.body.clientHeight;	
	new dialog().showWin('<spring:message code="purchase.order.DetailsOfBillOfDelivery"/>'/* "发货单详情" */, 1000, clientHeight, ctx + '/manager/order/delivery/displayDeliveryItemsByOrderItemIdView/'+orderItemId);
}

function operateFmt(v,r,i){
	var isVendor = $("#isVendor").val();
	isVendor= isVendor.replace(/(^\s+)|(\s+$)/g, "");
	  var s="";
      if(r.zlock == 1){
    	  return  s;
	  }else{
		  s=' <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="feedback.showFeedback(\'/manager/order/purchaseorder/feedback/'+ r.id +'\');"><spring:message code="purchase.order.Feedback"/>【'+r.feedbackCount+'】</a>'/* 反馈 */;
		  s=s+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="Order.showOrderPlanDetail('+ r.id +','+isVendor+');"><spring:message code="purchase.orderMain.orderItemList.DetailedDeliveryplan"/></a>'/* 详细送货计划 */;
		 
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
<!-- 主订单的明细 -->
<div style="padding:5px;">
   <input id="isVendor" name="isVendor" type="hidden"  value="${vendor}"/>
    <div>
		<%-- <c:if test="${vendor == false}">  
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOneOrder(${po.orderCode})">同步订单</a>
		</c:if> --%>
	</div>



	<div class="easyui-panel" data-options="fit:true">
		<form id="form-purchaseorderitem-search">
			<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td width="25%"> <spring:message code="purchase.orderMain.PurchaseOrderNumber"/>
					 :${po.orderCode }</td>
					<td width="25%"><spring:message code="purchase.orderMain.orderItemList.PurchasingTeam"/>  :${po.purchasingGroup.name }</td>
					<td width="25%"><spring:message code="purchase.orderMain.orderItemList.vendorCode"/> :${po.vendor.code }</td>
					<td width="25%"> <spring:message code="purchase.orderMain.orderItemList.VendorName"/>:${po.vendor.name }</td>
				</tr>
				<tr>
					<td width="25%"><spring:message code="purchase.orderMain.orderItemList.CreateTime"/>  :<fmt:formatDate value="${po.aedat }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><spring:message code="base.order.publishStatus"/><!-- 发布状态 -->：<c:if test="${po.publishStatus eq 0 }"><spring:message code="status.publish.0"/><!-- 未发布 --></c:if> <c:if test="${po.publishStatus eq 1 }"><spring:message code="status.publish.1"/><!-- 已发布 --></c:if> <c:if test="${po.publishStatus eq 2 }"><spring:message code="status.publish.2"/><!-- 部分发布 --></c:if></td>
					<td><spring:message code="purchase.orderMain.orderItemList.ReleaseDate"/>：<fmt:formatDate value="${po.publishTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
					<td><spring:message code="status.confirm"/><!-- 确认状态 -->：<c:if test="${po.confirmStatus eq 0 }"><spring:message code="status.confirm.0"/><!-- 待确认 --></c:if> <c:if test="${po.confirmStatus eq 1 }"><spring:message code="status.confirm.1"/><!-- 已确认 --></c:if> <c:if test="${po.confirmStatus eq 2 }"><spring:message code="status.confirm.2"/><!-- 部分已确认 --></c:if><c:if test="${po.confirmStatus eq -1 }"><spring:message code="purchase.orderMain.orderItemList.Reject"/></c:if></td>
				</tr>
				<tr>
					<td><spring:message code="purchase.orderMain.orderItemList.ConfirmTime"/><!-- 确认时间 -->： <fmt:formatDate value="${po.confirmTime }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
					<td><spring:message code="status.delivery"/><!-- 发货状态 -->：<c:if test="${po.deliveryStatus eq 0 }"><spring:message code="status.delivery.0"/><!-- 待发货 --></c:if> <c:if test="${po.deliveryStatus eq 1 }"><spring:message code="status.delivery.1"/><!-- 已发货 --></c:if> <c:if test="${po.deliveryStatus eq 2 }"><spring:message code="status.delivery.2"/><!-- 部分发货 --></c:if></td>
					<td><spring:message code="status.receive"/><!-- 收货状态 -->：<c:if test="${po.receiveStatus eq 0 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 1 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 2 }"><spring:message code="status.receive.2"/><!-- 部分收货 --></c:if></td>
				</tr>
			</table>
		</form>
	</div>
</div>

<!-- 订单的明细 -->
<div style="height: 400px">
	<table id="datagrid-orderItem-list" class="easyui-datagrid"  fit="true" title="<spring:message code="purchase.orderMain.orderItemList.OrderDetails"/>" 
		data-options="url:'${ctx}/manager/order/purchasemainorder/getOrderItemList/${orderId}/${vendor}',method:'post',singleSelect:false,   
		toolbar:'#orderItemListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead data-options="frozen:true">
		<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<th data-options="field:'operate',formatter:operateFmt, width: 150"><spring:message code="purchase.orderMain.orderItemList.operation"/></th>

					<th data-options="field:'orderCode',formatter:orderCodeItemFmt"><spring:message code="purchase.orderMain.PurchaseOrderNumber"/></th>
					<c:if test="${vendor == 'false '}">  
					<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><spring:message code="purchase.orderMain.orderItemList.vendorCode"/></th>
		       
		           </c:if>
		           <th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="purchase.orderMain.orderItemList.MaterialNumber"/></th>
		            <th data-options="field:'materialDescride',formatter:function(v,r,i){return r.material.name;}"><spring:message code="purchase.orderMain.orderItemList.MaterialDescription"/></th>
		          	<th data-options="field:'col3' , formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.NumberNotClear"/></th> <!-- 来自收货单后期加上计算 -->	
				</tr>
		</thead>

		<thead>
		       <tr>
				<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="purchase.orderMain.orderItemList.ConfirmStatus"/></th> 
				<th data-options="field:'itemNo'"><spring:message code="purchase.orderMain.orderItemList.LineItem"/></th>
				<c:if test="${vendor == 'false '}">  
				<th data-options="field:'purOrderType',formatter:function(v,r,i){return r.order.purOrderType.name;}"   ><spring:message code="purchase.orderMain.orderItemList.OrderType"/></th>
				</c:if>
				<th data-options="field:'asn',formatter:asnFmt">ASN</th>
				<th data-options="field:'companyCode' , formatter:function(v,r,i){ if(r.order.company){ return r.order.company.code; }  else{  return '' ;}     }"    ><spring:message code="purchase.orderMain.orderItemList.CompanyCode"/></th>
				<th data-options="field:'companyName' ,formatter:function(v,r,i){ if(r.order.company){  return r.order.company.name; } else{  return '' ; }  }"><spring:message code="purchase.orderMain.orderItemList.CompanyName"/></th>
				<th data-options="field:'factoryEntity.code'"><spring:message code="purchase.orderMain.orderItemList.FactoryCode"/></th>
				<th data-options="field:'factoryEntity.name'"><spring:message code="purchase.orderMain.orderItemList.FactoryName"/></th>
				<th data-options="field:'pstyp'"><spring:message code="purchase.orderMain.orderItemList.ItemType"/></th>
				<th data-options="field:' ztermms'  , formatter:function(v,r,i){return r.order.ztermms;} "   ><spring:message code="purchase.orderMain.orderItemList.TermOfPayment"/></th>
			     <c:if test="${vendor != 'false '}">  
					<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><spring:message code="purchase.orderMain.orderItemList.vendorCode"/></th>
		          </c:if>
		              <th data-options="field:'vendorName',formatter:function(v,r,i){return r.order.vendor.name;} "  ><spring:message code="purchase.orderMain.orderItemList.VendorName"/></th>
				<th data-options="field:'unitName'"><spring:message code="purchase.orderMain.orderItemList.OrderUnit"/></th>   
				<th data-options="field:'orderQty' ,formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.OrderNumber"/></th> 
				<c:if test="${vendor == 'false '}">  
				<th data-options="field:'zslsx' ,formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.OrderLimitNuber"/></th> 
				</c:if>
				
				<th data-options="field:'deQty' ,  formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.DeliveryNumber"/></th> <!-- 送货数量=在途数量+收货数量 -->
				
				<th data-options="field:'onwayQty' , formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.UnderWayNumber"/></th>
				<th data-options="field:'receiveQty' ,formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.ReceiptNumber"/></th>
				
				<th data-options="field:'col1' , formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.BadQuantityNub"/></th> <!-- 来自收货单后期加上计算 -->
				<th data-options="field:' col2 ' , formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.BadInspectionQuantity"/></th> <!-- 来自收货单后期加上计算 -->
				
					<c:if test="${vendor == 'false '}">  
					<th data-options="field:'zwqsl' , formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.SPANumberNotClear"/></th> <!-- 来自收货单后期加上计算 -->
						</c:if>
				
			    <th data-options="field:'undeliveryQty', formatter:getNumFmt "><spring:message code="purchase.orderMain.orderItemList.QuantityNotDelivered"/></th>
		<!-- 	    <th data-options="field:'currency'">币种</th> -->
			  
		  		<th data-options="field:'orderDate',formatter:function(v,r,i){return r.order.aedat;}"><spring:message code="purchase.orderMain.orderItemList.OrderApprovalDate"/></th>
		  		<th data-options="field:'requestTime'"><spring:message code="purchase.orderMain.orderItemList.DeliveryDate"/></th>   
		  		<th data-options="field:'confirmTime'"><spring:message code="purchase.orderMain.orderItemList.ConfirmTime"/></th>  
		  	  
		  		<th data-options="field:'receiveOrg'"><spring:message code="purchase.orderMain.orderItemList.ReceivingAddress"/></th>  
				<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="purchase.orderMain.orderItemList.PublishingState"/></th> 
			 	<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="purchase.orderMain.orderItemList.ShippingState"/></th>  
			    <th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="purchase.orderMain.orderItemList.ReceivingState"/></th>   
			     <th data-options="field:'zlock',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.orderItemList.Locked"/>';else return '<spring:message code="purchase.orderMain.orderItemList.NotLocked"/>';}"><spring:message code="purchase.orderMain.orderItemList.LockState"/></th>   
			     <c:if test="${vendor == 'false '}">  
			     <th data-options="field:'zsfxp',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.orderItemList.NewProduct"/>';else return '<spring:message code="purchase.orderMain.orderItemList.BatchProduction"/>';} "><spring:message code="purchase.orderMain.orderItemList.WhetherNewProduct"/></th> 
			     </c:if>
			     <th data-options="field:'zfree',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.orderItemList.AlreadyFree"/>';else return '<spring:message code="purchase.orderMain.orderItemList.NotFree"/>';} "><spring:message code="purchase.orderMain.orderItemList.FreeLogo"/></th> 
			     <th data-options="field:'retpo',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.orderItemList.ReturnedProduct"/>';else return '<spring:message code="purchase.orderMain.orderItemList.NotRetrunedProduct"/>';}"><spring:message code="purchase.orderMain.orderItemList.ReturnGoodsLogo"/></th> 
			     <th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.orderItemList.Deleted"/>';else return '<spring:message code="purchase.orderMain.orderItemList.NotDeleted"/>';}"><spring:message code="purchase.orderMain.orderItemList.DeleteLogo"/></th> 
			     <th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.orderItemList.NO"/>';else return '<spring:message code="purchase.orderMain.orderItemList.YES"/>';}"><spring:message code="purchase.orderMain.orderItemList.InwardDeliveryLogo"/></th> 
			     
		<!-- 	      <th data-options="field:'frgke',formatter:function(v,r,i){     }">审批状态</th>  -->
			      
			       <th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '<spring:message code="purchase.orderMain.orderItemList.AlreadyFrozen"/>';else return '<spring:message code="purchase.orderMain.orderItemList.NotFrozen"/>';}"><spring:message code="purchase.orderMain.orderItemList.FrozenState"/></th> 
			       <th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '<spring:message code="purchase.orderMain.orderItemList.Delivered"/>';else return '<spring:message code="purchase.orderMain.orderItemList.Undelivered"/>';}"><spring:message code="purchase.orderMain.orderItemList.DeliveryState"/></th> 
			     
				 <th data-options="field:'rejectReason'"><spring:message code="purchase.orderMain.orderItemList.ReasonsForRejection"/></th> 
				 
				  <th data-options="field:'bpumn'"><spring:message code="purchase.orderMain.orderItemList.Molecule"/></th> 
				  <th data-options="field:'bpumz' "><spring:message code="purchase.orderMain.orderItemList.Denominator"/></th> 
				  <th data-options="field:'lfimg' ,formatter:getNumFmt"><spring:message code="purchase.orderMain.orderItemList.BasicOrderNumber"/></th> 
				 <th data-options="field:'surBaseQty' ,formatter:getNumFmt"><spring:message code="purchase.orderMain.orderItemList.BasicNumberOfOrderResidualMatching"/></th> 
				<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		</tr></thead>
	</table>
	<div id="orderItemListToolbar" style="padding:5px;">
		<div>
			<c:if test="${vendor == 'false '}">  
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="unVetoOrderItems()"><spring:message code="purchase.orderMain.orderItemList.Agree"/></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vetoOrderItems() " ><spring:message code="purchase.orderMain.orderItemList.RefuseReject"/></a><!-- //采购商的驳回  -->
				<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="publishOrderItems()">发布</a> -->
			</c:if>  
			<c:if test="${vendor != 'false '}">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="orderOpt.operateOrder('datagrid-orderItem-list','confirm','orderItem')"><spring:message code="purchase.orderMain.orderItemList.Confirm"/></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="rejectOrderItems() "><spring:message code="purchase.orderMain.orderItemList.Reject"/></a><!-- //供应商的驳回    rejectOrderItems  -->
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="exportExcel (${orderId},${vendor})"><spring:message code="purchase.orderMain.orderItemList.Export"/></a>
			</c:if>  
		</div>
		<div>
			<form id="form-orderItem-search" method="post">
			<spring:message code="purchase.orderMain.PurchaseOrderNumber"/>：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.orderItemList.vendorCode"/>：<input type="text" name="search-LIKE_order.vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.orderItemList.VendorName"/>：<input type="text" name="search-LIKE_order.vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.orderMain.orderItemList.PublishingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderItemList.PendingRelease"/></option><option value="1"><spring:message code="purchase.orderMain.orderItemList.AlreadyPublished"/></option><option value="2"><spring:message code="purchase.orderMain.orderItemList.PartialRelease"/><spring:message code="purchase.orderMain.orderItemList.ReleaseDate"/></option></select>
			<spring:message code="purchase.orderMain.orderItemList.ConfirmStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderItemList.ToBeConfirmed"/></option><option value="1"><spring:message code="purchase.orderMain.orderItemList.AlreadyConfirmed"/></option><option value="-1"><spring:message code="purchase.orderMain.orderItemList.Reject"/><spring:message code="purchase.orderMain.orderItemList.Reject"/></option><option value="-2"><spring:message code="purchase.orderMain.orderItemList.RefuseReject"/></option></select><br>
			<spring:message code="purchase.orderMain.orderItemList.ShippingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_deliveryStatus"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderItemList.PendingShipment"/></option><option value="1"><spring:message code="purchase.orderMain.orderItemList.AlreadyShipped"/></option><option value="2"><spring:message code="purchase.orderMain.orderItemList.PartialShipment"/><spring:message code="purchase.orderMain.orderItemList.ReleaseDate"/></option></select>
			<spring:message code="purchase.orderMain.orderItemList.ReceivingState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_receiveStatus"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderItemList.GoodToBeReceived"/></option><option value="1"><spring:message code="purchase.orderMain.orderItemList.AlreadyReceived"/></option><option value="2"><spring:message code="purchase.orderMain.orderItemList.PartialReceipt"/><spring:message code="purchase.orderMain.orderItemList.ReleaseDate"/></option></select>
			<spring:message code="purchase.orderMain.orderItemList.OffState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="0"><spring:message code="purchase.orderMain.orderItemList.ToBeClose"/></option><option value="1"><spring:message code="purchase.orderMain.orderItemList.AlreadyClosed"/></option></select>
			
			<spring:message code="purchase.orderMain.orderItemList.LockState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_zfree"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="X"><spring:message code="purchase.orderMain.orderItemList.Locked"/></option><option value="!X"><spring:message code="purchase.orderMain.orderItemList.NotLocked"/></option>  </select>
			<spring:message code="purchase.orderMain.orderItemList.DeleteState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_loekz"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="X"><spring:message code="purchase.orderMain.orderItemList.Deleted"/></option><option value="!X"><spring:message code="purchase.orderMain.orderItemList.NotDeleted"/></option>  </select>
			<spring:message code="purchase.orderMain.orderItemList.DeliveryState"/>：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_elikz"><option value="">-<spring:message code="purchase.orderMain.orderItemList.Whole"/>-</option><option value="X"><spring:message code="purchase.orderMain.orderItemList.Delivered"/></option><option value="!X"><spring:message code="purchase.orderMain.orderItemList.Undelivered"/></option>  </select>
			<tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder('form-orderItem-search','datagrid-orderItem-list')"><spring:message code="purchase.orderMain.orderItemList.Query"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-orderItem-search').form('reset')"><spring:message code="purchase.orderMain.orderItemList.Reset"/></a> 
			</form>
		</div>
	</div>
</div>


	
	<!-- 采购商确认驳回窗口 -->
	<div id="win-veto" class="easyui-dialog" title="<spring:message code="purchase.orderMain.orderItemList.SupplierRejectsConfirmation"/>" style="width:600px;height:350px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div id="" style="padding:5px;">
			<form id="form-veto" method="post" 	>
			  <input id="veto_type" name="veto_type" type="hidden"/>
				<input id="veto_ids" name="veto_ids" type="hidden"/>
				<table>
					<tr class="border-none">
						<td>
						<label class="common-label"><spring:message code="purchase.orderMain.orderItemList.vendorCode"/>：</label><label  id="ve-code"></label>
						</td>
						<td>
						<label class="common-label"><spring:message code="purchase.orderMain.orderItemList.VendorName"/>：</label><label   id="ve-name"></label>
						</td>
					</tr>

					<tr id="veto-detail" style="display: black;" class="border-none">
						<td colspan="2">
						<label class="common-label" style="float: left;"><spring:message code="purchase.orderMain.orderItemList.DescriptionOfReasonsForRejection"/>:</label>
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
				<td ><spring:message code="purchase.orderMain.orderItemList.NumberOfOrder"/>:</td>
				<td>
				<input  id ="oldOrderQty"   name="orderQty"  type = "hidden" class="easyui-textbox" data-options="required:true" />
					<input  id ="orderQty"   name="orderQty" value="" class="easyui-textbox" data-options="required:true" />
				</td>
			</tr>
			</table>
		</form>
		<div id="dialog-adder-time">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="saveQry()"><spring:message code="purchase.orderMain.orderItemList.Submit"/></a> <a
				href="javascript:;" class="easyui-linkbutton"
				onclick="$('#form-qry-addoredit').form('reset')"><spring:message code="purchase.orderMain.orderItemList.Reset"/></a>
		</div>
	</div>


<script type="text/javascript">
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

function exportExcel(orderId,vendor){
     $('#form-orderItem-search').form('submit',{
 	   url:'${ctx}/manager/order/purchasemainorder/exportExcel/'+orderId+"/"+ vendor,
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

