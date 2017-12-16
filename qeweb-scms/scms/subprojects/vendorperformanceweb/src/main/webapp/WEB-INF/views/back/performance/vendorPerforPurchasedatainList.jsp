<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>采购额导入</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforPurchasedatain.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorPerforPurchasedatain',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="10%" data-options="field:'id',checkbox:true">供应商编码</th>
        <th width="20%" data-options="field:'vendorCode'">供应商编码</th>
        <th width="20%" data-options="field:'vendorName'">供应商名称</th>
        <th width="20%" data-options="field:'brandName'">品牌名称</th>
        <th width="20%" data-options="field:'vendorDate'">供货月份</th>
        <th width="10%" data-options="field:'defaultPurchase'">供货额</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
     <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="vendorPerforPurchasedatain.imp()">数据导入</a>    
      <form id="form" method="post">
      	<table>
      		<tr>
      			<td>供应商编码:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_vendorCode" type="text" style="width:150px;"/></td>
      			<td>供应商名称:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_vendorName" type="text" style="width:150px;"/></td>
      			<td>品牌:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_brandName" type="text" style="width:150px;"/></td>
      		    <td><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorPerforPurchasedatain.search()">查询</a></td>
      		</tr>
      	</table>
      </form>
    </div>
  </div>
  <div id="win-vendorPerforPurchasedatain-import" title="数据导入" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true,buttons:'#dialog-adder-bb'" >
			<form id="form-vendorPerforPurchasedatain-import" method="post" enctype="multipart/form-data" class="baseform"> 
				    <div>
			          <label>文件：</label>
			          <input type=file id="file" name="planfiles" />
			        </div>
				    <div>
			          <label>模板：</label>
			          <a href="javascript:;" style="font-size: 20px;" onclick="File.download('WEB-INF/templates/vendorPerforPurchasedatain.xls','vendorPerforPurchasedatain')">数据导入.xls</a>
			        </div>
			</form>  
		    <div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="vendorPerforPurchasedatain.saveImp()">提交</a>
				<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-vendorPerforPurchasedatain-import').form('reset')">重置</a>
			</div>
	</div>
<script type="text/javascript">
	$(function(){
		$('#datagrid').datagrid();
	});
</script>
</body>
</html>
