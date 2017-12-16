<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title>在线稽查不合格信息管理</title>
	<script type="text/javascript" src="${ctx}/static/script/qualityImprove/onlineInspection.js"></script> 
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid" class="easyui-datagrid"
		data-options="fit:true,url:'${ctx}/manager/qualityassurance/OnlineInspection/${vendor }',method:'post',singleSelect:false,   
		toolbar:'#materialInspectorListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		 <th data-options="field:'id',checkbox:true">ID</th>
		 <c:if test="${vendor == false }">
		 <th data-options="field:'manage',formatter:onlineInspection.manageFmt">操作</th>	
		</c:if>
		<th data-options="field:'startTime'">时间</th>
		<th data-options="field:'code'">考核单编号</th>
		<th data-options="field:'vendorba',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.name;}}">供应商名称</th>
		<th data-options="field:'materialcode',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.code;}}">故障件图号</th>
		<th data-options="field:'materialname',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.name;}}">故障件名称</th>
		<th data-options="field:'totalNumber'">数量</th>
		<th data-options="field:'describe'">不合格描述</th>
		<th data-options="field:'money'">考核金额（元）</th>
		<th data-options="field:'stages',formatter:function(v,r,i){if(r.stages==1){return '产品审核';} else if(r.stages==2){return '装配现场';} else if(r.stages==3){return '客户反馈';}  else if(r.stages==4){return '咸阳工厂';} }">发生地点</th>
		<th data-options="field:'qualityStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'qualityStatus',true);}">状态</th>
		</tr></thead>
	</table>
	<div id="materialInspectorListToolbar" style="padding:5px;">
		<div>
		<c:if test="${vendor == false }">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="onlineInspection.add()">新增</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-tab_edit',plain:true" onclick="onlineInspection.update()">修改</a>	
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="onlineInspection.imp()">数据导入</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="onlineInspection.release()">发布</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="onlineInspection.exportExcle()">导出</a>
			<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="onlineInspection.reportForms()">报表</a> -->
		</c:if>  		
		</div>
		<div>
			<form id="form-onlineInspection-search" method="post">
			供应商名称：<input type="text" name="search-LIKE_vendorBaseInfoEntity.name" class="easyui-textbox" style="width:150px;"/>
			物料图号：<input type="text" name="search-LIKE_materialEntity.code" class="easyui-textbox" style="width:150px;"/>
			物料名称：<input type="text" name="search-LIKE_materialEntity.name" class="easyui-textbox" style="width:150px;"/>
			状态：<select name="search-EQ_qualityStatus" data-options="editable:false" class="easyui-combobox" style="width:150px;">
                   	<option value=""></option>
                   	<option value="0">登记</option>
                   	<option value="1">发布</option>
                   </select>
			<br/><br/>
			时间：<input class="easyui-datebox" name="search-GTE_startTime" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
			<input class="easyui-datebox" name="search-LTE_startTime" data-options="showSeconds:true,editable:false" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="onlineInspection.search()">查询</a>
			</form>
		</div>
		
