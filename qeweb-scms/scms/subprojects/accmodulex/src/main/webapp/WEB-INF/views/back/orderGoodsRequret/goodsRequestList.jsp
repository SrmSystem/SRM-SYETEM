<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title><spring:message code="vendor.orderGoodsRequret.planManagement"/><!-- 要货计划管理 --></title>
	<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<script type="text/javascript">
	//选择框，全选或者不选
	function checkAll(checked){
		var allCheckBoxs=document.getElementsByName("id") ;
		for (var i=0;i<allCheckBoxs.length ;i++){
		if(allCheckBoxs[i].type=="checkbox"){
			allCheckBoxs[i].checked=checked;
		}
		} 
	}
	
	function getNumFmt(v,r,i) {
		if(v=="" || v==null ){
			return "";
		}
 		if(v == "0.000" || v==0.000 ){
			return "0";
		}
		var num=v;
	    num = num.toString().replace(/\$|\,/g,'');  
	    // 获取符号(正/负数)  
	    sign = (num == (num = Math.abs(num)));  
	    num = Math.floor(num*Math.pow(10,3)+0.50000000001);  // 把指定的小数位先转换成整数.多余的小数位四舍五入  
	    cents = num%Math.pow(10,3);              // 求出小数位数值  
	    num = Math.floor(num/Math.pow(10,3)).toString();   // 求出整数位数值  
	    cents = cents.toString();               // 把小数位转换成字符串,以便求小数位长度  
	    // 补足小数位到指定的位数  
	    while(cents.length<3)  
	      cents = "0" + cents;  
	      // 对整数部分进行千分位格式化.  
	      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
	        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

	      return (((sign)?'':'-') + num + '.' + cents);   
	   
	   
	}

	//千分位，整数
	function getNumIntegerFmt(v,r,i) {
		if(v=="" || v==null){
			return 0;
		}
		var num=v;
	    num = num.toString().replace(/\$|\,/g,'');  
	    // 获取符号(正/负数)  
	    sign = (num == (num = Math.abs(num)));  
	    num = Math.floor(num*Math.pow(10,3)+0.50000000001);  // 把指定的小数位先转换成整数.多余的小数位四舍五入  
	    cents = num%Math.pow(10,3);              // 求出小数位数值  
	    num = Math.floor(num/Math.pow(10,3)).toString();   // 求出整数位数值  
	    cents = cents.toString();               // 把小数位转换成字符串,以便求小数位长度  
	   
	      // 对整数部分进行千分位格式化.  
	      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
	        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

	      return (((sign)?'':'-') + num);  
	   
	}
	
	</script>
