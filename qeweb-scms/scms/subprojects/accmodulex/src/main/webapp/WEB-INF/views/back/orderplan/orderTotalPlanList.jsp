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
	<title><spring:message code="vendor.orderplan.forecastManagement"/></title>
	<META HTTP-EQUIV="pragma" CONTENT="no-cache" >
	<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache,must-revalidate" >
	<META HTTP-EQUIV="expires" CONTENT="0" >
	<script type="text/javascript" src="${ctx}/static/script/orderPlan/orderTotal.js"></script>
	<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
	<script type="text/javascript">
	function monthFmt(v,r,i) {
		return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showTotalPlanDetail('+ r.id +');">' + v + '</a>';
	} 
	
	
	function getNumFmt(v,r,i) {
 		if(v=="" || v==null || v == "0.000" ){
			return "0.000";
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
	
	</script>   
</head>
<body style="margin:0;padding:0;" onLoad="javascript:document.yourFormName.reset()">
	<table id="datagrid-purchaseTotalplan-list" title="预测计划总量列表" class="easyui-datagrid" fit="true"
		data-options="url:'${ctx}/manager/order/purchaseTotalPlan',method:'post',singleSelect:false,
		toolbar:'#purchaseTotalplanListToolbar',
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'id',checkbox:true"></th>
		<th align="right" data-options="field:'month',formatter:monthFmt"><spring:message code="vendor.orderplan.versionNumber"/></th> 
		<th align="left" data-options="field:'createUserName'"><spring:message code="vendor.orderplan.planner"/></th>  
		<th align="right" data-options="field:'createTime'"><spring:message code="vendor.orderplan.creationTime"/></th>
		</tr></thead>
	</table>
	<div id="purchaseTotalplanListToolbar" style="padding:5px;">
		<div>
             <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="importTotalPlan()"><spring:message code="vendor.orderplan.importRequirements"/></a>
		</div>
		
		<div>
			<form id="form-purchaseTotalplan-search" method="post">
			<!-- 版本号：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:80px;"/>
			计划员：<input type="text" name="search-LIKE_createUserName" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchaseTotalPlan()">查询</a> 
			<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-purchaseTotalplan-search').form('reset')">重置</a>    -->
			<table width="80%">
				<tr>
					<td><spring:message code="vendor.orderplan.versionNumber"/>：<input type="text" name="search-LIKE_month" class="easyui-textbox" style="width:200px;"/></td>
					<td><spring:message code="vendor.orderplan.planner"/>：<input type="text" name="search-LIKE_createUserName" class="easyui-textbox" style="width:200px;"/></td>
				</tr>
				<tr>
					<td colspan="3" align="right">
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchaseTotalPlan()"><spring:message code="vendor.orderplan.enquiries"/></a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-purchaseTotalplan-search').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a> 
					</td>
				</tr>
			</table>
			</form>
		</div>
	</div>
	
	<!-- 导入计划 -->
	<div id="win-totalPlan-import" class="easyui-window" title="导入预测计划" style="width:400px;height:200px"
	data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true" style="width:100%;padding:30px 70px 50px 70px">
			<form id="form-totalPlan-import" method="post" enctype="multipart/form-data" action="${ctx}/manager/order/purchaseTotalPlan/filesUpload"> 
				<div style="margin-bottom:20px">
					<spring:message code="vendor.orderplan.file"/>：<input type=file id="file" name="planfiles" /><br>
				</div>
				<div style="text-align: center;padding:5px;">
					<a href="javascript:;" class="easyui-linkbutton" onclick="saveimportplan();"><spring:message code="vendor.orderplan.save"/></a> 
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-totalPlan-import').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
				</div>
				</form>  
				<form id="form-purchaseTotalplan-filesDownload" style="margin-top:-50px">
				<spring:message code="vendor.orderplan.formwork"/>：<a href="javascript:;"  onclick="filesDownload()"><spring:message code="vendor.orderplan.totalTemplate"/>.xls</a>
			    </form>
			
		</div>
	</div>
		<!-- 计划详情 -->
	<div id="win-planitem-addoredit" class="easyui-window" title="采购计划详情" style="width:95%;height:500px"
		data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="purchaseplanListitemToolbar" style="padding:5px;">
			<input id="totalPlanId"  type="hidden">
				<div>
					<form id="form-purchaseTotalplanitem-search">
					<input id="id" name="id" value="-1" type="hidden"/>
						<table>
						<tr>
								<td   style="display:none"  >
							             <spring:message code="vendor.orderplan.timePeriod"/>：<input class="easyui-datebox" name="search-EQ_startDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">                                              
								</td>
								<td style="display:none">
							            <spring:message code="vendor.orderplan.to"/><input  class="easyui-datebox" name="search-EQ_endDate" data-options="showSeconds:false,editable:false" value="" style="width:150px">
								</td>
								<td >
								<spring:message code="vendor.orderplan.factoryCode"/>：<input type="text" name="search-EQ_factory.code" class="easyui-textbox" style="width:120px;"  />
								</td>
							     <td >
								<spring:message code="vendor.orderplan.materialNumber"/>：<input type="text" name="search-EQ_material.code" class="easyui-textbox" style="width:120px;"  />
								</td>
								<td >
								<spring:message code="vendor.orderplan.validState"/>：<select class="easyui-combobox" id="isNew" data-options="editable:false" name="search-EQ_isNew"><option value="">-<spring:message code="vendor.orderplan.all"/>-</option><option value="0"><spring:message code="vendor.orderplan.no"/></option><option value="1" selected ="selected" ><spring:message code="vendor.orderplan.yes"/></option></select>
								</td>
								<td >
								<spring:message code="vendor.orderplan.versionNumber"/>：<input type="text" name="search-LIKE_versionNumber" class="easyui-textbox" style="width:120px;"  />
								</td>
								<td>
									<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="searchPurchasePlanItem()"><spring:message code="vendor.orderplan.enquiries"/></a>
						            <a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-purchaseTotalplanitem-search').form('reset')"><spring:message code="vendor.orderplan.resetting"/></a>
								</td>
							</tr>
							<tr>
								
						</tr>
						</table>
					</form>
				</div>
			</div>
			<div style=" padding: 7px;border-top: 1px solid #E5E5E5;">
				    <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-delete'" onclick="deletePlan()"><spring:message code="vendor.orderplan.deleting"/></a>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-download'" onclick="exportPurchaseTotalPlan()"><spring:message code="vendor.orderplan.derivation"/></a>
			</div>
			
			
			<table id="datagrid-purchaseTotalplanitem-list" style="height:350px" title="预测计划总量详情 ----- 红色为历史版本 ------   绿色为生效版本  ------" 
				data-options="method:'post',singleSelect:false,
				toolbar:'#purchaseplanitemListToolbar',
				pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]" 
				>
				<thead data-options="frozen:true">
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
		       
		            <th data-options="field:'factoryCode',formatter:function(v,r,i){return r.factory.code;}"><spring:message code="vendor.orderplan.factory"/></th>
		            <th data-options="field:'groupCode',formatter:function(v,r,i){return r.purchasingGroup.code;}"><spring:message code="vendor.orderplan.purchasingGroup"/></th>
		            <th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/></th>
		              <th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/></th>
		         </tr>
		         </thead>
		         <thead>
		         <tr>
		              <th data-options="field:'versionNumber'"><spring:message code="vendor.orderplan.versionNumber"/></th>
		             <th data-options="field:'factoryName',formatter:function(v,r,i){return r.factory.name;}"><spring:message code="vendor.orderplan.factoryName"/></th>
		             <th data-options="field:'groupName',formatter:function(v,r,i){return r.purchasingGroup.name;}"><spring:message code="vendor.orderplan.namePurchasingGroup"/></th>
		            <th data-options="field:'materialUnit',formatter:function(v,r,i){return r.material.unit;}"><spring:message code="vendor.orderplan.unit"/></th>
		            <th data-options="field:'createTime'"><spring:message code="vendor.orderplan.modifyTime"/></th>

		            <th id='col1'  data-options="field:'col1',formatter:function(v,r,i){return  getNumFmt( r.purchasePlanItemHeadVO.col1) ; } "></th>
		            <th id='col2'  data-options="field:'col2',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col2) ;  }"></th>
		            <th id='col3'  data-options="field:'col3',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col3) ;  }"></th>
		            <th id='col4'  data-options="field:'col4',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col4) ;  }"></th>
		            <th id='col5'  data-options="field:'col5',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col5) ;  }"></th>
		            <th id='col6'  data-options="field:'col6',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col6) ;    }"></th>
		            <th id='col7'  data-options="field:'col7',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col7) ;   }  "></th>
		            <th id='col8'  data-options="field:'col8',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col8) ; }"></th>
		            <th id='col9'  data-options="field:'col9',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col9) ;  }"></th>
		            <th id='col10'  data-options="field:'col10',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col10) ;   }"></th>
		            <th id='col11'  data-options="field:'col11',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col11) ; }"></th>
		            <th id='col12'  data-options="field:'col12',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col12) ;  }"></th>
		            <th id='col13'  data-options="field:'col13',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col13) ;   } "></th>
		            <th id='col14'  data-options="field:'col14',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col14) ;  }"></th>
		            <th id='col15'  data-options="field:'col15',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col15) ; }"></th>
		            <th id='col16'  data-options="field:'col16',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col16) ;   }"></th>
		            <th id='col17'  data-options="field:'col17',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col17) ;  }"></th>
		            <th id='col18'  data-options="field:'col18',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col18) ; }"></th>
		            <th id='col19'  data-options="field:'col19',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col19) ;  }"></th>
		            <th id='col20'  data-options="field:'col20',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col20) ;  }"></th>
		            <th id='col21'  data-options="field:'col21',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col21) ; }"></th>
		            <th id='col22'  data-options="field:'col22',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col22) ; }"></th>
		            <th id='col23'  data-options="field:'col23',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col23) ; }"></th>
		            <th id='col24'  data-options="field:'col24',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col24) ;  }"></th>
		            <th id='col25'  data-options="field:'col25',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col25) ; }"></th>
		            <th id='col26'  data-options="field:'col26',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col26) ; }"></th>
		            <th id='col27'  data-options="field:'col27',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col27) ; }"></th>
		            <th id='col28'  data-options="field:'col28',formatter:function(v,r,i){return getNumFmt( r.purchasePlanItemHeadVO.col28) ; }"></th>		            
		         	<th data-options="field:'dayStock'"><spring:message code="vendor.orderplan.dayInventory"/></th>
		             <th data-options="field:'unpaidQty'"><spring:message code="vendor.orderplan.POnotDay"/></th>
		         </tr>
		         </thead>
				<input type="hidden" id="id" name="id" class="easyui-textbox" style="width:140px;"  />
				<input type="hidden" id="reject-ac" name="reject-ac" class="easyui-textbox" style="width:140px;"  />
				<input type="hidden" id="re-id" name="re-id" class="easyui-textbox" style="width:140px;"  />
			</table>
		</div>
	</div>
	
	
<script type="text/javascript">
//查询
function searchPurchaseTotalPlan(){
	var searchParamArray = $('#form-purchaseTotalplan-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseTotalplan-list').datagrid('load',searchParams);
}
//查询
function searchPurchasePlanItem(){
	var  id =  $("#id").val();

	getPurchaseTotalPlanItemHead(null);

	var searchParamArray = $('#form-purchaseTotalplanitem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseTotalplanitem-list').datagrid({
		url:'${ctx}/manager/order/purchaseTotalPlan/planTotalitem/'+id+'',
		queryParams:searchParams,
		rowStyler:function(index,row){
			if (row.isNew == 1){
			  color = 'background-color:;color:green;font-weight:bold;';
            }else{
          	  color = 'background-color:;color:red;font-weight:bold;';
            }
			 return color
		}
		});

}

//查看计划详情
function showTotalPlanDetail(id) {
	$("#totalPlanId").val(id);
	
	$('#form-purchaseTotalplanitem-search').form('clear');
	
	var data = $("#isNew").combobox('getData');
	$("#isNew").combobox('select',data[2].value);
	
	//加载id到隐藏的数据项
	$("#id").val(id);
	//加载表头
  	getPurchaseTotalPlanItemHead(id);  
	debugger;
	$('#win-planitem-addoredit').window({
		iconCls:'icon-add',
		title:'<spring:message code="vendor.orderplan.detailsExpected"/>'
	});  

	$('#win-planitem-addoredit').window('open');

 	//显示计划详情
 	var searchParamArray = $('#form-purchaseTotalplanitem-search').serializeArray();
	var searchParams = $.jqexer.formToJson(searchParamArray);
	$('#datagrid-purchaseTotalplanitem-list').datagrid({   
    	url:'${ctx}/manager/order/purchaseTotalPlan/planTotalitem/' + id,
    	queryParams:searchParams,
		rowStyler:function(index,row){
			if (row.isNew == 1){
			  color = 'background-color:;color:green;font-weight:bold;';
            }else{
          	  color = 'background-color:;color:red;font-weight:bold;';
            }
			 return color
		}
	});
}


//删除
function deletePlan() {
	var selections = $('#datagrid-purchaseTotalplanitem-list').datagrid('getSelections');
	if(selections.length==0){
		$.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.noRecordSelected"/><spring:message code="vendor.orderplan.noRecordSelected"/>！','info');
		return false;
	}
	var rows = $('#datagrid-purchaseTotalplanitem-list').datagrid('getSelections');
	for(i = 0;i < rows.length;i++) {
		debugger;
		 if(rows[i].isNew != null && rows[i].isNew == 0) {
			 $.messager.alert('<spring:message code="vendor.orderplan.prompting"/>','<spring:message code="vendor.orderplan.cannotBeDeleted"/>！','error');
			 return false;
		}   
    }  
	var params = $.toJSON(selections);
	$.ajax({
		url:'${ctx}/manager/order/purchaseTotalPlan/deleteTotalPlan',
		type:'POST',
		data:params,
		dataType:"json",
		contentType : 'application/json',
		success:function(data){
			$.messager.show({
				title:'<spring:message code="vendor.orderplan.news"/>',
				msg:'<spring:message code="vendor.orderplan.operationSuccessful"/>',
				timeout:2000,
				showType:'show',
				style:{
					right:'',
					top:document.body.scrollTop+document.documentElement.scrollTop,
					bottom:''
				}
			});
			$('#datagrid-purchaseTotalplanitem-list').datagrid('reload');
		}   
	});
}

//导出
function exportPurchaseTotalPlan() {

	var totalPlanId = $("#totalPlanId").val();
	$('#form-purchaseTotalplanitem-search').form('submit',{
		url: '${ctx}/manager/order/purchaseTotalPlan/exportExcel/'+totalPlanId,
		success:function(data){
			$.messager.progress('close');
		}
	});
}


//导入当月计划
function importTotalPlan() {
	$('#form-totalPlan-import').form('clear');   
	$('#win-totalPlan-import').window('open');  
}  

//查询表头
function getPurchaseTotalPlanItemHead(id){
	//查询进来
	if(id == null){
      id =  $("#id").val();
	}
	//获取时间选择框内容

 	var startDate =$("input[name='search-EQ_startDate']").val()  ;
	var endDate = $("input[name='search-EQ_endDate']").val() ;
	if(startDate ==null && startDate == ""){
		startDate="";
	}
	if(endDate ==null && endDate == ""){
		endDate="";
	}
	
    $.ajax({  
    	url:'${ctx}/manager/order/purchaseTotalPlan/planTotalItemHead/' + id +"?"+"startDate=" +startDate +"&"+"endDate="+endDate,
        async: false, // 注意此处需要同步，因为先绑定表头，才能绑定数据   
        type:'POST',  
        dataType:'json',
        cache:false,
        success:function(datas){//获取表头数据成功后，使用easyUi的datagrid去生成表格   
        	$("#col1").text(datas.col1);   
        	$("#col2").text(datas.col2);  
        	$("#col3").text(datas.col3);  
        	$("#col4").text(datas.col4);  
        	$("#col5").text(datas.col5);  
        	$("#col6").text(datas.col6);  
        	$("#col7").text(datas.col7);  
        	$("#col8").text(datas.col8);  
        	$("#col9").text(datas.col9);  
        	$("#col10").text(datas.col10);  
        	$("#col11").text(datas.col11);  
        	$("#col12").text(datas.col12);  
        	$("#col13").text(datas.col13);  
        	$("#col14").text(datas.col14);  
        	$("#col15").text(datas.col15);  
        	$("#col16").text(datas.col16);  
        	$("#col17").text(datas.col17);  
        	$("#col18").text(datas.col18);  
        	$("#col19").text(datas.col19);  
        	$("#col20").text(datas.col20);  
        	$("#col21").text(datas.col21);  
        	$("#col22").text(datas.col22);  
        	$("#col23").text(datas.col23);  
        	$("#col24").text(datas.col24);  
        	$("#col25").text(datas.col25);  
        	$("#col26").text(datas.col26);  
        	$("#col27").text(datas.col27); 
        	$("#col28").text(datas.col28); 
        	
/*     		$('#datagrid-purchaseTotalplanitem-list').datagrid({
    			rowStyler:function(index,row){
    				if (row.isNew == 1){
    				  color = 'background-color:;color:green;font-weight:bold;';
                    }else{
                  	  color = 'background-color:;color:red;font-weight:bold;';
                    }
    				 return color
    			}
    		}); */
        }
    });
}

</script>
</body>
</html>
