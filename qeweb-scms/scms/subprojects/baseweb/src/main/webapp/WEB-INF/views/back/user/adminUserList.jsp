<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser"%>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<% ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal(); %>
<html>
<head>
	<title>用户管理</title>
	<script type="text/javascript" src="${ctx}/static/script/permission/user.js"></script>
</head>

<body>
<div class="easyui-panel" data-options="region:'center',fit:true,border:false">
	<table id="datagrid-user-list" class="easyui-datagridx"
		data-options="url:'${ctx}/manager/admin/user',method:'post',singleSelect:false,
		queryParams : {'search-EQ_company.roleType':0},
		toolbar:'#userListToolbar',
		height:200,
		fit:true,
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th width="50px" data-options="field:'id',checkbox:true"></th>
		<th width="150px"  data-options="field:'manager',width:80,formatter:managerRightFmt">操作</th>
		<th width="150px"  data-options="field:'loginName'">登录名</th>
		<th width="150px"  data-options="field:'name'">昵称</th>
		<th width="150px"  data-options="field:'email',width:120">Email</th>
		<th width="150px"  data-options="field:'registerDate'">注册时间</th>
		<th width="150px"  data-options="field:'companyName',formatter:function(v,r,i){return r.company.name;}">所属公司</th>
		<th width="150px"  data-options="field:'companyCode',formatter:function(v,r,i){return r.company.code;}">所属公司编码</th>
		</tr></thead>
	</table>
</div>	
	<div id="userListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addUser()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="editUser()">修改</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteUser()">删除</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="resetPass()">重置密码</a>
		</div>
		<div>
			<form id="form-user-search" method="post">
			登录名：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width:80px;"/>
			昵称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			公司名称：<input type="text" name="search-LIKE_company.name" class="easyui-textbox" style="width:80px;"/>
			公司编码：<input type="text" name="search-LIKE_company.code" class="easyui-textbox" style="width:80px;"/>
			公司类型：<select type="select" name="search-EQ_company.roleType" class="easyui-combobox" style="width:80px;">
				<option value="">请选择</option>
				<option value="0">采购商</option>
				<option value="1">供应商</option>
				<option value="2">仓储商</option>
				<option value="3">运输商</option>
			</select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchUser()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-user-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-user-addoredit" class="easyui-window" title="新增用户" style="width:400px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-user-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<input id="companyId" name="companyId" type="hidden" value=""/>
					<td>上级组织:</td><td><input id="companyName"  class="easyui-textbox"  style="width:200px;" data-options="required:true,editable:false"/>
					<a href="javascript:;" class="easyui-linkbutton" onclick="lookUser()">选择</a>
					</td>
				</tr>
				<tr>
					<td>登陆名:</td><td><input class="easyui-textbox" id="loginName" name="loginName" type="text" style="width:200px;"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>昵称:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true" style="width:200px;"
					/></td>
				</tr>
				<tr>
					<td>Email:</td><td><input class="easyui-textbox" name="email" validType="email" type="text"
						data-options="required:true" style="width:200px;"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditUser()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-user-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
<div id="kk" class="easyui-dialog" title="添加组织" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#ttt'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"></th>
<!-- 		<th data-options="field:'manager',formatter:managerFmt">操作</th> -->
		<th data-options="field:'code'">编码</th>
		<th data-options="field:'name'">名称</th>
		<th data-options="field:'registerTime'">注册时间</th>
		<th data-options="field:'_orgType'">组织级别</th>
		<th data-options="field:'_roleType'">组织类型</th>
      </tr>
    </thead>
  </table>
  <div id="ttt">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe()">选择带回</a>    
      <form id="form2" method="post">
                     编码：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			名称：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch()">查询</a>      
      </form>
    </div>
  </div>   
</div> 

<div id="win-user-data" class="easyui-window" title="数据权限" style="width: 860px; height: 460px"
		data-options="iconCls:'icon-add',modal:true,closed:true,footer:'#tool-role-role2'">
		<input type="hidden" itemId="userId" value="0" />
		<table id="datagrid-user-data-list" title="数据权限设置" class="easyui-datagrid"
			data-options="fit:true,method:'post',singleSelect:false,
	   		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
			<thead>
				<tr>
					<th width="130px"  data-options="field:'oper',formatter:userDataFmt">操作</th>
					<th width="150px"  data-options="field:'dataCode'">数据权限编码</th>
					<th width="150px"  data-options="field:'dataName'">数据权限名称</th>
					<th width="150px"  data-options="field:'dataClazz'">数据类</th>
					<th width="150px"  data-options="field:'remark'">备注</th>
					<th width="150px"  data-options="field:'roleId',hidden:true">角色ID</th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dialog-data" class="easyui-dialog" data-options="closed:true,title:'数据权限'" style="width:60%;height:80%">
	</div>
<script type="text/javascript">
$(function(){
	$('#datagrid-user-list').datagrid();
});

</script>
</body>
</html>
