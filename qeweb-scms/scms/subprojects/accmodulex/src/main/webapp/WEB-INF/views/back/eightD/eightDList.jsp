<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title>8D整改管理</title>
	<link rel="stylesheet" type="text/css" href="../../themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="../../themes/icon.css">
	<link rel="stylesheet" type="text/css" href="../demo.css">
	<script type="text/javascript" src="../../jquery.min.js"></script>
	<script type="text/javascript" src="../../jquery.easyui.min.js"></script>
	<script type="text/javascript">
	
	function statusFmt(v,r,i){
		if(r.status == 0)
			return '待审核';
		else if(r.status == 1)
			return '已审核';
		return '待审核';
	}
	
	function publishStatusFmt(v,r,i){
		if(r.publishStatus == 0)
			return '待发布';
		else if(r.publishStatus == 1)
			return '已发布';
		return '待发布';
	}
	
	function reproveStatusFmt(v,r,i){
		if(r.reproveStatus == 0)
			return '待整改';
		else if(r.reproveStatus == 1)
			return '已整改';
		return '待整改';
	}
	function publish(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		if(selections[0]['publishStatus']!=0){
			$.messager.alert('提示','只能选择未发布的8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		$.messager.confirm('提示','确定发布该记录吗？',function(r){
			if(r){
				var url = '${ctx}/manager/qualityassurance/eightD/publishEightD/'+id;
				$.post(url,function(data){
					$.messager.alert('提示','发布成功','info');
					$('#datagrid-eightD-list').datagrid('reload');
				});
			}
		});
	}
	function approve(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		if(selections[0]['status']!=0){
			$.messager.alert('提示','只能选择未审核的8D整改','info');
			return false;
		}
		if(selections[0]['reproveStatus']!=1){
			$.messager.alert('提示','只能选择已整改的8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		$('#win-eightD-approve').window({
			iconCls:'icon-add',
			title:'新增8D整改'
		});
		/* $('#loginName').textbox('enable'); */
		$('#win-eightD-approve').form('clear');
		$('#win-eightD-approve').form('load','${ctx}/manager/qualityassurance/eightD/getRectification/'+id);
		/* $('#id').val(0); */
		$('#win-eightD-approve').window('open');
	}
	
	function addEightD(){
		$('#win-eightD-addoredit').window({
			iconCls:'icon-add',
			title:'新增8D整改'
		});
		/* $('#loginName').textbox('enable'); */
		$('#win-eightD-addoredit').form('clear');
		$('#win-eightD-addoredit').window('open');
		$('#submit11').show();
		$('#reset11').show();
	}
	
	function deleteEightD(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		var url = '${ctx}/manager/qualityassurance/eightD/deleteEightD/'+id;
		$.post(url,function(data){
			$.messager.alert('提示','删除成功','info');
			$('#datagrid-eightD-list').datagrid('reload');
		});
	}
	function rejectEightD(){
		$.messager.progress();
		url = '${ctx}/manager/qualityassurance/eightD/reject';
		sucMeg = '审核8D整改不通过！';
		$('#form-eightD-approve').form('submit',{
			ajax:true,
			url:url,
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.alert('提示',result.message,'info');
						$('#win-eightD-approve').window('close');
						$('#datagrid-eightD-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
	function approveEightD(){
		$.messager.progress();
		url = '${ctx}/manager/qualityassurance/eightD/approve';
		sucMeg = '审核8D整改成功！';
		$('#form-eightD-approve').form('submit',{
			ajax:true,
			url:url,
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){
						$.messager.alert('提示',result.message,'info');
						$('#win-eightD-approve').window('close');
						$('#datagrid-eightD-list').datagrid('reload');
					}else{
						$.messager.alert('提示',result.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
	
	function editEightD(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		if(selections[0]['publishStatus']!=0){
			$.messager.alert('提示','只能修改未发布的8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		var vId = selections[0]['vendor']['id'];
		var vName = selections[0]['vendor']['name'];
		var mId = selections[0]['material']['id'];
		var mName = selections[0]['material']['name'];
		$('#win-eightD-addoredit').window({
			iconCls:'icon-edit',
			title:'修改8D整改'
		});
		$('#win-eightD-addoredit').window('open');
		$('#win-eightD-addoredit').form('clear'); 
		$('#win-eightD-addoredit').form('load','${ctx}/manager/qualityassurance/eightD/getRectification/'+id);
		$("#mid").val(id);
		$("#vendorid").val(vId);
		$('#form-user-addoredit-parentId').textbox('setText',vName);
		$("#materialid").val(mId);
		$('#form-user-addoredit-parentId1').textbox('setText',mName);
		$('#submit11').show();
		$('#reset11').show();
	}
	
	function viewEightD1(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		var vId = selections[0]['vendor']['id'];
		var vName = selections[0]['vendor']['name'];
		var mId = selections[0]['material']['id'];
		var mName = selections[0]['material']['name'];
		var fileName = selections[0]['attachmentName'];
		$('#win-eightD-addoredit').window({
			iconCls:'icon-edit',
			title:'查看8D整改'
		});
		$('#win-eightD-addoredit').window('open');
		$('#win-eightD-addoredit').form('clear'); 
		$('#win-eightD-addoredit').form('load','${ctx}/manager/qualityassurance/eightD/getRectification/'+id);
		$("#vendorid").val(vId);
		$('#form-user-addoredit-parentId').textbox('setText',vName);
		$("#materialid").val(mId);
		$('#form-user-addoredit-parentId1').textbox('setText',mName);
		$('#submit11').hide();
		$('#reset11').hide();
		
	}
	
	function viewEightD(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		$('.viewReport').form('clear');
		$('.viewReport').form('load','${ctx}/manager/qualityassurance/eightD/getReport/'+id);
		$('#viewTable1').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/0', 
		});
		$('#viewTable2').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/1', 
		});
		$('#viewTable3').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/2', 
		});
		$('#viewTable4').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/3', 
		});
		$('#viewTable5').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/4', 
		});
		$('#win-eightD-view').window('open');
		
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
	function searchVendor() {
		var searchParamArray = $('#form2').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#blistssssss').datagrid('load', searchParams);
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
	
	function selectMaterial(){
		$('#dialog-reviews-material').window('open');
		$('#blistssssss1').datagrid({   
			url: ctx + '/manager/basedata/material',
			
	    	onDblClickCell: function(rowIndex){
	    		var selectionsb = $('#blistssssss1').datagrid('getSelections');
	    		if(selectionsb.length==0){
	    			$.messager.alert('提示','没有选择任何物料','info');
	    			return false;
	    		}
	    		if(selectionsb.length>1){
	    			$.messager.alert('提示','只能选取一个物料','info');
	    			return false;
	    		}
	    		$("#materialid").val(selectionsb[0]['id']);
	    		$('#form-user-addoredit-parentId1').textbox('setText',selectionsb[0]['name']);
	    		$('#dialog-reviews-material').window('close');
	    	}
		});
	}
	function search1() {
		var searchParamArray = $('#form3').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#blistssssss1').datagrid('load', searchParams);
	}
	function selectMaterial1(){
		var selectionsb = $('#blistssssss1').datagrid('getSelections');
		if(selectionsb.length==0){
			$.messager.alert('提示','没有选择任何物料','info');
			return false;
		}
		if(selectionsb.length>1){
			$.messager.alert('提示','只能选取一个物料','info');
			return false;
		}
		$("#materialid").val(selectionsb[0]['id']);
		$('#form-user-addoredit-parentId1').textbox('setText',selectionsb[0]['name']);
		$('#dialog-reviews-material').window('close');
	}
	
	function submitAddorEditEightD(){
		$.messager.progress();
		var url = '${ctx}/manager/qualityassurance/eightD/addEightD';
		var sucMeg = '添加8D整改成功！'; 
		if($('#mid').val()!=0 && $('#mid').val()!='0'){
			url = '${ctx}/manager/qualityassurance/eightD/updateEightD';
			sucMeg = '编辑8D整改成功！';
		}
		$('#form-eightD-addoredit').form('submit',{
			ajax:true,
			url:url,
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
					return false;
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示',result.message,'info');
					$('#win-eightD-addoredit').window('close');
					$('#datagrid-eightD-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.message,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
	
	function download(v,r,i){
		if(r.attachmentName==null){
			return "";
		}else{
			return '<a href="javascript:;" onclick="File.download(\''+r.attachmentPath+'\',\'\')">'+r.attachmentName+'</a>';
		}
	}
	
	function downloadReprove(v,r,i){
		if(r.reproveAttachmentName==null){
			return "";
		}else{
			return '<a href="javascript:;" onclick="File.download(\''+r.reproveAttachmentPath+'\',\'\')">'+r.reproveAttachmentName+'</a>';
		}
	}
	
	function searchAccountSetting() {
		debugger;
		var start=$('#startTime').datebox('getValue');
		var end=$('#endTime').datebox('getValue');
		if(start != "" && end !="" && end<=start){
			$.messager.alert('提示','开始时间不能大于结束时间','info');
			return false;
		}
		var searchParamArray = $('#form-eightD-search').serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#datagrid-eightD-list').datagrid('load', searchParams);
	}
	</script>
</head>

<body style="margin:0;padding:0;">

	<table id="datagrid-eightD-list" title="8D整改列表" class="easyui-datagrid"
		data-options="url:'${ctx}/manager/qualityassurance/eightD/${vendor }',method:'post',singleSelect:true,     
		fit:true,border:false,toolbar:'#eightDListToolbar',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'vendorCode',formatter:function(v,r,i){return r.vendor.code;}">供应商编码</th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料图号</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'happenPlace'">发生地</th>
		<th data-options="field:'malfunctionQty'">故障件数量</th>
		<th data-options="field:'recDescription'">现象描述</th>
		<th data-options="field:'publishStatus',formatter:publishStatusFmt">发布状态</th>
		<th data-options="field:'status',formatter:statusFmt">审核状态</th>
		<!-- <th data-options="field:'confirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}">确认状态</th>  -->
		<th data-options="field:'reproveStatus',formatter:reproveStatusFmt">整改状态</th>
		<th data-options="field:'approveAdvice'">审核意见</th>
		<th data-options="field:'happenTime'">事故时间</th> 
		<th data-options="field:'attachmentName',formatter:download">附件</th> 
		<th data-options="field:'reproveAttachmentName',formatter:downloadReprove">整改附件</th> 
		</tr></thead>
	</table>
	<div id="eightDListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="addEightD()">新增</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="editEightD()">修改</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="deleteEightD()">删除</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="viewEightD()">查看</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="publish()">发布</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="approve()">审核</a>
		</div>   
		<div>
			<form id="form-eightD-search" method="post">
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			物料图号：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
			发布状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus">
					<option value=""></option>
					<option value="0">待发布</option>
					<option value="1">已发布</option>
				</select>
			审核状态：<select class="easyui-combobox" name="search-EQ_status" 

data-options="editable:false">
					<option value=""></option>
					<option value="0">待审核</option>
					<option value="1">已审核</option>
				</select>
			整改状态：<select class="easyui-combobox" name="search-EQ_reproveStatus" 

data-options="editable:false">
					<option value=""></option>
					<option value="0">待整改</option>
					<option value="1">已整改</option>
				</select>
			事故时间： <input type="text" id="startTime" name="search-GT_happenTime" data-options="editable:false"  class="easyui-datebox" style="width:100px;">~  
					  <input type="text" id="endTime" name="search-LT_HappenTime" data-options="editable:false" class="easyui-datebox" style="width:100px;">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchAccountSetting()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-eightD-search').form('reset')">重置</a>
			</form>
		</div>
	</div> 
	
	<div id="win-eightD-addoredit" class="easyui-window" title="新增8D整改" style="width:900px;height:250px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-eightD-addoredit" method="post" enctype="multipart/form-data" >
				<input id="mid" name="mid" value="-1" type="hidden"/>
				<input type="hidden" id="vendorid" name="vendor.id"  value="" />
				<input type="hidden" id="materialid" name="material.id"  value="" />
				<input type="hidden" id="attachmentName" name="attachmentName" value=" " />	
				<input type="hidden" id="attachmentPath" name="attachmentPath" value=" " />
				<table style="padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>供应商:</td>
					<td>
						<input id="form-user-addoredit-parentId" name="vendor.name" class="easyui-textbox" style="width: 150px"data-options="required:true,readonly:true"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectVendor()">选择供应商</a>   
					</td>
					<td>事故时间:</td>
					<td>
						<input class="easyui-datetimebox" id="happenTime" name="happenTime" type="text" style="width:200px;" data-options="required:true,editable:false"/>
					</td>
				</tr>
				<tr>
					<td>物料:</td>
					<td>
						<input id="form-user-addoredit-parentId1" name="materialName" class="easyui-textbox" style="width: 150px"data-options="required:true,readonly:true"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectMaterial()">选&nbsp;择&nbsp;&nbsp;物&nbsp;料</a>   
					</td>
					<td>发生地:</td>
					<td>
						<input class="easyui-textbox" id="happenPlace" name="happenPlace" type="text" style="width:200px;" data-options="required:true" />
					</td>
				</tr>
				<tr>
					<td>故障件数量:</td>
					<td>
						<input class="easyui-numberbox" id="malfunctionQty" name="malfunctionQty" type="text" style="width:100px;" data-options="required:true" />
					</td>
					<td>现象描述:</td>
					<td>
						<textarea class="easyui-validatebox" style="width: 300px; height:60px;" id="recDescription" name="recDescription" data-options="required:true"></textarea>
					</td>
				</tr>
				<tr>
					<td>附件:</td>
					<td>
						<input type="file" class="easyui-validatebox input150" id="fujian" name="fujian" />
					</td>
					<td colspan="2">
						<a href="javascript:;" onclick="File.download($('#attachmentPath').val(),$('#attachmentName').val())">
						<input id="fileName" name="attachmentName" type="text" style="border: 0"/>
						</a>
					</td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" id="submit11" class="easyui-linkbutton" onclick="submitAddorEditEightD()">提交</a>
					<a href="javascript:;" id="reset11" class="easyui-linkbutton" onclick="$('#form-eightD-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div> 
	
	<div id="win-eightD-approve" class="easyui-window" title="审核8D整改" style="width:400px;height:250px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-eightD-approve" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>审核意见:</td><td><textarea class="easyui-validatebox" style="width: 300px; height:150px;" id="approveAdvice" name="approveAdvice" data-options="required:true"></textarea></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" id="submit11" class="easyui-linkbutton" onclick="approveEightD()">通过</a>
					<a href="javascript:;" id="reject11" class="easyui-linkbutton" onclick="rejectEightD()">驳回</a>
					<a href="javascript:;" id="reset11" class="easyui-linkbutton" onclick="$('#form-eightD-approve').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div> 
	
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
 	
 	<div id="win-eightD-view" class="easyui-window" title="查看8D整改" style="width:900px;height:250px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-tabs" >
			<div title="问题说明" >
				<form id="viewReportForm1" class="viewReport" method="post" >
					<table style="padding:5px;margin:auto;" cellpadding="5">
						<tr>
	                        <th align="right" nowrap="nowrap">发生地:</th>
	                        <td><input class="easyui-textbox" name="repHappenPlace" type="text" style="width:200px;" /></td>
	                    </tr>
						<tr>
	                        <th align="right" nowrap="nowrap">故障件数量:</th>
	                        <td><input class="easyui-numberbox" name="repMalfunctionQty" type="text" style="width:200px;" /></td>
	                    </tr>
						<tr>
	                        <th align="right" nowrap="nowrap">受影响的批次数量:</th>
	                        <td><input class="easyui-numberbox" name="affectedBatchQty" type="text" style="width:200px;" /></td>
	                    </tr>
						<tr>
	                        <th align="right" nowrap="nowrap">问题描述:</th>
	                        <td><textarea class="easyui-validatebox" style="width: 400px; height:60px;" name="reportDescription"></textarea></td>
	                    </tr>
					</table>
				</form>
			</div>
			<div title="向类似零件展开" >
		   		<table id="viewTable1" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit',
		   		singleSelect: true,method: 'post'">
	               <thead><tr>
		             	<th data-options="field:'material.c22'">物料编号/物料名称</th>  
		               <th data-options="field:'simIsProblem',width:100,
		              		formatter:function(value,row){
		              			   return row.simIsProblem=='1' ? '是':'否';
		                       },editor:{type:'combobox',
		                           options:{
		                               data:
		                               	[{'id':'1','text':'是'},
		                               	{'id':'2','text':'否'}],                 
		                               valueField:'id',
		                               textField:'text',
		                               required:true,
		                               editable:false 
	                               }
	                           }">是否存在问题</th>
		               <th data-options="field:'simRemark',editor:'textbox'">备注</th>
		          </tr></thead>
		   	</table>
			</div>
			<div title="临时围堵政策-立即的" >
		   		<table id="viewTable2" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit',
		   			singleSelect: true,method: 'post'">
	               <thead><tr>
	                 
		               <th data-options="field:'temConsiderMatter'">考虑事项</th>
		               <th data-options="field:'temQualifiedQty'">合格数</th>
		               <th data-options="field:'temUnqualifiedQty'">不合格数</th>
		               <th data-options="field:'temHandleWay'">不合格处置方式</th>
		          </tr></thead>
		          </table>
		          <form id="viewReportForm2" class="viewReport" method="post" >
					<table style="width:800px;height:auto;padding: 20px">
						<tr>
							<td style="height: 30px;">
						</tr>
						<tr>
	                        <td style="width: 500px;">首批交付合格部品如何特殊标识/标记？</td>
	                    </tr>
						<tr>
	                        <td><textarea class="easyui-validatebox" style="width: 500px; height:100px;" name="specialMark" data-options="required:true"></textarea></td>
	                    </tr>
					</table>
				</form>
			</div>
			<div title="原因分析" >
				<form id="viewReportForm3" class="viewReport" method="post" >
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
	                        <td><textarea class="easyui-validatebox" style="width: 600px; height:100px;" name="reason"></textarea></td>
	                    </tr>
					</table>
				</form>
			</div>
			<div title="根本原因确认" >
		   		<table id="viewTable3" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit',
		   			singleSelect: true,method: 'post'">
	               <thead><tr>
	               	   
		               <th data-options="field:'reaCategory'">类别</th>
		               <th data-options="field:'reaIsProblem',width:100,
		              		formatter:function(value,row){
		              			   return row.simIsProblem=='1' ? '是':'否';
		                       },editor:{type:'combobox',
		                           options:{
		                               data:
		                               	[{'id':'1','text':'是'},
		                               	{'id':'2','text':'否'}],                 
		                               valueField:'id',
		                               textField:'text',
		                               required:true,
		                               editable:false 
	                               }
	                           }">是否存在问题</th>
		               <th data-options="field:'reaRootCause'">问题产生的根本原因</th>
		               <th data-options="field:'reaConfirmedWay'">确认的方法、证据（可附后说明）</th>
		               <th data-options="field:'reaFinishTime'">完成时间</th>
		          </tr></thead>
		          </table>
			</div>
			<div title="制定永久措施并实施" >
		   		<table id="viewTable4" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options=" iconCls: 'icon-edit',
		   			singleSelect: true,method: 'post'">
	               <thead><tr>
	               	  
		               <th data-options="field:'makNumber'">序号</th>
		               <th data-options="field:'makRootCause'">问题产生的根本原因</th>
		               <th data-options="field:'makEvidence'">采取的永久措施及实施的证据（可附后说明）</th>
		               <th data-options="field:'makDutyDept'">责任部门</th>
		               <th data-options="field:'makFinishTime'">完成时间</th>
		          </tr></thead>
		   	</table>
			</div>
			<div title="永久措施效果验证" >
				<form id="viewReportForm4" class="viewReport" method="post" >
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
	                        <td><textarea class="easyui-validatebox" style="width: 600px; height:100px;" name="measureVerification"></textarea></td>
	                    </tr>
						
					</table>
				</form>
			</div>
			<div title="文件固化" >
				
		   		<table id="viewTable5" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit',
		   			singleSelect: true,method: 'post'">
	               <thead><tr>
	               	   
		               <th data-options="field:'filName'">须固化的文件</th>
		                <th data-options="field:'filYes',width:100,
		              		formatter:function(value,row){
		              			   return row.simIsProblem=='1' ? 'YES':'NO';
		                       },editor:{type:'combobox',
		                           options:{
		                               data:
		                               	[{'id':'1','text':'YES'},
		                               	{'id':'2','text':'NO'}],                 
		                               valueField:'id',
		                               textField:'text',
		                               required:true,
		                               editable:false 
	                               }
	                           }">YES/NO</th>
		               <th data-options="field:'filDept'">实施部门</th>
		               <th data-options="field:'filTime'">实施时间</th>
		               <th data-options="field:'filEvidence'">实施的证据</th>
		          </tr></thead>
		   	</table>
			</div>
		</div>
	</div>

<div id="dialog-reviews-material" class="easyui-dialog" title="选择物料带回" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 90%;height: 90%">
	<table id="blistssssss1" class="easyui-datagrid"	
		data-options="fit:true,method:'post',singleSelect:true,pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,toolbar:'#tt3'"
		>
		<thead><tr>
	        <th data-options="field:'id',checkbox:true">物料图号</th>
			<th data-options="field:'code'">物料图号</th>
			<th data-options="field:'name'">物料名称</th>
			<!-- <th data-options="field:'describe'">描述（技术状态描述）</th>
			<th data-options="field:'partsCode'">零部件编号</th>
			<th data-options="field:'partsName'">零部件类别</th>
			<th data-options="field:'picStatus'">图纸状态</th>
			<th data-options="field:'technician'">设计者</th>
			<th data-options="field:'abolished'">物料状态</th> -->
	      </tr></thead>
	</table>
	<div id="tt3">
	    <div>
	    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectMaterial1()">勾选带回物料</a>
	    </div>
	    <div>
	        <form id="form3" method="post">
	                              物料图号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
				物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
				<!-- 物料状态：<input type="text" name="search-EQ_abolished" class="easyui-textbox" style="width:80px;"/>
				图纸状态：<input id="aa" class="easyui-combobox" name="search-LIKE_picStatus"  style="width:80px;"/>
	 			零部件编号：<input type="text" name="search-LIKE_partsCode" class="easyui-textbox" style="width:80px;"/> -->
	    		 	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="search1()">查询</a>      
	    	  </form>
      	</div>
   	</div>
 </div>
</body>
</html>
