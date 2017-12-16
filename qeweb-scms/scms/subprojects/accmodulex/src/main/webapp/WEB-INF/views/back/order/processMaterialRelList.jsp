<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="purchase.order.Materiel"/>
</title>

</head>

<body>
<input id="processId" type="hidden" value="${processId}">
 
	<table id="datagrid-buyerMaterialRel-list" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/order/processData/getProcessMaterialRelList/${processId}',method:'post',singleSelect:false,
		toolbar:'#buyerMaterialRelListToolbar',title:'',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
		<thead>
			<tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
             	<th width="150px" data-options="field:'vcode',formatter:function(v,r,i){return r.material==null?'':r.material.code;}"><spring:message code="purchase.order.MaterialCoding"/>
             	</th>
			    <th width="150px" data-options="field:'vname',formatter:function(v,r,i){return r.material==null?'':r.material.name;}"><spring:message code="purchase.order.MaterialName"/>
			    </th>

			</tr>
		</thead>
	</table>
	<div id="buyerMaterialRelListToolbar">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="openMaterialWin()"><spring:message code="purchase.order.SelectMaterials"/>
			</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="delMaterialRel()"><spring:message code="purchase.order.DeleteMaterials"/>
			</a>
		</div>
		

	<!-- 选择供应商 -->
<div id="win-material-detail"  class="easyui-dialog"  title='<spring:message code="purchase.order.Materiel"/>' style="width:600px;height:450px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
			<table id="datagrid-material-list" title=""
				data-options="method:'post',singleSelect:false,
				toolbar:'#materialListToolbar',
				fit:true,
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
				<thead><tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
				<th width="150px" data-options="field:'code'"><spring:message code="purchase.order.MaterialCoding"/>
				</th>
				<th width="150px" data-options="field:'name'"><spring:message code="purchase.order.MaterialName"/>
				</th>
				</tr></thead>
			</table>
			
			<div id="materialListToolbar" >
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="choiceMaterial(${processId})"><spring:message code="purchase.order.Choice"/>
					</a>
				</div>
			<!-- <div>
			  <form id="form-Wuliao-search" method="post">
			  物料编码:<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width: 80px;" />
			  物料名称:<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width: 80px;" />
			  
			 <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchWuliao()" >查询</a> 
			 <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-Wuliao-search').form('reset')" >重置</a> 
			  </form>
			</div>	 -->
			
			</div>
	</div>
	



	

	<script type="text/javascript">
/* 	 $(function(){
		 $('#datagrid-material-list').datagrid();	
		 $('#win-material-detail').dialog();
	 });
	 function searchWuliao(){
		 var searchParamArray = $('#form-Wuliao-search').serializeArray();
		 var searchParams = $.jqexer.formToJson(searchParamArray);
		 $('#datagrid-material-list').datagrid('load',searchParams);
	 }
	 */
	
	</script>
</body>
</html>
