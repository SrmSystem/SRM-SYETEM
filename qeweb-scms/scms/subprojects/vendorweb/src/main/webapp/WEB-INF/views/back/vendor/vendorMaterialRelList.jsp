<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>供货关系管理</title>
	<script type="text/javascript" src="${ctx}/static/script/vendor/vendorSupplyRel.js"></script>
	<script type="text/javascript">
function managerFmt(v, r, i) {
	var str = '';
	str = '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="VendorMaterialSupplyRel.viewDetails('
			+ r.id
			+ ');">详情</a>&nbsp;|&nbsp;';
	if (r.status == "1") {
		str += '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="VendorMaterialRel.stop('
				+ r.id
				+ ');">停止</a>&nbsp;|&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" style="color:gray;text-decoration: none" >恢复</a>'
	} else {
		str += '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" style="color:gray;text-decoration: none">停止</a>&nbsp;|&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="VendorMaterialRel.recovery('
				+ r.id + ');">恢复</a>'
	}
	return str;

}
</script>
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-materialRel-list" title="供货关系列表" class="easyui-datagrid"
		data-options="
		fit:true,
		url:'${ctx}/manager/vendor/materialRel',method:'post',singleSelect:false,
		toolbar:'#materialRelListToolbar',
		pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
		>
		<thead><tr>
		
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'manager',formatter:managerFmt">管理</th>
		<th data-options="field:'vendor',formatter:function(v,r,i){return v==null?'':v.org.code;}">供应商代码</th>
		<th data-options="field:'vendorName'">供应商名称</th>
		<th data-options="field:'material',formatter:function(v,r,i){return v==null?'':v.code;}">物料图号</th>
		<th data-options="field:'materialName'">物料名称</th>
		<th data-options="field:'status',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',true);}">供货状态</th>
		<th data-options="field:'createTime'">创建时间</th>
		<th data-options="field:'dataFrom'">数据来源</th>
		</tr></thead>
	</table>
	<div id="materialRelListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="VendorMaterialRel.add()">维护</a>
		<!--<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="VendorMaterialRel.del()">删除</a>-->
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="VendorMaterialRel.imp()">导入</a>
		<a href="javascript:;" class="easyui-linkbutton" id="matRelExpBtn" data-options="iconCls:'icon-download',plain:true" onclick="expVendorMaterialRel()">导出</a>
		</div>
		<div>
			<form id="form-materialRel-search" method="post">
			供应商编号：<input type="text" name="search-LIKE_vendor.code" class="easyui-textbox" style="width:80px;"/>
			供应商名称：<input type="text" name="search-LIKE_vendorName" class="easyui-textbox" style="width:80px;"/>
			物料图号：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_materialName" class="easyui-textbox" style="width:80px;"/>
			供货状态：<!-- <input type="text" name="search-EQ_status" class="easyui-textbox" style="width:80px;"/> -->
			<select class="easyui-combobox" name="search-EQ_status" style="width:80px;" data-options="editable:false">
						<option value="1">是</option>
						<option value="0">否</option>
					</select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="VendorMaterialRel.query()">查询</a>
			</form>
		</div>
	</div>
		<!-- 导入供货关系 -->
			
	<div id="win-materialRel-import" class="easyui-window" title="导入供货关系" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			
			<form id="form-materialRel-import" method="post" enctype="multipart/form-data"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/templates/VendorMaterialRel.xls">供货关系模版.xls</a>     --%>
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/templates/VendorMaterialRel.xls','VendorMaterialRel')">供货关系模版.xls</a>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="VendorMaterialRel.saveImp()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialRel-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
	
<!-- 供货关系新增 -->	
<div id="dialog-editor" class="easyui-dialog" data-options="
  modal:true,closed:true,title:'供货关系列表',iconCls:'icon-application',
  buttons:[{
    text:'提交',
    iconCls:'icon-save',
    handler:function(){VendorMaterialRel.submit()}
  }]
