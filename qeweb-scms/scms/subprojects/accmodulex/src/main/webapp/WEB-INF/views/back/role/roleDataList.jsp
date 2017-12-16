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
	<script type="text/javascript" src="${ctx}/static/script/permission/user.js"></script>
</head>
<body style="margin:0;padding:0;">
	
	<div data-options="region:'center',footer:'#tool-role-data'" style="width: 98%; height: 90%">
		<table id="datagrid-role-data" title="数据列表" class="easyui-datagrid"
			data-options="fit:true,
			url:'${ctx}/manager/admin/roledata/getDataList/${dataIds}',idField : 'id',method:'post',singleSelect:false,
			queryParams : {'search-roleId' : '${roleId}', 'search-roleDataCfgId' : '${roleDataCfgId}'},
			onLoadSuccess : function(data){
					var allDatas = data.rows;
					for(var i = 0; i < allDatas.length; i ++) {
					    if( allDatas[i].isCheck == 1 ){
					     $(this).datagrid('selectRow', i);
					    }
					}
				},
			toolbar:'#roleListToolbar',
			pagination:true,rownumbers:true,pageSize:300,pageList:[300,500,800]"
			>
			<thead><tr>
			<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'code'"><spring:message code="vendor.coding"/><!-- 编码 --></th>
				<th data-options="field:'name'"><spring:message code="vendor.appellation"/><!-- 名称 --></th>
			</tr></thead>
		</table>
	</div>
	<div id="tool-role-data">
	  <c:if test="${type eq 'User' }">
	  	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="User.addUserData(${roleId}, ${roleDataCfgId})"><spring:message code="vendor.submit"/><!-- 提交 --></a>
	  </c:if>
	  <c:if test="${type eq 'Role' }">
		  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Role.addRoleData(${roleId}, ${roleDataCfgId})"><spring:message code="vendor.submit"/><!-- 提交 --></a>
	  </c:if>
	  <a href="javascript:;" class="easyui-linkbutton" onclick="loadData()" data-options="iconCls:'icon-clear'"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
	</div>
	<div id="roleListToolbar" style="padding:5px;">
		<div>
			<form id="form-role-search" method="post">
			<input type="hidden" name="search-roleId" value="${roleId}"/>
			<input type="hidden" name="search-roleDataCfgId" value="${roleDataCfgId}"/>
			<spring:message code="vendor.coding"/><!-- 编码 -->：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.appellation"/><!-- 名称 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchRoleData()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-role-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>


  <script type="text/javascript">
  		var dataIds = '${dataIds}';
  		
  		function searchRoleData(){
  			var searchParamArray = $('#form-role-search').serializeArray();
  			var searchParams = $.jqexer.formToJson(searchParamArray);
  			$('#datagrid-role-data').datagrid('load',searchParams);
  		}
   </script>
</body>
</html>
