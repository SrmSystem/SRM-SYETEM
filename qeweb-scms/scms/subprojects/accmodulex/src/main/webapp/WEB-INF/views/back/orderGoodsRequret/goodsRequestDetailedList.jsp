<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   
<c:set var="vendor" value="${vendor} "/> 

<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
  
<script type="text/javascript">

$(function() { 
	var tmp =  ${vendor};
		$('#datagrid-goodsRequestItem-list').datagrid({
			rowStyler:function(index,row){
				if (row.isRed == 1){
					return 'background-color:red;color:black;';
				}
			}
		});
});

function operateFmt(v,r,i){
	  var s="";
 	 var isVendor = $("#isVendor").val();
	 isVendor= isVendor.replace(/(^\s+)|(\s+$)/g, "");
	  //删除（出现四中状态的（isRed）和无任何asn 的要货计划生成的供货计划（isDelete） 可以删除 ）
	 
	  if(r.isDelete == 1 &&  isVendor == "false"  && r.isRed == 1){
		  //修改要货计划数量并且释放订单资源
		  s='&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="deleteOrderItemPlan('+r.id+');">'+'<spring:message code="vendor.deleting"/></a>'; /* 删除 */
	  }else if(r.isDelete == 0 &&  isVendor == "false"  && r.isRed == 1){
		  s='&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="notDeleteOrderItemPlan('+r.id+');">'+'<spring:message code="vendor.deleting"/></a>'; /* 删除 */
	  }

	  return s;
}

//采购主订单格式化编码
function orderCodeFmt(v,r,i){
	 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showOrderItemList('+ r.order.id +','+${vendor}+');">' + r.order.orderCode + '</a>'; 

}


