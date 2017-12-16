<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>物料类别管理</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/vendorPerforMaterialType.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorPerforMaterialType',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="30px" data-options="field:'id',checkbox:true">ID</th>
        <th width="15%" data-options="field:'code'">物料类别编号</th>
        <th width="15%" data-options="field:'name'">物料类别名称</th>
        <th width="15%" data-options="field:'remarks'">备注</th>
        <th width="15%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="materialType.add()">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="materialType.update()">修改</a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="materialType.release()">启用</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="materialType.dels()">作废</a>   
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true" onclick="materialType.deletes()">删除</a>   
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-database_go',plain:true" onclick="materialType.imp()">导入</a>   
      <form id="form" method="post">
                             物料类别编号:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             物料类别名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
                             生效状态:
                         <select name="search-EQ_abolished" class="easyui-combobox" style="width:80px;">
                         	<option value="">请选择</option>
                         	<option value="0">生效</option>
                         	<option value="1">作废</option>
                         </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="materialType.search()">查询</a>
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>        
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="dialog-materialType-saveUpadte" class="easyui-dialog" title="物料类别"
	data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
			<form id="form-materialType-saveUpadte" method="post" >
			    <input type="hidden" id="id" name="id" value="0"/>
			    <input type="hidden" id="abolished" name="abolished" value=""/>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>物料类别编号:</td><td><input class="easyui-textbox" id="code" name="code" type="text"
					data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>物料类别名称:</td><td><input class="easyui-textbox" id="levelName" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr><td> 备注:</td>
					<td>
						<input class="easyui-textbox" id="remarks" name="remarks" type="text"/>
					</td>
				</tr>
				</table>
			</form>
			<div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="materialType.submit()">提交</a>
				<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-materialType-saveUpadte').form('reset')">重置</a>
			</div>
	</div>
<div id="win-import"  title="数据导入" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true,buttons:'#dialog-adder-bb2'" >
			<form id="form-import" method="post" enctype="multipart/form-data" class="baseform"> 
				<div>
		          <label>文件：</label>
		          <input type=file id="file" name="planfiles" />
		        </div>
			    <div>
		          <label>模板：</label>
		          <a href="javascript:;" style="font-size: 20px;" onclick="File.download('WEB-INF/templates/vendorPerforMattye.xls','materialTypeList')">数据导入.xls</a>
		        </div>
			</form>  
		    <div id="dialog-adder-bb2">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="materialType.saveImp()">提交</a>
				<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-import').form('reset')">重置</a>
			</div>
	</div>
<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
</script>
</body>
</html>
