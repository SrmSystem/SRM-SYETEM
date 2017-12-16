<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
	<head>
		<title>毛坯废品信息管理</title>
		<script type="text/javascript" src="${ctx}/static/script/blankScrap/blankScrap.js"></script> 
	</head>
	
	<body style="margin:0;padding:0;">
		<table id="datagrid" class="easyui-datagrid"
			   data-options="fit:true,url:'${ctx}/manager/qualityassurance/blankScrap/${vendor}',method:'post',singleSelect:false,   
			   toolbar:'#datagridToolbar',<c:if test="${vendor == true }">queryParams:{'search-EQ_state':1},</c:if>
			   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
			 
			<thead><tr>
			<th data-options="field:'id',checkbox:true">ID</th>
			<th data-options="field:'month'">月份</th>	
			<!-- <th data-options="field:'startTime'">开始时间</th> -->
				<c:if test="${vendor == false }">
			<!-- <th data-options="field:'endTime'">结束时间</th>	 -->
			<th data-options="field:'manufacturerCode'">生产厂家编码</th>			
				</c:if>
			<th data-options="field:'manufacturer'">生产厂家</th>
			<th data-options="field:'drawingNo'">图号</th>
			<th data-options="field:'partsName'">零件名称</th>
			<th data-options="field:'amount'">不合格数量（件）</th>
			<th data-options="field:'totalAmount'">总数量（件）</th>
			<th data-options="field:'unqualifiedPhenomenon'">不合格现象描述</th>
			<th data-options="field:'handle'">处置</th>
				<c:if test="${vendor == false }">
			<th data-options="field:'state',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'qualityStatus',true);}">状态</th>
				</c:if>
			
			<!-- <th data-options="field:'total'">供货总数量</th>
			<th data-options="field:'rejectionRate'">毛坯废品率</th> -->
			</tr></thead>
		</table>
		<div id="datagridToolbar" style="padding:5px;">
			<div>
				<c:if test="${vendor == false }">
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="blankScrap.add()">新增</a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-tab_edit',plain:true" onclick="blankScrap.mod()">修改</a>	
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="blankScrap.import()">数据导入</a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="blankScrap.publish()">发布</a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="blankScrap.calcul()">计算</a>
					<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="blankScrap.reportForms()">报表</a> -->
				</c:if>  		
			</div>
			<div>
				<form id="form-blankScrap-search" method="post">
					<c:if test="${vendor == true }">
						<input type="hidden" name="search-EQ_state" value="1" />
					</c:if>
				生产厂家：<input type="text" name="search-LIKE_manufacturer" class="easyui-textbox" style="width:150px;"/>
				图号：<input type="text" name="search-LIKE_drawingNo" class="easyui-textbox" style="width:150px;"/>
				零件名称：<input type="text" name="search-LIKE_partsName" class="easyui-textbox" style="width:150px;"/>
				状态：<select name="search-EQ_state" data-options="editable:false" class="easyui-combobox" style="width:150px;">
	                   	<option value="">-全部-</option>
	                   	<option value="0">登记</option>
	                   	<option value="1">发布</option>
	                   </select>
				<br/><br/>
				月份：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:150px;"/>
				<!-- 开始时间：<input class="easyui-datebox" name="search-GTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
				<input class="easyui-datebox" name="search-LTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="blankScrap.search()">查询</a>
					<c:if test="${vendor == false }">
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-blankScrap-search').form('reset')">重置</a>
					</c:if>
				
				</form>
			</div>
		</div>



<!-- 新增模块 -->	
		<div id="add" class="easyui-dialog" title="毛坯废品信息管理" data-options="modal:true,closed:true,buttons:'#dialog-adder-a'">
		     <form id="addform"  method="post">
				<input type="hidden" id="id" name="id" value="">
				<table style="width: 100%;">
					<tr>
						<th>生产厂家编码：</th>
						<th><input type="text" id="manufacturerCode" name="manufacturerCode" data-options="readonly:true" class="easyui-textbox" style="width:150px;"/></th>
						<th>生产厂家：</th>
						<th><input type="text" id="manufacturer" name="manufacturer" data-options="readonly:true" class="easyui-textbox" style="width:150px;"/></th>
						<th><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="blankScrap.choose1()">选择供应商</a></th>
					</tr>
					<tr>
						<th>图号：</th>
						<th><input type="text" id="drawingNo" name="drawingNo" data-options="readonly:true" class="easyui-textbox" style="width:150px;"/></th>
						<th>零件名称：</th>
						<th><input type="text" id="partsName" name="partsName"  data-options="readonly:true" class="easyui-textbox" style="width:150px;"/></th>
						<th><a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="blankScrap.choose2()">选择物料</a></th>
					</tr>
					<tr >
						<th>不合格数量：</th>
						<th><input type="text" name="amount" class="easyui-numberbox" style="width:150px;" data-options="required:true"/></th>
						<th>处置：</th>
						<th><input type="text" name="handle" class="easyui-textbox" style="width:150px;" data-options="required:true"/></th>
						<th></th>
					</tr>
					<tr >
						<!-- <th>开始时间：</th>
						<th><input class="easyui-datetimebox" name="startTime" data-options="editable:false,required:true" value="" style="width:150px"></th> -->
						<th>总数量：</th>
						<th><input type="text" name="totalAmount" class="easyui-numberbox" style="width:150px;" data-options="required:true"/></th>
						<th>时间：</th>
						<th><input class="easyui-datetimebox" name="endTime" data-options="editable:false,required:true" value="" style="width:150px"></th>
						<th></th>
					</tr>
					<tr >
						<th>不合格现象描述：</th>
						<th colspan="4"><input type="text" name="unqualifiedPhenomenon" class="easyui-textbox" style="width:450px;"/></th>
					</tr>
				</table>
			 </form>
			 <div id="dialog-adder-a">
			    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="blankScrap.addsubmit()">提交</a>
			 </div>
		</div>
