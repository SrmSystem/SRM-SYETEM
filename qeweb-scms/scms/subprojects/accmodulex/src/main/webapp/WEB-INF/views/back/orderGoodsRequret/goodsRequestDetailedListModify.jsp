<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>   
<c:set var="vendor" value="${vendor} "/> 

<script type="text/javascript" src="${ctx}/static/script/purchase/common.js"></script>
  
<script type="text/javascript">

$(function() { 
	var tmp =  ${vendor};
		$('#datagrid-goodsRequestItem-list').datagrid({
			rowStyler:function(index,row){
				if (row.isRed == 1){
					return 'background-color:red;color:black;';
				}
			}
		});
});

function operateFmt(v,r,i){
	  var s="";
 	 var isVendor = $("#isVendor").val();
	 isVendor= isVendor.replace(/(^\s+)|(\s+$)/g, "");
	  //删除（出现四中状态的（isRed）和无任何asn 的要货计划生成的供货计划（isDelete） 可以删除 ）
	 
	  if(r.isDelete == 1 &&  isVendor == "false"  && r.isRed == 1){
		  //修改要货计划数量并且释放订单资源
		  s='&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="deleteOrderItemPlan('+r.id+');">'+'<spring:message code="vendor.deleting"/></a>'; /* 删除 */
	  }else if(r.isDelete == 0 &&  isVendor == "false"  && r.isRed == 1){
		  s='&nbsp;&nbsp;&nbsp;<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="notDeleteOrderItemPlan('+r.id+');">'+'<spring:message code="vendor.deleting"/></a>'; /* 删除 */
	  }

	  return s;
}

//采购主订单格式化编码
function orderCodeFmt(v,r,i){
	 return '<a href="javascript:;" class="easyui-linkbutton" data-options="plain:true" onclick="showOrderItemList('+ r.order.id +','+${vendor}+');">' + r.order.orderCode + '</a>'; 

}


