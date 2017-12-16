<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>


<html>
	<head>
		<title>零公里质检信息管理（采购方）</title>
		<script type="text/javascript" src="${ctx}/static/script/zeroKilometers/zeroKilometers.js"></script>
		<script type="text/javascript">
			function statusFmt(v,r,i){
				if(r.status == 0)
					return '待发布';
				else if(r.status == 1)
					return '已发布';
				else if(r.status == -1)
					return '关闭';
				return '待发布';
			}
		</script>
	</head>

	<body style="margin: 0; padding: 0;">
		
		<table id="datagrid-zeroKilometers-list" title="零公里质检列表" class="easyui-datagrid"
			data-options="fit:true,
			url:'${ctx}/manager/qualityassurance/zeroKilometers/${vendor }',method:'post',singleSelect:false,
			toolbar:'#zeroKilometersListToolbar',						
			pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<!-- <th data-options="field:'operate',formatter:zeroKilometers.operateFmt">操作</th> -->
					<!-- <th data-options="field:'startTime'">开始时间</th>
					<th data-options="field:'endTime'">结束时间</th> -->
					<th data-options="field:'month'">月份</th>
					<th data-options="field:'reportCode'">报告单编号</th>
					<th data-options="field:'vcode',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.code;}}">供应商编号</th>
					<th data-options="field:'vendorba',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.name;}}">供应商名称</th>
					<th data-options="field:'materialcode',formatter:function(v,r,i){if(r.material){return r.material.code;}}">祸首件图号</th>
					<th data-options="field:'materialname',formatter:function(v,r,i){if(r.material){return r.material.name;}}">祸首件名称</th>
					<th data-options="field:'factory'">生产厂</th>
					<th data-options="field:'motorFactory'">主机厂</th>
					<th data-options="field:'models'">车型</th>
					<th data-options="field:'counts'">台数</th>
					<th data-options="field:'totalCounts'">总台数</th>
					<th data-options="field:'mileage'">行驶里程</th>
					<th data-options="field:'maintenanceTime'">维修时间</th>
					<th data-options="field:'status',formatter:statusFmt">状态</th>
				</tr>
			</thead>
		</table>
		
		<div id="zeroKilometersListToolbar" style="padding: 5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="zeroKilometers.add()">新增</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-tab_edit',plain:true" onclick="zeroKilometers.update()">修改</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="zeroKilometers.import()">导入</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="zeroKilometers.publish()">发布</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="zeroKilometers.calcul()">计算</a>
				<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="zeroKilometers.reportForms()">报表</a> -->
			</div>		
			<div>
				<form id="form-zeroKilometers-search" method="post">
					供应商名称：<input type="text" name="search-LIKE_vendorBaseInfoEntity.name" class="easyui-textbox" style="width: 80px;" /> 
					祸首件名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width: 80px;" /> 
					发布状态：<select name="search-EQ_status" data-options="editable:false" class="easyui-combobox" style="width:150px;">
	                   	<option value="">-全部-</option>
	                   	<option value="0">待发布</option>
	                   	<option value="1">已发布</option>
	                   	<option value="-1">关闭</option>
	                   </select><br>
	          	         月份：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:150px;"/>
				     <!--    开始时间:<input id="sStart" name="search-GTE_startTime" class="easyui-datetimebox"   style="width:110px;"/>到
				        	 <input id="sEnd" name="search-LTE_startTime" class="easyui-datetimebox"    style="width:110px;"/>
		        	结束时间:<input id="eStart" name="search-GTE_endTime" class="easyui-datetimebox" style="width:110px;"/>到
				        	 <input id="eEnd" name="search-LTE_endTime" class="easyui-datetimebox"   style="width:110px;"/> -->
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="zeroKilometers.search()">查询</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-zeroKilometers-search').form('reset')">重置</a>
				</form>
			</div>
		</div>
				
				
	<!-- 新增 -->
		<div id="win-zeroKilometers-add" class="easyui-window" title="新增" style="width:850px; height: 400px" data-options="iconCls:'icon-add',modal:true,resizable:true,closed:true" >
			<form id="form-zeroKilometers-add" method="post" >
				<input type="hidden" id="id" name="id" value="0">					
				<table>
					<tr>
						<!-- <td>开始时间</td><td><input id="sTime" name="startTime" class="easyui-datetimebox"  data-options="required:true" style="width:150px;"/></td> -->
					    <td>时间</td><td><input id="eTime" name="endTime" class="easyui-datetimebox"  data-options="required:true,editable:false" style="width:150px;"/></td>
					    <td>维修时间</td><td><input id="aTime" name="maintenanceTime" class="easyui-datetimebox"  data-options="required:true,editable:false" style="width:150px;"/></td>
				    	<td>报告单编号</td><td><input class="easyui-textbox" id="aRepCode" name="reportCode" data-options="required:true" style="width: 150px;background-color: #F3F1F1"/></td>
					    <td></td>
				    </tr>
				    <tr>
						<td>总台数</td><td><input class="easyui-numberbox" id="aTotalCounts" name="totalCounts" data-options="required:true" style="width: 150px;background-color: #F3F1F1"/></td>
						<td>供应商编号</td><td><input type="hidden" id="vendorId" name="vendorId" value=""><input type="text" id="vendorCode" name="vendorCode" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></td>
						<td>供应商名称</td><td><input type="text" id="vendorName" name="vendorName" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></td>
						<td><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="zeroKilometers.add1()">选择供应商</a></td>
					</tr>
					<tr>
						<td>不合格台数</td><td><input class="easyui-numberbox" id="aCounts" name="counts" data-options="required:true"data-options="required:true" style="width: 150px;background-color: #F3F1F1"/></td>
						<td>物料图号</td><td><input type="hidden" id="materialId" name="materialId" value=""><input type="text" id="materialCode" name="materialCode" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></td>
						<td>物料名称</td><td><input type="text" id="materialName" name="materialName" data-options="disabled:true" class="easyui-textbox" style="width:150px;"/></td>
						<td><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="zeroKilometers.add2()">选择物料</a></th>
					</tr>
			     	<tr>
			     		<td>故障原因与分析</td><td><input class="easyui-textbox" id="aCauseAndAnalysis" name="causeAndAnalysis" style="width: 150px;background-color: #F3F1F1"/></td>
					    <td>主机厂</td><td><input id="motorFactory" class="easyui-textbox" name="motorFactory" style="width: 150px;background-color: #F3F1F1"/></td>
					    <td>车型</td><td><input class="easyui-textbox" id="models" name="models" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td></td>
				    </tr> 
				    <tr>
				    	<td>标准故障</td><td><input class="easyui-textbox" id="aStandardFault" name="standardFault" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td>故障简述</td><td><input class="easyui-textbox" id="aFaultDescription" name="faultDescription" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td>生产厂</td><td><input id="factory" class="easyui-textbox" name="factory" style="width: 150px;background-color: #F3F1F1"/></td>
				   		<td></td>
				    </tr>
				    <tr>
				    	<td>行驶里程</td><td><input class="easyui-numberbox" id="aMileage" name="mileage" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td>协议号</td><td><input class="easyui-textbox" id="aAgreementNo" name="agreementNo" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td>总成型号</td><td><input class="easyui-textbox" id="aAssemblyModel" name="assemblyModel" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td></td>
				    </tr>
				    <tr>
				    	<td>出厂编号</td><td><input class="easyui-textbox" id="aMileage" name="appearanceNumber" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td>片区</td><td><input class="easyui-textbox" id="aAgreementNo" name="area" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td>服务站</td><td><input class="easyui-textbox" id="aAssemblyModel" name="serviceStation" style="width: 150px;background-color: #F3F1F1"/></td>
				    	<td></td>
				    </tr>
				</table>
															
				<div style="text-align: center; padding: 5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="zeroKilometers.saveAdd()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#win-zeroKilometers-add').window('close')">关闭</a>
				</div>
			</form>
		</div>
	<!-- end -->
	
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
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="zeroKilometers.search1()">查询</a>
					</form>
				</div>
			</div>
		<div id="dialog-adder-bbb1">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="zeroKilometers.addsumbil1()">选择带回</a>
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
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="zeroKilometers.search2()">查询</a>
					</form>
				</div>
			</div>
		<div id="dialog-adder-bbb2">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="zeroKilometers.addsumbil2()">选择带回</a>
		 </div>
	</div>
	
