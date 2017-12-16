<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>基础数据</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
	<script type="text/javascript" src="${ctx}/static/script/performance/sourceData.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/performance/sourcedata',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="30px" data-options="field:'id',checkbox:true"></th>
        <th width="80px" data-options="field:'operate',formatter:PerformanceSourceData.optFmt"></th>
        <th width="140px" data-options="field:'code'">编码</th>
        <th width="110px" data-options="field:'performanceModelName'">类型</th>
        <th width="180px" data-options="field:'orgName'">供应商名称</th>
        <th width="120px" data-options="field:'orgCode'">供应商编码</th>
        <th width="100px" data-options="field:'brandName',formatter:brandris">品牌名称</th>
        <th width="100px" data-options="field:'factoryName'">工厂</th>
        <th width="100px" data-options="field:'matTypeCode'">零部件编码</th>
        <th width="100px" data-options="field:'matTypeName'">零部件分类</th>
        <th width="140px" data-options="field:'dimName'">维度</th>
        <th width="140px" data-options="field:'indexName'">指标</th>
        <th width="350px" data-options="field:'keyName'">要素</th>
        <th width="70px" data-options="field:'keyValue'">要素值</th>
        <th width="100px" data-options="field:'describe'">问题详细描述</th>
        <th width="130px" data-options="field:'attFileName',formatter:function(v,r,i) {
        	if(r.attFilePath == null || r.attFilePath == '')
        		return '';
        	return '<a href=javascript:; onclick=File.download(\''+ r.attFilePath+'\',\''+ r.attFileName +'\')>'+v+'</a>';
        }">附件</th>
        <th width="130px" data-options="field:'updateUserName'">操作人</th>
        <th width="130px" data-options="field:'createTime'">创建时间</th>
        <th width="130px" data-options="field:'assessTime'">评估时间</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
     <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="PerformanceSourceData.add()">新增</a>   
     <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-imp',plain:true" onclick="PerformanceSourceData.imp()">数据导入</a>    
     <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="PerformanceSourceData.del()">删除</a>    
      <form id="form-source-search" method="post">
      	<table>
      		<tr>
      			<td>类型:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_performanceModelName" value="" style="width:100px"></td>
      			<td>品牌:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_brandName" dvalue="" style="width:100px"></td>
      			<td>工厂:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_factoryName" style="width:100px"/></td>
      			<td>供应商编码:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_orgCode" dvalue="" style="width:100px"></td>
      			<td>供应商名称:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_orgName" style="width:100px"/></td>
      		</tr>
      		<tr>
      			<td>指标:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_indexName" type="text" style="width:100px;"/></td>
      			<td>维度:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_dimName" type="text" style="width:100px;"/></td>
      			<td>要素名称:</td>
      			<td><input class="easyui-textbox" name="search-LIKE_keyName" type="text"  style="width:100px;"/></td>
      		    <td rowspan="2">
      		    	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="PerformanceSourceData.search()">查询</a>
      		    	<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-source-search').form('reset')">重置</a>
      		    </td>
      		</tr>
      	</table>
      </form>
    </div>
  </div>
  <div id="dialog-imp" class="easyui-dialog" title="数据导入" style="width:400px;height:200px" data-options="iconCls:'icon-application',modal:true,closed:true,buttons:'#dialog-imp-bt'" >
    <div itemId="ct">
		<form id="dialog-imp-form" method="post" enctype="multipart/form-data" class="baseform"> 
			    <div>
		          <label>文件：</label>
		          <input class="easyui-filebox" required="true"  name="impFile" />
		        </div>
			    <div>
		          <label>模板：</label>
		          <span class="text"><a  href="javascript:;"  onclick="File.download('WEB-INF/templates/performance/souceDataTemplate.xls','souceDataTemplate')">数据导入.xls</a></span>
		        </div>
			    <div itemId="logDiv" class="hide">
		          <label>日志：</label>
		          <span class="text"><a itemId="logFile"  href="javascript:;" >导入日志</a></span>
		        </div>
		</form>
	</div>  
	    <div id="dialog-imp-bt">
			<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="PerformanceSourceData.impSubmit()">提交</a>
			<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#dialog-imp-form').form('reset')">重置</a>
		</div>
	</div>
	<div id="dialog-edit" class="easyui-dialog" title="新增"
		data-options="modal:true,closed:true,buttons:'#dialog-edit-bt'" style="width: 58%;height: 90%">
		<div itemId="ct">
		 <form method="post" itemId="form" id="dialog-edit-form" class="baseform" enctype="multipart/form-data">
		 <input  type="hidden" name="id" itemId="id" value=""/>
		 	<div>
	          <label>编号：</label>
	          <input class="easyui-textbox" data-options="disabled:true,width:200" name="code"/>
	        </div>
		 	<div>
	          <label>绩效类型：</label>
	          <select  id="performanceModelId" name="performanceModelId" data-options="required:true" style="width:200px; border: 1px solid #ccc"> </select>
	        </div>
		 	<div>
	          <label>供应商：</label>
	         
	          <input class="easyui-textbox" data-options="required:true,width:200,editable:false" id="orgName" name="orgName"/>
	          <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="lookOpen()">选择供应商</a>
	        </div>
		 	<div>
	          <label>品牌：</label>
	          
	          <input class="easyui-textbox" data-options="required:true,width:200,editable:false" id="brandName" name="brandName"/>
	          <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="lookOpen2()">选择品牌</a>
	        </div>
		 	<div>
	          <label>工厂：</label>
	          
	          <input class="easyui-textbox" data-options="required:true,width:200,editable:false" id="factoryName" name="factoryName"/>
	          <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="lookOpen3()">选择工厂</a>
	        </div>
		 	<div>
	          <label>零部件名称：</label>
	          
	          <input class="easyui-textbox" data-options="required:true,width:200,editable:false" id="matTypeName" name="matTypeName"/>
	          <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-edit'" onclick="lookOpen4()">选择分类</a>
	        </div>
	        <div style="display: none;">
	        	<input class="easyui-textbox" id="orgId" name="orgId"/>
	        	<input class="easyui-textbox" id="brandId" name="brandId"/>
	        	<input class="easyui-textbox" id="factoryId" name="factoryId"/>
	        	<input class="easyui-textbox" id="matTypeId" name="matTypeId"/>
	        </div>
	        <div>
	          <label>维度：</label>
	          <select  id="dimId" name="dimId" class="easyui-combobox"  style="width:200px; border: 1px solid #ccc"> </select>
	        </div>
	        <div>
	          <label>指标：</label>
	          <%-- 
	          <input class="easyui-combobox" data-options="
	          url : '${ctx}/manager/vendor/performance/index/getList',
	          textField : 'indexName',
	          valueField : 'id'
	          " name="indexId"/>--%>
	          <select  id="indexId" name="indexId" class="easyui-combobox"  style="width:200px; border: 1px solid #ccc"> </select>
	        </div>
		 	<div>
	          <label>要素：</label>
	          <select  id="keyName" name="keyName" style="width:200px; border: 1px solid #ccc"> </select>
	        </div>
		 	<div>
	          <label>要素值：</label>
	          <input class="easyui-textbox" data-options="required:true,width:200" name="keyValue"/>
	        </div>
		 	<div>
	          <label>问题详细描述：</label>
	          <input class="easyui-textbox" name="describe" data-options="required:true,width:200" />
	        </div>
		 	<div>
	          <label>附件：</label>
	          <input class="easyui-filebox"  name="attFile" data-options="width:200"/>
	        </div>
		 	<div>
	          <label>评估时间：</label>
	          <input class="easyui-datebox"  name="assessTime" data-options="width:200"/>
	        </div>
		 </form>
		 </div>
		 <div id="dialog-edit-bt">
		    <a href="javascript:;" class="easyui-linkbutton"  data-options="iconCls:'icon-save'" onclick="PerformanceSourceData.submit()">提交</a>
		 </div>
	</div>
	<div id="udd" class="easyui-dialog" title="更新公告" style="width:60%;height:98%"   
        data-options="iconCls:'icon-save',resizable:true,modal:true,closed:true">   
    </div> 
	<div id="org" class="easyui-dialog" title="添加供应商" style="width:60%;height:98%"   
	        data-options="iconCls:'icon-add',modal:true,closed:true">   
	    <table id="datagridss"  data-options="
	    fit:true,method:'post',singleSelect:false,
   		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
	    toolbar:'#ttt'
	  ">
	    <thead>
	      <tr>
	        <th width="30px" data-options="field:'id',checkbox:true">供应商代码</th>
	        <th width="40%" data-options="field:'code'">供应商代码</th>
	        <th width="40%" data-options="field:'name'">供应商名称</th>
	      </tr>
	    </thead>
	  </table>
	  <div id="ttt">
	    <div>
	      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe('org')">确认</a>    
	      <form id="formdatagridss" method="post">
                             供应商代码:<input class="easyui-textbox" name="search_LIKE_code" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search_LIKE_name" type="text" style="width:80px;"/>
	       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch('datagridss')">查询</a>      
	      </form>
	    </div>
	  </div>   
	</div> 
	<div id="brand" class="easyui-dialog" title="添加品牌" style="width:60%;height:98%"   
	        data-options="iconCls:'icon-add',modal:true,closed:true">   
	    <table id="datagridss2" data-options="
	    fit:true,method:'post',singleSelect:false,
   		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
	    toolbar:'#ttt2'
	  ">
	    <thead>
	      <tr>
	        <th width="30px" data-options="field:'id',checkbox:true">品牌代码</th>
	        <th width="25%" data-options="field:'code'">品牌代码</th>
	        <th width="25%" data-options="field:'name'">品牌名称</th>
	        <th width="25%" data-options="field:'rangename',formatter:function(v,r){if(r.range!=null) return r.range.name}">业务类型</th>
	      </tr>
	    </thead>
	  </table>
	  <div id="ttt2">
	    <div>
	      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe2('brand')">确认</a>    
	      <form id="formdatagridss2" method="post">
                             品牌代码:<input class="easyui-textbox" name="search_LIKE_code" type="text" style="width:80px;"/>
                             品牌名称:<input class="easyui-textbox" name="search_LIKE_name" type="text" style="width:80px;"/>
	       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch('datagridss2')">查询</a>      
	      </form>
	    </div>
	  </div>   
	</div> 
	<div id="factory" class="easyui-dialog" title="添加工厂" style="width:60%;height:98%"   
	        data-options="iconCls:'icon-add',modal:true,closed:true">   
	    <table id="datagridss3" data-options="
	    fit:true,method:'post',singleSelect:false,
   		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
	    toolbar:'#ttt3'
	  ">
	    <thead>
	      <tr>
	        <th width="30px" data-options="field:'id',checkbox:true">工厂代码</th>
	        <th width="40%" data-options="field:'code'">工厂代码</th>
	        <th width="40%" data-options="field:'name'">工厂名称</th>
	      </tr>
	    </thead>
	  </table>
	  <div id="ttt3">
	    <div>
	      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe3('factory')">确认</a>    
	      <form id="formdatagridss3" method="post">
                             工厂代码:<input class="easyui-textbox" name="search_LIKE_code" type="text" style="width:80px;"/>
                             工厂名称:<input class="easyui-textbox" name="search_LIKE_name" type="text" style="width:80px;"/>
	       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch('datagridss3')">查询</a>      
	      </form>
	    </div>
	  </div>   
	</div> 
	<div id="matType" class="easyui-dialog" title="添加分类" style="width:60%;height:98%"   
	        data-options="iconCls:'icon-add',modal:true,closed:true">   
	    <table id="datagridss4" data-options="
	    fit:true,method:'post',singleSelect:false,
   		pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
	    toolbar:'#ttt4'
	  ">
	    <thead>
	      <tr>
	        <th width="30px" data-options="field:'id',checkbox:true">分类代码</th>
	        <th width="40%" data-options="field:'code'">分类代码</th>
	        <th width="40%" data-options="field:'name'">分类名称</th>
	      </tr>
	    </thead>
	  </table>
	  <div id="ttt4">
	    <div>
	      <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-disk_upload',plain:true" onclick="xuanzhe4('matType')">确认</a>    
	      <form id="formdatagridss4" method="post">
                             分类代码:<input class="easyui-textbox" name="search_LIKE_code" type="text" style="width:80px;"/>
                             分类名称:<input class="easyui-textbox" name="search_LIKE_name" type="text" style="width:80px;"/>
	       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="addsearch('datagridss4')">查询</a>      
	      </form>
	    </div>
	  </div>   
	</div> 
	<script type="text/javascript">
	$(function() {
		$('#datagrid').datagrid();
		//绩效模型
	    $('#performanceModelId').combobox({
	  			url : '${ctx}/manager/vendor/performance/model/getEnable',
	  			editable : false, //不可编辑状态
	  			cache : false,
	  			panelHeight : '120',//自动高度适合
	  			valueField : 'id',
	  			textField : 'name',
	  	
	  			onHidePanel : function() {
	  				$("#dimId").combobox("setValue", '');
	  				$("#indexId").combobox("setValue", '');
	  				$("#keyName").combobox("setValue", '');
	  				var performanceModelId = $('#performanceModelId').combobox('getValue');
	  				$.ajax({
	  					type : "POST",
	  					url : "${ctx}/manager/vendor/performance/dimensions/getListByMod/" + performanceModelId,
	  					cache : false,
	  					dataType : "json",
	  					success : function(data) {
	  						$("#dimId").combobox("loadData", data);
	  					},
	  					error:function(data) {
	  						$.messager.fail(data.responseText);
	  					}
	  				});
	  			}
	  	});
		
		//维度
		$('#dimId').combobox({
			url : '${ctx}/manager/vendor/performance/dimensions/getList',
			editable : false, //不可编辑状态
			cache : false,
			panelHeight : '120',//自动高度适合
			valueField : 'id',
			textField : 'dimName',
	
			onHidePanel : function() {
				$("#indexId").combobox("setValue", '');
				$("#keyName").combobox("setValue", '');
				var dimId = $('#dimId').combobox('getValue');
					$.ajax({
						type : "POST",
						url : "${ctx}/manager/vendor/performance/index/getList/" + dimId,
						cache : false,
						dataType : "json",
						success : function(data) {
							$("#indexId").combobox("loadData", data);
						}
					});
			}
		});
		//指标
		$('#indexId').combobox({ 
		      //url:'itemManage!categorytbl', 
		      editable:false, //不可编辑状态
		      cache: false,
		      panelHeight: '120',//自动高度适合
		      valueField:'id',   
		      textField:'indexName',
		      
		      onHidePanel : function() {
					$("#keyName").combobox("setValue", '');
					var indexId = $('#indexId').combobox('getValue');
					if(indexId!='')
					{
						$.ajax({
							type : "POST",
							url : "${ctx}/manager/vendor/performance/index/getkey/" + indexId,
							cache : false,
							dataType : "json",
							success : function(data) {
								$("#keyName").combobox("loadData", data);
							},
							error:function(data) {
								$.messager.fail(data.responseText);
							}
						});
					}
					
				}
		 });
		//要素
		 $('#keyName').combobox({ 
		      //url:'itemManage!categorytbl', 
		      editable:false, //不可编辑状态
		      cache: false,
		      panelHeight: '120',//自动高度适合
		      valueField:'key',   
		      textField:'val'
		 });
	});
	function brandris(v,r){
		if(r.bussinessRange) 
			return r.bussinessRange.name+"("+r.bussinessRange.range.name+")";
	}
	function lookOpen() {
		clean($("#orgId"));
		$('#org').window('open');
		$('#datagridss').datagrid({url:'${ctx}/manager/admin/org?search_EQ_roleType=1'});
	};
	function lookOpen2() {
		clean($("#brandId"));
		var id=$("#orgId").val();
		if(id==null||id=="")
		{
			$.messager.alert('提示','请选择供应商！','info');
			return false;
		}
		$('#brand').window('open');
		$('#datagridss2').datagrid({url:'${ctx}/manager/vendor/materialSupplyRel/getMSRelList/'+id});
	};
	function lookOpen3() {
		clean($("#factoryId"));
		var vid=$("#orgId").val();
		var bid=$("#brandId").val();
		if(bid==null||bid=="")
		{
			$.messager.alert('提示','请选择品牌！','info');
			return false;
		}
		$('#factory').window('open');
		$('#datagridss3').datagrid({url:'${ctx}/manager/basedata/factory/getFactorySelects/'+vid+'/'+bid});
	};
	function lookOpen4() {
		clean($("#matTypeId"));
		var vid=$("#orgId").val();
		var bid=$("#brandId").val();
		var fid=$("#factoryId").val();
		if(fid==null||fid=="")
		{
			$.messager.alert('提示','请选择工厂！','info');
			return false;
		}
		$('#matType').window('open');
		$('#datagridss4').datagrid({url:'${ctx}/manager/basedata/factory/getMaterialSelects/'+vid+'/'+bid+"/"+fid});
	};
	function addsearch(name) {
		var searchParamArray = $('#form'+name).serializeArray();
		var searchParams = $.jqexer.formToJson(searchParamArray);
		$('#'+name).datagrid('load',searchParams);
	}
	function  xuanzhe(id) {
		var selections = $('#datagridss').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条记录！','info');
			return false;
		}
		$("#"+id+"Name").textbox('setValue',selections[0].name);
		$("#"+id+"Id").textbox('setValue',selections[0].id);
		$('#'+id).window('close');
	}
	function  xuanzhe2(id) {
		var selections = $('#datagridss2').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条记录！','info');
			return false;
		}
		$("#"+id+"Name").textbox('setValue',selections[0].name+"("+selections[0].range.name+")");
		$("#"+id+"Id").textbox('setValue',selections[0].id);
		$('#'+id).window('close');
	}
	function  xuanzhe3(id) {
		var selections = $('#datagridss3').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条记录！','info');
			return false;
		}
		$("#"+id+"Name").textbox('setValue',selections[0].name);
		$("#"+id+"Id").textbox('setValue',selections[0].id);
		$('#'+id).window('close');
	}
	function  xuanzhe4(id) {
		var selections = $('#datagridss4').datagrid('getSelections');
		if(selections.length==0){
			$.messager.alert('提示','没有选择任何记录！','info');
			return false;
		}
		if(selections.length>1){
			$.messager.alert('提示','只能选取一条记录！','info');
			return false;
		}
		$("#"+id+"Name").textbox('setValue',selections[0].name);
		$("#"+id+"Id").textbox('setValue',selections[0].id);
		$('#'+id).window('close');
	}
	
	function clean(obj) {
		if(obj.attr('id') == 'orgId') {
			$("#orgId").textbox("setValue", '');
			$("#orgName").textbox("setValue", '');
			$("#brandId").textbox("setValue", '');
			$("#brandName").textbox("setValue", '');
			$("#factoryId").textbox("setValue", '');
			$("#factoryName").textbox("setValue", '');
			$("#matTypeId").textbox("setValue", '');
			$("#matTypeName").textbox("setValue", '');
		} else if(obj.attr('id') == 'brandId') {
			$("#brandId").textbox("setValue", '');
			$("#brandName").textbox("setValue", '');
			$("#factoryId").textbox("setValue", '');
			$("#factoryName").textbox("setValue", '');
			$("#matTypeId").textbox("setValue", '');
			$("#matTypeName").textbox("setValue", '');
		} else if(obj.attr('id') == 'factoryId') {
			$("#factoryId").textbox("setValue", '');
			$("#factoryName").textbox("setValue", '');
			$("#matTypeId").textbox("setValue", '');
			$("#matTypeName").textbox("setValue", '');
		}else if(obj.attr('id') == 'matTypeId') {
			$("#matTypeId").textbox("setValue", '');
			$("#matTypeName").textbox("setValue", '');
		}
	}
	</script>
</body>
</html>
