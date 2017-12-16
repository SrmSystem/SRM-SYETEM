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
	<script type="text/javascript" src="${ctx}/static/script/permission/user.js"></script>
</head>
<body style="margin:0;padding:0;">
	<div data-options="region:'center',footer:'#tool-role-data'" style="width: 98%; height: 90%">
	  	<table id="datagrid-role-data" title="数据列表" 
			data-options="fit:true,rownumbers:true"
			>
			<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'code',width:120">编码</th>
				<th data-options="field:'name',width:120">名称</th>
			</tr></thead>
		</table>
	</div>
	<div id="tool-role-data">
	  <c:if test="${type eq 'User' }">
	  	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="User.addUserData(${roleId}, ${roleDataCfgId})">提交</a>
	  </c:if>
	  <c:if test="${type eq 'Role' }">
		  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Role.addRoleData(${roleId}, ${roleDataCfgId})">提交</a>
	  </c:if>
	  <a href="javascript:;" class="easyui-linkbutton" onclick="loadData()" data-options="iconCls:'icon-clear'">重置</a>
	</div>
	
  <script type="text/javascript">
  		var dataIds = '${dataIds}';
		$(function(){
			//加载data数据
			loadData();
		});
		
		function loadData() {
			$('#datagrid-role-data').datagrid({
				url : '${ctx}/manager/admin/roledata/getDataList',
				queryParams : {'search-roleId' : '${roleId}', 'search-roleDataCfgId' : '${roleDataCfgId}'},
				onLoadSuccess : function(data){
					var datas = dataIds.split(",")
					var allDatas = data.rows;
					for(var i = 0; i < allDatas.length; i ++) {
						if(datas.indexOf(allDatas[i].id + '') > -1) {
							$(this).datagrid('selectRow', i);
						}
					}
				}
			});
		}
   </script>
</body>
</html>
