<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
	<head>
		<title>质量改进（采购方）</title>
		<script type="text/javascript" src="${ctx}/static/script/qualityImprove/qualityImprove.js"></script>
		<script type="text/javascript">
			function dataStatusFmt(v,r,i){
				if(r.dataStatus == 0)
					return '待发布';
				else if(r.dataStatus == 1)
					return '已发布';
				else if(r.dataStatus == -1)
					return '关闭';
				return '待发布';
			}
			function improveStatusFmt(v,r,i){
				if(r.improveStatus == 0)
					return '等待整改';
				else if(r.improveStatus == 1)
					return '已整改';
				else if(r.improveStatus == -1)
					return '整改驳回';
				else if(r.improveStatus == -2)
					return '驳回反馈';
				return '等待整改';
			}
			
			function selectVendor(){
				$('#dialog-reviews-vendor').window('open');
				$('#blistssssss').datagrid({   
					url: ctx + '/manager/vendor/vendorInfor',
					
			    	onDblClickCell: function(rowIndex){
			    		var selectionsb = $('#blistssssss').datagrid('getSelections');
			    		if(selectionsb.length==0){
			    			$.messager.alert('提示','没有选择任何供应商','info');
			    			return false;
			    		}
			    		if(selectionsb.length>1){
			    			$.messager.alert('提示','只能选取一个供应商','info');
			    			return false;
			    		}
			    		$("#vendorid").val(selectionsb[0]['orgId']);
			    		$('#form-user-addoredit-parentId').textbox('setText',selectionsb[0]['name']);
			    		$('#dialog-reviews-vendor').window('close');
			    	}
				});
			}
			
			function selectVendor1(){
				var selectionsb = $('#blistssssss').datagrid('getSelections');
				if(selectionsb.length==0){
					$.messager.alert('提示','没有选择任何供应商','info');
					return false;
				}
				if(selectionsb.length>1){
					$.messager.alert('提示','只能选取一个供应商','info');
					return false;
				}
				$("#vendorid").val(selectionsb[0]['orgId']);
				$('#form-user-addoredit-parentId').textbox('setText',selectionsb[0]['name']);
				$('#dialog-reviews-vendor').window('close');
			}
			
			function searchVendor() {
				var searchParamArray = $('#form2').serializeArray();
				var searchParams = $.jqexer.formToJson(searchParamArray);
				$('#blistssssss').datagrid('load', searchParams);
			}
		</script>
	</head>

	<body style="margin: 0; padding: 0;">
		
		<table id="datagrid-qualityImprove-list" title="质量改进列表" class="easyui-datagrid"
			data-options="fit:true,
			url:'${ctx}/manager/qualityassurance/qualityImprove/${vendor}',method:'post',singleSelect:false,
			fit:true,border:false,toolbar:'#qualityImproveListToolbar',						
			pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<!-- <th data-options="field:'operate',formatter:qualityImprove.operateFmt">操作</th> -->
					<th data-options="field:'vendor.code'">供应商代码</th>
					<th data-options="field:'vendor.name'">供应商名称</th>
					<th data-options="field:'informFileName',formatter:qualityImprove.informFileDownLoad">质量改进通知</th>
					<th data-options="field:'improveFileName',formatter:qualityImprove.improveFileDownLoad">质量改进方案</th>
					<th data-options="field:'dataStatus',formatter:dataStatusFmt">发布状态</th>
					<th data-options="field:'improveStatus',formatter:improveStatusFmt">审核状态</th>
					<th data-options="field:'abolished',formatter:function(v,r,i){return StatusRender.render(v,'abolish',false);}">作废状态</th>  
					<th data-options="field:'createUserName'">创建人</th>
					<th data-options="field:'createTime'">创建时间</th>
					<!-- <th data-options="field:'reportUserName'">方案提交人</th>
					<th data-options="field:'reportTime'">方案提交时间</th> -->
				</tr>
			</thead>
		</table>
		
		<div id="qualityImproveListToolbar" style="padding: 5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="qualityImprove.addInform()">新增通知</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="qualityImprove.modInform()">修改通知</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="qualityImprove.publish()">发布</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="qualityImprove.close()">关闭</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="qualityImprove.abolishInform()">废除</a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="qualityImprove.resultHandle()">方案审核</a>
			</div>		
			<div>
				<form id="form-qualityImprove-search" method="post">
					供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width: 80px;" /> 
					发布状态：<select name="search-EQ_dataStatus"  class="easyui-combobox" 
data-options="editable:false" style="width:150px;">
	                   	<option value="">全部</option>
	                   	<option value="0">待发布</option>
	                   	<option value="1">已发布</option>
	                   	<option value="-1">关闭</option>
	                   </select>
					审核状态：<select name="search-EQ_improveStatus" class="easyui-combobox" 