function getNumFmt(v,r,i) {
	
		if(v=="" || v==null || v == "0.000" || v==0.000){
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

var editIndex = undefined;
function onClickRow2(index, data) {
	 var rows = $('#datagrid-goodsRequestMain-list').datagrid('getRows');
	 var i;
	 for(i = 0;i < rows.length;i++) {
		 editRow(i,rows[i]);
	 }
	
}


function editRow(index, data) {
	
	$('#datagrid-goodsRequestMain-list').datagrid('beginEdit', index);
	editIndex = index;
	
	var dhsl = $('#datagrid-goodsRequestMain-list').datagrid('getEditor', {
		index : index,
		field : 'dhsl'
	});
}


</script>
 <input id="isVendor" name="isVendor" type="hidden"  value="${vendor}"/>
<%-- <!-- 要货计划的详细 -->
<div style="height: 300px">
   <table id="datagrid-goodsRequestMain-list" class="easyui-datagrid"  fit="true" title="要货计划" 
		data-options="url:'${ctx}/manager/order/goodsRequest/getRequestList/${vendor}?flag=${flag}&factoryId=${factoryId}&purchasingGroupId=${purchasingGroupId}&materialId=${materialId}&meins=${meins}&ysts=${ysts}&shpl=${shpl}&vendorId=${vendorId}&type=${type}&beginRq=${beginRq}&endRq=${endRq}',method:'post',singleSelect:false,   
		<c:if test="${vendor == 'false '}">  
			 toolbar:'#goodsRequestMainListToolbar',
			 onClickRow: onClickRow2,
		</c:if>
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'factory.name' , formatter:function(v,r,i){return r.factory.name;} " ><spring:message code="vendor.orderplan.factoryName"/><!-- 工厂名称 --> </th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --></th>  
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/><!-- 物料描述 --></th>  
		<th data-options="field:'vendor.name' , formatter:function(v,r,i){return r.vendor.name;}    "><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		<th data-options="field:'vendor.code' ,formatter:function(v,r,i){return r.vendor.code;}  "><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'dhsl' ,editor:{type:'numberbox',options:{required:true,min:0,precision:'3'}},formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.cargoQuantity"/><!-- 要货数量 --></th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="vendor.postStatus"/><!-- 发布状态 --></th>  
		<th data-options="field:'vendorConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="vendor.orderplan.confirmStatus"/><!-- 确认状态 --></th>  
		<th data-options="field:'rq'  , formatter:function(v,r,i){return r.rq;}  "><spring:message code="vendor.orderGoodsRequret.expectedDateArrival"/><!-- 预计到货日期 --></th>
		<th data-options="field:'ysts'  , formatter:function(v,r,i){return r.ysts;}   "  ><spring:message code="vendor.orderGoodsRequret.logisticsDays"/><!-- 物流天数 --></th>
		<th data-options="field:'surQry' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.remainingMatchingBaseAmount"/><!-- 剩余匹配基本数量  --></th>
		</tr></thead>
	</table>
	<c:if test="${vendor == 'false '}"> 
	<div id="goodsRequestMainListToolbar" style="padding:5px;">
		<div>
		   <form id="form-goodsRequestMain-modify" method="post" enctype="multipart/form-data">
		<!-- 采购员菜单 -->
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="editgoodsQry();"><spring:message code="vendor.modification"/></a><!-- 修改 -->
		   </form>
		</div>
	</div>	
	</c:if>
</div> --%>

<!-- 要货计划的明细 -->
<div style="height: 300px">
	<table id="datagrid-goodsRequestItem-list" class="easyui-datagrid"  fit="true" title="要货计划明细" 
		data-options="url:'${ctx}/manager/order/goodsRequest/getItemList/${vendor}?flag=${flag}&factoryId=${factoryId}&purchasingGroupId=${purchasingGroupId}&materialId=${materialId}&meins=${meins}&vendorId=${vendorId}&type=${type}&beginRq=${beginRq}&endRq=${endRq}',method:'post',singleSelect:false,   
		toolbar:'#goodsRequestItemListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'operate',formatter:operateFmt, width: 80"><spring:message code="vendor.operation"/><!-- 操作 --></th>
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="vendor.orderplan.confirmStatus"/><!-- 确认状态 --></th>  
		<th data-options="field:'rejectReason'"><spring:message code="vendor.orderGoodsRequret.unsatisfiedCause"/><!-- 不满足原因 --></th>
		<th data-options="field:'orderCode',formatter:orderCodeFmt"><spring:message code="vendor.orderGoodsRequret.purchaseOrderNo."/><!-- 采购订单号 --></th>
		<th data-options="field:'groupName' , formatter:function(v,r,i){return r.purchaseGoodsRequest.group.name;} " ><spring:message code="vendor.orderplan.purchasingGroup"/><!-- 采购组 --></th>
		<th data-options="field:'itemNo'"><spring:message code="vendor.orderGoodsRequret.lineNumbers"/><!-- 行号 --></th>
		<th data-options="field:'buyerCode',formatter:function(v,r,i){return r.purchaseGoodsRequest.factory.name;}"><spring:message code="vendor.orderplan.factoryName"/><!-- 工厂名称 --></th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.purchaseGoodsRequest.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --></th>  
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.purchaseGoodsRequest.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/><!-- 物料描述 --></th>  
		
		<th data-options="field:'baseQty' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.matchingBaseQuantity"/><!-- 匹配基本数量 --></th>
		<th data-options="field:'unitName', formatter:function(v,r,i){return r.purchaseGoodsRequest.meins;}   "><spring:message code="vendor.orderGoodsRequret.basicUnit"/><!-- 基本单位 --></th>
		<th data-options="field:'orderQty' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.matchingOrderQuantity"/><!-- 匹配订单数量 --></th>
		<th data-options="field:'unitName1', formatter:function(v,r,i){return r.orderItem.meins;}   "><spring:message code="vendor.orderGoodsRequret.orderUnit"/><!-- 订单单位 --></th>
		
		<th data-options="field:'rq'  , formatter:function(v,r,i){return r.purchaseGoodsRequest.rq;}  "><spring:message code="vendor.orderGoodsRequret.expectedDateArrival"/><!-- 预计到货日期 --></th>
		<th data-options="field:'ysts'  , formatter:function(v,r,i){return r.purchaseGoodsRequest.ysts;}   "  ><spring:message code="vendor.orderGoodsRequret.logisticsDays"/><!-- 物流天数 --></th>
		<th data-options="field:'shpl'  , formatter:function(v,r,i){return r.purchaseGoodsRequest.shpl;}   "  ><spring:message code="vendor.buyerBoard.deliveryFrequency"/><!-- 送货频率 --></th>
		<th data-options="field:'vendor.name' , formatter:function(v,r,i){return r.purchaseGoodsRequest.vendor.name;}    "><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		<th data-options="field:'vendor.code' ,formatter:function(v,r,i){return r.purchaseGoodsRequest.vendor.code;}  "><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="vendor.postStatus"/><!-- 发布状态 --></th>  
		<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="vendor.orderGoodsRequret.deliveryStatus"/><!-- 发货状态 --></th> 
		<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="vendor.orderGoodsRequret.stateGoods"/><!-- 收货状态 --></th> 
		</tr></thead>
	</table>
	<div id="goodsRequestItemListToolbar" style="padding:5px;">
		<div>
		<!-- 采购员菜单 -->
			<c:if test="${vendor == 'false '}">  
<!-- 				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="unVetoGoodsRequest()">同意</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="vetoGoodsRequest() " >拒绝驳回</a> -->
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="publishGoodsRequest()"><spring:message code="vendor.posted"/><!-- 发布 --></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="exportExcel(${goodsId},${vendor})"><spring:message code="vendor.orderplan.derivation"/><!-- 导出 --></a>
			</c:if> 
		<!-- 供应商  -->
		<c:if test="${vendor != 'false '}">  
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="confirmGoodsRequest()"><spring:message code="vendor.orderGoodsRequret.satisfaction"/><!-- 满足 --></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="rejectGoodsRequest()"><spring:message code="vendor.orderGoodsRequret.notMeet"/><!-- 不满足 --></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="exportExcel(${vendor})"><spring:message code="vendor.orderplan.derivation"/><!-- 导出 --></a>
			</c:if>  
		
		</div>
		<div>
			<form id="form-goodsRequestItem-search" method="post">
			<spring:message code="vendor.orderNumber"/><!-- 订单号 --> ：<input type="text" name="search-LIKE_order.orderCode" class="easyui-textbox" style="width:80px;"/>
		    <spring:message code="vendor.orderplan.purchasingGroup"/><!-- 采购组  -->：  <input type="text" name="search-LIKE_purchaseGoodsRequest.group.name" class="easyui-textbox" style="width:80px;"/>
		    <spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --> ：  <input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
		    <spring:message code="vendor.orderplan.confirmStatus"/><!-- 确认状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_confirmStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toConfirmed"/><!-- 待确认 --></option><option value="1"><spring:message code="vendor.confirmed"/><!-- 已确认 --></option></select>
			<c:if test="${vendor == 'false '}">  
			<spring:message code="vendor.postStatus"/><!-- 发布状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toReleased"/><!-- 待发布 --></option><option value="1"><spring:message code="vendor.published"/><!-- 已发布 --></option></select>
			</c:if> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchaseItemPlan()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a> 
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-goodsRequestItem-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>   
			</form>
		</div>
	</div>
</div>

	<!-- 发货单 -->
	<div  style="height: 200px">
			<table id="datagrid-purchasedelivery-list" title="发货单详情" class="easyui-datagrid" fit="true"
				data-options="url:'${ctx}/manager/order/delivery/byRequestId?flag=${flag}&factoryId=${factoryId}&purchasingGroupId=${purchasingGroupId}&materialId=${materialId}&meins=${meins}&vendorId=${vendorId}&type=${type}&beginRq=${beginRq}&endRq=${endRq}',method:'post',singleSelect:false,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'deliveryCode',formatter:function(v,r,i){return r.delivery.deliveryCode;}"><spring:message code="vendor.orderGoodsRequret.ASNinvoiceNo"/><!-- ASN发货单号 --></th>
				<th data-options="field:'itemNo'"><spring:message code="vendor.orderGoodsRequret.lineNumbers"/><!-- 行号 --></th>
				<th data-options="field:'orderCode'"><spring:message code="vendor.orderGoodsRequret.purchaseOrderNo."/><!-- 采购订单号 --></th>
				<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
				<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
				<th data-options="field:'orderQty',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.cargoQuantity"/><!-- 要货数量 --></th> 
				<th data-options="field:'deliveryQty'"><spring:message code="vendor.orderGoodsRequret.deliveryNumber"/><!-- 发货数量 --></th>   
				<th data-options="field:'standardBoxNum',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.bulkpackQuantity"/><!-- 大包装数量 --></th>
				<th data-options="field:'boxNum',formatter:getNumIntegerFmt"><spring:message code="vendor.orderGoodsRequret.bulkPack"/><!-- 大包装总数 --></th>
				<th data-options="field:'minPackageQty',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.smallPackageQuantity"/><!-- 小包装数量 --></th>  
				<th data-options="field:'minBoxNum',formatter:getNumIntegerFmt"><spring:message code="vendor.orderGoodsRequret.totalNumberSmallpackages"/><!-- 小包装总数 --></th>
				
				<th data-options="field:'requestTime'"><spring:message code="vendor.orderGoodsRequret.askDeliveryTime"/><!-- 要求到货时间 --></th>   
				<th data-options="field:'manufactureDate'"><spring:message code="vendor.orderGoodsRequret.productionDate"/><!-- 生产日期 --></th> 
				<th data-options="field:'version'"><spring:message code="vendor.orderGoodsRequret.edition"/><!-- 版本 --></th>   
				<th data-options="field:'charg'"><spring:message code="vendor.orderGoodsRequret.batchNumber"/><!-- 批号 --></th> 
				<th data-options="field:'vendorCharg'"><spring:message code="vendor.orderGoodsRequret.tracesBatchNumber"/><!-- 追溯批号 --></th> 
					
				<th data-options="field:'meins'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
				<th data-options="field:'remark'"><spring:message code="vendor.remark"/><!-- 备注 --></th>    
				<th data-options="field:'inspectionReport',width:250,formatter:inspectionReportViewFmt"><spring:message code="vendor.orderGoodsRequret.inspectionReport"/><!-- 检验报告 --></th>   
				<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}"><spring:message code="vendor.orderGoodsRequret.deliveryStatus"/><!-- 发货状态 --></th>   
				<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}"><spring:message code="vendor.orderGoodsRequret.stateGoods"/><!-- 收货状态 --></th>   
				</tr></thead>
			</table>  
	</div>
	
 	<!-- 收货单 --> 
	<div  style="height: 200px"> 
			<table id="datagrid-purchasereceive-list" title="收货单详情" class="easyui-datagrid" fit="true"
				data-options="url:'${ctx}/manager/order/receive/byRequestId?flag=${flag}&factoryId=${factoryId}&purchasingGroupId=${purchasingGroupId}&materialId=${materialId}&meins=${meins}&vendorId=${vendorId}&type=${type}&beginRq=${beginRq}&endRq=${endRq}' ,method:'post',singleSelect:false,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'deliveryCode',formatter:function(v,r,i){return r.deliveryCode;}"><spring:message code="vendor.orderGoodsRequret.ASNinvoiceNo"/><!-- ASN发货单号 --></th>
				<th data-options="field:'itemNo'"><spring:message code="vendor.orderGoodsRequret.lineNumbers"/><!-- 行号 --></th>
				<th data-options="field:'orderCode', formatter:function(v,r,i){return isNULL(isNULL(isNULL(r.orderItem).order).orderCode)}"><spring:message code="vendor.orderGoodsRequret.purchaseOrderNo."/><!-- 采购订单号 --></th>
				<th data-options="field:'materialCode'"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
				<th data-options="field:'materialName'"><spring:message code="vendor.nameMaterial"/><!-- 物料名称 --></th>
				<th data-options="field:'orderItem.orderQty',formatter:getNumOrderQtyFmt"><spring:message code="vendor.orderGoodsRequret.orderQuantity"/><!-- 订单数量 --></th>
				<th data-options="field:'unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
				<th data-options="field:'receiveQty'"><spring:message code="vendor.orderGoodsRequret.goodsQuantity"/><!-- 收货数量 --></th>   
				<th data-options="field:'orderItem.col3',formatter:getNumCol3Fmt"><spring:message code="vendor.orderGoodsRequret.quantityOrderNotclear"/><!-- 订单未清数量 --></th>
				<th data-options="field:'deliveryQty',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.supplierDeliveryQuantity"/><!-- 供应商发货数量 --></th>
				<th data-options="field:'diffQty',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.differencesNumber"/><!-- 差异数量 --></th>
				<th data-options="field:'zdjsl',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.numberAACpendingInspection"/><!-- AAC待检数量 --></th>
				<th data-options="field:'zzjbl',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.quantityDefective"/><!-- 质量不良数量 --></th>
				<th data-options="field:'zsjhg',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.quantityQualifiedInspection"/><!-- 送检合格数量 -->	</th>
			
				<th data-options="field:'zllbl',formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.badQuantityMaterials"/><!-- 来料不良数量 --></th>
				<th data-options="field:'badRate'"><spring:message code="vendor.orderGoodsRequret.defectiveRate"/><!-- 不良率 --></th>  
				</tr></thead>
			</table>  
	</div> 






	
