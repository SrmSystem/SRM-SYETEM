<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<meta charset="UTF-8">
	<title>合同模板</title>
	<script type="text/javascript">
	function optFmt(v,r,i){
		
 		/** 操作列格式化 */
 		/* return '<a class="easyui-linkbutton" href="'+ctx+'/manager/contractmodule/contractmodule/toEdit?moduleId='+r.id+'">条款</a>'+
 		'  <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editModule(' + r.id + ','+ i+')">'+'编辑'+'</a>'; */
 		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openItem(' + r.id + ')">'+'条款'+'</a>'+
 		'  <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="editModule(' + r.id + ','+ i+')">'+'编辑'+'</a>'+
 		'  <a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="openItemCtp(' + r.id + ')">'+'合同预览'+'</a>';
 	}
	</script>
	<script type="text/javascript" src="${ctx}/static/script/system/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/contract/contractModule.js"></script>
</head>

<body>
<div class="easyui-panel" region="center" data-options="fit:true">
  <table id="datagrid" data-options="
    fit:true,title:'合同模板', url:'${ctx}/manager/contractmodule/contractmodule/getList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50], toolbar:'#toolbar-module'">
    <thead>
      <tr>
        <th width="50px" data-options="field:'id',checkbox:true"></th>
        <!-- <th data-options="field:'id',hidden:true"></th> -->
        <th width="130px" data-options="field:'operate',formatter:optFmt">操作</th>
        <th width="130px" data-options="field:'name',editor:'text'">模板名称</th>
        <th width="140px" data-options="field:'code',editor:'text'">模板编码</th>
        <th width="140px" data-options="field:'contractType',formatter:function(v,r,i){if(v==0) return '临时合同模板';else return '年度合同模板'}">模板类型</th>
      </tr>
    </thead>
  </table>
 </div>
 
<div id="toolbar-module" style="padding:5px;">
	<div>
		 <form id="form_module" method="post">
		 </form>
	</div>
	<a href="javascript:;" onclick="addNewModule()" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true">新增模板</a>
	<a href="javascript:;" onclick="deleteModule('datagrid')" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true">删除模板</a>
	<a href="javascript:;" onclick="updateModuleEdit('datagrid')" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存编辑</a>
</div>
  
  <!-- 新增模板页面 -->
  <div id="win-module-add" class="easyui-window" title="新增模板"
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
	<div class="easyui-panel" data-options="fit:true">
		<form id="form-module-add" method="post" >
			<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
			<tr>
				<td>模板名称:</td><td><input class="easyui-textbox" id="moduleName" name="name" type="text" data-options="required:true"/></td>
			</tr>
			<tr>
				<td>模板编码:</td><td><input class="easyui-textbox" id="moduleCode" name="code" type="text" data-options="required:true"/></td>
			</tr>
			<tr>
				<td>模板类型:</td>
				<td style="text-align: left">
				<select class="easyui-combobox" id="moduleType" name="contractType" type="text" data-options="required:true,editable:false">
				<option value="0">临时合同模板</option>
				<option value="1">年度合同模板</option>
				</select>
				</td>
			</tr>
			</table>
		<div style="text-align: center;padding:5px;">
			<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddModule()">提交</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-module-add').form('reset')">重置</a>
		</div>
		</form>
	</div>
  </div>
	


  <script type="text/javascript">
  var editRowId = null;  //正在编辑行的id
  var editIndex = null; 
  var clientWidth = document.body.clientWidth;
  var clientHeight = document.body.clientHeight;

  
 $(function(){
 	$("#datagrid").datagrid({});
 })
 	
/**
  * 打开新增模板页面
  */
function addNewModule(){
	//$('#win-module-add').window('open');
		$('#win-module-add').window({
			iconCls:'icon-edit',
			title:'新增模板'
		});
		$('#form-module-add').form('clear');
		$('#form-module-add').form('load',ctx+'/manager/contractmodule/contractmodule/createModuleCode');
		$('#win-module-add').window('open');
}
 	
 	
/**
  * 提交新增模板
  */
