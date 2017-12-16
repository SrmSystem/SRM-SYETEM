<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<head>
<script type="text/javascript" src="${ctx}/static/script/ep/epSubQuo.js"></script>
<script type="text/javascript"
	src="${ctx}/static/script/ep/epWholeQuoManage.js"></script>

<script type="text/javascript">
	function optFmt(v, r, i) {
		return '';
	}

	function initPrice() {
		var isIncludeTaxVal = $('#isIncludeTax').combobox('getValue');
		var rows = $("#datagrid-item-list").datagrid("getRows");
		var tt = 0;
		for (var i = 0; i < rows.length; i++) {
			var total = $('#datagrid-item-list').datagrid('getEditor', {
				index : i,
				field : 'subtotal'
			});
			if (total == null) {
				tt = Number(tt) + Number(rows[i].subtotal);
			} else {
				tt = Number(tt) + Number(total.target.val());
			}
		}

		if (isIncludeTaxVal == null || isIncludeTaxVal == "") {
			return false;
		} else if (isIncludeTaxVal == 0) {
			$("#totalQuotePrice").textbox('setValue', tt);
		} else if (isIncludeTaxVal == 1) {
			$("#quotePrice").textbox('setValue', tt);
		}
	}

	var editIndex = undefined;
	function endEditing() {
		if (editIndex == undefined) {
			return true
		}
		if ($('#datagrid-item-list').datagrid('validateRow', editIndex)) {
			$('#datagrid-item-list').datagrid('endEdit', editIndex);
			editIndex = undefined;
			return true;
		} else {
			return false;
		}
	}

	function onClickRow2(index, data) {
		<c:if test="${epWholeQuo.quoteStatus ne 1 }">

		if (editIndex != index) {
			if (endEditing()) {
				$('#datagrid-item-list').datagrid('beginEdit', index);
				editIndex = index;
			} else {
				$('#datagrid-item-list').datagrid('selectRow', editIndex);
			}
		}

		var ed = $('#datagrid-item-list').datagrid('getEditor', {
			index : index,
			field : 'qty'
		});
		var total = $('#datagrid-item-list').datagrid('getEditor', {
			index : index,
			field : 'subtotal'
		});
		var taxPricex = $('#datagrid-item-list').datagrid('getEditor', {
			index : index,
			field : 'totalQuotePrice'
		});

		$(ed.target).numberbox({
			onChange : function() {
				caculate();
				caculateTotal();
			}
		});
		$(taxPricex.target).numberbox({
			onChange : function() {
				caculate();
				caculateTotal();
			}
		});

		function caculate() {
			var itemQty = ed.target.val();
			var taxPrice = taxPricex.target.val();
			;
			$(ed.target).numberbox('setValue', itemQty);
			$(taxPricex.target).numberbox('setValue', taxPrice);
			$(total.target).numberbox('setValue', itemQty * taxPrice);
		}

		function caculateTotal() {
			var isIncludeTaxVal = $('#isIncludeTax').combobox('getValue');

			var rows = $("#datagrid-item-list").datagrid("getRows");
			var tt = 0;
			for (var i = 0; i < rows.length; i++) {
				var total = $('#datagrid-item-list').datagrid('getEditor', {
					index : i,
					field : 'subtotal'
				});
				if (total == null) {
					tt = Number(tt) + Number(rows[i].subtotal);
				} else {
					tt = Number(tt) + Number(total.target.val());
				}
				//$("#quotePrice").textbox('setValue',tt);    
			}
			//$("#quotePrice").textbox('setValue',tt); 
			if (isIncludeTaxVal == null || isIncludeTaxVal == "") {
				return false;
			} else if (isIncludeTaxVal == 0) {
				$("#totalQuotePrice").textbox('setValue', tt);
			} else if (isIncludeTaxVal == 1) {
				$("#quotePrice").textbox('setValue', tt);
			}

		}
		</c:if>
	}

	//添加删除行
	function appendRow() {
		if (endEditingx()) {
			$('#datagrid-costItem-list').datagrid('appendRow', {});
			//editIndexx = $('#datagrid-costItem-list').datagrid('getRows').length-1;
			//$('#datagrid-costItem-list').datagrid('selectRow', editIndexx)
			//	.datagrid('beginEdit', editIndexx);
		}
	}

	function deleteRow() {
		var row = $('#datagrid-costItem-list').datagrid('getSelected');
		if (row) {
			var rowIndex = $('#datagrid-costItem-list').datagrid('getRowIndex',
					row);
			$('#datagrid-costItem-list').datagrid('deleteRow', rowIndex);
		}
	}

	var editIndexx = undefined;
	function endEditingx() {
		if (editIndexx == undefined) {
			return true
		}
		if ($('#datagrid-costItem-list').datagrid('validateRow', editIndexx)) {
			$('#datagrid-costItem-list').datagrid('endEdit', editIndexx);
			editIndexx = undefined;
			return true;
		} else {
			return false;
		}
	}

	function onClickRowx(index, data) {
		if (editIndexx != index) {

			if (endEditingx()) {
				$('#datagrid-costItem-list').datagrid('beginEdit', index);
				editIndexx = index;
			} else {
				$('#datagrid-costItem-list').datagrid('selectRow', editIndexx);
			}
		}

		var ed = $('#datagrid-costItem-list').datagrid('getEditor', {
			index : index,
			field : 'qty'
		});
		var total = $('#datagrid-costItem-list').datagrid('getEditor', {
			index : index,
			field : 'totalPrice'
		});
		var taxPricex = $('#datagrid-costItem-list').datagrid('getEditor', {
			index : index,
			field : 'price'
		});

		$(ed.target).numberbox({
			onChange : function() {
				caculatex();
			}
		});
		$(taxPricex.target).numberbox({
			onChange : function() {
				caculatex();
			}
		});

		function caculatex() {
			var itemQty = ed.target.val();
			var taxPrice = taxPricex.target.val();
			;
			$(ed.target).numberbox('setValue', itemQty);
			$(taxPricex.target).numberbox('setValue', taxPrice);
			$(total.target).numberbox('setValue', itemQty * taxPrice);
		}

	}
