<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
	<head>
		<title>
			<c:if test="${ppmType == 1 }">
				交付PPM
			</c:if>
			<c:if test="${ppmType == 2 }">
				毛坯废品率
			</c:if>
			<c:if test="${ppmType == 3 }">
				零公里PPM
			</c:if>
			<c:if test="${ppmType == 4 }">
				售后PPM
			</c:if>
			<c:if test="${ppmType == 5 }">
				PPAP一次通过率
			</c:if>
		</title>
		<script type="text/javascript" src="${ctx}/static/script/ppm/ppm.js"></script> 
	</head>
	
	<body style="margin:0;padding:0;">
		<table id="datagrid" class="easyui-datagrid"
			   data-options="fit:true,url:'${ctx}/manager/qualityassurance/ppm/${ppmType}',method:'post',singleSelect:false,   
			   toolbar:'#datagridToolbar',
			   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
			 
			<thead><tr>
			<th data-options="field:'id',checkbox:true">ID</th>
			<th data-options="field:'month'">月份</th>	
			<th data-options="field:'vendor.code'">供应商编码</th>			
			<th data-options="field:'vendor.name'">供应商名称</th>
			<c:if test="${ppmType == 2 || ppmType == 3 || ppmType == 4 }">
				<th data-options="field:'material.code'">物料图号</th>
				<th data-options="field:'material.name'">物料名称</th>
			</c:if>
			<c:if test="${ppmType == 1 || ppmType == 3 || ppmType == 4 }">
				<th data-options="field:'ppm'">PPM值</th>
			</c:if>
			<c:if test="${ppmType == 2 }">
				<th data-options="field:'rate'">毛坯废品率</th>
			</c:if>
			<c:if test="${ppmType == 5 }">
				<th data-options="field:'rate'">PPAP一次通过率</th>
			</c:if>
			</tr></thead>
		</table>
		<div id="datagridToolbar" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="ppm.exportPPM(${ppmType})">导出</a>
			</div>
			<div>
				<form id="form-ppm-search" method="post">
					供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:100px;"/>
					供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:100px;"/>
					<c:if test="${ppmType == 2 || ppmType == 3 || ppmType == 4 }">
						物料图号：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:100px;"/>
						物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:100px;"/>
					</c:if>
					月份：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:100px;"/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="ppm.search()">查询</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-ppm-search').form('reset')">重置</a>
				</form>
			</div>
		</div>

	</body>
</html>