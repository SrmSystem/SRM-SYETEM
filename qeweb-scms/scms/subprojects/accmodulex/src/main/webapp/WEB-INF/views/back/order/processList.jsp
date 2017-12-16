<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
<title></title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	var ctx = '${pageContext.request.contextPath}';
</script>

<script type="text/javascript"
	src="${ctx}/static/script/contract/dialogx.js"></script>
	
	<script type="text/javascript">
		function operFmt(v,r,i){
			return '&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="buyerMaterialRelList('+r.id+')"><spring:message code="purchase.order.Materiel"/>/* 物料 */</a>';
		   return '';
		}
		</script>
</head>

<body>
	<table id="datagrid_materialGroup_list" class="easyui-datagrid" data-options=" fit:true,
    url:'${ctx}/manager/order/processData/getProcessList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#tt'
  ">
	<thead>
		<tr>
			<th width="50px" data-options="field:'id',checkbox:true"></th>
		    <th width="80px" data-options="field:'oper',formatter:operFmt"><spring:message code="purchase.order.operation"/>
		    </th>
		    <th width="140px" data-options="field:'code'"><spring:message code="purchase.order.Code"/>
		    </th>
			<th width="140px" data-options="field:'name'"><spring:message code="purchase.order.WorkingProcedureName"/>
			</th>
		</tr>
	</thead>
	</table>
	<div id="tt">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="openWin()" ><spring:message code="purchase.order.NewlyAdded"/>
			</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteOpt()"><spring:message code="purchase.order.Delete"/>
			</a>
		</div>
		<div>
			<form id="purchase_matgroup_form" method="post">
			          <spring:message code="purchase.order.Code"/>
			          :<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width: 100px;" />
				<spring:message code="purchase.order.Name"/>
				:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width: 100px;" />
				
			
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchx()"><spring:message code="purchase.order.Query"/>
				</a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#purchase_matgroup_form').form('reset')"><spring:message code="purchase.order.Reset"/>
				</a>
			</form>
		</div>
	</div>
	

	
	<!-- 新增采购 -->
	<div id="win-add-mat" class="easyui-window" title="" style="width: 300px; height: 250px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div align="center">
		<form id="form-mat-add" method="post">
		<table style="margin-top: 10px">
				<tr><td>
			<spring:message code="purchase.order.Code"/>
			：<input type="text" class="easyui-textbox" id="code" name="code"/>
			</td></tr> 
			
			<tr><td>
			<spring:message code="purchase.order.Name"/>
			：<input type="text" class="easyui-textbox" id="name" name="name"/>
			</td></tr> 
		</table>
		<div style="margin-top: 20px">
			<a id="link-mat-search" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="submitOpt()"><spring:message code="purchase.order.Submit"/>
			</a>
		</div>
		</form>
		</div>
	</div>
	
	
	<script type="text/javascript">
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	
	function openWin(title, width, height, url,dd) {

		$(dd).dialog({    
		    title: title,    
		    closed: false,    
		    cache: false, 
		    iconCls:'icon-save',
		    href: url,    
		    modal: true,
		    onClose : function() {
	            //$(this).dialog('destroy');
	          
	        }
		});
	}
	
	 function searchx(){
			var searchParamArray = $('#purchase_matgroup_form').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid_materialGroup_list').datagrid('load',searchParams);
		}
	 function buyerMaterialRelList(id) {
			var url=ctx+'/manager/order/processData/getProcessMaterialRelList/'+id;
			var title = '<spring:message code="purchase.order.Materiel"/>'/* 物料 */;
			 new dialogx().showWin(title, clientWidth, clientHeight,url,
						'dialog-material-process'); 
		}
	 
	 function openWin() {
			$('#win-add-mat').dialog({
				iconCls:'icon-add',
				title:'<spring:message code="purchase.order.NewlyAdded"/>'/* '新增' */
			});
			$('#form-mat-add').form('clear');
			$('#win-add-mat').dialog('open');
		}
		function submitOpt (){
			var url=ctx+"/manager/order/processData/saveSubmit";
			$.messager.progress({
				title:'<spring:message code="purchase.order.Prompt"/>'/* 提示 */,
				msg : '<spring:message code="purchase.order.Submission"/>...'/* '提交中' */
			});
			$('#form-mat-add').form('submit',{
				ajax:true,
				url:url,
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){
					$.messager.progress('close');
					try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,result.msg,'info');
						$('#win-add-mat').dialog('close');
						$('#datagrid_materialGroup_list').datagrid('reload');
					}else{
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,result.msg,'error');
					}
					}catch (e) {
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data,'error');
					}
				}
				
			});
		}
		function deleteOpt(){
			var datagridId = "#datagrid_materialGroup_list";
			var selections = $(datagridId).datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* '没有选择任何记录！' */,'info');
				return false;
			}
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/order/processData/deleteOpt',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					
						$.messager.show({
							title:'<spring:message code="purchase.order.news"/>'/* '消息' */,
							msg:'<spring:message code="purchase.order.DeleteSuccessfully"/>'/* '删除成功' */,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						$(datagridId).datagrid('reload');
					
				}
			});
		}
		
		function openMaterialWin () {
			$('#win-material-detail').dialog(); 
			$('#win-material-detail').dialog('open'); 
			$('#datagrid-material-list').datagrid({   
		    	url: ctx + '/manager/order/processData/getMaterialList'
			});
		}
		//选择
		function choiceMaterial(buyerId) {
			var rows = $('#datagrid-buyerMaterialRel-list').datagrid('getRows');
			
			var selections = $('#datagrid-material-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* '没有选择任何记录！' */,'info');
				return false;
			}
			for(var i=0;i<rows.length;i++){
				for(var j=0;j<selections.length;j++){
					if(rows[i].material.code == selections[j].code &&rows[i].material.name ==selections[j].name){
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.ContainsSelectedDataPleaseReselect"/>'/* '包含已选择的数据，请重新选择！' */,'info');
						return false;
					}
				}
			}
			

			$.messager.progress();
		 	var params = $.toJSON(selections);
         	$.ajax({
         	url:ctx+'/manager/order/processData/selMaterial/'+$('#processId').val(),
         		type:'POST',
         		data:params,
         		dataType:"json",
         		contentType : 'application/json',
         		success:function(data){
         			$.messager.progress('close');
         			try{
         				if(data.success){ 
         					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.SuccessfulSelection"/>'/* '选中成功' */,'info');
         					$('#win-material-detail').window('close');	
         					$('#datagrid-buyerMaterialRel-list').datagrid('reload'); 
         				}else{
         					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,data.message,'error');
         				}
         			}catch (e) {
         				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,e,'error'); 
         			} 
         		}
         	});
			
		}
		function delMaterialRel(){
			var selections = $('#datagrid-buyerMaterialRel-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,'<spring:message code="purchase.order.NoRecordWasSelected"/>'/* '没有选择任何记录！' */,'info');
				return false;
			}
			var params = $.toJSON(selections);
			$.ajax({
				url:ctx+'/manager/order/processData/delProcessMaterialRelList',
				type:'POST',
				data:params,
				contentType : 'application/json',
				success:function(data){
					
						$.messager.show({
							title:'<spring:message code="purchase.order.news"/>'/* '消息' */,
							msg:'<spring:message code="purchase.order.DeleteSuccessfully"/>'/* '删除成功' */,
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});

						$('#datagrid-buyerMaterialRel-list').datagrid('reload');
					
				}
			});
		}
	</script>
</body>
</html>