">
  <input type="hidden" itemId="phaseId" name="phaseId"/>
  <input type="hidden" itemId="vendorId" name="vendorId"/>
  <input type="hidden" itemId="orgId" name="orgId"/>
  <input type="hidden" itemId="changeType" name="changType"/>
  <input type="hidden" itemId="changeTypeText" name="changTypeText"/>
  <div class="easyui-layout" data-options="fit:true">
    <div class="easyui-panel" data-options="region:'center',border:false">
    <div class="easyui-layout" fit="true" border="false">
    <div class="easyui-panel" data-options="region:'north',border:false,height:250,title:'可选物料列表(右边V选择)',
    tools:[{iconCls:'icon-ok',title:'选择',handler:function(){VendorMaterialRel.Material.select()}}]">
      <table id="dialog-editor-mat-select" class="easyui-datagrid" data-options="
        fit:true,border:false,
        url:'${ctx}/manager/basedata/material',
        queryParams : {'search-EQ_abolished':0},
        pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],
        method:'post',toolbar:'#dialog-editor-mat-select-tt'
      ">
        <thead>
          <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'code'">物料编码</th>
            <th data-options="field:'name'">物料名称</th>
			<th data-options="field:'partsCode'">零部件编号</th>
			<th data-options="field:'partsName'">零部件类别</th>
			<th data-options="field:'picStatus'">图纸状态</th>
			<th data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">物料状态</th>
          </tr>
        </thead>
      </table>
    </div>
    <div class="easyui-panel" data-options="region:'center',title:'已选物料列表(点击行可编辑),右边按钮可移除已选项',
    tools:[{
        text:'移除',
        iconCls:'icon-remove',
        handler:function(){VendorMaterialRel.Material.removeSelected('#dialog-editor-mat-selected')}
      }],
    
    ">
      <table id="dialog-editor-mat-selected" class="easyui-datagrid" data-options="
      fit:true,border:false,
      idField:'id',
      onClickCell: RowEditor.onClickCell
      ">
        <thead>
          <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'code',width:160">物料编码</th>
            <th data-options="field:'name',width:200">物料名称</th>
            <th data-options="field:'unit',width:80,editor:'textbox'">单位</th>
            <th data-options="field:'dataFrom',width:100,editor:{
              type:'combobox',
              editable:false,
              options:{
                  valueField:'value',
                  textField:'text',
                  data : [
                  {value:'新产品开发',text:'新产品开发'},
                  {value:'二次布点',text:'二次布点'},
                  {value:'商改',text:'商改'},
                  ]
              }
            }">数据来源</th>
          </tr>
        </thead>
      </table>
    </div>
    </div>
    </div>
    <div class="easyui-panel" data-options="region:'east',width:500,border:false">
      <div class="easyui-layout" fit="true">
        <div class="easyui-panel" data-options="region:'north',height:250,title:'可选供应商(右边按钮可选择)',
        tools:[{iconCls:'icon-ok',title:'选择',handler:function(){VendorMaterialRel.Vendor.select()}}]
        ">
          <table id="dialog-editor-ven-select" class="easyui-datagrid" data-options="
            fit:true,border:false,
            url:'${ctx}/manager/vendor/vendorBaseInfo/getVendorList',
            queryParams : {'search-EQ_abolished':0},
            pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],
            toolbar:'#dialog-editor-ven-select-tt'
          ">
            <thead>
              <th data-options="field:'id',checkbox:true"></th>
              <th data-options="field:'orgCode',formatter:function(v,r,i){return r.org.code;}">编码</th>
              <th data-options="field:'orgName',formatter:function(v,r,i){return r.org.name;}">名称</th>
            </thead>
          
          </table>
        </div>
        <div class="easyui-panel" data-options="region:'center',title:'已选供应商(右边按钮可移除)',
        tools:[{
              iconCls:'icon-remove',
              handler:function(){VendorMaterialRel.Vendor.removeSelected('#dialog-editor-ven-selected')}
            }]
        ">
          <table id="dialog-editor-ven-selected" class="easyui-datagrid" data-options="
            fit:true,border:false,
            idField:'id'
           ">
           <thead>
            <tr>
            <th data-options="field:'id',checkbox:true"></th>
            <th data-options="field:'code',width:160">供应商代码</th>
            <th data-options="field:'name',width:200">供应商名称</th>
            </tr>
            </thead>
          </table>
        </div>
      
      </div>
    </div>
  </div>
</div>
<div id="dialog-editor-mat-select-tt">
   <form id="form-supply-material-select" method="post">
	<input type="hidden" name="search-EQ_abolished" value="0"/>
	物料图号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
	物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="VendorMaterialRel.Material.search()">查询</a>
   </form>
