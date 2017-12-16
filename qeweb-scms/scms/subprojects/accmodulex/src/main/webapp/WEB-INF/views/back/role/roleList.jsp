<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.role.roleManagement"/><!-- 角色管理 --></title>
	<script type="text/javascript" src="${ctx}/static/script/permission/role.js"></script>
		<script type="text/javascript">
		function getTypeStatus(status) {
			if(status == 'Y')
				return '<spring:message code="vendor.role.suppliers"/>';/* 供应商 */
			else if(status == 'N')
				return '<spring:message code="vendor.role.purchasers"/>';/* 采购商 */
			else 
				return '';
		}
	</script>
</head>
<body style="margin:0;padding:0;">
	<table id="datagrid-role-list" title="角色列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/admin/role',method:'post',singleSelect:false,
		toolbar:'#roleListMainToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'oper',formatter:Role.rowManagerFmt,width:300"><spring:message code="vendor.operation"/><!-- 操作 --></th>
		<th data-options="field:'code',width:100,formatter:function(v,r,i){if(r.roleType==1) 
		return '<font color=red>'+v+'</font>'; else return v;}"><spring:message code="vendor.role.characterEncoding"/><!-- 角色编码 --></th>
		<th data-options="field:'name',width:100"><spring:message code="vendor.role.roleOf"/><!-- 角色名 --></th>
		<th data-options="field:'isVendor',formatter:function(v,r,i){return getTypeStatus(v);},width:100"><spring:message code="vendor.role.roleType"/><!-- 角色类型 --></th>
			
		<th data-options="field:'remark',width:100"><spring:message code="vendor.role.remark"/><!-- 备注 --></th>
		<th data-options="field:'roleType',hidden:true"><spring:message code="vendor.role.roleType"/><!-- 角色类型 --></th>
		</tr></thead>
	</table>
	<div id="roleListMainToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addRole()"><spring:message code="vendor.new"/><!-- 新增 --></a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteRole()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
		</div>
		<div>
			<form id="form-roleMain-search" method="post">
			<spring:message code="vendor.role.characterEncoding"/><!-- 角色编码 -->：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.role.roleOf"/><!-- 角色名 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchRole()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-roleMain-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
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
					<td><spring:message code="vendor.role.characterEncoding"/><!-- 角色编码 -->:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true,invalidMessage:'该编码已存在'"
					/>
					</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.role.characterName"/><!-- 角色名称 -->:</td><td><input class="easyui-textbox" name="name" type="text" id="name"
						data-options="required:true,invalidMessage:'该名称已存在'"
					/></td>
				</tr>
				
				<tr>
					<td><spring:message code="vendor.role.roleType"/><!-- 角色类型 -->:</td>
						<td><select   class="easyui-combobox"  id="isVendor" name="isVendor" data-options="required:true,width:'138'">
							<option value="Y"><spring:message code="vendor.role.suppliers"/><!-- 供应商 --></option>
							<option value="N"><spring:message code="vendor.role.purchasers"/><!-- 采购商 --></option>
						</select></td>
					
				</tr>
				
				<tr>
					<td><spring:message code="vendor.role.remark"/><!-- 备注 -->:</td><td><input class="easyui-textbox" name="remark" type="text" id="remark" data-options=""/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditRole()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-role-addoredit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
	<div id="win-role-menu" class="easyui-dialog" title="权限分配" style="width:600px;height:450px"
	data-options="iconCls:'icon-add',modal:true,closed:true,footer:'#tool-role-menu'">
	  <div class="easyui-panel" data-options="fit:true">
	    <ul id="tree-role-menu" class="easyui-tree" data-options="checkbox:true">
	    </ul>
	  </div>
	</div>
	<div id="tool-role-menu">
	  <a href="javascript:;" class="easyui-linkbutton" id="btn-addMenuRight" data-options="iconCls:'icon-ok'"><spring:message code="vendor.submit"/><!-- 提交 --></a>
	  <a href="javascript:;" class="easyui-linkbutton" onclick="$('#tree-role-menu').tree('reload')" data-options="iconCls:'icon-clear'"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
	</div>	
	<div id="win-role-user" class="easyui-dialog" title="用户分配" style="width:860px;height:460px"
		data-options="iconCls:'icon-add',modal:true,closed:true,footer:'#tool-role-role'">
	<input type="hidden" itemId="roleId" value="0"/>
	<div style="width: 100%;height:90%">
		<div id = "roleList" style="float: left;width: 55%;height: 100%">
			<table id="datagrid-user-list" title="用户列表"
			   data-options="url:'${ctx}/manager/admin/user',method:'post',singleSelect:false,
			   toolbar:'#userListToolbar',
			   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
			  >
			  <thead><tr>
			  <th data-options="field:'id',checkbox:true"></th>
			  <th data-options="field:'loginName'"><spring:message code="vendor.role.userName"/><!-- 用户名 --></th>
			  <th data-options="field:'name'"><spring:message code="vendor.role.name"/><!-- 姓名 --></th>
			  <th data-options="field:'aacCompany'"><!-- 所属公司 --></th>
			  </tr></thead>
		     </table>
		     <div id="userListToolbar" style="padding:5px;">
			   <div>
			     <form id="form-user-search" method="post">
				         <spring:message code="vendor.role.userName"/><!-- 用户名 -->：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width:80px;"/>
				        <spring:message code="vendor.role.name"/> <!-- 姓名 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
				   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchUser()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
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
	  <a href="javascript:;" class="easyui-linkbutton" id="btn-addRoleUser" data-options="iconCls:'icon-ok'" onclick="Role.addRoleUser()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
	  <a href="javascript:;" class="easyui-linkbutton" onclick="removeSelectOption()" data-options="iconCls:'icon-clear'"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
	</div>

	<div id="win-role-data" class="easyui-dialog" title="数据权限" style="width: 860px; height: 460px"
		data-options="iconCls:'icon-add',modal:true,closed:true,footer:'#tool-role-role2'">
		<input type="hidden" itemId="roleId" value="0" />
		<table id="datagrid-role-data-list" title="数据权限设置" class="easyui-datagrid"
			data-options="fit:true,method:'post',singleSelect:false,
	   		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
			<thead>
				<tr>
					<th width="130px" data-options="field:'oper',formatter:Role.rowDataFmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
					<th data-options="field:'dataCode'"><spring:message code="vendor.role.dataAccessCoding"/><!-- 数据权限编码 --></th>
					<th data-options="field:'dataName'"><spring:message code="vendor.role.dataAccessName"/><!-- 数据权限名称 --></th>
					<th data-options="field:'dataClazz'"><spring:message code="vendor.role.dataClasses"/><!-- 数据类 --></th>
					<th data-options="field:'remark'"><spring:message code="vendor.role.remark"/><!-- 备注 --></th>
					<th data-options="field:'roleId',hidden:true"><spring:message code="vendor.role.characterID"/><!-- 角色ID --></th>
				</tr>
			</thead>
		</table>
	</div>
	
	<div id="win-role-log" class="easyui-window" title="日志" style="width:600px;height:450px" data-options="modal:true,closed:true">
			<table id="datagrid-role-log-list" title="日志" class="easyui-datagrid"
				data-options="method:'post',
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
					<thead><tr>
					<th data-options="field:'id',hidden:true"></th>      
					<th data-options="field:'optUserName'"><spring:message code="vendor.role.operator"/><!-- 操作人 --></th>
					<th data-options="field:'pcName'"><spring:message code="vendor.role.ipAddress"/><!-- ip地址 --></th>
					<th data-options="field:'contentStr'"><spring:message code="vendor.role.content"/><!-- 内容 --></th>
					<th data-options="field:'createTime'"><spring:message code="vendor.role.operatingTime"/><!-- 操作时间 --></th>
					</tr></thead>
			</table>
		</div>
	
	<div id="dialog-data" class="easyui-dialog" data-options="closed:true,title:'数据权限'" style="width:60%;height:80%">
	</div>

