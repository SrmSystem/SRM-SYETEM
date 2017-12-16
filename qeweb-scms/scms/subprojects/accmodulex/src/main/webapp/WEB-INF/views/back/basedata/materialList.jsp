<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<script type="text/javascript">
var ctx = '${pageContext.request.contextPath}';
</script>
<script type="text/javascript" src="${ctx}/static/script/basedata/materialList.js"></script>
<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<title><spring:message code="purchase.basedata.materialManagement"/><!-- 物料管理 --></title>
</head>

<body>
<%-- <div class="easyui-tabs" data-options="fit:true" style="height:100%;">
<!-- goal hidden properties -->
<input type="hidden" id="leafLevel" value="${leafLevel}"/>
<input type="hidden" id="noleafAllow" value="${noleafAllow}"/>
<div title="物料查看" data-options="fit:true">
  <div class="easyui-layout" data-options="fit:true">
  <div title="物料分类" class="easyui-panel" data-options="region:'west',width:205,tools:'#materialType-tt'">
    <div id="materialType-tt">
   <!--    <a class="icon-database_go" 
      href="javascript:;" title="导入分类" onclick="MaterialType.import('dialog-materialType-imp')"></a> -->
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
  </div> --%>
<!-- <div class="easyui-panel" data-options="region:'center'"> -->
	<table id="datagrid-material-list" title='<spring:message code="purchase.basedata.materialList"/><!-- 物料列表 -->' class="easyui-datagrid"
		data-options="
		fit:true,border:false,
		url:'${ctx}/manager/basedata/material',
		method:'post',singleSelect:false,
		toolbar:'#materialListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<!-- <th data-options="field:'operate',formatter:Material.operateFmt">操作</th> -->
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code}"><spring:message code="purchase.basedata.materialCoding"/><!-- 物料编码 -->
</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name}"><spring:message code="purchase.basedata.materialName"/><!-- 物料名称 --></th>
		
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.org.code}"><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.org.name}"><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th> 
		
		<th data-options="field:'factoryCode',formatter:function(v,r,i){return r.factoryEntity.code}"><spring:message code="purchase.basedata.FactoryCodings"/><!-- 工厂编码 --></th>
		<th data-options="field:'factoryName',formatter:function(v,r,i){return r.factoryEntity.name}"><spring:message code="purchase.basedata.FactoryNames"/><!-- 工厂名称 --></th>   
	
		<th data-options="field:'maxPackageQty'"><spring:message code="purchase.basedata.LargePackageNumber"/><!-- 大包装数 --></th>
		<th data-options="field:'minPackageQty'"><spring:message code="purchase.basedata.SmallPackageNumber"/><!-- 小包装数 --></th>
		
		<th data-options="field:'cdqwl',formatter:function(v,r,i){if(null!=r.material.cdqwl && 'X'==r.material.cdqwl) return '<spring:message code="purchase.basedata.material.cdqwl.long"/>'; else return '<spring:message code="purchase.basedata.material.cdqwl.short"/>'; }"><spring:message code="purchase.basedata.material.cdqwl"/><!-- 周期--></th>
		<th width="150px" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}"><spring:message code="purchase.basedata.EffectiveState"/><!-- 生效状态 --></th>
		
		
	<!-- 	<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">物料状态</th> -->
		</tr></thead>
	</table>
	<div id="materialListToolbar" style="padding:5px;">
		<div>
	<!-- 		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Material.add()">新增</a> -->

<!-- 			<a class="easyui-linkbutton" data-options="iconCls:'icon-database_go',plain:true" href="javascript:;" title="导入分类" onclick="Material.import('dialog-material-imp')">导入</a> -->
		
						<a href="javascript:;" class="easyui-linkbutton"
					data-options="iconCls:'icon-add',plain:true"
					onclick="Material.editPackage()"><spring:message code="purchase.basedata.MaintainTheNumberOfPackages"/><!-- 维护包装数 --></a>
					
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycOrder()"><spring:message code="vendor.synchronizing"/><!-- 同步 --></a> 
		           <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="Material.deleteFalse()"><spring:message code="vendor.nullify"/><!-- 作废 --></a>
				  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="Material.effect()"><spring:message code="vendor.takeEffect"/><!-- 生效 --></a>
		</div>
		<div>
			<form id="form-material-search" method="post">
			<input type="hidden" name="search-EQ_materialTypeId"/>
			<spring:message code="purchase.basedata.materialCoding"/><!-- 物料编码 -->
