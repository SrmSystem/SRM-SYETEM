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
	function reproveStatusFmt(v,r,i){
		if(r.reproveStatus == 0)
			return '待整改';
		else if(r.reproveStatus == 1)
			return '已整改';
		return '待整改';
	}
	
	function statusFmt(v,r,i){
		if(r.status == 0)
			return '待审核';
		else if(r.status == 1)
			return '已审核';
		return '待审核';
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
	
	function reprove(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		if(selections[0]['reproveStatus']==1){
			$.messager.alert('提示','只能选择待整改的8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		$('#win-eightDReport-add').window({
			iconCls:'icon-add',
			title:'填写8D报告'
		});
		$('#win-eightDReport-add').form('clear');
		$('#win-eightDReport-add').form('load','${ctx}/manager/qualityassurance/eightD/getReport/'+id);
		$('#datagrid-item-addoredit').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/0', 
		});
		$('#datagrid-item-addTem').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/1', 
		});
		$('#datagrid-item-addRea').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/2', 
		});
		$('#datagrid-item-addMak').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/3', 
		});
		$('#datagrid-item-addFil').datagrid({
			url:'${ctx}/manager/qualityassurance/eightD/getDetails/'+id+'/4', 
		});
		$('#eightDid').val(id);
		$('#win-eightDReport-add').window('open');
	}
	
	function save1(){
		$.messager.progress();
		var id = $('#eightDid').val();
		var url = '${ctx}/manager/qualityassurance/eightD/save1/'+id;
		$('#form-eightDReport-add1').form('submit',{
			ajax:true,
			url:url,
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
					$.messager.alert('提示','数据项请填写完整','error');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示',result.message,'info');
					//$('#win-eightD-addoredit').window('close');
					//$('#datagrid-eightD-list').datagrid('reload');
				}else{
					$.messager.alert('提示',result.message,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
	
	function save4(){
		$.messager.progress();
		var id = $('#eightDid').val();
		var url = '${ctx}/manager/qualityassurance/eightD/save4/'+id;
		$('#form-eightDReport-addReason').form('submit',{
			ajax:true,
			url:url,
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
					$.messager.alert('提示','数据项请填写完整','error');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示',result.message,'info');
				}else{
					$.messager.alert('提示',result.message,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
	
	function save7(){
		$.messager.progress();
		var id = $('#eightDid').val();
		var url = '${ctx}/manager/qualityassurance/eightD/save7/'+id;
		$('#form-eightDReport-add7').form('submit',{
			ajax:true,
			url:url,
			onSubmit:function(){
				var isValid = $(this).form('validate');
				if(!isValid){
					$.messager.progress('close');
					$.messager.alert('提示','数据项请填写完整','error');
				}
				return isValid;
			},
			success:function(data){
				$.messager.progress('close');
				try{
				var result = eval('('+data+')');
				if(result.success){
					$.messager.alert('提示',result.message,'info');
				}else{
					$.messager.alert('提示',result.message,'error');
				}
				}catch (e) {
					$.messager.alert('提示',data,'error');
				}
			}
			
		});
	}
	
	function save2(){
		$.messager.progress();
		/* var isValid = $('#form-item-addoredit').form('validate');
		if(!isValid){
			$.messager.progress('close');
			$.messager.alert('提示','数据项请填写完整','error');
			return false;
		} */
		RowEditor.accept('datagrid-item-addoredit');  
		var rows = $('#datagrid-item-addoredit').datagrid('getRows');
		if(rows.length == 0) {
			$.messager.progress('close');
			$.messager.alert('提示','明细数据项不能为空','error');
			return false;
		}
		for(i = 0;i < rows.length;i++) {
			 if(rows[i].material == null || rows[i].simIsProblem == null || rows[i].simRemark == null) {
				$.messager.progress('close');
				$.messager.alert('提示','明细数据项请填写完整','error');
				return false;
			} 
	    }
		var o =$('#datagrid-item-addoredit').datagrid('getData'); 
		var datas = JSON.stringify(o); 
		var id = $('#eightDid').val();
		$.messager.progress('close');
	  	$.ajax({
		 	url: ctx + '/manager/qualityassurance/eightD/save2/'+id,  
	        type: 'post',
	        data: "&datas=" + datas,   
	        dataType:"json",
	        success: function (data) {
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.alert('提示', data.message ,'info');
						//$('#form-request-addoredit').form('clear');   
						//$('#win-request-addoredit').window('close'); 
						//$dialog.dialog("close");
						//$('#datagrid-request-list').datagrid('reload'); 
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
	       	}
	      });
	}
	
	function save3(){
		$.messager.progress();
		/* var isValid = $('#form-item-addoredit').form('validate');
		if(!isValid){
			$.messager.progress('close');
			$.messager.alert('提示','数据项请填写完整','error');
			return false;
		} */
		RowEditor.accept('datagrid-item-addTem');  
		var rows = $('#datagrid-item-addTem').datagrid('getRows');
		if(rows.length == 0) {
			$.messager.progress('close');
			$.messager.alert('提示','明细数据项不能为空','error');
			return false;
		}
		for(i = 0;i < rows.length;i++) {
			 if(rows[i].temConsiderMatter == null || rows[i].temQualifiedQty == null || rows[i].temUnqualifiedQty == null|| rows[i].temHandleWay == null) {
				$.messager.progress('close');
				$.messager.alert('提示','明细数据项请填写完整','error');
				return false;
			} 
	    }
		var o =$('#datagrid-item-addTem').datagrid('getData'); 
		var datas = JSON.stringify(o); 
		var id = $('#eightDid').val();
		var specialMark = $('#specialMark').val();
		$.messager.progress('close');
	  	$.ajax({
		 	url: ctx + '/manager/qualityassurance/eightD/save3/'+id,  
	        type: 'post',
	        data: "&specialMark="+specialMark +"&datas=" + datas,   
	        dataType:"json",
	        success: function (data) {
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.alert('提示', data.message ,'info');
						//$('#form-request-addoredit').form('clear');   
						//$('#win-request-addoredit').window('close'); 
						//$dialog.dialog("close");
						//$('#datagrid-request-list').datagrid('reload'); 
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
	       	}
	      });
	}
	
	function save5(){
		$.messager.progress();
		RowEditor.accept('datagrid-item-addRea');  
		var rows = $('#datagrid-item-addRea').datagrid('getRows');
		if(rows.length == 0) {
			$.messager.progress('close');
			$.messager.alert('提示','明细数据项不能为空','error');
			return false;
		}
		for(i = 0;i < rows.length;i++) {
			 if(rows[i].reaCategory == null || rows[i].reaIsProblem == null || rows[i].reaRootCause == null || rows[i].reaConfirmedWay == null || rows[i].reaFinishTime == null) {
				$.messager.progress('close');
				$.messager.alert('提示','明细数据项请填写完整','error');
				return false;
			} 
	    }
		var o =$('#datagrid-item-addRea').datagrid('getData'); 
		var datas = JSON.stringify(o); 
		var id = $('#eightDid').val();
		$.messager.progress('close');
	  	$.ajax({
		 	url: ctx + '/manager/qualityassurance/eightD/save5/'+id,  
	        type: 'post',
	        data: "&datas=" + datas,   
	        dataType:"json",
	        success: function (data) {
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.alert('提示', data.message ,'info');
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
	       	}
	      });
	}
	
	function save6(){
		$.messager.progress();
		RowEditor.accept('datagrid-item-addMak');  
		var rows = $('#datagrid-item-addMak').datagrid('getRows');
		if(rows.length == 0) {
			$.messager.progress('close');
			$.messager.alert('提示','明细数据项不能为空','error');
			return false;
		}
		for(i = 0;i < rows.length;i++) {
			 if(rows[i].makNumber == null || rows[i].makRootCause == null || rows[i].makEvidence == null || rows[i].makDutyDept == null || rows[i].makFinishTime == null) {
				$.messager.progress('close');
				$.messager.alert('提示','明细数据项请填写完整','error');
				return false;
			} 
	    }
		var o =$('#datagrid-item-addMak').datagrid('getData'); 
		var datas = JSON.stringify(o); 
		var id = $('#eightDid').val();
		$.messager.progress('close');
	  	$.ajax({
		 	url: ctx + '/manager/qualityassurance/eightD/save6/'+id,  
	        type: 'post',
	        data: "&datas=" + datas,   
	        dataType:"json",
	        success: function (data) {
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.alert('提示', data.message ,'info');
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
	       	}
	      });
	}
	
	function save8(){
		$.messager.progress();
		RowEditor.accept('datagrid-item-addFil');  
		var rows = $('#datagrid-item-addFil').datagrid('getRows');
		if(rows.length == 0) {
			$.messager.progress('close');
			$.messager.alert('提示','明细数据项不能为空','error');
			return false;
		}
		for(i = 0;i < rows.length;i++) {
			 if(rows[i].filName == null || rows[i].filYes == null || rows[i].filDept == null || rows[i].filTime == null || rows[i].filEvidence == null) {
				$.messager.progress('close');
				$.messager.alert('提示','明细数据项请填写完整','error');
				return false;
			} 
	    }
		var o =$('#datagrid-item-addFil').datagrid('getData'); 
		var datas = JSON.stringify(o); 
		var id = $('#eightDid').val();
		$.messager.progress('close');
	  	$.ajax({
		 	url: ctx + '/manager/qualityassurance/eightD/save8/'+id,  
	        type: 'post',
	        data: "&datas=" + datas,   
	        dataType:"json",
	        success: function (data) {
				$.messager.progress('close');
				try{
					if(data.success){ 
						$.messager.alert('提示', data.message ,'info');
					}else{
						$.messager.alert('提示',data.message,'error');
					}
				}catch (e) {
					$.messager.alert('提示',e,'error'); 
				} 
	       	}
	      });
	}
	
	function submitReprove(){
		var id = $('#eightDid').val();
		$.messager.confirm('提示','确定提交吗？（请先确认是否每一项都已保存）',function(r){
			if(r){
				var url = '${ctx}/manager/qualityassurance/eightD/submitReprove/'+id;
				$.post(url,function(data){
					$.messager.alert('提示','提交整改成功','info');
					$('#win-eightDReport-add').window('close');
					$('#datagrid-eightD-list').datagrid('reload');
				});
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
	
	function uploadReprove(){
		var selections = $('#datagrid-eightD-list').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何8D整改记录','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一个8D整改','info');
			return false;
		}
		if(selections[0]['reproveStatus']!=0){
			$.messager.alert('提示','只能选择未整改的8D整改','info');
			return false;
		}
		var id = selections[0]['id'];
		$('#win-eightD-addoredit').window({
			iconCls:'icon-edit',
			title:'修改8D整改'
		});
		$('#win-eightD-addoredit').window('open');
		$('#win-eightD-addoredit').form('clear'); 
		$("#mid").val(id);
	}
	
	function submitAddorEditEightD(){
		$.messager.progress();
		var url = '${ctx}/manager/qualityassurance/eightD/uploadReprove';
		var sucMeg = '添加8D整改附件成功！'; 
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
		data-options="url:'${ctx}/manager/qualityassurance/eightD/${vendor }',method:'post',singleSelect:false,     
		fit:true,border:false,toolbar:'#eightDListToolbar',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
		<thead><tr>   
		<th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'vendorName',formatter:function(v,r,i){return r.vendor.name;}">供应商名称</th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}">物料图号</th>
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}">物料名称</th>
		<th data-options="field:'happenPlace'">发生地</th>
		<th data-options="field:'malfunctionQty'">故障件数量</th>
		<th data-options="field:'recDescription'">现象描述</th>
		<th data-options="field:'reproveStatus',formatter:reproveStatusFmt">整改状态</th>
		<th data-options="field:'status',formatter:statusFmt">审核状态</th>
		<th data-options="field:'approveAdvice'">审核意见</th>
		<th data-options="field:'happenTime'">事故时间</th> 
		<th data-options="field:'attachmentName',formatter:download">附件</th>  
		<th data-options="field:'reproveAttachmentName',formatter:downloadReprove">整改附件</th>  
		</tr></thead>
	</table>
	<div id="eightDListToolbar" style="padding:5px;">
		<div>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="viewEightD()">查看</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="reprove()">整改</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit',plain:true" onclick="uploadReprove()">上传整改附件</a>
		</div>   
		<div>
			<form id="form-eightD-search" method="post">
			供应商名称：<input type="text" name="search-LIKE_vendor.name" class="easyui-textbox" style="width:80px;"/>
			物料图号：<input type="text" name="search-LIKE_material.code" class="easyui-textbox" style="width:80px;"/>
			物料名称：<input type="text" name="search-LIKE_material.name" class="easyui-textbox" style="width:80px;"/>
			审核状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_status">
					<option value=""></option>
					<option value="0">待审核</option>
					<option value="1">已审核</option>
				</select>
			整改状态：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_reproveStatus">
					<option value=""></option>
					<option value="0">待整改</option>
					<option value="1">已整改</option>
				</select>
			事故时间： <input type="text" id="startTime" name="search-GT_happenTime" data-options="editable:false" class="easyui-datebox" style="width:100px;">~  
					  <input type="text" id="endTime" name="search-LT_HappenTime" data-options="editable:false" class="easyui-datebox" style="width:100px;">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchAccountSetting()">查询</a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-eightD-search').form('reset')">重置</a>
			</form>
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
		                <th data-options="field:'id',hidden:true"></th>
		             	<th data-options="field:'material.c22',width:400">物料编号/物料名称</th>  
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
		               <th data-options="field:'simRemark',width:200,editor:'textbox'">备注</th>
		          </tr></thead>
		   	</table>
			</div>
			<div title="临时围堵政策-立即的" >
		   		<table id="viewTable2" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit',
		   			singleSelect: true,method: 'post'">
	               <thead><tr>
	                   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'temConsiderMatter',width:150,editor:'textbox'">考虑事项</th>
		               <th data-options="field:'temQualifiedQty',width:80,editor:{type:'numberbox',options:{required:true}}">合格数</th>
		               <th data-options="field:'temUnqualifiedQty',width:80,editor:{type:'numberbox',options:{required:true}}">不合格数</th>
		               <th data-options="field:'temHandleWay',width:200,editor:'textbox'">不合格处置方式</th>
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
	               	   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'reaCategory',width:80,editor:'textbox'">类别</th>
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
		               <th data-options="field:'reaRootCause',width:150,editor:'textbox'">问题产生的根本原因</th>
		               <th data-options="field:'reaConfirmedWay',width:200,editor:'textbox'">确认的方法、证据（可附后说明）</th>
		               <th data-options="field:'reaFinishTime',width:100,editor:'datetimebox',required:true">完成时间</th>
		          </tr></thead>
		          </table>
			</div>
			<div title="制定永久措施并实施" >
		   		<table id="viewTable4" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options=" iconCls: 'icon-edit',
		   			singleSelect: true,method: 'post'">
	               <thead><tr>
	               	   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'makNumber',width:40,editor:'textbox'">序号</th>
		               <th data-options="field:'makRootCause',width:200,editor:'textbox'">问题产生的根本原因</th>
		               <th data-options="field:'makEvidence',width:250,editor:'textbox'">采取的永久措施及实施的证据（可附后说明）</th>
		               <th data-options="field:'makDutyDept',width:80,editor:'textbox'">责任部门</th>
		               <th data-options="field:'makFinishTime',width:100,editor:'datetimebox',required:true">完成时间</th>
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
	               	   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'filName',width:200,editor:'textbox'">须固化的文件</th>
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
		               <th data-options="field:'filDept',width:100,editor:'textbox'">实施部门</th>
		               <th data-options="field:'filTime',width:100,editor:'datetimebox',required:true">实施时间</th>
		               <th data-options="field:'filEvidence',width:200,editor:'textbox'">实施的证据</th>
		          </tr></thead>
		   	</table>
			</div>
		</div>
	</div> 
<!--  <div id="win-eightD-addoredit" class="easyui-window" title="查看8D整改" style="width:900px;height:250px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-eightD-addoredit" method="post" >
				<input id="id" name="id" value="-1" type="hidden"/>
				<input type="hidden" id="vendorid" name="vendor.id"  value="" />
				<input type="hidden" id="materialid" name="material.id"  value="" />
				<table style="padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>供应商:</td><td><input id="form-user-addoredit-parentId" name="vendor.name" class="easyui-textbox" style="width: 150px"data-options="required:true,readonly:true"></input>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectVendor()">选择供应商</a>   
					</td>
					<td>事故时间:</td><td><input class="easyui-datetimebox" id="happenTime" name="happenTime" type="text" style="width:200px;"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>物料:</td><td><input id="form-user-addoredit-parentId1" name="materialName" class="easyui-textbox" style="width: 150px"data-options="required:true,readonly:true"></input>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="selectMaterial()">选&nbsp;择&nbsp;&nbsp;物&nbsp;料</a>   
					</td>
					<td>发生地:</td><td><input class="easyui-textbox" id="happenPlace" name="happenPlace" type="text" style="width:200px;"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>故障件数量:</td><td><input class="easyui-numberbox" id="malfunctionQty" name="malfunctionQty" type="text" style="width:100px;"
						data-options="required:true"
					/>
					</td>
					<td>附件:</td><td><input class="easyui-textbox" id="materialName" name="materialName" type="text" style="width:200px;"
						data-options="required:true"
					/>
					</td>
				</tr>
				<tr>
					<td>现象描述:</td><td><textarea class="easyui-validatebox" style="width: 400px; height:60px;" id="recDescription" name="recDescription" data-options="required:true"></textarea></td>
				</tr>
				<tr>
					<td>问题处理要求:</td><td><input class="easyui-textarea" name="recDescription" type="text"
						data-options="required:true" style="width:200px;"
					/></td>
				</tr>
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" id="submit11" class="easyui-linkbutton" onclick="submitAddorEditEightD()">提交</a>
					<a href="javascript:;" id="reset11" class="easyui-linkbutton" onclick="$('#form-eightD-addoredit').form('reset')">重置</a>
				</div>
			</form>
		</div>
	</div>  -->

	<!-- 填写8D报告 --> 
	<div id="win-eightDReport-add" class="easyui-window" title="填写8D报告" style="width:850px;height:350px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<input type="hidden" name="eightDid" id="eightDid"/>
		<a href="javascript:;" class="easyui-linkbutton" onclick="submitReprove()">提交8D报告</a>
		<div class="easyui-tabs" >
			<div title="问题说明" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save1()">保存</a>
				<form id="form-eightDReport-add1" method="post" >
					<table style="padding:5px;margin:auto;" cellpadding="5">
						<tr>
	                        <th align="right" nowrap="nowrap">发生地:</th>
	                        <td><input class="easyui-textbox" id="repHappenPlace" name="repHappenPlace" type="text" style="width:200px;" data-options="required:true"/></td>
	                    </tr>
						<tr>
	                        <th align="right" nowrap="nowrap">故障件数量:</th>
	                        <td><input class="easyui-numberbox" id="repMalfunctionQty" name="repMalfunctionQty" type="text" style="width:200px;" data-options="required:true"/></td>
	                    </tr>
						<tr>
	                        <th align="right" nowrap="nowrap">受影响的批次数量:</th>
	                        <td><input class="easyui-numberbox" id="affectedBatchQty" name="affectedBatchQty" type="text" style="width:200px;" data-options="required:true"/></td>
	                    </tr>
						<tr>
	                        <th align="right" nowrap="nowrap">问题描述:</th>
	                        <td><textarea class="easyui-validatebox" style="width: 400px; height:60px;" id="reportDescription" name="reportDescription" data-options="required:true"></textarea></td>
	                    </tr>
					</table>
				</form>
			</div>
			<div title="向类似零件展开" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save2()">保存</a>
				<div id="tb" style="padding:5px;height:auto">
					<div style="margin-bottom:5px">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addoredit')"></a>
			        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addoredit')"></a>
					</div>
				</div>
		   		<table id="datagrid-item-addoredit" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit',
		   		singleSelect: true,toolbar: '#tb',method: 'post',onClickCell: RowEditor.onClickCell">
	               <thead><tr>
		                <th data-options="field:'id',hidden:true"></th>
		             	<th data-options="field:'material',width:'400px',formatter:function(v,r,i){return r.material.c22},
							editor:{
							type:'combogrid',
							options:{
							required:true,
							panelWidth:500,
							idField:'id',
							textField:'c22',
							method: 'post',
							pagination: true,            
							rownumbers: true,  
							url:'${ctx}/manager/order/goodsrequest/material',	
							columns:[[{field:'code',title:'物料编号',width:'130px'},
							{field:'name',title:'物料名称',width:'180px'},
							{field:'c22',title:'物料编号/物料名称',width:'180px'}
							]],
							keyHandler: {
							query:function(keyword){  
							      var queryParams = $(this).combogrid('grid').datagrid('options').queryParams;
							      queryParams['search-LIKE_code'] = keyword;
							      $(this).combogrid('setValue', keyword);
							    },
							 enter: function () {   
							    $(this).combogrid('grid').datagrid('reload');
							 // $(this).combogrid('grid').datagrid('getSelected') ;         
							   // $(this).combogrid('hidePanel');
							},
							up: function () {
							var selected = $(this).combogrid('grid').datagrid('getSelected');
							if (selected) {
							var index = $(this).combogrid('grid').datagrid('getRowIndex', selected);
							if (index > 0) { 
							$(this).combogrid('grid').datagrid('selectRow', index - 1);
							}
							} else {
							 	var rows = $(this).combogrid('grid').datagrid('getRows');
							 	$(this).combogrid('grid').datagrid('selectRow', rows.length - 1);
							}
							},
							down: function () { 
							var selected = $(this).combogrid('grid').datagrid('getSelected');
							if (selected) {
							var index = $(this).combogrid('grid').datagrid('getRowIndex', selected);
							if (index < $(this).combogrid('grid').datagrid('getData').rows.length - 1) {
							 $(this).combogrid('grid').datagrid('selectRow', index + 1);
							}
							} else {
							$(this).combogrid('grid').datagrid('selectRow', 0);
							}
							}
							}
							}
							}
							">物料编号/物料名称</th>  
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
		               <!-- <th data-options="field:'simIsProblem',width:200,editor:'textbox'">是否存在问题</th> -->
		               <th data-options="field:'simRemark',width:200,editor:'textbox'">备注</th>
		          </tr></thead>
		   	</table>
			</div>
			<div title="临时围堵政策-立即的" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save3()">保存</a>
				<div id="tbTem" style="padding:5px;height:auto">
					<div style="margin-bottom:5px">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addTem')"></a>
			        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addTem')"></a>
					</div>
				</div>
		   		<table id="datagrid-item-addTem" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
		   			singleSelect: true,toolbar: '#tbTem',method: 'post',onClickCell: RowEditor.onClickCell">
	               <thead><tr>
	                   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'temConsiderMatter',width:150,editor:'textbox'">考虑事项</th>
		               <th data-options="field:'temQualifiedQty',width:80,editor:{type:'numberbox',options:{required:true}}">合格数</th>
		               <th data-options="field:'temUnqualifiedQty',width:80,editor:{type:'numberbox',options:{required:true}}">不合格数</th>
		               <th data-options="field:'temHandleWay',width:200,editor:'textbox'">不合格处置方式</th>
		          </tr></thead>
		          </table>
		          <form id="form-eightDReport-add3" method="post" >
					<table style="width:800px;height:auto;padding: 20px">
						<tr>
							<td style="height: 30px;">
						</tr>
						<tr>
	                        <td style="width: 500px;">首批交付合格部品如何特殊标识/标记？</td>
	                    </tr>
						<tr>
	                        <td><textarea class="easyui-validatebox" style="width: 500px; height:100px;" id="specialMark" name="specialMark" data-options="required:true"></textarea></td>
	                    </tr>
					</table>
				</form>
			</div>
			<div title="原因分析" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save4()">保存</a>
				<form id="form-eightDReport-addReason" method="post" >
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
	                        <td><textarea class="easyui-validatebox" style="width: 600px; height:100px;" id="reason" name="reason" data-options="required:true"></textarea></td>
	                    </tr>
					</table>
				</form>
			</div>
			<div title="根本原因确认" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save5()">保存</a>
				<div id="tbRea" style="padding:5px;height:auto">
					<div style="margin-bottom:5px">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addRea')"></a>
			        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addRea')"></a>
					</div>
				</div>
		   		<table id="datagrid-item-addRea" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
		   			singleSelect: true,toolbar: '#tbRea',method: 'post',onClickCell: RowEditor.onClickCell">
	               <thead><tr>
	               	   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'reaCategory',width:80,editor:'textbox'">类别</th>
		               <!-- <th data-options="field:'reaIsProblem',width:80,editor:'textbox'">有没有问题</th> -->
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
		               <th data-options="field:'reaRootCause',width:150,editor:'textbox'">问题产生的根本原因</th>
		               <th data-options="field:'reaConfirmedWay',width:200,editor:'textbox'">确认的方法、证据（可附后说明）</th>
		               <th data-options="field:'reaFinishTime',width:100,editor:'datetimebox',required:true">完成时间</th>
		          </tr></thead>
		          </table>
			</div>
			<div title="制定永久措施并实施" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save6()">保存</a>
				<div id="tbMak" style="padding:5px;height:auto">
					<div style="margin-bottom:5px">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addMak')"></a>
			        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addMak')"></a>
					</div>
				</div>
		   		<table id="datagrid-item-addMak" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
		   			singleSelect: true,toolbar: '#tbMak',method: 'post',onClickCell: RowEditor.onClickCell">
	               <thead><tr>
	               	   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'makNumber',width:40,editor:'textbox'">序号</th>
		               <th data-options="field:'makRootCause',width:200,editor:'textbox'">问题产生的根本原因</th>
		               <th data-options="field:'makEvidence',width:250,editor:'textbox'">采取的永久措施及实施的证据（可附后说明）</th>
		               <th data-options="field:'makDutyDept',width:80,editor:'textbox'">责任部门</th>
		               <th data-options="field:'makFinishTime',width:100,editor:'datetimebox',required:true">完成时间</th>
		          </tr></thead>
		   	</table>
			</div>
			<div title="永久措施效果验证" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save7()">保存</a>
				<form id="form-eightDReport-add7" method="post" >
					<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
						<tr>
	                        <td><textarea class="easyui-validatebox" style="width: 600px; height:100px;" id="measureVerification" name="measureVerification" data-options="required:true"></textarea></td>
	                    </tr>
						
					</table>
				</form>
			</div>
			<div title="文件固化" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="save8()">保存</a>
				<div id="tbFil" style="padding:5px;height:auto">
					<div style="margin-bottom:5px">
						<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="RowEditor.append('datagrid-item-addFil')"></a>
			        	<a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="RowEditor.removeit('datagrid-item-addFil')"></a>
					</div>
				</div>
		   		<table id="datagrid-item-addFil" class="easyui-datagrid" style="width:800px;height:auto;padding: 20px" data-options="iconCls: 'icon-edit', 
		   			singleSelect: true,toolbar: '#tbFil',method: 'post',onClickCell: RowEditor.onClickCell">
	               <thead><tr>
	               	   <th data-options="field:'id',hidden:true"></th>
		               <th data-options="field:'filName',width:200,editor:'textbox'">须固化的文件</th>
		               <!-- <th data-options="field:'filYes',width:80,editor:'textbox'">YES/NO</th> -->
		               <th data-options="field:'filYes',width:100,
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
	                           }">是/否</th>
		               <th data-options="field:'filDept',width:100,editor:'textbox'">实施部门</th>
		               <th data-options="field:'filTime',width:100,editor:'datetimebox',required:true">实施时间</th>
		               <th data-options="field:'filEvidence',width:200,editor:'textbox'">实施的证据</th>
		          </tr></thead>
		   	</table>
			</div>
		</div>
	</div>
	
	<div id="win-eightD-addoredit" class="easyui-window" title="上传8D整改附件" style="width:900px;height:250px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-eightD-addoredit" method="post" enctype="multipart/form-data" >
				<input id="mid" name="mid" value="-1" type="hidden"/>
				<table style="padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td>附件:</td>
					<td>
						<input type="file" class="easyui-validatebox input150" id="fujian" name="fujian" />
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
	  
</body>
</html>
