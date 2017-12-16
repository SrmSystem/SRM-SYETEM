<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<html>
<head>
	<title><spring:message code="purchase.basedata.QuestionnaireManagement"/><!-- 问卷管理 --></title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/basedata/questionnaire.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/Questionnaire/getQuestionnaireList',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th data-options="field:'id',checkbox:true">ID</th>
        <th data-options="field:'title'"><spring:message code="purchase.basedata.Subject"/><!-- 题目 --></th>
        <th data-options="field:'releaseerUserName'"><spring:message code="vendor.releasePeople"/><!-- 发布人 --></th>
        <th data-options="field:'releaseTime'"><spring:message code="purchase.basedata.vendor.releaseTime"/><!-- 发布时间 --></th>
        <th data-options="field:'endTime'"><spring:message code="purchase.basedata.EndTime"/><!-- 结束时间 --></th>
        <th data-options="field:'status',formatter:questionnaire.vfmt1"><spring:message code="purchase.basedata.State"/><!-- 状态 --></th>
        <th data-options="field:'lookNumber'"><spring:message code="purchase.basedata.BrowseTimes"/><!-- 浏览次数 --></th>
        <th data-options="field:'answerNumber'"><spring:message code="purchase.basedata.AnswerTimes"/><!-- 答题次数 --></th>
        <th data-options="field:'noo',formatter:questionnaire.vfmt"><spring:message code="vendor.operation"/><!-- 操作 --></th>

      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="questionnaire.add()"><spring:message code="vendor.new"/><!-- 新增 --></a>
	    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="questionnaire.del()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>  
	    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="questionnaire.release()"><spring:message code="vendor.posted"/><!-- 发布 --></a>  
	    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="questionnaire.dels()"><spring:message code="vendor.BeOverdue"/><!-- 过期 --></a>  
      <form id="form" method="post">
                            <!--  题目 --><spring:message code="purchase.basedata.Subject"/>:<input class="easyui-textbox" name="search-LIKE_title" type="text" style="width:80px;"/>
                             <!-- 状态 --><spring:message code="purchase.basedata.State"/>:<select id="search-LIKE_status" name="search-EQ_status" class="easyui-combobox">
                     	<option value=""></option>        
                     	<option value="1"><!-- 已发布 --><spring:message code="vendor.published"/></option>        
                     	<option value="0"><!-- 未发布 --><spring:message code="vendor.notRelease"/></option>        
                     	<option value="-1"><!-- 已过期 --><spring:message code="vendor.Expired"/></option>        
                     </select>
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="questionnaire.search()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')"><spring:message code="purchase.basedata.ProductLineManager"/><spring:message code="vendor.resetting"/><!-- 重置 --></a>    
      </form>
      <form id="form-materialSupplyRel-export" method="post">
	  </form>
    </div>
  </div>
  <div id="dd" class="easyui-dialog" style="width:80%;height:90%"
    data-options="title:'<spring:message code="purchase.basedata.ProductLineManager"/>问卷操作',iconCls:'icon-save',resizable:true,modal:true,closed:true,buttons:'#dialog-adder-bb',draggable:false">
   		 <form id="formchase1" method="post">
				<font style="font-size: 18px;font-weight: 900;"><!-- 问卷调查题目 --><spring:message code="purchase.basedata.QuestionnaireSurvey"/>：</font>
				<input class="easyui-textbox" data-options="required:true" id="sxlzongyang" name="sxlzongyang"/>
				 <div style="float:right;"><font style="font-size: 18px;font-weight: 900;"> <!-- 过期时间 --><spring:message code="purchase.basedata.ExpirationTimes"/>：</font>
				 <input class="easyui-datetimebox" id="endTime" name="endTime" data-options="showSeconds:true,required:true,editable:false" value="" style="width:150px">
				</div>
				<br/><table id="daopetin" class="table table-bordered" >
						<thead  class="datagrid-header"><tr>
						<th style="width:25%"><spring:message code="purchase.basedata.QuestionnaireTitle"/><!-- 问卷题目 --></th>
						<th style="width:25%"><spring:message code="purchase.basedata.QuestionnaireTitleType"/><!-- 问卷题目类型 --></th>
						<th style="width:25%"><spring:message code="purchase.basedata.QuestionnaireAnswerOptions"/><!-- 问卷答案选项 --></th>
						<th style="width:25%"><spring:message code="vendor.operation"/><!-- 操作 --></th>
						</tr></thead>
				        <tbody  id="addupo">
				        </tbody>  
				</table>
			</form>
    <div id="dialog-adder-bb">
      <a id="addtr" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="addtimu()" href="javascript:;"><spring:message code="purchase.basedata.AddTitles"/><!-- 添加题目 --></a>
      <a id="addtrr" class="easyui-linkbutton" data-options="iconCls:'icon-save'" onclick="submits()" href="javascript:;"><spring:message code="vendor.submit"/><!-- 提交 --></a>
    </div>
