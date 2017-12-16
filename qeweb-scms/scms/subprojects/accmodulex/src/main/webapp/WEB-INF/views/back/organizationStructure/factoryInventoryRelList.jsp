<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 工厂与库存地点关系 --><spring:message code="purchase.organizationStructure.RelationshipBetweenPlantAndInventoryLocation"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/factoryInventoryRel.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-factoryInventoryRel-list" title='<!-- 工厂与库存地点关系列表 --><spring:message code="purchase.organizationStructure.RelationshipBetweenPlantAndInventoryLocationList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/factoryInventoryRel',method:'post',singleSelect:false,
		toolbar:'#factoryInventoryRelListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<!-- <th width="30px" data-options="field:'id',checkbox:true"></th> -->
	<!-- 	<th width="80px" data-options="field:'manager',formatter:Group.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'factory.code'"><spring:message code="purchase.organizationStructure.FactoryCode"/><!-- 工厂编码 --></th>
		<th width="150px" data-options="field:'factory.name'"><spring:message code="purchase.organizationStructure.FactoryName"/><!-- 工厂名称 --></th>
		<th width="150px" data-options="field:'inventory.code'"><spring:message code="purchase.organizationStructure.InventoryLocationCode"/><!-- 库存地点编码 --></th>
		<th width="150px" data-options="field:'inventory.name'"><spring:message code="purchase.organizationStructure.InventoryLocationName"/><!-- 库存地点名称 --></th>
		<th width="150px" data-options="field:'remark'"><spring:message code="purchase.organizationStructure.Remarks"/><!-- 备注 --></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/><!-- 生效状态 --></th>
		</tr></thead>
	</table>
	<div id="factoryInventoryRelListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="FactoryInventory.add()"><spring:message code="purchase.order.NewlyAdded"/><!-- 新增 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="FactoryInventory.abolish()"><spring:message code="purchase.organizationStructure.ToVoid"/><!-- 作废 --></a>
		</div>
		<div>
			<form id="form-factoryInventoryRel-search" method="post">
			<!-- 工厂编码 --><spring:message code="purchase.organizationStructure.FactoryCode"/>：<input type="text" name="query-LIKE_factory.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 工厂名称 --><spring:message code="purchase.organizationStructure.FactoryName"/>：<input type="text" name="query-LIKE_factory.name" class="easyui-textbox" style="width:80px;"/>
			<!-- 库存地点编码 --><spring:message code="purchase.organizationStructure.InventoryLocationCode"/>：<input type="text" name="query-LIKE_inventory.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 库存地点名称 --><spring:message code="purchase.organizationStructure.InventoryLocationName"/>：<input type="text" name="query-LIKE_inventory.name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="FactoryInventory.query()"><spring:message code="purchase.delivery.Query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-factoryInventoryRel-search').form('reset')"><spring:message code="purchase.organizationStructure.purchase.delivery.Reset"/><!-- 重置 --></a>
			</form>
		</div>
	</div>
	
		<div id="win-factoryInventoryRel-addoredit" class="easyui-dialog" title="新增工厂和库存地点关系" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-factoryInventoryRel-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 工厂 --><spring:message code="purchase.organizationStructure.Factory"/>:</td><td><input class="easyui-combobox" id="combobox_factory" name="factoryId" type="text"
					/></td>
				</tr>
					<tr>
					<td><!-- 库存地点 --><spring:message code="purchase.organizationStructure.InventoryLocation"/>:</td><td><input class="easyui-combobox" id="combobox_inventory" name=inventoryId type="text"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/>:</td><td><input class="easyui-textbox" id="remark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="FactoryInventory.submit()"><spring:message code="purchase.organizationStructure.Remarks"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-factoryInventoryRel-addoredit').form('reset')"><spring:message code="purchase.delivery.Reset"/><!-- 重置 --></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="FactoryInventory.submit()"><spring:message code="purchase.organizationStructure.Remarks"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="FactoryInventory.reset()"><spring:message code="purchase.delivery.Reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
	
	
	
	
	
	
</body>
</html>
