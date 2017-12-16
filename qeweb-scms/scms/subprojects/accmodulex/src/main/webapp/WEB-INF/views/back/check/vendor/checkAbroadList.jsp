<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<title><spring:message code="base.Check.Manage"/><!-- 对账单管理 --></title>
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
<div>
	<table id="datagrid-list" title='<spring:message code="base.Check.Manage"/>' class="easyui-datagrid"
		data-options="url:'${ctx}/manager/check/checks/getVendorAbroadList',method:'post',singleSelect:false,
		toolbar:'#checkListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'qadVoucher'"><spring:message code="base.Reconciliation.VoucherNO"/><!-- 凭证号 --></th>
		<th data-options="field:'operate',formatter:Check.operateFmtAbroad"><spring:message code="base.Reconciliation.Operate"/><!-- 操作 --></th>
		<th data-options="field:'code'"><spring:message code="base.Reconciliation.CheckNo"/><!-- 对账单号 --></th>
		<th data-options="field:'year'"><spring:message code="base.Reconciliation.Year"/><!-- 年 --></th>
		<th data-options="field:'month'"><spring:message code="base.Reconciliation.Month"/><!-- 月 --></th>
		<!-- <th data-options="field:'vendor.code'">供方编码</th>
		<th data-options="field:'vendor.name'">供方名称</th>
		<th data-options="field:'type'">类型</th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',true);}">发布状态</th> -->
		<!-- <th data-options="field:'vConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}">供方确认状态</th>
		<th data-options="field:'bConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}">采方确认状态</th>
		<th data-options="field:'billStatus',formatter:function(v,r,i){return StatusRender.render(v,'bill',true);}">开票状态</th> -->
		<th data-options="field:'exStatus',formatter:function(v,r,i){return StatusRender.render(v,'exception',true);}"><spring:message code="base.Reconciliation.AbnormalStates"/><!-- 异常状态 --></th>
		<th data-options="field:'exDealStatus',formatter:function(v,r,i){return StatusRender.render(v,'deal',true);}"><spring:message code="base.Reconciliation.ExceptionHandlingStates"/><!-- 异常处理状态 --></th>
		<!-- <th data-options="field:'exConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}">异常供方确认状态</th>
		<th data-options="field:'payStatus',formatter:function(v,r,i){return StatusRender.render(v,'pay',true);}">付款状态</th> -->
		<th data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(v,'closeStatus',true);}"><spring:message code="purchase.order.closeStatus"/><!-- 关闭状态 --></th>
		<th data-options="field:'createTime'"><spring:message code="purchase.order.created"/><!-- 创建时间 --></th>
		</tr></thead>
	</table>
	<div id="checkListToolbar" style="padding:5px;">
		<div>
			<form id="form-search" method="post">
			<spring:message code="base.Reconciliation.CheckNo"/><!-- 对账单号 -->：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="base.Reconciliation.Year"/><!-- 年 -->：<input type="text" name="search-EQ_year" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="base.Reconciliation.Month"/><!-- 月 -->：<input type="text" name="search-EQ_month" class="easyui-textbox" style="width:80px;"/>
			<!-- 供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/> -->
			<spring:message code="base.Reconciliation.PurchaseCheckStatus"/><!-- 采购核对状态 -->：<select class="easyui-combobox" name="search-EQ_bConfirmStatus"><option value=""><spring:message code="status"/><!-- ---全部--- --></option><option value="0"><spring:message code="status.confirm.0"/><!-- 待确认 --></option><option value="1"><spring:message code="status.confirm.1"/><!-- 已确认 --></option><option value="2"><spring:message code="status.confirm.2"/><!-- 部分确认 --></option></select>
			<spring:message code="base.Reconciliation.BillingStatus"/><!-- 开票状态 -->：<select class="easyui-combobox" name="search-EQ_billStatus"><option value=""><spring:message code="status"/><!-- ---全部--- --></option><option value="0"><spring:message code="status.bill.0"/><!-- 未开票 --></option><option value="1"><spring:message code="status.bill.1"/><!-- 已开票 --></option></select>
			<spring:message code="base.Reconciliation.DifferencesStatus"/><!-- 差异状态 -->：<select class="easyui-combobox" name="search-EQ_exStatus"><option value=""><spring:message code="status"/><!-- ---全部--- --></option><option value="0"><spring:message code="status.Reconciliation.AbnormalStates2"/><!-- 无差异 --></option><option value="1"><spring:message code="status.Reconciliation.AbnormalStates1"/><!-- 有差异 --></option></select>
			<spring:message code="base.Reconciliation.SupplierConfirmationStatus"/><!-- 供方确认状态 -->：<select class="easyui-combobox" name="search-EQ_vConfirmStatus"><option value=""><spring:message code="status"/><!-- ---全部--- --></option><option value="0"><spring:message code="status.confirm.0"/><!-- 待确认 --></option><option value="1"><spring:message code="status.confirm.1"/><!-- 已确认 --></option><option value="2"><spring:message code="status.confirm.2"/><!-- 部分确认 --></option></select>
			<spring:message code="base.Reconciliation.DifferencesPorcessStatus"/><!-- 采购差异处理状态 -->：<select class="easyui-combobox" name="search-EQ_exDealStatus"><option value=""><spring:message code="status"/><!-- ---全部--- --></option><option value="0"><spring:message code="status.Reconciliation.ProcessingStatus2"/><!-- 未处理 --></option><option value="1"><spring:message code="status.Reconciliation.ProcessingStatus1"/><!-- 已处理 --></option></select>
			<spring:message code="purchase.order.closeStatus"/><!-- 关闭状态 -->：<select class="easyui-combobox" name="search-EQ_closeStatus"><option value=""><spring:message code="status"/><!-- --全部--- --></option><option value="0"><spring:message code="status.close.0"/><!-- 未关闭 --></option><option value="1"><spring:message code="status.close.1"/><!-- 已关闭 --></option></select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()"><spring:message code="button.query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-search').form('reset')"><spring:message code="button.reset"/><!-- 重置 --></a>
			</form>
		</div>
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
