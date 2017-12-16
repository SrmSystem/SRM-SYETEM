<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>流水单号管理</title>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/serialNumberList.js">
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-serial-list" title="流水单号列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/serial',method:'post',singleSelect:false,
		toolbar:'#serialNumberListToolbar',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'key'">唯一标实</th>   
		<th data-options="field:'prefix'">流水号前缀</th>
		<th data-options="field:'dataString'">日期格式</th>
		<th data-options="field:'startNumber'">开始流水号</th>
		<th data-options="field:'dateTimeString'">流水号日期</th>
		<th data-options="field:'repeatCycle'">循环类型</th>
		<th data-options="field:'isVerify',formatter:function(v,r,i){if(r.isVerify) return '是'; return '否';}">是否数据库验证</th> 
		<th data-options="field:'remark'">备注</th> 
		<th data-options="field:'manager',formatter:managerFmt">管理</th>
		</tr></thead>
	</table>
	<div id="serialNumberListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addSerialNumber()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteSerialNumber()">删除</a>
		</div>
		<div>
			<form id="form-productLine-search" method="post">
			流水号前缀：<input type="text" name="search-LIKE_prefix" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchProductLine()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-productLine-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	
	<div id="win-serialNumber-addoredit" class="easyui-window" title="新增流水单号" style="width:520px;height:400px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-serialNumber-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>唯一标实:</td><td><input class="easyui-textbox" id="key" name="key" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td>流水号前缀:</td><td><input class="easyui-textbox" id="prefix" name="prefix" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td>日期格式:</td><td><input class="easyui-textbox" name="dataString" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td>开始流水号:</td><td><input class="easyui-textbox" name="startNumber" type="text" data-options="required:true,width:'120px'" /></td>
				</tr>
				<tr>
					<td>流水号日期:</td><td>
						<input class="easyui-datebox" name="dateTimeString" data-options="formatter:dateFormatter,parser:dateParser,required:true,width:'120px'" />
					</td>
				</tr>  
				<tr>
					<td>循环类型:</td><td style="text-align: left;">
					<select class="easyui-combobox" name="repeatCycle" data-options="required:true,width:'120px'"><option value="day">day</option><option value="month">month</option><option value="year">year</option></select>
					</td>
				</tr>
				<tr>
					<td>是否数据库验证:</td><td style="text-align: left;">     
						<select class="easyui-combobox" name="isVerify" data-options="required:true,width:'120px'"><option value="false">否</option><option value="true">是</option></select>
					</td>
				</tr>
				<tr>
					<td>备注:</td><td>
						<input class="easyui-textbox" name="remark" data-options="multiline:true" style="height:60px"></input>
					</td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddoreditSerialNumber()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetForm()">重置</a>
				</div>
			</form>
		</div>
	</div>

</body>
</html>
