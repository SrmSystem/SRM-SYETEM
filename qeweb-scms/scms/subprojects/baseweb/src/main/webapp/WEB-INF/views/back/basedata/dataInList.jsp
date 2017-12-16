<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>数据导入</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforDataIn.js"></script>
</head>

<body>
	<form id="form-vendorPerforDataIn-import" method="post" enctype="multipart/form-data" class="baseform"> 
	    <div>
          <label>文件：</label>
          <input type=file id="file" name="planfiles" />
        </div>
	</form>  
   <div id="dialog-adder-bb">
		<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="saveImp()">提交</a>
		<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-vendorPerforDataIn-import').form('reset')">重置</a>
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
					$.messager.alert('提示',"导入数据成功",'info');
					$('#win-vendorPerforDataIn-import').window('close');
					$('#datagrid').datagrid('reload');
				}else{
					$.messager.alert('提示',result.msg + "<br>导入日志请参阅<a href='javascript:;' onclick='File.showLog(\"" + result.log + "\")'><b>日志文件</b></a>" ,'error');
				}
				}catch (e) {  
					$.messager.alert('提示',data,'error');
				}
			}
		});
	}
   </script>
</body>
</html>
