<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>角色管理</title>
	<script type="text/javascript" src="${ctx}/static/script/permission/role.js"></script>
</head>
<body style="margin:0;padding:0;">
	<table id="datagrid-role-list" title="角色列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/admin/role',method:'post',singleSelect:false,
		toolbar:'#roleListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'oper',formatter:Role.rowManagerFmt,width:200">操作</th>
		<th data-options="field:'code',width:100,formatter:function(v,r,i){if(r.roleType==1) 
			return '<font color=red>'+v+'</font>'; else return v;}">角色编码</th>
		<th data-options="field:'name',width:100">角色名</th>
		<th data-options="field:'remark',width:100">备注</th>
		<th data-options="field:'roleType',hidden:true">角色类型</th>
		</tr></thead>
	</table>
	<div id="roleListToolbar" style="padding:5px;">
		<div>
		<shiro:hasPermission  name="sys:role:add">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addRole()">新增</a>
		</shiro:hasPermission>
		<shiro:hasPermission name="sys:role:del">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="deleteRole()">删除</a>
		</shiro:hasPermission>
		</div>
		<div>
			<form id="form-role-search" method="post">
			角色编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			角色名：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchRole()">查询</a>
			</form>
		</div>
	</div>
	<div id="win-role-addoredit" class="easyui-dialog" title="新增角色" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-role-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>角色编码:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true,invalidMessage:'该编码已存在'"
					/>
					</td>
				</tr>
				<tr>
					<td>角色名称:</td><td><input class="easyui-textbox" name="name" type="text" id="name"
						data-options="required:true,invalidMessage:'该名称已存在'"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" name="remark" type="text" id="remark" data-options=""/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditRole()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-role-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
	<div id="win-role-menu" class="easyui-dialog" title="权限分配" style="width:600px;height:450px"
	data-options="iconCls:'icon-add',modal:true,closed:true,footer:'#tool-role-menu'">
	  <div class="easyui-panel" data-options="fit:true">
	    <ul id="tree-role-menu" class="easyui-tree" data-options="
	      url:'${ctx}/manager/admin/menu/getMenuEasyuiTree'
	      ,checkbox:true
	    ">
	    </ul>
	  </div>
	</div>
	<div id="tool-role-menu">
	  <a href="javascript:;" class="easyui-linkbutton" id="btn-addMenuRight" data-options="iconCls:'icon-ok'">提交</a>
	  <a href="javascript:;" class="easyui-linkbutton" onclick="$('#tree-role-menu').tree('reload')" data-options="iconCls:'icon-clear'">重置</a>
	</div>	
	<div id="win-role-user" class="easyui-dialog" title="用户分配" style="width:860px;height:460px"
		data-options="iconCls:'icon-add',modal:true,closed:true,footer:'#tool-role-role'">
	<input type="hidden" itemId="roleId" value="0"/>
	<div style="width: 100%;height:90%">
		<div style="float: left;width: 55%;height: 100%">
			<table id="datagrid-user-list" title="用户列表" class="easyui-datagrid"
			   data-options="url:'${ctx}/manager/admin/user',method:'post',singleSelect:false,
			   toolbar:'#userListToolbar',
			   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
			  >
			  <thead><tr>
			  <th data-options="field:'id',checkbox:true"></th>
			  <th data-options="field:'loginName'">登录名</th>
			  <th data-options="field:'name'">用户名</th>
			  <th data-options="field:'companyName',formatter:function(v,r,i){return r.company.name;}">所属公司</th>
			  </tr></thead>
		     </table>
		     <div id="userListToolbar" style="padding:5px;">
			   <div>
			     <form id="form-user-search" method="post">
				         登录名：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width:80px;"/>
				         用户名：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
				   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchUser()">查询</a>
				 </form>
			   </div>
		     </div>
		</div>
		<div style="float: left;width: 10%;height: 100%">
			<div class="easyui-panel" title="操作" data-options="headerCls:'text-center'">
		      <div class="center-block" style="width:16px;">
	            <p><a class="easyui-linkbutton" data-options="iconCls:'icon-arrow_right'" href="javascript:;" onclick="Role.selectUser()"></a></p>
	            <p><a class="easyui-linkbutton" data-options="iconCls:'icon-arrow_left'" href="javascript:;" onclick="Role.removeUser()"></a></p>
	          </div>
		    </div>
		</div>
		<div style="float: left;width: 35%;height: 100%">
			<div class="easyui-panel" title="已分配用户" data-options="fit:true">
		      <select id="sel-role-user" multiple="multiple" style="width:100%;height:99%;">
		      </select>
		    </div>
		</div>
	</div> 
	</div>
	<div id="tool-role-role">
	  <a href="javascript:;" class="easyui-linkbutton" id="btn-addRoleUser" data-options="iconCls:'icon-ok'" onclick="Role.addRoleUser()">提交</a>
	  <a href="javascript:;" class="easyui-linkbutton" onclick="removeSelectOption()" data-options="iconCls:'icon-clear'">重置</a>
	</div>

	<div id="win-role-data" class="easyui-dialog" title="数据权限" style="width: 860px; height: 460px"
		data-options="iconCls:'icon-add',modal:true,closed:true,footer:'#tool-role-role2'">
		<input type="hidden" itemId="roleId" value="0" />
		<table id="datagrid-role-data-list" title="数据权限设置" class="easyui-datagrid"
			data-options="fit:true,method:'post',singleSelect:false,
	   		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
			<thead>
				<tr>
					<th width="130px" data-options="field:'oper',formatter:Role.rowDataFmt">操作</th>
					<th data-options="field:'dataCode'">数据权限编码</th>
					<th data-options="field:'dataName'">数据权限名称</th>
					<th data-options="field:'dataClazz'">数据类</th>
					<th data-options="field:'remark'">备注</th>
					<th data-options="field:'roleId',hidden:true">角色ID</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dialog-data" class="easyui-dialog" data-options="closed:true,title:'数据权限'" style="width:60%;height:80%">
	</div>

