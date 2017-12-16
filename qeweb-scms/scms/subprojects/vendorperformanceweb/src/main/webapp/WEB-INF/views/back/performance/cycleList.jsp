<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>周期设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/cycle.js"></script>
</head>

<body>
  <table id="datagrid" class="easyui-datagridx" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/performance/cycle',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="10%" data-options="field:'id',checkbox:true">周期ID</th>
        <th width="20%" data-options="field:'nop',formatter:cycle.vfmt">操作</th>
        <th width="10%" data-options="field:'code'">周期编码</th>
        <th width="10%" data-options="field:'cycleName'">周期名称</th>
        <th width="10%" data-options="field:'initDates'">准备天数</th>
        <th width="10%" data-options="field:'fixDates'">调整天数</th>
        <th width="10%" data-options="field:'remarks'">备注</th>
        <th width="10%" data-options="field:'defaultPurchase'">默认采购额分界线</th>
        <th width="10%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="cycle.saveUpadte(0)">新增</a>
    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="cycle.purchase()">设置采购额分界线</a>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="cycle.release()">启用</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="cycle.dels()">作废</a>   
      <form id="form" method="post">
                             周期编码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
                             周期名称:<input class="easyui-textbox" name="search-LIKE_cycleName" type="text" style="width:80px;"/>
                             生效状态:
                         <select name="search-EQ_abolished" class="easyui-combobox" style="width:80px;">
                         	<option value="">请选择</option>
                         	<option value="0">生效</option>
                         	<option value="1">作废</option>
                         </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="cycle.search()">查询</a> 
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>     
      </form>
    </div>
  </div>
<form id="form-export">
</form>

<div id="dialog-cycle-purchase" title="物料采购额分界线" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 80%;height: 80%">
	
		<div id="cc" class="easyui-layout" style="width:100%;height:100%;height: 100%">   
		    <div data-options="region:'east'" style="width:50%;">
			<form id="form-cycle-purchase1" method="post">
				<input id="mts" name="mts" type="hidden" value="">
				<input type="hidden" id="cid" name="cid" value="0"/>
				<table id="daopetin" class="table table-bordered" >
						<thead  class="datagrid-header"><tr>
						<th style="width:30%">物料分类</th>
						<th style="width:30%">物料类别</th>
						<th style="width:30%">采购额分界线</th>
						<th style="width:10%">操作</th>
						</tr></thead>
				        <tbody  id="addupo">
				        </tbody>  
				</table>
				<div id="trueid" style="text-align: center;padding:5px;width: 100%;float: left;">
					<input type="button" value="提交"  style="width: 50px;height: 25px;" onclick="cycle.sumction()">
					<input type="reset" value="重置"  style="width: 50px;height: 25px;">
				</div>
			</form>
		    </div>   
		    <div data-options="region:'west'" style="width:50%;">
		    	<table style="width: 100%;" id="blistssssss" class="easyui-datagridx"
					data-options="fit:true,method:'post',singleSelect:false,
					pagination:true,rownumbers:true,pageSize:5,pageList:[5,10,15,20],toolbar:'#tt2'"
					>
								<thead><tr>
								<th width="10%" data-options="field:'id',checkbox:true">物料类别ID</th>
						        <th width="25%" data-options="field:'code'">物料类别编码</th>
						        <th width="25%" data-options="field:'name'">物料类别名称</th>
						        <th width="20%" data-options="field:'levelDescribe'">等级描述</th>
						        <th width="20%" data-options="field:'faname'">物料分类</th>
								</tr></thead>
				</table>
				 <div id="tt2">
				    <div>
				    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="cycle.xuanzhe1()">确认</a>
				      <form id="form2" method="post">
				                             物料类别编码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:80px;"/>
				                             物料类别名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:80px;"/>
				       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="cycle.search2()">查询</a>      
				      </form>
				    </div>
				    <form id="form-export2">
					</form>
				 </div>	
		    </div>   
		</div>
</div>
<div id="dialog-cycle-saveUpadte" class="easyui-dialog" title="周期" data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
	<form id="form-cycle-saveUpadte" method="post" class="baseform">
	<input type="hidden" id="id" name="id" value="0"/>
				<table style="text-align: left;padding:5px;float: left;" cellpadding="5">
				<tr>
					<td>周&nbsp;期&nbsp;编&nbsp;码&nbsp;:</td><td><input class="easyui-combobox" id="code" name="code" type="text"
						data-options="required:true,
						data:[{value:'month',text:'month'},{value:'quarter',text:'quarter'},
						{value:'halfyear',text:'halfyear'},
						{value:'year',text:'year'}
						]
						"
					/>
					</td>
				</tr>
				<tr>
					<td>周&nbsp;期&nbsp;名&nbsp;称:</td><td><input class="easyui-textbox" id="cycleName" name="cycleName" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>准&nbsp;备&nbsp;天&nbsp;数&nbsp;</td><td><input class="easyui-numberbox" id="initDates" name="initDates" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>调&nbsp;整&nbsp;天&nbsp;数&nbsp;</td><td><input class="easyui-numberbox" id="fixDates" name="fixDates" type="text"
						data-options="required:true"
					/></td>
				</tr>
				<tr>
					<td>默认的采购额分界线:</td><td><input class="easyui-numberbox" id="defaultPurchase" name="defaultPurchase" type="text"/></td>
				</tr>
				<tr>
					<td>备注:</td><td><input class="easyui-textbox" id="remarks" name="remarks" type="text"/></td>
				</tr>
			</table>
	</form>
		<div id="dialog-adder-bb">
			<a href="javascript:;" data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="cycle.submit()">提交</a>
			<a href="javascript:;" data-options="iconCls:'icon-reset'"class="easyui-linkbutton" onclick="$('#form-cycle-saveUpadte').form('reset')">重置</a>
		</div>
</div>
<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
</script>
</body>
</html>
