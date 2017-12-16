<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>采购组织下物料类型</title>

</head>

<body>
<input id="buyerId" type="hidden" value="${buyerId}">
 
	<table id="datagrid-buyerMaterialTypeRel-list"
		data-options="fit:true,
		url:'${ctx}/manager/vendor/buyerVendorRel/getBuyerMaterialTypeRelList/${buyerId}',method:'post',singleSelect:false,
		toolbar:'#buyerMaterialTypeRelListToolbar',title:'',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
		<thead>
			<tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
             	<th width="150px" data-options="field:'vcode',formatter:function(v,r,i){return r.materialType==null?'':r.materialType.code;}">物料类型编码</th>
			    <th width="150px" data-options="field:'vname',formatter:function(v,r,i){return r.materialType==null?'':r.materialType.name;}">物料类型名称</th>

			</tr>
		</thead>
	</table>
	<div id="buyerMaterialTypeRelListToolbar">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerVendorRel.openMaterialTypeWin()">选择</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="buyerVendorRel.delMaterialTypeRel()">删除</a>
		</div>
		<div>
			<form id="form-MaterialType-search" method="post">
			物料类型编码：<input type="text" name="search-LIKE_materialType.code" class="easyui-textbox" style="width:80px;"/>
		              物料类型名称：<input type="text" name="search-LIKE_materialType.name" class="easyui-textbox" style="width:80px;"/>
		
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchMatterialType()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-MaterialType-search').form('reset')">重置</a>

			</form>
		 </div>
		 </div>
		

	<!-- 选择供应商 -->
<div id="win-materialType-detail"  title="物料类型" style="width:600px;height:450px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
			<table id="datagrid-materialType-list" title=""
				data-options="method:'post',singleSelect:false,
				toolbar:'#materialTypeListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
				<thead><tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
				<th width="150px" data-options="field:'code'">物料类型编码</th>
				<th width="150px" data-options="field:'name'">物料类型名称</th>
				</tr></thead>
			</table>
			
			<div id="materialTypeListToolbar" >
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerVendorRel.choiceMaterialType(${buyerId})">选择</a>
				</div>
			  <div>
				<form id="form-Wllx-search" method="post">
				物料类型编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			              物料类型名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchWllx()">查询</a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-Wllx-search').form('reset')">重置</a>
	
				</form>
			 </div>		
			</div>
	</div>
	



	

	<script type="text/javascript">
	$(function(){
		$('#datagrid-buyerMaterialTypeRel-list').datagrid();
		$('#win-materialType-detail').dialog(); 
	});
	function searchMatterialType(){
		var searchParamArray = $('#form-MaterialType-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-buyerMaterialTypeRel-list').datagrid('load',searchParams);
	}
	function searchWllx(){
		var searchParamArray = $('#form-Wllx-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-materialType-list').datagrid('load',searchParams);
	}
	</script>
</body>
</html>
