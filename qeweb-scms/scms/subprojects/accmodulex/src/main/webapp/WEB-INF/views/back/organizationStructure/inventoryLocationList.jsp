<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 库存地点管理 --><spring:message code="purchase.organizationStructure.InventoryLocationManager"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/inventoryLocation.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-inventory-list" title='<!-- 库存地点列表 --><spring:message code="purchase.organizationStructure.InventoryLocationList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/inventoryLocation',method:'post',singleSelect:false,
		toolbar:'#inventoryListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
	<!-- 	<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="80px" data-options="field:'manager',formatter:Inventory.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'code'"><spring:message code="purchase.organizationStructure.InventoryLocationCode"/><!-- 库存地点编码 --></th>
		<th width="150px" data-options="field:'name'"><spring:message code="purchase.organizationStructure.InventoryLocationName"/><!-- 库存地点名称 --></th>
			<th width="150px" data-options="field:'address'"><spring:message code="purchase.organizationStructure.DetailedAaddressOfInventoryLocation"/><!-- 库存地点详细地址 --></th>
		<th width="150px" data-options="field:'remark'"><spring:message code="purchase.organizationStructure.Remarks"/><!-- 备注 --></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/><!-- 生效状态 --></th>
		</tr></thead>
	</table>
	<div id="inventoryListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Inventory.add()"><spring:message code="vendor.new"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Inventory.abolish()"><spring:message code="purchase.organizationStructure.ToVoid"/><!-- 作废 --></a>
		</div>
		<div>
			<form id="form-inventory-search" method="post">
			<!-- 库存地点编码 --><spring:message code="purchase.organizationStructure.InventoryLocationCode"/>：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 库存地点名称 --><spring:message code="purchase.organizationStructure.InventoryLocationName"/>：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Inventory.query()"><spring:message code="purchase.organizationStructure.Query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-inventory-search').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	<div id="win-inventory-addoredit" class="easyui-dialog" title='<!-- 新增库存地点 --><spring:message code="purchase.organizationStructure.AddNewInventoryLocation"/>' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-inventory-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 库存地点编码 --><spring:message code="purchase.organizationStructure.InventoryLocationCode"/>:</td><td><input class="easyui-textbox" id="code" name="code" type="text" 
						data-options="required:true" />
				    </td>
				</tr>
				<tr>
					<td><!-- 库存地点名称 --><spring:message code="purchase.organizationStructure.InventoryLocationName"/>:</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 库存地点详细地址 --><spring:message code="purchase.organizationStructure.DetailedAaddressOfInventoryLocation"/>:</td><td><input class="easyui-textbox" id="address" name="address" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/>:</td><td><input class="easyui-textbox" id="remark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Inventory.submit()"><spring:message code="purchase.organizationStructure.Submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-inventory-addoredit').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Inventory.submit()"><spring:message code="purchase.organizationStructure.Submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="Inventory.reset()"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
