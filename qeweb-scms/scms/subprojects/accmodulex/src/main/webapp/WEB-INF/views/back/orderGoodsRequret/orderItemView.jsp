<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
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
function searchOrder(formId,tableId){
	var _formId = "#"+formId;
	var _tableId = "#"+tableId;
	var searchParamArray = $(_formId).serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$(_tableId).datagrid('load',searchParams);
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
					<td width="25%"><spring:message code="vendor.orderGoodsRequret.purchaseOrderNo."/><!-- 采购订单号 -->  :${po.orderCode }</td>
					<td width="25%"><spring:message code="vendor.orderplan.purchasingGroup"/><!-- 采购组 -->  :${po.purchasingGroup.name }</td>
					<td width="25%"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --> :${po.vendor.code }</td>
					<td width="25%"> <spring:message code="vendor.supplierName"/><!-- 供应商名称 -->:${po.vendor.name }</td>
				</tr>
				<tr>
					<td width="25%"><spring:message code="vendor.creationTime"/><!-- 创建时间 -->  :<fmt:formatDate value="${po.aedat }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
					<td><spring:message code="base.order.publishStatus"/><!-- 发布状态 -->：<c:if test="${po.publishStatus eq 0 }"><spring:message code="status.publish.0"/><!-- 未发布 --></c:if> <c:if test="${po.publishStatus eq 1 }"><spring:message code="status.publish.1"/><!-- 已发布 --></c:if> <c:if test="${po.publishStatus eq 2 }"><spring:message code="status.publish.2"/><!-- 部分发布 --></c:if></td>
					<td><spring:message code="vendor.orderGoodsRequret.releaseDate"/><!-- 发布日期 -->：<fmt:formatDate value="${po.publishTime }" pattern="yyyy-MM-dd HH:mm:ss"/> </td>
					<td><spring:message code="status.confirm"/><!-- 确认状态 -->：<c:if test="${po.confirmStatus eq 0 }"><spring:message code="status.confirm.0"/><!-- 待确认 --></c:if> <c:if test="${po.confirmStatus eq 1 }"><spring:message code="status.confirm.1"/><!-- 已确认 --></c:if> <c:if test="${po.confirmStatus eq 2 }"><spring:message code="status.confirm.2"/><!-- 部分已确认 --></c:if><c:if test="${po.confirmStatus eq -1 }"><spring:message code="vendor.orderplan.reject"/><!-- 驳回 --></c:if></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.orderGoodsRequret.setTime"/><!-- 确认时间 --><!-- 确认时间 -->： <fmt:formatDate value="${po.confirmTime }" pattern="yyyy-MM-dd HH:mm:ss"/>  </td>
					<td><spring:message code="status.delivery"/><!-- 发货状态 -->：<c:if test="${po.deliveryStatus eq 0 }"><spring:message code="status.delivery.0"/><!-- 待发货 --></c:if> <c:if test="${po.deliveryStatus eq 1 }"><spring:message code="status.delivery.1"/><!-- 已发货 --></c:if> <c:if test="${po.deliveryStatus eq 2 }"><spring:message code="status.delivery.2"/><!-- 部分发货 --></c:if></td>
					<td><spring:message code="status.receive"/><!-- 收货状态 -->：<c:if test="${po.receiveStatus eq 0 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 1 }"><spring:message code="status.receive.0"/><!-- 待收货 --></c:if> <c:if test="${po.receiveStatus eq 2 }"><spring:message code="status.receive.2"/><!-- 部分收货 --></c:if></td>
				</tr>
			</table>
		</form>
	</div>
</div>

<!-- 订单的明细 -->
<div style="height: 400px">
	<table id="datagrid-orderItem-list" class="easyui-datagrid"  fit="true" title="订单明细" 
		data-options="url:'${ctx}/manager/order/purchasemainorder/getOrderItemList/${orderId}/${vendor}',method:'post',singleSelect:false,   
		toolbar:'#orderItemListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
	 <thead data-options="frozen:true">
		<tr>
			<th data-options="field:'orderCode',  formatter:function(v,r,i){return r.order.orderCode;}  "><spring:message code="vendor.orderGoodsRequret.purchaseOrderNo."/><!-- 采购订单号 --></th>
			<c:if test="${vendor == 'false '}">  
				<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
	 
		     </c:if>
		     <th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --></th>
		     <th data-options="field:'materialDescride',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/><!-- 物料描述 --></th>
			<th data-options="field:'col3' , formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.outstandingAmount"/><!-- 未清数量 --></th> <!-- 来自收货单后期加上计算 -->	
		</tr>
	</thead>

	<thead>
	   <tr>
		<th data-options="field:'itemNo'"><spring:message code="vendor.orderGoodsRequret.lineItem"/><!-- 行项目 --></th>
		<c:if test="${vendor == 'false '}">
		<th data-options="field:'purOrderType',formatter:function(v,r,i){return r.order.purOrderType.name;}"   ><spring:message code="vendor.orderGoodsRequret.orderType"/><!-- 订单类型 --></th>
		</c:if>
		<th data-options="field:'companyCode' , formatter:function(v,r,i){ if(r.order.company){ return r.order.company.code; }  else{  return '' ;}     }"    ><spring:message code="vendor.companyCode"/><!-- 公司编码 --></th>
		<th data-options="field:'companyName' ,formatter:function(v,r,i){ if(r.order.company){  return r.order.company.name; } else{  return '' ; }  }"><spring:message code="vendor.companyName"/><!-- 公司名称 --></th>
		<th data-options="field:'factoryEntity.code'"><spring:message code="vendor.orderplan.factoryCode"/><!-- 工厂编码 --></th>
		<th data-options="field:'factoryEntity.name'"><spring:message code="vendor.orderplan.factoryName"/><!-- 工厂名称 --></th>
		<th data-options="field:'pstyp'"><spring:message code="vendor.orderGoodsRequret.projectType"/><!-- 项目类型 --></th>
		<c:if test="${vendor != 'false '}">  
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.order.vendor.code;}"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		</c:if>
		 <th data-options="field:'vendorName',formatter:function(v,r,i){return r.order.vendor.name;} "  ><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		<th data-options="field:' ztermms'  , formatter:function(v,r,i){return r.order.ztermms;} "   ><spring:message code="vendor.orderGoodsRequret.termsPayment"/><!-- 付款条件 --></th>
		
		<th data-options="field:'unitName'"><spring:message code="vendor.orderGoodsRequret.orderUnit"/><!-- 订单单位 --></th>   
		<th data-options="field:'orderQty' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.orderQuantity"/><!-- 订单数量 --></th> 
		<c:if test="${vendor == 'false '}">    
		<th data-options="field:'zslsx' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.orderQuantityCeiling"/><!-- 订单数量上限 --></th> 
		</c:if>
		<th data-options="field:'deQty' , formatter:getNumFmt " ><spring:message code="vendor.orderGoodsRequret.deliveryQuantity"/><!-- 送货数量 --></th> <!-- 送货数量=在途数量+收货数量 -->
		
		<th data-options="field:'onwayQty', formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.quantityTransit"/><!-- 在途数量 --></th>
		<th data-options="field:'receiveQty',formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.goodsQuantity"/><!-- 收货数量 --></th>
		
		<th data-options="field:'col1', formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.badQuantityMaterials"/><!-- 来料不良数量 --></th> <!-- 来自收货单后期加上计算 -->
		<th data-options="field:' col2 ' , formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.quantityDefectiveInspection"/><!-- 送检不良数量 --></th> <!-- 来自收货单后期加上计算 -->
			
				<c:if test="${vendor == 'false '}">    
					<th data-options="field:'zwqsl' , formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.SAPnotClear"/><!-- SAP未清数量 --></th> <!-- 来自收货单后期加上计算 -->
					</c:if>
	    <th data-options="field:'undeliveryQty' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.unshippedQuantity"/><!-- 未发货数量 --></th>
	 
  		<th data-options="field:'orderDate',formatter:function(v,r,i){return r.order.aedat;}"><spring:message code="vendor.orderGoodsRequret.dateOrderApproval"/><!-- 订单审批日期 --></th>
  		<th data-options="field:'requestTime'"><spring:message code="vendor.orderGoodsRequret.deliveryDate"/><!-- 交货日期 --></th>   
  		<th data-options="field:'confirmTime'"><spring:message code="vendor.orderGoodsRequret.setTime"/><!-- 确认时间 --></th>  
  		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="vendor.orderplan.confirmStatus"/><!-- 确认状态 --></th>   
  		<th data-options="field:'receiveOrg'"><spring:message code="vendor.orderGoodsRequret.receivingAddress"/><!-- 收货地址 --></th>  
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="vendor.postStatus"/><!-- 发布状态 --></th> 
	 	<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="vendor.orderGoodsRequret.deliveryStatus"/><!-- 发货状态 --></th>  
	    <th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="vendor.orderGoodsRequret.stateGoods"/><!-- 收货状态 --></th>   
	    <th data-options="field:'zlock',formatter:function(v,r,i){return StatusRender.render(v,'lock',false);}"><spring:message code="vendor.orderGoodsRequret.locked"/><!-- 锁定状态 --></th>
	    <c:if test="${vendor == 'false '}">        
	    <th data-options="field:'zsfxp',formatter:function(v,r,i){if(v=='X') return '新品';else return '量产';} "><spring:message code="vendor.orderGoodsRequret.whetherNewproduct"/><!-- 是否新品 --></th> 
	    </c:if>
	     <th data-options="field:'zfree',formatter:function(v,r,i){if(v=='X') return '已免费';else return '未免费';}"><spring:message code="vendor.orderGoodsRequret.freeLogo"/><!-- 免费标识 --></th> 
	     <th data-options="field:'retpo',formatter:function(v,r,i){if(v=='X') return '已退换';else return '未退换';}"><spring:message code="vendor.orderGoodsRequret.returnLogo"/><!-- 退货标识 --></th> 
	     <th data-options="field:'loekz',formatter:function(v,r,i){if(v=='X') return '已删除';else return '未删除';}"><spring:message code="vendor.orderGoodsRequret.deleteLogo"/><!-- 删除标识 --></th> 
	     <th data-options="field:'bstae',formatter:function(v,r,i){if(v=='X') return '否';else return '是';}"><spring:message code="vendor.orderGoodsRequret.identifyDeliveryForm"/><!-- 内向交货单标识 --></th> 
	      
	       <th data-options="field:'lockStatus',formatter:function(v,r,i){if(v=='1') return '已冻结';else return '未冻结';}"><spring:message code="vendor.orderGoodsRequret.frozen"/><!-- 冻结状态 --></th> 
	       <th data-options="field:'elikz',formatter:function(v,r,i){if(v=='X') return '已交付';else return '未交付';}"><spring:message code="vendor.orderGoodsRequret.deliveryStatus"/><!-- 交付状态 --></th> 
		 <th data-options="field:'rejectReason'"><spring:message code="vendor.orderplan.dismissReason"/><!-- 驳回原因 --></th> 
		 <th data-options="field:'bpumn'"><spring:message code="vendor.orderGoodsRequret.numerator"/><!-- 分子 --></th> 
		  <th data-options="field:'bpumz' "><spring:message code="vendor.orderGoodsRequret.denominator"/><!-- 分母 --></th> 
		  <th data-options="field:'lfimg' ,formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.orderQuantity"/><!-- 订单基本数量 --></th> 
		 
		 <th data-options="field:'surBaseQty' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.numberBaseAmount"/><!-- 订单剩余匹配基本数量 --></th> 
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		</tr></thead>
	</table>
	<div id="orderItemListToolbar" style="padding:5px;">
		<div>
			<form id="form-orderItem-search" method="post">
			<spring:message code="vendor.orderGoodsRequret.purchaseOrderNo."/><!-- 采购订单号 -->：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：<input type="text" name="search-LIKE_order.vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：<input type="text" name="search-LIKE_order.vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.postStatus"/><!-- 发布状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toReleased"/><!-- 待发布 --></option><option value="1"><spring:message code="vendor.published"/><!-- 已发布 --></option><option value="2"><spring:message code="vendor.orderplan.partRelease"/><!-- 部分发布 --></option></select>
			<spring:message code="vendor.orderplan.confirmStatus"/><!-- 确认状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toConfirmed"/><!-- 待确认 --></option><option value="1"><spring:message code="vendor.confirmed"/><!-- 已确认 --></option><option value="-1"><spring:message code="vendor.orderplan.reject"/><!-- 驳回 --></option><option value="-2"><spring:message code="vendor.orderplan.refusedDismiss"/><!-- 拒绝驳回 --></option></select><br>
			<spring:message code="vendor.orderGoodsRequret.deliveryStatus"/><!-- 发货状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_deliveryStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.orderGoodsRequret.awaitingDelivery"/><!-- 待发货 --></option><option value="1"><spring:message code="vendor.orderGoodsRequret.hasShipped"/><!-- 已发货 --></option><option value="2"><spring:message code="vendor.orderGoodsRequret.partShipment"/><!-- 部分发货 --></option></select>
			<spring:message code="vendor.orderGoodsRequret.stateGoods"/><!-- 收货状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_receiveStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.orderGoodsRequret.forGoods"/><!-- 待收货 --></option><option value="1"><spring:message code="vendor.orderGoodsRequret.haveGoods"/><!-- 已收货 --></option><option value="2"><spring:message code="vendor.orderGoodsRequret.partGoods"/><!-- 部分收货 --></option></select>
		       <spring:message code="vendor.orderGoodsRequret.closedPosition"/><!-- 关闭状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.orderGoodsRequret.stayClose"/><!-- 待关闭 --></option><option value="1"><spring:message code="vendor.closed"/><!-- 已关闭 --></option></select>
			<tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder('form-orderItem-search','datagrid-orderItem-list')"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-orderItem-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a> 
			</form>
		</div>
	</div>
</div>


