<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>调查表模版管理</title>
	<script type="text/javascript">
	  var Constants = {
		templateType : <%=VendorModuleTypeConstant.getSurveyTemplateTypeJson()%>	  
	  };
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorSurvey.js"></script>
</head>

<body>
<div class="easyui-panel" data-options="region:'center',fit:true,border:false">
	<table id="datagrid-vendorSurveyTemplate-list" title="调查表模版列表" class="easyui-datagrid" style="overflow: auto"
		data-options="
		fit:true,
		method:'post',singleSelect:false,
		toolbar:'#vendorSurveyTemplateListToolbar',
		fitColumns:true,
		queryParams:{'search-EQ_abolished':0},
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead data-options="frozen:true">
			<tr>
		      <th data-options="field:'id',checkbox:true"></th>
		      <th data-options="field:'manager',formatter:SurveyTemplate.managerFmt">管理</th>
		      <th data-options="field:'code',formatter:SurveyTemplate.codeFmt">调查表模版编号</th>
		      <th data-options="field:'name'">调查表模版名称</th>
		      <th data-options="field:'buyer.name'">采购商名称</th>
			</tr>
		</thead>
		<thead ><tr>
		
		<th data-options="field:'templateType',formatter:SurveyTemplate.typeFmt">模版类型</th>
		<th data-options="field:'beanId'">bean</th>
		<th data-options="field:'path'">路径</th>
		<th data-options="field:'sn'">顺序</th>
		</tr></thead>
	</table>
	<div id="vendorSurveyTemplateListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="SurveyTemplate.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="SurveyTemplate.del()">删除</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="SurveyTemplate.loading()">加载到已存在供应商</a>
		</div>
		<div>
			<form id="form-vendorSurveyTemplate-search" method="post">
			<input type="hidden" name="search-EQ_abolished" value="0"/>
			调查表模版编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			调查表模版名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="SurveyTemplate.search()">查询</a>
			</form>
		</div>
	</div>
	<div id="win-vendorSurveyTemplate-addoredit" class="easyui-dialog" title="新增调查表模版" style="width:400px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true,
	buttons:'#win-vendorSurveyTemplate-addoredit-bt'
	">
		<div itemId="ct">
			<form class="baseform" id="form-vendorSurveyTemplate-addoredit" data-options="onLoadSuccess:SurveyTemplate.infoLoadAfter" method="post" enctype="multipart/form-data">
				<input id="id" name="id" value="-1" type="hidden"/>
				<div>
					<label>模版编号:</label>
					<input class="easyui-textbox" itemId="code" id="code" name="code" type="text"
						data-options="required:true"
					/>
				</div>
				<div>
					<label>模版名称:</label>
					<input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/>
				</div>
				<div>
					<label>模版类型:</label>
				    <select name="templateType" class="easyui-combobox" data-options="required:true,editable:false">
				  	  <option value="0">xml</option>
				  	  <option value="1">bean</option>
				    </select>
				</div>
				<div>
					<label>beanId:</label>
					<input class="easyui-textbox" name="beanId" type="text"/>
				</div>
				<div>
					<label>模版文件:</label>
					<input class="easyui-filebox" id="editPathFile" name="pathFile" type="text" itemId="pathFile"
						data-options="prompt:'选择模版文件',formatter:fillPathFile",
						style="width:150px;"
					/>
					<input type="hidden" id="editPath" name="path" />
					<!-- <a itemId="pathFile-link" href="javascript:;" style="overflow: hidden;">附件</a> -->
				</div>
				<div>
					<label>顺序:</label>
					<input class="easyui-textbox" name="sn" type="text"/>
				</div>
				<div>
					<label>备注:</label>
					<input class="easyui-textbox" name="remark" type="text"/>
				</div>
			</form>
		</div>
		<div id="win-vendorSurveyTemplate-addoredit-bt">
		  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="SurveyTemplate.submit()">提交</a>
		  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reset'" onclick="$('#form-vendorSurveyTemplate-addoredit').form('reset')">重置</a>
		</div>
	</div>
	
<!-- 调查表预览弹出窗口 -->
   <div id="window-surveyPreview" class="easyui-window" data-options="closed:true,modal:true">
   </div>	
</div>

<!-- 一些初始化的脚本 -->
<script type="text/javascript">
//调查表模版加载初始数据
$('#datagrid-vendorSurveyTemplate-list').datagrid({
	  url : ctx+'/manager/vendor/vendorSurveyTemplate'
});
function fillPathFile(){
	return $('#editPathFile').val();
}
</script>
</body>

</html>
