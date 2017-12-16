<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>询价单供应商</title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/ep/epPriceManage.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/ep/epWholeQuoManage.js"></script>
</head>
<body style="margin:0;padding:0;">
<table id="datagrid-epPrice-list" title="询价供应商列表" class="easyui-datagrid"
	data-options="url:'${ctx}/manager/ep/epVendor/${vendor}/${isApplication}',method:'post',singleSelect:false,
	fit:true,border:false,toolbar:'#epVendorToolbar',
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
	<thead><tr>   
	<th width="50px" data-options="field:'id',checkbox:true"></th>
	<th width="100px" data-options="field:'epPriceId',formatter:function(v,r,i){return r.epPrice.id},hidden:true">询价单Id</th>
	<th width="130px" data-options="field:'enquirePriceCode',formatter:epCodeVendorFmt">询价单号</th>
	<th width="130px" data-options="field:'projectName',formatter:function(v,r,i){return r.epPrice.projectName}">项目名称</th>
	<th width="100px" data-options="field:'createTime',formatter:function(v,r,i){return r.epPrice.createTime}">询价时间</th>
	<th width="100px" data-options="field:'applicationDeadline',formatter:function(v,r,i){return r.epPrice.applicationDeadline}">报名截止时间</th>
	<th width="100px" data-options="field:'quoteEndTime',formatter:function(v,r,i){return r.epPrice.quoteEndTime}">报价截止时间</th>
	<th width="100px" data-options="field:'materialType',formatter:function(v,r,i){if(r.epPrice.materialType=='0') return '无料号' ;else if(r.epPrice.materialType=='1') return '有料号';},hidden:true">询价物料类型</th>
	<th width="100px" data-options="field:'quoteWay',formatter:function(v,r,i){if(r.epPrice.quoteWay=='0') return '分项报价' ;else if(r.epPrice.quoteWay=='1') return '整体报价';}">报价方式</th>
	<th width="100px" data-options="field:'joinWay',formatter:function(v,r,i){if(r.epPrice.joinWay=='0') return '邀请' ;else if(r.epPrice.joinWay=='1') return '公开';}">参与方式</th>
	<th width="100px" data-options="field:'createUserName',formatter:function(v,r,i){return r.epPrice.createTime}">创建人</th>
	<th width="100px" data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(r.epPrice.publishStatus,'publishStatus',false);}">发布状态</th>
	<th width="100px" data-options="field:'applicationStatus',formatter:function(v,r,i){return StatusRender.render(r.epPrice.applicationStatus,'applicationStatus',false);}">报名状态</th>
	<th width="100px" data-options="field:'quoteStatus',formatter:function(v,r,i){return StatusRender.render(r.epPrice.quoteStatus,'quoteStatus',false);}">报价状态</th>
	<th width="100px" data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(r.epPrice.closeStatus,'closeStatus',false);}">关闭状态</th>
	</tr></thead>
</table>
<div id="epVendorToolbar" style="padding:5px;height:auto">
  <form id="form-epPrice-search" method="post">
	  <table style="width: 90%">
		<tr>
			<td style="width: 20%">项目名称：<input type="text" name="search-LIKE_epPrice.projectName" class="easyui-textbox" style="width:80px;"/></td>
			<td style="width: 20%">物料编码：<input type="text" name="search-LIKE_materialCode" class="easyui-textbox" style="width:80px;"/></td> 
			<td style="width: 20%">物料名称：<input type="text" name="search-LIKE_materialName" class="easyui-textbox" style="width:80px;"/></td>  
		</tr>
		<tr>
			<td>报价方式：<select class="easyui-combobox" name="search-EQ_epPrice.quoteWay" data-options="editable:false"><option value="">-全部-</option><option value="0">分项报价</option><option value="1">整体报价</option></select></td> 
			<td>发布状态：<select class="easyui-combobox" name="search-EQ_epPrice.publishStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未发布</option><option value="1">已发布</option></select></td>   
			<td>报名状态：<select class="easyui-combobox" name="search-EQ_epPrice.applicationStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未开始</option><option value="1">报名中</option><option value="2">报名完成</option></select></td>   
			<td>报价状态：<select class="easyui-combobox" name="search-EQ_epPrice.quoteStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未报价</option><option value="1">报价中</option><option value="2">报价完成</option></select></td>   
			<td>关闭状态：<select class="easyui-combobox" name="search-EQ_epPrice.closeStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未关闭</option><option value="1">已关闭</option></select></td> 
		</tr>
		<tr>
			<!-- <td>询价物料类型：<select class="easyui-combobox" name="search-EQ_epPrice.materialType"><option value="">-全部-</option><option value="0">无料号</option><option value="1">有料号</option></select></td>  -->  
	  	    <td colspan="2">询价时间：<input type="text" name="search-GTE_epPrice.createTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/>-
	  	  			  <input type="text" name="search-LTE_epPrice.createTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/>
	  	    </td>
		    <td colspan="2">报价截止时间：<input type="text" name="search-GTE_epPrice.quoteEndTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/>-
		  				 <input type="text" name="search-LTE_epPrice.quoteEndTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/></td> 
		</tr>
	  </table>
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchEpPriceList()">查询</a>  
	  <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-epPrice-search').form('reset')">重置</a>
  </form>
</div> 
<script type="text/javascript">
var clientWidth = document.body.clientWidth;	//获取窗口的高
var clientHeight = document.body.clientHeight;	//获取窗口的宽
</script>
</body>
</html>
