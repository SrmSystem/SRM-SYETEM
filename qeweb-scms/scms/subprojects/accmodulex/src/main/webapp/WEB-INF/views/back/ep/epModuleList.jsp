<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>询比价模板列表</title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/ep/epModuleManage.js"></script>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-epModule-list" title="询比价列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/ep/epModule',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#epModuleToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>   
		<th width="50px" data-options="field:'id',checkbox:true"></th>
		<th width="130px" data-options="field:'code',formatter:codeFmt">编号</th>
		<th width="130px" data-options="field:'name'">名称</th>
		<th width="100px" data-options="field:'remarks'">备注</th>
		<th width="80px" data-options="field:'isDefault',formatter:function(v,r,i){return r.isDefault==1?'是':'否';}">是否默认模板</th>
		</tr></thead>
	</table>
	<div id="epModuleToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addEpModule()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="updateEpModule()">修改</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteEpModule()">删除</a>
		</div>
		<div>
			<form id="form-epModule-search" method="post">
			编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>  
			名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>  
			是否默认模板：<select class="easyui-combobox" name="search-EQ_isDefault" data-options="editable:false"><option value="">-全部-</option><option value="0">否</option><option value="1">是</option></select> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchEpModuleList()">查询</a>  
			</form>
		</div>
	</div> 
</body>
</html>
