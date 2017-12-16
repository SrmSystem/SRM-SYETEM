<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
	<head>
		<title>二方审核管理</title>
		<script type="text/javascript" src="${ctx}/static/script/twoaudit/twoAudit.js"></script>	
	</head>
<body style="margin: 0; padding: 0;">
<table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/qualityassurance/twoAudit',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="3%" data-options="field:'id',checkbox:true">ID</th>
        <th width="8%"data-options="field:'workOrder'">工单号</th>
        <th width="9%" data-options="field:'vendorCode',formatter:function(v,r){if(r.org) return r.org.code; else ''}">供应商编码</th>
        <th width="9%" data-options="field:'vendorName',formatter:function(v,r){if(r.org) return r.org.name; else ''}">供应商名称</th>
        <th width="8%" data-options="field:'correctionStatus',formatter:twoAudit.correctiveStatusFmt">整改状态</th>
        <th width="10%" data-options="field:'correctionContent'">审核计划内容</th>
        <th width="9%" data-options="field:'planFileName',formatter:twoAudit.planDownLoad">审核计划附件</th>
        <th width="9%" data-options="field:'solutionContent'">最新提交内容</th>
        <th width="5%" data-options="field:'fileUrl',formatter:twoAudit.fileUrl">最新提交附件</th>
        <th width="8%" data-options="field:'endStatus',formatter:twoAudit.endStatus">结案状态</th>
        <th width="11%" data-options="field:'correctionEndContent'">计划结案内容</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
  	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="twoAudit.addPlan()">新增计划</a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="twoAudit.qrs()">发布计划</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="twoAudit.solution()">计划审核</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="twoAudit.endSolutionStart()">计划结案</a>  
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
	<!-- 新增/修改 计划 -->
		<div id="win-twoAudit-addPlan" class="easyui-dialog" title="计划" style="width: 500px;height:500px" data-options="modal:true,closed:true,buttons:'#dialog-adder-a'" >
			<form id="form-twoAudit-addPlan" method="post" enctype="multipart/form-data" ">		
				<input type="hidden" id="orgId" name="orgId" value="0">			
				<input type="hidden" id="id" name="id" value="0">			
				<table width="100%">
					<tr><td width="30%">供应商代码</td><td><input type="text" id="vendCode" name="vendorCode" readonly="readonly" class="easyui-textbox" data-options="required:true" style="width:150px;"/>
							<a href="javascript:;" id="xuanzeanniu" class="easyui-linkbutton" onclick="twoAudit.chooseVenCode()">选择</a>
						</td>
					</tr>
					<tr><td>供应商名称</td><td><input type="text" id="vendName" name="vendorName" readonly="readonly" class="easyui-textbox" data-options="required:true" style="width:150px;"/></td></tr>
					<tr>
						<td>审核计划内容:</td>
						<td height="100px" colspan="3"><input class="easyui-textbox" id="correctionContent" name="correctionContent" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true" value=""/></td>
					</tr>
					<tr><td>审核计划附件</td><td><input type=file id="planFile" name="planfiles" /></td></tr>
				</table>
							
				<div id="dialog-adder-a">
					<a href="javascript:;" class="easyui-linkbutton" onclick="twoAudit.savePlan()">保存</a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-twoAudit-addPlan').form('reset')">重置</a>
				</div>
			</form>
		</div>
	<!-- end -->
	
	<!-- 新增/修改 计划时选择的供应商代码模块  -->
		<div id="kk" class="easyui-dialog" title="选择供应商" style="width:60%;height:98%" data-options="iconCls:'icon-add',modal:true,closed:true">   
		    <table id="datagrid-twoAuditVendor-list" class="easyui-datagrid" data-options="fit:true,method:'post',singleSelect:true,pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,toolbar:'#ttt'">
			    <thead>
			      <tr>
			        <th data-options="field:'id',checkbox:true"></th>
			        <th data-options="field:'code'">供应商代码</th>
			        <th data-options="field:'name'">供应商名称</th>
			        <th data-options="field:'countryText'">国家</th>
			        <th data-options="field:'provinceText'">省份</th>
			        <th data-options="field:'vendorLevel'">供应商等级</th>
			        <th data-options="field:'vendorClassify'">供应商分类</th>
			        <th data-options="field:'vendorClassify2'">供应商分类<br/>&nbsp;&nbsp;(A,B,C)</th>
			        <th data-options="field:'regtime'">注册时间</th>
			      </tr>
			    </thead>
		  	</table>
		  	<div id="ttt">
			    <div>
			      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="bringBack()">选择带回</a>    
			      <form id="form-twoAuditVendor-search" method="post">
			                             供应商代码:<input type="text" class="easyui-textbox" name="search-LIKE_code" class="easyui-textbox" style="width:80px;"/>
			                             供应商名称:<input type="text" class="easyui-textbox" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="twoAudit.addsearch()">查询</a> 
			       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-twoAuditVendor-search').form('reset')">重置</a>     
			      </form>
			    </div>
		  	</div>   
		</div> 
	<!-- end -->
 <div id="win-import" title="方案审核" style="height:80%;width: 700px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true,buttons:'#dialog-adder-bb'" >
	<form id="form-import" method="post"> 
	<input type="hidden" id="uid" name="uid" value="" />
	<input type="hidden" id="typee" name="typee" value="" />
		<table>
			<tr>
				<td>整改要求:</td>
				<td height="100px" colspan="3"><input class="easyui-textbox" id="correctionContent" name="correctionContent" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true,disabled:true" value=""/></td>
			</tr>
			<tr>
				<td>整改方案:</td>
				<td height="100px" colspan="3"><input class="easyui-textbox" id="solutionContent" name="solutionContent" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true,disabled:true" value=""/></td>
			</tr>
			<tr>
				<td>整改方案附件:</td>
				<td id="fujian" height="20px" colspan="3"></td>
			</tr>
			<tr>
				<td>审核意见:</td>
				<td height="100px" colspan="3"><input class="easyui-textbox" id="sContent" name="sContent" type="text" style="width:100%;height: 100%" data-options="required:true,height:40,multiline:true" value=""/></td>
			</tr>
		</table>
	</form>  
	 <div id="dialog-adder-bb">
		<a href="javascript:;"  data-options="iconCls:'icon-ok'" class="easyui-linkbutton" onclick="twoAudit.solutionSubmit(1)">审核通过</a>
		<a href="javascript:;"  data-options="iconCls:'icon-no'" class="easyui-linkbutton" onclick="twoAudit.solutionSubmit(2)">审核不通过</a>
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
 <div id="win-import3" title="整改结案" style="width:30%;height:30%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true,buttons:'#dialog-adder-bb2'"> 
       
        <table  style="width: 100%;height: 100%">
     		 <tr>
				<td style="width: 30%;height: 100%">结案评论:</td>
				<td  style="width: 70%;height: 100%">
				 <form id="sxlform" method="post" style="width: 100%;height: 100%">
					<textarea id="usContent" name="usContent" style="width:100%;height: 100%"></textarea>
				</form> 
				</td>
			</tr>
		</table>  
		<
	<div id="dialog-adder-bb2">
		<a href="javascript:;"  data-options="iconCls:'icon-ok'" class="easyui-linkbutton" onclick="twoAudit.endSolution()">结案</a>
	</div>
 </div>
<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		})
	</script>
	</body>
</html>