</head>
<body style="margin:0;padding:0;">
	<table id="datagrid-goodsRequest-list" style="height: 100%">

	</table>
	<div id="goodsRequestListToolbar" style="padding:5px;">
		<div>
		 <c:if test="${vendor == false}">  
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sycGoodsRequest()"><spring:message code="vendor.orderGoodsRequret.synchronizePlan"/><!-- 同步要货计划 --></a>
		    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="publishGoodsRequestMain()"><spring:message code="vendor.posted"/><!-- 发布 --></a>
		 </c:if> 
		  <c:if test="${vendor == true}">  
		     <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="confirmMainGoodsRequest()"><spring:message code="vendor.confirm"/><!-- 确认 --></a>
		 </c:if> 
		 <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="exportGoodsRequest(${vendor})"><spring:message code="vendor.orderGoodsRequret.exportDeliveryPlan"/><!-- 导出要货计划 --></a>
		</div>
		<div>
			<form id="form-goodsRequest-search" method="post" style="margin-top:5px;">
			<spring:message code="vendor.orderGoodsRequret.estimatedDelivery"/><!-- 预计到货时间 -->：<input  id="startTime" class="easyui-datebox" name="search-GTE_rq" data-options="showSeconds:false,editable:false" value="" style="width:150px">&nbsp;&nbsp;<spring:message code="vendor.to"/><!-- 到 -->&nbsp;&nbsp;
						       <input class="easyui-datebox" id="endTime" name="search-LTE_rq" data-options="showSeconds:false,editable:false" value="" style="width:150px">&nbsp;&nbsp;&nbsp;&nbsp;
		    <spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --> ：  <input type="text"  id="material_codes"    name="search-IN_material.code" class="easyui-textbox "    style="width:180px;"/>
		    
		    
		    <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'" onclick="openMaterialS()"></a>
		  
		    <c:if test="${vendor == false}">  
		    <spring:message code="vendor.supplierCode"/><!-- 供应商编码 --> ：  <input type="text"  id="vendor_codes"    name="search-IN_vendor.code"  class="easyui-textbox" style="width:180px;"/>
		<!--     <input id="vendor_code"   name="search-IN_vendor.code"  value="" type="hidden"/> -->
		    <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'" onclick="openVendorS()"></a>
		    </c:if> 
		    </br>
		    <spring:message code="vendor.buyerBoard.purchasingGroup"/><!-- 采购组 --> ：  <input type="text" name="search-LIKE_group.name" class="easyui-textbox" style="width:80px;"/>
		     <c:if test="${vendor == false}">  
			<spring:message code="vendor.postStatus"/><!-- 发布状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_publishStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toReleased"/><!-- 待发布 --></option><option value="1"><spring:message code="vendor.published"/><!-- 已发布 --></option><option value="2"><spring:message code="vendor.orderplan.partRelease"/><!-- 部分发布 --></option></select>
			</c:if> 
			<spring:message code="vendor.orderplan.confirmStatus"/><!-- 确认状态 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_vendorConfirmStatus"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="0"><spring:message code="vendor.toConfirmed"/><!-- 待确认 --></option><option value="1"><spring:message code="vendor.confirmed"/><!-- 已确认 --></option> <option value="2"><spring:message code="vendor.partConfirmation"/><!-- 部分确认 --></option>   </select>
			<spring:message code="vendor.orderGoodsRequret.newProduct"/><!-- 新品 -->：<select class="easyui-combobox" data-options="editable:false" name="search-EQ_flag"><option value="">-<spring:message code="vendor.all"/><!-- 全部 -->-</option><option value="X"><spring:message code="vendor.orderGoodsRequret.newProduct"/><!-- 新品 --></option><option value="O"><spring:message code="vendor.orderGoodsRequret.nonNewProducts"/><!-- 非新品 --></option>  </select>
			
			<!-- 待办 --><input type="hidden" id="backlogId" name="search-IN_backlogId" value="${backlogId}"/><!-- 待办 -->
			<div>
				<table style="width: 100%">
					<tr align="right">
						<td>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchGoodsPlan()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a> 
							<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-goodsRequest-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>   
						</td>
					</tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
	
	
		 <!-- 同步要货加护 -->
 	<div id="win-sync-addoredit" class="easyui-window"
		style="width: 300px; height: 200px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder1-time'">
		<form id="form-sync-addoredit" method="post" onkeydown="if(event.keyCode==13)return false;" >
			<table style="text-align: right; padding: 5px; margin: auto;" cellpadding="5">
			<tr>
				<td ><spring:message code="vendor.orderplan.factoryCode"/><!-- 工厂编码 -->:</td>
				<td >
					<input  id ="factoryCode"    class="easyui-textbox"  name="factoryCode" value=""   data-options="required:true" />
				</td>
			</tr>
			<tr>
			<td ><spring:message code="vendor.materialCode"/><!-- 物料编码 -->:</td>
				<td>
				   <input  id ="materialcode"     type="hidden"  name="materialcode" value=""   data-options="required:true" />
					<input  id ="materialcodes"    class="easyui-textbox"     data-options="required:true" />
				
				</td>
				<td> <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'," onclick="sysOpenMaterialS()"></a></td>
			</tr>
			</table>
		</form>
		<div id="dialog-adder1-time">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="syncQry()"><spring:message code="vendor.orderplan.submit"/><!-- 提交 --></a> <a
				href="javascript:;" class="easyui-linkbutton"
				onclick="$('#form-sync-addoredit').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
		</div>
	</div>
	
  <!-- 物料的查询 -->
     	<div id="win-material-detail" class="easyui-window" title="选择物料" style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true">
		<div id="div_material_detail" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="choiceM()"><spring:message code="vendor.confirm"/><!-- 确认 --></a>
			</div>
			<div>
			      <a href="javascript:;"  class="easyui-linkbutton"   data-options="iconCls:'icon-add',plain:true"  onclick="loadContent1()">加载内容     <!-- 加载内容 --></a>:
                  <input type="text"   data-options=" prompt :  '将内容copy后，点击 加载内容  '  "   id = "copyMaterialContent1" class="easyui-textbox"     style="width:180px;"/>
			</div>
			
			<div>
			 <a class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"  onclick="addMaterial();" ><spring:message code="vendor.orderGoodsRequret.readClipboardContents"/><!-- 读取剪贴板内容 --></a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="insert()"><spring:message code="vendor.orderGoodsRequret.adds"/><!-- 添加 --></a>
		     <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="removeit()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
          <!--    <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="accept()">保存</a> -->
             <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="resetMaterial()"><spring:message code="vendor.orderGoodsRequret.emptying"/><!-- 清空 --></a>

			</div>
		</div>
		<div id="idGoodsRequestListMaterial2">
			<table id="datagrid-material-list" title="物料列表" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: onClickRow,
				rownumbers:true">
				<thead><tr>
			   <th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
				</tr></thead>
			</table>
		</div>
		
	</div>