<div id="add" class="easyui-dialog" title="在线稽查不合格信息管理" data-options="modal:true,closed:true,buttons:'#dialog-adder-bbb'">
     <form id="addform"  method="post">
		<input type="hidden" id="id" name="id" value="">
			<table style="width: 100%;">
				<tr>
					<th>供应商编号：<input type="hidden" id="vendorId" name="vendorId" value=""></th>
					<th><input type="text" id="vendorCode" name="vendorCode" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th>供应商名称：</th>
					<th><input type="text" id="vendorName" name="vendorName" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="onlineInspection.add1()">选择供应商</a></th>
				</tr>
				<tr>
					<th>物料图号：<input type="hidden" id="materialId" name="materialId" value=""></th>
					<th><input type="text" id="materialCode" name="materialCode" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th>物料名称：</th>
					<th><input type="text" id="materialName" name="materialName" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="onlineInspection.add2()">选择物料</a></th>
				</tr>
				<tr >
					<th>开始时间：</th>
					<th><input class="easyui-datetimebox" name="startTime" data-options="editable:false,required:true" value="" style="width:150px"></th>
					<th>数量：</th>
					<th><input type="text" name="totalNumber" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th></th>
				</tr>
				<tr >
					<th>考核金额（元）：</th>
					<th><input type="text" name="money" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th>发生地点：</th>
					<th><select name="stages" class="easyui-combobox"  data-options="editable:false,required:true"  style="width:150px;">
		                   	<option value=""></option>
		                   	<option value="1">产品审核</option>
		                   	<option value="2">装配现场</option>
		                   	<option value="3">客户反馈</option>
		                   	<option value="4">咸阳工厂</option>
	                   </select></th>
					<th></th>
				</tr>
				<tr >
					<th>不合格描述：</th>
					<th colspan="4"><input type="text" name="describe" class="easyui-textbox" style="width:450px;"/></th>
				</tr>
			</table>
		</form>
		 <div id="dialog-adder-bbb">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="onlineInspection.addsumbil()">提交</a>
		 </div>
	</div>
	  <div id="winImport" class="easyui-dialog" title="数据导入" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true,buttons:'#dialog-adder-bb'" >
			<form id="formImport" method="post" enctype="multipart/form-data" class="baseform"> 
				    <div>
			          <label>文件：</label>
			          <input type=file id="file" name="planfiles" />
			        </div>
				    <div>
			          <label>模板：</label>
			          <a href="javascript:;" style="font-size: 20px;" onclick="File.download('WEB-INF/templates/onlineInspection.xls','onlineInspection')">数据导入.xls</a>
			        </div>
			</form>  
		    <div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="onlineInspection.impSumbim()">提交</a>
			</div>
	</div>
	
	
	
	
	<div id="add1" class="easyui-dialog" title="选择供应商"data-options="modal:true,closed:true,buttons:'#dialog-adder-bbb1'"  style="width: 50%;height: 50%;">
		<table id="datagrid1" class="easyui-datagrid"
				data-options="fit:true,method:'post',singleSelect:false,toolbar:'#m1232',
				pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
				>
					<thead>
						 <tr>
					         <th data-options="field:'id',checkbox:true">供应商代码</th>
					         <th data-options="field:'org.code'">供应商代码</th>
					         <th data-options="field:'name'">供应商名称</th>
					         <th data-options="field:'countryText'">国家</th>
		                     <th data-options="field:'provinceText'">省份</th>
					      </tr>
					</thead>
			</table>
			 <div id="m1232" style="padding:5px;">
			 	<div>
					<form id="formsearch1" method="post">
					 供应商代码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:150px;"/>
                                     供应商名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:150px;"/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="onlineInspection.search1()">查询</a>
					</form>
				</div>
			</div>
		<div id="dialog-adder-bbb1">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="onlineInspection.addsumbil1()">选择带回</a>
		 </div>
	</div>
	<div id="add2" class="easyui-dialog" title="选择物料"data-options="modal:true,closed:true,buttons:'#dialog-adder-bbb2'"  style="width: 50%;height: 50%;">
		<table id="datagrid2" class="easyui-datagrid"
				data-options="fit:true,method:'post',singleSelect:false,toolbar:'#m123',
				pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
				>
					<thead>
						 <tr>
					        <th data-options="field:'id',checkbox:true">物料ID</th>
					        <th data-options="field:'code'">物料编号</th>
					        <th data-options="field:'name'">物料名称</th>
					      </tr>
					</thead>
			</table>
			 <div id="m123" style="padding:5px;">
			 	<div>
					<form id="formsearch2" method="post">
					物料编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:150px;"/>
					物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:150px;"/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="onlineInspection.search2()">查询</a>
					</form>
				</div>
			</div>
		<div id="dialog-adder-bbb2">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="onlineInspection.addsumbil2()">选择带回</a>
		 </div>
	</div>
	
	
	<!-- 报表  -->
		<div id="oiReportForms" class="easyui-dialog" title="报表" style="width:80%;height:100%" data-options="iconCls:'icon-add',modal:true,closed:true">  
			<table id="datagrid-oiReportForms-list"  class="easyui-datagrid"
				   data-options="fit:true,
				   url:'${ctx}/manager/qualityassurance/OnlineInspection/${vendor}',method:'post',singleSelect:false,
				   toolbar:'#oiReportFormsToolbar',						
				   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
				
				<thead>
					<tr>
						<th data-options="field:'startTime'">时间</th>
						<th data-options="field:'code'">考核单编号</th>
						<th data-options="field:'vendorba',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.name;}}">供应商名称</th>
						<th data-options="field:'materialcode',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.code;}}">故障件图号</th>
						<th data-options="field:'materialname',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.name;}}">故障件名称</th>
						<th data-options="field:'totalNumber'">数量</th>
						<th data-options="field:'describe'">不合格描述</th>
						<th data-options="field:'money'">考核金额（元）</th>
						<th data-options="field:'stages',formatter:function(v,r,i){if(r.stages==1){return '产品审核';} else if(r.stages==2){return '装配现场';} else if(r.stages==3){return '客户反馈';} }">发生地点</th>
					</tr>
				</thead>
			</table>
			
			<div id="oiReportFormsToolbar" style="padding: 5px;">
				<div>
					<form id="form-oiReportForms-search" method="post">
						供应商名称：<input type="text" name="search-LIKE_vendorBaseInfoEntity.name" class="easyui-textbox" style="width: 150px;" /> 
						故障件图号：<input type="text" name="search-LIKE_materialEntity.code" class="easyui-textbox" style="width: 150px;" /> 
						故障号名称：<input type="text" name="search-LIKE_materialEntity.name" class="easyui-textbox" style="width: 150px;" /> 
				                开始时间：<input class="easyui-datebox" name="search-GTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
						<input class="easyui-datebox" name="search-LTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="onlineInspection.searchRf()">查询</a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-oiReportForms-search').form('reset')">重置</a>
					</form>
				</div>
			</div>
		</div>	
<!-- 结束 -->	
</body>
</html>