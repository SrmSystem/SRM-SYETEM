<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 公司与工厂关系 --><spring:message code="purchase.organizationStructure.RelationshipBetweenCompanyAndFactory"/> </title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/companyFactoryRel.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-companyFactoryRel-list" title='<!-- 公司和工厂关系列表 --><spring:message code="purchase.organizationStructure.RelationshipBetweenCompanyAndFactoryList"/> ' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/companyFactoryRel',method:'post',singleSelect:false,
		toolbar:'#companyFactoryRelListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
	<!-- 	<th width="80px" data-options="field:'manager',formatter:Group.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'company.code'"><spring:message code="purchase.organizationStructure.CompanyCode"/><!--  公司编码 --></th>
		<th width="150px" data-options="field:'company.name'"><spring:message code="purchase.organizationStructure.CompanyName"/> <!-- 公司名称 --></th>
		<th width="150px" data-options="field:'factory.code'"><spring:message code="purchase.organizationStructure.FactoryCode"/> <!-- 工厂编码 --></th>
		<th width="150px" data-options="field:'factory.name'"><spring:message code="purchase.organizationStructure.FactoryName"/> <!-- 工厂名称 --></th>
		<%-- <th width="150px" data-options="field:'remark'"><spring:message code="purchase.organizationStructure.Remarks"/> <!-- 备注 --></th> --%>
		<%-- <th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/> <!-- 生效状态 --></th> --%>
		</tr></thead>
	</table>
	<div id="companyFactoryRelListToolbar" style="padding:5px;">
		<div>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="purchase.organizationStructure.Synchronization"/> <!-- 同步 --></a> 
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="CompanyFactory.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="CompanyFactory.abolish()">作废</a> -->
		</div>
		<div>
			<form id="form-companyFactoryRel-search" method="post">
			<!-- 公司编码 --><spring:message code="purchase.organizationStructure.CompanyCode"/> ：<input type="text" name="query-LIKE_company.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 公司名称 --><spring:message code="purchase.organizationStructure.CompanyName"/> ：<input type="text" name="query-LIKE_company.name" class="easyui-textbox" style="width:80px;"/>
			<!-- 工厂编码 --><spring:message code="purchase.organizationStructure.FactoryCode"/> ：<input type="text" name="query-LIKE_factory.code" class="easyui-textbox" style="width:80px;"/>
			<!-- 工厂名称 --><spring:message code="purchase.organizationStructure.FactoryName"/> ：<input type="text" name="query-LIKE_factory.name" class="easyui-textbox" style="width:80px;"/>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="CompanyFactory.query()"><spring:message code="purchase.order.Query"/> <!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-companyFactoryRel-search').form('reset')"><spring:message code="purchase.order.Reset"/> <!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
		<div id="win-companyFactoryRel-addoredit" class="easyui-dialog" title='<!-- 新增公司与工厂关系 --><spring:message code="purchase.organizationStructure.NewAddRelationshipBetweenCompanyAndFactory"/> ' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-companyFactoryRel-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 公司 --><spring:message code="purchase.organizationStructure.Company"/> :</td><td><input class="easyui-combobox" id="combobox_company" name="companyId" type="text"
					/></td>
				</tr>
					<tr>
					<td><!-- 工厂 --><spring:message code="purchase.organizationStructure.Factory"/> :</td><td><input class="easyui-combobox" id="combobox_factory" name="factoryId" type="text"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/> :</td><td><input class="easyui-textbox" id="remark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="CompanyFactory.submit()"><spring:message code="purchase.organizationStructure.Submit"/> <!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-companyFactoryRel-addoredit').form('reset')"><spring:message code="purchase.organizationStructure.enquiries"/> <!-- 重置 --></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="CompanyFactory.submit()"><spring:message code="purchase.organizationStructure.Submit"/> <!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="CompanyFactory.reset()"><spring:message code="purchase.order.Reset"/><!--  重置 --></a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	function sycOrder(){
		$.messager.progress();
		$.ajax({
			url:'${ctx}/manager/basedata/companyFactoryRel/sycOrder',
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
						$('#datagrid-material-list').datagrid('reload'); 
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