<script type="text/javascript">
function searchRole(){
	var searchParamArray = $('#form-role-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-role-list').datagrid('load',searchParams);
}
function searchUser(){
	var searchParamArray = $('#form-user-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-user-list').datagrid('load',searchParams);
}

function addRole(){
	$('#win-role-addoredit').window({
		iconCls:'icon-add',
		title:'新增角色'
	});
	$('#code').textbox('enable');
	$('#form-role-addoredit').form('clear');
	$('#id').val(0);
	$('#win-role-addoredit').window('open');
}

function submitAddorEditRole(){
	var url = '${ctx}/manager/admin/role/addNewRole';
	var sucMeg = '添加角色成功！';
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = '${ctx}/manager/admin/role/update';
		sucMeg = '编辑角色成功！';
	}
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	$('#form-role-addoredit').form('submit',{
		ajax:true,
		url:url,
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			$.messager.progress('close');
			try{
			var result = eval('('+data+')');
			if(result.success){
				$.messager.alert('提示',sucMeg,'info');
				$('#win-role-addoredit').window('close');
				$('#datagrid-role-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
		
	});
}

function deleteRole(){
	var selections = $('#datagrid-role-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	for(var i = 0; i < selections.length; i ++) {
		if(selections[i].roleType == 1) {
			$.messager.alert('提示','包含系统内置角色无法删除！','info');
			return false;
		}
	}
	var params = $.toJSON(selections);
	$.messager.confirm('提示','确定删除操作？',function(r){
		if(r){
			$.ajax({
				url:'${ctx}/manager/admin/role/deleteRole',
				type:'POST',
				data:params,
				dataType:"json",
				contentType : 'application/json',
				success:function(result){
					if(result.success) {
						$.messager.success(result.msg);
					} else {
						$.messager.fail(result.msg);
					}
					$('#datagrid-role-list').datagrid('reload');
				}, 
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			});
		}
	});
}

function editRole(id, roleType){
	$('#win-role-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑角色'
	});
	if(roleType == 1) //系统内置角色
		$('#code').textbox('disable');
	else 
		$('#code').textbox('enable');
	$('#win-role-addoredit').window('open');
	$('#form-role-addoredit').form('load','${ctx}/manager/admin/role/getRole/'+id);
}

function removeSelectOption(){
	var select = $('#sel-role-user');
	var str = "";
	var opt = select[0];
	for(var i=opt.options.length-1; i>=0; i--)
	{
		opt.remove(i);
	}
}
$(function(){
			
});

</script>
</body>
</html>
