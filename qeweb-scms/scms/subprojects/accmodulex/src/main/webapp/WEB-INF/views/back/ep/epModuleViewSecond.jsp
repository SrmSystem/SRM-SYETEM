<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
</script>	
<div data-options="fit:true">
	<div>
		<c:if test="${isUpdate }">  
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="saveSecondItem()">保存</a>
		</c:if>
	</div>
	
	<div class="easyui-panel">
		<form id="form-epModuleItem-search">
			<input id="id" name="id" value="${epModuleItem.id }" type="hidden"/>
			<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>报价类型名称：${epModuleItem.name }</td>
					<td>单位：${epModuleItem.unitId }</td>
					<td>是否一级明细：
						<c:if test="${epModuleItem.isTop eq 0}">否</c:if>
						<c:if test="${epModuleItem.isTop eq 1}">是</c:if>
					</td>
				</tr>
				<tr>
					<td>备注：${epModuleItem.remarks }</td>
				</tr>
		</form>  
	</div>
	
	<c:if test="${isUpdate }">  
		<div id="epModuleItemSecondtb" style="padding:5px;height:auto">
			<div style="margin-bottom:5px">
				<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-secondItem-addoredit')"></a>
	        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-secondItem-addoredit')"></a>
			</div>
		</div> 
	</c:if> 
	<table id="datagrid-secondItem-addoredit" title="模板二级明细" class="easyui-datagrid"
		data-options="method:'post',singleSelect:false,toolbar: '#epModuleItemSecondtb',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
		onClickCell: RowEditor.onClickCell,
		url: '${ctx }/manager/ep/epModule/getEpModuleItemSecond/${epModuleItem.id }'">
		<thead><tr>  
		<th width="50px" data-options="field:'id',checkbox:true"></th>
		<th width="80px" data-options="field:'name',width:80<c:if test="${isUpdate }">,editor:'textbox'</c:if>">报价类型名称</th>
		<th width="80px" data-options="field:'unitId',width:80<c:if test="${isUpdate }">,editor:'textbox'</c:if>">单位</th>
		<c:if test="${!isUpdate }">
			<th width="100px" data-options="field:'isTop',width:100,formatter: function(v,r,i){return r.isTop ==1?'是':'否'}">是否一级明细</th>
	    </c:if>
		<th width="200px" data-options="field:'remarks',width:200<c:if test="${isUpdate }">,editor:'textbox'</c:if>">备注</th>
		</tr></thead>
	</table> 
</div>
	
