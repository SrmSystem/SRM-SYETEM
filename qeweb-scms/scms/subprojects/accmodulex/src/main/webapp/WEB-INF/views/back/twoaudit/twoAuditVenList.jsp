<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
	<head>
		<title>二方审核管理（供方）</title>
		<script type="text/javascript" src="${ctx}/static/script/twoaudit/twoAudit.js"></script>
	</head>

	<body style="margin: 0; padding: 0;">
<table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/qualityassurance/twoAudit/correctionList2',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="3%" data-options="field:'id',checkbox:true">ID</th>
        <th width="10%"data-options="field:'workOrder'">工单号</th>
        <th width="9%" data-options="field:'vendorCode',formatter:function(v,r){if(r.org) return r.org.code; else ''}">供应商编码</th>
        <th width="9%" data-options="field:'vendorName',formatter:function(v,r){if(r.org) return r.org.name; else ''}">供应商名称</th>
        <th width="8%" data-options="field:'correctionStatus',formatter:twoAudit.correctiveStatusFmt">整改状态</th>
        <th width="10%" data-options="field:'correctionContent'">审核计划内容</th>
        <th width="9%" data-options="field:'planFileName',formatter:twoAudit.planDownLoad">审核计划附件</th>
        <th width="9%" data-options="field:'solutionContent'">最新提交内容</th>
        <th width="5%" data-options="field:'fileUrl',formatter:twoAudit.fileUrl">最新提交附件</th>
        <th width="8%" data-options="field:'endStatus',formatter:twoAudit.endStatus">结案状态</th>
        <th width="11%" data-options="field:'correctionEndContent'">计划结案内容</th>
        <th width="11%" data-options="field:'reportFilePath',formatter:twoAudit.reportFilePath">计划结案附件</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
  	  <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="twoAudit.vendorsolution()">方案提交</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-lock',plain:true" onclick="twoAudit.lookSolution()">查看整改过程</a>  
    <div>
      <form id="form" method="post">
                <table>
		      		<tr>
		      			<td>供应商编码:</td>
		      			<td><input class="easyui-textbox" name="search-LIKE_org.code" type="text" style="width:150px;"/></td>
		      			<td>供应商名称:</td>
		      			<td><input class="easyui-textbox" name="search-LIKE_org.name" type="text"  style="width:150px;"/></td>
		      			<td>整改状态:</td>
		      			<td>
		      			<select name="search-EQ_correctionStatus" data-options="editable:false" class="easyui-combobox" style="width:150px;">
                         	<option value=""></option>
                         	<option value="-1">未发布</option>
                         	<option value="0">等待提交</option>
                         	<option value="1">等待审核</option>
                         	<option value="2">审核通过</option>
                         	<option value="3">审核不通过</option>
                         	<option value="4">整改结束</option>
                         </select> 
		      			</td>
		      			<td>整改结案状态:</td>
		      			<td>
		      			<select name="search-EQ_endStatus" data-options="editable:false" class="easyui-combobox" style="width:150px;">
                         	<option value=""></option>
                         	<option value="0">未结案</option>
                         	<option value="1">结案</option>
                         </select>
		      			</td>
		      			<td rowspan="2"><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="twoAudit.search()">查询</a><a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a></td>
		      		</tr>
		      	</table> 
      </form>
    </div>
  </div>
<form id="form-export">
</form>
 <div id="win-import" title="提交 方案" data-options="iconCls:'icon-disk_upload',modal:true,closed:true,buttons:'#dialog-adder-bb'" >
	<form id="form-importww" method="post"  enctype="multipart/form-data"> 
		<input id="uid" name="uid" type="hidden"  value="" />
	   	<table>
			<tr>
				<td>整改要求:</td>
				<td height="100px" colspan="3"><input class="easyui-textbox" id="correctionContent" name="correctionContent" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true,disabled:true" value=""/></td>
			</tr>
			<tr>
				<td>整改方案:</td>
				<td height="100px" colspan="3"><input class="easyui-textbox" id="solutionContent" name="solutionContent" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true" value=""/></td>
			</tr>
			<tr>
				<td>整改方案附件:</td>
				<td id="fujian" height="20px" colspan="3"><input type=file id="file" name="planfiles" /></td>
			</tr>
		</table>
	</form>  
	 <div id="dialog-adder-bb">
		<a href="javascript:;"  data-options="iconCls:'icon-ok'" class="easyui-linkbutton" onclick="solutionSubmits()">提交</a>
	</div>
 </div>
 <div id="win-import2" title="方案审核" style="width:70%;height:70%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true">   
	   <table id="datagrid2"
					data-options="method:'post',singleSelect:true,fit:true,
					pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
					>
						<thead>
							 <tr>
						        <th width="15%" data-options="field:'id',checkbox:true">整改方案ID</th>
						        <th width="10%" data-options="field:'createTime'">审核时间</th>
						        <th width="10%" data-options="field:'createUserName'">审核人</th>
						        <th width="10%" data-options="field:'auditStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'audit',true);}">审核状态</th>
						        <th width="20%" data-options="field:'auditReason'">审核原因</th>
						        <th width="20%" data-options="field:'solutionContent'">整改方案</th>
						        <th width="10%" data-options="field:'fileUrl',formatter:function(v,r,i) {
						        	if(r.fileUrl == null || r.fileUrl == '')
						        		return '';
						        	return '<a href=javascript:; onclick=File.download(\''+ r.fileUrl+'\',\'附件\')>下载</a>';
						        }">附件</th>
						      </tr>
						</thead>
				</table>  
 </div>
<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		})
  function solutionSubmits(){
	$.messager.progress();
	$('#form-importww').form('submit',{
		ajax:true,
		iframe: true,    
		url:ctx+'/manager/qualityassurance/twoAudit/filesUpload', 
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
				$.messager.alert('提示',"提交成功,请等待采购方确认！",'info');
				$('#win-import').window('close');
				$('#datagrid').datagrid('reload');
			}else{
				$.messager.alert('提示',result.msg  ,'error');
			}
			}catch (e) {  
				$.messager.alert('提示',data,'error');
			}
		},
		error:function(data) {
			$.messager.fail(data.responseText);
		}
	});
}
</script>
	</body>
</html>

