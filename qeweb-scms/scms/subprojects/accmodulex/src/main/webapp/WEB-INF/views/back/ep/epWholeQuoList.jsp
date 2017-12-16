<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>整项报价信息</title>
	<script type="text/javascript" src="${ctx}/static/script/ep/epWholeQuoManage.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>
		<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
	<%-- <script type="text/javascript" src="${ctx}/static/script/ep/epWholeQuoManage.js"></script> --%>
	<script type="text/javascript" src="${ctx}/static/script/ep/epSubQuo.js"></script>
</head>
<body style="margin:0;padding:0;">
<div class="easyui-panel" style="overflow: auto;width: 100%;height: 100%">
	<input id="epVendorId" name="epVendorId" value="${epVendorId}" hidden="true" />
	<input id="epPriceQuoteStatus" value="${epPrice.quoteStatus}" hidden="true" />
	<table id="datagrid-epEPWholeQuo-list" title="" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/ep/epWholeQuo/buyerGetList/${epPriceId}/${epVendorId}/${isVendor}',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#epWholeQuoToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,idField: 'id'">
		<thead><tr>   
		<th width="50px" data-options="field:'id',checkbox:true,hidden:false"></th>
		<th width="100px" data-options="field:'opt',formatter:seeInfoFmt">操作</th>
		<th width="100px" data-options="field:'epMaterialCode',formatter:function(v,r,i){return r.epMaterial.materialCode}">物料编码</th>
		<th width="100px" data-options="field:'epMaterialName',formatter:function(v,r,i){return r.epMaterial.materialName}">物料名称</th>
		<th width="100px" data-options="field:'planPurchaseQty',formatter:function(v,r,i){return r.epMaterial.planPurchaseQty}">数量</th>
		<th width="100px" data-options="field:'freight',formatter:function(v,r,i){return r.epMaterial.freight}">运输费用</th>
		<th width="100px" data-options="field:'totalQuotePrice'">无税单价</th>
		<th width="100px" data-options="field:'quotePrice'">含税单价</th>
		<c:if test="${epPrice.quoteStatus eq 2}">
			<th width="100px" data-options="field:'negotiatedPrice',formatter:function(v,r,i){if(v==null || v=='') return r.totalQuotePrice;else return v;},editor:{type:'numberbox', options: {precision:2}}">协商单价</th>
		</c:if>
		<th width="100px" data-options="field:'supplyCycle'">供货周期</th>
		<th width="100px" data-options="field:'taxRate'">税率</th>
		<th width="100px" data-options="field:'taxCategory'">税种</th>
		<th width="100px" data-options="field:'warrantyPeriod'">保质期</th>
		<th width="100px" data-options="field:'transportationMode'">运输方式</th>
		<!-- <th width="100px" data-options="field:'paymentMeans'">付款方式</th> -->
		<th width="100px" data-options="field:'quoteStatus',formatter:function(v,r,i){return StatusRender.render(v,'wholeQuoteStatus',false);}">报价状态</th>
		<!-- <th width="100px" data-options="field:'requoteStatus',formatter:function(v,r,i){return StatusRender.render(v,'requoteStatus',false);}">重新报价状态</th> -->
		<th width="100px" data-options="field:'negotiatedStatus',formatter:function(v,r,i){return StatusRender.render(v,'negotiatedStatus',false);}">采方议价状态</th>
		<th width="100px" data-options="field:'negotiatedCheckStatus',formatter:function(v,r,i){return StatusRender.render(v,'negotiatedCheckStatus',false);}">供方确认议价状态</th>
		<th width="100px" data-options="field:'cooperationStatus',formatter:function(v,r,i){return StatusRender.render(v,'cooperationStatus',false);}">合作状态</th>
		<th width="100px" data-options="field:'auditStatus',formatter:function(v,r,i){if(r.eipApprovalStatus==null||r.eipApprovalStatus=='') return '未审核'; else return StatusRender.render(r.eipApprovalStatus,'audit',false);}">审核状态</th>
		</tr></thead>
	</table>
	<div id="epWholeQuoToolbar" style="padding:5px;height:auto">
	  <form id="form-epPrice-search" method="post">
	 		 <input type="hidden" id="tableDatas" name="tableDatas" />
	  
	   	<%-- <c:if test="${epPrice.quoteWay eq 1}"> --%>
		  	<c:if test="${epPrice.quoteStatus eq 2}">
		  	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveNegotiatedPriceBefore('datagrid-epEPWholeQuo-list')">保存协商价</a>  
<!-- 			  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveNegotiatedPrice('datagrid-epEPWholeQuo-list')">提交协商价审核</a>   -->
<!-- 			  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="uploadQuotePrice('datagrid-epEPWholeQuo-list')">询价结果回传</a>   -->
		  	</c:if>
	  	<%-- </c:if> --%>
	  </form>
	</div> 
</div>



<!-- 重新选择报价截止时间页面 -->
<div id="win-reset-quoteEndTime" class="easyui-window" title="询价单信息"
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
	<div class="easyui-panel" data-options="fit:true">
		<form id="form-reset-quoteEndTime">
			<table style="width: 90%;margin: auto;margin-top: 20px">
			<input id="id" name="id" hidden="true" />
			<tr>
				<td>询价物料名称：<input id="materialName" name="epMaterial.materialName" style="width:160px;border: 0px" readonly="readonly"/></td>
			</tr>
			<tr>
				<td>询价供应商名称：<input id="vendorName" name="epVendor.vendorName" style="width:160px;border: 0px" readonly="readonly"/></td>
			</tr>
			<tr>
				<td>报价截止时间：<input id="quoteEndTime" name="quoteEndTime" class="easyui-datetimebox" style="width:160px;" required="true"/></td>
			</tr>
			</table>
		</form>
		<div style="width: 40%;margin: auto;margin-top: 10px">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:''" onclick="submitQuoteEndTime()">提交</a>
		</div>
	</div>
</div>

<script type="text/javascript">
var clientWidth = document.body.clientWidth;	
var clientHeight = document.body.clientHeight;	

	function buyerOpenEpWholeQuoWin(epPriceId,epMaterialId,isVendor,quoteWay){
		var epVendorId = $("#epVendorId").val();
		var href = ctx + '/manager/ep/epWholeQuo/buyerOpenEpWholeQuoWin?epPriceId='+ epPriceId+'&epMaterialId='+epMaterialId+'&epVendorId='+epVendorId+'&isVendor='+isVendor;
		var title = "";
		if(quoteWay==0){
			title="分项报价详情";
		}else if(quoteWay==1){
			title="整项报价详情";
		}
		 new dialog().showWin(title, clientWidth, clientHeight,ctx + '/manager/ep/epWholeQuo/buyerOpenEpWholeQuoWin?epPriceId='+ epPriceId+'&epMaterialId='+epMaterialId+'&epVendorId='+epVendorId+'&isVendor='+isVendor,'dialog-subView');
	}
	
	
</script>

</body>
</html>