：<input    id="material_codes" type="text" name="search-IN_material.code" class="easyui-textbox" style="width:80px;"/>
			  <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'" onclick="openMaterialS()"></a>
			<spring:message code="purchase.basedata.materialName"/><!-- 物料名称 -->：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.supplierCode"/><!-- 供应商编码 -->：<input  id="vendor_codes"  type="text" name="search-IN_org.code" class="easyui-textbox" style="width:80px;"/>
			 <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'" onclick="openVendorS()"></a>
			<spring:message code="vendor.supplierName"/><!-- 供应商名称 -->：<input type="text" name="search-LIKE_org.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.basedata.FactoryCodings"/><!-- 工厂编码 -->：<input type="text" name="search-LIKE_factoryEntity.code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.basedata.FactoryNames"/><!-- 工厂名称 -->：<input type="text" name="search-LIKE_factoryEntity.name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="purchase.basedata.EffectiveForState"/><!-- 生效的状态 -->：<select class="easyui-combobox"  style="width:80px;" data-options="editable:false" name="search-EQ_abolished"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.takeEffect"/><!-- 生效 --></option><option value="1"><spring:message code="purchase.basedata.Invalid"/><!-- 失效 --></option></select>					
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
		    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Material.search()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-material-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
			<div id="win-package-addoredit" class="easyui-window"
		style="width: 400px; height: 400px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-remark'">
		<form id="form-package-addoredit" method="post">
		
			<input id="vendorMaterialRelIds" name="vendorMaterialRelIds"  value="0" type="hidden" />
			
			<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">

			<tr>
	<td ><spring:message code="purchase.basedata.LargePackageNumber"/><!-- 大包装数 -->：</td>
	<td><input class="easyui-textbox"  name="maxPackageQty" type="text"
						data-options="required:true"
					/>
					</td>
					</tr>
			
			<tr>		
	<td ><spring:message code="purchase.basedata.SmallPackageNumber"/><!-- 小包装数 -->：</td>
	<td><input class="easyui-textbox"  name="minPackageQty" type="text"
						data-options="required:true"
					/>
					</td>
						
				</tr>
			</table>
		</form>
		<div id="dialog-adder-remark">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="Material.submitPackage()"><spring:message code="vendor.submit"/><!-- 提交 --></a> <a
				href="javascript:;" class="easyui-linkbutton"
				onclick="$('#form-package-addoredit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
		</div>
	</div>
	
	
	  <!-- 物料的查询 -->
<div id="win-searchMaterial-detail" class="easyui-window" title='<spring:message code="purchase.basedata.SelectMaterials"/><!-- 选择物料 -->' style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true">
		<div id="div_material_detail" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="choiceM()"><spring:message code="vendor.confirm"/><!-- 确认 --></a>
			</div>
			<div>
			    <a href="javascript:;"  class="easyui-linkbutton"   data-options="iconCls:'icon-add',plain:true"  onclick="loadContent1()">加载内容     <!-- 加载内容 --></a>:
                <input type="text"   data-options=" prompt :  '将内容copy后，点击 加载内容  '  "   id = "copyMaterialContentSMD" class="easyui-textbox"     style="width:180px;"/>
			</div>
			<div>
			 <a class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"  onclick="addMaterial();" ><spring:message code="purchase.basedata.ReadClipboardContents"/><!-- 读取剪贴板内容 --></a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="insert()"><spring:message code="purchase.basedata.Add"/><!-- 添加 --></a>
		     <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="removeit()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
          <!--    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">保存</a> -->
             <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="resetMaterial()"><spring:message code="purchase.basedata.Empty"/><!-- 清空 --></a>
			</div>
		</div>
		<div id="idMateriaListMaterial">
			<table id="datagrid-searchMaterial-list" title='<spring:message code="purchase.basedata.materialList"/><!-- 物料列表 -->' class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: onClickRow,
				rownumbers:true">
				<thead><tr>
			   <th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code="purchase.basedata.materialCoding"/><!-- 物料编码 -->
			   </th>
				</tr></thead>
			</table>
		</div>	
	</div>
</div> 
  
	
	
	
	
	<!--   供应商的查询 -->
   	<div id="win-searchOrg-detail" class="easyui-window" title='<spring:message code="purchase.basedata.SelectSuppliers"/><!-- 选择供应商 -->' style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true">
		<div id="div_org_detail" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="choiceV()"><spring:message code="purchase.basedata.Confim"/><!-- 确定 --></a>
			</div>
			<div>
			     <a href="javascript:;"  class="easyui-linkbutton"   data-options="iconCls:'icon-add',plain:true"  onclick="loadContent3()">加载内容     <!-- 加载内容 --></a>:
                 <input type="text"   data-options=" prompt :  '将内容copy后，点击 加载内容  '  "   id = "copyVendorContentSOD" class="easyui-textbox"     style="width:180px;"/>
			</div>
			<div>
			 <a class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"  onclick="addVendor();" ><spring:message code="purchase.basedata.ReadClipboardContents"/><!-- 读取剪贴板内容 --></a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="insertV()"><spring:message code="purchase.basedata.Add"/><!-- 添加 --></a>
		     <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="removeitV()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
            <!--  <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="acceptV()">保存</a> -->
             <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="resetVendor()"><spring:message code="purchase.basedata.Empty"/><!-- 清空 --></a>
		
			</div>
		</div>
		<div id="idMateriaListVendor">
			<table id="datagrid-searchOrg-list" title='<spring:message code="purchase.basedata.SupplierList"/><!-- 供应商列表 -->' class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: onClickRowV,
				rownumbers:true">
				<thead><tr>
				<th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code="vendor.coding"/><!-- 编码 --></th>
				</tr></thead>
			</table>
		</div>	
	</div>
