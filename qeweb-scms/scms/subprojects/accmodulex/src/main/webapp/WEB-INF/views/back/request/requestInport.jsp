<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
	
<!-- 导入要货单 -->
<div id="win-request-import" class="easyui-window" title="导入要货单" style="width:400px;height:200px"
data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
		<form id="form-request-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/goodsrequest/filesUpload"> 
			<div style="margin-bottom:20px">
				文件：<input type=file id="file" name="planfiles" /><br>
				模板： <a href="javascript:;" onclick="File.download('WEB-INF/template/PurchaseRequest.xls','要货单模版')">要货单模版.xls</a>     
			</div>
			<div style="text-align: center;padding:5px;">
				<a href="javascript:;" class="easyui-linkbutton" onclick="RequestManage.saveimportrequest();">保存</a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-request-import').form('reset')">重置</a>
			</div>
		</form>  
	</div>
</div>