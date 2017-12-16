<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.check.listAccountExceptions"/><!-- 对账异常列表 --></title>
	<script type="text/javascript">
		/* function managerFmt(v,r,i){
			var t = new Date(v);
			return t.toLocaleString();
		} */
		
	</script>

	<script type="text/javascript" src="${ctx}/static/script/check/dialog2.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/check/check.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-list" title="对账单管理" class="easyui-datagrid" fit="true"
		data-options="url:'${ctx}/manager/check/checks/getExceptionVendorList',method:'post',singleSelect:false,
		toolbar:'#checkExListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],multiSort:true,onClickCell:CellEditor.onClickCell"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'checkItem.check.code',sortable:true"><spring:message code="vendor.check.statementNo"/><!-- 对账单号 --></th>
		<th data-options="field:'checkItem.check.year',sortable:true"><spring:message code="vendor.year"/><!-- 年 --></th>
		<th data-options="field:'checkItem.check.month',sortable:true"><spring:message code="vendor.months"/><!-- 月 --></th>
		<!-- <th data-options="field:'checkItem.check.vendor.code',sortable:true">供方编码</th>
		<th data-options="field:'checkItem.check.vendor.name',sortable:true">供方名称</th> -->
		<th data-options="field:'deliveryCode'"><spring:message code="vendor.check.ASNInvoice"/><!-- ASN发货单号 --></th>
		<th data-options="field:'returnCode'"><spring:message code="vendor.check.returnsNumber"/><!-- 退货单号 --></th>
<!-- 		<th data-options="field:'checkItem.recItem.receive.receiveCode',sortable:true">收货单号</th> -->
	<!--<th data-options="field:'checkItem.recItem.attr9'">QAD收货单号</th> -->
		<th data-options="field:'materialCode'"><spring:message code="vendor.check.partNumber"/><!-- 零件号 --></th>
		<th data-options="field:'receiveQty'"><spring:message code="vendor.check.quantity"/><!-- 数量 --></th>
		<th data-options="field:'unitName'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
		<!-- <th data-options="field:'checkItem.checkPrice',sortable:true">核价单价</th> -->
		<th data-options="field:'checkItem.vendorCheckPrice'"><spring:message code="vendor.check.supplierFillsPrice"/><!-- 供应商填写单价 --></th>
		<th data-options="field:'buyerCheckPrice'"><spring:message code="vendor.check.buyerUnitPrice"/><!-- 采购商核对单价 --></th>
		<th data-options="field:'exDiscription',editor:{type:'text',options:{required:true}}, formatter:function(v,r,i){return checkNull(r.exDiscription);}"><spring:message code="vendor.check.differencesThat"/><!-- 差异说明 --></th>
		<th data-options="field:'checkItem.exDealStatus',formatter:function(v,r,i){return StatusRender.render(r.checkItem.exDealStatus,'deal',true);}"><spring:message code="vendor.check.differentialTreatmentStatus"/><!-- 差异处理状态 --></th>
		<th data-options="field:'checkItem.exConfirmStatus',formatter:function(v,r,i){return StatusRender.render(r.checkItem.exConfirmStatus,'confirm',true);}"><spring:message code="vendor.check.differentialConfirmation"/><!-- 差异确认状态 --></th>
		<th data-options="field:'createTime'"><spring:message code="vendor.creationTime"/><!-- 创建时间 --></th>
		
		</tr></thead>
	</table>
	<div id="checkExListToolbar" style="padding:5px;">
		<div>
		<%-- <shiro:hasPermission name="check:ex:v:confirmEx"> --%> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Check.confirmEx()"><spring:message code="vendor.confirm"/><!-- 确认 --></a>
		<%-- </shiro:hasPermission> --%>
		</div>
		<div>
			<form id="form-search" method="post">
			<!-- 增加查询条件：对账时间、所属年、零件号、采购单号、采方差异处理状态 ， 去掉查询条件：供应商编码、供应商名称 -->
			<spring:message code="vendor.check.statementNo"/><!-- 对账单号 -->：<input type="text" name="search-LIKE_checkItem.check.code" class="easyui-textbox" style="width:120px;"/>&nbsp;
			<spring:message code="vendor.check.checkTime"/><!-- 对账时间 -->：<input type="text" name="search-GTE_checkItem.createTime" data-options="editable:false" class="easyui-datebox" style="width:100px;"/>
			- <input type="text" name="search-LTE_checkItem.createTime" data-options="editable:false" class="easyui-datebox" style="width:100px;"/>&nbsp;
			<spring:message code="vendor.year"/><!-- 年 -->：<input type="text" name="search-EQ_checkItem.check.year" class="easyui-textbox" style="width:80px;"/>&nbsp;
			<spring:message code="vendor.months"/><!-- 月 -->：<input type="text" name="search-EQ_checkItem.check.month" class="easyui-textbox" style="width:80px;"/>&nbsp;
			<spring:message code="vendor.check.partNumber"/><!-- 零件号 -->：<input type="text" name="search-EQ_checkItem.material.code" class="easyui-textbox" style="width:120px;"/>&nbsp;</br>
			<spring:message code="vendor.check.purchaseOrderno"/><!-- 采购单号 -->：<input type="text" name="search-LIKE_checkItem.orderCode" class="easyui-textbox" style="width:120px;"/>&nbsp;
			<spring:message code="vendor.check.extractionProcessState"/><!-- 采方处理状态 -->：<select id="selectType" class="easyui-combobox" data-options="editable:false" name="search-EQ_checkItem.exDealStatus">
							<option value="" selected="selected">-<spring:message code="vendor.all"/><!-- 全部 -->-</option>
							<option value="0"><spring:message code="vendor.untreated"/><!-- 未处理 --></option>
							<option value="1"><spring:message code="vendor.processed"/><!-- 已处理 --></option>
						</select>&nbsp;
			<!-- 供应商编码：<input type="text" name="search-LIKE_checkItem.check.vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_checkItem.check.vendor.name" class="easyui-textbox" style="width:80px;"/> -->
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	

<script type="text/javascript">
function doSearch(){
	var $startTime = $("input[name='search-GTE_checkItem.createTime']");
	var $endTime = $("input[name='search-LTE_checkItem.createTime']");
	if($endTime.val().length != 0 && $startTime.val() > $endTime.val()){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */, '<spring:message code="vendor.check.starCannotTime"/>!'/* 开始时间不能大于结束时间 */, 'error');
		return;
	}
	var searchParamArray = $('#form-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-list').datagrid('load',searchParams);
}


</script>
</body>
</html>
