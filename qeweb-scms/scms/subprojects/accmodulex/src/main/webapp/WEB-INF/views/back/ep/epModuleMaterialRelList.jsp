<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>询比价-物料报价模型关系</title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/ep/epModuleMaterialRel.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-epModuleMaterialRel-list" title="物料报价模型关系列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/ep/epModuleMaterialRel',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#epModuleMaterialRelToolbar',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
<!-- 		<th data-options="field:'manager',formatter:EpModuleMaterialRel.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'material',formatter:function(v,r,i){return v==null?'':v.code;}">物料图号</th>
		<th width="150px" data-options="field:'materialName',formatter:function(v,r,i){return r.material==null?'':r.material.name;}">物料名称</th>
		<th width="150px" data-options="field:'emModule',formatter:function(v,r,i){return v==null?'':v.code;}">报价模型编码</th>
		<th width="150px" data-options="field:'emModuleName',formatter:function(v,r,i){return r.emModule==null?'':r.emModule.name;}">报价模型名称</th>
		</tr></thead>
	</table>
	<div id="epModuleMaterialRelToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="EpModuleMaterialRel.add()">新增</a>
 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="EpModuleMaterialRel.del()">删除</a> 
 								<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="EpModuleMaterialRel.openImportWin()">导入</a>
		</div>
		<div>
			<form id="form-epModuleMaterialRel-search" method="post">
			<input name="query-EQ_bussinessType" type="hidden" value="0"/>
			物料图号：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
			报价模型编码：<input type="text" name="search-LIKE_emModule.code" class="easyui-textbox" style="width:80px;"/>
			报价模型名称：<input type="text" name="search-LIKE_emModule.name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="EpModuleMaterialRel.query()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-epModuleMaterialRel-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-epModuleMaterialRel-addoredit" class="easyui-window" title="新增物料报价模型关系"
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<div itemId="ct">
			<form id="form-epModuleMaterialRel-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">

				<tr>
					<td>
						物料名称：<input  id="materialId" name="materialId" type="hidden" value="">
						<input class="easyui-textbox" id="materialName" name="materialName" value="" type="text" data-options="required:true"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="EpModuleMaterialRel.openWinMaterial()"></a>
					</td>
				</tr>
				<tr>
					<td>
						报价模型名称：<input  id="moduleId" name="moduleId" type="hidden" value="">
						<input class="easyui-textbox" id="moduleName" name="moduleName" value="" type="text" data-options="required:true"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="EpModuleMaterialRel.openWinModule()"></a>
					</td>
				</tr>

				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="EpModuleMaterialRel.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-epModuleMaterialRel-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
	
		
	<div id="win-material-detail" title="物料" class="easyui-window"
		data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<table id="datagrid-material-list" title="物料列表"
			data-options="method:'post',singleSelect:true,
				toolbar:'#materialListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
			<thead>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="45%" data-options="field:'code'">物料图号</th>
					<th width="45%" data-options="field:'name'">物料名称</th>
				</tr>
			</thead>
		</table>

		<div id="materialListToolbar" style="padding: 5px;">
			<div>
				<a  href="javascript:;"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true"
					onclick="EpModuleMaterialRel.choiceMaterial()">选择</a>
			</div>
			<div>
				<form id="form-material-search" method="post">
					物料图号：<input type="text" name="search-LIKE_code"
						class="easyui-textbox" style="width: 80px;" /> 物料名称：<input
						type="text" name="search-LIKE_name" class="easyui-textbox"
						style="width: 80px;" /> <a 
						href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'"
						onclick="EpModuleMaterialRel.searchMaterial()">查询</a>
				</form>
			</div>
		</div>
	</div>
	
		<div id="win-module-detail" title="报价模型" class="easyui-window"
		data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<table id="datagrid-module-list" title="报价模型列表"
			data-options="method:'post',singleSelect:true,
				toolbar:'#moduleListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
			<thead>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="45%" data-options="field:'code'">报价模型编码</th>
					<th width="45%" data-options="field:'name'">报价模型名称</th>
				</tr>
			</thead>
		</table>

		<div id="moduleListToolbar" style="padding: 5px;">
			<div>
				<a  href="javascript:;"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true"
					onclick="EpModuleMaterialRel.choiceModule()">选择</a>
			</div>
			<div>
				<form id="form-module-search" method="post">
					报价模型编码：<input type="text" name="search-LIKE_code"
						class="easyui-textbox" style="width: 80px;" /> 报价模型名称：<input
						type="text" name="search-LIKE_name" class="easyui-textbox"
						style="width: 80px;" /> <a 
						href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-search'"
						onclick="EpModuleMaterialRel.searchModule()">查询</a>
				</form>
			</div>
		</div>
	</div>
	
	  	<!-- 导入-->
	<div id="win-rel-import" class="easyui-window" title="导入" style="width:400px;height:300px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-rel-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/vendorSelection/materialGroupVendorRel/filesUpload"> 
				<div style="margin-bottom:20px">
					选择文件：<input type=file id="file" name="planfiles" /><br>
					文件模板： <a href="javascript:;" onclick="File.download('WEB-INF/templates/MaterialModuleRel.xls','物料报价模型关系模板')">物料报价模型关系模板.xls</a>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="EpModuleMaterialRel.saveimport();">提交</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-rel-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
</body>
</html>