<script type="text/javascript">
function searchRole(){
	var searchParamArray = $('#form-roleMain-search').serializeArray();
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
		title:'<spring:message code="vendor.role.newRole"/>'/* 新增角色 */
	});
	$('#code').textbox('enable');
	$('#form-role-addoredit').form('clear');
	$('#id').val(0);
	$('#win-role-addoredit').window('open');
}

function submitAddorEditRole(){
	
	var code = $("#code").val();
	var name = $("#name").val();
	var isVendor = $("#isVendor").combobox('getValue');
	var remark = $("#remark").val();
	if(code==undefined || code=='' || code==null){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.role.encodingCannotEmpty"/>'/* 角色编码不能为空 */,'info');
		return;
	}
	if(name==undefined || name=='' || name==null){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.role.nameCannotEmpty"/>'/* 角色名称不能为空 */,'info');
		return;
	}
	if(isVendor==undefined || isVendor=='' || isVendor==null){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.role.typeCannotEmpty"/>'/* 角色类型不能为空 */,'info');
		return;
	}
	
	var url = '${ctx}/manager/admin/role/addNewRole';
	var sucMeg = '<spring:message code="vendor.role.addRoleSuccess"/>！';/* 添加角色成功 */
	if($('#id').val()!=0 && $('#id').val()!='0'){
		url = '${ctx}/manager/admin/role/update';
		sucMeg = '<spring:message code="vendor.role.editRoleSuccess"/>！';/* 编辑角色成功 */
	}
	$.messager.progress({
		title:'<spring:message code="vendor.prompting"/>',/* 提示 */
		msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	
	var paramArray = $('#form-role-addoredit').serializeArray();
	var params = $.jqexer.formToJson(paramArray);
	$.ajax({
		url:url,
		type:'POST',
		data:JSON.stringify(params),
		dataType:"json",
		contentType : 'application/json',
		success:function(result){
			$.messager.progress('close');
			try{
				
				if(result.success){
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,sucMeg,'info');
					$('#win-role-addoredit').window('close');
					$('#datagrid-role-list').datagrid('reload');
				}else{
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,result.msg,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data,'error');
			}
		}, 
		error:function(data) {
			$.messager.fail(data.responseText);
		}
	}); 
}

function deleteRole(){
	var selections = $('#datagrid-role-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.role.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(var i = 0; i < selections.length; i ++) {
		if(selections[i].roleType == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.role.built-inDeleted"/>！'/* 包含系统内置角色无法删除 */,'info');
			return false;
		}
	}
	var params = $.toJSON(selections);
	$.messager.confirm('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.role.determineDeleteOperation"/>？'/* 确定删除操作 */,function(r){
		if(r){
 			$.ajax({
				url:'${ctx}/manager/admin/role/validateDeleteRole',
				type:'POST',
				data:params,
				dataType:"json",
				contentType : 'application/json',
				success:function(result){
					if(result.success) { 
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
					 } else {
						$.messager.fail(result.msg);
					}
				}, 
				error:function(data) {
					$.messager.fail(data.responseText);
				}
			}); 
			

		}
	});
}

/* function editRole(id, roleType){
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
} */

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
