<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>


<html>
<head>
<title><spring:message code="vendor.common.purchasingMaterialManagement"/><!-- 采购物料管理 --></title>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<script type="text/javascript">
	var ctx = '${pageContext.request.contextPath}';
</script>

<script type="text/javascript">

</script>
</head>

<body>



	<table id="datagrid" class="easyui-datagrid"
		data-options="
    fit:true,
    singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
    toolbar:'#tt'
  ">
		<thead>

	</table>
	<div id="tt">
		<div>

				<a href="javascript:;" onclick="addFile()"
					class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true"><spring:message code="vendor.new"/><!-- 新增 --></a>
					

		
		</div>
		
		
			
		<div>
			
		</div>
	</div>

	<div id="win-vendor-addoredit" class="easyui-window" title="供应商"
		style="width: 400px; height: 400px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-vendor'">
		<div align="center">
		<form id="form-vendor-addoredit" method="post" enctype="multipart/form-data">
				
				
			<table style="text-align: right; padding: 5px; margin: auto;"
				cellpadding="5">
				<tr>
										<td><spring:message code="vendor.attachment"/><!-- 附件 -->:</td>
						<td><input type=file id="file" name="planfiles"   /></td>
						</tr>
						
				<tr>
						<td><spring:message code="vendor.common.downloadAttachment"/><!-- 附件下载 -->:<a href="javascript:;" onclick="downFile(10)"> <spring:message code="vendor.download"/><!-- 下载 --> </a></td>
						<td></td>
				</tr>
				

	

			</table>
			<div id="dialog-adder-vendor">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="submitFile()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
		  </div>
		</form>
		</div>
	</div>
	
	



	

	<script type="text/javascript">
	
	

	function addFile(){
		$('#win-vendor-addoredit').dialog({
			iconCls:'icon-edit',
			title:'<spring:message code="vendor.modification"/>'/* 修改 */
		});
		$('#form-vendor-addoredit').form('clear');
		$('#win-vendor-addoredit').dialog('open');
		
	
	}
	 function submitFile(){
		var url = ctx+'/manager/common/baseFile/addEntity';
	
		$.messager.progress({
			title:'<spring:message code="vendor.prompting"/>',/* 提示 */
			msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
		});
		$('#form-vendor-addoredit').form('submit',{
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
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,sucMeg,'info');
				}else{
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,result.msg,'error');
				}
				}catch (e) {
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,data,'error');
				}
			}
			
		});
	}
	
	  function downFile (id){
			
			var url = ctx+'/manager/common/baseFile/downloadFile';
			var inputs = '<input type="hidden" name="billId" value="'+id+'">';
			
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
		    .appendTo('body').submit().remove(); 
		} 
	

	

	</script>
</body>
</html>