function getNumFmt(v,r,i) {
	
		if(v=="" || v==null || v == "0.000" || v==0.000){
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
	    // 补足小数位到指定的位数  
	    while(cents.length<3)  
	      cents = "0" + cents;  
	      // 对整数部分进行千分位格式化.  
	      for (var i = 0; i < Math.floor((num.length-(1+i))/3); i++)  
	        num = num.substring(0,num.length-(4*i+3))+','+ num.substring(num.length-(4*i+3));  

	      return (((sign)?'':'-') + num + '.' + cents);  
   
}

var editIndex = undefined;
function onClickRow2(index, data) {
	 var rows = $('#datagrid-goodsRequestMain-list').datagrid('getRows');
	 var i;
	 for(i = 0;i < rows.length;i++) {
		 editRow(i,rows[i]);
	 }
	
}


function editRow(index, data) {
	
	$('#datagrid-goodsRequestMain-list').datagrid('beginEdit', index);
	editIndex = index;
	
	var dhsl = $('#datagrid-goodsRequestMain-list').datagrid('getEditor', {
		index : index,
		field : 'dhsl'
	});
}


</script>
<!-- 要货计划的详细 -->
<div style="height: 300px">
   <input id="isVendor" name="isVendor" type="hidden"  value="${vendor}"/>
 
   <table id="datagrid-goodsRequestMain-list" class="easyui-datagrid"  fit="true" title="要货计划" 
		data-options="url:'${ctx}/manager/order/goodsRequest/getRequestList/${vendor}?flag=${flag}&factoryId=${factoryId}&purchasingGroupId=${purchasingGroupId}&materialId=${materialId}&meins=${meins}&vendorId=${vendorId}&type=${type}&beginRq=${beginRq}&endRq=${endRq}',method:'post',singleSelect:false,   
		<c:if test="${vendor == 'false '}">  
			 toolbar:'#goodsRequestMainListToolbar',
			 onClickRow: onClickRow2,
		</c:if>
		pagination:true,rownumbers:true,pageSize:10,pageList:[10,20,50]"
		>
		<thead><tr>
		<th data-options="field:'factory.name' , formatter:function(v,r,i){return r.factory.name;} " ><spring:message code="vendor.orderplan.factoryName"/><!-- 工厂名称 --> </th>
		<th data-options="field:'materialCode',formatter:function(v,r,i){return r.material.code;}"><spring:message code="vendor.orderplan.materialNumber"/><!-- 物料号 --></th>  
		<th data-options="field:'materialName',formatter:function(v,r,i){return r.material.name;}"><spring:message code="vendor.orderplan.materialDescription"/><!-- 物料描述 --></th>  
		<th data-options="field:'vendor.name' , formatter:function(v,r,i){return r.vendor.name;}    "><spring:message code="vendor.supplierName"/><!-- 供应商名称 --></th>
		<th data-options="field:'vendor.code' ,formatter:function(v,r,i){return r.vendor.code;}  "><spring:message code="vendor.supplierCode"/><!-- 供应商编码 --></th>
		<th data-options="field:'dhsl' ,editor:{type:'numberbox',options:{required:true,min:0,precision:'3'}},formatter:getNumFmt"><spring:message code="vendor.orderGoodsRequret.cargoQuantity"/><!-- 要货数量 --></th>
		<th data-options="field:'publishStatus',formatter:function(v,r,i){return StatusRender.render(v,'publishStatus',false);}"><spring:message code="vendor.postStatus"/><!-- 发布状态 --></th>  
		<th data-options="field:'vendorConfirmStatus',formatter:function(v,r,i){return StatusRender.render(v,'confirm',false);}"><spring:message code="vendor.orderplan.confirmStatus"/><!-- 确认状态 --></th>  
		<th data-options="field:'rq'  , formatter:function(v,r,i){return r.rq;}  "><spring:message code="vendor.orderGoodsRequret.expectedDateArrival"/><!-- 预计到货日期 --></th>
		<th data-options="field:'ysts'  , formatter:function(v,r,i){return r.ysts;}   "  ><spring:message code="vendor.orderGoodsRequret.logisticsDays"/><!-- 物流天数 --></th>
		<th data-options="field:'shpl'  , formatter:function(v,r,i){return r.shpl;}   "  ><spring:message code="vendor.buyerBoard.deliveryFrequency"/><!-- 送货频率 --></th>
		<th data-options="field:'surQry' ,formatter:getNumFmt "><spring:message code="vendor.orderGoodsRequret.remainingMatchingBaseAmount"/><!-- 剩余匹配基本数量  --></th>
		</tr></thead>
	</table>
	<c:if test="${vendor == 'false '}"> 
	<div id="goodsRequestMainListToolbar" style="padding:5px;">
		<div>
		   <form id="form-goodsRequestMain-modify" method="post" enctype="multipart/form-data">
		<!-- 采购员菜单 -->
				<a href="javascript:;" class="easyui-linkbutton" data-options="iconCls:'icon-add',plain:true" onclick="editgoodsQry();"><spring:message code="vendor.modification"/></a><!-- 修改 -->
		   </form>
		</div>
	</div>	
	</c:if>
</div>

<script type="text/javascript">

/**
 * 修改要货计划总量
 */
function editgoodsQry(){
	
	 $.messager.progress({
			title:'<spring:message code="vendor.prompting"/>'/* 提示 */,
			msg : '<spring:message code="vendor.submission"/>...'/* 提交中 */
	});
	$('#datagrid-goodsRequestMain-list').datagrid('acceptChanges'); 
	var rows = $("#datagrid-goodsRequestMain-list").datagrid('getRows');
	
	if(rows.length==0){
		$.messager.alert('<spring:message code="vendor.prompting"/>'/* 提示 */,'没有任何记录！','info');
		return false;
	}
	var length=rows.length;
	var str="{total:"+length+",rows:"+$.toJSON(rows)+"}";
	var datas=str;
	$('#form-goodsRequestMain-modify').form('submit',{
		ajax:true,
		iframe: true,   
		url: '${ctx}/manager/order/goodsRequest/editMainGoodsRequest', 
		onSubmit:function(param){
			param.datas = datas;
		}, 
		success: function (data) {  
				$.messager.progress('close');
				try{
					var result = eval('('+data+')');
					if(result.success){ 
						$.messager.show({
							title:'<spring:message code="vendor.news"/>',/* 消息 */
							msg:  result.message, 
							timeout:2000,
							showType:'show',
							style:{
								right:'',
								top:document.body.scrollTop+document.documentElement.scrollTop,
								bottom:''
							}
						});
						$("#datagrid-goodsRequestMain-list").datagrid('reload');
					}else{
						editIndex = undefined;
						$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',result.message,'error');
					}
				}catch (e) {
					editIndex = undefined;
					$.messager.alert('<spring:message code="purchase.order.Prompt"/><!-- 提示 -->',e,'error'); 
				} 
	       	}
	 }); 
	
}
</script>

