<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title>供应商物料类型管理</title>
	<script type="text/javascript">
		function orgCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showOrderDetail('+ r.id +');">' + v + '</a>';
		}
		function operFmt(v,r,i){
			return '<button class="btn-link" onclick="displayEdit('+r.id+')">维护</button>';
		}
		
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-order-list" title="供应商列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/vendor/vendorMaterialTypeRel',method:'post',singleSelect:true,   
		toolbar:'#orderListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'oper',formatter:operFmt">操作</th>
				<th data-options="field:'code',formatter:orgCodeFmt">供应商编码</th>
				<th data-options="field:'name'">供应商名称</th>
				<th data-options="field:'registerTime'">注册时间</th>
				<th data-options="field:'_orgType'">组织级别</th>
				<th data-options="field:'_roleType'">组织类型</th>

		
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		

		</tr></thead>
	</table>
	<div id="orderListToolbar" style="padding:5px;">
		<div><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="viewRel()">查看</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="editRel()">维护</a>
 
		</div>
		<div>
			<form id="form-order-search" method="post">
			供应商编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			

			<tags:dynamic objName='${extended }' type='queryForm'></tags:dynamic> 
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchOrder()">查询</a>
			</form>
		</div>
	</div>
	


	
		

	
	
<script type="text/javascript">

		function downPic(){
			var selections = $('#datagrid-order-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			var fileName=selections[0].fileName;
			var filePath=selections[0].filePath;
			var id=selections[0].id;
			
			if(filePath==null || filePath==''){
				$.messager.alert('提示','文件路径不能为空','warning');
				return false;
			}
			var url = ctx+'/manager/tecpic/tecpic/downloadFile';
			fileName = fileName==null?"":fileName;
			var inputs = '<input type="hidden" name="filePath" value="'+filePath+'"><input type="hidden" name="fileName" value="'+fileName+'"><input type="hidden" name="id" value="'+id+'">';
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
	        .appendTo('body').submit().remove();
		}     
	

	
	 function openWin(title, width, height, url) {
		var $dialog = $('<div/>').dialog({     
            title: title,     
            width: width,     
            height: height,     
            iconCls : 'icon-application',    
            closed: true,     
            cache: false,     
            href: url,     
            modal: true,
            onClose : function() {
                $(this).dialog('destroy');
            }
       });    
	  $dialog.dialog('autoSizeMax');
	  $dialog.dialog('open'); 
	  
	}
	
	
function searchOrder(){
	var searchParamArray = $('#form-order-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-order-list').datagrid('load',searchParams);
}

function showOrderDetail(id) {
	openWin('查看', 800, 600, ctx + '/manager/vendor/vendorMaterialTypeRel/viewVendor/'+id);
	
}

function viewRel() {
	var selections = $('#datagrid-order-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	openWin('查看', 800, 600, ctx + '/manager/vendor/vendorMaterialTypeRel/viewVendor/'+selections[0].id);
	
}

function editRel() {
	var selections = $('#datagrid-order-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选择任何记录！','info');
		return false;
	}
	openWin('维护', 800, 600, ctx + '/manager/vendor/vendorMaterialTypeRel/displayVendor/'+selections[0].id);
}

function displayEdit(id) {
	new openWin('维护', 800, 600, ctx + '/manager/vendor/vendorMaterialTypeRel/displayVendor/'+id);
}



</script>
</body>
</html>
