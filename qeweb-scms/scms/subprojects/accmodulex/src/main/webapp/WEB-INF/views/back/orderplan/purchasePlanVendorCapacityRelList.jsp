<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title><spring:message code="vendor.orderplan.supplierRelationshipMaintenance"/></title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/purchasePlanVendorCapacityRel.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<script type="text/javascript">
	function modifyFmt(v,r,i) {
		var type = "'update'";
		return '<a href="javascript:;" class="easyui-linkbutton"  data-options="plain:true" onclick="showdialog('+ r.id +','+type+');"><spring:message code="vendor.orderplan.edit"/></a>';
	} 
	</script>   
</head>
<body style="margin:0;padding:0;">
	<table id="vendorCapacity" title="供应商产能关系维护" class="easyui-datagrid" fit="true"
		data-options="url:'${ctx}/manager/order/purchasePlanVendorCapacityRel',method:'post',singleSelect:false,
		toolbar:'#vendorCapacityListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}"><spring:message code="vendor.orderplan.supplierCode"/></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}"><spring:message code="vendor.orderplan.supplierName"/></th>
		<th width="150px" data-options="field:'capacityNames'"><spring:message code="vendor.orderplan.capacityTableField"/></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="vendor.orderplan.effectiveState"/></th>
		<th data-options="field:'modify',formatter:modifyFmt"><spring:message code="vendor.orderplan.operation"/></th> 
		</tr></thead>
	</table>
	<div id="vendorCapacityListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showdialog(0,'add');"><spring:message code="vendor.orderplan.new"/></a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteCapacity();"><spring:message code="vendor.orderplan.deleting"/></a>
		</div>
		<div>
			<form id="form-vendorCapacity-search" method="post">
			<spring:message code="vendor.orderplan.supplierCode"/>：<input type="text" name="query-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.orderplan.supplierName"/>：<input type="text" name="query-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.orderplan.effectiveState"/>：<select class="easyui-combobox" data-options="editable:false" name="query-EQ_abolished"><option value=""><spring:message code="vendor.orderplan.all"/></option><option value="0"><spring:message code="vendor.orderplan.takeEffect"/></option><option value="1"><spring:message code="vendor.orderplan.invalid"/></option></select>
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlan()"><spring:message code="vendor.orderplan.enquiries"/></a> 
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-vendorCapacity-search').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>   
						</td>
					</tr>
				</table>
			</div>
			
			</form>
		</div>
	</div>
<script type="text/javascript">
//查询
function searchPurchasePlan(){
	var searchParamArray = $('#form-vendorCapacity-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#vendorCapacity').datagrid('load',searchParams);
}
</script>
</body>
</html>
