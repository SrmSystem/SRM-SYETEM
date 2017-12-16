<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.user.purchasingGroup"/><!-- 采购组 --></title>
</head>
<body style="margin:0;padding:0;">
	
	<div data-options="region:'center',footer:'#tool-group-data'" style="width: 98%; height: 90%">
		<table id="datagrid-group-data" title="数据列表" class="easyui-datagrid"
			data-options="fit:true,
			url:'${ctx}/manager/basedata/purchasingGroup/getCheckpurchasingGroupList/${dataIds}',idField : 'id',method:'post',singleSelect:false,
			onLoadSuccess : function(data){
					var allDatas = data.rows;
					for(var i = 0; i < allDatas.length; i ++) {
					    if( allDatas[i].isCheck == 1 ){
					     $(this).datagrid('selectRow', i);
					    }
					}
				},
			toolbar:'#groupListToolbar',
			pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
			>
			<thead><tr>
			<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'code'"><spring:message code="vendor.coding"/><!-- 编码 --></th>
				<th data-options="field:'name'"><spring:message code="vendor.appellation"/><!-- 名称 --></th>
			</tr></thead>
		</table>
	</div>
	<div id="tool-group-data">
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="addGroupData(${userId})"><spring:message code="vendor.submit"/><!-- 提交 --></a>
	  <a href="javascript:;" class="easyui-linkbutton" onclick="clearSelections()" data-options="iconCls:'icon-clear'"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
	</div>
	<div id="groupListToolbar" style="padding:5px;">
		<div>
			<form id="form-group-search" method="post">
			<spring:message code="vendor.coding"/><!-- 编码 -->：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.appellation"/><!-- 名称 -->：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchGroupData()"><spring:message code=""/><!-- 查询 --></a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-group-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>


  <script type="text/javascript">
  		var dataIds = '${dataIds}';
  		
  		function clearSelections() {  
  		    $('#datagrid-group-data' ).datagrid('clearSelections');  
  		}

  		function searchGroupData(){
  			var searchParamArray = $('#form-group-search').serializeArray();
  			var searchParams = $.jqexer.formToJson(searchParamArray);
  			$('#datagrid-group-data').datagrid('load',searchParams);
  		}
  		
  		
  		function addGroupData(userId){
  			var selections = $('#datagrid-group-data').datagrid('getSelections');
  			var dataIds = '';
  			for(var i = 0; i < selections.length; i ++) {
  				dataIds += (selections[i].id + ',');
  			}
  			$.messager.progress();  
  			$.ajax({
  				url:ctx+'/manager/admin/user/addGroupConfig',
  				method:'post',
  				data:{userId:userId,dataIds:dataIds},
  				dataType:'json',
  				success:function(data){
  					$.messager.progress('close');
  					if(data.success){
  						$(".panel-tool-close").click();
  						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */, '<spring:message code="vendor.user.saveDataSuccess"/>'/* 保存数据成功 */,'info');
  					}else{
  						$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */, '<spring:message code="vendor.user.failedSaveData"/>'/* 保存数据失败 */,'error');
  					}
  				}
  			});
  		}

  		
  		
  		
   </script>
</body>
</html>
