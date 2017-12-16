<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="com.qeweb.scm.vendormodule.constants.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>供应商确认意向</title>
	<script type="text/javascript">
	  var vendorPropertyConstants = <%=VendorModuleTypeConstant.getVendorPropertyJson()%>;
	</script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorConfirm.js"></script>
</head>

<body style="margin:0;padding:0;">
<div class="easyui-layout" data-options="fit:true">
  <div class="easyui-panel" data-options="region:'center'">
	<table id="datagrid" title="待确认供应商" class="easyui-datagrid"
		data-options="fit:true,border:false,
		url:'${ctx}/manager/vendor/vendorBaseInfo',method:'post',singleSelect:false,
		queryParams:{'search-EQ_org.confirmStatus':0},
		toolbar:'#tt',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'org',formatter:VendorConfirm.viewFmt">企业全称</th>
		<th data-options="field:'property',formatter:function(v,r,i){return vendorPropertyConstants[v];}">企业性质</th>
		<th data-options="field:'provinceText'">省份</th>
		<th data-options="field:'cityText'">城市</th>
		<th data-options="field:'phone',formatter:function(v,r){return r.org.phone;}">联系电话</th>
		<th data-options="field:'email',formatter:function(v,r){return r.org.email;}">Email</th>
		<th data-options="field:'confirmStatus',align:'center',formatter:function(v,r){return StatusRender.render(r.org.confirmStatus,'confirm',true);}">确认状态</th>
		<th data-options="field:'mainProduct'">主要产品</th>

        <th data-options="field:'buyerCode'">采购组织编码</th>
        <th data-options="field:'buyerName'">采购组织名称</th> 

		</tr></thead>
	</table>
	<div id="tt" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="VendorConfirm.confirm()">确认</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-2012080412301',plain:true" onclick="VendorConfirm.confirmReject()">驳回</a>
		</div>
		<div>
			<form id="form-vendorPhase-search" method="post">
			企业全称：<input type="text" name="search-LIKE_org.name" class="easyui-textbox" style="width:80px;"/>
			Email：<input type="text" name="search-LIKE_org.email" class="easyui-textbox" style="width:80px;"/>
			状态:<!-- <input id="ff" class="easyui-combobox" name="search-EQ_org.confirmStatus"  style="width:80px;"/> -->
			<select name="search-EQ_org.confirmStatus" class="easyui-combobox"  style="width:80px;" data-options="editable:false">
				<option selected="selected" value="0">待确认</option>
                <option value="-1">驳回</option>
            </select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchVendorPhase()">查询</a>
			</form>
		</div>
	</div>
	<div id="win-vendorPhase-addoredit" class="easyui-window" title="新增供应商阶段" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-vendorPhase-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>供应商阶段编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>供应商阶段名称:</td><td><input class="easyui-textbox" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="submitAddorEditvendorPhase()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-vendorPhase-add').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
  </div>
</div>

<div id="dialog-baseInfo" class="easyui-dialog" data-options="closed:true,title:'供应商注册信息'" style="width:80%;height:80%">
<div itemId="ct" style="height:80%;"></div>
</div>
<script type="text/javascript">
	function searchVendorPhase(){
		var searchParamArray = $('#form-vendorPhase-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid').datagrid('load',searchParams);
	}
	$(function(){
		$('#ff').combobox({    
		    url:ctx+'/manager/vendor/vendorBaseInfo/getSturst',    
		    valueField:'id',    
		    textField:'text' ,
		   	onSelect: function(rec){    
		           $("input[name='search-EQ_org.confirmStatus']").val(rec.id);   
		      }
		})
	});
	
	</script>
</body>
</html>
	