</div> 
  
  <!--   同步物料的查询 -->
      	<div id="win-sysMaterial-detail" class="easyui-window" title="选择物料" style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true">
		<div id="div_sysMaterial-_detail" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="sysChoiceM()"><spring:message code="vendor.confirm"/><!-- 确认 --></a>
			</div>
			<div>
			     <a href="javascript:;"  class="easyui-linkbutton"   data-options="iconCls:'icon-add',plain:true"  onclick="loadContent2()">加载内容     <!-- 加载内容 --></a>:
                 <input type="text"   data-options=" prompt :  '将内容copy后，点击 加载内容  '  "   id = "copyMaterialContent2" class="easyui-textbox"     style="width:180px;"/>
			</div>
			
			<div>
             <a class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"  onclick="addMaterialSys();" ><spring:message code="vendor.orderGoodsRequret.readClipboardContents"/><!-- 读取剪贴板内容 --></a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="insertSys()"><spring:message code="vendor.orderGoodsRequret.adds"/><!-- 添加 --></a>
		     <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="removeitSys()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
<!--              <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="acceptSys()">保存</a> -->
             <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="resetMaterialSys()"><spring:message code="vendor.orderGoodsRequret.emptying"/><!-- 清空 --></a>
			</div>
		</div>
		<div id ="idGoodsRequestListMaterial">
			<table id="datagrid-sysMaterial-list" title="物料列表" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: onClickRowSys,
				rownumbers:true">
				<thead><tr>
			 		<th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
				</tr></thead>
			</table>
		</div>
	</div>
</div>
  
  
  
  
<!--   供应商的查询 -->
   	<div id="win-org-detail" class="easyui-window" title="选择供应商" style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true">
		<div id="div_org_detail" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="choiceV()"><spring:message code="vendor.orderGoodsRequret.determine"/><!-- 确定 --></a>
			</div>
			<div>
			     <a href="javascript:;"  class="easyui-linkbutton"   data-options="iconCls:'icon-add',plain:true"  onclick="loadContent3()">加载内容     <!-- 加载内容 --></a>:
                 <input type="text"   data-options=" prompt :  '将内容copy后，点击 加载内容  '  "   id = "copyVendorContent3" class="easyui-textbox"     style="width:180px;"/>
			</div>
			
			<div>
			 <a class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"  onclick="addVendor();" ><spring:message code="vendor.orderGoodsRequret.readClipboardContents"/><!-- 读取剪贴板内容 --></a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="insertV()"><spring:message code="vendor.orderGoodsRequret.adds"/><!-- 添加 --></a>
		     <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="removeitV()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
            <!--  <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="acceptV()">保存</a> -->
             <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="resetVendor()"><spring:message code="vendor.orderGoodsRequret.emptying"/><!-- 清空 --></a>
		
		
		
		   
		
			</div>
		</div>
		<div id="idGoodsRequestListVendor">
			<table id="datagrid-org-list" title="供应商列表" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: onClickRowV,
				rownumbers:true">
				<thead><tr>
					<th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code="vendor.coding"/><!-- 编码 --></th>
				</tr></thead>
			</table>
		</div>
		
	</div>
</div> 
	
	
		<!-- 供应商驳回窗口 -->
	<div id="win-reject" class="easyui-dialog" title='不满足' style="width:400px;height:170px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-bb2'">
		<div class="easyui-panel" data-options="fit:true">
			<form id="form-reject" method="post" >
				<input id="reject_ids" name="reject_ids" type="hidden"/>
				<table style="text-align: left;padding:5px;margin:auto;" cellpadding="5">
				<tr>
					<td><spring:message code="label.reson"/><!-- 原因 --><textarea rows="4" cols="50" id="reject_reason" name="reject_reason"></textarea></td> 
				</tr>
				</table>
				<div id="dialog-adder-bb2" style="text-align: center;">
					<a href="javascript:;"  data-options="iconCls:'icon-save'" class="easyui-linkbutton" onclick="reject()"><spring:message code="button.submit"/><!-- 提交 --></a>
					<a href="javascript:;"  data-options="iconCls:'icon-reload'" class="easyui-linkbutton" onclick="$('#form-reject').form('reset')"><spring:message code="button.reset"/><!-- 重置 --></a>
				</div>
			</form>
		</div>
	</div>
	
	
	
<script type="text/javascript">
$(function() {
	$('#startTime').datebox('setValue', formatterDate(new Date()));
	$('#endTime').datebox('setValue', formatterDate(getNextDay(30)));
	//渲染表格头及内容
	getPurchasePlanItemHead();
});


//得到当前日期
formatterDate = function(date) {
var day = date.getDate() > 9 ? date.getDate() : "0" + date.getDate();
var month = (date.getMonth() + 1) > 9 ? (date.getMonth() + 1) : "0"
+ (date.getMonth() + 1);
return date.getFullYear() + '-' + month + '-' + day;
};



//查询
function searchGoodsPlan(){
	document.getElementById("backlogId").value="";//清除待办
	//渲染表格头及内容
	getPurchasePlanItemHead();
}

