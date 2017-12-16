<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.check.checkList"/><!-- 对账列表 --></title>
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
		data-options="url:'${ctx}/manager/check/checks/getList',method:'post',singleSelect:false,
		toolbar:'#checkListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],multiSort:true"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<!-- <th data-options="field:'qadVoucher'">凭证号</th> -->
		<th data-options="field:'operate',formatter:Check.operateFmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
		<th data-options="field:'code',sortable:true"><spring:message code="vendor.check.statementNo"/><!-- 对账单号 --></th>
		<th data-options="field:'year',sortable:true"><spring:message code="vendor.year"/><!-- 年 --></th>
		<th data-options="field:'month',sortable:true"><spring:message code="vendor.months"/><!-- 月 --></th>
		<th data-options="field:'vendor.code',sortable:true"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'buyer.code',sortable:true"><spring:message code="vendor.procurementOrganizationCode"/><!-- 采购组织编码 --></th>
		<th data-options="field:'buyer.name',sortable:true"><spring:message code="vendor.namePurchasingOrganization"/><!-- 采购组织名称 --></th>
		<th data-options="field:'type',formatter:function(v,r,i){return StatusRender.render(v,'orderType',false);}"><spring:message code="vendor.type"/><!-- 类型 --></th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',true);}"><spring:message code="vendor.postStatus"/><!-- 发布状态 --></th>
		<th data-options="field:'vConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.supplierConfirmsStatus"/><!-- 供方确认状态 --></th>
		<th data-options="field:'bConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.extractionState"/><!-- 采方确认状态 --></th>
		<th data-options="field:'billStatus',formatter:function(v,r,i){return StatusRender.render(v,'bill',true);}"><spring:message code="vendor.check.stateInvoice"/><!-- 开票状态 --></th>
		<th data-options="field:'exStatus',formatter:function(v,r,i){return StatusRender.render(v,'exception',true);}"><spring:message code="vendor.check.abnormalState"/><!-- 异常状态 --></th>
		<th data-options="field:'exDealStatus',formatter:function(v,r,i){return StatusRender.render(v,'deal',true);}"><spring:message code="vendor.check.exceptionHandlingState"/><!-- 异常处理状态 --></th>
		<th data-options="field:'exConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',true);}"><spring:message code="vendor.check.abnormalConfirmationStatus"/><!-- 异常供方确认状态 --></th>
		<th data-options="field:'reviewStatus',formatter:function(v,r,i){if(v==null) return '';else return StatusRender.render(v,'reviewStatus',true);}"><spring:message code="vendor.check.invoiceStatus"/><!-- 发票审核状态 --></th>
		<!-- 
		<th data-options="field:'payStatus',formatter:function(v,r,i){return StatusRender.render(v,'pay',true);}">付款状态</th>
		 -->
		<th data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(v,'closeStatus',true);}"><spring:message code="vendor.check.closedPosition"/><!-- 关闭状态 --></th>
		<th data-options="field:'createTime'"><spring:message code="vendor.creationTime"/><!-- 创建时间 --></th>
		<th data-options="field:'vendor.name',sortable:true"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		</tr></thead>
	</table>
	<div id="checkListToolbar" style="padding:5px;">
		<div>
		<%-- <shiro:hasPermission name="check:list:close">  --%>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'" onclick="Check.close()"><spring:message code="vendor.shutDown"/><!-- 关闭 --></a>
		<%-- </shiro:hasPermission> --%>
			<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Check.create()">生成对账单</a> --> 
		</div>
		<div>
			<form id="form-search" method="post">
			<spring:message code="vendor.check.statementNo"/><!-- 对账单号 -->：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.check.subordinateYears"/><!-- 所属年 -->：<input type="text" name="search-EQ_year" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.months"/><!-- 月 -->：<input type="text" name="search-EQ_month" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.check.supplierConfirmsStatus"/><!-- 供方确认状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_vConfirmStatus"><option value="">-<!-- 全部 --><spring:message code="vendor.all"/>-</option><option value="0"><spring:message code="vendor.toConfirmed"/><!-- 待确认 --></option><option value="1"><!-- 已确认 --><spring:message code="vendor.confirmed"/></option><option value="2"><!-- 部分确认 --><spring:message code="vendor.partConfirmation"/></option></select>
			<spring:message code="vendor.check.purchaseVerificationStatus"/><!-- 采购核对状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_bConfirmStatus"><option value="">-<!-- 全部 --><spring:message code="vendor.all"/>-</option><option value="0"><!-- 待确认 --><spring:message code="vendor.toConfirmed"/></option><option value="1"><!-- 已确认 --><spring:message code="vendor.confirmed"/></option><option value="2"><!-- 部分确认 --><spring:message code="vendor.partConfirmation"/></option></select>
			<spring:message code="vendor.check.stateInvoice"/><!-- 开票状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_billStatus"><option value="">-<!-- 全部 --><spring:message code="vendor.all"/>-</option><option value="0"><!-- 未开票 --><spring:message code="vendor.notInvoice"/></option><option value="1"><!-- 已开票 --><spring:message code="vendor.makeInvoice"/></option></select>
			<spring:message code="vendor.check.differencesState"/><!-- 差异状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_exStatus"><option value="">-<!-- 全部 --><spring:message code="vendor.all"/>-</option><option value="0"><!-- 无差异 --><spring:message code="vendor.noDifference"/></option><option value="1"><!-- 有差异 --><spring:message code="vendor.differences"/></option></select>
			<spring:message code="vendor.check.purchaseDifferentialStatus"/><!-- 采购差异处理状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_exDealStatus"><option value="">-<!-- 全部 --><spring:message code="vendor.all"/>-</option><option value="0"><!-- 未处理 --><spring:message code="vendor.untreated"/></option><option value="1"><!-- 已处理 --><spring:message code="vendor.processed"/></option></select>
			<spring:message code="vendor.check.closedPosition"/><!-- 关闭状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_closeStatus"><option value="">-<!-- 全部 --><spring:message code="vendor.all"/>-</option><option value="0"><!-- 未关闭 --><spring:message code="vendor.notClosed"/></option><option value="1"><!-- 已关闭 --><spring:message code="vendor.closed"/></option></select>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="doSearch()"><!-- 查询 --><spring:message code="vendor.enquiries"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-search').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
						</td>
					</tr>
				</table>
			</div>
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
