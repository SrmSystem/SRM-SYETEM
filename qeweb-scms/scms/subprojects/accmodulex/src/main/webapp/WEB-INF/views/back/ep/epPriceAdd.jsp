<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
	<style>
		.showDiv{
			display: block;
		}
		
		.hideDiv{
			display: none;
		}
	</style>
	<script type="text/javascript" src="${ctx}/static/base/util/IsTimeUtil.js"></script>
	<script type="text/javascript">
	$.extend($.fn.validatebox.defaults.rules, {
	    comboVry: {
	        validator: function (value, param) {//param为默认值
	        	if (value == "" || value.indexOf('-全部-') >= 0) { 
	                return false;  
	             }else {  
	                return true;  
	             }     
	        },
	        message: '该输入项为必输项'
	    }
	});
	
	//表格无数据时显示没有符合条件的记录
	$.fn.datagrid.defaults.minHeight = 200;
	$.fn.datagrid.defaults.onLoadSuccess=function(data){
	 if (data.total == 0) {
		    var cols = $.data(this, 'datagrid').options.columns[0];
			var tr1 = $('<tr height="80px" style="font-size:20px;transform:rotate(0deg)"><td align="center" style="color:#aaa;border-width: 0 0 0 0;" colspan='+cols.length+'>没有符合条件的记录！</td></tr>');
			var tb = $(this).prev().find('.datagrid-body').find('tbody');
			tr1.prependTo(tb);
	 }
	}
	
	$("#buyerId").combobox({
		 onChange: function () {
				 $('#datagrid-item-vendor').datagrid('loadData', { total: 0, rows: [] });  
				 $('#datagrid-item-material').datagrid('loadData', { total: 0, rows: [] });  
			}
	});
	
	$("#joinWay").combobox({
		 onChange: function () {
			 var joinWayVal = $('#joinWay').combobox('getValue');
			 if(joinWayVal==1){
				$("#isShowTd").hide(); 
				$("#toolbar_item").removeClass("showDiv");
				 $("#toolbar_item").addClass("hideDiv");
				 $('#datagrid-item-vendor').datagrid('loadData', { total: 0, rows: [] });  
				 
			 /*  $("#materialToolbar_item").removeClass("hideDiv");
				 $("#materialToolbar_item").addClass("showDiv");
				 $('#datagrid-item-material').datagrid('loadData', { total: 0, rows: [] });   */
			 }else{
				 $("#isShowTd").show(); 
				  $("#toolbar_item").removeClass("hideDiv");
				 $("#toolbar_item").addClass("showDiv");
			 }
			}
	});
	$("#isDim").combobox({
		 onChange: function () {
			 var isDimVal = $('#isDim').combobox('getValue');
			 if(isDimVal==1){
				 //选择物料按钮的隐藏
				 $("#materialToolbar_item").removeClass("showDiv");
				 $("#materialToolbar_item").addClass("hideDiv");
				 $('#datagrid-item-material').datagrid('loadData', { total: 0, rows: [] });  
			 }else{
				//选择物料按钮的显示
				 $("#materialToolbar_item").removeClass("hideDiv");
				 $("#materialToolbar_item").addClass("showDiv");
			 }
			}
	});
	$(function(){
		var publishVal = $("#publishVal").val();
		var joinVal = $("#joinVal").val();
		var isDimVal = $("#isDimVal").val();
		 if(joinVal==1){
			 $("#isShowTd").hide();
		 }else{
			 $("#isShowTd").show(); 
		 }
		 if(publishVal==1){
			 $("#materialToolbar_item").addClass("hideDiv");//隐藏
			 $("#toolbar_item").addClass("hideDiv");//隐藏
		 }else{
			 if(joinVal==1){
				 $("#toolbar_item").addClass("hideDiv");//隐藏
			 }else{
				 $("#toolbar_item").addClass("showDiv");
				 if(isDimVal==1){
					$("#materialToolbar_item").addClass("hideDiv");//隐藏
				 }else{
					$("#materialToolbar_item").addClass("showDiv");
				}
			 }
		 }
	 })
	 
	function getQuoteStatus (status) {
		if(status == 0)
			return '未报价';
		else if(status == 1 )
			return '已报价';
		else 
			return '未报价';
	}
	</script>

