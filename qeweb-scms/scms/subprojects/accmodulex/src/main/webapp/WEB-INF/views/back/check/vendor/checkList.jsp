<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.check.checkList"/><!-- 对账列表 --></title>

	<script type="text/javascript" src="${ctx}/static/script/check/dialog2.js"></script>
		<script type="text/javascript" src="${ctx}/static/script/check/check.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-list" title="对账单管理" class="easyui-datagrid" fit="true"
		data-options="url:'${ctx}/manager/check/checks/getVendorList',method:'post',singleSelect:false,
		toolbar:'#checkListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'operate',formatter:Check.operateFmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
		<th data-options="field:'code'"><spring:message code="vendor.check.statementNo"/><!-- 对账单号 --></th>
		<th data-options="field:'year'"><spring:message code="vendor.year"/><!-- 年 --></th>
		<th data-options="field:'month'"><spring:message code="vendor.months"/><!-- 月 --></th>
		<th data-options="field:'buyer.code'"><spring:message code="vendor.procurementOrganizationCode"/><!-- 采购组织编码 --></th>
		<th data-options="field:'buyer.name'"><spring:message code="vendor.namePurchasingOrganization"/><!-- 采购组织名称 --></th>
		<!--<th data-options="field:'vendor.name'">供方名称</th>
		<th data-options="field:'type'">类型</th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',true);}">发布状态</th> -->
		<th data-options="field:'vConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.supplierConfirmsStatus"/><!-- 供方确认状态 --></th>
		<th data-options="field:'bConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.extractionState"/><!-- 采方确认状态 --></th>
		<th data-options="field:'billStatus',formatter:function(v,r,i){return StatusRender.render(v,'bill',true);}"><spring:message code="vendor.check.stateInvoice"/><!-- 开票状态 --></th>
		<th data-options="field:'exStatus',formatter:function(v,r,i){return StatusRender.render(v,'exception',true);}"><spring:message code="vendor.check.abnormalState"/><!-- 异常状态 --></th>
		<th data-options="field:'exDealStatus',formatter:function(v,r,i){return StatusRender.render(v,'deal',true);}"><spring:message code="vendor.check.exceptionHandlingState"/><!-- 异常处理状态 --></th>
		<th data-options="field:'exConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.abnormalConfirmationStatus"/><!-- 异常供方确认状态 --></th>
		<th data-options="field:'reviewStatus',formatter:function(v,r,i){return StatusRender.render(v,'reviewStatus',true);}"><spring:message code="vendor.check.invoiceStatus"/><!-- 发票审核状态 --></th>
		<!-- 
		<th data-options="field:'payStatus',formatter:function(v,r,i){return StatusRender.render(v,'pay',true);}">付款状态</th>
		 -->
		<th data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(v,'closeStatus',true);}"><spring:message code="vendor.check.closedPosition"/><!-- 关闭状态 --></th>
		<th data-options="field:'createTime'"><spring:message code="vendor.creationTime"/><!-- 创建时间 --></th>
		</tr></thead>
	</table>
	<div id="checkListToolbar" style="padding:5px;">
		<div>
			<form id="form-search" method="post">
			<spring:message code="vendor.check.statementNo"/><!-- 对账单号 -->：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.check.subordinateYears"/><!-- 所属年 -->：<input type="text" name="search-EQ_year" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.months"/><!-- 月 -->：<input type="text" name="search-EQ_month" class="easyui-textbox" style="width:80px;"/>
			<!-- 供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/> -->
			<spring:message code="vendor.check.purchaseVerificationStatus"/><!-- 采购核对状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_bConfirmStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toConfirmed"/><!-- 待确认 --></option><option value="1"><spring:message code="vendor.confirmed"/><!-- 已确认 --></option><option value="2"><spring:message code="vendor.partConfirmation"/><!-- 部分确认 --></option></select>
			<spring:message code="vendor.check.stateInvoice"/><!-- 开票状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_billStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.notInvoice"/><!-- 未开票 --></option><option value="1"><spring:message code="vendor.makeInvoice"/><!-- 已开票 --></option></select>
			<spring:message code=""/><!-- 差异状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_exStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.noDifference"/><!-- 无差异 --></option><option value="1"><spring:message code="vendor.differences"/><!-- 有差异 --></option></select>
			<spring:message code="vendor.check.supplierConfirmsStatus"/><!-- 供方确认状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_vConfirmStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toConfirmed"/><!-- 待确认 --></option><option value="1"><spring:message code="vendor.confirmed"/><!-- 已确认 --></option><option value="2"><spring:message code="vendor.partConfirmation"/><!-- 部分确认 --></option></select>
			<spring:message code="vendor.check.purchaseDifferentialStatus"/><!-- 采购差异处理状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_exDealStatus"><option value="">-<!-- 全部 -->-</option><option value="0"><spring:message code="vendor.untreated"/><!-- 未处理 --></option><option value="1"><spring:message code="vendor.processed"/><!-- 已处理 --></option></select>
			<spring:message code="vendor.check.closedPosition"/><!-- 关闭状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.notClosed"/><!-- 未关闭 --></option><option value="1"><spring:message code="vendor.closed"/><!-- 已关闭 --></option></select>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	

<script type="text/javascript">
function doSearch(){
	var searchParamArray = $('#form-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-list').datagrid('load',searchParams);
}



</script>
</body>
</html>
