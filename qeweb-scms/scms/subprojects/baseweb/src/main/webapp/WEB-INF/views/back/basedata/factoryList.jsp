<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>工厂管理</title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/factory.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-factory-list" title="工厂列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/factory',method:'post',singleSelect:false,
		toolbar:'#factoryListToolbar',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'operate',formatter:Factory.operateFmt">操作</th>
		<th data-options="field:'code'">工厂编号</th>
		<th data-options="field:'name'">工厂名称</th>
		<th data-options="field:'nop',formatter:Factory.vfmt">所拥有品牌</th>
		<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
		</tr></thead>
	</table>
	<div id="factoryListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Factory.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Factory.abolish()">作废</a>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="Factory.del()">删除</a> -->
		</div>
		<div>
			<form id="form-factory-search" method="post">
			工厂编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			工厂名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Factory.search()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-factory-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="win-openvfSurvey" class="easyui-dialog" title="My Dialog" style="width:500px;height:300px;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true">   
		<table id="datagrid-openvfSurvey-list" class="easyui-datagrid" title="所拥有的品牌"  class="easyui-datagrid"
			data-options="method:'post',singleSelect:false,fit:true,
			toolbar:'#openvfSurveyListToolbar',
			pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]">
		    <thead>
			    <tr>
			      <th data-options="field:'code'">品牌编码</th>
			      <th data-options="field:'name'">品牌名称</th>
			    </tr>
			 </thead>
		  </table>  
</div> 
	<div id="win-factory-addoredit" class="easyui-window" title="新增工厂" style="width:80%;height:80%;"
	data-options="iconCls:'icon-add',modal:true,closed:true,toolbar:'#win-factory-tt'">
		<div itemId="ct"  style="height: 100%">
		<div class="row" style="height: 100%">
		<div class="col-md-5" style="height: 100%">
		    <div id="win-factory-tt">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Factory.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="resetForm()">重置</a>
			</div>
			<form id="factoryTt" method="post">
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>工厂编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="prompt:'编号由系统自动生成'"
					/>
					</td>
				</tr>
				<tr>
					<td>工厂名称:</td><td><input class="easyui-textbox" id="factoryName" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" id="factoryRemark" name="remark" type="text"
					/></td>
				</tr>
				</table>
			</form>
			
			<table id="datagrid-brand-selected" class="easyui-datagrid" title="工厂信息" 
			data-options="toolbar:'#factoryTt',idField:'id',fit:true">
			  <thead>
			    <tr>
			      <th data-options="field:'id',checkbox:true"></th>
			      <th data-options="field:'operate',width:80,formatter:Factory.removeSelectdBrandFmt">操作</th>
			      <th data-options="field:'brandCode',width:150">品牌编码</th>
			      <th data-options="field:'brandName',width:150">品牌名称</th>
			    </tr>
			  </thead>
			</table>
		</div>	
		<div class="col-md-7" style="margin-left:-1%; height: 100%;">
		  <table id="datagrid-brand" class="easyui-datagrid" title="可选品牌" data-options="
		    url:'${ctx}/manager/basedata/bussinessRange',
		    queryParams : {'query-EQ_bussinessType':2,'query-EQ_abolished':0},
		    pagination:true,pageSize:10,pageList:[10,20,50],
		    rownumbers:true,
		    toolbar:'#brandTt',fit:true
		  ">
		    <thead>
			    <tr>
			      <th data-options="field:'id',checkbox:true"></th>
			      <th data-options="field:'code'">品牌编码</th>
			      <th data-options="field:'name'">品牌名称</th>
			    </tr>
			 </thead>
		  </table>
		  <div id="brandTt">
		    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok'" onclick="Factory.selectBrand()">选择</a>
		  </div>
		</div>
		</div>
	</div>
	</div>
<script type="text/javascript">
function resetForm(){
	$('#factoryName').textbox('setValue','');
	$('#factoryRemark').textbox('setValue','');
}
$(function(){
	$("#win-openvfSurvey").window('close');
})
</script>
</body>
</html>
