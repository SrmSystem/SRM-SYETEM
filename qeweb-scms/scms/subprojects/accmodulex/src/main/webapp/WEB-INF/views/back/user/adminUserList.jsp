<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.service.ShiroDbRealm.ShiroUser"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ page import="org.apache.shiro.SecurityUtils"%>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<% ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal(); %>
<html>
<head>
	<title><spring:message code="vendor.user.userManagement"/><!-- 用户管理 --></title>
	<script type="text/javascript" src="${ctx}/static/script/permission/user.js"></script>
    <script type="text/javascript" src="${ctx}/static/script/basedata/dialog.js"></script>
</head>

<body>

<div class="easyui-panel" data-options="region:'center',fit:true,border:false">
	<table id="datagrid-user-list" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/admin/user',method:'post',singleSelect:false,
		toolbar:'#userListToolbar',
		height:200,
		fit:true,
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		<th width="50px" data-options="field:'id',checkbox:true"></th>
		<th width="350px"  data-options="field:'manager',formatter:managerRightFmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
		<th width="150px"  data-options="field:'loginName'"><spring:message code="vendor.userName"/><!-- 用户名 --></th>
		<th width="150px"  data-options="field:'name'"><spring:message code="vendor.name"/><!-- 姓名 --></th>
		<th width="80px" data-options="field:'enabledStatus',formatter:function(v,r,i){return getEnableStatus(v,r.effectiveTime);}"><spring:message code="vendor.user.accountStatus"/><!-- 账户状态 --></th>
			
		<th width="150px"  data-options="field:'dep'"><spring:message code="vendor.user.subordinateDepartment"/><!-- 所属部门 --></th>
		<th width="150px"  data-options="field:'effectiveTime'"><spring:message code="vendor.user.accountPeriod"/><!-- 账户有效期 --></th>
					
		<th width="150px"  data-options="field:'email',width:120">Email</th>
		<th width="150px"  data-options="field:'classSystem',formatter:function(v,r,i){   if (r.classSystem == null ) { return '' } else  { return r.classSystem.name;}} "><spring:message code="vendor.user.shifts"/><!-- 班制 --></th>
		<th width="150px"  data-options="field:'registerDate'"><spring:message code="vendor.RegistrationTime"/><!-- 注册时间 --></th>
		<th width="150px"  data-options="field:'companyName'"><spring:message code="vendor.user.companyAffiliation"/><!-- 所属公司 --></th>
		<th width="150px"  data-options="field:'companyCode'"><spring:message code="vendor.user.companyCode"/><!-- 所属公司编码 --></th>
		
		<th data-options="field:'companyId',hidden:true"></th>
		</tr></thead>
	</table>
</div>	
	<div id="userListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addUser(0)"><spring:message code="vendor.user.addAACUser"/><!-- 新增AAC内部用户 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addUser(1)"><spring:message code="vendor.user.newSupplier"/><!-- 新增供应商 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="editUser()"><spring:message code="vendor.modification"/><!-- 修改 --></a>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteUser()">删除</a> -->
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="resetPass()"><spring:message code="vendor.user.resetPassword"/>重置密码</a>
		</div>
		<div>
			<form id="form-user-search" method="post">
			<spring:message code="vendor.userName"/><!-- 用户名 -->：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.name"/><!-- 姓名 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.companyName"/><!-- 公司名称 -->：<input type="text" name="search-LIKE_company.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.companyCode"/><!-- 公司编码 -->：<input type="text" name="search-LIKE_company.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.companyType"/><!-- 公司类型 -->：<select type="select" name="search-EQ_company.roleType" class="easyui-combobox" data-options="editable:false" style="width:80px;">
				<option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option>
				<option value="0"><spring:message code="vendor.purchasers"/><!-- 采购商 --></option>
				<option value="1"><spring:message code="vendor.suppliers"/><!-- 供应商 --></option>
			<!-- 	<option value="2">仓储商</option>
				<option value="3">运输商</option> -->
			</select>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchUser()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-user-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
		<div id="win-user-log" class="easyui-window" title="日志" style="width:600px;height:450px" data-options="modal:true,closed:true">
			<table id="datagrid-user-log-list" title="日志" class="easyui-datagrid"
				data-options="method:'post',
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
					<thead><tr>
					<th data-options="field:'id',hidden:true"></th>      
					<th data-options="field:'optUserName'"><spring:message code="vendor.user.operator"/><!-- 操作人 --></th>
					<th data-options="field:'pcName'"><spring:message code="vendor.user.ipAddress"/><!-- ip地址 --></th>
					<th data-options="field:'contentStr'"><spring:message code="vendor.content"/><!-- 内容 --></th>
					<th data-options="field:'createTime'"><spring:message code="vendor.operatingTime"/><!-- 操作时间 --></th>
					</tr></thead>
			</table>
		</div>
	
	<div id="win-user-addoredit" class="easyui-window" title="新增用户" style="width:400px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-user-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<input id="companyId" name="companyId" type="hidden" value=""/>
					<tr id="vendorDiv">
						<td><spring:message code="vendor.user.superiorOrganization"/><!-- 上级组织 -->:</td><td><input id="companyName"  class="easyui-textbox"  style="width:200px;" data-options="required:false,editable:false"/>
						<a href="javascript:;" class="easyui-linkbutton" onclick="lookUser()"><spring:message code="vendor.choose"/><!-- 选择 --></a>
						</td>
					</tr>
					<tr id="buyerDiv">
						<td><spring:message code="vendor.user.companyAffiliation"/><!-- 所属公司 -->:</td><td><input class="easyui-textbox" name="aacCompany" type="text"
						data-options="required:false" style="width:200px;"
					/></td>
					<tr>
					<td><spring:message code="vendor.userName"/><!-- 用户名 -->:</td><td><input class="easyui-textbox" id="loginName" name="loginName" type="text" style="width:200px;"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.name"/><!-- 姓名 -->:</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true" style="width:200px;"
					/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.user.subordinateDepartment"/><!-- 所属部门 -->:</td><td><input class="easyui-textbox" name="dep" type="text"
						data-options="required:false" style="width:200px;"
					/></td>
				</tr>
				
								<tr>
					<td><spring:message code="vendor.user.accountPeriod"/><!-- 账户有效期 -->:</td><td><input class="easyui-datebox"  id="dd" name="effectiveTime" type="text"
						data-options="required:true" style="width:200px;"
					/></td>
				</tr>
				<tr>
					<td>Email:</td><td><input class="easyui-textbox"  id="email" name="email" validType="email" type="text"
						data-options="required:true" style="width:200px;"
					/></td>
				</tr>
				
				<tr>
					<td><spring:message code="vendor.user.shifts"/><!-- 班制 -->:</td><td><input class="easyui-combobox"  id ="classSystemId"   name="classSystemId"  type="text"
						data-options="required:true" style="width:200px;"
					/></td>
				</tr>
				
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditUser()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-user-addoredit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
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
	<!-- 	<th data-options="field:'manager',formatter:managerFmt">操作</th> -->
		<th data-options="field:'code'"><spring:message code="vendor.coding"/><!-- 编码 --></th>
		<th data-options="field:'name'"><spring:message code="vendor.appellation"/><!-- 名称 --></th>
		<th data-options="field:'registerTime'"><spring:message code="vendor.RegistrationTime"/><!-- 注册时间 --></th>
	<!-- 	<th data-options="field:'_orgType'">组织级别</th>
		<th data-options="field:'_roleType'">组织类型</th> -->
      </tr>
    </thead>
  </table>
  <div id="ttt">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe()"><spring:message code="vendor.chooseBack"/><!-- 选择带回 --></a>    
      <form id="form2" method="post">
                     <spring:message code="vendor.coding"/><!-- 编码 -->：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.appellation"/><!-- 名称 -->：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a> 
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form2').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>     
      </form>
    </div>
  </div>   
