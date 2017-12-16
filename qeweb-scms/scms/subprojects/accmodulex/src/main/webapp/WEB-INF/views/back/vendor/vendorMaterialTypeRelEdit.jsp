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
		function orderCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showOrderDetail('+ r.id +');">' + v + '</a>';
		}
		
	</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-materialType-list"  class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/vendor/vendorMaterialTypeRel/displayVendorMaterialTypeRelList/${vendorId}',method:'post',singleSelect:false,   
		toolbar:'#datagrid-materialType-list-tb',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,10,15,20]"
		>
		<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		
		<th data-options="field:'materialTypeCode',formatter:function(v,r,i){return r.materialType.code;}">物料类型编码</th>
		<th data-options="field:'materialTypeName',formatter:function(v,r,i){return r.materialType.name;}">物料类型名称</th>
		<th data-options="field:'topMaterialTypeName'">顶级物料类型</th>
		
			

		
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		

		</tr></thead>
	</table>
	
    <div id="datagrid-materialType-list-tb" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="materialTypeManage.openWin()">选择</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="del()">删除</a>
		</div>
	</div>
	


			<!-- 物料类型信息 -->
<div id="win-materialType-detail" class="easyui-dialog" title="物料类型" style="width:600px;height:450px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
			<table id="datagrid-materialType-list-edit" title="物料类型列表" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#materialListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
				<thead><tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'code'">编码</th>
				<th data-options="field:'name'">名称</th>
				</tr></thead>
			</table>
			
			<div id="materialListToolbar" style="padding:5px;">
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="materialTypeManage.choice()">选择</a>
				</div>
				<div>
				<form id="form-materialType-search" method="post">
					编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
					名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="materialTypeManage.searchMaterialType()">查询</a>
			    </form>
		       </div>
			</div>
	</div>
		

	
	
<script type="text/javascript">
var materialTypeManage = {
		//弹出选择框	
		openWin : function() {
			$('#win-materialType-detail').dialog('open'); 
			$('#win-materialType-detail').dialog('autoSizeMax'); 
			$('#datagrid-materialType-list-edit').datagrid({   
		    	url: ctx + '/manager/vendor/vendorMaterialTypeRel/getMaterialTypeList'
			});
		},
		searchMaterialType: function() {
			var searchParamArray = $('#form-materialType-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-materialType-list-edit').datagrid('load',searchParams);
		},
		//选择
		choice : function() {
			var selections = $('#datagrid-materialType-list-edit').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
		 	$.messager.progress();
		 	var params = $.toJSON(selections);
         	$.ajax({
         		url:'${ctx}/manager/vendor/vendorMaterialTypeRel/selRel/'+${vendorId},
         		type:'POST',
         		data:params,
         		dataType:"json",
         		contentType : 'application/json',
         		success:function(data){
         			$.messager.progress('close');
         			try{
         				if(data.success){ 
         					$.messager.show({
         						title:'消息',
         						msg:  data.message, 
         						timeout:2000,
         						showType:'show',
         						style:{
         							right:'',
         							top:document.body.scrollTop+document.documentElement.scrollTop,
         							bottom:''
         						}
         					});
         					$('#win-materialType-detail').window('close');	
         					$('#datagrid-materialType-list').datagrid('reload'); 
         				}else{
         					$.messager.alert('提示',data.message,'error');
         				}
         			}catch (e) {
         				$.messager.alert('提示',e,'error'); 
         			} 
         		}
         	});
			
		}
	}
     	
	 function del(){
	 	var selections = $('#datagrid-materialType-list').datagrid('getSelections');
	 	if(selections.length==0){
	 		$.messager.alert('提示','没有选择任何记录！','info');
	 		return false;
	 	}
	 	$.messager.progress();
	 	var params = $.toJSON(selections);
	 	$.messager.confirm("操作提示", "确定要执行操作吗？", function (data) {
	         if (data) {
	         	$.ajax({
	         		url:'${ctx}/manager/vendor/vendorMaterialTypeRel/delRel',
	         		type:'POST',
	         		data:params,
	         		dataType:"json",
	         		contentType : 'application/json',
	         		success:function(data){
	         			$.messager.progress('close');
	         			try{
	         				if(data.success){ 
	         					$.messager.success(data.message);
	         					$('#datagrid-materialType-list').datagrid('reload'); 
	         				}else{
	         					$.messager.alert('提示',data.message,'error');
	         				}
	         			}catch (e) {
	         				$.messager.alert('提示',e,'error'); 
	         			} 
	         		}
	         	});
	         } else {
	         	$.messager.progress('close');
	         }
	     });
	 }
	



</script>
</body>
</html>