<%-- 	<!-- 采购商确认驳回窗口 -->
	<div id="win-veto" class="easyui-dialog" title="供应商驳回确认" style="width:600px;height:350px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div id="" style="padding:5px;">
			<form id="form-veto" method="post" 	>
			  <input id="veto_type" name="veto_type" type="hidden"/>
				<input id="veto_ids" name="veto_ids" type="hidden"/>
				<table>
					<tr class="border-none">
						<td>
						<label class="common-label">供应商编码：</label><label  id="ve-code"></label>
						</td>
						<td>
						<label class="common-label">供应商名称：</label><label   id="ve-name"></label>
						</td>
					</tr>

					<tr id="veto-detail" style="display: black;" class="border-none">
						<td colspan="2">
						<label class="common-label" style="float: left;">驳回原因描述:</label>
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
	</div> --%>

<script type="text/javascript">
//查询
function searchPurchaseItemPlan(){
    var searchParamArray = $('#form-goodsRequestItem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-goodsRequestItem-list').datagrid('load',searchParams);
}

//采购商发布要货计划  
function publishGoodsRequest(){    
	var selections = $("#datagrid-goodsRequestItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].publishStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.ccPublished"/>！'/* 包含已发布记录无法重复发布 */,'error');
			return false;
		} 
		
		if( selections[i].orderItem.loekz	 == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.containsdeletionNPublished"/>！'/* 包含删除标识数据无法发布 */,'error');
			return false;
		}
		if(selections[i].orderItem.lockStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.frozenDataCannotpublished"/>！'/* 包含冻结标识数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.zlock	 == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.lockingIdentificationPublished"/>！'/* 包含锁定标识数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.elikz == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.deliveryCompletedReleased"/>！'/* 包含交货已完成数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.bstae == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.noConfirmationDelivery"/>'/* 存在非内向交货单标识无法确认 */,'error');
			return false;
		}
		
		
		
		
		
	}
	var params = $.toJSON(selections);
    
    $.messager.progress({
		title:'<spring:message code="vendor.prompting"/>',/* 提示 */
		msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	$.ajax({
		url:ctx + '/manager/order/goodsRequest/singlePublishGoodsRequests',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="vendor.news"/>',/* 消息 */
						msg:  data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$("#datagrid-goodsRequestItem-list").datagrid('reload'); 
					$("#datagrid-goodsRequest-list").datagrid('reload');
					if(data.goods){
						var publishStatus = data.goods.publishStatus;
						if(publishStatus == 1){
							$(".publishStatus").text('<spring:message code="vendor.published"/>'/* 已发布 */);
						}else if(publishStatus == 0){
							$(".publishStatus").text('<spring:message code="vendor.notRelease"/>');/* 未发布 */
						}else if(publishStatus == 2){
							$(".publishStatus").text('<spring:message code="vendor.orderplan.partRelease"/>');/* 部分发布 */
						}
					}
					
				}else{
					
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}
//供应商确认(供货计划)
function confirmGoodsRequest(){
	var selections = $("#datagrid-goodsRequestItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.containsCannotReconfirmed"/>！'/* 包含已确认记录无法重复确认 */,'error');
			return false;
		} 
		
		if( selections[i].orderItem.loekz	 == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.containsdeletionNPublished"/>！'/* 包含删除标识数据无法发布 */,'error');
			return false;
		}
		if(selections[i].orderItem.lockStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.frozenDataCannotpublished"/>！'/* 包含冻结标识数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.zlock	 == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.lockingIdentificationPublished"/>！'/* 包含锁定标识数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.elikz == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.deliveryCompletedReleased"/>！'/* 包含交货已完成数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.bstae == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.noConfirmationDelivery"/>'/* 存在非内向交货单标识无法确认 */,'error');
			return false;
		}
		
	}
    var params = $.toJSON(selections);
	$.ajax({
		url:ctx + '/manager/order/goodsRequest/confirmOrderItemPlan',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="vendor.news"/>',/* 消息 */
						msg:  data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$("#datagrid-goodsRequestItem-list").datagrid('reload'); 
					$("#datagrid-goodsRequest-list").datagrid('reload');
					if(data.goods){
						var confirmStatus = data.goods.vendorConfirmStatus;
						if(confirmStatus == 1){
							$(".confirmStatus").text('<spring:message code="vendor.confirmed"/>');/* 已确认 */
						}else if(confirmStatus == 0){
							$(".confirmStatus").text('<spring:message code="vendor.unconfirmed"/>');/* 未确认 */
						}else if(confirmStatus == 2){
							$(".confirmStatus").text('<spring:message code="vendor.partConfirmation"/>');/* 部分确认 */
						}
					}

				}else{
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}

//供应商驳回窗口打开(供货计划)
function rejectGoodsRequest(){
	var selections = $("#datagrid-goodsRequestItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	var reject_ids="";
	for(i = 0; i < selections.length; i ++) {
		reject_ids=reject_ids+selections[i].id + ",";
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.containsRecordCannotPerformed"/>！'/* 包含已确认记录无法执行操作 */,'error');
			return false;
		} 
		if(selections[i].confirmStatus == -1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.containsConfirmedRepeated"/>！'/* 包含已确认驳回无法重复执行操作 */,'error');
			return false;
		} 
		if(selections[i].confirmStatus == -2) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.containsRecordCannssot"/>！'/* 包含采购商拒绝驳回的记录，无法驳回 */,'error');
			return false;
		} 
		
		if( selections[i].orderItem.loekz	 == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.containsdeletionNPublished"/>！'/* 包含删除标识数据无法发布 */,'error');
			return false;
		}
		if(selections[i].orderItem.lockStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.frozenDataCannotpublished"/>！'/* 包含冻结标识数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.zlock	 == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.lockingIdentificationPublished"/>！'/* 包含锁定标识数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.elikz == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.deliveryCompletedReleased"/>！'/* 包含交货已完成数据无法发布 */,'error');
			return false;
		}
		if( selections[i].orderItem.bstae == "X") {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.noConfirmationDelivery"/>'/* 存在非内向交货单标识无法确认 */,'error');
			return false;
		}
		
		
		
		
	}
	document.getElementById("reject_ids").value=reject_ids;
	$('#win-reject').window('open');
}
//供应商驳回
function reject(){
	$.messager.confirm('<spring:message code="vendor.prompting"/>'/* 提示 */,/* 确定要执行此操作 */'<spring:message code="vendor.orderGoodsRequret.determineOperation"/>？<font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.prompting"/>',/* 提示 */
				msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
			});
			
			$('#form-reject').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx + '/manager/order/goodsRequest/rejectOrderItemPlan',
				success:function(data){
					$.messager.progress('close');
					var obj = JSON.parse(data);
					try{
						if(obj.success){ 
							$.messager.show({
								title:'<spring:message code="vendor.news"/>',/* 消息 */
								msg:  obj.message, 
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$("#datagrid-goodsRequestItem-list").datagrid('reload'); 
							$("#datagrid-goodsRequest-list").datagrid('reload');
							$('#win-reject').window('close');
							if(data.goods){
								var confirmStatus = data.goods.vendorConfirmStatus;
								if(confirmStatus == 1){
									$(".confirmStatus").text('<spring:message code="vendor.confirmed"/>');/* 已确认 */
								}else if(confirmStatus == 0){
									$(".confirmStatus").text('<spring:message code="vendor.unconfirmed"/>');/* 未确认 */
								}else if(confirmStatus == 2){
									$(".confirmStatus").text('<spring:message code="vendor.partConfirmation"/>');/* 部分确认 */
								}
							}

						}else{
							$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,obj.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
					} 
				}
			});
		}
	});

}

