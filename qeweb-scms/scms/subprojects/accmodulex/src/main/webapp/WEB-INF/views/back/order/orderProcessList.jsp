<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<html>
<head>
<title><spring:message code="purchase.order.WorkingProcedure"/></title>


</head>

<body>
<script type="text/javascript">
var editIndex = undefined;
function endEditing(){
	if (editIndex == undefined){return true}
	if ($('#datagrid-orderProcess-list').datagrid('validateRow', editIndex)){
		$('#datagrid-orderProcess-list').datagrid('endEdit', editIndex);
		editIndex = undefined;
		return true;
	} else {
		return false;
	}
}

function onClickRow2(index,data){
	$("#indexVal").val(index);
	if (editIndex != index){
		if (endEditing()){
			$('#datagrid-orderProcess-list').datagrid('beginEdit', index);
			editIndex = index;
		} else {
			$('#datagrid-orderProcess-list').datagrid('selectRow', editIndex);
		}
	}
}

function filexFmt(v,r,i){
	return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="uploadFile('+ i  +');">'<spring:message code="purchase.order.Upload"/>'</a>'/* 上传 */;
}

function fileNameFmt(v,r,i){
	if(r.filePath != null){
		return '<a href="javascript:;" onclick="File.download(\''+r.filePath+'\',\''+'\')">'+r.fileName+'</a>';
	}else{
		return '';
	}
}

function uploadFile(i){
	$('#form-file-upload').form('reset');
	$('#line').val(i);
	$('#win-file-upload').window('open');
}
</script>

<div class="easyui-panel"
		style="overflow: auto; width: 100%; height: 100%">
		
		
<input id="orderItemId" type="hidden" value="${orderItemId}">

	<c:if test="${isVendor}">
 	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveOrderProcess()"><spring:message code="purchase.order.Preservation"/></a>
 	</c:if>
 	
 	<form id="form-orderProcess" method="post" >
 	
 		  		<table style="padding:10px;width: 90%;margin: auto;">

					
			<tr>
				<td width="50%"><spring:message code="purchase.order.MaterialCoding"/>:${orderItem.material.code}
				</td>
				<td width="50%"><spring:message code="purchase.order.MaterialName"/>:${orderItem.material.name}
				</td>
			</tr>
			
						<tr>
				<td width="50%"><spring:message code="purchase.order.OrderQuantity"/>:${orderItem.orderNum}
				</td>
				<td width="50%">
				<spring:message code="purchase.order.RequiredArrivalTime"/>:<fmt:formatDate value="${orderItem.orderTime}" pattern="yyyy-MM-dd"/> 
				</td>
			</tr>

			</table>
			
 	
 	 <input type="hidden" name="tableDatas" id="tableDatas" />
 	 
	<table id="datagrid-orderProcess-list"
		data-options="fit:false,
		url:'${ctx}/manager/order/process/getPurchaseOrderProcessList/${orderItemId}',method:'post',singleSelect:false,
		toolbar:'#orderProcessListToolbar',title:'',onClickRow:onClickRow2,
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]">
		<thead>
			<tr>
				
             	<th width="150px" data-options="field:'processName'"><spring:message code="purchase.order.WorkingProcedureName"/></th>
			    <th width="150px" data-options="field:'orderNum',editor:{type:'numberbox', options: {readonly:false,required:true}}"><spring:message code="purchase.order.Number"/></th>
			    
			    	<c:if test="${isVendor}">
			    <th width="150px"  data-options="field:'filex',formatter:filexFmt"><spring:message code="purchase.order.AttachmentUpload"/></th>
			    </c:if>
	            <th width="150px" data-options="field:'fileName',formatter:fileNameFmt"><spring:message code="purchase.order.AttachmentName"/></th>
	            <th width="150px"  data-options="field:'filePath',hidden:true"><spring:message code="purchase.order.AttachmentPath"/></th>
			    
			    <th width="150px" data-options="field:'orderTime'"><spring:message code="purchase.order.Date"/></th>
			       

			<th  data-options="field:'id',hidden:true"></th>
			<th  data-options="field:'orderCount',hidden:true"></th>
			 <input type="hidden" id="indexVal" >
			</tr>
		</thead>
	</table>
			
		
	</form>
	
	
	<div id="orderProcessListToolbar">
		<div>
		
		</div>
		
    </div>
		
		</div>
		
		
<!-- 上传附件-->
	<div id="win-file-upload" class="easyui-dialog" title="<spring:message code="purchase.order.UploadingAttachments"/>" data-options="modal:true,closed:true,buttons:'#dialog-adder-a'">

		<div id="userListToolbar" style="padding: 5px;">
			<div>
				<a id="link-userSel-choice" href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="saveFile()"><spring:message code="purchase.order.Upload"/></a>
			</div>
			<div>
				<form id="form-file-upload" method="post" enctype="multipart/form-data">
					<input type="hidden" name="line" id="line"/> 
					<input type="hidden" name="type" id="type"/> 
						<spring:message code="purchase.order.Attachment"/>：<input type="file" class="easyui-validatebox input150" id="fujian" name="fujian" />
				</form>
			</div>
		</div>
	</div>


	

	<script type="text/javascript">
	$(function(){
		$('#datagrid-orderProcess-list').datagrid();
	});
	
	function saveFile(){
		var line = $('#line').val();
		$('#form-file-upload').form('submit', {
			url : '${ctx}/manager/order/process/saveFile',
			onSubmit: function(){
				var isValid = $(this).form('validate');
				if (!isValid){
				}
				return isValid;
			},
			success: function(data){
				var jdata = eval('('+data+')');
				if(jdata.success){
					var tableId = 'datagrid-orderProcess-list';
					
					RowEditor.accept(tableId);  
					var columns = $('#'+tableId).datagrid("options").columns;
					var rows = $('#'+tableId).datagrid("getRows");
				
						rows[line][columns[0][3].field]=jdata.fileName;
						rows[line][columns[0][4].field]=jdata.filePath;
						$($('#'+tableId).prev().find('.datagrid-body').find('tr')[line]).find("td[field='fileName']").find("div").html(jdata.fileName);
					
					$('#win-file-upload').dialog('close');
				}else{
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,jdata.message,'error');
				}
			},
			error:function(data){
				var jdata = eval('('+data+')');
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>'/* 提示 */,jdata.message,'error');
			}
		});
	}
	

	</script>
</body>
</html>
