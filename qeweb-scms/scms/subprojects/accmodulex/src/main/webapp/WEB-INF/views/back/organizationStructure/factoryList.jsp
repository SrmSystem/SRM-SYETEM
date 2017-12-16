<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 工厂管理 --><spring:message code="purchase.organizationStructure.FactoryManagement"/> </title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/organizationStructure/factory.js"></script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-factory-list" title='<!-- 工厂列表 --><spring:message code="purchase.organizationStructure.FactoryList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/factory',method:'post',singleSelect:false,
		toolbar:'#factoryListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<!-- <th width="80px" data-options="field:'manager',formatter:Factory.operateFmt">操作</th> -->
		<th width="150px" data-options="field:'code'"><!-- 工厂编号 --><spring:message code="purchase.organizationStructure.FactoryNumber"/> </th>
		<th width="150px" data-options="field:'name'"><!-- 工厂名称 --><spring:message code="purchase.organizationStructure.FactoryName"/> </th>
		<th width="150px" data-options="field:'remark'"><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/> </th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><!-- 生效状态 --><spring:message code="purchase.organizationStructure.EffectiveState"/> </th>
		</tr></thead>
	</table>
	<div id="factoryListToolbar" style="padding:5px;">
		<div>
<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Factory.add()">新增</a> -->
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Factory.abolish()"><!-- 作废 --><spring:message code="purchase.organizationStructure.ToVoid"/> </a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Factory.effect()"><!-- 生效 --><spring:message code="purchase.organizationStructure.TakeEffects"/> </a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><!-- 同步 --><spring:message code="purchase.organizationStructure.Synchronization"/> </a> 
		</div>
		<div>
			<form id="form-factory-search" method="post">
			<!-- 工厂编号 --><spring:message code="purchase.organizationStructure.FactoryNumber"/> ：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 工厂名称 --><spring:message code="purchase.organizationStructure.FactoryName"/> ：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<!-- 生效的状态 --><spring:message code="purchase.organizationStructure.EffectiveState"/> ：<select class="easyui-combobox"  style="width:80px;" data-options="editable:false" name="search-EQ_abolished"><option value="">-<!-- 全部 --><spring:message code="purchase.organizationStructure.Wholes"/> -</option><option value="0"><!-- 生效 --><spring:message code="purchase.organizationStructure.TakeEffects"/> </option><option value="1"><!-- 失效 --><spring:message code="purchase.organizationStructure.Invalid"/> </option></select>
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Factory.query()"><spring:message code="purchase.organizationStructure.Query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-factory-search').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	<div id="win-factory-addoredit" class="easyui-dialog" title='新增工厂<spring:message code="purchase.organizationStructure.Remarks"/> ' style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-factory-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><!-- 工厂代码 --><spring:message code="purchase.organizationStructure.FactoryCode"/> :</td><td><input class="easyui-textbox" id="code" name="code" type="text" 
						data-options="required:true" />
				    </td>
				</tr>
				<tr>
					<td><!-- 工厂名称 --><spring:message code="purchase.organizationStructure.FactoryName"/> :</td><td><input class="easyui-textbox" id="name" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><!-- 备注 --><spring:message code="purchase.organizationStructure.Remarks"/> :</td><td><input class="easyui-textbox" id="remark" name="remark" type="text"
					/></td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Factory.submit()"><spring:message code="purchase.organizationStructure.Submit"/> <!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-factory-addoredit').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/> <!-- 重置 --></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Factory.submit()"><spring:message code="purchase.organizationStructure.Submit"/> <!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="Factory.reset()"><spring:message code="purchase.organizationStructure.Reset"/> <!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
		<script type="text/javascript">
	function sycOrder(){
		$.messager.progress();
		$.ajax({
			url:'${ctx}/manager/basedata/factory/sycOrder',
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
						$('#datagrid-factory-list').datagrid('reload'); 
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
