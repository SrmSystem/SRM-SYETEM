<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.abnormal.abnormalFeedback"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/abnormal/abnormalback.js"></script>
	
 	<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/css/smoothness/jquery.ui.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctx }/static/cuslibs/notie/css/css.css"/>
    <link rel="stylesheet" href="${ctx }/static/cuslibs/notie/themes/default/default.css" />
	<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/plugins/code/prettify.css" />
	<script charset="utf-8" src="${ctx }/static/cuslibs/notie/kindeditor-min.js"></script>
	<script charset="utf-8" src="${ctx }/static/cuslibs/notie/lang/zh_CN.js"></script>

	
	<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="commentArea1"]', {
				cssPath : ctx+'/static/cuslibs/notie/plugins/code/prettify.css',
				uploadJson : ctx+'/static/cuslibs/notie/upload_json.jsp',
				fileManagerJson : ctx+'/static/cuslibs/notie/file_manager_json.jsp',
				allowFileManager : true,
				afterCreate : function() {
					var self = this;
					K.ctrl(document, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
					K.ctrl(self.edit.doc, 13, function() {
						self.sync();
						document.forms['example'].submit();
					});
				},
				afterBlur: function(){this.sync();}
			});
		});
	</script>
	
<style>
.ke-container{
margin-left: 30px;
}
.window{
top:-4px  ! important; 
}
</style>
	
</head>

<body style="margin:0;padding:0;">
	<table id="datagrid-abnormal-list" title="异常信息列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/abnormal/abnormalFeedback',method:'post',singleSelect:false,
		toolbar:'#abnormalListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="80px" data-options="field:'manager',formatter:AbnormalBack.operateFmt"><spring:message code="vendor.abnormal.operation"/></th>
		<th width="80px" data-options="field:'abnormalFeedbackName'"><spring:message code="vendor.abnormal.abnormalFeedbackName"/></th>
<!-- 		<th  width="100px"  data-options="field:'commentYn' ,formatter:function(v,r,i){if(v=='1') return '允许';else return '不允许';}     " >是否评论</th> -->
		<th width="120px" data-options="field:'topYn'   ,formatter:function(v,r,i){if(v=='1') return '<spring:message code="vendor.abnormal.resetting"/>';else return '<spring:message code="vendor.abnormal.notTop"/>';}    "><spring:message code="vendor.abnormal.isTop"/></th>
		<th width="120px" data-options="field:'effectiveStartDate'"><spring:message code="vendor.abnormal.effectiveStart"/></th>
		<th  width="120px"  data-options="field:'effectiveEndDate'"><spring:message code="vendor.abnormal.effectiveEnd"/></th> 
		<th width="150px" data-options="field:'createTime'"><spring:message code="vendor.abnormal.creationDate"/></th>
		<th width="100px" data-options="field:'publishName'"><spring:message code="vendor.abnormal.releaser"/></th>
		</tr></thead>
	</table>
	<div id="abnormalListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="AbnormalBack.addOpt()"><spring:message code="vendor.abnormal.newAddition"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="AbnormalBack.del()"><spring:message code="vendor.abnormal.deleting"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="AbnormalBack.publish()"><spring:message code="vendor.abnormal.posted"/></a>
		</div>
		<div>
			<form id="form-abnormal-search" method="post">
			<spring:message code="vendor.abnormal.abnormalFeedbackName"/>：<input type="text" name="query-LIKE_abnormalFeedbackName" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.abnormal.creationDate"/>： <input type="text" name="query-GT_createTime" data-options="editable:false" class="easyui-datebox" ><spring:message code="vendor.abnormal.to"/>
					        <input type="text" name="query-LT_createTime" data-options="editable:false" class="easyui-datebox" >
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="AbnormalBack.searchOrder()"><spring:message code="vendor.abnormal.enquiries"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-abnormal-search').form('reset')"><spring:message code="vendor.abnormal.resetting"/></a>
			</form>
		</div>
	</div>
	<!-- 新增异常信息 -->
	 <div id="win-abnormal-addoredit" class="easyui-dialog" title="添加异常反馈" style="width:600px;height:800px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-abnormal-addoredit" method="post"  enctype="multipart/form-data">
				<input id="id" name="id" value="-1" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="vendor.abnormal.abnormalFeedbackName"/>:</td><td style="text-align: left"><input class="easyui-textbox" id="abnormalFeedbackName" name="abnormalFeedbackName"  type="text" 
						data-options="required:true"  width="150px" />
				    </td>
				</tr>
