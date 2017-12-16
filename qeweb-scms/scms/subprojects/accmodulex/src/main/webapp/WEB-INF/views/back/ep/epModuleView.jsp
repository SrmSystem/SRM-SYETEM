<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
$(function(){
	$('#isDefault').combobox({
		data: [{id: "1", text: "是"},{id: "0", text: "否"}],
		valueField: "id",   
		textField: "text",
		panelHeight : 50,
		editable: false
	});
});
</script>	
<div data-options="fit:true">
	<div>
		<c:if test="${isUpdate }">  
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpModule(1)">保存</a>
		</c:if>
	</div>
	
	<div class="easyui-panel">
		<form id="form-epModule-addoredit">
				<input id="id" name="id" value="${epModule.id }" hidden="true"/>
			<c:if test="${!isUpdate }">
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
					<tr>
						<td>编号：${epModule.code }</td>
						<td>名称：${epModule.name }</td>
						<td>是否默认模板：
							<c:if test="${epModule.isDefault eq 0}">否</c:if>
							<c:if test="${epModule.isDefault eq 1}">是</c:if>
						</td>
					</tr>
					<tr>
						<td>备注：${epModule.remarks }</td>
					</tr>
				</table>
			</c:if>
			<c:if test="${isUpdate }">
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
					<tr>
						<td>编号:<input id="code" name="code" value="${epModule.code }" class="easyui-textbox" data-options="required:true" style="width:120px;"/>
						<td>名称:<input id="name" name="name" value="${epModule.name }" class="easyui-textbox" data-options="required:true" style="width:120px;"/>
						<%-- <td>是否默认模板:${epModule.isDefault eq 0 ? "否" : "是"}</td> --%>
		                 <td>是否默认模板：
							<select id="isDefault" name="isDefault" class="easyui-combobox" data-options="editable:false,required:true">
							<option value="${epModule.isDefault}">-全部-</option><option value="0">否</option><option value="1">是</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="5">备注:<input id="remarks" name="remarks" value="${epModule.remarks }" class="easyui-textbox"  data-options="required:false" style="width:360px;"/></td>
					</tr>
					<tr>
				</tr>
				</table>
			</c:if>
		</form>  
	</div>
	 
		<div id="epModuleItemtb" style="padding:5px;height:auto">
			<div style="margin-bottom:5px">
			 <c:if test="${isUpdate }">  
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addoredit')"></a>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addoredit')"></a>
<!-- 	        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="updateEpModuleItemDetail()">修改</a> -->
			</c:if>
<!-- 	        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="showEpModuleItemDetail()">查看</a> -->
			</div>
		</div> 
	 
	<table id="datagrid-item-addoredit" title="模板一级明细" class="easyui-datagrid"
		data-options="method:'post',singleSelect:false,toolbar: '#epModuleItemtb',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
		onClickCell: RowEditor.onClickCell,
		url: '${ctx }/manager/ep/epModule/getEpModuleItem/${epModule.id }'">
		<thead><tr>  
		<th width="50px" data-options="field:'id',checkbox:true"></th>
		<th width="80px" data-options="field:'name',width:80<c:if test="${isUpdate }">,editor:'textbox'</c:if>">报价类型名称</th>
		<th width="80px" data-options="field:'unitId',width:80<c:if test="${isUpdate }">,editor:'textbox'</c:if>">单位</th>
<%-- 		<c:if test="${!isUpdate }">
			<th width="100px" data-options="field:'isTop',width:100,formatter: function(v,r,i){return r.isTop ==1?'是':'否'}">是否一级明细</th>
	    </c:if> --%>
		<th width="200px" data-options="field:'remarks',width:200<c:if test="${isUpdate }">,editor:'textbox'</c:if>">备注</th>
		</tr></thead>
	</table> 
</div>
	
