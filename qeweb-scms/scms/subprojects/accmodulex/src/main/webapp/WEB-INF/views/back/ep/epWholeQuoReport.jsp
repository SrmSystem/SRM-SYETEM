<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
<title>整项报价报表</title>
<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
<script type="text/javascript" src="${ctx}/static/script/ep/epReportManage.js"></script>
<%-- <script type="text/javascript" src="${ctx}/static/script/ep/epPriceManage.js"></script> --%>

</head>
<body style="margin: 0;padding: 0;">
<div class="easyui-panel" style="overflow: auto;width: 100%;height: 100%">
	<table id="datagrid-epWholeQuo-report" title="询比价报表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/ep/epWholeQuo/getList',method:'post',singleSelect:false,toolbar:'#reportToolbar',
		fit:true,border:false,queryParams:{'search-EQ_epVendor.applicationStatus':'1'},
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
	<thead><tr>
		<th width="50px" data-options="field:'id',checkbox:true">id</th>
		<th width="140px" data-options="field:'opt',formatter:fmtOpt">操作</th>
		<th width="140px" data-options="field:'epCreateTime',formatter:function(v,r,i){return r.epPrice.createTime}">询价日期</th>
		<th width="140px" data-options="field:'enquirePriceCode',formatter:function(v,r,i){return r.epPrice.enquirePriceCode}">询价单号</th>
		<!-- <th data-options="field:'enquirePriceCode',formatter:fmtEpPriceCode">询价单号</th> -->
		<th width="140px" data-options="field:'vendorCode',formatter:function(v,r,i){return r.epVendor.vendorCode}">供应商编码</th>
		<th width="140px" data-options="field:'vendorName',formatter:function(v,r,i){return r.epVendor.vendorName}">供应商名称</th>
		<th width="140px" data-options="field:'materialCode',formatter:function(v,r,i){return r.epMaterial.materialCode}">物料编号</th>
		<th width="140px" data-options="field:'materialName',formatter:function(v,r,i){return r.epMaterial.materialName}">物料名称</th>
		<th width="140px" data-options="field:'materialSpec',formatter:function(v,r,i){return r.epMaterial.materialSpec}">规格型号</th>
		<th width="140px" data-options="field:'materialUnit',formatter:function(v,r,i){return r.epMaterial.materialUnit}">单位</th>
		<th width="140px" data-options="field:'quotePrice'">初次报价</th>
		<th width="140px" data-options="field:'negotiatedPrice'">核价价格</th>
		<!-- 差异率计算：差异率= （1-核价价格/报价）*100% -->
		<th width="140px" data-options="field:'diffRate',formatter:fmtDifferVal">差异率</th>
		<th width="140px" data-options="field:'remarks'">备注</th>
	</tr></thead>
	</table>
	
	<div id="reportToolbar" style="padding:5px;height:auto">
	<form id="form-epWholeQuo-search" method="post">
		<table style="width: 90%">
		<tr>
		<td>询价单号：<input type="text" name = "search-LIKE_epPrice.enquirePriceCode" class="easyui-textbox"/></td>
		<td>供应商编码：<input type="text" name = "search-LIKE_epVendor.vendorCode" class="easyui-textbox"/></td>
		<td>供应商名称：<input type="text" name = "search-LIKE_epVendor.vendorName" class="easyui-textbox"/></td>
		<td>结果：<!-- 需要增加查询条件：结果是否为N/A，方便统计。N/A表示供应商已经确认参加报名，但未报价的情况。 -->
			<select class="easyui-combobox" id="quoteStatus" name="search-EQ_quoteStatus" data-options="required:true,editable:false">
			<option value="">-全部-</option>
			<option value="0">N</option>
			<option value="1">A</option>
			</select>	
		</td>
		</tr>
		<tr>
		<td>物料编码：<input type="text" name = "search-LIKE_epMaterial.materialCode" class="easyui-textbox"/></td>
		<td>物料名称：<input type="text" name = "search-LIKE_epMaterial.materialName" class="easyui-textbox"/></td>
		<td colspan="2">询价时间：
			<input type="text" name="search-GTE_epPrice.createTime" class="easyui-datetimebox" data-options="editable:false"/>-
	  	  	<input type="text" name="search-LTE_epPrice.createTime" class="easyui-datetimebox" data-options="editable:false"/>
		</td>
		</tr>
		</table>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchList()">查询</a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-epWholeQuo-search').form('reset')">重置</a>
	</form>
	</div>
</div>
<!-- <script type="text/javascript">
var clientWidth = document.body.clientWidth;	//获取窗口的高
var clientHeight = document.body.clientHeight;	//获取窗口的宽
$(function(){
	
 }) 
</script> -->
</body>
</html>