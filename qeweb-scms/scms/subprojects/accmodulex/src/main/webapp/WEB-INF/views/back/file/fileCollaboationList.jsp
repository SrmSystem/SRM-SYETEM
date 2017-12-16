<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
	<title><spring:message code="vendor.file.fileCoordinatedManagemen"/></title>
	<script type="text/javascript"> var ctx = '${pageContext.request.contextPath}';</script>
	<script type="text/javascript" src="${ctx}/static/script/file/fileCollaboation.js"></script>
	

	<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/css/smoothness/jquery.ui.css" type="text/css" />
    <link rel="stylesheet" type="text/css" href="${ctx }/static/cuslibs/notie/css/css.css"/>
    <link rel="stylesheet" href="${ctx }/static/cuslibs/notie/themes/default/default.css" />
	<link rel="stylesheet" href="${ctx }/static/cuslibs/notie/plugins/code/prettify.css" />
	<script charset="utf-8" src="${ctx }/static/cuslibs/notie/kindeditor-min.js"></script>
	<script charset="utf-8" src="${ctx }/static/cuslibs/notie/lang/zh_CN.js"></script>

	
	<script>
		KindEditor.ready(function(K) {
			var editor1 = K.create('textarea[name="describe"]', {
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
	<table id="datagrid-fileCollaboation-list" title="文件协同列表" class="easyui-datagrid"
		data-options="fit:true,
		url:'${ctx}/manager/file/fileCollaboation',method:'post',singleSelect:false,
		toolbar:'#fileCollaboationListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th width="30px" data-options="field:'id',checkbox:true"></th>
		<th width="50px" data-options="field:'manager',formatter:FileCollaboation.operateFmt"><spring:message code="vendor.operation"/></th>
		<th width="80px" data-options="field:'title'"><spring:message code="vendor.file.collaborativeName"/>	</th>
		<th  width="100px"  data-options="field:'collaborationType',formatter:function(v,r,i){return r.collaborationType.name;}"   ><spring:message code="vendor.file.collaborativeType"/></th>
		<th width="120px" data-options="field:'validStartTime'"><spring:message code="vendor.effectiveStartTime"/></th>
		<th width="120px" data-options="field:'validEndTime'"><spring:message code="vendor.effectiveEndTime"/></th>
		<th  width="120px"  data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="vendor.postStatus"/></th> 
		<th width="120px" data-options="field:'publishTime'"><spring:message code="vendor.releaseTime"/></th>
		<th width="100px" data-options="field:'publishUser',   formatter:function(v,r,i){ if(r.publishUser){  return r.publishUser.name; }  else{  return '' ;   }  }" "><spring:message code="vendor.releasePeople"/></th>
		<th width="150px" data-options="field:'createTime'"><spring:message code="vendor.creationTime"/></th>
		<th width="150px" data-options="field:'remark',formatter:FileCollaboation.viewFmt"><spring:message code="vendor.file.supplierFile"/></th>
		</tr></thead>
	</table>
	<div id="fileCollaboationListToolbar" style="padding:5px;">
		<div>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="FileCollaboation.add()"><spring:message code="vendor.new"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="FileCollaboation.del()"><spring:message code="vendor.deleting"/></a>
		<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="FileCollaboation.publish()"><spring:message code="vendor.posted"/></a>
		</div>
		<div>
			<form id="form-fileCollaboation-search" method="post">
			<spring:message code="vendor.file.collaborativeName"/>	：<input type="text" name="query-LIKE_title" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.creationTime"/>： <input type="text" name="query-GT_createTime" data-options="editable:false" class="easyui-datebox" ><spring:message code="vendor.to"/> 
					        <input type="text" name="query-LT_createTime" data-options="editable:false" class="easyui-datebox" >
			<spring:message code="vendor.file.collaborativeType"/>：<input class="easyui-combobox" data-options="valueField:'value',textField:'text',url : '${ctx}/manager/basedata/dict/getDict/FILE_COLLABOATION',editable:false" style="width:140px;" name="query-EQ_collaborationType.code" />
			<spring:message code="vendor.postStatus"/>：<select class="easyui-combobox" data-options="editable:false" name="query-EQ_publishStatus"><option value="">-<spring:message code="vendor.all"/>-</option><option value="0"><spring:message code="vendor.notRelease"/></option><option value="1"><spring:message code="vendor.published"/></option></select>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="FileCollaboation.query()"><spring:message code="vendor.enquiries"/></a>
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-fileCollaboation-search').form('reset')"><spring:message code="vendor.resetting"/></a>
			</form>
		</div>
	</div>
	<div id="win-fileCollaboation-addoredit" class="easyui-dialog" title="新增文件协同" style="width:600px;height:800px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div itemId="ct">
			<form id="form-fileCollaboation-addoredit" method="post"  enctype="multipart/form-data">
				<input id="id" name="id" value="-1" type="hidden"/>
				<input id="filePath" name="filePath" type="hidden"/>
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="vendor.file.fileCollaborationName"/>:</td><td style="text-align:left"><input class="easyui-textbox" id="title" name="title" type="text" 
						data-options="required:true"  width="240px" />
				    </td>
				</tr>
				<tr>
					<td><spring:message code="vendor.file.collaborativeName"/>:</td><td style="text-align:left"><input class="easyui-combobox" id=collaborationTypeCode name="collaborationTypeCode" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.file.documentCollaborationTemplate"/>:</td><td style="text-align:left"><input class="easyui-filebox"  id="file" name="file" data-options="prompt:'选择...'" >
				
						<a href="javascript:;"  onclick="downFile()"><label id="fileName" ></label</a>
						<a href="javascript:;"><label style="color: red" id="msg" ></label</a>
					</td>
				</tr>
				<tr>
					<td><spring:message code="vendor.startDate"/>:</td><td style="text-align:left"><input id="validStartTime" name="validStartTime" class="easyui-datebox" data-options="required:true,editable:false" style="width:140px"/></td>
				</tr>
				<tr>
					<td><spring:message code="vendor.endDate"/>:</td><td style="text-align:left"><input id="validEndTime" name="validEndTime" class="easyui-datebox" data-options="required:true,editable:false" style="width:140px"/></td>
				</tr>
				<tr>
				     <input id="vendorIds" name="vendorIds" type="hidden"/>
					<td><spring:message code="vendor.permissions"/>:</td><td style="text-align:left"><input class="easyui-textbox" id="companyName"   name="vendorNames"  data-options="editable:false,required:true" type="text"/>
					<a href="javascript:;" class="easyui-linkbutton" onclick="lookUser()"><spring:message code="vendor.choose"/></a>
					</td>
					
				</tr>
				<tr>
				<td>
				</td>
				</tr>
				
				
				<tr>
					<td colspan="2" style="text-align:center">
						<textarea id="describe" name="describe" style="width: 80%;height:80%"></textarea>
					</td>
				</tr>
				</table>
				<div id="br1"  style="text-align: center;padding:5px;display:block;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="FileCollaboation.submit()"><spring:message code="vendor.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-fileCollaboation-addoredit').form('reset')"><spring:message code="vendor.resetting"/></a>
				</div>
				<div id="br2" style="text-align: center;padding:5px;display: none;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="FileCollaboation.submit()"><spring:message code="vendor.submit"/></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="FileCollaboation.reset()"><spring:message code="vendor.resetting"/></a>
				</div>
			</form>
		</div>
	</div>
	
		<div id="kk" class="easyui-dialog" title="添加组织" style="width:60%;height:98%"   
        data-options="iconCls:'icon-add',modal:true,closed:true">   
    <table id="datagridss" class="easyui-datagrid" data-options="
    fit:true,method:'post',singleSelect:false,
	pagination:true,rownumbers:true,pageSize:300,pageList:[300,350,400],
    toolbar:'#ttt',
    onLoadSuccess : function(data){
					var allDatas = data.rows;
					var vendorId = $('#vendorIds').val();
					for(var i = 0; i < allDatas.length; i ++) {
						var id = allDatas[i].id;
					    if(vendorId.indexOf(id)>= 0){
					     $(this).datagrid('selectRow', i);
					    }
					}
				},
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true"></th>
		<th data-options="field:'code'"><spring:message code="vendor.coding"/></th>
		<th data-options="field:'name'"><spring:message code="vendor.appellation"/></th>
		<th data-options="field:'registerTime'"><spring:message code="vendor.RegistrationTime"/></th>
		<th data-options="field:'_orgType'"><spring:message code="vendor.organizational"/></th>
		<th data-options="field:'_roleType'"><spring:message code="vendor.tissueTypes"/></th>
      </tr>
    </thead>
  </table>
  <div id="ttt">
    <div>
      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe()"><spring:message code="vendor.chooseBack"/></a>    
      <form id="form2" method="post">
            <spring:message code="vendor.coding"/>：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.appellation"/>：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch()"><spring:message code="vendor.enquiries"/></a> 
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form2').form('reset')"><spring:message code="vendor.resetting"/></a>     
      </form>
    </div>
  </div>   
</div>
	

	
	
</body>
</html>
