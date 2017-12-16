<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>采购组织下供应商</title>

</head>

<body>
<input id="buyerId" type="hidden" value="${buyerId}">
 
	<table id="datagrid-buyerVendorRel-list"
		data-options="fit:true,
		url:'${ctx}/manager/vendor/buyerVendorRel/getBuyerVendorRelList/${buyerId}',method:'post',singleSelect:false,
		toolbar:'#buyerVendorRelListToolbar',title:'',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
		<thead>
			<tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
             	<th width="150px" data-options="field:'vcode',formatter:function(v,r,i){return r.vendor==null?'':r.vendor.code;}">供应商编码</th>
			    <th width="150px" data-options="field:'vname',formatter:function(v,r,i){return r.vendor==null?'':r.vendor.name;}">供应商名称</th>

			</tr>
		</thead>
	</table>
	<div id="buyerVendorRelListToolbar">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerVendorRel.openWin()">选择供应商</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="buyerVendorRel.delRel()">删除供应商</a>
		</div>
		<div>
			<form id="form-Supplier-search" method="post">
			 供应商编码：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
		               供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
		
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchSupplier()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-Supplier-search').form('reset')">重置</a>

			</form>
		 </div>
		 </div>
		
		
		

	<!-- 选择供应商 -->
<div id="win-vendor-detail"  title="供应商" style="width:600px;height:450px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
			<table id="datagrid-vendor-list" title=""
				data-options="method:'post',singleSelect:false,
				toolbar:'#vendorListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
				<thead><tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
				<th width="150px" data-options="field:'code'">供应商编码</th>
				<th width="150px" data-options="field:'name'">供应商名称</th>
				</tr></thead>
			</table>
			
			<div id="vendorListToolbar" >
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerVendorRel.choice(${buyerId})">选择</a>
				</div>
		   <div>
			<form id="form-Gys-search" method="post">
			 供应商编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
		               供应商名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
		
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchGys()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-Gys-search').form('reset')">重置</a>

			</form>
		 </div>
			</div>
	</div>
	
	
  


	

	<script type="text/javascript">
	$(function(){
		$('#datagrid-buyerVendorRel-list').datagrid();
		$('#win-vendor-detail').dialog(); 
	});
	function searchSupplier(){
		var searchParamArray = $('#form-Supplier-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-buyerVendorRel-list').datagrid('load',searchParams);
	}
	function searchGys(){
		var searchParamArray = $('#form-Gys-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-vendor-list').datagrid('load',searchParams);
	}
	
	
	
	
	</script>
</body>
</html>
