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
	src="${ctx}/static/script/contract/dialog.js"></script>
	
	<script type="text/javascript">
		function operFmt(v,r,i){
		return '&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="buyerMaterialRelList('+r.id+')"><spring:message code="purchase.order.Supplier"/></a>';
		
		}
		</script>
</head>

<body>
	<table id="datagrid_materialGroup_list" class="easyui-datagrid" data-options=" fit:true,
    url:'${ctx}/manager/order/materialPlan/getMaterialPlanList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#tt'
  ">
	<thead>
		<tr>
			<th width="50px" data-options="field:'id',checkbox:true"></th>
		    <th width="80px" data-options="field:'oper',formatter:operFmt"><spring:message code="purchase.order.operation"/></th>
		 	<th width="120px" data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code}"><spring:message code="purchase.order.MaterialCoding"/>
		 	</th>
			<th width="120px" data-options="field:'materialName',formatter:function(v,r,i){return r.material.name}"><spring:message code="purchase.order.MaterialName"/>
			</th>
			<th width="120px" data-options="field:'planTime'"><spring:message code="purchase.order.DemandDate"/>
			</th>
			<th width="120px" data-options="field:'planNum'"><spring:message code="purchase.order.DemamdNumber"/>
			</th>
			<th width="120px" data-options="field:'publishStatus',formatter:function(v,r,i){return getPublishStatus(v);}"><spring:message code="purchase.order.PublishingState"/>
			</th>
			<th width="120px" data-options="field:'publishTime'"><spring:message code="purchase.order.ReleaseDate"/>
			</th>
			<th width="120px" data-options="field:'publishUserName',formatter:function(v,r,i){if(r.publishUser==null) return '';else return r.publishUser.name}"><spring:message code="purchase.order.Publisher"/>
			</th>
		</tr>
	</thead>
	</table>
	<div id="tt">
		<div>
			
		</div>
		<div>
			<form id="purchase_matgroup_form" method="post">
			         <spring:message code="purchase.order.MaterialCoding"/>
			         :<input class="easyui-textbox" name="search-LIKE_material.code" type="text" style="width: 140px;" />
				<spring:message code="purchase.order.MaterialName"/>
				:<input class="easyui-textbox" name="search-LIKE_material.name" type="text" style="width: 140px;" />
			         <spring:message code="purchase.order.DemandDate"/>
			         :<input type="text" name="search-GTE_planTime" class="easyui-datebox" data-options="showSeconds:false,formatter:formatDate" style="width:130px;"/>
     -<input type="text" name="search-LTE_planTime" class="easyui-datebox" data-options="showSeconds:false,formatter:formatDate" style="width:130px;"/>
				<spring:message code="purchase.order.PublishingState"/>
				:<select class="easyui-combobox" name="search-EQ_publishStatus" style="width:100px;">
      	 			<option value="">-<spring:message code="purchase.order.Whole"/>
      	 			-</option>
      	 			<option value="0"><spring:message code="purchase.order.Unpublished"/>
      	 			</option>
      	 			<option value="1"><spring:message code="purchase.order.AlreadyPublished"/>
      	 			</option>
      	 		</select>	
			
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchx()"><spring:message code="purchase.order.Query"/>
				</a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#purchase_matgroup_form').form('reset')"><spring:message code="purchase.order.Reset"/>
				</a>
			</form>
		</div>
	</div>
	

	
	
	
	
	<script type="text/javascript">
	var clientWidth = document.body.clientWidth;
	var clientHeight = document.body.clientHeight;
	
	function formatDate(date){
	    var month = date.getMonth()+1;
	    if( "" != date ){
	        if( date.getMonth() +1 < 10 ){
	            month = '0' + (date.getMonth() +1);
	        }
	        var day = date.getDate();
	        if( date.getDate() < 10 ){
	            day = '0' + date.getDate();
	        }
	       //返回格式化后的时间
	        return date.getFullYear()+'-'+month+'-'+day;
	    }else{
	        return "";
	    }
	}
	
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
	
	function getPublishStatus(status) {
		if(status == 0)
			return '<spring:message code="purchase.order.Unpublished"/>';
		else if(status == 1)
			return '<spring:message code="purchase.order.AlreadyPublished"/>';
		else 
			return '';
	}
	
	
	 function searchx(){
			var searchParamArray = $('#purchase_matgroup_form').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid_materialGroup_list').datagrid('load',searchParams);
		}
	 
	 function buyerMaterialRelList(id) {
			var url=ctx+'/manager/order/materialPlan/displayVendorList/'+id;
			var title = $.i18n.prop('purchase.order.Supplier');
			 new dialog().showWin(title, clientWidth, clientHeight,url,
						'dialog-material-vendor'); 
	 }
	 
	 function searchVendor(){
			var searchParamArray = $('#form-proVendor-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#datagrid-proVendor-list').datagrid('load',searchParams);
	 }
	  function choiceVendor(){
			var selections = $('#datagrid-proVendor-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>','<spring:message code="purchase.order.NoRecordWasSelected"/>','info');
				return false;
			}
			$('#win-proVendor-detail').dialog('close');	
			var $dgSelected = $('#datagrid-vendorQuota-list');
			var datas=$dgSelected.datagrid('getData').rows;
			for(var i=0;i<selections.length;i++){
				var bool=true;
				for(var j=0;j<datas.length;j++){
					if(selections[i].id==datas[j].vendorId){
						bool=false;
						break;
					}
				}
				if(bool){
					var m = {};
				 	m.vendorId = selections[i].id;
				 	m.vendorCode=selections[i].code;
					m.vendorName=selections[i].name;
					$dgSelected.datagrid('appendRow',m);
				}
			}
		}
	  
		function openVendorWin(){
		
			$.parser.parse($('#win-proVendor-detail'));
			$('#win-proVendor-detail').dialog('open'); 
			$('#datagrid-proVendor-list').datagrid({   
		    	url: ctx + '/manager/order/materialPlan/getOrgList'
			});
		}
		
		function deleteVendor(datagridId){
			var selectedArray = $(datagridId).datagrid('getSelections');
			if(selectedArray.length<=0){
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>','<spring:message code="purchase.order.selectRecord"/>','warning');
			}
			for(var i=0;i<selectedArray.length;i++){
					var index = $(datagridId).datagrid('getRowIndex',selectedArray[i]);
					$(datagridId).datagrid('deleteRow',index);
			}
		}
		
		function publishMaterialPlan(type){
			$.messager.progress();
			$('#datagrid-vendorQuota-list').datagrid('acceptChanges'); 
				  
			var rows = $('#datagrid-vendorQuota-list').datagrid('getRows');
			  
			if(rows == null || rows.length == 0) {
						$.messager.progress('close');
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>','<spring:message code="purchase.order.PleaseAddSupplier"/>','error');
						return false;
			} 
			var tt=0;
			for(var i=0;i<rows.length;i++){
				tt=tt+Number(rows[i].planNum);
			}
			if(tt!=Number($("#planCount").val())){
				$.messager.progress('close');
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>','<spring:message code="purchase.order.QuantityAddedOfSupplierDemandIsNotEqualToQuantityOfMaterialRequirement"/>','error');
				$('#datagrid-vendorQuota-list').datagrid('beginEdit', 0);
				return false;
			}
			

			
			var o =$('#datagrid-vendorQuota-list').datagrid('getData'); 
			var datas = JSON.stringify(o);   
	
			$("#selectTableDatas").val(datas);

			$('#form-vendorQuota-addoredit').form('submit',{
				ajax:true,
			
				url:ctx+'/manager/order/materialPlan/saveMaterialPlanVendor/'+type,  
				onSubmit:function(){
					var isValid = $(this).form('validate');
					if(!isValid){
						$.messager.progress('close');
					}
					return isValid;
				},
				success:function(data){
					data = $.evalJSON(data);
					if(data.success){
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>',data.msg,'info');
					}else{
						$.messager.alert('<spring:message code="purchase.order.Prompt"/>',data.msg,'error');
					}
					$.messager.progress('close');
					
					 $('#dialog-material-vendor').dialog('destroy');
					$('#datagrid_materialGroup_list').datagrid('reload');
					
				}
			});
		}
	 
	
		

	</script>
</body>
</html>
