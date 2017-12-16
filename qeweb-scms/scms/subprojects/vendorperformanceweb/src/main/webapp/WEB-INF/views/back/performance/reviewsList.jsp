<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
	<title>参评设置</title>
	<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<script type="text/javascript">
	  var ctx = '${pageContext.request.contextPath}';
	</script>
 	<script type="text/javascript" src="${ctx}/static/script/performance/reviews.js"></script>
</head>

<body>
  <table id="datagrid" data-options="
    fit:true,
    url:'${ctx}/manager/vendor/vendorPerforReviews',method:'post',singleSelect:false,
    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100], 
    toolbar:'#tt'
  ">
    <thead>
      <tr>
        <th width="5%" data-options="field:'id',checkbox:true">参评ID</th>
        <th width="25%" data-options="field:'code',formatter:function(v,r){if(r.vendorBaseInfoEntity) {if(r.vendorBaseInfoEntity.org){return r.vendorBaseInfoEntity.org.code;}} else ''}">供应商代码</th>
        <th width="25%" data-options="field:'name',formatter:function(v,r){if(r.vendorBaseInfoEntity) return r.vendorBaseInfoEntity.name; else ''}">供应商名称</th>
        <th width="20%" data-options="field:'vendorPhaseName',formatter:function(v,r){if(r.vendorBaseInfoEntity) {if(r.vendorBaseInfoEntity.vendorPhase){return r.vendorBaseInfoEntity.vendorPhase.name;}} else ''}">供应商阶段</th>
        <th width="20%" data-options="field:'cycleName',formatter:function(v,r){if(r.cycleEntity) return r.cycleEntity.cycleName; else ''}">周期</th>
        <th width="5%" data-options="field:'joinStatus',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'yesOrNo',true);}">是否参评</th>
      </tr>
    </thead>
  </table>
  <div id="tt">
    <div>
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-accept',plain:true" onclick="reviews.release()">参评</a>  
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-reload',plain:true" onclick="reviews.dels()">取消参评</a>   
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="reviews.xuanzhe()">选择供应商</a>   
	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-remove',plain:true" onclick="reviews.remove()">删除供应商</a>   
      <form id="form" method="post">
                             供应商编码:<input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.code" type="text" style="width:80px;"/>
                             供应商名称:<input class="easyui-textbox" name="search-LIKE_vendorBaseInfoEntity.name" type="text" style="width:80px;"/>
                             是否参评:
                <select name="search-EQ_joinStatus" class="easyui-combobox" style="width:80px;">
                	<option value="">请选择</option>
                	<option value="1">是</option>
                	<option value="0">否</option>
                </select>   
       <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="reviews.search()">查询</a>   
       <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form').form('reset')">重置</a>     
      </form>
    </div>
  </div>
<form id="form-export">
</form>
<div id="dialog-reviews-save" class="easyui-dialog" title="选择周期" data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb'">
	<form id="form-reviews-save" method="post">
			<input type="hidden" id="ids" name="ids" value=""/>
				<table style="text-align: left;padding:5px;float: left;" cellpadding="5">
				<tr>
					<td>周&nbsp;期&nbsp;编&nbsp;码&nbsp;:</td><td>
						<input id="cc" class="easyui-combobox" name="cycid"  style="width:80px;" data-options="required:true,editable:false"/>
					</td>
				</tr>
			</table>
			<div id="dialog-adder-bb">
				<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="reviews.save()">提交</a>
				<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-reviews-save').form('reset')">重置</a>
			</div>
	</form>
</div>
<div id="dialog-reviews-vendor" title="选择供应商带回" data-options="iconCls:'icon-add',modal:true,closed:true" style="width: 100%;height: 100%">
    	<input type="hidden" id="numberGS" value="" />
    	<table style="width: 100%;" id="blistssssss" class="easyui-datagridx"	
			data-options="fit:true,method:'post',singleSelect:false,url:'${ctx}/manager/vendor/vendorInfor',
			pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],toolbar:'#tt2'"
			>
			<thead><tr>
		        <th width="5%" data-options="field:'id',checkbox:true">供应商代码</th>
		        <th width="20%" data-options="field:'code'">供应商代码</th>
		        <th width="20%" data-options="field:'name'">供应商名称</th>
		        <th width="15%" data-options="field:'property',formatter:function(v,r,i){if(r.property==1) return '国企'; if(r.property==2) return'独资';if(r.property==3) return '合资';if(r.property==4) return '民营';}">企业属性</th>
		        <th width="15%" data-options="field:'countryText'">国家</th>
		        <th width="10%" data-options="field:'provinceText'">省份</th>
		        <th width="10%" data-options="field:'vendorPhase',formatter:function(v,r){if(r.vendorPhase) return r.vendorPhase.name; else ''}">阶段</th>
		      </tr></thead>
		</table>
		 <div id="tt2" style="height: 55%">
		    <div  style="height: 100%">
		    <div  style="height: 20%">
		    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BaseVendor.xuanzhe1()">勾选带回供应商</a>
		    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BaseVendor.xuanzhe2()">根据条件带回供应商</a>
		    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="BaseVendor.xuanzhe3()">带回全部供应商</a>
		    <font style="color: #F00">提示：选择供应商的带回时，必须选择下面的周期</font>
		    </div>
		     <div  style="height: 60%">
		    	<table id="datagrid3" class="easyui-datagridx" data-options="
		    	 fit:true,title:'选择参评周期',
				    url:'${ctx}/manager/vendor/performance/cycle?search-EQ_abolished=0',method:'post',singleSelect:false,
				    pagination:true,rownumbers:true,pageSize:15,pageList:[15,30,50,100],
				    toolbar:'#tt3' 
				  ">
				    <thead>
				      <tr>
				        <th width="5%" data-options="field:'id',checkbox:true">周期ID</th>
				        <th width="20%" data-options="field:'code'">周期编码</th>
				        <th width="20%" data-options="field:'cycleName'">周期名称</th>
				        <th width="15%" data-options="field:'initDates'">准备天数</th>
				        <th width="15%" data-options="field:'fixDates'">调整天数</th>
				        <th width="20%" data-options="field:'remarks'">备注</th>
				        <th width="5%" data-options="field:'abolished',align:'center',formatter:function(v,r,i){return StatusRender.render(v,'abolish',true);}">生效状态</th>
				      </tr>
				    </thead>
				  </table>
				 </div>
				 <div  style="height: 20%">
		        <form id="form2" method="post">
                                 供应商代码:<input class="easyui-textbox" name="search-LIKE_code" type="text" style="width:150px;"/>
                                 供应商名称:<input class="easyui-textbox" name="search-LIKE_name" type="text" style="width:150px;"/>
                                 供应商阶段:<input id="ccdd" class="easyui-combobox" name="search-LIKE_vendorPhase.name"  style="width:150px;"/>
      		 	<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="BaseVendor.search()">查询</a>      
		      </form>
		      </div>
		    </div>
		    <form id="formeee" method="post">
			</form>
		 </div>	 
		 
	</div>
	<script type="text/javascript">
		$(function(){
			$('#datagrid').datagrid();
		});
	</script>
</body>
</html>
