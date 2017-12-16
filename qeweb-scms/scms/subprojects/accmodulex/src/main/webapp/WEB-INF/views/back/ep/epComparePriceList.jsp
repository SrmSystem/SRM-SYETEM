<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>比价信息</title>
	<script type="text/javascript" src="${ctx}/static/script/ep/epComparePriceManage.js"></script>
</head>
<body style="margin:0;padding:0;">
<div style="height: 50%">
		<table id="datagrid-epEPWholeQuox-list" title="" class="easyui-datagrid"
			data-options="url:'${ctx}/manager/ep/epComparePrice/buyerGetList/${epPriceId}/${epMaterialId}',method:'post',singleSelect:false,
			fit:true,border:false,toolbar:'#epWholeQuoToolbar',
			pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
			<thead><tr>   
			<th width="30px" data-options="field:'id',checkbox:true"></th>
		<!-- 	<th data-options="field:'opt',formatter:comparePriceManage.viewFmt">操作</th> -->
			
			<th width="130px" data-options="field:'epVendorCode',formatter:function(v,r,i){return r.epVendor.vendorCode}">供应商编码</th>
			<th width="130px" data-options="field:'epVendorName',formatter:function(v,r,i){return r.epVendor.vendorName}">供应商名称</th>
			<th width="130px" data-options="field:'quoteStatus',formatter:function(v,r,i){return StatusRender.render(v,'wholeQuoteStatus',false);}">报价状态</th>
			<th width="130px" data-options="field:'quotePrice'">含税单价</th>
				
			<th width="130px" data-options="field:'planPurchaseQty',formatter:function(v,r,i){return r.epMaterial.planPurchaseQty}">数量</th>
			<th width="130px" data-options="field:'freight',formatter:function(v,r,i){return r.epMaterial.freight}">运输费用</th>
			<th width="130px" data-options="field:'supplyCycle'">供货周期</th>
			<th width="130px" data-options="field:'taxRate'">税率</th>
			<th width="130px" data-options="field:'taxCategory'">税种</th>
			<th width="130px" data-options="field:'warrantyPeriod'">保质期</th>
			<th width="130px" data-options="field:'transportationMode'">运输方式</th>
			<!-- <th width="130px" data-options="field:'paymentMeans'">付款方式</th> -->
		
			</tr></thead>
		</table>
	
		<div id="epWholeQuoToolbar" style="padding:5px;height:auto">
				 <!--  <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="comparePriceManage.compareSubPrice()">分项比价</a>  -->
		</div> 
	</div>
	
	  	<c:if test="${epPrice.quoteWay eq 0 }">
		<div style="height: 50%">
			<table id="datagrid-moduleItem-list" title="" class="easyui-datagrid"
				data-options="url:'${ctx}/manager/ep/epComparePrice/getModuleItemList/${epMaterialId}',method:'post',singleSelect:false,
				fit:true,border:false,
				pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
				<thead><tr>   
				<th data-options="field:'optx',formatter:comparePriceManage.viewModuleItemFmt">操作</th>
				
				<th data-options="field:'name'">类别名称</th>
			
				</tr></thead>
			</table>
		</div>



		<div id="win-sub-detail" title="分项比价" class="easyui-window"
			data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
			
			<table id="datagrid-subQuo-list" title="分项比价明细" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,toolbar: '#epSubItemtb',
				pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
				<thead><tr>  
				<th width="50px" data-options="field:'id',checkbox:true"></th>
				    
			<th width="140px" data-options="field:'epVendorCodex',formatter:function(v,r,i){return r.wholeQuo.epVendor.vendorCode}">供应商编码</th>
			<th width=140px" data-options="field:'epVendorNamex',formatter:function(v,r,i){return r.wholeQuo.epVendor.vendorName}">供应商名称</th>
				
				<th width="100px" data-options="field:'totalQuotePrice'">含税单价</th>
				<th width="100px" data-options="field:'qty',width:80">数量</th>
				<th width="100px" data-options="field:'subtotal',width:80">小计</th>
		
				</tr></thead>
			</table> 
			
		</div>
	</c:if>
</body>
</html>