</div> 
	
	
	
	
	
	
	
	
	
<%-- 	<div id="dialog-material-opt" class="easyui-dialog" title="物料" style="width:400px;height:200px"
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
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'">物料图号</th>
		<th data-options="field:'name'">物料名称</th>
		
				<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">物料状态</th>
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
			物料状态：<input type="text" name="search-EQ_abolished" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="Material.search_category()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-material-search-category').form('reset')">重置</a>
			</form>
		</div>
	</div>
</div>
</div>
</div>
</div> --%>

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

function sycOrder(){
	$.messager.progress();
	$.ajax({
		url:'${ctx}/manager/basedata/material/sycOrder',
		type:'POST',
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="purchase.order.news"/>'/* 消息 */,
						msg:  data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#datagrid-material-list').datagrid('reload'); 
				}else{
					$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',data.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="purchase.order.Prompt"/>',e,'error'); 
			} 
		}
	});
}

/******************************************** 物料的窗口的打开-开始******************************* */
function openMaterialS() {
	$("#copyMaterialContentSMD").textbox('setValue','')//赋值
	$('#win-searchMaterial-detail').window('open');
	//清除所有的行项目
	$('#datagrid-searchMaterial-list').datagrid('loadData', { total: 0, rows: [] });
	 var rows = $('#datagrid-searchMaterial-list').datagrid("getRows").length;
	 if(rows < 5){
		 for(var i = 0 ;i<=5 ;i++){
				insert();
			}
	 }
	
	//清除当前列表缓存
	 $('#datagrid-searchMaterial-list').datagrid('clearSelections');
	 $("#idMateriaListMaterial").find("tbody tr input:eq(1)").focus();
}
/* 选择带会物料 */
function choiceM() {
	accept();
	var selections = $('#datagrid-searchMaterial-list').datagrid('getRows');
	var codes = "";
	for(var i=0;i<selections.length;i++){
		if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
			codes += selections[i].code+",";
		}
	}
	$("#material_code").val(codes);
	$("#material_codes").textbox("setValue",codes);
	$('#win-searchMaterial-detail').window('close');	
	
	
}

var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-searchMaterial-list').datagrid('validateRow', editIndex)){
			var ed = $('#datagrid-searchMaterial-list').datagrid('getEditor', {index:editIndex,field:'code'});
			$('#datagrid-searchMaterial-list').datagrid('beginEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow(index){
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-searchMaterial-list').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-searchMaterial-list').datagrid('selectRow', editIndex);
			}
		}
	}
	function insert(){
		if (endEditing()){
			$('#datagrid-searchMaterial-list').datagrid('appendRow',{code:''});
			editIndex = $('#datagrid-searchMaterial-list').datagrid('getRows').length-1;
			$('#datagrid-searchMaterial-list').datagrid('selectRow', editIndex)
					.datagrid('beginEdit', editIndex);
		}
		
	}
	function removeit(){
		if (editIndex == undefined){return}
		$('#datagrid-searchMaterial-list').datagrid('cancelEdit', editIndex)
				.datagrid('deleteRow', editIndex);
		editIndex = undefined;
	}
	function accept(){
		if (endEditing()){
			$('#datagrid-searchMaterial-list').datagrid('acceptChanges');
		}
	}
	function reject(){
		$('#datagrid-searchMaterial-list').datagrid('rejectChanges');
		editIndex = undefined;
	}

	function addMaterial(){
		var arr = handlMaterial();
		if(arr != null ){
			//清除所有的行项目
			$('#datagrid-searchMaterial-list').datagrid('loadData', { total: 0, rows: [] }); 
			for(var i= 0 ; i<arr.length ; i++ ){
				if (endEditing()){
					$('#datagrid-searchMaterial-list').datagrid('appendRow',{code:arr[i]});
					editIndex = $('#datagrid-searchMaterial-list').datagrid('getRows').length-1;
					$('#datagrid-searchMaterial-list').datagrid('selectRow', editIndex)
							.datagrid('beginEdit', editIndex);
				}
			}
		}
	}
	
	function resetMaterial(){
		//清除所有的行项目
		$('#datagrid-searchMaterial-list').datagrid('loadData', { total: 0, rows: [] }); 
		editIndex = undefined;
	}
	
	//加载input内容(物料1)
	function loadContent1(){
		var arr = handlInputMaterial( $('#copyMaterialContentSMD').val() );
		if(arr != null ){
			//清除所有的行项目
			$('#datagrid-searchMaterial-list').datagrid('loadData', { total: 0, rows: [] }); 
			for(var i= 0 ; i<arr.length ; i++ ){
				if (endEditing()){
					$('#datagrid-searchMaterial-list').datagrid('appendRow',{code:arr[i]});
					editIndex = $('#datagrid-searchMaterial-list').datagrid('getRows').length-1;
					$('#datagrid-searchMaterial-list').datagrid('selectRow', editIndex)
							.datagrid('beginEdit', editIndex);
				}
			}
		}
	}

