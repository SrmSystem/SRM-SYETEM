<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
<head>
	<title>售后质量信息管理</title>
	<script type="text/javascript" src="${ctx}/static/script/qualityImprove/postSalesService.js"></script> 
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid" class="easyui-datagrid"
		data-options="fit:true,url:'${ctx}/manager/qualityassurance/PostSalesService/${vendor }',method:'post',singleSelect:false,   
		toolbar:'#materialInspectorListToolbar',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList"
		>
		<thead><tr>
		 <th data-options="field:'id',checkbox:true">ID</th>	
		<!-- <th data-options="field:'startTime'">开始时间</th>
		<th data-options="field:'endTime'">结束时间</th> -->
		<th data-options="field:'month'">月份</th>
		<th data-options="field:'code'">报告单编号</th>
		<th data-options="field:'vcode',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.code;}}">供应商编号</th>
		<th data-options="field:'vendorba',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.name;}}">供应商名称</th>
		<th data-options="field:'materialcode',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.code;}}">祸首件图号</th>
		<th data-options="field:'materialname',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.name;}}">祸首件名称</th>
		<th data-options="field:'generatings'">生产厂</th>
		<th data-options="field:'hosts'">主机厂</th>
		<th data-options="field:'models'">车型</th>
		<th data-options="field:'totalNumber'">台数</th>
		<th data-options="field:'totalCounts'">总台数</th>
		<th data-options="field:'driving'">行驶里程</th>
		<th data-options="field:'repairTime'">维修时间</th>
		<th data-options="field:'describe'">标准故障</th>
		<th data-options="field:'agreement'">协议号</th>
		<th data-options="field:'totalmodel'">总成型号</th>
		<th data-options="field:'outcode'">出厂编号</th>
		<th data-options="field:'area'">片区</th>
		<th data-options="field:'station'">服务站</th>
		<th data-options="field:'fault'">故障简述</th>
		<th data-options="field:'reason'">故障原因与分析</th>
		<th data-options="field:'qualityStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'qualityStatus',true);}">状态</th>
		</tr></thead>
	</table>
	<div id="materialInspectorListToolbar" style="padding:5px;">
		<div>
		<c:if test="${vendor == false }">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="postSalesService.add()">新增</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-tab_edit',plain:true" onclick="postSalesService.update()">修改</a>	
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="postSalesService.imp()">数据导入</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="postSalesService.release()">发布</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="postSalesService.calcul()">计算</a>
			<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="postSalesService.reportForms()">报表</a> -->
		</c:if>  		
		</div>
		<div>
			<form id="form-postSalesService-search" method="post">
			供应商名称：<input type="text" name="search-LIKE_vendorBaseInfoEntity.name" class="easyui-textbox" style="width:150px;"/>
			物料图号：<input type="text" name="search-LIKE_materialEntity.code" class="easyui-textbox" style="width:150px;"/>
			物料名称：<input type="text" name="search-LIKE_materialEntity.name" class="easyui-textbox" style="width:150px;"/>
			状态：<select name="search-EQ_qualityStatus" data-options="editable:false" class="easyui-combobox" style="width:150px;">
                   	<option value=""></option>
                   	<option value="0">登记</option>
                   	<option value="1">发布</option>
                   </select>
			<br/><br/>
			月份：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:150px;"/>
			<!-- 开始时间：<input class="easyui-datebox" name="search-GTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
			<input class="easyui-datebox" name="search-LTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			结束时间：<input class="easyui-datebox" name="search-GTE_endTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
			<input class="easyui-datebox" name="search-LTE_endTime" data-options="showSeconds:true" value="" style="width:150px"> -->
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="postSalesService.search()">查询</a>
			</form>
		</div>
		
