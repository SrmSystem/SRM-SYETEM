<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
	<head>
		<title>质量改进（供方）</title>
		<script type="text/javascript" src="${ctx}/static/script/qualityImprove/qualityImprove.js"></script>
		<script type="text/javascript">
			function improveStatusFmt(v,r,i){
				if(r.improveStatus == 0)
					return '等待整改';
				else if(r.improveStatus == 1)
					return '已整改';
				else if(r.improveStatus == -1)
					return '整改驳回';
				else if(r.improveStatus == -2)
					return '驳回反馈';
				return '等待整改';
			}
		</script>
	</head>

	<body style="margin: 0; padding: 0;">
		
		<table id="datagrid-qualityImprove-list" title="质量改进列表（供方）" class="easyui-datagrid"
			data-options="fit:true,
			url:'${ctx}/manager/qualityassurance/qualityImprove/${vendor}',method:'post',singleSelect:false,
			fit:true,border:false,toolbar:'#qualityImproveListToolbar',queryParams:{'search-EQ_dataStatus':1},				
			pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<!-- <th data-options="field:'operate',formatter:qualityImprove.operateFmt">操作</th> -->
					<th data-options="field:'vendor.name'">供应商名称</th>
					<th data-options="field:'informFileName',formatter:qualityImprove.informFileDownLoad">质量改进通知</th>
					<th data-options="field:'improveFileName',formatter:qualityImprove.improveFileDownLoad">质量改进方案</th>
					<th data-options="field:'improveStatus',formatter:improveStatusFmt">审核状态</th>
					<th data-options="field:'createUserName'">创建人</th>
					<th data-options="field:'createTime'">创建时间</th>
				</tr>
			</thead>
		</table>
		
		<div id="qualityImproveListToolbar" style="padding: 5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_go',plain:true" onclick="qualityImprove.addImprove()">上传/修改质量改进方案</a>
			</div>		
			<div>
				<form id="form-qualityImprove-search" method="post">
					审核状态：<select name="search-EQ_improveStatus" 
data-options="editable:false" class="easyui-combobox" style="width:150px;">
	                   	<option value="">全部</option>
	                   	<option value="0">等待整改</option>
	                   	<option value="1">已整改</option>
	                   	<option value="-1">整改驳回</option>
                   		<option value="-2">驳回反馈</option>
	                   </select>
					创建时间:<input type="text" name="search-GT_createTime" class="easyui-datebox" 
data-options="editable:false" style="width:100px;">~  
					  <input type="text" name="search-LT_createTime" class="easyui-datebox" 
data-options="editable:false" style="width:100px;">
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="qualityImprove.search()">查询</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-qualityImprove-search').form('reset')">重置</a>
				</form>
			</div>
		</div>
	
	
	<!-- 上传/修改质量改进方案 -->
		<div id="win-qualityImprove-addImprove" class="easyui-easyui-dialog" title="上传/修改质量改进方案 " style="width:30%; height: 35%" data-options="modal:true,closed:true,buttons:'#dialog-adder-a'">
			<form id="form-qualityImprove-addImprove" method="post" enctype="multipart/form-data" style="text-align: center;">
				<table>
					<tr><td>改进通知附件</td><td><input type=file id="improveFile" name="improveFile" /></td></tr>
				</table>		
	          </form> 			
			<div id="dialog-adder-a">
				<a href="javascript:;" class="easyui-linkbutton" onclick="qualityImprove.saveImprove()">保存</a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#win-qualityImprove-addImprove').window('close');">关闭</a>
			</div>
			
		</div>
	<!-- end -->
	
	
	
	
	
	
<script type="text/javascript">
	function df(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		return y+'-'+m+'-'+d;
	} 
	
</script>	
				
	</body>
</html>