</div> 
<div id="win-user-role" class="easyui-dialog" title="角色列表" style="width:860px;height:460px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<input type="hidden" itemId="userId" value="0"/>
	<div style="width: 100%;height:90%">
		
			<table id="datagrid-role-list" title="角色列表" class="easyui-datagrid"
			   data-options="method:'post',singleSelect:false,
			   toolbar:'#roleListToolbar1',
			   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
			  >
			  <thead><tr>
			  <th data-options="field:'id',hidden:true"></th>
			  <th data-options="field:'code',formatter:function(v,r,i){return r.role.code;}"><spring:message code="vendor.characterEncoding"/><!-- 角色编码 --></th>
			  <th data-options="field:'name',formatter:function(v,r,i){return r.role.name;}"><spring:message code="vendor.characterName"/><!-- 角色名称 --></th>
			  </tr></thead>
		     </table>
		     <div id="roleListToolbar1" style="padding:5px;">
			   <div>
			     <form id="form-role-user-search" method="post">
				         <spring:message code="vendor.characterEncoding"/><!-- 角色编码 -->：<input type="text" name="search-LIKE_role.code" class="easyui-textbox" style="width:80px;"/>
				         <spring:message code="vendor.characterName"/><!-- 角色名称 -->：<input type="text" name="search-LIKE_role.name" class="easyui-textbox" style="width:80px;"/>
				   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchRole()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
				 </form>
			   </div>
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
					<th width="150px"  data-options="field:'oper',formatter:userDataFmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>
					<th width="150px"  data-options="field:'dataCode'"><spring:message code="vendor.user.dataAccessCoding"/><!-- 数据权限编码 --></th>
					<th width="150px"  data-options="field:'dataName'"><spring:message code="vendor.user.dataAccessName"/><!-- 数据权限名称 --></th>
					<th width="150px"  data-options="field:'dataClazz'"><spring:message code="vendor.user.dataClasses"/><!-- 数据类 --></th>
					<th width="150px"  data-options="field:'remark'"><spring:message code="vendor.remark"/><!-- 备注 --></th>
					<th width="150px"  data-options="field:'roleId',hidden:true"><spring:message code="vendor.user.characterID"/><!-- 角色ID --></th>
				</tr>
			</thead>
		</table>
	</div>
	<div id="dialog-data" class="easyui-dialog" data-options="closed:true,title:'数据权限'" style="width:60%;height:80%">
	</div>
	
	
			
<script type="text/javascript">
/* $(function(){
	$('#datagrid-user-list').datagrid();
}); */
function searchRole(){
	var searchParamArray = $('#form-role-user-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-role-list').datagrid('load',searchParams);
}
</script>
</body>
</html>
