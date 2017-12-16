<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="purchase.basedata.PublicCodeConfiguration"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/dict.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-dict-list" title='<!-- 公用代码配置列表 --><spring:message code="purchase.basedata.PublicCodeConfigurationList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/dict',method:'post',singleSelect:false,
		toolbar:'#dictListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'operate',formatter:Dict.operateFmt"><!-- 操作 --><spring:message code="vendor.operation"/></th>
		<th data-options="field:'dictCode'"><!-- 编号 --><spring:message code="purchase.basedata.Coding"/></th>
		<th data-options="field:'dictName'"><!-- 名称 --><spring:message code="purchase.basedata.Names"/></th>
		<th data-options="field:'dictDesc'"><!-- 描述 --><spring:message code="purchase.basedata.Describe"/></th>
		</tr></thead>
	</table>
	<div id="dictListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Dict.add()"><!-- 新增 --><spring:message code="vendor.new"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="Dict.del()"><!-- 删除 --><spring:message code="vendor.deleting"/></a>
		</div>
		<div>
			<form id="form-dict-search" method="post">
			<!-- 编号 --><spring:message code="purchase.basedata.Coding"/>：<input type="text" name="search-LIKE_dictCode" class="easyui-textbox" style="width:80px;"/>
			<!-- 名称 --><spring:message code="purchase.basedata.Names"/>：<input type="text" name="search-LIKE_dictName" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Dict.search()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-dict-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	
	
	<!-- 新增 -->
	<div id="win-dict-addoredit" class="easyui-window" title='<spring:message code="vendor.edit"/>' style="width:400px;height:250px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="dict_form" method="post">
	            <input id="dictId" name="id" value="0" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 编号 --><spring:message code="purchase.basedata.Coding"/>:</td><td><input class="easyui-textbox" id="dictCode" name="dictCode" type="text"
					    data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td><!-- 名称 --><spring:message code="purchase.basedata.Names"/>:</td><td><input class="easyui-textbox" id="dictName" name="dictName" type="text"
						data-options="required:true"/></td>
				</tr>
				<tr>
					<td><!-- 描述 --><spring:message code="purchase.basedata.Describe"/>:</td><td><input class="easyui-textbox" id="dictDesc" name="dictDesc" type="text"/></td>
				</tr>
				</table>
			</form>
			 <div align="center">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Dict.submit()"><!-- 提交 --><spring:message code="vendor.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#dict_form').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
			</div>
	  </div>
	</div>  
</body>
</html>
