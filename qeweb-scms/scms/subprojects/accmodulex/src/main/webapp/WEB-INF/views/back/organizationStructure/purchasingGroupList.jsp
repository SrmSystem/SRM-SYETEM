<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 采购组管理 --><spring:message code="purchase.organizationStructure.PurchasingGroupManagement"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/purchasingGruop.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-group-list" title='<!-- 采购组列表 --><spring:message code="purchase.organizationStructure.ListOfPurchasingGroups"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/purchasingGroup',method:'post',singleSelect:false,
		toolbar:'#groupListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
	<!-- 	<th width="80px" data-options="field:'manager',formatter:Group.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'code'"><!-- 采购组编码 --><spring:message code="purchase.delivery.PurchasingOrganizationCode"/></th>
		<th width="150px" data-options="field:'name'"><!-- 采购组名称 --><spring:message code="purchase.delivery.PurchasingOrganizationName"/></th>
		<th width="150px" data-options="field:'remark'"><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/><!-- 生效状态 --></th>
		</tr></thead>
	</table>
	<div id="groupListToolbar" style="padding:5px;">
		<div>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Group.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Group.abolish()">作废</a> -->
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Group.abolish()"><spring:message code="purchase.organizationStructure.ToVoid"/><!-- 作废 --></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Group.effect()"><spring:message code="purchase.organizationStructure.TakeEffects"/><!-- 生效 --></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="purchase.organizationStructure.Synchronization"/><!-- 同步 --></a> 
		</div>
		<div>
			<form id="form-group-search" method="post">
			<!-- 采购组编码 --><spring:message code="purchase.delivery.PurchasingOrganizationCode"/>：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 采购组名称 --><spring:message code="purchase.delivery.PurchasingOrganizationName"/>：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<!-- 生效的状态 --><spring:message code="purchase.organizationStructure.Remarks"/>：<select class="easyui-combobox"  style="width:80px;" data-options="editable:false" name="query-EQ_abolished"><option value="">-<spring:message code="purchase.organizationStructure.Wholes"/><!-- 全部 -->-</option><option value="0"><spring:message code="purchase.organizationStructure.TakeEffects"/><!-- 生效 --></option><option value="1"><spring:message code="purchase.organizationStructure.Invalid"/><!-- 失效 --></option></select>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Group.query()"><spring:message code="purchase.organizationStructure.Query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-group-search').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	<div id="win-group-addoredit" class="easyui-dialog" title='新增库存地点' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-group-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 采购组编码 --><spring:message code="purchase.delivery.PurchasingOrganizationCode"/>:</td><td><input class="easyui-textbox" id="code" name="code" type="text" 
						data-options="required:true" />
				    </td>
				</tr>
				<tr>
					<td><!-- 采购组名称 --><spring:message code="purchase.delivery.PurchasingOrganizationName"/>:</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 工厂 --><spring:message code="purchase.organizationStructure.Factory"/>:</td><td><input class="easyui-combobox" id="combobox_factory" name="factoryId" type="text"
					/></td>
				</tr>
					<tr>
					<td><!-- 采购组织 --><spring:message code="purchase.organizationStructure.PurchasingOrganization"/>:</td><td><input class="easyui-combobox" id="combobox_org" name="orgId" type="text"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/>:</td><td><input class="easyui-textbox" id="remark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Group.submit()"><spring:message code="purchase.organizationStructure.Submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-group-addoredit').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Group.submit()"><spring:message code="purchase.organizationStructure.Submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="Group.reset()"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
		<script type="text/javascript">
	function sycOrder(){
		$.messager.progress();
		$.ajax({
			url:'${ctx}/manager/basedata/purchasingGroup/sycOrder',
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
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data.message,'error');
					}
				}catch (e) {
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
				} 
			}
		});
	}
	</script>
</body>
</html>