var beginRq;
var endRq;
//渲染表格头及内容
function getPurchasePlanItemHead(){
	beginRq=$('#startTime').datebox('getValue');
	endRq=$('#endTime').datebox('getValue');
	if(null==beginRq || ''==beginRq || null==endRq || ''==endRq){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'预计到货时间的开始时间和结束时间不能为空！','error');
	    return false;
	}
	var diffDay=datedifference(beginRq,endRq);
	if(diffDay>30){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'预计到货时间的开始时间和结束时间不能不能大于30天！','error');
	    return false;
	}else if(diffDay<0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'预计到货时间的结束时间必须大于开始时间！','error');
	    return false;
	}
	
    $.ajax({  
    	url:'${ctx}/manager/order/goodsRequest/getRequestRowHeader/'+beginRq+'/'+endRq,
        async: false, // 注意此处需要同步，因为先绑定表头，才能绑定数据   
        type:'POST',  
        dataType:'json',  
        cache:false,
        success:function(datas){//获成功后，使用easyUi的datagrid去生成表格   	
        	var size=datas.size;
            var frozenColumns=[];
            var frozenarray =[];
            var columns=[];
            var array =[];
            
            for(var i=0;i<6;i++){
            	frozenarray.push({field:'',title:'',formatter:'',checkbox:''});
            }
            frozenColumns.push(frozenarray);
            
            frozenColumns[0][0]['field']='check';
            frozenColumns[0][0]['title']=function(v,r,i){return '<input id="checkboxId" type="checkbox" style="width:20px" onclick="checkAll(this.checked)"/>';};
            frozenColumns[0][0]['formatter']=function(v,r,i){if(r.type==2)  return '';else return '<input name="id" type="checkbox" style="width:20px"/>';};
  	         
  	         
            frozenColumns[0][1]['field']='operate';
            frozenColumns[0][1]['title']='操作';
            frozenColumns[0][1]['formatter']=function operateFmt(v,r,i){
	        	 if(r.type==2)  return '';else {
	        		 var  v= ""
	        			 var isVendor = ${vendor};
	        			 if( isVendor == false){
	        				 v='<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showModifyGoodsRequestDetail(\''+ r.flag +'\',\''+r.factoryId+'\',\''+r.purchasingGroupId+'\',\''+r.materialId+'\',\''+r.meins+'\',\''+r.vendorId+'\',\''+r.type+'\',\''+r.beginRq+'\',\''+r.endRq+'\','+${vendor}+');">'+'<spring:message code="vendor.orderGoodsRequret.modify"/></a>';/* 修改 */
	        			 }
	        			 var s='&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showGoodsRequestDetail(\''+ r.flag +'\',\''+r.factoryId+'\',\''+r.purchasingGroupId+'\',\''+r.materialId+'\',\''+r.meins+'\',\''+r.vendorId+'\',\''+r.type+'\',\''+r.beginRq+'\',\''+r.endRq+'\','+${vendor}+');">'+'<spring:message code="vendor.orderGoodsRequret.particulars"/></a>&nbsp;&nbsp;';/* 详情 */
	        			  
	        			  return  s+v;
	        		 
	        	 }
	    		};
	    		
	    	frozenColumns[0][2]['field']='factoryCode';
		    frozenColumns[0][2]['title']='工厂编码';
		    frozenColumns[0][2]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
		    
		    frozenColumns[0][3]['field']='materialCode';
	    	frozenColumns[0][3]['title']='物料号';
	    	frozenColumns[0][3]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
	    	
	    	frozenColumns[0][4]['field']='materialName';
	    	frozenColumns[0][4]['title']='物料描述';
	    	frozenColumns[0][4]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
	    	frozenColumns[0][4]['width']=120;
	    	
	    	frozenColumns[0][5]['field']='drawingNumber';
	    	frozenColumns[0][5]['title']='图号';
	    	frozenColumns[0][5]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
	    	
    	     
	    
	         
	    	 for(var i=0;i<size+8;i++){
	    	        array.push({field:'',title:'',formatter:'',checkbox:''});
	            }
	    	 columns.push(array);
	    	 
	    	columns[0][0]['field']='vendorConfirmStatus';
	    	columns[0][0]['title']='确认状态';
		    columns[0][0]['formatter']=function(v,r,i){if(r.type==2)  return '';else return StatusRender.render(v,'confirm',false);};
	  	         
		    columns[0][1]['field']='flag';
		    columns[0][1]['title']='是否新品';
		    columns[0][1]['formatter']=function(v,r,i){if(r.type==2)  return '';else if(v=='X') return '新品';else return '量产';};
		        
		    columns[0][2]['field']='groupName';
		    columns[0][2]['title']='采购组';
		    columns[0][2]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
	  	         
		    columns[0][3]['field']='meins';
		    columns[0][3]['title']='基本单位';
		    columns[0][3]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
		      
	    	
	    	columns[0][4]['field']='vendorName';
	    	columns[0][4]['title']='供应商名称';
	    	columns[0][4]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
	         
	    	columns[0][5]['field']='vendorCode';
	    	columns[0][5]['title']='供应商编码';
	    	columns[0][5]['formatter']=function(v,r,i){if(r.type==2)  return '';else return v;};
	         
	    	columns[0][6]['field']='publishStatus';
	    	columns[0][6]['title']='发布状态';
	    	columns[0][6]['formatter']=function(v,r,i){if(r.type==2)  return '';else return StatusRender.render(v,'publishStatus',false);};
	         
	    	columns[0][7]['field']='type';
	    	columns[0][7]['title']='类型';
	    	columns[0][7]['formatter']=function(v,r,i){if(v==1) return '计划到货数量';else return '剩余匹配量';};
    	     
	        
	    	  for(var i=8;i<size+8;i++){
	    	   var j=i-8;
    	       columns[0][i]['field']='col' + j;
    	       columns[0][i]['title']=datas['col' + j]; 
    	       columns[0][i]['formatter']=getNumFmt;
    	     }
    	    
        var searchParamArray = $('#form-goodsRequest-search').serializeArray();
    	var searchParams = $.jqexer.formToJson(searchParamArray);
        	$('#datagrid-goodsRequest-list').datagrid({    
        	    queryParams:searchParams,
        	    method:'post',
        	    singleSelect:false,
        	    toolbar:'#goodsRequestListToolbar',
        	    method:'post',
        	    singleSelect:false,
        	    pagination:true,
        	    rownumbers:true,
        	    fitColumns: true,
        	    pageSize:20,
        	    pageList:[20,50,100],
        	    frozenColumns:frozenColumns,
        	    columns:columns,
        	    url:'${ctx}/manager/order/goodsRequest/getRequestRowList/${vendor}'
        	});  
        }  
    });
}

