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
  <table id="datagrid" class="easyui-datagrid" data-options="
    fit:true,url:'${ctx}/manager/vendor/vendorPerforDataIn',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="2%" data-options="field:'id',checkbox:true">供应商编码</th>
        <th width="7%" data-options="field:'vendorCode'">供应商编码</th>
        <th width="7%" data-options="field:'vendorName'">供应商名称</th>
        <th width="7%" data-options="field:'brandName'">品牌名称</th>
        <th width="7%" data-options="field:'factoryName'">工厂名称</th>
        <th width="7%" data-options="field:'materialCode'">物料类别编号</th>
        <th width="7%" data-options="field:'materialName'">物料类别名称</th>
        <th width="7%" data-options="field:'indexName'">指标名称</th>
        <th width="7%" data-options="field:'element'">要素名称</th>
        <th width="7%" data-options="field:'elementValue'">要素值</th>
        <th width="7%" data-options="field:'assessDate'">评估时间</th>
        <th width="7%" data-options="field:'cycleName',formatter:function(v,r){if(r.cycleEntity) return r.cycleEntity.cycleName; else ''}">周期</th>
        <th width="7%" data-options="field:'updateUserName'">更新人</th>
        <th width="7%" data-options="field:'lastUpdateTime'">更新时间</th>
        
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
     <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="vendorPerforDataIn.imp()">数据导入</a>    
     <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="vendorPerforDataIn.updateStart()">修改</a>    
      <form id="form" method="post">
      	<table>
      		<tr>
      			<td>开始评估时间:</td>
      			<td><input class="easyui-datebox" name="search-GTE_assessDate" data-options="showSeconds:true" value="" style="width:150px"></td>
      			<td>结束评估时间:</td>
      			<td><input class="easyui-datebox" name="search-LTE_assessDate" data-options="showSeconds:true" value="" style="width:150px"></td>
      			<td>周期:</td>
      			<td><input class="easyui-combobox" id="search-EQ_cycleId" name="search-EQ_cycleId" style="width:150px"/></td>
      		</tr>
      		<tr>
      			<td>指标名称:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_indexName" type="text" style="width:150px;"/></td>
      			<td>品牌:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_brandName" type="text" style="width:150px;"/></td>
      			<td>要素名称:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_element" type="text"  style="width:150px;"/></td>
      		    <td rowspan="2"><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="vendorPerforDataIn.search()">查询</a><a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>  </td>
      		</tr>
      	</table>
      </form>
    </div>
  </div>
  <div id="win-vendorPerforDataIn-import"  title="数据导入" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true,buttons:'#dialog-adder-bb'" >
			<form id="form-vendorPerforDataIn-import" method="post" enctype="multipart/form-data" class="baseform"> 
				    <div>
			          <label>周期：</label>
			          <input id="cc" class="easyui-combobox" name="cycleId"  style="width:80px;" data-options="required:true,editable:false"/>
			        </div>
				    <div>
			          <label>文件：</label>
			          <input type=file id="file" name="planfiles" />
			        </div>
				    <div>
			          <label>模板：</label>
			          <a href="javascript:;" style="font-size: 20px;" onclick="File.download('WEB-INF/templates/vendorPerforDataIn.xls','vendorPerforDataIn')">数据导入.xls</a>
			        </div>
			</form>  
		    <div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="vendorPerforDataIn.saveImp()">提交</a>
				<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-vendorPerforDataIn-import').form('reset')">重置</a>
			</div>
	</div>
	<div id="updateapp" title="要素修改"
		data-options="modal:true,closed:true,buttons:'#dialog-adder-bbb'">
		 <form method="post" id="ff" class="baseform">
		 <input  type="hidden" id="useid" name="useid" value=""/>
		 	<div>
	          <label>供应商名称：</label>
	          <input class="easyui-textbox" data-options="required:true,disabled:true" name="vendorName"/>
	        </div>
		 	<div>
	          <label>品牌名称：</label>
	          <input class="easyui-textbox" data-options="required:true,disabled:true" name="brandName"/>
	        </div>
		 	<div>
	          <label>品牌名称：</label>
	          <input class="easyui-textbox" data-options="required:true,disabled:true" name="factoryName"/>
	        </div>
		 	<div>
	          <label>零部件名称：</label>
	          <input class="easyui-textbox" data-options="required:true,disabled:true" name="materialName"/>
	        </div>
		 	<div>
	          <label>要素名称：</label>
	          <input class="easyui-textbox" data-options="required:true,disabled:true" name="element"/>
	        </div>
		 	<div>
	          <label>要素值：</label>
	          <input class="easyui-textbox" data-options="required:true" name="elementValue"/>
	        </div>
		 </form>
		 <div id="dialog-adder-bbb">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="vendorPerforDataIn.update()">提交</a>
		 </div>
	</div>
	<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
			$('#search-EQ_cycleId').combobox({    
				url:ctx+'/manager/vendor/performance/index/getVendorPerforCycle',    
				valueField:'id',    
				textField:'text' ,
				onSelect: function(rec){    
					$("input[name='search-EQ_cycleId']").val(rec.id);
				}
			});
		})
	</script>
</body>
</html>