//采购商的驳回
function vetoGoodsRequest(){
	//驳回
	var selections = $("#datagrid-goodsRequestItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	if(selections.length >1){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.selectRecord"/>！'/* 请选择一条记录 */,'info');
		return false;
	}
    //驳回数据的ids
	var veto_ids="";
	for(i = 0; i < selections.length; i ++) {
		veto_ids=veto_ids+selections[i].id + ",";
		if(selections[i].confirmStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.containsPublishedRecordsDismissed"/>！'/* 包含已发布记录无法驳回 */,'error');
			return false;
		} 
		if(selections[i].confirmStatus != -1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.rejectRecord"/>！'/* 请选择驳回记录 */,'error');
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

	$("#veto-label").text('<spring:message code="vendor.orderplan.refusalReject"/>：');	/* 拒绝驳回原因 */
	
	document.getElementById("dialog-adder-bbb1").style.display="none";//隐藏
	document.getElementById("dialog-adder-bbb2").style.display="";//显示

	$('#win-veto').window('open');   
}
//采购商的驳回提交
function veto(){
	
	$.messager.confirm('<spring:message code="vendor.prompting"/>'/* 提示 */,/* 确定当前操作 */'<spring:message code="vendor.orderplan.determineOperation"/>？<font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.prompting"/>',/* 提示 */
				msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
			});
			
			$('#form-veto').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/order/goodsRequest/vetoOrderItemPlan', 
				success:function(data){
					$.messager.progress('close');
					var obj = JSON.parse(data);
					try{
						if(obj.success){ 
							$.messager.show({
								title:'<spring:message code="vendor.news"/>',/* 消息 */
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
							$("#datagrid-goodsRequestItem-list").datagrid('reload'); 
							$("#datagrid-goodsRequest-list").datagrid('reload');
						}else{
							$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,obj.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
					} 
				}
			});
		}
	});
}
//采购商的同意
function unVetoGoodsRequest(){
	//驳回
	var selections = $("#datagrid-goodsRequestItem-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	if(selections.length >1){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.selectRecord"/>！'/* 请选择一条记录 */,'info');
		return false;
	}
    //驳回数据的ids
	var veto_ids="";
	for(i = 0; i < selections.length; i ++) {
		veto_ids=veto_ids+selections[i].id + ",";
		if( selections[i].vetoStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.CrecordsCannotSupplier"/>！'/* 包含同意记录无法重复同意供应商的驳回 */,'error');
			return false;
		} 
		if(selections[i].confirmStatus != -1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.rejectRecord"/>！'/* 请选择驳回记录 */,'error');
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
	
	
	$("#veto-label").text('<spring:message code="vendor.orderplan.adjustReason"/>：');/* 调整理由 */
	document.getElementById("dialog-adder-bbb1").style.display="";//隐藏
	document.getElementById("dialog-adder-bbb2").style.display="none";//显示
	
	$('#win-veto').window('open');   
}

//采购商的同意提交
function unVeto(){
	$.messager.confirm('<spring:message code="vendor.prompting"/>'/* 提示 */,/* 确定当前操作 */'<spring:message code="vendor.orderplan.determineOperation"/>？<font style="color: #F00;font-weight: 900;"></font>',function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.prompting"/>',/* 提示 */
				msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
			});
			
			$('#form-veto').form('submit',{
				ajax:true,
				iframe: true,    
				url:ctx+'/manager/order/goodsRequest/unVetoOrderItemPlan', 
				success:function(data){
					$.messager.progress('close');
					var obj = JSON.parse(data);
					try{
						if(obj.success){ 
							$.messager.show({
								title:'<spring:message code="vendor.news"/>',/* 消息 */
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
							$("#datagrid-goodsRequestItem-list").datagrid('reload'); 
							$("#datagrid-goodsRequest-list").datagrid('reload');
							if(obj.goods){
								var confirmStatus = obj.goods.vendorConfirmStatus;
								var publishStatus = obj.goods.publishStatus;
								if(confirmStatus == 1){
									$(".confirmStatus").text('<spring:message code="vendor.confirmed"/>');/* 已确认 */
								}else if(confirmStatus == 0){
									$(".confirmStatus").text('<spring:message code="vendor.unconfirmed"/>');/* 未确认 */
								}else if(confirmStatus == 2){
									$(".confirmStatus").text('<spring:message code="vendor.partConfirmation"/>');/* 部分确认 */
								}
								if(publishStatus == 1){
									$(".publishStatus").text('<spring:message code="vendor.published"/>');/* 已发布 */
								}else if(publishStatus == 0){
									$(".publishStatus").text('<spring:message code="vendor.notRelease"/>');/* 未发布 */
								}else if(publishStatus == 2){
									$(".publishStatus").text('<spring:message code="vendor.orderplan.partRelease"/>');/* 部分发布 */
								}
							}
						}else{
							$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,obj.message,'error');
						}
					}catch (e) {
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
					} 
				}
			});
			
		}
	});
}

