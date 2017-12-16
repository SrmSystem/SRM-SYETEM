<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>

<script type="text/javascript" src="${ctx}/static/script/basedata/warn.js"></script>
</head>
<body>
<table id="datagrid-promotion-list" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/common/warning/getItemList/${mainId}',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#promotionListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead>
		<tr>
		
		<th data-options="field:'operate',formatter:promotionFmt,width:'100'"><spring:message code="vendor.operation"/><!-- 操作 --></th>
		 <th data-options="field:'role',formatter:function(v,r,i){return r.role.name;}" width="100px"    ><spring:message code="vendor.common.characterName"/><!-- 角色名称 --></th>
		<th data-options="field:'warnTime' ,width:'100'"><spring:message code="vendor.common.processingTime"/><!-- 处理时长（小时） --></th>
		<th data-options="field:'warnContent' ,width:'100'"><spring:message code="vendor.common.remindContent"/><!-- 提醒内容 --></th>
		
		</tr>
		</thead>
		</table>
		<div id="promotionListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addPromotion()"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<input id="warnMainId" name="warnMainId" value="${mainId}" type="hidden" />
		</div>
		</div>
		<input type="hidden" value="${mainId}" id="text"/>
		<div id="win-promotion-edit" class="easyui-dialog" title="编辑晋级提醒预警" style="width:500px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-promotion-edit" method="post" >
				
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				  <input id="id" name="id" value="-1" type="hidden"/>
				  <input id="main_Id"  name="warnMainId"  type="hidden" /> 
				<tr>
					<td><spring:message code="vendor.common.characterName"/><!-- 角色名称 -->:</td>
					<td>
					<input id="roleSelect" class="easyui-combobox" style="width:180px" name="roleId" />
			        
			       
			        </td>
					
				</tr>
				<tr>
				<td><spring:message code="vendor.common.processingTime"/><!-- 处理时长 -->:</td><td>
<!-- 				<input class="easyui-textbox" style="width:180px" name="warnTime" type="text"
						data-options="required:true;validType:['number','length[11,11]']"   digits="true"/> -->
						
						<input i class="input easyui-numberbox" min="0.01"  data-options="required:true" style="width:180px"  max="100000000" type="text" name="warnTime" />
						
						
						</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.common.remindContent"/><!-- 提醒内容 -->:</td><td><textarea class="textarea easyui-validatebox" style="width:180px;height:120px" name="warnContent" type="text"
						data-options="required:true">  </textarea></td>
					</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitEditPromotionSet()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-promotion-edit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
