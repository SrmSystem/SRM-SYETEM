<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- 条款管理页面 -->
<div style="height: 500px">
	<div class="easyui-panel" region="center" data-options="fit:true">
	<input id="moduleIdval" value="${module.id}" type="hidden"/>
	<table id="treegrid-item-list" class="easyui-treegrid"
		data-options="fit:true,
		url:'${ctx}/manager/contractmodule/contractmodule/getModuleItemTreeGrid/${module.id}',method:'post',singleSelect:true,
		idField:'id',treeField:'name',animate:true,onContextMenu:ContractModuleItem.onContextMenu,onHeaderContextMenu:ContractModuleItem.onHeaderContextMenu,rownumbers:true,
		toolbar:'#toolbar-item',onLoadSuccess:ContractModuleItem.gridLoadSuc,onEndEdit:ContractModuleItem.gridLoadSuc,onCancelEdit:ContractModuleItem.gridLoadSuc
		"
		>
		<thead><tr>
		<th width="50px" data-options="field:'id',hidden:true"></th>
		<th width="140px" data-options="field:'name',editor:'text'">条款名称</th>
		<th width="140px" data-options="field:'code',editor:'text'">条款编码</th>
		<th width="140px" data-options="field:'content',editor:'textbox',multiline:true">条款内容</th>
		<th width="100px" data-options="field:'operate',formatter:ContractModuleItem.fmt_upAndDown">操作</th>
		</tr></thead>
	</table>
  </div>
  	<div id="module-item-head" class="easyui-menu">
		<div data-options="iconCls:'icon-add'" onclick="ContractModuleItem.addNewItem(3)">新增条款</div>
	</div>
	<div id="module-item-opt" class="easyui-menu">
		<div data-options="iconCls:'icon-add'" onclick="ContractModuleItem.addNewItem(0)">新增平级条款</div>
		<div data-options="iconCls:'icon-add'" onclick="ContractModuleItem.addNewItem(1)">新增下级条款</div>
		<div data-options="iconCls:'icon-copy'" onclick="ContractModuleItem.copyItem()">复制新增</div>
		<div data-options="iconCls:'icon-edit'" onclick="ContractModuleItem.editItem()">编辑条款</div>
		<div data-options="iconCls:'icon-delete'" onclick="ContractModuleItem.deleteItem()">删除条款</div>
	</div>
	<div id="toolbar-item" style="padding:5px;">
		<div>
			 <form id="form_module_item">
			 </form>
	 	</div>
		<a href="javascript:;" onclick="ContractModuleItem.openItemCtp(${module.id})" class="easyui-linkbutton" data-options="plain:true">合同模板预览</a>
		<a href="javascript:;" onclick="ContractModuleItem.createPdf(${module.id},1)" class="easyui-linkbutton" data-options="plain:true">生成PDF</a>
		<!-- <a href="javascript:;" onclick="ContractModuleItem.updateItem()" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">保存</a> -->
		<!--  <a href="${ctx}/manager/contractmodule/contractmodule" class="easyui-linkbutton" data-options="iconCls:'icon-back',plain:true">返回</a>-->
	</div>
</div>

<!-- 新增条款页面 -->
 <div id="win-item-add" class="easyui-window" title="新增条款"
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-item-add" method="post">
				<input id="moduleId" name="moduleId" value="" type="hidden"/>
				<input id="parentId" name="parentId" type="hidden"/>
				<input id="beforeId" name="beforeId" type="hidden"/>
				<input id="moduleItemId" name="id" value="0" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>条款名称:</td>
					<td><input class="easyui-textbox" id="itemName" name="name" type="text" data-options="required:true"/></td>
				</tr>
				<tr>
					<td>条款编号:</td>
					<td><input class="easyui-textbox" id="itemCode" name="code" type="text" data-options="required:true"/></td>
				</tr>
				<tr>
					<td>条款内容:</td>
					<td><input class="easyui-textbox" id="itemContent" name="content" type="text" 
					data-options="multiline:true" style="height:120px"/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddItem()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-item-add').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>	