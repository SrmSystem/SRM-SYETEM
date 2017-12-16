<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><!-- 采购订单类型管理 --><spring:message code="purchase.organizationStructure.PurchaseOrderTypeManagement"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-orderType-list" title='<!-- 采购订单类型列表 --><spring:message code="purchase.organizationStructure.PurchaseOrderTypeList"/>' class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/basedata/orderType',method:'post',singleSelect:false,
		toolbar:'#orderTypeListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="150px" data-options="field:'code'"><spring:message code="purchase.organizationStructure.PurchaseOrderTypeNumber"/><!-- 采购订单类型编号 --></th>
		<th width="150px" data-options="field:'name'"><spring:message code="purchase.organizationStructure.PurchaseOrderTypeName"/><!-- 采购订单类型名称 --></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.organizationStructure.EffectiveState"/><!-- 生效状态 --></th>
		</tr></thead>
	</table>
	<div id="orderTypeListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="purchase.organizationStructure.Synchronization"/><!-- 同步 --></a> 
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="abolished()"><spring:message code="purchase.organizationStructure.Abolish"/><!-- 废除 --></a> 
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="effect()"><spring:message code="purchase.organizationStructure.TakeEffects"/><!-- 生效 --></a> 
		</div>
		<div>
			<form id="form-orderType-search" method="post">
			<!-- 采购订单类型编号 --><spring:message code="purchase.organizationStructure.Query"/>：<input type="text" name="query-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<!-- 采购订单类型名称 --><spring:message code="purchase.organizationStructure.PurchaseOrderTypeName"/>：<input type="text" name="query-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<!-- 生效的状态 --><spring:message code="purchase.organizationStructure.EffectiveState"/>：<select class="easyui-combobox"  style="width:80px;" data-options="editable:false" name="query-EQ_abolished"><option value="">-<spring:message code="purchase.organizationStructure.Wholes"/><!-- 全部 -->-</option><option value="0"><spring:message code="purchase.organizationStructure.TakeEffects"/><!-- 生效 --></option><option value="1"><spring:message code="purchase.organizationStructure.Invalid"/><!-- 失效 --></option></select>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchx()"><spring:message code="purchase.organizationStructure.Query"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-orderType-search').form('reset')"><spring:message code="purchase.organizationStructure.Reset"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>

	<script type="text/javascript">
	
	 function searchx(){
		var searchParamArray = $('#form-orderType-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-orderType-list').datagrid('load',searchParams);
	}
	 
	 function effect(){
		 var selections = $('#datagrid-orderType-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 --> ','<spring:message code="purchase.order.NoRecordWasSelected"/>'/* 没有选择任何记录！*/,'info');
				return false;
			}
			var params = $.toJSON(selections);
			/* 确定要生效该记录吗？ */
			$.messager.confirm('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.organizationStructure.AreYouSureItsGoingToTakeEffect"/><br/><font style="color: #F00;font-weight: 900;"></font>',function(r){
				if(r){
					for(var i=0;i<selections.length;i++){
						if(selections[i]["abolished"]=='0'){
							$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.organizationStructure.RecordOfExistenceItCannotBeRepeated"/>','info');/* 存在生效的记录！无法重复生效 ！ */
							return false;
						}
					}
					$.ajax({
						url:ctx+'/manager/basedata/orderType/effect',
						type:'POST',
						data:params,
						contentType : 'application/json',
						dataType:"json",
						success:function(data1){
							$.messager.progress('close');
							try{
								var result = data1;
								if(result.success){
									$.messager.show({
										title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
										msg:'<spring:message code="purchase.organizationStructure.SuccessfulEntryRecord"/>'/* 生效记录成功 */,
										timeout:2000,
										showType:'show',
										style:{
											right:'',
											top:document.body.scrollTop+document.documentElement.scrollTop,
											bottom:''
										}
									});
									$('#datagrid-orderType-list').datagrid('reload');
								}else{
									$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.msg,'error');
								}
								}catch (e) {
									$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data,'error');
								}	
							
							
						}
					});
				}
				
				
			});
			
			
			
	 }
	 

	 function abolished(){
		 var selections = $('#datagrid-orderType-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.order.NoRecordWasSelected"/>','info');/* 没有选择任何记录！ */
				return false;
			}
			var params = $.toJSON(selections);
			
			$.messager.confirm('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.organizationStructure.AreYouSureYouWantToCancelTheRecord"/><br/><font style="color: #F00;font-weight: 900;"></font>'/* 确定要废除该记录吗？ */,function(r){
				if(r){
					for(var i=0;i<selections.length;i++){
						if(selections[i]["abolished"]=='1'){
							$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->','<spring:message code="purchase.organizationStructure.ThereIsanInvalidRecord"/>'/* 存在已作废的记录！ */,'info');
							return false;
						}
					}
					$.ajax({
						url:ctx+'/manager/basedata/orderType/abolishBatch',
						type:'POST',
						data:params,
						contentType : 'application/json',
						dataType:"json",
						success:function(data1){
							$.messager.progress('close');
							try{
								var result = data1;
								if(result.success){
									$.messager.show({
										title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
										msg:'<spring:message code="purchase.organizationStructure.AbolitionOfRecordSuccess"/>'/* 废除记录成功 */,
										timeout:2000,
										showType:'show',
										style:{
											right:'',
											top:document.body.scrollTop+document.documentElement.scrollTop,
											bottom:''
										}
									});
									$('#datagrid-orderType-list').datagrid('reload');
								}else{
									$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.msg,'error');
								}
								}catch (e) {
									$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data,'error');
								}	
							
							
						}
					});
				}
				
				
			});
			
			
			
		}
	 
	function sycOrder(){
		$.messager.progress();
		$.ajax({
			url:'${ctx}/manager/basedata/orderType/sycOrder',
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
						$('#datagrid-orderType-list').datagrid('reload'); 
					}else{
						$.messager.alert('<spring:message code="purchase.order.Prompt"/> '/* 提示 */,data.message,'error');
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