//查看要货计划详情
function showGoodsRequestDetail(flag,factoryId,purchasingGroupId,materialId,meins,vendorId,type,beginRq,endRq,vendor) {
	//要货计划明细
		 $dialog = $('<div/>').dialog({     
		        title: '<spring:message code="vendor.orderGoodsRequret.needPlan"/>',    /*  要货计划 */
		        iconCls : 'pag-search',    
		        closed: true,     
		        cache: false,     
		        href: ctx + '/manager/order/goodsRequest/viewItemList/' + vendor+"?flag="+flag+"&factoryId="+factoryId+"&purchasingGroupId="+purchasingGroupId+"&materialId="+materialId+"&meins="+meins+"&vendorId="+vendorId+"&type="+type+"&beginRq="+beginRq+"&endRq="+endRq,     
		        modal: true,  
		        maximizable:true,
		        maximized:true,
		        onLoad:function(){  
		        	
		        },               
		        onClose:function(){
		            $(this).dialog('destroy');
		        },
		        buttons : [ 
		         ]  

		   });    
		  $dialog.dialog('open');
}

//查看要货计划详情
function showModifyGoodsRequestDetail(flag,factoryId,purchasingGroupId,materialId,meins,vendorId,type,beginRq,endRq,vendor) {
	//要货计划明细
		 $dialog = $('<div/>').dialog({     
		        title: '<spring:message code="vendor.orderGoodsRequret.needPlan"/>',    /*  要货计划 */
		        iconCls : 'pag-search',    
		        closed: true,     
		        cache: false,     
		        href: ctx + '/manager/order/goodsRequest/viewItemListModify/' + vendor+"?flag="+flag+"&factoryId="+factoryId+"&purchasingGroupId="+purchasingGroupId+"&materialId="+materialId+"&meins="+meins+"&vendorId="+vendorId+"&type="+type+"&beginRq="+beginRq+"&endRq="+endRq,     
		        modal: true,  
		        maximizable:true,
		        maximized:true,
		        height: 800, 
		        onLoad:function(){  
		        	
		        },               
		        onClose:function(){
		            $(this).dialog('destroy');
		        },
		        buttons : [ 
		         ]  

		   });    
		  $dialog.dialog('open');
}
/* 导出 */
function exportGoodsRequest(vendor){
	var searchParamArray = $('#form-goodsRequest-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
        $.messager.confirm('提示','确认导出吗?',function(result){
            if(result){
                $.ajax({ 
                	url : ctx+'/manager/order/goodsRequest/exportExcel/'+vendor, 
                    type : 'post', 
                    dataType:"json",
                    contentType : 'application/json',
                    data : JSON.stringify(searchParams), 
                    success : function(data){ 
                        if(!data.code) {
                            $.messager.alert('提示',data.msg,'info');
                            return false;
                        }else{
                            if(data.fileName == null){
                                $.messager.alert('提示','导出失败','info');
                            }else{
                                window.location.href=encodeURI(ctx+'/manager/order/goodsRequest/downloadExcel?fileName='+ data.fileName);
                            }
                        }
                    } 
                });
            }
        });
}



//同步要货计划
function sycGoodsRequest(){
		$('#win-sync-addoredit').dialog({
			iconCls:'icon-edit',
			title:'<spring:message code="vendor.orderGoodsRequret.synchronizePlan"/>'/* 同步要货计划 */
		});
		$('#form-sync-addoredit').form('clear');
		$('#win-sync-addoredit').dialog('open');
}


function syncQry(){
	var sucMeg = '<spring:message code="vendor.operationSuccessful"/>！';/* 操作成功 */
	var  factoryCode  =  document.getElementById("factoryCode").value;
    var materialcode =document.getElementById("materialcodes").value;
    if( (factoryCode == ""  || factoryCode  == null)   || ( materialcode == "" || materialcode == null  ) ){
    	$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderGoodsRequret.factoriesMaterialsEmpty"/>！'/* 工厂和物料不能为空 */,'error');
    	return;
    }
    
	var url = ctx+'/manager/order/goodsRequest/sycGoodsRequestByFactoryCodeAndMateriel/'+factoryCode+"/"+materialcode;
	$.messager.progress({
		title:'<spring:message code="vendor.prompting"/>',/* 提示 */
		msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	
  	$.ajax({
  		url:url,
        type: 'POST',
    	dataType:"json",
		contentType : 'application/json',
        success: function (data) {   
			$.messager.progress('close');
			var obj = data;
			try{
				debugger;
				if(obj.success){ 
					$.messager.show({
						title:'<spring:message code="vendor.news"/>',/* 消息 */
						msg: obj.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$('#win-sync-addoredit').dialog('close');
					$("#datagrid-goodsRequest-list").datagrid('reload');
				}else{
					$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,obj.message,'error');
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
        }
      });
}

//采购商发布要货计划  （批量）
function publishGoodsRequestMain(){

	var selections = $("#datagrid-goodsRequest-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].publishStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.ccPublished"/>！'/* 包含已发布记录无法重复发布 */,'error');
			return false;
		} 
		
	}

    var params = $.toJSON(selections);
    $.messager.progress({
		title:'<spring:message code="vendor.prompting"/>'/* 提示 */,
		msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	$.ajax({
		url:ctx + '/manager/order/goodsRequest/batchPublishGoodsRequest',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="vendor.news"/>',/* 消息 */
						msg: data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$("#datagrid-goodsRequest-list").datagrid('reload');
				}else{
					
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}


//采购商确认要货计划  （批量）
function confirmMainGoodsRequest(){
	var selections = $("#datagrid-goodsRequest-list").datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'<spring:message code="vendor.orderplan.noRecordSelected"/>！'/* 没有选择任何记录 */,'info');
		return false;
	}
	for(i = 0; i < selections.length; i ++) {
		if(selections[i].vendorConfirmStatus == 1) {
			$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'包含已确认的记录无法重复确认！','error');
			return false;
		} 
	}
    var params = $.toJSON(selections);
    $.messager.progress({
		title:'<spring:message code="vendor.prompting"/>',/* 提示 */
		msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	$.ajax({
		url:ctx + '/manager/order/goodsRequest/batchConfirmGoodsRequest',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.progress('close');
			try{
				if(data.success){ 
					$.messager.show({
						title:'<spring:message code="vendor.news"/>',/* 消息 */
						msg: data.message, 
						timeout:2000,
						showType:'show',
						style:{
							right:'',
							top:document.body.scrollTop+document.documentElement.scrollTop,
							bottom:''
						}
					});
					$("#datagrid-goodsRequestItem-list").datagrid('reload'); 
					$("#datagrid-goodsRequest-list").datagrid('reload');

					
				}else{
					
				}
			}catch (e) {
				$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,e,'error'); 
			} 
		}
	});
}



/******************************************** 同步物料的窗口的打开-开始******************************* */

/* 同步物料物料的窗口的打开 */
function sysOpenMaterialS() {
	
	$("#copyMaterialContent2").textbox('setValue','')//赋值
	
	$('#win-sysMaterial-detail').window('open');
	//清除当前列表缓存
	//清除所有的行项目
	/* $('#datagrid-sysMaterial-list').datagrid('loadData', { total: 0, rows: [] });  */
	 var rows = $('#datagrid-sysMaterial-list').datagrid("getRows").length;
	 if(rows < 5){
		for(var i = 0 ;i<=5 ;i++){
			insertSys();
		}
	}
	
	 $('#datagrid-sysMaterial-list').datagrid('clearSelections');
	 
	 $("#idGoodsRequestListMaterial").find("tbody tr input:eq(1)").focus();

	 
}
/* 选择带会物料 */
function sysChoiceM() {
	acceptSys();
	var selections = $('#datagrid-sysMaterial-list').datagrid('getRows');
	var codes = "";
	for(var i=0;i<selections.length;i++){
		if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
			codes += selections[i].code+",";
		}
	}
	$("#materialcode").val(codes);
	$("#materialcodes").textbox("setValue",codes); 
	$('#win-sysMaterial-detail').window('close');	
}




var editIndexSys = undefined;
function endEditingSys(){
	if (editIndexSys == undefined){return true}
	if ($('#datagrid-sysMaterial-list').datagrid('validateRow', editIndexSys)){
		var ed = $('#datagrid-sysMaterial-list').datagrid('getEditor', {index:editIndexSys,field:'code'});
		$('#datagrid-sysMaterial-list').datagrid('beginEdit', editIndexSys);
		editIndexSys = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickRowSys(index){
	if (editIndexSys != index){
		if (endEditingSys()){
			$('#datagrid-sysMaterial-list').datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndexSys = index;
		} else {
			$('#datagrid-sysMaterial-list').datagrid('selectRow', editIndexSys);
		}
	}
}
function insertSys(){
	if (endEditingSys()){
		$('#datagrid-sysMaterial-list').datagrid('appendRow',{code:''});
		editIndexSys = $('#datagrid-sysMaterial-list').datagrid('getRows').length-1;
		$('#datagrid-sysMaterial-list').datagrid('selectRow', editIndexSys)
				.datagrid('beginEdit', editIndexSys);
	}
	
}
function removeitSys(){
	if (editIndexSys == undefined){return}
	$('#datagrid-sysMaterial-list').datagrid('cancelEdit', editIndexSys)
			.datagrid('deleteRow', editIndexSys);
	editIndexSys = undefined;
}
function acceptSys(){
	if (endEditingSys()){
		$('#datagrid-sysMaterial-list').datagrid('acceptChanges');
	}
}
function rejectSys(){
	$('#datagrid-sysMaterial-list').datagrid('rejectChanges');
	editIndexSys = undefined;
}

function addMaterialSys(){
	var arr = handlMaterial();
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-sysMaterial-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingSys()){
				$('#datagrid-sysMaterial-list').datagrid('appendRow',{code:arr[i]});
				editIndexSys = $('#datagrid-sysMaterial-list').datagrid('getRows').length-1;
				$('#datagrid-sysMaterial-list').datagrid('selectRow', editIndexSys)
						.datagrid('beginEdit', editIndexSys);
			}
		}
	}
}

function resetMaterialSys(){
	//清除所有的行项目
	$('#datagrid-sysMaterial-list').datagrid('loadData', { total: 0, rows: [] }); 
}
/******************************************** 同步物料的窗口的打开-结束******************************* */


/******************************************** 物料的窗口的打开-开始******************************* */
function openMaterialS() {
	
	$("#copyMaterialContent1").textbox('setValue','')//赋值
	$('#win-material-detail').window('open');
	//清除所有的行项目
	/* $('#datagrid-material-list').datagrid('loadData', { total: 0, rows: [] });  */
	 var rows = $('#datagrid-material-list').datagrid("getRows").length;
	 if(rows < 5){
		 for(var i = 0 ;i<=5 ;i++){
				insert();
			}
	 }
	
	//清除当前列表缓存
	 $('#datagrid-material-list').datagrid('clearSelections');
	
	 $("#idGoodsRequestListMaterial2").find("tbody tr input:eq(1)").focus();
	
}
/* 选择带会物料 */
function choiceM() {
	accept();
	var selections = $('#datagrid-material-list').datagrid('getRows');
	var codes = "";
	for(var i=0;i<selections.length;i++){
		if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
			codes += selections[i].code+",";
		}
	}
	$("#material_code").val(codes);
	$("#material_codes").textbox("setValue",codes);
	$('#win-material-detail').window('close');	
	
	
}

var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-material-list').datagrid('validateRow', editIndex)){
			var ed = $('#datagrid-material-list').datagrid('getEditor', {index:editIndex,field:'code'});
			$('#datagrid-material-list').datagrid('beginEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow(index){
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-material-list').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-material-list').datagrid('selectRow', editIndex);
			}
		}
	}
	function insert(){
		if (endEditing()){
			$('#datagrid-material-list').datagrid('appendRow',{code:''});
			editIndex = $('#datagrid-material-list').datagrid('getRows').length-1;
			$('#datagrid-material-list').datagrid('selectRow', editIndex)
					.datagrid('beginEdit', editIndex);
		}
		
	}
	function removeit(){
		if (editIndex == undefined){return}
		$('#datagrid-material-list').datagrid('cancelEdit', editIndex)
				.datagrid('deleteRow', editIndex);
		editIndex = undefined;
	}
	function accept(){
		if (endEditing()){
			$('#datagrid-material-list').datagrid('acceptChanges');
		}
	}
	function reject(){
		$('#datagrid-material-list').datagrid('rejectChanges');
		editIndex = undefined;
	}

	function addMaterial(){
		var arr = handlMaterial();
		if(arr != null ){
			//清除所有的行项目
			$('#datagrid-material-list').datagrid('loadData', { total: 0, rows: [] }); 
			for(var i= 0 ; i<arr.length ; i++ ){
				if (endEditing()){
					$('#datagrid-material-list').datagrid('appendRow',{code:arr[i]});
					editIndex = $('#datagrid-material-list').datagrid('getRows').length-1;
					$('#datagrid-material-list').datagrid('selectRow', editIndex)
							.datagrid('beginEdit', editIndex);
				}
			}
		}
	}
	
	function resetMaterial(){
		//清除所有的行项目
		$('#datagrid-material-list').datagrid('loadData', { total: 0, rows: [] }); 
	}