/* ----------------------------------------物料的窗口的打开-结束------------------------------------------------------ */


/* 供应商的窗口的打开 */
function openVendorS() {	
	$("#copyVendorContentSOD").textbox('setValue','')//赋值
	$('#win-searchOrg-detail').window('open');
	//清除所有的行项目
	$('#datagrid-searchOrg-list').datagrid('loadData', { total: 0, rows: [] });
	 var rows = $('#datagrid-searchOrg-list').datagrid("getRows").length;
	 if(rows < 5){
		for(var i = 0 ;i<=5 ;i++){
			insertV();
		}
	 }

	//清除当前列表缓存
	 $('#datagrid-searchOrg-list').datagrid('clearSelections');
	 $("#idMateriaListVendor").find("tbody tr input:eq(1)").focus();
}

/* 选择带会供应商 */
function choiceV() {
	acceptV();
	var selections = $('#datagrid-searchOrg-list').datagrid('getRows');
	var codes = "";
	for(var i=0;i<selections.length;i++){
		if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
			codes += selections[i].code+",";
		}
	}
	$("#vendor_code").val(codes);
	$("#vendor_codes").textbox("setValue",codes);
	$('#win-searchOrg-detail').window('close');	
}



var editIndexV = undefined;
function endEditingV(){
	if (editIndexV == undefined){return true}
	if ($('#datagrid-searchOrg-list').datagrid('validateRow', editIndexV)){
		var ed = $('#datagrid-searchOrg-list').datagrid('getEditor', {index:editIndexV,field:'code'});
		$('#datagrid-searchOrg-list').datagrid('beginEdit', editIndexV);
		editIndexV = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickRowV(index){
	if (editIndexV != index){
		if (endEditingV()){
			$('#datagrid-searchOrg-list').datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndexV = index;
		} else {
			$('#datagrid-searchOrg-list').datagrid('selectRow', editIndexV);
		}
	}
}
function insertV(){
	if (endEditingV()){
		$('#datagrid-searchOrg-list').datagrid('appendRow',{code:''});
		editIndexV = $('#datagrid-searchOrg-list').datagrid('getRows').length-1;
		$('#datagrid-searchOrg-list').datagrid('selectRow', editIndexV)
				.datagrid('beginEdit', editIndexV);
	}
	
}
function removeitV(){
	if (editIndexV == undefined){return}
	$('#datagrid-searchOrg-list').datagrid('cancelEdit', editIndexV)
			.datagrid('deleteRow', editIndexV);
	editIndexV = undefined;
}
function acceptV(){
	if (endEditingV()){
		$('#datagrid-searchOrg-list').datagrid('acceptChanges');
	}
}
function rejectV(){
	$('#datagrid-searchOrg-list').datagrid('rejectChanges');
	editIndexV = undefined;
}

function addVendor(){
	var arr = handlVendor();
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-searchOrg-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingV()){
				$('#datagrid-searchOrg-list').datagrid('appendRow',{code:arr[i]});
				editIndexV = $('#datagrid-searchOrg-list').datagrid('getRows').length-1;
				$('#datagrid-searchOrg-list').datagrid('selectRow', editIndexV)
						.datagrid('beginEdit', editIndexV);
			}
		}
	}
}

function resetVendor(){
	//清除所有的行项目
	$('#datagrid-searchOrg-list').datagrid('loadData', { total: 0, rows: [] }); 
	editIndexV = undefined;
}

//加载input供应商内容（供应商）
function loadContent3(){
	var arr = handlInputVendor($('#copyVendorContentSOD').val());
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-searchOrg-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingV()){
				$('#datagrid-searchOrg-list').datagrid('appendRow',{code:arr[i]});
				editIndexV = $('#datagrid-searchOrg-list').datagrid('getRows').length-1;
				$('#datagrid-searchOrg-list').datagrid('selectRow', editIndexV)
						.datagrid('beginEdit', editIndexV);
			}
		}
	}
}



</script>
</body>
</html>