</div>

<div id="udd" class="easyui-dialog" title='<spring:message code="purchase.basedata.UpdateQuestionnaire"/><!-- 更新问卷 -->' style="width:60%;height:98%"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true,draggable:false">   
        
</div> 
<div id="ddS" class="easyui-dialog" title='<spring:message code="purchase.basedata.QuestionnaireInvestigation"/><!-- 问卷调查 -->' style="width:60%;height:98%;"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true,draggable:false">

</div> 
<script type="text/javascript">
var number=1;
function addtimu(){
	number=number+1;
	$("#addupo").append(
			'<tr id="p'+number+'">'
			+'<td><input class="easyui-textbox" data-options="required:true" style="width:100%" name="sxltitle"/></td>'
			+'<td><select name="sxltitletype"class="easyui-combobox" style="width:100%" data-options="required:true,editable:false"><option value=""></option><option value="1"><spring:message code="purchase.basedata.RadioTopics"/></option><option value="2"><spring:message code="purchase.basedata.AlternativeTitle"/></option> <option value="3"><spring:message code="purchase.basedata.TextTopic"/></option></select></td>'
			+'<td><table id="a'+number+'"></table><a  onclick="addAwent(\'a'+number+'\')" class="easyui-linkbutton" data-options="iconCls:\'icon-add\'"><spring:message code="purchase.basedata.AddAnswers"/></a></td>'/* 添加答案 */
			+'<td><a href="javascript:;" id="sc'+number+'" class="easyui-linkbutton" onclick="deletep(\'p'+number+'\')"><spring:message code="vendor.deleting"/></a>'/* 删除 */
			+'<input type="hidden" name="ids" value="s-'+number+'"/><input type="hidden" id="ipsa'+number+'" value="0"/></td>'
			+'</tr>'
	);
	$.parser.parse($('#p'+number));
}
function deletep(pname)
{
	$("#"+pname).remove();
}
function addAwent(n){
	var ips=$("#ips"+n).val();
	ips=ips+"1";
	$("#ips"+n).val(ips);
	var ss='<tr id="xtd'+n+ips+'"><td><input class="easyui-textbox" name="sxltitlesa'+n+'" style="width:100%" value=""/><td><tr/>';
	$('#'+n).append(ss);
	$.parser.parse($('#xtd'+n+ips));
}
function submits()
{
	$.messager.progress();
	$('#formchase1').form('submit',{
		url:ctx+'/manager/Questionnaire/addquestionnaire',
		onSubmit:function(){
			var isValid = $(this).form('validate');
			if(!isValid){
				$.messager.progress('close');
			}
			return isValid;
		},
		success:function(data){
			$.messager.progress('close');
			if(data==1)
			{
				$('#dd').window('close');
				$('#datagrid').datagrid('reload');
				$.messager.alert('<spring:message code="vendor.submit"/><!-- 提交 -->','<spring:message code="vendor.PromptSuccess"/><!-- 提交成功! -->','info');
			}
			else
			{
				$.messager.alert('<spring:message code="vendor.submit"/><!-- 提交 -->',data,'error');
			}
		}
	});
}
</script>
</body>
</html>
