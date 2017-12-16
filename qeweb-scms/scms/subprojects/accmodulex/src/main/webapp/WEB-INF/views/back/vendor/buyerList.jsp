<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>采购组织管理</title>
	<script type="text/javascript" src="${ctx}/static/script/vendor/buyerVendorRel.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/vendor/dialog.js"></script>
		
	<script type="text/javascript">
		function operFmt(v,r,i){
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="buyerVendorRelList('+r.id+')">供应商</a>'
		+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="buyerMaterialRelList('+r.id+')">物料</a>'
		+'&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="buyerMaterialTypeRelList('+r.id+')">物料类型</a>';
		
		}
		</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-buyer-list"
		data-options="fit:true,
		url:'${ctx}/manager/vendor/buyerVendorRel',method:'post',singleSelect:false,
		toolbar:'#buyerListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[5,10,15,20]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="10%" data-options="field:'oper',formatter:operFmt">操作</th>
		<th width="10%" data-options="field:'code'">采购组织编码</th>
		<th width="23%" data-options="field:'name'">采购组织名称</th>
		</tr></thead>
	</table>
	<div id="buyerListToolbar" style="padding:5px;">
		<div>

		</div>
		<div>
			<form id="form-buyer-search" method="post">
			采购组织编码：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			采购组织名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
		
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchBuyer()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-buyer-search').form('reset')">重置</a>

			</form>
		</div>
	</div>
	<div id="dialog-buyerVendorRel" style="width: 80%; height: 80%" data-options="iconCls:'icon-add',modal:true,closed:true"></div>
	<div id="dialog-buyerMaterialRel" style="width: 80%; height: 80%" data-options="iconCls:'icon-add',modal:true,closed:true"></div>
	<div id="dialog-buyerMaterialTypeRel" style="width: 80%; height: 80%" data-options="iconCls:'icon-add',modal:true,closed:true"></div>


<script type="text/javascript">
$(function(){
	$('#datagrid-buyer-list').datagrid();
})
function searchBuyer(){
	var searchParamArray = $('#form-buyer-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-buyer-list').datagrid('load',searchParams);
}

function openWin(title, width, height, url,dd) {

	$(dd).dialog({    
	    title: title,    
	    closed: false,    
	    cache: false, 
	    iconCls:'icon-save',
	    href: url,    
	    modal: true,
	    onClose : function() {
            //$(this).dialog('destroy');
          
        }
	});
}


function buyerVendorRelList(id) {
	var url=ctx+'/manager/vendor/buyerVendorRel/getBuyerVendorRelList/'+id;
	 new openWin('供应商', 800, 600, url,'#dialog-buyerVendorRel');
}

function buyerMaterialRelList(id) {
	var url=ctx+'/manager/vendor/buyerVendorRel/getBuyerMaterialRelList/'+id;
	 new openWin('物料', 800, 600, url,'#dialog-buyerMaterialRel');
}

function buyerMaterialTypeRelList(id) {
	var url=ctx+'/manager/vendor/buyerVendorRel/getBuyerMaterialTypeRelList/'+id;
	 new openWin('物料类型', 800, 600, url,'#dialog-buyerMaterialTypeRel');
}


</script>
</body>
</html>
