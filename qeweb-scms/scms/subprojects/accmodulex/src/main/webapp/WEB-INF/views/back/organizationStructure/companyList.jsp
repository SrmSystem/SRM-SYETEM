<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 公司管理 --><spring:message code="purchase.organizationStructure.CompanyManagement"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/company.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-company-list" title='<!-- 公司列表 --><spring:message code="purchase.organizationStructure.CompanyList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/company',method:'post',singleSelect:false,
		toolbar:'#companyListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
	<!-- 	<th width="80px" data-options="field:'manager',formatter:Company.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'code'"><spring:message code="purchase.organizationStructure.CompanyNumbers"/><!-- 公司编号 --></th>
		<th width="150px" data-options="field:'name'"><spring:message code="purchase.organizationStructure.CompanyName"/><!-- 公司名称 --></th>
		<th width="150px" data-options="field:'remark'"><spring:message code="purchase.organizationStructure.Remarks"/><!-- 备注 --></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/><!-- 生效状态 --></th>
		</tr></thead>
	</table>
	<div id="companyListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="purchase.organizationStructure.Synchronization"/><!-- 同步 --></a> 
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Company.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Company.abolish()">作废</a> -->
		</div>
		<div>
			<form id="form-company-search" method="post">
			<!-- 公司编号 --><spring:message code="purchase.organizationStructure.CompanyNumbers"/>：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 公司名称 --><spring:message code="purchase.organizationStructure.CompanyName"/>：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<!-- 生效的状态 --><spring:message code="purchase.organizationStructure.EffectiveState"/>：<select class="easyui-combobox"  style="width:80px;" data-options="editable:false" name="query-EQ_abolished"><option value="">-<spring:message code="purchase.organizationStructure.Wholes"/><!-- 全部 -->-</option><option value="0"><spring:message code="purchase.organizationStructure.TakeEffects"/><!-- 生效 --></option><option value="1"><spring:message code="purchase.organizationStructure.Invalid"/><!-- 失效 --></option></select>
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Company.query()"><spring:message code="purchase.order.Query"/> <!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-company-search').form('reset')"><spring:message code="purchase.order.Reset"/> <!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	<div id="win-company-addoredit" class="easyui-dialog" title='<!-- 新增公司 --><spring:message code="purchase.organizationStructure.AddNewCompany"/>' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-company-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 公司代码 --><spring:message code="purchase.delivery.CompanyCode"/>:</td><td><input class="easyui-textbox" id="code" name="code" type="text" 
						data-options="required:true" />
				    </td>
				</tr>
				<tr>
					<td><!-- 公司名称 --><spring:message code="purchase.organizationStructure.CompanyName"/>:</td><td><input class="easyui-textbox" id="gongsiname" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/>:</td><td><input class="easyui-textbox" id="gongsiremark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Company.submit()"><spring:message code="purchase.organizationStructure.Submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-company-addoredit').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Company.submit()"><spring:message code="purchase.organizationStructure.Submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="Company.reset()"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
	<script type="text/javascript">
	function sycOrder(){
		$.messager.progress();
		$.ajax({
			url:'${ctx}/manager/basedata/company/sycOrder',
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
						$('#datagrid-company-list').datagrid('reload'); 
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
