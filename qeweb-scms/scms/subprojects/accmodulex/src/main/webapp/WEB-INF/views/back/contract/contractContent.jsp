<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!-- 条款管理页面 -->
<div style="height: 500px">
	<div class="easyui-panel" region="center" data-options="fit:true">
	<table id="treegrid-item-list" class="easyui-treegrid"
		data-options="fit:true,
		url:'${ctx}/manager/contract/contractContent/getContentTreeGrid/${contract.id}',method:'post',singleSelect:true,
		idField:'id',treeField:'name',animate:true,onContextMenu:ContractContent.onContextMenu,onHeaderContextMenu:ContractContent.onHeaderContextMenu,rownumbers:true,
		toolbar:'#toolbar-item',onLoadSuccess:ContractContent.gridLoadSuc,onEndEdit:ContractContent.gridLoadSuc,onCancelEdit:ContractContent.gridLoadSuc
		"
		>
		<thead><tr>
		<th width="50px" data-options="field:'id',hidden:true"></th>
		<th width="140px" data-options="field:'name',editor:'text'">条款名称</th>
		<th width="140px" data-options="field:'code',editor:'text'">条款编码</th>
		<th width="140px" data-options="field:'content',editor:'textbox',multiline:true">条款内容</th>
			<c:if test="${contract.auditStatus eq 0}">
		<th width="100px" data-options="field:'operate',formatter:ContractContent.fmt_upAndDown">操作</th>
		</c:if>
			
		</tr></thead>
	</table>
  </div>
  
  	<c:if test="${contract.auditStatus eq 0}">
	  	<div id="module-item-head" class="easyui-menu">
			<div data-options="iconCls:'icon-add'" onclick="ContractContent.addNewItem(3)">新增条款</div>
		</div>
		<div id="module-item-opt" class="easyui-menu">
			<div data-options="iconCls:'icon-add'" onclick="ContractContent.addNewItem(0)">新增平级条款</div>
			<div data-options="iconCls:'icon-add'" onclick="ContractContent.addNewItem(1)">新增下级条款</div>
			<div data-options="iconCls:'icon-copy'" onclick="ContractContent.copyItem()">复制新增</div>
			<div data-options="iconCls:'icon-edit'" onclick="ContractContent.editItem()">编辑条款</div>
			<div data-options="iconCls:'icon-delete'" onclick="ContractContent.deleteItem()">删除条款</div>
		</div>
	</c:if>
		<div id="toolbar-item" style="padding:5px;">
		<c:if test="${moduleId gt 0}">
								<a href="javascript:;" onclick="ContractContent.openItemCtp(${contract.id})" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">合同模板预览</a>
<%-- 								<a href="javascript:;" onclick="ContractModuleItem.createPdf(${module.id},1)" class="easyui-linkbutton" data-options="plain:true">生成PDF</a> --%>
		</c:if>
		<c:if test="${roleType eq 0}">
			<c:if test="${contract.auditStatus eq 0}">
				<a href="javascript:;" onclick="ContractManage.checkContract('${contract.id}','${contract.isPdf}')"  class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">提交审核</a>
			</c:if>
			<c:if test="${contract.publishStatus eq 0}">
				<c:if test="${contract.auditStatus eq 1}">
					<a href="javascript:;" onclick="ContractManage.publishContract('${contract.id}')" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">发布</a>
				</c:if>
			</c:if>
		</c:if>	
			<c:if test="${contract.confirmStatus eq 0}">
				<c:if test="${contract.publishStatus eq 1}">
					<c:if test="${roleType eq 1}">
						<a href="javascript:;" onclick="ContractManage.displayUploadConfirm('${contract.id}')" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">确认</a>
						<a href="javascript:;" onclick="ContractManage.confirmContract('${contract.id}','rejectContract')" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true">驳回</a>
					</c:if>
				</c:if>
			</c:if>
		</div>

</div>

<!-- 新增条款页面 -->
 <div id="win-item-add" class="easyui-window" title="新增条款"
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-item-add" method="post">
				<input id="contractId" name="contractId" value="${contract.id}" type="hidden"/>
				<input id="parentId" name="parentId" type="hidden"/>
				<input id="beforeId" name="beforeId" type="hidden"/>
				<input id="contentId" name="id" value="0" type="hidden"/>
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
					<a href="javascript:;" class="easyui-linkbutton" onclick="ContractContent.submitAddItem()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-item-add').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
	
		<!-- 上传文件 -->
	<div id="win-file-import" class="easyui-window" title="上传文件" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-file-import" method="post" enctype="multipart/form-data" action=""> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
						<input type=hidden id="id" name="id"/>   
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="ContractManage.submitConfirmFile();">提交</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-file-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>