<!-- 结束 -->		
		
		
		
<!-- 数据导入 -->
		<div id="winImport" class="easyui-dialog" title="数据导入" style="width:400px;height:200px" data-options="iconCls:'icon-disk_upload',modal:true,closed:true,buttons:'#dialog-imp-a'" >
			<form id="formImport" method="post" enctype="multipart/form-data" class="baseform"> 
			    <div>
		          <label>文件：</label>
		          <input type=file id="file" name="planfiles" />
		        </div>
			    <div>
		          <label>模板：</label>
		          <a href="javascript:;" style="font-size: 20px;" onclick="File.download('WEB-INF/templates/BlankScrap.xls','blankScrap')">数据导入.xls</a>
		        </div>
			</form>  
			<div id="dialog-imp-a">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="blankScrap.saveImp()">提交</a>
			</div>
		</div>
<!-- 结束 -->			
		
		
		
<!-- 选择供应商 -->		
		<div id="choose1" class="easyui-dialog" title="选择供应商" data-options="modal:true,closed:true,buttons:'#dialog-choose-a'"  style="width: 50%;height: 50%;">
			<table id="datagrid1" class="easyui-datagrid"
				   data-options="fit:true,method:'post',singleSelect:false,toolbar:'#chooseToolbar1',
				   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
					
				<thead>
					<tr>
				    	<th data-options="field:'id',checkbox:true">供应商代码</th>
				        <th data-options="field:'org.code'">供应商代码</th>
				        <th data-options="field:'name'">供应商名称</th>
				        <th data-options="field:'countryText'">国家</th>
		                <th data-options="field:'provinceText'">省份</th>
				    </tr>
				</thead>
			</table>
		    <div id="chooseToolbar1" style="padding:5px;">
			 	<div>
					<form id="formsearchVen" method="post">
						 供应商代码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:150px;"/>
	                                         供应商名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:150px;"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="blankScrap.searchVen()">查询</a>
					</form>
				</div>
		    </div>
			<div id="dialog-choose-a">
			    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="blankScrap.bringBackVen()">选择带回</a>
			</div>
		</div>
<!-- 结束 -->		



<!-- 选择物料 -->		
		<div id="choose2" class="easyui-dialog" title="选择物料"data-options="modal:true,closed:true,buttons:'#dialog-choose-b'"  style="width: 50%;height: 50%;">
			<table id="datagrid2" class="easyui-datagrid"
				   data-options="fit:true,method:'post',singleSelect:false,toolbar:'#chooseToolbar2',
				   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">
					
				<thead>
					<tr>
				    	<th data-options="field:'id',checkbox:true">物料ID</th>
				        <th data-options="field:'code'">物料编号</th>
				        <th data-options="field:'name'">物料名称</th>
				    </tr>
				</thead>
			</table>
			<div id="chooseToolbar2" style="padding:5px;">
			 	<div>
					<form id="formsearchMat" method="post">
						物料编号：<input type="text" name="search-LIKE_code" class="easyui-textbox" style="width:150px;"/>
						物料名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:150px;"/>
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="blankScrap.searchMat()">查询</a>
					</form>
				</div>
			</div>
			<div id="dialog-choose-b">
			    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="blankScrap.bringBackMat()">选择带回</a>
			</div>
		</div>
<!-- 结束 -->	
	
		
		
<!-- 报表  -->
		<div id="bsReportForms" class="easyui-dialog" title="报表" style="width:80%;height:100%" data-options="iconCls:'icon-add',modal:true,closed:true">  
			<table id="datagrid-bsReportForms-list"  class="easyui-datagrid"
				   data-options="fit:true,
				   url:'${ctx}/manager/qualityassurance/blankScrap/${vendor}',method:'post',singleSelect:false,
				   toolbar:'#bsReportFormsToolbar',						
				   pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList">          
				
				<thead>
					<tr>
						<th data-options="field:'id',checkbox:true"></th>
						<th data-options="field:'startTime'">开始时间</th>
						<th data-options="field:'endTime'">结束时间</th>
						<th data-options="field:'manufacturer'">生产厂家</th>
						<th data-options="field:'drawingNo'">图号</th>
						<th data-options="field:'partsName'">零件名称</th>
						<th data-options="field:'unqualifiedAmount'">不合格数量</th>
						<th data-options="field:'total'">供货总数量</th>
						<th data-options="field:'rejectionRate'">毛坯废品率</th>
					</tr>
				</thead>
			</table>
			
			<div id="bsReportFormsToolbar" style="padding: 5px;">
				<div>
					<c:if test="${vendor == false }">
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="blankScrap.exportExcle()">导出</a>
					</c:if>  		
				</div>
				<div>
					<form id="form-bsReportForms-search" method="post">
						生产厂家：<input type="text" name="search-LIKE_manufacturer" class="easyui-textbox" style="width: 150px;" /> 
						图号：<input type="text" name="search-LIKE_drawingNo" class="easyui-textbox" style="width: 150px;" /> 
						零件名称：<input type="text" name="search-LIKE_partsName" class="easyui-textbox" style="width: 150px;" /></br>
				                开始时间：<input class="easyui-datebox" name="search-GTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;到&nbsp;&nbsp;
						<input class="easyui-datebox" name="search-LTE_startTime" data-options="showSeconds:true" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
						<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="blankScrap.searchRf()">查询</a>
						<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-bsReportForms-search').form('reset')">重置</a>
					</form>
				</div>
			</div>
		</div>	
<!-- 结束 -->	




	</body>
</html>