</div>
<div id="dialog-editor-ven-select-tt">
<form id="form-supply-vendor-select" method="post">
  编码:<input class="easyui-textbox" type="text" name="search-LIKE_org.code" style="width:80px;"/>
  名称:<input class="easyui-textbox" type="text" name="search-LIKE_org.name" style="width:80px;"/>
 <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="VendorAdmittance.Material.search()">查询</a>
</form>
</div>
	
	<!--<div id="win-materialRel-addoredit" class="easyui-window" title="新增供货关系" style="width:400px;height:200px" data-options="iconCls:'icon-add',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-materialRel-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>供应商名称:</td><td><input class="easyui-textbox" id="vendorName" name="vendorName" type="text"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>物料名称:</td><td><input class="easyui-textbox" name="materialName" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>供货状态:</td><td>
					<select class="easyui-combobox" name="status" style="width:100%;" data-options="required:true,editable:false">
						<option value='1'>是</option>
						<option value='0'>否</option>
					</select>
					</td>
				</tr>
				<tr>
					<td>数据来源:</td><td><input class="easyui-textbox" name="dataFrom" type="text"
						data-options="required:true"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="VendorMaterialRel.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialRel-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>-->
	
	
	<div id="win-materialSupplyRel" class="easyui-dialog" title="供货关系详情" style="width:900px;height:500px"
	data-options="modal:true,closed:true">
			<table id="datagrid-materialSupplyRel-list" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,
				toolbar:'#materialSupplyRelListToolbar',fit:true,
				pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
				>
					<thead><tr>
					<th data-options="field:'id',checkbox:true"></th>
					<th data-options="field:'materialRelId',hidden:true"></th>
					<!-- <th data-options="field:'vendor.code'">供应商代码</th>
					<th data-options="field:'vendorName'">供应商名称</th>
					<th data-options="field:'material.code'">物料图号</th>
					<th data-options="field:'materialName'">物料名称</th> -->
					<th data-options="field:'bussinessRangeName'">业务范围</th>
					<th data-options="field:'bussinessName'">业务类型</th>
					<th data-options="field:'brandName'">品牌</th>
					<th data-options="field:'productLineName'">产品线</th>
					<th data-options="field:'factoryName'">工厂</th>
					<th data-options="field:'ismain',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',false);}">是否主供</th>
					<th data-options="field:'supplyCoefficient'">供货系数</th>
					<th data-options="field:'createTime'">生效时间</th>
					</tr></thead>
			</table>
	<div id="materialSupplyRelListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="VendorMaterialSupplyRel.add()">新增</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="VendorMaterialSupplyRel.imp()">导入</a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="VendorMaterialSupplyRel.del()">删除</a>
		<a href="javascript:;" class="easyui-linkbutton" id="matSupRelExpBtn" data-options="iconCls:'icon-download',plain:true" onclick="exp()">导出</a>
		<form id="form-materialSupplyRel-export" method="post">
		</form>
		</div>
		</div>
		
		<!-- 导入供货系数 -->
			
	<div id="win-materialSupplyRel-import" class="easyui-window" title="导入供货关系" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			
			<form id="form-materialSupplyRel-import" method="post" enctype="multipart/form-data"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="file" name="planfiles" /><br>
					<%-- 模板：<a href="${ctx}/templates/VendorMaterialSupplyRel.xls">供货系数模版.xls</a>   --%> 
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/templates/VendorMaterialSupplyRel.xls','供货系数模版')">供货系数模版.xls</a>
					
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="VendorMaterialSupplyRel.saveImp()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialSupplyRel-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>
	
	
		<input id="hiddenMaterialRelId" name="materialRelId" type="hidden"/>
		<div id="win-materialSupplyRel-addoredit" class="easyui-dialog" title="新增供货关系详情" style="width:600px;height:350px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-materialSupplyRel-addoredit" method="post" >
				<input id="supplyId" name="id" value="-1" type="hidden"/>
				<input id="materialRelId" name="materialRelId" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				
				<tr>
					<td>业务范围:</td>
					<td>
						<input id="combobox_bRange" name="bussinessRangeId" class="easyui-combobox" />
					</td>
				</tr>
				<tr>
					<td>业务类型</td>
					<td>
						<input id="combobox_bType" name="bussinessId" class="easyui-combobox" />
					</td>
				</tr>
				<tr>
					<td>品牌</td>
					<td>
						<input id="combobox_brand" name="brandId" class="easyui-combobox" />
					</td>
				</tr>
				<tr>
					<td>产品线</td>
					<td>
						<input id="combobox_pLine" name="productLineId" class="easyui-combobox" />
					</td>
				</tr>
				<tr>
					<td>工厂</td>
					<td>
						<input id="combobox_factory" name="factoryId" class="easyui-combobox" />
					</td>
				</tr>
				<tr>
					<td>是否主供</td>
					<td>
						<input id="combobox_ismain" name="ismain" class="easyui-combobox" />
					</td>
				</tr>
				<tr>
					<td>供货系数</td>
					<td>
						<input name="supplyCoefficient" class="easyui-numberbox" precision="2" />
					</td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="VendorMaterialSupplyRel.submit()">提交</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-materialSupplyRel-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>
	</div>
