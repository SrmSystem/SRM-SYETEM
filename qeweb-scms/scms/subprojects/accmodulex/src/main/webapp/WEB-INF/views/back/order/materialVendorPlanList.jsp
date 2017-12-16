<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<title><spring:message code="purchase.order.Supplier"/></title>
</head>



<body style="margin: 0; padding: 0;">

<div class="easyui-panel"
		style="overflow: auto; width: 100%; height: 100%">
		
			<form id="form-lingshi" method="post">
			</form>
			
				<c:if test="${materialPlan.publishStatus eq 0}">
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="publishMaterialPlan(0)"><spring:message code="purchase.order.Preservation"/></a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="publishMaterialPlan(1)"><spring:message code="purchase.order.Release"/></a>
				</c:if>		
						
	  <form id="form-vendorQuota-addoredit" method="post" >
	  		<table style="padding:10px;width: 90%;margin: auto;">

					
			<tr>
				<td width="50%"><spring:message code="purchase.order.MaterialCoding"/>:${materialPlan.material.code}
				</td>
				<td width="50%"><spring:message code="purchase.order.MaterialName"/>:${materialPlan.material.name}
				</td>
			</tr>
			<tr>
				<td width="50%"><spring:message code="purchase.order.DemandDate"/>:<fmt:formatDate value="${materialPlan.planTime}" pattern="yyyy-MM-dd"/> 
				</td>
				<td width="50%"><spring:message code="purchase.order.DemamdNumber"/>:${materialPlan.planNum}
				</td>
			</tr>
			</table>
	  
		 
		 <input type="hidden" name="tableDatas" id="selectTableDatas" />
		 <input type="hidden" name="id"  id="materialPlanId" value="${materialPlan.id}">
		 <input type="hidden" name="planNum"  id="planCount" value="${materialPlan.planNum}">
		 
		 
	  </form>
	  

	
	
	  <table id="datagrid-vendorQuota-list" class="easyui-datagrid" data-options="fit:false,method:'post', url:'${ctx}/manager/order/materialPlan/getMaterialVendorPlanList/${materialPlan.id}',
	  singleSelect:false,toolbar:'#quotaToolbar',title:'<spring:message code="purchase.order.SupplierList"/>',pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],checkOnSelect:true
			<c:if test="${materialPlan.publishStatus eq 0}">
			,onClickRow:onClickRow2
			</c:if>

		">
				<thead>
					<tr>
					   	<th data-options="field:'id',checkbox:true"></th>
					   	<th data-options="field:'vendorCode'"><spring:message code="purchase.order.vendorCode"/></th>
       					<th data-options="field:'vendorName'"><spring:message code="purchase.order.VendorName"/></th>
					    <th data-options="field:'planNum',editor:{type:'numberbox', options: {required:true}}"><spring:message code="purchase.order.DemamdNumber"/></th>   
					</tr>
				</thead>
			</table>
			<div id="quotaToolbar">
			<div>
				<c:if test="${materialPlan.publishStatus eq 0}">
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="openVendorWin()"><spring:message code="purchase.order.Choice"/></a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteVendor('#datagrid-vendorQuota-list')"><spring:message code="purchase.order.Delete"/></a>
				</c:if>
			</div>
			</div>
	
</div>
		
		<!-- 选择供应商 -->
	<div id="win-proVendor-detail" title="<spring:message code="purchase.order.Supplier"/>" class="easyui-window" style="width: 600px; height: 450px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<table id="datagrid-proVendor-list" title="<spring:message code="purchase.order.SupplierList"/>" data-options="method:'post',singleSelect:false, toolbar:'#proVendorListToolbar',
				fit:true,pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
			<thead>
				<tr>
					<th width="30px" data-options="field:'id',checkbox:true"></th>
					<th width="30%" data-options="field:'code'"><spring:message code="purchase.order.vendorCode"/></th>
					<th width="30%" data-options="field:'name'"><spring:message code="purchase.order.VendorName"/></th>

				</tr>
			</thead>
		</table>

		<div id="proVendorListToolbar" style="padding: 5px;">
			<div>
				<a id="link-vendorSel-choice" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="choiceVendor()"><spring:message code="purchase.order.Choice"/></a>
			</div>
			<div>
				<form id="form-proVendor-search" method="post">
					<spring:message code="purchase.order.vendorCode"/>：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width: 80px;" /> 
					<spring:message code="purchase.order.VendorName"/>：<input type="text" name="search_LIKE_name" class="easyui-textbox"style="width: 80px;" /> 
					<a id="link-vendorSel-search" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchVendor()"><spring:message code="purchase.order.Query"/></a>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
	var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-vendorQuota-list').datagrid('validateRow', editIndex)){
			$('#datagrid-vendorQuota-list').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	
	function onClickRow2(index,data){
		$("#indexVal").val(index);
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-vendorQuota-list').datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-vendorQuota-list').datagrid('selectRow', editIndex);
			}
		}
	}
	
</script>
</body>



