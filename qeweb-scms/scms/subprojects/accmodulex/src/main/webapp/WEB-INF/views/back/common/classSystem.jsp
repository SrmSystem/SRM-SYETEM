<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:set var="ctx" value="${pageContext.request.contextPath}"/>

<html>
<head>
<title></title>


<script type="text/javascript" src="${ctx}/static/script/contract/dialog.js"></script>
<script type="text/javascript" src="${ctx}/static/script/basedata/classSystem.js"></script>
</head>

<body>

 <div id="classSystem-tabs" class="easyui-tabs"  data-options="tabPosition:'top',fit:true,onSelect:classSystem.refreshTabClassSystem"> 
    <div title="班制设置" itemId="1"  style="padding:10px">
       <table id="datagrid-classSystem-list" class="easyui-datagrid"
				data-options="url:'${ctx}/manager/common/classSystem/getClassSystem',method:'post',singleSelect:false,
				fit:true,border:false,toolbar:'#classSystemToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
				>
				<thead>
				<tr>
				<th data-options="field:'operate',formatter:classSystem.operateClassFmt,width:'100'"><spring:message code="vendor.operation"/><!-- 操作 --></th>
				<th data-options="field:'name',width:'100' "><spring:message code="vendor.common.nameSystem"/><!-- 班制名称 --></th>
				<th data-options="field:'morningStart'+'morningEnd',formatter:classSystem.morningTimeFmt,width:'100'"><spring:message code="vendor.common.morningHours"/><!-- 上午时间段 --></th>
				<th data-options="field:'afterStart'+'afterEnd',formatter:classSystem.afterTimeFmt,width:'100'"><spring:message code="vendor.common.afternoonPeriod"/><!-- 下午时间段 --></th>
				<th data-options="field:'billType',width:'100' ,formatter:function(v,r,i){   if (r.billType == 0 ) { return ' 五天' } else if (r.billType == 1 )   { return ' 五天半'}  else if( r.billType == 2 ){  return ' 六天'  }   }    "><spring:message code="vendor.common.shiftsType"/><!-- 班制类型 --></th>
				<th data-options="field:'remarks'"><spring:message code="vendor.remark"/><!-- 备注 --></th>
				
				</tr>
				</thead>
		</table>
	   <div id="classSystemToolbar" style="padding:5px;">
	   		<div>
	        <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="classSystem.addClassSystem()"><spring:message code="vendor.new"/><!-- 新增 --></a>
	        </div>
		<div>
			<form id="form-classSystem-search" method="post">
			<spring:message code="vendor.common.nameSystem"/><!-- 班制名称 -->：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<spring:message code="vendor.common.shiftsType"/><!-- 班制类型 -->：<input type="text" name="search-LIKE_billType" class="easyui-textbox" style="width:80px;"/>
			
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
		   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="classSystem.searchClassSystem()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
		<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-classSystem-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>
			 
		</form>
		</div>
		</div>
	</div> 

		
  <div title="节假日设置" itemId="2"  style="padding:10px">
	 <table id="datagrid-holidays-list" class="easyui-datagrid"
				data-options="url:'${ctx}/manager/common/classSystem/getHolidays',method:'post',singleSelect:false,
				fit:true,width:100,border:false,toolbar:'#holidaysToolbar',
				multiSort:true,remoteSort:true,sortName:'dayTime',sortOrder:'DESC',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]" 
				>
				<thead>
				<tr>
				<th data-options="field:'operate',formatter:classSystem.operateFmt,width:'100'"><spring:message code="vendor.operation"/><!-- 操作 --></th>
				<th data-options="field:'year' ,width:'100'"><spring:message code="vendor.common.year"/><!-- 年份 --></th>
				<th data-options="field:'dayTime',width:'100'"><spring:message code="vendor.common.dates"/><!-- 日期 --></th>
				<th data-options="field:'name',width:'100'"><spring:message code="vendor.common.nameFestival"/><!-- 节日名称 --></th>
				
				</tr>
				</thead>
		</table> 
		
		<div id="holidaysToolbar" style="padding:5px;">
			<div>
	       	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add'" onclick="classSystem.addHoliday ()"><spring:message code="vendor.new"/><!-- 新增 --></a>
	        </div>
		<div>
			<form id="form-holidays-search" method="post">
			<spring:message code="vendor.common.year"/><!-- 年份 -->：<input type="text" name="search-EQ_year" class="easyui-textbox" style="width:80px;"/>
			
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
			   <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="classSystem.searchHoliday()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
		   <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-holidays-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
						</td>
					</tr>
				</table>
			</div>

		</form>
		</div>
		</div>
	</div> 
 </div>
		
		 <div id="win-classSystem-edit" class="easyui-dialog" title="新增班制设置" style="width:500px;height:350px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-classSystem-edit" method="post" >
				
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<input id="classSystemId" type="hidden" name="id"/>
				<input id="abolished" type="hidden"  value="0"  name="abolished"/>
				<tr>
					<td><spring:message code="vendor.common.nameSystem"/><!-- 班制名称 -->:</td><td><input class="easyui-textbox"  name="name" type="text" data-options="required:true" 
						/></td>
					</tr>
				<tr>
					<td><spring:message code="vendor.common.morningHours"/><!-- 上午时间段 -->:</td><td><input id="morningStart" class="easyui-combobox"  name="morningStart" data-options="" />
					
					<input id="morningEnd" class="easyui-combobox"  name="morningEnd" data-options="required:true" />
					
					
					</td>
					</tr>
					<tr>
					<td><spring:message code="vendor.common.afternoonPeriod"/><!-- 下午时间段 -->:</td><td><input id="afterStart" class="easyui-combobox"  name="afterStart" data-options="" />
					  
			        <input id="afterEnd" class="easyui-combobox"  name="afterEnd" data-options="required:true" />
					
			        </td>
					</tr>
					
					<tr>
					<td><spring:message code="vendor.common.shiftsType"/><!-- 班制类型 -->:</td><td><select class="easyui-combobox"  name="billType" data-options="required:true"  style="width:150px;">
					<option value="0"><spring:message code="vendor.common.fiveDays"/><!-- 五天 --></option>
			        <option value="1"><spring:message code="vendor.common.fiveHalfDays"/><!-- 五天半 --></option>
			        <option value="2"><spring:message code="vendor.common.sixDays"/><!-- 六天 --></option>
			        </select>
			        </td>
					</tr>
					
					<tr>
					<td><spring:message code="vendor.remark"/><!-- 备注 -->:</td>
					<td><textarea id="content" class="textarea easyui-validatebox"   style="width:300px;height:120px" name="remarks" type="text"> </textarea>
					
					</td>
					</tr>
						 
					</tr>
					
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="classSystem.submitClassSystem()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-classSystem-edit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div> 


<div id="win-holidays-edit" class="easyui-dialog" title="新增节假日设置" style="width:300px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-holidays-edit" method="post" >
				
				<table style="text-align: right;padding:5px;margin:auto;" cellpadding="5">
				
				<tr>
					<td><spring:message code="vendor.common.holidayDate"/><!-- 节假日日期 -->:</td><td><input class="easyui-datebox"  name="dayTime" type="text"
						data-options="required:true,editable:false,onSelect:classSystem.year"/><s/td>
					</tr>
				<tr>
					<td><spring:message code="vendor.common.year"/><!-- 年份 -->:</td><td><input id="year" class="easyui-textbox"  name="year" type="text"
						 data-options="required:true" /></td>
					</tr>
					
					<tr>
					<td><spring:message code="vendor.common.nameFestival"/><!-- 节日名称 -->:</td><td><input class="easyui-textbox"  name="name" type="text"
						 data-options="required:true" /></td>
					</tr>
					
				</table>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="classSystem.submitHolidays()"><spring:message code="vendor.submit"/><!-- 提交 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-holidays-edit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div> 

</body>

</html>