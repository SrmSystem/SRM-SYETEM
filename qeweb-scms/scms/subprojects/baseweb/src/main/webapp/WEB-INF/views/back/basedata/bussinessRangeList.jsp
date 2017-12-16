<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>业务范围管理</title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/bussinessRange.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-bussinessRange-list" title="业务范围列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/bussinessRange',method:'post',singleSelect:false,
		toolbar:'#bussinessRangeListToolbar',queryParams:{'query-EQ_bussinessType':0},
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="80px" data-options="field:'manager',formatter:BussinessRange.operateFmt">操作</th>
		<th width="150px" data-options="field:'code'">公司业务范围编号</th>
		<th width="150px" data-options="field:'name'">公司业务范围名称</th>
		<th width="150px" data-options="field:'remark'">备注</th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
		</tr></thead>
	</table>
	<div id="bussinessRangeListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BussinessRange.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="BussinessRange.abolish()">作废</a>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="BussinessRange.del()">删除</a> -->
		</div>
		<div>
			<form id="form-bussinessRange-search" method="post">
			<input name="query-EQ_bussinessType" type="hidden" value="0"/>
			公司业务范围编号：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			公司业务范围名称：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="BussinessRange.query()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-bussinessRange-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-bussinessRange-addoredit" class="easyui-dialog" title="新增业务范围" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-bussinessRange-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>公司业务范围编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="prompt:'编号由系统自动产生'"
					/>
					</td>
				</tr>
				<tr>
					<td>公司业务范围名称:</td><td><input class="easyui-textbox" id="gongsiname" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" id="gongsiremark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="BussinessRange.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-bussinessRange-addoredit').form('reset')">重置</a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="BussinessRange.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="BussinessRange.reset()">重置</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