<!-- 新增询价单 -->
<div class="easyui-panel" data-options="fit:true">
	<form title="新增询价单" id="form-epPrice-add" method="post" enctype="multipart/form-data">
		<input id="id" name="id" value="${epPriceId}" hidden="true" />
		<input id="publishVal" name="publishStatus" value="${newEpPrice.publishStatus}" hidden="true" />
		<input id="joinVal" value="${newEpPrice.joinWay}" hidden="true" />
	<%-- 	<input id="isDimVal" value="${newEpPrice.isDim}" hidden="true" /> --%>
	<div id="epPrice-item">
		<div>
		<c:if test="${newEpPrice.publishStatus ne 1 }">
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpPrice('save')">保存</a>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="saveEpPrice('publish')">保存并发布</a>
		</c:if>
		</div>
		<table style="padding:10px;width: 90%;margin: auto;text-align: right">
			<tr>
				<td width="25%">询价单号:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    ${newEpPrice.enquirePriceCode}
					</c:when>
					<c:otherwise>
						<input id="enquirePriceCode" name="enquirePriceCode" value="${newEpPrice.enquirePriceCode}" class="easyui-textbox" data-options="required:true,editable:false" style="width:120px;"/>
					</c:otherwise>
				</c:choose>
				</td>
				<td width="25%">项目名称:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    ${newEpPrice.projectName}
					</c:when>
					<c:otherwise>
						<input id="projectName" name="projectName" value="${newEpPrice.projectName}" class="easyui-textbox" data-options="required:true,prompt:'最多15个汉字',validType:'length[0,15]'" maxlength="15" style="width:120px;"/>
					</c:otherwise>
				</c:choose>
				</td>
				<td width="25%">报名截止时间:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    <fmt:formatDate value="${newEpPrice.applicationDeadline}" type="both" pattern="yyyy-MM-dd"/>
					</c:when>
					<c:otherwise>
						<input id="applicationDeadline" name="applicationDeadline" value="${newEpPrice.applicationDeadline}" class="easyui-datebox" data-options="required:true,editable:false,validType:'isTimeRight'" style="width:130px"/>
					</c:otherwise>
				</c:choose>
				</td>
				<td width="25%">报价截止时间:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    <fmt:formatDate value="${newEpPrice.quoteEndTime}" type="both" pattern="yyyy-MM-dd"/>
					</c:when>
					<c:otherwise>
						<input id="quoteEndTime" name="quoteEndTime" value="${newEpPrice.quoteEndTime}" class="easyui-datebox" data-options="required:true,editable:false,validType:'isTimeRight'" style="width:130px"/>
					</c:otherwise>
				</c:choose>
				</td>
			</tr>
			<tr>
				<td>
					报价方式:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					  <c:if test="${newEpPrice.quoteWay eq 0 }">分项报价</c:if><c:if test="${newEpPrice.quoteWay eq 1 }">整体报价</c:if>
					</c:when>
					<c:otherwise>
						<select class="easyui-combobox" id="quoteWay" name="quoteWay" data-options="required:true,validType:'comboVry',editable:false" style="width: 100px" >
						<option value="">-全部-</option>
						<option value="0" ${newEpPrice.quoteWay eq "0" ? "selected" : "" }>分项报价</option>
						<option value="1" ${newEpPrice.quoteWay eq "1" ? "selected" : "" }>整体报价</option>
						</select>
					</c:otherwise>
				</c:choose>
				</td>
				<td>参与方式:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					  <c:if test="${newEpPrice.joinWay eq 0 }">邀请</c:if><c:if test="${newEpPrice.joinWay eq 1 }">公开</c:if>
					</c:when>
					<c:otherwise>
						<select class="easyui-combobox" id="joinWay" name="joinWay" valueField="${newEpPrice.joinWay}" data-options="required:true,validType:'comboVry',editable:false" style="width: 100px">
						<option value="">-全部-</option>
						<option value="0" ${newEpPrice.joinWay eq "0" ? "selected" : "" }>邀请</option>
						<option value="1" ${newEpPrice.joinWay eq "1" ? "selected" : "" }>公开</option>
						</select>
					</c:otherwise>
				</c:choose>
				</td>
				<%-- <td>报价结果公开方式:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					  <c:if test="${newEpPrice.resultOpenWay eq 0 }">全公开</c:if><c:if test="${newEpPrice.resultOpenWay eq 1 }">仅公开供应商</c:if>
					  <c:if test="${newEpPrice.resultOpenWay eq 2 }">仅公开价格</c:if><c:if test="${newEpPrice.resultOpenWay eq 3 }">不公开</c:if>
					</c:when>
					<c:otherwise>
						<select class="easyui-combobox" id="resultOpenWay" name="resultOpenWay" data-options="required:true,validType:'comboVry'" style="width: 100px" >
						<option value="">-全部-</option>
						<option value="0" ${newEpPrice.resultOpenWay eq "0" ? "selected" : "" }>全公开</option>
						<option value="1" ${newEpPrice.resultOpenWay eq "1" ? "selected" : "" }>仅公开供应商</option>
						<option value="2" ${newEpPrice.resultOpenWay eq "2" ? "selected" : "" }>仅公开价格</option>
						<option value="3" ${newEpPrice.resultOpenWay eq "3" ? "selected" : "" }>不公开</option>
						</select>
					</c:otherwise>
				</c:choose>
				</td> --%>
				<td>报价类型:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					  <c:if test="${newEpPrice.quoteType eq 0 }">新产品报价</c:if><c:if test="${newEpPrice.quoteType eq 1 }">商改产品报价</c:if>
					</c:when>
					<c:otherwise>
						<select class="easyui-combobox" id="quoteType" name="quoteType" data-options="required:false,validType:'comboVry',editable:false" style="width: 100px" >
						<option value="">-全部-</option>
						<option value="0" ${newEpPrice.quoteType eq "0" ? "selected" : "" }>新产品报价</option>
						<option value="1" ${newEpPrice.quoteType eq "1" ? "selected" : "" }>商改产品报价</option>
						</select>
					</c:otherwise>
				</c:choose>
				</td>
				<td>采购组织:
					<c:choose>
					  <c:when test="${newEpPrice.publishStatus eq 1}">
							${newEpPrice.buyer.name}
					</c:when>
					<c:otherwise>
					
					<input id="buyerId" class="easyui-combobox" data-options="editable:false" name="buyerId" value="${newEpPrice.buyerId}"
						data-options="
						url:'${ctx}/manager/vendor/vendorNav/getOrgCombo',
						valueField : 'value',
						textField : 'text',
						required:true"
					/>
					
					</c:otherwise>
					</c:choose>
				</td>
			</tr>
			<tr>
				
				<td>价格有效开始时间:
					<c:choose>
						<c:when test="${newEpPrice.publishStatus eq 1}">
						     <fmt:formatDate value="${newEpPrice.priceStartTime}" type="both" pattern="yyyy-MM-dd"/>
						</c:when>
						<c:otherwise>
							<input id="priceStartTime" name="priceStartTime" value="${newEpPrice.priceStartTime}" class="easyui-datebox" data-options="required:false,editable:false" style="width:130px"/>
						</c:otherwise>
					</c:choose>
				</td>
				<td>价格有效结束时间:
					<c:choose>
						<c:when test="${newEpPrice.publishStatus eq 1}">
						   <fmt:formatDate value="${newEpPrice.priceEndTime}" type="both" pattern="yyyy-MM-dd"/>
						</c:when>
						<c:otherwise>
							<input id="priceEndTime" name="priceEndTime" value="${newEpPrice.priceEndTime}" class="easyui-datebox" data-options="required:true,editable:false" style="width:130px"/>
						</c:otherwise>
					</c:choose>
				</td>