function notDeleteOrderItemPlan(){
	$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.cargoPlanASNnotDeleted"/>！'/* 该要货计划有ASN不能删除 */,'error'); 
}

//采购商的删除提交
function deleteOrderItemPlan(id){

	$.messager.confirm('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.determineOperation"/>？<font style="color: #F00;font-weight: 900;"></font>'/* 确定当前操作 */,function(r){
		if(r){
			$.messager.progress({
				title:'<spring:message code="vendor.prompting"/>',/* 提示 */
				msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
			});
			
			$.ajax({
				url:ctx + '/manager/order/goodsRequest/deleteOrderItemPlan/'+id,
				type:'POST',
				dataType:"json",
				contentType : 'application/json',
				success:function(data){
					$.messager.progress('close');
					try{
						if(data.success){ 
							$.messager.show({
								title:'<spring:message code="vendor.news"/>',/* 消息 */
								msg:  data.message, 
								timeout:2000,
								showType:'show',
								style:{
									right:'',
									top:document.body.scrollTop+document.documentElement.scrollTop,
									bottom:''
								}
							});
							$("#datagrid-goodsRequestItem-list").datagrid('reload'); 
							$("#datagrid-goodsRequest-list").datagrid('reload');
							if(data.goods){
								var publishStatus = data.goods.publishStatus;
								if(publishStatus == 1){
									$(".publishStatus").text('<spring:message code="vendor.published"/>');/* 已发布 */
								}else if(publishStatus == 0){
									$(".publishStatus").text('<spring:message code="vendor.notRelease"/>');/* 未发布 */
								}else if(publishStatus == 2){
									$(".publishStatus").text('<spring:message code="vendor.orderplan.partRelease"/>');/* 部分发布 */
								}
							}
							
						}else{
							
						}
					}catch (e) {
						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
					} 
				}
			});

			
			
			
			
		}
	});
}