<!-- 导入 -->
			
	<div id="win-impzk-import" class="easyui-window" title="导入" style="width:400px;height:250px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true" >
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-impzk-import" method="post" enctype="multipart/form-data"> 
				<div style="margin-bottom:20px">
					文件：<input type=file id="impFile" name="impFile"  /><br>	
					模板：<a href="javascript:;" onclick="File.download('WEB-INF/templates/ZeroKilometers.xls','ZeroKilometers')">模版.xls</a>				
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="zeroKilometers.saveImport()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-impzk-import').form('reset')">重置</a>
				</div>
			</form>  
		</div>
	</div>	
	
		
		<!--报表  -->
		<div id="zkReportForms" class="easyui-dialog" title="报表" style="width:80%;height:100%" data-options="iconCls:'icon-add',modal:true,closed:true">  
			<table id="datagrid-zkReportForms-list" title="零公里质检列表" class="easyui-datagrid"
				data-options="fit:true,
				url:'${ctx}/manager/qualityassurance/zeroKilometers/${vendor }',method:'post',singleSelect:false,
				toolbar:'#zkReportForms-Toolbar',						
				pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
				<thead>
					<tr>
						<th data-options="field:'id',checkbox:true"></th>
						<!-- <th data-options="field:'operate',formatter:zeroKilometers.operateFmt">操作</th> -->
						<th data-options="field:'startTime'">开始时间</th>
						<th data-options="field:'endTime'">结束时间</th>
						<th data-options="field:'reportCode'">报告单编号</th>
						<th data-options="field:'vcode',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.code;}}">供应商编号</th>
						<th data-options="field:'vendorba',formatter:function(v,r,i){if(r.vendorBaseInfoEntity){return r.vendorBaseInfoEntity.name;}}">供应商名称</th>
						<th data-options="field:'materialcode',formatter:function(v,r,i){if(r.material){return r.material.code;}}">祸首件图号</th>
						<th data-options="field:'materialname',formatter:function(v,r,i){if(r.material){return r.material.name;}}">祸首件名称</th>
						<th data-options="field:'models'">车型</th>
						<th data-options="field:'counts'">台数</th>
						<th data-options="field:'totalCount'">入库总数</th>
						<th data-options="field:'zeroPPM'">零公里PPM</th>
					</tr>
				</thead>
			</table>
			
			<div id="zkReportForms-Toolbar" style="padding: 5px;">
				<div>
					<form id="form-zkReportForms-search" method="post">
						供应商名称：<input type="text" name="search-LIKE_vendorName" class="easyui-textbox" style="width: 80px;" /> 
						祸首件图号：<input type="text" name="search-LIKE_firstPictureCode" class="easyui-textbox" style="width: 80px;" /> 
						祸首件名称：<input type="text" name="search-LIKE_firstPictureName" class="easyui-textbox" style="width: 80px;" /></br>
				        开始时间:<input id="rfsStart" name="search-GTE_startTime" class="easyui-datetimebox"    style="width:110px;"/>到
				        	 <input id="rfsEnd" name="search-LTE_startTime" class="easyui-datetimebox"    style="width:110px;"/>
		        	结束时间:<input id="rfeStart" name="search-GTE_endTime" class="easyui-datetimebox"    style="width:110px;"/>到
				        	 <input id="rfeEnd" name="search-LTE_endTime" class="easyui-datetimebox"    style="width:110px;"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="zeroKilometers.rfSearch()">查询</a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-zkReportForms-search').form('reset')">重置</a>
					</form>
				</div>
			</div>
		</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
