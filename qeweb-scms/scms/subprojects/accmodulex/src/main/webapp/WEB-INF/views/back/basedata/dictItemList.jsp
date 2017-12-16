<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 公用代码配置 --><spring:message code="purchase.basedata.PublicCodeConfiguration"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/dict.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-dictItem-list" title='<!-- 公用代码配置列表 --><spring:message code="purchase.basedata.PublicCodeConfigurationList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/dict/getDictItemByDictId/${dictId}',method:'post',singleSelect:false,
		toolbar:'#dictItemListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'operate',formatter:DictItem.operateFmt"><!-- 操作 --><spring:message code="vendor.operation"/></th>
		<th data-options="field:'code'"><!-- 编号 --><spring:message code="purchase.basedata.Coding"/></th>
		<th data-options="field:'name'"><!-- 名称 --><spring:message code="purchase.basedata.Names"/></th>
		</tr></thead>
	</table>
	<div id="dictItemListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="DictItem.add(${dictId})"><!-- 新增 --><spring:message code="vendor.new"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="DictItem.del()"><!-- 删除 --><spring:message code="vendor.deleting"/></a>
		</div>
		<div>
			<form id="form-dictItem-search" method="post">
			<!-- 编号 --><spring:message code="purchase.basedata.Coding"/>：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 名称 --><spring:message code="purchase.basedata.Names"/>：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="DictItem.search()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-dictItem-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	
	
	<!-- 新增 -->
	<div id="win-dictItem-addoredit" class="easyui-window" title='<spring:message code="purchase.basedata.EditSubkey"/>' style="width:400px;height:250px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="dictItem_form" method="post">
	            <input id="dictItemId" name="id" value="-1" type="hidden"/>
	            <input type="hidden" id="dictId" value="0" />
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 编号 --><spring:message code="purchase.basedata.Coding"/>:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
					    data-options="required:true"/>
					</td>
				</tr>
				<tr>
					<td><!-- 名称 --><spring:message code="purchase.basedata.Names"/>:</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true"/></td>
				</tr>
				</table>
			</form>
			 <div align="center">
					<a href="javascript:;" class="easyui-linkbutton" onclick="DictItem.submit()"><!-- 提交 --><spring:message code="vendor.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#dictItem_form').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
			</div>
	  </div>
	</div>  
	  
</body>
</html>