/**
 * 修改要货计划总量
 */
function editgoodsQry(){
	
	 $.messager.progress({
			title:'<spring:message code="vendor.prompting"/>'/* 提示 */,
			msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	$('#datagrid-goodsRequestMain-list').datagrid('acceptChanges'); 
	var rows = $("#datagrid-goodsRequestMain-list").datagrid('getRows');
	
	if(rows.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'没有任何记录！','info');
		return false;
	}
	var length=rows.length;
	var str="{total:"+length+",rows:"+$.toJSON(rows)+"}";
	var datas=str;
	$('#form-goodsRequestMain-modify').form('submit',{
		ajax:true,
		iframe: true,   
		url: '${ctx}/manager/order/goodsRequest/editMainGoodsRequest', 
		onSubmit:function(param){
			param.datas = datas;
		}, 
		success: function (data) {  
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){ 
						$.messager.show({
							title:'<spring:message code="vendor.news"/>',/* 消息 */
							msg:  data.message, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$("#datagrid-goodsRequestMain-list").datagrid('reload');
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

function exportExcel(goodsId,vendor){
    $('#form-goodsRequestItem-search').form('submit',{
	   url:'${ctx}/manager/order/goodsRequest/exportExcel/'+goodsId+"/"+ vendor,
	   success:function(data){
			$.messager.progress('close');
		}
	}); 
}

function resetVeto(id){
    $("#ve-veto").val("");
}

//跳转订单详情页面
function showOrderItemList(id,vendor){
	 $dialog = $('<div/>').dialog({     
	        title: '<spring:message code="vendor.orderGoodsRequret.purchaseOrderlist"/>',     /* 采购订单列表 */
	        iconCls : 'pag-search',    
	        closed: true,     
	        cache: false,     
	        href: ctx + '/manager/order/goodsRequest/viewOrderItem/' + id + "/"+vendor,     
	        modal: true,  
	        maximizable:true,
	        maximized:true,
	        onLoad:function(){  
	        	
	        },               
	        onClose:function(){
	            $(this).dialog('destroy');
	        },
	        buttons : [ 
	         ]  

	   });    
	  $dialog.dialog('open');
}

function inspectionReportViewFmt(v,r,i){
	  if(null!=r.inspectionPath && null!=r.inspectionName){
			return '<a tyle="margin-right:10px" href="javascript:;" onclick="File.download(\''+r.inspectionPath+'\',\'\')">'+r.inspectionName+'</a>';
		}
}

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

</script>

