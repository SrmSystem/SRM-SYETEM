<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 采购组织管理 --><spring:message code="purchase.organizationStructure.PurchasingOrganizationManagement"/> </title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-buyerOrg-list" title='<!-- 采购组织列表 --><spring:message code="purchase.organizationStructure.PurchasingOrganizationList"/> ' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/buyerOrg',method:'post',singleSelect:false,
		toolbar:'#buyerOrgListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="150px" data-options="field:'code'"><spring:message code="purchase.organizationStructure.PurchasingOrganizationNumber"/> <!-- 采购组织编号 --></th>
		<th width="150px" data-options="field:'name'"><spring:message code="purchase.organizationStructure.PurchasingOrganizationName"/> <!-- 采购组织名称 --></th>
		<%-- <th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/> <!-- 生效状态 --></th> --%>
		</tr></thead>
	</table>
	<div id="buyerOrgListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="purchase.organizationStructure.Synchronization"/> <!-- 同步 --></a> 
		</div>
		<div>
			<form id="form-buyerOrg-search" method="post">
			<!-- 采购组织编号 --><spring:message code="purchase.organizationStructure.PurchasingOrganizationNumber"/> ：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 采购组织名称 --><spring:message code="purchase.organizationStructure.PurchasingOrganizationName"/> ：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<%-- <!-- 生效的状态 --><spring:message code="purchase.organizationStructure.EffectiveState"/> ：<select class="easyui-combobox"  style="width:80px;" data-options="editable:false" name="query-EQ_abolished"><option value="">-<!-- 全部 --><spring:message code="purchase.order.Whole"/> -</option><option value="0"><!-- 生效 --><spring:message code="purchase.organizationStructure.TakeEffects"/> </option><option value="1"><!-- 失效 --><spring:message code="vendor.lapse"/> </option></select> --%>
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchx()"><spring:message code="vendor.enquiries"/> <!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-buyerOrg-search').form('reset')"><spring:message code="vendor.resetting"/> <!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
	
	 function searchx(){
		var searchParamArray = $('#form-buyerOrg-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-buyerOrg-list').datagrid('load',searchParams);
	}
	
	function sycOrder(){
		$.messager.progress();
		$.ajax({
			url:'${ctx}/manager/basedata/buyerOrg/sycOrder',
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
						$('#datagrid-buyerOrg-list').datagrid('reload'); 
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