<script type="text/javascript">
function exp(){
	var materialRelId = $('#hiddenMaterialRelId').val();
	
	$("#matSupRelExpBtn").linkbutton('disable');
	//导出
	$('#form-materialSupplyRel-export').form('submit',{
		url:'${ctx}/manager/vendor/materialSupplyRel/exportExcel/'+materialRelId, 
		success:function(data){
			$.messager.progress('close');
		}
	});
}
function expVendorMaterialRel(){
	$("#matRelExpBtn").linkbutton('disable');
	//导出
	$('#form-materialRel-search').form('submit',{
		url:'${ctx}/manager/vendor/materialRel/exportExcel', 
		success:function(data){
			$.messager.progress('close');
		}
	});
	
}
$(function() { 
	$('#combobox_factory').combobox({ 
        url: '${ctx}/manager/basedata/factory/getFactorySelect',
        editable:false,
        cache: false,
        valueField:'value',   
        textField:'text'
	});
	$('#combobox_ismain').combobox({ 
        url: '${ctx}/manager/database/statusDict/getStatusDictByType/yesOrNo',
        editable:false,
        cache: false,
        valueField:'value',   
        textField:'text'
	});
	  // 下拉框选择控件，下拉框的内容是动态查询数据库信息
	  $('#combobox_bRange').combobox({ 
	          url: '${ctx}/manager/basedata/bussinessRange/getBussinessRange/-1/0',
	          editable:false,
	          cache: false,
	          valueField:'value',   
	          textField:'text',
	          
	    onHidePanel: function(){
	        $("#combobox_bType").combobox("setValue",'');
	        $("#combobox_brand").combobox("setValue",'');
	        var id = $('#combobox_bRange').combobox('getValue');		
	      	//alert(id);
	      	
	      $.ajax({
	        type: "POST",
	        url: '${ctx}/manager/basedata/bussinessRange/getBussinessRange/' + id+'/1',
	        cache: false,
	        dataType : "json",
	        success: function(data){
	        	$("#combobox_bType").combobox("loadData",data);
	        }
	      }); 	
	    }
});    
	  
	  $('#combobox_bType').combobox({ 
	      editable:false,
	      cache: false,
	      panelHeight: '150',//自动高度适合
	      valueField:'value',   
	      textField:'text',
	      onHidePanel: function(){
		        $("#combobox_brand").combobox("setValue",'');//区级
		        var id = $('#combobox_bType').combobox('getValue');		
		      	//alert(id);
		      	
		      $.ajax({
		        type: "POST",
		        url: ctx+'/manager/basedata/bussinessRange/getBussinessRange/' + id+'/2',
		        cache: false,
		        dataType : "json",
		        success: function(data){
		        	$("#combobox_brand").combobox("loadData",data);
		        }
		      }); 	
		   }
	     });
	  $('#combobox_brand').combobox({ 
	      editable:false, //不可编辑状态
	      cache: false,
	      panelHeight: '150',//自动高度适合
	      valueField:'value',   
	      textField:'text',
	      onHidePanel: function(){
		        $("#combobox_pLine").combobox("setValue",'');//区级
		        var id = $('#combobox_brand').combobox('getValue');		
		      	//alert(id);
		      	
		      $.ajax({
		        type: "POST",
		        url: ctx+'/manager/basedata/bussinessRange/getBussinessRange/' + id+'/3',
		        cache: false,
		        dataType : "json",
		        success: function(data){
		        	$("#combobox_pLine").combobox("loadData",data);
		        }
		      }); 	
		   }
	  });
	  $('#combobox_pLine').combobox({ 
	      editable:false, //不可编辑状态
	      cache: false,
	      panelHeight: '150',//自动高度适合
	      valueField:'value',   
	      textField:'text',
	  });
	 
});
</script>
</body>
</html>