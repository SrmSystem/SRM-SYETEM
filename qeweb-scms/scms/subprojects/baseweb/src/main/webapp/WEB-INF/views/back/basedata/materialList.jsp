<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
</script>
<script type="text/javascript" src="${ctx}/static/script/basedata/materialList.js"></script>
	<title>物料管理</title>
</head>

<body>
<div class="easyui-tabs" data-options="fit:true" style="height:100%;">
<!-- goal hidden properties -->
<input type="hidden" id="leafLevel" value="${leafLevel}"/>
<input type="hidden" id="noleafAllow" value="${noleafAllow}"/>
<div title="物料查看" data-options="fit:true">
  <div class="easyui-layout" data-options="fit:true">
  <div title="物料分类" class="easyui-panel" data-options="region:'west',width:205,tools:'#materialType-tt'">
    <div id="materialType-tt">
      <a class="icon-database_go" 
      href="javascript:;" title="导入分类" onclick="MaterialType.importType('dialog-materialType-imp')"></a>
    </div>
    <div id="dialog-materialType-imp" class="easyui-dialog" data-options="
        title:'导入分类',
        modal:true,closed:true,
        width:280,height:300,
        toolbar:'#dialog-materialType-imp-tt'
        ">
      <div itemId="ct">  
      <form itemId="form" id="form-materialType-imp" class="text-center" method="post" enctype="multipart/form-data">
        <input class="easyui-filebox" name="importFile" data-options="required:true,buttonText:'选择文件'"/>
      </form>
      <div id="dialog-materialType-imp-tt">
        <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:;" onclick="MaterialType.importSubmit('dialog-materialType-imp','tree-materialType')">提交</a>
      </div>
      </div>
    </div>
  	<ul id="tree-materialType" class="easyui-tree" data-options="url:'${ctx}/manager/basedata/materialType/getMaterialTypeEasyuiTree',
  	queryParams:{id:0},formatter:MaterialType.textFmt
	,onContextMenu:MaterialType.contextMenu,onClick:MaterialType.nodeClick">
  	</ul>
  	<div id="menu-materialtype" class="easyui-menu">
		<div id="menu-materialtype-add" onclick="MaterialType.add(0)" data-options="iconCls:'icon-add'">添加平级分类</div>
		<div id="menu-materialtype-add-low" onclick="MaterialType.add(1)" data-options="iconCls:'icon-add'">添加下级分类</div>
		<div id="menu-materialtype-edit" onclick="MaterialType.edit()" data-options="iconCls:'icon-edit'">编辑分类</div>
		<div id="menu-materialtype-del" onclick="MaterialType.deleteFalse()" data-options="iconCls:'icon-remove'">作废分类</div>
	</div>
	<div id="win-materialType" class="easyui-window" style="width:400px;height:260px;"
	data-options="modal:true,closed:true"
	>
	  <input type="hidden" itemId="type"/>
	  <div itemId="ct">
		<form id="form-materialType" method="post">
			<input type="hidden" name="id" value="0" id="materialType-id"/>
			<input type="hidden" name="parentId" id="materialType-parentId"/>
			<input type="hidden" name="beforeId" id="materialType-beforeId"/>
			<table class="form-tb">
				<tr>
				<td>编码:</td><td><input name="code" class="easyui-textbox" id="materialType-code" data-options="required:true"/></td>
				</tr>
				<tr>
				<td>名称:</td><td><input name="name" class="easyui-textbox" id="materialType-name" data-options="required:true"/></td>
				</tr>
			</table>
			<div style="text-align: center;padding:5px;">
				<a href="javascript:;" class="easyui-linkbutton" onclick="MaterialType.submit()">提交</a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialType').form('reset')">重置</a>
			</div>	
		</form>
	  </div>
	</div>
  </div>
<div class="easyui-panel" data-options="region:'center'">
	<table id="datagrid-material-list" title="物料列表" class="easyui-datagrid"
		data-options="
		fit:true,border:false,
		method:'post',singleSelect:false,
		toolbar:'#materialListToolbar',
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'operate',formatter:Material.operateFmt">操作</th>
		<th data-options="field:'code'">物料图号</th>
		<th data-options="field:'name'">物料名称</th>
		<th data-options="field:'describe'">描述（技术状态描述）</th>
		<th data-options="field:'partsCode'">零部件编号</th>
		<th data-options="field:'partsName'">零部件名称</th>
		<th data-options="field:'picStatus'">图纸状态</th>
		<th data-options="field:'technician'">设计者</th>
		<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">物料状态</th>
		</tr></thead>
	</table>
	<div id="materialListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Material.add()">新增</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Material.deleteFalse()">作废</a>
			<a class="easyui-linkbutton" data-options="iconCls:'icon-database_go',plain:true" href="javascript:;" title="导入分类" onclick="Material.importMaterial('dialog-material-imp')">导入</a>
		</div>
		<div>
			<form id="form-material-search" method="post">
			<input type="hidden" name="search-EQ_materialTypeId"/>
			物料图号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			图纸状态：<input id="aa" class="easyui-combobox" name="search-LIKE_picStatus"  style="width:80px;"/>
			物料状态：<input type="text" name="search-EQ_abolished" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
 			零部件编号：<input type="text" name="search-LIKE_partsCode" class="easyui-textbox" style="width:80px;"/>
