<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title>供应商物料类型管理</title>
	<script type="text/javascript">
		function orderCodeFmt(v,r,i){
			return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showOrderDetail('+ r.id +');">' + v + '</a>';
		}
		
	</script>
</head>

<body style="margin:0;padding:0;">


	
	<table id="datagrid-materialType-list" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/vendor/vendorMaterialTypeRel/getVendorMaterialTypeRelList/${vendorId}',method:'post',singleSelect:true,   
		fit:true,
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
				<th data-options="field:'id',hidden:true,checkbox:false"></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		
		<th data-options="field:'materialTypeCode',formatter:function(v,r,i){return r.materialType.code;}">物料类型编码</th>
		<th data-options="field:'materialTypeName',formatter:function(v,r,i){return r.materialType.name;}">物料类型名称</th>
		<th data-options="field:'topMaterialTypeName'">顶级物料类型</th>
		
			

		
		<tags:dynamic objName='${extended }' type='queryTable'></tags:dynamic>
		

		</tr></thead>
	</table>
	

	


	
		

	
	
<script type="text/javascript">

		function downPic(){
			var selections = $('#datagrid-order-list').datagrid('getSelections');
			if(selections.length==0){
				$.messager.alert('提示','没有选择任何记录！','info');
				return false;
			}
			var fileName=selections[0].fileName;
			var filePath=selections[0].filePath;
			var id=selections[0].id;
			
			if(filePath==null || filePath==''){
				$.messager.alert('提示','文件路径不能为空','warning');
				return false;
			}
			var url = ctx+'/manager/tecpic/tecpic/downloadFile';
			fileName = fileName==null?"":fileName;
			var inputs = '<input type="hidden" name="filePath" value="'+filePath+'"><input type="hidden" name="fileName" value="'+fileName+'"><input type="hidden" name="id" value="'+id+'">';
			jQuery('<form action="'+ url +'" method="post">'+inputs+'</form>')
	        .appendTo('body').submit().remove();
		}     
	

	
	 function showWin (title, width, height, url) {
		$dialog = $('<div/>').dialog({     
            title: title,     
            width: width,     
            height: height,     
            iconCls : 'pag-search',    
            closed: true,     
            cache: false,     
            href: url,     
            modal: true,  
            toolbar:'#toolbar',  
            onLoad:function(){  
            	
            },               
            buttons : [ /*{    
                 text : '确定',    
                 iconCls : 'ope-save',    
                 handler : function() { 
                	                    
                 }    
             }, */{    
                 text : '关闭',    
                 iconCls : 'ope-close',    
                 handler : function() {    
                     $dialog.dialog('close');    
                 }    
             } ]  

       });    
        
	  $dialog.dialog('open');  
	}
	
	



</script>
</body>
</html>
