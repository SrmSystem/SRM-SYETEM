<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${ctx}/static/script/ep/epModuleManage.js"></script>
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
<!-- 新增模板主表单 -->
<div class="easyui-panel" data-options="fit:true">
	<div id="epModule-add" style="height:auto;padding-top: 20px">
		<form id="form-epModule-addoredit" method="post" >
			<input id="id" name="id" value=0 hidden="true" />
			<div id="deliveryListToolbar" style="padding:5px;">
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpModule(0)">保存</a>
				</div>
			<div>
			<div>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
					<tr>
						<td>编号:<input id="code" name="code" class="easyui-textbox" data-options="required:true" style="width:120px;"/>
						<td>名称:<input id="name" name="name" class="easyui-textbox" data-options="required:true" style="width:120px;"/>
						<td>是否默认模板:<input id="isDefault" name="isDefault" class="easyui-textbox" data-options="required:true" style="width:120px;"/>
					</tr>
					<tr>
						<td colspan="5">备注:<input id="remarks" name="remarks" class="easyui-textbox"  data-options="required:false" style="width:360px;"/></td>
					</tr>
					<tr>
				</tr>
				</table>
			</div>
		</form> 
   	</div>
   	<div id="tb" style="padding:5px;height:auto">
		<div style="margin-bottom:5px">
			<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addoredit')"></a>
        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addoredit')"></a>
		</div>
	</div>
  		<table id="datagrid-item-addoredit" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
  			singleSelect: true,toolbar: '#tb',method: 'post',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,onClickCell: RowEditor.onClickCell">
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
               <th width="100px" data-options="field:'remarks',width:200,editor:'textbox'">备注</th>
      <!--          <th width="100px" data-options="field:'addSecondItem',formatter:addSecondItemFmt">添加二级明细</th> -->
               
          </tr></thead>
   	</table>
</div>