<%-- 				<td id="isShowTd">是否按供应商维度询价:
					<c:choose>
					  <c:when test="${newEpPrice.publishStatus eq 1}">
					  <c:if test="${newEpPrice.isDim eq '0' }">否</c:if><c:if test="${newEpPrice.isDim eq '1' }">是</c:if>
					</c:when>
					<c:otherwise>
						<select class="easyui-combobox" id="isDim" name="isDim" valueField="${newEpPrice.isDim}" data-options="required:true,validType:'comboVry'" style="width: 100px">
						<option value="0" ${newEpPrice.isDim eq "0" ? "selected" : "" } selected="selected">否</option>
						<option value="1" ${newEpPrice.isDim eq "1" ? "selected" : "" }>是</option>
						</select>
					</c:otherwise>
					</c:choose>				
				</td> --%>
			</tr>
			<tr>
				<td colspan="4">询价公告:
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					  <input class="easyui-textbox" id="remarks" name="remarks" value="${newEpPrice.remarks}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px" readonly="readonly"/>
					</c:when>
					<c:otherwise>
						<input class="easyui-textbox" id="remarks" name="remarks" value="${newEpPrice.remarks}" type="text" max="300"
					data-options="multiline:true,prompt:'输入文本(不超过300字)。。。',validType:'length[0,300]'" style="width:90%;height: 80px"/>
					</c:otherwise>
				</c:choose>
				</td>
			</tr>
			<tr>
				<td colspan="2">询价附件上传: 
				<c:if test="${newEpPrice.publishStatus ne 1}">
				<input class="easyui-filebox" name="quoteSpecFile" data-options="buttonText:'浏览'"/>
				</c:if>
				<a href="javascript:;" onclick="File.download('${newEpPrice.quoteSpecificationUrl}','${newEpPrice.quoteSpecFileName}')">${newEpPrice.quoteSpecFileName}</a>
				</td>
				<td colspan="2">特殊报价模板上传:
				<c:if test="${newEpPrice.publishStatus ne 1}">
				<input class="easyui-filebox" name="quoteTemplateFile" data-options="buttonText:'浏览'"/>
				</c:if>
				<a href="javascript:;" onclick="File.download('${newEpPrice.quoteTemplateUrl }','${newEpPrice.quoteTemplateFileName}')">${newEpPrice.quoteTemplateFileName}</a>
				</td>
			</tr>
	<%-- 		<tr>
				<td>科长:
				<input type="text" id="personId1" name="signPerson1Id" value="${newEpPrice.signPerson1.id}" hidden="true" />
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    ${newEpPrice.signPerson1.name}
					</c:when>
					<c:otherwise>
						<input type="text" id="singPerson1" name="person1Name" class="easyui-textbox" style="width:100px;" data-options="editable:false" value="${newEpPrice.signPerson1.name}"/>
			  			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('1','CountersignPerson')"></a>					
			  		</c:otherwise>
				</c:choose>
				</td>
				<td>采购部长: 
				<input type="text" id="personId2" name="signPerson2Id" value="${newEpPrice.signPerson2.id}" hidden="true" />
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    ${newEpPrice.signPerson2.name}
					</c:when>
					<c:otherwise>
						<input type="text" id="singPerson2" name="person2Name" class="easyui-textbox" style="width:100px;" data-options="editable:false" value="${newEpPrice.signPerson2.name}"/>
			  			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('2','CountersignPerson')"></a>					
			  		</c:otherwise>
				</c:choose>
				</td>
				<td>采购副总经理: 
				<input type="text" id="personId3" name="signPerson3Id" value="${newEpPrice.signPerson3.id}" hidden="true" />
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    ${newEpPrice.signPerson3.name}
					</c:when>
					<c:otherwise>
						<input type="text" id="singPerson3" name="person3Name" class="easyui-textbox" style="width:100px;" data-options="editable:false" value="${newEpPrice.signPerson3.name}"/>
			  			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('3','CountersignPerson')"></a>					
			  		</c:otherwise>
				</c:choose>
				</td>
				<td>技术中心: 
				<input type="text" id="personId4" name="signPerson4Id" value="${newEpPrice.signPerson4.id}" hidden="true" />
				<c:choose>
					<c:when test="${newEpPrice.publishStatus eq 1}">
					    ${newEpPrice.signPerson4.name}
					</c:when>
					<c:otherwise>
						<input type="text" id="singPerson4" name="person4Name" class="easyui-textbox" style="width:100px;" data-options="editable:false" value="${newEpPrice.signPerson4.name}"/>
			  			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-more'" onclick="UserManage.openWin('4','CountersignPerson')"></a>					
			  		</c:otherwise>
				</c:choose>
				</td>
			</tr>
			<tr>
				<td>审批部门: 
					<c:choose>
					  <c:when test="${newEpPrice.publishStatus eq 1}">
					  <c:if test="${newEpPrice.checkDep eq 0 }">技术审批</c:if><c:if test="${newEpPrice.checkDep eq 1 }">工艺审批</c:if>
					</c:when>
					<c:otherwise>
						<select class="easyui-combobox" id="checkDep" name="checkDep" valueField="${newEpPrice.checkDep}" data-options="required:true,validType:'comboVry'" style="width: 100px">
						<option value="">-全部-</option>
						<option  value="1" ${newEpPrice.checkDep eq 1 ? "selected" : "" }>技术审批</option>
						<option value="0" ${newEpPrice.checkDep eq 0 ? "selected" : "" }>工艺审批</option>
						</select>
					</c:otherwise>
					</c:choose>
				</td>
				<td></td>
				<td></td>
				<td></td>
			</tr> --%>
		</table>
	</div>
  	<div id="item-material" style="margin-top: 20px;height: 350px ">
  	<c:if test="${newEpPrice.publishStatus eq 1}">
		 <table title="有料号物料列表" id="datagrid-item-material" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epPrice/getEpMaterialList/${epPriceId}',iconCls:'icon-edit',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#materialToolbar',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,onClickCell: RowEditor.onClickCell">
	</c:if>
	<c:if test="${newEpPrice.publishStatus ne 1}">  
	  	<table title="有料号物料列表" id="datagrid-item-material" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epPrice/beforePublishMaterial/${epPriceId}',iconCls:'icon-edit',method:'post',toolbar:'#materialToolbar',
	    	fit:true,singleSelect:false,border:false,rownumbers:false,fitColumns:false,onClickCell: RowEditor.onClickCell">
	</c:if>
	        <thead><tr>
	        	<th data-options="field:'id',checkbox:true"></th> 
	        	<c:if test="${newEpPrice.publishStatus ne 1}">
	        	<th data-options="field:'opt',formatter:optMaterialFmt">操作</th>
	        	</c:if>
	        	  	<c:if test="${newEpPrice.quoteStatus eq 2}">
	        	  		<th data-options="field:'optx',formatter:optCompareFmt">操作</th>
	        	  	</c:if>
	        	<th data-options="field:'materialId',hidden:true">物料id</th>
	        	<th data-options="field:'materialCode'">物料编码</th> 
	        	<th data-options="field:'materialName'">物料名称</th> 
	        	<th data-options="field:'materialSpec'">型号/规格</th> 
	        	<th data-options="field:'materialUnit'">计量单位</th> 
	        	<th data-options="field:'expectedBrand',editor:'textbox'">产地/品牌</th>
	        	<th data-options="field:'warranty',editor:'textbox'">保修期</th>
	        	<th data-options="field:'freight',editor:'numberbox'">运输费</th>
	        	<th data-options="field:'planPurchaseQty',editor:{type:'numberbox',options:{required:false}}">预计采购量</th> 
	        	<th data-options="field:'arrivalTime',editor:{type:'datebox',options:{required:false,validType:'isTimeRight'}}" formatter="formatterdate" width="180px">期望到库日期</th> 	
