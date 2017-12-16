<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   

<html>
	<head>
		<title><spring:message code="vendor.buyerBoard.buyerKanban"/><!-- 采购员看板 --></title>
		<script type="text/javascript" src="${ctx}/static/script/basedata/common.js"></script>
		<script type="text/javascript" src="${ctx}/static/script/buyerBoard/buyerBoard.js"></script> 
	</head>
	
	<body style="margin:0;padding:0;">
		<table id="buyerBoardDatagrId"
			   data-options="fit:true,method:'post',singleSelect:false,   
			   toolbar:'#buyerBoardDatagridToolbar',
			   pagination:true,rownumbers:true,pageSize:12,pageList:[12,20]">
			<thead><tr>
			<th data-options="field:'factoryCode'"><spring:message code="vendor.orderplan.factory"/><!-- 工厂 --></th>	
			<th data-options="field:'materialCode'"><spring:message code="vendor.buyerBoard.material"/><!-- 物料 --></th>
			<th data-options="field:'materialName'" ><spring:message code="vendor.orderplan.materialDescription"/><!-- 物料描述 --></th>
			<th data-options="field:'vendorCode'" ><spring:message code="vendor.suppliers"/><!-- 供应商 --></th>
			<th data-options="field:'proportion'" ><spring:message code="vendor.buyerBoard.accountedFor"/><!-- 占比 --></th>
			<th data-options="field:'groupCode'"><spring:message code="vendor.buyerBoard.purchasingGroup"/><!-- 采购组 --></th>
			<th data-options="field:'plannedTurnaroundDay'"><spring:message code="vendor.buyerBoard.plannedTurnoverDays"/><!-- 计划周转天数 --></th>
			<th data-options="field:'qualityDay'"><spring:message code="vendor.buyerBoard.qualityInspectionDays"/><!-- 质量检验天数 --></th>
			<th data-options="field:'deliveryFrequency'"><spring:message code="vendor.buyerBoard.deliveryFrequency"/><!-- 送货频率 --></th>
			<th data-options="field:'transportationDay'"><spring:message code="vendor.buyerBoard.shippingDays"/><!-- 运输天数 --></th>
			<th data-options="field:'unit'"><spring:message code="vendor.unit"/><!-- 单位 --></th>
			<th data-options="field:'dtype'"></th>
			
			<th id="dat01" data-options="field:'qty01'"></th><th id="dat02" data-options="field:'qty02'"></th><th id="dat03" data-options="field:'qty03'"></th><th id="dat04" data-options="field:'qty04'"></th>
			<th id="dat05" data-options="field:'qty05'"></th><th id="dat06" data-options="field:'qty06'"></th><th id="dat07" data-options="field:'qty07'"></th><th id="dat08" data-options="field:'qty08'"></th>
			<th id="dat09" data-options="field:'qty09'"></th><th id="dat10" data-options="field:'qty10'"></th><th id="dat11" data-options="field:'qty11'"></th><th id="dat12" data-options="field:'qty12'"></th>
			<th id="dat13" data-options="field:'qty13'"></th><th id="dat14" data-options="field:'qty14'"></th><th id="dat15" data-options="field:'qty15'"></th><th id="dat16" data-options="field:'qty16'"></th>
			<th id="dat17" data-options="field:'qty17'"></th><th id="dat18" data-options="field:'qty18'"></th><th id="dat19" data-options="field:'qty19'"></th><th id="dat20" data-options="field:'qty20'"></th>
			<th id="dat21" data-options="field:'qty21'"></th><th id="dat22" data-options="field:'qty22'"></th><th id="dat23" data-options="field:'qty23'"></th><th id="dat24" data-options="field:'qty24'"></th>
			<th id="dat25" data-options="field:'qty25'"></th><th id="dat26" data-options="field:'qty26'"></th><th id="dat27" data-options="field:'qty27'"></th><th id="dat28" data-options="field:'qty28'"></th>
			<th id="dat29" data-options="field:'qty29'"></th><th id="dat30" data-options="field:'qty30'"></th><th id="dat31" data-options="field:'qty31'"></th><th id="dat32" data-options="field:'qty32'"></th>
			<th id="dat33" data-options="field:'qty33'"></th><th id="dat34" data-options="field:'qty34'"></th><th id="dat35" data-options="field:'qty35'"></th><th id="dat36" data-options="field:'qty36'"></th>
			<th id="dat37" data-options="field:'qty37'"></th><th id="dat38" data-options="field:'qty38'"></th><th id="dat39" data-options="field:'qty39'"></th><th id="dat40" data-options="field:'qty40'"></th>
			<th id="dat41" data-options="field:'qty41'"></th><th id="dat42" data-options="field:'qty42'"></th><th id="dat43" data-options="field:'qty43'"></th><th id="dat44" data-options="field:'qty44'"></th>
			<th id="dat45" data-options="field:'qty45'"></th><th id="dat46" data-options="field:'qty46'"></th><th id="dat47" data-options="field:'qty47'"></th><th id="dat48" data-options="field:'qty48'"></th>
			<th id="dat49" data-options="field:'qty49'"></th><th id="dat50" data-options="field:'qty50'"></th><th id="dat51" data-options="field:'qty51'"></th><th id="dat52" data-options="field:'qty52'"></th>
			<th id="dat53" data-options="field:'qty53'"></th><th id="dat54" data-options="field:'qty54'"></th><th id="dat55" data-options="field:'qty55'"></th><th id="dat56" data-options="field:'qty56'"></th>
			<th id="dat57" data-options="field:'qty57'"></th><th id="dat58" data-options="field:'qty58'"></th><th id="dat59" data-options="field:'qty59'"></th><th id="dat60" data-options="field:'qty60'"></th>
			</tr></thead>
		</table>
		<div id="buyerBoardDatagridToolbar" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-package_green',plain:true" onclick="buyerBoard.exportBP()"><spring:message code="vendor.orderplan.derivation"/><!-- 导出 --></a>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="synBuyerBoard()"><spring:message code="vendor.buyerBoard.synchronousBuyerKanban"/><!-- 同步采购员看板 --></a>
			</div>
			<div>
				<form id="form-bb-search" method="post">
					<spring:message code="vendor.orderplan.factory"/><!-- 工厂 -->：<input type="text" name="search-LIKE_factoryCode" class="easyui-textbox" style="width:100px;"/>
					<spring:message code="vendor.buyerBoard.material"/><!-- 物料 -->：<input type="text" name="search-materialCode" class="easyui-textbox" style="width:100px;"/>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="buyerBoard.search()"><spring:message code="vendor.enquiries"/><!-- 查询 --></a>
					<a href="javascript:;" class="easyui-linkbutton" onclick="$('#form-ppb-search').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
				</form>
			</div>
		</div>
		
		<!-- 同步采购员看板 -->
 	<div id="win-sync-addBuyerBoard" class="easyui-window"
		style="width: 300px; height: 200px"
		data-options="iconCls:'icon-add',modal:true,closed:true,buttons:'#dialog-adder-buyerBoard'">
		<form id="form-sync-addBuyerBoard" method="post" onkeydown="if(event.keyCode==13)return false;" >
			<table style="text-align: right; padding: 5px; margin: auto;" cellpadding="5">
			<tr>
				<td ><spring:message code="vendor.orderplan.factoryCode"/><!-- 工厂编码 -->:</td>
				<td >
					<input  id ="factoryCode" class="easyui-textbox"  name="factoryCode" value=""  data-options="required:true" />
				</td>
			</tr>
			<tr>
				<td ><spring:message code="vendor.materialCode"/><!-- 物料编码 --></td>
				<td >
					<input  id ="materialCode" class="easyui-textbox"  name="materialCode" value=""  data-options="required:true" />
				</td>
				<td> <a href="javascript:;" class="easyui-linkbutton" style="width:30px" data-options="iconCls:'icon-search'," onclick="buyerBoard.sysOpenMaterialBB()"></a></td>
			</tr>
			</table>
		</form>
		<div id="dialog-adder-buyerBoard" style="padding:5px;">
			<a href="javascript:;" class="easyui-linkbutton"
				onclick="buyerBoard.synBuyerBoard()"><spring:message code="vendor.submit"/><!-- 提交 --></a> <a
				href="javascript:;" class="easyui-linkbutton"
				onclick="$('#form-sync-addBuyerBoard').form('reset')"><spring:message code="vendor.resetting"/><!-- 重置 --></a>
		</div>
	</div>
	
	 <!--   同步物料的查询 -->
   <div id="win-sysMaterialBB-detail" class="easyui-window" title="选择物料" style="width:600px;height:450px" data-options="iconCls:'icon-add',modal:true,closed:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="div_sysMaterial-_detail" style="padding:5px;">
				<div>
					<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerBoard.sysChoiceMaterial()"><spring:message code="vendor.orderplan.confirm"/><!-- 确认 --></a>
				</div>
				<div>
	             <a class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true"  onclick="buyerBoard.addMaterialSys();" ><spring:message code="vendor.orderGoodsRequret.readClipboardContents"/><!-- 读取剪贴板内容 --></a>
				 <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="buyerBoard.insertSys()"><spring:message code="vendor.orderGoodsRequret.adds"/><!-- 添加 --></a>
			     <a href="javascript:void(0)" class="easyui-linkbutton" data-options="iconCls:'icon-delete',plain:true" onclick="buyerBoard.removeitSys()"><spring:message code="vendor.orderplan.deleting"/><!-- 删除 --></a>
	             <a href="javascript:void(0)" class="easyui-linkbutton" data-options="plain:true" onclick="buyerBoard.resetMaterialSys()"><spring:message code="vendor.orderGoodsRequret.emptying"/><!-- 清空 --></a>
				</div>
			</div>
			<table id="datagrid-sysMaterialBB-list" title="物料列表" class="easyui-datagrid"
				data-options="method:'post',singleSelect:false,idField : 'id',onClickRow: buyerBoard.onClickRowSys,
				rownumbers:true">
				<thead><tr>
			 		<th data-options="field:'code' ,editor:'textbox' ,width:200"><spring:message code=""/>物料编码</th>
				</tr></thead>
			</table>
		</div>
	</div>
		
		<script type="text/javascript">
		
		//同步采购员看板 
		function synBuyerBoard(){
				$('#win-sync-addBuyerBoard').dialog({
					iconCls:'icon-edit',
					title:'<spring:message code="vendor.buyerBoard.synchronousBuyerKanban"/>'/* 同步采购员看板 */
				});
				$('#form-sync-addBuyerBoard').form('clear');
				$('#win-sync-addBuyerBoard').dialog('open');
		}
		
		function mergeCellsByField(tableID, colList) {
		     var ColArray = colList.split(",");
		     var tTable = $("#" + tableID);
		     var TableRowCnts = tTable.datagrid("getRows").length;
		     var tmpA;
		     var tmpB;
		     var PerTxt = "";
		     var CurTxt = "";
		     var alertStr = "";
		     for (j = ColArray.length - 1; j >= 0; j--) {
		         PerTxt = "";
		         tmpA = 1;
		         tmpB = 0;

		         for (i = 0; i <= TableRowCnts; i++) {
		             if (i == TableRowCnts) {
		                 CurTxt = "";
		             }
		             else {
		                CurTxt = tTable.datagrid("getRows")[i].factoryCode+tTable.datagrid("getRows")[i].materialCode+tTable.datagrid("getRows")[i].vendorCode+tTable.datagrid("getRows")[i].groupCode; 
		             }
		             if (PerTxt == CurTxt) {
		                 tmpA += 1;
		             }
		             else {
		                 tmpB += tmpA;
		                 
		                 tTable.datagrid("mergeCells", {
		                     index: i - tmpA,
		                     field: ColArray[j],//合并字段
		                     rowspan: tmpA,
		                     colspan: null
		                 });
		                 tmpA = 1;
		             }
		             PerTxt = CurTxt;
		         }
		     }
		 }
		
		
		$(function() {
			//加载表头
			getBuyerBoardHead();
			var searchParamArray = $('#form-bb-search').serializeArray();
			var searchParams = $.jqexer.formToJson(searchParamArray);
			$('#buyerBoardDatagrId').datagrid({   
		    	url:'${ctx}/manager/order/buyerBoard/getList',
		    	queryParams:searchParams,
		    	onLoadSuccess: function(data) {
			    	  if (data.rows.length > 0) {
		                     //调用mergeCellsByField()合并单元格
		                    mergeCellsByField("buyerBoardDatagrId", 'factoryCode,materialCode,materialName,vendorCode,proportion,groupCode,plannedTurnaroundDay,qualityDay,,deliveryFrequency,transportationDay,unit');
		                 }
			    }
		    	
			});
		});
		
		
		function getBuyerBoardHead(){
		    $.ajax({  
		    	url:'${ctx}/manager/order/buyerBoard/getBuyerBoardHead',
		        async: false, // 注意此处需要同步，因为先绑定表头，才能绑定数据   
		        type:'POST',  
		        dataType:'json',  
		        cache:false,
		        success:function(datas){//获成功后，使用easyUi的datagrid去生成表格   	
		        	if(datas.entity!=null && datas.entity!='' && datas.entity!=undefined){
			        	$("#dat01").text(datas.entity.dat01);$("#dat02").text(datas.entity.dat02);$("#dat03").text(datas.entity.dat03);$("#dat04").text(datas.entity.dat04);
			        	$("#dat05").text(datas.entity.dat05);$("#dat06").text(datas.entity.dat02);$("#dat07").text(datas.entity.dat07);$("#dat08").text(datas.entity.dat08);
			        	$("#dat09").text(datas.entity.dat09);$("#dat10").text(datas.entity.dat10);$("#dat11").text(datas.entity.dat11);$("#dat12").text(datas.entity.dat12);
			        	$("#dat13").text(datas.entity.dat13);$("#dat14").text(datas.entity.dat14);$("#dat15").text(datas.entity.dat15);$("#dat16").text(datas.entity.dat16);
			        	$("#dat17").text(datas.entity.dat17);$("#dat18").text(datas.entity.dat18);$("#dat19").text(datas.entity.dat19);$("#dat20").text(datas.entity.dat20);
			        	$("#dat21").text(datas.entity.dat21);$("#dat22").text(datas.entity.dat22);$("#dat23").text(datas.entity.dat23);$("#dat24").text(datas.entity.dat24);
			        	$("#dat25").text(datas.entity.dat25);$("#dat26").text(datas.entity.dat26);$("#dat27").text(datas.entity.dat27);$("#dat28").text(datas.entity.dat28);
			        	$("#dat29").text(datas.entity.dat29);$("#dat30").text(datas.entity.dat30);$("#dat31").text(datas.entity.dat31);$("#dat32").text(datas.entity.dat32);
			        	$("#dat33").text(datas.entity.dat33);$("#dat34").text(datas.entity.dat34);$("#dat35").text(datas.entity.dat35);$("#dat36").text(datas.entity.dat36);
			        	$("#dat37").text(datas.entity.dat37);$("#dat38").text(datas.entity.dat38);$("#dat39").text(datas.entity.dat39);$("#dat40").text(datas.entity.dat40);
			        	$("#dat41").text(datas.entity.dat41);$("#dat42").text(datas.entity.dat42);$("#dat43").text(datas.entity.dat43);$("#dat44").text(datas.entity.dat44);
			        	$("#dat45").text(datas.entity.dat45);$("#dat46").text(datas.entity.dat46);$("#dat47").text(datas.entity.dat47);$("#dat48").text(datas.entity.dat48);
			        	$("#dat49").text(datas.entity.dat49);$("#dat50").text(datas.entity.dat50);$("#dat51").text(datas.entity.dat51);$("#dat52").text(datas.entity.dat52);
			        	$("#dat53").text(datas.entity.dat53);$("#dat54").text(datas.entity.dat54);$("#dat55").text(datas.entity.dat55);$("#dat56").text(datas.entity.dat56);
			        	$("#dat57").text(datas.entity.dat57);$("#dat58").text(datas.entity.dat58);$("#dat59").text(datas.entity.dat59);$("#dat60").text(datas.entity.dat60);
		        	}
		        }  
		    });
		}
		</script> 
	</body>
</html>