function submitAddModule(){
 	$('#form-module-add').form('submit',{
 		url:'${ctx}/manager/contractmodule/contractmodule/saveModule',
 		success:function(data){
 			data = $.evalJSON(data);
 			if(data.success){
 				$.messager.alert('提示',data.msg,'info');
 				$('#datagrid').datagrid('reload');
 			}else{
 				$.messager.alert('提示',data.msg,'error');
 			}
 			$('#win-module-add').window("close");
 		}
 	});
 }
 
/**
 * 删除模板
 */
 function deleteModule(datagridId){
	 datagridId = '#' + datagridId;
	 var selections = $(datagridId).datagrid('getSelections');
	 if(selections.length==0){
		 $.messager.alert('提示','没有选中任何数据','info');
		 $(datagridId).datagrid('reload');
		 return false;
	 }
	 var params = $.toJSON(selections);
	 $.messager.confirm('提示','确定要删除吗？',function(r){
	  if(r){
		 $.ajax({
			url:'${ctx}/manager/contractmodule/contractmodule/deleteModule',
			type:'POST',
			data:params,
			dataType : 'json',
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				if(data.success){
					$.messager.alert('提示',data.msg,'info');
					$(datagridId).datagrid('reload');
				}else{
					$.messager.alert('提示',data.msg,'error');
				}
			}
		});
	  }
	 });
 }
 
 /**
  * 打开条款页面
  */
 function openItem(id){
	 var title="条款管理";
	//new dialog().showWin(title, 1000, 500, ctx + '/manager/contractmodule/contractmodule/toEdit?moduleId=' + id);
	new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contractmodule/contractmodule/toEdit?moduleId=' + id);
 }
 
 /**
  * 打开合同预览页面
  */
 function openItemCtp(id){
	//var title="合同预览";
	//new dialog().showWin(title, clientWidth, clientHeight, ctx + '/manager/contractmoduleItem/moduleItem/getModuleItemEasyuiTree?moduleId=' + id);
	window.open(ctx + '/manager/contractmoduleItem/moduleItem/getModuleItemEasyuiTree?moduleId='+ id);
  }
/**
 * 编辑模板
 */
function editModule(selectedId,rowIndex){
	var datagridId = '#datagrid';
	//如果不等于正在编辑的行，则询问
	if(editRowId!=null && selectedId!=editRowId){
		$.messager.confirm('提示','有未保存的编辑,您要保存后编辑吗？',function(r){
			if(r){
				updateModuleEdit('datagrid'); //保存修改的数据
			}else{
				 $(datagridId).datagrid('cancelEdit',rowIndex);  //取消选中行的数据修改
				 return false;
			}
			$(datagridId).datagrid('beginEdit', rowIndex);
			editRowId = selectedId;
			editIndex = rowIndex;
			
		});
	}else{
		editRowId = selectedId;
		editIndex = rowIndex;
		 $(datagridId).datagrid('beginEdit', rowIndex);	
	}	
}

/**
 * 保存编辑的模板
 */
function updateModuleEdit(datagridId){
	datagridId = '#' + datagridId;
	$(datagridId).datagrid('acceptChanges');    //保存表格中的数据
	var selections = $(datagridId).datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('提示','没有选中任何数据','info');
		 $(datagridId).datagrid('reload');
		return false;
	}
	$.messager.progress({
		title:'提示',
		msg : '提交中...'
	});
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/contractmodule/contractmodule/updateModuleEdit',
		type:'POST',
		data:params,
		async:false,
		dataType : 'json',
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			if(data.success){
				$.messager.alert('提示',data.msg,'info');
				//$(datagridId).datagrid('reload');
				$(datagridId).datagrid('refreshRow',editIndex);
			}
		}
	});
	editRowId =null;
}

	
/**
  * 提交新增条款
  */
function submitAddItem(){
	$('#form-item-add').form('submit',{
		ajax:true,
		iframe: true,   
		url:'${ctx}/manager/contractmoduleItem/moduleItem/saveModuleItem',
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			data = $.evalJSON(data);
			if(data.success){
				$.messager.alert('提示',data.msg,'info');
				$('#treegrid-item-list').treegrid('reload');
			}else{
				$.messager.alert('提示',data.msg,'error');
			}
			$('#win-item-add').window("close");
		}
	});
 }
  </script>
</body>
</html>
