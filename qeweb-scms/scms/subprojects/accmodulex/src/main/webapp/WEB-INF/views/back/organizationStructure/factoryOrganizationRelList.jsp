<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 工厂与采购组织关系 --><spring:message code="purchase.organizationStructure.RelationshipBetweenFactoryAndPurchasingOrganization"/> </title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/factoryOrganizationRel.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-factoryOrganization-list" title='<!-- 工厂与采购组织关系列表 --><spring:message code="purchase.organizationStructure.RelationshipBetweenFactoryAndPurchasingOrganizationList"/> ' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/factoryOrganizationRel',method:'post',singleSelect:false,
		toolbar:'#factoryOrganizationListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
	<!-- 	<th width="80px" data-options="field:'manager',formatter:Group.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'factory.code'"><spring:message code="purchase.organizationStructure.FactoryCode"/> <!-- 工厂编码 --></th>
		<th width="150px" data-options="field:'factory.name'"><spring:message code="purchase.organizationStructure.FactoryName"/> <!-- 工厂名称 --></th>
		<th width="150px" data-options="field:'org.code'"><spring:message code="purchase.organizationStructure.PurchasingOrganizationCode"/> <!-- 采购组织编码 --></th>
		<th width="150px" data-options="field:'org.name'"><spring:message code="purchase.organizationStructure.PurchasingOrganizationName"/> <!-- 采购组织名称 --></th>
		<%-- <th width="150px" data-options="field:'remark'"><spring:message code="purchase.organizationStructure.Remarks"/> <!-- 备注 --></th> --%>
		<%-- <th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/><!--  生效状态 --></th> --%>
		</tr></thead>
	</table>
	<div id="factoryOrganizationListToolbar" style="padding:5px;">
		<div>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="FactoryOrg.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="FactoryOrg.abolish()">作废</a> -->
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="purchase.organizationStructure.Synchronization"/> <!-- 同步 --></a> 
		</div>
		<div>
			<form id="form-factoryOrganization-search" method="post">
			<!-- 工厂编码 --><spring:message code="purchase.organizationStructure.FactoryCode"/> ：<input type="text" name="query-LIKE_factory.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 工厂名称 --><spring:message code="purchase.organizationStructure.FactoryName"/> ：<input type="text" name="query-LIKE_factory.name" class="easyui-textbox" style="width:80px;"/>
			<!-- 采购组织编码 --><spring:message code="purchase.organizationStructure.PurchasingOrganizationCode"/> ：<input type="text" name="query-LIKE_org.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 采购组织名称 --><spring:message code="purchase.organizationStructure.PurchasingOrganizationName"/> ：<input type="text" name="query-LIKE_org.name" class="easyui-textbox" style="width:80px;"/>
			
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="FactoryOrg.query()"><spring:message code="purchase.organizationStructure.Query"/> <!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-factoryOrganization-search').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/> <!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
		<div id="win-factoryOrganization-addoredit" class="easyui-dialog" title='新增工厂和采购组织关系<spring:message code="purchase.organizationStructure.AddNewRelationshipBetweenFactoryAndPurchasingOrganization"/> ' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-factoryOrganization-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 工厂 --><spring:message code="purchase.organizationStructure.Factory"/>:</td><td><input class="easyui-combobox" id="combobox_factory" name="factoryId" type="text"
					/></td>
				</tr>
					<tr>
					<td><!-- 采购组织 --><spring:message code="purchase.organizationStructure.PurchasingOrganization"/> :</td><td><input class="easyui-combobox" id="combobox_org" name="orgId" type="text"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/> :</td><td><input class="easyui-textbox" id="remark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="FactoryOrg.submit()"><spring:message code="purchase.organizationStructure.Submit"/><!--  提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-factoryOrganization-addoredit').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/> <!-- 重置 --></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="FactoryOrg.submit()"><spring:message code="purchase.organizationStructure.Submit"/> <!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="FactoryOrg.reset()"><spring:message code="purchase.organizationStructure.Reset"/> <!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
	
	
		<script type="text/javascript">
	function sycOrder(){
		$.messager.progress();
		$.ajax({
			url:'${ctx}/manager/basedata/factoryOrganizationRel/sycOrder',
			type:'POST',
			dataType:"json",
			contentType : 'application/json',
			success:function(data){
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.show({
							title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
							msg:  data.message, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$('#datagrid-factoryOrganization-list').datagrid('reload'); 
					}else{
						$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',e,'error'); 
				} 
			}
		});
	}
	</script>
	
	
	
</body>
</html>
