<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>VMI库存管理</title>
	<script type="text/javascript">
		function managerFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editMinInventory('+r.id+');">编辑</a>';
		}
	</script>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-inventory-list" title="VMI库存列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/inventory/vmi',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#inventoryListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料编码</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'stockQty'">三方库存量</th>
		<th data-options="field:'req3day',formatter:function(v,r,i){return '';}">需求量（3天）</th>
		<th data-options="field:'reqcurmonth',formatter:function(v,r,i){return '';}">需求量（当月）</th>  
		<th data-options="field:'minStockQty',formatter:function(v,r,i){if(r.minInventory) return r.minInventory.stockMinQty; else return '';}">最小库存</th>
		<th data-options="field:'manager',formatter:managerFmt">管理</th>
		</tr></thead>
	</table>
	<div id="inventoryListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download',plain:true" onclick="exportvmi()">导出VMI库存</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="importMinInventory()">批量设置最小库存</a>
		</div>
		<div>
			<form id="form-inventory-search" method="post">
			供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			物料编码：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>   
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchInventory()">查询</a>
			</form>
		</div>
	</div>  

	<!-- 导入最小库存设置 -->
	<div id="win-mininventory-import" class="easyui-window" title="批量设置最小库存" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-mininventory-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/inventory/vmi/filesUpload"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/template/MinInventory.xls">最小库存模版.xls</a>   --%>
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/template/MinInventory.xls','最小库存模版')">最小库存模版.xls</a>  
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveMinInventory();">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-order-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
	
	<div id="win-mininventory-addoredit" class="easyui-window" title="编辑最小库存" style="width:500px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-mininventory-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<input id="vendorId" name="vendorId" value="-1" type="hidden"/>
				<input id="materialId" name="materialId" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">   
				<tr>
					<td>供应商编码:</td><td><input class="easyui-textbox" name="vendorCode" id="vendorCode" type="text" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>供应商名称:</td><td><input class="easyui-textbox" name="vendorName" id="vendorName" type="text" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>物料编码:</td><td><input class="easyui-textbox" name="materialCode" id="materialCode" type="text" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>物料名称:</td><td><input class="easyui-textbox" name="materialName" id="materialName" type="text" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>当前库存:</td><td><input class="easyui-textbox" name="stockQty" id="stockQty" type="text" data-options="required:true" /></td>
				</tr>
				<tr>
					<td>最小库存:</td><td><input class="easyui-textbox" name="stockMinQty" id="stockMinQty" type="text" data-options="required:true" /></td>
				</tr>
				
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitMinInventory()">提交</a>
				</div>
			</form>
		</div>
	</div>


<script type="text/javascript">
function searchInventory(){
	var searchParamArray = $('#form-inventory-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-inventory-list').datagrid('load',searchParams);
}

function importMinInventory() {
	$('#form-mininventory-import').form('clear');   
	$('#win-mininventory-import').window('open');  
}

//保存批量设置
function saveMinInventory() {
	$.messager.progress();
	$('#form-mininventory-import').form('submit',{
		ajax:true,
		iframe: true,    
		url:'${ctx}/manager/inventory/vmi/filesUpload', 
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
				$.messager.alert('提示','批量设置最小库存成功','info');
				$('#win-mininventory-import').window('close');
				$('#datagrid-inventory-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
			}
			}catch (e) {  
				$.messager.alert('提示',data,'error');
			}
		}
	});
}

//修改最小库存
function editMinInventory(id) {
	$('#win-mininventory-addoredit').window({
		iconCls:'icon-edit',
		title:'编辑最小库存'
	});
	$('#vendorCode').textbox('readonly');
	$('#vendorName').textbox('readonly');
	$('#materialCode').textbox('readonly');
	$('#materialName').textbox('readonly');
	$('#stockQty').textbox('readonly');
	$('#win-mininventory-addoredit').window('open');  
	$('#form-mininventory-addoredit').form('clear'); 
	$('#form-mininventory-addoredit').form('load','${ctx}/manager/inventory/vmi/getInventory/'+id);
}   

//保存最小库存
function submitMinInventory() {
	$.messager.progress();
	$('#form-mininventory-addoredit').form('submit',{
		ajax:true,
		url: '${ctx}/manager/inventory/vmi/updateMin',
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
				$.messager.alert('提示',result.msg,'info');
				$('#win-mininventory-addoredit').window('close');
				$('#datagrid-inventory-list').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg,'error');
			}
			}catch (e) {
				$.messager.alert('提示',data,'error');
			}
		}
	});
}

//导出VMI库存
function exportvmi() {
	$('#form-inventory-search').form('submit',{
		url: '${ctx}/manager/inventory/vmi/exportExcel',
		success:function(data){
			$.messager.progress('close');
		}
	});
}

</script>   
</body>
</html>