<div id="add" class="easyui-dialog" title="售后质量信息管理" data-options="modal:true,closed:true,buttons:'#dialog-adder-bbb'">
     <form id="addform"  method="post">
		<input type="hidden" id="id" name="id" value="">
			<table style="width: 100%;">
				<tr>
					<th>供应商编号：<input type="hidden" id="vendorId" name="vendorId" value=""></th>
					<th><input type="text" id="vendorCode" name="vendorCode" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th>供应商名称：</th>
					<th><input type="text" id="vendorName" name="vendorName" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="postSalesService.add1()">选择供应商</a></th>
				</tr>
				<tr>
					<th>物料图号：<input type="hidden" id="materialId" name="materialId" value=""></th>
					<th><input type="text" id="materialCode" name="materialCode" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th>物料名称：</th>
					<th><input type="text" id="materialName" name="materialName" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></th>
					<th><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="postSalesService.add2()">选择物料</a></th>
				</tr>
				<tr>
					<!-- <th>开始时间：</th>
					<th><input class="easyui-datetimebox" name="startTime" data-options="editable:false,required:true" value="" style="width:150px"></th> -->
					<th>时间：</th>
					<th><input class="easyui-datetimebox" name="endTime" data-options="editable:false,required:true" value="" style="width:150px"></th>
					<th>数量：</th>
					<th><input type="text" name="totalNumber" class="easyui-numberbox" style="width:150px;" data-options="required:true"/></th>
					<th></th>
				</tr>
				<tr >
					<th>车型：</th>
					<th><input type="text" name="models" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th>总数量：</th>
					<th><input type="text" name="totalCounts" class="easyui-numberbox" style="width:150px;" data-options="required:true"/></th>
					<th></th>
				</tr>
				<tr >
					<th>生产厂：</th>
					<th><input type="text" name="generatings" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th>主机厂：</th>
					<th><input type="text" name="hosts" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th></th>
				</tr>
				<tr >
					<th>行驶里程：</th>
					<th><input type="text" name="driving" class="easyui-numberbox" style="width:150px;" data-options="required:true"/></th>
					<th>维修时间：</th>
					<th><input class="easyui-datetimebox" name="repairTime" data-options="editable:false,required:true" value="" style="width:150px"></th>
					<th></th>
				</tr>
				<tr >
					<th>标准故障：</th>
					<th><input type="text" name="describe" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th>协议号：</th>
					<th><input type="text" name="agreement" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th></th>
				</tr>
				<tr >
					<th>总成型号：</th>
					<th><input type="text" name="totalmodel" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th>出厂编号：</th>
					<th><input type="text" name="outcode" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th></th>
				</tr>
				<tr >
					<th>片区：</th>
					<th><input type="text" name="area" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th>服务站：</th>
					<th><input type="text" name="station" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
					<th></th>
				</tr>
				<tr >
					<th>故障简述：</th>
					<th colspan="4"><input type="text" name="fault" class="easyui-textbox" style="width:450px;"/></th>
				</tr>
				<tr >
					<th>故障原因与分析：</th>
					<th colspan="4"><input type="text" name="reason" class="easyui-textbox" style="width:500px;"/></th>
				</tr>
			</table>
		</form>
		 <div id="dialog-adder-bbb">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="postSalesService.addsumbil()">提交</a>
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
			          <a href="javascript:;" style="font-size: 20px;" onclick="File.download('WEB-INF/templates/postSalesService.xls','postSalesService')">数据导入.xls</a>
			        </div>
			</form>  
		    <div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="postSalesService.impSumbim()">提交</a>
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
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="postSalesService.search1()">查询</a>
					</form>
				</div>
			</div>
		<div id="dialog-adder-bbb1">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="postSalesService.addsumbil1()">选择带回</a>
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
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="postSalesService.search2()">查询</a>
					</form>
				</div>
			</div>
		<div id="dialog-adder-bbb2">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="postSalesService.addsumbil2()">选择带回</a>
		 </div>
	</div>
	
	
	<!-- 报表  -->
		<div id="psReportForms" class="easyui-dialog" title="报表" style="width:80%;height:100%" data-options="iconCls:'icon-add',modal:true,closed:true">  
			<table id="datagrid-psReportForms-list"  class="easyui-datagrid"
				   data-options="fit:true,
				   url:'${ctx}/manager/qualityassurance/PostSalesService/${vendor}',method:'post',singleSelect:false,
				   toolbar:'#psReportFormsToolbar',						
				   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
				
				<thead>
					<tr>
						<th data-options="field:'id',checkbox:true"></th>
						<th data-options="field:'startTime'">开始时间</th>
						<th data-options="field:'endTime'">结束时间</th>
						<th data-options="field:'vcode',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.code;}}">供应商编号</th>
						<th data-options="field:'vendorba',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.name;}}">供应商名称</th>
						<th data-options="field:'materialcode',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.code;}}">祸首件图号</th>
						<th data-options="field:'materialname',formatter:function(v,r,i){if(r.materialEntity){return r.materialEntity.name;}}">祸首件名称</th>
						<th data-options="field:'totalNumber'">台数</th>
						<th data-options="field:'totalCounts'">售后总数量</th>
						<th data-options="field:'salePpm'">售后PPM</th>
					</tr>
				</thead>
			</table>
			
			<div id="psReportFormsToolbar" style="padding: 5px;">
				<div>
					<form id="form-psReportForms-search" method="post">
						供应商代码：<input type="text" name="search-LIKE_vendorBaseInfoEntity.code" class="easyui-textbox" style="width: 150px;" /> 
						供应商名称：<input type="text" name="search-LIKE_vendorBaseInfoEntity.name" class="easyui-textbox" style="width: 150px;" /> 
						祸首件图号：<input type="text" name="search-LIKE_materialEntity.code" class="easyui-textbox" style="width: 150px;" /></br>
				                祸首件名称：<input class="easyui-datebox" name="search-GTE_materialEntity.name" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
						<input class="easyui-datebox" name="search-LTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="postSalesService.searchRf()">查询</a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-psReportForms-search').form('reset')">重置</a>
					</form>
				</div>
			</div>
		</div>	
<!-- 结束 -->
	
</body>
</html>