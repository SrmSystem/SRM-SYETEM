<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title></title>

</head>

<body>
	 
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="subCheck()">提交审核</a>
						
	  <form id="form-vendorQuotaCheck" method="post" >
	  		<table style="padding:10px;width: 90%;margin: auto;">
			<tr>
				<td width="50%">科长:
					<input type="text" id="personId1" name="signPerson1Id" value="" hidden="true" />
					<input type="text" id="singPerson1" name="person1Name" class="easyui-textbox" style="width:100px;" data-options="required:true,editable:false" value=""/>
			  		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('1','CountersignPerson')"></a>	
				</td>
				<td width="50%">采购部长: 
					<input type="text" id="personId2" name="signPerson2Id" value="" hidden="true" />
					<input type="text" id="singPerson2" name="person2Name" class="easyui-textbox" style="width:100px;" data-options="required:false,editable:false" value=""/>
			  		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('2','CountersignPerson')"></a>		
				</td>
			</tr>
			<tr>
				<td width="50%">采购副总经理: 
					<input type="text" id="personId3" name="signPerson3Id" value="" hidden="true" />
					<input type="text" id="singPerson3" name="person3Name" class="easyui-textbox" style="width:100px;" data-options="required:false,editable:false" value=""/>
			  		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('3','CountersignPerson')"></a>	
				</td>
				<td width="50%">技术中心: 
					<input type="text" id="personId4" name="signPerson4Id" value="" hidden="true" />
					<input type="text" id="singPerson4" name="person4Name" class="easyui-textbox" style="width:100px;" data-options="required:false,editable:false" value=""/>
			  		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('4','CountersignPerson')"></a>		
				</td>
			</tr>
			<tr>
				<td>审批部门: 
						<select class="easyui-combobox" id="checkDep" name="checkDep"  data-options="required:true,validType:'comboVry'" style="width: 100px">
						<option value="">-全部-</option>
						<option  value="1" >技术审批</option>
						<option value="0" >工艺审批</option>
						</select>
					
				</td>
				<td width="50%">
				</td>
			</tr>

		
			</table>
	  
		 <input type="hidden" name="tableDatas" id="tableDatas" value="${paramx}"></input>
	  </form>



<!-- 会签人信息 -->
<div id="win-signPerson-detail" class="easyui-window" title="会签人信息" 
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="userListToolbar" style="padding:5px;">
			<input type="text" id="tempVal" hidden="true"/>
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="UserManage.choice('datagrid-signPerson-list')">选择 </a>
			</div>
			<div>
			<form id="form-signPerson-search" method="post">
				用户编码：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width:80px;"/>
				用户名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="UserManage.search()">查询</a>
		</form>
	</div>
		</div>
		<table id="datagrid-signPerson-list" title="会签人信息"
			data-options="method:'post',singleSelect:false,
			pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,30]">
			<thead><tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
				<th width="30%" data-options="field:'loginName'">用户编码</th>
				<th width="30%" data-options="field:'name'">用户名称</th>
			</tr></thead>
		</table>
	</div>
</div>
	<script type="text/javascript">

	
    </script>
</body>