<%--         	    <th data-options="field:'compareMaterialCode',
        	    	editor:{
						type:'combogrid',
						options:{
							panelWidth:380,
							idField:'code',
							textField:'code',
							method: 'post',
							pagination: true,            
		 					rownumbers: true,           
							url:'${ctx}/manager/vendorSelection/buyerMaterial/getMaterialList',
							columns:[[
								{field:'code',title:'编码'},
								{field:'name',title:'名称'}
							]]
						}
					}
        	    ">类比零部件编码</th>      
        	    <th data-options="field:'compareMaterialPrice',editor:'numberbox'">类比零部件现价</th>
				<th data-options="field:'compareVendorName',
					editor:{
						type:'combogrid',
						options:{
							panelWidth:380,
							idField:'name',
							textField:'name',
							method: 'post',
							pagination: true,            
		 					rownumbers: true,           
							url:'${ctx}/manager/ep/epPrice/getOrgList',
							columns:[[
								{field:'code',title:'编码'},
								{field:'name',title:'名称'}
							]]
						}
					}
				">类比零部件供应商</th>
        	    <th data-options="field:'productStatusDiffer',editor:'textbox'">产品状态差异说明</th>   --%>  	
	        	<th data-options="field:'remarks',editor:'textbox'" width="200px">备注</th>  
	       	</tr></thead>
	   	</table>
	   	<c:if test="${newEpPrice.publishStatus ne 1 }">
	   	<div id="materialToolbar" style="padding:5px;height:auto">
   			<div id="materialToolbar_item" >
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="MaterialManage.openWin()">选择物料</a>
			</div>
		</div>
		</c:if>
   	</div>
   	
   	<div id="item-vendor" class="easyui-panel" style="margin-top: 10px;;height: 280px ">
   	<c:if test="${newEpPrice.publishStatus ne 1}"> 
		<table title="供应商列表" id="datagrid-item-vendor" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epPrice/beforePublishVendor/${epPriceId}',iconCls:'icon-edit',method:'post',toolbar:'#vendorToolbar',
	    	fit:true,singleSelect:false,border:false,rownumbers:false,fitColumns:false,onClickCell: RowEditor.onClickCell">
	</c:if>    	
	<c:if test="${newEpPrice.publishStatus eq 1}">     	
	    	<table title="供应商列表" id="datagrid-item-vendor" class="easyui-datagrid" data-options="url:'${ctx}/manager/ep/epPrice/getEpVendorList/${epPriceId}',iconCls:'icon-edit',method:'post',singleSelect:false,
		fit:true,border:false,toolbar:'#vendorToolbar',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,onClickCell: RowEditor.onClickCell">
	</c:if>
		     <thead><tr>
		     	<th data-options="field:'id',checkbox:true"></th> 
		     	<th data-options="field:'opt',formatter:optVendorFmt">操作</th> 
		     	<th data-options="field:'vendorId',hidden:true">供应商Id</th> 
		     	<th data-options="field:'vendorCode'">供应商编码</th> 
		     	<th data-options="field:'vendorName'">供应商名称</th> 
		     	<th data-options="field:'accessStatus'">准入状态</th>	<!-- 选择供应商自动带出  -->
		     	<th data-options="field:'address'">地址</th> 
		     	<th data-options="field:'legalRep',formatter:function(v,r,i){if(v == 'null' || v == null) return ' ';else return v; }">联系人</th> 
		     	<th data-options="field:'linkPhone',formatter:function(v,r,i){if(v == 'null' || v== null) return ' ';else return v; }">联系电话</th> 
		     	<th data-options="field:'orgEmail',formatter:function(v,r,i){if(v == 'null' || v== null) return ' ';else return v; }">Email</th> 
		     	<th data-options="field:'quoteStatus',formatter:function(v,r,i){return getQuoteStatus(v);}">报价状态</th>
		  <!--    	<th data-options="field:'eipApprovalStatus',formatter:function(v,r,i){if(v == null || v=='') return '未审核';else return StatusRender.render(v,'audit',false); }">审核状态</th> -->
		     </tr></thead>
	   	</table>
   		<div id="vendorToolbar" style="padding:5px;height:auto;" >
   			<div id="toolbar_item" >
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true" onclick="OrgManage.openWin()">选择供应商</a>
			</div>
		</div>
   	</div>
   
   	</form> 
