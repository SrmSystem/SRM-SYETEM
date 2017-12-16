<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="com.qeweb.scm.basemodule.utils.StringUtils" %> 
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html>
<head>
	<title><spring:message code="vendor.orderGoodsRequret.cargoPlanReport"/><!-- 要货计划报表 --></title>
	<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
	<script type="text/javascript">	
	function getNumFmt(v,r,i) {
		
 		if(v=="" || v==null || v == "0.000" ){
			return 0.000;
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
	
	function getNumFmtOne(v) {
		
 		if(v=="" || v==null || v == "0.000" ){
			return 0.000;
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
	
	
	
 	function goodsformatter(number) {
 		if(number != null && number != '' && number != 'undefined'){
 			if(number.indexOf("red")!=-1){
 				number = number.replace('red','');
 	 	     	number = "<font color='red'>"+number+"</font>"; 
 	 	     	return number;
 	 		}else{
 	 			return getNumFmtOne(number);
 	 		}
 		}else{
 			return '0.000';
 		}
 		
 	}


	
	//-----------------------------------------------------------//

	
	</script>   
</head>
<body style="margin:0;padding:0;">
	<table id="datagrid-goodsRequestReport-list" title="要货看板列表" fit="true"
		data-options="method:'post',singleSelect:false,
		toolbar:'#goodsRequestListReportToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead frozen="true">
			<tr>
				<th data-options="field:'id',checkbox:true"></th>
				<th data-options="field:'factoryCode' ,formatter:function(v,r,i){return r.factory.code;}"><spring:message code="vendor.orderplan.factoryCode"/><!-- 工厂编码 --></th>
				<th data-options="field:'vendor.code' ,formatter:function(v,r,i){return r.vendor.code;}  "><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
				<th data-options="field:'material.code',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --></th> 
				<th data-options="field:'material.name',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/><!-- 物料描述 --></th> 
			</tr>
		</thead>
		<thead >
			<tr>
			   <th data-options="field:'groupName' ,formatter:function(v,r,i){return r.group.name;}"><spring:message code="vendor.orderplan.purchasingGroup"/><!-- 采购组 --></th>
			   <th data-options="field:'group.code',formatter:function(v,r,i){return r.group.code;}"><spring:message code="vendor.orderplan.buyer"/><!-- 采购员 --></th>
			   <th data-options="field:'meins',formatter:function(v,r,i){return r.meins;}"><spring:message code="vendor.orderplan.unit"/><!-- 单位 --></th> 
			   <th data-options="field:'factoryName' ,formatter:function(v,r,i){return r.factory.name;}"><spring:message code="vendor.orderplan.factoryName"/><!-- 工厂名称 --></th>
			   <th data-options="field:'vendor.name', formatter:function(v,r,i){return r.vendor.name;}    "><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
			   <th id='monOne'     data-options="field:'colOne',styler:function(v,r,i) {  return ' background-color:#199ED8;color:black'} ,   formatter: function(v,r,i){ if(r.data.monOne){  return  getNumFmtOne( r.data.monOne ) } else{  return '0.000' }     }"></th>
			   <th id='col1'  data-options="field:'col1',formatter:function(v,r,i){ if(r.data.headVo){  return  goodsformatter(r.data.headVo.headCol1);}else {return '0.000'}   }"></th>
		       <th id='col2'  data-options="field:'col2',formatter:function(v,r,i){ if(r.data.headVo){ return  goodsformatter(r.data.headVo.headCol2);} else {return '0.000' }  }"></th>
		       <th id='col3'  data-options="field:'col3',formatter:function(v,r,i){ if(r.data.headVo){ return  goodsformatter(r.data.headVo.headCol3);} else {return '0.000' } }"></th>
		       <th id='col4'  data-options="field:'col4',formatter:function(v,r,i){ if(r.data.headVo){  return goodsformatter(r.data.headVo.headCol4);} else {return '0.000' } }"></th>
		       <th id='col5'  data-options="field:'col5',formatter:function(v,r,i){ if(r.data.headVo){  return goodsformatter(r.data.headVo.headCol5);}else {return '0.000' } }"></th>
		       <th id='col6'  data-options="field:'col6',formatter:function(v,r,i){ if(r.data.headVo){ return  goodsformatter(r.data.headVo.headCol6);} else {return '0.000' } }"></th>
		       <th id='col7'  data-options="field:'col7',formatter:function(v,r,i){ if(r.data.headVo){  return  goodsformatter(r.data.headVo.headCol7);} else {return '0.000' } }"></th>
		        <th id='monTwo'     data-options="field:'colTwo', styler:function(v,r,i) {  return ' background-color:#199ED8;color:black'} ,formatter:function(v,r,i){ if(r.data.monTwo){ return   getNumFmtOne(r.data.monTwo  ) } else{  return '0.000' }           }"></th>
		       <th id='col8'  data-options="field:'col8',formatter:function(v,r,i){ if(r.data.headVo){  return   goodsformatter(r.data.headVo.headCol8);} else {return '0.000' } }"></th>
		       <th id='col9'  data-options="field:'col9',formatter:function(v,r,i){ if(r.data.headVo){  return goodsformatter(r.data.headVo.headCol9);} else {return '0.000' } }"></th>
		       <th id='col10'  data-options="field:'col10',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol10);} else {return '0.000' } }"></th>
		       <th id='col11'  data-options="field:'col11',formatter:function(v,r,i){ if(r.data.headVo){  return goodsformatter(r.data.headVo.headCol11);} else {return '0.000' } }"></th>
		       <th id='col12'  data-options="field:'col12',formatter:function(v,r,i){ if(r.data.headVo){return  goodsformatter(r.data.headVo.headCol12);} else {return '0.000' } }"></th>
		       <th id='col13'  data-options="field:'col13',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol13);} else {return '0.000' } }"></th>
		       <th id='col14'  data-options="field:'col14',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol14);} else {return '0.000' } }"></th>
		       <th id='monThree'    data-options="field:'colThree',styler:function(v,r,i) {  return ' background-color:#199ED8;color:black'} ,formatter:function(v,r,i){ if( r.data.monThree){  return  getNumFmtOne(r.data.monThree )  }    else{  return '0.000'  }     }"></th>
		       <th id='col15'  data-options="field:'col15',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol15);} else {return '0.000' } }"></th>
		       <th id='col16'  data-options="field:'col16',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol16);} else {return '0.000' } }"></th>
		       <th id='col17'  data-options="field:'col17',formatter:function(v,r,i){ if(r.data.headVo){return  goodsformatter(r.data.headVo.headCol17);} else {return '0.000' } }"></th>
		       <th id='col18'  data-options="field:'col18',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol18);} else {return '0.000' } }"></th>
		       <th id='col19'  data-options="field:'col19',formatter:function(v,r,i){ if(r.data.headVo){ return  goodsformatter(r.data.headVo.headCol19);} else {return '0.000' } }"></th>
		       <th id='col20'  data-options="field:'col20',formatter:function(v,r,i){ if(r.data.headVo){ return  goodsformatter(r.data.headVo.headCol20);} else {return '0.000' } }"></th>
		       <th id='col21'  data-options="field:'col21',formatter:function(v,r,i){  if(r.data.headVo){return  goodsformatter(r.data.headVo.headCol21);} else {return '0.000' } }"></th>
		       <th id='monFour'   data-options="field:'colFour',styler:function(v,r,i) {  return ' background-color:#199ED8;color:black'} ,formatter:function(v,r,i){if(r.data.monFour){  return  getNumFmtOne(r.data.monFour)} else{ return '0.000'} }"></th>
		       <th id='col22'  data-options="field:'col22',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol22);} else {return '0.000' } }"></th>
		       <th id='col23'  data-options="field:'col23',formatter:function(v,r,i){ if(r.data.headVo){return  goodsformatter(r.data.headVo.headCol23);} else {return '0.000' } }"></th>
		       <th id='col24'  data-options="field:'col24',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol24);} else {return '0.000' } }"></th>
		       <th id='col25'  data-options="field:'col25',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol25);} else {return '0.000' } }"></th>
		       <th id='col26'  data-options="field:'col26',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol26);} else {return '0.000' } }"></th>
		       <th id='col27'  data-options="field:'col27',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol27);} else {return '0.000' } }"></th>
		       <th id='col28'  data-options="field:'col28',formatter:function(v,r,i){ if(r.data.headVo){ return goodsformatter(r.data.headVo.headCol28);} else {return '0.000' } }"></th>
			</tr>
		</thead>
	</table>
	<div id="goodsRequestListReportToolbar" style="padding:10px;">
		<div>
           <form id="form-goodsRequestListReport-search"  method="post"  >
            <spring:message code="vendor.orderplan.factoryCode"/><!-- 工厂编码 -->:<input type="text"  id="factory_codes"    name="search-EQ_factory.code"  class="easyui-textbox" style="width:180px;    "/>
            <spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --> :<input type="text"  id="material_codes"   name="search-IN_material.code" class="easyui-textbox "    style="width:180px;"/>
            <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'" onclick="openMaterialS()"></a>
            <spring:message code="vendor.orderplan.purchasingGroup"/><!-- 采购组 --> ：  <input type="text" name="search-EQ_group.code" class="easyui-textbox" style="width:180px;" />
            <spring:message code="vendor.supplierCode"/><!-- 供应商编码 --> ：  <input type="text"  id="vendor_codes"   name="search-IN_vendor.code"  class="easyui-textbox" style="width:180px;"/>
            <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'" onclick="openVendorS()"></a>
			<div>
				<table style="width: 100% ;padding:10px;" >
					<tr align="right">
						<td>
							<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchGoodsRepoetPlan()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a> 
							<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-goodsRequestListReport-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>   
						</td>
						
					</tr>
					<tr >
				       <td >
				           <HR style="border:1px solid #CED4D4  ;margin-top: 0px; margin-bottom: 0px ;" width="100%" SIZE=3>
				          <label style="margin-top: 10px;" ><spring:message code="vendor.orderGoodsRequret.Note"/><!-- 说明：预测计划以蓝色显示，以周为单位展现，日期对应的是每周的周一；要货计划以天为单位展示，不满足的要货计划以红色显示。 --></label> 
				       </td>
			       </tr>
				</table>
			</div>
			</form>
		</div>
	</div>
	
	  <!--   同步物料的查询 -->
  <div id="win-materialGRR-detail" class="easyui-window" title="选择物料" style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true">
		<div id="div_materialGRR-_detail" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="choiceM()"><spring:message code="vendor.confirm"/><!-- 确认 --></a>
			</div>
			<div>
			     <a href="javascript:;"  class="easyui-linkbutton"   data-options="iconCls:'icon-add',plain:true"  onclick="loadContent2()">加载内容     <!-- 加载内容 --></a>:
                 <input type="text"   data-options=" prompt :  '将内容copy后，点击 加载内容  '  "   id = "copyMaterialContent2" class="easyui-textbox"     style="width:180px;"/>
			</div>
			<div>
             <a class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"  onclick="addMaterial();" ><spring:message code="vendor.orderGoodsRequret.readClipboardContents"/><!-- 读取剪贴板内容 --></a>
			 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="insert()"><spring:message code="vendor.orderGoodsRequret.adds"/><!-- 添加 --></a>
		     <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="removeit()"><spring:message code="vendor.deleting"/><!-- 删除 --></a>
             <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="resetMaterial()"><spring:message code="vendor.orderGoodsRequret.emptying"/><!-- 清空 --></a>
			</div>
		</div>
		<div id="idGoodsRequestReportMaterial">
			<table id="datagrid-materialGRR-list" title="物料列表" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: onClickRowSys,
				rownumbers:true">
				<thead>
					<tr>
				 		<th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code="vendor.materialCode"/><!-- 物料编码 --></th>
					</tr>
				</thead>
			</table>
		</div>	
	</div>
</div>
	
<!--   供应商的查询 -->
<div id="win-orgGRR-detail" class="easyui-window" title="选择供应商" style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
	<div class="easyui-panel" data-options="fit:true">
		<div id="div_orgGRR_detail" style="padding:5px;">
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
		<div id="idGoodsRequestReportVendor">
			<table id="datagrid-orgGRR-list" title="供应商列表" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: onClickRowV,
				rownumbers:true">
				<thead><tr>
				<th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code="vendor.coding"/><!-- 编码 --></th>
				</tr></thead>
			</table>
		</div>	
	</div>
</div> 	
	
	
	
<script type="text/javascript">

$(function() {
	//加载表头
	getPurchasePlanItemHead();
	
	//显示计划详情
 	var searchParamArray = $('#form-goodsRequestListReport-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-goodsRequestReport-list').datagrid({   
    	url:'${ctx}/manager/order/goodsRequest/getGoodsReportList',
    	queryParams:searchParams
    	
	});
});
//查询
function searchGoodsRepoetPlan(){
	
	//加载表头
	getPurchasePlanItemHead();
	
	//显示计划详情
 	var searchParamArray = $('#form-goodsRequestListReport-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-goodsRequestReport-list').datagrid({   
    	url:'${ctx}/manager/order/goodsRequest/getGoodsReportList',
    	queryParams:searchParams
    	
	});
	
}
function getPurchasePlanItemHead(){
    $.ajax({  
    	url:'${ctx}/manager/order/goodsRequest/goodsReportHeadGet/',
        async: false, // 注意此处需要同步，因为先绑定表头，才能绑定数据   
        type:'POST',  
        dataType:'json',  
        cache:false,
        success:function(datas){//获成功后，使用easyUi的datagrid去生成表格   	
        	$("#monOne").text(datas.monOne);   
        	$("#monTwo").text(datas.monTwo);  
        	$("#monThree").text(datas.monThree);  
        	$("#monFour").text(datas.monFour); 

        	
        	$("#col1").text(datas.headVo.headCol1);   
        	$("#col2").text(datas.headVo.headCol2);  
        	$("#col3").text(datas.headVo.headCol3);  
        	$("#col4").text(datas.headVo.headCol4);  
        	$("#col5").text(datas.headVo.headCol5);  
        	$("#col6").text(datas.headVo.headCol6);  
        	$("#col7").text(datas.headVo.headCol7);  
        	$("#col8").text(datas.headVo.headCol8);  
        	$("#col9").text(datas.headVo.headCol9);  
        	$("#col10").text(datas.headVo.headCol10);  
        	$("#col11").text(datas.headVo.headCol11);  
        	$("#col12").text(datas.headVo.headCol12);  
        	$("#col13").text(datas.headVo.headCol13);  
        	$("#col14").text(datas.headVo.headCol14);  
        	$("#col15").text(datas.headVo.headCol15);  
        	$("#col16").text(datas.headVo.headCol16);  
        	$("#col17").text(datas.headVo.headCol17);  
        	$("#col18").text(datas.headVo.headCol18);  
        	$("#col19").text(datas.headVo.headCol19);  
        	$("#col20").text(datas.headVo.headCol20);  
        	$("#col21").text(datas.headVo.headCol21);  
        	$("#col22").text(datas.headVo.headCol22);  
        	$("#col23").text(datas.headVo.headCol23);  
        	$("#col24").text(datas.headVo.headCol24);  
        	$("#col25").text(datas.headVo.headCol25);  
        	$("#col26").text(datas.headVo.headCol26);  
        	$("#col27").text(datas.headVo.headCol27); 
        	$("#col28").text(datas.headVo.headCol28); 
        }  
    });
}


/******************************************** 物料的窗口的打开-开始******************************* */
function openMaterialS() {
	
	$("#copyMaterialContent2").textbox('setValue','')//赋值
	$('#win-materialGRR-detail').window('open');
	//清除所有的行项目
	$('#datagrid-materialGRR-list').datagrid('loadData', { total: 0, rows: [] });
	 var rows = $('#datagrid-materialGRR-list').datagrid("getRows").length;
	 if(rows < 5){
		 for(var i = 0 ;i<=5 ;i++){
				insert();
			}
	 }
	//清除当前列表缓存
	 $('#datagrid-materialGRR-list').datagrid('clearSelections');
	
	 $("#idGoodsRequestReportMaterial").find("tbody tr input:eq(1)").focus();
}
/* 选择带会物料 */
function choiceM() {
	accept();
	var selections = $('#datagrid-materialGRR-list').datagrid('getRows');
	var codes = "";
	for(var i=0;i<selections.length;i++){
		if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
			codes += selections[i].code+",";
		}
	}
	$("#material_code").val(codes);
	$("#material_codes").textbox("setValue",codes);
	$('#win-materialGRR-detail').window('close');	
	
	
}

var editIndex = undefined;
	function endEditing(){
		if (editIndex == undefined){return true}
		if ($('#datagrid-materialGRR-list').datagrid('validateRow', editIndex)){
			var ed = $('#datagrid-materialGRR-list').datagrid('getEditor', {index:editIndex,field:'code'});
			$('#datagrid-materialGRR-list').datagrid('beginEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}
	function onClickRow(index){
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-materialGRR-list').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-materialGRR-list').datagrid('selectRow', editIndex);
			}
		}
	}
	function insert(){
		if (endEditing()){
			$('#datagrid-materialGRR-list').datagrid('appendRow',{code:''});
			editIndex = $('#datagrid-materialGRR-list').datagrid('getRows').length-1;
			$('#datagrid-materialGRR-list').datagrid('selectRow', editIndex)
					.datagrid('beginEdit', editIndex);
		}
		
	}
	function removeit(){
		if (editIndex == undefined){return}
		$('#datagrid-materialGRR-list').datagrid('cancelEdit', editIndex)
				.datagrid('deleteRow', editIndex);
		editIndex = undefined;
	}
	function accept(){
		if (endEditing()){
			$('#datagrid-materialGRR-list').datagrid('acceptChanges');
		}
	}
	function reject(){
		$('#datagrid-materialGRR-list').datagrid('rejectChanges');
		editIndex = undefined;
	}

	function addMaterial(){
		var arr = handlMaterial();
		if(arr != null ){
			//清除所有的行项目
			$('#datagrid-materialGRR-list').datagrid('loadData', { total: 0, rows: [] }); 
			for(var i= 0 ; i<arr.length ; i++ ){
				if (endEditing()){
					$('#datagrid-materialGRR-list').datagrid('appendRow',{code:arr[i]});
					editIndex = $('#datagrid-materialGRR-list').datagrid('getRows').length-1;
					$('#datagrid-materialGRR-list').datagrid('selectRow', editIndex)
							.datagrid('beginEdit', editIndex);
				}
			}
		}
	}
	
	function resetMaterial(){
		//清除所有的行项目
		$('#datagrid-materialGRR-list').datagrid('loadData', { total: 0, rows: [] }); 
		editIndex = undefined;
	}
	
	//加载input内容(物料2)
	function loadContent2(){
		var arr = handlInputMaterial( $('#copyMaterialContent2').val());
		if(arr != null ){
			//清除所有的行项目
			$('#datagrid-materialGRR-list').datagrid('loadData', { total: 0, rows: [] }); 
			for(var i= 0 ; i<arr.length ; i++ ){
				if (endEditing()){
					$('#datagrid-materialGRR-list').datagrid('appendRow',{code:arr[i]});
					editIndex = $('#datagrid-materialGRR-list').datagrid('getRows').length-1;
					$('#datagrid-materialGRR-list').datagrid('selectRow', editIndex)
							.datagrid('beginEdit', editIndex);
				}
			}
			editIndex = undefined;
		}
	}
	
	function onClickRowSys(index){
		if (editIndex != index){
			if (endEditing()){
				$('#datagrid-materialGRR-list').datagrid('selectRow', index)
						.datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-materialGRR-list').datagrid('selectRow', editIndexSys);
			}
		}
	}

/* ----------------------------------------物料的窗口的打开-结束------------------------------------------------------ */


/* ----------------------------------------供应商的窗口的打开------------------------------------------------------ */


/* 供应商的窗口的打开 */
function openVendorS() {	
	
	$("#copyVendorContent3").textbox('setValue','')//赋值
	$('#win-orgGRR-detail').window('open');
	//清除所有的行项目
	$('#datagrid-orgGRR-list').datagrid('loadData', { total: 0, rows: [] });
	var rows = $('#datagrid-orgGRR-list').datagrid("getRows").length;
	 if(rows < 5){
		 for(var i = 0 ;i<=5 ;i++){
				insertV();
			}
	 }
	
	//清除当前列表缓存
	 $('#datagrid-orgGRR-list').datagrid('clearSelections');
	
	 $("#idGoodsRequestReportVendor").find("tbody tr input:eq(1)").focus();
}

/* 选择带会供应商 */
function choiceV() {
	acceptV();
	var selections = $('#datagrid-orgGRR-list').datagrid('getRows');
	var codes = "";
	for(var i=0;i<selections.length;i++){
		if(selections[i].code != null && selections[i].code != "" && selections[i].code != 'undefined'){
			codes += selections[i].code+",";
		}
	}
	$("#vendor_code").val(codes);
	$("#vendor_codes").textbox("setValue",codes);
	$('#win-orgGRR-detail').window('close');	
}



var editIndexV = undefined;
function endEditingV(){
	if (editIndexV == undefined){return true}
	if ($('#datagrid-orgGRR-list').datagrid('validateRow', editIndexV)){
		var ed = $('#datagrid-orgGRR-list').datagrid('getEditor', {index:editIndexV,field:'code'});
		$('#datagrid-orgGRR-list').datagrid('beginEdit', editIndexV);
		editIndexV = undefined;
		return true;
	} else {
		return false;
	}
}
function onClickRowV(index){
	if (editIndexV != index){
		if (endEditingV()){
			$('#datagrid-orgGRR-list').datagrid('selectRow', index)
					.datagrid('beginEdit', index);
			editIndexV = index;
		} else {
			$('#datagrid-orgGRR-list').datagrid('selectRow', editIndexV);
		}
	}
}
function insertV(){
	if (endEditingV()){
		$('#datagrid-orgGRR-list').datagrid('appendRow',{code:''});
		editIndexV = $('#datagrid-orgGRR-list').datagrid('getRows').length-1;
		$('#datagrid-orgGRR-list').datagrid('selectRow', editIndexV)
				.datagrid('beginEdit', editIndexV);
	}
	
}
function removeitV(){
	if (editIndexV == undefined){return}
	$('#datagrid-orgGRR-list').datagrid('cancelEdit', editIndexV)
			.datagrid('deleteRow', editIndexV);
	editIndexV = undefined;
}
function acceptV(){
	if (endEditingV()){
		$('#datagrid-orgGRR-list').datagrid('acceptChanges');
	}
}
function rejectV(){
	$('#datagrid-orgGRR-list').datagrid('rejectChanges');
	editIndexV = undefined;
}

function addVendor(){
	var arr = handlVendor();
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-orgGRR-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingV()){
				$('#datagrid-orgGRR-list').datagrid('appendRow',{code:arr[i]});
				editIndexV = $('#datagrid-orgGRR-list').datagrid('getRows').length-1;
				$('#datagrid-orgGRR-list').datagrid('selectRow', editIndexV)
						.datagrid('beginEdit', editIndexV);
			}
		}
	}
}

function resetVendor(){
	//清除所有的行项目
	$('#datagrid-orgGRR-list').datagrid('loadData', { total: 0, rows: [] }); 
	editIndexV = undefined;
}

//加载input供应商内容（供应商）
function loadContent3(){
	var arr = handlInputVendor( $('#copyVendorContent3').val() );
	if(arr != null ){
		//清除所有的行项目
		$('#datagrid-orgGRR-list').datagrid('loadData', { total: 0, rows: [] }); 
		for(var i= 0 ; i<arr.length ; i++ ){
			if (endEditingV()){
				$('#datagrid-orgGRR-list').datagrid('appendRow',{code:arr[i]});
				editIndexV = $('#datagrid-orgGRR-list').datagrid('getRows').length-1;
				$('#datagrid-orgGRR-list').datagrid('selectRow', editIndexV)
						.datagrid('beginEdit', editIndexV);
			}
		}
		editIndexV = undefined;
	}
}

</script>
</body>
</html>