/* ----------------------------------------物料的窗口的打开-结束------------------------------------------------------ */


/* 供应商的窗口的打开 */
function openVendorS() {	
	
	$("#copyVendorContent3").textbox('setValue','')//赋值
	$('#win-org-detail').window('open');
	//清除所有的行项目
	/* $('#datagrid-org-list').datagrid('loadData', { total: 0, rows: [] });  */
	var rows = $('#datagrid-org-list').datagrid("getRows").length;
	 if(rows < 5){
		 for(var i = 0 ;i<=5 ;i++){
				insertV();
			}
	 }
	
	//清除当前列表缓存
	 $('#datagrid-org-list').datagrid('clearSelections');
	
	 $("#idGoodsRequestListVendor").find("tbody tr input:eq(1)").focus();
}

/* 选择带会供应商 */
function choiceV() {
	acceptV();
	var selections = $('#datagrid-org-list').datagrid('getRows');
	var codes = "";
	for(var i=0;i<selections.length;i++){
		if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
			codes += selections[i].code+",";
		}
	}
	$("#vendor_code").val(codes);
	$("#vendor_codes").textbox("setValue",codes);
	$('#win-org-detail').window('close');	
}



var editIndexV = undefined;
function endEditingV(){
	if (editIndexV == undefined){return true}
	if ($('#datagrid-org-list').datagrid('validateRow', editIndexV)){
		var ed = $('#datagrid-org-list').datagrid('getEditor', {index:editIndexV,field:'code'});
		$('#datagrid-org-list').datagrid('beginEdit', editIndexV);
		editIndexV = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickRowV(index){
	if (editIndexV != index){
		if (endEditingV()){
			$('#datagrid-org-list').datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndexV = index;
		} else {
			$('#datagrid-org-list').datagrid('selectRow', editIndexV);
		}
	}
}
function insertV(){
	if (endEditingV()){
		$('#datagrid-org-list').datagrid('appendRow',{code:''});
		editIndexV = $('#datagrid-org-list').datagrid('getRows').length-1;
		$('#datagrid-org-list').datagrid('selectRow', editIndexV)
				.datagrid('beginEdit', editIndexV);
	}
	
}
function removeitV(){
	if (editIndexV == undefined){return}
	$('#datagrid-org-list').datagrid('cancelEdit', editIndexV)
			.datagrid('deleteRow', editIndexV);
	editIndexV = undefined;
}
function acceptV(){
	if (endEditingV()){
		$('#datagrid-org-list').datagrid('acceptChanges');
	}
}
function rejectV(){
	$('#datagrid-org-list').datagrid('rejectChanges');
	editIndexV = undefined;
}

function addVendor(){
	var arr = handlVendor();
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-org-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingV()){
				$('#datagrid-org-list').datagrid('appendRow',{code:arr[i]});
				editIndexV = $('#datagrid-org-list').datagrid('getRows').length-1;
				$('#datagrid-org-list').datagrid('selectRow', editIndexV)
						.datagrid('beginEdit', editIndexV);
			}
		}
	}
}

