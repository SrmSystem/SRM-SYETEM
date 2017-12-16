<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>供应商提交整改方案</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/correction.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/VendorPerforCorrection/correctionList2',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="3%" data-options="field:'id',checkbox:true">整改ID</th>
        <th width="5%" data-options="field:'cycleName',formatter:function(v,r){if(r.cycleEntity) return r.cycleEntity.cycleName;}">评估周期</th>
        <th width="5%" data-options="field:'assessDate'">评估时间</th>
        <th width="10%" data-options="field:'vendorCode'">供应商编码</th>
        <th width="10%" data-options="field:'vendorName'">供应商名称</th>
        <th width="10%" data-options="field:'brandName'">品牌名称</th>
        <th width="10%" data-options="field:'correctionDate'">提出整改要求时间</th>
        <th width="10%" data-options="field:'requireDate'">要求整改完成时间</th>
        <th width="8%" data-options="field:'correctionStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'correctionStatus',true);}">整改状态</th>
        <th width="8%" data-options="field:'endStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'endStatus',true);}">结案状态</th>
        <th width="10%" data-options="field:'correctionContent'">整改要求</th>
        <th width="10%" data-options="field:'planFilePath',formatter:correction.planDownLoad">整改要求附件</th>
        <th width="11%" data-options="field:'correctionEndContent'">整改结案评论</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="correction.vendorsolution()">方案提交</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-lock',plain:true" onclick="correction.lookSolution()">查看整改过程</a>  
    <div>
      <form id="form" method="post">
                <table>
		      		<tr>
		      			<td>开始评估时间:</td>
		      			<td><input class="easyui-datebox" name="search-GTE_assessDate" data-options="showSeconds:true" value="" style="width:150px"></td>
		      			<td>结束评估时间:</td>
		      			<td><input class="easyui-datebox" name="search-LTE_assessDate" data-options="showSeconds:true" value="" style="width:150px"></td>
		      			<td>周期:</td>
		      			<td><input class="easyui-combobox" id="search-EQ_cycleId" name="search-EQ_cycleId" style="width:150px"/></td>
		      		</tr>
		      		<tr>
		      			<td>品牌:</td>
		      			<td><input class="easyui-textbox" name="search-LIKE_brandName" type="text" style="width:150px;"/></td>
		      			<td>供应商编码:</td>
		      			<td><input class="easyui-textbox" name="search-LIKE_vendorCode" type="text" style="width:150px;"/></td>
		      			<td>供应商名称:</td>
		      			<td><input class="easyui-textbox" name="search-LIKE_vendorName" type="text"  style="width:150px;"/></td>
		      		</tr>
		      		<tr>
		      			<td>整改提出时间:</td>
		      			<td><input class="easyui-datebox" name="search-GTE_correctionDate" data-options="showSeconds:true" value="" style="width:150px"></td>
		      			<td>整改提出结束时间:</td>
		      			<td><input class="easyui-datebox" name="search-LTE_correctionDate" data-options="showSeconds:true" value="" style="width:150px"></td>
		      			<td>整改状态:</td>
		      			<td>
		      			<select name="search-EQ_correctionStatus" class="easyui-combobox" style="width:150px;">
                         	<option value=""></option>
                         	<option value="0">新建</option>
                         	<option value="1">方案提交</option>
                         	<option value="2">审核通过</option>
                         	<option value="3">审核不通过</option>
                         	<option value="4">整改结束</option>
                         </select> 
		      			</td>
		      		</tr>
		      		<tr>
		      			<td>要求完成时间:</td>
		      			<td><input class="easyui-datebox" name="search-GTE_requireDate" data-options="showSeconds:true" value="" style="width:150px"></td>
		      			<td>要求完成结束时间:</td>
		      			<td><input class="easyui-datebox" name="search-LTE_requireDate" data-options="showSeconds:true" value="" style="width:150px"></td>
		      			<td>整改结案状态:</td>
		      			<td>
		      			<select name="search-EQ_endStatus" class="easyui-combobox" style="width:150px;">
                         	<option value=""></option>
                         	<option value="0">未结案</option>
                         	<option value="1">结案</option>
                         </select>
		      			</td>
		      			<td rowspan="2"><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="correction.search()">查询</a><a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>  </td>
		      		</tr>
		      	</table> 
      </form>
    </div>
  </div>
<form id="form-export">
</form>
 <div id="win-import" title="提交方案" data-options="iconCls:'icon-setting',modal:true,closed:true,buttons:'#dialog-adder-bb'" style="width: 80%;height: 80%">
	<form id="form-importww" method="post"  enctype="multipart/form-data"> 
		<input id="uid" name="uid" type="hidden"  value="" />
	   	<table style="width: 100%;height: 80%">
			<tr>
				<td>整改开始时间:</td>
				<td><input class="easyui-textbox" name="correctionDate" data-options="required:true,disabled:true" value="" style="width:150px"></td>
				<td>要求完成时间:</td>
				<td><input class="easyui-textbox" name="requireDate" data-options="required:true,disabled:true" value="" style="width:150px"></td>
			</tr>
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
					pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20]"
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
			$('#search-EQ_cycleId').combobox({    
				url:ctx+'/manager/vendor/performance/index/getVendorPerforCycle',    
				valueField:'id',    
				textField:'text' ,
				onSelect: function(rec){    
					$("input[name='search-EQ_cycleId']").val(rec.id);
				}
			});
		})
  function solutionSubmits(){
	$.messager.progress();
	$('#form-importww').form('submit',{
		ajax:true,
		iframe: true,    
		url:ctx+'/manager/vendor/VendorPerforCorrection/filesUpload', 
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
