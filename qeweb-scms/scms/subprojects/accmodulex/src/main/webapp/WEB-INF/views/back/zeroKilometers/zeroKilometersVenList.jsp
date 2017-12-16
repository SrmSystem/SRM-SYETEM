<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
	<head>
		<title>零公里质检信息管理（供方）</title>
		<script type="text/javascript" src="${ctx}/static/script/zeroKilometers/zeroKilometers.js"></script>
		<script type="text/javascript">
			function statusFmt(v,r,i){
				if(r.status == 0)
					return '待发布';
				else if(r.status == 1)
					return '已发布';
				else if(r.status == -1)
					return '关闭';
				return '待发布';
			}
		</script>
	</head>

	<body style="margin: 0; padding: 0;">
		
		<table id="datagrid-zeroKilometers-list" title="零公里质检列表" class="easyui-datagrid"
			data-options="fit:true,
			url:'${ctx}/manager/qualityassurance/zeroKilometers/${vendor }',method:'post',singleSelect:false,
			toolbar:'#zeroKilometersListToolbar',queryParams:{'search-EQ_status':1},							
			pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<!-- <th data-options="field:'operate',formatter:zeroKilometers.operateFmt">操作</th> -->
					<th data-options="field:'month'">月份</th>
					<th data-options="field:'reportCode'">报告单编号</th>
					<th data-options="field:'vcode',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.code;}}">供应商编号</th>
					<th data-options="field:'vendorba',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.name;}}">供应商名称</th>
					<th data-options="field:'materialcode',formatter:function(v,r,i){if(r.material){return r.material.code;}}">祸首件图号</th>
					<th data-options="field:'materialname',formatter:function(v,r,i){if(r.material){return r.material.name;}}">祸首件名称</th>
					<th data-options="field:'factory'">生产厂</th>
					<th data-options="field:'motorFactory'">主机厂</th>
					<th data-options="field:'models'">车型</th>
					<th data-options="field:'counts'">台数</th>
					<th data-options="field:'mileage'">行驶里程</th>
					<th data-options="field:'maintenanceTime'">维修时间</th>
					<th data-options="field:'status',formatter:statusFmt">状态</th>
				</tr>
			</thead>
		</table>
		
		<div id="zeroKilometersListToolbar" style="padding: 5px;">
			<div>
				<form id="form-zeroKilometers-search" method="post">
					<input type="hidden" name="search-EQ_status" value="1" />
					祸首件图号：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width: 80px;" /> 
					祸首件名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width: 80px;" /> 
				        维修时间:<input id="sStart" name="search-GTE_maintenanceTime" data-options="editable:false" class="easyui-datebox"   style="width:110px;"/>到
				        	 <input id="sEnd" name="search-LTE_maintenanceTime" data-options="editable:false" class="easyui-datebox"   style="width:110px;"/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="zeroKilometers.vSearch()">查询</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-zeroKilometers-search').form('reset')">重置</a>
				</form>
			</div>
		</div>
				
				
	
	
	
<script type="text/javascript">
		
		
	
	
</script>	
				
	</body>
</html>

