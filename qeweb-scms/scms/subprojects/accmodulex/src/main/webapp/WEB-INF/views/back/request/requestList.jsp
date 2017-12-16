<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>要货单管理</title>
	<script type="text/javascript">
		var vendor = ${vendor};
	</script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>   
	<script type="text/javascript" src="${ctx}/static/script/purchase/RequestManage.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-request-list" title="要货单列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/order/goodsrequest/${vendor}',method:'post',singleSelect:false,
		toolbar:'#requestListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>  
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'manage',formatter:RequestManage.requestCodeFmt">操作</th>
		<th data-options="field:'requestCode'">要货单号</th>
		<c:if test="${vendor == false }">
			<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
			<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		</c:if>
		<th data-options="field:'sender',formatter:function(v,r,i){if(r.sender) return r.sender.name; else return '';}">发货方</th>
		<th data-options="field:'receiveOrg'">收货方</th>
		<th data-options="field:'purchaseUser',formatter:function(v,r,i){return r.purchaseUser.name;}">采购员</th>
		<c:if test="${vendor == false }">
			<th data-options="field:'orderDate'">需求日期</th>
			<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}">发布状态</th>
			<th data-options="field:'publishTime'">发布时间</th>
		</c:if>
		<th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}">确认状态</th>
		<th data-options="field:'confirmTime'">确认时间</th>
		<th data-options="field:'deliveryStatus',formatter:function(v,r,i){return StatusRender.render(v,'deliveryStatus',false);}">发货状态</th>
		<th data-options="field:'receiveStatus',formatter:function(v,r,i){return StatusRender.render(v,'receiveStatus',false);}">收货状态</th>
		<th data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(v,'closeStatus',false);}">关闭状态</th>  
		</tr></thead>
	</table>
	<div id="requestListToolbar" style="padding:5px;">
		<div>
			<c:if test="${vendor == false }">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RequestManage.addRequest()">新建</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RequestManage.operateRequest('publish')">发布</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RequestManage.operateRequest('close')">关闭</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RequestManage.importRequest()">导入要货单</a>
			</c:if>  
			<c:if test="${vendor }">
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RequestManage.operateRequest('confirm')">确认</a>
			</c:if>
		</div>
		<div>
			<form id="form-request-search" method="post">
			要货单号：<input type="text" name="search-LIKE_requestCode" class="easyui-textbox" style="width:100px;"/>
			<c:if test="${vendor == false }">
				供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:100px;"/>
				供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:100px;"/>
				发布状态：<select class="easyui-combobox" name="search-EQ_publishStatus"><option value="">-全部-</option><option value="0">未发布</option><option value="1">已发布</option></select>
				<br/>
			</c:if>
			确认状态：<select class="easyui-combobox" name="search-EQ_confirmStatus"><option value="">-全部-</option><option value="0">未确认</option><option value="1">已确认</option></select>
			发货状态：<select class="easyui-combobox" name="search-EQ_deliveryStatus"><option value="">-全部-</option><option value="0">未发货</option><option value="1">已发货</option><option value="2">部分发货</option></select>
			收货状态：<select class="easyui-combobox" name="search-EQ_receiveStatus"><option value="">-全部-</option><option value="0">未收货</option><option value="1">已收货</option><option value="2">部分收货</option></select>
			关闭状态：<select class="easyui-combobox" name="search-EQ_closeStatus"><option value="">-全部-</option><option value="0">未关闭</option><option value="1">已关闭</option></select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="RequestManage.searchrequest()">查询</a>
			</form>
		</div>
	</div>
	
	<%-- 要货单导入--%>
	<%@include file="requestInport.jsp" %> 

<script type="text/javascript">
$(function(){

})
</script>
</body>
</html>
