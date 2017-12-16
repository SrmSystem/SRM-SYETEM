<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 流水单号管理 --><spring:message code="purchase.basedata.FlowOddNumberManagement"/></title>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/serialNumberList.js">
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-serial-list" title='<!-- 流水单号列表 --><spring:message code="purchase.basedata.FlowOddNumberList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/serial',method:'post',singleSelect:false,
		toolbar:'#serialNumberListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'key'"><spring:message code="purchase.basedata.UniqueStandard"/><!-- 唯一标实 --></th>   
		<th data-options="field:'prefix'"><spring:message code="purchase.basedata.SerialNumberPrefix"/><!-- 流水号前缀 --></th>
		<th data-options="field:'dataString'"><spring:message code="purchase.basedata.DateFormat"/><!-- 日期格式 --></th>
		<th data-options="field:'startNumber'"><spring:message code="purchase.basedata.StartSerialNumber"/><!-- 开始流水号 --></th>
		<th data-options="field:'dateTimeString'"><spring:message code="purchase.basedata.SerialNumberDate"/><!-- 流水号日期 --></th>
		<th data-options="field:'repeatCycle'"><spring:message code="purchase.basedata.CycleType"/><!-- 循环类型 --></th>
		<th data-options="field:'isVerify',formatter:function(v,r,i){if(r.isVerify) return '<spring:message code="vendor.YES"/>'; return '<spring:message code="vendor.NO"/>';}"><spring:message code="purchase.basedata.IsDaabaseValidationAvailable"/><!-- 是否数据库验证 --></th> 
		<th data-options="field:'remark'"><spring:message code="purchase.basedata.Remarks"/><!-- 备注 --></th> 
		<th data-options="field:'manager',formatter:managerFmt"><spring:message code="purchase.basedata.ProductLineManager"/><spring:message code="purchase.basedata.Administration"/><!-- 管理 --></th>
		</tr></thead>
	</table>
	<div id="serialNumberListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addSerialNumber()"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteSerialNumber()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
		</div>
		<div>
			<form id="form-productLine-search" method="post">
			<!-- 流水号前缀 --><spring:message code="purchase.basedata.ProductLineManager"/>：<input type="text" name="search-LIKE_prefix" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchProductLine()"><spring:message code="purchase.basedata.Query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-productLine-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	
	<div id="win-serialNumber-addoredit" class="easyui-window" title='<!-- 新增流水单号 --><spring:message code="purchase.basedata.AddFlowOddNumber"/>' style="width:520px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-serialNumber-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 唯一标实 --><spring:message code="purchase.basedata.UniqueStandard"/>:</td><td><input class="easyui-textbox" id="key" name="key" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td><!-- 流水号前缀 --><spring:message code="purchase.basedata.SerialNumberPrefix"/>:</td><td><input class="easyui-textbox" id="prefix" name="prefix" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td><!-- 日期格式 --><spring:message code="purchase.basedata.DateFormat"/>:</td><td><input class="easyui-textbox" name="dataString" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td><!-- 开始流水号 --><spring:message code="purchase.basedata.StartSerialNumber"/>:</td><td><input class="easyui-textbox" name="startNumber" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td><!-- 流水号日期 --><spring:message code="purchase.basedata.SerialNumberDate"/>:</td><td>
						<input class="easyui-datebox" name="dateTimeString" data-options="formatter:dateFormatter,parser:dateParser,required:true,width:'120px'" />
					</td>
				</tr>  
				<tr>
					<td><!-- 循环类型 --><spring:message code="purchase.basedata.CycleType"/>:</td><td style="text-align: left;">
					<select class="easyui-combobox" name="repeatCycle" data-options="required:true,width:'120px'"><option value="day">day</option><option value="month">month</option><option value="year">year</option></select>
					</td>
				</tr>
				<tr>
					<td><!-- 是否数据库验证 --><spring:message code="purchase.basedata.IsDaabaseValidationAvailable"/>:</td><td style="text-align: left;">     
						<select class="easyui-combobox" name="isVerify" data-options="required:true,width:'120px'"><option value="false"><spring:message code="purchase.basedata.NO"/></option><option value="true"><spring:message code="purchase.basedata.YES"/></option></select>
					</td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.basedata.Remarks"/>:</td><td>
						<input class="easyui-textbox" name="remark" data-options="multiline:true" style="height:60px"></input>
					</td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddoreditSerialNumber()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetForm()"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>

</body>
</html>
