<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="purchase.basedata.RegionalManagement"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/areaSelect.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/areaList.js"></script>
	
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-area-list" title='<spring:message code="base.AreaList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/area',method:'post',singleSelect:false,
		toolbar:'#areaListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'upName'"><spring:message code="base.ParentName"/><!-- 上级名称 --></th>
		<th data-options="field:'code'"><spring:message code="base.AreaCode"/><!-- 区域编码 --></th>
		<th data-options="field:'name'"><spring:message code="base.AreaName"/><!-- 区域名称 --></th>
		</tr></thead>
	</table>
	<div id="areaListToolbar" style="padding:5px;">
		<div>
		<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addCountry()">新增国家</a> -->
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addProvince()"><spring:message code="button.add"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteArea()"><spring:message code="button.delete"/><!-- 删除 --></a>
		</div>
		<div>
			<form id="form-area-search" method="post">
			<spring:message code="base.AreaCode"/><!-- 区域编码 -->：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="base.AreaName"/><!-- 区域名称 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchArea()"><spring:message code="button.query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-area-search').form('reset')"><spring:message code="button.reset"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	<div id="win-country-addoredit" class="easyui-window" title='<spring:message code="button.add"/>' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-country-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<input name="parentId" value="0" data-options="required:true" type="hidden"/>
				<input name="level" value="0" type="hidden" />
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="base.AreaCode"/><!-- 区域编码 -->:</td><td><input class="easyui-textbox" name="code" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><spring:message code="base.AreaName"/><!-- 区域名称 -->:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditarea()"><spring:message code="button.submit"/><spring:message code="button.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-country-addoredit').form('reset')"><spring:message code="button.reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
	
	
	
	
	<div id="win-province-addoredit" class="easyui-window" title='<spring:message code="button.add"/>' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-province-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<input id="level" name="level" value="1" type="hidden"/>
				<input id="parentId" name="parentId" type="hidden">
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="purchase.order.site"/><!-- 地址 -->:</td>
					<td>
						<input id="combobox_province" name="province" class="easyui-combobox" >
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input id="combobox_city" name="city" class="easyui-combobox" >
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<input id="combobox_area" name="area" class="easyui-combobox" >
					</td>
				</tr>
				<tr>
					<td><spring:message code="base.AreaCode"/><!-- 区域编码 -->:</td><td><input class="easyui-textbox" name="code" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><spring:message code="base.AreaName"/><!-- 区域名称 -->:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditProvince()"><spring:message code="button.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-province-addoredit').form('reset')"><spring:message code="button.reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>



</body>
</html>
