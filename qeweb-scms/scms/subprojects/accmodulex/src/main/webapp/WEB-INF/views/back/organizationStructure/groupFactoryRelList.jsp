<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 采购组与采购组织关系 --><spring:message code="purchase.organizationStructure.RelationshipBetweenPurchasingGroupAndPurchasingOrganization"/> </title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/groupFactoryRel.js"></script>
</head>

<body style="margin:0;padding:0;">

	
	<table id="datagrid-groupFactory-list" title='<!-- 采购组与采购组织关系列表 --><spring:message code="purchase.organizationStructure.RelationshipBetweenPurchasingGroupAndPurchasingOrganizationList"/> ' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/groupFactoryRel',method:'post',singleSelect:false,
		toolbar:'#groupFactoryListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<!-- <th width="30px" data-options="field:'id',checkbox:true"></th> -->
	<!-- 	<th width="80px" data-options="field:'manager',formatter:Group.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'group.code'"><spring:message code="purchase.delivery.PurchasingOrganizationCode"/> <!-- 采购组编码 --></th>
		<th width="150px" data-options="field:'group.name'"><spring:message code="purchase.delivery.PurchasingOrganizationName"/> <!-- 采购组名称 --></th>
		<th width="150px" data-options="field:'factory.code'"><spring:message code="purchase.organizationStructure.FactoryCode"/> <!-- 工厂编码 --></th>
		<th width="150px" data-options="field:'factory.name'"><spring:message code="purchase.organizationStructure.FactoryName"/> <!-- 工厂名称 --></th>
		<th width="150px" data-options="field:'remark'"><spring:message code="purchase.organizationStructure.Remarks"/> <!-- 备注 --></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/> <!-- 生效状态 --></th>
		</tr></thead>
	</table>
	<div id="groupFactoryListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="GroupFactory.abolish()"><spring:message code="purchase.organizationStructure.ToVoid"/><!-- 作废 --></a>
		</div>
		<div>
			<form id="form-groupFactory-search" method="post">
			<!-- 采购组编码 --><spring:message code="purchase.delivery.PurchasingOrganizationCode"/> ：<input type="text" name="query-LIKE_group.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 采购组名称 --><spring:message code="purchase.delivery.PurchasingOrganizationName"/> ：<input type="text" name="query-LIKE_group.name" class="easyui-textbox" style="width:80px;"/>
			<!-- 工厂编码 --><spring:message code="purchase.organizationStructure.FactoryCode"/> ：<input type="text" name="query-LIKE_factory.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 工厂名称 --><spring:message code="purchase.organizationStructure.FactoryName"/> ：<input type="text" name="query-LIKE_factory.name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="GroupFactory.query()"><spring:message code="purchase.organizationStructure.Query"/> <!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-groupFactory-search').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/> <!-- 重置 --></a>
			</form>
		</div>
	</div>
</body>
</html>