</div>

<!-- 供应商信息 -->
<div id="win-org-detail" class="easyui-window" title="供应商信息"
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="vendorListToolbar" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="OrgManage.choice('datagrid-item-vendor')">选择</a>
			</div>
			<div>
			<form id="form-org-search" method="post">
				编码：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
				名称：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
				<input type="hidden" name="search_EQ_roleType" value="1"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="OrgManage.searchOrg()">查询</a>
		</form>
	</div>
		</div>
		<table id="datagrid-org-list" title="供应商信息"
			data-options="method:'post',singleSelect:false,
			pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,30]">
			<thead><tr>
			<th data-options="field:'id',checkbox:true"></th>
			<th data-options="field:'code'">供应商编码 </th>
			<th data-options="field:'name'">供应商名称</th>
			<th data-options="field:'legalPerson'">联系人</th>
			<th data-options="field:'phone'">联系电话</th>
			<th data-options="field:'email'">email</th>
			</tr></thead>
		</table>
	</div>
</div>

<!-- 物料信息 -->
<div id="win-material-detail" class="easyui-window" title="物料信息"
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="materialListToolbar" style="padding:5px;">
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="MaterialManage.choice('datagrid-item-material')">选择 </a>
			</div>
			<div>
			<form id="form-material-search" method="post">
				物料编码：<input type="text" name="search_LIKE_code" class="easyui-textbox" style="width:80px;"/>
				物料名称：<input type="text" name="search_LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="MaterialManage.searchMaterial()">查询</a>
		</form>
	</div>
		</div>
		<table id="datagrid-material-list" title="物料信息"
			data-options="method:'post',singleSelect:false,
			pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,30]">
			<thead><tr>
			<th data-options="field:'id',checkbox:true"></th>
			<th data-options="field:'code'">物料编码</th>
			<th data-options="field:'name'">物料名称</th>
			<th data-options="field:'specification'">规格</th>
			<th data-options="field:'unit'">单位</th>
			</tr></thead>
		</table>
	</div>
</div>

<!-- 会签人信息 -->
<div id="win-signPerson-detail" class="easyui-window" title="会签人信息" 
	data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">
		<div class="easyui-panel" data-options="fit:true">
			<div id="userListToolbar" style="padding:5px;">
			<input type="text" id="tempVal" hidden="true"/>
			<div>
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="UserManage.choice('datagrid-signPerson-list')">选择 </a>
			</div>
			<div>
			<form id="form-signPerson-search" method="post">
				用户编码：<input type="text" name="search-LIKE_loginName" class="easyui-textbox" style="width:80px;"/>
				用户名称：<input type="text" name="search-LIKE_name" class="easyui-textbox" style="width:80px;"/>
			<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="UserManage.search()">查询</a>
		</form>
	</div>
		</div>
		<table id="datagrid-signPerson-list" title="会签人信息"
			data-options="method:'post',singleSelect:false,
			pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,30]">
			<thead><tr>
				<th width="30px" data-options="field:'id',checkbox:true"></th>
				<th width="30%" data-options="field:'loginName'">用户编码</th>
				<th width="30%" data-options="field:'name'">用户名称</th>
			</tr></thead>
		</table>
	</div>
</div>