<!-- 				<tr>
				<td>是否评论:</td>
				<td style="text-align:left">
						<select id="commentYn" class="easyui-combobox" data-options="required:true,editable:false"  name="commentYn" style="width:150px;">    
    						<option value="1">允许</option>
    						<option value="0">不允许</option>
						</select> 
				    </td>
				</tr> -->
				<tr>
					<td><spring:message code="vendor.abnormal.isTop"/>:</td>
					<td style="text-align:left">
					<select class="easyui-combobox" id="topYn" name="topYn"  style="width:150px;"
						 data-options="required:true,editable:false">
						 <option value="1"><spring:message code="vendor.abnormal.topper"/></option>
						 <option value="0"><spring:message code="vendor.abnormal.notTop"/></option>
					 </select>
				    </td>
				</tr>
				<tr>
					<td><spring:message code="vendor.abnormal.startDate"/>:</td><td style="text-align:left"><input id="effectiveStartDate" name="effectiveStartDate" class="easyui-datebox" data-options="required:true,editable:false" style="width:150px"/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.abnormal.endDate"/>:</td><td style="text-align:left"><input id="effectiveEndDate" name="effectiveEndDate" class="easyui-datebox" data-options="required:true,editable:false" style="width:150px"/></td>
				</tr>
<!-- 				<tr>
				     <input id="vendorIds" name="vendorIds" type="hidden"/>
					<td>权限设置:</td><td style="text-align:left"><input class="easyui-textbox" id="companyName"   name="vendorNames"  data-options="editable:false,required:true"  type="text"/>
					<a href="javascript:;" class="easyui-linkbutton" onclick="lookUser()">选择</a>
					</td>
				</tr> -->
				<tr>
				<td>
				</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align:center">
						<textarea id="commentArea1" name="commentArea1" style="width: 80%;height:80%"></textarea>
						<input   id="commentArea" name="commentArea"  type="hidden"   
						  width="150px" />
					</td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="AbnormalBack.submit()"><spring:message code="vendor.abnormal.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-abnormal-addoredit').form('reset')"><spring:message code="vendor.abnormal.resetting"/></a>
				</div>
				 <div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="AbnormalBack.submit()"><spring:message code="vendor.abnormal.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="AbnormalBack.reset()"><spring:message code="vendor.abnormal.resetting"/></a>
				</div>
			</form>
		</div>
	</div> 
	
 		<div id="kk" class="easyui-dialog" title="添加组织" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
    toolbar:'#ttt'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'"><spring:message code="vendor.abnormal.coding"/></th>
		<th data-options="field:'name'"><spring:message code="vendor.abnormal.appellation"/></th>
		<th data-options="field:'registerTime'"><spring:message code="vendor.abnormal.registrationTime"/></th>
		<th data-options="field:'_orgType'"><spring:message code="vendor.abnormal.organizational"/></th>
		<th data-options="field:'_roleType'"><spring:message code="vendor.abnormal.tissueTypes"/></th>
      </tr>
    </thead>
  </table>
  <div id="ttt">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe()"><spring:message code="vendor.abnormal.chooseBack"/></a>    
      <form id="form2" method="post">
            <spring:message code="vendor.abnormal.coding"/>：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.abnormal.appellation"/>：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch()"><spring:message code="vendor.abnormal.enquiries"/></a> 
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form2').form('reset')"><spring:message code="vendor.abnormal.resetting"/></a>     
      </form>
    </div>
  </div>   
</div>
</body>
</html>