<!-- 			<br>分类状态 ： <select class="easyui-combobox" id="material-categoryStatus" name="search-EQ_categoryStatus"><option value="">全部</option><option value="1">已分类</option><option value="0">未分类</option></select> -->
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Material.search()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-material-search').form('reset')">重置</a>
			</form>
		</div>
	</div>
	<div id="dialog-material-opt" class="easyui-dialog" title="物料" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-material-opt" method="post" >
				<input itemId="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>物料分类:</td>
					<td class="text-left">
					<input itemId="materialTypeId" name="materialTypeId" type="hidden"/>
					<label itemId="materialType"></label>
					</td>
				</tr>
				<tr>
					<td>物料图号:</td><td><input class="easyui-textbox" itemId="code" name="code" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>物料名称:</td><td><input class="easyui-textbox" itemId="name" name="name" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>物料描述:</td><td><input class="easyui-textbox" itemId="describe" name="describe" type="text" /></td>
				</tr>
				<tr>
					<td>图纸状态:</td><td>
					<input id="bc" itemId="picStatus" class="easyui-combobox" name="picStatus"  style="width:100%;"data-options="required:true,editable:false"/>
					</td>
				</tr>
				<tr>
					<td>设计者:</td><td><input class="easyui-textbox" itemId="technician" name="technician" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>物料状态:</td><td><select class="easyui-combobox" name="abolished" style="width:100%;"
						data-options="required:true">
						<option value='0'>启用</option>
						<option value='1'>禁用</option>
					</select>
					</td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="Material.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-material-opt').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
	
	<div id="dialog-material-imp" class="easyui-dialog" data-options="
        title:'导入物料',
        modal:true,closed:true,
        width:280,height:300,
        toolbar:'#dialog-material-imp-tt'
        ">
      <div itemId="ct">  
      <form itemId="form" id="form-material-imp" class="text-center" method="post" enctype="multipart/form-data">
        <input class="easyui-filebox" name="importFile" data-options="required:true,buttonText:'选择文件'"/>
      </form>
      <div id="dialog-material-imp-tt">
        <a class="easyui-linkbutton" iconCls="icon-ok" href="javascript:;" onclick="Material.importSubmit('dialog-material-imp','datagrid-material-list')">提交</a>
      </div>
      </div>
    </div>
	
  </div>
  </div>	
</div>
<div title="分类设置" data-options="fit:true">
<div class="easyui-layout" data-options="fit:true">
  <div class="easyui-panel" title="物料分类" data-options="region:'west',width:205">
  	<ul id="tree-materialType-category" class="easyui-tree" data-options="url:'${ctx}/manager/basedata/materialType/getMaterialTypeEasyuiTree',queryParams:{id:0},fomatter:MaterialType.textFmt">
  	</ul>
	<div id="win-materialType-category" class="easyui-window" style="width:400px;height:260px;"
	data-options="modal:true,closed:true"
	>
		<form id="form-materialType-category" method="post">
			<input type="hidden" name="id" value="0" id="materialType-id"/>
			<input type="hidden" name="parentId" id="materialType-parentId"/>
			<input type="hidden" name="beforeId" id="materialType-beforeId"/>
			<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
				<td>编码:</td><td><input name="code" class="easyui-textbox" id="materialType-code" data-options="required:true"/></td>
				</tr>
				<tr>
				<td>名称:</td><td><input name="name" class="easyui-textbox" id="materialType-name" data-options="required:true"/></td>
				</tr>
			</table>
			<div style="text-align: center;padding:5px;">
				<a href="javascript:;" class="easyui-linkbutton" onclick="MaterialType.submit()">提交</a>
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialType-category').form('reset')">重置</a>
			</div>	
		</form>
	</div>
  </div>
  <div class="easyui-panel" data-options="region:'center'">
	<table id="datagrid-material-list-category" title="物料列表" class="easyui-datagrid"
		data-options="
		fit:true,
		border:false,
		url:'${ctx}/manager/basedata/material',method:'post',singleSelect:false,
		toolbar:'#materialListToolbar-category',queryParams:{'search-EQ_categoryStatus':0},
		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">物料图号</th>
		<th data-options="field:'name'">物料名称</th>
		<th data-options="field:'describe'">描述（技术状态描述）</th>
		<th data-options="field:'partsCode'">零部件编号</th>
		<th data-options="field:'partsName'">零部件类别</th>
		<th data-options="field:'picStatus'">图纸状态</th>
		<th data-options="field:'technician'">设计者</th>
		<th data-options="field:'abolished'">物料状态</th>
		</tr></thead>
	</table>
	<div id="materialListToolbar-category" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-ok',plain:true" onclick="Material.category()">分类</a>
		</div>
		<div>
			<form id="form-material-search-category" method="post">
			<input type="hidden" name="search-EQ_materialTypeId"/>
			<input type="hidden" name="search-EQ_categoryStatus" value="0"/>
			物料图号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			图纸状态：<input id="bb" itemId="picStatus" class="easyui-combobox" name="search-LIKE_picStatus"  style="width:80px;" data-options="editable:false"/>
			物料状态：<input type="text" name="search-EQ_abolished" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			零部件编号：<input type="text" name="search-LIKE_partsCode" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Material.search_category()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-material-search-category').form('reset')">重置</a>
			</form>
		</div>
	</div>
</div>
</div>
</div>
</div>

<form id="form-category-submit" method="post">
	<input type="hidden" name="materialIds"/>
	<input type="hidden" name="materialTypeId"/>
</form>
<script type="text/javascript">
$(function(){
	$('#aa').combobox({    
	    url:ctx+'/manager/basedata/material/getPicStatus',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='search-LIKE_picStatus']").val(rec.text);   
	      }
	});
	$('#bb').combobox({    
	    url:ctx+'/manager/basedata/material/getPicStatus',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='picStatus']").val(rec.text);   
	      }
	});
	$('#bc').combobox({    
	    url:ctx+'/manager/basedata/material/getPicStatus',    
	    valueField:'id',    
	    textField:'text' ,
	   	onSelect: function(rec){    
	           $("input[name='picStatus']").val(rec.text);   
	      }
	});
})
</script>
</body>
</html>