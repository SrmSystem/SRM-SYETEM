<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title>采购组织下物料</title>

</head>

<body>
<input id="buyerId" type="hidden" value="${buyerId}">
 
	<table id="datagrid-buyerMaterialRel-list"
		data-options="fit:true,
		url:'${ctx}/manager/vendor/buyerVendorRel/getBuyerMaterialRelList/${buyerId}',method:'post',singleSelect:false,
		toolbar:'#buyerMaterialRelListToolbar',title:'',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
		<thead>
			<tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
             	<th width="150px" data-options="field:'vcode',formatter:function(v,r,i){return r.material==null?'':r.material.code;}">物料编码</th>
			    <th width="150px" data-options="field:'vname',formatter:function(v,r,i){return r.material==null?'':r.material.name;}">物料名称</th>

			</tr>
		</thead>
	</table>
	<div id="buyerMaterialRelListToolbar">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerVendorRel.openMaterialWin()">选择物料</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="buyerVendorRel.delMaterialRel()">删除物料</a>
		</div>
		 <div>
			<form id="form-Matter-search" method="post">
			物料编码：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
		              物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
		
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchMatter()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-Matter-search').form('reset')">重置</a>

			</form>
		 </div>
		 </div>
		

	<!-- 选择供应商 -->
<div id="win-material-detail"  title="物料" style="width:600px;height:450px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
			<table id="datagrid-material-list" title=""
				data-options="method:'post',singleSelect:false,
				toolbar:'#materialListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]">
				<thead><tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
				<th width="150px" data-options="field:'code'">物料编码</th>
				<th width="150px" data-options="field:'name'">物料名称</th>
				</tr></thead>
			</table>
			
			<div id="materialListToolbar" >
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerVendorRel.choiceMaterial(${buyerId})">选择</a>
				</div>
		   <div>
			<form id="form-Wl-search" method="post">
			物料编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
		              物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
		
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchWl()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-Wl-search').form('reset')">重置</a>

			</form>
		 </div>
			</div>
	</div>
	



	

	<script type="text/javascript">
	$(function(){
		$('#datagrid-buyerMaterialRel-list').datagrid();
		$('#win-material-detail').dialog(); 
	});
	function searchMatter(){
		var searchParamArray = $('#form-Matter-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-buyerMaterialRel-list').datagrid('load',searchParams);
	}
	function searchWl(){
		var searchParamArray = $('#form-Wl-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-material-list').datagrid('load',searchParams);
	}
	</script>
</body>
</html>
