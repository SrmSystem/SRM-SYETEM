<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>新建/发布询价单</title>
	<script type="text/javascript" src="${ctx}/static/script/purchase/dialog.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/ep/epPriceManage.js"></script>
	<script type="text/javascript" src="${ctx}/static/base/util/IsTimeUtil.js"></script>
</head>
<body style="margin:0;padding:0;">
<table id="datagrid-epPrice-list" title="询价单列表" class="easyui-datagrid"
	data-options="url:'${ctx}/manager/ep/epPrice/${vendor}/${isApplication}',method:'post',singleSelect:false,
	fit:true,border:false,toolbar:'#epPriceToolbar',
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
	<thead><tr>   
	<th width="50px" data-options="field:'id',checkbox:true"></th>
	<c:if test="${!vendor}">
	<th width="100px" data-options="field:'optxx',formatter:optxxFmt">操作</th>
	<th width="130px" data-options="field:'enquirePriceCode',formatter:epCodeFmt">询价单号</th>
	</c:if>
	<c:if test="${vendor}">
	<th width="130px" data-options="field:'enquirePriceCode',formatter:epCodeVendorFmt">询价单号</th>
	</c:if>
	<th width="130px" data-options="field:'projectName'">项目名称</th>
	<th width="100px" data-options="field:'createTime'">询价时间</th>	<!-- 创建时间 -->
	<th width="100px" data-options="field:'applicationDeadline'">报名截止时间</th>
	<th width="100px" data-options="field:'quoteEndTime'">报价截止时间</th>
	<th data-options="field:'buyerName',formatter:function(v,r,i){if(r.buyer==null) return '';else return r.buyer.name}">采购组织</th>
	<th width="100px" data-options="field:'materialType',formatter:function(v,r,i){if(v=='0') return '无料号' ;else if(v=='1') return '有料号';},hidden:true">询价物料类型</th>
	<th width="100px" data-options="field:'quoteWay',formatter:function(v,r,i){if(v=='0') return '分项报价' ;else if(v=='1') return '整体报价';}">报价方式</th>
	<th width="100px" data-options="field:'joinWay',formatter:function(v,r,i){if(v=='0') return '邀请' ;else if(v=='1') return '公开';}">参与方式</th>
	<th width="100px" data-options="field:'createUserName'">创建人</th>
	<th width="100px" data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}">发布状态</th>
	<th width="100px" data-options="field:'applicationStatus',formatter:function(v,r,i){return StatusRender.render(v,'applicationStatus',false);}">报名状态</th>
	<th width="100px" data-options="field:'quoteStatus',formatter:function(v,r,i){return StatusRender.render(v,'quoteStatus',false);}">报价状态</th>
	<th width="100px" data-options="field:'closeStatus',formatter:function(v,r,i){return StatusRender.render(v,'closeStatus',false);}">关闭状态</th>
	</tr></thead>
</table>
<div id="epPriceToolbar" style="padding:5px;height:auto">
  <form id="form-epPrice-search" method="post">
	  <table style="width: 90%">
		<tr>
			<td style="width: 25%">项目名称：<input type="text" name="search-LIKE_projectName" class="easyui-textbox"/></td>
			<td style="width: 25%">询价单号：<input type="text" name="search-LIKE_enquirePriceCode" class="easyui-textbox"/></td>
			<td style="width: 25%">物料编码：<input type="text" name="search-LIKE_materialCode" class="easyui-textbox"/></td> 
			<td style="width: 25%">参与方式：<select class="easyui-combobox" name="search-EQ_joinWay" data-options="editable:false"><option value="">-全部-</option><option value="0">邀请</option><option value="1">公开</option></select></td>
		</tr>
		<tr>
		    <td>物料名称：<input type="text" name="search-LIKE_materialName" class="easyui-textbox"/></td> 
			<c:if test="${!vendor}">  
			<td>供应商编码：<input type="text" name="search-LIKE_vendorCode" class="easyui-textbox"/></td>    
			<td>供应商名称：<input type="text" name="search-LIKE_vendorName" class="easyui-textbox"/></td> 
			</c:if> 
			<td>报名状态：<select class="easyui-combobox" name="search-EQ_applicationStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未开始</option><option value="1">报名中</option><option value="2">报名完成</option></select></td>   
		</tr>
		<tr>
			<td>报价方式：<select class="easyui-combobox" name="search-EQ_quoteWay" data-options="editable:false"><option value="">-全部-</option><option value="0">分项报价</option><option value="1">整体报价</option></select></td> 
			<td>发布状态：<select class="easyui-combobox" name="search-EQ_publishStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未发布</option><option value="1">已发布</option></select></td>   
		    <td>报价状态：<select class="easyui-combobox" name="search-EQ_quoteStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未报价</option><option value="1">报价中</option><option value="2">报价完成</option></select></td>   
			<td>关闭状态：<select class="easyui-combobox" name="search-EQ_closeStatus" data-options="editable:false"><option value="">-全部-</option><option value="0">未关闭</option><option value="1">已关闭</option></select></td> 
			
		</tr>
		<tr>
			<!-- <td>询价物料类型：<select class="easyui-combobox" name="search-EQ_materialType"><option value="">-全部-</option><option value="0">无料号</option><option value="1">有料号</option></select></td> -->   
	  	    <td colspan="2">询价时间：<input type="text" name="search-GTE_createTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/>-
	  	  			  <input type="text" name="search-LTE_createTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/>
	  	    </td>
		    <td colspan="2">报价截止时间：<input type="text" name="search-GTE_quoteEndTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/>-
		  				 <input type="text" name="search-LTE_quoteEndTime" class="easyui-datetimebox" data-options="showSeconds:false,formatter:formatDate,editable:false" style="width:130px;"/></td> 
		</tr>
	  </table>
	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchEpPriceList()">查询</a>  
	  <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-epPrice-search').form('reset')">重置</a>
  </form>
  <c:if test="${!vendor}">
  <div align="right">
	<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addMaterialEpModule()">新增物料申购单询价单</a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addPurchaseEpModule()">新增采购计划询价单</a> -->
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addPriceEpModule('0','新增询价单','addWin')">新增询价单</a>
	<c:if test="${isBuyManage}">
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-no',plain:true" onclick="stopQuote()">停止报价</a>
	</c:if>
  </div>
  </c:if>
</div> 


	<div id="win-time-addoredit" class="easyui-window"
		style="width: 400px; height: 300px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-time'">
		<form id="form-time-addoredit" method="post">
			<input id="buyerMaterialId" name="id" value="0" type="hidden" /> 
			<table style="text-align: right; padding: 5px; margin: auto;" cellpadding="5">
			<tr>
				<td >报名截止时间:</td>
				<td>
					<input  name="applicationDeadline" value="" class="easyui-datebox" data-options="required:true,editable:false,validType:'isTimeRight'" />
				</td>
			</tr>
			<tr><td></td><td></td>
			</tr>
						<tr>
				<td >报价截止时间:</td>
				<td>
				<input  name="quoteEndTime" value="" class="easyui-datebox" data-options="required:true,editable:false,validType:'isTimeRight'" />
				</td>
			</tr>
				
			</table>
		</form>
		<div id="dialog-adder-time">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="saveEditTime()">提交</a> <a
				href="javascript:;" class="easyui-linkbutton"
				onclick="$('#form-time-addoredit').form('reset')">重置</a>
		</div>
	</div>
<script type="text/javascript">
var clientWidth = document.body.clientWidth;	//获取窗口的高
var clientHeight = document.body.clientHeight;	//获取窗口的宽
$(function(){
	
 }) 
</script>
</body>
</html>