</script>
<style>
.infoT {
	border-top: 1px solid #888;
	margin-top: 10px;
}

.infoT th {
	text-align: center;
	height: 25px;
	font-size: 12px;
	background-color: #D0D0D0;
	border: 1px solid #888;
}

.infoT td {
	text-align: center;
	height: 20px;
	font-size: 12px;
	border: 1px solid #888;
}
</style>
</head>
<body style="margin: 0; padding: 0;">
	<div class="easyui-panel"
		style="overflow: auto; width: 100%; height: 100%">

		<form id="form-subQuo-addoredit" method="post"
			enctype="multipart/form-data">
			<input id="epWholeQuoId" name="id" value="${epWholeQuo.id}"
				hidden="true" />
			<div>
				<c:if test="${epWholeQuo.quoteStatus ne 1  }">
					<a href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-save',plain:true"
						onclick="subQuoManage.saveSubQuo(0)">保存</a>
					<a href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-save',plain:true"
						onclick="subQuoManage.saveSubQuo(1)">提交</a>
				</c:if>
				<c:if
					test="${epWholeQuo.negotiatedStatus eq 1 && epWholeQuo.negotiatedCheckStatus eq 0}">
					<a href="javascript:;" class="easyui-linkbutton"
						data-options="iconCls:'icon-save',plain:true"
						onclick="confirmEpWholeSubQuo('accept')">接受议价</a>
					<!-- <a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-save',plain:true" onclick="confirmEpWholeQuo('refuse')">拒绝议价</a> -->
				</c:if>

			</div>
			<table style="width: 90%; margin: auto; margin-top: 20px">
				<tr>
					<td style="width: 30%">询价单号：${epWholeQuo.epPrice.enquirePriceCode}</td>
					<td style="width: 30%">供应商：${epWholeQuo.epVendor.vendorName}</td>
					<td style="width: 30%">报价日期： <c:choose>
							<c:when test="${epWholeQuo.quoteStatus eq 1}">
					<fmt:formatDate value="${epWholeQuo.createTime}" type="both" pattern="yyyy-MM-dd"/>
				</c:when>
							<c:otherwise>
								<input id="epWholeQuoTime" name="createTime"
									value="${epWholeQuo.createTime}" class="easyui-datebox"
									data-options="required:true,editable:false"
									style="width: 130px" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>
			<table class="infoT"
				style="width: 90%; margin: auto; margin-top: 10px; border: 1px">
				<tr>
					<th style="width: 6%;">物料编码</th>
					<th style="width: 6%;">物料名称</th>
					<th style="width: 10%;">规格型号</th>
					<th style="width: 5%;">单位</th>
					<th style="width: 6%;">数量</th>
					<th style="width: 7%;">运输费用</th>
					<th style="width: 10%;">无税单价</th>
					<th style="width: 10%;">含税单价</th>
					<c:if test="${epWholeQuo.negotiatedStatus eq 1}">
						<th style="width: 10%;">协商单价</th>
					</c:if>
					<th style="width: 13%;">产地/品牌</th>
					<th style="width: 10%;">保修期</th>
					<th style="width: 10%;">供货周期</th>
					<th style="width: 17%;">备注</th>
				</tr>
				<tr>
					<td>${epWholeQuo.epMaterial.materialCode}</td>
					<!-- 物料编码 -->
					<td>${epWholeQuo.epMaterial.materialName}</td>
					<!-- 物料名称 -->
					<td>${epWholeQuo.epMaterial.materialSpec}</td>
					<!-- 规格型号 -->
					<td>${epWholeQuo.epMaterial.materialUnit}</td>
					<!-- 单位 -->
					<td>${epWholeQuo.epMaterial.planPurchaseQty}</td>
					<!-- 数量 -->
					<td>${epWholeQuo.epMaterial.freight}</td>
					<!-- 运输费用 -->
					<td><input id="totalQuotePrice" name="totalQuotePrice"
						value=" ${epWholeQuo.totalQuotePrice}" class="easyui-numberbox"
						readonly="readonly" data-options="required:true,precision:2"
						style="width: 100px;" /> <!-- -无税单价 --></td>
					<td><input id="quotePrice" name="quotePrice"
						value=" ${epWholeQuo.quotePrice}" class="easyui-numberbox"
						readonly="readonly" data-options="required:true,precision:2"
						style="width: 100px;" /></td>
					<!-- 含税单价 -->

					<c:if test="${epWholeQuo.negotiatedStatus eq 1}">
						<td>${epWholeQuo.negotiatedPrice}</td>
						<!-- 协议单价 -->
					</c:if>

					<td>${epWholeQuo.brand}</td>
					<!-- 产地/品牌 -->
					<td>${epWholeQuo.epMaterial.warranty}</td>
					<!-- 保修期 -->
					<td><c:choose>
							<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.supplyCycle}
				</c:when>
							<c:otherwise>
								<input id="supplyCycle" name="supplyCycle"
									value=" ${epWholeQuo.supplyCycle}" class="easyui-textbox"
									style="width: 100px;" />
							</c:otherwise>
						</c:choose></td>
					<!-- 供货周期 -->
					<td>${epWholeQuo.epMaterial.remarks}</td>
					<!-- 备注  -->
				</tr>
			</table>

			<table style="width: 90%; margin: auto; margin-top: 10px">
				<tr>
					<td>税率： <c:choose>
							<c:when test="${epWholeQuo.quoteStatus eq 1}">
								<c:if test="${epWholeQuo.taxRate eq 0.03 }">3%</c:if>
								<c:if test="${epWholeQuo.taxRate eq 0.04 }">4%</c:if>
								<c:if test="${epWholeQuo.taxRate eq 0.06 }">6%</c:if>
								<c:if test="${epWholeQuo.taxRate eq 0.07 }">7%</c:if>
								<c:if test="${epWholeQuo.taxRate eq 0.11 }">11%</c:if>
								<c:if test="${epWholeQuo.taxRate eq 0.13 }">13%</c:if>
								<c:if test="${epWholeQuo.taxRate eq 0.17 }">17%</c:if>

							</c:when>
							<c:otherwise>
								<select class="easyui-combobox" id="taxRate" name="taxRate"
									data-options="required:true,validType:'comboVry',editable:false"
									style="width: 100px">
									<option value="">-全部-</option>
									<option value="0.03"
										${epWholeQuo.taxRate eq "0.03" ? "selected" : "" }>3%</option>
									<option value="0.04"
										${epWholeQuo.taxRate eq "0.04" ? "selected" : "" }>4%</option>
									<option value="0.06"
										${epWholeQuo.taxRate eq "0.06" ? "selected" : "" }>6%</option>
									<option value="0.07"
										${epWholeQuo.taxRate eq "0.07" ? "selected" : "" }>7%</option>
									<option value="0.11"
										${epWholeQuo.taxRate eq "0.11" ? "selected" : "" }>11%</option>
									<option value="0.13"
										${epWholeQuo.taxRate eq "0.13" ? "selected" : "" }>13%</option>
									<option value="0.17"
										${epWholeQuo.taxRate eq "0.17" ? "selected" : "" }>17%</option>
								</select>
							</c:otherwise>
						</c:choose>
					</td>
					<c:if test="${epWholeQuo.quoteStatus ne 1}">
						<td>是否含税： <select class="easyui-combobox" id="isIncludeTax"
							name="isIncludeTax"
							data-options="required:true,validType:'comboVry',editable:false"
							style="width: 90px">
								<option value="">-全部-</option>
								<option value="0" ${epWholeQuo.isIncludeTax eq "0" ? "selected" : "" }>无税</option>
								<option value="1" ${epWholeQuo.isIncludeTax eq "1" ? "selected" : "" }>含税</option>
						</select>
						</td>
					</c:if>
					<td>税种： <c:choose>
							<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.taxCategory}
				</c:when>
							<c:otherwise>
								<select class="easyui-combobox" id="taxCategory"
									name="taxCategory"
									data-options="required:true,validType:'comboVry',editable:false"
									style="width: 130px">
									<option value="">-全部-</option>
									<option value="可抵扣"
										${epWholeQuo.taxCategory eq "可抵扣" ? "selected" : "" }>可抵扣</option>
									<option value="不可抵扣"
										${epWholeQuo.taxCategory eq "不可抵扣" ? "selected" : "" }>不可抵扣</option>
								</select>
							</c:otherwise>
						</c:choose>
					</td>

					<td></td>
				</tr>
				<tr>
					<td colspan="4">保质期： <c:choose>
							<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.warrantyPeriod}
				</c:when>
							<c:otherwise>
								<input id="warrantyPeriod" name="warrantyPeriod"
									value="${epWholeQuo.warrantyPeriod}" class="easyui-textbox"
									data-options="required:true" style="width: 130px;" />
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
				<tr>
					<td colspan="4">运输方式： <c:choose>
							<c:when test="${epWholeQuo.quoteStatus eq 1}">
					${epWholeQuo.transportationMode}
				</c:when>
							<c:otherwise>
								<select class="easyui-combobox" id="transportationMode"
									name="transportationMode"
									data-options="required:true,validType:'comboVry',editable:false"
									style="width: 130px">
									<option value="">-全部-</option>
									<option value="空运"
										${epWholeQuo.transportationMode eq "空运" ? "selected" : "" }>空运</option>
									<option value="汽运"
										${epWholeQuo.transportationMode eq "汽运" ? "selected" : "" }>汽运</option>
									<option value="铁运"
										${epWholeQuo.transportationMode eq "铁运" ? "selected" : "" }>铁运</option>
									<option value="船运"
										${epWholeQuo.transportationMode eq "船运" ? "selected" : "" }>船运</option>
									<option value="需方自提"
										${epWholeQuo.transportationMode eq "需方自提" ? "selected" : "" }>需方自提</option>
								</select>
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			
				<tr>
					<td colspan="4">报价附件： <c:choose>
							<c:when test="${epWholeQuo.quoteStatus ne 1}">
								<input class="easyui-filebox" name="quoteTemplate"
									data-options="buttonText:'浏览'" style="width: 200px" />
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose> <a href="javascript:;"
						onclick="File.download('${epWholeQuo.quoteTemplateUrl}','${epWholeQuo.quoteTemplateName}')">${epWholeQuo.quoteTemplateName}</td>
					<!-- <td>图片附件：</td> -->
				</tr>
				<%-- 	   <tr>
		   <td colspan="4">技术承诺：<input class="easyui-textbox" id="technologyPromises" name="technologyPromises" value="${epWholeQuo.technologyPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">质量承诺：<input class="easyui-textbox" id="qualityPromises" name="qualityPromises" value="${epWholeQuo.qualityPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">服务承诺：<input class="easyui-textbox" id="servicePromises" name="servicePromises" value="${epWholeQuo.servicePromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/></td>
	   </tr>
	   <tr>
		   <td colspan="4">交期承诺：<input class="easyui-textbox" id="deliveryPromises" name="deliveryPromises" value="${epWholeQuo.deliveryPromises}" type="text" max="300"
					data-options="multiline:true" style="width:90%;height: 80px"/></td>
	   </tr> --%>
				<tr>
					<td colspan="4">其他承诺：<input class="easyui-textbox"
						id="otherPromises" name="otherPromises"
						value="${epWholeQuo.otherPromises}" type="text" max="300"
						data-options="multiline:true" style="width: 90%; height: 80px" /></td>
				</tr>
			</table>


			<input type="hidden" name="tableDatas" id="tableDatas" />
		</form>

		<table id="datagrid-item-list" title="报价明细" class="easyui-datagrid"
			data-options="method:'post',singleSelect:false,toolbar: '#epSubItemtb',
		pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList,
		onClickRow: onClickRow2,onLoadSuccess:initPrice,
		url: '${ctx }/manager/ep/epSubQuo/getSubQuoByWholeId/${epWholeQuo.id}'">
			<thead>
				<tr>
					<th data-options="field:'id',checkbox:true"></th>
					<th data-options="field:'wholeQuoId',hidden:true"></th>

					<th data-options="field:'materialName',width:80,editor:'textbox'">费目类别</th>
					<th data-options="field:'remarks',width:80,editor:'textbox'">费目备注</th>
					
					<th
						data-options="field:'totalQuotePrice',width:80,editor:{type:'numberbox', options: {required:true,precision:2}}">单价</th>
					<th data-options="field:'qty',width:80,editor:'numberbox'">数量</th>
					<th
						data-options="field:'subtotal',width:80,editor:{type:'numberbox', options: {precision:2}}">小计</th>
					<c:if test="${epWholeQuo.negotiatedStatus eq 1}">
						<th data-options="field:'negotiatedSubPrice',width:80">协商价格</th>
						<th data-options="field:'negotiatedSubTotalPrice',width:80">协商小计</th>
					</c:if>
				</tr>
			</thead>
		</table>
	</div>

	<div id="win-cost-detail" title="费目明细" class="easyui-window"
		data-options="iconCls:'icon-add',modal:true,closed:true,fit:true">

		<div id="tbCost" style="padding: 5px; height: auto">
			<div style="margin-bottom: 5px">
				<c:if test="${epWholeQuo.quoteStatus ne 1  }">
					<a href="javascript:void(0)" class="easyui-linkbutton"
						data-options="iconCls:'icon-save',plain:true"
						onclick="subQuoManage.saveSubQuoCost()">保存</a>

					<a href="#" class="easyui-linkbutton" id="cost-item-add"
						iconCls="icon-add" plain="true" onclick="javascript:appendRow()"></a>
					<a href="#" class="easyui-linkbutton" id="cost-item-del"
						iconCls="icon-remove" plain="true"
						onclick="javascript:deleteRow()"></a>
				</c:if>
			</div>

		</div>


		<form id="form-subQuoCost-addoredit" method="post">
			<input type="hidden" name="id" id="epQuoSubId" value=""></input> <input
				type="hidden" name="tableCostDatas" id="tableCostDatas" />
		</form>
		<table id="datagrid-costItem-list" class="easyui-datagrid"
			style="width: 800px; height: auto; padding: 20px"
			data-options="iconCls: 'icon-edit', 
	  			singleSelect: true,toolbar: '#tbCost',method: 'post',pagination:true,rownumbers:true,pageSize:pageSize,pageList:pageList, onClickRow:onClickRowx">
			<thead>
				<tr>

					<th data-options="field:'id',checkbox:true"></th>
					<th data-options="field:'epQuoSubId',hidden:true"></th>

					<th data-options="field:'name',width:80,editor:'textbox'">费目名称</th>
				
					<th
						data-options="field:'price',width:80,editor:{type:'numberbox', options: {readonly:false,precision:2}}">含税单价</th>
					<th
						data-options="field:'qty',width:80,editor:{type:'numberbox', options: {readonly:false}}">数量</th>
					<th
						data-options="field:'totalPrice',width:80,editor:{type:'numberbox', options: {readonly:true,precision:2}}">小计</th>

				</tr>
			</thead>
		</table>
	</div>
	<script type="text/javascript">
		$(function() {
			var curr_time = new Date();
			var strDate = curr_time.getFullYear() + "-";
			strDate += curr_time.getMonth() + 1 + "-";
			strDate += curr_time.getDate() + "-";
			strDate += curr_time.getHours() + ":";
			strDate += curr_time.getMinutes() + ":";
			strDate += curr_time.getSeconds();
			$("#epWholeQuoTime").datebox("setValue", strDate);
		});
	</script>
</body>
