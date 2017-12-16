<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/static/script/ep/epModuleManage.js"></script>
<script type="text/javascript">
</script>
<!-- 模板一级明细 -->
<div class="easyui-panel" data-options="fit:true">
	<div class="easyui-panel">
		<form id="form-epModuleItem-search">
			<div id="deliveryListToolbar" style="padding:5px;">
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveSecondItem()">保存</a>
				</div>
			<div>
			<input id="id" name="id" value="${firstItem.id }" type="hidden"/>
			<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>报价类型名称：${firstItem.name }</td>
					<td>单位：${firstItem.unitId }</td>
					<td>是否一级明细：
						<c:if test="${firstItem.isTop eq 0}">否</c:if>
						<c:if test="${firstItem.isTop eq 1}">是</c:if>
					</td>
				</tr>
				<tr>
					<td>备注：${firstItem.remarks }</td>
				</tr>
		</form>  
	</div>
   	<div id="tbSecond" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-secondItem-addoredit')"></a>
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-secondItem-addoredit')"></a>
		</div>
	</div>
  		<table id="datagrid-secondItem-addoredit" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
  			singleSelect: true,toolbar: '#tbSecond',method: 'post',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,onClickCell: RowEditor.onClickCell,url: '${ctx }/manager/ep/epModule/getEpModuleItemSecond/${firstItem.id }'">
              <thead><tr>
               <th width="50px" data-options="field:'id',checkbox:true"></th> 
               <th width="130px" data-options="field:'name',width:80,editor:'textbox'">报价类型名称</th>
               <th width="100px" data-options="field:'unitId',width:80,editor:'textbox'">单位</th>
               <!-- <th data-options="field:'isTop',width:100,editor:
					 			{type:'combobox',
		                           options:{
		                               data:
		                               	[{'id':'1','text':'是'},
		                               	{'id':'0','text':'否'}],                 
		                               valueField:'id',
		                               textField:'text',
		                               required:false,
		                               editable:false,
		                               panelHeight : 50,
	                               }
	                           }">是否一级明细</th> -->
               <th width="80px" data-options="field:'remarks',width:200,editor:'textbox'">备注</th>
          </tr></thead>
   	</table>
</div>