data-options="editable:false" style="width:150px;">
	                   	<option value="">全部</option>
	                   	<option value="0">等待整改</option>
	                   	<option value="1">已整改</option>
	                   	<option value="-1">整改驳回</option>
                   		<option value="-2">驳回反馈</option>
	                   </select>
	              	   创建时间:<input type="text" name="search-GT_createTime" class="easyui-datebox" data-options="editable:false" style="width:100px;">~  
					  <input type="text" name="search-LT_createTime" data-options="editable:false" class="easyui-datebox" style="width:100px;">
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="qualityImprove.search()">查询</a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-qualityImprove-search').form('reset')">重置</a>
				</form>
			</div>
		</div>
				
				
	<!-- 新增通知 -->
		<div id="win-qualityImprove-addInform" class="easyui-dialog" title="新增通知" style="width:50%; height: 40%" data-options="modal:true,closed:true,buttons:'#dialog-adder-a'">
			<form id="form-qualityImprove-addInform" method="post" enctype="multipart/form-data" ">	
			<input id="mid" name="mid" value="-1" type="hidden"/>
			<input type="hidden" id="vendorid" name="vendor.id"  value="" />				
				<table>
					<td>供应商:</td>
					<td>
						<input id="form-user-addoredit-parentId" name="vendor.name" class="easyui-textbox" style="width: 150px"data-options="required:true,readonly:true"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectVendor()">选择供应商</a>   
					</td>
					<tr><td>改进通知附件</td><td><input type=file id="informFile" name="informfiles" /></td></tr>
				</table>
			</form>												
			 <div id="dialog-adder-a">
				<a href="javascript:;" class="easyui-linkbutton" onclick="qualityImprove.saveInform()">保存</a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-qualityImprove-addInform').form('reset')">重置</a>
			</div>
		</div>
	<!-- end -->
	

	
	
	
	<!-- 修改通知时选择的供应商代码模块  -->
		<div id="dialog-reviews-vendor" class="easyui-dialog" title="选择供应商带回" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 90%;height: 90%">
			<table id="blistssssss" class="easyui-datagrid"	
			data-options="fit:true,method:'post',singleSelect:true,pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,toolbar:'#tt2'" >
				<thead><tr>
			        <th data-options="field:'id',checkbox:true">供应商代码</th>
			        <th data-options="field:'code'">供应商代码</th>
			        <th data-options="field:'name'">供应商名称</th>
			        <th data-options="field:'property',formatter:function(v,r,i){if(r.property==1) return '国企'; if(r.property==2) return'独资';if(r.property==3) return '合资';if(r.property==4) return '民营';}">企业属性</th>
			        <th data-options="field:'countryText'">国家</th>
			        <th data-options="field:'provinceText'">省份</th>
			        <th data-options="field:'vendorPhase',formatter:function(v,r){if(r.vendorPhase) return r.vendorPhase.name; else ''}">阶段</th>
			      </tr></thead>
			</table>
			<div id="tt2">
			    <div>
			    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectVendor1()">勾选带回供应商</a>
			    </div>
			    <div>
			        <form id="form2" method="post">
			                               供应商代码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:150px;"/>
			                               供应商名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:150px;"/>
			    		 	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchVendor()">查询</a>      
			        </form>
		        </div>
	   		</div>
	 	</div>
	<!-- end -->
	
	
	
	<!-- 发布通知 -->
		<div id="win-qualityImprove-publishInform" class="easyui-dialog" title="发布通知" style="width:40%; height: 60%" data-options="modal:true,closed:true,buttons:'#dialog-adder-e'">
			<input id="publishInform" name="publishInform" value="" type="hidden" />
			<form id="form-qualityImprove-publishInform" method="post" enctype="multipart/form-data">	
				<table>
					<tr><td>供应商代码</td><td><input type="text" id="pVendCode" readonly="readonly"  class="easyui-textbox" style="width:150px;"/></td></tr>
					<tr><td>供应商名称</td><td><input type="text" id="pVendName" readonly="readonly" class="easyui-textbox" style="width:150px;"/></td></tr>
					<tr><td>改进通知附件</td><td><input type="text" id="pInformFile"  readonly="readonly" class="easyui-textbox" style="width:150px;"/></td></tr>					
				</table>					
			</form>	
			<div id="dialog-adder-e">
				<a href="javascript:;" class="easyui-linkbutton" onclick="qualityImprove.publishInform()">发布</a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="$('#win-qualityImprove-publishInform').window('close');">关闭</a>
			</div>
		</div>
	<!-- end -->
	
	
	
	
	
	
	<!-- 方案审核 -->
		<div id="win-qualityImprove-resHandle" class="easyui-dialog" title="方案审核" style="width:40%; height: 60%" data-options="modal:true,closed:true,buttons:'#dialog-adder-f'">
			<input id="resHandle" name="resHandle" value="" type="hidden" />
			<form id="form-qualityImprove-resHandle" method="post" enctype="multipart/form-data">
				<table>
					<tr><td>供应商代码</td><td><input type="text" id="aVendCode" readonly="readonly"  class="easyui-textbox" style="width:150px;"/></td></tr>
					<tr><td>供应商名称</td><td><input type="text" id="aVendName" readonly="readonly" class="easyui-textbox" style="width:150px;"/></td></tr>
					<tr><td>改进通知</td><td><input type="text" id="aInformFile"  readonly="readonly" class="easyui-textbox" style="width:150px;"/></td></tr>
					<tr><td>改进方案</td><td><input type="text" id="aImproveFile"  readonly="readonly" class="easyui-textbox" style="width:150px;"/>
				</table>		
			</form>			
			<div id="dialog-adder-f">
				<a href="javascript:;" class="easyui-linkbutton" onclick="qualityImprove.conImprove()">继续整改</a> 
				<a href="javascript:;" class="easyui-linkbutton" onclick="qualityImprove.finishImprove()">整改完成并关闭</a>
			</div>
			
		</div>
	<!-- end -->
	
	
	
	
<script type="text/javascript">
	function bringBack(){
		var selections = $('#datagrid-qualityImproveVendor-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$("#vendCode").textbox('setValue',selections[0].org.code);
		$("#vendName").textbox('setValue',selections[0]["name"]);
		$('#kk').window('close');
	}	
	
	function bringBack1(){
		var selections = $('#datagrid-qualityImproveVendor-list1').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		$("#modVendCode").textbox('setValue',selections[0].org.code);
		$("#modVendName").textbox('setValue',selections[0]["name"]);
		$('#kk1').window('close');
	}		
</script>	
				
	</body>
</html>

