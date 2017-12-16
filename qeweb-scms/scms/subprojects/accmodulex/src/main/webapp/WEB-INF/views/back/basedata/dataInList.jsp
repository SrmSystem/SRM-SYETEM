<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<title><!-- 数据导入 --><spring:message code="purchase.basedata.DataImport"/></title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforDataIn.js"></script>
</head>

<body>
	<form id="form-vendorPerforDataIn-import" method="post" enctype="multipart/form-data" class="baseform"> 
	    <div>
          <label><!-- 文件 --><spring:message code="purchase.basedata.Files"/>：</label>
          <input type=file id="file" name="planfiles" />
        </div>
	</form>  
   <div id="dialog-adder-bb">
		<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="saveImp()"><!-- 提交 --><spring:message code="vendor.submit"/></a>
		<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-vendorPerforDataIn-import').form('reset')"><!-- 重置 --><spring:message code="vendor.resetting"/></a>
   </div>
   <script type="text/javascript">
   function saveImp() {
		$.messager.progress();
		$('#form-vendorPerforDataIn-import').form('submit',{
			ajax:true,
			iframe: true,    
			url:ctx+'/manager/basedata/dataIn/filesUpload', 
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
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>','<spring:message code="purchase.basedata.DataImportSuccessfully"/>'/* "导入数据成功" */,'info');
					$('#win-vendorPerforDataIn-import').window('close');
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>',result.msg + "<br>'<spring:message code="purchase.basedata.ImportLogsPleaseSee"/>'<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>'<spring:message code="purchase.basedata.LogFiles"/>'</b></a>" ,'error');
				}
				}catch (e) {  
					$.messager.alert('<spring:message code="purchase.order.Prompt"/>',data,'error');
				}
			}
		});
	}
   </script>
</body>
</html>