function resetVendor(){
	//清除所有的行项目
	$('#datagrid-org-list').datagrid('loadData', { total: 0, rows: [] }); 
}





//加载input内容(物料1)
function loadContent1(){
	var arr = handlInputMaterial( $('#copyMaterialContent1').val() );
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-material-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditing()){
				$('#datagrid-material-list').datagrid('appendRow',{code:arr[i]});
				editIndex = $('#datagrid-material-list').datagrid('getRows').length-1;
				$('#datagrid-material-list').datagrid('selectRow', editIndex)
						.datagrid('beginEdit', editIndex);
			}
		}
	}
}


//加载input内容(物料2)
function loadContent2(){
	var arr = handlInputMaterial( $('#copyMaterialContent2').val() );
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-sysMaterial-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingSys()){
				$('#datagrid-sysMaterial-list').datagrid('appendRow',{code:arr[i]});
				editIndexSys = $('#datagrid-sysMaterial-list').datagrid('getRows').length-1;
				$('#datagrid-sysMaterial-list').datagrid('selectRow', editIndexSys)
						.datagrid('beginEdit', editIndexSys);
			}
		}
	}
}

//加载input供应商内容（供应商）
function loadContent3(){
	var arr = handlInputVendor( $('#copyVendorContent3').val() );
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-org-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingV()){
				$('#datagrid-org-list').datagrid('appendRow',{code:arr[i]});
				editIndexV = $('#datagrid-org-list').datagrid('getRows').length-1;
				$('#datagrid-org-list').datagrid('selectRow', editIndexV)
						.datagrid('beginEdit', editIndexV);
			}
		}
	}
}




</script>
</body>
</html>