<script type="text/javascript">
		
	$(function() { 
		$('#combobox_status').combobox({ 
	        url: '${ctx}/manager/database/statusDict/getStatusByStatusTypeCombobox/qualityStatus',
	        editable:false,
	        cache: false,
	        valueField:'id',   
	        textField:'text'
		});
	});

	
	function bringBack(){
		var selections = $('#datagrid-zeroKilometersVendor-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$("#aVendCode").textbox("setValue",selections[0].org.code);
		$("#aVendName").textbox("setValue",selections[0]["name"]);
/* 		$("#aVendCode").val(selections[0]["code"]);
		$("#aVendName").val(selections[0]["name"]); */
		$('#kk1').window('close');
	}	
	function mbringBack(){
		var selections = $('#datagrid-mzeroKilometersVendor-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$("#maVendCode").textbox("setValue",selections[0].org.code);
		$("#maVendName").textbox("setValue",selections[0]["name"]);
/* 		$("#aVendCode").val(selections[0]["code"]);
		$("#aVendName").val(selections[0]["name"]); */
		$('#mkk1').window('close');
	}	
	
	function bringBack2(){
		var selections = $('#datagrid-zeroKilometersVendor2-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		/* $("#aFirstPictureCode").val(selections[0]["code"]);
		$("#aFirstPictureName").val(selections[0]["name"]); */
		$("#aFirstPictureCode").textbox("setValue",selections[0]["code"]);
		$("#aFirstPictureName").textbox("setValue",selections[0]["name"]);
		$('#kk2').window('close');
	}	
	function mbringBack2(){
		var selections = $('#datagrid-mzeroKilometersVendor2-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		/* $("#aFirstPictureCode").val(selections[0]["code"]);
		$("#aFirstPictureName").val(selections[0]["name"]); */
		$("#maFirstPictureCode").textbox("setValue",selections[0]["code"]);
		$("#maFirstPictureName").textbox("setValue",selections[0]["name"]);
		$('#mkk2').window('close');
	}	
</script>	
				
	</body>
</html>

